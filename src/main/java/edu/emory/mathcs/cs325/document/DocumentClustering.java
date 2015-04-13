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

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.emory.clir.clearnlp.collection.list.IntArrayList;
import edu.emory.clir.clearnlp.collection.map.IntObjectHashMap;
import edu.emory.clir.clearnlp.collection.map.ObjectIntHashMap;
import edu.emory.clir.clearnlp.collection.pair.ObjectIntPair;
import edu.emory.clir.clearnlp.component.utils.NLPUtils;
import edu.emory.clir.clearnlp.tokenization.AbstractTokenizer;
import edu.emory.clir.clearnlp.util.FileUtils;
import edu.emory.clir.clearnlp.util.MathUtils;
import edu.emory.clir.clearnlp.util.lang.TLanguage;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class DocumentClustering
{
	static public void main(String[] args) throws Exception
	{
		final String inputDir = "/Users/jdchoi/Emory/courses/CS325/dat/clustering/train";
		final String inputExt = ".txt";
		final int K = 7;
		
		AbstractTokenizer tokenizer = NLPUtils.getTokenizer(TLanguage.ENGLISH);
		IntObjectHashMap<String> genreMap = new IntObjectHashMap<>();
		List<List<String>> documents = new ArrayList<>();
		VectorSpaceModel model = new VectorSpaceModel();
		AbstractKmeans kmeans = new KmeanCosine();
		List<String> document;
		String genre;
		int idx;
		
		List<String> filenames = FileUtils.getFileList(inputDir, inputExt, true);
		Collections.sort(filenames);
		
		for (String filename : filenames)
		{
			idx = inputDir.length() + 1;
			genre = filename.substring(idx, idx+2);
			document = tokenizer.tokenize(new FileInputStream(filename));
			genreMap.put(documents.size(), genre);
			documents.add(document);
		}
		
		List<Term[]> tfidfs = model.toTFIDFs(documents);
		List<IntArrayList> clusters = kmeans.cluster(tfidfs, K, 0.001);
		System.out.println("Score: "+getPurityScrore(genreMap, clusters));
	}
	
	static public double getPurityScrore(IntObjectHashMap<String> genreMap, List<IntArrayList> clusters)
	{
		int purityCount = clusters.stream().mapToInt(cluster -> getPurityCount(genreMap, cluster)).sum();
		int totalCount  = clusters.stream().mapToInt(cluster -> cluster.size()).sum();
		return MathUtils.divide(purityCount, totalCount);
	}
	
	static public int getPurityCount(IntObjectHashMap<String> genreMap, IntArrayList cluster)
	{
		ObjectIntHashMap<String> map = new ObjectIntHashMap<>();
		int i, size = cluster.size();
		
		for (i=0; i<size; i++)
			map.add(genreMap.get(cluster.get(i)));

		ObjectIntPair<String> p = Collections.max(map.toList());
		System.out.printf("%s: %5.2f (%d/%d)\n", p.o, MathUtils.getAccuracy(p.i, size), p.i, size);
		return p.i;
	}
}
