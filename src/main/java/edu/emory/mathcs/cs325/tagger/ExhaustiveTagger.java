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
import java.util.List;

import edu.emory.mathcs.cs325.classifier.AbstractClassifier;
import edu.emory.mathcs.cs325.classifier.Prediction;
import edu.emory.mathcs.cs325.feature.StringFeature;
import edu.emory.mathcs.cs325.utils.DSUtils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class ExhaustiveTagger extends AbstractTagger
{
	public ExhaustiveTagger(AbstractClassifier classifier)
	{
		super(classifier);
	}

	@Override
	public List<TagList> decode(List<String> words)
	{
		List<TagList> allTags = new ArrayList<>();
		decodeAux(allTags, words, new TagList(), 0);
		return DSUtils.getBestList(allTags);
	}
	
	private void decodeAux(List<TagList> allTags, List<String> words, TagList tags, int index)
	{
		if (index >= words.size())
		{
			allTags.add(tags);
			return;
		}
		
		List<StringFeature> features = getFeatures(words, tags, index);
		TagList copy;
		
		for (Prediction p : getPredictions(features))
		{
			copy = new TagList(tags);
			copy.add(p);
			decodeAux(allTags, words, copy, index+1);
		}
	}
	
	protected List<Prediction> getPredictions(List<StringFeature> features)
	{
		return classifier.predict(features);
	}
}
