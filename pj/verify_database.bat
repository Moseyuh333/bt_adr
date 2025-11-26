@echo off
echo ========================================
echo VERIFY DATABASE DATA
echo ========================================
echo.

echo Checking if app is installed...
adb shell pm list packages | findstr bookstore
echo.

echo Checking database file...
adb shell ls -la /data/data/com.example.bookstore/databases/
echo.

echo Pulling database to check...
adb pull /data/data/com.example.bookstore/databases/bookstore_database bookstore_db_backup.db
echo.

echo ✅ Database pulled to: bookstore_db_backup.db
echo You can open it with SQLite browser to verify data
echo.
pause

