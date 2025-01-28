package fr.mpau_ws.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.mpau_ws.bean.RequestTimestamp;
import fr.mpau_ws.bean.Timer;
import fr.mpau_ws.bean.TimerMode;
import fr.mpau_ws.controler.TimerController;
import fr.mpau_ws.controler.UserController;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Classe service des timers [Timer = ID + Workday + List<Workperiod>]
 * 
 * @author Jonathan
 * @version 1.7 (22/01/2025)
 * @since 11/11/2017
 */
@Path("/timer")
public class TimerService {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(TimerService.class);
	private TimerController timerCtrl = new TimerController();
	private UserController userCtrl = new UserController();

	/**
	 * Constructeur
	 */
	public TimerService() {}

	/**
	 * Services
	 */

	/**
	 * Récupère le mode en fonction de l'état du timer
	 * 
	 * @param authString
	 * @return TimerMode
	 */
	// URI: /timer/mode
	@GET
	@Path("/mode")
	@Produces(MediaType.APPLICATION_JSON)
	public TimerMode serviceGetTimerMode(@HeaderParam("authorization") String authString) {
		logger.info("TimerService --> GET /mode");
		TimerMode timerMode = null;
		// Contrôle des arguments / paramètres
		userCtrl.controlAuthString(authString);
		// Service
		timerMode = timerCtrl.getTimerMode(authString);
		// Flux retour
		return timerMode;
	}

	/**
	 * Récupère le timer non terminé le plus ancien de l'utilisateur
	 * 
	 * @param authString
	 * @return Timer
	 */
	// URI: /timer/last
	@GET
	@Path("/last")
	@Produces(MediaType.APPLICATION_JSON)
	public Timer serviceGetLastTimer(@HeaderParam("authorization") String authString) {
		logger.info("TimerService --> GET /last");
		Timer timer = null;
		// Contrôle des arguments / paramètres
		userCtrl.controlAuthString(authString);
		// Service
		timer = timerCtrl.getLastTimer(authString);
		// Flux retour
		return timer;
	}

	/**
	 * Récupère tous les timers de l'utilisateur
	 * 
	 * @param authString
	 * @return List<Timer>
	 */
	// URI: /timer/all
	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Timer> serviceGetAllTimer(@HeaderParam("authorization") String authString) {
		logger.info("TimerService --> GET /all");
		List<Timer> listTimer = null;
		// Contrôle les arguments / paramètres
		userCtrl.controlAuthString(authString);
		// Service
		listTimer = timerCtrl.getAllTimers(authString);
		// Flux retour
		return listTimer;
	}

	/**
	 * Récupère tous les timers terminés de l'utilisateur du mois voulu (de 0 pour mois actuel à -12 pour même mois année précédente)
	 * 
	 * @param month
	 * @param authString
	 * @return List<Timer>
	 */
	// URI: /timer/list/{relatifMonth}
	@GET
	@Path("/list/{relatifMonth}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Timer> serviceGetTimers(@PathParam("relatifMonth") int relatifMonth, @HeaderParam("authorization") String authString) {
		logger.info("TimerService --> GET /list/" + String.valueOf(relatifMonth));
		List<Timer> listTimer = null;
		// Contrôle les arguments / paramètres
		userCtrl.controlAuthString(authString);
		timerCtrl.controlRelatifMonth(relatifMonth);
		// Service
		listTimer = timerCtrl.getTimers(authString, relatifMonth);
		// Flux retour
		return listTimer;
	}

	/**
	 * Débute un nouveau timer pour l'utilisateur
	 * 
	 * @param authString
	 * @param rqTimestamp
	 * @return Timer
	 */
	// URI: /timer/start
	@POST
	@Path("/start")
	@Produces(MediaType.APPLICATION_JSON)
	public Timer serviceStartTimer(@HeaderParam("authorization") String authString, RequestTimestamp rqTimestamp) {
		logger.info("TimerService --> POST");
		Timer startedTimer = null;
		// Contrôle les arguments / paramètres
		userCtrl.controlAuthString(authString);
		timerCtrl.controlRequestTimestamp(rqTimestamp);
		// Service
		startedTimer = timerCtrl.startTimer(authString, rqTimestamp);
		// Flux retour
		return startedTimer;
	}

	/**
	 * Met en pause le timer de l'utilisateur
	 * 
	 * @param authString
	 * @param rqTimestamp
	 * @return Timer
	 */
	// URI: /timer/pause
	@PUT
	@Path("/pause")
	@Produces(MediaType.APPLICATION_JSON)
	public Timer servicePauseTimer(@HeaderParam("authorization") String authString, RequestTimestamp rqTimestamp) {
		logger.info("TimerService --> PUT /pause");
		Timer pausedTimer = null;
		// Contrôle les arguments / paramètres
		userCtrl.controlAuthString(authString);
		timerCtrl.controlRequestTimestamp(rqTimestamp);
		// Service
		pausedTimer = timerCtrl.pauseTimer(authString, rqTimestamp);
		// Flux retour
		return pausedTimer;
	}

	/**
	 * Relance le timer de l'utilisateur
	 * 
	 * @param authString
	 * @param rqTimestamp
	 * @return Timer
	 */
	// URI: /timer/restart
	@PUT
	@Path("/restart")
	@Produces(MediaType.APPLICATION_JSON)
	public Timer serviceRestartTimer(@HeaderParam("authorization") String authString, RequestTimestamp rqTimestamp) {
		logger.info("TimerService --> PUT /restart");
		Timer restartedTimer = null;
		// Contrôle les arguments / paramètres
		userCtrl.controlAuthString(authString);
		timerCtrl.controlRequestTimestamp(rqTimestamp);
		// Service
		restartedTimer = timerCtrl.restartTimer(authString, rqTimestamp);
		// Flux retour
		return restartedTimer;
	}

	/**
	 * Termine le timer de l'utilisateur
	 * 
	 * @param authString
	 * @param rqTimestamp
	 * @return Timer
	 */
	// URI: /timer/stop
	@PUT
	@Path("/stop")
	@Produces(MediaType.APPLICATION_JSON)
	public Timer serviceStopTimer(@HeaderParam("authorization") String authString, RequestTimestamp rqTimestamp) {
		logger.info("TimerService --> PUT /stop");
		Timer stopedTimer = null;
		// Contrôle les arguments / paramètres
		userCtrl.controlAuthString(authString);
		timerCtrl.controlRequestTimestamp(rqTimestamp);
		// Service
		stopedTimer = timerCtrl.stopTimer(authString, rqTimestamp);
		// Flux retour
		return stopedTimer;
	}

	/**
	 * Supprime un timer de l'utilisateur
	 * 
	 * @param workdayId
	 * @param authString
	 * @return Timer
	 */
	// URI: /timer/{workdayId}
	@DELETE
	@Path("/{workdayId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Timer serviceDeleteTimer(@PathParam("workdayId") int workdayId, @HeaderParam("authorization") String authString) {
		logger.info("TimerService --> DELETE /" + String.valueOf(workdayId));
		Timer deletedTimer = null;
		// Contrôle les arguments / paramètres
		userCtrl.controlAuthString(authString);
		timerCtrl.controlWorkdayID(workdayId);
		// Service
		deletedTimer = timerCtrl.deleteTimer(authString, workdayId);
		// Flux retour
		return deletedTimer;
	}

}
