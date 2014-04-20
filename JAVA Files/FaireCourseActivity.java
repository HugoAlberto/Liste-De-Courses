package com.example.listedecourse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FaireCourseActivity extends BaseActivity {
	
	Button btnShopping;
	Spinner spinnerListe;
	public List<Map<String, String>> listeDesMapsListe = new ArrayList<Map<String, String>>();
	private AdapterExpandableRayonProduit monAdapter;
	private ExpandableListView listeViewDesProduitsDeLaListeParRayons;
	
	public String url(){
		SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
	    final String id = pref.getString(PREF_ID, null);
		return baseUrl+"?tag=getLists&userId="+id;
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faire_courses);
		listeViewDesProduitsDeLaListeParRayons=(ExpandableListView) findViewById(R.id.expandableListViewProduits);
		btnShopping = (Button) findViewById(R.id.buttonPoserDansCaddy);
		btnShopping.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				boolean supressionAEffectuer=false;
				String adresse = baseUrl+"?tag=buyProduct";
				for(int i=0;i<monAdapter.getEnsRayon().getNbRayon();i++) {
					for(int j=0;j<monAdapter.getEnsRayon().getRayon(i).getNbArticle();j++) {
						if(monAdapter.getEnsRayon().getRayon(i).getArticle(j).isSelected()) {
							String prod = monAdapter.getEnsRayon().getRayon(i).getArticle(j).getNo();
							adresse+="&tabNoProduit[]="+prod;
							supressionAEffectuer=true;
						}
					}
				}
				if(supressionAEffectuer)accessWebService(adresse);
			}
		});
		spinnerListe = (Spinner)findViewById(R.id.spinnerListe);
		spinnerListe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		    	@SuppressWarnings("unchecked")
				String listId = ((HashMap<String,String>)(spinnerListe.getSelectedItem())).get("listeId");
	            accessWebService(baseUrl+"?tag=listDoShopping&listId="+listId);
		    }
		    public void onNothingSelected(AdapterView<?> parent) {
		    // Another interface callback
		    }
		});
	}

	public void traiterDonneesRecues(String jsonResult){
		EnsembleRayons ensRayons = new EnsembleRayons();
		try{
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("listeInfos");
			if(jsonMainNode!=null) {
				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					String number = jsonChildNode.optString("listeId");
					String name = jsonChildNode.optString("listeNom");
					listeDesMapsListe.add(creerMapListe(number,name));
				}
				SimpleAdapter sAListe = new SimpleAdapter(this,listeDesMapsListe,R.layout.liste_layout,new String[] { "listeLib" },new int[] { R.id.itemLibelleListe});
				try {
					spinnerListe.setAdapter(sAListe);
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
					String premiereListeId = jsonChildNode.optString("listeId");
					Log.i("ListeDeCourse",premiereListeId);
					String adresse=baseUrl+"?listDoShopping&listId="+ premiereListeId;
					accessWebService(adresse);
				}
				catch(NullPointerException e) {
					Log.i("ListeDeCourse",listeDesMapsListe.toString());
				}
			}
			else{
				jsonMainNode = jsonResponse.optJSONArray("coursesAFaire");
				if(jsonMainNode != null) {
					for (int i = 0; i < jsonMainNode.length(); i++) {
						JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
						String name = jsonChildNode.optString("rayonLib");
						String number = jsonChildNode.optString("rayonId");
						String prodNumber = jsonChildNode.optString("produitId");
						String prodName = jsonChildNode.optString("produitLib");
						String prodQte = jsonChildNode.optString("listeQte");
						ensRayons.addArticle(prodNumber, prodName, prodQte, number, name);
					}
					monAdapter = new AdapterExpandableRayonProduit(this);
					monAdapter.setEnsRayon(ensRayons);
					try{ 
						listeViewDesProduitsDeLaListeParRayons.setAdapter(monAdapter);
						int count = monAdapter.getGroupCount();
						for (int position = 1; position <= count; position++)
							listeViewDesProduitsDeLaListeParRayons.expandGroup(position - 1);
					}
					catch(NullPointerException e){
						Log.i("Error", e.toString());
					}
				}
			}
		}
		catch (JSONException e) {
			Log.i("Error",e.toString());
		}
	}
	private HashMap<String, String> creerMapListe(String number, String name) {
		HashMap<String, String> mapListe = new HashMap<String, String>();
		mapListe.put("listeId", number);
		mapListe.put("listeLib", name);
		return mapListe;
	}
}