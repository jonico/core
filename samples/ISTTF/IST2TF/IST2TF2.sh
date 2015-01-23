#!/bin/sh

APP_NAME="TFS2TF2"
APP_LONG_NAME="CCF 2.2 TFS2TF"

# Wrapper
WRAPPER_CMD="./wrapper"
WRAPPER_CONF="tfs2tf2.conf"

BITS="$(getconf LONG_BIT)"
test -x "${WRAPPER_CMD}-linux-x86-${BITS}" && WRAPPER_CMD="${WRAPPER_CMD}-linux-x86-${BITS}"

eval "\"$WRAPPER_CMD\" -c \"$WRAPPER_CONF\" wrapper.daemonize=TRUE >/dev/null 2> /dev/null" 
