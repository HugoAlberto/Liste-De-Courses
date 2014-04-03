package com.example.listedecourse;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.listedecourse.R;
import com.example.listedecourse.RegisterActivity;

public class LoginActivity extends BaseActivity {
	Button btnLogin;
	Button btnLinkToRegister;
	EditText inputEmail;
	EditText inputPassword;
	TextView loginErrorMsg;

	private String url = "index.php";
	public String url(){return baseUrl+url;};
	private static String KEY_SUCCESS = "success";
	private static String UID = "uid";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

		// Importing all assets like buttons, text fields
		inputEmail = (EditText) findViewById(R.id.loginEmail);
		inputPassword = (EditText) findViewById(R.id.loginPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
		loginErrorMsg = (TextView) findViewById(R.id.login_error);	

		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {				
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				String adresse=baseUrl+"index.php?tag=login&email="+email+"&password="+password;
				accessWebService(adresse);
				Log.i("LOGIN",adresse);
				getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
		        .edit()
		        .putString(PREF_USERNAME, email)
		        .putString(PREF_PASSWORD, password)
		        .commit();
			}
		});

		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});
	}

	@Override
	void traiterDonneesRecues(String result) {
		// check for login response
		try {
			JSONObject json = new JSONObject(result);
			if (json.getString(KEY_SUCCESS) != null) {
				loginErrorMsg.setText("");
				String res = json.getString(KEY_SUCCESS);
				if(Integer.parseInt(res) == 1){
					String id = json.getString(UID);
					Log.i("UID", id);
					getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
			        .edit()
			        .putString(PREF_ID, id)
			        .commit();
					Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
					dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(dashboard);
					finish();
				}else{
					// Error in login
					loginErrorMsg.setText("Incorrect username/password");
					getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
			        .edit()
			        .putString(PREF_USERNAME, null)
			        .putString(PREF_PASSWORD, null)
			        .commit();	
			    }
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
