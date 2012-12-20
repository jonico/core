#! /bin/sh

APP_NAME="JIRA2TF"
APP_LONG_NAME="JIRA2TF"

# Wrapper
WRAPPER_CMD="./wrapper"
WRAPPER_CONF="jira2sfee.conf"

eval "$WRAPPER_CMD -c $WRAPPER_CONF"
