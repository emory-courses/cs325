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

import edu.emory.mathcs.cs325.ngrams.smoothing.NoSmoothing;
import edu.emory.mathcs.cs325.utils.StringDoublePair;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class UnigramTest
{
	@Test
	public void test()
	{
		Unigram unigram = new Unigram(new NoSmoothing());
		
		unigram.add("A", 1);
		unigram.add("B", 2);
		unigram.add("B", 4);
		unigram.add("C", 3);
		unigram.add("C", 6);
		unigram.add("D", 4);
		unigram.resetLikelihoods();

		assertEquals(unigram.getLikelihood("A"), 0.05, 0);
		assertEquals(unigram.getLikelihood("B"), 0.3 , 0);
		assertEquals(unigram.getLikelihood("C"), 0.45, 0);
		assertEquals(unigram.getLikelihood("D"), 0.2 , 0);
		assertEquals(unigram.getLikelihood("E"), 0   , 0);
		
		StringDoublePair p = unigram.getMaximumLikelihood();

		assertEquals(p.getString(), "C");
		assertEquals(p.getDouble(), 0.45, 0);
	}
}
