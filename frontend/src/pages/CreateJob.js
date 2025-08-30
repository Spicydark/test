import React, { useState } from 'react';
import { Container, Row, Col, Card, Form, Button, Alert, Spinner } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { jobService } from '../services/api';

const CreateJob = () => {
  const { isRecruiter } = useAuth();
  const navigate = useNavigate();
  
  const [jobData, setJobData] = useState({
    role: '',
    description: '',
    experience: 0,
    skillSet: []
  });
  const [skillInput, setSkillInput] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setJobData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleAddSkill = (e) => {
    e.preventDefault();
    if (skillInput.trim() && !jobData.skillSet.includes(skillInput.trim())) {
      setJobData(prev => ({
        ...prev,
        skillSet: [...prev.skillSet, skillInput.trim()]
      }));
      setSkillInput('');
    }
  };

  const handleRemoveSkill = (skillToRemove) => {
    setJobData(prev => ({
      ...prev,
      skillSet: prev.skillSet.filter(skill => skill !== skillToRemove)
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!jobData.role || !jobData.description) {
      setError('Please fill in all required fields');
      return;
    }

    if (jobData.skillSet.length === 0) {
      setError('Please add at least one required skill');
      return;
    }

    try {
      setLoading(true);
      setError('');
      setSuccess('');
      
      await jobService.createJob({
        ...jobData,
        experience: parseInt(jobData.experience)
      });
      
      setSuccess('Job posted successfully!');
      
      // Redirect to dashboard after a short delay
      setTimeout(() => {
        navigate('/dashboard');
      }, 2000);
    } catch (err) {
      setError(err.response?.data || 'Failed to create job posting. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setJobData({
      role: '',
      description: '',
      experience: 0,
      skillSet: []
    });
    setSkillInput('');
    setError('');
    setSuccess('');
  };

  if (!isRecruiter()) {
    return (
      <Container className="py-5">
        <Alert variant="warning">
          <h5>Access Denied</h5>
          <p>Only recruiters can create job postings. Job seekers can browse and apply for jobs.</p>
        </Alert>
      </Container>
    );
  }

  return (
    <Container className="py-4">
      <Row>
        <Col lg={8} className="mx-auto">
          <Card className="shadow">
            <Card.Header>
              <h2 className="mb-0">Post a New Job</h2>
              <p className="text-muted mb-0 mt-1">
                Create a job posting to find the perfect candidate
              </p>
            </Card.Header>
            
            <Card.Body className="p-4">
              {error && (
                <Alert variant="danger" className="mb-4">
                  {error}
                </Alert>
              )}

              {success && (
                <Alert variant="success" className="mb-4">
                  {success}
                  <div className="mt-2">
                    <small>Redirecting to dashboard...</small>
                  </div>
                </Alert>
              )}

              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3">
                  <Form.Label>Job Title / Role *</Form.Label>
                  <Form.Control
                    type="text"
                    name="role"
                    value={jobData.role}
                    onChange={handleInputChange}
                    placeholder="e.g., Senior React Developer, Marketing Manager"
                    required
                  />
                  <Form.Text className="text-muted">
                    Enter a clear and descriptive job title
                  </Form.Text>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Required Experience (years)</Form.Label>
                  <Form.Control
                    type="number"
                    name="experience"
                    value={jobData.experience}
                    onChange={handleInputChange}
                    min="0"
                    max="50"
                    placeholder="0"
                  />
                  <Form.Text className="text-muted">
                    Minimum years of experience required (0 for entry level)
                  </Form.Text>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Job Description *</Form.Label>
                  <Form.Control
                    as="textarea"
                    rows={6}
                    name="description"
                    value={jobData.description}
                    onChange={handleInputChange}
                    placeholder="Describe the role, responsibilities, requirements, and what you're looking for in a candidate..."
                    required
                  />
                  <Form.Text className="text-muted">
                    Provide a detailed description of the position
                  </Form.Text>
                </Form.Group>

                <Form.Group className="mb-4">
                  <Form.Label>Required Skills *</Form.Label>
                  
                  {/* Add Skill Form */}
                  <div className="input-group mb-3">
                    <Form.Control
                      type="text"
                      value={skillInput}
                      onChange={(e) => setSkillInput(e.target.value)}
                      placeholder="Add a required skill (e.g., JavaScript, Python, Project Management)"
                      onKeyPress={(e) => {
                        if (e.key === 'Enter') {
                          handleAddSkill(e);
                        }
                      }}
                    />
                    <Button variant="outline-primary" onClick={handleAddSkill}>
                      Add Skill
                    </Button>
                  </div>

                  {/* Skills Display */}
                  {jobData.skillSet.length > 0 && (
                    <div className="border rounded p-3 bg-light">
                      <div className="d-flex flex-wrap gap-2">
                        {jobData.skillSet.map((skill, index) => (
                          <span
                            key={index}
                            className="badge bg-primary d-flex align-items-center"
                          >
                            {skill}
                            <button
                              type="button"
                              className="btn-close btn-close-white ms-2"
                              aria-label="Remove skill"
                              onClick={() => handleRemoveSkill(skill)}
                              style={{ fontSize: '0.7em' }}
                            />
                          </span>
                        ))}
                      </div>
                    </div>
                  )}
                  
                  {jobData.skillSet.length === 0 && (
                    <div className="text-muted small">
                      No skills added yet. Add the skills required for this position.
                    </div>
                  )}
                </Form.Group>

                <div className="d-grid gap-2 d-md-flex justify-content-md-end">
                  <Button variant="outline-secondary" type="button" onClick={handleReset}>
                    Reset Form
                  </Button>
                  <Button 
                    variant="primary" 
                    type="submit" 
                    disabled={loading}
                    className="px-4"
                  >
                    {loading ? (
                      <>
                        <Spinner as="span" animation="border" size="sm" className="me-2" />
                        Posting Job...
                      </>
                    ) : (
                      'Post Job'
                    )}
                  </Button>
                </div>
              </Form>
            </Card.Body>
          </Card>

          {/* Job Preview */}
          {(jobData.role || jobData.description || jobData.skillSet.length > 0) && (
            <Card className="shadow-sm mt-4">
              <Card.Header>
                <h5 className="mb-0">Job Posting Preview</h5>
              </Card.Header>
              <Card.Body>
                <h6>{jobData.role || 'Job Title'}</h6>
                <p className="text-muted mb-2">
                  <i className="fas fa-briefcase me-2"></i>
                  {jobData.experience} year{jobData.experience !== 1 ? 's' : ''} experience required
                </p>
                
                {jobData.description && (
                  <div className="mb-3">
                    <h6 className="h6">Description:</h6>
                    <p className="mb-0" style={{ whiteSpace: 'pre-wrap' }}>
                      {jobData.description}
                    </p>
                  </div>
                )}
                
                {jobData.skillSet.length > 0 && (
                  <div className="mb-0">
                    <h6 className="h6">Required Skills:</h6>
                    <div>
                      {jobData.skillSet.map((skill, index) => (
                        <span key={index} className="badge bg-secondary me-1 mb-1">
                          {skill}
                        </span>
                      ))}
                    </div>
                  </div>
                )}
              </Card.Body>
            </Card>
          )}
        </Col>
      </Row>
    </Container>
  );
};

export default CreateJob;