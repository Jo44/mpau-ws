package fr.mpau_ws.bean;

/**
 * Classe modèle du mode du timer
 * 
 * @author Jonathan
 * @version 1.2 (22/01/2025)
 * @since 20/11/2017
 */
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
	 * Getters
	 */

	public String getMode() {
		return mode;
	}

	public long getLastTimestamp() {
		return lastTimestamp;
	}

}
