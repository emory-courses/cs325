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

import edu.emory.mathcs.cs325.ngrams.model.ILanguageModel;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class AbstractSegment
{
	protected ILanguageModel l_model;
	
	/** @param model a language model. */
	public AbstractSegment(ILanguageModel model)
	{
		l_model = model;
	}
	
	/**
	 * @return the word sequence segmented from the string with the highest probability. 
	 * @param the string to segment.
	 */
	public abstract Sequence segment(String s);
}
