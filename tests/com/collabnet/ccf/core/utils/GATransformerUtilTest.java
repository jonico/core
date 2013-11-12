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

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

public class GATransformerUtilTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testStripHTML() throws IOException {
        Class thisClass = this.getClass();
        ClassLoader loader = thisClass.getClassLoader();
        InputStream is = loader
                .getResourceAsStream("com/collabnet/ccf/core/utils/sample.html");
        InputStream isHtmlStripped = loader
                .getResourceAsStream("com/collabnet/ccf/core/utils/html-stripped.txt");
        int readChar = -1;
        StringBuffer buffer = new StringBuffer();
        while ((readChar = is.read()) != -1) {
            buffer.append((char) readChar);
        }
        is.close();

        readChar = -1;
        StringBuffer bufferHtmlStripped = new StringBuffer();
        while ((readChar = isHtmlStripped.read()) != -1) {
            bufferHtmlStripped.append((char) readChar);
        }
        isHtmlStripped.close();
        String htmlStripped = bufferHtmlStripped.toString();
        String stripped = GATransformerUtil.stripHTML(buffer.toString());
        assertEquals(htmlStripped.trim(), stripped.trim());
    }

}
