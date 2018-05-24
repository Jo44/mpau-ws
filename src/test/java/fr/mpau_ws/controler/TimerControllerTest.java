package fr.mpau_ws.controler;

import static org.junit.Assert.assertTrue;

import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import fr.mpau_ws.model.RequestTimestamp;

@RunWith(MockitoJUnitRunner.class)
public class TimerControllerTest {
	private static final Logger logger = LogManager.getLogger(TimerControllerTest.class);

	/**
	 * Attributs
	 */
	private static TimerController timerCtrl = new TimerController();

	/**
	 * Initialisation
	 */
	@BeforeClass
	public static void initialize() {
		logger.debug("### Initialisation de TimerControllerTest ###");
	}

	/**
	 * Test le contrôle du RequestTimestamp valide
	 */
	@Test
	public void controlRequestTimestampOK() {
		boolean valid = true;
		RequestTimestamp rqtTimestamp = new RequestTimestamp(999999999L);
		try {
			timerCtrl.controlRequestTimestamp(rqtTimestamp);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(valid);
	}

	/**
	 * Test le contrôle du RequestTimestamp non-valide (timestamp négatif)
	 */
	@Test
	public void controlRequestTimestampNegative() {
		boolean valid = true;
		RequestTimestamp rqtTimestamp = new RequestTimestamp(-999999999L);
		try {
			timerCtrl.controlRequestTimestamp(rqtTimestamp);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(!valid);
	}

	/**
	 * Test le contrôle du RequestTimestamp non-valide (Null Parameter)
	 */
	@Test
	public void controlRequestTimestampNullParameter() {
		boolean valid = true;
		RequestTimestamp rqtTimestamp = null;
		try {
			timerCtrl.controlRequestTimestamp(rqtTimestamp);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(!valid);
	}

	/**
	 * Test le contrôle du WorkdayID valide
	 */
	@Test
	public void controlWorkdayIDOK() {
		boolean valid = true;
		int workdayId = 1;
		try {
			timerCtrl.controlWorkdayID(workdayId);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(valid);
	}

	/**
	 * Test le contrôle du WorkdayID non-valide (Id négatif)
	 */
	@Test
	public void controlWorkdayIDNegative() {
		boolean valid = true;
		int workdayId = -1;
		try {
			timerCtrl.controlWorkdayID(workdayId);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(!valid);
	}

	/**
	 * Test le contrôle du RelatifMonth valide (compris entre 0 et -12)
	 */
	@Test
	public void controlRelatifMonthOK() {
		boolean valid = true;
		int relatifMonth = 0;
		try {
			timerCtrl.controlRelatifMonth(relatifMonth);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(valid);
	}

	/**
	 * Test le contrôle du RelatifMonth non-valide (supérieur à 0)
	 */
	@Test
	public void controlRelatifMonthSup() {
		boolean valid = true;
		int relatifMonth = 1;
		try {
			timerCtrl.controlRelatifMonth(relatifMonth);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(!valid);
	}

	/**
	 * Test le contrôle du RelatifMonth non-valide (inférieur à -12)
	 */
	@Test
	public void controlRelatifMonthInf() {
		boolean valid = true;
		int relatifMonth = -13;
		try {
			timerCtrl.controlRelatifMonth(relatifMonth);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(!valid);
	}

	// TODO:
	// - tests unitaires des différentes méthodes du timer controller

	/**
	 * Clôture
	 */
	@AfterClass
	public static void close() {
		logger.debug("### Tous les tests 'TimerController' ont ete effectues ###");
	}

}
