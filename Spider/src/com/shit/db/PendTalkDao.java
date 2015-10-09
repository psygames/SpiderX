package com.shit.db;

import com.shit.config.QueueConfig;
import com.shit.config.SpFlag;
import com.shit.domain.User;
import com.shit.queue.QueueManager;
import com.shit.tools.SpThreadCtrl;

public class PendTalkDao implements Runnable
{
	public static void startUp(String name)
	{
		try
		{
			Thread t = new Thread(new PendTalkDao());
			t.setName(PendUserDao.class.getName() + " -- " + name);
			t.start();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	MongoUtil mongo = null;

	public PendTalkDao()
	{
		mongo = new MongoUtil("spider", "user");
	}

	@Override
	public void run()
	{
		while (true)
		{
			int qsize = QueueManager.instance.pendTalk.size();
			if (SpThreadCtrl.pendTalkDao && qsize <= QueueConfig.PEND_SPIDER_TALK_QUEUE_NEED_ADD_MIN_SIZE)
			{
				User u = User.parse((mongo.findOneAndUpdate("{flag:"+SpFlag.USER_FINISH+",authority:true}", "{flag:"+SpFlag.TALK_POP+"}")));

				if (u != null)
				{
					QueueManager.instance.pendTalk.add(u);
				}
			}

			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
