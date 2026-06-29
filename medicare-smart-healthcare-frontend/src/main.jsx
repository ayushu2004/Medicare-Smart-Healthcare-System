import React, { useEffect, useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { Activity, CalendarDays, FileText, HeartPulse, LogOut, Pill, Shield, Stethoscope, UserPlus, Users } from 'lucide-react';
import { api, getUser, logout, saveAuth } from './api';
import './styles.css';

const roles = ['ADMIN', 'DOCTOR', 'PATIENT', 'RECEPTIONIST'];

function App() {
  const [user, setUser] = useState(getUser());
  const [authMode, setAuthMode] = useState('login');

  function handleLogout() {
    logout();
    setUser(null);
  }

  if (!user) {
    return <AuthPage mode={authMode} setMode={setAuthMode} onAuth={() => setUser(getUser())} />;
  }

  return <Dashboard user={user} onLogout={handleLogout} />;
}

function AuthPage({ mode, setMode, onAuth }) {
  const [form, setForm] = useState({
    name: '', email: 'admin@medicare.com', password: 'Admin@123', phone: '', gender: '', bloodGroup: '', address: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  async function submit(e) {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const payload = mode === 'login'
        ? { email: form.email, password: form.password }
        : form;
      const res = mode === 'login' ? await api.login(payload) : await api.register(payload);
      saveAuth(res);
      onAuth();
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  function demo(email, password) {
    setForm(prev => ({ ...prev, email, password }));
  }

  return (
    <div className="auth-shell">
      <div className="auth-hero">
        <div className="brand"><HeartPulse size={38} /> Medicare Smart Health Care</div>
        <h1>Connected Healthcare Management System</h1>
        <p>React frontend connected to Spring Boot REST API with JWT authentication and RBAC.</p>
        <div className="hero-grid">
          <Feature icon={<Shield />} title="JWT + RBAC" text="Separate dashboards for admin, doctor, patient and receptionist." />
          <Feature icon={<CalendarDays />} title="Appointments" text="Patients book visits, staff and doctors manage status." />
          <Feature icon={<FileText />} title="Records" text="Doctors create records and prescriptions securely." />
        </div>
      </div>

      <form className="auth-card" onSubmit={submit}>
        <h2>{mode === 'login' ? 'Login' : 'Patient Registration'}</h2>
        <p className="muted">Backend URL: <code>http://localhost:8080/api</code></p>

        {error && <div className="alert error">{error}</div>}

        {mode === 'register' && (
          <>
            <label>Name<input value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} required /></label>
            <label>Phone<input value={form.phone} onChange={e => setForm({ ...form, phone: e.target.value })} /></label>
            <div className="two-cols">
              <label>Gender<input value={form.gender} onChange={e => setForm({ ...form, gender: e.target.value })} /></label>
              <label>Blood Group<input value={form.bloodGroup} onChange={e => setForm({ ...form, bloodGroup: e.target.value })} /></label>
            </div>
            <label>Address<input value={form.address} onChange={e => setForm({ ...form, address: e.target.value })} /></label>
          </>
        )}

        <label>Email<input type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} required /></label>
        <label>Password<input type="password" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} required /></label>

        <button className="primary" disabled={loading}>{loading ? 'Please wait...' : mode === 'login' ? 'Login' : 'Register'}</button>

        <button type="button" className="link-btn" onClick={() => setMode(mode === 'login' ? 'register' : 'login')}>
          {mode === 'login' ? 'New patient? Register here' : 'Already have account? Login'}
        </button>

        {mode === 'login' && (
          <div className="demo-box">
            <strong>Demo login:</strong>
            <button type="button" onClick={() => demo('admin@medicare.com', 'Admin@123')}>Admin</button>
            <button type="button" onClick={() => demo('doctor@medicare.com', 'Doctor@123')}>Doctor</button>
            <button type="button" onClick={() => demo('patient@medicare.com', 'Patient@123')}>Patient</button>
            <button type="button" onClick={() => demo('reception@medicare.com', 'Reception@123')}>Reception</button>
          </div>
        )}
      </form>
    </div>
  );
}

function Feature({ icon, title, text }) {
  return <div className="feature">{icon}<b>{title}</b><span>{text}</span></div>;
}

function Dashboard({ user, onLogout }) {
  const [tab, setTab] = useState('overview');
  const navItems = useMemo(() => getNav(user.role), [user.role]);

  return (
    <div className="layout">
      <aside className="sidebar">
        <div className="brand small"><HeartPulse /> Medicare</div>
        <div className="user-card">
          <b>{user.name}</b>
          <span>{user.email}</span>
          <em>{user.role}</em>
        </div>
        <nav>
          {navItems.map(item => (
            <button key={item.id} className={tab === item.id ? 'active' : ''} onClick={() => setTab(item.id)}>
              {item.icon}{item.label}
            </button>
          ))}
        </nav>
        <button className="logout" onClick={onLogout}><LogOut size={18} /> Logout</button>
      </aside>

      <main className="main">
        <header>
          <h1>{navItems.find(i => i.id === tab)?.label || 'Dashboard'}</h1>
          <p>Smart healthcare system dashboard for <b>{user.role}</b>.</p>
        </header>
        {tab === 'overview' && <Overview user={user} />}
        {tab === 'users' && <UsersPanel />}
        {tab === 'doctors' && <DoctorsPanel />}
        {tab === 'patients' && <PatientsPanel user={user} />}
        {tab === 'appointments' && <AppointmentsPanel user={user} />}
        {tab === 'records' && <RecordsPanel user={user} />}
        {tab === 'prescriptions' && <PrescriptionsPanel user={user} />}
      </main>
    </div>
  );
}

function getNav(role) {
  const base = [{ id: 'overview', label: 'Overview', icon: <Activity size={18} /> }];
  if (role === 'ADMIN') base.push({ id: 'users', label: 'User Management', icon: <Users size={18} /> });
  base.push({ id: 'doctors', label: 'Doctors', icon: <Stethoscope size={18} /> });
  if (['ADMIN', 'DOCTOR', 'RECEPTIONIST', 'PATIENT'].includes(role)) base.push({ id: 'appointments', label: 'Appointments', icon: <CalendarDays size={18} /> });
  if (['ADMIN', 'DOCTOR', 'RECEPTIONIST'].includes(role)) base.push({ id: 'patients', label: 'Patients', icon: <Users size={18} /> });
  if (['ADMIN', 'DOCTOR', 'PATIENT'].includes(role)) base.push({ id: 'records', label: 'Medical Records', icon: <FileText size={18} /> });
  if (['ADMIN', 'DOCTOR', 'PATIENT'].includes(role)) base.push({ id: 'prescriptions', label: 'Prescriptions', icon: <Pill size={18} /> });
  return base;
}

function Overview({ user }) {
  return (
    <div className="grid cards">
      <Stat icon={<Shield />} label="Logged in as" value={user.role} />
      <Stat icon={<HeartPulse />} label="Authentication" value="JWT Active" />
      <Stat icon={<Activity />} label="API" value="Spring Boot" />
      <div className="panel wide">
        <h3>Role Permissions</h3>
        <ul className="permissions">
          <li><b>Admin:</b> create users, view all data</li>
          <li><b>Doctor:</b> view patients, create records and prescriptions</li>
          <li><b>Patient:</b> book appointments, view own records and prescriptions</li>
          <li><b>Receptionist:</b> view patients and manage appointment status</li>
        </ul>
      </div>
    </div>
  );
}

function Stat({ icon, label, value }) {
  return <div className="stat">{icon}<span>{label}</span><b>{value}</b></div>;
}

function UsersPanel() {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState('');
  const [form, setForm] = useState({ name: '', email: '', password: 'Password@123', role: 'DOCTOR', phone: '', specialization: '', licenseNumber: '', availability: '' });

  async function load() {
    try { setUsers(await api.users()); } catch (e) { setError(e.message); }
  }
  useEffect(() => { load(); }, []);

  async function create(e) {
    e.preventDefault();
    setError('');
    try {
      await api.createUser(form);
      setForm({ ...form, name: '', email: '', phone: '', specialization: '', licenseNumber: '', availability: '' });
      load();
    } catch (e) { setError(e.message); }
  }

  return <div className="grid two">
    <div className="panel">
      <h3><UserPlus size={18} /> Create User</h3>
      {error && <div className="alert error">{error}</div>}
      <form onSubmit={create} className="stack">
        <input placeholder="Name" value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} required />
        <input placeholder="Email" type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} required />
        <input placeholder="Password" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} required />
        <select value={form.role} onChange={e => setForm({ ...form, role: e.target.value })}>{roles.map(r => <option key={r}>{r}</option>)}</select>
        <input placeholder="Phone" value={form.phone} onChange={e => setForm({ ...form, phone: e.target.value })} />
        {form.role === 'DOCTOR' && <>
          <input placeholder="Specialization" value={form.specialization} onChange={e => setForm({ ...form, specialization: e.target.value })} />
          <input placeholder="License Number" value={form.licenseNumber} onChange={e => setForm({ ...form, licenseNumber: e.target.value })} />
          <input placeholder="Availability" value={form.availability} onChange={e => setForm({ ...form, availability: e.target.value })} />
        </>}
        <button className="primary">Create</button>
      </form>
    </div>
    <TablePanel title="Users" rows={users} columns={['id', 'name', 'email', 'role', 'enabled']} />
  </div>;
}

function DoctorsPanel() {
  const [rows, setRows] = useState([]);
  const [error, setError] = useState('');
  useEffect(() => { api.doctors().then(setRows).catch(e => setError(e.message)); }, []);
  return <TablePanel title="Available Doctors" rows={rows.map(flatProfile)} error={error} columns={['id', 'name', 'email', 'specialization', 'phone', 'availability']} />;
}

function PatientsPanel() {
  const [rows, setRows] = useState([]);
  const [error, setError] = useState('');
  useEffect(() => { api.patients().then(setRows).catch(e => setError(e.message)); }, []);
  return <TablePanel title="Patients" rows={rows.map(flatProfile)} error={error} columns={['id', 'name', 'email', 'phone', 'gender', 'bloodGroup', 'address']} />;
}

function AppointmentsPanel({ user }) {
  const [doctors, setDoctors] = useState([]);
  const [appointments, setAppointments] = useState([]);
  const [error, setError] = useState('');
  const [form, setForm] = useState({ doctorId: '', appointmentTime: '', reason: '' });

  async function load() {
    setError('');
    try {
      setDoctors(await api.doctors());
      const data = ['ADMIN', 'RECEPTIONIST'].includes(user.role) ? await api.allAppointments() : await api.myAppointments();
      setAppointments(data.map(flatAppointment));
    } catch (e) { setError(e.message); }
  }
  useEffect(() => { load(); }, []);

  async function book(e) {
    e.preventDefault();
    try {
      await api.bookAppointment({ ...form, doctorId: Number(form.doctorId) });
      setForm({ doctorId: '', appointmentTime: '', reason: '' });
      load();
    } catch (e) { setError(e.message); }
  }

  async function update(id, status) {
    try { await api.updateAppointmentStatus(id, status); load(); } catch (e) { setError(e.message); }
  }

  return <div className="grid two">
    {user.role === 'PATIENT' && <div className="panel">
      <h3>Book Appointment</h3>
      {error && <div className="alert error">{error}</div>}
      <form onSubmit={book} className="stack">
        <select value={form.doctorId} onChange={e => setForm({ ...form, doctorId: e.target.value })} required>
          <option value="">Select doctor</option>
          {doctors.map(d => <option key={d.id} value={d.id}>{d.user?.name} - {d.specialization}</option>)}
        </select>
        <input type="datetime-local" value={form.appointmentTime} onChange={e => setForm({ ...form, appointmentTime: e.target.value })} required />
        <textarea placeholder="Reason" value={form.reason} onChange={e => setForm({ ...form, reason: e.target.value })} />
        <button className="primary">Book</button>
      </form>
    </div>}
    <div className="panel wide">
      <h3>Appointments</h3>
      {error && <div className="alert error">{error}</div>}
      <DataTable rows={appointments} columns={['id', 'patient', 'doctor', 'appointmentTime', 'reason', 'status']} />
      {['ADMIN', 'DOCTOR', 'RECEPTIONIST'].includes(user.role) && <div className="status-actions">
        <p>Select appointment ID to update status:</p>
        {appointments.map(a => <div key={a.id} className="status-row"><b>#{a.id}</b>{['CONFIRMED','COMPLETED','CANCELLED'].map(s => <button key={s} onClick={() => update(a.id, s)}>{s}</button>)}</div>)}
      </div>}
    </div>
  </div>;
}

function RecordsPanel({ user }) {
  const [rows, setRows] = useState([]);
  const [patients, setPatients] = useState([]);
  const [error, setError] = useState('');
  const [form, setForm] = useState({ patientId: '', symptoms: '', diagnosis: '', notes: '' });

  async function load() {
    try {
      if (user.role === 'DOCTOR') setPatients(await api.patients());
      const data = user.role === 'PATIENT' ? await api.myMedicalRecords() : await api.allMedicalRecords();
      setRows(data.map(flatRecord));
    } catch (e) { setError(e.message); }
  }
  useEffect(() => { load(); }, []);

  async function create(e) {
    e.preventDefault();
    try { await api.createMedicalRecord({ ...form, patientId: Number(form.patientId) }); setForm({ patientId: '', symptoms: '', diagnosis: '', notes: '' }); load(); } catch (e) { setError(e.message); }
  }

  return <div className="grid two">
    {user.role === 'DOCTOR' && <div className="panel"><h3>Create Medical Record</h3><form className="stack" onSubmit={create}>
      <select value={form.patientId} onChange={e => setForm({ ...form, patientId: e.target.value })} required><option value="">Select patient</option>{patients.map(p => <option key={p.id} value={p.id}>{p.user?.name}</option>)}</select>
      <input placeholder="Symptoms" value={form.symptoms} onChange={e => setForm({ ...form, symptoms: e.target.value })} />
      <input placeholder="Diagnosis" value={form.diagnosis} onChange={e => setForm({ ...form, diagnosis: e.target.value })} />
      <textarea placeholder="Notes" value={form.notes} onChange={e => setForm({ ...form, notes: e.target.value })} />
      <button className="primary">Save Record</button>
    </form></div>}
    <TablePanel title="Medical Records" rows={rows} error={error} columns={['id', 'patient', 'doctor', 'symptoms', 'diagnosis', 'notes']} />
  </div>;
}

function PrescriptionsPanel({ user }) {
  const [rows, setRows] = useState([]);
  const [patients, setPatients] = useState([]);
  const [error, setError] = useState('');
  const [form, setForm] = useState({ patientId: '', medicineName: '', dosage: '', instructions: '', startDate: '', endDate: '' });

  async function load() {
    try {
      if (user.role === 'DOCTOR') setPatients(await api.patients());
      const data = user.role === 'PATIENT' ? await api.myPrescriptions() : await api.allPrescriptions();
      setRows(data.map(flatPrescription));
    } catch (e) { setError(e.message); }
  }
  useEffect(() => { load(); }, []);

  async function create(e) {
    e.preventDefault();
    try { await api.createPrescription({ ...form, patientId: Number(form.patientId) }); setForm({ patientId: '', medicineName: '', dosage: '', instructions: '', startDate: '', endDate: '' }); load(); } catch (e) { setError(e.message); }
  }

  return <div className="grid two">
    {user.role === 'DOCTOR' && <div className="panel"><h3>Create Prescription</h3><form className="stack" onSubmit={create}>
      <select value={form.patientId} onChange={e => setForm({ ...form, patientId: e.target.value })} required><option value="">Select patient</option>{patients.map(p => <option key={p.id} value={p.id}>{p.user?.name}</option>)}</select>
      <input placeholder="Medicine" value={form.medicineName} onChange={e => setForm({ ...form, medicineName: e.target.value })} required />
      <input placeholder="Dosage" value={form.dosage} onChange={e => setForm({ ...form, dosage: e.target.value })} />
      <input placeholder="Instructions" value={form.instructions} onChange={e => setForm({ ...form, instructions: e.target.value })} />
      <input type="date" value={form.startDate} onChange={e => setForm({ ...form, startDate: e.target.value })} />
      <input type="date" value={form.endDate} onChange={e => setForm({ ...form, endDate: e.target.value })} />
      <button className="primary">Save Prescription</button>
    </form></div>}
    <TablePanel title="Prescriptions" rows={rows} error={error} columns={['id', 'patient', 'doctor', 'medicineName', 'dosage', 'instructions', 'startDate', 'endDate']} />
  </div>;
}

function TablePanel({ title, rows, columns, error }) {
  return <div className="panel wide"><h3>{title}</h3>{error && <div className="alert error">{error}</div>}<DataTable rows={rows} columns={columns} /></div>;
}

function DataTable({ rows, columns }) {
  if (!rows?.length) return <div className="empty">No data found.</div>;
  return <div className="table-wrap"><table><thead><tr>{columns.map(c => <th key={c}>{c}</th>)}</tr></thead><tbody>{rows.map((row, i) => <tr key={row.id || i}>{columns.map(c => <td key={c}>{formatValue(row[c])}</td>)}</tr>)}</tbody></table></div>;
}

function formatValue(v) {
  if (v === null || v === undefined || v === '') return '-';
  if (typeof v === 'boolean') return v ? 'Yes' : 'No';
  if (typeof v === 'object') return JSON.stringify(v);
  return String(v).replace('T', ' ');
}

function flatProfile(p) {
  return { ...p, name: p.user?.name, email: p.user?.email, role: p.user?.role };
}
function flatAppointment(a) {
  return { id: a.id, patient: a.patient?.user?.name, doctor: a.doctor?.user?.name, appointmentTime: a.appointmentTime, reason: a.reason, status: a.status };
}
function flatRecord(r) {
  return { id: r.id, patient: r.patient?.user?.name, doctor: r.doctor?.user?.name, symptoms: r.symptoms, diagnosis: r.diagnosis, notes: r.notes };
}
function flatPrescription(p) {
  return { id: p.id, patient: p.patient?.user?.name, doctor: p.doctor?.user?.name, medicineName: p.medicineName, dosage: p.dosage, instructions: p.instructions, startDate: p.startDate, endDate: p.endDate };
}

createRoot(document.getElementById('root')).render(<App />);
