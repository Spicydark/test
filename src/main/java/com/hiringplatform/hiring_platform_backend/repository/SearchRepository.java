package com.hiringplatform.hiring_platform_backend.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hiringplatform.hiring_platform_backend.model.*;

/**
 * Defines a contract for custom search operations on JobPostings.
 * This interface separates the advanced search logic from the standard
 * CRUD operations provided by JobPostingRepository, allowing for a more
 * complex and dedicated implementation.
 */
@Component
public interface SearchRepository {
	
	/**
	 * Performs a text-based search across multiple fields of the JobPosting documents.
	 * The specific implementation will define the search mechanism (e.g., regex or Atlas Search).
	 *
	 * @param text The keyword or phrase to search for.
	 * @return A List of JobPosting objects that match the search criteria.
	 */
	List<JobPosting> findByText(String text);

}
