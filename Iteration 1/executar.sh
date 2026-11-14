#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
if [ ! -f dist/assistente-vestuario.jar ]; then sh compilar.sh; fi
java -Dfile.encoding=UTF-8 -jar dist/assistente-vestuario.jar "$@"
