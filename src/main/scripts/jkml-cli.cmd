@echo off

setlocal

set "APP_HOME=%~dp0"
set "APP_HOME=%APP_HOME:~0,-1%"

if not "%JAVA_HOME%" == "" goto hasJavaHome
	echo The required JAVA_HOME environment variable is not defined >&2
	exit /b 1
:hasJavaHome

"%JAVA_HOME%\bin\java.exe" -jar "%APP_HOME%\@project.build.finalName@.jar" %*
