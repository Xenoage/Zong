package musicxmltestsuite.report;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.map;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Results for each test row, i.e. for
 * each project-dependent test.
 * 
 * @author Andreas Wenger
 */
public class TestRow {
	
	@Getter private String testName;
	private Map<String, TestStatus> results = map();
	@Getter @Setter private boolean unneeded;
	
	public TestRow(String testName) {
		this.testName = testName;
	}
	
	public void set(String project, TestStatus status) {
		results.put(project, status);
	}
	
	public TestStatus get(String project) {
		return notNull(results.get(project), TestStatus.NotAvailable);
	}

}
