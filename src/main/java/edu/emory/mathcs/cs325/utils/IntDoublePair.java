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
public class IntDoublePair implements Comparable<IntDoublePair>
{
	private int i;
	private double d;
	
	public IntDoublePair(int i, double d)
	{
		set(i, d);
	}

	public int getInt()
	{
		return i;
	}
	
	public double getDouble()
	{
		return d;
	}
	
	public void set(int i, double d)
	{
		setInt(i);
		setDouble(d);
	}
	
	public void setInt(int i)
	{
		this.i = i;
	}
	
	public void setDouble(double d)
	{
		this.d = d;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(IntDoublePair o)
	{
		return (int)Math.signum(d - o.d);
	}
}
