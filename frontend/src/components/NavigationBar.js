import React from 'react';
import { Navbar, Nav, Button, Container } from 'react-bootstrap';
import { LinkContainer } from 'react-router-bootstrap';
import { useAuth } from '../context/AuthContext';

const NavigationBar = () => {
  const { user, isAuthenticated, isRecruiter, isJobSeeker, logout } = useAuth();

  const handleLogout = () => {
    logout();
  };

  return (
    <Navbar bg="primary" variant="dark" expand="lg" className="mb-4">
      <Container>
        <LinkContainer to="/">
          <Navbar.Brand>Hiring Platform</Navbar.Brand>
        </LinkContainer>
        
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <LinkContainer to="/jobs">
              <Nav.Link>Browse Jobs</Nav.Link>
            </LinkContainer>
            
            {isAuthenticated() && (
              <LinkContainer to="/dashboard">
                <Nav.Link>Dashboard</Nav.Link>
              </LinkContainer>
            )}
            
            {isRecruiter() && (
              <LinkContainer to="/create-job">
                <Nav.Link>Post Job</Nav.Link>
              </LinkContainer>
            )}
            
            {isJobSeeker() && (
              <LinkContainer to="/profile">
                <Nav.Link>My Profile</Nav.Link>
              </LinkContainer>
            )}
          </Nav>
          
          <Nav>
            {isAuthenticated() ? (
              <>
                <Navbar.Text className="me-3">
                  Welcome, {user?.username}
                  {user?.role && (
                    <span className="badge bg-secondary ms-2">
                      {user.role === 'RECRUITER' ? 'Recruiter' : 'Job Seeker'}
                    </span>
                  )}
                </Navbar.Text>
                <Button variant="outline-light" onClick={handleLogout}>
                  Logout
                </Button>
              </>
            ) : (
              <>
                <LinkContainer to="/login">
                  <Button variant="outline-light" className="me-2">
                    Login
                  </Button>
                </LinkContainer>
                <LinkContainer to="/register">
                  <Button variant="light">
                    Register
                  </Button>
                </LinkContainer>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default NavigationBar;