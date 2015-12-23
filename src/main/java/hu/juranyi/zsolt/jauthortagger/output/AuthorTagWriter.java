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
import hu.juranyi.zsolt.jauthortagger.model.JavaFile;
import hu.juranyi.zsolt.jauthortagger.model.JavaFilePatterns;
import hu.juranyi.zsolt.jauthortagger.util.Log;

public class AuthorTagWriter {

	private static final Logger LOG = Log.forClass(AuthorTagWriter.class);
	private final File outputDir;

	public AuthorTagWriter() {
		this(null);
	}

	public AuthorTagWriter(File outputDir) {
		this.outputDir = outputDir;
	}

	public void writeAuthorTags(JavaFile javaFile) {
		if (null == javaFile || null == javaFile.getTypeName()) {
			return;
		}

		// skipping
		if (AuthorTaggingMode.SKIP == javaFile.getTaggingMode()) {
			LOG.info("Skipping type: {}", javaFile.getTypeName());
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

		// I/O init
		File inputFile = javaFile.getFile();
		File outputFile = javaFile.getFile();
		File tempFile = new File("temp.java");
		tempFile.deleteOnExit();
		if (null != outputDir) {
			outputFile = new File(outputDir, javaFile.getTypeName().replaceAll("\\.", "\\" + File.separator) + ".java");
			File p = outputFile.getParentFile();
			if (null != p && !p.exists()) {
				p.mkdirs();
			}
		}

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

				// let's print the current line except its an old author tag
				if (!isAuthorLine) {
					w.write(line);
					w.newLine();
				}
			}

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
					LOG.trace("Overwriting {}", outputFile.getAbsolutePath());
					outputFile.delete();
					tempFile.renameTo(outputFile);
				} catch (IOException e) {
					LOG.error("", e);
				}
			}
		}
	}

}
