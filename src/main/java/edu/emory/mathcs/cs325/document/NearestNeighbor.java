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
package edu.emory.mathcs.cs325.document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;

import edu.emory.clir.clearnlp.collection.map.ObjectDoubleHashMap;
import edu.emory.clir.clearnlp.collection.pair.ObjectDoublePair;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class NearestNeighbor
{
	private Map<String,List<Term[]>> vector_space;
	
	public NearestNeighbor()
	{
		vector_space = new HashMap<>();
	}
	
	public void train(String genre, Term[] document)
	{
		vector_space.computeIfAbsent(genre, k -> new ArrayList<>()).add(document);
	}
	
	public String decode(final int K, Term[] document, BiFunction<Term[],Term[],Double> simFunc)
	{
		List<ObjectDoublePair<String>> list = new ArrayList<>();
		String genre;
		
		for (Entry<String,List<Term[]>> e : vector_space.entrySet())
		{
			genre = e.getKey();
			
			for (Term[] d : e.getValue())
				list.add(new ObjectDoublePair<String>(genre, simFunc.apply(d, document)));
		}

		ObjectDoubleHashMap<String> map = new ObjectDoubleHashMap<>();
		Collections.sort(list, Collections.reverseOrder());
		
		for (int i=0; i<K && i<list.size(); i++)
			map.add(list.get(i).o, 1);
		
		return Collections.max(map.toList()).o;
	}
}
