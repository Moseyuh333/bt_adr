@echo off
REM ===================================================
REM Script to uninstall old app and install new APK
REM ===================================================

echo Uninstalling old version of app...

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
echo Installing new APK...
%ADB_PATH% install "D:\New folder\bt_adr\pj\app\build\outputs\apk\debug\app-debug.apk"

echo.
echo Installation complete!
echo.
pause

