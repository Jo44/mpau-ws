package fr.mpau_ws.exception;

/**
 * Classe d'exception fonctionnelle
 * 
 * @author Jonathan
 * @version 1.0
 * @since 28/01/2018
 */

public class FonctionnalException extends Exception {

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
	public FonctionnalException(String message) {
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
