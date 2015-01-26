
CCF FOR SPIRATEST


  KNOWN ISSUES
  
    SERVICE INSTALLATION
    
      On amd64 virtual machines like ccf.ebaotech.com, the service.bat script choses the wrong tomcat executable tomcat7x86.exe.
      This service fails to start with exit code 0 and these messages in the log file apache/logs/commons-daemon-<date>.log:
		[2015-01-26 16:04:35] [info]  Starting service...
		[2015-01-26 16:04:35] [error] %1 is not a valid Win32 application.
		[2015-01-26 16:04:35] [error] Failed creating java C:\Program Files\Java\jdk1.7.0_71\jre\bin\server\jvm.dll
		[2015-01-26 16:04:35] [error] %1 is not a valid Win32 application.
		[2015-01-26 16:04:35] [error] ServiceStart returned 1
		[2015-01-26 16:04:35] [error] %1 is not a valid Win32 application.
		[2015-01-26 16:04:35] [info]  Run service finished.
		[2015-01-26 16:04:35] [info]  Commons Daemon procrun finished
       
      Solution:
      1 - backup the file apache/bin/tomcat7x86.exe
      2 - copy the file apache/bin/tomcat7amd64.exe to apache/bin/tomcat7x86.exe
      3 - the service should now start.
      
      NOTE: this will have to be patched again after a tomcat update!
      
      Additional Information:
        == tomcat version info
		D:\apache-tomcat-7.0.53\bin>version.bat
		setting up CCF system properties
		setting ccf.home to D:\ccf
		JAVA_OPTS:  -Xms512m -Xmx1024m -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=256m "-Dccf.home=D:\ccf"
		Using CATALINA_BASE:   "D:\apache-tomcat-7.0.53"
		Using CATALINA_HOME:   "D:\apache-tomcat-7.0.53"
		Using CATALINA_TMPDIR: "D:\apache-tomcat-7.0.53\temp"
		Using JRE_HOME:        "C:\Program Files\Java\jdk1.7.0_71"
		Using CLASSPATH:       "D:\apache-tomcat-7.0.53\bin\bootstrap.jar;D:\apache-tomcat-7.0.53\bin\tomcat-juli.jar"
		Server version: Apache Tomcat/7.0.53
		Server built:   Mar 25 2014 06:20:16
		Server number:  7.0.53.0
		OS Name:        Windows Server 2008 R2
		OS Version:     6.1
		Architecture:   amd64
		JVM Version:    1.7.0_71-b14
		JVM Vendor:     Oracle Corporation
		
		== service install output
		D:\apache-tomcat-7.0.53\bin>service.bat install
		Installing the service 'TeamForgeConnector' ...
		Using CATALINA_HOME:    "D:\apache-tomcat-7.0.53"
		Using CATALINA_BASE:    "D:\apache-tomcat-7.0.53"
		Using JAVA_HOME:        "C:\Program Files\Java\jdk1.7.0_71"
		Using JRE_HOME:         "C:\Program Files\Java\jdk1.7.0_71\jre"
		Using JVM:              "C:\Program Files\Java\jdk1.7.0_71\jre\bin\server\jvm.dll"
		"D:\apache-tomcat-7.0.53\bin\tomcat7x86.exe"
		setting up CCF system properties
		setting ccf.home to D:\ccf
		The service 'TeamForgeConnector' has been installed.
		
		NOTICE: the wrong executable "D:\apache-tomcat-7.0.53\bin\tomcat7x86.exe"
		
		
		