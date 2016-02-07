package material;

import static com.xenoage.utils.math.Delta.df;
import static java.lang.Math.abs;
import static material.ExampleResult.Result.Accepted;
import static material.ExampleResult.Result.Failed;
import static material.ExampleResult.Result.Perfect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Test result for an example.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
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
	
	/**
	 * When this result is still not failed, and the given values are not equal,
	 * this result is set to failed.
	 */
	public void checkEquals(String comment, int expected, int actual) {
		if (this.result == Result.Failed)
			return;
		if (actual != expected) {
			this.result = Result.Failed;
			this.comment = comment + " - expected " + expected + " but was " + actual;
		}
	}
	
	/**
	 * When this result is still not failed, and the given values are not equal,
	 * this result is set to failed.
	 */
	public void checkEquals(String comment, float expected, float actual) {
		if (this.result == Result.Failed)
			return;
		if (abs(actual - expected) > df) {
			this.result = Result.Failed;
			this.comment = comment + " - expected " + expected + " but was " + actual;
		}
	}
	
}
