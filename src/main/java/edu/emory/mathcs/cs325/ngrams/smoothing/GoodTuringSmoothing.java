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
package edu.emory.mathcs.cs325.ngrams.smoothing;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class GoodTuringSmoothing implements ISmoothing
{
	@Override
	public double getUnseenLikelihood()
	{
		return 0;
	}

	@Override
	public Map<String,Double> getProbabilityMap(Map<String, Long> countMap, long totalCounts)
	{
		Map<String,Double> map = new HashMap<>(countMap.size());
		
		for (Entry<String,Long> entry : countMap.entrySet())
			map.put(entry.getKey(), (double)entry.getValue() / totalCounts);
		
		return map;
	}

	@Override
	public ISmoothing createInstance()
	{
		return new GoodTuringSmoothing();
	}
}
