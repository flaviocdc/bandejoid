package br.ufrj.bandejoid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		AsyncTask<Void, Void, String> asyncTask = new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String json = lerCardapioJSON();
				return json;
			}
			
			@Override
			protected void onPostExecute(String result) {
				List<Almoco> semana = new ArrayList<Almoco>();
				//TextView textView = (TextView) findViewById(R.id.hello_world);
				
				JSONArray jsonArray;
				try {
					jsonArray = new JSONArray(result);
					for (int i = 0; i < 5; i++ )  
					{
						semana.add(new Almoco(jsonArray.getJSONObject(i)));
					}
					
					TextView salada = (TextView) findViewById(R.id.saladaValue);
					salada.setText(semana.get(0).salada);
					
					TextView pratoPrincipal = (TextView) findViewById(R.id.pratoPrincipalValue);
					pratoPrincipal.setText(semana.get(0).pratoPrincipal);
					
					TextView acompanhamento1 = (TextView) findViewById(R.id.acompanhamento1Value);
					acompanhamento1.setText(semana.get(0).acompanhamento1);
					
					TextView acompanhamento2 = (TextView) findViewById(R.id.acompanhamento2Value);
					acompanhamento2.setText(semana.get(0).acompanhamento2);
					
					TextView guarnicao = (TextView) findViewById(R.id.guarnicaoValue);
					guarnicao.setText(semana.get(0).guarnicao);
					
					TextView sobremesa = (TextView) findViewById(R.id.sobremesaValue);
					sobremesa.setText(semana.get(0).sobremesa);
					
					TextView suco = (TextView) findViewById(R.id.sucoValue);
					suco.setText(semana.get(0).suco);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.execute();
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public String lerCardapioJSON() {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://private.rootshell.la/~marquinho/restaurante.php");
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				Log.e(MainActivity.class.getSimpleName(),
						"Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
	
	class Almoco {
		String salada;
		String pratoPrincipal;
		String acompanhamento1;
		String acompanhamento2;
		String guarnicao;
		String sobremesa;
		String suco;
		
		Almoco(JSONObject json) throws JSONException
		{
			this.salada          = json.getString("salada");
			this.pratoPrincipal  = json.getString("principal");
			this.acompanhamento1 = json.getString("acompanhamento1");
			this.acompanhamento2 = json.getString("acompanhamento2");
			this.guarnicao 		 = json.getString("guarnicao");
			this.sobremesa 		 = json.getString("sobremesa");
			this.suco 		     = json.getString("suco");
			
		}
	}
}
