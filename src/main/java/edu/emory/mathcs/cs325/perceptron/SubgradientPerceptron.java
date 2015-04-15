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
public class SubgradientPerceptron extends AbstractMultiPerceptron
{
	public SubgradientPerceptron(double alpha, int featureSize, int labelSize)
	{
		super(alpha, featureSize, labelSize);
	}
	
	public SubgradientPerceptron(double alpha, int featureSize, int labelSize, boolean average)
	{
		super(alpha, featureSize, labelSize, average);
	}

	@Override
	public int train(int[] x, int y)
	{
		double[] yhats = yhats(x);
		yhats[y] -= 1;
		int argmax = getBest(yhats).getInt();
//		int argmax = decode(x).getInt();
		
		if (y != argmax)
		{
			double delta = alpha;
			update(x, y, delta);
			update(x, argmax, -delta);
		}
		
		return argmax;
	}
	
	@Override
	public int train(int[] x, int y, int s)
	{
		int argmax = train(x, y);
		
		if (y != argmax)
		{
			double delta = alpha * s;
			updateAverage(x, y, delta);
			updateAverage(x, argmax, -delta);
		}
		
		return argmax;
	}	
}
