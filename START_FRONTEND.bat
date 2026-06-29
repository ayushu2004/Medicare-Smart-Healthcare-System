@echo off
title Medicare Frontend
cd /d "%~dp0medicare-smart-healthcare-frontend"
echo Starting React frontend on http://localhost:5173
if not exist node_modules (
  npm install
)
npm run dev
pause
