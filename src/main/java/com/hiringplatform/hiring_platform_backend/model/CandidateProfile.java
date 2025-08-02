package com.hiringplatform.hiring_platform_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

/**
 * Represents a candidate's detailed professional profile in the database.
 * This model is distinct from the User model, as it stores application-specific
 * information rather than authentication credentials.
 */
@Document(collection = "CandidateProfiles")
public class CandidateProfile {

    /**
     * The unique identifier for the candidate profile document.
     */
    @Id
    private String id;

    /**
     * A foreign key linking this profile to a specific User account in the 'users' collection.
     * This ensures that each profile is owned by a registered user.
     */
    private String userId;

    /**
     * The full name of the candidate.
     */
    private String fullName;

    /**
     * The contact email address for the candidate.
     */
    private String email;

    /**
     * The candidate's total years of professional experience.
     */
    private int totalExperience;

    /**
     * A list of the candidate's professional skills.
     */
    private List<String> skills;

    /**
     * A URL pointing to the candidate's externally hosted resume (e.g., in a cloud storage service).
     */
    private String resumeUrl;

    // --- Getters and Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(int totalExperience) {
        this.totalExperience = totalExperience;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }
}
