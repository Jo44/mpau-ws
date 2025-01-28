package fr.mpau_ws.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.mpau_ws.bean.User;
import fr.mpau_ws.controler.UserController;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Classe service des utilisateurs
 * 
 * @author Jonathan
 * @version 1.4 (22/01/2025)
 * @since 11/11/2017
 */
@Path("/user")
public class UserService {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(UserService.class);
	private UserController userCtrl = new UserController();

	/**
	 * Constructeur
	 */
	public UserService() {}

	/**
	 * Services
	 */

	/**
	 * Récupère un utilisateur (via Header Param 'Autorization')
	 * 
	 * @param authString
	 * @return User
	 */
	// URI: /user
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public User serviceGetUser(@HeaderParam("authorization") String authString) {
		User user = null;
		// Contrôle des arguments / paramètres
		userCtrl.controlAuthString(authString);
		// Service
		user = userCtrl.getUser(authString);
		// Flux retour
		user.hidePass();
		return user;
	}

	/**
	 * Ajoute un nouvel utilisateur
	 * 
	 * @param user
	 * @return User
	 */
	// URI: /user
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public User serviceAddUser(User user) {
		logger.info("UserService --> POST");
		User addedUser = null;
		// Contrôle des arguments / paramètres
		userCtrl.controlUser(user);
		// Service
		addedUser = userCtrl.addUser(user);
		// Flux retour
		addedUser.hidePass();
		return addedUser;
	}

	/**
	 * Met à jour l'utilisateur
	 * 
	 * @param user
	 * @return User
	 */
	// URI: /user
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public User serviceUpdateUser(@HeaderParam("authorization") String authString, User user) {
		logger.info("UserService --> PUT");
		User updatedUser = null;
		// Contrôle des arguments / paramètres
		userCtrl.controlAuthString(authString);
		userCtrl.controlUser(user);
		// Service
		updatedUser = userCtrl.updateUser(authString, user);
		// Flux retour
		updatedUser.hidePass();
		return updatedUser;
	}

}
