#!/bin/sh
#This is the startup script for the CCF Database

. ../../bin/setEnv.sh

CLASSPATH=$LIB_PATH/extlib/hsqldb.jar
export CLASSPATH
$JAVA_HOME/bin/java org.hsqldb.Server -database.0 ccfDatabase -dbname.0 xdb > db.log
