@echo off
title Medicare Backend
cd /d "%~dp0medicare-smart-healthcare"
echo Starting Spring Boot backend on http://localhost:8080
where mvn >nul 2>nul
if %errorlevel%==0 (
  mvn clean spring-boot:run
) else (
  echo Maven is not added to PATH.
  echo If Maven exists in Downloads, edit this file or run this manually:
  echo ^& "C:\Users\%USERNAME%\Downloads\apache-maven-3.9.16-bin\apache-maven-3.9.16\bin\mvn.cmd" clean spring-boot:run
  pause
)
