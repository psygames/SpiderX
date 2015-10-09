package com.shit.domain;

import org.bson.Document;
import com.shit.tools.DomainValueFormater;
import java.util.Date;

public class Comment extends Document
{
	/** 
	 * @generate
	 * String key;
	 * String qq;
	 * String message;
	 * String linkUrl;
	 * String messageType;
	 * String phoneType;
	 * Date dateTime;
	 */

	private static final long serialVersionUID = 1L;

	public Comment(){}
	public Comment(Document doc)
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

	public String getQq()
	{
		return (String) this.get("qq");
	}

	public void setQq(String qq)
	{
		this.put("qq", qq);
	}

	public String getMessage()
	{
		return (String) this.get("message");
	}

	public void setMessage(String message)
	{
		this.put("message", message);
	}

	public String getLinkUrl()
	{
		return (String) this.get("linkUrl");
	}

	public void setLinkUrl(String linkUrl)
	{
		this.put("linkUrl", linkUrl);
	}

	public String getMessageType()
	{
		return (String) this.get("messageType");
	}

	public void setMessageType(String messageType)
	{
		this.put("messageType", messageType);
	}

	public String getPhoneType()
	{
		return (String) this.get("phoneType");
	}

	public void setPhoneType(String phoneType)
	{
		this.put("phoneType", phoneType);
	}

	public Date getDateTime()
	{
		return (Date) this.get("dateTime");
	}

	public void setDateTime(Date dateTime)
	{
		this.put("dateTime", dateTime);
	}

	public static Comment parse(Document doc)
	{
		if (doc != null)
			return new Comment(doc);
		return null;
	}
}
