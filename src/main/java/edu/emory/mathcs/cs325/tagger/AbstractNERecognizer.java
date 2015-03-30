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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import edu.emory.mathcs.cs325.feature.AbstractFeatureExtractor;
import edu.emory.mathcs.cs325.feature.SimpleFeatureExtractor;
import edu.emory.mathcs.cs325.perceptron.AbstractPerceptron;
import edu.emory.mathcs.cs325.token.NERToken;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class AbstractNERecognizer
{
	static final Pattern SPACE = Pattern.compile(" ");
	
	public AbstractFeatureExtractor<NERToken> collect(InputStream in) throws Exception
	{
		AbstractFeatureExtractor<NERToken> extractor = new SimpleFeatureExtractor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		List<NERToken> tokens;
		int i;
		
		while (!(tokens = getNERTokens(reader)).isEmpty())
		{
			for (i=0; i<tokens.size(); i++)
				extractor.collect(tokens, i);
		}
		
		in.close();
		return extractor;
	}
	
	protected List<NERToken> getNERTokens(BufferedReader reader) throws Exception
	{
		List<NERToken> tokens = new ArrayList<>();
		String line;
		String[] t;
		
		while ((line = reader.readLine()) != null)
		{
			if (line.trim().isEmpty()) break;
			t = SPACE.split(line);
			tokens.add(new NERToken(t[3], t[0], t[1], t[2]));
		}
		
		return tokens;
	}
	
	public abstract void train(AbstractFeatureExtractor<NERToken> extractor, AbstractPerceptron perceptron, InputStream in) throws Exception;
	
	public double evaluate(AbstractFeatureExtractor<NERToken> extractor, AbstractPerceptron perceptron, InputStream in) throws Exception
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		int i, y, correct = 0, total = 0;
		List<NERToken> tokens;
		NERToken token;
		int[] x, ys;
		
		while (!(tokens = getNERTokens(reader)).isEmpty())
		{
			ys = extractLabels(extractor, tokens);
			total += ys.length;
			
			for (i=0; i<tokens.size(); i++)
			{
				token = tokens.get(i);
				x = extractor.getFeatureIndices(tokens, i);
				y = perceptron.decode(x).getInt();
				token.setLabel(extractor.getLabel(y));
				if (ys[i] == y) correct++;
			}
		}
		
		return 100d * correct / total;
	}
	
	private int[] extractLabels(AbstractFeatureExtractor<NERToken> extractor, List<NERToken> tokens)
	{
		int[] labels = new int[tokens.size()];
		NERToken token;
		
		for (int i=tokens.size()-1; i>=0; i--)
		{
			token = tokens.get(i);
			labels[i] = extractor.getLabelIndex(token.getLabel());
			token.setLabel(null);
		}
		
		return labels;
	}
}
