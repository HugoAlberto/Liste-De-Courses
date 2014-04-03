package com.example.listedecourse;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.listedecourse.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends BaseActivity {
	/**
	 * Register button
	 * Register you if your credentials are ok and log you in
	 */
	Button btnRegister;
	/**
	 * Link to login button
	 * Take you to the login page if you already have an account
	 */
	Button btnLinkToLogin;
	/**
	 * Input name
	 */
	EditText inputFullName;
	/**
	 * Input E-mail
	 */
	EditText inputEmail;
	/**
	 * Input Password
	 */
	EditText inputPassword;
	/**
	 * Error message
	 */
	TextView registerErrorMsg;
	/**
	 * Success variable if your registration is ok
	 */
	private static String KEY_SUCCESS = "success";
	/**
	 * Url of the register web page
	 */
	private String url = "index.php";
	public String url(){return baseUrl+url;};
	
	/**
	 * Get the informations back
	 * If the array is 'success' on the first cell
	 * 	Get you the the dashboard page
	 * Else
	 * 	Show you an error message and set to null the preferences login and pass
	 */
	@Override
	void traiterDonneesRecues(String result) {
		try {
			JSONObject json = new JSONObject(result);
			Log.i("LOGIN",json.toString());
			// check for login response
			if (json.getString(KEY_SUCCESS) != null) {
				registerErrorMsg.setText("");
				String res = json.getString(KEY_SUCCESS); 
				if(Integer.parseInt(res) == 1){
					// user successfully registred
					Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
					dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(dashboard);
					finish();
				}else{
					// Error in registration
					registerErrorMsg.setText("Error occured in registration");
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
		catch (Exception e) {
		    Log.e("Error", "", e);
		}
	}
	
	/**
	 * On the activity's creation
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		// Importing all assets like buttons, text fields
		inputFullName = (EditText) findViewById(R.id.registerName);
		inputEmail = (EditText) findViewById(R.id.registerEmail);
		inputPassword = (EditText) findViewById(R.id.registerPassword);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
		registerErrorMsg = (TextView) findViewById(R.id.register_error);
		
		btnRegister.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View view) {
				String name = inputFullName.getText().toString();
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				String adresse=baseUrl+"index.php?tag=register&name="+name+"&email="+email+"&password="+password;
				accessWebService(adresse);
				getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
		        .edit()
		        .putString(PREF_USERNAME, email)
		        .putString(PREF_PASSWORD, password)
		        .commit();
			}
		});
		// Link to Login Screen
		btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),LoginActivity.class);
				startActivity(i);
				finish();
			}
		});
	}
}
