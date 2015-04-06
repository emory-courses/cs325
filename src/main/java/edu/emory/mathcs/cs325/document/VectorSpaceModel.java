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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.emory.clir.clearnlp.collection.map.ObjectIntHashMap;
import edu.emory.clir.clearnlp.collection.pair.ObjectIntPair;
import edu.emory.clir.clearnlp.util.DSUtils;
import edu.emory.clir.clearnlp.util.MathUtils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class VectorSpaceModel implements Serializable
{
	private static final long serialVersionUID = 4172483442205081702L;
	private List<ObjectIntPair<String>> term_list;
	private ObjectIntHashMap<String> term_id_map;
	private int id_count;

	public VectorSpaceModel()
	{
		term_id_map = new ObjectIntHashMap<>();
		term_list   = new ArrayList<>();
		id_count    = 0;
	}
	
	/**
	 * @param document represented as a list of strings.
	 */
	public Term[] toBagOfWords(List<String> document, boolean df)
	{
		// To be filled.
		ObjectIntHashMap<String> map = new ObjectIntHashMap<>();
		for (String term : document) map.add(term);
		Term[] terms = new Term[map.size()];
		int id, i = 0;
		
		for (ObjectIntPair<String> term : map)
		{
			id = getID(term.o);
			
			if (id < 0)	// term doesn't exist
			{
				id = id_count;
				term_id_map.put(term.o, ++id_count);
				term_list.add(new ObjectIntPair<String>(term.o, 0));
			}
			
			terms[i++] = new Term(id, term.i);
			if (df) term_list.get(id).i++;
		}
		
		Arrays.sort(terms);
		return terms;
	}
	
	/** @param documents each document is represented as a list of strings. */
	public List<Term[]> toTFIDFs(List<List<String>> documents)
	{
		final int DOCUMENT_SIZE = documents.size();
		List<Term[]> xs = new ArrayList<>();
		
		for (List<String> document : documents)
			xs.add(toBagOfWords(document, true));
		
		for (Term[] terms : xs)
		{
			for (Term term : terms)
			{
				term.setDocumentFrequency(getDocumentFrequency(term.getID()));
				term.setScore(getTFIDF(term.getTermFrequency(), term.getDocumentFrequency(), DOCUMENT_SIZE));
			}
		}
		
		return xs;
	}
	
	/** @return the term corresponding to the ID if exists; otherwise, null. */
	public String getTerm(int id)
	{
		return DSUtils.isRange(term_list, id) ? term_list.get(id).o : null; 
	}
	
	/** @return the ID of the term if exists; otherwise, -1. */
	public int getID(String term)
	{
		return term_id_map.get(term) - 1;
	}
	
	public int getDocumentFrequency(int id)
	{
		return DSUtils.isRange(term_list, id) ? term_list.get(id).i : 0; 
	}
	
	public List<ObjectIntPair<String>> getDocumentFrequencies()
	{
		return term_list;
	}
	
	public void resetDocumentFrequency()
	{
		for (ObjectIntPair<String> term : term_list)
			term.i = 0;
	}
	
	static public double getTFIDF(int termFrequency, int documentFrequency, int documentSize)
	{
		return Math.log(MathUtils.divide(documentSize, documentFrequency)) * termFrequency; 
	}
	
	static public double getEuclideanDistance(Term[] d1, Term[] d2)
	{
		int i = 0, j = 0, len1 = d1.length, len2 = d2.length; 
		double sum = 0;
		Term t1, t2;

		while (i<len1 && j<len2)
		{
			t1 = d1[i];
			t2 = d2[j];
			
			if (t1.getID() < t2.getID())
			{
				sum += MathUtils.sq(t1.getScore());
				i++;
			}
			else if (t1.getID() > t2.getID())
			{
				sum += MathUtils.sq(t2.getScore());
				j++;
			}
			else
			{
				sum += MathUtils.sq(t1.getScore() - t2.getScore());
				i++; j++;
			}
		}
		
		for (; i<len1; i++) sum += MathUtils.sq(d1[i].getScore());
		for (; j<len2; j++) sum += MathUtils.sq(d2[j].getScore());
		
		return Math.sqrt(sum);
	}
	
	static public double getCosineSimilarity(Term[] d1, Term[] d2)
	{
		int i = 0, j = 0, len1 = d1.length, len2 = d2.length; 
		double num = 0, den1 = 0, den2 = 0;
		Term t1, t2;

		while (i<len1 && j<len2)
		{
			t1 = d1[i];
			t2 = d2[j];
			den1 += MathUtils.sq(t1.getScore());
			den2 += MathUtils.sq(t2.getScore());
			
			if (t1.getID() < t2.getID())
				i++;
			else if (t1.getID() > t2.getID())
				j++;
			else
			{
				num += t1.getScore() * t2.getScore();
				i++; j++;
			}
		}
		
		for (; i<len1; i++) den1 += MathUtils.sq(d1[i].getScore());
		for (; j<len2; j++) den2 += MathUtils.sq(d2[j].getScore());
		
		return num / (Math.sqrt(den1) * Math.sqrt(den2));
	}
}
