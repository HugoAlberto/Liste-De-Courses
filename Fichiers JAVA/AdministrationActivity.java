package com.example.listedecourse;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AdministrationActivity extends BaseActivity {

	/**
	 * Modify button
	 * Modify the current password by a new one
	 */
	Button btnModify;
	/**
	 * Share button
	 * Share automaticly a list with another user
	 */
	Button btnShare;
	/**
	 * Delete button
	 * Delete someone that you shared your list with
	 */
	Button btnDelete;
	/**
	 * New password input
	 */
	EditText inputPassword;
	/**
	 * Add a new user to your auto share list
	 */
	EditText inputShare;
	/**
	 * Spinner of the users you share your lists with
	 */
	Spinner userSpinner;
    ArrayList<String> userName = new ArrayList<String>();
	private static String KEY_SUCCESS = "success";
	private static String NO_USER_WITH_THIS_MAIL = "noUserExisting";

	private String url = "index.php";
	public String url(){return baseUrl+url;};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
	    final String id = pref.getString(PREF_ID, null);
		String adresse=baseUrl+"index.php?tag=listSharedWith&login="+id;
		accessWebService(adresse);
		
		setContentView(R.layout.activity_administration);
		
		inputPassword = (EditText) findViewById(R.id.editTextPassword);
		btnModify = (Button) findViewById(R.id.button1);
		btnModify.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				String pass = inputPassword.getText().toString();
				String adresse=baseUrl+"index.php?tag=password&pass="+pass+"&from="+id;
				accessWebService(adresse);
			}
		});
		
		inputShare = (EditText) findViewById(R.id.editTextShare);
		btnShare = (Button) findViewById(R.id.button2);
		btnShare.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				String share = inputShare.getText().toString();
				String adresse=baseUrl+"index.php?tag=share&email="+share+"&from="+id;
				accessWebService(adresse);
			}
		});
		
		btnDelete = (Button) findViewById(R.id.button3);
		btnDelete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String userMail = userSpinner.getSelectedItem().toString();
				String adresse=baseUrl+"index.php?tag=deleteSharedUser&delete="+userMail+"&from="+id;
				accessWebService(adresse);
				Log.i("LOGIN",adresse);
			}
		});
	}

	@Override
	void traiterDonneesRecues(String result) {
		// check for server response
		try {
			JSONObject json = new JSONObject(result);
			JSONArray jsonArray = json.optJSONArray("membreNom");
			if(jsonArray!=null) {
				for (int i = 0; i < jsonArray.length(); i++) {
				    JSONObject jsonObject = jsonArray.getJSONObject(i);
				    userName.add(jsonObject.optString("email"));
				    Log.i("lesUsers", jsonObject.optString("email"));
				}
			} else {
				userName.add(json.getString("error"));
				Log.i("noUsers", json.getString("error"));
			}
			if (json.getString(KEY_SUCCESS) != null) {
				String res = json.getString(KEY_SUCCESS);
				if(Integer.parseInt(res) == 1) {
					finish();
					startActivity(getIntent());
				}
				if(Integer.parseInt(res) == 2) {
					inputPassword.setText("");
					Toast.makeText(getApplicationContext(), "SUCCESS, your password has been updated",Toast.LENGTH_LONG).show();
				}
			}
			
			if (json.getString(NO_USER_WITH_THIS_MAIL) != null) {
				String res = json.getString(NO_USER_WITH_THIS_MAIL);
				if(Integer.parseInt(res) == 1) {
					Toast.makeText(getApplicationContext(), "Email incorect. No user existing with this Email",Toast.LENGTH_LONG).show();
				}
			}
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		userSpinner = (Spinner)findViewById(R.id.spinnerUnshare);
		userSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, userName));
	}
}
