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
package edu.emory.mathcs.cs325.document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class Document
{
	private List<Term[]> terms;
	private String genre;
	
	public Document()
	{
		terms = new ArrayList<>();
	}
	
	public Document(String genre)
	{
		terms = new ArrayList<>();
		setGenre(genre);
	}
	
	public List<Term[]> getTerms()
	{
		return terms;
	}

	public void setTerms(List<Term[]> terms)
	{
		this.terms = terms;
	}
	
	public String getGenre()
	{
		return genre;
	}
	
	public void setGenre(String genre)
	{
		this.genre = genre;
	}
}
