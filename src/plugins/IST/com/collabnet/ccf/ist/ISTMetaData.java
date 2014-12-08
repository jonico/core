package com.collabnet.ccf.ist;

import java.text.DateFormat;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.utils.JerichoUtils;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.ArrayOfint;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteArtifactCustomProperty;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteCustomList;
import com.inflectra.spirateam.mylyn.core.internal.services.soap.RemoteCustomProperty;

public class ISTMetaData {

    public static ArrayList<String> getMultiListValues(
            RemoteArtifactCustomProperty prop) {
        if (prop.getIntegerListValue().getValue() != null) {
            ArrayOfint mvalues = prop.getIntegerListValue().getValue();
            ArrayList<String> list = new ArrayList<String>();
            RemoteCustomProperty propDef = prop.getDefinition().getValue();
            RemoteCustomList multi = propDef.getCustomList().getValue();

            for (int i : mvalues.getInt()) {
                list.add(multi.getValues().getValue()
                        .getRemoteCustomListValue().get(i).getName().getValue());
            }
            return list;
        }
        return null;
    }

    public static final String getSingleListValue(
            RemoteArtifactCustomProperty prop) {
        if (prop.getIntegerListValue().getValue() != null) {
            ArrayOfint listIndex = prop.getIntegerListValue().getValue();
            RemoteCustomProperty propDef = prop.getDefinition().getValue();

            int singleIndex = listIndex.getInt().get(0);
            RemoteCustomList listDef = propDef.getCustomList().getValue();
            return listDef.getValues().getValue().getRemoteCustomListValue()
                    .get(singleIndex).getName().getValue();
        }
        return null;
    }

    public static String getValue(RemoteArtifactCustomProperty prop) {

        RemoteCustomProperty propDef = prop.getDefinition().getValue();

        ISTFieldTypes type = ISTFieldTypes.valueOf(propDef
                .getCustomPropertyTypeName().getValue());
        switch (type) {
            case Text:
                return JerichoUtils
                        .htmlToText(prop.getStringValue().getValue());
            case Date:
                return df.format(ISTHandler.toDate(prop.getDateTimeValue()
                        .getValue()));
            case List:
                return getSingleListValue(prop);
            case MultiList:
                ArrayList<String> selected = getMultiListValues(prop);
                return StringUtils.join(selected.toArray(), ",");
            case Integer:
                return String.valueOf(prop.getIntegerValue().getValue());
            default:
                return null;
        }
    }

    private static DateFormat df  = GenericArtifactHelper.df;

    private static final Log  log = LogFactory.getLog(ISTReader.class);
}
