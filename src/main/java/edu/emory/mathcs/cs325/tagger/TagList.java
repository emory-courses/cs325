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

import edu.emory.mathcs.cs325.classifier.Prediction;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class TagList implements Comparable<TagList>
{
	private List<String> tags;
	private double score;
	
	public TagList()
	{
		tags  = new ArrayList<>();
		score = 0;
	}
	
	public TagList(TagList p)
	{
		tags  = new ArrayList<>(p.tags);
		score = p.score;
	}
	
	public TagList(List<String> list)
	{
		tags  = new ArrayList<>(list);
		score = 0;
	}
	
	public List<String> getTags()
	{
		return tags;
	}
	
	public String getTag(int index)
	{
		return tags.get(index);
	}
	
	public double getScore()
	{
		return score;
	}

	public void add(Prediction p)
	{
		tags.add(p.getLabel());
		score += p.getScore();
	}

	@Override
	public int compareTo(TagList o)
	{
		return (int)Math.signum(score - o.score);
	}
	
	@Override
	public String toString()
	{
		return score+" "+tags.toString();
	}
}