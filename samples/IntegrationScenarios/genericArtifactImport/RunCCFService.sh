#!/bin/sh

. ../../../bin/setEnv.sh

$JAVA_HOME/bin/java -Xms512m -Xmx1024m -cp config:$LIB_PATH -Djava.ext.dirs=$LIB_PATH:$EXT_LIB_PATH org.openadaptor.spring.SpringAdaptor -config config/config.xml