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
package edu.emory.mathcs.cs325.semantics;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.decomposition.SingularValueDecompositor;
import org.la4j.matrix.dense.Basic2DMatrix;

import edu.emory.clir.clearnlp.collection.pair.ObjectDoublePair;
import edu.emory.clir.clearnlp.component.utils.NLPUtils;
import edu.emory.clir.clearnlp.tokenization.AbstractTokenizer;
import edu.emory.clir.clearnlp.util.DSUtils;
import edu.emory.clir.clearnlp.util.FileUtils;
import edu.emory.clir.clearnlp.util.lang.TLanguage;
import edu.emory.mathcs.cs325.document.Term;
import edu.emory.mathcs.cs325.document.VectorSpaceModel;
import edu.emory.mathcs.cs325.utils.IntDoublePair;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class LatentSemanticAnalysis
{
	private VectorSpaceModel vs_model;
	private Matrix td_matrix;
	
	public LatentSemanticAnalysis(List<List<String>> documents, int k) throws Exception
	{
		vs_model = new VectorSpaceModel();
		vs_model.addStopWords(DSUtils.createStringHashSet(new FileInputStream("/Users/jdchoi/Downloads/stop-words-collection-2014-02-24/stop-words/stop-words_english_1_en.txt")));
		List<Term[]> list = vs_model.toTFIDFs(documents);
//		List<Term[]> list = documents.stream().map(document -> vs_model.toBagOfWords(document, false)).collect(Collectors.toList());
		int T = vs_model.getTermSize(), D = list.size();
		td_matrix = new Basic2DMatrix(T, D);
		
		for (int docID=0; docID<D; docID++)
			for (Term term : list.get(docID))
				td_matrix.set(term.getID(), docID, term.getScore());
		
//		toLSA(k);
	}
	
	void toLSA(int k)
	{
		SingularValueDecompositor d = new SingularValueDecompositor(td_matrix);
		Matrix[] usv = d.decompose();
		
		int[] top = getTopDiagonals(usv[1], k);		
		Matrix  u = new Basic2DMatrix(usv[0].rows(), k);
		Matrix  s = new Basic2DMatrix(k, k);
		Matrix  v = new Basic2DMatrix(usv[2].rows(), k);
		
		for (int i=0; i<k; i++)
		{
			u.setColumn(i, usv[0].getColumn(top[i]));
			s.setColumn(i, usv[1].getColumn(top[i]));
			v.setColumn(i, usv[2].getColumn(top[i]));
		}

		td_matrix = u.multiply(s).multiply(v.transpose());
	}
	
	private int[] getTopDiagonals(Matrix m, int k)
	{
		List<IntDoublePair> list = new ArrayList<>();
		
		for (int i=m.rows()-1; i>=0; i--)
			list.add(new IntDoublePair(i, m.get(i, i)));
		
		Collections.sort(list, Collections.reverseOrder());
		int[] indices = new int[k];
		
		for (int i=0; i<k; i++)
			indices[i] = list.get(i).getInt();
		
		return indices;
	}
	
	public List<ObjectDoublePair<String>> getTopSimilarTerms(String term, int k)
	{
		List<ObjectDoublePair<String>> list = new ArrayList<>();
		int termID = vs_model.getID(term);
		if (termID < 0) return list;
		
		for (int i=td_matrix.rows()-1; i>=0; i--)
			if (i != termID)
				list.add(new ObjectDoublePair<String>(vs_model.getTerm(i), getCosineSimilarityTerm(termID, i)));
		
		Collections.sort(list, Collections.reverseOrder());
		return (k > list.size()) ? list : list.subList(0, k);
	}
	
	public double getCosineSimilarityTerm(int i, int j)
	{
		return getCosineSimilarity(td_matrix.getRow(i), td_matrix.getRow(j));
	}
	
	public double getCosineSimilarityDocument(int i, int j)
	{
		return getCosineSimilarity(td_matrix.getColumn(i), td_matrix.getColumn(j));
	}
	
	public double getCosineSimilarity(Vector v1, Vector v2)
	{
		return v1.innerProduct(v2) / Math.sqrt(v1.norm() * v2.norm());
	}
	
	static public void main(String[] args) throws Exception
	{
		final String inputDir = "/Users/jdchoi/Emory/courses/CS325/dat/clustering/train";
		final String inputExt = ".txt";
		
		List<String> filenames = FileUtils.getFileList(inputDir, inputExt, true);
		AbstractTokenizer tokenizer = NLPUtils.getTokenizer(TLanguage.ENGLISH);
		List<List<String>> documents = new ArrayList<>();
		
		for (String filename : filenames)
			documents.add(tokenizer.tokenize(new FileInputStream(filename)));
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		LatentSemanticAnalysis lsa = new LatentSemanticAnalysis(documents, 1000);
		String line;
		int k = 10;
		
		while (true)
		{
			System.out.print("Enter a term: "); line = reader.readLine();
			
			for (ObjectDoublePair<String> p : lsa.getTopSimilarTerms(line.trim(), k))
				System.out.printf("%20s: %5.4f\n", p.o, p.d);
		}
	}
}
