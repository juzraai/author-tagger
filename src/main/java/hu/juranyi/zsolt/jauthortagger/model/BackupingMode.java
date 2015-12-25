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
 * <b>JAuthorTagger</b> has 4 choices on how to handle backups of your original
 * <code>.java</code> files. At least one of them should suit your needs. :)
 *
 * @author Zsolt Jurányi
 *
 */
public enum BackupingMode {

	/**
	 * A backup will be created of every <code>.java</code> file in your
	 * project, before modifying the original ones.
	 */
	BACKUP, //
	/**
	 * No backup will be created and also previous backups will be deleted.
	 */
	NO_BACKUP, //
	/**
	 * Overwrites your current <code>.java</code> files with the backup file to
	 * undo modifications.
	 */
	RESTORE, //
	/**
	 * Your <code>.java</code> files will remain unmodified, new test files will
	 * be created instead.
	 */
	TEST;

}
