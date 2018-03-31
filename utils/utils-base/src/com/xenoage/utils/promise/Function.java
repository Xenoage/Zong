package com.xenoage.utils.promise;

/**
 * TODO: Experimental.
 *
 * @author Andreas Wenger
 */
public interface Function<I, O> {

	O run(I value);

}
