@echo off
REM ===================================================
REM Build and Install Bookstore App
REM ===================================================

cd /d "D:\New folder\bt_adr\pj"

echo Building APK...
call gradlew.bat clean assembleDebug

if errorlevel 1 (
    echo.
    echo Build failed!
    pause
    exit /b 1
)

echo.
echo Build successful!
echo.
echo APK location: D:\New folder\bt_adr\pj\app\build\outputs\apk\debug\app-debug.apk
echo.
echo Uninstalling old version...

REM Try common ADB paths
if exist "C:\Users\%USERNAME%\AppData\Local\Android\Sdk\platform-tools\adb.exe" (
    set ADB_PATH=C:\Users\%USERNAME%\AppData\Local\Android\Sdk\platform-tools\adb.exe
) else if exist "C:\Android\Sdk\platform-tools\adb.exe" (
    set ADB_PATH=C:\Android\Sdk\platform-tools\adb.exe
) else (
    set ADB_PATH=adb
)

%ADB_PATH% uninstall com.example.bookstore

echo.
echo Installing new version...
%ADB_PATH% install "D:\New folder\bt_adr\pj\app\build\outputs\apk\debug\app-debug.apk"

if errorlevel 1 (
    echo.
    echo Installation failed!
    echo Make sure Android emulator is running
    pause
    exit /b 1
)

echo.
echo Installation successful!
echo.
echo Launching app...
%ADB_PATH% shell am start -n com.example.bookstore/.MainActivity

echo.
echo Done! App is running on your emulator.
pause

