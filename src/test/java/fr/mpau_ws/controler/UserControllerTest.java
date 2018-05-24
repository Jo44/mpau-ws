package fr.mpau_ws.controler;

import static org.junit.Assert.assertTrue;

import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import fr.mpau_ws.dao.UserDAO;
import fr.mpau_ws.exception.FonctionnalException;
import fr.mpau_ws.exception.TechnicalException;
import fr.mpau_ws.model.User;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
	private static final Logger logger = LogManager.getLogger(UserControllerTest.class);

	/**
	 * Attributs
	 */
	final private static String userName = "Name";
	final private static String userPass = "Pass";
	final private static String userEmail = "name@domain.com";
	final private User fakeUser = new User(0, userName, userPass, userEmail, 0, 0, 0);
	private static UserController userCtrl = new UserController();

	/**
	 * Initialisation
	 */
	@BeforeClass
	public static void initialize() {
		logger.debug("### Initialisation de UserControllerTest ###");
	}

	/**
	 * Test le contrôle du string d'authentification valide
	 */
	@Test
	public void controlAuthStringOK() {
		boolean valid = true;
		String authString = "OK";
		try {
			userCtrl.controlAuthString(authString);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(valid);
	}

	/**
	 * Test le contrôle du string d'authentification non-valide (vide)
	 */
	@Test
	public void controlAuthStringEmpty() {
		boolean empty = false;
		String authString = "";
		try {
			userCtrl.controlAuthString(authString);
		} catch (WebApplicationException wae) {
			empty = true;
		}
		assertTrue(empty);
	}

	/**
	 * Test le contrôle du string d'authentification non-valide (Null)
	 */
	@Test
	public void controlAuthStringNullParameter() {
		boolean nullParameter = false;
		String authString = null;
		try {
			userCtrl.controlAuthString(authString);
		} catch (WebApplicationException wae) {
			nullParameter = true;
		}
		assertTrue(nullParameter);
	}

	/**
	 * Test le contrôle de l'utilisateur valide
	 */
	@Test
	public void controlUserOK() {
		boolean valid = true;
		User user = fakeUser;
		try {
			userCtrl.controlUser(user);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(valid);
	}

	/**
	 * Test le contrôle de l'utilisateur non-valide (nom manquant)
	 */
	@Test
	public void controlUserWrongName() {
		boolean valid = true;
		User user = new User(0, "", userPass, userEmail, 0, 0, 0);
		try {
			userCtrl.controlUser(user);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(!valid);
	}

	/**
	 * Test le contrôle de l'utilisateur non-valide (pass manquant)
	 */
	@Test
	public void controlUserWrongPass() {
		boolean valid = true;
		User user = new User(0, userName, "", userEmail, 0, 0, 0);
		try {
			userCtrl.controlUser(user);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(!valid);
	}

	/**
	 * Test le contrôle de l'utilisateur non-valide (email manquant/incorrect)
	 */
	@Test
	public void controlUserWrongEmail() {
		boolean valid = true;
		User user = new User(0, userName, userPass, "namedomain.com", 0, 0, 0);
		try {
			userCtrl.controlUser(user);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(!valid);
	}

	/**
	 * Test le contrôle de l'utilisateur non-valide (Null parameters)
	 */
	@Test
	public void controlUserNullParameters() {
		boolean valid = true;
		User user = null;
		try {
			userCtrl.controlUser(user);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(!valid);
	}

	/**
	 * Test le décodage d'un utilisateur valide via AuthString
	 */
	@Test
	public void getUserOK() {
		User user = null;
		String authString = "Basic TmFtZTpQYXNz";
		try {
			// Mock DAO
			UserDAO userDAO = Mockito.mock(UserDAO.class);
			Mockito.when(userDAO.getUser(userName, userPass)).thenReturn(fakeUser);
			userCtrl.setUserDAO(userDAO);
			// Fin Mock
			user = userCtrl.getUser(authString);
		} catch (TechnicalException | WebApplicationException e) {
			user = null;
		}
		assertTrue(user != null);
	}

	/**
	 * Test le décodage d'un utilisateur non-valide via AuthString
	 */
	@Test
	public void getUserWrongAuthString() {
		User user = fakeUser;
		String authString = null;
		try {
			// Mock DAO
			UserDAO userDAO = Mockito.mock(UserDAO.class);
			Mockito.when(userDAO.getUser(userName, userPass)).thenReturn(fakeUser);
			userCtrl.setUserDAO(userDAO);
			// Fin Mock
			user = userCtrl.getUser(authString);
		} catch (TechnicalException tex) {
			user = fakeUser;
		} catch (WebApplicationException wae) {
			user = null;
		}
		assertTrue(user == null);
	}

	/**
	 * Test l'ajout d'un utilisateur valide
	 */
	@Test
	public void addUserOK() {
		User user = fakeUser;
		try {
			// Mock DAO
			UserDAO userDAO = Mockito.mock(UserDAO.class);
			Mockito.when(userDAO.addUser(fakeUser)).thenReturn(fakeUser);
			userCtrl.setUserDAO(userDAO);
			// Fin Mock
			user = userCtrl.addUser(user);
		} catch (FonctionnalException | TechnicalException | WebApplicationException e) {
			user = null;
		}
		assertTrue(user != null);
	}

	/**
	 * Test l'ajout d'un utilisateur non-valide (name/email déjà existant)
	 */
	@Test
	public void addUserAlreadyExisting() {
		User user = fakeUser;
		try {
			// Mock DAO
			UserDAO userDAO = Mockito.mock(UserDAO.class);
			Mockito.when(userDAO.addUser(fakeUser)).thenReturn(null);
			userCtrl.setUserDAO(userDAO);
			// Fin Mock
			user = userCtrl.addUser(user);
		} catch (FonctionnalException | TechnicalException e) {
			user = fakeUser;
		} catch (WebApplicationException wae) {
			user = null;
		}
		assertTrue(user == null);
	}

	/**
	 * Test la modification d'un utilisateur valide
	 */
	@Test
	public void updateUserOK() {
		User user = fakeUser;
		String authString = "Basic TmFtZTpQYXNz";
		try {
			// Mock DAO
			UserDAO userDAO = Mockito.mock(UserDAO.class);
			Mockito.when(userDAO.getUser(userName, userPass)).thenReturn(fakeUser);
			Mockito.when(userDAO.updateUser(fakeUser, 0)).thenReturn(fakeUser);
			userCtrl.setUserDAO(userDAO);
			// Fin Mock
			user = userCtrl.updateUser(authString, user);
		} catch (FonctionnalException | TechnicalException | WebApplicationException e) {
			user = null;
		}
		assertTrue(user != null);
	}

	/**
	 * Test la modification d'un utilisateur non-valide (email déjà existant)
	 */
	@Test
	public void updateUserAlreadyExisting() {
		User user = fakeUser;
		String authString = "Basic TmFtZTpQYXNz";
		try {
			// Mock DAO
			UserDAO userDAO = Mockito.mock(UserDAO.class);
			Mockito.when(userDAO.getUser(userName, userPass)).thenReturn(fakeUser);
			Mockito.when(userDAO.updateUser(fakeUser, 0)).thenReturn(null);
			userCtrl.setUserDAO(userDAO);
			// Fin Mock
			user = userCtrl.updateUser(authString, user);
		} catch (FonctionnalException | TechnicalException e) {
			user = fakeUser;
		} catch (WebApplicationException wae) {
			user = null;
		}
		assertTrue(user == null);
	}

	/**
	 * Test la mise à jour d'un utilisateur valide
	 */
	@Test
	public void updateUserInterOK() {
		boolean updated = true;
		User user = fakeUser;
		String authString = "Basic TmFtZTpQYXNz";
		try {
			// Mock DAO
			UserDAO userDAO = Mockito.mock(UserDAO.class);
			Mockito.when(userDAO.getUser(userName, userPass)).thenReturn(fakeUser);
			userCtrl.setUserDAO(userDAO);
			// Fin Mock
			userCtrl.updateUserInter(authString, user);
		} catch (TechnicalException | WebApplicationException e) {
			updated = false;
		}
		assertTrue(updated);
	}

	/**
	 * Test la mise à jour d'un utilisateur valide mais base non disponible
	 */
	@Test
	public void updateUserInterTechnicalFail() {
		boolean updated = true;
		User user = fakeUser;
		String authString = "Basic TmFtZTpQYXNz";
		try {
			// Mock DAO
			UserDAO userDAO = Mockito.mock(UserDAO.class);
			Mockito.when(userDAO.getUser(userName, userPass)).thenThrow(new TechnicalException("Base hors ligne"));
			userCtrl.setUserDAO(userDAO);
			// Fin Mock
			userCtrl.updateUserInter(authString, user);
		} catch (TechnicalException | WebApplicationException e) {
			updated = false;
		}
		assertTrue(!updated);
	}

	/**
	 * Clôture
	 */
	@AfterClass
	public static void close() {
		logger.debug("### Tous les tests 'UserController' ont ete effectues ###");
	}

}
