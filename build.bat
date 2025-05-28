@echo off
REM R2-D2 Morse Translator Build Script for Windows

echo.
echo --- Cleaning 'out' directory ---
if exist out rmdir /s /q out
mkdir out

echo.
echo --- Compiling Java source files ---
javac -d out src/*.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Java compilation failed!
    pause
    goto :eof
)

echo.
echo --- Running R2-D2 Morse Translator GUI ---
java -cp out MorseTranslatorGUI

echo.
echo --- Build and Run Complete ---
pause