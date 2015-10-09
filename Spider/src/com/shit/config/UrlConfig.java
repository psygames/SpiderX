package com.shit.config;

public class UrlConfig
{
	public static final String BSAE_USER_ADD_URL = "http://ic2.s21.qzone.qq.com/cgi-bin/feeds/feeds_html_module?i_uin=";
	public static final String BSAE_TALK_ADD_URL_HEAD = "http://ic2.qzone.qq.com/cgi-bin/feeds/feeds_html_act_all?hostuin=";
	public static final String BSAE_TALK_ADD_URL_MIDDLE = "&scope=0&filter=all&flag=1&refresh=0&firstGetGroup=0&mixnocache=0&scene=0&begintime=undefined&icServerTime=&start=";
	public static final String BSAE_TALK_ADD_URL_TAIL = "&count=10";		
			
	public static final String getTalkAddUrl(String qq,int start)
	{
		return BSAE_TALK_ADD_URL_HEAD + qq + BSAE_TALK_ADD_URL_MIDDLE +start+ BSAE_TALK_ADD_URL_TAIL;
	}
	
	public static final String HAS_TALKSHOW_FLAG = "class=\\x22f-info\\x22>";

	public static final String TALKSHOW_APPID_START_FLAG = ",appid:'";
	public static final String TALKSHOW_APPID_END_FLAG = "'";
	
	public static final String TALKSHOW_KEY_START_FLAG = ",key:'";
	public static final String TALKSHOW_KEY_END_FLAG = "'";
	
	public static final String TALKSHOW_DATE_START_FLAG = "abstime:'";
	public static final String TALKSHOW_DATE_END_FLAG = "'";
	
	public static final String TALKSHOW_MSG_START_FLAG = "class=\\x22f-info\\x22>"; // class=\x22f-info\x22>
	public static final String TALKSHOW_MSG_END_FLAG = "\\x3C\\/div>"; // \x3C\/div>

	public static final String TALKSHOW_COMMENT_START_FLAG = "icon-comment\\x22>\\x3C\\/i>评论("; //icon-comment\x22>\x3C\/i>
	public static final String TALKSHOW_COMMENT_END_FLAG = ")\\x3C\\/a>";
	
	public static final String TALKSHOW_PHONE_STYLE_START_FLAG = "phone-style state\\x22>"; //icon-comment\x22>\x3C\/i>
	public static final String TALKSHOW_PHONE_STYLE_END_FLAG = "\\x3C\\/a>";
	
	public static final String TALKSHOW_MESSAGE_TYPE_START_FLAG = ",typeid:'"; //icon-comment\x22>\x3C\/i>
	public static final String TALKSHOW_MESSAGE_TYPE_END_FLAG = "'";
	
	public static final String TALKSHOW_EXTEND_URL_START_FLAG = "data-originurl=\\x22"; //icon-comment\x22>\x3C\/i>
	public static final String TALKSHOW_EXTEND_URL_END_FLAG = "\\x22";
	
}
