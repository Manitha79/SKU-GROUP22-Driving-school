@echo off
title Apex Driving School System - Maven WAR Builder
echo ====================================================================
echo Building Apex Driving School System WAR package...
echo ====================================================================

set "JAVA_HOME=C:\Users\Sajan\.jdks\openjdk-27"
set "MVN_CMD=C:\Program Files\JetBrains\IntelliJ IDEA 2026.2.3\plugins\maven-plugin\lib\maven3\bin\mvn.cmd"

if not exist "%MVN_CMD%" (
    set "MVN_CMD=mvn"
)

cd /d "%~dp0"
call "%MVN_CMD%" clean package

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ====================================================================
    echo [SUCCESS] DrivingSchoolSystem.war built successfully!
    echo Located at: %~dp0target\DrivingSchoolSystem.war
    echo Deploy this WAR into your Tomcat 10.1\webapps folder.
    echo ====================================================================
) else (
    echo.
    echo [ERROR] Maven build encountered issues. Please review output above.
)
pause
