package com.smartsheet.platform.cs.helloworld.model;

import java.util.ArrayList;

/**
 * @author kskeem
 *	A model class to represent the Sheet return by the Smartsheet API.
 */
public class Sheet {

	String id;
	String name;
	String accessLevel;

	public Sheet() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	@Override
	public String toString() {
		return "SmartSheet [id=" + id + ", name=" + name + ", accessLevel="
				+ accessLevel + "]";
	}
	
	
	/**
	 * @author kskeem
	 *	A simple class for deserializing a list of sheets.
	 */
	public static class SheetList extends ArrayList<Sheet> {
		private static final long serialVersionUID = -4862965427045760134L;
	}
}
