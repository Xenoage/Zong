package material;

/**
 * Base interface for Ross test suites.
 * 
 * @author Andreas Wenger
 */
public interface RossSuite<T>
	extends Suite<T> {

	@Override default String getName() {
		return "Ross";
	}
	
}
