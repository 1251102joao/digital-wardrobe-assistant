@echo off
setlocal
chcp 65001 > nul
cd /d "%~dp0"
if not exist dist\assistente-vestuario.jar call compilar.bat
if errorlevel 1 exit /b 1
java -Dfile.encoding=UTF-8 -jar dist\assistente-vestuario.jar %*
