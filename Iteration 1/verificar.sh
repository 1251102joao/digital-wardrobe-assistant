#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
sh compilar.sh
mkdir -p build/test-classes
find src/test/java -name '*.java' -print | sort > build/testes.txt
javac --release 17 -encoding UTF-8 -cp build/classes -d build/test-classes @build/testes.txt
java -Dfile.encoding=UTF-8 -cp build/classes:build/test-classes org.Tests.TestesAplicacao
