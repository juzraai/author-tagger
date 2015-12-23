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

package hu.juranyi.zsolt.jauthortagger;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;

import hu.juranyi.zsolt.jauthortagger.input.AuthorTaggerConfig;
import hu.juranyi.zsolt.jauthortagger.input.JavaFileAnalyzer;
import hu.juranyi.zsolt.jauthortagger.input.JavaFileEnumerator;
import hu.juranyi.zsolt.jauthortagger.model.JavaFile;
import hu.juranyi.zsolt.jauthortagger.output.AuthorTagWriter;
import hu.juranyi.zsolt.jauthortagger.util.Log;

/**
 *
 * @author Zsolt
 *
 */
public class JAuthorTagger {
	// TODO DOC all classes
	// TODO JUnit tests

	static {
		// Log.setLogLevel(AuthorTaggerConfig.class, Log.Level.WARN);
		Log.setLogLevel(JavaFileEnumerator.class, Log.Level.WARN);
		// Log.setLogLevel(JavaFileAnalyzer.class, Log.Level.WARN);
		// Log.setLogLevel(AuthorTagWriter.class, Log.Level.WARN);
	}

	private static Logger LOG;

	public static void main(String[] args) {

		// TEST DRIVE:
		args = "C:\\tmp\\test-project nobackup".split(" ");

		File projectDir = null;
		boolean test = false;
		boolean backup = true;
		if (args.length >= 1) {
			projectDir = new File(args[0]);
			Log.setLogFile(new File(projectDir, ".authors-log").getAbsolutePath());
			if (!projectDir.exists() || !projectDir.isDirectory()) {
				projectDir = null;
			}
		}
		if (args.length >= 2) {
			test = "test".equalsIgnoreCase(args[1]);
			if ("nobackup".equalsIgnoreCase(args[1])) {
				backup = false;
			}

		}
		if (null == projectDir) {
			System.out.println("JAuthorTagger  by  Zsolt Juranyi");
			System.out.println("github.com/juzraai/author-tagger");
			System.out.println("\nUsage:\n\t<project-dir> [nobackup|test]");
			System.out.println("\nJAuthorTagger will create backup files unless you provide the 2nd argument.");
			System.out.println("\nWhen 'nobackup' is present, previous backup files will be deleted.");
			System.out.println("When 'test' is present, no modification will be made to your files, new ones");
			System.out.println("will be created instead.");
			System.out.println("\nSee full documentation on GitHub!");
		} else {
			LOG = Log.forClass(JAuthorTagger.class);
			new JAuthorTagger(projectDir, backup, test).start();
		}
	}

	private final File projectDir;
	private final boolean backup;
	private final boolean test;

	public JAuthorTagger(File projectDir, boolean backup, boolean test) {
		this.projectDir = projectDir;
		this.backup = backup;
		this.test = test;
	}

	private void start() {
		LOG.info("Enumerating .java files in project directory: {}", projectDir.getAbsolutePath());
		List<JavaFile> javaFiles = new JavaFileEnumerator().enumerateJavaFiles(new File(projectDir, "src"));

		LOG.info("Analyzing {} .java files", javaFiles.size());
		new JavaFileAnalyzer().analyzeJavaFiles(javaFiles);

		LOG.info("Reading project configuration and tagging (in memory)");
		new AuthorTaggerConfig(new File(projectDir, ".authors")).loadAndApply(javaFiles);

		if (test) {
			LOG.info("Writing to disk (test mode)");
		} else {
			LOG.info("Writing to disk");
		}
		LOG.info("Writing to disk");
		AuthorTagWriter w = new AuthorTagWriter(backup, test);
		for (JavaFile javaFile : javaFiles) {
			w.writeAuthorTags(javaFile);
		}

		LOG.info("Done!");
	}
}
