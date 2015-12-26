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
	 * Additional file extension for backup files.
	 */
	private static final String BACKUP_FILE_SUFFIX = ".at-save";

	/**
	 * Filename of the generated diff report.
	 */
	private static final String DIFF_REPORT_FILE = ".authors-diff-report.html";

	/**
	 * The configuration file's name.
	 */
	private static final String PROJECT_CONFIG_FILE = ".authors";

	/**
	 * The log file's name.
	 */
	private static final String PROJECT_LOG_FILE = ".authors-log";

	/**
	 * Additional file extension for temporary files.
	 */
	private static final String TEMP_FILE_SUFFIX = ".at-temp";

	/**
	 * Additional file extension for test files.
	 */
	private static final String TEST_FILE_SUFFIX = ".at-test";

	// TODO doc

	public static File backupFileOf(File javaFile) {
		return new File(javaFile.getAbsolutePath() + BACKUP_FILE_SUFFIX);
	}

	public static File configFileOf(File projectDir) {
		return new File(projectDir, PROJECT_CONFIG_FILE);
	}

	public static File diffReportOf(File projectDir) {
		return new File(projectDir, DIFF_REPORT_FILE);
	}

	public static File logFileOf(File projectDir) {
		return new File(projectDir, PROJECT_LOG_FILE);
	}

	public static File tempFileOf(File javaFile) {
		return new File(javaFile.getAbsolutePath() + TEMP_FILE_SUFFIX);
	}

	public static File testFileOf(File javaFile) {
		return new File(javaFile.getAbsolutePath() + TEST_FILE_SUFFIX);
	}
}
