/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet")
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

package com.collabnet.ccf.pi.qc.v90;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifact;
import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;
import com.collabnet.ccf.core.utils.DateUtil;
import com.collabnet.ccf.pi.qc.v90.api.IConnection;

public class QCMigrator extends QCWriter {
	private static final Log log = LogFactory.getLog(QCMigrator.class);
	public Document createArtifact(Document gaDocument) {
		return this.updateArtifact(gaDocument);
	}
	protected Object[] processXMLDocument(Document gaDocument) {
		Object[] result = super.processXMLDocument(gaDocument);
		if(result != null && result.length == 1){
			Document returnDoc1 = (Document) result[0];
			GenericArtifact inDoc = getArtifactFromDocument(returnDoc1);
			GenericArtifact outDoc = new GenericArtifact();

			outDoc.setArtifactAction(GenericArtifact.ArtifactActionValue.UPDATE);
			outDoc.setArtifactMode(GenericArtifact.ArtifactModeValue.COMPLETE);
			outDoc.setArtifactType(GenericArtifact.ArtifactTypeValue.PLAINARTIFACT);

			outDoc.setTargetArtifactId(inDoc.getSourceArtifactId());
			outDoc.setTargetArtifactLastModifiedDate(inDoc.getSourceArtifactLastModifiedDate());
			outDoc.setTargetArtifactVersion(inDoc.getSourceArtifactVersion());
			outDoc.setTargetRepositoryId(inDoc.getSourceRepositoryId());
			outDoc.setTargetRepositoryKind(inDoc.getSourceRepositoryKind());
			//outDoc.setTargetSystemEncoding(inDoc.getSourceSystemEncoding());
			outDoc.setTargetSystemId(inDoc.getSourceSystemId());
			outDoc.setTargetSystemKind(inDoc.getSourceSystemKind());
			outDoc.setTargetSystemTimezone(inDoc.getSourceSystemTimezone());

			outDoc.setSourceArtifactId(inDoc.getTargetArtifactId());
			outDoc.setSourceArtifactLastModifiedDate(inDoc.getTargetArtifactLastModifiedDate());
			outDoc.setSourceArtifactVersion(inDoc.getTargetArtifactVersion());
			outDoc.setSourceRepositoryId(inDoc.getTargetRepositoryId());
			outDoc.setSourceRepositoryKind(inDoc.getTargetRepositoryKind());
			//outDoc.setSourceSystemEncoding(inDoc.getTargetSystemEncoding());
			outDoc.setSourceSystemId(inDoc.getTargetSystemId());
			outDoc.setSourceSystemKind(inDoc.getTargetSystemKind());
			outDoc.setSourceSystemTimezone(inDoc.getTargetSystemTimezone());

			outDoc.setTransactionId(inDoc.getTargetArtifactVersion());

			Document returnDoc2 = null;
			try {
				returnDoc2 = GenericArtifactHelper.createGenericArtifactXMLDocument(outDoc);
			} catch (GenericArtifactParsingException e) {
				String message = "Exception while trying to creat the reverse artifact";
				log.error(message, e);
				throw new CCFRuntimeException(message, e);
			}
			result = new Document[]{returnDoc1, returnDoc2};
		}
		return result;
	}
	protected void updateDefect(IConnection connection, String targetArtifactId,
			GenericArtifact genericArtifact, List<GenericArtifactField> allFields) throws Exception{
//		String targetSystemTimezone = genericArtifact.getTargetSystemTimezone();

		// retrieve version to update
//		List<String> targetAutimeAndTxnIdBeforeUpdate = getAutimeAndTxnId(
//				connection, targetArtifactId, null, ARTIFACT_TYPE_PLAINARTIFACT);
//		String targetTransactionIdBeforeUpdate = targetAutimeAndTxnIdBeforeUpdate.get(0);
//		int targetTransactionIdBeforeUpdateInt = Integer.parseInt(targetTransactionIdBeforeUpdate);
		// now do conflict resolution
//		if (!AbstractWriter.handleConflicts(targetTransactionIdBeforeUpdateInt, genericArtifact)) {
//			return;
//		}

		//IQCDefect updatedArtifact = defectHandler.updateDefect(
		//		connection, targetArtifactId, allFields, this
		//				.getUserName(), targetSystemTimezone);

		// FIXME This is not atomic
//		getDefectHandler().updateDefect(
//				connection, targetArtifactId, allFields, this
//						.getUserName(), targetSystemTimezone);
		log
				.info("QC Defect " + targetArtifactId + " is migrated successfully with the changes from "+genericArtifact.getSourceArtifactId());
		genericArtifact.setTargetArtifactId(targetArtifactId);
		// FIXME This is not atomic
		List<String> targetAutimeAndTxnId = getAutimeAndTxnIdForDefect(
				connection, targetArtifactId, null, ARTIFACT_TYPE_PLAINARTIFACT);
		genericArtifact.setTargetArtifactVersion(targetAutimeAndTxnId.get(0));
		genericArtifact.setTargetArtifactLastModifiedDate(DateUtil.format(DateUtil.parseQCDate(targetAutimeAndTxnId.get(1))));
	}
	public Document updateArtifact(Document gaDocument) {
		GenericArtifact genericArtifact = getArtifactFromDocument(gaDocument);
		List<GenericArtifactField> allFields = this.getAllGenericArtfactFields(genericArtifact);
		String targetArtifactId = genericArtifact.getTargetArtifactId();
		int qcDefectId = -1;
		if(StringUtils.isEmpty(targetArtifactId)
				|| targetArtifactId.equals(GenericArtifact.VALUE_UNKNOWN)){
			log.warn("Target artifact id " + targetArtifactId +" is not a valid QC defect id.");
			return null;
		}
		else {
			try {
				qcDefectId = Integer.parseInt(targetArtifactId);
			}
			catch(NumberFormatException e) {
				log.warn("Target artifact id " + targetArtifactId +" is not a valid QC defect id.");
				return null;
			}
		}
		IConnection connection = this.connect(genericArtifact);
		QCHandler defectHandler = this.getDefectHandler();
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(qcDefectId);
		List<IQCDefect> defects = defectHandler.getDefectsWithIds(connection, ids);
		if(defects.size() == 0){
			log.warn("Target artifact id " + targetArtifactId
					+" does not exist in the repository " + genericArtifact.getTargetRepositoryId());
			return null;
		}
		try {
			genericArtifact.setArtifactAction(GenericArtifact.ArtifactActionValue.UPDATE);
			this.updateDefect(connection, targetArtifactId, genericArtifact, allFields);
		} catch (Exception e) {
			String cause = "Exception occured while updating defect in QC:"
				+ genericArtifact.toString();
			log.error(cause, e);
			genericArtifact.setErrorCode(GenericArtifact.ERROR_EXTERNAL_SYSTEM_WRITE);
			// sending this artifact to HOSPITAL
			throw new CCFRuntimeException(cause,e);

		} finally {
			this.disconnect(connection);
			connection = null;
		}
		return this.returnDocument(genericArtifact);
	}
}
