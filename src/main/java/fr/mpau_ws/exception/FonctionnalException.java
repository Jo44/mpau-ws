package fr.mpau_ws.exception;

/**
 * Classe d'exception fonctionnelle
 * 
 * @author Jonathan
 * @version 1.0 (22/01/2025)
 * @since 22/01/2025
 */
public class FonctionnalException extends Exception {

	/**
	 * Attributs
	 */

	private static final long serialVersionUID = 8443371910404153844L;
	private String message;

	/**
	 * Constructeur
	 */
	public FonctionnalException(String message) {
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
