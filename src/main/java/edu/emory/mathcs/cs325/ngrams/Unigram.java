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
package edu.emory.mathcs.cs325.ngrams;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Unigram
{
	private Map<String,Double> m_probs;
	private int n_total;
	
	public Unigram()
	{
		m_probs = new HashMap<>();
		n_total = 0;
	}
	
	public void add(String word, int count)
	{
		m_probs.compute(word, (k,v) -> (v == null) ? count : v+count);
		n_total += count;
	}
	
	public double get(String word)
	{
		Double d = m_probs.get(word);
		return (d != null) ? d : 0d;
	}
	
	public void finalize()
	{
		for (Entry<String,Double> entry : m_probs.entrySet())
			m_probs.compute(entry.getKey(), (k,v) -> v/n_total);		
	}
}
