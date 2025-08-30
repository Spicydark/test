import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Alert } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { jobService, candidateService } from '../services/api';

const Dashboard = () => {
  const { user, isRecruiter, isJobSeeker } = useAuth();
  const [stats, setStats] = useState({
    totalJobs: 0,
    myJobs: 0,
    applications: 0
  });
  const [recentJobs, setRecentJobs] = useState([]);
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboardData();
  }, [user]); // eslint-disable-line react-hooks/exhaustive-deps

  const loadDashboardData = async () => {
    try {
      setLoading(true);
      
      // Load jobs data
      const jobs = await jobService.getAllJobs();
      setRecentJobs(jobs.slice(0, 5)); // Show latest 5 jobs
      
      const totalJobs = jobs.length;
      const myJobs = isRecruiter() ? jobs.filter(job => job.recruiterId === user.id).length : 0;
      
      setStats({
        totalJobs,
        myJobs,
        applications: 0 // This would need a separate API endpoint to track applications
      });

      // Load profile for job seekers
      if (isJobSeeker() && user.id) {
        try {
          const profileData = await candidateService.getProfile(user.id);
          setProfile(profileData);
        } catch (err) {
          // Profile doesn't exist yet
          setProfile(null);
        }
      }
    } catch (err) {
      console.error('Failed to load dashboard data:', err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <Container className="py-5 text-center">
        <div className="spinner-border" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-2">Loading dashboard...</p>
      </Container>
    );
  }

  return (
    <Container className="py-4">
      <Row>
        <Col>
          <h1 className="mb-4">
            Welcome back, {user?.username}!
            <span className={`badge ms-3 ${isRecruiter() ? 'bg-info' : 'bg-success'}`}>
              {isRecruiter() ? 'Recruiter' : 'Job Seeker'}
            </span>
          </h1>
        </Col>
      </Row>

      {/* Stats Cards */}
      <Row className="mb-4">
        <Col md={4}>
          <Card className="text-center border-0 shadow-sm">
            <Card.Body>
              <div className="display-4 text-primary">{stats.totalJobs}</div>
              <p className="text-muted mb-0">Total Jobs Available</p>
            </Card.Body>
          </Card>
        </Col>
        
        {isRecruiter() && (
          <Col md={4}>
            <Card className="text-center border-0 shadow-sm">
              <Card.Body>
                <div className="display-4 text-info">{stats.myJobs}</div>
                <p className="text-muted mb-0">Jobs Posted by You</p>
              </Card.Body>
            </Card>
          </Col>
        )}
        
        <Col md={4}>
          <Card className="text-center border-0 shadow-sm">
            <Card.Body>
              <div className="display-4 text-success">{stats.applications}</div>
              <p className="text-muted mb-0">
                {isRecruiter() ? 'Applications Received' : 'Applications Sent'}
              </p>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row>
        {/* Main Content */}
        <Col lg={8}>
          {isRecruiter() ? (
            <Card className="shadow-sm">
              <Card.Header>
                <div className="d-flex justify-content-between align-items-center">
                  <h5 className="mb-0">Recruiter Actions</h5>
                </div>
              </Card.Header>
              <Card.Body>
                <p className="text-muted">Manage your job postings and find the best candidates.</p>
                
                <div className="d-grid gap-2 d-md-flex">
                  <Link to="/create-job" className="btn btn-primary">
                    <i className="fas fa-plus me-2"></i>
                    Post New Job
                  </Link>
                  <Link to="/jobs" className="btn btn-outline-primary">
                    <i className="fas fa-list me-2"></i>
                    View All Jobs
                  </Link>
                </div>

                {stats.myJobs > 0 && (
                  <Alert variant="info" className="mt-3">
                    <i className="fas fa-info-circle me-2"></i>
                    You have {stats.myJobs} active job posting{stats.myJobs !== 1 ? 's' : ''}.
                  </Alert>
                )}
              </Card.Body>
            </Card>
          ) : (
            <Card className="shadow-sm">
              <Card.Header>
                <h5 className="mb-0">Job Seeker Actions</h5>
              </Card.Header>
              <Card.Body>
                <p className="text-muted">Find your next opportunity and manage your profile.</p>
                
                <div className="d-grid gap-2 d-md-flex mb-3">
                  <Link to="/jobs" className="btn btn-primary">
                    <i className="fas fa-search me-2"></i>
                    Browse Jobs
                  </Link>
                  <Link to="/profile" className="btn btn-outline-primary">
                    <i className="fas fa-user me-2"></i>
                    {profile ? 'Edit Profile' : 'Create Profile'}
                  </Link>
                </div>

                {!profile && (
                  <Alert variant="warning">
                    <i className="fas fa-exclamation-triangle me-2"></i>
                    Complete your profile to increase your chances of getting hired!
                    <Link to="/profile" className="alert-link ms-2">Create Profile</Link>
                  </Alert>
                )}
              </Card.Body>
            </Card>
          )}

          {/* Recent Jobs */}
          <Card className="shadow-sm mt-4">
            <Card.Header>
              <h5 className="mb-0">Recent Job Postings</h5>
            </Card.Header>
            <Card.Body>
              {recentJobs.length === 0 ? (
                <p className="text-muted">No jobs available at the moment.</p>
              ) : (
                <div className="list-group list-group-flush">
                  {recentJobs.map(job => (
                    <div key={job.id} className="list-group-item border-0 px-0">
                      <div className="d-flex justify-content-between align-items-start">
                        <div>
                          <h6 className="mb-1">
                            <Link to={`/jobs/${job.id}`} className="text-decoration-none">
                              {job.role}
                            </Link>
                          </h6>
                          <p className="mb-1 small text-muted">
                            {job.experience} year{job.experience !== 1 ? 's' : ''} experience • 
                            {job.skillSet.slice(0, 3).join(', ')}
                            {job.skillSet.length > 3 && ` +${job.skillSet.length - 3} more`}
                          </p>
                          <small className="text-muted">
                            {job.description.substring(0, 100)}...
                          </small>
                        </div>
                        <Link to={`/jobs/${job.id}`} className="btn btn-sm btn-outline-primary">
                          View
                        </Link>
                      </div>
                    </div>
                  ))}
                </div>
              )}
              
              <div className="text-center mt-3">
                <Link to="/jobs" className="btn btn-outline-primary">
                  View All Jobs
                </Link>
              </div>
            </Card.Body>
          </Card>
        </Col>

        {/* Sidebar */}
        <Col lg={4}>
          <Card className="shadow-sm">
            <Card.Header>
              <h6 className="mb-0">Quick Links</h6>
            </Card.Header>
            <Card.Body>
              <div className="d-grid gap-2">
                <Link to="/jobs" className="btn btn-outline-primary btn-sm">
                  Browse All Jobs
                </Link>
                {isRecruiter() && (
                  <Link to="/create-job" className="btn btn-outline-success btn-sm">
                    Post a Job
                  </Link>
                )}
                {isJobSeeker() && (
                  <Link to="/profile" className="btn btn-outline-info btn-sm">
                    Manage Profile
                  </Link>
                )}
              </div>
            </Card.Body>
          </Card>

          {/* Platform Stats */}
          <Card className="shadow-sm mt-4">
            <Card.Header>
              <h6 className="mb-0">Platform Statistics</h6>
            </Card.Header>
            <Card.Body>
              <div className="small">
                <div className="d-flex justify-content-between mb-2">
                  <span>Total Jobs:</span>
                  <strong>{stats.totalJobs}</strong>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span>Active Users:</span>
                  <strong>1,000+</strong>
                </div>
                <div className="d-flex justify-content-between">
                  <span>Success Rate:</span>
                  <strong>85%</strong>
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Dashboard;