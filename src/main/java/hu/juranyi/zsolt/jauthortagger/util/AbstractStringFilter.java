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
 * Abstract <code>String</code> filtering utility which accepts a readable
 * filter as input, immediately converts it to a regular expression and provides
 * a test method. A conversion is what is abstract. Readable filter is usually a
 * simple search term with jokers ('?', '*') inside, but it's handled by the
 * implementation.
 *
 * @author Zsolt Jurányi
 *
 */
public abstract class AbstractStringFilter {

	private final String filter;
	private final Pattern pattern;

	/**
	 * Creates an instance using a readable filter which will be converted into
	 * a regular expression. The <code>filterToPattern</code> method will be
	 * called to perform the conversion.
	 *
	 * @param filter
	 *            - Readable filter <code>String</code> to use.
	 * @see #filterToPattern(String)
	 */
	public AbstractStringFilter(String filter) {
		this.filter = filter;
		this.pattern = filterToPattern(filter);
	}

	/**
	 * Tests the given input <code>String</code> whether it matches the stored
	 * pattern.
	 *
	 * @param str
	 *            - Input <code>String</code> to test.
	 * @return <code>true</code> if the input is non-null and matches the stored
	 *         pattern, <code>false</code> otherwise.
	 */
	public boolean accept(String str) {
		return null != str && pattern.matcher(str).find();
	}

	/**
	 * Method to convert the readable filter into a regular expression.
	 *
	 * @param filter
	 *            - The filter <code>String</code> to convert.
	 * @return The regular expression as a <code>Pattern</code> object.
	 */
	protected abstract Pattern filterToPattern(String filter);

	/**
	 * Returns the readable filter.
	 *
	 * @return The readable filter.
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * Returns the regular expression as a <code>Pattern</code> object.
	 *
	 * @return The regular expression as a <code>Pattern</code> object.
	 */
	public Pattern getPattern() {
		return pattern;
	}

	@Override
	public String toString() {
		return "AbstractStringFilter [filter=" + filter + ", pattern=" + pattern + "]";
	}

}
