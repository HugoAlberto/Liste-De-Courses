package com.example.listedecourse;

import java.util.ArrayList;
import java.util.List;

public class ModelRayonGarni {
	private String nomRayon;
	private String noRayon;
	private List <ModelArticle> listeDesArticles;
	
	public ModelRayonGarni(String no,String nom){
	  nomRayon=nom;
	  noRayon=no;
	  listeDesArticles = new ArrayList<ModelArticle>();
	}
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
		  		  
	void add(ModelArticle article) {
		listeDesArticles.add(article);
	}
		  
	int getNbArticle() {
		return listeDesArticles.size();
	}
		  
	ModelArticle getArticle(int indice) {
		return listeDesArticles.get(indice);
	}
}
