package com.xenoage.utils.promise;

/**
 * Interface for a promise executor.
 *
 * @author Andreas Wenger
 */
public interface Executor<T> {

	void run(Return<T> r);

}
