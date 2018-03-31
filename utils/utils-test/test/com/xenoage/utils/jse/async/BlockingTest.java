package com.xenoage.utils.jse.async;

import static com.xenoage.utils.jse.async.Sync.sync;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.xenoage.utils.async.AsyncResult;
import com.xenoage.utils.async.AsyncProducer;

/**
 * Tests for {@link Sync}.
 * 
 * @author Andreas Wenger
 */
public class BlockingTest {
	
	/** Duration of computation. */
	public static final int duration = 500;
	
	/**
	 * Computes the square root. Needs about {@value #duration} ms.
	 */
	private static class Sqrt
		implements AsyncProducer<Integer> {
		
		private int val; //negativ numbers produce exception
		private int duration;
		
		public Sqrt(int val, int duration) {
			this.val = val;
			this.duration = duration;
		}

		@Override public void produce(AsyncResult<Integer> result) {
			try {
				Thread.sleep(duration);
			} catch (InterruptedException e) {
			}
			if (val >= 0)
				result.onSuccess((int) Math.round(Math.sqrt(val)));
			else
				result.onFailure(new Exception("sqrt only supported for values > 0"));
		}
		
	}
	
	@Test public void testAsyncProducerBlocking() {
		//test sqrt(25). at least <duration> ms required.
		long time = System.currentTimeMillis();
		try {
			int result = sync(new Sqrt(25, duration));
			assertEquals(5, result);
			assertTrue(System.currentTimeMillis() - time >= 500);
		} catch (Exception ex) {
			fail();
		}
		//test sqrt(-1). must fail.
		time = System.currentTimeMillis();
		try {
			sync(new Sqrt(-1, duration));
			fail();
		} catch (Exception ex) {
		}
		//test very fast computation
		try {
			int result = sync(new Sqrt(16, 0));
			assertEquals(4, result);
		} catch (Exception ex) {
			fail();
		}
	}

}
