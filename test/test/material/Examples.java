package material;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static org.junit.Assert.fail;

import java.util.List;

/**
 * Access to the examples.
 * 
 * @author Andreas Wenger
 */
public class Examples {

	/**
	 * Filters the given list of examples by the given part of a name.
	 * @param examples               the examples to filter
	 * @param namePart               an example qualifies, if its name contains this string
	 * @param minExpectedTestsCount  minimum number of expected results, otherwise fail
	 */
	public static <T extends ExampleBase> List<T> filter(List<T> examples,
		String namePart, int minExpectedTestsCount) {
		List<T> ret = alist();
		int found = 0;
		for (T example : examples) {
			if (example.getName().contains(namePart)) {
				found++;
				ret.add(example);
			}
		}
		if (found < minExpectedTestsCount)
			fail("only " + found + " test found");
		return ret;
	}
	
}
