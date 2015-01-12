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
public class StringDoublePair implements Comparable<StringDoublePair>
{
	private String s;
	private double d;
	
	public StringDoublePair(String s, double d)
	{
		set(s, d);
	}
	
	public StringDoublePair(StringDoublePair p)
	{
		set(p.s, p.d);
	}
	
	public String getString()
	{
		return s;
	}

	public double getDouble()
	{
		return d;
	}

	public void set(String s, double d)
	{
		setString(s);
		setDouble(d);
	}
	
	public void setString(String s)
	{
		this.s = s;
	}
	
	public void setDouble(double d)
	{
		this.d = d;
	}

	@Override
	public int compareTo(StringDoublePair o)
	{
		return (int)Math.signum(d - o.d);
	}
}
