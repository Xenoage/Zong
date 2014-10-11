package musicxmltestsuite.report;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;
import java.util.regex.Pattern;

import musicxmltestsuite.MusicXmlTestSuite;

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
	
	public void reportUnsupported(String testName) {
		getRow(testName);
	}
	
	public void reportUnneeded(String testName) {
		getRow(testName).setUnneeded(true);
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
		template = template.replaceAll(Pattern.quote("[[rows]]"), createHtmlRows());
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
		template = template.replaceAll(Pattern.quote("[[test]]"), row.getTestName());
		for (String project : projects) {
			String cellHtml = createHtmlCell(row, project);
			template = template.replaceAll(Pattern.quote("[[" + project + "]]"), cellHtml);
		}
		return template;
	}

	private String createHtmlCell(TestRow row, String project) {
		String template = loadHtmlTemplate("cell");
		String status;
		switch (row.get(project)) {
			case Success:
				status = "success";
				break;
			case Failure:
				status = "failure";
				break;
			case ToDo:
				status = "todo";
				break;
			default:
				if (row.isUnneeded())
					status = "unneeded";
				else
					status = "notavailable";
		}
		template = template.replaceAll(Pattern.quote("[[status]]"), status);
		return template;
	}
	
}
