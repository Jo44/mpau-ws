package fr.mpau_ws.bean;

/**
 * Classe modèle des workdays
 * 
 * @author Jonathan
 * @version 1.1 (22/01/2025)
 * @since 11/11/2017
 */
public class WorkDay {

	/**
	 * Attributs
	 */

	private int id;
	private int userWd;
	private long startWd;
	private long stopWd;
	private boolean finished;

	/**
	 * Constructeurs
	 */
	public WorkDay() {}

	public WorkDay(int id, int userWd, long startWd, long stopWd, boolean finished) {
		this.id = id;
		this.userWd = userWd;
		this.startWd = startWd;
		this.stopWd = stopWd;
		this.finished = finished;
	}

	/**
	 * Getters
	 */

	public int getId() {
		return id;
	}

	public int getUserWd() {
		return userWd;
	}

	public long getStartWd() {
		return startWd;
	}

	public long getStopWd() {
		return stopWd;
	}

	public boolean isFinished() {
		return finished;
	}

}
