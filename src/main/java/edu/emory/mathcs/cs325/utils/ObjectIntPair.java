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
public class ObjectIntPair<T>
{
	private T o;
	private int i;
	
	public ObjectIntPair(T o, int i)
	{
		set(o, i);
	}
	
	public T getObject()
	{
		return o;
	}

	public int getInt()
	{
		return i;
	}
	
	public void set(T o, int i)
	{
		setObject(o);
		setInt(i);
	}
	
	public void setObject(T o)
	{
		this.o = o;
	}
	
	public void setInt(int i)
	{
		this.i = i;
	}
}
