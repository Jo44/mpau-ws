package fr.mpau_ws.controler;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.mpau_ws.bean.ErrorCodes;
import fr.mpau_ws.bean.User;
import fr.mpau_ws.dao.UserDAO;
import fr.mpau_ws.exception.FonctionnalException;
import fr.mpau_ws.exception.TechnicalException;
import jakarta.ws.rs.WebApplicationException;

/**
 * Classe de controleurs des utilisateurs
 * 
 * @author Jonathan
 * @version 1.4 (22/01/2025)
 * @since 11/11/2017
 */
public class UserController {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(UserController.class);
	private static Map<String, User> listUsers = new HashMap<String, User>();
	private UserDAO userDAO = new UserDAO();

	/**
	 * Constructeur
	 */
	public UserController() {}

	/**
	 * Méthodes de contrôles
	 */

	/**
	 * Contrôle que le string d'authentification est présent
	 * 
	 * @param authString
	 * @throws WebApplicationException
	 */
	public void controlAuthString(String authString) throws WebApplicationException {
		if (authString == null || authString.trim().isEmpty()) {
			logger.error("AuthString manquant");
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.PreconditionFailed.toString()));
		}
	}

	/**
	 * Contrôle que l'utilisateur fournis en paramètre de requête est valide
	 * 
	 * @param user
	 * @throws WebApplicationException
	 */
	public void controlUser(User user) throws WebApplicationException {
		boolean validUser = true;
		if (user != null) {
			if (user.getName() == null || user.getName().trim().isEmpty()) {
				validUser = false;
			}
			if (user.getPass() == null || user.getPass().trim().isEmpty()) {
				validUser = false;
			}
			if (user.getEmail() == null || user.getEmail().trim().isEmpty() || !isValidEmail(user.getEmail().trim())) {
				validUser = false;
			}
		} else {
			validUser = false;
		}
		if (!validUser) {
			logger.error("Utilisateur fournis non-valide");
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.PreconditionFailed.toString()));
		}
	}

	/**
	 * Vérifie si l'email est valide via Apache Commons Validator
	 * 
	 * @param email
	 * @return boolean
	 */
	private boolean isValidEmail(String email) {
		boolean valid = EmailValidator.getInstance().isValid(email);
		return valid;
	}

	/**
	 * Méthodes des services
	 */

	/**
	 * Récupère un utilisateur via le string d'authentification (disponible pour tous les services)
	 * 
	 * @param authString
	 * @return User
	 * @throws WebApplicationException
	 */
	public User getUser(String authString) throws WebApplicationException {
		// Cherche l'utilisateur dans la liste en mémoire
		User user = listUsers.get(authString);
		if (user == null) {
			// Cherche l'utilisateur dans la base de données
			user = userAuthenticated(authString);
			if (user != null) {
				// Ajout de l'utilisateur dans la liste en mémoire
				listUsers.put(authString, user);
			} else {
				logger.error("Utilisateur non reconnu");
				throw new WebApplicationException(Integer.valueOf(ErrorCodes.PreconditionFailed.toString()));
			}
		}
		return user;
	}

	/**
	 * Décode l'authString pour récupérer l'utilisateur via username / password
	 * 
	 * @param authString
	 * @return User
	 * @throws WebApplicationException
	 */
	private User userAuthenticated(String authString) throws WebApplicationException {
		User user = null;
		// Le header d'authorisation suit le format "Basic {username:password)*encode Base64*"
		// On extrait les données avant le décodage pour interrogation en BDD
		String decodedAuth = "";
		String username = "";
		String password = "";
		String authInfo = "";
		String[] authParts = null;
		byte[] bytes = null;
		try {
			authParts = authString.split("\\s+");
			authInfo = authParts[1];
			bytes = Base64.getDecoder().decode(authInfo);
			decodedAuth = new String(bytes);
			username = decodedAuth.substring(0, decodedAuth.indexOf(':'));
			password = decodedAuth.substring(decodedAuth.indexOf(':') + 1, decodedAuth.length());
			user = userDAO.getUser(username, password);
		} catch (IllegalArgumentException | IndexOutOfBoundsException | NullPointerException e) {
			logger.error("AuthString erroné");
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.PreconditionFailed.toString()));
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.ServiceUnavailable.toString()));
		}
		return user;
	}

	/**
	 * Ajoute un utilisateur via celui fournis
	 * 
	 * @param user
	 * @return User
	 * @throws WebApplicationException
	 */
	public User addUser(User user) throws WebApplicationException {
		User addedUser = null;
		try {
			// Ajout de l'utilisateur en base
			addedUser = userDAO.addUser(user);
			if (addedUser != null) {
				// Génère la string d'authentification
				String authString = encodeAuthString(user.getName().trim(), user.getPass().trim());
				// Ajout de l'utilisateur dans la liste en mémoire
				listUsers.put(authString, addedUser);
			} else {
				logger.error("Impossible d'ajouter l'utilisateur");
				throw new WebApplicationException(Integer.valueOf(ErrorCodes.PreconditionFailed.toString()));
			}
		} catch (FonctionnalException fex) {
			logger.error(fex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.PreconditionFailed.toString()));
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.ServiceUnavailable.toString()));
		}
		return addedUser;
	}

	/**
	 * Met à jour l'utilisateur via le string d'authentification et l'utilisateur fournis
	 * 
	 * @param authString
	 * @param user
	 * @return User
	 * @throws WebApplicationException
	 */
	public User updateUser(String authString, User user) throws WebApplicationException {
		User updatedUser = null;
		try {
			// Récupère l'utilisateur
			User currentUser = getUser(authString);
			// Met à jour l'utilisateur
			updatedUser = userDAO.updateUser(user, currentUser.getId());
			if (updatedUser != null) {
				// Si ajout effectué en base, enleve l'utilisateur de la liste en mémoire (car string d'authentification peut être
				// modifié), génère le nouveau string, puis ajout dans la liste en mémoire
				listUsers.remove(authString);
				String newAuthString = encodeAuthString(user.getName(), user.getPass());
				listUsers.put(newAuthString, updatedUser);
			} else {
				logger.error("Impossible de modifier l'utilisateur");
				throw new WebApplicationException(Integer.valueOf(ErrorCodes.PreconditionFailed.toString()));
			}
		} catch (FonctionnalException fex) {
			logger.error(fex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.PreconditionFailed.toString()));
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.ServiceUnavailable.toString()));
		}
		return updatedUser;
	}

	/**
	 * Retourne le string d'authentification encodé via le nom et le mot de passe
	 * 
	 * @param userName
	 * @param userPass
	 * @return String
	 */
	private String encodeAuthString(String userName, String userPass) {
		// Encode Base64 {userName:userPass}
		String authString = null;
		String uncodeAuthString = userName.trim() + ":" + userPass.trim();
		byte[] encodedBytes = Base64.getEncoder().encode(uncodeAuthString.getBytes());
		authString = encodedBytes.toString();
		return authString;
	}

	/**
	 * Met à jour l'utilisateur (nombre d'interventions et l'ID de l'intervention max) puis met à jour la liste en mémoire
	 * 
	 * @param authString
	 * @param user
	 */
	public void updateUserInter(String authString, User user) {
		try {
			userDAO.updateUserInter(user.getId());
			listUsers.remove(authString);
			getUser(authString);
		} catch (TechnicalException tex) {
			logger.error(tex.getMessage());
			throw new WebApplicationException(Integer.valueOf(ErrorCodes.ServiceUnavailable.toString()));
		}
	}

	/**
	 * Setter
	 */

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
