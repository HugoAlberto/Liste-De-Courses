package com.example.listedecourse;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public abstract class BaseActivity extends Activity {
	//protected String baseUrl="http://alberto-hugo.com/listeCourses/";
	protected String baseUrl="http://10.0.2.2/listeCourses/index.php";
	abstract String url();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		accessWebService(url());
	}
	/**
	 * Create preferences named LogsInfos
	 */
	public static final String PREFS_NAME = "LogsInfos";
	/**
	 * Username preferences
	 */
	protected static final String PREF_USERNAME = "username";
	/**
	 * Password preferences
	 */
	protected static final String PREF_PASSWORD = "password";
	/**
	 * Unique id preferences
	 */
	protected static final String PREF_ID = "uid";
    
	public boolean onCreateOptionsMenu(Menu monMenu) {
	    MenuInflater monInflater = getMenuInflater();
	    monInflater.inflate(R.menu.main, monMenu);
	    return super.onCreateOptionsMenu(monMenu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId())
		{
    		case R.id.action_admin: Intent i1 = new Intent(this,AdministrationActivity.class );startActivity(i1);finish();break;
	    	case R.id.action_home: Intent i0 = new Intent(this,DashboardActivity.class );startActivity(i0);finish();break;
		    case R.id.action_courses: Intent i = new Intent(this,FaireCourseActivity.class );startActivity(i);finish();break;
			//case R.id.action_magasin: Intent i1 = new Intent(this,MagasinActivity.class );startActivity(i1);finish();break;			
			case R.id.action_liste: Intent i2 = new Intent(this,RemplirListe.class );startActivity(i2);finish();break;	
		}		
		return true;
	}
	
	public void accessWebService(String adresse) {
		 WebServiceClient monWebService = new WebServiceClient(new InterfaceDeCallBack() {
			 
			 @Override
			 public void receptionDonneesTerminee(String result) {
				 traiterDonneesRecues(result);
			 }
		 });
		 monWebService.execute(new String[] { adresse });
	}
	
	abstract void  traiterDonneesRecues(String result);
}
