package com.example.listedecourse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;
import android.util.Log;
	
//ASync is an asynchrony task that will not run as the same time as the application
public class WebServiceClient extends AsyncTask<String , Void, String> {
	final InterfaceDeCallBack monInterfaceDeCallBack;
	
    public WebServiceClient(InterfaceDeCallBack monInterface) {
    	//Constructor
        monInterfaceDeCallBack= monInterface;
    }
    
	protected String doInBackground(String... params) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			//Params[0] have the web service address			
			HttpPost httppost = new HttpPost(params[0]);
			HttpResponse response = httpclient.execute(httppost);
			String reponse = inputStreamToString(
			response.getEntity().getContent()).toString();
			return reponse;
	    }
		catch (ClientProtocolException e) {
			Log.i("Error","Error http: Server not accessible");
			e.printStackTrace();
		} 
		catch (IOException e) {
			Log.i("Error","Erreur i/o: Timeout");   
			e.printStackTrace();
		}
		catch (Exception e) {
		    Log.e("Error", "Pointer Null, Not page were wanted");
		}
		return null;
	}//fin du doInBackground
	 
	private StringBuilder inputStreamToString(InputStream is) {
		String ligneLue = "";
		StringBuilder reponse = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		try {
			while ((ligneLue = rd.readLine()) != null) {
				reponse.append(ligneLue);
			}
		}
		catch (IOException e) {
			Log.i("Error","Error: HTTP Session has stopped");
		}
		return reponse;
	} 
	@Override
	protected void onPostExecute(String resultat){
	//Quand on a fini de recevoir la chaine resultat contient la reponse du webservice il faut exécuter le code prévu
		monInterfaceDeCallBack.receptionDonneesTerminee(resultat);
	}
}// fin du  WebServiceClient