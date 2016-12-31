package musicxmltestsuite.tests.base;


import musicxmltestsuite.tests.utils.ErroneousScore;

@ErroneousScore
public interface Base41h
	extends Base {

	@Override default String getFileName() {
		return "41h-TooManyParts.xml";
	}

}
