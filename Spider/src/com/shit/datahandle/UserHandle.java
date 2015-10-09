package com.shit.datahandle;

import java.util.ArrayList;
import java.util.List;

import com.shit.config.UrlConfig;
import com.shit.domain.User;
import com.shit.queue.QueueManager;
import com.shit.web.SpHttpClient;

public class UserHandle implements Runnable, SpHandle
{
	private SpHttpClient myHttp = null;

	public UserHandle()
	{
		myHttp = new SpHttpClient();
	}

	public static UserHandle startUp(String name)
	{
		UserHandle uh = new UserHandle();
		Thread t = new Thread(uh);
		t.setName(UserHandle.class.getName() + " -- " + name);
		t.start();
		return uh;
	}

	@Override
	public void run()
	{
		while (true)
		{
			if (QueueManager.instance.joinUser.size() < 1000)
			{
				User pendUser = QueueManager.instance.pendUser.pop();
				if (pendUser != null)
				{
					String qq = pendUser.getQq();
					String pageData = myHttp.getContentFormUrl(UrlConfig.BSAE_USER_ADD_URL + qq, "iso-8859-1");

					if (pageData != null)
					{
						String nickname = searchNickName(pageData);
						if (nickname == null)
						{
							pendUser.setNickname("");
							pendUser.setAuthority(false);
						} else
						{
							pendUser.setNickname(nickname);
							pendUser.setAuthority(true);
						}

						QueueManager.instance.finishUser.add(pendUser);

						List<String> otherQqs = searchOtherUsers(pageData, qq);
						for (String otrqq : otherQqs)
						{
							User u = new User();
							u.setQq(otrqq);
							QueueManager.instance.joinUser.add(u);
						}
					}
				}
			}
			try
			{
				Thread.sleep(5);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}

		}
	}

	public String searchNickName(String pageData)
	{
		String tag = "nickname:'";

		int sIndex = pageData.indexOf(tag);
		if (sIndex == -1)
			return null;

		pageData = pageData.substring(sIndex + tag.length());
		String nickname = pageData.substring(0, pageData.indexOf('\''));
		return nickname;
	}

	public List<String> searchOtherUsers(String pageData, String selfQq)
	{
		String tag = "c_tx q_namecard\"  link=\"nameCard_";

		List<String> users = new ArrayList<String>();

		int sIndex = pageData.indexOf(tag);
		while (sIndex != -1)
		{
			pageData = pageData.substring(sIndex + tag.length());
			String qq = pageData.substring(0, pageData.indexOf('\"'));
			if (qq != selfQq && checkQq(qq) && !users.contains(qq))
			{
				users.add(qq);
			}

			sIndex = pageData.indexOf(tag);
		}

		return users;
	}

	public boolean checkQq(String qq)
	{
		if (qq.length() > 3 && qq.length() <= 10)
		{
			for (char c : qq.toCharArray())
			{
				if (c < '0' || c > '9')
					return false;
			}
			return true;
		}

		return false;
	}

	@Override
	public SpHttpClient getSpHttpClient()
	{
		return myHttp;
	}

	@Override
	public long getTotalDownloadSize()
	{
		return myHttp.getTotalDownloadSize();
	}

}