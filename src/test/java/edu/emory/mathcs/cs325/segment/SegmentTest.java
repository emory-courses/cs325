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
package edu.emory.mathcs.cs325.segment;

import java.io.FileInputStream;

import org.junit.Test;

import edu.emory.mathcs.cs325.ngrams.Bigram;
import edu.emory.mathcs.cs325.ngrams.Unigram;
import edu.emory.mathcs.cs325.ngrams.model.Backoff;
import edu.emory.mathcs.cs325.ngrams.model.ILanguageModel;
import edu.emory.mathcs.cs325.ngrams.smoothing.DiscountSmoothing;
import edu.emory.mathcs.cs325.ngrams.smoothing.NoSmoothing;
import edu.emory.mathcs.cs325.segment.AbstractSegment;
import edu.emory.mathcs.cs325.segment.Segment;
import edu.emory.mathcs.cs325.segment.Sequence;
import edu.emory.mathcs.cs325.utils.IOUtils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class SegmentTest
{
	@Test
	public void test() throws Exception
	{
		Unigram unigram = new Unigram(new DiscountSmoothing(0.9));
		Bigram bigram = new Bigram(new NoSmoothing());

		IOUtils.readUnigrams(unigram, new FileInputStream("/Users/jdchoi/Emory/courses/CS325/dat/1grams.txt"));
		IOUtils.readBigrams (bigram , new FileInputStream("/Users/jdchoi/Emory/courses/CS325/dat/2grams.txt"));
		
		double unigramWeight = 0.01;
		ILanguageModel model = new Backoff(unigram, bigram, unigramWeight);
		AbstractSegment segment = new Segment(model);
		
		test(segment, "therealdeal");
		test(segment, "isitoveryet");
		test(segment, "isplayingnow");
		test(segment, "thisiswhoweare");
		test(segment, "areallygoodjob");
	}
	
	void test(AbstractSegment segment, String s)
	{
		Sequence sequence = segment.segment(s);
		System.out.println(sequence.getSequence()+" "+sequence.getMaximumLikelihood());		
	}
}
