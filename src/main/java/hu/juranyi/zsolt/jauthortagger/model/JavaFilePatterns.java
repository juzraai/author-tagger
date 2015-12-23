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

import java.util.regex.Pattern;

import hu.juranyi.zsolt.jauthortagger.input.JavaFileAnalyzer;
import hu.juranyi.zsolt.jauthortagger.output.AuthorTagWriter;

/**
 * Constants used for recognizing lines in <code>.java</code> files. They are
 * used by the analyzer and the tagger, in really simple algorithms which parses
 * the files line by line sequentially.
 *
 * @author Zsolt Jurányi
 * @see AuthorTagWriter
 * @see JavaFileAnalyzer
 *
 */
public class JavaFilePatterns {

	/**
	 * Pattern to match lines that begin with annotations.
	 */
	public static final Pattern ANNOTATION_PATTERN = Pattern.compile("^@[^ ]+.*");

	/**
	 * Pattern to match lines that contain a Javadoc <code>@author</code> tag.
	 * The line should look like this: <code>" * @author name"</code>.
	 */
	public static final Pattern AUTHOR_PATTERN = Pattern.compile("^ \\* @author (.*)(\\/\\/.*)?$");

	/**
	 * Pattern to match package declaration. The <code>package</code> keyword
	 * must be at the beginning of the line.
	 */
	public static final Pattern PACKAGE_PATTERN = Pattern.compile("^package ([^ ;]+);", Pattern.CASE_INSENSITIVE);

	/**
	 * Pattern to match type declaration. The line can contain annotations but
	 * <code>public</code> must be at the beginning of the line.
	 */
	public static final Pattern TYPE_DECLARATION_PATTERN = Pattern.compile(
			"^public( (@[^ ]+|abstract|final|strictfp))* (class|enum|@?interface) (?<n>[^ ]+)",
			Pattern.CASE_INSENSITIVE);

}
