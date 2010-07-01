#! /bin/sh

APP_NAME="TF2SWP"
APP_LONG_NAME="TF2SWP"

# Wrapper
WRAPPER_CMD="./wrapper"
WRAPPER_CONF="tf2swp.conf"

eval "$WRAPPER_CMD -c $WRAPPER_CONF"
