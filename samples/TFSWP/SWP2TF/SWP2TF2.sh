#! /bin/sh

APP_NAME="SWP2TF2"
APP_LONG_NAME="CCF 2.0 SWP2TF"

# Wrapper
WRAPPER_CMD="./wrapper"
WRAPPER_CONF="swp2tf2.conf"

eval "$WRAPPER_CMD -c $WRAPPER_CONF"
