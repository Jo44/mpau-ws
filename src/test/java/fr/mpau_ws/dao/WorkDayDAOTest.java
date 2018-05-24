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

import fr.mpau_ws.exception.TechnicalException;
import fr.mpau_ws.model.WorkDay;
import fr.mpau_ws.tools.PoolConnection;
import fr.mpau_ws.tools.Settings;

public class WorkDayDAOTest {

	private static final Logger logger = LogManager.getLogger(WorkDayDAOTest.class);
	private static WorkDayDAO wdDAO = new WorkDayDAO();
	final private WorkDay fakeWd = new WorkDay(0, 99, 0, 0, false);

	/**
	 * Initialisation
	 */
	@BeforeClass
	public static void initialize() {
		logger.debug("### Initialisation de WorkDayDAOTest ###");
		initializeDB();
	}

	/**
	 * Test la récupération d'une Workday non-terminée valide
	 */
	@Test
	public void getWorkDayNotCompletedOK() {
		WorkDay wd = null;
		try {
			wd = wdDAO.getWorkDayNotCompleted(1);
		} catch (TechnicalException tex) {
			wd = null;
		}
		assertTrue(wd != null);
	}

	/**
	 * Test la récupération d'une WorkDay non-terminée pour un utilisateur non-valide
	 */
	@Test
	public void getWorkDayNotCompletedWrongUser() {
		WorkDay wd = fakeWd;
		try {
			wd = wdDAO.getWorkDayNotCompleted(99);
		} catch (TechnicalException tex) {
			wd = fakeWd;
		}
		assertTrue(wd == null);
	}

	/**
	 * Test de récupération de toutes les WorkDays de l'utilisateur valide
	 */
	@Test
	public void getAllWorkDaysOK() {
		List<WorkDay> listWd = null;
		try {
			listWd = wdDAO.getAllWorkDays(1);
		} catch (TechnicalException tex) {
			listWd = null;
		}
		assertTrue(listWd != null);
	}

	/**
	 * Test de récupération de toutes les WorkDays de l'utilisateur non-valide
	 */
	@Test
	public void getAllWorkDaysWrongUser() {
		List<WorkDay> listWd = new ArrayList<>();
		try {
			listWd = wdDAO.getAllWorkDays(99);
		} catch (TechnicalException tex) {
			listWd = new ArrayList<>();
		}
		assertTrue(listWd == null);
	}

	/**
	 * Test de récupération de toutes les WorkDays de l'utilisateur valide pour un mois valide
	 */
	@Test
	public void getAllWorkDaysForMonthOk() {
		List<WorkDay> listWd = null;
		try {
			listWd = wdDAO.getAllWorkDaysForMonth(1, 9, 2001);
		} catch (TechnicalException tex) {
			listWd = null;
		}
		assertTrue(listWd != null);
	}

	/**
	 * Test de récupération de toutes les WorkDays de l'utilisateur valide pour un mois non-valide
	 */
	@Test
	public void getAllWorkDaysForMonthEmpty() {
		List<WorkDay> listWd = new ArrayList<>();
		try {
			listWd = wdDAO.getAllWorkDaysForMonth(1, 9, 2000);
		} catch (TechnicalException tex) {
			listWd = new ArrayList<>();
		}
		assertTrue(listWd == null);
	}

	/**
	 * Test de récupération d'une WorkDay de l'utilisateur valide
	 */
	@Test
	public void getWorkDayOK() {
		WorkDay wd = null;
		try {
			wd = wdDAO.getWorkDay(2, 1);
		} catch (TechnicalException tex) {
			wd = null;
		}
		assertTrue(wd != null);
	}

	/**
	 * Test de récupération d'une WorkDay non-valide de l'utilisateur valide
	 */
	@Test
	public void getWorkDayWrongWd() {
		WorkDay wd = fakeWd;
		try {
			wd = wdDAO.getWorkDay(99, 1);
		} catch (TechnicalException tex) {
			wd = fakeWd;
		}
		assertTrue(wd == null);
	}

	/**
	 * Test de récupération d'une WorkDay valide de l'utilisateur non-valide
	 */
	@Test
	public void getWorkDayWrongUser() {
		WorkDay wd = fakeWd;
		try {
			wd = wdDAO.getWorkDay(2, 99);
		} catch (TechnicalException tex) {
			wd = fakeWd;
		}
		assertTrue(wd == null);
	}

	/**
	 * Test l'ajout d'une nouvelle WorkDay pour un utilisateur valide
	 */
	@Test
	public void startWorkDayOK() {
		WorkDay wd = null;
		try {
			wd = wdDAO.startWorkDay(1717171717L, 1);
		} catch (TechnicalException tex) {
			wd = null;
		}
		assertTrue(wd != null);
	}

	/**
	 * Test l'ajout d'une nouvelle WorkDay pour un utilisateur non-valide (fonctionne car on ne s'inquiète pas de savoir à ce moment si l'utilisateur
	 * existe ou pas, le contrôle est fait en amont)
	 */
	@Test
	public void startWorkDayWrongUser() {
		WorkDay wd = null;
		try {
			wd = wdDAO.startWorkDay(1717171717L, 98);
		} catch (TechnicalException tex) {
			wd = null;
		}
		assertTrue(wd != null);
	}

	/**
	 * Test la mise à jour d'une WorkDay (clôture) valide
	 */
	@Test
	public void stopWorkDayOK() {
		WorkDay wd = null;
		try {
			wd = wdDAO.stopWorkDay(1515151515L, 3);
		} catch (TechnicalException tex) {
			wd = null;
		}
		assertTrue(wd != null);
	}

	/**
	 * Test la mise à jour d'une WorkDay (clôture) non-valide
	 */
	@Test
	public void stopWorkDayWrongWd() {
		WorkDay wd = fakeWd;
		try {
			wd = wdDAO.stopWorkDay(1515151515L, 99);
		} catch (TechnicalException tex) {
			wd = fakeWd;
		}
		assertTrue(wd == null);
	}

	/**
	 * Test la suppression d'une WorkDay valide pour un utilisateur valide
	 */
	@Test
	public void deleteWorkDayOK() {
		WorkDay wd = null;
		try {
			wd = wdDAO.deleteWorkDay(4, 1);
		} catch (TechnicalException tex) {
			wd = null;
		}
		assertTrue(wd != null);
	}

	/**
	 * Test la suppression d'une WorkDay non-valide pour un utilisateur valide
	 */
	@Test
	public void deleteWorkDayWrongWd() {
		WorkDay wd = fakeWd;
		try {
			wd = wdDAO.deleteWorkDay(55, 1);
		} catch (TechnicalException tex) {
			wd = fakeWd;
		}
		assertTrue(wd == null);
	}

	/**
	 * Test la suppression d'une WorkDay valide pour un utilisateur non-valide
	 */
	@Test
	public void deleteWorkDayWrongUser() {
		WorkDay wd = fakeWd;
		try {
			wd = wdDAO.deleteWorkDay(1, 55);
		} catch (TechnicalException tex) {
			wd = fakeWd;
		}
		assertTrue(wd == null);
	}

	/**
	 * Clôture
	 */
	@AfterClass
	public static void close() {
		logger.debug("### Tous les tests 'WorkDayDAO' ont ete effectues ###");
	}

	/**
	 * Initialisation de la database
	 */
	private static void initializeDB() {
		Connection con = null;
		PreparedStatement pstmt = null;

		// - drop la table workday
		// - create la table workday
		// - insert d'une workday (x4)

		try {
			logger.debug("Initialisation de la base de donnees en cours ..");
			con = PoolConnection.getConnection();
			try {
				logger.debug("Suppression de la table 'WorkDays' ..");
				pstmt = con.prepareStatement(Settings.getProperty("wdtest.deleteTable"));
				pstmt.execute();
			} catch (SQLException e) {
				logger.error("Impossible de supprimer la table.");
			}
			logger.debug("Creation de la table 'WorkDays' ..");
			pstmt = con.prepareStatement(Settings.getProperty("wdtest.createTable"));
			pstmt.execute();
			logger.debug("Insertion de 4 WorkDays (dont 2 non-terminée) dans la table 'WorkDays' ..");
			pstmt = con.prepareStatement(Settings.getProperty("wdtest.insertFinishedWorkDay"));
			pstmt.setInt(1, 1);
			pstmt.setLong(2, 1000000000L);
			pstmt.setLong(3, 1111111111L);
			pstmt.setBoolean(4, true);
			pstmt.execute();
			pstmt.setInt(1, 1);
			pstmt.setLong(2, 1212121212L);
			pstmt.setLong(3, 1313131313L);
			pstmt.setBoolean(4, true);
			pstmt.execute();
			pstmt = con.prepareStatement(Settings.getProperty("wdtest.insertWorkDay"));
			pstmt.setInt(1, 1);
			pstmt.setLong(2, 1414141414L);
			pstmt.execute();
			pstmt.setInt(1, 1);
			pstmt.setLong(2, 1616161616L);
			pstmt.execute();
			logger.debug("Initialisation de la base de données : OK");
		} catch (SQLException e) {
			logger.error("Erreur lors de l'initialisation de la base de donnees pour les tests !!");
		}
	}

}
