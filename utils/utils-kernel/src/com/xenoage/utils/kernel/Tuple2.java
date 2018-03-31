package com.xenoage.utils.kernel;

/**
 * This class combines two instances of two given classes
 * to one instance.
 * 
 * This is especially useful for multiple return values.
 * 
 * @author Andreas Wenger
 */
public final class Tuple2<T1, T2> {

	private final T1 e1;
	private final T2 e2;


	public Tuple2(T1 e1, T2 e2) {
		this.e1 = e1;
		this.e2 = e2;
	}

	public static <T1, T2> Tuple2<T1, T2> t(T1 e1, T2 e2) {
		return new Tuple2<>(e1, e2);
	}

	public T1 get1() {
		return e1;
	}

	public T2 get2() {
		return e2;
	}

	@Override public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((e1 == null) ? 0 : e1.hashCode());
		result = prime * result + ((e2 == null) ? 0 : e2.hashCode());
		return result;
	}

	@Override public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tuple2<?, ?> other = (Tuple2<?, ?>) obj;
		if (e1 == null) {
			if (other.e1 != null)
				return false;
		}
		else if (!e1.equals(other.e1))
			return false;
		if (e2 == null) {
			if (other.e2 != null)
				return false;
		}
		else if (!e2.equals(other.e2))
			return false;
		return true;
	}

	@Override public String toString() {
		return "(" + e1 + ", " + e2 + ")";
	}

}
