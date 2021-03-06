; Script generated by the HM NIS Edit Script Wizard.

; HM NIS Edit Wizard helper defines
!define TEMP1 $R0 ;Temp variable
!define PRODUCT_NAME "CollabNet Connector Framework"
!define PRODUCT_VERSION "1.0"
!define PRODUCT_PUBLISHER "CollabNet"
!define PRODUCT_WEB_SITE "http://www.open.collab.net"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"
!define MUI_HEADERIMAGE
!include 'LogicLib.nsh'
!include 'NSISpcre.nsh'
!include "Sections.nsh"
!include "nsDialogs.nsh"

Var /GLOBAL INSTALL_DB
Var /GLOBAL IS_SERVICE
BrandingText /TRIMLEFT "Collabnet Connector Framework"

; MUI 1.67 compatible ------
!include "MUI.nsh"

; MUI Settings
!define MUI_ABORTWARNING

; Welcome page
!insertmacro MUI_PAGE_WELCOME
;!insertmacro MUI_PAGE_LICENSE "..\License.txt"
!define MUI_LICENSEPAGE_CHECKBOX
!insertmacro MUI_PAGE_LICENSE "..\License.html"

; Directory page
!insertmacro MUI_PAGE_DIRECTORY

Page custom Configure_CCFdb

; Instfiles page
!insertmacro MUI_PAGE_INSTFILES

!define MUI_FINISHPAGE_RUN
!define MUI_FINISHPAGE_RUN_TEXT "Launch OpenCollabNet"
!define MUI_FINISHPAGE_RUN_FUNCTION "LaunchLink"
!define MUI_FINISHPAGE_TEXT '${PRODUCT_NAME} ${PRODUCT_VERSION} has been installed on your computer. \r\n\r\nClick Finish to close this wizard.'
!insertmacro MUI_PAGE_FINISH

; Uninstaller pages
!insertmacro MUI_UNPAGE_INSTFILES

; Language files
!insertmacro MUI_LANGUAGE "English"

;Regular Expression Macro
!insertmacro REMatches

; MUI end ------

Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile "${PRODUCT_NAME}-${PRODUCT_VERSION}-win32.exe"
InstallDir "$PROGRAMFILES\CollabNet\Connector Framework v1.0"
ShowInstDetails show
ShowUnInstDetails show

Section "Read INI file"
  ;Get Install Options dialog user input
  ReadINIStr ${TEMP1} "$PLUGINSDIR\configure_CCFdb.ini" "Field 1" "State"
SectionEnd

Function "LaunchLink"
  ExecShell "open" "http://open.collab.net/"
FunctionEnd

Section "MainSection" SEC01
  ;;; Get the current working directory
  System::Call "kernel32::GetCurrentDirectory(i ${NSIS_MAX_STRLEN}, t .r0)"
SectionEnd

Icon "$0\..\images\cn_icon.ico"
UninstallIcon "$0\..\images\cn_icon.ico"

Section "MainSection" SEC02

  SetOverwrite try
  SetOutPath "$INSTDIR"
  File "$0\..\..\License.html"
  File "$0\..\..\README.txt"

  ${If} "$INSTALL_DB" = 1
     SetOutPath "$INSTDIR\CCFDBService"
     File "$0\..\ServiceWrappers\CCFDBService\UninstallCCFDBService.bat"
     File "$0\..\ServiceWrappers\CCFDBService\wrapper-community-license.txt"
     File "$0\..\ServiceWrappers\CCFDBService\wrapper.exe"
     File "$0\..\ServiceWrappers\CCFDBService\InstallCCFDBService.bat"

     SetOutPath "$INSTDIR\CCFDBService\config"
     File "$0\..\ServiceWrappers\CCFDBService\config\wrapper.conf"

     SetOutPath "$INSTDIR\CCFDBService\lib"
     File "$0\..\ServiceWrappers\CCFDBService\lib\hsqldb.jar"
     File "$0\..\ServiceWrappers\CCFDBService\lib\wrapper.dll"
     File "$0\..\ServiceWrappers\CCFDBService\lib\wrapper.jar"

     SetOutPath "$INSTDIR\CCFDBService\db"
     File "$0\..\..\config\db-schema\CCFDB.script"
     SetOutPath "$INSTDIR\CCFDBService\logs"
  ${EndIf}

  SetOutPath "$INSTDIR\lib"
  ;File "$0\..\..\build\jars\CCFQCPluginV90.jar"
  File "$0\..\..\build\jars\CCF-JUnit-V10.jar"
  File "$0\..\..\build\jars\CCFSFEEPluginV44.jar"
  File "$0\..\..\build\jars\CCFCoreV10.jar"

  SetOutPath "$INSTDIR\lib\extlib"
  File "$0\..\..\src\core\lib\openadaptor-3.4\bootstrap.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\log4j.properties"
  File "$0\..\..\src\core\lib\openadaptor-3.4\openadaptor-depends.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\openadaptor-spring.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\openadaptor-stub.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\openadaptor.jar"

  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\activation.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\axis_1.4.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\carol.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\cglib-nodep.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\commons-codec_1.3.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\commons-collections_3.2.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\commons-discovery_0.2.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\commons-httpclient_3.0.1.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\commons-jxpath_1.2.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\commons-lang_2.0.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\commons-logging.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\commons-net_1.2.2.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\dom4j_1.6.1.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\dumbster.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\hsqldb.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\jaxen_1.1-beta-9.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\jaxrpc.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\jdom.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\jetty-util_6.0.1.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\jetty_6.0.1.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\jmock-cglib-1.2.0.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\jmock-core-1.2.0.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\jms.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\jmxremote.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\jmxri.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\jmxtools.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\jonas_timer.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\jotm.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\jotm_jrmp_stubs.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\json.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\js_1_6R5.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\jta-spec_1.0.1.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\junit.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\log4j-1.2.15.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\mail.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\mockrunner-jdbc.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\mockrunner-jms.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\mysql-connector-java-5.0.6-bin.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\qname.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\quartz_1.5.2.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\resolver_from_xerces_2.9.0.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\rome_0.9.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\saaj.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\script.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\serializer_from_xerces_2.9.0.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\servlet-api_2.5-6.0.1.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\spring-core_2.0.2.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\spring-mock.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\spring_2.0.2.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\stax-api_1.0.1.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\velocity_1.4.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\wsdl4j_1.6.2.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\wstx-asl_3.0.1.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\xercesImpl_2.9.0.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\xfire-all_1.2.3.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\xml-apis_from_xerces_2.9.0.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\XmlSchema_1.1.jar"
  File "$0\..\..\src\core\lib\openadaptor-3.4\3rdparty\xstream-1.2.1.jar"

  File "$0\..\..\src\plugins\HPQC\lib\jacob\jacob.jar"

  File "$0\..\..\src\plugins\SFEE\v44\lib\sf_soap44_sdk.jar"

  SetOutPath "$INSTDIR\lib\jni"
  File "$0\..\..\src\plugins\HPQC\lib\jacob\jacob.dll"

  SetOutPath "$INSTDIR\samples"
  File "$0\..\..\samples\README.txt"

  SetOutPath "$INSTDIR\samples\QC-SFEE\2Way"
  File "$0\..\..\samples\QC-SFEE\2Way\README.txt"

  SetOutPath "$INSTDIR\samples\QC-SFEE\2Way\config"
  File "$0\..\..\samples\QC-SFEE\2Way\config\TwoWayWiring.xml"

  SetOutPath "$INSTDIR\samples\QC-SFEE\2Way\xslt"
  File "$0\..\..\samples\QC-SFEE\2Way\xslt\qc2sfee.xsl"
  File "$0\..\..\samples\QC-SFEE\2Way\xslt\sfee2qc.xsl"

  SetOutPath "$INSTDIR\samples\QC-SFEE\2Way\db"
  File "$0\..\..\samples\QC-SFEE\2Way\db\QC2SFEE-2Way.script"

  SetOutPath "$INSTDIR\samples\QC-SFEE\2Way\ServiceWrapper"
  File "$0\..\..\samples\QC-SFEE\2Way\ServiceWrapper\InstallCCFService.bat"
  File "$0\..\..\samples\QC-SFEE\2Way\ServiceWrapper\log4j.properties"
  File "$0\..\..\samples\QC-SFEE\2Way\ServiceWrapper\UninstallCCFService.bat"
  File "$0\..\..\samples\QC-SFEE\2Way\ServiceWrapper\wrapper-community-license.txt"
  File "$0\..\..\samples\QC-SFEE\2Way\ServiceWrapper\wrapper.exe"

  SetOutPath "$INSTDIR\samples\QC-SFEE\2Way\ServiceWrapper\config"
  File "$0\..\..\samples\QC-SFEE\2Way\ServiceWrapper\config\wrapper.conf"

  SetOutPath "$INSTDIR\samples\QC-SFEE\2Way\ServiceWrapper\lib"
  File "$0\..\..\samples\QC-SFEE\2Way\ServiceWrapper\lib\hsqldb.jar"
  File "$0\..\..\samples\QC-SFEE\2Way\ServiceWrapper\lib\wrapper.dll"
  File "$0\..\..\samples\QC-SFEE\2Way\ServiceWrapper\lib\wrapper.jar"

  SetOutPath "$INSTDIR\samples\QC-SFEE\QC2SFEE"
  File "$0\..\..\samples\QC-SFEE\QC2SFEE\README.txt"

  SetOutPath "$INSTDIR\samples\QC-SFEE\QC2SFEE\db"
  File "$0\..\..\samples\QC-SFEE\QC2SFEE\db\QC2SFEE.script"

  SetOutPath "$INSTDIR\samples\QC-SFEE\QC2SFEE\xslt"
  File "$0\..\..\samples\QC-SFEE\QC2SFEE\xslt\qc2sfee.xsl"

  SetOutPath "$INSTDIR\samples\QC-SFEE\QC2SFEE\config"
  File "$0\..\..\samples\QC-SFEE\QC2SFEE\config\QC2SFEE.xml"

  SetOutPath "$INSTDIR\samples\QC-SFEE\SFEE2QC"
  File "$0\..\..\samples\QC-SFEE\SFEE2QC\README.txt"

  SetOutPath "$INSTDIR\samples\QC-SFEE\SFEE2QC\config"
  File "$0\..\..\samples\QC-SFEE\SFEE2QC\config\SFEE2QC.xml"

  SetOutPath "$INSTDIR\samples\QC-SFEE\SFEE2QC\db"
  File "$0\..\..\samples\QC-SFEE\SFEE2QC\db\SFEE2QC.script"

  SetOutPath "$INSTDIR\samples\QC-SFEE\SFEE2QC\xslt"
  File "$0\..\..\samples\QC-SFEE\SFEE2QC\xslt\sfee2qc.xsl"

  SetOutPath "$INSTDIR\samples\QC2QC"
  File "$0\..\..\samples\QC2QC\README.txt"

  SetOutPath "$INSTDIR\samples\QC2QC\config"
  File "$0\..\..\samples\QC2QC\config\QC2QC-1Way.xml"

  SetOutPath "$INSTDIR\samples\QC2QC\db"
  File "$0\..\..\samples\QC2QC\db\QC2QC-1Way.script"
  
  SetOutPath "$INSTDIR\samples\SFEE2SFEE"
  File "$0\..\..\samples\SFEE2SFEE\README.txt"

  SetOutPath "$INSTDIR\samples\SFEE2SFEE\bidirectional-mirroring\config"
  File "$0\..\..\samples\SFEE2SFEE\bidirectional-mirroring\config\ccf.properties"
  File "$0\..\..\samples\SFEE2SFEE\bidirectional-mirroring\config\sfee.properties"
  File "$0\..\..\samples\SFEE2SFEE\bidirectional-mirroring\config\SFEE2SFEE.xml"
  File "$0\..\..\samples\SFEE2SFEE\bidirectional-mirroring\config\wrapper.conf"

  SetOutPath "$INSTDIR\samples\SFEE2SFEE\bidirectional-mirroring"
  File "$0\..\..\samples\SFEE2SFEE\bidirectional-mirroring\RunCCFService.bat"

  SetOutPath "$INSTDIR\samples\SFEE2SFEE\bidirectional-mirroring\db"
  File "$0\..\..\samples\SFEE2SFEE\bidirectional-mirroring\db\SFEE2SFEE.script"

  SetOutPath "$INSTDIR\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation"
  File "$0\..\..\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\RunCCFService.bat"

  SetOutPath "$INSTDIR\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\config"
  File "$0\..\..\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\config\ccf.properties"
  File "$0\..\..\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\config\sfee.properties"
  File "$0\..\..\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\config\SFEE2SFEE.xml"
  File "$0\..\..\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\config\wrapper.conf"

  SetOutPath "$INSTDIR\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\db"
  File "$0\..\..\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\db\SFEE2SFEE.script"

  SetOutPath "$INSTDIR\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\xslt"
  File "$0\..\..\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\xslt\cu074+tracker1013+cu074+tracker1014.xsl"
  File "$0\..\..\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\xslt\cu074+tracker1014+cu074+tracker1013.xsl"
  File "$0\..\..\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\xslt\cu074+tracker1016+cu074+tracker1017.xsl"

  SetOutPath "$INSTDIR\samples\SFEE2SFEE\unidirectional-transformation"
  File "$0\..\..\samples\SFEE2SFEE\unidirectional-transformation\RunCCFService.bat"

  SetOutPath "$INSTDIR\samples\SFEE2SFEE\unidirectional-transformation\config"
  File "$0\..\..\samples\SFEE2SFEE\unidirectional-transformation\config\ccf.properties"
  File "$0\..\..\samples\SFEE2SFEE\unidirectional-transformation\config\sfee.properties"
  File "$0\..\..\samples\SFEE2SFEE\unidirectional-transformation\config\UnidirectionalTransformationWiring.xml"
  File "$0\..\..\samples\SFEE2SFEE\unidirectional-transformation\config\wrapper.conf"

  SetOutPath "$INSTDIR\samples\SFEE2SFEE\unidirectional-transformation\db"
  File "$0\..\..\samples\SFEE2SFEE\unidirectional-transformation\db\SFEE2SFEE_Unidirectional.script"

  SetOutPath "$INSTDIR\samples\SFEE2SFEE\unidirectional-transformation\xslt"
  File "$0\..\..\samples\SFEE2SFEE\unidirectional-transformation\xslt\cu074+tracker1016+cu074+tracker1017.xsl"
  
  CreateDirectory "$SMPROGRAMS\Collabnet Connector Framework"
  CreateShortCut "$SMPROGRAMS\Collabnet Connector Framework\Uninstall.lnk" "$INSTDIR\uninst.exe"
SectionEnd

Section -Post
  StrCpy $0 "%d/%m/%Y"
  nsisdt::currentdate
  WriteUninstaller "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayName" "$(^Name)"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${PRODUCT_VERSION}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayIcon" "$INSTDIR\uninst.exe"

  ${If} "$INSTALL_DB" = 1
       ;Install the service here
       nsExec::Exec '"$INSTDIR\CCFDBService\InstallCCFDBService.bat"'
  ${EndIf}

  ${If} $IS_SERVICE = 1
      ;nsExec::Exec 'sc.exe create CCFDBService binpath= "\"$INSTDIR\CCFDBService\InstallCCFDBService.bat\"" displayname= "CCFDBService" start= auto'
      ;Start the service here
      nsExec::Exec '"$INSTDIR\CCFDBService\wrapper.exe" -t "$INSTDIR\CCFDBService\config\wrapper.conf"'
  ${EndIf}
  Exec "notepad $INSTDIR\README.txt"
SectionEnd

Function un.onUninstSuccess
  HideWindow
  MessageBox MB_ICONINFORMATION|MB_OK "$(^Name) was successfully removed from your computer."
FunctionEnd

Function un.onInit
  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 "Are you sure you want to completely remove $(^Name) and all of its components?" IDYES +2
  Abort
FunctionEnd

Section Uninstall

  Delete "$SMPROGRAMS\Collabnet Connector Framework\Uninstall.lnk"
  RMDir "$SMPROGRAMS\Collabnet Connector Framework"

  ;Stop the service
  nsExec::Exec '\"$INSTDIR\CCFDBService\wrapper.exe\" -p \"$INSTDIR\CCFDBService\config\wrapper.conf\" ">>" \"$INSTDIR\CCFDBService\logs\wrapper.log\"'
  ;Uninstall the service
  nsExec::Exec '"$INSTDIR\CCFDBService\UninstallCCFDBService.bat"'

  Delete "$INSTDIR\${PRODUCT_NAME}.url"
  Delete "$INSTDIR\uninst.exe"

  Delete "$INSTDIR\CCFDBService\UninstallCCFDBService.bat"
  Delete "$INSTDIR\CCFDBService\wrapper-community-license.txt"
  Delete "$INSTDIR\CCFDBService\wrapper.exe"
  Delete "$INSTDIR\CCFDBService\InstallCCFDBService.bat"

  Delete "$INSTDIR\CCFDBService\config\wrapper.conf"

  Delete "$INSTDIR\CCFDBService\lib\hsqldb.jar"
  Delete "$INSTDIR\CCFDBService\lib\wrapper.dll"
  Delete "$INSTDIR\CCFDBService\lib\wrapper.jar"

  Delete "$INSTDIR\CCFDBService\db\CCFDB.script"

  ;Delete "$INSTDIR\lib\CCFQCPluginV90.jar"
  Delete "$INSTDIR\lib\CCF-JUnit-V10.jar"
  Delete "$INSTDIR\lib\CCFSFEEPluginV44.jar"
  Delete "$INSTDIR\lib\CCFCoreV10.jar"

  Delete "$INSTDIR\lib\extlib\bootstrap.jar"
  Delete "$INSTDIR\lib\extlib\log4j.properties"
  Delete "$INSTDIR\lib\extlib\openadaptor-depends.jar"
  Delete "$INSTDIR\lib\extlib\openadaptor-spring.jar"
  Delete "$INSTDIR\lib\extlib\openadaptor-stub.jar"
  Delete "$INSTDIR\lib\extlib\openadaptor.jar"

  Delete "$INSTDIR\lib\extlib\activation.jar"
  Delete "$INSTDIR\lib\extlib\axis_1.4.jar"
  Delete "$INSTDIR\lib\extlib\carol.jar"
  Delete "$INSTDIR\lib\extlib\cglib-nodep.jar"
  Delete "$INSTDIR\lib\extlib\commons-codec_1.3.jar"
  Delete "$INSTDIR\lib\extlib\commons-collections_3.2.jar"
  Delete "$INSTDIR\lib\extlib\commons-discovery_0.2.jar"
  Delete "$INSTDIR\lib\extlib\commons-httpclient_3.0.1.jar"
  Delete "$INSTDIR\lib\extlib\commons-jxpath_1.2.jar"
  Delete "$INSTDIR\lib\extlib\commons-lang_2.0.jar"
  Delete "$INSTDIR\lib\extlib\commons-logging.jar"
  Delete "$INSTDIR\lib\extlib\commons-net_1.2.2.jar"
  Delete "$INSTDIR\lib\extlib\dom4j_1.6.1.jar"
  Delete "$INSTDIR\lib\extlib\dumbster.jar"
  Delete "$INSTDIR\lib\extlib\hsqldb.jar"
  Delete "$INSTDIR\lib\extlib\jaxen_1.1-beta-9.jar"
  Delete "$INSTDIR\lib\extlib\jaxrpc.jar"
  Delete "$INSTDIR\lib\extlib\jdom.jar"
  Delete "$INSTDIR\lib\extlib\jetty-util_6.0.1.jar"
  Delete "$INSTDIR\lib\extlib\jetty_6.0.1.jar"
  Delete "$INSTDIR\lib\extlib\jmock-cglib-1.2.0.jar"
  Delete "$INSTDIR\lib\extlib\jmock-core-1.2.0.jar"
  Delete "$INSTDIR\lib\extlib\jms.jar"
  Delete "$INSTDIR\lib\extlib\jmxremote.jar"
  Delete "$INSTDIR\lib\extlib\jmxri.jar"
  Delete "$INSTDIR\lib\extlib\jmxtools.jar"
  Delete "$INSTDIR\lib\extlib\jonas_timer.jar"
  Delete "$INSTDIR\lib\extlib\jotm.jar"
  Delete "$INSTDIR\lib\extlib\jotm_jrmp_stubs.jar"
  Delete "$INSTDIR\lib\extlib\json.jar"
  Delete "$INSTDIR\lib\extlib\js_1_6R5.jar"
  Delete "$INSTDIR\lib\extlib\jta-spec_1.0.1.jar"
  Delete "$INSTDIR\lib\extlib\junit.jar"
  Delete "$INSTDIR\lib\extlib\log4j-1.2.15.jar"
  Delete "$INSTDIR\lib\extlib\mail.jar"
  Delete "$INSTDIR\lib\extlib\mockrunner-jdbc.jar"
  Delete "$INSTDIR\lib\extlib\mockrunner-jms.jar"
  Delete "$INSTDIR\lib\extlib\mysql-connector-java-5.0.6-bin.jar"
  Delete "$INSTDIR\lib\extlib\qname.jar"
  Delete "$INSTDIR\lib\extlib\quartz_1.5.2.jar"
  Delete "$INSTDIR\lib\extlib\resolver_from_xerces_2.9.0.jar"
  Delete "$INSTDIR\lib\extlib\rome_0.9.jar"
  Delete "$INSTDIR\lib\extlib\saaj.jar"
  Delete "$INSTDIR\lib\extlib\script.jar"
  Delete "$INSTDIR\lib\extlib\serializer_from_xerces_2.9.0.jar"
  Delete "$INSTDIR\lib\extlib\servlet-api_2.5-6.0.1.jar"
  Delete "$INSTDIR\lib\extlib\spring-core_2.0.2.jar"
  Delete "$INSTDIR\lib\extlib\spring-mock.jar"
  Delete "$INSTDIR\lib\extlib\spring_2.0.2.jar"
  Delete "$INSTDIR\lib\extlib\stax-api_1.0.1.jar"
  Delete "$INSTDIR\lib\extlib\velocity_1.4.jar"
  Delete "$INSTDIR\lib\extlib\wsdl4j_1.6.2.jar"
  Delete "$INSTDIR\lib\extlib\wstx-asl_3.0.1.jar"
  Delete "$INSTDIR\lib\extlib\xercesImpl_2.9.0.jar"
  Delete "$INSTDIR\lib\extlib\xfire-all_1.2.3.jar"
  Delete "$INSTDIR\lib\extlib\xml-apis_from_xerces_2.9.0.jar"
  Delete "$INSTDIR\lib\extlib\XmlSchema_1.1.jar"
  Delete "$INSTDIR\lib\extlib\xstream-1.2.1.jar"

  Delete "$INSTDIR\lib\extlib\jacob.jar"

  Delete "$INSTDIR\lib\extlib\sf_soap44_sdk.jar"

  Delete "$INSTDIR\lib\jni\jacob.dll"

  Delete "$INSTDIR\samples\README.txt"

  Delete "$INSTDIR\samples\QC-SFEE\2Way\README.txt"

  Delete "$INSTDIR\samples\QC-SFEE\2Way\config\TwoWayWiring.xml"

  Delete "$INSTDIR\samples\QC-SFEE\2Way\xslt\qc2sfee.xsl"
  Delete "$INSTDIR\samples\QC-SFEE\2Way\xslt\sfee2qc.xsl"

  Delete "$INSTDIR\samples\QC-SFEE\2Way\db\QC2SFEE-2Way.script"

  Delete "$INSTDIR\samples\QC-SFEE\2Way\ServiceWrapper\InstallCCFService.bat"
  Delete "$INSTDIR\samples\QC-SFEE\2Way\ServiceWrapper\log4j.properties"
  Delete "$INSTDIR\samples\QC-SFEE\2Way\ServiceWrapper\UninstallCCFService.bat"
  Delete "$INSTDIR\samples\QC-SFEE\2Way\ServiceWrapper\wrapper-community-license.txt"
  Delete "$INSTDIR\samples\QC-SFEE\2Way\ServiceWrapper\wrapper.exe"

  Delete "$INSTDIR\samples\QC-SFEE\2Way\ServiceWrapper\config\wrapper.conf"

  Delete "$INSTDIR\samples\QC-SFEE\2Way\ServiceWrapper\lib\hsqldb.jar"
  Delete "$INSTDIR\samples\QC-SFEE\2Way\ServiceWrapper\lib\wrapper.dll"
  Delete "$INSTDIR\samples\QC-SFEE\2Way\ServiceWrapper\lib\wrapper.jar"

  Delete "$INSTDIR\samples\QC-SFEE\QC2SFEE\README.txt"

  Delete "$INSTDIR\samples\QC-SFEE\QC2SFEE\config\QC2SFEE.xml"

  Delete "$INSTDIR\samples\QC-SFEE\QC2SFEE\db\QC2SFEE.script"

  Delete "$INSTDIR\samples\QC-SFEE\QC2SFEE\xslt\qc2sfee.xsl"

  Delete "$INSTDIR\samples\QC-SFEE\SFEE2QC\README.txt"

  Delete "$INSTDIR\samples\QC-SFEE\SFEE2QC\config\SFEE2QC.xml"

  Delete "$INSTDIR\samples\QC-SFEE\SFEE2QC\db\SFEE2QC.script"

  Delete "$INSTDIR\samples\QC-SFEE\SFEE2QC\xslt\sfee2qc.xsl"

  Delete "$INSTDIR\samples\QC2QC\README.txt"

  Delete "$INSTDIR\samples\QC2QC\config\QC2QC-1Way.xml"

  Delete "$INSTDIR\samples\QC2QC\db\QC2QC-1Way.script"

  Delete "$INSTDIR\samples\SFEE2SFEE\unidirectional-transformation\xslt\cu074+tracker1016+cu074+tracker1017.xsl"
  Delete "$INSTDIR\samples\SFEE2SFEE\unidirectional-transformation\RunCCFService.bat"
  Delete "$INSTDIR\samples\SFEE2SFEE\unidirectional-transformation\db\SFEE2SFEE_Unidirectional.script"
  Delete "$INSTDIR\samples\SFEE2SFEE\unidirectional-transformation\config\wrapper.conf"
  Delete "$INSTDIR\samples\SFEE2SFEE\unidirectional-transformation\config\UnidirectionalTransformationWiring.xml"
  Delete "$INSTDIR\samples\SFEE2SFEE\unidirectional-transformation\config\sfee.properties"
  Delete "$INSTDIR\samples\SFEE2SFEE\unidirectional-transformation\config\ccf.properties"
  Delete "$INSTDIR\samples\SFEE2SFEE\README.txt"
  Delete "$INSTDIR\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\xslt\cu074+tracker1016+cu074+tracker1017.xsl"
  Delete "$INSTDIR\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\xslt\cu074+tracker1014+cu074+tracker1013.xsl"
  Delete "$INSTDIR\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\xslt\cu074+tracker1013+cu074+tracker1014.xsl"
  Delete "$INSTDIR\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\RunCCFService.bat"
  Delete "$INSTDIR\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\db\SFEE2SFEE.script"
  Delete "$INSTDIR\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\config\wrapper.conf"
  Delete "$INSTDIR\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\config\SFEE2SFEE.xml"
  Delete "$INSTDIR\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\config\sfee.properties"
  Delete "$INSTDIR\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\config\ccf.properties"
  Delete "$INSTDIR\samples\SFEE2SFEE\bidirectional-mirroring\RunCCFService.bat"
  Delete "$INSTDIR\samples\SFEE2SFEE\bidirectional-mirroring\db\SFEE2SFEE.script"
  Delete "$INSTDIR\samples\SFEE2SFEE\bidirectional-mirroring\config\wrapper.conf"
  Delete "$INSTDIR\samples\SFEE2SFEE\bidirectional-mirroring\config\SFEE2SFEE.xml"
  Delete "$INSTDIR\samples\SFEE2SFEE\bidirectional-mirroring\config\sfee.properties"
  Delete "$INSTDIR\samples\SFEE2SFEE\bidirectional-mirroring\config\ccf.properties"

  Delete "$INSTDIR\License.html"
  Delete "$INSTDIR\README.txt"

  RMDir "$INSTDIR\samples\SFEE2SFEE\unidirectional-transformation\xslt"
  RMDir "$INSTDIR\samples\SFEE2SFEE\unidirectional-transformation\db"
  RMDir "$INSTDIR\samples\SFEE2SFEE\unidirectional-transformation\config"
  RMDir "$INSTDIR\samples\SFEE2SFEE\unidirectional-transformation"
  RMDir "$INSTDIR\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\xslt"
  RMDir "$INSTDIR\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\db"
  RMDir "$INSTDIR\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation\config"
  RMDir "$INSTDIR\samples\SFEE2SFEE\combined-bidirectional-mirroring-with-unidirectional-transformation"
  RMDir "$INSTDIR\samples\SFEE2SFEE\bidirectional-mirroring\db"
  RMDir "$INSTDIR\samples\SFEE2SFEE\bidirectional-mirroring\config"
  RMDir "$INSTDIR\samples\SFEE2SFEE\bidirectional-mirroring"

  RMDir "$INSTDIR\CCFDBService\config"
  RMDir "$INSTDIR\CCFDBService\lib"
  RMDir "$INSTDIR\CCFDBService\db"
  RMDir "$INSTDIR\CCFDBService\logs"
  RMDir "$INSTDIR\CCFDBService"

  RMDir "$INSTDIR\lib\extlib\jni"
  RMDir "$INSTDIR\lib\extlib"
  RMDir "$INSTDIR\lib\jni"
  RMDir "$INSTDIR\lib"

  RMDir "$INSTDIR\samples\QC-SFEE\2Way\ServiceWrapper\config"
  RMDir "$INSTDIR\samples\QC-SFEE\2Way\ServiceWrapper\lib"
  RMDir "$INSTDIR\samples\QC-SFEE\2Way\ServiceWrapper"
  RMDir "$INSTDIR\samples\QC-SFEE\2Way\config"
  RMDir "$INSTDIR\samples\QC-SFEE\2Way\xslt"
  RMDir "$INSTDIR\samples\QC-SFEE\2Way\db"
  RMDir "$INSTDIR\samples\QC-SFEE\2Way"

  RMDir "$INSTDIR\samples\QC-SFEE\QC2SFEE\db"
  RMDir "$INSTDIR\samples\QC-SFEE\QC2SFEE\xslt"
  RMDir "$INSTDIR\samples\QC-SFEE\QC2SFEE\config"
  RMDir "$INSTDIR\samples\QC-SFEE\QC2SFEE"

  RMDir "$INSTDIR\samples\QC-SFEE\SFEE2QC\config"
  RMDir "$INSTDIR\samples\QC-SFEE\SFEE2QC\db"
  RMDir "$INSTDIR\samples\QC-SFEE\SFEE2QC\xslt"
  RMDir "$INSTDIR\samples\QC-SFEE\SFEE2QC"
  RMDir "$INSTDIR\samples\QC-SFEE"

  RMDir "$INSTDIR\samples\QC2QC\config"
  RMDir "$INSTDIR\samples\QC2QC\db"
  RMDir "$INSTDIR\samples\QC2QC"

  RMDir "$INSTDIR\samples\SFEE2SFEE"

  RMDir "$INSTDIR\samples"

  RMDir "$INSTDIR"

  DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}"
  SetAutoClose true
SectionEnd

XPStyle on
Function .onInit
    ;;;;;;;;; show splash screen ;;;;;;;;;;;;;;;;;;;;;
    # the plugins dir is automatically deleted when the installer exits
    InitPluginsDir
    File /oname=$PLUGINSDIR\splash.bmp "$0\..\images\cn_splash.bmp"
    advsplash::show 1000 600 400 -1 $PLUGINSDIR\splash
    Pop $0          ; $0 has '1' if the user closed the splash screen early,
                    ; '0' if everything closed normally, and '-1' if some error occurred.
    Delete $PLUGINSDIR\cn_splash.bmp
    ;;;;; README page
    File /oname=$PLUGINSDIR\configure_CCFdb.ini "configure_CCFdb.ini"
FunctionEnd

Function validate
    ${if} $1 == 0 #DB will not be installed
       StrCpy $INSTALL_DB "0"
       StrCpy $IS_SERVICE "0"
       ${if} $3 == 1
          MessageBox MB_OK "Sorry, Unable to configure HSQL DB as a service without installing it"
          Call Configure_CCFdb
       ${Endif}
    ${Else}
       StrCpy $INSTALL_DB "1"
       ${if} $3 == 1
          StrCpy $IS_SERVICE "1"
       ${Else}
          StrCpy $IS_SERVICE "0"
       ${EndIf}
    ${EndIf}

FunctionEnd

Function Configure_CCFdb
    ;;;Display Readme page
    !insertmacro MUI_HEADER_TEXT "CollabNet Connector Framework" "Configuration panel of CollabNet Connector Framework Database"
    Push ${TEMP1}
    InstallOptions::dialog "$PLUGINSDIR\configure_CCFdb.ini"
    Pop ${TEMP1}
    StrCmp ${TEMP1} "success" 0 continue
    ReadINIStr $1 "$PLUGINSDIR\configure_CCFdb.ini" "Field 3" "State"
    ReadINIStr $2 "$PLUGINSDIR\configure_CCFdb.ini" "Field 4" "State"
    ReadINIStr $3 "$PLUGINSDIR\configure_CCFdb.ini" "Field 5" "State"
    continue: Pop ${TEMP1}
    call validate

    ;GetDlgItem $1 $HWNDPARENT 2008
    ;GetFunctionAddress $0 OnClick
    ;nsDialogs::OnClick /NOUNLOAD $1 $0
FunctionEnd

