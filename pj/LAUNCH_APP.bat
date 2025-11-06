@echo off
REM Auto-launch Bookstore App - finds Android SDK automatically

setlocal enabledelayedexpansion

echo.
echo ===== BOOKSTORE ANDROID APP - AUTO LAUNCHER =====
echo.

REM Try to find Android SDK in common locations
set "SDK_PATHS=%LOCALAPPDATA%\Android\Sdk" "C:\Android\sdk" "%ANDROID_HOME%"

for %%P in (%SDK_PATHS%) do (
    if exist "%%P\platform-tools\adb.exe" (
        set "ADB=%%P\platform-tools\adb.exe"
        echo Found Android SDK at: %%P
        goto :found
    )
)

echo ERROR: Android SDK not found!
echo Please set ANDROID_HOME environment variable or install Android Studio
pause
exit /b 1

:found
echo.
echo Checking connected devices/emulators...
"!ADB!" devices

echo.
echo Launching Bookstore App...
"!ADB!" shell am start -n com.example.bookstore/.MainActivity

if errorlevel 1 (
    echo.
    echo ERROR: Failed to launch app
    echo Make sure emulator is running!
    pause
    exit /b 1
)

echo.
echo App launched successfully!
timeout /t 3

