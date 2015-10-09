package com.shit.db;

import com.shit.domain.Talk;
import com.shit.queue.QueueManager;

public class JoinTalkDao implements Runnable
{
	public static void startUp(String name)
	{
		try
		{
			Thread t = new Thread(new JoinTalkDao());
			t.setName(JoinTalkDao.class.getName()+ " -- "+name);
			t.start();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	MongoUtil mongo = null;

	public JoinTalkDao()
	{
		mongo = new MongoUtil("spider", "talk");
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				Talk talk = QueueManager.instance.joinTalk.pop();
				if (talk != null)
				{
					long count = mongo.count("{key:'" + talk.getKey()+ "'}");
					if (count == 0)
					{
						mongo.insertOne(talk);
					}
				}
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}
			
			
			try
			{
				Thread.sleep(1);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

	}

}
