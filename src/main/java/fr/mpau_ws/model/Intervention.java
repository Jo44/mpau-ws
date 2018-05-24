package fr.mpau_ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe modèle des interventions
 * 
 * @author Jonathan
 * @version 1.2 (28/01/2018)
 * @since 11/11/2017
 */

@XmlRootElement(name = "intervention")
@XmlAccessorType(XmlAccessType.FIELD)
public class Intervention {

	/**
	 * Attributs
	 */
	private int interId;
	private int interUserId;
	private long interDate;
	private int interDuree;
	private String interSecteur;
	private boolean interSmur;
	private int interTypeId;
	private int interSoustypeId;
	private int interAgepatientId;
	private String interCommentaire;

	/**
	 * Constructeurs
	 */
	public Intervention() {}

	public Intervention(int interId, int interUserId, long interDate, int interDuree, String interSecteur, boolean interSmur, int interTypeId,
			int interSoustypeId, int interAgepatientId, String interCommentaire) {
		this.interId = interId;
		this.interUserId = interUserId;
		this.interDate = interDate;
		this.interDuree = interDuree;
		this.interSecteur = interSecteur;
		this.interSmur = interSmur;
		this.interTypeId = interTypeId;
		this.interSoustypeId = interSoustypeId;
		this.interAgepatientId = interAgepatientId;
		this.interCommentaire = interCommentaire;
	}

	/**
	 * Getters / Setters
	 */
	public int getInterId() {
		return interId;
	}

	public int getInterUserId() {
		return interUserId;
	}

	public long getInterDate() {
		return interDate;
	}

	public int getInterDuree() {
		return interDuree;
	}

	public String getInterSecteur() {
		return interSecteur;
	}

	public boolean getInterSmur() {
		return interSmur;
	}

	public int getInterTypeId() {
		return interTypeId;
	}

	public int getInterSoustypeId() {
		return interSoustypeId;
	}

	public int getInterAgepatientId() {
		return interAgepatientId;
	}

	public String getInterCommentaire() {
		return interCommentaire;
	}

}
