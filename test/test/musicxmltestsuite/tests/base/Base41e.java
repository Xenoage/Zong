package musicxmltestsuite.tests.base;


public interface Base41e
	extends Base {

	@Override default String getFileName() {
		return "41e-StaffGroups-InstrumentNames-Linebroken.xml";
	}


	String[] expectedPartNames = { "Long\r\nStaff\r\nName" };
	String[] expectedPartAbbreviations = { "St.\r\nNm." };

}
