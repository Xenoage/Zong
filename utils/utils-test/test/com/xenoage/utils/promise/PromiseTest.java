package com.xenoage.utils.promise;

import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;

import static com.xenoage.utils.jse.promise.Sync.sync;
import static com.xenoage.utils.promise.Calculator.Op.Div;
import static com.xenoage.utils.promise.Calculator.Op.Plus;
import static com.xenoage.utils.promise.Calculator.calc;
import static org.junit.Assert.*;

/**
 * Tests for {@link Promise}.
 *
 * @author Andreas Wenger
 */
public class PromiseTest {

	static int result1, result2;

	/**
	 * Tests promises with the old syntax style before Java 8.
	 */
	@Test public void testOldStyle()
			throws Exception {
		result1 = 0;
		result2 = 0;
		//start with 5.
		sync(new Promise<Integer>(r -> r.resolve(5))
		//now a async function which needs 2 seconds. 5 * 2 = 10.
		.thenAsync(value -> new Promise<Integer>(r -> new Timer().schedule(new TimerTask() {
			@Override public void run() {
				r.resolve(value * 2);
			}
		}, 1000)))
		//now a sync function. 10 + 3 = 13.
		.thenSync(value -> value + 3)
		//now another async function which needs only 500 ms. 13 * 3 = 39.
		.thenAsync(value -> new Promise<Integer>(r -> new Timer().schedule(new TimerTask() {
			@Override public void run() {
				r.resolve(value * 3);
			}
		}, 500)))
		//now save the result
		.thenDo(value -> result1 = value));
		assertEquals(39, result1);
	}

	/**
	 * The same test as {@link #testOldStyle()}, but much more concise using Java 8 lambdas.
	 */
	@Test public void testLambdaStyle()
			throws Exception{
		result1 = 0;
		result2 = 0;
		//start with 5.
		sync(new Promise<Integer>(r -> r.resolve(5))
		//now a async function which needs 2 seconds. 5 * 2 = 10.
		.thenAsync(v -> new Promise<Integer>(r ->
			new Timer().schedule(new TimerTask() {
				@Override public void run() {
					r.resolve(v * 2);
				}
			},1000)))
		//now a sync function. 10 + 3 = 13.
		.thenSync(v -> v + 3)
		//now another async function which needs only 500 ms. 13 * 3 = 39.
		.thenAsync(v -> new Promise<Integer>(r ->
			new Timer().schedule(new TimerTask() {
				@Override public void run() {
					r.resolve(v * 3);
				};
			}, 500)))
		//now save the result
		.thenDo(v -> result1 = v));
		assertEquals(39, result1);
	}

	@Test public void testFail()
			throws Exception {
		result1 = 0;
		result2 = 0;
		try {
			sync(new Promise<Integer>(r -> r.resolve(4))
				.thenSync(v -> v / 0) //div/0 error
				.thenSync(v -> { result1 = 10; return 20; }) //may not be executed
				.thenDo(v -> result2 = v) //may not be executed
				.onError(e -> result1 = 1)); //must be executed
		} catch (Exception ex) {
		}
		assertEquals(1, result1);
		assertEquals(0, result2);
	}


	@Test public void testCalc()
			throws Exception {
		result1 = 0;
		sync(calc(1, Plus, 3)
				.thenAsync(v -> calc(v, Div, 2))
				.thenAsync(v -> calc(v, Plus, 4))
				.thenDo(v -> result1 = v));
		assertEquals(6, result1);
	}

	@Test public void testCalcFail()
			throws Exception {
		result1 = 0;
		result2 = 0;
		try {
			sync(calc(1, Plus, 3)
				.thenAsync(
						v -> calc(v, Div, 0)) //div/0 error
				.thenAsync(
						v -> calc(v, Plus, 4))
				.thenDo(
						v -> result1 = v)
				.onError(
						e -> result2 = 1)); //must be executed
		} catch (Exception ex) {
		}
		assertEquals(0, result1);
		assertEquals(1, result2);
	}


}
