@echo off

:nt
rem
rem Find the application home.
rem
rem %~dp0 is location of current script under NT
set _REALPATH=%~dp0

cd %_REALPATH%

echo f | xcopy /f /y "%RQP_LOCATION%\RequisitePro\lib\proxies.jar" "..\..\..\src\plugins\RQP\lib\proxies.jar"
echo f | xcopy /f /y "%RQP_LOCATION%\common\RJCB.jar" "..\..\..\src\plugins\RQP\lib\RJCB.jar"