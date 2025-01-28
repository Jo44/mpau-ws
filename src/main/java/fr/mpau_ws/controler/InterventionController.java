package fr.mpau_ws.controler;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.mpau_ws.bean.ErrorCodes;
import fr.mpau_ws.bean.Intervention;
import fr.mpau_ws.bean.User;
import fr.mpau_ws.dao.InterventionDAO;
import fr.mpau_ws.exception.FonctionnalException;
import fr.mpau_ws.exception.TechnicalException;
import jakarta.ws.rs.WebApplicationException;

/**
 * Classe de controleurs des interventions
 * 
 * @author Jonathan
 * @version 1.6 (22/01/2025)
 * @since 11/11/2017
 */
public class InterventionController {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(InterventionController.class);
	private UserController userCtrl = new UserController();
	private InterventionDAO interDAO = new InterventionDAO();

	/**
	 * Constructeur
	 */
	public InterventionController() {}

	/**
	 * Méthodes de contrôles
	 */

	/**
	 * Contrôle que l'ID de l'intervention est valide
	 * 
	 * @param interId
	 */
	public void controlInterID(int interId) {
		if (interId <= 0) {
			logger.error("InterID manquant (ou <= 0)");
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.PreconditionFailed.toString()));
		}
	}

	/**
	 * Contrôle que le nombre d'intervention est valide
	 * 
	 * @param nbInter
	 */
	public void controlNbInter(int nbInter) {
		if (nbInter <= 0) {
			logger.error("NbInter manquant (ou <= 0)");
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.PreconditionFailed.toString()));
		}
	}

	/**
	 * Contrôle que l'intervention est valide
	 * 
	 * @param inter
	 */
	public void controlIntervention(Intervention inter) {
		boolean validInter = true;
		if (inter != null) {
			if (inter.getDateInter() == 0) {
				validInter = false;
			}
			if (inter.getDuree() < 1) {
				validInter = false;
			}
			if (inter.getSecteur() == null || inter.getSecteur().trim().isEmpty()) {
				validInter = false;
			}
			if (inter.getMainTypeId() < 1 || inter.getMainTypeId() > 2) {
				validInter = false;
			}
			if (inter.getSousTypeId() < 1 || inter.getSousTypeId() > 8) {
				validInter = false;
			}
			if (inter.getAgePatientId() < 1 || inter.getAgePatientId() > 11) {
				validInter = false;
			}
		} else {
			validInter = false;
		}
		if (!validInter) {
			logger.error("Intervention fournis non-valide");
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.PreconditionFailed.toString()));
		}
	}

	/**
	 * Méthodes des services
	 */

	/**
	 * Récupère la liste des interventions de l'utilisateur selon le sens de sélection, l'ID de l'intervention de départ et le nombre d'intervention
	 * 
	 * @param authString
	 * @param next
	 * @param interId
	 * @param nbInter
	 * @return List<Intervention>
	 */
	public List<Intervention> getInterventions(String authString, boolean next, int interId, int nbInter) {
		List<Intervention> listInter = null;
		// Récupère l'utilisateur
		User user = userCtrl.getUser(authString);
		// Récupère la liste des interventions
		try {
			listInter = interDAO.getInterventions(interId, nbInter, next, user.getId());
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.ServiceUnavailable.toString()));
		}
		return listInter;
	}

	/**
	 * Récupère l'intervention de l'utilisateur selon son ID
	 * 
	 * @param authString
	 * @param interId
	 * @return Intervention
	 */
	public Intervention getIntervention(String authString, int interId) {
		Intervention inter = null;
		// Récupère l'utilisateur
		User user = userCtrl.getUser(authString);
		// Récupère l'intervention
		try {
			inter = interDAO.getIntervention(interId, user.getId());
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.ServiceUnavailable.toString()));
		}
		return inter;
	}

	/**
	 * Récupère l'utilisateur, ajoute l'intervention, met à jour l'utilisateur
	 * 
	 * @param authString
	 * @param inter
	 * @return Intervention
	 */
	public Intervention addIntervention(String authString, Intervention inter) {
		Intervention addedInter = null;
		// Récupère l'utilisateur
		User user = userCtrl.getUser(authString);
		// Ajoute l'intervention et met à jour l'utilisateur
		try {
			addedInter = interDAO.addIntervention(inter, user.getId());
			if (addedInter != null) {
				userCtrl.updateUserInter(authString, user);
			}
		} catch (FonctionnalException fex) {
			logger.error(fex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.PreconditionFailed.toString()));
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.ServiceUnavailable.toString()));
		}
		return addedInter;
	}

	/**
	 * Met à jour une intervention selon son ID et l'objet Intervention
	 * 
	 * @param authString
	 * @param interId
	 * @param inter
	 * @return Intervention
	 */
	public Intervention updateIntervention(String authString, int interId, Intervention inter) {
		Intervention updatedInter = null;
		// Récupère l'utilisateur
		User user = userCtrl.getUser(authString);
		// Met à jour l'intervention
		try {
			updatedInter = interDAO.updateIntervention(inter, interId, user.getId());
		} catch (FonctionnalException fex) {
			logger.error(fex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.PreconditionFailed.toString()));
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.ServiceUnavailable.toString()));
		}
		return updatedInter;
	}

	/**
	 * Récupère l'utilisateur, supprimer l'intervention et met à jour l'utilisateur
	 * 
	 * @param authString
	 * @param interId
	 * @return Intervention
	 */
	public Intervention deleteIntervention(String authString, int interId) {
		Intervention deletedInter = null;
		// Récupère l'utilisateur
		User user = userCtrl.getUser(authString);
		// Supprime l'intervention et met à jour l'utilisateur
		try {
			deletedInter = interDAO.deleteIntervention(interId, user.getId());
			if (deletedInter != null) {
				userCtrl.updateUserInter(authString, user);
			}
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.ServiceUnavailable.toString()));
		}
		return deletedInter;
	}

	/**
	 * Setters
	 */

	public void setUserController(UserController userCtrl) {
		this.userCtrl = userCtrl;
	}

	public void setInterventionDAO(InterventionDAO interDAO) {
		this.interDAO = interDAO;
	}

}
