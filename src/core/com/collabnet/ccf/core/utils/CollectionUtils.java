package com.collabnet.ccf.core.utils;

import java.util.Collection;

public class CollectionUtils {
	@SuppressWarnings("unchecked")
	public static boolean isEmptyOrNull(Collection c){
		boolean empty = true;
		for(Object o:c){
			if(o != null){
				empty = false;
			}
		}
		return empty;
	}
}
