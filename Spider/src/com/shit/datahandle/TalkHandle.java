package com.shit.datahandle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shit.config.UrlConfig;
import com.shit.domain.Talk;
import com.shit.domain.User;
import com.shit.queue.QueueManager;
import com.shit.web.SpHttpClient;

public class TalkHandle implements Runnable, SpHandle
{
	private SpHttpClient myHttp = null;

	public TalkHandle()
	{
		myHttp = new SpHttpClient();
	}

	public static TalkHandle startUp(String name)
	{
		TalkHandle th = new TalkHandle();
		Thread t = new Thread(th);
		t.setName(TalkHandle.class.getName() + " -- " + name);
		t.start();
		return th;
	}

	@Override
	public void run()
	{
		while (true)
		{
			if (QueueManager.instance.joinTalk.size() < 1000)
			{
				User pendTalk = QueueManager.instance.pendTalk.pop();
				if (pendTalk != null)
				{
					int start = 0;
					String qq = pendTalk.getQq();
					String url = UrlConfig.getTalkAddUrl(qq, start);
					String pageData = myHttp.getContentFormUrl(url, "utf-8");
					while (pageData != null && pageData.contains(UrlConfig.HAS_TALKSHOW_FLAG))
					{
						List<Talk> allTalks = searchAllTalks(url, pageData, qq,pendTalk.getNickname());
						for (Talk talk : allTalks)
						{
							QueueManager.instance.joinTalk.add(talk);
						}

						start += 10;
						url = UrlConfig.getTalkAddUrl(qq, start);
						pageData = myHttp.getContentFormUrl(url, "utf-8");
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

	public List<Talk> searchAllTalks(String url, String pageData, String selfQq,String selfNickname)
	{
		String tagSplit = UrlConfig.TALKSHOW_APPID_START_FLAG;

		String tagAppid = UrlConfig.TALKSHOW_APPID_START_FLAG;
		String tagAppidEnd = UrlConfig.TALKSHOW_APPID_END_FLAG;

		String tagKey = UrlConfig.TALKSHOW_KEY_START_FLAG;
		String tagKeyEnd = UrlConfig.TALKSHOW_KEY_END_FLAG;

		String tagDate = UrlConfig.TALKSHOW_DATE_START_FLAG;
		String tagDateEnd = UrlConfig.TALKSHOW_DATE_END_FLAG;

		String tagMsg = UrlConfig.TALKSHOW_MSG_START_FLAG;
		String tagMsgEnd = UrlConfig.TALKSHOW_MSG_END_FLAG;

		String tagComc = UrlConfig.TALKSHOW_COMMENT_START_FLAG;
		String tagComcEnd = UrlConfig.TALKSHOW_COMMENT_END_FLAG;

		String tagPhoneType = UrlConfig.TALKSHOW_PHONE_STYLE_START_FLAG;
		String tagPhoneTypeEnd = UrlConfig.TALKSHOW_PHONE_STYLE_END_FLAG;

		String tagMessageType = UrlConfig.TALKSHOW_MESSAGE_TYPE_START_FLAG;
		String tagMessageTypeEnd = UrlConfig.TALKSHOW_MESSAGE_TYPE_END_FLAG;

		String tagExtendUrl = UrlConfig.TALKSHOW_EXTEND_URL_START_FLAG;
		String tagExtendUrlEnd = UrlConfig.TALKSHOW_EXTEND_URL_END_FLAG;

		List<Talk> talks = new ArrayList<Talk>();

		String[] splitTalks = pageData.split(tagSplit);
		for (int i = 1; i < splitTalks.length; i++)
		{
			String data = tagSplit + splitTalks[i];

			String appid = getContentStrByTag(data, tagAppid, tagAppidEnd);
			String key = getContentStrByTag(data, tagKey, tagKeyEnd);
			Date date = getContentDateByTag(data, tagDate, tagDateEnd);
			String message = getContentStrByTag(data, tagMsg, tagMsgEnd);
			int commentTimes = getContentIntByTag(data, tagComc, tagComcEnd);
			String extendUrl = getContentStrByTag(data, tagExtendUrl, tagExtendUrlEnd);
			String messageType = getContentStrByTag(data, tagMessageType, tagMessageTypeEnd);
			String phoneType = getContentStrByTag(data, tagPhoneType, tagPhoneTypeEnd);

			message = formatMessage(message);
			extendUrl = formatMessage(extendUrl);
			
			Talk talk = new Talk();
			talk.setAppid(appid);
			talk.setQq(selfQq);
			talk.setNickname(selfNickname);
			talk.setKey(key);
			talk.setDateTime(date);
			talk.setMessage(message);
			talk.setCommentCount(commentTimes);
			talk.setPraiseCount(0);
			talk.setMessageType(messageType);
			talk.setPhoneType(phoneType);
			talk.setExtendUrl(extendUrl);

			if (checkTalkMsg(talk) && !talks.contains(talk))
			{
				talks.add(talk);
			}
		}

		return talks;
	}

	public String formatMessage(String message)
	{
		message = message.replace("\\x3C", "<");
		message = message.replace("\\/", "/");
		message = message.replace("&amp;", "&");
		message = message.replace("\\x22", "'");
		return message;
	}

	public String getContentStrByTag(String data, String tagStart, String tagEnd)
	{
		int sIndex = data.indexOf(tagStart);
		int tagStartIndex = sIndex + tagStart.length();
		String result = (sIndex == -1) ? "" : data.substring(tagStartIndex, data.indexOf(tagEnd, tagStartIndex));
		return result;
	}

	public int getContentIntByTag(String data, String tagStart, String tagEnd)
	{
		int sIndex = data.indexOf(tagStart);
		int tagStartIndex = sIndex + tagStart.length();
		String result = (sIndex == -1) ? "0" : data.substring(tagStartIndex, data.indexOf(tagEnd, tagStartIndex));
		return Integer.parseInt(result);
	}

	public Date getContentDateByTag(String data, String tagStart, String tagEnd)
	{
		int sIndex = data.indexOf(tagStart);
		int tagStartIndex = sIndex + tagStart.length();
		String result = (sIndex == -1) ? "0" : data.substring(tagStartIndex, data.indexOf(tagEnd, tagStartIndex));
		return new Date(Long.parseLong(result) * 1000);
	}

	public boolean checkTalkMsg(Talk talkMsg)
	{
		return true;
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