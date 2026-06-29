const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'https://medicare-smart-healthcare-system-3.onrender.com/api';

export function getToken() {
  return localStorage.getItem('medicare_token');
}

export function getUser() {
  const user = localStorage.getItem('medicare_user');
  return user ? JSON.parse(user) : null;
}

export function saveAuth(authResponse) {
  localStorage.setItem('medicare_token', authResponse.token);
  localStorage.setItem('medicare_user', JSON.stringify({
    userId: authResponse.userId,
    name: authResponse.name,
    email: authResponse.email,
    role: authResponse.role,
  }));
}

export function logout() {
  localStorage.removeItem('medicare_token');
  localStorage.removeItem('medicare_user');
}

export async function apiRequest(path, options = {}) {
  const token = getToken();
  const headers = {
    'Content-Type': 'application/json',
    ...(options.headers || {}),
  };

  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers,
  });

  const contentType = response.headers.get('content-type') || '';
  const data = contentType.includes('application/json') ? await response.json() : await response.text();

  if (!response.ok) {
    const message = data?.message || data?.error || (Array.isArray(data?.details) ? data.details.join(', ') : null) || 'Request failed';
    throw new Error(message);
  }

  return data;
}

export const api = {
  login: (payload) => apiRequest('/auth/login', { method: 'POST', body: JSON.stringify(payload) }),
  register: (payload) => apiRequest('/auth/register', { method: 'POST', body: JSON.stringify(payload) }),

  doctors: () => apiRequest('/doctors'),
  patients: () => apiRequest('/patients'),
  myPatientProfile: () => apiRequest('/patients/me'),

  users: () => apiRequest('/admin/users'),
  createUser: (payload) => apiRequest('/admin/users', { method: 'POST', body: JSON.stringify(payload) }),
  setUserEnabled: (id, enabled) => apiRequest(`/admin/users/${id}/enabled?enabled=${enabled}`, { method: 'PATCH' }),

  allAppointments: () => apiRequest('/appointments'),
  myAppointments: () => apiRequest('/appointments/mine'),
  bookAppointment: (payload) => apiRequest('/appointments', { method: 'POST', body: JSON.stringify(payload) }),
  updateAppointmentStatus: (id, status) => apiRequest(`/appointments/${id}/status?status=${status}`, { method: 'PATCH' }),

  allMedicalRecords: () => apiRequest('/medical-records'),
  myMedicalRecords: () => apiRequest('/medical-records/mine'),
  createMedicalRecord: (payload) => apiRequest('/medical-records', { method: 'POST', body: JSON.stringify(payload) }),

  allPrescriptions: () => apiRequest('/prescriptions'),
  myPrescriptions: () => apiRequest('/prescriptions/mine'),
  createPrescription: (payload) => apiRequest('/prescriptions', { method: 'POST', body: JSON.stringify(payload) }),
};
