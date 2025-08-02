package com.hiringplatform.hiring_platform_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "JobPostings")
public class JobPosting {

    @Id
    private String id;
    private String recruiterId; 
    private String role;
    private String description;
    private int experience;
    private List<String> skillSet;

    // --- Getters and Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRecruiterId() { return recruiterId; } 
    public void setRecruiterId(String recruiterId) { this.recruiterId = recruiterId; } 
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    public List<String> getSkillSet() { return skillSet; }
    public void setSkillSet(List<String> skillSet) { this.skillSet = skillSet; }
}
