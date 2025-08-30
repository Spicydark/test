import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Form, Badge, Spinner, Alert } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { jobService } from '../services/api';
import { useAuth } from '../context/AuthContext';

const JobList = () => {
  const [jobs, setJobs] = useState([]);
  const [filteredJobs, setFilteredJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [experienceFilter, setExperienceFilter] = useState('');
  
  const { isJobSeeker } = useAuth();

  useEffect(() => {
    fetchJobs();
  }, []);

  useEffect(() => {
    filterJobs();
  }, [jobs, searchTerm, experienceFilter]); // eslint-disable-line react-hooks/exhaustive-deps

  const fetchJobs = async () => {
    try {
      setLoading(true);
      const jobsData = await jobService.getAllJobs();
      setJobs(jobsData);
    } catch (err) {
      setError('Failed to load jobs. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const filterJobs = () => {
    let filtered = jobs;

    if (searchTerm) {
      filtered = filtered.filter(job =>
        job.role.toLowerCase().includes(searchTerm.toLowerCase()) ||
        job.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
        job.skillSet.some(skill => skill.toLowerCase().includes(searchTerm.toLowerCase()))
      );
    }

    if (experienceFilter) {
      const expRange = parseInt(experienceFilter);
      filtered = filtered.filter(job => {
        if (expRange === 0) return job.experience <= 1;
        if (expRange === 2) return job.experience >= 2 && job.experience <= 5;
        if (expRange === 5) return job.experience > 5;
        return true;
      });
    }

    setFilteredJobs(filtered);
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    if (searchTerm.trim()) {
      try {
        setLoading(true);
        const searchResults = await jobService.searchJobs(searchTerm);
        setJobs(searchResults);
      } catch (err) {
        setError('Search failed. Please try again.');
      } finally {
        setLoading(false);
      }
    } else {
      fetchJobs();
    }
  };

  if (loading) {
    return (
      <Container className="py-5 text-center">
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Loading...</span>
        </Spinner>
        <p className="mt-2">Loading jobs...</p>
      </Container>
    );
  }

  return (
    <Container className="py-4">
      <Row>
        <Col>
          <h1 className="mb-4">Browse Jobs</h1>
        </Col>
      </Row>

      {error && (
        <Alert variant="danger" className="mb-4">
          {error}
        </Alert>
      )}

      {/* Search and Filter Section */}
      <Row className="mb-4">
        <Col md={8}>
          <Form onSubmit={handleSearch}>
            <Form.Group>
              <div className="input-group">
                <Form.Control
                  type="text"
                  placeholder="Search jobs by title, skills, or description..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
                <Button variant="primary" type="submit">
                  Search
                </Button>
              </div>
            </Form.Group>
          </Form>
        </Col>
        <Col md={4}>
          <Form.Select
            value={experienceFilter}
            onChange={(e) => setExperienceFilter(e.target.value)}
          >
            <option value="">All Experience Levels</option>
            <option value="0">Entry Level (0-1 years)</option>
            <option value="2">Mid Level (2-5 years)</option>
            <option value="5">Senior Level (5+ years)</option>
          </Form.Select>
        </Col>
      </Row>

      {/* Results Summary */}
      <Row className="mb-3">
        <Col>
          <p className="text-muted">
            Showing {filteredJobs.length} job{filteredJobs.length !== 1 ? 's' : ''}
            {searchTerm && ` for "${searchTerm}"`}
          </p>
        </Col>
      </Row>

      {/* Jobs List */}
      <Row>
        {filteredJobs.length === 0 ? (
          <Col>
            <Card className="text-center py-5">
              <Card.Body>
                <h5>No jobs found</h5>
                <p className="text-muted">
                  Try adjusting your search criteria or browse all available jobs.
                </p>
                <Button variant="primary" onClick={() => {
                  setSearchTerm('');
                  setExperienceFilter('');
                  fetchJobs();
                }}>
                  View All Jobs
                </Button>
              </Card.Body>
            </Card>
          </Col>
        ) : (
          filteredJobs.map((job) => (
            <Col md={6} lg={4} key={job.id} className="mb-4">
              <Card className="h-100 shadow-sm">
                <Card.Body>
                  <Card.Title className="h5">{job.role}</Card.Title>
                  <Card.Text className="text-muted small mb-2">
                    {job.experience} year{job.experience !== 1 ? 's' : ''} experience required
                  </Card.Text>
                  <Card.Text className="mb-3">
                    {job.description.length > 150 
                      ? `${job.description.substring(0, 150)}...` 
                      : job.description
                    }
                  </Card.Text>
                  
                  <div className="mb-3">
                    <strong className="small">Skills:</strong>
                    <div className="mt-1">
                      {job.skillSet.slice(0, 3).map((skill, index) => (
                        <Badge bg="secondary" className="me-1 mb-1" key={index}>
                          {skill}
                        </Badge>
                      ))}
                      {job.skillSet.length > 3 && (
                        <Badge bg="light" text="dark">
                          +{job.skillSet.length - 3} more
                        </Badge>
                      )}
                    </div>
                  </div>
                </Card.Body>
                
                <Card.Footer className="bg-transparent">
                  <div className="d-grid gap-2">
                    <Link to={`/jobs/${job.id}`} className="btn btn-outline-primary">
                      View Details
                    </Link>
                    {isJobSeeker() && (
                      <Link to={`/jobs/${job.id}`} className="btn btn-primary">
                        Apply Now
                      </Link>
                    )}
                  </div>
                </Card.Footer>
              </Card>
            </Col>
          ))
        )}
      </Row>
    </Container>
  );
};

export default JobList;