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

package hu.juranyi.zsolt.jauthortagger.util;

import java.util.regex.Pattern;

/**
 * <p>
 * Implementation of <code>AbstractStringFilter</code>, which handles the input
 * filter as a specific filter for class names. The accepted filters are these:
 * </p>
 * <ul>
 * <li><code>"ClassName"</code> - Simple filter to test only the short class
 * name. It can contain jokers "?" and "*". "?" matches 1 character and "*"
 * matches a character sequence of any length (including zero). Dot character is
 * excluded.</li>
 * <li><code>"package.name.ClassName"</code> - Filter to test the fully
 * qualified name. It can contain jokers "?", "*" and "**". "?" and "*" are the
 * same as above, they both exclude ".". "**" matches a character sequence of
 * any length without any restriction.</li>
 * <li><code>"/regular-expression/"</code> - Speaks for itself, the given
 * regular expression will be used without any transformation, only the leading
 * and trailing slashes will be trimmed.</li>
 * </ul>
 *
 * @author Zsolt Jurányi
 *
 */
public class ClassNameFilter extends AbstractStringFilter {

	/**
	 * Creates an instance using a readable filter which will be converted into
	 * a regular expression. The <code>filterToPattern</code> method will be
	 * called to perform the conversion.
	 *
	 * @param filter
	 *            - Readable filter <code>String</code> to use.
	 * @see #filterToPattern(String)
	 */
	public ClassNameFilter(String filter) {
		super(filter);
	}

	@Override
	protected Pattern filterToPattern(String filter) {
		String regex = filter;
		if (regex.matches("\\/.*\\/")) { // regex - enclosed in slashes
			regex = regex.substring(1, regex.length() - 1);
		} else if (regex.contains(".")) { // full class name - contains dot
			regex = regex.replaceAll("\\.", "\\\\."); // . -> \.
			regex = regex.replaceAll("\\?", "[^.]"); // ? -> [^.]
			regex = regex.replaceAll("\\*\\*", ".×"); // ** -> .×
			regex = regex.replaceAll("\\*", "[^.]*"); // * -> [^.]*
			regex = regex.replaceAll("×", "*"); // × -> .*
			regex = "^" + regex + "$";
		} else { // short class name
			regex = regex.replaceAll("\\?", "[^.]"); // ? -> [^.]
			regex = regex.replaceAll("\\*", "[^.]*"); // * -> [^.]*
			regex = "(.*\\.)?" + regex + "$"; // (.*\.)?filter$
		}
		return Pattern.compile(regex);
	}

}
