package com.shit.main;

import com.shit.datahandle.SpHandle;
import com.shit.datahandle.TalkHandle;
import com.shit.datahandle.UserHandle;
import com.shit.db.FinishUserDao;
import com.shit.db.JoinTalkDao;
import com.shit.db.JoinUserDao;
import com.shit.db.PendTalkDao;
import com.shit.db.PendUserDao;
import com.shit.queue.QueueManager;
import com.shit.tools.NetworkManager;
import com.shit.tools.ProfilerManager;
import com.shit.tools.SpThreadCtrl;

public class SpiderManager implements Runnable
{
	public static void startUp(String name)
	{
		Thread t = new Thread(new SpiderManager());
		t.setName("SpiderManager -- Stratup");
		t.start();
	}

	public void run()
	{
		QueueManager.init();
		
		PendUserDao.startUp("0");
		FinishUserDao.startUp("0");
		JoinUserDao.startUp("0");
		JoinUserDao.startUp("1");
		JoinUserDao.startUp("2");
		
		PendTalkDao.startUp("0");
		JoinTalkDao.startUp("0");
		JoinTalkDao.startUp("1");
		JoinTalkDao.startUp("2");
		JoinTalkDao.startUp("3");
		
		for(int i=0;i<5;i++)
		{
			SpHandle spHandle = UserHandle.startUp(String.valueOf(i));
			NetworkManager.getInstance().add(spHandle.getClass().getName(), spHandle);
		}
		
		for(int i=0;i<40;i++)
		{
			SpHandle spHandle = TalkHandle.startUp(String.valueOf(i));
			NetworkManager.getInstance().add(spHandle.getClass().getName(), spHandle);
		}
		
		ProfilerManager.startUp();
		SpThreadCtrl.startUp();
	}
}
