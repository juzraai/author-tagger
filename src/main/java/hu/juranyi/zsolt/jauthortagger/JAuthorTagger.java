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

import static hu.juranyi.zsolt.jauthortagger.model.BackupMode.BACKUP;
import static hu.juranyi.zsolt.jauthortagger.model.BackupMode.NO_BACKUP;
import static hu.juranyi.zsolt.jauthortagger.model.BackupMode.RESTORE;
import static hu.juranyi.zsolt.jauthortagger.model.BackupMode.TEST;
import static hu.juranyi.zsolt.jauthortagger.model.Filenames.backupFileOf;
import static hu.juranyi.zsolt.jauthortagger.model.Filenames.configFileOf;
import static hu.juranyi.zsolt.jauthortagger.model.Filenames.logFileOf;
import static hu.juranyi.zsolt.jauthortagger.model.Filenames.testFileOf;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;

import hu.juranyi.zsolt.jauthortagger.input.AuthorTaggerConfig;
import hu.juranyi.zsolt.jauthortagger.input.JavaFileAnalyzer;
import hu.juranyi.zsolt.jauthortagger.input.JavaFileEnumerator;
import hu.juranyi.zsolt.jauthortagger.model.BackupMode;
import hu.juranyi.zsolt.jauthortagger.model.JavaFile;
import hu.juranyi.zsolt.jauthortagger.output.AuthorTagWriter;
import hu.juranyi.zsolt.jauthortagger.output.DiffReportWriter;
import hu.juranyi.zsolt.jauthortagger.util.DiffCalculator;
import hu.juranyi.zsolt.jauthortagger.util.IOUtils;
import hu.juranyi.zsolt.jauthortagger.util.Log;

/**
 * The main class of <b>JAuthorTagger</b>. This contains the simple CLI and the
 * main script.
 *
 * @author Zsolt Jurányi
 *
 */
public class JAuthorTagger {

	static {
		// Log.setLogLevel(AuthorTaggerConfig.class, Log.Level.WARN);
		Log.setLogLevel(JavaFileEnumerator.class, Log.Level.WARN);
		// Log.setLogLevel(JavaFileAnalyzer.class, Log.Level.WARN);
		// Log.setLogLevel(AuthorTagWriter.class, Log.Level.WARN);
	}

	private static Logger LOG;
	private static BackupMode DEFAULT_BACKUPING_MODE = BACKUP;

	/**
	 * Implements CLI and when appropriate parameters are received, creates an
	 * instance of <code>JAuthorTagger</code> and calls its <code>start()</code>
	 * method. The first argument must be the path of the project to work on,
	 * the second optional argument can override the default backup mode.
	 *
	 * @param args
	 *            Command line arguments.
	 * @see BackupMode
	 * @see #start()
	 */
	public static void main(String[] args) {
		File projectDir = null;
		BackupMode backupMode = DEFAULT_BACKUPING_MODE;

		// receive project dir as 1st arg
		if (args.length >= 1) {
			projectDir = new File(args[0]);
			if (!projectDir.exists() || !projectDir.isDirectory()) {
				projectDir = null;
			}
		}

		// receive backup mode as 2nd arg
		if (args.length >= 2) {
			if ("nobackup".equalsIgnoreCase(args[1])) {
				backupMode = NO_BACKUP;
			} else if ("restore".equalsIgnoreCase(args[1])) {
				backupMode = RESTORE;
			} else if ("test".equalsIgnoreCase(args[1])) {
				backupMode = TEST;
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
			Log.setLogFile(logFileOf(projectDir).getAbsolutePath());
			LOG = Log.forClass(JAuthorTagger.class);

			// do the magic
			new JAuthorTagger(projectDir, backupMode).start();
		}
	}

	private final File projectDir;
	private final BackupMode backupMode;

	/**
	 * Creates an instance.
	 *
	 * @param projectDir
	 *            The directory of the project to work on.
	 */
	public JAuthorTagger(File projectDir) {
		this(projectDir, DEFAULT_BACKUPING_MODE);
	}

	/**
	 * Creates an instance.
	 *
	 * @param projectDir
	 *            The directory of the project to work on.
	 * @param backupMode
	 *            The backup mode.
	 * @see BackupMode
	 */
	public JAuthorTagger(File projectDir, BackupMode backupMode) {
		this.projectDir = projectDir;
		this.backupMode = backupMode;
		if (null == LOG) {
			LOG = Log.forClass(JAuthorTagger.class);
		}
	}

	/**
	 * Returns the backup mode.
	 *
	 * @return The backup mode.
	 */
	public BackupMode getBackupMode() {
		return backupMode;
	}

	/**
	 * Returns the project directory.
	 *
	 * @return The project directory.
	 */
	public File getProjectDir() {
		return projectDir;
	}

	/**
	 * The main program. First, it enumerates <code>.java</code> files in the
	 * given project directory. Secondly - if backup mode is not
	 * <code>RESTORE</code>, analyzes the files by parsing existing author tags,
	 * then loads and applies the configuration on them. After that, it calls
	 * the tagger magic to inject new authors (or restore backups), and
	 * calculates diff on-the-fly. Finally it exports the diff report.
	 *
	 * @see BackupMode
	 * @see JavaFile
	 * @see JavaFileEnumerator
	 * @see JavaFileAnalyzer
	 * @see AuthorTaggerConfig
	 * @see DiffCalculator
	 * @see DiffReportWriter
	 */
	public void start() {
		LOG.info("JAuthorTagger running in {} mode", backupMode);

		LOG.info("Enumerating .java files in project directory: {}", projectDir.getAbsolutePath());
		List<JavaFile> javaFiles = new JavaFileEnumerator().enumerateJavaFiles(new File(projectDir, "src"));

		if (RESTORE != backupMode) {
			LOG.info("Analyzing {} .java files", javaFiles.size());
			new JavaFileAnalyzer().analyzeJavaFiles(javaFiles);

			LOG.info("Reading project configuration and tagging (in memory)");
			new AuthorTaggerConfig(configFileOf(projectDir)).loadAndApply(javaFiles);

			LOG.info("Writing to disk");
		} else {
			LOG.info("Restoring backup files");
		}

		AuthorTagWriter w = new AuthorTagWriter(backupMode);
		for (JavaFile javaFile : javaFiles) {

			// pre-read original content for diff calculation
			List<String> originalContent = IOUtils.fileToStringList(javaFile.getFile());

			// do magic (author tagging or restoring)
			w.writeAuthorTags(javaFile);

			// read output and calculate diff
			File originalFile = (BACKUP == backupMode) ? backupFileOf(javaFile.getFile()) : javaFile.getFile();
			File modifiedFile = (TEST == backupMode) ? testFileOf(javaFile.getFile()) : javaFile.getFile();
			List<String> modifiedContent = IOUtils.fileToStringList(modifiedFile);
			DiffCalculator dc = new DiffCalculator(originalFile, modifiedFile, originalContent, modifiedContent);
			javaFile.setDiffResult(dc.calculateDiff());
		}

		if (RESTORE != backupMode) {
			LOG.info("Generating diff report");
			new DiffReportWriter(projectDir, backupMode, javaFiles).writeDiffReport();
		}

		LOG.info("Done!");
	}
}
