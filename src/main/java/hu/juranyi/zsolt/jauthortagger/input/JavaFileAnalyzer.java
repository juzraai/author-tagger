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

package hu.juranyi.zsolt.jauthortagger.input;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

import org.slf4j.Logger;

import hu.juranyi.zsolt.jauthortagger.model.JavaFile;
import hu.juranyi.zsolt.jauthortagger.model.JavaFilePatterns;
import hu.juranyi.zsolt.jauthortagger.util.Log;

/**
 * Analyzes <code>JavaFile</code> objects by reading the physical file line by
 * line and looking for package declaration, type name and existing
 * <code>@author</code> tags.
 *
 * @author Zsolt Jurányi
 * @see JavaFile
 *
 */
public class JavaFileAnalyzer {

	private static final Logger LOG = Log.forClass(JavaFileAnalyzer.class);

	/**
	 * <p>
	 * Analyzes the given <code>JavaFile</code> object. It should have a valid
	 * <code>File</code> object inside, because the physical <code>.java</code>
	 * file will be read line by line. It looks for the package name, the type
	 * name and the existing <code>@author</code> tags, and fills
	 * <code>JavaFile</code> object's <code>typeName</code> and
	 * <code>authors</code> fields accordingly. It only reads the file until it
	 * finds the first public type declaration. It saves the index of the type
	 * declaration's first line (it cares about annotations:)), so the tagger
	 * will know when to stop modifying and switch to simple copying.
	 * </p>
	 * <p>
	 * The algorithm is really simple so there are some <b>limitations</b> on
	 * the <code>.java</code> files:
	 * </p>
	 * <ul>
	 * <li>the package declaration's line should start with the keyword
	 * <code>package</code> (without leading inline comment)</li>
	 * <li>the authors should be in one line each in this format:
	 * <code>" * @author name"</code></li>
	 * <li>the first type declaration in the file must be the public one</li>
	 * <li>also the <code>public</code> must be at the beginning of the line
	 * </li>
	 * <li>the algorithm does not check if the lines are in the proper block or
	 * outside of a multiline comment, it will pick the first line that matches
	 * the appropriate pattern</li>
	 * </ul>
	 *
	 * @param javaFile
	 *            - The <code>JavaFile</code> object to be analyzed.
	 * @return <code>true</code> if analyzation succeded, <code>false</code> if
	 *         there were any errors.
	 * @see JavaFile
	 */
	public boolean analyzeJavaFile(JavaFile javaFile) {
		boolean success = false;
		// TODO handle root pkg classes -> no package declaration
		// TODO handle package-info.java -> no type declaration

		LOG.trace("Analyzing .java file: {}", javaFile.getFile().getAbsoluteFile());
		Scanner s = null;
		try {
			s = new Scanner(javaFile.getFile(), "UTF-8");
			int ln = -1;
			String packageName = null;

			// find package name
			while (null == packageName && s.hasNextLine()) {
				ln++;
				String line = s.nextLine();
				Matcher m = JavaFilePatterns.PACKAGE_PATTERN.matcher(line);
				if (m.find()) {
					packageName = m.group(1).trim();
				}
			}

			// find old authors and type declaration
			while (null == javaFile.getTypeName() && s.hasNextLine()) {
				ln++;
				String line = s.nextLine();

				Matcher authorMatcher = JavaFilePatterns.AUTHOR_PATTERN.matcher(line);
				Matcher annotationMatcher = JavaFilePatterns.ANNOTATION_PATTERN.matcher(line);
				Matcher typeMatcher = JavaFilePatterns.TYPE_DECLARATION_PATTERN.matcher(line);

				if (authorMatcher.find()) {
					String author = authorMatcher.group(1).trim();
					javaFile.getAuthors().add(author);
				} else if (annotationMatcher.find()) {
					javaFile.setTypeDeclarationStartLine(ln);
				} else if (typeMatcher.find()) {
					if (-1 == javaFile.getTypeDeclarationStartLine()) {
						javaFile.setTypeDeclarationStartLine(ln);
					}
					String typeName = typeMatcher.group("n");
					javaFile.setTypeName(packageName + "." + typeName);
				}
			}

			LOG.trace("{} ", javaFile);
			success = null != javaFile.getTypeName();
		} catch (FileNotFoundException e) {
			LOG.error("Error while analyzing .java file", e);
		} finally {
			if (null != s) {
				s.close();
			}
		}
		return success;
	}

	/**
	 * Analyzes the given <code>JavaFile</code> objects by calling
	 * <code>analyzeJavaFiles</code> method on them.
	 *
	 * @param javaFiles
	 *            - The <code>JavaFile</code> objects to be analyzed.
	 * @see #analyzeJavaFile(JavaFile)
	 * @see JavaFile
	 */
	public void analyzeJavaFiles(List<JavaFile> javaFiles) {
		LOG.debug("Analyzing {} .java files", javaFiles.size());
		int i = 0;
		while (i < javaFiles.size()) {
			if (!analyzeJavaFile(javaFiles.get(i))) {
				javaFiles.remove(i);
			} else {
				i++;
			}
		}
	}

}
