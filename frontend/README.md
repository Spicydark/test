# Hiring Platform Frontend

A modern React frontend application for the Hiring Platform, built with React, Bootstrap, and React Router. This application provides a complete user interface for both job seekers and recruiters to interact with the hiring platform backend.

## Features

### For Job Seekers
- **Browse Jobs**: View all available job postings with search and filter capabilities
- **Job Search**: Search jobs by title, skills, or description
- **Job Details**: View detailed job information and requirements
- **Apply for Jobs**: Submit applications directly through the platform
- **Profile Management**: Create and manage professional profiles
- **Dashboard**: Personalized dashboard with job recommendations and application status

### For Recruiters
- **Post Jobs**: Create detailed job postings with required skills and experience
- **Manage Postings**: View and manage all posted jobs
- **Dashboard**: Recruiter-specific dashboard with job posting statistics
- **View Applications**: (Backend sends email notifications when candidates apply)

### General Features
- **Authentication**: Secure login and registration with JWT tokens
- **Role-based Access**: Different interfaces for job seekers and recruiters
- **Responsive Design**: Mobile-friendly interface using Bootstrap
- **Real-time Updates**: Dynamic content updates without page refreshes

## Technology Stack

- **React 18+**: Modern React with functional components and hooks
- **Bootstrap 5**: Responsive CSS framework for styling
- **React Bootstrap**: Bootstrap components for React
- **React Router Dom**: Client-side routing
- **Axios**: HTTP client for API communication
- **Font Awesome**: Icons for enhanced UI

## Prerequisites

- Node.js 14 or later
- npm or yarn package manager
- Backend API running on `http://localhost:8080`

## Installation & Setup

1. **Navigate to the frontend directory**:
   ```bash
   cd frontend
   ```

2. **Install dependencies**:
   ```bash
   npm install
   ```

3. **Start the development server**:
   ```bash
   npm start
   ```

4. **Open your browser** and navigate to `http://localhost:3000`

## Available Scripts

- `npm start`: Runs the app in development mode
- `npm run build`: Builds the app for production
- `npm test`: Launches the test runner
- `npm run eject`: Ejects from Create React App (irreversible)

## Project Structure

```
frontend/
├── public/
│   ├── index.html          # Main HTML template
│   └── ...                 # Static assets
├── src/
│   ├── components/         # Reusable React components
│   │   └── NavigationBar.js
│   ├── context/           # React Context providers
│   │   └── AuthContext.js # Authentication context
│   ├── pages/             # Page components
│   │   ├── Home.js        # Landing page
│   │   ├── Login.js       # User login
│   │   ├── Register.js    # User registration
│   │   ├── JobList.js     # Job listings
│   │   ├── JobDetails.js  # Job detail view
│   │   ├── Dashboard.js   # User dashboard
│   │   ├── Profile.js     # Job seeker profile
│   │   └── CreateJob.js   # Job creation (recruiters)
│   ├── services/          # API service layer
│   │   └── api.js         # Backend API communication
│   ├── App.js             # Main application component
│   ├── App.css            # Global styles
│   └── index.js           # Application entry point
├── package.json           # Dependencies and scripts
└── README.md              # This file
```

## Backend Integration

The frontend communicates with the backend API through the following endpoints:

### Authentication
- `POST /register` - User registration
- `POST /login` - User login

### Job Management
- `GET /posts/all` - Get all jobs
- `GET /posts/search/{text}` - Search jobs
- `POST /posts/add` - Create job (recruiters only)
- `POST /posts/apply/{jobId}` - Apply for job (job seekers only)

### Profile Management
- `POST /candidate/profile` - Create/update profile (job seekers only)
- `GET /candidate/profile/{userId}` - Get user profile

## Key Components

### Authentication System
- JWT-based authentication with automatic token management
- Role-based access control (Job Seeker vs Recruiter)
- Persistent login state across browser sessions

### Navigation
- Dynamic navigation based on user role and authentication status
- Bootstrap navigation with responsive design

### Job Management
- Advanced job search with keyword filtering
- Experience level filtering
- Responsive job cards with skill tags
- Detailed job view with application functionality

### Profile Management
- Rich profile creation for job seekers
- Skills management with add/remove functionality
- Resume URL integration
- Profile preview functionality

## Styling and Design

- **Bootstrap 5**: Core styling framework
- **Custom CSS**: Enhanced styling in `App.css`
- **Font Awesome**: Icons throughout the application
- **Responsive Design**: Mobile-first approach
- **Color Scheme**: Professional blue and gray palette

## Development Notes

### Proxy Configuration
The development server is configured to proxy API requests to `http://localhost:8080` for seamless backend integration during development.

### Error Handling
- Comprehensive error handling for API calls
- User-friendly error messages
- Loading states for better UX

### State Management
- React Context for global authentication state
- Local component state for form management
- Proper cleanup and memory management

## Deployment

1. **Build the production version**:
   ```bash
   npm run build
   ```

2. **Serve the built files** using any static file server:
   ```bash
   npm install -g serve
   serve -s build
   ```

3. **Configure your web server** to serve the React app and proxy API calls to the backend.

## Contributing

1. Follow the existing code structure and naming conventions
2. Add proper error handling for new features
3. Ensure responsive design for all new components
4. Test thoroughly across different user roles
5. Update this README for any new features or changes

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## License

This project is part of the Hiring Platform application suite.

### Code Splitting

This section has moved here: [https://facebook.github.io/create-react-app/docs/code-splitting](https://facebook.github.io/create-react-app/docs/code-splitting)

### Analyzing the Bundle Size

This section has moved here: [https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size](https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size)

### Making a Progressive Web App

This section has moved here: [https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app](https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app)

### Advanced Configuration

This section has moved here: [https://facebook.github.io/create-react-app/docs/advanced-configuration](https://facebook.github.io/create-react-app/docs/advanced-configuration)

### Deployment

This section has moved here: [https://facebook.github.io/create-react-app/docs/deployment](https://facebook.github.io/create-react-app/docs/deployment)

### `npm run build` fails to minify

This section has moved here: [https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify](https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify)
