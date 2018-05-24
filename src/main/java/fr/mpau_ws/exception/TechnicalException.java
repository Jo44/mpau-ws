package fr.mpau_ws.exception;

/**
 * Classe d'exception technique
 * 
 * @author Jonathan
 * @version 1.0
 * @since 28/01/2018
 */

public class TechnicalException extends Exception {

	/**
	 * Attributs
	 */
	private static final long serialVersionUID = 8443371910404153844L;
	private String message;

	/**
	 * Constructeur
	 * 
	 * @param message
	 */
	public TechnicalException(String message) {
		this.message = message;
	}

	/**
	 * Getter / Setter
	 */
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
