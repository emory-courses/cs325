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

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.emory.clir.clearnlp.collection.pair.ObjectIntPair;
import edu.emory.clir.clearnlp.util.FileUtils;
import edu.emory.clir.clearnlp.util.IOUtils;
import edu.emory.clir.clearnlp.util.Splitter;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class StopWordsGenerator
{
	static public void main(String[] args) throws Exception
	{
		final String inputDir = "/Users/jdchoi/Desktop/genre-pos/train";
		final String inputExt = ".txt";
		final int cutoff = 50;
		
		VectorSpaceModel model = new VectorSpaceModel();
		BufferedReader reader;
		String line;
		
		for (String filename : FileUtils.getFileList(inputDir, inputExt, true))
		{
			reader = IOUtils.createBufferedReader(filename);
			
			while ((line = reader.readLine()) != null)
				model.toBagOfWords(Arrays.asList(Splitter.splitSpace(line)), true);
		}

		List<ObjectIntPair<String>> dfs = model.getDocumentFrequencies();
		Collections.sort(dfs, Collections.reverseOrder());
		
		for (int i=0; i<cutoff; i++)
			System.out.println(dfs.get(i).o+" "+dfs.get(i).i);
	}
}
