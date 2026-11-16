@echo off
setlocal
chcp 65001 > nul
cd /d "%~dp0"
call compilar.bat
if errorlevel 1 exit /b 1
if not exist build\test-classes mkdir build\test-classes
(for /r src\test\java %%f in (*.java) do @echo "%%f") > build\testes.txt
javac --release 17 -encoding UTF-8 -cp build\classes -d build\test-classes @build\testes.txt
if errorlevel 1 exit /b 1
java -Dfile.encoding=UTF-8 -cp "build\classes;build\test-classes" org.Tests.TestesAplicacao
