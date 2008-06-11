call ..\..\..\bin\setEnv.bat

%JAVA_HOME%\bin\java.exe -cp config;%LIB_PATH% -Djava.ext.dirs=%LIB_PATH%;%EXT_LIB_PATH% org.openadaptor.spring.SpringAdaptor -config config\FLATFILE2SFEE.xml