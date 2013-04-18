package com.collabnet.ccf.rqp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.GenericArtifactField;
import com.collabnet.ccf.core.ga.GenericArtifactField.FieldValueTypeValue;
import com.collabnet.ccf.rqp.enums.RQPType;
import com.rational.reqpro.rpx.enumAttrDataTypes;

public class RQPMetaData {

	private static final Long RQP_OFFSET_VERSION_NUMBER = 9999L;
	private static final Log log = LogFactory.getLog(RQPHandler.class);
	private static final String RQP_REQUIREMENT_TYPE_PREFIX = "URS";
	private static final String RQP_FUNCTIONAL_REQUIREMENT_TYPE_PREFIX = "FNR";
	private static final SimpleDateFormat isoLocal = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	public static final String RQP_REPOSITORY_DELIMITER = "-";
	public static final String RQP_PROJECT_EXTENSION = ".rqs";

	/**
	 * Retrieves the type of RequisitePro requirement, according to repositoryId
	 * Note: RepositoryId has the form repositoryName-FNR // repositoryName-URS
	 * 
	 * @param repositoryId
	 * @return
	 */
	public static RQPType retrieveRQPTypeFromRepositoryId(String repositoryId) {
		RQPType rqpType = null;

		if (repositoryId != null) {
			String[] repositoryIdSplitted = repositoryId.split(RQP_REPOSITORY_DELIMITER);

			if ((repositoryIdSplitted.length == 3)) {
				String requirementType = repositoryIdSplitted[2];

				if (requirementType.equalsIgnoreCase(RQP_REQUIREMENT_TYPE_PREFIX)) {
					rqpType = RQPType.USER_STORY;
				} else if (requirementType.equalsIgnoreCase(RQP_FUNCTIONAL_REQUIREMENT_TYPE_PREFIX)) {
					rqpType = RQPType.FUNCTIONAL_REQUIREMENT;
				}else{
					rqpType = RQPType.PACKAGE;
				}
			}
		}
		return rqpType;
	}

	public static GenericArtifactField.FieldValueTypeValue translateRQPFieldValueTypeToCCFFieldValueType(int dataType) {

		if (dataType == enumAttrDataTypes.eAttrDataTypes_Text) {
			return FieldValueTypeValue.STRING;
		}
		if (dataType == enumAttrDataTypes.eAttrDataTypes_Date) {
			return FieldValueTypeValue.DATE;
		}
		if (dataType == enumAttrDataTypes.eAttrDataTypes_List) {
			return FieldValueTypeValue.STRING;
		}
		if (dataType == enumAttrDataTypes.eAttrDataTypes_MultiSelect) {
			return FieldValueTypeValue.STRING;
		}
		if (dataType == enumAttrDataTypes.eAttrDataTypes_Real) {
			return FieldValueTypeValue.STRING;
		}
		if (dataType == enumAttrDataTypes.eAttrDataTypes_Time) {
			return FieldValueTypeValue.DATETIME;
		}

		log.warn("WorkenItem Field Type was not identified, returning String value");
		return FieldValueTypeValue.STRING;
	}

	public static String formatDate(Date date) {
		String formattedDate = null;
		if (date != null) {
			String dateString = isoLocal.format(date);
			formattedDate = dateString.substring(0, dateString.length() - 1);
		}
		return formattedDate;
	}

	public static String extractProjectNameFromRepositoryId(String repositoryId) {
		return repositoryId.split(RQP_REPOSITORY_DELIMITER)[0];
	}

	public static String extractRQPItemTypeFromRepositoryId(String targetRepositoryId) {
		return targetRepositoryId.split(RQP_REPOSITORY_DELIMITER)[2];
	}

	public static String extractPackageNameFromRepositoryId(String sourceRepositoryId) {
		return sourceRepositoryId.split(RQP_REPOSITORY_DELIMITER)[0];
	}

	/**
	 * @param requirement
	 * @return the version number from RQP to TF. Ex. v(RQP)1.000 => v(TF)1000
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static long processRQPVersionNumber(String versionNumber) throws NumberFormatException, IOException {
		return Long.parseLong(versionNumber.replaceFirst("\\.", "")) - RQP_OFFSET_VERSION_NUMBER;
	}


}
