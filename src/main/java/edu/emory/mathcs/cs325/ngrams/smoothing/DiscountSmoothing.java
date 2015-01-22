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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import edu.emory.mathcs.cs325.ngrams.Bigram;
import edu.emory.mathcs.cs325.ngrams.Unigram;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class DiscountSmoothing extends LaplaceSmoothing
{
	/**
	 * Initializes the prior of discount smoothing.
	 * @param alpha the discount rate (<= 1).
	 */
	public DiscountSmoothing(double alpha)
	{
		super(alpha);
	}
	
	@Override
	public void estimateMaximumLikelihoods(Unigram unigram)
	{
		Map<String,Long> countMap = unigram.getCountMap();
		int size = countMap.size();
		
		d_unseenLikelihood = d_alpha * Collections.min(countMap.values());
		Map<String,Double> map = new HashMap<>(size);
		double d = d_unseenLikelihood / size;
		
		for (Entry<String,Long> entry : countMap.entrySet())
			map.put(entry.getKey(), (entry.getValue() / size) - d);

		unigram.setLikelihoodMap(map);
	}
	
	@Override
	public void estimateMaximumLikelihoods(Bigram bigram)
	{
		Map<String,Unigram> unigramMap = bigram.getUnigramMap();
		
		for (Unigram unigram : unigramMap.values())
			unigram.estimateMaximumLikelihoods();
		
		d_unseenLikelihood =  d_alpha * getMin(unigramMap);
	}
	
	private double getMin(Map<String,Unigram> unigramMap)
	{
		double min = Double.MAX_VALUE;
		
		for (Unigram unigram : unigramMap.values())
			min = Math.min(min, unigram.getLikelihood(null));
		
		return min;
	}

	@Override
	public ISmoothing createInstance()
	{
		return new DiscountSmoothing(d_alpha);
	}
}
