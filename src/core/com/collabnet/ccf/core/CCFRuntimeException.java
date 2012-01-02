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

package com.collabnet.ccf.core;

/**
 * This exception is intended for the CCF components to throw
 * when there is an application exception and it should be
 * propogated to the hospital.
 * 
 * @author Madhusuthanan Seetharam (madhusuthanan@collab.net)
 *
 */
public class CCFRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 3190879430557600332L;
	/**
	 * Constructs a CCFRuntimeException with the cause
	 * provided.
	 * 
	 * @param cause - A description of the exceptional condition
	 */
	public CCFRuntimeException(String cause){
		super(cause);
	}

	/**
	 * Constructs a CCFRuntimeException with the cause providedand
	 * root cause exception.
	 * 
	 * @param cause - A description of the exceptional condition
	 * @param exception - The root cause exception that should be 
	 * 						sent to the hospital
	 */
	public CCFRuntimeException(String cause, Throwable exception){
		super(cause, exception);
	}
}
