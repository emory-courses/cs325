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
package edu.emory.mathcs.cs325.token;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class NERToken extends AbstractToken
{
	private String word;
	private String posTag;
	private String chunkTag;
	
	public NERToken(String label, String word, String posTag, String chunkTag)
	{
		super(label);
		setWord(word);
		setPOSTag(posTag);
		setChunkTag(chunkTag);
	}
	
	public String getWord()
	{
		return word;
	}
	
	public void setWord(String word)
	{
		this.word = word;
	}
	
	public String getPOSTag()
	{
		return posTag;
	}
	
	public void setPOSTag(String posTag)
	{
		this.posTag = posTag;
	}
	
	public String getChunkTag()
	{
		return chunkTag;
	}
	
	public void setChunkTag(String chunkTag)
	{
		this.chunkTag = chunkTag;
	}
}
