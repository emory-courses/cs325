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
package edu.emory.mathcs.cs325.classifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.emory.mathcs.cs325.ngrams.Bigram;
import edu.emory.mathcs.cs325.ngrams.Unigram;
import edu.emory.mathcs.cs325.ngrams.smoothing.NoSmoothing;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class NaiveBayes extends AbstractClassifier
{
	private Map<String,Bigram> feature_likelihoods;
	private Unigram            prior_likelihoods;
	private double             d_epsilon;
	
	/**
	 * Initializes an unigram for the prior table.
	 * Initializes a map of bigrams for the feature tables. 
	 * @param epsilon the unseen probability.
	 */
	public NaiveBayes(double epsilon)
	{
		prior_likelihoods   = new Unigram(new NoSmoothing());
		feature_likelihoods = new HashMap<>();
		d_epsilon = epsilon;
	}
	
	@Override
	public void addInstance(String label, List<StringFeature> features)
	{
		prior_likelihoods.add(label, 1);
		
		for (StringFeature feature : features)
			feature_likelihoods.computeIfAbsent(feature.getType(), key -> new Bigram(new NoSmoothing())).add(label, feature.getValue(), 1);
	}
	
	@Override
	public void train()
	{
		prior_likelihoods.estimateMaximumLikelihoods();
		
		for (Bigram m : feature_likelihoods.values())
			m.estimateMaximumLikelihoods();
	}

	@Override
	public List<Prediction> predict(List<StringFeature> features)
	{
		List<Prediction> predictions = getPriorList();
		double score;
		Bigram map;
		
		for (StringFeature feature : features)
		{
			map = feature_likelihoods.get(feature.getType());
			
			for (Prediction prediction : predictions)
			{
				score = map.getLikelihood(prediction.getLabel(), feature.getValue());
				if (score == 0) score = d_epsilon;
				prediction.addScore(Math.log(score));
			}
		}
		
		return predictions;
	}
	
	private List<Prediction> getPriorList()
	{
		List<Prediction> predictions = new ArrayList<>();
		
		for (Entry<String,Double> entry : prior_likelihoods.getLikelihoodMap().entrySet())
			predictions.add(new Prediction(entry.getKey(), Math.log(entry.getValue())));
		
		return predictions;
	}
}
