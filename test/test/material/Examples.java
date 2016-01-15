package material;

import static org.junit.Assert.fail;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Access to the examples.
 * 
 * @author Andreas Wenger
 */
public class Examples {
	
	/**
	 * Runs the given test for all examples in the given list of suites.
	 */
	public static <T extends ExampleBase> void test(List<Suite<T>> suites, BiConsumer<Suite<T>, T> test) {
		for (Suite<T> suite : suites) {
			for (T example : suite.getExamples())
				test.accept(suite, example);
		}
	}
	
	/**
	 * Runs the given test for the examples in the given list of suites,
	 * which contain the given part of a name.
	 * @param namePart       an example qualifies, if its name contains this string
	 * @param minTestsCount  minimum number of expected results, otherwise fail
	 */
	public static <T extends ExampleBase> void test(List<Suite<T>> suites, String namePart,
		int minTestsCount, BiConsumer<Suite<T>, T> test) {
		int found = 0;
		for (Suite<T> suite : suites) {
			for (T example : suite.getExamples()) {
				if (example.getName().contains(namePart)) {
					found++;
					test.accept(suite, example);
				}
			}
		}
		if (found < minTestsCount)
			fail("only " + found + " test found");
	}
	
}
