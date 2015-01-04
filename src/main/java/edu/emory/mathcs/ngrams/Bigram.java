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
package edu.emory.mathcs.ngrams;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Bigram
{
	Map<String,Unigram> m_probs;
	
	public Bigram()
	{
		m_probs = new HashMap<>();
	}
	
	public void add(String word1, String word2, int count)
	{
		Unigram unigram = m_probs.get(word1);
		
		if (unigram == null)
		{
			unigram = new Unigram();
			m_probs.put(word1, unigram);
		}
		
		unigram.add(word2, count);
	}
	
	public double get(String word1, String word2)
	{
		Unigram unigram = m_probs.get(word1);
		return (unigram != null) ? unigram.get(word2) : 0d;
	}

	public void finalize()
	{
		for (Unigram unigram : m_probs.values())
			unigram.finalize();
	}
}
