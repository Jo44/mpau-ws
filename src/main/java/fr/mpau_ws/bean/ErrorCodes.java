package fr.mpau_ws.bean;

/**
 * Enumération des codes retours
 * 
 * @author Jonathan
 * @version 1.0 (22/01/2025)
 * @since 22/01/2025
 */
public enum ErrorCodes {
	PreconditionFailed("412"), ServiceUnavailable("503");

	/**
	 * Attributs
	 */

	private String name = "";

	/**
	 * Constructeur
	 */
	ErrorCodes(String name) {
		this.name = name;
	}

	/**
	 * To String
	 */
	public String toString() {
		return name;
	}

}
