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
public class NgramTest
{
	@Test
	public void testUnigram()
	{
		Unigram unigram = new Unigram(new NoSmoothing());
		
		unigram.add("A", 1);
		unigram.add("B", 2);
		unigram.add("B", 4);
		unigram.add("C", 3);
		unigram.add("C", 6);
		unigram.add("D", 4);
		unigram.resetMLEs();

		assertEquals(unigram.getMLE("A"), 0.05, 0);
		assertEquals(unigram.getMLE("B"), 0.3 , 0);
		assertEquals(unigram.getMLE("C"), 0.45, 0);
		assertEquals(unigram.getMLE("D"), 0.2 , 0);
		assertEquals(unigram.getMLE("E"), 0   , 0);
		
		StringDoublePair p = unigram.getBest();

		assertEquals(p.getString(), "C");
		assertEquals(p.getDouble(), 0.45, 0);
	}
	
	@Test
	public void testBigram()
	{
		Bigram bigram = new Bigram(new NoSmoothing());
		
		bigram.add("A", "A1", 2);
		bigram.add("A", "A2", 3);
		bigram.add("B", "B1", 1);
		bigram.add("B", "B2", 4);
		bigram.resetMLEs();

		assertEquals(bigram.getMLE("A","A1"), 0.4, 0);
		assertEquals(bigram.getMLE("A","A2"), 0.6, 0);
		assertEquals(bigram.getMLE("B","B1"), 0.2, 0);;
		assertEquals(bigram.getMLE("B","B2"), 0.8, 0);
		assertEquals(bigram.getMLE("A","A0"),   0, 0);
		assertEquals(bigram.getMLE("C","A1"),   0, 0);
		
		StringDoublePair p;
		
		p = bigram.getBest("A");
		assertEquals(p.getString(), "A2");
		assertEquals(p.getDouble(), 0.6, 0);
		
		p = bigram.getBest("B");
		assertEquals(p.getString(), "B2");
		assertEquals(p.getDouble(), 0.8, 0);
	}
}
