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
import hu.juranyi.zsolt.jauthortagger.model.BackupingMode;
import hu.juranyi.zsolt.jauthortagger.model.Filenames;
import hu.juranyi.zsolt.jauthortagger.model.JavaFile;
import hu.juranyi.zsolt.jauthortagger.output.AuthorTagWriter;
import hu.juranyi.zsolt.jauthortagger.output.DiffReportWriter;
import hu.juranyi.zsolt.jauthortagger.util.DiffCalculator;
import hu.juranyi.zsolt.jauthortagger.util.IOUtils;
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
	private static BackupingMode DEFAULT_BACKUPING_MODE = BackupingMode.BACKUP;

	public static void main(String[] args) {
		File projectDir = null;
		BackupingMode backupingMode = DEFAULT_BACKUPING_MODE;

		// receive project dir as 1st arg
		if (args.length >= 1) {
			projectDir = new File(args[0]);
			if (!projectDir.exists() || !projectDir.isDirectory()) {
				projectDir = null;
			}
		}

		// receive backuping mode as 2nd arg
		if (args.length >= 2) {
			if ("nobackup".equalsIgnoreCase(args[1])) {
				backupingMode = BackupingMode.NO_BACKUP;
			} else if ("restore".equalsIgnoreCase(args[1])) {
				backupingMode = BackupingMode.RESTORE;
			} else if ("test".equalsIgnoreCase(args[1])) {
				backupingMode = BackupingMode.TEST;
			}
		}

		// project dir is necessary
		if (null == projectDir) {
			System.out.println("JAuthorTagger  by  Zsolt Juranyi");
			System.out.println("github.com/juzraai/author-tagger");
			System.out.println("\nUsage:\n\t<project-dir> [nobackup|test|restore]");
			System.out.println("\nJAuthorTagger will create backup files unless you provide the 2nd argument.");
			System.out.println("\nWhen 'nobackup' is present, previous backup files will be deleted.");
			System.out.println("When 'test' is present, no modification will be made to your files, new ones");
			System.out.println("will be created instead.");
			System.out.println("When 'restore' is present, backups from the previous run will be restored.");
			System.out.println("\nSee full documentation on GitHub!");
		} else {

			// init log here because log file will be project specific
			Log.setLogFile(new File(projectDir, Filenames.PROJECT_LOG_FILE).getAbsolutePath());
			LOG = Log.forClass(JAuthorTagger.class);

			// do the magic
			new JAuthorTagger(projectDir, backupingMode).start();
		}
	}

	private final File projectDir;
	private final BackupingMode backupingMode;

	public JAuthorTagger(File projectDir) {
		this(projectDir, DEFAULT_BACKUPING_MODE);
	}

	public JAuthorTagger(File projectDir, BackupingMode backupingMode) {
		this.projectDir = projectDir;
		this.backupingMode = backupingMode;
		if (null == LOG) {
			LOG = Log.forClass(JAuthorTagger.class);
		}
	}

	public BackupingMode getBackupingMode() {
		return backupingMode;
	}

	public File getProjectDir() {
		return projectDir;
	}

	public void start() {
		LOG.info("JAuthorTagger running in {} mode", backupingMode);

		LOG.info("Enumerating .java files in project directory: {}", projectDir.getAbsolutePath());
		List<JavaFile> javaFiles = new JavaFileEnumerator().enumerateJavaFiles(new File(projectDir, "src"));

		if (BackupingMode.RESTORE != backupingMode) {
			LOG.info("Analyzing {} .java files", javaFiles.size());
			new JavaFileAnalyzer().analyzeJavaFiles(javaFiles);

			LOG.info("Reading project configuration and tagging (in memory)");
			new AuthorTaggerConfig(new File(projectDir, Filenames.PROJECT_CONFIG_FILE)).loadAndApply(javaFiles);

			LOG.info("Writing to disk");
		} else {
			LOG.info("Restoring backup files");
		}

		AuthorTagWriter w = new AuthorTagWriter(backupingMode);
		for (JavaFile javaFile : javaFiles) {

			// pre-read original content for diff calculation
			List<String> originalContent = IOUtils.fileToStringList(javaFile.getFile());

			// do magic (author tagging or restoring)
			w.writeAuthorTags(javaFile);

			// read output and calculate diff
			File originalFile = (BackupingMode.BACKUP == backupingMode)
					? new File(javaFile.getFile().getAbsolutePath() + Filenames.BACKUP_FILE_SUFFIX)
					: javaFile.getFile();
			File modifiedFile = (BackupingMode.TEST == backupingMode)
					? new File(javaFile.getFile().getAbsolutePath() + Filenames.TEST_FILE_SUFFIX) : javaFile.getFile();
			List<String> modifiedContent = IOUtils.fileToStringList(modifiedFile);
			DiffCalculator dc = new DiffCalculator(originalFile, modifiedFile, originalContent, modifiedContent);
			javaFile.setDiffResult(dc.calculateDiff());
		}

		if (BackupingMode.RESTORE != backupingMode) {
			LOG.info("Generating diff report");
			new DiffReportWriter(projectDir, backupingMode, javaFiles).writeDiffReport();
		}

		LOG.info("Done!");
	}
}
