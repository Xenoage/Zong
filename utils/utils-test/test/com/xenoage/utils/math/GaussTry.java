package com.xenoage.utils.math;

import com.xenoage.utils.math.Gauss;

public class GaussTry {

	public static void main(String... args) {
		long time = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++)
			Gauss.solve(new double[][] { { 1, 2, 3 }, { 2, 5, 7 }, { 8, 2, 1 } },
					1, 7, 3);
		System.out.println(System.currentTimeMillis() - time);
	}

}
