#!/bin/sh

APP_NAME="SWP2TF2"
APP_LONG_NAME="CCF 2.0 SWP2TF"

# Wrapper
WRAPPER_CMD="./wrapper"
WRAPPER_CONF="swp2tf2.conf"

BITS="$(getconf LONG_BIT)"
test -x "${WRAPPER_CMD}-linux-x86-${BITS}" && WRAPPER_CMD="${WRAPPER_CMD}-linux-x86-${BITS}"

eval "\"$WRAPPER_CMD\" -c \"$WRAPPER_CONF\"" 
