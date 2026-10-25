@echo off
echo ============================================================================
echo   Kongu Engineering College (Autonomous) - Regulation 2024 (R2024)
echo   Compiling Java JDBC Academic & Continuous Evaluation System...
echo ============================================================================
javac -encoding UTF-8 -cp ".;lib/*" *.java
if %ERRORLEVEL% equ 0 (
    echo [SUCCESS] Compilation completed without errors.
) else (
    echo [ERROR] Compilation failed.
)
pause
