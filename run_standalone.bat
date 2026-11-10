@echo off
title Apex Driving School System - Standalone Runner
echo ====================================================================
echo Starting Apex Driving School System (Single-File Unified Edition)...
echo ====================================================================

set "JAVA_EXE="
if exist "C:\Users\Sajan\.jdks\openjdk-27\bin\java.exe" set "JAVA_EXE=C:\Users\Sajan\.jdks\openjdk-27\bin\java.exe"
if "%JAVA_EXE%"=="" (
    for /f "tokens=*" %%i in ('where java 2^>nul') do set "JAVA_EXE=%%i"
)

if "%JAVA_EXE%"=="" (
    echo [ERROR] java.exe was not found. Please install JDK 17 or higher.
    pause
    exit /b 1
)

echo Using Java: %JAVA_EXE%
cd /d "%~dp0"
"%JAVA_EXE%" DrivingSchoolSystemUnified.java
pause
