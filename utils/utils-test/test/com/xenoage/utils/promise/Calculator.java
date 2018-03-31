package com.xenoage.utils.promise;

import static com.xenoage.utils.promise.Calculator.Op.Div;
import static com.xenoage.utils.promise.Calculator.Op.Plus;

/**
 * A helper class for {@link PromiseTest}, performing simple calculations.
 *
 * @author Andreas Wenger
 */
public class Calculator {

	public enum Op {
		Plus,
		Div;
	}

	/**
	 * Returns the result asynchronously.
	 */
	public static Promise<Integer> calc(int value1, Op op, int value2) {
		Promise<Integer> ret = new Promise<>(r -> {
			//Plus returns immediately
			if (op == Plus) {
				r.resolve(value1 + value2);
			}
			//Simulate more time with Div
			else if (op == Div) {
				//new Timer().schedule(new TimerTask() {
				//	@Override public void run() {
						if (value2 == 0)
							r.reject(new ArithmeticException());
						else
							r.resolve(value1 / value2);
				//	}
				//},500);
			}
			else {
				r.reject(new UnsupportedOperationException());
			}
		});
		return ret;
	}

}
