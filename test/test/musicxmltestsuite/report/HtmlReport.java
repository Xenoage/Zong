package musicxmltestsuite.report;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import musicxmltestsuite.MusicXmlTestSuite;
import musicxmltestsuite.tests.base.Base;

import com.xenoage.utils.jse.io.JseFileUtils;
import com.xenoage.utils.jse.io.JseStreamUtils;

/**
 * Creates an HTML report of the MusicXML implementation status
 * of the subprojects, based on the given tests.
 * 
 * @author Andreas Wenger
 */
public class HtmlReport {
	
	public static final String[] projects = { "musicxml", "musicxml-in", "layout", "midi-out"};
	public static final String dirReport = "reports/";
	
	private List<TestRow> rows = alist();
	
	
	public void report(String testName, String project, TestStatus status) {
		getRow(testName).set(project, status);
	}
	
	public void report(String testName, TestStatus status) {
		getRow(testName).setDefaultStatus(status);
	}
	
	private TestRow getRow(String testName) {
		//find existing row for test
		for (TestRow r : rows)
			if (r.getTestName().equals(testName))
				return r;
		//not found, so add one
		TestRow r = new TestRow(testName);
		rows.add(r);
		return r;
	}
	
	public void writeToHtmlFile() {
		rows.sort((r1, r2) -> r1.getTestName().compareTo(r2.getTestName()));
		String template = loadHtmlTemplate("template");
		template = template.replace("[[rows]]", createHtmlRows());
		JseFileUtils.writeFile(template, getHtmlFilePath());
	}

	private String loadHtmlTemplate(String name) {
		return JseStreamUtils.readToString(
			getClass().getResourceAsStream("templates/" + name + ".html"));
	}
	
	private String getHtmlFilePath() {
		return "reports/" + MusicXmlTestSuite.class.getSimpleName() + ".html";
	}

	private String createHtmlRows() {
		StringBuilder ret = new StringBuilder();
		for (TestRow row : rows)
			ret.append(createHtmlRow(row));
		return ret.toString();
	}

	private String createHtmlRow(TestRow row) {
		String template = loadHtmlTemplate("row");
		String testLink = createTestLink(row.getTestName());
		template = template.replace("[[test]]", testLink);
		for (String project : projects) {
			String cellHtml = createHtmlCell(row, project);
			template = template.replace("[[" + project + "]]", cellHtml);
		}
		return template;
	}
	
	private String createTestLink(String testName) {
		return "<a href=\"" + Base.onlinePath + "#" + testName.replace(".xml", ".ly") +
			"\">" + testName + "</a>";
	}

	private String createHtmlCell(TestRow row, String project) {
		String template = loadHtmlTemplate("cell");
		TestStatus status = row.get(project);
		template = template.replace("[[status]]", ""+status);
		return template;
	}
	
}
