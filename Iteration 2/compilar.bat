@echo off
setlocal
cd /d "%~dp0"
if not exist build\classes mkdir build\classes
if not exist dist mkdir dist
(for /r src\main\java %%f in (*.java) do @echo "%%f") > build\fontes.txt
javac --release 17 -encoding UTF-8 -d build\classes @build\fontes.txt
if errorlevel 1 exit /b 1
jar --create --file dist\assistente-vestuario.jar --main-class org.UI.Main -C build\classes .
if errorlevel 1 exit /b 1
echo Compilacao concluida: dist\assistente-vestuario.jar
