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

package hu.juranyi.zsolt.jauthortagger.model;

import java.io.File;

/**
 * Constants of filenames, filename parts and related helper functions brought
 * to one place.
 *
 * @author Zsolt Jurányi
 *
 */
public class Filenames {

	/**
	 * Additional file extension for backup files: <code>.at-save</code>
	 */
	private static final String BACKUP_FILE_SUFFIX = ".at-save";

	/**
	 * Filename of the generated diff report:
	 * <code>.authors-diff-report.html</code>
	 */
	private static final String DIFF_REPORT_FILE = ".authors-diff-report.html";

	/**
	 * Filename of the diff report template resource:
	 * <code>authors-diff-report.vm</code>
	 */
	public static final String DIFF_REPORT_TEMPLATE = "authors-diff-report.vm";

	/**
	 * The configuration file's name: <code>.authors</code>
	 */
	private static final String PROJECT_CONFIG_FILE = ".authors";

	/**
	 * The log file's name: <code>.authors-log</code>
	 */
	private static final String PROJECT_LOG_FILE = ".authors-log";

	/**
	 * Additional file extension for temporary files: <code>.at-temp</code>
	 */
	private static final String TEMP_FILE_SUFFIX = ".at-temp";

	/**
	 * Additional file extension for test files: <code>.at-test</code>
	 */
	private static final String TEST_FILE_SUFFIX = ".at-test";

	/**
	 * Creates a new <code>File</code> object that points to the backup of the
	 * given file. Basically it appends <code>BACKUP_FILE_SUFFIX</code> to the
	 * given path as an additional file extension.
	 *
	 * @param javaFile
	 *            The <code>File</code> you want to backup.
	 * @return A new <code>File</code> object that points to the backup of the
	 *         given file.
	 * @see #BACKUP_FILE_SUFFIX
	 */
	public static File backupFileOf(File javaFile) {
		return new File(javaFile.getAbsolutePath() + BACKUP_FILE_SUFFIX);
	}

	/**
	 * Creates a new <code>File</code> object that points to the configuration
	 * file of the given project directory. Basically it appends
	 * <code>PROJECT_CONFIG_FILE</code> to the given path as a file inside the
	 * directory.
	 *
	 * @param projectDir
	 *            The project directory.
	 * @return A new <code>File</code> object that points to the configuration
	 *         file of the given project directory.
	 * @see #PROJECT_CONFIG_FILE
	 */
	public static File configFileOf(File projectDir) {
		return new File(projectDir, PROJECT_CONFIG_FILE);
	}

	/**
	 * Creates a new <code>File</code> object that points to the diff report
	 * file of the given project directory. Basically it appends
	 * <code>DIFF_REPORT_FILE</code> to the given path as a file inside the
	 * directory.
	 *
	 * @param projectDir
	 *            The project directory.
	 * @return A new <code>File</code> object that points to the diff report
	 *         file of the given project directory.
	 * @see #DIFF_REPORT_FILE
	 */
	public static File diffReportOf(File projectDir) {
		return new File(projectDir, DIFF_REPORT_FILE);
	}

	/**
	 * Creates a new <code>File</code> object that points to the log file of the
	 * given project directory. Basically it appends
	 * <code>PROJECT_LOG_FILE</code> to the given path as a file inside the
	 * directory.
	 *
	 * @param projectDir
	 *            The project directory.
	 * @return A new <code>File</code> object that points to the log file of the
	 *         given project directory.
	 * @see #PROJECT_LOG_FILE
	 */
	public static File logFileOf(File projectDir) {
		return new File(projectDir, PROJECT_LOG_FILE);
	}

	/**
	 * Creates a new <code>File</code> object that points to the temporary file
	 * used for the given file. Basically it appends
	 * <code>TEMP_FILE_SUFFIX</code> to the given path as an additional file
	 * extension.
	 *
	 * @param javaFile
	 *            The <code>File</code> you want to use as base for the
	 *            temporary file.
	 * @return A new <code>File</code> object that points to the temporary file
	 *         used for the given file.
	 * @see #TEMP_FILE_SUFFIX
	 */
	public static File tempFileOf(File javaFile) {
		return new File(javaFile.getAbsolutePath() + TEMP_FILE_SUFFIX);
	}

	/**
	 * Creates a new <code>File</code> object that points to the test file used
	 * for the given file. Basically it appends <code>TEST_FILE_SUFFIX</code> to
	 * the given path as an additional file extension.
	 *
	 * @param javaFile
	 *            The <code>File</code> you want to use as base for the test
	 *            file.
	 * @return A new <code>File</code> object that points to the test file used
	 *         for the given file.
	 * @see #TESP_FILE_SUFFIX
	 */
	public static File testFileOf(File javaFile) {
		return new File(javaFile.getAbsolutePath() + TEST_FILE_SUFFIX);
	}
}
