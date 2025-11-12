@echo off
echo.
echo ===================================
echo   XOA VA CAI LAI APP
echo ===================================
echo.
echo Dang xoa app cu...
adb uninstall com.example.bookstore
echo.
echo Dang build va cai app moi...
cd /d "%~dp0"
call gradlew.bat clean installDebug
echo.
echo ===================================
echo   XONG! App da duoc cai lai.
echo ===================================
echo.
echo Mo app tren emulator de test!
echo.
pause

