package com.example.listedecourse;

public class ModelArticle {
	private String nomArticle;
	private String noArticle;
	private String qteAAcheter;
	private boolean selected;
	public ModelArticle(String no, String nom, String qte){
	  nomArticle=nom;
	  noArticle=no;
	  qteAAcheter=qte;
	  selected=false;
	}
	public String getNom() {
	    return nomArticle;
	}

	public void setNom(String nom) {
	    nomArticle = nom;
	}
	
	public String getNo() {
		return noArticle;
	}

	public void setNo(String no) {
		noArticle = no;
	}
		  
	public String getQte() {
		return qteAAcheter;
	}
		  
	public void setQte(String qte) {
		qteAAcheter = qte;
	}  
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selectionne) {
		this.selected = selectionne;
	}
}
