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
import fr.mpau_ws.model.WorkPeriod;
import fr.mpau_ws.tools.PoolConnection;
import fr.mpau_ws.tools.Settings;

public class WorkPeriodDAOTest {

	private static final Logger logger = LogManager.getLogger(WorkPeriodDAOTest.class);
	private static WorkPeriodDAO wpDAO = new WorkPeriodDAO();
	final private WorkPeriod fakeWp = new WorkPeriod(0, 99, 0, 0, false);

	/**
	 * Initialisation
	 */
	@BeforeClass
	public static void initialize() {
		logger.debug("### Initialisation de WorkPeriodDAOTest ###");
		initializeDB();
	}

	/**
	 * Test la récupération d'une WorkPeriod non-terminée pour une WorkDay valide
	 */
	@Test
	public void getWorkPeriodNotCompletedOK() {
		WorkPeriod wp = null;
		try {
			wp = wpDAO.getWorkPeriodNotCompleted(1);
		} catch (TechnicalException tex) {
			wp = null;
		}
		assertTrue(wp != null);
	}

	/**
	 * Test la récupération d'une WorkPeriod non-terminée pour une WorkDay non-valide
	 */
	@Test
	public void getWorkPeriodNotCompletedWrongWd() {
		WorkPeriod wp = fakeWp;
		try {
			wp = wpDAO.getWorkPeriodNotCompleted(99);
		} catch (TechnicalException tex) {
			wp = fakeWp;
		}
		assertTrue(wp == null);
	}

	/**
	 * Test la récupération d'une WorkPeriod non-terminée pour une WorkDay valide mais terminée
	 */
	@Test
	public void getWorkPeriodNotCompletedFinishedWd() {
		WorkPeriod wp = fakeWp;
		try {
			wp = wpDAO.getWorkPeriodNotCompleted(3);
		} catch (TechnicalException tex) {
			wp = fakeWp;
		}
		assertTrue(wp == null);
	}

	/**
	 * Test de récupération de toutes les WorkPeriods de la WorkDay valide
	 */
	@Test
	public void getAllWorkPeriodsOK() {
		List<WorkPeriod> listWp = null;
		try {
			listWp = wpDAO.getAllWorkPeriods(1);
		} catch (TechnicalException tex) {
			listWp = null;
		}
		assertTrue(listWp != null);
	}

	/**
	 * Test de récupération de toutes les WorkPeriods de la WorkDay non-valide
	 */
	@Test
	public void getAllWorkPeriodsWrongUser() {
		List<WorkPeriod> listWp = new ArrayList<>();
		try {
			listWp = wpDAO.getAllWorkPeriods(99);
		} catch (TechnicalException tex) {
			listWp = new ArrayList<>();
		}
		assertTrue(listWp == null);
	}

	/**
	 * Test l'ajout d'une nouvelle WorkPeriod pour une WorkDay valide
	 */
	@Test
	public void startWorkPeriodOK() {
		WorkPeriod wp = null;
		try {
			wp = wpDAO.startWorkPeriod(1010101010L, 4);
		} catch (TechnicalException tex) {
			wp = null;
		}
		assertTrue(wp != null);
	}

	/**
	 * Test l'ajout d'une nouvelle WorkPeriod pour une WorkDay non-valide (fonctionne car on ne s'inquiète pas de savoir à ce moment si la WorkDay
	 * existe ou pas, le contrôle est fait en amont)
	 */
	@Test
	public void startWorkPeriodWrongWd() {
		WorkPeriod wp = null;
		try {
			wp = wpDAO.startWorkPeriod(1010101010L, 98);
		} catch (TechnicalException tex) {
			wp = null;
		}
		assertTrue(wp != null);
	}

	/**
	 * Test la mise à jour d'une WorkPeriod (clôture) valide
	 */
	@Test
	public void stopWorkPeriodOK() {
		WorkPeriod wp = null;
		try {
			wp = wpDAO.stopWorkPeriod(1515151515L, 5);
		} catch (TechnicalException tex) {
			wp = null;
		}
		assertTrue(wp != null);
	}

	/**
	 * Test la mise à jour d'une WorkPeriod (clôture) non-valide
	 */
	@Test
	public void stopWorkPeriodWrongWp() {
		WorkPeriod wp = fakeWp;
		try {
			wp = wpDAO.stopWorkPeriod(1515151515L, 99);
		} catch (TechnicalException tex) {
			wp = fakeWp;
		}
		assertTrue(wp == null);
	}

	/**
	 * Test la suppression des WorkPeriods pour une WorkDay valide
	 */
	@Test
	public void deleteWorkPeriodsOK() {
		List<WorkPeriod> listWp = null;
		try {
			listWp = wpDAO.deleteWorkPeriods(2);
		} catch (TechnicalException tex) {
			listWp = null;
		}
		assertTrue(listWp != null);
	}

	/**
	 * Test la suppression des WorkPeriods pour une WorkDay non-valide
	 */
	@Test
	public void deleteWorkPeriodsWrongWd() {
		List<WorkPeriod> listWp = new ArrayList<>();
		try {
			listWp = wpDAO.deleteWorkPeriods(97);
		} catch (TechnicalException tex) {
			listWp = new ArrayList<>();
		}
		assertTrue(listWp == null);
	}

	/**
	 * Clôture
	 */
	@AfterClass
	public static void close() {
		logger.debug("### Tous les tests 'WorkPeriodDAO' ont ete effectues ###");
	}

	/**
	 * Initialisation de la database
	 */
	private static void initializeDB() {
		Connection con = null;
		PreparedStatement pstmt = null;

		// - drop la table workperiod
		// - create la table workperiod
		// - insert d'une workperiod (x6)

		try {
			logger.debug("Initialisation de la base de donnees en cours ..");
			con = PoolConnection.getConnection();
			try {
				logger.debug("Suppression de la table 'WorkPeriods' ..");
				pstmt = con.prepareStatement(Settings.getProperty("wptest.deleteTable"));
				pstmt.execute();
			} catch (SQLException e) {
				logger.error("Impossible de supprimer la table.");
			}
			logger.debug("Creation de la table 'WorkPeriods' ..");
			pstmt = con.prepareStatement(Settings.getProperty("wptest.createTable"));
			pstmt.execute();
			logger.debug("Insertion de 6 WorkPeriods (dont 2 non-terminée) dans la table 'WorkPeriods' ..");
			pstmt = con.prepareStatement(Settings.getProperty("wptest.insertFinishedWorkPeriod"));
			pstmt.setInt(1, 1);
			pstmt.setLong(2, 1010101010L);
			pstmt.setLong(3, 1111111111L);
			pstmt.setBoolean(4, true);
			pstmt.execute();
			pstmt.setInt(1, 1);
			pstmt.setLong(2, 1212121212L);
			pstmt.setLong(3, 1313131313L);
			pstmt.setBoolean(4, true);
			pstmt.execute();
			pstmt.setInt(1, 2);
			pstmt.setLong(2, 1010101010L);
			pstmt.setLong(3, 1111111111L);
			pstmt.setBoolean(4, true);
			pstmt.execute();
			pstmt.setInt(1, 3);
			pstmt.setLong(2, 1010101010L);
			pstmt.setLong(3, 1111111111L);
			pstmt.setBoolean(4, true);
			pstmt.execute();
			pstmt = con.prepareStatement(Settings.getProperty("wptest.insertWorkPeriod"));
			pstmt.setInt(1, 1);
			pstmt.setLong(2, 1414141414L);
			pstmt.execute();
			pstmt.setInt(1, 2);
			pstmt.setLong(2, 1212121212L);
			pstmt.execute();
			logger.debug("Initialisation de la base de données : OK");
		} catch (SQLException e) {
			logger.error("Erreur lors de l'initialisation de la base de donnees pour les tests !!");
		}
	}

}
