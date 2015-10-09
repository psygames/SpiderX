package com.shit.queue;

import java.util.LinkedList;

public class SyncQueue<E> extends LinkedList<E>
{
	private static final long serialVersionUID = -3058944141016556632L;
	private int totalPopCount;
	private int totalAddCount;
	private String name = "";

	public SyncQueue()
	{
		this.name = "default";
	}

	public SyncQueue(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public synchronized boolean add(E e)
	{
		this.totalAddCount++;
		super.add(e);
		return true;
	};

	public synchronized E pop()
	{
		if(size()>0)
			this.totalPopCount++;
		return this.poll();
	}

	public int getTotalAddCount()
	{
		return this.totalAddCount;
	}

	public int getTotalPopCount()
	{
		return this.totalPopCount;
	}
}
