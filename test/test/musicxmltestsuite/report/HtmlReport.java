package musicxmltestsuite.report;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Tuple2.t;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import musicxmltestsuite.MusicXmlTestSuite;

import com.xenoage.utils.jse.io.JseFileUtils;
import com.xenoage.utils.jse.io.JseStreamUtils;
import com.xenoage.utils.kernel.Tuple2;

/**
 * Creates an HTML report of the MusicXML implementation status
 * of the subprojects, based on the given tests.
 * 
 * @author Andreas Wenger
 */
public class HtmlReport {
	
	public static final String[] projects = { "musicxml", "musicxml-in", "layout", "midi-out"};
	
	public List<Tuple2<String, Map<String, TestStatus>>> results = alist();
	
	public void begin() {
	}
	
	public void report(String test, String project, TestStatus status) {
		Tuple2<String, Map<String, TestStatus>> row = results.get(getTestIndex(test));
		row.get2().put(project, status);
	}
	
	private int getTestIndex(String test) {
		//find existing row for test
		for (int i : range(results))
			if (results.get(i).get1().equals(test))
				return i;
		//not found, so add one
		results.add(t(test, new HashMap<String, TestStatus>()));
		return results.size() - 1;
	}
	
	public void finish() {
		String template = JseStreamUtils.readToString(
			getClass().getResourceAsStream("templates/template.html"));
		StringBuilder rows = new StringBuilder();
		for (Tuple2<String, Map<String, TestStatus>> result : results) {
			String row = JseStreamUtils.readToString(getClass().getResourceAsStream("templates/row.html"));
			row = row.replaceAll(Pattern.quote("[[test]]"), result.get1());
			for (String project : projects) {
				String statusHtml = JseStreamUtils.readToString(
					getClass().getResourceAsStream("templates/status.html"));
				String img = "notavailable";
				TestStatus status = result.get2().get(project);
				if (status == TestStatus.Success)
					img = "success";
				else if (status == TestStatus.Failure)
					img = "failure";
				else if (status == TestStatus.SuccessButToDo)
					img = "todo";
				statusHtml = statusHtml.replaceAll(Pattern.quote("[[status]]"), img);
				row = row.replaceAll(Pattern.quote("[[" + project + "]]"), statusHtml);
			}
			rows.append(row);
		}
		template = template.replaceAll(Pattern.quote("[[rows]]"), rows.toString());
		JseFileUtils.writeFile(template, "reports/" +
			MusicXmlTestSuite.class.getSimpleName() + ".html");
	}

}
