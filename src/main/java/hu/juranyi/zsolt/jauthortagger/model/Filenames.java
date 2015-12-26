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
	public static final String BACKUP_FILE_SUFFIX = ".at-save";

	/**
	 * Filename of the generated diff report.
	 */
	public static final String DIFF_REPORT_FILE = ".authors-diff-report.html";

	/**
	 * The configuration file's name.
	 */
	public static final String PROJECT_CONFIG_FILE = ".authors";

	/**
	 * The log file's name.
	 */
	public static final String PROJECT_LOG_FILE = ".authors-log";

	/**
	 * Additional file extension for temporary files.
	 */
	public static final String TEMP_FILE_SUFFIX = ".at-temp";

	/**
	 * Additional file extension for test files.
	 */
	public static final String TEST_FILE_SUFFIX = ".at-test";

	// TODO helper methods to ease creating File objects
}
