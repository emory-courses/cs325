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
import java.util.Collections;
import java.util.List;

import edu.emory.mathcs.cs325.ngrams.Bigram;
import edu.emory.mathcs.cs325.ngrams.smoothing.NoSmoothing;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class HiddenMarkov
{
	private Bigram observation_likelihoods;
	private Bigram transition_likelihoods;
	private List<String> labels;
	private double epsilon;
	
	/**
	 * Initializes a bigram for the transition and observation likelihoods.
	 * @param epsilon the unseen probability.
	 */
	public HiddenMarkov(double epsilon)
	{
		observation_likelihoods = new Bigram(new NoSmoothing());
		transition_likelihoods  = new Bigram(new NoSmoothing());
		this.epsilon = epsilon;
	}
	
	public void addObservation(String state, String observation)
	{
		observation_likelihoods.add(state, observation, 1);
	}
	
	public void addTransition(String previousState, String currentState)
	{
		transition_likelihoods.add(previousState, currentState, 1);
	}
	
	public void train()
	{
		observation_likelihoods.estimateMaximumLikelihoods();
		transition_likelihoods .estimateMaximumLikelihoods();
		labels = new ArrayList<>(observation_likelihoods.getUnigramMap().keySet());
		Collections.sort(labels);
	}
	
	public List<String> getLabels()
	{
		return labels;
	}
	
	public String getLabel(int index)
	{
		return labels.get(index);
	}
	
	public double getOverallLikelihood(String previousState, String currentState, String observation)
	{
		return getTransitionLikelihood(previousState, currentState) * getObservationLikelihood(currentState, observation);
	}
	
	public double getTransitionLikelihood(String previousState, String currentState)
	{
		double likelihood = transition_likelihoods.getLikelihood(previousState, currentState);
		return (likelihood == 0) ? epsilon : likelihood; 
	}
	
	public double getObservationLikelihood(String state, String observation)
	{
		double likelihood = observation_likelihoods.getLikelihood(state, observation);
		return (likelihood == 0) ? epsilon : likelihood; 
	}
}
