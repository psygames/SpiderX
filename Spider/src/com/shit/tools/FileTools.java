package com.shit.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileTools
{
	public static void writeToFile(String text, String path,boolean append)
	{
		try
		{
			File file = new File(path);
			if (!file.exists())
				file.createNewFile();
			FileOutputStream out = new FileOutputStream(file, append);
			out.write(text.getBytes());
			out.close();
		} catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public static String readFromFile(String path)
	{
		FileReader fr;
		BufferedReader br;
		String content = "";
		try
		{
			fr = new FileReader(path);
			br = new BufferedReader(fr);
			String tempString;
			while ((tempString = br.readLine()) != null)
			{
				content += tempString;
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return content;
	}
}
