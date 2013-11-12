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

package com.collabnet.ccf.core.utils;

/**
 * This class is a utility class that provides methods for String manipulations
 * in the XSLT transformers.
 * 
 * @author Madhusuthanan Seetharam (madhusuthanan@collab.net)
 * 
 */
public class GATransformerUtil {
    static FormatterProxy proxy = null;

    static {
        try {
            Class.forName("net.htmlparser.jericho.Source");
        } catch (ClassNotFoundException e) {
            proxy = new StringUtils();
        }
        if (proxy == null) {
            proxy = new JerichoUtils();
        }
    }

    public static String encodeHTMLToEntityReferences(String html) {
        return proxy.convertTextToHtml(html);
    }

    /**
     * This method strips all the HTML tags present int the original String that
     * is passed.
     * 
     * @param original
     *            - the String that is to be stripped off the HTML tags present
     *            in it.
     * @return the original String with all the HTML tags stripped off.
     */
    public static String stripHTML(String original) {
        return proxy.convertHtmlToText(original);
    }

    /**
     * Trims off the leading and trailing white spaces of the String that is
     * passed in.
     * 
     * @param stringToTrim
     *            - The String to be trimmed
     * @return the trimmed String.
     */
    public static String trim(String stringToTrim) {
        return proxy.trimString(stringToTrim);
    }
}
