package com.shit.main;

public class Main
{
	public static void main(String[] args)
	{
		Thread t = new Thread(new SpiderManager());
		t.start();
	}
}
