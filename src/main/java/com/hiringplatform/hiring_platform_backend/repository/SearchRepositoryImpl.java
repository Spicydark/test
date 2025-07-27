package com.hiringplatform.hiring_platform_backend.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Repository;

import com.hiringplatform.hiring_platform_backend.model.*;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Repository
public class SearchRepositoryImpl implements SearchRepository{
	
	@Autowired
	MongoClient client;
	
	@Autowired
	MongoConverter converter;
	
	@Override
	public List<JobPosting> findByText(String text) {
		final List<JobPosting> posts = new ArrayList<>();
		
		MongoDatabase database = client.getDatabase("hiring-platform");
		MongoCollection<Document> collection = database.getCollection("JobPostings");
		AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search", 
		    new Document("text", 
		    new Document("query", text)
		                .append("path", Arrays.asList("skillSet", "description", "role"))
		                .append("matchCriteria", "any"))), 
		    new Document("$sort", 
		    new Document("experience", 1L)), 
		    new Document("$limit", 5L)));
		
		for(Document post : result) {
			posts.add(converter.read(JobPosting.class, post));
		}
		
//		for more simpler		
//		result.forEach(docs -> posts.add(converter.read(JobPosting.class, docs)));
		
		return posts;
	}

}
