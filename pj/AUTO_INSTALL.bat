@echo off
REM ===================================================
REM Auto-Find SDK and Install App
REM ===================================================
REM This script automatically finds Android SDK and installs app

setlocal enabledelayedexpansion

echo Searching for Android SDK...
echo.

REM Check common SDK locations
if exist "%USERPROFILE%\AppData\Local\Android\Sdk\platform-tools\adb.exe" (
    set "SDK_PATH=%USERPROFILE%\AppData\Local\Android\Sdk"
    echo Found SDK at: !SDK_PATH!
    goto :found
)

if exist "C:\Android\Sdk\platform-tools\adb.exe" (
    set "SDK_PATH=C:\Android\Sdk"
    echo Found SDK at: !SDK_PATH!
    goto :found
)

if exist "%ANDROID_HOME%\platform-tools\adb.exe" (
    set "SDK_PATH=%ANDROID_HOME%"
    echo Found SDK at: !SDK_PATH!
    goto :found
)

REM If not found, show error
echo.
echo ERROR: Android SDK not found!
echo.
echo Please:
echo 1. Install Android Studio
echo 2. Open Android Studio (it will download SDK)
echo 3. Check Tools ^> SDK Manager for SDK location
echo 4. Run this script again
echo.
echo Or add to PATH: [SDK]\platform-tools
echo.
pause
exit /b 1

:found
echo.
echo SDK Location: !SDK_PATH!
echo.
set "ADB=!SDK_PATH!\platform-tools\adb.exe"

echo Uninstalling old version...
"!ADB!" uninstall com.example.bookstore

echo.
echo Installing new version...
"!ADB!" install "D:\New folder\bt_adr\pj\app\build\outputs\apk\debug\app-debug.apk"

if errorlevel 1 (
    echo.
    echo Installation failed!
    echo Make sure:
    echo - Emulator is running
    echo - Or device is connected
    echo.
    pause
    exit /b 1
)

echo.
echo Installation successful!
echo.
echo Launching app...
"!ADB!" shell am start -n com.example.bookstore/.MainActivity

echo.
echo Done! Check your emulator/device.
pause
endlocal

