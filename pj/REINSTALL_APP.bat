@echo off
echo ============================================
echo   BOOKISH BLISS HEAVEN - REINSTALL APP
echo ============================================
echo.

echo [1/3] Kiem tra emulator...
adb devices
echo.

echo [2/3] Xoa app cu...
adb uninstall com.example.bookstore
echo.

echo [3/3] Cai dat app moi...
cd /d "%~dp0"
call gradlew.bat installDebug
echo.

echo ============================================
echo   HOAN THANH!
echo ============================================
echo.
echo App da duoc cai dat lai len emulator.
echo Ban co the mo app tu menu hoac:
echo   adb shell am start -n com.example.bookstore/.MainActivity
echo.
pause

