package com.collabnet.ccf.ist;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.zip.CRC32;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.CCFRuntimeException;
import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.utils.JerichoUtils;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteArtifactCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteComment;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteDocument;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteArtifactCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteComment;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteDocument;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteIncident;

/**
 * More a module for our kind of versions
 *
 * @author volker
 *
 */
public class ISTArtifactVersionHelper {

    public static final int generateHash(RemoteIncident ri,
            String commentsDump, String documentsDump, String customsDump) {
        String fullString = "";
        //        String hashInfo = "empty";
        int theVersion = 0;
        long theDigest = 0;

        // serialize all known internal fields, but excluding the last updated info!

        fullString += String.valueOf(ri.getArtifactTypeId());
        fullString += ri.getClosedDate().isNil() ? "unset" : df
                .format(ISTHandler.toDate(ri.getClosedDate().getValue()));
        fullString += String.valueOf(ri.getCompletionPercent());
        fullString += df.format(ISTHandler.toDate(ri.getConcurrencyDate()));
        fullString += ri.getCreationDate().isNil() ? "unset" : df
                .format(ISTHandler.toDate(ri.getCreationDate().getValue()));
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
        fullString += ri.getStartDate().isNil() ? "unset" : df
                .format(ISTHandler.toDate(ri.getStartDate().getValue()));
        fullString += String.valueOf(ri.getTestRunStepId().getValue());
        fullString += String.valueOf(ri.getVerifiedReleaseId().getValue());
        fullString += ri.getVerifiedReleaseVersionNumber().getValue();

        // add list field values
        fullString += commentsDump;
        fullString += documentsDump;
        fullString += customsDump;

        try {
            CRC32 crc = new CRC32();
            byte[] bytesOfMessage = fullString.getBytes("UTF-8");
            crc.update(bytesOfMessage);
            theDigest = crc.getValue();
            theVersion = (int) (Long.valueOf(theDigest) % Math.pow(
                    2,
                    20));

            //            hashInfo = Long.toHexString(theDigest) + "  "
            //                    + Math.abs(Long.valueOf(theDigest).hashCode()) + "  "
            //                    + theVersion;
        } catch (UnsupportedEncodingException e) {
            String cause = "Failed to generate version hash for incident #"
                    + ri.getIncidentId().getValue();
            throw new CCFRuntimeException(cause, e);
        }

        return theVersion;
    }

    public static long getFullVersion(int incrementPart, int hashPart) {
        return (((long) incrementPart) << 32) | (hashPart & 0xffffffffL);
    }

    public static final int getHashPart(long fullVersion) {
        return (int) (fullVersion);
    }

    public static final long getIncrementedVersion(long fullVersion) {
        return getFullVersion(
                getVersionPart(fullVersion) + 1,
                getHashPart(fullVersion));
    }

    public static int getVersionPart(long fullVersion) {
        return (int) (fullVersion >> 32);
    }

    /**
     * returns a good enough unique integer identifier for the RemoteIncident.
     * It is based on all mandatory incident fields, all comment texts and all
     * attached file
     *
     * @param inc
     * @return
     */

    private ISTMetaData       metaHelper = null;

    private static DateFormat df         = GenericArtifactHelper.df;

    private static final Log  log        = LogFactory
            .getLog(ISTArtifactVersionHelper.class);

    public ISTArtifactVersionHelper(ISTMetaData md) {
        this.metaHelper = md;
    }

    public String generateAttachmentsDump(ArrayOfRemoteDocument documents) {
        String ret = "";
        for (RemoteDocument d : documents.getRemoteDocument()) {
            ret += d.getAttachmentId().getValue()
                    + d.getFilenameOrUrl().getValue()
                    + df.format(ISTHandler.toDate(d.getUploadDate()));
        }

        return ret;
    }

    public String generateCommentsDump(ArrayOfRemoteComment comments) {
        String ret = "";
        for (RemoteComment c : comments.getRemoteComment()) {
            ret += c.getCommentId().getValue()
                    + JerichoUtils.htmlToText(c.getText().getValue())
                    + df.format(ISTHandler.toDate(c.getCreationDate()
                            .getValue()));
        }
        return ret;
    }

    public String generateCustomsDump(
            ArrayOfRemoteArtifactCustomProperty properties) {

        String ret = "";
        for (RemoteArtifactCustomProperty prop : properties
                .getRemoteArtifactCustomProperty()) {
            ret += metaHelper.getName(prop) + metaHelper.getValue(prop);
        }

        return ret;
    }

}
