MEDICARE SMART HEALTHCARE - FIXED BLACK UI VERSION

This zip contains:
1. medicare-smart-healthcare          = Spring Boot backend
2. medicare-smart-healthcare-frontend = React frontend with black/dark UI

FIXES INCLUDED:
- JWT Illegal base64 character '-' fixed
- JWT secret changed to safe plain text
- Frontend black background/theme added
- CORS enabled for React localhost:5173

RUN BACKEND:
cd medicare-smart-healthcare
mvn clean spring-boot:run

If mvn is not recognized but your Maven is in Downloads, run:
& "C:\Users\Ayushu\Downloads\apache-maven-3.9.16-bin\apache-maven-3.9.16\bin\mvn.cmd" clean spring-boot:run

RUN FRONTEND in another terminal:
cd medicare-smart-healthcare-frontend
npm install
npm run dev

Open:
http://localhost:5173

LOGIN:
admin@medicare.com       Admin@123
doctor@medicare.com      Doctor@123
patient@medicare.com     Patient@123
reception@medicare.com   Reception@123
