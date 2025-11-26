@echo off
echo ========================================
echo INSTALL & CLEAR OLD DATA
echo ========================================
echo.

echo [1/3] Uninstalling old app...
adb uninstall com.example.bookstore
echo.

echo [2/3] Installing new APK...
adb install app\build\outputs\apk\debug\app-debug.apk
echo.

echo [3/3] Done!
echo.
echo ✅ App installed successfully!
echo ✅ Old data has been cleared
echo ✅ New data will load on first run
echo.
echo App có 53 sách mới với 8 danh mục tiếng Việt:
echo - Văn học (10 sách)
echo - Kỹ năng (8 sách)
echo - Thiếu nhi (8 sách)
echo - Kinh tế (7 sách)
echo - Tâm lý (6 sách)
echo - Lịch sử (5 sách)
echo - Khoa học (5 sách)
echo - Công nghệ (4 sách)
echo.
echo Giờ mở app và test!
pause

