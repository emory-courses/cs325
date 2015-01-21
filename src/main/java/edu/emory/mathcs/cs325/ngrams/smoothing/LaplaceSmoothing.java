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
package edu.emory.mathcs.cs325.ngrams.smoothing;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.emory.mathcs.cs325.ngrams.Bigram;
import edu.emory.mathcs.cs325.ngrams.Unigram;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class LaplaceSmoothing implements ISmoothing
{
	protected double d_unseenLikelihood;
	protected double d_alpha;
	
	/**
	 * Initializes the prior of Laplace smoothing.
	 * @param alpha the prior.
	 */
	public LaplaceSmoothing(double alpha)
	{
		d_alpha = alpha;
	}
	
	@Override
	public void estimateMaximumLikelihoods(Unigram unigram)
	{
		Map<String,Long> countMap = unigram.getCountMap();
		double t = d_alpha * countMap.size() + unigram.getTotalCount();
		Map<String,Double> map = new HashMap<>(countMap.size());
		
		for (Entry<String,Long> entry : countMap.entrySet())
			map.put(entry.getKey(), (d_alpha + entry.getValue()) / t);

		unigram.setLikelihoodMap(map);
		d_unseenLikelihood = d_alpha / t;
	}
	
	@Override
	public void estimateMaximumLikelihoods(Bigram bigram)
	{
		Map<String,Unigram> unigramMap = bigram.getUnigramMap();
		
		for (Unigram unigram : unigramMap.values())
			unigram.estimateMaximumLikelihoods();
		
		Set<String> allWords = bigram.getAllWords();
		d_unseenLikelihood = d_alpha / (d_alpha * allWords.size());
	}
	
	@Override
	public double getUnseenLikelihood()
	{
		return d_unseenLikelihood;
	}

	@Override
	public ISmoothing createInstance()
	{
		return new LaplaceSmoothing(d_alpha);
	}
}
