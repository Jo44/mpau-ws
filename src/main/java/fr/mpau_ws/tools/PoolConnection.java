package fr.mpau_ws.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe en charge des connexions à la base de données
 * 
 * @author Jonathan
 * @version 1.0
 * @since 11/11/2017
 *
 */
public class PoolConnection {
	private static Connection cnx = null;
	private static String url = null;
	private static String user = null;
	private static String password = null;

	static {
		try {
			Class.forName(Settings.getProperty("driver"));
			url = Settings.getProperty("url");
			user = Settings.getProperty("user");
			password = Settings.getProperty("password");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NullPointerException e2) {
			e2.printStackTrace();
		}
	}

	/**
	 * Récupère une connection prête à l'emploi
	 * 
	 * @return Connection
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		if (cnx == null || cnx.isClosed())
			cnx = DriverManager.getConnection(url, user, password);
		return cnx;
	}

	/**
	 * Ferme la connection
	 * 
	 * @throws SQLException
	 */
	public static void closeConnection() throws SQLException {
		if (cnx != null)
			cnx.close();
		if (cnx != null)
			cnx = null;
	}

	/**
	 * Test la connection
	 * 
	 * @return true si OK, false si KO
	 * @throws SQLException
	 */
	public static boolean testConnection() throws SQLException {
		boolean retour = false;
		try {
			cnx = DriverManager.getConnection(url, user, password);
			retour = true;
			if (cnx != null) {
				try {
					cnx.close();
				} catch (SQLException e) {
					throw e;
				} finally {
					cnx = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return retour;
	}

}
