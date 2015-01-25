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
		m_counts.merge(word, count, (oldCount, newCount) -> oldCount + newCount);
		t_counts += count;
	}
	
	/** Estimates the maximum likelihoods of all unigrams. */
	public void estimateMaximumLikelihoods()
	{
		i_smoothing.estimateMaximumLikelihoods(this);
	}
	
	/**
	 * @return the likelihood of the {@code word}.
	 * @param word the word to get the likelihood for.
	 */
	public double getLikelihood(String word)
	{
		return m_likelihoods.getOrDefault(word, i_smoothing.getUnseenLikelihood());
	}
	
	/** @return the (word, likelihood) pair whose likelihood is the highest if exists; otherwise, {@code null}. */
	public Entry<String,Double> getBest()
	{
//		return m_likelihoods.entrySet().stream().max(Entry.comparingByValue()).orElse(null);
		return m_likelihoods.isEmpty() ? null : Collections.max(m_likelihoods.entrySet(), Entry.comparingByValue());
	}
	
	/** @return the list of (word, likelihood) pairs sorted in descending order.  */
	public List<Entry<String,Double>> toSortedList() 
	{
//		return m_likelihoods.entrySet().stream().sorted(Entry.comparingByValue(Collections.reverseOrder())).collect(Collectors.toList());
		List<Entry<String,Double>> list = new ArrayList<>(m_likelihoods.entrySet());
		Collections.sort(list, Entry.comparingByValue(Collections.reverseOrder()));
		return list;
	}
	
	/** @return the total count of all words. */
	public long getTotalCount()
	{
		return t_counts;
	}
	
	/** @return the map whose keys and values are the words and their counts. */
	public Map<String,Long> getCountMap()
	{
		return m_counts;
	}
	
	public Map<String,Double> getLikelihoodMap()
	{
		return m_likelihoods;
	}
	
	/** Assigns the likelihood map to this unigram. */
	public void setLikelihoodMap(Map<String,Double> map)
	{
		m_likelihoods = map;
	}
}
