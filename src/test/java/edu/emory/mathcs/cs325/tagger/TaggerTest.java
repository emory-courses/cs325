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
package edu.emory.mathcs.cs325.tagger;

import java.util.List;

import org.junit.Test;

import edu.emory.mathcs.cs325.classifier.NaiveBayes;
import edu.emory.mathcs.cs325.utils.DSUtils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class TaggerTest
{
	@Test
	public void testGreedyTagger()
	{
		testTagger(new GreedyTagger    (new NaiveBayes(0.0001)));
		testTagger(new ExhaustiveTagger(new NaiveBayes(0.0001)));
		testTagger(new TopTagger       (new NaiveBayes(0.0001)));
	}
	
	void testTagger(AbstractTagger tagger)
	{
		List<String> words    = DSUtils.createList("John","bought","a"  ,"book","from","Mary");
		List<String> goldTags = DSUtils.createList("noun","verb"  ,"det","noun","prep","noun");
		
		tagger.addSentence(words, goldTags);
		tagger.train();
		
		words    = DSUtils.createList("Susan","made","a"  ,"cake","for" ,"Tom");
		goldTags = DSUtils.createList("noun" ,"verb","det","noun","prep","noun");

		for (TagList sysTags : tagger.decode(words))
			printScore(goldTags, sysTags);
	}
	
	void printScore(List<String> goldTags, TagList sysTags)
	{
		int correct = 0, i, total = goldTags.size();
		
		for (i=0; i<total; i++)
		{
			if (goldTags.get(i).equals(sysTags.getTag(i)))
				correct++;
		}
		
		System.out.println(sysTags.toString());
		System.out.printf("%5.2f (%d/%d)\n", 100d*correct/total, correct, total);
	}
}
