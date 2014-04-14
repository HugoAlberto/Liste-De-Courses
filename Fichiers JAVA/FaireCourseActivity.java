package com.example.listedecourse;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FaireCourseActivity extends BaseActivity {
	
	Button btnShopping;
	private AdapterExpandableRayonProduit monAdapter;
	private String url = "index.php";
	public String url(){return baseUrl+url+"?tag=listDoShopping";};
	private ExpandableListView listeViewDesProduitsDeLaListeParRayons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faire_courses);
		listeViewDesProduitsDeLaListeParRayons=(ExpandableListView) findViewById(R.id.expandableListViewProduits);
		btnShopping = (Button) findViewById(R.id.buttonPoserDansCaddy);
		btnShopping.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				boolean supressionAEffectuer=false;
				String adresse = url()+"?tag=buyProduct";
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
	}

	public void traiterDonneesRecues(String jsonResult){
		EnsembleRayons ensRayons= new EnsembleRayons();
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("coursesAFaire");
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				String name = jsonChildNode.optString("rayonLib");
				String number = jsonChildNode.optString("rayonId");
				String prodNumber = jsonChildNode.optString("produitId");
				String prodName = jsonChildNode.optString("produitLib");
				String prodQte = jsonChildNode.optString("listeQte");
				ensRayons.addArticle(prodNumber, prodName, prodQte, number, name);
			}
		} catch (JSONException e) {
			Toast.makeText(getApplicationContext(), "Error" + e.toString(),Toast.LENGTH_SHORT).show();
		}
		monAdapter = new AdapterExpandableRayonProduit(this);
		monAdapter.setEnsRayon(ensRayons);
		try{ 
			listeViewDesProduitsDeLaListeParRayons.setAdapter(monAdapter);
		}
		catch(NullPointerException e){

		}
	}
}