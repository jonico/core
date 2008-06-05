rem This is the startup script for the CCF Database
set CLASSPATH=..\lib\extlib\hsqldb.jar
java org.hsqldb.Server -database.0 db/CCFDB -dbname.0 xdb > logs\db.log