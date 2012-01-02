/*
 * Copyright 2009 CollabNet, Inc. ("CollabNet")
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **/

package com.collabnet.ccf.core.utils;

import org.dom4j.Document;
import org.dom4j.Element;

import com.collabnet.ccf.core.ga.GenericArtifactHelper;
import com.collabnet.ccf.core.ga.GenericArtifactParsingException;

public final class CCFUtils {
	public static final String delimiter = "-";
	private static final String[] replaceCharacters = new String[]{":","\\{", "\\}", "/","\\\\"};
	public static String getTempFileName(Document document) throws GenericArtifactParsingException{
		Element element = document.getRootElement();
		String sourceSystemId = XPathUtils.getAttributeValue(element, GenericArtifactHelper.SOURCE_SYSTEM_ID);
		String sourceRepositoryId = XPathUtils.getAttributeValue(element, GenericArtifactHelper.SOURCE_REPOSITORY_ID);
		String targetSystemId = XPathUtils.getAttributeValue(element, GenericArtifactHelper.TARGET_SYSTEM_ID);
		String targetRepositoryId = XPathUtils.getAttributeValue(element, GenericArtifactHelper.TARGET_REPOSITORY_ID);
		
		String repositoryKey = sourceSystemId + delimiter + sourceRepositoryId + delimiter
						+ targetSystemId + delimiter + targetRepositoryId;
		for(String replaceCharacter:replaceCharacters){
			repositoryKey = repositoryKey.replaceAll(replaceCharacter, delimiter);
		}
		return repositoryKey;
	}
	public static String getTempFileName(String someString){
		for(String replaceCharacter:replaceCharacters){
			someString = someString.replaceAll(replaceCharacter, delimiter);
		}
		return someString;
	}
}
