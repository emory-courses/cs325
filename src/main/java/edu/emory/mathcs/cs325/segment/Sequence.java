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
package edu.emory.mathcs.cs325.segment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Sequence implements Comparable<Sequence>
{
	private List<String> l_words;
	private double d_total;
	
	public Sequence()
	{
		l_words = new ArrayList<>();
		d_total = 0;
	}
	
	public Sequence(Sequence sequence)
	{
		l_words = new ArrayList<>(sequence.l_words);
		d_total = sequence.d_total;
	}
	
	public void add(String word, double likelihood)
	{
		l_words.add(word);
		d_total += Math.log(likelihood);
	}
	
	public String getPreviousWord()
	{
		return l_words.isEmpty() ? null : l_words.get(l_words.size()-1);
	}
	
	public List<String> getSequence()
	{
		return l_words;
	}
	
	public double getMaximumLikelihood()
	{
		return d_total / l_words.size();
	}
	
	@Override
	public int compareTo(Sequence o)
	{
		return (int)Math.signum(getMaximumLikelihood() - o.getMaximumLikelihood()); 
	}
}