import axios from 'axios';

const API_BASE_URL = process.env.NODE_ENV === 'development' ? '' : 'http://localhost:8080';

// Create axios instance with base configuration
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor to include JWT token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor to handle token expiry
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expired or invalid
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Authentication services
export const authService = {
  login: async (username, password) => {
    const response = await api.post('/login', { username, password });
    return response.data;
  },

  register: async (username, password, email, role) => {
    const response = await api.post('/register', { username, password, email, role });
    return response.data;
  },
};

// Job posting services
export const jobService = {
  getAllJobs: async () => {
    const response = await api.get('/posts/all');
    return response.data;
  },

  searchJobs: async (searchTerm) => {
    const response = await api.get(`/posts/search/${encodeURIComponent(searchTerm)}`);
    return response.data;
  },

  createJob: async (jobData) => {
    const response = await api.post('/posts/add', jobData);
    return response.data;
  },

  applyForJob: async (jobId) => {
    const response = await api.post(`/posts/apply/${jobId}`);
    return response.data;
  },
};

// Candidate profile services
export const candidateService = {
  getProfile: async (userId) => {
    const response = await api.get(`/candidate/profile/${userId}`);
    return response.data;
  },

  saveProfile: async (profileData) => {
    const response = await api.post('/candidate/profile', profileData);
    return response.data;
  },
};

export default api;