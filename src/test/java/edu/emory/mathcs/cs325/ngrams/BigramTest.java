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
public class BigramTest
{
	@Test
	public void test()
	{
		Bigram bigram = new Bigram(new NoSmoothing());
		
		bigram.add("A", "A1", 2);
		bigram.add("A", "A2", 3);
		bigram.add("B", "B1", 1);
		bigram.add("B", "B2", 4);
		bigram.resetLikelihoods();

		assertEquals(bigram.getLikelihood("A","A1"), 0.4, 0);
		assertEquals(bigram.getLikelihood("A","A2"), 0.6, 0);
		assertEquals(bigram.getLikelihood("B","B1"), 0.2, 0);;
		assertEquals(bigram.getLikelihood("B","B2"), 0.8, 0);
		assertEquals(bigram.getLikelihood("A","A0"),   0, 0);
		assertEquals(bigram.getLikelihood("C","A1"),   0, 0);
		
		StringDoublePair p;
		
		p = bigram.getMaximumLikelihood("A");
		assertEquals(p.getString(), "A2");
		assertEquals(p.getDouble(), 0.6, 0);
		
		p = bigram.getMaximumLikelihood("B");
		assertEquals(p.getString(), "B2");
		assertEquals(p.getDouble(), 0.8, 0);
	}
}
