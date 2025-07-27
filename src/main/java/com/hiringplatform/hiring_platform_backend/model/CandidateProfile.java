package com.hiringplatform.hiring_platform_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

/**
 * This class represents a candidate's professional profile.
 * Each instance will be stored as a document in the "CandidateProfiles" collection.
 */
@Document(collection = "CandidateProfiles")
public class CandidateProfile {

    @Id
    private String id;

    private String userId; // This will link the profile to a User account (from the 'users' collection)
    private String fullName;
    private String email;
    private int totalExperience; // in years
    private List<String> skills;
    private String resumeUrl; // A link to an externally hosted resume (e.g., S3, Google Drive)

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
