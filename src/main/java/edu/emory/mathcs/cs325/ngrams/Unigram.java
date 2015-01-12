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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.emory.mathcs.cs325.ngrams.smoothing.ISmoothing;
import edu.emory.mathcs.cs325.utils.StringDoublePair;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Unigram
{
	private Map<String,Double> m_likelihoods;
	private Map<String,Long>   m_counts;
	private long               t_counts;
	private ISmoothing         i_smoothing;
	
	public Unigram(ISmoothing smoothing)
	{
		m_counts = new HashMap<>();
		t_counts = 0;
		i_smoothing = smoothing;
	}
	
	/**
	 * Increments the {@code count} of the {@code word}.
	 * @param word  the word to be added.
	 * @param count the count to be incremented.
	 */
	public void add(String word, long count)
	{
		m_counts.compute(word, (k,v) -> (v == null) ? count : v + count);
		t_counts += count;
	}
	
	/**
	 * @return the MLE of the {@code word}.
	 * @param word the word to get the MLE for.
	 */
	public double getMLE(String word)
	{
		Double d = m_likelihoods.get(word);
		return (d != null) ? d : i_smoothing.getUnseenLikelihood();
	}
	
	/** Resets all MLEs using {@link #m_counts} and {@link #t_counts}. */
	public void resetMLEs()
	{
		m_likelihoods = i_smoothing.getProbabilityMap(m_counts, t_counts);
	}
	
	/**
	 * @return the (word, MLE) pair whose likelihood is the highest if exists; otherwise, {@code null}.
	 * @see StringDoublePair
	 */
	public StringDoublePair getBest()
	{
		if (m_likelihoods.isEmpty()) return null;
		StringDoublePair p = new StringDoublePair(null, -1);
		
		for (Entry<String,Double> entry : m_likelihoods.entrySet())
		{
			if (p.getDouble() < entry.getValue())
				p.set(entry.getKey(), entry.getValue());
		}
		
		return p;
	}
	
	/** @return the list of (word, MLE) pairs sorted in descending order.  */
	public List<StringDoublePair> toSortedList() 
	{
		List<StringDoublePair> list = new ArrayList<>(m_likelihoods.size());
		
		for (Entry<String,Double> entry : m_likelihoods.entrySet())
			list.add(new StringDoublePair(entry.getKey(), entry.getValue()));
		
		Collections.sort(list, Collections.reverseOrder());
		return list;
	}
}
