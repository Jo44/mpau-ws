package fr.mpau_ws.exception;

/**
 * Classe d'exception technique
 * 
 * @author Jonathan
 * @version 1.0 (22/01/2025)
 * @since 22/01/2025
 */
public class TechnicalException extends Exception {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 8443371910404153844L;
	private String message;

	/**
	 * Constructeur
	 */
	public TechnicalException(String message) {
		this.message = message;
	}

	/**
	 * Getter/Setter
	 */

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
