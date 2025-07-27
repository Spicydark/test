package com.hiringplatform.hiring_platform_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

/**
 * This class represents a single job posting in our database.
 * Each instance of this class will be stored as a document in the "JobPostings" collection.
 */
@Document(collection = "JobPostings") // Tells MongoDB to store this in a collection named "JobPostings"
public class JobPosting {

    @Id // Marks this field as the primary key for the document
    private String id;

    private String role;
    private String description;
    private int experience; // Required experience in years
    private List<String> skillSet; // A list of required skills

    // --- Getters and Setters ---
    // Lombok will generate these for us automatically, but we write them here for clarity.

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public List<String> getSkillSet() {
        return skillSet;
    }

    public void setSkillSet(List<String> skillSet) {
        this.skillSet = skillSet;
    }

    @Override
    public String toString() {
        return "JobPosting{" +
                "id='" + id + '\'' +
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                ", experience=" + experience +
                ", skillSet=" + skillSet +
                '}';
    }
}

