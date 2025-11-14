@echo off
REM ===================================================
REM Find ADB Path Helper
REM ===================================================

echo Searching for ADB...
echo.

REM Check standard locations
if exist "C:\Users\%USERNAME%\AppData\Local\Android\Sdk\platform-tools\adb.exe" (
    echo Found ADB at: C:\Users\%USERNAME%\AppData\Local\Android\Sdk\platform-tools\adb.exe
    set "ADB_FOUND=1"
    goto :install
)

if exist "C:\Android\Sdk\platform-tools\adb.exe" (
    echo Found ADB at: C:\Android\Sdk\platform-tools\adb.exe
    set "ADB_FOUND=1"
    goto :install
)

if exist "%ANDROID_HOME%\platform-tools\adb.exe" (
    echo Found ADB at: %ANDROID_HOME%\platform-tools\adb.exe
    set "ADB_FOUND=1"
    goto :install
)

echo.
echo ERROR: ADB not found!
echo.
echo Please ensure Android Studio is installed with SDK.
echo Or set ANDROID_HOME environment variable.
echo.
pause
exit /b 1

:install
echo.
echo Uninstalling old app...
adb uninstall com.example.bookstore

echo.
echo Installing new app...
adb install "D:\New folder\bt_adr\pj\app\build\outputs\apk\debug\app-debug.apk"

echo.
echo Done!
pause

