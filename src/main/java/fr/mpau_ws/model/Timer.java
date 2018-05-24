package fr.mpau_ws.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modèle des timers
 * 
 * @author Jonathan
 * @version 1.0
 * @since 11/11/2017
 */

@XmlRootElement(name = "timer")
@XmlAccessorType(XmlAccessType.FIELD)
public class Timer {

	/**
	 * Attributs
	 */
	private int timerId;
	private WorkDay timerWorkday;
	private List<WorkPeriod> timerWorkperiodsList;

	/**
	 * Constructeurs
	 */
	public Timer() {}

	public Timer(int timerId, WorkDay timerWorkday, List<WorkPeriod> timerWorkperiodsList) {
		this.timerId = timerId;
		this.timerWorkday = timerWorkday;
		this.timerWorkperiodsList = timerWorkperiodsList;
	}

	/**
	 * Getters / Setters
	 */
	public int getTimerId() {
		return timerId;
	}

	public WorkDay getTimerWorkday() {
		return timerWorkday;
	}

	public List<WorkPeriod> getTimerWorkperiodsList() {
		return timerWorkperiodsList;
	}

}
