package musicxmltestsuite.tests.utils;

import java.util.function.Function;

import com.xenoage.utils.kernel.Range;


public class LambdaUtils {

	public static String[] buildArray(Range range, Function<Integer, String> conversion) {
		String[] ret = new String[range.getCount()];
		int c = 0;
		for (int i : range) {
			ret[c++] = conversion.apply(i);
		}
		return ret;
	}
	
}
