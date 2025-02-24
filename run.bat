@echo off
setlocal enabledelayedexpansion

:: Find the latest JAR file
for /f "delims=" %%a in ('dir /b /a-d /o-d "%~dp0\out\artifacts\zoomly_jar\*.jar"') do set LATEST_JAR=%%a

:: Find Java executable
for /f "delims=" %%a in ('where java.exe') do set JAVA_PATH=%%a

:: Verify requirements
if not defined LATEST_JAR (
    echo Error: JAR file not found! Please build your project first.
    pause
    exit /b 1
)

if not defined JAVA_PATH (
    echo Error: Java not installed or not in PATH!
    pause
    exit /b 1
)

:: Launch application
"%JAVA_PATH%" -jar "%~dp0\out\artifacts\zoomly_jar\%LATEST_JAR%"
if errorlevel 1 (
    echo Error: Failed to launch application!
    pause
    exit /b 1
)