package com.xenoage.utils.kernel;

/**
 * This class combines three instances of three given classes
 * to one instance.
 * 
 * This is especially useful for multiple return values.
 * 
 * @author Andreas Wenger
 */
public class Tuple3<T1, T2, T3> {

	private final T1 e1;
	private final T2 e2;
	private final T3 e3;


	public Tuple3(T1 e1, T2 e2, T3 e3) {
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
	}

	public static <P1, P2, P3> Tuple3<P1, P2, P3> t3(P1 e1, P2 e2, P3 e3) {
		return new Tuple3<>(e1, e2, e3);
	}

	public T1 get1() {
		return e1;
	}

	public T2 get2() {
		return e2;
	}

	public T3 get3() {
		return e3;
	}

	@Override public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((e1 == null) ? 0 : e1.hashCode());
		result = prime * result + ((e2 == null) ? 0 : e2.hashCode());
		result = prime * result + ((e3 == null) ? 0 : e3.hashCode());
		return result;
	}

	@Override public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tuple3<?, ?, ?> other = (Tuple3<?, ?, ?>) obj;
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
		if (e3 == null) {
			if (other.e3 != null)
				return false;
		}
		else if (!e3.equals(other.e3))
			return false;
		return true;
	}

	@Override public String toString() {
		return "(" + e1 + ", " + e2 + ", " + e3 + ")";
	}

}
