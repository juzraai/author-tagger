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

package hu.juranyi.zsolt.jauthortagger.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import hu.juranyi.zsolt.jauthortagger.util.AbstractStringFilter;
import hu.juranyi.zsolt.jauthortagger.util.ClassNameFilter;

/**
 * Tests <code>ClassNameFilter</code> class' main functionality: the filter to
 * regex conversion.
 *
 * @author Zsolt Jurányi
 *
 */
public class ClassNameFilterTest {

	@Test
	public void fullNameFilterToRegex() {
		AbstractStringFilter filter = new ClassNameFilter("Cla??Name*");
		String expectedRegex = "(.*\\.)?Cla[^.][^.]Name[^.]*$";
		String generatedRegex = filter.getPattern().pattern();
		assertEquals(expectedRegex, generatedRegex);
	}

	@Test
	public void nullInput() {
		assertFalse(new ClassNameFilter("*").accept(null));
	}

	@Test
	public void regexFilterToRegex() {
		AbstractStringFilter filter = new ClassNameFilter("/Will this remain a regular expression?/");
		String expectedRegex = "Will this remain a regular expression?";
		String generatedRegex = filter.getPattern().pattern();
		assertEquals(expectedRegex, generatedRegex);
	}

	@Test
	public void shortNameFilterToRegex() {
		AbstractStringFilter filter = new ClassNameFilter("package*.name?.**ClassName");
		String expectedRegex = "^package[^.]*\\.name[^.]\\..*ClassName$";
		String generatedRegex = filter.getPattern().pattern();
		assertEquals(expectedRegex, generatedRegex);
	}

}
