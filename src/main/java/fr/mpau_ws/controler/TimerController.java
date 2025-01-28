package fr.mpau_ws.controler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.mpau_ws.bean.ErrorCodes;
import fr.mpau_ws.bean.RequestTimestamp;
import fr.mpau_ws.bean.Timer;
import fr.mpau_ws.bean.TimerMode;
import fr.mpau_ws.bean.User;
import fr.mpau_ws.bean.WorkDay;
import fr.mpau_ws.bean.WorkPeriod;
import fr.mpau_ws.dao.WorkDayDAO;
import fr.mpau_ws.dao.WorkPeriodDAO;
import fr.mpau_ws.exception.TechnicalException;
import jakarta.ws.rs.WebApplicationException;

/**
 * Classe de controleurs des timers [Timer = ID + Workday + List<Workperiod>]
 * 
 * @author Jonathan
 * @version 1.7 (22/01/2025)
 * @since 11/11/2017
 */
public class TimerController {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(TimerController.class);
	private UserController userCtrl = new UserController();
	private WorkDayDAO wdDAO = new WorkDayDAO();
	private WorkPeriodDAO wpDAO = new WorkPeriodDAO();

	/**
	 * Constructeur
	 */
	public TimerController() {}

	/**
	 * Méthodes de contrôles
	 */

	/**
	 * Contrôle que le timestamp fournis est valide
	 * 
	 * @param rqTimestamp
	 */
	public void controlRequestTimestamp(RequestTimestamp rqTimestamp) {
		if (rqTimestamp == null || rqTimestamp.getTimestamp() <= 0) {
			logger.error("RequestTimestamp manquant (ou <= 0)");
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.PreconditionFailed.toString()));
		}
	}

	/**
	 * Contrôle que l'ID de la Workday est valide
	 * 
	 * @param workdayId
	 */
	public void controlWorkdayID(int workdayId) {
		if (workdayId <= 0) {
			logger.error("WorkdayID manquant (ou <= 0)");
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.PreconditionFailed.toString()));
		}
	}

	/**
	 * Contrôle que le mois demandé est possible (de 0 pour mois actuel à -12 pour mois antérieur max)
	 * 
	 * @param relatifMonth
	 */
	public void controlRelatifMonth(int relatifMonth) {
		if (relatifMonth > 0 || relatifMonth < -12) {
			logger.error("RelatifMonth incorrect (> 0 ou < -12)");
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.PreconditionFailed.toString()));
		}
	}

	/**
	 * Méthodes des services
	 */

	/**
	 * Récupère l'utilisateur, puis la Workday non terminée, puis les Workperiods de la Workday pour déterminer le TimerMode (mode + timestamp
	 * dernière action si Pause/Restart)
	 * 
	 * @param authString
	 * @return TimerMode
	 */
	public TimerMode getTimerMode(String authString) {
		TimerMode timerMode = null;
		String mode = "UNKNOWN";
		long lastTimestamp = 0;
		// Récupère le dernier timer
		Timer timer = getLastTimer(authString);
		// Détermine le mode :
		// si timer null -> Pas de Workday en cours -> Mode START
		// si timer non null -> Récupère la liste des Workperiods, et vérifie si la dernière est clôturée
		// si non clôturée -> Mode PAUSE + récupère le timestamp de début de la Workperiod
		// si clôturée -> Mode RESTART + récupère le timestamp de fin de la Workperiod
		if (timer != null) {
			if (timer.getWorkperiodsList() != null && timer.getWorkperiodsList().size() > 0) {
				List<WorkPeriod> listWp = timer.getWorkperiodsList();
				if (!listWp.get(listWp.size() - 1).isFinished()) {
					mode = "PAUSE";
					lastTimestamp = listWp.get(listWp.size() - 1).getStartWp();
				} else {
					mode = "RESTART";
					lastTimestamp = listWp.get(listWp.size() - 1).getStopWp();
				}
			}
		} else {
			mode = "START";
		}
		timerMode = new TimerMode(mode, lastTimestamp);
		return timerMode;
	}

	/**
	 * Récupère l'utilisateur, puis la Workday non terminée, puis les Workperiods de la Workday et renvoi le timer
	 * 
	 * @param authString
	 * @return Timer
	 */
	public Timer getLastTimer(String authString) {
		Timer lastTimer = null;
		// Récupère l'utilisateur
		User user = userCtrl.getUser(authString);
		// Récupère la Workday non terminée, puis ses Workperiods et renvoi le tout sous forme de Timer
		try {
			WorkDay wd = wdDAO.getWorkDayNotCompleted(user.getId());
			if (wd != null) {
				List<WorkPeriod> listWp = wpDAO.getAllWorkPeriods(wd.getId());
				lastTimer = new Timer(0, wd, listWp);
			}
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.ServiceUnavailable.toString()));
		}
		return lastTimer;
	}

	/**
	 * Récupère l'utilisateur, puis la liste de toutes les Workdays, puis la liste de leurs propres Workperiods et renvoi le tout sous forme de
	 * List<Timer>
	 * 
	 * @param authString
	 * @return List<Timer>
	 */
	public List<Timer> getAllTimers(String authString) {
		List<Timer> listTimer = null;
		// Récupère l'utilisateur
		User user = userCtrl.getUser(authString);
		// Récupère la liste des Workdays, puis leurs propres Workperiods et renvoi le tout sous forme de List<Timer>
		try {
			List<WorkDay> listWorkDays = wdDAO.getAllWorkDays(user.getId());
			if (listWorkDays != null && listWorkDays.size() > 0) {
				List<WorkPeriod> listWorkPeriods = null;
				listTimer = new ArrayList<Timer>();
				int increment = 0;
				for (WorkDay workDay : listWorkDays) {
					listWorkPeriods = wpDAO.getAllWorkPeriods(workDay.getId());
					Timer timer = new Timer(increment++, workDay, listWorkPeriods);
					listTimer.add(timer);
				}
			}
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.ServiceUnavailable.toString()));
		}
		return listTimer;
	}

	/**
	 * Récupère l'utilisateur, puis la liste de toutes les Workdays terminées du mois demandé, puis la liste de leurs propres Workperiods et renvoi le
	 * tout sous forme de List<Timer>
	 * 
	 * @param authString
	 * @param relatifMonth
	 * @return List<Timer>
	 */
	public List<Timer> getTimers(String authString, int relatifMonth) {
		List<Timer> listTimer = null;
		// Récupère l'utilisateur
		User user = userCtrl.getUser(authString);
		// Détermine le mois demandé en fonction du relatifMonth (exemple : -5 -> 5 mois antérieur à la date actuelle)
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, relatifMonth);
		int month = now.get(Calendar.MONTH) + 1;
		int year = now.get(Calendar.YEAR);
		// Récupère la liste des Workdays terminées du mois demandé, puis leurs propres Workperiods et renvoi le tout sous forme
		// de List<Timer>
		try {
			List<WorkDay> listWorkDays = wdDAO.getAllWorkDaysForMonth(user.getId(), month, year);
			if (listWorkDays != null && listWorkDays.size() > 0) {
				List<WorkPeriod> listWorkPeriods = null;
				listTimer = new ArrayList<Timer>();
				int increment = 0;
				for (WorkDay workDay : listWorkDays) {
					listWorkPeriods = wpDAO.getAllWorkPeriods(workDay.getId());
					Timer timer = new Timer(increment++, workDay, listWorkPeriods);
					listTimer.add(timer);
				}
			}
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.ServiceUnavailable.toString()));
		}
		return listTimer;
	}

	/**
	 * Récupère l'utilisateur, débute une nouvelle Workday, puis une nouvelle Workperiod et renvoi le timer
	 * 
	 * @param authString
	 * @param rqTimestamp
	 * @return Timer
	 */
	public Timer startTimer(String authString, RequestTimestamp rqTimestamp) {
		Timer addedTimer = null;
		// Récupère l'utilisateur
		User user = userCtrl.getUser(authString);
		// Vérifie qu'une Workday non terminée n'existe pas, débute une nouvelle Workday, récupère la Workday, puis débute
		// une nouvelle Workperiod et la récupère pour renvoyer un Timer
		try {
			WorkDay wd = wdDAO.getWorkDayNotCompleted(user.getId());
			if (wd == null) {
				wdDAO.startWorkDay(rqTimestamp.getTimestamp(), user.getId());
				wd = wdDAO.getWorkDayNotCompleted(user.getId());
				if (wd != null) {
					wpDAO.startWorkPeriod(rqTimestamp.getTimestamp(), wd.getId());
					WorkPeriod wp = wpDAO.getWorkPeriodNotCompleted(wd.getId());
					List<WorkPeriod> listWp = new ArrayList<>();
					listWp.add(wp);
					addedTimer = new Timer(0, wd, listWp);
				}
			}
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.ServiceUnavailable.toString()));
		}
		return addedTimer;
	}

	/**
	 * Récupère l'utilisateur, puis la Workday non terminée, puis clôture la Workperiod non terminée et renvoi le timer
	 * 
	 * @param authString
	 * @param rqTimestamp
	 * @return Timer
	 */
	public Timer pauseTimer(String authString, RequestTimestamp rqTimestamp) {
		Timer pausedTimer = null;
		// Récupère l'utilisateur
		User user = userCtrl.getUser(authString);
		// Récupère la Workday non terminée, récupère la Workperiod non terminée de la Workday, clôture la Workperiod et la
		// récupère pour renvoyer un Timer
		try {
			WorkDay wd = wdDAO.getWorkDayNotCompleted(user.getId());
			if (wd != null) {
				WorkPeriod wp = wpDAO.getWorkPeriodNotCompleted(wd.getId());
				if (wp != null) {
					wpDAO.stopWorkPeriod(rqTimestamp.getTimestamp(), wp.getId());
					// Met à jour de la workperiod pour retour
					wp = new WorkPeriod(wp.getId(), wp.getWpWdId(), wp.getStartWp(), rqTimestamp.getTimestamp(), true);
					List<WorkPeriod> listWp = new ArrayList<>();
					listWp.add(wp);
					pausedTimer = new Timer(0, wd, listWp);
				}
			}
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.ServiceUnavailable.toString()));
		}
		return pausedTimer;
	}

	/**
	 * Récupère l'utilisateur, puis la Workday non terminée, débute une nouvelle Workperiod et renvoi le timer
	 * 
	 * @param authString
	 * @param rqTimestamp
	 * @return Timer
	 */
	public Timer restartTimer(String authString, RequestTimestamp rqTimestamp) {
		Timer restartedTimer = null;
		// Récupère l'utilisateur
		User user = userCtrl.getUser(authString);
		// Récupère la Workday non terminée, vérifie qu'une Workperiod non terminée n'existe pas, puis débute une nouvelle
		// Workperiod et la récupère pour renvoyer un timer
		try {
			WorkDay wd = wdDAO.getWorkDayNotCompleted(user.getId());
			if (wd != null) {
				WorkPeriod wp = wpDAO.getWorkPeriodNotCompleted(wd.getId());
				if (wp == null) {
					wpDAO.startWorkPeriod(rqTimestamp.getTimestamp(), wd.getId());
					wp = wpDAO.getWorkPeriodNotCompleted(wd.getId());
					List<WorkPeriod> listWp = new ArrayList<>();
					listWp.add(wp);
					restartedTimer = new Timer(0, wd, listWp);
				}
			}
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.ServiceUnavailable.toString()));
		}
		return restartedTimer;
	}

	/**
	 * Récupère l'utilisateur, puis la Workday non terminée, clôture la Workperiod non terminée, puis clôture la Workperiod non terminée et renvoi le
	 * timer
	 * 
	 * @param authString
	 * @param rqTimestamp
	 * @return Timer
	 */
	public Timer stopTimer(String authString, RequestTimestamp rqTimestamp) {
		Timer stopedTimer = null;
		// Récupère l'utilisateur
		User user = userCtrl.getUser(authString);
		// Récupère la Workday non terminée, récupère la Workperiod non terminée, puis clôture la Workperiod si besoin, puis
		// clôture la Workday et renvoi le timer
		try {
			WorkDay wd = wdDAO.getWorkDayNotCompleted(user.getId());
			if (wd != null) {
				List<WorkPeriod> listWp = null;
				WorkPeriod wp = wpDAO.getWorkPeriodNotCompleted(wd.getId());
				if (wp != null) {
					wpDAO.stopWorkPeriod(rqTimestamp.getTimestamp(), wp.getId());
					// Met à jour de la workperiod pour retour
					wp = new WorkPeriod(wp.getId(), wp.getWpWdId(), wp.getStartWp(), rqTimestamp.getTimestamp(), true);
					listWp = new ArrayList<>();
					listWp.add(wp);
				}
				wdDAO.stopWorkDay(rqTimestamp.getTimestamp(), wd.getId());
				// Met à jour de la workday pour retour
				wd = new WorkDay(wd.getId(), wd.getUserWd(), wd.getStartWd(), rqTimestamp.getTimestamp(), true);
				stopedTimer = new Timer(0, wd, listWp);
			}
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.ServiceUnavailable.toString()));
		}
		return stopedTimer;
	}

	/**
	 * Récupère l'utilisateur, puis supprime la Workday, puis supprime les Workperiods si besoin et renvoi le timer
	 * 
	 * @param authString
	 * @param workdayId
	 * @return Timer
	 */
	public Timer deleteTimer(String authString, int workdayId) {
		Timer deletedTimer = null;
		// Récupère l'utilisateur
		User user = userCtrl.getUser(authString);
		// Supprime la Workday, puis supprime ses Workperiods et renvoi le timer
		try {
			WorkDay wd = wdDAO.deleteWorkDay(workdayId, user.getId());
			if (wd != null) {
				List<WorkPeriod> listWp = wpDAO.deleteWorkPeriods(workdayId);
				deletedTimer = new Timer(0, wd, listWp);
			}
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.ServiceUnavailable.toString()));
		}
		return deletedTimer;
	}

	/**
	 * Setters
	 */

	public void setUserController(UserController userCtrl) {
		this.userCtrl = userCtrl;
	}

	public void setWorkDayDAO(WorkDayDAO wdDAO) {
		this.wdDAO = wdDAO;
	}

	public void setWorkPeriod(WorkPeriodDAO wpDAO) {
		this.wpDAO = wpDAO;
	}

}
