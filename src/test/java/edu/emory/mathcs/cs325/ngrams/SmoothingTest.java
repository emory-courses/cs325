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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.emory.mathcs.cs325.ngrams.smoothing.LaplaceSmoothing;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class SmoothingTest
{
	@Test
	public void testUnigramLaplace()
	{
		Unigram unigram = new Unigram(new LaplaceSmoothing(1));
		
		unigram.add("A", 1);
		unigram.add("B", 2);
		unigram.add("C", 4);
		unigram.estimateMaximumLikelihoods();

		assertEquals(0.2, unigram.getLikelihood("A"), 0);
		assertEquals(0.3, unigram.getLikelihood("B"), 0);
		assertEquals(0.5, unigram.getLikelihood("C"), 0);
		assertEquals(0.1, unigram.getLikelihood("D"), 0);
	}
	
	@Test
	public void testBigramLaplace()
	{
		Bigram bigram = new Bigram(new LaplaceSmoothing(1));
		
		bigram.add("A", "A1", 1);
		bigram.add("A", "A2", 2);
		bigram.add("B", "B1", 5);
		bigram.add("B", "B2", 3);
		bigram.estimateMaximumLikelihoods();

		assertEquals(0.4, bigram.getLikelihood("A","A1"), 0);
		assertEquals(0.6, bigram.getLikelihood("A","A2"), 0);
		assertEquals(0.6, bigram.getLikelihood("B","B1"), 0);
		assertEquals(0.4, bigram.getLikelihood("B","B2"), 0);
		assertEquals(0.2, bigram.getLikelihood("A","A0"), 0);
		assertEquals(0.1, bigram.getLikelihood("B","B0"), 0);
		assertEquals(0  , bigram.getLikelihood("C","A1"), 0);
	}
}
