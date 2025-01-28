package fr.mpau_ws.bean;

/**
 * Classe modèle des requêtes timestamp pour utilisation du timer
 * 
 * @author Jonathan
 * @version 1.2 (22/01/2025)
 * @since 13/11/2017
 */
public class RequestTimestamp {

	/**
	 * Attributs
	 */

	long timestamp;

	/**
	 * Constructeurs
	 */
	public RequestTimestamp() {}

	public RequestTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Getter
	 */

	public long getTimestamp() {
		return timestamp;
	}

}
