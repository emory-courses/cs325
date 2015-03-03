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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class HW2Test
{
	@Test
	public void test()
	{
//		AbstractTagger tagger = new TopTagger(new NaiveBayes(0.0001));
		AbstractTagger tagger = new HMMTagger(0.0001);
		
		try
		{
			System.out.println("Training");
			train(tagger, new FileInputStream("/Users/jdchoi/Emory/courses/CS325/dat/trn.pos"));
			System.out.println("Decoding");
			evaluate(tagger, new FileInputStream("/Users/jdchoi/Emory/courses/CS325/dat/dev.pos"));
		}
		catch (Exception e) {e.printStackTrace();}
	}
	
	void train(AbstractTagger tagger, InputStream in) throws Exception
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		List<String> words = new ArrayList<>(), tags = new ArrayList<>();;
		Pattern p = Pattern.compile("\t");
		String line;
		String[] t;
		
		while ((line = reader.readLine()) != null)
		{
			t = p.split(line);
			
			if (t.length < 2)
			{
				tagger.addSentence(words, tags);
				words.clear();
				tags.clear();
			}
			else
			{
				words.add(t[0]);
				tags .add(t[1]);
			}
		}
		
		tagger.train();
	}
	
	void evaluate(AbstractTagger tagger, InputStream in) throws Exception
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		List<String> words = new ArrayList<>(), tags = new ArrayList<>();;
		Pattern p = Pattern.compile("\t");
		int total = 0, correct = 0;
		String line;
		String[] t;
		
		while ((line = reader.readLine()) != null)
		{
			t = p.split(line);
			
			if (t.length < 2)
			{
				total += words.size();
				correct += correct(tags, tagger.decode(words).get(0));
				words.clear();
				tags.clear();
			}
			else
			{
				words.add(t[0]);
				tags .add(t[1]);
			}
		}
		
		System.out.printf("%5.2f (%d/%d)\n", 100d*correct/total, correct, total);
	}
	
	int correct(List<String> tags, TagList list)
	{
		int i, size = tags.size(), c = 0;
		
		for (i=0; i<size; i++)
		{
			if (tags.get(i).equals(list.getTag(i)))
				c++;
		}
		
		return c;
	}
}
