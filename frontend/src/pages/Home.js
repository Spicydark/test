import React from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import { useAuth } from '../context/AuthContext';

const Home = () => {
  const { isAuthenticated } = useAuth();

  return (
    <div className="bg-light min-vh-100">
      {/* Hero Section */}
      <section className="bg-primary text-white py-5">
        <Container>
          <Row className="align-items-center">
            <Col lg={8}>
              <h1 className="display-4 fw-bold">Find Your Dream Job</h1>
              <p className="lead">
                Connect with top employers and discover opportunities that match your skills and aspirations.
              </p>
              {!isAuthenticated() && (
                <div className="mt-4">
                  <LinkContainer to="/register">
                    <Button variant="light" size="lg" className="me-3">
                      Get Started
                    </Button>
                  </LinkContainer>
                  <LinkContainer to="/jobs">
                    <Button variant="outline-light" size="lg">
                      Browse Jobs
                    </Button>
                  </LinkContainer>
                </div>
              )}
              {isAuthenticated() && (
                <div className="mt-4">
                  <LinkContainer to="/jobs">
                    <Button variant="light" size="lg" className="me-3">
                      Browse Jobs
                    </Button>
                  </LinkContainer>
                  <LinkContainer to="/dashboard">
                    <Button variant="outline-light" size="lg">
                      Go to Dashboard
                    </Button>
                  </LinkContainer>
                </div>
              )}
            </Col>
          </Row>
        </Container>
      </section>

      {/* Features Section */}
      <section className="py-5">
        <Container>
          <Row className="text-center mb-5">
            <Col>
              <h2 className="fw-bold">Why Choose Our Platform?</h2>
              <p className="text-muted">We make hiring and job searching simple and effective</p>
            </Col>
          </Row>
          
          <Row>
            <Col md={4} className="mb-4">
              <Card className="h-100 border-0 shadow-sm">
                <Card.Body className="text-center p-4">
                  <div className="bg-primary text-white rounded-circle d-inline-flex align-items-center justify-content-center mb-3" 
                       style={{width: '60px', height: '60px'}}>
                    <i className="fas fa-search fa-lg"></i>
                  </div>
                  <Card.Title>Smart Job Search</Card.Title>
                  <Card.Text>
                    Find jobs that match your skills and experience with our advanced search and filtering system.
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
            
            <Col md={4} className="mb-4">
              <Card className="h-100 border-0 shadow-sm">
                <Card.Body className="text-center p-4">
                  <div className="bg-success text-white rounded-circle d-inline-flex align-items-center justify-content-center mb-3" 
                       style={{width: '60px', height: '60px'}}>
                    <i className="fas fa-user-tie fa-lg"></i>
                  </div>
                  <Card.Title>Professional Profiles</Card.Title>
                  <Card.Text>
                    Create detailed professional profiles to showcase your skills and experience to recruiters.
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
            
            <Col md={4} className="mb-4">
              <Card className="h-100 border-0 shadow-sm">
                <Card.Body className="text-center p-4">
                  <div className="bg-info text-white rounded-circle d-inline-flex align-items-center justify-content-center mb-3" 
                       style={{width: '60px', height: '60px'}}>
                    <i className="fas fa-handshake fa-lg"></i>
                  </div>
                  <Card.Title>Direct Applications</Card.Title>
                  <Card.Text>
                    Apply directly to jobs and connect with recruiters through our streamlined application process.
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
          </Row>
        </Container>
      </section>

      {/* CTA Section */}
      <section className="bg-secondary text-white py-5">
        <Container>
          <Row className="text-center">
            <Col>
              <h3>Ready to Get Started?</h3>
              <p className="mb-4">Join thousands of job seekers and recruiters on our platform</p>
              {!isAuthenticated() && (
                <LinkContainer to="/register">
                  <Button variant="primary" size="lg">
                    Sign Up Now
                  </Button>
                </LinkContainer>
              )}
            </Col>
          </Row>
        </Container>
      </section>
    </div>
  );
};

export default Home;