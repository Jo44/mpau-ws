package fr.mpau_ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modèle des workdays
 * 
 * @author Jonathan
 * @version 1.0
 * @since 11/11/2017
 */

@XmlRootElement(name = "workday")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkDay {

	/**
	 * Attributs
	 */
	private int wdId;
	private int wdUserId;
	private long wdStart;
	private long wdStop;
	private boolean wdFinished;

	/**
	 * Constructeurs
	 */
	public WorkDay() {}

	public WorkDay(int wdId, int wdUserId, long wdStart, long wdStop, boolean wdFinished) {
		this.wdId = wdId;
		this.wdUserId = wdUserId;
		this.wdStart = wdStart;
		this.wdStop = wdStop;
		this.wdFinished = wdFinished;
	}

	/**
	 * Getters / Setters
	 */
	public int getWdId() {
		return wdId;
	}

	public int getWdUserId() {
		return wdUserId;
	}

	public long getWdStart() {
		return wdStart;
	}

	public long getWdStop() {
		return wdStop;
	}

	public boolean isWdFinished() {
		return wdFinished;
	}

}
