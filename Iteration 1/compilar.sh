#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
mkdir -p build/classes dist
find src/main/java -name '*.java' -print | sort > build/fontes.txt
javac --release 17 -encoding UTF-8 -d build/classes @build/fontes.txt
jar --create --file dist/assistente-vestuario.jar --main-class org.UI.Main -C build/classes .
echo "Compilação concluída: dist/assistente-vestuario.jar"
