package com.shit.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.shit.datahandle.SpHandle;

public class NetworkManager
{
	private static NetworkManager instance = null;

	public static NetworkManager getInstance()
	{
		if (instance == null)
			instance = new NetworkManager();
		return instance;
	}

	private Map<String, ArrayList<SpHandle>> handlesMap = null;
	private Map<String, Long> lastHandleTotalDownloadSizeMap = null;
	private long lastTotalDownloadSize = 0L;

	public NetworkManager()
	{
		handlesMap = new HashMap<String, ArrayList<SpHandle>>();
		lastHandleTotalDownloadSizeMap = new HashMap<String, Long>();
	}

	public void add(String type, SpHandle handle)
	{
		if (!handlesMap.containsKey(type))
		{
			handlesMap.put(type, new ArrayList<SpHandle>());
			lastHandleTotalDownloadSizeMap.put(type, 0L);
		}
		handlesMap.get(type).add(handle);
	}

	public List<SpHandle> getByType(String type)
	{
		if (handlesMap.containsKey(type))
			return handlesMap.get(type);
		return null;
	}

	public long getDownloadSizeByType(String type)
	{
		long lastSize = 0;
		long size = getTotalDownloadSizeByType(type);
		Long lsize = lastHandleTotalDownloadSizeMap.get(type);
		if (lsize != null)
			lastSize = lsize;
		lastHandleTotalDownloadSizeMap.put(type, size);
		return size - lastSize;
	}

	public long getTotalDownloadSizeByType(String type)
	{
		long size = 0;
		List<SpHandle> handles = getByType(type);
		if (handles != null)
		{
			for (SpHandle h : handles)
			{
				size += h.getTotalDownloadSize();
			}
		}
		return size;
	}

	public long getTotalDownloadSize()
	{
		long size = 0;
		for (Entry<String, ArrayList<SpHandle>> kv : handlesMap.entrySet())
		{
			size += getTotalDownloadSizeByType(kv.getKey());
		}
		return size;
	}

	public long getDownloadSize()
	{
		long lastsize = lastTotalDownloadSize;
		lastTotalDownloadSize = getTotalDownloadSize();
		return lastTotalDownloadSize - lastsize;
	}
}
