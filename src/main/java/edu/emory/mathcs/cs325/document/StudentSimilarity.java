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

import edu.emory.clir.clearnlp.collection.map.IntObjectHashMap;
import edu.emory.clir.clearnlp.collection.pair.ObjectDoublePair;
import edu.emory.clir.clearnlp.component.utils.NLPUtils;
import edu.emory.clir.clearnlp.tokenization.AbstractTokenizer;
import edu.emory.clir.clearnlp.util.FileUtils;
import edu.emory.clir.clearnlp.util.lang.TLanguage;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class StudentSimilarity
{
	static public void main(String[] args) throws Exception
	{
		final String inputDir = "/Users/jdchoi/Emory/courses/CS325/dat/clustering/students";
		final String inputExt = ".txt";
		
		AbstractTokenizer tokenizer = NLPUtils.getTokenizer(TLanguage.ENGLISH);
		IntObjectHashMap<String> map = new IntObjectHashMap<>();
		List<List<String>> documents = new ArrayList<>();
		VectorSpaceModel model = new VectorSpaceModel();
		List<String> document;
		String basename;
		
		List<String> filenames = FileUtils.getFileList(inputDir, inputExt, true);
		Collections.sort(filenames);
		
		for (String filename : filenames)
		{
			basename = FileUtils.getBaseName(filename);
			document = tokenizer.tokenize(new FileInputStream(filename));
			map.put(documents.size(), basename.substring(0, basename.length()-4));
			documents.add(document);
		}
		
		List<Term[]> tfidfs = model.toTFIDFs(documents);
		List<ObjectDoublePair<String>> list;
		int i, j, size = tfidfs.size();
		
		for (i=0; i<size; i++)
		{
			list = new ArrayList<>();
			
			for (j=0; j<size; j++)
				if (i != j)
					list.add(new ObjectDoublePair<>(map.get(j), VectorSpaceModel.getCosineSimilarity(tfidfs.get(i), tfidfs.get(j))));
			
			Collections.sort(list, Collections.reverseOrder());
			
			System.out.println(map.get(i));
			for (ObjectDoublePair<String> p : list) System.out.printf("%10s: %f\n", p.o, p.d);
		}
	}
}
