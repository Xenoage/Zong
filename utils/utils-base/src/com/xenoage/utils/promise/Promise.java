package com.xenoage.utils.promise;

/**
 * TODO: Experimental.
 *
 * This class is very tedious to use with old Java style (anonymous classes).
 * Instead, use the lambda style. See the test cases for this class for
 * some useful examples.
 *
 * @author Andreas Wenger
 */
public class Promise<T> {

	private static class Handler<T, R> {
		Function<Object, R> onResolved;
		Consumer<Exception> onRejected;
		Return<Object> ret;

		Handler(Function<Object, R> onResolved, Consumer<Exception> onRejected, Return<Object> ret) {
			this.onResolved = onResolved;
			this.onRejected = onRejected;
			this.ret = ret;
		}
	}

	private State state = State.Pending;
	private Object value;
	//asynchronous callback, if the value can not be resolved immediately
	private Handler<T, Object> deferred = null; //TODO: list, instead of only the last registered one


	public Promise(Executor<T> exec) {
		final Promise<T> self = this;
		exec.run(new Return<T>() {
			@Override public void resolve(T result) {
				self.resolve(result);
			}
			@Override public void reject(Exception error) {
				self.reject(error);
			}
		});
	}

	private synchronized void resolve(Object value) {
		if (value instanceof Promise) {
			//resolve promise
			Promise p = ((Promise) value);
			p.thenDo(this::resolve).onError(e -> reject((Exception) e));
		}
		else {
			//plain value
			this.state = State.Resolved;
			this.value = value;
			if (deferred != null) {
				handle(deferred);
			}
		}
	}

	private synchronized void reject(Exception error) {
		this.state = State.Rejected;
		this.value = error;
		if (deferred != null) {
			handle(deferred);
		}
	}

	private <R> void handle(Handler<T, R> handler) {
		if (state == State.Pending) {
			//executor not finished yet; remember handler
			deferred = (Handler) handler;
		}
		else if (state == State.Resolved) {
			//resolved
			if (handler.onResolved == null) {
				handler.ret.resolve((T) value);
			}
			else {
				try {
					Object ret = handler.onResolved.run(value);
					handler.ret.resolve(ret);
				}
				catch (Exception error) {
					handler.ret.reject(error);
				}
			}
		}
		else {
			//rejected
			if (handler.onRejected == null) {
				handler.ret.reject((Exception) value);
			}
			else {
				try {
					handler.onRejected.run((Exception) value);
					handler.ret.resolve(null);
				} catch (Exception error) {
					handler.ret.resolve(error);
				}
			}
		}
	}

	/**
	 * Returns a promise for the result of the given synchronous function
	 * applied to the value of this promise.
	 */
	public synchronized <R> Promise<R> thenSync(final Function<T, R> onResolved) {
		return thenInternal((Function) onResolved);
	}

	/**
	 * Returns a promise for the result of the given asynchronous function
	 * applied to the value of this promise.
	 */
	public synchronized <R> Promise<R> thenAsync(final Function<T, Promise<R>> onResolved) {
		return thenInternal((Function) onResolved);
	}

	/**
	 * Calls the given consumer, when this promise is resolved, and returns this promise for further use.
	 */
	public synchronized Promise<T> thenDo(final Consumer<T> onResolved) {
		return thenInternal(Utils.toFunction(onResolved));
	}

	private <R> Promise<R> thenInternal(final Function<T, Object> onResolved) {
		return new Promise<>(r -> handle(
			new Handler<>((Function) onResolved, null, (Return) r)));
	}

	/**
	 * Calls the given consumer, when this promise is rejected, and returns this promise for further use.
	 */
	public synchronized Promise<T> onError(final Consumer<Exception> onRejected) {
		return new Promise<>(r -> handle(
			(Handler) new Handler<>(null, onRejected, (Return) r)));
	}



}
