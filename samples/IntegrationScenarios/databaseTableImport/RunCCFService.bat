call ..\..\..\bin\setEnv.bat

%JAVA_HOME%\bin\java.exe -Xms512m -Xmx1024m -cp config;%LIB_PATH% -Djava.ext.dirs=%LIB_PATH%;%EXT_LIB_PATH% org.openadaptor.spring.SpringAdaptor -config config\databaseTableImport.xml