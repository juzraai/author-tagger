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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import hu.juranyi.zsolt.jauthortagger.model.AuthorTaggingMode;
import hu.juranyi.zsolt.jauthortagger.model.JavaFile;
import hu.juranyi.zsolt.jauthortagger.util.ClassNameFilter;
import hu.juranyi.zsolt.jauthortagger.util.Log;

/**
 * Class for loading, interpreting and applying project configuration on
 * <code>JavaFile</code> objects. The project configuration file is a simple
 * text file which will be interpreted line by line, sequentially.
 *
 * @author Zsolt Jurányi
 * @see JavaFile
 *
 */
public class AuthorTaggerConfig { // TODO DOC: config file format...

	private static final Logger LOG = Log.forClass(AuthorTaggerConfig.class);
	private static final Pattern FILTER_SECTION_HEADER_PATTERN = Pattern
			.compile("^\\$\\t(?<f>[^\\t]+)(\\t(?<m>merge|overwrite|skip))?$", Pattern.CASE_INSENSITIVE);
	private static final Pattern AUTHOR_SECTION_HEADER_PATTERN = Pattern.compile("^@\\t(?<a>.*)$");
	private final File configFile;

	/**
	 * Creates an instance.
	 *
	 * @param configFile
	 *            A <code>File</code> object pointing to a project configuration
	 *            file e.g. "path/to/project/.authors".
	 */
	public AuthorTaggerConfig(File configFile) {
		this.configFile = configFile;
	}

	/**
	 * Adds a new author to the given <code>JavaFile</code> objects. Called by
	 * <code>loadAndApply</code>.
	 *
	 * @param filteredJavaFiles
	 *            List of <code>JavaFile</code> objects to be be modified.
	 * @param author
	 *            The new author to be added to the <code>JavaFile</code>
	 *            objects.
	 * @see #loadAndApply(List)
	 * @see JavaFile
	 */
	protected void addAuthor(List<JavaFile> filteredJavaFiles, String author) {
		LOG.debug("@author {} >> {} .java files", author, filteredJavaFiles.size());
		for (JavaFile javaFile : filteredJavaFiles) {
			LOG.trace("@author {} >> {}", author, javaFile.getTypeName());
			javaFile.getNewAuthors().add(author);
		}
	}

	/**
	 * Filters the given <code>JavaFile</code> objects with the given filter
	 * <code>String</code>. The filter will be converted into a regular
	 * expression using <code>ClassNameFilter</code>. <code>JavaFiles</code>
	 * will be filtered by their <code>typeName</code> field. Called by
	 * <code>loadAndApply</code>.
	 *
	 * @param javaFiles
	 *            The <code>JavaFile</code> objects to be filtered.
	 * @param filter
	 *            The filter <code>String</code> to use.
	 * @return A new list containing <code>JavaFile</code> objects accepted by
	 *         the filter.
	 * @see #loadAndApply(List)
	 * @see ClassNameFilter
	 * @see JavaFile
	 */
	protected List<JavaFile> filterJavaFiles(List<JavaFile> javaFiles, String filter) {
		List<JavaFile> filteredJavaFiles = new ArrayList<JavaFile>();
		if (null == filter || filter.trim().isEmpty()) {
			return filteredJavaFiles;
		}
		String regex = ClassNameFilter.filterStringToRegex(filter);
		for (JavaFile javaFile : javaFiles) {
			String typeName = javaFile.getTypeName();
			if (null != typeName && typeName.matches(regex)) {
				filteredJavaFiles.add(javaFile);
			}
		}
		return filteredJavaFiles;
	}

	/**
	 * Returns the <code>File</code> object pointing to the project
	 * configuration file.
	 *
	 * @return The <code>File</code> object pointing to the project
	 *         configuration file.
	 */
	public File getConfigFile() {
		return configFile;
	}

	/**
	 * Loads, interprets and applies the configuration on the given
	 * <code>JavaFile</code> objects. Filters them and sets tagging mode or adds
	 * new authors according to the configuration file.
	 *
	 * @param javaFiles
	 *            List of <code>JavaFile</code> objects to be modified.
	 * @see AuthorTaggingMode
	 * @see ClassNameFilter
	 * @see JavaFile
	 */
	public void loadAndApply(List<JavaFile> javaFiles) {
		Scanner s = null;
		try {
			s = new Scanner(configFile, "UTF-8");
			List<JavaFile> filteredJavaFiles = null;
			String author = null;
			while (s.hasNextLine()) {
				String line = s.nextLine().replaceAll("#.*$", "").replaceAll("\\s+$", "");
				Matcher fm = FILTER_SECTION_HEADER_PATTERN.matcher(line);
				Matcher am = AUTHOR_SECTION_HEADER_PATTERN.matcher(line);

				if (fm.find()) {
					filteredJavaFiles = filterJavaFiles(javaFiles, fm.group("f"));
					author = null;
					setTaggingMode(filteredJavaFiles, fm.group("m"));
				} else if (am.find()) {
					author = am.group("a").trim();
					filteredJavaFiles = null;
				} else if (line.startsWith("\t")) {
					line = line.trim();
					if (null != filteredJavaFiles) {
						addAuthor(filteredJavaFiles, line);
					} else if (null != author) {
						addAuthor(filterJavaFiles(javaFiles, line), author);
					}
				}
			}
		} catch (FileNotFoundException e) {
			LOG.error("Config file not found", e);
		} finally {
			if (null != s) {
				s.close();
			}
		}
	}

	/**
	 * Sets the tagging mode in the given <code>JavaFile</code> objects. Called
	 * by <code>loadAndApply</code>.
	 *
	 * @param filteredJavaFiles
	 *            List of <code>JavaFile</code> objects to be be modified.
	 * @param modeString
	 *            The tagging mode as a <code>String</code>. It should have one
	 *            of these values: <code>"MERGE"</code>,
	 *            <code>"OVERWRITE"</code>, <code>"SKIP"</code>. If it's
	 *            <code>null</code> or unrecognizable, nothing will be modified.
	 * @see #loadAndApply(List)
	 * @see AuthorTaggingMode
	 * @see JavaFile
	 */
	protected void setTaggingMode(List<JavaFile> filteredJavaFiles, String modeString) {
		AuthorTaggingMode taggingMode = null;
		try {
			taggingMode = AuthorTaggingMode.valueOf(modeString.toUpperCase());
		} catch (Exception e) {
			LOG.warn("Invalid tagging mode: {}", modeString);
		}
		if (null != taggingMode) {
			LOG.debug("Setting tagging mode to {} for {} .java files", taggingMode, filteredJavaFiles.size());
			for (JavaFile javaFile : filteredJavaFiles) {
				LOG.trace("{} :: {}", taggingMode, javaFile.getTypeName());
				javaFile.setTaggingMode(taggingMode);
			}
		}
	}

}
