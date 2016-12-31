package musicxmltestsuite;

import musicxmltestsuite.report.VisualHtmlReport;
import org.junit.Test;


public class MusicXmlTestSuiteHtmlReport {

	public static final String dirReport = "reports/musicxmltestsuite/";

	@Test public void statusHtmlReport() {
		MusicXmlTestSuite.runWithHtmlStatusReport();
	}

	@Test public void visualHtmlReport()
			throws Throwable{
		VisualHtmlReport.run();
	}
	
}
