package com.collabnet.ccf.core.hospital;

public final class CCFErrorCode {
	public final static int SUCCESS = 0;
	public final static int GENERIC_ARTIFACT_NOT_SCHEMA_COMPLIANT = 1;
	public final static int GENERIC_ARTIFACT_PARSING_ERROR = 2;
	public final static int EXTERNAL_SYSTEM_CONNECTION_ERROR = 3;
	public final static int MAX_CONNECTIONS_REACHED_FOR_POOL = 4;
	public final static int EXTERNAL_SYSTEM_WRITE_ERROR = 5;
	public final static int TRANSFORMER_FILE_ERROR = 6;
	public final static int TRANSFORMER_TRANSFORMATION_ERROR = 7;
}
