package fr.mpau_ws.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe en charge des connexions à la base de données MariaDB
 * 
 * @author Jonathan
 * @version 1.0 (22/01/2025)
 * @since 22/01/2025
 */
public class Database {

	/**
	 * Attributs
	 */

	private static final Logger logger = LogManager.getLogger(Database.class);
	private static Database instance;
	private static Connection cnx = null;
	private static String url = null;
	private static String user = null;
	private static String password = null;

	/**
	 * Constructeur
	 */
	private Database() {
		// Initialisation des paramètres
		initParameters();
	}

	/**
	 * Initialisation des paramètres
	 */
	private void initParameters() {
		try {
			Class.forName(Settings.getStringProperty("driver"));
			url = Settings.getStringProperty("url");
			user = Settings.getStringProperty("user");
			password = Settings.getStringProperty("password");
		} catch (Exception ex) {
			logger.error("Erreur lors de la récupération des paramètres de la base de données");
			logger.error(ex.getMessage());
		}
	}

	/**
	 * Récupère une connection prête à l'emploi
	 * 
	 * @return Connection
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		if (cnx == null || cnx.isClosed())
			cnx = DriverManager.getConnection(url, user, password);
		return cnx;
	}

	/**
	 * Ferme la connection
	 * 
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException {
		if (cnx != null)
			cnx.close();
		if (cnx != null)
			cnx = null;
	}

	/**
	 * Test la connection
	 * 
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean testConnection() throws SQLException {
		boolean retour = false;
		try {
			cnx = DriverManager.getConnection(url, user, password);
			retour = true;
			if (cnx != null) {
				try {
					cnx.close();
				} catch (SQLException sqlex) {
					throw sqlex;
				} finally {
					cnx = null;
				}
			}
		} catch (SQLException sqlex) {
			sqlex.printStackTrace();
			throw sqlex;
		}
		return retour;
	}

	/**
	 * Récupère l'unique instance de Database (Singleton)
	 * 
	 * @return Database
	 */
	public static synchronized Database getInstance() {
		if (instance == null) {
			instance = new Database();
		}
		return instance;
	}

}
