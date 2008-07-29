#!/bin/sh
# This is a common script file that sets the CLASSPATH and
# environment variables for the CollabNet Connector framework
# startup scripts.

# Please refer http://ccf.collab.net/

# Before running anything set your JAVA_HOME
# and the CCF_HOME env variables.
# Point the JAVA_HOME to the JDK directory on your machine.
# You will need JDK 1.5 and above.
# Point the CCF_HOME to the directory where you have extracted the CCF
JAVA_HOME=
CCF_HOME=

if [ -z $JAVA_HOME ]
then
        echo "JAVA_HOME is not set. Point JAVA_HOME to the JDK directory in your machine"
        exit 1
elif [ -z $CCF_HOME ]
then
        echo "CCF_HOME is not set. Please point the CCF_HOME to the CCF installation directory"
        exit 2
fi

export JAVA_HOME
export CCF_HOME

export LIB_PATH=$CCF_HOME/lib
export EXT_LIB_PATH=$CCF_HOME/lib/extlib
