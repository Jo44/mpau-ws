package fr.mpau_ws.dao;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.mpau_ws.exception.FonctionnalException;
import fr.mpau_ws.exception.TechnicalException;
import fr.mpau_ws.model.Intervention;
import fr.mpau_ws.tools.PoolConnection;
import fr.mpau_ws.tools.Settings;

public class InterventionDAOTest {
	private static final Logger logger = LogManager.getLogger(InterventionDAOTest.class);

	/**
	 * Attributs
	 */
	final private static String interSecteur = "Secteur";
	final private static String interComment = "Commentaire";
	final private Intervention fakeInter = new Intervention(0, 0, 1000000000L, 15, interSecteur, true, 1, 1, 1, interComment);
	final private Intervention newInter = new Intervention(0, 0, 1111111111L, 30, interSecteur, false, 2, 2, 2, interComment);
	private static InterventionDAO interDAO = new InterventionDAO();

	/**
	 * Initialisation
	 */
	@BeforeClass
	public static void initialize() {
		logger.debug("### Initialisation de InterventionDAOTest ###");
		initializeDB();
	}

	/**
	 * Test de récupération d'une liste d'interventions de l'utilisateur valide
	 */
	@Test
	public void getInterventionsOK() {
		List<Intervention> listInter = null;
		try {
			listInter = interDAO.getInterventions(0, 50, true, 1);
		} catch (TechnicalException tex) {
			listInter = null;
		}
		assertTrue(listInter != null);
	}

	/**
	 * Test de récupération d'une liste vide des interventions de l'utilisateur valide
	 */
	@Test
	public void getInterventionsEmptyOK() {
		List<Intervention> listInter = null;
		try {
			listInter = interDAO.getInterventions(0, 50, false, 1);
		} catch (TechnicalException tex) {
			listInter = new ArrayList<Intervention>();
		}
		assertTrue(listInter == null);
	}

	/**
	 * Test de récupération d'une liste des interventions d'un utilisateur non reconnu
	 */
	@Test
	public void getInterventionsUnknowUser() {
		List<Intervention> listInter = null;
		try {
			listInter = interDAO.getInterventions(0, 50, true, 99);
		} catch (TechnicalException tex) {
			listInter = new ArrayList<Intervention>();
		}
		assertTrue(listInter == null);
	}

	/**
	 * Test de récupération d'une intervention de l'utilisateur valide
	 */
	@Test
	public void getInterventionOK() {
		Intervention inter = null;
		try {
			inter = interDAO.getIntervention(1, 1);
		} catch (TechnicalException tex) {
			inter = null;
		}
		assertTrue(inter != null);
	}

	/**
	 * Test de récupération d'une intervention inexistante de l'utilisateur
	 */
	@Test
	public void getInterventionWrongInter() {
		Intervention inter = null;
		try {
			inter = interDAO.getIntervention(99, 1);
		} catch (TechnicalException tex) {
			inter = fakeInter;
		}
		assertTrue(inter == null);
	}

	/**
	 * Test de récupération d'une intervention d'un utilisateur inconnu
	 */
	@Test
	public void getInterventionWrongUser() {
		Intervention inter = null;
		try {
			inter = interDAO.getIntervention(1, 99);
		} catch (TechnicalException tex) {
			inter = fakeInter;
		}
		assertTrue(inter == null);
	}

	/**
	 * Test de l'ajout d'une nouvelle intervention pour l'utilisateur valide
	 */
	@Test
	public void addInterventionOK() {
		Intervention inter = newInter;
		try {
			inter = interDAO.addIntervention(inter, 1);
		} catch (TechnicalException | FonctionnalException e) {
			inter = null;
		}
		assertTrue(inter != null);
	}

	/**
	 * Test de l'ajout d'une nouvelle intervention vide pour l'utilisateur (paramètres null)
	 */
	@Test
	public void addInterventionNullParameters() {
		Intervention inter = null;
		try {
			inter = interDAO.addIntervention(inter, 1);
		} catch (FonctionnalException fex) {
			inter = null;
		} catch (TechnicalException tex) {
			inter = fakeInter;
		}
		assertTrue(inter == null);
	}

	/**
	 * Test de la modification d'une intervention valide
	 */
	@Test
	public void updateInterventionOK() {
		Intervention inter = newInter;
		try {
			inter = interDAO.updateIntervention(inter, 1, 1);
		} catch (TechnicalException | FonctionnalException e) {
			inter = null;
		}
		assertTrue(inter != null);
	}

	/**
	 * Test de la modification d'une intervention pour utilisateur non valide
	 */
	@Test
	public void updateInterventionWrongUser() {
		Intervention inter = newInter;
		try {
			inter = interDAO.updateIntervention(inter, 1, 99);
		} catch (TechnicalException | FonctionnalException e) {
			inter = fakeInter;
		}
		assertTrue(inter == null);
	}

	/**
	 * Test de la modification d'une intervention inexistante pour utilisateur valide
	 */
	@Test
	public void updateInterventionWrongInter() {
		Intervention inter = newInter;
		try {
			inter = interDAO.updateIntervention(inter, 99, 1);
		} catch (TechnicalException | FonctionnalException e) {
			inter = fakeInter;
		}
		assertTrue(inter == null);
	}

	/**
	 * Test de la modification d'une intervention vide pour utilisateur valide (paramètres null)
	 */
	@Test
	public void updateInterventionNullParameters() {
		Intervention inter = null;
		try {
			inter = interDAO.updateIntervention(inter, 1, 1);
		} catch (FonctionnalException fex) {
			inter = null;
		} catch (TechnicalException tex) {
			inter = fakeInter;
		}
		assertTrue(inter == null);
	}

	/**
	 * Test de la suppression d'une intervention valide
	 */
	@Test
	public void deleteInterventionOK() {
		Intervention inter = null;
		try {
			inter = interDAO.deleteIntervention(2, 1);
		} catch (TechnicalException e) {
			inter = null;
		}
		assertTrue(inter != null);
	}

	/**
	 * Test de la suppression d'une intervention inexistante pour l'utilisateur
	 */
	@Test
	public void deleteInterventionWrongInter() {
		Intervention inter = fakeInter;
		try {
			inter = interDAO.deleteIntervention(99, 1);
		} catch (TechnicalException e) {
			inter = fakeInter;
		}
		assertTrue(inter == null);
	}

	/**
	 * Test de la suppression d'une intervention valide
	 */
	@Test
	public void deleteInterventionWrongUser() {
		Intervention inter = fakeInter;
		try {
			inter = interDAO.deleteIntervention(1, 99);
		} catch (TechnicalException e) {
			inter = fakeInter;
		}
		assertTrue(inter == null);
	}

	/**
	 * Clôture
	 */
	@AfterClass
	public static void close() {
		logger.debug("### Tous les tests 'InterventionDAO' ont ete effectues ###");
	}

	/**
	 * Initialisation de la database
	 */
	private static void initializeDB() {
		Connection con = null;
		PreparedStatement pstmt = null;

		// - drop la table intervention
		// - create la table intervention
		// - insert d'une intervention (x3)

		try {
			logger.debug("Initialisation de la base de donnees en cours ..");
			con = PoolConnection.getConnection();
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
			logger.debug("Insertion de 3 interventions dans la table 'Interventions' ..");
			pstmt = con.prepareStatement(Settings.getProperty("intertest.insertIntervention"));
			pstmt.setInt(1, 1);
			pstmt.setInt(2, 1);
			pstmt.setLong(3, 1000000000L);
			pstmt.setInt(4, 15);
			pstmt.setString(5, interSecteur);
			pstmt.setBoolean(6, true);
			pstmt.setInt(7, 1);
			pstmt.setInt(8, 1);
			pstmt.setInt(9, 1);
			pstmt.setString(10, interComment);
			pstmt.execute();
			pstmt.execute();
			pstmt.execute();
			logger.debug("Initialisation de la base de données : OK");
		} catch (SQLException e) {
			logger.error("Erreur lors de l'initialisation de la base de donnees pour les tests !!");
		}
	}

}
