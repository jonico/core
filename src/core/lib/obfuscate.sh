#!/bin/sh
DIR="$(dirname $(readlink -f $0))"
java -cp "${DIR}/CCFCoreV10.jar:${DIR}/extlib/commons-codec_1.3.jar" \
    com.collabnet.ccf.core.utils.Obfuscator "$@"
