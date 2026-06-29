# Medicare Smart Health Care System

A Java Spring Boot REST API project for a smart healthcare/Medicare system with:

- JWT authentication
- Role-Based Access Control (RBAC)
- SQL persistence using Spring Data JPA
- H2 database for quick demo and MySQL profile for production/local SQL
- Patients, doctors, appointments, medical records and prescriptions

## Tech stack

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Security
- Spring Data JPA / Hibernate
- JWT using `jjwt`
- H2 / MySQL
- Maven

## Roles

| Role | Capabilities |
|---|---|
| `ADMIN` | Manage users, view all medical data |
| `DOCTOR` | View patients, manage own appointments, create medical records and prescriptions |
| `PATIENT` | Register/login, view doctors, book appointments, view own records and prescriptions |
| `RECEPTIONIST` | View patients/doctors, view and update appointments |

## Run the project

### Option 1: H2 demo database

```bash
cd medicare-smart-healthcare
mvn spring-boot:run
```

H2 console: <http://localhost:8080/h2-console>

- JDBC URL: `jdbc:h2:mem:medicaredb`
- Username: `sa`
- Password: empty

### Option 2: MySQL

Create/start MySQL, then run:

```bash
cd medicare-smart-healthcare
export SPRING_PROFILES_ACTIVE=mysql
export DB_URL='jdbc:mysql://localhost:3306/medicare_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC'
export DB_USERNAME=root
export DB_PASSWORD=root
export JWT_SECRET='replace-with-a-long-secret-key-at-least-32-characters'
mvn spring-boot:run
```

## Demo users seeded on startup

| Role | Email | Password |
|---|---|---|
| Admin | `admin@medicare.com` | `Admin@123` |
| Doctor | `doctor@medicare.com` | `Doctor@123` |
| Patient | `patient@medicare.com` | `Patient@123` |
| Receptionist | `reception@medicare.com` | `Reception@123` |

## API examples

### 1. Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@medicare.com","password":"Admin@123"}'
```

Copy the returned `token` and use it as:

```bash
Authorization: Bearer <token>
```

### 2. Register patient

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H 'Content-Type: application/json' \
  -d '{
    "name":"New Patient",
    "email":"newpatient@example.com",
    "password":"Patient@123",
    "phone":"9000000000",
    "gender":"Female",
    "bloodGroup":"A+",
    "address":"Mumbai"
  }'
```

### 3. Admin creates a doctor

```bash
curl -X POST http://localhost:8080/api/admin/users \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <ADMIN_TOKEN>' \
  -d '{
    "name":"Dr. Meera Rao",
    "email":"meera@medicare.com",
    "password":"Doctor@123",
    "role":"DOCTOR",
    "phone":"9888888888",
    "specialization":"Cardiology",
    "licenseNumber":"CARD-2001",
    "availability":"Mon-Sat 09:00-13:00"
  }'
```

### 4. List doctors

```bash
curl http://localhost:8080/api/doctors \
  -H 'Authorization: Bearer <TOKEN>'
```

### 5. Patient books appointment

Use a doctor profile `id` from `/api/doctors`.

```bash
curl -X POST http://localhost:8080/api/appointments \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <PATIENT_TOKEN>' \
  -d '{
    "doctorId":1,
    "appointmentTime":"2026-07-01T10:30:00",
    "reason":"Fever and headache"
  }'
```

### 6. Doctor creates medical record

Use a patient profile `id` from `/api/patients`.

```bash
curl -X POST http://localhost:8080/api/medical-records \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <DOCTOR_TOKEN>' \
  -d '{
    "patientId":1,
    "symptoms":"Fever, headache",
    "diagnosis":"Viral fever",
    "notes":"Rest and hydration advised"
  }'
```

### 7. Doctor creates prescription

```bash
curl -X POST http://localhost:8080/api/prescriptions \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <DOCTOR_TOKEN>' \
  -d '{
    "patientId":1,
    "medicineName":"Paracetamol",
    "dosage":"500mg twice daily",
    "instructions":"After food",
    "startDate":"2026-07-01",
    "endDate":"2026-07-03"
  }'
```

## Endpoint summary

| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/auth/register` | Public |
| POST | `/api/auth/login` | Public |
| GET | `/api/doctors` | Authenticated users |
| GET | `/api/doctors/{id}` | Authenticated users |
| GET | `/api/patients` | Admin, Doctor, Receptionist |
| GET | `/api/patients/me` | Patient |
| POST | `/api/admin/users` | Admin |
| GET | `/api/admin/users` | Admin |
| PATCH | `/api/admin/users/{id}/enabled?enabled=false` | Admin |
| POST | `/api/appointments` | Patient |
| GET | `/api/appointments` | Admin, Receptionist |
| GET | `/api/appointments/mine` | Patient, Doctor |
| PATCH | `/api/appointments/{id}/status?status=CONFIRMED` | Admin, Doctor, Receptionist |
| POST | `/api/medical-records` | Doctor |
| GET | `/api/medical-records` | Admin, Doctor |
| GET | `/api/medical-records/mine` | Patient, Doctor |
| GET | `/api/medical-records/patient/{patientId}` | Admin, Doctor |
| POST | `/api/prescriptions` | Doctor |
| GET | `/api/prescriptions` | Admin, Doctor |
| GET | `/api/prescriptions/mine` | Patient, Doctor |
| GET | `/api/prescriptions/patient/{patientId}` | Admin, Doctor |

## Project structure

```text
src/main/java/com/medicare/smarthealthcare
├── config          # Security and seed data
├── controller      # REST APIs
├── dto             # Request/response objects
├── entity          # JPA entities and enums
├── exception       # Global error handling
├── repository      # Spring Data JPA repositories
├── security        # JWT service/filter
└── service         # Business services
```
