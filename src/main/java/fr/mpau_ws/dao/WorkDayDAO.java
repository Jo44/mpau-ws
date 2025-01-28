package fr.mpau_ws.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mariadb.jdbc.ClientPreparedStatement;

import fr.mpau_ws.bean.WorkDay;
import fr.mpau_ws.exception.TechnicalException;
import fr.mpau_ws.tool.Database;
import fr.mpau_ws.tool.Settings;

/**
 * Classe DAO des workdays
 * 
 * @author Jonathan
 * @version 1.3 (22/01/2025)
 * @since 11/11/2017
 */
public class WorkDayDAO {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(WorkDayDAO.class);

	/**
	 * Constructeur
	 */
	public WorkDayDAO() {}

	/**
	 * Récupère la WorkDay non terminée selon l'ID utilisateur
	 * 
	 * @param userID
	 * @return WorkDay
	 * @throws TechnicalException
	 */
	public WorkDay getWorkDayNotCompleted(int userID) throws TechnicalException {
		WorkDay wd = null;
		Connection con = null;
		ClientPreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			logger.debug("WorkDayDAO -> récupération de la workday non-finie de l'utilisateur [" + userID + "]");
			con = Database.getInstance().getConnection();
			pstmt = (ClientPreparedStatement) con.prepareStatement(Settings.getStringProperty("wd.selectNotCompletedWorkDay"));
			pstmt.setInt(1, userID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id");
				long start = rs.getLong("date_start");
				long stop = rs.getLong("date_stop");
				wd = new WorkDay(id, userID, start, stop, false);
				logger.debug("WorkDayDAO => récupération: SUCCES");
			} else {
				logger.debug("WorkDayDAO => récupération: ECHEC");
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
		return wd;
	}

	/**
	 * Récupère la liste des WorkDays selon l'ID utilisateur
	 * 
	 * @param userID
	 * @return List<WorkDay>
	 * @throws TechnicalException
	 */
	public List<WorkDay> getAllWorkDays(int userID) throws TechnicalException {
		List<WorkDay> listWd = new ArrayList<>();
		Connection con = null;
		ClientPreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			logger.debug("WorkDayDAO -> récupération de la liste des workdays de l'utilisateur [" + userID + "]");
			con = Database.getInstance().getConnection();
			pstmt = (ClientPreparedStatement) con.prepareStatement(Settings.getStringProperty("wd.selectAllWorkDays"));
			pstmt.setInt(1, userID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				long start = rs.getLong("date_start");
				long stop = rs.getLong("date_stop");
				boolean finished = rs.getBoolean("finished");
				WorkDay wd = new WorkDay(id, userID, start, stop, finished);
				listWd.add(wd);
			}
			if (listWd.size() > 0) {
				logger.debug("WorkDayDAO => récupération: SUCCES");
			} else {
				listWd = null;
				logger.debug("WorkDayDAO => récupération: ECHEC");
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
		return listWd;
	}

	/**
	 * Récupère la liste des WorkDays selon l'ID utilisateur, le mois et l'année voulu
	 * 
	 * @param userID
	 * @param month
	 * @param year
	 * @return List<WorkDay>
	 * @throws TechnicalException
	 */
	public List<WorkDay> getAllWorkDaysForMonth(int userID, int month, int year) throws TechnicalException {
		List<WorkDay> listWd = new ArrayList<>();
		Connection con = null;
		ClientPreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			logger.debug("WorkDayDAO -> récupération de la liste des workdays de l'utilisateur [" + userID + "] pour le mois [" + month
					+ "] de l'année [" + year + "]");
			con = Database.getInstance().getConnection();
			pstmt = (ClientPreparedStatement) con.prepareStatement(Settings.getStringProperty("wd.selectAllWorkDaysForMonth"));
			pstmt.setInt(1, userID);
			pstmt.setInt(2, month);
			pstmt.setInt(3, year);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				long start = rs.getLong("date_start");
				long stop = rs.getLong("date_stop");
				boolean finished = rs.getBoolean("finished");
				WorkDay wd = new WorkDay(id, userID, start, stop, finished);
				listWd.add(wd);
			}
			if (listWd.size() > 0) {
				logger.debug("WorkDayDAO => récupération: SUCCES");
			} else {
				listWd = null;
				logger.debug("WorkDayDAO => récupération: ECHEC");
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
		return listWd;
	}

	/**
	 * Récupère la WorkDay selon l'ID et l'ID utilisateur
	 * 
	 * @param wdID
	 * @param userID
	 * @return WorkDay
	 * @throws TechnicalException
	 */
	public WorkDay getWorkDay(int wdID, int userID) throws TechnicalException {
		WorkDay wd = null;
		Connection con = null;
		ClientPreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			logger.debug("WorkDayDAO -> récupération de la workday [" + wdID + "] de l'utilisateur [" + userID + "]");
			con = Database.getInstance().getConnection();
			pstmt = (ClientPreparedStatement) con.prepareStatement(Settings.getStringProperty("wd.selectWorkDay"));
			pstmt.setInt(1, wdID);
			pstmt.setInt(2, userID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				long start = rs.getLong("date_start");
				long stop = rs.getLong("date_stop");
				boolean finished = rs.getBoolean("finished");
				wd = new WorkDay(wdID, userID, start, stop, finished);
				logger.debug("WorkDayDAO => récupération: SUCCES");
			} else {
				logger.debug("WorkDayDAO => récupération: ECHEC");
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
		return wd;
	}

	/**
	 * Ajout un nouveau WorkDay selon l'ID utilisateur
	 * 
	 * @param wdStart
	 * @param userID
	 * @return WorkDay
	 * @throws TechnicalException
	 */
	public WorkDay startWorkDay(long wdStart, int userID) throws TechnicalException {
		WorkDay addedWd = null;
		Connection con = null;
		ClientPreparedStatement pstmt = null;
		int rowExecuted = 0;

		try {
			logger.debug("WorkDayDAO -> ouverture d'une nouvelle workday pour l'utilisateur [" + userID + "]");
			con = Database.getInstance().getConnection();
			pstmt = (ClientPreparedStatement) con.prepareStatement(Settings.getStringProperty("wd.startWorkDay"));
			pstmt.setInt(1, userID);
			pstmt.setLong(2, wdStart);
			rowExecuted = pstmt.executeUpdate();
			if (rowExecuted == 1) {
				addedWd = new WorkDay(0, userID, wdStart, 0L, false);
				logger.debug("WorkDayDAO => ajout: SUCCES");
			} else {
				logger.debug("WorkDayDAO => ajout: ECHEC");
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
		return addedWd;
	}

	/**
	 * Met à jour une WorkDay pour la clôturer selon son ID
	 * 
	 * @param wdStop
	 * @param wdID
	 * @return WorkDay
	 * @throws TechnicalException
	 */
	public WorkDay stopWorkDay(long wdStop, int wdID) throws TechnicalException {
		WorkDay updatedWd = null;
		Connection con = null;
		ClientPreparedStatement pstmt = null;
		int rowExecuted = 0;

		try {
			logger.debug("WorkDayDAO -> clôture de la workday [" + wdID + "]");
			con = Database.getInstance().getConnection();
			pstmt = (ClientPreparedStatement) con.prepareStatement(Settings.getStringProperty("wd.finishWorkDay"));
			pstmt.setLong(1, wdStop);
			pstmt.setInt(2, wdID);
			rowExecuted = pstmt.executeUpdate();
			if (rowExecuted == 1) {
				updatedWd = new WorkDay(wdID, 0, wdStop, wdStop, true);
				logger.debug("WorkDayDAO => mise à jour: SUCCES");
			} else {
				logger.debug("WorkDayDAO => mise à jour: ECHEC");
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
		return updatedWd;
	}

	/**
	 * Supprime la WorkDay selon l'ID de la WorkDay et l'ID utilisateur
	 * 
	 * @param wdID
	 * @param userID
	 * @return WorkDay
	 * @throws TechnicalException
	 */
	public WorkDay deleteWorkDay(int wdID, int userID) throws TechnicalException {
		WorkDay deletedWd = null;
		Connection con = null;
		ClientPreparedStatement pstmt = null;
		int rowExecuted = 0;

		try {
			logger.debug("WorkDayDAO -> suppression de la workday [" + wdID + "] de l'utilisateur [" + userID + "]");
			WorkDay tmpWd = getWorkDay(wdID, userID);
			con = Database.getInstance().getConnection();
			pstmt = (ClientPreparedStatement) con.prepareStatement(Settings.getStringProperty("wd.deleteWorkDay"));
			pstmt.setInt(1, wdID);
			pstmt.setInt(2, userID);
			rowExecuted = pstmt.executeUpdate();
			if (rowExecuted == 1) {
				deletedWd = tmpWd;
				logger.debug("WorkDayDAO => suppression: SUCCES");
			} else {
				logger.debug("WorkDayDAO => suppression: ECHEC");
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
		return deletedWd;
	}

}
