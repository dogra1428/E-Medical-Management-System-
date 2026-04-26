@echo off
title E-Medical Management System
color 0A

echo ===================================================
echo     E-Medical Management System Startup Script
echo ===================================================
echo.
echo Compiling Java Source Files...

if not exist "out" mkdir out
javac -d out -cp "lib/flatlaf-3.2.5.jar" src/utils/*.java src/models/*.java src/api/*.java src/ui/*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Compilation failed. Please ensure you have Java Development Kit (JDK) 17+ installed.
    pause
    exit /b %ERRORLEVEL%
)

echo Compilation Successful!
echo.
echo Launching Application...
java -cp "out;lib/flatlaf-3.2.5.jar" ui.MainFrame

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Application crashed or failed to start.
    pause
)
