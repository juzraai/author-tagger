package hu.juranyi.zsolt.jauthortagger.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import hu.juranyi.zsolt.jauthortagger.util.ClassNameFilter;

public class ClassNameFilterTest {

	@Test
	public void fullNameFilterToRegex() {
		String filterString = "Cla??Name*";
		String expectedRegex = "(.*\\.)?Cla[^.][^.]Name[^.]*$";
		String generatedRegex = ClassNameFilter.filterStringToRegex(filterString);
		assertEquals(expectedRegex, generatedRegex);
	}

	@Test
	public void regexFilterToRegex() {
		String filterString = "/Will this remain a regular expression?/";
		String expectedRegex = "Will this remain a regular expression?";
		String generatedRegex = ClassNameFilter.filterStringToRegex(filterString);
		assertEquals(expectedRegex, generatedRegex);
	}

	@Test
	public void shortNameFilterToRegex() {
		String filterString = "package*.name?.**ClassName";
		String expectedRegex = "^package[^.]*\\.name[^.]\\..*ClassName$";
		String generatedRegex = ClassNameFilter.filterStringToRegex(filterString);
		assertEquals(expectedRegex, generatedRegex);
	}

}
