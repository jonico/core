call ..\..\..\bin\setEnv.bat

%JAVA_HOME%\bin\java.exe -Xms512m -Xmx1024m -cp config;%LIB_PATH% -Djava.library.path=%EXT_LIB_PATH%  -Djava.ext.dirs=%LIB_PATH%;%EXT_LIB_PATH% com.collabnet.ccf.core.recovery.HospitalArtifactReplayer -config config\config.xml -hospital logs/hospital.txt