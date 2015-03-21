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
public class BinaryPerceptron extends AbstractPerceptron
{
	public BinaryPerceptron(double alpha, int featureSize)
	{
		super(alpha);
		weight_vector = new double[featureSize];
	}

	@Override
	public void train(int[] x, int y)
	{
		double delta = alpha * (y - decode(x));
		update(x, delta);
	}
	
	@Override
	public int decode(int[] x)
	{
		return I(yhat(x));
	}
	
	double yhat(int[] x)
	{
		double sum = 0;
		
		for (int xj : x)
			sum += weight_vector[xj];
		
		return sum;
	}
	
	int I(double y)
	{
		return (y >= 0) ? 1 : -1;
	}
	
	void update(int[] x, double delta)
	{
		for (int xj : x)
			weight_vector[xj] *= delta;
	}
}
