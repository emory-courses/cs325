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

import edu.emory.mathcs.cs325.ngrams.Bigram;
import edu.emory.mathcs.cs325.ngrams.Unigram;


/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Interpolation implements ILanguageModel
{
	private Unigram n_1gram;
	private Bigram  n_2gram;
	private double  d_1gramWeight;
	private double  d_2gramWeight;
	
	/**
	 * @param unigram the unigram with smoothing.
	 * @param bigram the bigram with smoothing.
	 * @param unigramWeight the unigram weight.
	 * @param bigramWeight the bigram weight.
	 */
	public Interpolation(Unigram unigram, Bigram bigram, int unigramWeight, int bigramWeight)
	{
		n_1gram = unigram;
		n_2gram = bigram;
		d_1gramWeight = unigramWeight;
		d_2gramWeight = bigramWeight;
	}
	
	@Override
	public double getLikelihood(String word1, String word2)
	{
		return d_1gramWeight * n_1gram.getLikelihood(word2) + d_2gramWeight * n_2gram.getLikelihood(word1, word2);
	}
}
