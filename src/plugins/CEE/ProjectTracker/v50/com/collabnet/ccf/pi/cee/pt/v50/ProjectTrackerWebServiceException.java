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

package com.collabnet.ccf.pi.cee.pt.v50;

import com.collabnet.ccf.core.CCFRuntimeException;

public class ProjectTrackerWebServiceException extends CCFRuntimeException{

	private static final long serialVersionUID = -1677050094927451251L;
	public ProjectTrackerWebServiceException(String cause) {
		super(cause);
	}
	public ProjectTrackerWebServiceException(String cause, Throwable t) {
		super(cause, t);
		// TODO Auto-generated constructor stub
	}
}
