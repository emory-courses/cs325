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
public abstract class AbstractPerceptron
{
	protected double[] weight_vector;
	protected double alpha;
	
	public AbstractPerceptron(double alpha)
	{
		this.alpha = alpha;
	}
	
	/**
	 * @param x feature indices.
	 * @param y true class label.
	 */
	public abstract void train (int[] x, int y);
	
	/**
	 * @param x feature indices.
	 * @return the best predicted class label.
	 */
	public abstract int decode(int[] x);
}
