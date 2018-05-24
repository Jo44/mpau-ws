package fr.mpau_ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modèle du mode du timer
 * 
 * @author Jonathan
 * @version 1.1 (23/11/2017)
 * @since 20/11/2017
 */

@XmlRootElement(name = "timerMode")
@XmlAccessorType(XmlAccessType.FIELD)
public class TimerMode {

	/**
	 * Attributs
	 */
	private String mode;
	private long lastTimestamp;

	/**
	 * Constructeurs
	 */
	public TimerMode() {}

	public TimerMode(String mode, long lastTimestamp) {
		this.mode = mode;
		this.lastTimestamp = lastTimestamp;
	}

	/**
	 * Getters / Setters
	 */
	public String getMode() {
		return mode;
	}

	public long getLastTimestamp() {
		return lastTimestamp;
	}

}
