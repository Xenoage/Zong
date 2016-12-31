package musicxmltestsuite.tests.base;

import musicxmltestsuite.tests.utils.ErroneousScore;

@ErroneousScore
public interface Base41g
	extends Base {

	@Override default String getFileName() {
		return "41g-PartNoId.xml";
	}

}
