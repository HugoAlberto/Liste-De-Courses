package com.example.listedecourse;
/**
 * La classe rayon correspond à une liste dans la base de données
 */
public class ModelListe {
	private String nomListe;
	private String noListe;
	private String noQte;
	private boolean selected;
	/**
	 * @param List number
	 * @param List name
	 * @param Quantity
	 */
	public ModelListe(String no,String nom, String qte){
	  nomListe=nom;
	  noListe=no;
	  noQte=qte;
	  selected=false;
	}
	/**
	 * Get list name
	 * @return List name
	 */
	public String getNom() {
		return nomListe;
	}
	/**
	 * Get quantity
	 * @return Quantity
	 */
	public String getQte() {
		return noQte;
	}
	/**
	 * Get list number
	 * @return List Number
	 */
	public String getNo() {
		return noListe;
	}
	/**
	 * Set name
	 * @param Name
	 */
	public void setNom(String nom) {
		nomListe = nom;
	}
	/**
	 * Set number
	 * @param Number
	 */
	public void setNo(String no) {
		noListe = no;
	}
	/**
	 * Set Quantity
	 * @param Quantity
	 */
	public void setQte(String qte) {
		noQte = qte;
	}
	/**
	 * Is selected ?
	 * @return isSelected
	 */
	public boolean isSelected() {
		return selected;
	}
	/**
	 * Set selected
	 * @param Selected
	 */
	public void setSelected(boolean selectionne) {
		this.selected = selectionne;
	}
}
