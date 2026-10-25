@echo off
rem Double-click this file to build the Cyber AI Guide app.
cd /d "%~dp0"
set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
echo Building Cyber AI Guide... (first build can take a few minutes)
echo.
call gradlew.bat assembleDebug
echo.
if exist "app\build\outputs\apk\debug\app-debug.apk" (
    echo ============================================================
    echo   BUILD OK - your app file is ready:
    echo   app\build\outputs\apk\debug\app-debug.apk
    echo ============================================================
) else (
    echo   BUILD FAILED - read the error messages above.
)
echo.
pause
