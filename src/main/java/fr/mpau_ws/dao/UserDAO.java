package fr.mpau_ws.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.mpau_ws.bean.User;
import fr.mpau_ws.exception.FonctionnalException;
import fr.mpau_ws.exception.TechnicalException;
import fr.mpau_ws.tool.Database;
import fr.mpau_ws.tool.Settings;

/**
 * Classe DAO des utilisateurs
 * 
 * @author Jonathan
 * @version 1.2 (22/01/2025)
 * @since 11/11/2017
 */
public class UserDAO {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(UserDAO.class);

	/**
	 * Constructeur
	 */
	public UserDAO() {}

	/**
	 * Récupère un utilisateur via username et password
	 * 
	 * @param username
	 * @param password
	 * @return User
	 * @throws TechnicalException
	 */
	public User getUser(String username, String password) throws TechnicalException {
		User user = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = Database.getInstance().getConnection();
			pstmt = con.prepareStatement(Settings.getStringProperty("user.selectUser"));
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String pass = rs.getString("pass");
				String email = rs.getString("email");
				int nbTotalInter = rs.getInt("nb_inter");
				int interIdMax = rs.getInt("inter_id_max");
				long inscriptionDate = rs.getLong("date");
				user = new User(id, name, pass, email, nbTotalInter, interIdMax, inscriptionDate);
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
		return user;
	}

	/**
	 * Ajoute un nouvel utilisateur selon l'utilisateur fournis
	 * 
	 * @param user
	 * @return User
	 * @throws TechnicalException
	 * @throws FonctionnalException
	 */
	public User addUser(User user) throws TechnicalException, FonctionnalException {
		User addedUser = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		int rowExecuted = 0;

		try {
			logger.debug("UserDAO -> ajout d'un nouvel utilisateur");
			Calendar now = Calendar.getInstance();
			con = Database.getInstance().getConnection();
			pstmt = con.prepareStatement(Settings.getStringProperty("user.insertUser"));
			pstmt.setString(1, user.getName().trim());
			pstmt.setString(2, user.getPass().trim());
			pstmt.setString(3, user.getEmail().trim());
			rowExecuted = pstmt.executeUpdate();
			if (rowExecuted == 1) {
				addedUser = new User(0, user.getName().trim(), user.getPass().trim(), user.getEmail().trim(), 0, 0, now.getTimeInMillis());
				logger.debug("UserDAO => ajout: SUCCES");
			} else {
				logger.debug("UserDAO => ajout: ECHEC");
			}
		} catch (SQLIntegrityConstraintViolationException ex) {
			logger.error(ex.getMessage());
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
		return addedUser;
	}

	/**
	 * Met à jour le mot de passe et l'email d'un utilisateur selon son ID et le nouvel utilisateur
	 * 
	 * @param user
	 * @param userID
	 * @return User
	 * @throws TechnicalException
	 * @throws FonctionnalException
	 */
	public User updateUser(User user, int userID) throws TechnicalException, FonctionnalException {
		User updatedUser = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		int rowExecuted = 0;

		try {
			logger.debug("UserDAO -> mise à jour de l'utilisateur [" + userID + "]");
			con = Database.getInstance().getConnection();
			pstmt = con.prepareStatement(Settings.getStringProperty("user.updateUser"));
			pstmt.setString(1, user.getPass().trim());
			pstmt.setString(2, user.getEmail().trim());
			pstmt.setInt(3, userID);
			rowExecuted = pstmt.executeUpdate();
			if (rowExecuted == 1) {
				updatedUser = new User(userID, user.getName(), user.getPass().trim(), user.getEmail().trim(), user.getNbInter(), user.getInterIdMax(),
						user.getInscriptionDate());
				logger.debug("UserDAO => mise à jour: SUCCES");
			} else {
				logger.debug("UserDAO => mise à jour: ECHEC");
			}
		} catch (SQLIntegrityConstraintViolationException ex) {
			logger.error(ex.getMessage());
		} catch (NullPointerException npe) {
			throw new FonctionnalException(npe.getMessage());
		} catch (SQLException ex2) {
			throw new TechnicalException(ex2.getMessage());
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
		return updatedUser;
	}

	/**
	 * Met à jour le nombre d'interventions et l'ID de l'interventions max en fonction de l'ID utilisateur
	 * 
	 * @param userID
	 * @throws TechnicalException
	 */
	public void updateUserInter(int userID) throws TechnicalException {
		Connection con = null;
		PreparedStatement pstmt = null;
		int rowExecuted = 0;

		try {
			logger.debug("UserDAO -> met à jour le nombre total d'interventions et l'ID Max d'intervention de l'utilisateur [" + userID + "]");
			con = Database.getInstance().getConnection();
			pstmt = (PreparedStatement) con.prepareStatement(Settings.getStringProperty("user.updateUserInter"));
			pstmt.setInt(1, userID);
			pstmt.setInt(2, userID);
			pstmt.setInt(3, userID);
			rowExecuted = pstmt.executeUpdate();
			if (rowExecuted == 1) {
				logger.debug("UserDAO => mise à jour: SUCCES");
			} else {
				logger.debug("UserDAO => mise à jour: ECHEC");
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
	}

}
