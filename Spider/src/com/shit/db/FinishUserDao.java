package com.shit.db;

import com.shit.config.SpFlag;
import com.shit.domain.User;
import com.shit.queue.QueueManager;

public class FinishUserDao implements Runnable
{
	public static void startUp(String name)
	{
		try
		{
			Thread t = new Thread(new FinishUserDao());
			t.setName(FinishUserDao.class.getName() +" -- " + name);
			t.start();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	MongoUtil mongo = null;

	public FinishUserDao()
	{
		mongo = new MongoUtil("spider", "user");
	}

	@Override
	public void run()
	{
		while (true)
		{
			User u = QueueManager.instance.finishUser.pop();
			if (u != null)
			{
				String filter = "{qq:'" + u.getQq() + "'}";

				User newUser = new User();
				if (u.getAuthority())
					newUser.setNickname(u.getNickname());
				newUser.setAuthority(u.getAuthority());
				newUser.setFlag(SpFlag.USER_FINISH);
				mongo.updateOne(filter, newUser.toJson());
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
