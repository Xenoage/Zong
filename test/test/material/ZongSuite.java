package material;

/**
 * Base interface for Zong!'s own test suites.
 * 
 * @author Andreas Wenger
 */
public interface ZongSuite<T>
	extends Suite<T> {

	@Override default String getName() {
		return "Zong!";
	}
	
}
