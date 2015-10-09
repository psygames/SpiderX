package com.shit.domain;

import org.bson.Document;
import com.shit.tools.DomainValueFormater;

public class User extends Document
{
	/** 
	 * @generate
	 * String qq;
	 * String nickname;
	 * int flag;
	 * boolean authority;
	 */

	private static final long serialVersionUID = 1L;

	public User(){}
	public User(Document doc)
	{
		for(Entry<String, Object> kv : doc.entrySet())
		{
			Object val = DomainValueFormater.formatValue(kv.getKey(), kv.getValue());
			this.put(kv.getKey(), val);
		}
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

	public int getFlag()
	{
		return (int) this.get("flag");
	}

	public void setFlag(int flag)
	{
		this.put("flag", flag);
	}

	public boolean getAuthority()
	{
		return (boolean) this.get("authority");
	}

	public void setAuthority(boolean authority)
	{
		this.put("authority", authority);
	}

	public static User parse(Document doc)
	{
		if (doc != null)
			return new User(doc);
		return null;
	}
}
