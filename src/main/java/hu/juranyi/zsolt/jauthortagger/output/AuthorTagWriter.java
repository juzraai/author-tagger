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

package hu.juranyi.zsolt.jauthortagger.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.Logger;

import hu.juranyi.zsolt.jauthortagger.model.AuthorTaggingMode;
import hu.juranyi.zsolt.jauthortagger.model.BackupingMode;
import hu.juranyi.zsolt.jauthortagger.model.Filenames;
import hu.juranyi.zsolt.jauthortagger.model.JavaFile;
import hu.juranyi.zsolt.jauthortagger.model.JavaFilePatterns;
import hu.juranyi.zsolt.jauthortagger.util.Log;

public class AuthorTagWriter {

	private static final Logger LOG = Log.forClass(AuthorTagWriter.class);
	private final BackupingMode backupingMode;

	public AuthorTagWriter(BackupingMode backupingMode) {
		this.backupingMode = backupingMode;
	}

	public void writeAuthorTags(JavaFile javaFile) {
		if (null == javaFile || null == javaFile.getFile()) {
			return;
		}

		// I/O init
		File inputFile = javaFile.getFile();
		File testFile = new File(javaFile.getFile().getAbsolutePath() + Filenames.TEST_FILE_SUFFIX);
		File outputFile = (BackupingMode.TEST == backupingMode) ? testFile : javaFile.getFile();
		File backupFile = new File(javaFile.getFile().getAbsolutePath() + Filenames.BACKUP_FILE_SUFFIX);
		File tempFile = new File(javaFile.getFile().getAbsolutePath() + Filenames.TEMP_FILE_SUFFIX);

		// restoring
		if (BackupingMode.RESTORE == backupingMode) {
			if (backupFile.exists()) {
				outputFile.delete();
				backupFile.renameTo(outputFile);
			}
			return;
		}

		// previous test files should be deleted
		if (BackupingMode.TEST != backupingMode) {
			testFile.delete();
		}

		// and backups is sometimes
		if (BackupingMode.NO_BACKUP == backupingMode) {
			backupFile.delete();
		}

		// skipping
		if (null == javaFile.getTypeName() || AuthorTaggingMode.SKIP == javaFile.getTaggingMode()) {
			LOG.info("Skipping file: {}", javaFile.getFile().getAbsolutePath());
			return;
		}

		LOG.info("Processing type: {}", javaFile.getTypeName());

		// build up final author list
		Set<String> authorsToWrite = new LinkedHashSet<String>();
		if (AuthorTaggingMode.MERGE == javaFile.getTaggingMode()) {
			authorsToWrite.addAll(javaFile.getOldAuthors());
		}
		authorsToWrite.addAll(javaFile.getNewAuthors());
		LOG.debug("Final author list for {} is: {}", javaFile.getTypeName(), authorsToWrite);

		// let's roll
		Scanner s = null;
		BufferedWriter w = null;
		try {
			s = new Scanner(inputFile, "UTF-8");
			w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "UTF8"));
			int ln = -1;
			boolean javadocFound = false;
			boolean authorsWritten = false;

			// copy first part - till the type declaration
			// replace author list during copying
			while (ln <= javaFile.getTypeDeclarationStartLine() && s.hasNextLine()) {
				ln++;
				String line = s.nextLine();
				boolean atJavadocStart = line.startsWith("/**");
				boolean atJavadocEnd = javadocFound && line.trim().equals("*/");
				boolean atTypeDeclaration = ln == javaFile.getTypeDeclarationStartLine();
				boolean isAuthorLine = javadocFound && JavaFilePatterns.AUTHOR_PATTERN.matcher(line).find();
				boolean noJavadoc = atTypeDeclaration && !javadocFound;

				if (atJavadocStart) {
					javadocFound = true;

				} else if (!authorsWritten) {

					if (noJavadoc) {
						w.write("/**");
						w.newLine();
					}

					if (noJavadoc || atJavadocEnd || isAuthorLine) {
						for (String author : authorsToWrite) {
							w.write(" * @author " + author);
							w.newLine();
						}
						authorsWritten = true;
					}

					if (noJavadoc) {
						w.write(" */");
						w.newLine();
					}
				}

				// print the current line except its an old author tag
				// (we printed merged old authors above)
				if (!isAuthorLine) {
					w.write(line);
					w.newLine();
				}
			}

			// copy the rest of the file
			while (s.hasNextLine()) {
				w.write(s.nextLine());
				w.newLine();
			}

			w.flush();
		} catch (IOException e) {
			LOG.error("Error while writing author tags", e);
		} finally {
			if (null != s) {
				s.close();
			}
			if (null != w) {
				try {
					w.close();

					// save backup if needed
					if (BackupingMode.BACKUP == backupingMode) {
						LOG.trace("Backuping to: {}", backupFile.getAbsolutePath());
						backupFile.delete();
						outputFile.renameTo(backupFile);
					}

					// place the file to the right place
					LOG.trace("Writing {}", outputFile.getAbsolutePath());
					outputFile.delete();
					tempFile.renameTo(outputFile);
				} catch (IOException e) {
					LOG.error("Error when closing temp file", e);
				}
			}
		}
	}

}
