package com.collabnet.ccf.pi.cee.pt.v50;

import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.lifecycle.LifecycleComponent;
import org.apache.axis.EngineConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import com.collabnet.ccf.pi.cee.pt.v50.ws.TrackerWebServicesClient;
import com.collabnet.tracker.common.ClientArtifact;
import com.collabnet.tracker.core.TrackerClientManager;
import com.collabnet.tracker.core.TrackerWebServicesClient;
import com.collabnet.tracker.core.model.TrackerArtifactType;
import com.collabnet.tracker.core.util.TrackerUtil;
import com.collabnet.tracker.ws.ArtifactType;
import com.collabnet.tracker.ws.HistoryActivity;
import com.collabnet.tracker.ws.HistoryTransaction;

import com.collabnet.tracker.ws.history.ArtifactHistoryManager;
import com.collabnet.tracker.ws.history.ArtifactHistoryService;
import com.collabnet.tracker.ws.history.ArtifactHistoryServiceLocator;
import com.collabnet.tracker.ws.history.ArtifactHistorySoapBindingStub;
import com.collabnet.tracker.ws.HistoryTransactionList;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.activation.FileDataSource;

public class PTReader extends LifecycleComponent implements IDataProcessor {

	private static final Log log = LogFactory.getLog(PTReader.class);
	
	private boolean keepAlive=true;

	public void validate(List exceptions) {
		// check whether all necessary properties are set
		;
	}

	public void setKeepAlive(String keepAlive) {
		if (keepAlive.equals("false"))
			this.keepAlive=false;
	}

	public String isKeepAlive() {
		return keepAlive?"true":"false";
	}
	
	public Object[] process(Object data) {

		try {
			
			// This is the basic object containing the API for interacting with PT
 			TrackerWebServicesClient twsclient = TrackerClientManager.getInstance().createClient("http://devpro5.cu023.cubit.maa.collab.net",
																			  "test1111", "test1111",
																			  null,
																			  null, null);
 			Collection<TrackerArtifactType> tAT;
 			
 			// To get the artifact types
 			tAT = twsclient.getArtifactTypes();
 			for (TrackerArtifactType type : tAT )
 			{
 				System.out.println(type.getDisplayName());
 				System.out.println(type.getNamespace());
 				System.out.println(type.getTagName());
 			}
 			Date fromdate = new Date(0);
 			Date todate = new Date();
 			

 			// Get the changed artifacts for a given set of artifact types, given the from time,
 			// changes between from time and now will be returned
 			Set<String> artifactTypes = new HashSet<String>();
  			artifactTypes.add("{urn:cu023.cubit.maa.collab.net/PT/IZ-PT/}issue_tracker_artifact3 ");
 			String changedArtifacts[];
 			changedArtifacts = twsclient.getChangedArtifacts(artifactTypes, "1212046195408");
 			for (String artifact: changedArtifacts)
 			{
 				System.out.println(artifact);
 			}
 			
 			
 			
 			// Obtain the history of changes between two given times
 			// return value includes transactions and activities
 			ArtifactType[] ata = new ArtifactType[1];
 			ata[0] = new ArtifactType("issue_tracker_artifact3", "urn:cu023.cubit.maa.collab.net/PT/IZ-PT/", "Issue Tracker artifact3");
 			
 			HistoryTransactionList ahl = twsclient.getArtifactChanges(ata, (long)System.currentTimeMillis() - 600000  /*1212046195408*/, (Long)System.currentTimeMillis());
 			
 			HistoryTransaction hta[] = ahl.getHistoryTransaction();
 			if(hta != null)
 			{
		 			for (HistoryTransaction ht: hta)
		 			{
		 				System.out.println("------------------------------------");
		 				System.out.println(ht.getModifiedBy());
		 				System.out.println(ht.getModifiedOn());
		 				System.out.println(ht.getType());
		 				
		 				HistoryActivity[] haa = ht.getHistoryActivity();
		 				
		 				for (HistoryActivity ha : haa)
		 				{
		 					System.out.println("```````````````````````````````");
		 					System.out.println(ha.getArtifactId());
		 					System.out.println(ha.getNamespace());
		 					System.out.println(ha.getTagName());
		 					System.out.println(ha.getType());
		 					System.out.println(ha.getOldValue(0));
		 					System.out.println(ha.getNewValue(0));
		 					System.out.println("```````````````````````````````");
		 				}
		 				
		 				System.out.println("===============================");
		
		 			}
 			}
 			

 			// Create a new artifact
 			List <ClientArtifact> cla;
 			ClientArtifact ca;
 			cla = new ArrayList<ClientArtifact>();
 			ca = new ClientArtifact();
 			ca.setTagName("issue_tracker_artifact3");
 			ca.setNamespace("urn:cu023.cubit.maa.collab.net/PT/IZ-PT/");
 			ca.addAttributeValue("urn:cu023.cubit.maa.collab.net/PT/IZ-PT/", "summary", "CFWAPI2");
 			cla.add(ca);
 			twsclient.createArtifactList(cla);
 			
 			
 			// Modify an existing artifact
 			ca = new ClientArtifact();
 			ca.setTagName("issue_tracker_artifact3");
 			ca.setNamespace("urn:cu023.cubit.maa.collab.net/PT/IZ-PT/");
 			ca.addAttributeValue("urn:cu023.cubit.maa.collab.net/PT/IZ-PT/", "summary", "CFWAPI22222");
 			String id = "EG42945";
 			ca.addAttributeValue("urn:cu023.cubit.maa.collab.net/PT/IZ-PT/", "id", id);
 			cla.add(ca);
 			twsclient.updateArtifactList(cla);

 			
 			// Adding attachments
 			String filename = "e:\\madan\\a.rar";
 			FileDataSource attachment = new FileDataSource(filename);
 			id = ca.getArtifactID();
 			id = "EG42945";
			twsclient.postAttachment(id, "Attachment added from pt ws client", attachment);
			System.out.println("      Attached file: " + filename);

			// retrieving attachments
			String attachmentid = "3711";
			InputStream is = twsclient.downloadAttachmentAsStream(id, attachmentid);
			byte [] bytes = new byte[10000];
			int filesize = is.read(bytes);
			for (int i = 0 ; i < filesize ; i++) {
				System.out.print((char)bytes[i]);
			}
		}
		catch (Exception e) {
			log.error("Exception while trying to getChangedArtifactIDs");
			log.error("================================================");
			log.error(e.getStackTrace());
			log.error("================================================");
		}
		
		return (Object[]) data; 
	}

	public void reset(Object context) {
	}
}
