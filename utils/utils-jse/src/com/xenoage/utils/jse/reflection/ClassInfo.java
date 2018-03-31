package com.xenoage.utils.jse.reflection;

public class ClassInfo {
	public String name;
	public String parent;
	public String[] interfaces;

	public ClassInfo(String name, String parent, String... interfaces) {
		this.name = name;
		this.parent = parent;
		this.interfaces = interfaces;
	}
}
