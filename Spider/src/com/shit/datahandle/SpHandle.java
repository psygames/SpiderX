package com.shit.datahandle;

import com.shit.web.SpHttpClient;

public interface SpHandle
{
	public SpHttpClient getSpHttpClient();
	public long getTotalDownloadSize();
}
