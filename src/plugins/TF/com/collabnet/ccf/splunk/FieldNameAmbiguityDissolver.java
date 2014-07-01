package com.collabnet.ccf.splunk;

import java.util.HashMap;

public class FieldNameAmbiguityDissolver {
	private static final char[] specialChars = { ' ', '~','!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '+', '|', '}', '{', '"', ':', '?', '>', '<', '`', '-', '=', '\\', ']', '[', '\'', ';', '/', '.',','};
	private static final String[] replaceChars={ "","_T_","_E_","_R_","_ASH_","_D_","_P_","_C_","_A_","_S_","_OP_","_CL_","_PL_","_OR_","_CB_","_OB_","_DQ_","_CLN_","_Q_","_GT_","_LT_","_QO_","-","_EQ_","_BS_","_BC_","_BO_","_AP_","_SCL_","_FS_","_DOT_","_COM_"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$ //$NON-NLS-17$ //$NON-NLS-18$ //$NON-NLS-19$ //$NON-NLS-20$ //$NON-NLS-21$ //$NON-NLS-22$ //$NON-NLS-23$ //$NON-NLS-24$ //$NON-NLS-25$ //$NON-NLS-26$ //$NON-NLS-27$ //$NON-NLS-28$ //$NON-NLS-29$ //$NON-NLS-30$ //$NON-NLS-31$ //$NON-NLS-32$
	
	/**
	 * This method has been inspired by the netbeans project
	 * @param name
	 * @return
	 */
	private static String getReplacement(char c)
	{
		for (int i=0;i<specialChars.length;i++) {
			if(specialChars[i]==c) {
				return replaceChars[i];
			}
		}
		return null;
	}

	/**
	 * This method has been inspired by the netbeans project
	 * @param name
	 * @return
	 */
	private static String makeValidElementName(String name) {
		StringBuffer elementName = new StringBuffer();
		if (name == null)
			name = ""; //$NON-NLS-1$

		name = name.trim();
		int size = name.length();
		char ncChars[] = name.toCharArray();

		int i = 0;

		for (i = 0; i < size; i++) {
			char ch = ncChars[i];
			if (((i == 0)
					&& !(Character.isJavaIdentifierStart(ch) && (ch != '$')) && !Character
					.isDigit(ch))
					|| ((i > 0) && !(Character.isJavaIdentifierPart(ch) && (ch != '$')))) {
				String replace = getReplacement(ch);
				if (replace != null) {
					elementName.insert(elementName.length(), replace);

				} else {
					elementName.insert(elementName.length(), "_Z_"); //$NON-NLS-1$
				}
			} else {
				elementName.append(ncChars[i]);
			}
		}
		if ((i > 0) && Character.isDigit(elementName.charAt(0))) {
			elementName.insert(0, "X_"); //$NON-NLS-1$
		}
		if ((i > 0) && elementName.charAt(0) == '_') {
			elementName.insert(0, "X"); //$NON-NLS-1$
		}
		return elementName.toString();
	}

	/**
	 * attribute contains the mapping from the original field name to a field
	 * name that can reused as an XML element name
	 */
	private HashMap<String, Boolean> fieldMapping = new HashMap<String, Boolean>();
	
	/**
	 * attribute contains the reverse mapping from the XML element friendly name
	 * back to the original field name
	 */
	private HashMap<String, String> reverseFieldMapping = new HashMap<String, String>();

	/**
	 * Generate XML-element-friendly name out of original field name
	 * 
	 * @param fieldName
	 *            original field name
	 * @param isFlexField
	 *            true if it is a custom field
	 * @return new XML-element-friendly name
	 */
	public String generateNewFieldName(String fieldName, boolean isFlexField) {
		String encodedName = makeValidElementName(fieldName);
		String firstEncodedName = encodedName;
		int number = 1;
		while (true) {
			// no key may show up twice
			if (fieldMapping.containsKey(encodedName)) {
					++number;
					encodedName = firstEncodedName + "_" //$NON-NLS-1$
							+ Integer.toString(number);
			} else {
				// create entry
				fieldMapping.put(encodedName, new Boolean(isFlexField));
				reverseFieldMapping.put(encodedName, fieldName);
				return encodedName;
			}
		}
	}

	/**
	 * reset the field (reverse) mapping data structures
	 */
	public void resetFieldNameMapping() {
		fieldMapping.clear();
		reverseFieldMapping.clear();
	}


}
