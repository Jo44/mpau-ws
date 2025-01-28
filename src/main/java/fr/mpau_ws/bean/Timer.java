package fr.mpau_ws.bean;

import java.util.List;

/**
 * Classe modèle des timers
 * 
 * @author Jonathan
 * @version 1.1 (22/01/2025)
 * @since 11/11/2017
 */
public class Timer {

	/**
	 * Attributs
	 */

	private int id;
	private WorkDay workday;
	private List<WorkPeriod> workperiodsList;

	/**
	 * Constructeurs
	 */
	public Timer() {}

	public Timer(int id, WorkDay workday, List<WorkPeriod> workperiodsList) {
		this.id = id;
		this.workday = workday;
		this.workperiodsList = workperiodsList;
	}

	/**
	 * Getters
	 */

	public int getId() {
		return id;
	}

	public WorkDay getWorkday() {
		return workday;
	}

	public List<WorkPeriod> getWorkperiodsList() {
		return workperiodsList;
	}

}
