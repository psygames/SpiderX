package com.shit.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DomainValueFormater
{
	public static List<String> className = new ArrayList<String>()
	{
		private static final long serialVersionUID = 1L;
		{
			add("User");
			add("Talk");
			add("Comment");
		}
	};

	public static Object formatValue(String type ,Object obj)
	{
		type = String.valueOf(type.charAt(0)).toUpperCase()+type.substring(1);
		if(className.contains(type))
		{
			try
			{
				Class<?> cls = Class.forName("com.shit.domain."+type);
				Constructor<?> []constructor = cls.getConstructors();
				constructor[1].newInstance(obj);
			} catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e)
			{
				e.printStackTrace();
			}
		}
		return obj;
	}
}
