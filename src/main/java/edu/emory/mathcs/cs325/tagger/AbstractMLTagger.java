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
import edu.emory.mathcs.cs325.classifier.StringFeature;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class AbstractMLTagger
{
	protected AbstractClassifier classifier;
	
	public AbstractMLTagger() {}
	
	/** @param classifier a multi-classifier. */
	public AbstractMLTagger(AbstractClassifier classifier)
	{
		this.classifier = classifier;
	}

	/**
	 * Adds training instances from a sentences.
	 * @param words the words in the sentence.
	 * @param tags the pos-tags in the sentence.
	 */
	public void addSentence(List<String> words, List<String> tags)
	{
		TagList list = new TagList(tags);
		List<StringFeature> features;
		int i, size = words.size();
		
		for (i=0; i<size; i++)
		{
			 features = getFeatures(words, list, i);
			 classifier.addInstance(tags.get(i), features);
		}
	}
	
	public void train()
	{
		classifier.train();
	}
	
	/**
	 * @return a list of string features from the current state.
	 * @param words the list of words.
	 * @param tags the list of previously found pos-tags.
	 * @param index the current index.
	 */
	protected List<StringFeature> getFeatures(List<String> words, TagList tags, int index)
	{
		List<StringFeature> features = new ArrayList<>();
		String t;

		// current word form
		features.add(new StringFeature("f0", words.get(index)));
		
		// previous tag
		t = (index-1 < 0) ? null : tags.getTag(index-1);
		features.add(new StringFeature("f1", t));
		
		return features;
	}
	
	/**
	 * @return the list of tags with the overall probability.
	 * @param words the list of words.
	 */
	public abstract List<TagList> decode(List<String> words);
}
