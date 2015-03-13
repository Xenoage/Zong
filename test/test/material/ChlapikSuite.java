package material;

/**
 * Base interface for Chlapik test suites.
 * 
 * @author Andreas Wenger
 */
public interface ChlapikSuite<T>
	extends Suite<T> {

	@Override default String getName() {
		return "Chlapik";
	}
	
}
