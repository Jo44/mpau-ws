package fr.mpau_ws.controler;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import fr.mpau_ws.dao.InterventionDAO;
import fr.mpau_ws.exception.FonctionnalException;
import fr.mpau_ws.exception.TechnicalException;
import fr.mpau_ws.model.Intervention;
import fr.mpau_ws.model.User;

@RunWith(MockitoJUnitRunner.class)
public class InterventionControllerTest {
	private static final Logger logger = LogManager.getLogger(InterventionControllerTest.class);

	/**
	 * Attributs
	 */
	final private static String sector = "Sector";
	final private static String comment = "Comment";
	final private Intervention fakeInter = new Intervention(0, 0, 1000000000L, 60, sector, false, 1, 6, 10, comment);
	final private List<Intervention> fakeList = new ArrayList<Intervention>();
	final private User fakeUser = new User(10, "name", "pass", "email", 0, 0, 0L);
	private static InterventionController interCtrl = new InterventionController();

	/**
	 * Initialisation
	 */
	@BeforeClass
	public static void initialize() {
		logger.debug("### Initialisation de InterventionControllerTest ###");
	}

	/**
	 * Test le contrôle de l'intervention ID valide
	 */
	@Test
	public void controlInterIDOK() {
		boolean valid = true;
		int interID = 1;
		try {
			interCtrl.controlInterID(interID);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(valid);
	}

	/**
	 * Test le contrôle de l'intervention ID non-valide
	 */
	@Test
	public void controlInterIDWrongID() {
		boolean valid = true;
		int interID = -5;
		try {
			interCtrl.controlInterID(interID);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(!valid);
	}

	/**
	 * Test le contrôle du nombre d'intervention valide
	 */
	@Test
	public void controlNbInterOK() {
		boolean valid = true;
		int nbInter = 1;
		try {
			interCtrl.controlNbInter(nbInter);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(valid);
	}

	/**
	 * Test le contrôle du nombre d'intervention non-valide
	 */
	@Test
	public void controlNbInterWrongNbInter() {
		boolean valid = true;
		int nbInter = -10;
		try {
			interCtrl.controlNbInter(nbInter);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(!valid);
	}

	/**
	 * Test le contrôle d'une intervention valide
	 */
	@Test
	public void controlInterventionOK() {
		boolean valid = true;
		Intervention inter = fakeInter;
		try {
			interCtrl.controlIntervention(inter);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(valid);
	}

	/**
	 * Test le contrôle d'une intervention non-valide (date incorrect)
	 */
	@Test
	public void controlInterventionWrongDate() {
		boolean valid = true;
		Intervention inter = new Intervention(0, 0, 0L, 60, sector, false, 1, 6, 10, comment);
		try {
			interCtrl.controlIntervention(inter);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(!valid);
	}

	/**
	 * Test le contrôle d'une intervention non-valide (secteur manquant)
	 */
	@Test
	public void controlInterventionMissingSector() {
		boolean valid = true;
		Intervention inter = new Intervention(0, 0, 1000000000L, 60, "", false, 1, 6, 10, comment);
		try {
			interCtrl.controlIntervention(inter);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(!valid);
	}

	/**
	 * Test le contrôle d'une intervention non-valide (soustype incorrect)
	 */
	@Test
	public void controlInterventionWrongSubType() {
		boolean valid = true;
		Intervention inter = new Intervention(0, 0, 1000000000L, 60, sector, false, 1, 9, 10, comment);
		try {
			interCtrl.controlIntervention(inter);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(!valid);
	}

	/**
	 * Test le contrôle d'une intervention non-valide (Null parameters)
	 */
	@Test
	public void controlInterventionNullParameters() {
		boolean valid = true;
		Intervention inter = null;
		try {
			interCtrl.controlIntervention(inter);
		} catch (WebApplicationException wae) {
			valid = false;
		}
		assertTrue(!valid);
	}

	/**
	 * Test la récupération d'une liste d'interventions valide
	 */
	@Test
	public void getInterventionsOK() {
		List<Intervention> listInter = null;
		try {
			// Mock Controller / DAO
			UserController userCtrl = Mockito.mock(UserController.class);
			Mockito.when(userCtrl.getUser("user")).thenReturn(fakeUser);
			interCtrl.setUserController(userCtrl);
			InterventionDAO interDAO = Mockito.mock(InterventionDAO.class);
			Mockito.when(interDAO.getInterventions(0, 10, true, fakeUser.getId())).thenReturn(fakeList);
			interCtrl.setInterventionDAO(interDAO);
			// Fin Mock
			listInter = interCtrl.getInterventions("user", true, 0, 10);
		} catch (TechnicalException | WebApplicationException e) {
			listInter = null;
		}
		assertTrue(listInter != null);
	}

	/**
	 * Test la récupération d'une liste d'interventions non-valide (user incorrect)
	 */
	@Test
	public void getInterventionsWrongUser() {
		List<Intervention> listInter = fakeList;
		try {
			// Mock Controller
			UserController userCtrl = Mockito.mock(UserController.class);
			Mockito.when(userCtrl.getUser(null)).thenThrow(new WebApplicationException());
			interCtrl.setUserController(userCtrl);
			// Fin Mock
			listInter = interCtrl.getInterventions(null, true, 0, 10);
		} catch (WebApplicationException wae) {
			listInter = null;
		}
		assertTrue(listInter == null);
	}

	/**
	 * Test la récupération d'une intervention valide
	 */
	@Test
	public void getInterventionOK() {
		Intervention inter = null;
		try {
			// Mock Controller / DAO
			UserController userCtrl = Mockito.mock(UserController.class);
			Mockito.when(userCtrl.getUser("user")).thenReturn(fakeUser);
			interCtrl.setUserController(userCtrl);
			InterventionDAO interDAO = Mockito.mock(InterventionDAO.class);
			Mockito.when(interDAO.getIntervention(1, 10)).thenReturn(fakeInter);
			interCtrl.setInterventionDAO(interDAO);
			// Fin Mock
			inter = interCtrl.getIntervention("user", 1);
		} catch (TechnicalException | WebApplicationException e) {
			inter = null;
		}
		assertTrue(inter != null);
	}

	/**
	 * Test la récupération d'une intervention non-valide (ID incorrect)
	 */
	@Test
	public void getInterventionWrongID() {
		Intervention inter = fakeInter;
		try {
			// Mock Controller / DAO
			UserController userCtrl = Mockito.mock(UserController.class);
			Mockito.when(userCtrl.getUser("user")).thenReturn(fakeUser);
			interCtrl.setUserController(userCtrl);
			InterventionDAO interDAO = Mockito.mock(InterventionDAO.class);
			Mockito.when(interDAO.getIntervention(999, 10)).thenReturn(null);
			interCtrl.setInterventionDAO(interDAO);
			// Fin Mock
			inter = interCtrl.getIntervention("user", 999);
		} catch (TechnicalException | WebApplicationException e) {
			inter = fakeInter;
		}
		assertTrue(inter == null);
	}

	/**
	 * Test l'ajout d'une intervention valide
	 */
	@Test
	public void addInterventionOK() {
		Intervention inter = fakeInter;
		try {
			// Mock Controller / DAO
			UserController userCtrl = Mockito.mock(UserController.class);
			Mockito.when(userCtrl.getUser("user")).thenReturn(fakeUser);
			interCtrl.setUserController(userCtrl);
			InterventionDAO interDAO = Mockito.mock(InterventionDAO.class);
			Mockito.when(interDAO.addIntervention(inter, fakeUser.getId())).thenReturn(fakeInter);
			interCtrl.setInterventionDAO(interDAO);
			// Fin Mock
			inter = interCtrl.addIntervention("user", inter);
		} catch (FonctionnalException | TechnicalException | WebApplicationException e) {
			inter = null;
		}
		assertTrue(inter != null);
	}

	/**
	 * Test l'ajout d'une intervention non-valide
	 */
	@Test
	public void addInterventionWrongUser() {
		Intervention inter = fakeInter;
		try {
			// Mock Controller / DAO
			UserController userCtrl = Mockito.mock(UserController.class);
			Mockito.when(userCtrl.getUser(null)).thenThrow(new WebApplicationException());
			interCtrl.setUserController(userCtrl);
			// Fin Mock
			inter = interCtrl.addIntervention(null, fakeInter);
		} catch (WebApplicationException wae) {
			inter = null;
		}
		assertTrue(inter == null);
	}

	/**
	 * Test la mise à jour d'une intervention valide
	 */
	@Test
	public void updateInterventionOK() {
		Intervention inter = fakeInter;
		try {
			// Mock Controller / DAO
			UserController userCtrl = Mockito.mock(UserController.class);
			Mockito.when(userCtrl.getUser("user")).thenReturn(fakeUser);
			interCtrl.setUserController(userCtrl);
			InterventionDAO interDAO = Mockito.mock(InterventionDAO.class);
			Mockito.when(interDAO.updateIntervention(inter, inter.getInterId(), fakeUser.getId())).thenReturn(fakeInter);
			interCtrl.setInterventionDAO(interDAO);
			// Fin Mock
			inter = interCtrl.updateIntervention("user", inter.getInterId(), inter);
		} catch (FonctionnalException | TechnicalException | WebApplicationException e) {
			inter = null;
		}
		assertTrue(inter != null);
	}

	/**
	 * Test la mise à jour d'une intervention non-valide (inter ID inexistante)
	 */
	@Test
	public void updateInterventionWrongInterID() {
		Intervention inter = fakeInter;
		try {
			// Mock Controller / DAO
			UserController userCtrl = Mockito.mock(UserController.class);
			Mockito.when(userCtrl.getUser("user")).thenReturn(fakeUser);
			interCtrl.setUserController(userCtrl);
			InterventionDAO interDAO = Mockito.mock(InterventionDAO.class);
			Mockito.when(interDAO.updateIntervention(inter, -1, fakeUser.getId())).thenReturn(null);
			interCtrl.setInterventionDAO(interDAO);
			// Fin Mock
			inter = interCtrl.updateIntervention("user", -1, inter);
		} catch (FonctionnalException | TechnicalException | WebApplicationException wae) {
			inter = fakeInter;
		}
		assertTrue(inter == null);
	}

	/**
	 * Test la suppression d'une intervention valide
	 */
	@Test
	public void deleteInterventionOK() {
		Intervention inter = fakeInter;
		try {
			// Mock Controller / DAO
			UserController userCtrl = Mockito.mock(UserController.class);
			Mockito.when(userCtrl.getUser("user")).thenReturn(fakeUser);
			interCtrl.setUserController(userCtrl);
			InterventionDAO interDAO = Mockito.mock(InterventionDAO.class);
			Mockito.when(interDAO.deleteIntervention(inter.getInterId(), fakeUser.getId())).thenReturn(fakeInter);
			interCtrl.setInterventionDAO(interDAO);
			// Fin Mock
			inter = interCtrl.deleteIntervention("user", inter.getInterId());
		} catch (TechnicalException | WebApplicationException e) {
			inter = null;
		}
		assertTrue(inter != null);
	}

	/**
	 * Test la suppression d'une intervention non-valide (inter ID inexistante)
	 */
	@Test
	public void deleteInterventionWrongInterID() {
		Intervention inter = fakeInter;
		try {
			// Mock Controller / DAO
			UserController userCtrl = Mockito.mock(UserController.class);
			Mockito.when(userCtrl.getUser("user")).thenReturn(fakeUser);
			interCtrl.setUserController(userCtrl);
			InterventionDAO interDAO = Mockito.mock(InterventionDAO.class);
			Mockito.when(interDAO.deleteIntervention(-1, fakeUser.getId())).thenReturn(null);
			interCtrl.setInterventionDAO(interDAO);
			// Fin Mock
			inter = interCtrl.deleteIntervention("user", -1);
		} catch (TechnicalException | WebApplicationException e) {
			inter = fakeInter;
		}
		assertTrue(inter == null);
	}

	/**
	 * Clôture
	 */
	@AfterClass
	public static void close() {
		logger.debug("### Tous les tests 'InterventionController' ont ete effectues ###");
	}

}
