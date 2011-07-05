#! /bin/sh

APP_NAME="TF2SWP2"
APP_LONG_NAME="CCF 2.x TF2SWP"

# Wrapper
WRAPPER_CMD="./wrapper"
WRAPPER_CONF="tf2swp2.conf"

eval "$WRAPPER_CMD -c $WRAPPER_CONF"
