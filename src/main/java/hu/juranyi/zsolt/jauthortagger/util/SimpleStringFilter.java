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
 * Implementation of <code>AbstractStringFilter</code>, which handles the input
 * filter as a simple search term. The filter can include jokers: "?" will match
 * 1 character, and "*" will match a character sequence of any length.
 *
 * @author Zsolt Jurányi
 * @see AbstractStringFilter
 *
 */
public class SimpleStringFilter extends AbstractStringFilter {

	/**
	 * Creates an instance using a readable filter which will be converted into
	 * a regular expression. The <code>filterToPattern</code> method will be
	 * called to perform the conversion.
	 *
	 * @param filter
	 *            - Readable filter <code>String</code> to use.
	 * @see #filterToPattern(String)
	 */
	public SimpleStringFilter(String filter) {
		super(filter);
	}

	@Override
	protected Pattern filterToPattern(String filter) {
		String regex = filter;
		if (regex.matches("\\/.*\\/")) { // enclosed in slashes -> just trim
			regex = regex.substring(1, regex.length() - 1);
		} else { // readable filter with jokers ? and *
			regex = regex.replace('?', '.').replace("*", ".*");
		}
		return Pattern.compile(regex);
	}

}
