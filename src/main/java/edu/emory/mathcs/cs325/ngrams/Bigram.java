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
import java.util.List;
import java.util.Map;

import edu.emory.mathcs.cs325.ngrams.smoothing.ISmoothing;
import edu.emory.mathcs.cs325.utils.StringDoublePair;

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
		Unigram unigram = m_unigrams.get(word1);
		
		if (unigram == null)
		{
			unigram = new Unigram(i_smoothing.createInstance());
			m_unigrams.put(word1, unigram);
		}
		
		unigram.add(word2, count);
	}
	
	/**
	 * @return the MLE of the {@code word1} and {@code word2} sequence if exists; otherwise, {@code 0}.
	 * @param word1 the first word.
	 * @param word2 the second word following {@code word1}.
	 */
	public double getMLE(String word1, String word2)
	{
		Unigram unigram = m_unigrams.get(word1);
		return (unigram != null) ? unigram.getMLE(word2) : 0d;
	}
	
	/** Resets all MLEs of {@link #m_unigrams}. */
	public void resetMLEs()
	{
		for (Unigram unigram : m_unigrams.values())
			unigram.resetMLEs();
	}
	
	/**
	 * @return the (word, MLE) pair whose likelihood is the highest among words following {@code word} if exists; otherwise, {@code null}.
	 * @param word the first word.
	 */
	public StringDoublePair getBest(String word)
	{
		Unigram unigram = m_unigrams.get(word);
		return (unigram != null) ? unigram.getBest() : null;
	}
	
	/**
	 * @return the list of (word, MLE) pairs sorted in descending order whose words follow {@code word}.
	 * @param word the first word.
	 */
	public List<StringDoublePair> getSortedList(String word)
	{
		Unigram unigram = m_unigrams.get(word);
		return (unigram != null) ? unigram.toSortedList() : new ArrayList<>();
	}
}
