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
package edu.emory.mathcs.cs325.feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.emory.mathcs.cs325.token.AbstractToken;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class AbstractFeatureExtractor<T extends AbstractToken>
{
	private Map<String,Map<String,Integer>> feature_map;
	private Map<String,Integer> label_map;
	private List<String> label_values;
	private int feature_size;
	private int label_size;
	
	public AbstractFeatureExtractor()
	{
		feature_map  = new HashMap<>();
		label_map    = new HashMap<>();
		label_values = new ArrayList<>();
		feature_size = 1;
		label_size   = 0;
	}
	
	abstract public List<StringFeature> getStringFeatures(List<T> tokens, int index);
	
	public void collect(List<T> tokens, int index)
	{
		List<StringFeature> features = getStringFeatures(tokens, index);
		String label = tokens.get(index).getLabel();
		
		for (StringFeature feature : features)
			feature_map.computeIfAbsent(feature.getType(), k -> new HashMap<>()).putIfAbsent(feature.getValue(), feature_size++);
		
		if (!label_map.containsKey(label))
		{
			label_map.putIfAbsent(label, label_size++);
			label_values.add(label);
		}
	}
	
	public int[] getFeatureIndices(List<T> tokens, int index)
	{
		List<StringFeature> features = getStringFeatures(tokens, index);
		List<Integer> indices = new ArrayList<>();
		Map<String,Integer> map;
		Integer idx;
		
		indices.add(0);	// bias
		
		for (StringFeature feature : features)
		{
			map = feature_map.get(feature.getType());
			
			if (map != null)
			{
				idx = map.get(feature.getValue());
				if (idx != null) indices.add(idx);
			}
		}
		
		int[] x = new int[indices.size()];
		for (int i=x.length-1; i>=0; i--) x[i] = indices.get(i);
		return x;
	}
	
	public int getFeatureSize()
	{
		return feature_size;
	}
	
	public int getLabelSize()
	{
		return label_size;
	}
	
	public int getLabelIndex(String label)
	{
		return label_map.get(label);
	}
	
	public String getLabel(int index)
	{
		return label_values.get(index);
	}
}
