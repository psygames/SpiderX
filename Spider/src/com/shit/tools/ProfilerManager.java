package com.shit.tools;

import java.util.List;

import org.bson.Document;

import com.shit.db.MongoUtil;
import com.shit.queue.QueueManager;
import com.shit.queue.SyncQueue;

public class ProfilerManager implements Runnable
{
	public static void startUp()
	{
		Thread t = new Thread(new ProfilerManager());
		t.setName("TimeManager -- ");
		t.start();
	}

	public ProfilerManager()
	{
		init();
	}

	public void init()
	{
		mongo = new MongoUtil("spider", "profile");
		queueList = QueueManager.instance.queueList;
		lastQueueAdd = new int[queueList.size()];
		lastQueuePop = new int[queueList.size()];
	}

	MongoUtil mongo = null;
	List<SyncQueue<?>> queueList = null;
	int[] lastQueueAdd = null;
	int[] lastQueuePop = null;

	private String curLog = "";

	@Override
	public void run()
	{
		long time1 = System.currentTimeMillis();
		while (true)
		{
			long time2 = System.currentTimeMillis();
			if ((time2 - time1) >= 1000)
			{
				Document doc = new Document();
				doc.put("time", time2);
				String logStr = "---------------------------------------------------------\n";
				String logQName = "\t";
				String logAdd = "Add\t";
				String logPop = "Pop\t";
				String logSize = "Size\t";
				for (int i = 0; i < queueList.size(); i++)
				{
					SyncQueue<?> queue = queueList.get(i);
					String queueName = queue.getName();
					int queueAdd = getAdd(queue, i);
					int queuePop = getPop(queue, i);
					int queueSize = queue.size();
					
					doc.put(queueName+"Add", queueAdd);
					doc.put(queueName+"Pop", queuePop);
					doc.put(queueName+"Size", queueSize);
					
					logQName += queueName + "\t";
					logAdd += queueAdd + "\t";
					logPop += queuePop + "\t";
					logSize += queueSize + "\t";
				}
				
				
				String downloadStr = getDownloadSpeedFormatStr();
				doc.put("DownloadStr", downloadStr);
				
				logQName+= "Download\t";
				logPop+= downloadStr +"\t";

				logStr += logQName + "\n" + logAdd + "\n" + logPop
						+ "\n" + logSize + "\n---------------------------------------------------------\n";

				saveToDB(doc);
				System.out.println(logStr);
				time1 += 1000;
			}

			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	private int getAdd(SyncQueue<?> queue, int index)
	{
		int totalAdd = queue.getTotalAddCount();
		int lastadd = lastQueueAdd[index];
		lastQueueAdd[index] = totalAdd;
		int queueAdd = totalAdd - lastadd;
		return queueAdd;
	}

	private int getPop(SyncQueue<?> queue, int index)
	{
		int totalPop = queue.getTotalPopCount();
		int lastpop = lastQueuePop[index];
		lastQueuePop[index] = totalPop;
		int queuePop = totalPop - lastpop;
		return queuePop;
	}

	private long getDownloadSpeed()
	{
		return NetworkManager.getInstance().getDownloadSize();
	}

	private String getDownloadSpeedFormatStr()
	{
		long size = getDownloadSpeed();
		String rst = "";
		String unit = "B/s";
		if (size > 1024)
		{
			size /= 1024;
			unit = "K/s";
			if (size > 1024)
			{
				if (size < 10 * 1024)
				{
					size = size * 10 / 1024;
					rst = size / 10 + "." + size % 10;
				} else
				{
					size /= 1024;
				}
				unit = "M/s";
			}
		}

		if (rst == "")
			rst = "" + size;

		rst += " " + unit;
		return rst;
	}

	public void printAdd(String str)
	{
		while (str.length() < 15)
			str += " ";
		curLog += str;
	}

	public void printToConsole()
	{
		System.out.println(curLog);
	}

	public void saveLog(String path)
	{
		FileTools.writeToFile(curLog + "\r\n", path, true);
	}

	public void clearLog()
	{
		curLog = "";
	}
	
	public void saveToDB(Document doc)
	{
		mongo.insertOne(doc);
	}
}
