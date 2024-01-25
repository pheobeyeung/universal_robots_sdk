package com.ur.urcap.examples.pickorplaceswing.pickorplace;

public enum TemplateType {

	EMPTY("Pick or Place"), PICK("Pick"), PLACE("Place");

	private final String name;

	TemplateType(final String name) {
		this.name = name;
	}

	public final String getName() {
		return name;
	}

	public static TemplateType valueOfByName(String name) {
		if (PICK.name.equals(name)) {
			return PICK;
		} else if (PLACE.name.equals(name)) {
			return PLACE;
		}
		return EMPTY;
	}
}