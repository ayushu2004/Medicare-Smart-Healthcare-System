# Medicare Smart Healthcare System

A full-stack healthcare management platform built with **Spring Boot**, **React**, **JWT authentication**, **role-based access control (RBAC)**, and an SQL database. The application provides separate workflows for administrators, doctors, patients, and receptionists to manage appointments, medical records, prescriptions, and users in a secure and structured way.

---

## Project Overview

The Medicare Smart Healthcare System is designed to digitize common hospital and clinic operations. It allows patients to register and book appointments, doctors to manage patient records and prescriptions, receptionists to manage appointment status, and administrators to control users and system access.

The backend is developed as a REST API using Spring Boot, while the frontend is built using React with a modern dark theme dashboard. Authentication is handled using JWT, and authorization is controlled using role-based access.

---

## Key Features

### Authentication and Security

* User login using email and password
* JWT-based authentication
* Secure password hashing using BCrypt
* Role-based access control using Spring Security
* Protected REST APIs based on user roles
* Stateless backend authentication

### User Roles

| Role         | Description                                                                             |
| ------------ | --------------------------------------------------------------------------------------- |
| Admin        | Manages users and has access to system-wide data                                        |
| Doctor       | Views patients, manages appointments, creates medical records and prescriptions         |
| Patient      | Registers, logs in, books appointments, and views own medical records and prescriptions |
| Receptionist | Views patients/doctors and manages appointment status                                   |

### Admin Module

* View all users
* Create users with specific roles
* Create doctors, patients, receptionists, and admins
* Enable or disable users
* Manage system access

### Patient Module

* Patient registration
* Patient login
* View available doctors
* Book appointments
* View own appointments
* View own medical records
* View own prescriptions

### Doctor Module

* View patient list
* View assigned appointments
* Update appointment status
* Create medical records
* Create prescriptions
* View medical history

### Receptionist Module

* View doctors
* View patients
* View appointments
* Update appointment status

### Appointment Module

* Patients can book appointments with doctors
* Admin, doctor, and receptionist can update appointment status
* Supported statuses:

  * REQUESTED
  * CONFIRMED
  * COMPLETED
  * CANCELLED

### Medical Records and Prescriptions

* Doctors can create medical records for patients
* Doctors can create prescriptions
* Patients can only view their own records and prescriptions
* Admin and doctors have controlled access based on role

---

## Tech Stack

### Backend

* Java 17+
* Spring Boot
* Spring Web
* Spring Security
* Spring Data JPA
* Hibernate
* JWT
* BCrypt Password Encoder
* H2 Database
* MySQL-ready configuration
* Maven

### Frontend

* React.js
* Vite
* JavaScript
* CSS
* Fetch API
* LocalStorage for JWT token
* Responsive dark dashboard UI

### Database

* H2 in-memory database for development/demo
* MySQL configuration included for production-style setup

---

## System Architecture

```text
React Frontend
      |
      | HTTP Requests with JWT Token
      v
Spring Boot REST API
      |
      | Spring Security + RBAC
      v
Service Layer
      |
      v
Spring Data JPA / Hibernate
      |
      v
SQL Database
```

---

## Project Structure

```text
medicare-smart-healthcare
├── src/main/java/com/medicare/smarthealthcare
│   ├── config
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── exception
│   ├── repository
│   ├── security
│   └── service
│
├── src/main/resources
│   ├── application.properties
│   └── application-mysql.properties
│
└── pom.xml

medicare-smart-healthcare-frontend
├── src
│   ├── api.js
│   ├── main.jsx
│   └── styles.css
│
├── index.html
└── package.json
```

---

## Database Entities

The system includes the following major entities:

* AppUser
* PatientProfile
* DoctorProfile
* Appointment
* MedicalRecord
* Prescription

### Relationship Overview

* One user can have one patient profile or doctor profile depending on role
* A patient can book many appointments
* A doctor can have many appointments
* A doctor can create many medical records
* A doctor can create many prescriptions
* Medical records and prescriptions are linked to patients and doctors

---

## API Endpoint Summary

### Authentication

| Method | Endpoint             | Access |
| ------ | -------------------- | ------ |
| POST   | `/api/auth/register` | Public |
| POST   | `/api/auth/login`    | Public |

### Admin

| Method | Endpoint                        | Access |
| ------ | ------------------------------- | ------ |
| GET    | `/api/admin/users`              | Admin  |
| POST   | `/api/admin/users`              | Admin  |
| PATCH  | `/api/admin/users/{id}/enabled` | Admin  |

### Doctors

| Method | Endpoint            | Access              |
| ------ | ------------------- | ------------------- |
| GET    | `/api/doctors`      | Authenticated users |
| GET    | `/api/doctors/{id}` | Authenticated users |

### Patients

| Method | Endpoint             | Access                      |
| ------ | -------------------- | --------------------------- |
| GET    | `/api/patients`      | Admin, Doctor, Receptionist |
| GET    | `/api/patients/me`   | Patient                     |
| GET    | `/api/patients/{id}` | Admin, Doctor, Receptionist |

### Appointments

| Method | Endpoint                        | Access                      |
| ------ | ------------------------------- | --------------------------- |
| POST   | `/api/appointments`             | Patient                     |
| GET    | `/api/appointments`             | Admin, Receptionist         |
| GET    | `/api/appointments/mine`        | Patient, Doctor             |
| PATCH  | `/api/appointments/{id}/status` | Admin, Doctor, Receptionist |

### Medical Records

| Method | Endpoint                                   | Access          |
| ------ | ------------------------------------------ | --------------- |
| POST   | `/api/medical-records`                     | Doctor          |
| GET    | `/api/medical-records`                     | Admin, Doctor   |
| GET    | `/api/medical-records/mine`                | Patient, Doctor |
| GET    | `/api/medical-records/patient/{patientId}` | Admin, Doctor   |

### Prescriptions

| Method | Endpoint                                 | Access          |
| ------ | ---------------------------------------- | --------------- |
| POST   | `/api/prescriptions`                     | Doctor          |
| GET    | `/api/prescriptions`                     | Admin, Doctor   |
| GET    | `/api/prescriptions/mine`                | Patient, Doctor |
| GET    | `/api/prescriptions/patient/{patientId}` | Admin, Doctor   |

---

## Demo Credentials

| Role         | Email                    | Password        |
| ------------ | ------------------------ | --------------- |
| Admin        | `admin@medicare.com`     | `Admin@123`     |
| Doctor       | `doctor@medicare.com`    | `Doctor@123`    |
| Patient      | `patient@medicare.com`   | `Patient@123`   |
| Receptionist | `reception@medicare.com` | `Reception@123` |

---

## How to Run the Project

### Prerequisites

Make sure the following are installed:

* Java 17 or above
* Maven
* Node.js and npm

---

## Backend Setup

Navigate to the backend folder:

```bash
cd medicare-smart-healthcare
```

Run the Spring Boot application:

```bash
mvn clean spring-boot:run
```

The backend will start at:

```text
http://localhost:8080
```

Successful startup message:

```text
Tomcat started on port 8080
Started SmartHealthcareApplication
```

---

## Frontend Setup

Open a new terminal and navigate to the frontend folder:

```bash
cd medicare-smart-healthcare-frontend
```

Install dependencies:

```bash
npm install
```

Start the frontend:

```bash
npm run dev
```

The frontend will start at:

```text
http://localhost:5173
```

---

## H2 Database Console

The project uses H2 database by default for easy local testing.

Open:

```text
http://localhost:8080/h2-console
```

Use the following values:

```text
Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:mem:medicaredb
User Name: sa
Password: leave blank
```

Example query:

```sql
SELECT * FROM APP_USERS;
```

---

## MySQL Configuration

The project also includes MySQL profile support.

Use the MySQL profile:

```bash
export SPRING_PROFILES_ACTIVE=mysql
```

Example environment variables:

```bash
export DB_URL="jdbc:mysql://localhost:3306/medicare_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
export DB_USERNAME="root"
export DB_PASSWORD="root"
export JWT_SECRET="yourlongjwtsecretkey"
```

Then run:

```bash
mvn spring-boot:run
```

---

## Security Implementation

The project uses Spring Security with JWT.

### Login Flow

1. User submits email and password
2. Backend authenticates credentials
3. JWT token is generated
4. Frontend stores token in localStorage
5. Token is sent in the Authorization header for protected APIs

Example header:

```text
Authorization: Bearer <jwt_token>
```

### RBAC Flow

Each user has a role stored in the database. Spring Security checks the role before allowing access to protected endpoints.

Example:

```java
@PreAuthorize("hasRole('DOCTOR')")
```

---

## Skills Demonstrated

This project demonstrates practical understanding of:

* REST API development
* Java backend development
* Spring Boot architecture
* Spring Security
* JWT authentication
* Role-based authorization
* SQL database design
* JPA entity relationships
* React frontend development
* API integration
* Protected routes and dashboard-based UI
* Error handling
* Full-stack project structuring

---

## Future Enhancements

* Email notifications for appointment confirmation
* Payment gateway integration
* Doctor availability calendar
* Patient health reports dashboard
* File upload for medical documents
* Search and filter functionality
* Pagination for large data tables
* Deployment using Docker
* PostgreSQL/MySQL production deployment

---

## Conclusion

The Medicare Smart Healthcare System is a complete full-stack healthcare management application. It combines a secure Spring Boot REST API with a React frontend and demonstrates core concepts such as authentication, authorization, SQL database handling, role-based dashboards, and real-world healthcare workflows.


## 👨‍💻 Author

**Ayushu Singh**  
Java Full Stack Developer | Software Engineer  
B.Tech CSE Graduate (2022–2026)
