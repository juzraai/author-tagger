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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import hu.juranyi.zsolt.jauthortagger.input.JavaFileEnumerator;
import hu.juranyi.zsolt.jauthortagger.model.JavaFile;
import hu.juranyi.zsolt.jauthortagger.model.JavaFiles;
import hu.juranyi.zsolt.jauthortagger.util.TestUtils;

/**
 * Verifies whether the enumerator finds only the right files.
 * 
 * @author Zsolt Jurányi
 *
 */
public class JavaFileEnumeratorTest {

	@Test
	public void enumeratesTheRightFiles() {
		List<File> expectedFiles = new ArrayList<File>();
		TestUtils.createEmptyFile("ClassInProjectRoot.java");
		expectedFiles.add(TestUtils.createEmptyFile("src/main/java/ClassInRootPkg.java"));
		expectedFiles.add(TestUtils.createEmptyFile("src/main/java/some/package/ClassInAPkg.java"));
		TestUtils.createEmptyFile("src/main/resources/ResourceFile.txt");

		List<File> enumeratedFiles = new ArrayList<File>();
		JavaFileEnumerator e = new JavaFileEnumerator();
		JavaFiles javaFiles = e.enumerateJavaFiles(new File(TestUtils.TEST_DIR, "src"));
		for (JavaFile javaFile : javaFiles) {
			enumeratedFiles.add(javaFile.getFile());
		}

		assertEquals(expectedFiles, enumeratedFiles);
		TestUtils.deleteTestDir();
	}

}
