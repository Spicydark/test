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

/**
 * The concrete implementation of the SearchRepository interface.
 * This class uses the native MongoDB Java driver to build and execute
 * a powerful aggregation pipeline for text-based searches, specifically
 * leveraging MongoDB Atlas Search features.
 */
@Repository
public class SearchRepositoryImpl implements SearchRepository{
	
	/**
	 * Injected MongoClient for low-level interaction with the MongoDB server.
	 */
	@Autowired
	MongoClient client;
	
	/**
	 * Injected converter to manually map BSON Documents back to Java POJOs (JobPosting).
	 */
	@Autowired
	MongoConverter converter;
	
	/**
	 * Performs an advanced text search on the JobPostings collection using an Atlas Search aggregation pipeline.
	 *
	 * @param text The search keyword provided by the user.
	 * @return A List of JobPosting objects that match the search criteria, sorted by experience and limited to 5 results.
	 */
	@Override
	public List<JobPosting> findByText(String text) {
		final List<JobPosting> posts = new ArrayList<>();
		
		// Get a handle to the specific database and collection.
		MongoDatabase database = client.getDatabase("hiring-platform");
		MongoCollection<Document> collection = database.getCollection("JobPostings");
		
		// Execute the aggregation pipeline.
		AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
			// Stage 1: Use Atlas Search ($search) for efficient, indexed text searching.
			new Document("$search", 
				new Document("text", 
					new Document("query", text) // Use the dynamic search text from the method parameter.
						.append("path", Arrays.asList("skillSet", "description", "role")) // Specify the fields to search within.
				)
			), 
			// Stage 2: Sort the results by the 'experience' field in ascending order (1).
			new Document("$sort", 
				new Document("experience", 1L)
			), 
			// Stage 3: Limit the number of returned documents to 5.
			new Document("$limit", 5L)
		));
		
		// Iterate through the raw BSON Document results and convert each one
		// back into a JobPosting Java object.
		result.forEach(doc -> posts.add(converter.read(JobPosting.class, doc)));
		
		return posts;
	}

}
