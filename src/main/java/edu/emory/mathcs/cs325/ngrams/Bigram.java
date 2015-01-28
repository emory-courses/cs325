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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.emory.mathcs.cs325.ngrams.smoothing.ISmoothing;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Bigram
{
	private Map<String,Unigram> m_unigrams;
	private ISmoothing i_smoothing;
	
	public Bigram(ISmoothing smoothing)
	{
		m_unigrams  = new HashMap<>();
		i_smoothing = smoothing;
	}
	
	/**
	 * Increments the {@code count} of the {@code word1} and {@code word2} sequence.
	 * @param word1 the first word.
	 * @param word2 the second word following {@code word1}. 
	 * @param count the count to be added.
	 */
	public void add(String word1, String word2, long count)
	{
		m_unigrams.computeIfAbsent(word1, key -> new Unigram(i_smoothing.createInstance())).add(word2, count);
	}
	
	/** Estimates the maximum likelihoods of all bigrams. */
	public void estimateMaximumLikelihoods()
	{
		i_smoothing.estimateMaximumLikelihoods(this);
	}
	
	/**
	 * @return the likelihood of the {@code word1} and {@code word2} sequence if exists; otherwise, {@code 0}.
	 * @param word1 the first word.
	 * @param word2 the second word following {@code word1}.
	 */
	public double getLikelihood(String word1, String word2)
	{
		Unigram unigram = m_unigrams.get(word1);
		return (unigram != null) ? unigram.getLikelihood(word2) : i_smoothing.getUnseenLikelihood();
	}
	
	/**
	 * @return the (word, likelihood) pair whose likelihood is the highest among words following {@code word} if exists; otherwise, {@code null}.
	 * @param word the first word.
	 */
	public Entry<String,Double> getBest(String word)
	{
		Unigram unigram = m_unigrams.get(word);
		return (unigram != null) ? unigram.getBest() : null;
	}
	
	/**
	 * @return the list of (word, likelihood) pairs sorted in descending order whose words follow {@code word}.
	 * @param word the first word.
	 */
	public List<Entry<String,Double>> getSortedList(String word)
	{
		Unigram unigram = m_unigrams.get(word);
		return (unigram != null) ? unigram.toSortedList() : new ArrayList<>();
	}
	
	/** @return the map whose keys and values are words and their unigram maps. */
	public Map<String,Unigram> getUnigramMap()
	{
		return m_unigrams;
	}
	
	public boolean contains(String word1, String word2)
	{
		Unigram unigram = m_unigrams.get(word1);
		return (unigram != null) && unigram.contains(word2); 
	}
	
	public Set<String> getWordSet()
	{
		Set<String> set = new HashSet<>();
		
		for (Entry<String,Unigram> entry : m_unigrams.entrySet())
		{
			set.add(entry.getKey());
			set.addAll(entry.getValue().getCountMap().keySet());
		}
		
		return set;
	}
}
