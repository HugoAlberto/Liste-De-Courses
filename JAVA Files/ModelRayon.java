package com.example.listedecourse;
/**
 * La classe rayon correspond � un rayon dans la base de donn�es
 */
public class ModelRayon {
	/**
	 * Le nom du rayon aussi appel� rayonLib dans la table rayon
	 */
	private String nomRayon;
	/**
	 * Le num�ro du rayon aussi appel� rayonId dans la table rayon
	 */
	private String noRayon;
	/**
	 * Bool�en indiquant si le rayon est s�lectionn� en vue de la suppression
	 */
	private boolean selected;
	/**
	 *
	 * @param no num�ro du rayon
	 * @param nom nom du rayon
	 */
	public ModelRayon(String no,String nom){
	  nomRayon=nom;
	  noRayon=no;
	  selected=false;
	}
	/**
	 * Accesseur en lecture de la propri�t� nomRayon
	 * @return Le nom du rayon
	 */
	public String getNom() {
	    return nomRayon;
	  }

	  public void setNom(String nom) {
	    nomRayon = nom;
	  }
	  public String getNo() {
		return noRayon;
	  }

	  public void setNo(String no) {
		noRayon = no;
	  }
	  public boolean isSelected() {
	    return selected;
	  }

	  public void setSelected(boolean selectionne) {
	    this.selected = selectionne;
	  }

}
