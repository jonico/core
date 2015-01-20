package com.collabnet.ccf.ist.sandbox;

import java.util.ArrayList;
import java.util.Date;

import com.collabnet.ccf.ist.ISTCustomFieldType;
import com.collabnet.ccf.ist.ISTMetaCache;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfRemoteComment;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfint;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportCustomPropertyRetrieveCustomListByIdServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.IImportExportIncidentAddCommentsServiceFaultMessageFaultFaultMessage;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteArtifactCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteComment;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteCustomList;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteIncident;

public class RunnerHelper {

    private static void addRandomComments(LoremIpsum li, RemoteIncident i) {
        // add comments every now and then
        if (li.getTruth()) {
            ArrayOfRemoteComment comments = ReaderRunner.objectFactory
                    .createArrayOfRemoteComment();
            for (int t = 0; t < 5; t++) {
                if (li.getTruth()) {
                    RemoteComment comment = ReaderRunner.objectFactory.createRemoteComment();
                    // RemoteComment comment = new RemoteComment();
                    comment.setArtifactId(i.getIncidentId().getValue());
                    comment.setText(ISTMetaCache.CreateJAXBString(
                            "Text",
                            li.getLine()));
                    comment.setCreationDate(ISTMetaCache
                            .CreateJAXBXMLGregorianCalendar(
                                    "CreationDate",
                                    ReaderRunner.toXMLGregorianCalendar(new Date())));
    
                    if (comments.getRemoteComment().add(
                            comment))
                        System.out.println("    added comment to #"
                                + comment.getArtifactId());
                    else
                        System.out.println("    failed to add comment to #"
                                + i.getIncidentId().getValue());
                }
            }
    
            System.out.println("==== list of comments intended for incident #"
                    + i.getIncidentId().getValue());
    
            for (RemoteComment rc : comments.getRemoteComment()) {
                System.out
                        .printf(
                                "  on Incident #%d, created on %s, %d characters: %s\n",
                                rc.getArtifactId(),
                                ReaderRunner.df.format(ReaderRunner.toDate(rc.getCreationDate()
                                        .getValue())),
                                rc.getText().getValue().length(),
                                rc.getText().getValue());
            }
            try {
                ReaderRunner.soap.incidentAddComments(comments);
            } catch (IImportExportIncidentAddCommentsServiceFaultMessageFaultFaultMessage e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
    
        }
    }

    public static ArrayList<String> getMultiListValues(
            RemoteArtifactCustomProperty prop) {
        if (!prop.getIntegerListValue().isNil()) {
            ArrayOfint mvalues = prop.getIntegerListValue().getValue();
            ArrayList<String> list = new ArrayList<String>();
            RemoteCustomProperty propDef = prop.getDefinition().getValue();
            RemoteCustomList multi = propDef.getCustomList().getValue();
    
            // TODO add CustomListValues Cache
            for (int i : mvalues.getInt()) {
                list.add(multi.getValues().getValue()
                        .getRemoteCustomListValue().get(
                                i).getName().getValue());
            }
            return list;
        }
        return null;
    }

    public static final String getSingleListValue(
            RemoteArtifactCustomProperty prop) {
        RemoteCustomProperty propDef = prop.getDefinition().getValue();
    
        if (!prop.getIntegerValue().isNil()
                && !propDef.getCustomPropertyId().isNil()) {
            int listIndex = prop.getIntegerValue().getValue();
            RemoteCustomList listDef = null;
            try {
                listDef = ReaderRunner.soap.customPropertyRetrieveCustomListById(propDef
                        .getCustomPropertyId().getValue());
            } catch (IImportExportCustomPropertyRetrieveCustomListByIdServiceFaultMessageFaultFaultMessage e) {
                return "(custom list def not found)";
                // e.printStackTrace();
            }
            return listDef.getValues().getValue().getRemoteCustomListValue()
                    .get(
                            listIndex).getName().getValue();
        }
        return "";
    }

    public static String getValue(RemoteArtifactCustomProperty prop) {
    
        RemoteCustomProperty propDef = prop.getDefinition().getValue();
    
        ISTCustomFieldType type = ISTCustomFieldType.valueOf(propDef
                .getCustomPropertyTypeName().getValue());
        switch (type) {
            case Text:
                if (prop.getStringValue().isNil()) {
                    return "(not set)";
                } else {
                    String val = prop.getStringValue().getValue().trim();
                    if (val.contains(System.getProperty("line.separator"))) {
                        String first = val.split("\n")[0];
                        return first.trim() + "(...)";
                    } else {
                        return val.trim();
                    }
                }
            case Date:
                if (prop.getDateTimeValue().isNil())
                    return "(not set)";
                else
                    return ReaderRunner.df
                            .format(ReaderRunner.toDate(prop.getDateTimeValue().getValue()));
            case List:
                return getSingleListValue(prop);
            case MultiList:
                String multi = "";
                ArrayList<String> selected = getMultiListValues(prop);
                for (String s : selected) {
                    multi += s + ",";
                }
                return multi;
            case Integer:
                return String.valueOf(prop.getIntegerValue().getValue());
            default:
                return null;
        }
    }

}
