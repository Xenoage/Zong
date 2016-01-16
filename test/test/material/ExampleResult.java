package material;

import static material.ExampleResult.Result.Accepted;
import static material.ExampleResult.Result.Failed;
import static material.ExampleResult.Result.Perfect;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Test result for an example.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public class ExampleResult {
	
	public static enum Result {
		Perfect,
		Accepted,
		Failed;
	}
	
	private ExampleBase example;
	private Result result;
	private String comment;
	
	
	public static ExampleResult perfect(ExampleBase example) {
		return new ExampleResult(example, Perfect, null);
	}
	
	public static ExampleResult accepted(ExampleBase example, String comment) {
		return new ExampleResult(example, Accepted, comment);
	}
	
	public static ExampleResult failed(ExampleBase example, String comment) {
		return new ExampleResult(example, Failed, comment);
	}
	
}
