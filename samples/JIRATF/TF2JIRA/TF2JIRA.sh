#! /bin/sh

APP_NAME="TF2JIRA"
APP_LONG_NAME="TF2JIRA"

# Wrapper
WRAPPER_CMD="./wrapper"
WRAPPER_CONF="sfee2jira.conf"

eval "$WRAPPER_CMD -c $WRAPPER_CONF"
