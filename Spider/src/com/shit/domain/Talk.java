package com.shit.domain;

import org.bson.Document;
import com.shit.tools.DomainValueFormater;
import java.util.Date;

public class Talk extends Document
{
	/** 
	 * @generate
	 * String key;
	 * String appid;
	 * String qq;
	 * String nickname;
	 * String phoneType;
	 * String message;
	 * String messageType;
	 * String extendUrl;
	 * Date dateTime;
	 * int commentCount;
	 * int praiseCount;
	 */

	private static final long serialVersionUID = 1L;

	public Talk(){}
	public Talk(Document doc)
	{
		for(Entry<String, Object> kv : doc.entrySet())
		{
			Object val = DomainValueFormater.formatValue(kv.getKey(), kv.getValue());
			this.put(kv.getKey(), val);
		}
	}

	public String getKey()
	{
		return (String) this.get("key");
	}

	public void setKey(String key)
	{
		this.put("key", key);
	}

	public String getAppid()
	{
		return (String) this.get("appid");
	}

	public void setAppid(String appid)
	{
		this.put("appid", appid);
	}

	public String getQq()
	{
		return (String) this.get("qq");
	}

	public void setQq(String qq)
	{
		this.put("qq", qq);
	}

	public String getNickname()
	{
		return (String) this.get("nickname");
	}

	public void setNickname(String nickname)
	{
		this.put("nickname", nickname);
	}

	public String getPhoneType()
	{
		return (String) this.get("phoneType");
	}

	public void setPhoneType(String phoneType)
	{
		this.put("phoneType", phoneType);
	}

	public String getMessage()
	{
		return (String) this.get("message");
	}

	public void setMessage(String message)
	{
		this.put("message", message);
	}

	public String getMessageType()
	{
		return (String) this.get("messageType");
	}

	public void setMessageType(String messageType)
	{
		this.put("messageType", messageType);
	}

	public String getExtendUrl()
	{
		return (String) this.get("extendUrl");
	}

	public void setExtendUrl(String extendUrl)
	{
		this.put("extendUrl", extendUrl);
	}

	public Date getDateTime()
	{
		return (Date) this.get("dateTime");
	}

	public void setDateTime(Date dateTime)
	{
		this.put("dateTime", dateTime);
	}

	public int getCommentCount()
	{
		return (int) this.get("commentCount");
	}

	public void setCommentCount(int commentCount)
	{
		this.put("commentCount", commentCount);
	}

	public int getPraiseCount()
	{
		return (int) this.get("praiseCount");
	}

	public void setPraiseCount(int praiseCount)
	{
		this.put("praiseCount", praiseCount);
	}

	public static Talk parse(Document doc)
	{
		if (doc != null)
			return new Talk(doc);
		return null;
	}
}
