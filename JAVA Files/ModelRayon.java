package com.example.listedecourse;
/**
 * La classe rayon correspond à un rayon dans la base de données
 */
public class ModelRayon {
	/**
	 * Le nom du rayon aussi appelé rayonLib dans la table rayon
	 */
	private String nomRayon;
	/**
	 * Le numéro du rayon aussi appelé rayonId dans la table rayon
	 */
	private String noRayon;
	/**
	 * Booléen indiquant si le rayon est sélectionné en vue de la suppression
	 */
	private boolean selected;
	/**
	 *
	 * @param no numéro du rayon
	 * @param nom nom du rayon
	 */
	public ModelRayon(String no,String nom){
	  nomRayon=nom;
	  noRayon=no;
	  selected=false;
	}
	/**
	 * Accesseur en lecture de la propriété nomRayon
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
