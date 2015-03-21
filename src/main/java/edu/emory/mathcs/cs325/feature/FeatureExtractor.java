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

import edu.emory.mathcs.cs325.tagger.TagList;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class FeatureExtractor
{
	private Map<String,Map<String,Integer>> m_features;
	private int n_size;

	public FeatureExtractor()
	{
		m_features = new HashMap<>();
		n_size = 1;
	}
	
	public void add(List<StringFeature> features)
	{
		for (StringFeature feature : features)
			m_features.computeIfAbsent(feature.getType(), k -> new HashMap<>()).putIfAbsent(feature.getValue(), n_size++);
	}

	public List<StringFeature> getStringFeatures(List<String> words, TagList tags, int index)
	{
		List<StringFeature> features = new ArrayList<>();
		String t;

		// current word form
		features.add(new StringFeature("f0", words.get(index)));
		
		// previous word form
		t = (index-1 < 0) ? null : words.get(index-1);
		features.add(new StringFeature("f1", t));
		
		// next word form
		t = (index+1 > words.size()) ? null : words.get(index+1);
		features.add(new StringFeature("f2", t));
		
		// previous tag
		t = (index-1 < 0) ? null : tags.getTag(index-1);
		features.add(new StringFeature("f3", t));
		
		return features;
	}
	
	public List<Integer> getFeatureIndices(List<String> words, TagList tags, int index)
	{
		List<StringFeature> features = getStringFeatures(words, tags, index);
		List<Integer> indices = new ArrayList<>();
		Map<String,Integer> map;
		Integer idx;
		
		indices.add(0);
		
		for (StringFeature feature : features)
		{
			map = m_features.get(feature.getType());
			
			if (map != null)
			{
				idx = map.get(feature.getValue());
				if (idx != null) indices.add(idx);
			}
		}
		
		return indices;
	}
	
	public double[] getScores(double[][] weights, List<Integer> indices)
	{
		int i, len = weights[0].length;
		double[] scores = new double[len];
		
		for (int index : indices)
		{
			for (i=0; i<len; i++)
				scores[i] += weights[index][i];
		}
		
		return scores;
	}
}
