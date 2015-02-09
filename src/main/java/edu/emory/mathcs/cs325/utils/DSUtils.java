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
package edu.emory.mathcs.cs325.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.emory.mathcs.cs325.classifier.StringFeature;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class DSUtils
{
	@SuppressWarnings("unchecked")
	static public <T>List<T> createList(T... keys)
	{
		List<T> list = new ArrayList<>(keys.length);
		for (T key : keys) list.add(key);
		return list;
	}
	
	static public <T extends Comparable<T>>List<T> getBestList(List<T> list)
	{
		List<T> best = new ArrayList<>();
		T max = Collections.max(list);
		
		for (T key : list)
		{
			if(key.compareTo(max) == 0)
				best.add(key);
		}
		
		return best;
	}
	
	static public List<StringFeature> createFeatureVector(String... typeValue)
	{
		List<StringFeature> features = new ArrayList<>();
		int i, len = typeValue.length;
		
		for (i=0; i<len; i+=2)
			features.add(new StringFeature(typeValue[i], typeValue[i+1]));
		
		return features;
	}
}
