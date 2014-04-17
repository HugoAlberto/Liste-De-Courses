package com.example.listedecourse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.listedecourse.LoginActivity;
import com.example.listedecourse.R;

public class DashboardActivity extends BaseActivity {
	/**
     * Logout button
     * Log you out and set your preferences credentials to empty
     * */
	Button btnLogout;
	/**
     * Radius management button
     * Get you to the radius management page
     * */
	//Button btnMagasin;
	/**
	 * Administration button
	 * Get you to the administration page
	 */
	Button btnAdmin;
	/**
     * List button
     * Get you to your list page
     * */
	Button btnList;
	/**
     * Shopping button
     * Get you the shopping page
     * */
	Button btnShopping;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Dashboard Screen for the application
         * */
        // Check login status in database
        SharedPreferences pref = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);   
        String username = pref.getString(PREF_USERNAME, null);
        String password = pref.getString(PREF_PASSWORD, null);
        Log.i("username + password",username+" "+password);
		if(username == null || password == null){
        	// user is not logged in show login screen
        	Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        	login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(login);
        	finish();
		}
        else{
			setContentView(R.layout.dashboard);
			/*
			btnMagasin = (Button) findViewById(R.id.action_magasin);
			btnMagasin.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View arg0) {
    				Intent magasin = new Intent(getApplicationContext(), MagasinActivity.class);
    				startActivity(magasin);
    	        	finish();
    			}
			});
			*/
			btnAdmin = (Button) findViewById(R.id.action_admin);
			btnAdmin.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View arg0) {
    				Intent admin = new Intent(getApplicationContext(), AdministrationActivity.class);
    				startActivity(admin);
    	        	finish();
    			}
			});
			btnList = (Button) findViewById(R.id.action_liste);
			btnList.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View arg0) {
    				Intent list = new Intent(getApplicationContext(), RemplirListe.class);
    				startActivity(list);
    	        	finish();
    			}
			});
			btnShopping = (Button) findViewById(R.id.action_courses);
			btnShopping.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View arg0) {
    				Intent shopping = new Intent(getApplicationContext(), FaireCourseActivity.class);
    				startActivity(shopping);
    	        	finish();
    			}
			});
			
        	btnLogout = (Button) findViewById(R.id.btnLogout);
        	btnLogout.setOnClickListener(new View.OnClickListener() {
    			public void onClick(View arg0) {
    				getSharedPreferences(PREFS_NAME,MODE_PRIVATE)
    		        .edit()
    		        .putString(PREF_USERNAME, null)
    		        .putString(PREF_PASSWORD, null)
    		        .commit();
    				Intent login = new Intent(getApplicationContext(), LoginActivity.class);
    	        	login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	        	startActivity(login);
    	        	finish();
    			}
        	});
        	
        }
    }
    //Useless here
	@Override
	String url() {
		return null;
	}
	@Override
	void traiterDonneesRecues(String result) {
		
	}
}