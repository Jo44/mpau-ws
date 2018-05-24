package fr.mpau_ws.dao;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.mpau_ws.exception.FonctionnalException;
import fr.mpau_ws.exception.TechnicalException;
import fr.mpau_ws.model.User;
import fr.mpau_ws.tools.PoolConnection;
import fr.mpau_ws.tools.Settings;

public class UserDAOTest {
	private static final Logger logger = LogManager.getLogger(UserDAOTest.class);

	/**
	 * Attributs
	 */
	final private static String userName = "Name";
	final private static String userPass = "Pass";
	final private static String userEmail = "em@il.fr";
	final private User fakeUser = new User(0, userName, userPass, userEmail, 0, 0, 0);
	private static UserDAO userDAO = new UserDAO();

	/**
	 * Initialisation
	 */
	@BeforeClass
	public static void initialize() {
		logger.debug("### Initialisation de UserDAOTest ###");
		initializeDB();
	}

	/**
	 * Test de récupération d'un utilisateur valide
	 */
	@Test
	public void getUserOK() {
		User user = null;
		try {
			user = userDAO.getUser(userName + "1", userPass + "1");
		} catch (TechnicalException tex) {
			user = null;
		}
		assertTrue(user != null);
	}

	/**
	 * Test de récupération d'un utilisateur non-valide (nom incorrect)
	 */
	@Test
	public void getUserWrongName() {
		User user = null;
		try {
			user = userDAO.getUser(userName + "42", userPass + "1");
		} catch (TechnicalException tex) {
			user = fakeUser;
		}
		assertTrue(user == null);
	}

	/**
	 * Test de récupération d'un utilisateur non-valide (mot de passe incorrect)
	 */
	@Test
	public void getUserWrongPass() {
		User user = null;
		try {
			user = userDAO.getUser(userName + "1", userPass + "42");
		} catch (TechnicalException tex) {
			user = fakeUser;
		}
		assertTrue(user == null);
	}

	/**
	 * Test de récupération d'un utilisateur non-valide (paramètres null)
	 */
	@Test
	public void getUserNullParameters() {
		User user = null;
		try {
			user = userDAO.getUser(null, null);
		} catch (TechnicalException tex) {
			user = fakeUser;
		}
		assertTrue(user == null);
	}

	/**
	 * Test de l'ajout d'un nouvel utilisateur valide
	 */
	@Test
	public void addUserOK() {
		User user = new User(0, userName + "3", userPass + "3", userEmail + "3", 0, 0, 0);
		try {
			user = userDAO.addUser(user);
		} catch (TechnicalException | FonctionnalException e) {
			user = null;
		}
		assertTrue(user != null);
	}

	/**
	 * Test de l'ajout d'un nouvel utilisateur non-valide (nom déjà pris)
	 */
	@Test
	public void addUserWrongName() {
		User user = new User(0, userName + "1", userPass + "4", userEmail + "4", 0, 0, 0);
		try {
			user = userDAO.addUser(user);
		} catch (TechnicalException | FonctionnalException e) {
			user = fakeUser;
		}
		assertTrue(user == null);
	}

	/**
	 * Test de l'ajout d'un nouvel utilisateur non-valide (email déjà pris)
	 */
	@Test
	public void addUserWrongEmail() {
		User user = new User(0, userName + "5", userPass + "5", userEmail + "1", 0, 0, 0);
		try {
			user = userDAO.addUser(user);
		} catch (TechnicalException | FonctionnalException e) {
			user = fakeUser;
		}
		assertTrue(user == null);
	}

	/**
	 * Test de l'ajout d'un nouvel utilisateur non-valide (paramètres null)
	 */
	@Test
	public void addUserNullParameters() {
		User user = null;
		try {
			user = userDAO.addUser(user);
		} catch (FonctionnalException fex) {
			user = null;
		} catch (TechnicalException tex) {
			user = fakeUser;
		}
		assertTrue(user == null);
	}

	/**
	 * Test de la modification d'un utilisateur valide
	 */
	@Test
	public void updateUserOK() {
		User user = new User(0, userName + "1", userPass + "6", userEmail + "6", 0, 0, 0);
		try {
			user = userDAO.updateUser(user, 2);
		} catch (TechnicalException | FonctionnalException e) {
			user = null;
		}
		assertTrue(user != null);
	}

	/**
	 * Test de la modification d'un utilisateur non-valide (email déjà pris)
	 */
	@Test
	public void updateUserWrongEmail() {
		User user = new User(0, userName + "7", userPass + "7", userEmail + "1", 0, 0, 0);
		try {
			user = userDAO.updateUser(user, 2);
		} catch (TechnicalException | FonctionnalException ex) {
			user = fakeUser;
		}
		assertTrue(user == null);
	}

	/**
	 * Test de la modification d'un utilisateur non-valide (paramètres null)
	 */
	@Test
	public void updateUserNullParameters() {
		User user = null;
		try {
			user = userDAO.updateUser(user, 2);
		} catch (FonctionnalException fex) {
			user = null;
		} catch (TechnicalException tex) {
			user = fakeUser;
		}
		assertTrue(user == null);
	}

	/**
	 * Test de la mise à jour d'un utilisateur valide (intervention déjà insérée)
	 */
	@Test
	public void updateUserInterOK() {
		// Pour le test, l'intervention a déjà été insérée, mais l'utilisateur n'a pas encore été mis à jour
		// donc son nombre total d'intervention = 0 et l'ID Max d'intervention = 0
		boolean change = true;
		try {
			userDAO.updateUserInter(1);
			User user = userDAO.getUser(userName + "1", userPass + "1");
			if (user.getNbTotalInter() == 0 || user.getInterIdMax() == 0) {
				change = false;
			}
		} catch (TechnicalException e) {
			change = false;
		}
		assertTrue(change);
	}

	/**
	 * Test de la mise à jour d'un utilisateur non-valide
	 */
	@Test
	public void updateUserInterWrongUser() {
		// Ne crash pas même si utilisateur non-valide (comportement voulu)
		boolean crash = false;
		try {
			userDAO.updateUserInter(75);
		} catch (TechnicalException e) {
			crash = true;
		}
		assertTrue(!crash);
	}

	/**
	 * Clôture
	 */
	@AfterClass
	public static void close() {
		logger.debug("### Tous les tests 'UserDAO' ont ete effectues ###");
	}

	/**
	 * Initialisation de la database
	 */
	private static void initializeDB() {
		Connection con = null;
		PreparedStatement pstmt = null;

		// - drop la table users
		// - create la table users
		// - insert d'un utilisateur (x2)
		// - drop la table intervention
		// - create la table intervention
		// - insert d'une intervention (pour test mise à jour de l'utilisateur)

		try {
			logger.debug("Initialisation de la base de donnees en cours ..");
			con = PoolConnection.getConnection();
			try {
				logger.debug("Suppression de la table 'Users' ..");
				pstmt = con.prepareStatement(Settings.getProperty("usertest.deleteTable"));
				pstmt.execute();
			} catch (SQLException e) {
				logger.error("Impossible de supprimer la table.");
			}
			logger.debug("Creation de la table 'Users' ..");
			pstmt = con.prepareStatement(Settings.getProperty("usertest.createTable"));
			pstmt.execute();
			logger.debug("Insertion de 2 utilisateurs dans la table 'Users' ..");
			pstmt = con.prepareStatement(Settings.getProperty("usertest.insertUser"));
			pstmt.setString(1, userName + "1");
			pstmt.setString(2, userPass + "1");
			pstmt.setString(3, userEmail + "1");
			pstmt.execute();
			pstmt.setString(1, userName + "2");
			pstmt.setString(2, userPass + "2");
			pstmt.setString(3, userEmail + "2");
			pstmt.execute();
			try {
				logger.debug("Suppression de la table 'Interventions' ..");
				pstmt = con.prepareStatement(Settings.getProperty("intertest.deleteTable"));
				pstmt.execute();
			} catch (SQLException e) {
				logger.error("Impossible de supprimer la table.");
			}
			logger.debug("Creation de la table 'Interventions' ..");
			pstmt = con.prepareStatement(Settings.getProperty("intertest.createTable"));
			pstmt.execute();
			logger.debug("Insertion d'une intervention dans la table 'Interventions' ..");
			pstmt = con.prepareStatement(Settings.getProperty("intertest.insertIntervention"));
			pstmt.setInt(1, 1);
			pstmt.setInt(2, 1);
			pstmt.setLong(3, 1515151515L);
			pstmt.setInt(4, 60);
			pstmt.setString(5, "Secteur");
			pstmt.setBoolean(6, true);
			pstmt.setInt(7, 1);
			pstmt.setInt(8, 1);
			pstmt.setInt(9, 1);
			pstmt.setString(10, "Commentaire");
			pstmt.execute();
			logger.debug("Initialisation de la base de données : OK");
		} catch (SQLException e) {
			logger.error("Erreur lors de l'initialisation de la base de donnees pour les tests !!");
		}
	}

}
