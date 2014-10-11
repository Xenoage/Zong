package musicxmltestsuite;

import java.util.regex.Pattern;

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
		report.begin();
		runner.run(MusicXmlTestSuite.class);
		report.finish();
	}
	
	@Override public void testFailure(Failure failure)
		throws Exception {
		report(TestStatus.Failure, failure.getDescription().getClassName());
	}

	@Override public void testFinished(Description description)
		throws Exception {
		report(TestStatus.Success, description.getClassName());
	}

	@Override public void testIgnored(Description description)
		throws Exception {
		report(TestStatus.NotAvailable, description.getClassName());
	}
	
	private void report(TestStatus status, String className) {
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
		String[] path = className.split(Pattern.quote("."));
		return path[path.length - 2];
	}

}
