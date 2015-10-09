package com.shit.db;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoUtil
{
	MongoClient client = null;
	MongoDatabase db = null;
	MongoCollection<Document> collection = null;

	public MongoUtil(String dbStr, String collectionStr)
	{
		client = new MongoClient();
		db = client.getDatabase(dbStr);
		collection = db.getCollection(collectionStr);
	}

	private Document documentConvert(Document doc)
	{
		if (!doc.getClass().equals(Document.class))
			return Document.parse(doc.toJson());
		return doc;
	}

	public void insertOne(Document doc)
	{
		doc = documentConvert(doc);
		collection.insertOne(doc);
	}

	public void insertOne(String json)
	{
		collection.insertOne(Document.parse(json));
	}

	public void updateOne(String filterJson, String updateJson)
	{
		updateOne(Document.parse(filterJson), Document.parse(updateJson));
	}

	public void updateOne(Document filterDoc, Document updateDoc)
	{
		filterDoc = documentConvert(filterDoc);
		updateDoc = documentConvert(updateDoc);
		Document dUpdate = new Document();
		dUpdate.put("$set", updateDoc);
		collection.updateOne(filterDoc, dUpdate);
	}
	
	public Document findOne(String filterJson)
	{
		return findOne(Document.parse(filterJson));
	}
	
	public Document findOne(Document filterDoc)
	{
		filterDoc = documentConvert(filterDoc);
		return collection.find(filterDoc).first();
	}

	public Document findOneAndUpdate(String filterJson, String updateJson)
	{
		return findOneAndUpdate(Document.parse(filterJson), Document.parse(updateJson));
	}

	public Document findOneAndUpdate(Document filterDoc, Document updateDoc)
	{
		filterDoc = documentConvert(filterDoc);
		updateDoc = documentConvert(updateDoc);
		Document dUpdate = new Document();
		dUpdate.put("$set", updateDoc);
		return collection.findOneAndUpdate(filterDoc, dUpdate);
	}

	public long count(String filterJson)
	{
		return count(Document.parse(filterJson));
	}

	public long count(Document filterDoc)
	{
		filterDoc = documentConvert(filterDoc);
		return collection.count(filterDoc);
	}
}
