package com.example.listedecourse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

public class RemplirListe extends BaseActivity{
	
	private String url = "listeRayons.php";
	public String url(){return baseUrl+url;};
	/**
	 * Radius spinner
	 */
	private Spinner spinnerRayon;
	/**
	 * Product spinner
	 */
	private Spinner spinnerProduit;
	/**
	 * Add button
	 * Add a product of a radius with the quantity desired in the list
	 */
	private Button boutonAjouter;
	/**
	 * Delete button
	 * Delete selected product in the list
	 */
	private Button boutonSupprimer;
	/**
	 * Product list view
	 */
	private ListView listViewProduits;
	/**
	 * Radius List
	 */
	public List<Map<String, String>> listeDesMapsRayon = new ArrayList<Map<String, String>>();
	/**
	 * Product List
	 */
	public List<Map<String, String>> listeDesMapsProduit = new ArrayList<Map<String, String>>();
	/**
	 * List List
	 */
	public List<ModelListe> listeDesMapsProduitDsListe = new ArrayList<ModelListe>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remplir_liste);
		
		listViewProduits=(ListView)findViewById(R.id.listViewProduits);
		boutonAjouter=(Button) findViewById(R.id.buttonAjouterALaListe);
		boutonAjouter.setOnClickListener(listenerAjouterListe);
		boutonSupprimer=(Button) findViewById(R.id.buttonSupprimerALaListe);
		boutonSupprimer.setOnClickListener(listenerSupprimerListe);
		spinnerRayon= (Spinner)findViewById(R.id.spinnerRayon);
		spinnerRayon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		    	@SuppressWarnings("unchecked")
				String nomDuRayon=((HashMap<String,String>)(spinnerRayon.getSelectedItem())).get("rayonLib");
		    	Log.i("ListeDeCourse",nomDuRayon);
	            accessWebService(baseUrl+"listeProduits.php?rayon="+nomDuRayon);
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    // Another interface callback
		    }
		});
		spinnerProduit=(Spinner) findViewById(R.id.SpinnerProduit);
	}

	@Override
	public void traiterDonneesRecues(String jsonResult){
		SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
	    final String id = pref.getString(PREF_ID, null);
		try{
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("rayonInfos");
			if(jsonMainNode!=null){
				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					String name = jsonChildNode.optString("rayonLib");
					String number = jsonChildNode.optString("rayonId");
					listeDesMapsRayon.add(creerMapRayon(name, number));
				}
				SimpleAdapter sARayon = new SimpleAdapter(this,listeDesMapsRayon,R.layout.rayon_layout,new String[] { "rayonLib" },new int[] { R.id.itemLibelle});
				try{
					spinnerRayon.setAdapter(sARayon);
					String adresse=baseUrl+"listeCourses.php?action=vue&id="+id;
					accessWebService(adresse);
				}
				catch(NullPointerException e){
					Log.i("ListeDeCourse",listeDesMapsRayon.toString());
				}
			}
			else{
				jsonMainNode = jsonResponse.optJSONArray("produitsDuRayon");
				if(jsonMainNode!=null){
					listeDesMapsProduit.clear();
					for (int i = 0; i < jsonMainNode.length(); i++) {
						JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
						String name = jsonChildNode.optString("produitLib");
						String number = jsonChildNode.optString("produitId");
						listeDesMapsProduit.add(creerMapProduit(name, number));
					}
					SimpleAdapter saProduit = new SimpleAdapter(this,listeDesMapsProduit,R.layout.produit_layout,new String[] { "produitLib" },new int[] { R.id.itemLibelleProduit});
					try{
						spinnerProduit.setAdapter(saProduit);
					}
					catch(NullPointerException e){
						Log.i("ListeDeCourse",listeDesMapsRayon.toString());
					}
				}
				else{
					jsonMainNode = jsonResponse.optJSONArray("listeDeCourse");
					Log.i("ListeDeCourse",jsonMainNode.toString());
					if(jsonMainNode!=null){
						listeDesMapsProduitDsListe.clear();
						for (int i = 0; i < jsonMainNode.length(); i++) {
							JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
							String idProduit = jsonChildNode.optString("produitId");
							String qte = jsonChildNode.optString("listeQte");
							String libelle=jsonChildNode.optString("produitLib");
							ModelListe nouvelleListe=new ModelListe(idProduit,libelle,qte);
							listeDesMapsProduitDsListe.add(nouvelleListe);
						}
						ListeAdapter listeAdapter = new ListeAdapter(this,listeDesMapsProduitDsListe);
						try{
							listViewProduits.setAdapter(listeAdapter);
						}
						catch(NullPointerException e){
							Log.i("ListeDeCourse",listeDesMapsProduitDsListe.toString());
						}
					}
				}
			}
		}
		catch (JSONException e) {
			//Toast.makeText(getApplicationContext(), "Error" + e.toString(),Toast.LENGTH_SHORT).show();
		}
	}
	
	private OnClickListener listenerSupprimerListe=new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.i("ListeDeCourse","Suppress button");
			int nombreDeProduit = listeDesMapsProduitDsListe.size();
			String adresse=baseUrl+"listeProduits.php?action=delete";
			boolean supressionAEffectuer=false;
			for(int i=0;i<nombreDeProduit; i++) {
				if(listeDesMapsProduitDsListe.get(i).isSelected()) {
					String noDuProduit=listeDesMapsProduitDsListe.get(i).getNo();
					adresse+="&tabNoProduit[]="+noDuProduit;
					Log.v("ListeDeCourse","Le produit = "+noDuProduit);
					supressionAEffectuer=true;
				}
			}
			if(supressionAEffectuer)
				accessWebService(adresse);
		}
	};
	
	private OnClickListener listenerAjouterListe=new AdapterView.OnClickListener(){
		
		@Override
		public void onClick(View v) {
			SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
		    final String id = pref.getString(PREF_ID, null);
			spinnerProduit= (Spinner)findViewById(R.id.SpinnerProduit);
			@SuppressWarnings("unchecked")
			String noProduit=((HashMap<String,String>)(spinnerProduit.getSelectedItem())).get("produitId");
			String qte=((EditText)findViewById(R.id.editTextQuantite)).getText().toString();
			String adresse=baseUrl+"listeCourses.php?action=ajout&produitId="+noProduit+"&qte="+qte+"&id="+id;
			Log.i("ListeDeCourse",adresse);
			accessWebService(adresse);
		}
	};

	private HashMap<String, String> creerMapRayon(String name, String number) {
		HashMap<String, String> mapRayon = new HashMap<String, String>();
		mapRayon.put("rayonId", number);
		mapRayon.put("rayonLib", name);
		return mapRayon;
	}

	private HashMap<String, String> creerMapProduit(String name, String number) {
		HashMap<String, String> mapRayon = new HashMap<String, String>();
		mapRayon.put("produitId", number);
		mapRayon.put("produitLib", name);
		return mapRayon;
	}
}
