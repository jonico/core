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
rem bin directory file.

set JAVA_HOME=
set CCF_HOME=

set CCF_HOME=%~dp0..

set LIB_PATH=%CCF_HOME%\lib
set EXT_LIB_PATH=%CCF_HOME%\lib\extlib

