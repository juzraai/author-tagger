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
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import hu.juranyi.zsolt.jauthortagger.model.JavaFile;
import hu.juranyi.zsolt.jauthortagger.model.JavaFiles;
import hu.juranyi.zsolt.jauthortagger.util.ClassNameFilter;
import hu.juranyi.zsolt.jauthortagger.util.Log;
import hu.juranyi.zsolt.jauthortagger.util.SimpleStringFilter;

/**
 * <p>
 * Class for loading, interpreting and applying project configuration on
 * <code>JavaFiles</code> objects. The project configuration file is a simple
 * text file which will be interpreted line by line, sequentially:
 * </p>
 * <ul>
 * <li>configuration lines are those which match the pattern:
 * <code>[any whitespace] ACTION [any whitespace] PARAMETER</code></li>
 * <li>lines that not match the pattern will be skipped, so you can write simple
 * text around configuration lines</li>
 * <li>whitespaces are optional, they will be thrown away when parsing</li>
 * <li>available actions:
 * <ul>
 * <li><code>$</code> (<u>$</u>elect)</li>
 * <li><code>@</code> ( <u>@uthor</u>)</li>
 * <li><code>!</code> (special operation)</li>
 * <li><code>-</code> (deletion)</li>
 * <li><code>+</code> (addition)</li>
 * </ul>
 * </li>
 * <li><code>$</code> - Starts a new section. The parameter must be a class name
 * filter (see <code>ClassNameFilter</code>'s doc). It selects your
 * <code>.java</code> files, and the following operations (special, del, add)
 * will be performed on them.</li>
 * <li><code>@</code> - Starts a new section. The parameter must be an author
 * name. The following operations (del, add) will affect this author.</li>
 * <li><code>!</code> - There are only one special action, and this is
 * <code>"skip"</code> (write without quotes). It can be used in <code>$</code>
 * sections. The skip action tells <b>JAuthorTagger</b> to forget the selected
 * classes, do not perform any operation on them.</li>
 * <li><code>-</code> - Used in a <code>$</code> section, the parameter must be
 * an author name filter (see <code>SimpleStringFilter</code>'s doc), and means
 * that matching authors in the selected classes will be removed. Used in a
 * <code>@</code> section, the parameter must be a class name filter, and means
 * that the selected author will be removed from matching classes.</li>
 * <li><code>+</code> - Used in a <code>$</code> section, the parameter must be
 * an author name: it will be added to the selected classes. Used in a
 * <code>@</code> section, the parameter must be a class name filter, and means
 * that the given author will be added to matching classes.</li>
 * </ul>
 *
 * @author Zsolt Jurányi
 * @see JavaFiles
 * @see ClassNameFilter
 * @see SimpleStringFilter
 *
 */
public class AuthorTaggerConfig {

	private static final Logger LOG = Log.forClass(AuthorTaggerConfig.class);
	private static final Pattern CONF_LINE_PATTERN = Pattern.compile("^\\s*(?<a>[$@!\\-+])\\s*(?<p>.*)\\s*$");
	private final File configFile;

	/**
	 * Creates an instance.
	 *
	 * @param configFile
	 *            - A <code>File</code> object pointing to a project
	 *            configuration file e.g. "path/to/project/.authors".
	 */
	public AuthorTaggerConfig(File configFile) {
		this.configFile = configFile;
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
	 * <code>JavaFiles</code> object. Adds or removes authors using the filters
	 * defined in the configuration file. Skipping is also handled here: skipped
	 * <code>JavaFile</code> objects will be removed from the memory so they
	 * won't reach the tagging procedure.
	 *
	 * @param javaFiles
	 *            - A <code>JavaFiles</code> to work on.
	 * @see JavaFile
	 * @see JavaFiles
	 */
	public void loadAndApply(JavaFiles javaFiles) {
		Scanner s = null;
		try {
			s = new Scanner(configFile, "UTF-8");
			String classFilter = null;
			String author = null;
			while (s.hasNextLine()) {
				String line = s.nextLine().replaceAll("#.*$", "").replaceAll("\\s+$", "");

				Matcher m = CONF_LINE_PATTERN.matcher(line);
				if (m.find()) {
					String action = m.group("a");
					String param = m.group("p");
					if ("$".equals(action)) {
						// new block - rules for selected classes
						classFilter = param;
						author = null;
					} else if ("@".equals(action)) {
						// new block - rules for an author
						author = param;
						classFilter = null;
					} else if ("!".equals(action)) {
						// special action
						if ("skip".equalsIgnoreCase(param) && null != classFilter) {
							LOG.trace("SKIP :: {}", classFilter);
							javaFiles.skip(classFilter);
						}
					} else if ("+".equals(action)) {
						// addition
						if (null != classFilter) {
							LOG.trace("{} >> {}", param, classFilter);
							javaFiles.addAuthor(classFilter, param);
						} else if (null != author) {
							LOG.trace("{} >> {}", author, param);
							javaFiles.addAuthor(param, author);
						}
					} else if ("-".equals(action)) {
						// deletion
						if (null != classFilter) {
							LOG.trace("{} << {}", param, classFilter);
							javaFiles.delAuthor(classFilter, param);
						} else if (null != author) {
							LOG.trace("{} << {}", author, param);
							javaFiles.delAuthor(param, author);
						}
					}
				} // conf line
			}
		} catch (FileNotFoundException e) {
			LOG.error("Config file not found", e);
		} finally {
			if (null != s) {
				s.close();
			}
		}
		for (JavaFile javaFile : javaFiles) {
			LOG.trace("{}", javaFile);
		}
	}

}
