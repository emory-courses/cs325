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



/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class MultiPerceptron extends AbstractPerceptron
{
	private int feature_size;
	private int label_size;
	
	public MultiPerceptron(double alpha, int featureSize, int labelSize)
	{
		super(alpha);
		weight_vector = new double[featureSize*labelSize];
		feature_size  = featureSize;
		label_size    = labelSize;
	}

	@Override
	public void train(int[] x, int y)
	{
		double delta = alpha * (y - decode(x));
		update(x, y, delta);
	}
	
	@Override
	public int decode(int[] x)
	{
		double[] ys = yhats(x);
		int max = 0;
		
		for (int i=1; i<label_size; i++)
		{
			if (ys[max] < ys[i])
				max = i;
		}
		
		return max;
	}
	
	/** Row-based. */
	double[] yhats(int[] x)
	{
		double[] sum = new double[label_size];
		
		for (int y=0; y<label_size; y++)
			for (int xj : x)
				sum[y] += weight_vector[getIndex(xj, y)];
		
		return sum;
	}
	
	void update(int[] x, int y, double delta)
	{
		for (int xj : x)
			weight_vector[getIndex(xj, y)] *= delta;
	}
	
	int getIndex(int xj, int y)
	{
		return y * feature_size + xj;
	}
}
