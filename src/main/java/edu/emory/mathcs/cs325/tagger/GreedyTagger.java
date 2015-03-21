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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.emory.mathcs.cs325.classifier.AbstractClassifier;
import edu.emory.mathcs.cs325.feature.StringFeature;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class GreedyTagger extends AbstractTagger
{
	public GreedyTagger(AbstractClassifier classifier)
	{
		super(classifier);
	}

	@Override
	public List<TagList> decode(List<String> words)
	{
		TagList tags = new TagList();
		
		List<StringFeature> features;
		int i, size = words.size();
		
		for (i=0; i<size; i++)
		{
			 features = getFeatures(words, tags, i);
			 tags.add(Collections.max(classifier.predict(features)));
		}
		
		List<TagList> list = new ArrayList<>(1);
		list.add(tags);
		return list;
	}
}
