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
package edu.emory.mathcs.ngrams;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class UnigramTest
{
	@Test
	public void test()
	{
		Unigram unigram = new Unigram();
		
		unigram.add("A", 1);
		unigram.add("B", 2);
		unigram.add("B", 4);
		unigram.add("C", 3);
		unigram.add("C", 6);
		unigram.add("D", 4);
		unigram.finalize();

		assertEquals(unigram.get("A"), 0.05, 0);
		assertEquals(unigram.get("B"), 0.3 , 0);
		assertEquals(unigram.get("C"), 0.45, 0);
		assertEquals(unigram.get("D"), 0.2 , 0);
		assertEquals(unigram.get("E"), null);
	}
	
	void addAll(Unigram unigram, InputStream in)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			Pattern p = Pattern.compile("\t");
			String line;
			String[] t;
			
			while ((line = reader.readLine()) != null)
			{
				t = p.split(line);
				unigram.add(t[0], Integer.parseInt(t[1]));
			}
			
			unigram.finalize();
		}
		catch (IOException e) {e.printStackTrace();}
	}
}
