package musicxmltestsuite.tests.base;

public interface Base51b
	extends Base {

	@Override default String getFileName() {
		return "51b-Header-Quotes.xml";
	}

	String expectedMovementTitle = "\"Quotes\" in header fields";
	String expectedComposer = "Some \"Tester\" Name";
	String expectedPartName = "Staff \"Test\"";

}
