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
package edu.emory.mathcs.cs325.classifier;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Prediction implements Comparable<Prediction>
{
	private String label;
	private double score;

	public Prediction(String label, double score)
	{
		setLabel(label);
		setScore(score);
	}
	
	public Prediction(Prediction p)
	{
		setLabel(p.getLabel());
		setScore(p.getScore());
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public double getScore()
	{
		return score;
	}
	
	public void setScore(double score)
	{
		this.score = score;
	}
	
	public void addScore(double score)
	{
		this.score += score;
	}

	@Override
	public int compareTo(Prediction o)
	{
		return (int)Math.signum(score - o.score);
	}
	
	@Override
	public String toString()
	{
		return label+":"+score;
	}
}
