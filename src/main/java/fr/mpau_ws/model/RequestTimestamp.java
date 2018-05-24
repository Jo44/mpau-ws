package fr.mpau_ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modèle des requêtes timestamp pour utilisation du timer
 * 
 * @author Jonathan
 * @version 1.0
 * @since 13/11/2017
 */

@XmlRootElement(name = "timestamp")
@XmlAccessorType(XmlAccessType.FIELD)
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
	 * Getters / Setters
	 */
	public long getTimestamp() {
		return timestamp;
	}

}
