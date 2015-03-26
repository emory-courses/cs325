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

import edu.emory.mathcs.cs325.utils.IntDoublePair;



/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class AbstractMultiPerceptron extends AbstractPerceptron
{
	protected int label_size;
	
	public AbstractMultiPerceptron(double alpha, int featureSize, int labelSize)
	{
		super(alpha);
		label_size    = labelSize;
		weight_vector = new double[featureSize * labelSize];
	}
	
	public AbstractMultiPerceptron(double alpha, int featureSize, int labelSize, boolean average)
	{
		super(alpha);
		label_size    = labelSize;
		weight_vector = new double[featureSize * labelSize];
		if (average) average_vector = new double[featureSize * labelSize];
	}
	
	@Override
	public IntDoublePair decode(int[] x)
	{
		return getBest(yhats(x));
	}
	
	protected IntDoublePair getBest(double[] yhats)
	{
		int max = 0;
		
		for (int i=1; i<label_size; i++)
		{
			if (yhats[max] < yhats[i])
				max = i;
		}
		
		return new IntDoublePair(max, yhats[max]);
	}
	
	protected double[] yhats(int[] x)
	{
		double[] d = new double[label_size];
		int i;
		
		for (int j : x)
			for (i=0; i<label_size; i++)
				d[i] += weight_vector[getWeightIndex(j, i)];

		return d;
	}
	
	protected int I(int y, int i)
	{
		return (y == i) ? 1 : -1;
	}
	
	protected void update(int[] x, int y, double delta)
	{
		for (int j : x)
			weight_vector[getWeightIndex(j, y)] += delta;
	}
	
	protected void updateAverage(int[] x, int y, double delta)
	{
		for (int j : x)
			average_vector[getWeightIndex(j, y)] += delta;
	}
	
	protected int getWeightIndex(int j, int i)
	{
		return label_size * j + i;
	}
}
