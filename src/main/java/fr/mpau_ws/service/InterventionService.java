package fr.mpau_ws.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.mpau_ws.bean.Intervention;
import fr.mpau_ws.controler.InterventionController;
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
 * Classe service des interventions
 * 
 * @author Jonathan
 * @version 1.6 (22/01/2025)
 * @since 11/11/2017
 */
@Path("/intervention")
public class InterventionService {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(InterventionService.class);
	private InterventionController interCtrl = new InterventionController();
	private UserController userCtrl = new UserController();

	/**
	 * Constructeur
	 */
	public InterventionService() {}

	/**
	 * Services
	 */

	/**
	 * Récupère la liste des interventions de l'utilisateur
	 * 
	 * @param next
	 * @param interId
	 * @param nbInter
	 * @param authString
	 * @return List<Intervention>
	 */
	// URI: /intervention/list/{next}/from/{interId}/by/{nbInter}
	@GET
	@Path("/list/{next}/from/{interId}/by/{nbInter}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Intervention> serviceGetInterventions(@PathParam("next") boolean next, @PathParam("interId") int interId,
			@PathParam("nbInter") int nbInter, @HeaderParam("authorization") String authString) {
		logger.info(
				"InterventionService --> GET /list/" + String.valueOf(next) + "/from/" + String.valueOf(interId) + "/by/" + String.valueOf(nbInter));
		List<Intervention> listInter = null;
		// Contrôle des arguments / paramètres
		userCtrl.controlAuthString(authString);
		interCtrl.controlInterID(interId);
		interCtrl.controlNbInter(nbInter);
		// Service
		listInter = interCtrl.getInterventions(authString, next, interId, nbInter);
		// Flux retour
		return listInter;
	}

	/**
	 * Récupère l'intervention de l'utilisateur
	 * 
	 * @param interId
	 * @param authString
	 * @return Intervention
	 */
	// URI: /intervention/{interId}
	@GET
	@Path("/{interId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Intervention serviceGetIntervention(@PathParam("interId") int interId, @HeaderParam("authorization") String authString) {
		logger.info("InterventionService --> GET /" + String.valueOf(interId));
		Intervention inter = null;
		// Contrôle des arguments / paramètres
		userCtrl.controlAuthString(authString);
		interCtrl.controlInterID(interId);
		// Service
		inter = interCtrl.getIntervention(authString, interId);
		// Flux retour
		return inter;
	}

	/**
	 * Ajoute une intervention à l'utilisateur
	 * 
	 * @param authString
	 * @param inter
	 * @return Intervention
	 */
	// URI: /intervention
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Intervention serviceAddIntervention(@HeaderParam("authorization") String authString, Intervention inter) {
		logger.info("InterventionService --> POST");
		Intervention addedInter = null;
		// Contrôle des arguments / paramètres
		userCtrl.controlAuthString(authString);
		interCtrl.controlIntervention(inter);
		// Service
		addedInter = interCtrl.addIntervention(authString, inter);
		// Flux retour
		return addedInter;
	}

	/**
	 * Met à jour une intervention de l'utilisateur
	 * 
	 * @param interId
	 * @param authString
	 * @param inter
	 * @return Intervention
	 */
	// URI: /intervention/{interId}
	@PUT
	@Path("/{interId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Intervention serviceUpdateIntervention(@PathParam("interId") int interId, @HeaderParam("authorization") String authString,
			Intervention inter) {
		logger.info("InterventionService --> PUT /" + String.valueOf(interId));
		Intervention updatedInter = null;
		// Contrôle des arguments / paramètres
		userCtrl.controlAuthString(authString);
		interCtrl.controlInterID(interId);
		interCtrl.controlIntervention(inter);
		// Service
		updatedInter = interCtrl.updateIntervention(authString, interId, inter);
		// Flux retour
		return updatedInter;
	}

	/**
	 * Supprime une intervention de l'utilisateur
	 * 
	 * @param interId
	 * @param authString
	 * @return Intervention
	 */
	// URI: /intervention/{interId}
	@DELETE
	@Path("/{interId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Intervention serviceDeleteIntervention(@PathParam("interId") int interId, @HeaderParam("authorization") String authString) {
		logger.info("InterventionService --> DELETE /" + String.valueOf(interId));
		Intervention deletedInter = null;
		// Contrôle des arguments / paramètres
		userCtrl.controlAuthString(authString);
		interCtrl.controlInterID(interId);
		// Service
		deletedInter = interCtrl.deleteIntervention(authString, interId);
		// Flux retour
		return deletedInter;
	}

}
