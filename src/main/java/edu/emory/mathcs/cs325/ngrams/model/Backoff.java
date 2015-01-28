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
package edu.emory.mathcs.cs325.ngrams.model;

import java.util.Map;

import edu.emory.mathcs.cs325.ngrams.Bigram;
import edu.emory.mathcs.cs325.ngrams.Unigram;
import edu.emory.mathcs.cs325.utils.MathUtils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Backoff implements ILanguageModel
{
	private Unigram n_1gram;
	private Bigram  n_2gram;
	private double  d_1gramWeight;
	
	public Backoff(Unigram unigram, Bigram bigram, double alpha)
	{
		n_1gram = unigram;
		n_2gram = bigram;
		d_1gramWeight = getUnigramWeight(alpha);
	}
	
	private double getUnigramWeight(double alpha)
	{
		double uniAvg = MathUtils.average(n_1gram.getLikelihoodMap().values());
		Map<String,Double> map;
		double sum = 0;
		int total = 0;
		
		for (Unigram unigram : n_2gram.getUnigramMap().values())
		{
			map = unigram.getLikelihoodMap();
			sum += MathUtils.sum(map.values());
			total += map.size();
		}
		
		double biAvg = sum / total;
		return alpha * biAvg / uniAvg;
	}

	@Override
	public double getLikelihood(String word1, String word2)
	{
		return n_2gram.contains(word1, word2) ? n_2gram.getLikelihood(word1, word2) : d_1gramWeight * n_1gram.getLikelihood(word2);
	}
}
