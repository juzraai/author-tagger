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
 * Handy utility which turns readable class name filters into regular
 * expressions. The readable filters can contain joker characters as well. The
 * accepted filters are these:
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
public class ClassNameFilter {

	/**
	 * Transforms the given filter into a regular expression using
	 * <code>filterStringToRegex</code> then compiles it to a Java
	 * <code>Pattern</code> object.
	 *
	 * @param filter
	 *            The filter <code>String</code> to compile.
	 * @return The given filter as a <code>Pattern</code> object.
	 * @see #filterStringToRegex(String)
	 */
	public static Pattern filterStringToPattern(String filter) {
		return Pattern.compile(filterStringToRegex(filter));
	}

	/**
	 * Transforms the given filter into a regular expression. Joker characters
	 * will be replaced with appropriate regular expression parts or if the
	 * input is a regular expression, only the leading and trailing slashes will
	 * be trimmed. See the class documentation for detailed information about
	 * filter formats.
	 *
	 *
	 * @param filter
	 *            The filter <code>String</code> to transform.
	 * @return The filter transformed into a regular expression.
	 * @see ClassNameFilter
	 */
	public static String filterStringToRegex(String filter) {
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
		return regex;
	}

	private final String filter;
	private final String regex;

	/**
	 * Creates an instance.
	 *
	 * @param filter
	 *            A filter <code>String</code> to be used in this instance.
	 */
	public ClassNameFilter(String filter) {
		this.filter = filter;
		this.regex = filterStringToRegex(filter);
	}

	/**
	 * Test the given class name against the stored filter <code>String</code>
	 *
	 * @param className
	 *            The class name to test.
	 * @return <code>true</code> if the given class name matches the filter.
	 */
	public boolean accept(String className) {
		return null != className && className.matches(regex);
	}

	/**
	 * Returns the filter <code>String</code>.
	 *
	 * @return The filter <code>String</code>.
	 */
	public String getFilter() {
		return filter;
	}

	@Override
	public String toString() {
		return "ClassNameFilter [filter=" + filter + "]";
	}

}
