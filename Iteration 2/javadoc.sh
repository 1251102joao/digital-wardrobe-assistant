#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
mkdir -p build
find src/main/java -name '*.java' -print | sort > build/fontes.txt
javadoc -quiet -encoding UTF-8 -docencoding UTF-8 -charset UTF-8 -d build/javadoc @build/fontes.txt
echo "Documentação: build/javadoc/index.html"
