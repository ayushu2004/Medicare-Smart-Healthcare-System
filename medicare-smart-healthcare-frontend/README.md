# Medicare Smart Health Care Frontend

React frontend connected with the Spring Boot Medicare REST API.

## Run backend first

```bash
cd medicare-smart-healthcare
mvn spring-boot:run
```

Backend API should run on:

```text
http://localhost:8080/api
```

## Run React frontend

Open another terminal:

```bash
cd medicare-smart-healthcare-frontend
npm install
npm run dev
```

Frontend will run on Vite, usually:

```text
http://localhost:5173
```

## Environment variable

Create `.env` if your backend URL is different:

```bash
cp .env.example .env
```

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

## Demo accounts

| Role | Email | Password |
|---|---|---|
| Admin | `admin@medicare.com` | `Admin@123` |
| Doctor | `doctor@medicare.com` | `Doctor@123` |
| Patient | `patient@medicare.com` | `Patient@123` |
| Receptionist | `reception@medicare.com` | `Reception@123` |

## Features included

- Login with JWT
- Patient registration
- Role based dashboard UI
- Admin user creation
- Doctor listing
- Patient listing
- Appointment booking and status update
- Medical record creation/viewing
- Prescription creation/viewing
