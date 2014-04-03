package com.example.listedecourse;

import java.util.ArrayList;
import java.util.List;

public class EnsembleRayons {
	private List <ModelRayonGarni> lesRayons;
	
	public EnsembleRayons() {
		lesRayons = new ArrayList<ModelRayonGarni>();
	}
	/**
	 * Get radiuses number
	 * @return size
	 */
	int getNbRayon() {
		return lesRayons.size();
	}
	
	/**
	 * Add a radius
	 * @param leRayon
	 */
	void add(ModelRayonGarni leRayon) {
		lesRayons.add(leRayon);
	}
	
	/**
	 * Get radius number
	 * @param indice
	 * @return Radius indice
	 */
	ModelRayonGarni getRayon(int indice) {
		return lesRayons.get(indice);
	}
	
	/**
	 * Return null if no radiuses where found
	 * @param noRadius
	 * @return Radius number
	 */
	ModelRayonGarni getRayonById(String noRayon) {
		ModelRayonGarni leRayon=null;
		int indiceRayon=0;
		while(!(indiceRayon==lesRayons.size() || lesRayons.get(indiceRayon).getNo().equals(noRayon))) {
			indiceRayon++;
		}
		if(!(indiceRayon==lesRayons.size()))
			leRayon=lesRayons.get(indiceRayon);
		return leRayon;
	}
	
	/**
	 * Add a product to the radius name or add a product to a new radius
	 * @param no 		number
	 * @param libelle 	name
	 * @param noR 		radius number
	 * @param libR		radius name
	 * @param qte 		quantity
	 */
	public void addArticle(String no, String libelle, String qte, String noR, String libR) {
		ModelRayonGarni leRayon=getRayonById(noR);
        if(leRayon==null) {
            leRayon=new ModelRayonGarni(noR, libR);
            leRayon.add(new ModelArticle(no, libelle, qte));
            lesRayons.add(leRayon);
        }
        else
        	leRayon.add(new ModelArticle(no, libelle, qte));

	}
}
