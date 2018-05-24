package fr.mpau_ws.service;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.mpau_ws.controler.UserController;
import fr.mpau_ws.model.User;

/**
 * Classe service des utilisateurs
 * 
 * @author Jonathan
 * @version 1.3 (14/02/2018)
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
