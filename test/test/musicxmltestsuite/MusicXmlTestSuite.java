package musicxmltestsuite;

import musicxmltestsuite.report.HtmlReport;
import musicxmltestsuite.report.TestStatus;
import musicxmltestsuite.tests.base.Base;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.extensions.cpsuite.ClasspathSuite.BaseTypeFilter;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

/**
 * Suite for running the unofficial MusicXML Test Suite tests.
 * 
 * Running this file as a JUnit suite runs all the tests in the suite.
 * Running thus file as a Java application creates a report HTML file
 * (see {@link HtmlReport}).
 * 
 * @author Andreas Wenger
 */
@RunWith(ClasspathSuite.class)
@BaseTypeFilter(Base.class)
public class MusicXmlTestSuite
	extends RunListener {
	
	private static JUnitCore runner = null;
	private static HtmlReport report = new HtmlReport();
	
	public static void main(String[] args)
	{
		runner = new JUnitCore();
		runner.addListener(new MusicXmlTestSuite());
		runner.run(MusicXmlTestSuite.class);
		report.writeToHtmlFile();
	}
	
	@Override public void testFailure(Failure failure) {
		report(TestStatus.Failure, failure.getDescription());
	}

	@Override public void testFinished(Description description) {
		if (description.getAnnotation(ToDo.class) != null)
			report(TestStatus.ToDo, description);
		else
			report(TestStatus.Success, description);
	}

	@Override public void testIgnored(Description description) {
		report(TestStatus.NotAvailable, description);
	}
	
	private void report(TestStatus status, Description description) {
		String className = description.getClassName();
		report.report(getTestFile(className), getProjectName(className), status);
	}
	
	private String getTestFile(String className) {
		try {
			Base base = (Base) Class.forName(className).newInstance();
			return base.getFileName();
		} catch (Exception ex) {
			return "?";
		}
	}
	
	private String getProjectName(String className) {
		try {
			Base base = (Base) Class.forName(className).newInstance();
			return base.getProjectName();
		} catch (Exception ex) {
			return "?";
		}
	}

}
