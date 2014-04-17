package com.example.listedecourse;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Class MagasinActivity
 * This activity do :
 * <ol>
 * 	<li>Show the radiuses' store names
 * 	<li>New radius is added when the text edit field nomRayon isn't empty and the user pressed the add button.</li>
 * 	<li>The radius delete when the user press the delete button.</li>
 * </ol>
 */
public class MagasinActivity extends BaseActivity {	
	/**
	 * Store radius list
	 */
	private ListView listeViewDesRayons;
	/**
	 * Radius add button
	 */
	private Button boutonAjouter;
	/**
	 * Radius delete button when the radius is selected by the user
	 */
	private Button boutonSupprimer;	
	/**
	 * Radius add button
	 */
	private Button boutonModifier;
	String leRayon;
	/**
	 * Executed code when the store button is pressed
	 * @param savedInstanceState not used for now
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_magasin);
		boutonAjouter=(Button)findViewById(R.id.buttonAjouterRayon);
		boutonAjouter.setOnClickListener(listenerAjouterRayon);
		boutonSupprimer=(Button)findViewById(R.id.buttonSupprimerRayon);
		boutonSupprimer.setOnClickListener(listenerSupprimerRayon);
		boutonModifier=(Button)findViewById(R.id.buttonModifierRayon);
		boutonModifier.setOnClickListener(listenerModifierRayon);
	}
	public String url(){return baseUrl;};
	public List<ModelRayon> listeDesRayons = new ArrayList<ModelRayon>();

	@Override
	public void traiterDonneesRecues(String jsonResult) {	
		listeDesRayons.clear();
		listeViewDesRayons = (ListView) findViewById(R.id.listViewRayons);
		try{
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("rayonInfos");
			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				String nomRayon = jsonChildNode.optString("rayonLib");
				String noRayon = jsonChildNode.optString("rayonId");
				ModelRayon nouveauRayon=new ModelRayon(noRayon, nomRayon);
				listeDesRayons.add(nouveauRayon);
			}
		} 
		catch (JSONException e) {
			Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();
		}	
		RayonAdapter rayonAdapter = new RayonAdapter(this,listeDesRayons);
		try{ 
			listeViewDesRayons.setAdapter(rayonAdapter);	  
		}
		catch(NullPointerException e){
		}
	}
	/**
	 * This listener add a new radius when user click on the add button
	 */
	@SuppressLint("ShowToast")
	private OnClickListener listenerAjouterRayon=new OnClickListener(){
		@Override
		public void onClick(View v) {

			AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
			alert.setTitle("Add a new Radius");
			alert.setMessage("Type the name of the new radius :");

			// Set an EditText view to get user input 
			final EditText input = new EditText(v.getContext());
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				if(input.length()>0) {
					int nombreDeRayon = listeDesRayons.size();
					boolean alreadyE = false;
					for(int i=0;i<nombreDeRayon; i++) {
						if(listeDesRayons.get(i).getNom().equals(input.getText().toString())) {
							alreadyE = true;
							Toast.makeText(getApplicationContext(), "Radius already existing", Toast.LENGTH_LONG).show();
						}
					}
					if(!alreadyE) {
						String nomDuRayon=input.getText().toString();
						String adresse=baseUrl+"listeRayons.php?action=ajout&nomRayon="+nomDuRayon;
						accessWebService(adresse);
						input.getText().clear();
					}
				}
				else {
					Toast.makeText(getApplicationContext(), "Radius empty", Toast.LENGTH_LONG).show();
				}
			  }
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});
			alert.show();
		}
	};

	private OnClickListener listenerSupprimerRayon=new OnClickListener() {
		@Override
		public void onClick(View v) {
			int nombreDeRayon = listeDesRayons.size();
			String adresse=baseUrl+"listeRayons.php?action=delete";
			boolean supressionAEffectuer=false;
			for(int i=0;i<nombreDeRayon; i++) {
				if(listeDesRayons.get(i).isSelected()) {
					String noDuRayon=listeDesRayons.get(i).getNo();
					adresse+="&tabNoRayon[]="+noDuRayon;
					supressionAEffectuer=true;
				}
			}//For's end
			if(supressionAEffectuer)accessWebService(adresse);
		}//onClick's end
	};//OnClickListener's end

	private OnClickListener listenerModifierRayon=new OnClickListener() {
		String adresse=baseUrl+"listeRayons.php?action=modify";
		String nomR;
		@Override
		public void onClick(View v) {
			int nombreDeRayon = listeDesRayons.size();
			//How many radius selected
			int unRayon=0;
			//The radius id selected
			for(int i=0;i<nombreDeRayon; i++) {
				if(listeDesRayons.get(i).isSelected()) {
					unRayon++;
					leRayon=listeDesRayons.get(i).getNo();
					nomR=listeDesRayons.get(i).getNom();
				}
			}
			//If no radius is selected
			if(unRayon==0) {
				Toast.makeText(getApplicationContext(), "Select one radius to modify", Toast.LENGTH_LONG).show();
			}
			//If one radius is selected
			if(unRayon==1) {
				AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());

				alert.setTitle("Add a new Radius");
				alert.setMessage("Type the name of the new radius :");
				final EditText input = new EditText(v.getContext());
				input.setText(nomR);
				alert.setView(input);
				input.setSelectAllOnFocus(true);

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					if(input.length()>0) {
						int nombreDeRayon = listeDesRayons.size();
						boolean alreadyE = false;
						for(int i=0;i<nombreDeRayon; i++) {
							if(listeDesRayons.get(i).getNom().equals(input.getText().toString())) {
								alreadyE = true;
								Toast.makeText(getApplicationContext(), "Radius already existing", Toast.LENGTH_LONG).show();
							}
						}
						if(!alreadyE) {
							String nomDuRayon=input.getText().toString();
							adresse+="&noRayon="+leRayon+"&nomRayon="+nomDuRayon;
							accessWebService(adresse);
							input.getText().clear();
						}
					}
					else {
						Toast.makeText(getApplicationContext(), "Radius empty", Toast.LENGTH_LONG).show();
					}
				  }
				});
				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				  public void onClick(DialogInterface dialog, int whichButton) {
				  }
				});
				alert.show();
			}
			//If more than one radius is selected
			if(unRayon>1) {
				Toast.makeText(getApplicationContext(), "Select ONE radius to modify", Toast.LENGTH_LONG).show();
			}
		}
	};
}
