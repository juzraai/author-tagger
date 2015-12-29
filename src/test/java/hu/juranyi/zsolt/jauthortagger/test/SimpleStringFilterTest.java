package hu.juranyi.zsolt.jauthortagger.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import hu.juranyi.zsolt.jauthortagger.util.AbstractStringFilter;
import hu.juranyi.zsolt.jauthortagger.util.ClassNameFilter;
import hu.juranyi.zsolt.jauthortagger.util.SimpleStringFilter;

/**
 * Tests <code>SimpleStringFilter</code> class' main functionality: the filter
 * to regex conversion.
 *
 * @author Zsolt Jurányi
 *
 */
public class SimpleStringFilterTest {

	@Test
	public void nullInput() {
		assertFalse(new ClassNameFilter("*").accept(null));
	}

	@Test
	public void regexFilterToRegex() {
		AbstractStringFilter filter = new SimpleStringFilter("/Will this remain a regular expression?/");
		String expectedRegex = "Will this remain a regular expression?";
		String generatedRegex = filter.getPattern().pattern();
		assertEquals(expectedRegex, generatedRegex);
	}

	@Test
	public void simpleFilterToRegex() {
		AbstractStringFilter filter = new SimpleStringFilter("Search? Term* With Multiple J*kers");
		String expectedRegex = "Search. Term.* With Multiple J.*kers";
		String generatedRegex = filter.getPattern().pattern();
		assertEquals(expectedRegex, generatedRegex);
	}

}
