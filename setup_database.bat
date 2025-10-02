@echo off
echo Setting up ShopDatabase...
echo Please make sure MySQL is running on localhost:3306
echo.

REM Check if mysql command is available
mysql --version >nul 2>&1
if errorlevel 1 (
    echo MySQL client not found. Please install MySQL client or add it to PATH.
    echo You can also manually create the database using the SQL file: setup_database.sql
    pause
    exit /b 1
)

REM Try to connect and setup database
echo Connecting to MySQL...
mysql -u root -p < setup_database.sql 2>nul
if errorlevel 1 (
    echo Failed to create database. Please check your MySQL credentials and connection.
    echo You can also manually create the database using the SQL file: setup_database.sql
    pause
    exit /b 1
)

echo Database setup completed!
pause