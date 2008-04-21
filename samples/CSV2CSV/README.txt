 Collabnet Connector Framework - Samples
				    ---------------------------------------

About:

   The CCF Samples are provided to give the users a kick-start on using the CCF framework.

This sample does the following...
    
    * reads delimited records from file
    * converts delimited input into a map
    * converts map to xml
    * converts xml to generic artifact xml format
    * does a transformation on the data to change some field values
    * converts the generic artifact format back to a ordered map xml structure
    * converts the ordered map xml structure back to a map
    * write map as a csv file  

  Run it like this...
    
    java org.openadaptor.spring.SpringAdaptor -config config/csv2csv.xml -bean Adaptor
    
  You need the following in your classpath
  
    lib
    lib/openadaptor.jar
    lib/openadaptor-spring.jar
    lib/openadaptor-depends.jar
    
 Happy Connecting!