#! /bin/sh

APP_NAME="CCF Central Database"
APP_LONG_NAME="CCF Central Database"

# Wrapper
WRAPPER_CMD="./wrapper"
WRAPPER_CONF="centralccfdatabase.conf"

eval "$WRAPPER_CMD -c $WRAPPER_CONF"
