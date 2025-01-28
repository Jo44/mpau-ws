package fr.mpau_ws.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mariadb.jdbc.ClientPreparedStatement;

import fr.mpau_ws.bean.WorkPeriod;
import fr.mpau_ws.exception.TechnicalException;
import fr.mpau_ws.tool.Database;
import fr.mpau_ws.tool.Settings;

/**
 * Classe DAO des workperiods
 * 
 * @author Jonathan
 * @version 1.2 (22/01/2025)
 * @since 11/11/2017
 */
public class WorkPeriodDAO {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(WorkPeriodDAO.class);

	/**
	 * Constructeur
	 */
	public WorkPeriodDAO() {}

	/**
	 * Récupère la WorkPeriod non terminée selon l'ID de la WorkDay
	 * 
	 * @param wdID
	 * @return WorkPeriod
	 * @throws TechnicalException
	 */
	public WorkPeriod getWorkPeriodNotCompleted(int wdID) throws TechnicalException {
		WorkPeriod wp = null;
		Connection con = null;
		ClientPreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			logger.debug("WorkPeriodDAO -> récupération de la workperiod non-finie de la workday [" + wdID + "]");
			con = Database.getInstance().getConnection();
			pstmt = (ClientPreparedStatement) con.prepareStatement(Settings.getStringProperty("wp.selectNotCompletedWorkPeriod"));
			pstmt.setInt(1, wdID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id");
				long start = rs.getLong("date_start");
				long stop = rs.getLong("date_stop");
				wp = new WorkPeriod(id, wdID, start, stop, false);
				logger.debug("WorkPeriodDAO => récupération: SUCCES");
			} else {
				logger.debug("WorkPeriodDAO => récupération: ECHEC");
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
		return wp;
	}

	/**
	 * Récupère la liste des WorkPeriods selon l'ID de la WorkDay
	 * 
	 * @param wdID
	 * @return List<WorkPeriod>
	 * @throws TechnicalException
	 */
	public List<WorkPeriod> getAllWorkPeriods(int wdID) throws TechnicalException {
		List<WorkPeriod> listWp = new ArrayList<WorkPeriod>();
		Connection con = null;
		ClientPreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			logger.debug("WorkPeriodDAO -> récupération de la liste des workperiods pour la workday [" + wdID + "]");
			con = Database.getInstance().getConnection();
			pstmt = (ClientPreparedStatement) con.prepareStatement(Settings.getStringProperty("wp.selectWorkPeriods"));
			pstmt.setInt(1, wdID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				long start = rs.getLong("date_start");
				long stop = rs.getLong("date_stop");
				boolean finished = rs.getBoolean("finished");
				WorkPeriod wp = new WorkPeriod(id, wdID, start, stop, finished);
				listWp.add(wp);
			}
			if (listWp.size() > 0) {
				logger.debug("WorkPeriodDAO => récupération: SUCCES");
			} else {
				listWp = null;
				logger.debug("WorkPeriodDAO => récupération: ECHEC");
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
		return listWp;
	}

	/**
	 * Ajout une nouvelle WorkPeriod selon l'ID de la WorkDay
	 * 
	 * @param wpStart
	 * @param wdID
	 * @return WorkPeriod
	 * @throws TechnicalException
	 */
	public WorkPeriod startWorkPeriod(long wpStart, int wdID) throws TechnicalException {
		WorkPeriod addedWp = null;
		Connection con = null;
		ClientPreparedStatement pstmt = null;
		int rowExecuted = 0;

		try {
			logger.debug("WorkPeriodDAO -> ouverture d'une nouvelle workperiod pour la workday [" + wdID + "]");
			con = Database.getInstance().getConnection();
			pstmt = (ClientPreparedStatement) con.prepareStatement(Settings.getStringProperty("wp.startWorkPeriod"));
			pstmt.setInt(1, wdID);
			pstmt.setLong(2, wpStart);
			rowExecuted = pstmt.executeUpdate();
			if (rowExecuted == 1) {
				addedWp = new WorkPeriod(0, wdID, wpStart, 0L, false);
				logger.debug("WorkPeriodDAO => ajout: SUCCES");
			} else {
				logger.debug("WorkPeriodDAO => ajout: ECHEC");
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
		return addedWp;
	}

	/**
	 * Met à jour une WorkPeriod pour la clôturer selon son ID
	 * 
	 * @param wpStop
	 * @param wpID
	 * @return WorkPeriod
	 * @throws TechnicalException
	 */
	public WorkPeriod stopWorkPeriod(long wpStop, int wpID) throws TechnicalException {
		WorkPeriod updatedWp = null;
		Connection con = null;
		ClientPreparedStatement pstmt = null;
		int rowExecuted = 0;

		try {
			logger.debug("WorkPeriodDAO -> clôture de la workperiod [" + wpID + "]");
			con = Database.getInstance().getConnection();
			pstmt = (ClientPreparedStatement) con.prepareStatement(Settings.getStringProperty("wp.finishWorkPeriod"));
			pstmt.setLong(1, wpStop);
			pstmt.setInt(2, wpID);
			rowExecuted = pstmt.executeUpdate();
			if (rowExecuted == 1) {
				updatedWp = new WorkPeriod(wpID, 0, wpStop, wpStop, true);
				logger.debug("WorkPeriodDAO => mise à jour: SUCCES");
			} else {
				logger.debug("WorkPeriodDAO => mise à jour: ECHEC");
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
		return updatedWp;
	}

	/**
	 * Supprime les WorkPeriods selon l'ID de la WorkDay
	 * 
	 * @param wdID
	 * @return List<WorkPeriod>
	 * @throws TechnicalException
	 */
	public List<WorkPeriod> deleteWorkPeriods(int wdID) throws TechnicalException {
		List<WorkPeriod> deletedListWp = null;
		Connection con = null;
		ClientPreparedStatement pstmt = null;
		int rowExecuted = 0;

		try {
			logger.debug("WorkPeriodDAO -> suppression des workperiods pour la workday [" + wdID + "]");
			List<WorkPeriod> tmpListWp = getAllWorkPeriods(wdID);
			con = Database.getInstance().getConnection();
			pstmt = (ClientPreparedStatement) con.prepareStatement(Settings.getStringProperty("wp.deleteWorkPeriods"));
			pstmt.setInt(1, wdID);
			rowExecuted = pstmt.executeUpdate();
			if (rowExecuted >= 1) {
				deletedListWp = tmpListWp;
				logger.debug("WorkPeriodDAO => suppression: SUCCES");
			} else {
				logger.debug("WorkPeriodDAO => suppression: ECHEC");
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
		return deletedListWp;
	}

}
