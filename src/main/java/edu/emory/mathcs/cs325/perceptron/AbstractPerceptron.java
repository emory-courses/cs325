/**
 * Copyright 2015, Emory University
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.emory.mathcs.cs325.perceptron;

import java.util.List;

import edu.emory.mathcs.cs325.utils.IntDoublePair;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class AbstractPerceptron
{
	protected double[] average_vector;
	protected double[] weight_vector;
	protected double   alpha;
	
	public AbstractPerceptron(double alpha)
	{
		this.alpha = alpha;
	}
	
	/**
	 * @return the predicated label.
	 * @param x feature indices.
	 * @param y true class label.
	 */
	public abstract int train(int[] x, int y);
	
	/**
	 * @return the predicated label.
	 * @param x feature indices.
	 * @param y true class label.
	 * @param s averaging step.
	 */
	public abstract int train(int[] x, int y, int s);
	
	/**
	 * @param x feature indices.
	 * @return the best predicted label and score.
	 */
	public abstract IntDoublePair decode(int[] x);
	
	protected int I(double y)
	{
		return (y >= 0) ? 1 : -1;
	}
	
	public void train(final int M, List<int[]> xs, List<Integer> ys)
	{
		boolean average = average_vector != null;
		final int T = xs.size();
		int m, t, s = M * T;
		
		for (m=0; m<M; m++)
			for (t=0; t<T; t++)
			{
				if (average) train(xs.get(t), ys.get(t));
				else		 train(xs.get(t), ys.get(t), s--);
			}
		
		System.arraycopy(average_vector, 0, weight_vector, 0, weight_vector.length);
	}
}
