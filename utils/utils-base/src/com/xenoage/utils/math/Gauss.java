package com.xenoage.utils.math;

/**
 * This class allows to solve a system of linear equations.
 * It is based on the class developed in the scope of the lecture
 * "Numerisches Programmieren" at the TUM in the winter semester 07/08.
 * 
 * @author Andreas Wenger
 */
public class Gauss {

	/**
	 * Computes and returns the solution of the SLE <code>A * x = b</code> by
	 * using Gauss elimination with partial pivoting.
	 * @param A  invertible n * n matrix [row, column]
	 * @param b  vector with n elements
	 * @return  solution vector
	 */
	public static double[] solve(double[][] A, double... b) {
		int n = A.length;

		//create a triangular matrix column after column
		//j: current column, from where on all rows beginning with j+1 are set to 0
		for (int j = 0; j < n; j++) {
			//find maximum in the rows j to n-1
			int max = j;
			for (int i = j; i < n; i++) {
				if (Math.abs(A[i][j]) > Math.abs(A[max][j])) {
					max = i;
				}
			}
			//switch rows, if a better pivot was found
			if (max != j) {
				for (int k = 0; k < n; k++) {
					double swap = A[j][k];
					A[j][k] = A[max][k];
					A[max][k] = swap;
				}
				double swap = b[j];
				b[j] = b[max];
				b[max] = swap;
			}
			//divide the remaining rows by the current row
			for (int i = j + 1; i < n; i++) {
				double factor = A[i][j] / A[j][j];
				for (int k = j; k < n; k++) {
					A[i][k] = A[i][k] - factor * A[j][k];
				}
				b[i] = b[i] - factor * b[j];
			}
		}

		//backward substitution
		double[] rx = new double[n];
		double sum = 0;
		for (int i = n - 1; i >= 0; i--) {
			sum = 0;
			for (int iSum = n - 1; iSum > i; iSum--) {
				sum += A[i][iSum] * rx[iSum];
			}
			rx[i] = (b[i] - sum) / A[i][i];
		}

		return rx;
	}

}
