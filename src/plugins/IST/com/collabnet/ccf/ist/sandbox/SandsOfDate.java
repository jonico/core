package com.collabnet.ccf.ist.sandbox;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.collabnet.ccf.core.ga.GenericArtifactHelper;

public class SandsOfDate {

    public static void main(String[] args) {
        p(df.getTimeZone().getID());
        dfgmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        p(dfgmt.getTimeZone().getID());

        Calendar kc = Calendar.getInstance();
        Calendar sc = Calendar.getInstance(TimeZone
                .getTimeZone("Asia/Shanghai"));
        Calendar gc = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        Date kd = kc.getTime();
        Date sd = sc.getTime();
        Date gd = gc.getTime();

        pd(
                "Koh Chang",
                kd);
        pd(
                " Shanghai",
                sd);
        pd(
                "      gmt",
                gd);

    }

    private static void p(String s) {
        System.out.println(s);
    }

    private static void pd(String i, Date d) {
        String s = i + ": " + d.toString() + "  " + d.getTime();
        s += " " + d.getTimezoneOffset();
        p(s);
        p("           " + df.format(d));
        p("           " + dfgmt.format(d));
    }

    private static final DateFormat df    = GenericArtifactHelper.df;
    private static final DateFormat dfgmt = new SimpleDateFormat(
                                                  "EEE, d MMM yyyy HH:mm:ss.SSS Z");

}
