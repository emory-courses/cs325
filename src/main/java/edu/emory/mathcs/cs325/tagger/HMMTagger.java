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

import edu.emory.mathcs.cs325.classifier.HiddenMarkov;
import edu.emory.mathcs.cs325.utils.IntDoublePair;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class HMMTagger extends AbstractTagger
{
	static public String START_STATE = "START_STATE";
	static public String END_STATE   = "END_STATE";
	private HiddenMarkov hm_model;
	
	public HMMTagger(double epsilon)
	{
		hm_model = new HiddenMarkov(epsilon);
	}
	
	@Override
	public void addSentence(List<String> words, List<String> tags)
	{
		String currenetState, previousState = START_STATE;
		int i, size = words.size();
		
		for (i=0; i<size; i++)
		{
			currenetState = tags.get(i);
			hm_model.addTransition(previousState, currenetState);
			hm_model.addObservation(currenetState, words.get(i));
			previousState = currenetState;
		}
	}
		
	@Override
	public void train()
	{
		hm_model.train();
	}
	
	@Override
	public List<TagList> decode(List<String> observations)
	{
		final int N = hm_model.getLabels().size(); 
		final int T = observations.size();
		
		double[][] viterbi  = new double[N][T];
		int[][] backpointer = new int[N][T];
		IntDoublePair max;
		int s, t = 0;
		
		// initialization
		for (s=0; s<N; s++)
		{
			viterbi[s][0] = hm_model.getOverallLikelihood(START_STATE, hm_model.getLabel(s), observations.get(t));
			backpointer[s][0] = 0;
		}
		
		// recursion
		for (t=1; t<T; t++)
		{
			for (s=0; s<N; s++)
			{
				max = max(viterbi, t, hm_model.getLabel(s), observations.get(t), N);
				viterbi[s][t] = max.getDouble();
				backpointer[s][t] = max.getInt();
			}
		}
		
		max = max(viterbi, t, END_STATE, null, N);
		List<TagList> list = new ArrayList<>(1);
		list.add(getTags(backpointer, max.getInt(), N, t));
		return list;
	}
	
	private IntDoublePair max(double[][] viterbi, int t, String currentState, String observation, final int N)
	{
		int s = 0;
		double d = viterbi[s][t-1] * hm_model.getOverallLikelihood(hm_model.getLabel(s), currentState, observation);
		IntDoublePair max = new IntDoublePair(s, d);
		
		for (s=1; s<N; s++)
		{
			d = viterbi[s][t-1] * hm_model.getOverallLikelihood(hm_model.getLabel(s), currentState, observation);
			if (d > max.getDouble()) max.set(s, d);
		}
		
		return max;
	}
	
	private TagList getTags(int[][] backpointer, int max, final int N, final int T)
	{
		List<String> tags = new ArrayList<>();
		int t;
		
		tags.add(hm_model.getLabel(max));
		
		for (t=T-1; t>0; t--)
		{
			max = backpointer[max][t];
			tags.add(hm_model.getLabel(max));
		}

		Collections.reverse(tags);
		return new TagList(tags);
	}
}
