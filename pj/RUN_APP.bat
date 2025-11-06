@echo off
REM Quick launcher script for Bookstore App on Android Emulator

echo.
echo ===== BOOKSTORE ANDROID APP LAUNCHER =====
echo.

REM Check if Android SDK is available
where adb >nul 2>&1
if errorlevel 1 (
    echo ERROR: ADB not found in PATH
    echo Please add Android SDK tools to your PATH
    echo Typically located at: C:\Users\YourUsername\AppData\Local\Android\Sdk\platform-tools
    pause
    exit /b 1
)

echo Checking for connected devices...
adb devices

echo.
echo Available emulators - run one of these:
echo.
echo 1. Open Android Studio ^> Device Manager
echo 2. Create or select an emulator
echo 3. Click "Play" to start it
echo.
echo Once emulator is running, execute:
echo    cd D:\pj
echo    .\gradlew.bat installDebug
echo.
echo Then launch the app:
echo    adb shell am start -n com.example.bookstore/.MainActivity
echo.
pause

