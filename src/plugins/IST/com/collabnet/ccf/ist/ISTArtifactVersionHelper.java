package com.collabnet.ccf.ist;

import java.io.UnsupportedEncodingException;
import java.util.zip.CRC32;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.utils.JerichoUtils;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ObjectFactory;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteIncident;

/**
 * More a module for our kind of versions
 *
 * @author volker
 *
 */
public class ISTArtifactVersionHelper {

    /**
     * returns a good enough unique integer identifier for the RemoteIncident.
     * It is based on all mandatory incident fields, all comment texts and all
     * attached file
     *
     * @param inc
     * @return
     */
    public static final long generateFullVersion(ISTConnection connection,
            ObjectFactory objectFactory, RemoteIncident ri,
            String commentsStamp, String documentsStamp) {
        String fullString = "";
        //        String hashInfo = "empty";
        long theVersion = 0;
        long theDigest = 0;

        // serialize all known internal fields, but excluding the last updated info!

        // TODO use CCF DateFormatter for all date values

        fullString += String.valueOf(ri.getArtifactTypeId());
        fullString += String.valueOf(ISTHandler.toDate(ri.getClosedDate()
                .getValue()));
        fullString += String.valueOf(ri.getCompletionPercent());
        fullString += String
                .valueOf(ISTHandler.toDate(ri.getConcurrencyDate()));
        fullString += String.valueOf(ISTHandler.toDate(ri.getCreationDate()
                .getValue()));
        fullString += JerichoUtils.htmlToText(ri.getDescription().getValue());
        fullString += String.valueOf(ri.getDetectedReleaseId().getValue());
        fullString += ri.getDetectedReleaseVersionNumber().getValue();
        fullString += String.valueOf(ri.getEstimatedEffort().getValue());
        fullString += String.valueOf(ri.getFixedBuildId().getValue());
        fullString += ri.getFixedBuildName().getValue();
        fullString += String.valueOf(ri.getIncidentId().getValue());
        fullString += String.valueOf(ri.getIncidentStatusId().getValue());
        fullString += ri.getIncidentStatusName().getValue();
        fullString += String.valueOf(ri.getIncidentStatusOpenStatus()
                .getValue());
        fullString += String.valueOf(ri.getIncidentTypeId().getValue());
        fullString += ri.getIncidentTypeName().getValue();
        fullString += ri.getName().getValue();
        fullString += String.valueOf(ri.getOpenerId().getValue());
        fullString += ri.getOpenerName().getValue();
        fullString += String.valueOf(ri.getOwnerId().getValue());
        fullString += String.valueOf(ri.getPriorityId().getValue());
        fullString += ri.getPriorityName().getValue();
        fullString += String.valueOf(ri.getProjectedEffort().getValue());
        fullString += String.valueOf(ri.getProjectId().getValue());
        fullString += ri.getProjectName().getValue();
        fullString += String.valueOf(ri.getRemainingEffort().getValue());
        fullString += String.valueOf(ri.getResolvedReleaseId().getValue());
        fullString += ri.getResolvedReleaseVersionNumber().getValue();
        fullString += String.valueOf(ri.getSeverityId().getValue());
        fullString += ri.getSeverityName().getValue();
        fullString += String.valueOf(ISTHandler.toDate(ri.getStartDate()
                .getValue()));
        fullString += String.valueOf(ri.getTestRunStepId().getValue());
        fullString += String.valueOf(ri.getVerifiedReleaseId().getValue());
        fullString += ri.getVerifiedReleaseVersionNumber().getValue();

        // add comments
        fullString += commentsStamp;
        // add attachments
        fullString += documentsStamp;

        // add custom fields

        try {
            CRC32 crc = new CRC32();
            byte[] bytesOfMessage = fullString.getBytes("UTF-8");
            crc.update(bytesOfMessage);
            theDigest = crc.getValue();
            theVersion = (long) (Long.valueOf(theDigest) % Math.pow(2, 20));

            //            hashInfo = Long.toHexString(theDigest) + "  "
            //                    + Math.abs(Long.valueOf(theDigest).hashCode()) + "  "
            //                    + theVersion;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return theVersion;
    }

    public static long getFullVersion(int incrementPart, int hashPart) {
        return (((long) incrementPart) << 32) | (hashPart & 0xffffffffL);
    }

    public static final int getHashPart(long fullVersion) {
        return (int) (fullVersion);
    }

    public static int getIncrementPart(long fullVersion) {
        return (int) (fullVersion >> 32);
    }

    public static final long incrementVersion(long fullVersion) {
        return getFullVersion(getIncrementPart(fullVersion) + 1,
                getHashPart(fullVersion));
    }

    private static final Log log = LogFactory
                                         .getLog(ISTArtifactVersionHelper.class);

}
