/*
 Copyright (C) 2001 - 2008 The Software Conservancy as Trustee. All rights reserved.

 Permission is hereby granted, free of charge, to any person obtaining a copy of
 this software and associated documentation files (the "Software"), to deal in the
 Software without restriction, including without limitation the rights to use, copy,
 modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 and to permit persons to whom the Software is furnished to do so, subject to the
 following conditions:

 The above copyright notice and this permission notice shall be included in all 
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 Nothing in this notice shall be deemed to grant any rights to trademarks, copyrights,
 patents, trade secrets or any other intellectual property of the licensor or any
 contributor except as expressly stated herein. No patent license is granted separate
 from the Software, for code that you delete from the Software, or for combinations
 of the Software with other software or hardware.
*/

package trial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openadaptor.auxil.processor.simplerecord.FilterProcessor;
import org.openadaptor.core.adaptor.Adaptor;
import org.openadaptor.core.router.Router;

/**
 * This example code is equivalent to the router.xml spring example
 * 
 * It shows how to construct an adaptor with a Router rather than a Pipeline.
 * In this example the configuration of the router is identical to that of
 * the router in router.xml.
 * 
 * Typically the reasons for using a Router are that you need some kind
 * of branched linkage between the adaptor component and / or exception
 * management for specific exceptions.
 * 
 * @author perryj
 *
 */
public class TrialCCFCoreMapping {

  public static ResultSet executeSql(String sql) throws ClassNotFoundException, SQLException {
	  	Connection conn;                                                //our connnection to the db - presist for life of program

        // Load the HSQL Database Engine JDBC driver
        // hsqldb.jar should be in the class path or made part of the current jar
        Class.forName("org.hsqldb.jdbcDriver");

        // connect to the database.   This will load the db files and start the
        // database if it is not alread running.
        // db_file_name_prefix is used to open or create files that hold the state
        // of the db.
        // It can contain directory names relative to the
        // current working directory
        conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/xdb",    // filenames
                                           "sa",                     // username
                                           "");                      // password
        
        Statement st = null;
        ResultSet rs = null;

        st = conn.createStatement();         // statement objects can be reused with

        // repeated calls to execute but we
        // choose to make a new one each time
        rs = st.executeQuery(sql);    // run the query

        // Memory leak here? taken care by the gc?
        //st.close();    // NOTE!! if you close a statement the associated ResultSet is

        // closed too
        // so you should copy the contents to some other object.
        // the result set is invalidated also  if you recycle an Statement
        // and try to execute some other query before the result set has been
        // completely examined.

        return rs;
  }
  public static Object getReaderObject (String repositoryId, String readerClass) throws ClassNotFoundException, SQLException {
	  
	  readerClass = "java.util.Map";	// hardcoding for this code to pass temproarily
	  Object readerObject = Class.forName(readerClass);
	  
	  // get the repos params for this repository
	  String sql = "SELECT PARAM_NAME, PARAM_VALUE FROM REPOSITORY_PARAMS WHERE REPOSITORY_ID = " + repositoryId;

	  ResultSet rs = executeSql(sql);
      for (; rs.next(); ) {
      	String paramName = rs.getObject(1).toString();
      	String paramValue = rs.getObject(2).toString();

      	// Set the param name and value into the reader object's hashmap
      	// setParam(String, String) should be implemented by all readers and writers
      	// readerObject.setParam(paramName, paramValue);

      	System.out.println(paramName + " " + paramValue);
      }

	  // get the mapping sync info for this mapping
	  sql = "SELECT RM.ID, SYNC_PARAM_NAME, SYNC_PARAM_VALUE FROM MAPPING_SYNC_INFO MSI, REPOSITORY_MAPPINGS RM WHERE MSI.MAPPING_ID = RM.ID AND RM.SOURCE_ID = " + repositoryId;

	  rs = executeSql(sql);
      for (; rs.next(); ) {
    	String mappingId = rs.getObject(1).toString();
      	String syncParamName = rs.getObject(2).toString();
      	String syncParamValue = rs.getObject(3).toString();

      	// Set the param name and value into the reader object's hashmap
      	// setParam(String, String) should be implemented by all readers and writers
      	// readerObject.setParam(paramName, paramValue);

      	System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%");
      	System.out.println(mappingId + " " + syncParamName + " " + syncParamValue);
      	System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%");
      }

	  return readerObject;
  }
  public static List<Object> getAllActiveReaders() throws ClassNotFoundException, SQLException {

	  	List <Object> objectList = new ArrayList<Object>();

        String sql = "SELECT R.ID, R.IDENTIFIER, R.READER_TYPE from REPOSITORIES R where ";
        sql += "R.ID IN (SELECT RM.SOURCE_ID FROM REPOSITORY_MAPPINGS RM)";

        ResultSet rs = executeSql(sql);
        for (; rs.next(); ) {
        	String repositoryId = rs.getObject(1).toString();
        	String repositoryIdentifier = rs.getObject(2).toString();
        	String readerClass = rs.getObject(3).toString();

        	Object readerObject = getReaderObject(repositoryId, readerClass);
        	objectList.add(readerObject);
        	System.out.println(repositoryId + " " + repositoryIdentifier + " " + readerClass);
        }

        return objectList;
  }
  
  public static List<FilterTransformerWriter> getAllFilterTransformerWriters() throws ClassNotFoundException, SQLException {

	  	List <FilterTransformerWriter> objectList = new ArrayList<FilterTransformerWriter>();

	  // One transformation file per writer - we can save on the writer instances like
	  // we do for the readers
      String sql = "SELECT R.ID, R.IDENTIFIER, R.WRITER_TYPE, RM.TRANSFORMER_CLASS, RM.XSLT_FILE_NAME  from REPOSITORIES R, REPOSITORY_MAPPINGS RM where ";
      sql += "R.ID = RM.TARGET_ID";

      ResultSet rs = executeSql(sql);
      for (; rs.next(); ) {
      	String repositoryId = rs.getObject(1).toString();
      	String repositoryIdentifier = rs.getObject(2).toString();
      	String writerClass = rs.getObject(3).toString();
      	String transformerClass = rs.getObject(4).toString();
      	String xsltFileName = rs.getObject(5).toString();

      	FilterProcessor filterObject = new FilterProcessor();
      	// set filter parameters

      	// Make the transformer object
      	Object transformerObject = Class.forName(transformerClass);
      	// set the transformer class' xslt file field
      	
      	// get the writer object with parameters set
      	System.out.println(writerClass);
      	writerClass = "java.util.Map";	// hardcoding for this code to pass temproarily
      	Object writerObject = Class.forName(writerClass);
      	
      	FilterTransformerWriter filterTransformerWriter = new FilterTransformerWriter(filterObject, transformerObject, writerObject);
      	objectList.add(filterTransformerWriter);
      	System.out.println(repositoryId + " " + repositoryIdentifier + " " + writerClass + " " + xsltFileName);
      }

      return objectList;
  }

  public static void main (String[] args) {
	List<Object> readers = null;
	List<FilterTransformerWriter> filterTransformerWriters = null;
	/* Read the mapping data here */
	try {
		System.out.println("==========================================");
		readers = getAllActiveReaders();
		System.out.println("==========================================");
		filterTransformerWriters = getAllFilterTransformerWriters();
		System.out.println("==========================================");
	}
	catch (Exception e) {
		System.out.println(e.getMessage());
	}

	System.exit(0);
	Map processMap = new HashMap();
	int i, j;
	List<Object> filters = new ArrayList<Object>();
	
	// First set the filter, tranformer and writer wiring
	for (i = 0; i < filterTransformerWriters.size(); i++) {
		FilterTransformerWriter filterTransformerWriter = filterTransformerWriters.get(i);
		Object map = processMap.put(filterTransformerWriter.getFilter(), filterTransformerWriter.getTransformer());
		processMap.put(filterTransformerWriter.getTransformer(), filterTransformerWriter.getWriter());
		
		filters.add(map);
	}

	// TODO: Associate the synchronization info with the readers
	// Map all the readers
	for (i = 0 ; i < readers.size() ; i++) {
		Object reader = readers.get(i);
		// map each reader to all filters
		for ( j = 0 ; j < filters.size() ; j++) {
			Object filter = filters.get(j);
			processMap.put(reader, filter);
		}
	}

	Router router = new Router();
	router.setProcessMap(processMap);
	
	Adaptor adaptor = new Adaptor();
	adaptor.setMessageProcessor(router);
	
	// this starts the adaptor
	adaptor.run();


  }

}
