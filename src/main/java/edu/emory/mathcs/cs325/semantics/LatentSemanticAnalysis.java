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

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;

import edu.emory.clir.clearnlp.collection.pair.ObjectDoublePair;
import edu.emory.clir.clearnlp.component.utils.NLPUtils;
import edu.emory.clir.clearnlp.tokenization.AbstractTokenizer;
import edu.emory.clir.clearnlp.util.FileUtils;
import edu.emory.clir.clearnlp.util.lang.TLanguage;
import edu.emory.mathcs.cs325.document.Term;
import edu.emory.mathcs.cs325.document.VectorSpaceModel;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class LatentSemanticAnalysis
{
	private VectorSpaceModel vs_model;
	
	public Matrix getTermDocumentMatrix(List<List<String>> documents)
	{
		vs_model = new VectorSpaceModel();
		List<Term[]> list = vs_model.toTFIDFs(documents);
		int i, T = vs_model.getTermSize(), D = list.size();
		Matrix matrix = new Basic2DMatrix(T, D);
		
		for (i=0; i<D; i++)
			for (Term term : list.get(i))
				matrix.set(term.getID(), i, term.getScore());

		return matrix;
	}
	
	public List<ObjectDoublePair<String>> getTopSimilarTerms(Matrix matrix, String term, int k)
	{
		List<ObjectDoublePair<String>> list = new ArrayList<>();
		int termID = vs_model.getID(term);
		if (termID < 0) return list;
		int i, size = matrix.rows();
		
		for (i=0; i<size; i++)
		{
			if (i != termID)
				list.add(new ObjectDoublePair<String>(vs_model.getTerm(i), getCosineSimilarityTerm(matrix, termID, i)));
		}
		
		Collections.sort(list, Collections.reverseOrder());
		return (k > list.size()) ? list : list.subList(0, k);
	}
	
	public double getCosineSimilarityTerm(Matrix matrix, int i, int j)
	{
		return getCosineSimilarity(matrix.getRow(i), matrix.getRow(j));
	}
	
	public double getCosineSimilarityDocument(Matrix matrix, int i, int j)
	{
		return getCosineSimilarity(matrix.getColumn(i), matrix.getColumn(j));
	}
	
	public double getCosineSimilarity(Vector v1, Vector v2)
	{
		return v1.innerProduct(v2) / Math.sqrt(v1.norm()*v2.norm());
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
		LatentSemanticAnalysis lsa = new LatentSemanticAnalysis();
		Matrix matrix = lsa.getTermDocumentMatrix(documents);
		String line;
		int k = 10;
		
		while (true)
		{
			System.out.print("Enter a term: "); line = reader.readLine();
			
			for (ObjectDoublePair<String> p : lsa.getTopSimilarTerms(matrix, line.trim(), k))
				System.out.printf("%20s: %5.4f\n", p.o, p.d);
		}
	}
}
