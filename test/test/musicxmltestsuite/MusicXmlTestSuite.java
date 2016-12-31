package musicxmltestsuite;

import musicxmltestsuite.report.StatusHtmlReport;
import musicxmltestsuite.report.TestStatus;
import musicxmltestsuite.tests.base.Base;
import musicxmltestsuite.tests.base.OtherTests;
import musicxmltestsuite.tests.utils.ToDo;
import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.extensions.cpsuite.ClasspathSuite.BaseTypeFilter;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.util.Arrays;
import java.util.Map;

/**
 * Suite for running the unofficial MusicXML Test Suite tests.
 * 
 * Running this file as a JUnit suite runs all the tests in the suite.
 *
 * Running the {@link #runWithHtmlStatusReport()} method creates report HTML files
 * (see {@link StatusHtmlReport}).
 * 
 * @author Andreas Wenger
 */
@RunWith(ClasspathSuite.class)
@BaseTypeFilter(Base.class)
public class MusicXmlTestSuite
	extends RunListener {

	private static JUnitCore runner = null;
	private static StatusHtmlReport report = new StatusHtmlReport();

	public static void runWithHtmlStatusReport() {
		runner = new JUnitCore();
		runner.addListener(new MusicXmlTestSuite());
		runner.run(MusicXmlTestSuite.class);
		Map<TestStatus, String[]> otherTests = OtherTests.getOtherTests();
		for (TestStatus status : otherTests.keySet())
			Arrays.stream(otherTests.get(status)).forEach(
				t -> report.report(t, status));
		report.writeToHtmlFile();
	}
	
	@Override public void testFailure(Failure failure) {
		report(TestStatus.Failure, failure.getDescription());
	}

	@Override public void testFinished(Description description) {
		if (isToDoAnnotated(description))
			report(TestStatus.Incomplete, description);
		else
			report(TestStatus.Complete, description);
	}

	@Override public void testIgnored(Description description) {
		report(TestStatus.UnsupportedToDo, description);
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
	
	private boolean isToDoAnnotated(Description description) {
		if (description.getAnnotation(ToDo.class) != null) //@Test method annotated
			return true;
		if (description.getTestClass().isAnnotationPresent(ToDo.class)) //test class annotated
			return true;
		for (Class<?> interf : description.getTestClass().getInterfaces())
			if (interf.isAnnotationPresent(ToDo.class)) //interface class annotated
				return true;
		return false;
	}

}
