package fr.mpau_ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modèle des utilisateurs
 * 
 * @author Jonathan
 * @version 1.1 (28/01/2018)
 * @since 11/11/2017
 */

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

	/**
	 * Attributs
	 */
	private int id;
	private String name;
	private String pass;
	private String email;
	private int nbTotalInter;
	private int interIdMax;
	private long inscriptionDate;

	/**
	 * Constructeurs
	 */
	public User() {}

	public User(int id, String name, String pass, String email, int nbTotalInter, int interIdMax, long inscriptionDate) {
		this.id = id;
		this.name = name;
		this.pass = pass;
		this.email = email;
		this.nbTotalInter = nbTotalInter;
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
	 * Getters / Setters
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

	public int getNbTotalInter() {
		return nbTotalInter;
	}

	public int getInterIdMax() {
		return interIdMax;
	}

	public long getInscriptionDate() {
		return inscriptionDate;
	}

}
