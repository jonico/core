call ..\..\..\bin\setEnv.bat

%JAVA_HOME%\bin\java.exe -cp config;%LIB_PATH% -Djava.ext.dirs=%LIB_PATH%;%EXT_LIB_PATH% -Dlog4j.configuration=%LIB_PATH%\log4j.properties org.openadaptor.spring.SpringAdaptor -config config\SFEE2SFEE.xml