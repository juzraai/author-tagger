/*
 * Copyright 2015 Zsolt Jurányi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hu.juranyi.zsolt.jauthortagger.test;

import static hu.juranyi.zsolt.jauthortagger.model.BackupMode.BACKUP;
import static hu.juranyi.zsolt.jauthortagger.model.BackupMode.NO_BACKUP;
import static hu.juranyi.zsolt.jauthortagger.model.BackupMode.TEST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import difflib.Delta;
import hu.juranyi.zsolt.jauthortagger.input.JavaFileAnalyzer;
import hu.juranyi.zsolt.jauthortagger.model.BackupMode;
import hu.juranyi.zsolt.jauthortagger.model.DiffResult;
import hu.juranyi.zsolt.jauthortagger.model.Filenames;
import hu.juranyi.zsolt.jauthortagger.model.JavaFile;
import hu.juranyi.zsolt.jauthortagger.output.AuthorTagWriter;
import hu.juranyi.zsolt.jauthortagger.util.DiffCalculator;
import hu.juranyi.zsolt.jauthortagger.util.IOUtils;
import hu.juranyi.zsolt.jauthortagger.util.Log;
import hu.juranyi.zsolt.jauthortagger.util.TestUtils;

/**
 * Tests the writer module: the backup modes and the <code>@author</code> tag
 * injecting.
 * 
 * @author Zsolt Jurányi
 *
 */
public class AuthorTagWriterTest {

	private static final Logger LOG = Log.forClass(AuthorTagWriterTest.class);

	@BeforeClass
	@AfterClass
	public static void cleanup() {
		TestUtils.deleteTestDir();
	}

	@Test
	public void backupMode() {
		testBackupMode(BACKUP);
	}

	@Test
	public void nobackupMode() {
		testBackupMode(NO_BACKUP);
	}

	@Test
	public void tagInjecting() {
		String author = "Test Man";

		List<String> resNames = new ArrayList<String>();
		for (String suffix : ",A,AD,ADP,AP,D,DP,P".split(",")) {
			resNames.add("Class" + suffix);
			if (!suffix.contains("P")) {
				resNames.add("PkgInf" + suffix);
			}
		}
		Collections.sort(resNames);

		JavaFileAnalyzer a = new JavaFileAnalyzer();
		AuthorTagWriter w = new AuthorTagWriter(TEST);

		for (String resName : resNames) {
			try {

				// spit out input file

				String inputName = "injecting-tests/" + resName + ".java";
				TestUtils.exportResourceFile(resName + ".java_", inputName);
				File inputFile = new File(TestUtils.TEST_DIR, inputName);
				File outputFile = Filenames.testFileOf(inputFile);

				// generate JavaFile for modifications

				JavaFile javaFile = new JavaFile(inputFile);
				a.analyzeJavaFile(javaFile);
				javaFile.getAuthors().add(0, author);

				// call writer

				w.writeAuthorTags(javaFile);

				// verify output

				List<String> inputLines = IOUtils.fileToStringList(inputFile);
				List<String> outputLines = IOUtils.fileToStringList(outputFile);
				DiffResult outputDiff = new DiffCalculator(inputFile, outputFile, inputLines, outputLines)
						.calculateDiff();

				boolean oneInsert = true, oneDelete = true;
				for (Delta<String> d : outputDiff.getPatch().getDeltas()) {

					// no change delta
					assertFalse(Delta.TYPE.CHANGE == d.getType());

					// only one insert
					if (Delta.TYPE.INSERT == d.getType()) {
						assertTrue(oneInsert);
						oneInsert = false;
					}

					// delete is ok if it's me (because it's redundant in files)
					if (Delta.TYPE.DELETE == d.getType()) {
						assertEquals(1, d.getOriginal().getLines().size());
						assertEquals(" * @author Zsolt Jurányi", d.getOriginal().getLines().get(0));
						assertTrue(oneDelete);
						oneDelete = false;
					}
				}
				assertTrue(2 >= outputDiff.getPatch().getDeltas().size());

				int expectedPosition = javaFile.getTypeDeclarationStartLine();
				int actualPosition = outputDiff.getPatch().getDeltas().get(0).getRevised().getPosition();
				assertTrue(actualPosition <= expectedPosition);
			} catch (AssertionError e) {
				LOG.error("FAILED IN ITERATION: {}", resName);
				throw e;
			}
		}
	}

	private void testBackupMode(BackupMode mode) {
		String resName = "Class.java_";
		String inputName = "backup-mode-tests/" + mode + ".java";
		File inputFile = new File(TestUtils.TEST_DIR, inputName);
		File outputFile = null;
		String author = "Test Man";

		// spit out input file

		TestUtils.exportResourceFile(resName, inputName);
		List<String> originalLines = IOUtils.fileToStringList(inputFile);

		// generate JavaFile for modifications

		JavaFile javaFile = new JavaFile(inputFile);
		new JavaFileAnalyzer().analyzeJavaFile(javaFile);
		assertTrue(javaFile.getAuthors().isEmpty());
		javaFile.getAuthors().add(author);

		// call writer

		AuthorTagWriter w = new AuthorTagWriter(mode);
		w.writeAuthorTags(javaFile);

		// verify output

		if (BACKUP == mode) {
			File temp = inputFile;
			inputFile = Filenames.backupFileOf(inputFile);
			outputFile = temp;
		} else if (NO_BACKUP == mode) {
			outputFile = inputFile;
		} else if (TEST == mode) {
			outputFile = Filenames.testFileOf(inputFile);
		}

		if (!inputFile.equals(outputFile)) {
			List<String> inputLines = IOUtils.fileToStringList(inputFile);
			DiffResult inputDiff = new DiffCalculator(inputFile, inputFile, originalLines, inputLines).calculateDiff();
			assertTrue(inputDiff.getPatch().getDeltas().isEmpty());
		}

		assertTrue(outputFile.exists());

		javaFile = new JavaFile(outputFile);
		new JavaFileAnalyzer().analyzeJavaFile(javaFile);
		assertEquals(Arrays.asList(author), javaFile.getAuthors());
	}

	@Test
	public void testMode() {
		testBackupMode(TEST);
	}

}
