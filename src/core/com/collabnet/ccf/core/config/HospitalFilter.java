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

package com.collabnet.ccf.core.config;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openadaptor.auxil.processor.script.ScriptProcessor;
import org.openadaptor.core.IDataProcessor;
import org.openadaptor.core.exception.ValidationException;

import com.collabnet.ccf.core.CCFRuntimeException;

/**
 * The hospital filter executes the script as setup in the config.xml property
 * "script" or "scriptFile". If the last result of the script is boolean true,
 * HospitalFilter returns the result of the script. Otherwise, the filter throws
 * a CCFRuntimeException with the message configured in the property
 * "exceptionMessage", causing the artifact to be hospitalized.
 * 
 * @author jnicolai, ctaylor
 * 
 */
public class HospitalFilter extends ScriptProcessor implements IDataProcessor {

    private String exceptionMessage;

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    @Override
    public Object[] process(Object input) {
        Object[] scriptResult = super.process(input);
        Object result = getLastResult();
        if (result != null && result instanceof Boolean
                && ((Boolean) result).booleanValue()) {
            return scriptResult;
        } else {
            throw new CCFRuntimeException(getExceptionMessage());
        }
    }

    /**
     * define the exception message to be thrown if the script doesn't return
     * successfully.
     * 
     * @param exceptionMessage
     *            the message of the CCFException
     */
    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void validate(List exceptions) {
        super.validate(exceptions);
        if (StringUtils.isEmpty(getExceptionMessage())) {
            exceptions.add(new ValidationException(
                    "exceptionMessage is null or empty.", this));
        }
    }
}
