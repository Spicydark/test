import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Badge, Alert, Spinner } from 'react-bootstrap';
import { useParams, useNavigate } from 'react-router-dom';
import { jobService } from '../services/api';
import { useAuth } from '../context/AuthContext';

const JobDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [job, setJob] = useState(null);
  const [loading, setLoading] = useState(true);
  const [applying, setApplying] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  
  const { isJobSeeker, isAuthenticated } = useAuth();

  useEffect(() => {
    fetchJobDetails();
  }, [id]); // eslint-disable-line react-hooks/exhaustive-deps

  const fetchJobDetails = async () => {
    try {
      setLoading(true);
      // Since there's no individual job endpoint, fetch all jobs and find the one we need
      const jobs = await jobService.getAllJobs();
      const foundJob = jobs.find(j => j.id === id);
      
      if (foundJob) {
        setJob(foundJob);
      } else {
        setError('Job not found');
      }
    } catch (err) {
      setError('Failed to load job details. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleApply = async () => {
    if (!isAuthenticated()) {
      navigate('/login');
      return;
    }

    if (!isJobSeeker()) {
      setError('Only job seekers can apply for jobs.');
      return;
    }

    try {
      setApplying(true);
      setError('');
      await jobService.applyForJob(id);
      setSuccess('Application submitted successfully! The recruiter has been notified.');
    } catch (err) {
      setError(err.response?.data || 'Failed to submit application. Please try again.');
    } finally {
      setApplying(false);
    }
  };

  if (loading) {
    return (
      <Container className="py-5 text-center">
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Loading...</span>
        </Spinner>
        <p className="mt-2">Loading job details...</p>
      </Container>
    );
  }

  if (error && !job) {
    return (
      <Container className="py-5">
        <Alert variant="danger">
          {error}
          <div className="mt-3">
            <Button variant="primary" onClick={() => navigate('/jobs')}>
              Back to Jobs
            </Button>
          </div>
        </Alert>
      </Container>
    );
  }

  return (
    <Container className="py-4">
      <Row>
        <Col lg={8}>
          <Card className="shadow-sm">
            <Card.Body className="p-4">
              <div className="d-flex justify-content-between align-items-start mb-3">
                <div>
                  <h1 className="h2 mb-2">{job.role}</h1>
                  <p className="text-muted mb-0">
                    <i className="fas fa-briefcase me-2"></i>
                    {job.experience} year{job.experience !== 1 ? 's' : ''} experience required
                  </p>
                </div>
                <Button variant="outline-secondary" onClick={() => navigate('/jobs')}>
                  <i className="fas fa-arrow-left me-2"></i>
                  Back to Jobs
                </Button>
              </div>

              {(error || success) && (
                <div className="mb-4">
                  {error && <Alert variant="danger">{error}</Alert>}
                  {success && <Alert variant="success">{success}</Alert>}
                </div>
              )}

              <div className="mb-4">
                <h3 className="h5 mb-3">Job Description</h3>
                <p className="text-justify">{job.description}</p>
              </div>

              <div className="mb-4">
                <h3 className="h5 mb-3">Required Skills</h3>
                <div>
                  {job.skillSet.map((skill, index) => (
                    <Badge bg="primary" className="me-2 mb-2" key={index}>
                      {skill}
                    </Badge>
                  ))}
                </div>
              </div>

              <div className="mb-4">
                <h3 className="h5 mb-3">Experience Requirements</h3>
                <p>
                  <strong>{job.experience} year{job.experience !== 1 ? 's' : ''}</strong> of relevant experience required
                </p>
              </div>
            </Card.Body>
          </Card>
        </Col>

        <Col lg={4}>
          <Card className="shadow-sm">
            <Card.Body>
              <h3 className="h5 mb-3">Apply for this Job</h3>
              
              {!isAuthenticated() ? (
                <div>
                  <p className="text-muted mb-3">
                    You need to be logged in to apply for this job.
                  </p>
                  <div className="d-grid gap-2">
                    <Button variant="primary" onClick={() => navigate('/login')}>
                      Login to Apply
                    </Button>
                    <Button variant="outline-primary" onClick={() => navigate('/register')}>
                      Create Account
                    </Button>
                  </div>
                </div>
              ) : !isJobSeeker() ? (
                <Alert variant="info" className="mb-0">
                  Only job seekers can apply for jobs. Recruiters can view job details.
                </Alert>
              ) : success ? (
                <Alert variant="success" className="mb-0">
                  <i className="fas fa-check-circle me-2"></i>
                  Application submitted successfully!
                </Alert>
              ) : (
                <div>
                  <p className="text-muted mb-3">
                    Ready to apply? Click the button below to submit your application.
                  </p>
                  <div className="d-grid">
                    <Button 
                      variant="primary" 
                      size="lg"
                      onClick={handleApply}
                      disabled={applying}
                    >
                      {applying ? (
                        <>
                          <Spinner as="span" animation="border" size="sm" className="me-2" />
                          Applying...
                        </>
                      ) : (
                        <>
                          <i className="fas fa-paper-plane me-2"></i>
                          Apply Now
                        </>
                      )}
                    </Button>
                  </div>
                </div>
              )}
            </Card.Body>
          </Card>

          {/* Additional Job Information */}
          <Card className="shadow-sm mt-4">
            <Card.Body>
              <h3 className="h6 mb-3">Job Information</h3>
              <div className="mb-2">
                <strong>Job ID:</strong> {job.id}
              </div>
              <div className="mb-2">
                <strong>Posted by:</strong> Recruiter #{job.recruiterId}
              </div>
              <div className="mb-2">
                <strong>Required Skills:</strong> {job.skillSet.length}
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default JobDetails;