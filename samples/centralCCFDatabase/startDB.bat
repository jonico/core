rem This is the startup script for the CCF Database
set CLASSPATH=..\..\lib\extlib\hsqldb.jar
call ..\..\bin\setEnv.bat

%JAVA_HOME%\bin\java org.hsqldb.Server -database.0 ccfDatabase -dbname.0 xdb > logs\db.log