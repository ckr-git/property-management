@echo off
echo Starting Property Management System...
echo.

REM Start Backend
echo [Backend] Starting...
start "Backend" cmd /k "cd /d "%~dp0" && mvn spring-boot:run"

REM Wait
timeout /t 10 /nobreak >nul

REM Start Frontend  
echo [Frontend] Starting...
cd /d "%~dp0frontend"
start "Frontend" cmd /k "npm install && npm run dev"

echo.
echo Services starting in separate windows
echo Backend:  http://localhost:8080/api
echo Frontend: http://localhost:3000
echo.
pause