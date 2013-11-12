/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet") Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.collabnet.ccf.pi.qc;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.NullRecordException;
import org.openadaptor.core.exception.RecordFormatException;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.pi.qc.v90.QCConnectHelper;

public class QCReaderOutputVerificator extends QCConnectHelper implements IDataProcessor {

    private String           fileName;
    private static final Log log = LogFactory.getLog(QCConnectHelper.class);

    public QCReaderOutputVerificator() {
        super();
    }

    public String getFileName() {
        return fileName;
    }

    public Object[] process(Object data) {

        //String fileName;
        if (data == null) {
            throw new NullRecordException(
                    "Expected Document. Null record not permitted.");
        }

        if (!(data instanceof ArrayList)) {
            throw new RecordFormatException("Expected Document. Got ["
                    + data.getClass().getName() + "]");
        }
        Object[] result = processXMLDocuments((Object) data, this.getFileName());
        return result;
    }

    @SuppressWarnings("unchecked")
    public Object[] processXMLDocuments(Object data, String fileName) {

        int runStatus = 1;
        List<String> failure = new ArrayList<String>();
        List<String> success = new ArrayList<String>();

        String passStatus = "Run Passed";
        success.add(passStatus);
        String failStatus = "Run Failed";
        failure.add(failStatus);

        List<Document> incomingDocumentList = (List<Document>) data;

        try {
            FileInputStream in = new FileInputStream(fileName);
            ObjectInputStream s = new ObjectInputStream(in);

            PrintWriter out1 = new PrintWriter(new FileWriter("inDoc.xml"));
            PrintWriter out2 = new PrintWriter(new FileWriter("retDoc.xml"));

            List<List<Document>> retObj = (List<List<Document>>) s.readObject();
            List<Document> retrievedDocumentList = (List<Document>) retObj
                    .get(0);
            if (incomingDocumentList
                    .equals((List<Document>) retrievedDocumentList))
                return success.toArray();

            int retrievedSize = retrievedDocumentList.size();
            int incomingSize = incomingDocumentList.size();

            if (retrievedSize != incomingSize)
                return failure.toArray();

            for (int cnt = 0; cnt < retrievedSize; cnt++) {
                Document inDoc = incomingDocumentList.get(cnt);
                Document retDoc = retrievedDocumentList.get(cnt);

                String inRecord = inDoc.asXML();
                String retRecord = retDoc.asXML();
                //inDoc.
                if (inRecord.equals(retRecord))
                    runStatus = 1;
                else
                    runStatus = 0;

                out1.write(inDoc.asXML());
                out2.write(retDoc.asXML());
            }
        } catch (IOException e) {
            System.out.println("File handling exception" + e);
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found exception" + e);
        }

        if (runStatus == 0)
            return failure.toArray();
        return success.toArray();
    }

    public void reset(Object context) {
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @SuppressWarnings("unchecked")
    public void validate(List exceptions) {
        super.validate(exceptions);
        // Capture the return exception list and validate the exceptions
        if (getFileName() == null) {
            log.error("serverUrl-property not set");
            exceptions.add(new ValidationException(
                    "serverUrl-property not set", this));
        }
    }

}