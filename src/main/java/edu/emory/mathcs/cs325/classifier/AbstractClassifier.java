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
package edu.emory.mathcs.cs325.classifier;

import java.util.List;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public abstract class AbstractClassifier
{
	/**
	 * Adds a training instance to the classifier.
	 * @param label the class label.
	 * @param features the features.
	 */
	public abstract void addInstance(String label, List<StringFeature> features);
	
	/** Trains this classifier. */
	public abstract void train();
	
	/**
	 * @return the list of predictions given the features. 
	 * @param features the features.
	 */
	public abstract List<Prediction> predict(List<StringFeature> features);
}
