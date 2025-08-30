import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Form, Button, Alert, Spinner } from 'react-bootstrap';
import { useAuth } from '../context/AuthContext';
import { candidateService } from '../services/api';

const Profile = () => {
  const { user, isJobSeeker } = useAuth();
  const [profileData, setProfileData] = useState({
    fullName: '',
    email: '',
    totalExperience: 0,
    skills: [],
    resumeUrl: ''
  });
  const [skillInput, setSkillInput] = useState('');
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [hasProfile, setHasProfile] = useState(false);

  useEffect(() => {
    if (isJobSeeker() && user?.id) {
      loadProfile();
    } else {
      setLoading(false);
    }
  }, [user, isJobSeeker]); // eslint-disable-line react-hooks/exhaustive-deps

  const loadProfile = async () => {
    try {
      setLoading(true);
      const profile = await candidateService.getProfile(user.id);
      setProfileData(profile);
      setHasProfile(true);
    } catch (err) {
      // Profile doesn't exist yet, that's okay
      setHasProfile(false);
      setProfileData({
        fullName: '',
        email: user?.email || '',
        totalExperience: 0,
        skills: [],
        resumeUrl: ''
      });
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setProfileData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleAddSkill = (e) => {
    e.preventDefault();
    if (skillInput.trim() && !profileData.skills.includes(skillInput.trim())) {
      setProfileData(prev => ({
        ...prev,
        skills: [...prev.skills, skillInput.trim()]
      }));
      setSkillInput('');
    }
  };

  const handleRemoveSkill = (skillToRemove) => {
    setProfileData(prev => ({
      ...prev,
      skills: prev.skills.filter(skill => skill !== skillToRemove)
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!profileData.fullName || !profileData.email) {
      setError('Please fill in all required fields');
      return;
    }

    try {
      setSaving(true);
      setError('');
      setSuccess('');
      
      await candidateService.saveProfile(profileData);
      setSuccess('Profile saved successfully!');
      setHasProfile(true);
    } catch (err) {
      setError(err.response?.data || 'Failed to save profile. Please try again.');
    } finally {
      setSaving(false);
    }
  };

  if (!isJobSeeker()) {
    return (
      <Container className="py-5">
        <Alert variant="warning">
          <h5>Access Denied</h5>
          <p>Only job seekers can create and manage profiles. Recruiters can view candidate profiles through job applications.</p>
        </Alert>
      </Container>
    );
  }

  if (loading) {
    return (
      <Container className="py-5 text-center">
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Loading...</span>
        </Spinner>
        <p className="mt-2">Loading profile...</p>
      </Container>
    );
  }

  return (
    <Container className="py-4">
      <Row>
        <Col lg={8} className="mx-auto">
          <Card className="shadow">
            <Card.Header>
              <h2 className="mb-0">
                {hasProfile ? 'Edit Profile' : 'Create Profile'}
              </h2>
              <p className="text-muted mb-0 mt-1">
                Complete your professional profile to attract recruiters
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
                </Alert>
              )}

              <Form onSubmit={handleSubmit}>
                <Row>
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Full Name *</Form.Label>
                      <Form.Control
                        type="text"
                        name="fullName"
                        value={profileData.fullName}
                        onChange={handleInputChange}
                        placeholder="Enter your full name"
                        required
                      />
                    </Form.Group>
                  </Col>
                  
                  <Col md={6}>
                    <Form.Group className="mb-3">
                      <Form.Label>Email *</Form.Label>
                      <Form.Control
                        type="email"
                        name="email"
                        value={profileData.email}
                        onChange={handleInputChange}
                        placeholder="Enter your email"
                        required
                      />
                    </Form.Group>
                  </Col>
                </Row>

                <Form.Group className="mb-3">
                  <Form.Label>Total Experience (years)</Form.Label>
                  <Form.Control
                    type="number"
                    name="totalExperience"
                    value={profileData.totalExperience}
                    onChange={handleInputChange}
                    min="0"
                    max="50"
                    placeholder="0"
                  />
                  <Form.Text className="text-muted">
                    Total years of professional experience
                  </Form.Text>
                </Form.Group>

                <Form.Group className="mb-3">
                  <Form.Label>Resume URL</Form.Label>
                  <Form.Control
                    type="url"
                    name="resumeUrl"
                    value={profileData.resumeUrl}
                    onChange={handleInputChange}
                    placeholder="https://example.com/your-resume.pdf"
                  />
                  <Form.Text className="text-muted">
                    Link to your online resume (Google Drive, Dropbox, etc.)
                  </Form.Text>
                </Form.Group>

                <Form.Group className="mb-4">
                  <Form.Label>Skills</Form.Label>
                  
                  {/* Add Skill Form */}
                  <div className="input-group mb-3">
                    <Form.Control
                      type="text"
                      value={skillInput}
                      onChange={(e) => setSkillInput(e.target.value)}
                      placeholder="Add a skill (e.g., JavaScript, Python, React)"
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
                  {profileData.skills.length > 0 && (
                    <div className="border rounded p-3 bg-light">
                      <div className="d-flex flex-wrap gap-2">
                        {profileData.skills.map((skill, index) => (
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
                  
                  {profileData.skills.length === 0 && (
                    <div className="text-muted small">
                      No skills added yet. Add your professional skills to help recruiters find you.
                    </div>
                  )}
                </Form.Group>

                <div className="d-grid gap-2 d-md-flex justify-content-md-end">
                  <Button variant="outline-secondary" type="button" onClick={loadProfile}>
                    Reset
                  </Button>
                  <Button 
                    variant="primary" 
                    type="submit" 
                    disabled={saving}
                    className="px-4"
                  >
                    {saving ? (
                      <>
                        <Spinner as="span" animation="border" size="sm" className="me-2" />
                        Saving...
                      </>
                    ) : (
                      hasProfile ? 'Update Profile' : 'Create Profile'
                    )}
                  </Button>
                </div>
              </Form>
            </Card.Body>
          </Card>

          {/* Profile Preview */}
          {(profileData.fullName || profileData.skills.length > 0) && (
            <Card className="shadow-sm mt-4">
              <Card.Header>
                <h5 className="mb-0">Profile Preview</h5>
              </Card.Header>
              <Card.Body>
                <h6>{profileData.fullName || 'Your Name'}</h6>
                <p className="text-muted mb-2">
                  {profileData.email || 'your.email@example.com'}
                </p>
                <p className="mb-2">
                  <strong>Experience:</strong> {profileData.totalExperience} year{profileData.totalExperience !== 1 ? 's' : ''}
                </p>
                
                {profileData.skills.length > 0 && (
                  <div className="mb-2">
                    <strong>Skills:</strong>
                    <div className="mt-1">
                      {profileData.skills.map((skill, index) => (
                        <span key={index} className="badge bg-secondary me-1 mb-1">
                          {skill}
                        </span>
                      ))}
                    </div>
                  </div>
                )}
                
                {profileData.resumeUrl && (
                  <p className="mb-0">
                    <strong>Resume:</strong>{' '}
                    <a href={profileData.resumeUrl} target="_blank" rel="noopener noreferrer">
                      View Resume
                    </a>
                  </p>
                )}
              </Card.Body>
            </Card>
          )}
        </Col>
      </Row>
    </Container>
  );
};

export default Profile;