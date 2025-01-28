package fr.mpau_ws.bean;

/**
 * Classe modèle des workperiods
 * 
 * @author Jonathan
 * @version 1.1 (22/01/2025)
 * @since 11/11/2017
 */
public class WorkPeriod {

	/**
	 * Attributs
	 */

	private int id;
	private int wpWdId;
	private long startWp;
	private long stopWp;
	private boolean finished;

	/**
	 * Constructeurs
	 */
	public WorkPeriod() {}

	public WorkPeriod(int id, int wpWdId, long startWp, long stopWp, boolean finished) {
		this.id = id;
		this.wpWdId = wpWdId;
		this.startWp = startWp;
		this.stopWp = stopWp;
		this.finished = finished;
	}

	/**
	 * Getters
	 */

	public int getId() {
		return id;
	}

	public int getWpWdId() {
		return wpWdId;
	}

	public long getStartWp() {
		return startWp;
	}

	public long getStopWp() {
		return stopWp;
	}

	public boolean isFinished() {
		return finished;
	}

}
