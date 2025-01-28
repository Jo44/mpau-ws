package fr.mpau_ws.bean;

/**
 * Classe modèle des interventions
 * 
 * @author Jonathan
 * @version 1.3 (22/01/2025)
 * @since 11/11/2017
 */
public class Intervention {

	/**
	 * Attributs
	 */

	private int id;
	private int userInter;
	private long dateInter;
	private int duree;
	private String secteur;
	private boolean smur;
	private int mainTypeId;
	private int sousTypeId;
	private int agePatientId;
	private String commentaire;

	/**
	 * Constructeurs
	 */
	public Intervention() {}

	public Intervention(int id, int userInter, long dateInter, int duree, String secteur, boolean smur, int mainTypeId, int sousTypeId,
			int agePatientId, String commentaire) {
		this.id = id;
		this.userInter = userInter;
		this.dateInter = dateInter;
		this.duree = duree;
		this.secteur = secteur;
		this.smur = smur;
		this.mainTypeId = mainTypeId;
		this.sousTypeId = sousTypeId;
		this.agePatientId = agePatientId;
		this.commentaire = commentaire;
	}

	/**
	 * Getters
	 */

	public int getId() {
		return id;
	}

	public int getUserInter() {
		return userInter;
	}

	public long getDateInter() {
		return dateInter;
	}

	public int getDuree() {
		return duree;
	}

	public String getSecteur() {
		return secteur;
	}

	public boolean isSmur() {
		return smur;
	}

	public int getMainTypeId() {
		return mainTypeId;
	}

	public int getSousTypeId() {
		return sousTypeId;
	}

	public int getAgePatientId() {
		return agePatientId;
	}

	public String getCommentaire() {
		return commentaire;
	}

}
