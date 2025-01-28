package fr.mpau_ws.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.mpau_ws.bean.Intervention;
import fr.mpau_ws.exception.FonctionnalException;
import fr.mpau_ws.exception.TechnicalException;
import fr.mpau_ws.tool.Database;
import fr.mpau_ws.tool.Settings;

/**
 * Classe DAO des interventions
 * 
 * @author Jonathan
 * @version 1.4 (22/01/2025)
 * @since 11/11/2017
 */
public class InterventionDAO {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(InterventionDAO.class);

	/**
	 * Constructeur
	 */
	public InterventionDAO() {}

	/**
	 * Récupère une liste de X interventions maximum à partir de l'ID de l'intervention de début, du nombre d'interventions voulus, du sens de
	 * récupération et de l'ID utilisateur
	 * 
	 * @param inter_id
	 * @param nb_inter
	 * @param next
	 * @param user_id
	 * @return List<Intervention>
	 * @throws TechnicalException
	 */
	public List<Intervention> getInterventions(int inter_id, int nb_inter, boolean next, int user_id) throws TechnicalException {
		List<Intervention> listInter = new ArrayList<Intervention>();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// Récupère la requête SQL et log en fonction du boolean next
		String selectInterventions = null;
		if (next) {
			selectInterventions = Settings.getStringProperty("inter.selectNextInterventions");
			logger.debug("InterventionDAO -> récupération de " + nb_inter + " interventions suivant l'intervention [" + inter_id
					+ "] pour l'utilisateur [" + user_id + "]");
		} else {
			selectInterventions = Settings.getStringProperty("inter.selectPreviousInterventions");
			logger.debug("InterventionDAO -> récupération de " + nb_inter + " interventions précédent l'intervention [" + inter_id
					+ "] pour l'utilisateur [" + user_id + "]");
		}
		try {
			con = Database.getInstance().getConnection();
			pstmt = (PreparedStatement) con.prepareStatement(selectInterventions);
			pstmt.setInt(1, user_id);
			pstmt.setInt(2, inter_id);
			pstmt.setInt(3, nb_inter);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				long date = rs.getLong("date");
				int duree = rs.getInt("duree");
				String secteur = rs.getString("secteur");
				boolean smur = rs.getBoolean("smur");
				int type = rs.getInt("maintype_id");
				int sousType = rs.getInt("soustype_id");
				int agePatient = rs.getInt("agepatient_id");
				String commentaire = rs.getString("commentaire");
				Intervention intervention = new Intervention(id, user_id, date, duree, secteur, smur, type, sousType, agePatient, commentaire);
				listInter.add(intervention);
			}
			if (listInter.size() > 0) {
				logger.debug("InterventionDAO => récupération: SUCCES");
			} else {
				listInter = null;
				logger.debug("InterventionDAO => récupération: ECHEC");
			}
		} catch (SQLException ex) {
			throw new TechnicalException(ex.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException ex) {
				throw new TechnicalException(ex.getMessage());
			}
		}
		return listInter;
	}

	/**
	 * Récupère une intervention selon son ID et l'ID utilisateur
	 * 
	 * @param interID
	 * @param userID
	 * @return Intervention
	 * @throws TechnicalException
	 */
	public Intervention getIntervention(int interID, int userID) throws TechnicalException {
		Intervention intervention = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			logger.debug("InterventionDAO -> récupération de l'intervention [" + interID + "] pour l'utilisateur [" + userID + "]");
			con = Database.getInstance().getConnection();
			pstmt = (PreparedStatement) con.prepareStatement(Settings.getStringProperty("inter.selectIntervention"));
			pstmt.setInt(1, interID);
			pstmt.setInt(2, userID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				long date = rs.getLong("date");
				int duree = rs.getInt("duree");
				String secteur = rs.getString("secteur");
				boolean smur = rs.getBoolean("smur");
				int type = rs.getInt("maintype_id");
				int sousType = rs.getInt("soustype_id");
				int agePatient = rs.getInt("agepatient_id");
				String commentaire = rs.getString("commentaire");
				intervention = new Intervention(interID, userID, date, duree, secteur, smur, type, sousType, agePatient, commentaire);
				logger.debug("InterventionDAO => récupération: SUCCES");
			} else {
				logger.debug("InterventionDAO => récupération: ECHEC");
			}
		} catch (SQLException ex) {
			throw new TechnicalException(ex.getMessage());
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException ex) {
				throw new TechnicalException(ex.getMessage());
			}
		}
		return intervention;
	}

	/**
	 * Ajout une intervention selon l'ID utilisateur et la nouvelle intervention
	 * 
	 * @param inter
	 * @param userID
	 * @return Intervention
	 * @throws TechnicalException
	 * @throws FonctionnalException
	 */
	public Intervention addIntervention(Intervention inter, int userID) throws TechnicalException, FonctionnalException {
		Intervention addedInter = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		int rowExecuted = 0;

		try {
			logger.debug("InterventionDAO -> ajout d'une intervention pour l'utilisateur [" + userID + "]");
			con = Database.getInstance().getConnection();
			pstmt = (PreparedStatement) con.prepareStatement(Settings.getStringProperty("inter.insertIntervention"));
			pstmt.setInt(1, userID);
			pstmt.setInt(2, userID);
			pstmt.setLong(3, inter.getDateInter());
			pstmt.setInt(4, inter.getDuree());
			pstmt.setString(5, inter.getSecteur().trim());
			pstmt.setBoolean(6, inter.isSmur());
			pstmt.setInt(7, inter.getMainTypeId());
			pstmt.setInt(8, inter.getSousTypeId());
			pstmt.setInt(9, inter.getAgePatientId());
			if (inter.getCommentaire() != null) {
				pstmt.setString(10, inter.getCommentaire().trim());
			} else {
				pstmt.setString(10, null);
			}
			rowExecuted = pstmt.executeUpdate();
			if (rowExecuted == 1) {
				addedInter = inter;
				logger.debug("InterventionDAO => ajout: SUCCES");
			} else {
				logger.debug("InterventionDAO => ajout: ECHEC");
			}
		} catch (NullPointerException npe) {
			throw new FonctionnalException(npe.getMessage());
		} catch (SQLException ex) {
			logger.error(ex.getMessage());
			throw new TechnicalException(ex.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException ex) {
				throw new TechnicalException(ex.getMessage());
			}
		}
		return addedInter;
	}

	/**
	 * Met à jour une intervention selon son ID, l'ID utilisateur et la nouvelle intervention
	 * 
	 * @param inter
	 * @param interID
	 * @param userID
	 * @return Intervention
	 * @throws TechnicalException
	 * @throws FonctionnalException
	 */
	public Intervention updateIntervention(Intervention inter, int interID, int userID) throws TechnicalException, FonctionnalException {
		Intervention updatedInter = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		int rowExecuted = 0;

		try {
			logger.debug("InterventionDAO -> mise à jour de l'intervention [" + interID + "] pour l'utilisateur [" + userID + "]");
			con = Database.getInstance().getConnection();
			pstmt = (PreparedStatement) con.prepareStatement(Settings.getStringProperty("inter.updateIntervention"));
			pstmt.setLong(1, inter.getDateInter());
			pstmt.setInt(2, inter.getDuree());
			pstmt.setString(3, inter.getSecteur().trim());
			pstmt.setBoolean(4, inter.isSmur());
			pstmt.setInt(5, inter.getMainTypeId());
			pstmt.setInt(6, inter.getSousTypeId());
			pstmt.setInt(7, inter.getAgePatientId());
			if (inter.getCommentaire() != null) {
				pstmt.setString(8, inter.getCommentaire().trim());
			} else {
				pstmt.setString(8, null);
			}
			pstmt.setInt(9, interID);
			pstmt.setInt(10, userID);
			rowExecuted = pstmt.executeUpdate();
			if (rowExecuted == 1) {
				updatedInter = new Intervention(interID, userID, inter.getDateInter(), inter.getDuree(), inter.getSecteur(), inter.isSmur(),
						inter.getMainTypeId(), inter.getSousTypeId(), inter.getAgePatientId(), inter.getCommentaire());
				logger.debug("InterventionDAO => mise à jour: SUCCES");
			} else {
				logger.debug("InterventionDAO => mise à jour: ECHEC");
			}
		} catch (NullPointerException npe) {
			throw new FonctionnalException(npe.getMessage());
		} catch (SQLException ex) {
			throw new TechnicalException(ex.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException ex) {
				throw new TechnicalException(ex.getMessage());
			}
		}
		return updatedInter;
	}

	/**
	 * Supprime une intervention selon son ID et l'ID utilisateur
	 * 
	 * @param interID
	 * @param userID
	 * @return Intervention
	 * @throws TechnicalException
	 */
	public Intervention deleteIntervention(int interID, int userID) throws TechnicalException {
		Intervention deletedInter = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		int rowExecuted = 0;

		try {
			logger.debug("InterventionDAO -> suppression de l'intervention [" + interID + "] pour l'utilisateur [" + userID + "]");
			Intervention tmpInter = getIntervention(interID, userID);
			con = Database.getInstance().getConnection();
			pstmt = (PreparedStatement) con.prepareStatement(Settings.getStringProperty("inter.deleteIntervention"));
			pstmt.setInt(1, interID);
			pstmt.setInt(2, userID);
			rowExecuted = pstmt.executeUpdate();
			if (rowExecuted == 1) {
				deletedInter = tmpInter;
				logger.debug("InterventionDAO => suppression: SUCCES");
			} else {
				logger.debug("InterventionDAO => suppression: ECHEC");
			}
		} catch (SQLException ex) {
			throw new TechnicalException(ex.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException ex) {
				throw new TechnicalException(ex.getMessage());
			}
		}
		return deletedInter;
	}

}
