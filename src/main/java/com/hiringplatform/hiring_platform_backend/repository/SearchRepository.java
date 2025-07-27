package com.hiringplatform.hiring_platform_backend.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hiringplatform.hiring_platform_backend.model.*;

@Component
public interface SearchRepository {
	
	List<JobPosting> findByText(String text);

}
