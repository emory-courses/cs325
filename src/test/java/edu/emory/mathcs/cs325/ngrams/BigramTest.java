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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.junit.Test;

import edu.emory.mathcs.cs325.ngrams.Bigram;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class BigramTest
{
	@Test
	public void test()
	{
		Bigram bigram = new Bigram();
		
		bigram.add("A", "A1", 2);
		bigram.add("A", "A2", 3);
		bigram.add("B", "B1", 1);
		bigram.add("B", "B2", 4);
		bigram.finalize();

		assertEquals(bigram.get("A","A1"), 0.4, 0);
		assertEquals(bigram.get("A","A2"), 0.6, 0);
		assertEquals(bigram.get("B","B1"), 0.2, 0);;
		assertEquals(bigram.get("B","B2"), 0.8, 0);
		assertEquals(bigram.get("A","A0"), 0,   0);
		assertEquals(bigram.get("C","A1"), 0,   0);
	}
	
	void addAll(Bigram bigram, InputStream in)
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
				bigram.add(t[0], t[1], Integer.parseInt(t[2]));
			}
			
			bigram.finalize();
		}
		catch (IOException e) {e.printStackTrace();}
	}
}
