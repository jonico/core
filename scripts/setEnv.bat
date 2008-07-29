@echo off
rem This is a common script file that sets the CLASSPATH and
rem environment variables for the CollabNet Connector framework
rem startup scripts.

rem Please refer http://ccf.collab.net/

rem Before running anything set your JAVA_HOME
rem and the CCF_HOME env variables.
rem Point the JAVA_HOME to the JDK directory on your machine.
rem You will need JDK 1.5 and above.
rem Point the CCF_HOME to the directory where you have extracted the CCF

rem If you haven't set the CCF_HOME directory it will be taken as the parent directory of the
rem bin directory.

set JAVA_HOME=C:\mount\Java\jdk1.6.0_05
set CCF_HOME=

if (%JAVA_HOME%)==() goto NO_JAVA_HOME
if not exist %JAVA_HOME%\bin\java.exe goto NO_PROPER_JAVA_HOME
if (%CCF_HOME%)==() set CCF_HOME=%~dp0..
if not exist %CCF_HOME% goto NO_PROPER_CCF_HOME

set LIB_PATH=%CCF_HOME%\lib
set EXT_LIB_PATH=%CCF_HOME%\lib\extlib

goto :EOF

:NO_JAVA_HOME
echo JAVA_HOME is not set. Please point JAVA_HOME to the JDK directory in your machine
goto :EOF

:NO_PROPER_JAVA_HOME
echo JAVA_HOME is not properly set. Please point JAVA_HOME to the JDK directory in your machine
goto :EOF

:NO_PROPER_CCF_HOME
echo CCF_HOME is not properly set. Please point the CCF_HOME to the CCF installation directory
goto :EOF