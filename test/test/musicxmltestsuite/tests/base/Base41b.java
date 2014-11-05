package musicxmltestsuite.tests.base;

import static com.xenoage.utils.kernel.Range.range;
import static musicxmltestsuite.tests.utils.LambdaUtils.buildArray;


public interface Base41b
	extends Base {

	@Override default String getFileName() {
		return "41b-MultiParts-MoreThan10.xml";
	}
	
	String[] expectedNames = buildArray(range(20), i -> "P" + i);
	
}
