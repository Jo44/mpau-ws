package fr.mpau_ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modèle des workperiods
 * 
 * @author Jonathan
 * @version 1.0
 * @since 11/11/2017
 */

@XmlRootElement(name = "workperiod")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkPeriod {

	/**
	 * Attributs
	 */
	private int wpId;
	private int wpWdId;
	private long wpStart;
	private long wpStop;
	private boolean wpFinished;

	/**
	 * Constructeurs
	 */
	public WorkPeriod() {}

	public WorkPeriod(int wpId, int wpWdId, long wpStart, long wpStop, boolean wpFinished) {
		this.wpId = wpId;
		this.wpWdId = wpWdId;
		this.wpStart = wpStart;
		this.wpStop = wpStop;
		this.wpFinished = wpFinished;
	}

	/**
	 * Getters / Setters
	 */
	public int getWpId() {
		return wpId;
	}

	public int getWpWdId() {
		return wpWdId;
	}

	public long getWpStart() {
		return wpStart;
	}

	public long getWpStop() {
		return wpStop;
	}

	public boolean isWpFinished() {
		return wpFinished;
	}

}
