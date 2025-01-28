package fr.mpau_ws.bean;

/**
 * Classe modèle des utilisateurs
 * 
 * @author Jonathan
 * @version 1.2 (22/01/2025)
 * @since 11/11/2017
 */
public class User {

	/**
	 * Attributs
	 */

	private int id;
	private String name;
	private String pass;
	private String email;
	private int nbInter;
	private int interIdMax;
	private long inscriptionDate;

	/**
	 * Constructeurs
	 */
	public User() {}

	public User(int id, String name, String pass, String email, int nbInter, int interIdMax, long inscriptionDate) {
		this.id = id;
		this.name = name;
		this.pass = pass;
		this.email = email;
		this.nbInter = nbInter;
		this.interIdMax = interIdMax;
		this.inscriptionDate = inscriptionDate;
	}

	/**
	 * Permet de remplacer le password par des étoiles pour éviter son affichage dans les réponses du webservice
	 */
	public void hidePass() {
		this.pass = "********";
	}

	/**
	 * Getters
	 */

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPass() {
		return pass;
	}

	public String getEmail() {
		return email;
	}

	public int getNbInter() {
		return nbInter;
	}

	public int getInterIdMax() {
		return interIdMax;
	}

	public long getInscriptionDate() {
		return inscriptionDate;
	}

}
