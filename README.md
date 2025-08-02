Hiring Platform Backend
A secure and scalable backend service for a modern hiring platform, built with Java, Spring Boot, and MongoDB. This project provides a complete set of RESTful APIs to manage user roles, job postings, candidate profiles, and application notifications.

Features
Role-Based Access Control: Distinct functionalities and permissions for Recruiters and Job Seekers.

Secure JWT Authentication: Stateless authentication using JSON Web Tokens (JWT) to secure all protected endpoints.

Job Posting Management: Recruiters can create, and anyone can view and search for job postings.

Advanced Job Search: A powerful text-based search functionality using a native MongoDB Aggregation Pipeline to query across multiple fields.

Candidate Profile Management: Job seekers can create and manage their own detailed professional profiles.

Email Notification System: Automatically sends an email notification to the recruiter when a candidate applies for one of their job postings.

Technologies Used
Backend: Java 21, Spring Boot 3.2.5

Database: MongoDB

Security: Spring Security, JSON Web Tokens (JJWT)

Build Tool: Maven

Email: Spring Boot Mail Sender

Getting Started
Follow these instructions to get a local copy of the project up and running for development and testing purposes.

Prerequisites
JDK 21 or later

Apache Maven

MongoDB Server (local instance or a cloud service like MongoDB Atlas)

A Gmail account with an App Password enabled for sending emails.

Installation & Configuration
Clone the repository:

git clone <your-repository-url>
cd hiring-platform-backend

Install dependencies:

mvn install

Configure the application:
Open the src/main/resources/application.properties file and update the following properties with your own credentials.

MongoDB Configuration:

# Replace with your MongoDB connection string
spring.data.mongodb.uri=mongodb+srv://<username>:<password>@<cluster-url>/hiring-platform?retryWrites=true&w=majority
spring.data.mongodb.database=hiring-platform

Email Sender Configuration (Gmail):

# Your Gmail account and a 16-character App Password
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_gmail_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

Note: For production, it is highly recommended to use environment variables for sensitive data instead of hardcoding them in this file.

Running the Application
You can run the application using your IDE or from the command line:

mvn spring-boot:run

The application will start on http://localhost:8080.

API Endpoints
The following is a complete list of the available API endpoints.

Authentication
Register User
POST   /register
Role: Public
Description: Registers a new user with a RECRUITER or JOB_SEEKER role.

Login User
POST   /login
Role: Public
Description: Authenticates a user and returns a JWT.

Job Posts
Get All Jobs
GET    /posts/all
Role: Public
Description: Retrieves a list of all job postings.

Search Jobs
GET    /posts/search/{text}
Role: Public
Description: Searches for jobs based on a keyword.

Add New Job
POST   /posts/add
Role: RECRUITER
Description: Creates a new job posting.

Apply for Job
POST   /posts/apply/{jobId}
Role: JOB_SEEKER
Description: Applies for a job, sending an email to the recruiter.

Candidate Profiles
Create/Update Profile
POST   /candidate/profile
Role: JOB_SEEKER
Description: Creates or updates the logged-in job seeker's profile.

Get Profile
GET    /candidate/profile/{userId}
Role: Authenticated
Description: Retrieves the profile of a specific candidate.
