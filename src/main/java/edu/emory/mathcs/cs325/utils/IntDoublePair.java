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
package edu.emory.mathcs.cs325.utils;

/**
 * @author Jinho D. Choi ({@code jinho.choi@emory.edu})
 */
public class IntDoublePair
{
	private int o1;
	private double o2;
	
	public IntDoublePair(int o1, double p2)
	{
		set(o1, o2);
	}

	public int getInt()
	{
		return o1;
	}
	
	public double getDouble()
	{
		return o2;
	}
	
	public void set(int o1, double o2)
	{
		setInt (o1);
		setDouble(o2);
	}
	
	public void setInt(int o)
	{
		o1 = o;
	}
	
	public void setDouble(double o)
	{
		o2 = o;
	}
}
