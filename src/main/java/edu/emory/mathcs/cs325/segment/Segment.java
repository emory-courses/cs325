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
package edu.emory.mathcs.cs325.segment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.emory.mathcs.cs325.ngrams.model.ILanguageModel;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Segment extends AbstractSegment
{
	public Segment(ILanguageModel model)
	{
		super(model);
	}
	
	@Override
	public Sequence segment(String s)
	{
		List<Sequence> list = new ArrayList<>();
		segmentAux(list, s, 0, new Sequence());
		return Collections.max(list);
	}
	
	private void segmentAux(List<Sequence> list, String s, int beginIndex, Sequence sequence)
	{
		if (beginIndex == s.length())
		{
			list.add(sequence);
			return;
		}
		
		String word1 = sequence.getPreviousWord();
		int endIndex, len = s.length();
		double likelihood;
		Sequence copy;
		String word2;
		
		for (endIndex=beginIndex+1; endIndex<=len; endIndex++)
		{
			word2 = s.substring(beginIndex, endIndex);
			likelihood = l_model.getLikelihood(word1, word2);
			copy = new Sequence(sequence);
			copy.add(word2, likelihood);
			segmentAux(list, s, endIndex, copy);
		}
	}
}
