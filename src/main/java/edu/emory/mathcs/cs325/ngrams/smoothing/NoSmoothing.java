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

import java.util.Map;
import java.util.stream.Collectors;

import edu.emory.mathcs.cs325.ngrams.Bigram;
import edu.emory.mathcs.cs325.ngrams.Unigram;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class NoSmoothing implements ISmoothing
{
	@Override
	public double getUnseenLikelihood()
	{
		return 0;
	}

	@Override
	public void estimateMaximumLikelihoods(Unigram unigram)
	{
		Map<String,Long> countMap = unigram.getCountMap();
		Map<String,Double> map = countMap.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> (double)entry.getValue()/unigram.getTotalCount()));
		unigram.setLikelihoodMap(map);
	}
	
	@Override
	public void estimateMaximumLikelihoods(Bigram bigram)
	{
		Map<String,Unigram> unigramMap = bigram.getUnigramMap();
		unigramMap.values().stream().forEach(unigram -> unigram.estimateMaximumLikelihoods());
	}

	@Override
	public ISmoothing createInstance()
	{
		return new NoSmoothing();
	}
}
