package musicxmltestsuite.tests.base;

import static com.xenoage.utils.kernel.Range.range;
import static musicxmltestsuite.tests.utils.LambdaUtils.buildArray;


/**
 * A piece with 20 parts to check whether an application supports that
 * many parts and whether they are correctly sorted. 
 * 
 * @author Andreas Wenger
 */
public interface Base41b
	extends Base {

	@Override default String getFileName() {
		return "41b-MultiParts-MoreThan10.xml";
	}
	
	String[] expectedNames = buildArray(range(20), i -> "P" + i);
	
}
