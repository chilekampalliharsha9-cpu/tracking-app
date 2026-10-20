@echo off
set DIR=%~dp0
if exist "%DIR%\gradle\wrapper\gradle-wrapper.jar" (
  java -classpath "%DIR%\gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
) else (
  echo Gradle wrapper not found. Please run Android Studio's Gradle wrapper setup.
  exit /b 1
)
