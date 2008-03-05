package com.collabnet.ccf.core.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author madan@collab.net
 *
 */
public class Field {

	private String name;
	private String datatype;
	private List<String> values;
	// The display name as seen on the screen
	private String displayName;
	// The datatype could be char and the editStyle could be ListCombo (eg from QC)
	private String editStyle;
	private boolean changed;

	public Field(String name, String displayName, String datatype, String editStyle, String singleValue, boolean changed) {
		super();
		this.name = name;
		this.displayName = displayName;
		this.datatype = datatype;
		this.editStyle = editStyle;
		this.changed = changed;
		this.values = new ArrayList<String>();
		values.add(singleValue);
	}
	
	public Field(String name, String displayName, String datatype, String editStyle, List<String> values, boolean changed) {
		super();
		this.name = name;
		this.displayName = displayName;
		this.datatype = datatype;
		this.editStyle = editStyle;
		this.changed = changed;
		this.values = values;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEditStyle() {
		return editStyle;
	}

	public void setEditStyle(String editStyle) {
		this.editStyle = editStyle;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

}
