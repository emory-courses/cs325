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

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import edu.emory.clir.clearnlp.collection.list.IntArrayList;
import edu.emory.clir.clearnlp.collection.pair.DoubleIntPair;
import edu.emory.clir.clearnlp.collection.set.IntHashSet;
import edu.emory.clir.clearnlp.util.Joiner;
import edu.emory.clir.clearnlp.util.MathUtils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class AbstractKmeans
{
	public List<IntArrayList> clusterForge(List<Term[]> documents, final int K, final double threshold)
	{
		final int T = maxID(documents) + 1;
		double[][] prevCentroids, currCentroids = initCentroids(documents, K, T);
		List<IntArrayList> clusters;
		double distance;
		int iter = 1;
		
		do
		{
			clusters = maximize(documents, currCentroids, K);
			prevCentroids = currCentroids;
			currCentroids = estimate(documents, clusters, K, T);
			distance = averageDistance(prevCentroids, currCentroids, K);
			System.out.printf("%4d: %5.4f [%s]\n", iter++, distance, Joiner.join(clusters.stream().map(cluster -> cluster.size()).collect(Collectors.toList()), ","));
		}
		while (distance > threshold);
		
		return clusters;
	}
	
	public List<IntArrayList> clusterRandom(List<Term[]> documents, final int K, final double threshold)
	{
		final int T = maxID(documents) + 1;
		List<IntArrayList> clusters = initClusters(K, documents.size());
		double[][] prevCentroids, currCentroids = estimate(documents, clusters, K, T);
		double distance;
		int iter = 1;
		
		do
		{
			clusters = maximize(documents, currCentroids, K);
			prevCentroids = currCentroids;
			currCentroids = estimate(documents, clusters, K, T);
			distance = averageDistance(prevCentroids, currCentroids, K);
			System.out.printf("%4d: %5.4f [%s]\n", iter++, distance, Joiner.join(clusters.stream().map(cluster -> cluster.size()).collect(Collectors.toList()), ","));
		}
		while (distance > threshold);
		
		return clusters;
	}
	
	private int maxID(List<Term[]> documents)
	{
		int max = -1;
		
		for (Term[] document : documents)
			for (Term term : document)
				max = Math.max(max, term.getID());
		
		return max;
	}
	
	private double[][] initCentroids(List<Term[]> documents, final int K, final int T)
	{
		double[][] centroids = new double[K][];
		int i, count = 0, D = documents.size();
		IntHashSet set = new IntHashSet();
		Random rand = new Random(1);
		double[] centroid;
		
		while (set.size() < K)
		{
			i = rand.nextInt(D);
			
			if (!set.contains(i))
			{
				set.add(i);
				centroid = new double[T];
				centroids[count++] = centroid;
				
				for (Term t : documents.get(i))
					centroid[t.getID()] = t.getScore();
			}
		}
		
		return centroids;
	}
	
	private List<IntArrayList> initClusters(final int K, final int D)
	{
		List<IntArrayList> clusters = IntStream.range(0, K).mapToObj(i -> new IntArrayList()).collect(Collectors.toList());
		Random rand = new Random(1);
		
		for (int i=0; i<D; i++)
			clusters.get(rand.nextInt(K)).add(i);
		
		return clusters;
	}
	
	private double[] computeCentroids(List<Term[]> documents, IntArrayList cluster, final int T)
	{
		int i, D = documents.size(), C = cluster.size();
		double[] centroid = new double[T];
		
		for (i=0; i<C; i++)
			for (Term term : documents.get(cluster.get(i)))
				centroid[term.getID()] += term.getScore();
		
		for (i=0; i<T; i++)
			centroid[i] /= C;
		
		return centroid;
	}

	private List<IntArrayList> maximize(List<Term[]> documents, double[][] centroids, final int K)
	{
		List<IntArrayList> clusters = IntStream.range(0, K).mapToObj(i -> new IntArrayList()).collect(Collectors.toList());
		DoubleIntPair min = new DoubleIntPair(0, 0);
		int i, j, size = documents.size();
		Term[] document;
		double d;
		
		for (i=0; i<size; i++)
		{
			document = documents.get(i);
			min.set(distance(document, centroids[0]), 0);
			
			for (j=1; j<K; j++)
			{
				d = distance(document, centroids[j]);
				if (min.d > d) min.set(d, j);
			}

			clusters.get(min.i).add(i);
		}
		
		return clusters;
	}
	
	private double[][] estimate(List<Term[]> documents, List<IntArrayList> clusters, final int K, final int T)
	{
		double[][] centroids = new double[K][];
		
		for (int i=0; i<K; i++)
			centroids[i] = computeCentroids(documents, clusters.get(i), T);

		return centroids;
	}
	
	protected abstract double distance(Term[] document, double[] centroid);
	
	private double averageDistance(double[][] prevCentroids, double[][] currCentroids, final int K)
	{
		return IntStream.range(0, K).mapToDouble(i -> getEuclideanDistance(prevCentroids[i], currCentroids[i])).average().getAsDouble();
	}
	
	private double getEuclideanDistance(double[] prevCentroid, double[] currCentroid)
	{
		double sum = 0;
		
		for (int i=prevCentroid.length-1; i>=0; i--)
			sum += MathUtils.sq(prevCentroid[i] - currCentroid[i]);
		
		return Math.sqrt(sum);
	}
}
