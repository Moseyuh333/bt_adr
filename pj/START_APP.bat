@echo off
REM Simple launcher - requires Android Studio to have started emulator first

setlocal enabledelayedexpansion

REM Try to find adb in Android SDK
for %%P in (
    "%LOCALAPPDATA%\Android\Sdk\platform-tools\adb.exe"
    "%ANDROID_HOME%\platform-tools\adb.exe"
    "C:\Android\sdk\platform-tools\adb.exe"
) do (
    if exist "%%P" (
        set "ADB=%%P"
        goto :found
    )
)

echo ERROR: adb.exe not found!
echo Please start Android Studio and launch emulator manually
pause
exit /b 1

:found
echo.
echo Launching Bookstore App...
"!ADB!" shell am start -n com.example.bookstore/.MainActivity

echo.
echo If app doesn't appear, emulator may not be running.
echo Start emulator from Android Studio Device Manager first.
echo.
pause

