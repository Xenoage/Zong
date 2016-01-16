package material;

import java.util.List;

/**
 * Base class for a test collection.
 * 
 * @author Andreas Wenger
 */
public interface Suite<T> {
	
	String getName();
	List<T> getExamples();
	
}
