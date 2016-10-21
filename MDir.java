/*
 * This program implements the modified Dirichlet distribution and its mode
 * finding algorithm as described in:
 * 
 * Kewei Tu, "Modified Dirichlet Distribution: Allowing Negative Parameters to
 * Induce Stronger Sparsity". In the Conference on Empirical Methods in Natural
 * Language Processing (EMNLP 2016).
 * 
 * If you use this code in your research, please cite the publication.
 */

package cn.edu.shanghaitech.ai.mdir;

/**
 * Modified Dirichlet distribution. It is an extension of the Dirichlet
 * distribution that allows the parameters to be negative. It not only induces
 * much stronger sparsity, but also simultaneously performs smoothing. It is
 * still conjugate to the multinomial distribution.
 * 
 * @author Kewei Tu
 * 
 */
public class MDir {
	protected int dim;
	/**
	 * c_i = alpha_i - 1
	 */
	protected double[] c;
	protected double epsilon;

	protected double[] mode;

	public MDir(int dim, double[] alpha, double epsilon) {
		this.dim = dim;
		c = new double[dim];
		if (alpha != null)
			setAlpha(alpha);

		double maxEps = 1.0 / dim;
		if (epsilon > maxEps) {
			System.err
					.println("[mDir Warning] MDir(): espilon is too large, reset to the max value.");
			this.epsilon = maxEps;
		} else if (epsilon < 0) {
			System.err
					.println("[mDir Warning] MDir(): espilon is negative, reset to 0.");
			this.epsilon = 0;
		} else
			this.epsilon = epsilon;
	}

	public void setAlpha(double[] alpha) {
		for (int i = 0; i < dim; i++) {
			c[i] = alpha[i] - 1;
		}
		mode = null;
	}

	public double[] getMode() {
		if (mode != null)
			return mode;

		mode = new double[dim];
		double sum = 0;
		int nEps = 0;
		for (int i = 0; i < dim; i++) {
			if (c[i] > 0) {
				mode[i] = c[i];
				sum += mode[i];
			} else {
				mode[i] = -1;
				nEps++;
			}
		}

		// if c<=0 for all i
		if (nEps == dim) {
			int iMax = 0;
			double max = c[0];
			mode[0] = epsilon;
			boolean multiModes = false;
			for (int i = 1; i < dim; i++) {
				if (c[i] > max) {
					max = c[i];
					iMax = i;
					multiModes = false;
				} else if (c[i] == max)
					multiModes = true;
				mode[i] = epsilon;
			}
			mode[iMax] = 1 - epsilon * (dim - 1);

			// warn if there are multiple equivalent modes
			if (multiModes)
				System.err
						.println("[mDir Warning] getMode(): multiple modes, returning one of them.");

			return mode;
		}

		boolean done;
		do {
			double x = (1 - epsilon * nEps) / sum;
			sum = 0;
			done = true;
			for (int i = 0; i < dim; i++) {
				if (mode[i] != -1) {
					mode[i] *= x;
					if (mode[i] < epsilon) {
						mode[i] = -1;
						nEps++;
						done = false;
					} else {
						sum += mode[i];
					}
				}
			}
		} while (!done);

		for (int i = 0; i < dim; i++) {
			if (mode[i] == -1)
				mode[i] = epsilon;
		}

		return mode;
	}

	/**
	 * @return the unnormalized log probability at the mode
	 */
	public double getLogProbAtMode() {
		if (mode == null)
			getMode();
		double logProbAtMode = 0;
		for (int i = 0; i < dim; i++) {
			logProbAtMode += c[i] * Math.log(mode[i]);
		}
		return logProbAtMode;
	}

	/**
	 * @param point
	 * @return the unnormalized log probability at the point
	 */
	public double getLogProb(double[] point) {
		double logProbAtMode = 0;
		for (int i = 0; i < dim; i++) {
			if (c[i] != 0 || point[i] != 0)
				logProbAtMode += c[i] * Math.log(point[i]);
			// otherwise, it is 0 * log 0, which should be 0
		}
		return logProbAtMode;
	}

}
