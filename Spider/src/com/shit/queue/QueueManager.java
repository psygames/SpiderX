package com.shit.queue;

import java.util.ArrayList;
import java.util.List;

import com.shit.domain.Talk;
import com.shit.domain.User;

public class QueueManager
{
	public static QueueManager instance = null;
	public static void init()
	{
		if(instance == null)
			instance = new QueueManager();
	}
	
	public SyncQueue<User> pendUser = null;
	public SyncQueue<User> finishUser = null;
	public SyncQueue<User> joinUser = null;
	public SyncQueue<User> pendTalk = null;
	public SyncQueue<Talk> joinTalk = null;
	
	public List<SyncQueue<?>> queueList = null;
	
	public QueueManager ()
	{
		pendUser = new SyncQueue<>("UPend");
		finishUser = new SyncQueue<>("UFsh");
		joinUser = new SyncQueue<>("UJoin");
		pendTalk = new SyncQueue<>("TPend");
		joinTalk = new SyncQueue<>("TJoin");
		
		queueList = new ArrayList<SyncQueue<?>>();
		queueList.add(pendUser);
		queueList.add(finishUser);
		queueList.add(joinUser);
		queueList.add(pendTalk);
		queueList.add(joinTalk);
	}
	
	
}
