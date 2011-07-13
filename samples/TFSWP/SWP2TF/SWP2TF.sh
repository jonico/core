#! /bin/sh

APP_NAME="SWP2TF"
APP_LONG_NAME="SWP2TF"

# Wrapper
WRAPPER_CMD="./wrapper"
WRAPPER_CONF="swp2tf.conf"

eval "$WRAPPER_CMD -c $WRAPPER_CONF"
