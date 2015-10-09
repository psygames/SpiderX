package com.shit.db;

import com.shit.config.SpFlag;
import com.shit.domain.User;
import com.shit.queue.QueueManager;

public class JoinUserDao implements Runnable
{
	public static void startUp(String name)
	{
		try
		{
			Thread t = new Thread(new JoinUserDao());
			t.setName(JoinUserDao.class.getName() + " -- " + name);
			t.start();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	MongoUtil mongo = null;

	public JoinUserDao()
	{
		mongo = new MongoUtil("spider", "user");
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				User user = QueueManager.instance.joinUser.pop();
				if (user != null)
				{
					long count = mongo.count("{qq:'" + user.getQq() + "'}");
					if (count == 0)
					{
						user.setFlag(SpFlag.USER_NEW);
						user.setAuthority(false);
						mongo.insertOne(user);
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
