package com.shit.db;

import com.shit.config.QueueConfig;
import com.shit.config.SpFlag;
import com.shit.domain.User;
import com.shit.queue.QueueManager;
import com.shit.tools.SpThreadCtrl;

public class PendUserDao implements Runnable
{
	public static void startUp(String name)
	{
		try
		{
			Thread t = new Thread(new PendUserDao());
			t.setName(PendUserDao.class.getName() +" -- " + name);
			t.start();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	MongoUtil mongo = null;

	public PendUserDao()
	{
		mongo = new MongoUtil("spider", "user");
	}

	@Override
	public void run()
	{
		while (true)
		{
			int qsize = QueueManager.instance.pendUser.size();
			if (SpThreadCtrl.pendUserDao && qsize <= QueueConfig.PEND_SPIDER_USER_QUEUE_NEED_ADD_MIN_SIZE)
			{
				User u = User.parse((mongo.findOneAndUpdate("{flag:"+SpFlag.USER_NEW+"}", "{flag:"+SpFlag.USER_POP+"}")));

				if (u != null)
				{
					QueueManager.instance.pendUser.add(u);
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
