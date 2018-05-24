package fr.mpau_ws.model;

/**
 * Enumération des codes retours
 * 
 * @author Jonathan
 * @version 1.0
 * @since 28/01/2018
 */

public enum ErrorCodes {
	PreconditionFailed("412"), ServiceUnavailable("503");

	private String name = "";

	ErrorCodes(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}
}
