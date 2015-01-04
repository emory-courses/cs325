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

import java.util.HashMap;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Unigram extends HashMap<String,Double>
{
	private static final long serialVersionUID = -5613317171603511430L;
	private int n_total;
	
	public Unigram()
	{
		n_total = 0;
	}
	
	public void add(String word, int count)
	{
		compute(word, (k,v) -> (v == null) ? count : v+count);
		n_total += count;
	}
	
	public void finalize()
	{
		for (Entry<String,Double> entry : entrySet())
			compute(entry.getKey(), (k,v) -> v/n_total);		
	}
}
