package com.collabnet.ccf.core.utils;

public interface FormatterProxy {
    public String convertHtmlToText(String original);

    public String convertTextToHtml(String html);

    public String trimString(String stringToTrim);
}
