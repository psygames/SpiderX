package com.shit.tools;

import java.util.List;

import org.bson.Document;

import com.shit.db.MongoUtil;
import com.shit.queue.QueueManager;
import com.shit.queue.SyncQueue;

public class SpThreadCtrl implements Runnable
{
	public static boolean pendUserDao = true;
	public static boolean pendTalkDao = true;

	private MongoUtil mongo = null;

	public static void startUp()
	{
		Thread t = new Thread(new SpThreadCtrl());
		t.setName("SpThreadCtrl -- ");
		t.start();
	}

	public SpThreadCtrl()
	{
		mongo = new MongoUtil("spider", "status");
	}

	@Override
	public void run()
	{
		boolean running = true;
		while (true)
		{
			Document doc = mongo.findOne("{version:'0.0.1'}");
			pendUserDao = doc.getBoolean("pendUserDao");
			pendTalkDao = doc.getBoolean("pendTalkDao");

			if (!pendUserDao && !pendTalkDao)
			{
				running = false;
				List<SyncQueue<?>> queueList = QueueManager.instance.queueList;
				for (SyncQueue<?> q : queueList)
				{
					if(q.size()>0)
					{
						running = true;
						break;
					}
				}
			}
			
			if(!running)
				System.out.println("All the queue has been clear !!! ");

			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
