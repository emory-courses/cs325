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
package edu.emory.mathcs.cs325.tagger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import edu.emory.mathcs.cs325.feature.AbstractFeatureExtractor;
import edu.emory.mathcs.cs325.perceptron.AbstractPerceptron;
import edu.emory.mathcs.cs325.perceptron.SubgradientPerceptron;
import edu.emory.mathcs.cs325.token.NERToken;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class SimpleNERecognizer extends AbstractNERecognizer
{
	@Override
	public void train(AbstractFeatureExtractor<NERToken> extractor, AbstractPerceptron perceptron, InputStream in) throws Exception
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		List<NERToken> tokens;
		NERToken token;
		int i, y;
		int[] x;
		
		while (!(tokens = getNERTokens(reader)).isEmpty())
		{
			for (i=0; i<tokens.size(); i++)
			{
				token = tokens.get(i);
				x = extractor.getFeatureIndices(tokens, i);
				y = extractor.getLabelIndex(token.getLabel());
				perceptron.train(x, y);
			}
		}
	}
	
	static public void main(String[] args) throws Exception
	{
		final String TRN_FILE = "/Users/jdchoi/Emory/courses/CS325/dat/eng.trn";
		final String DEV_FILE = "/Users/jdchoi/Emory/courses/CS325/dat/eng.dev";
		final double ALPHA = 0.01;
		final int MAX_ITER = 10;
		
		SimpleNERecognizer ner = new SimpleNERecognizer();
		
		AbstractFeatureExtractor<NERToken> ex = ner.collect(new FileInputStream(TRN_FILE));
		AbstractPerceptron perceptron = new SubgradientPerceptron(ALPHA, ex.getFeatureSize(), ex.getLabelSize());
		double score;

		for (int i=0; i<MAX_ITER; i++)
		{
			ner.train(ex, perceptron, new FileInputStream(TRN_FILE));
			score = ner.evaluate(ex, perceptron, new FileInputStream(DEV_FILE));
			System.out.printf("%3d: %5.2f\n", i, score);
		}
	}
}
