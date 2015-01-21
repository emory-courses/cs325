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

import edu.emory.mathcs.cs325.ngrams.Bigram;
import edu.emory.mathcs.cs325.ngrams.Unigram;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public interface ISmoothing
{
	/**
	 * Estimates the maximum likelihoods of all words in {@code unigram}.
	 * @param unigram the unigram consisting of words and their counts. 
	 */
	void estimateMaximumLikelihoods(Unigram unigram);
	
	/**
	 * Estimates the maximum likelihoods of all words in {@code bigram}.
	 * @param bigram the bigram consisting of words and their counts. 
	 */
	void estimateMaximumLikelihoods(Bigram bigram);

	/** @return the likelihood of unseen word. */
	double getUnseenLikelihood();

	/** @return a new smoothing instance. */
	ISmoothing createInstance();
}
