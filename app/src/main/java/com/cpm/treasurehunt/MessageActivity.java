package com.cpm.treasurehunt;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import xmlHandler.FailureXMLHandler;

import com.cpm.Constants.CommonString;


import com.cpm.message.AlertMessage;
import com.cpm.xmlGetterSetter.FailureGetterSetter;
import com.cpm.xmlGetterSetter.MessageSetterGetter;



import JsonHandler.JsonHandler;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MessageActivity  extends Activity{
	
	private Dialog dialog;
	private ProgressBar pb;
	private TextView percentage, message;
	private Data data;
	private FailureGetterSetter failureGetterSetter = null;
	private SharedPreferences preferences = null;
	int eventType;
	String result;
	Button ref;

	ListView lv;
	String username;
	
	MessageSetterGetter jcpData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);
		lv = (ListView)findViewById(R.id.msglist); 
		ref = (Button)findViewById(R.id.refresh);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		username = preferences.getString(CommonString.KEY_USERNAME, null);
		
		new InstantUpload(MessageActivity.this,"error").execute();
		
		
		ref.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new InstantUpload(MessageActivity.this,"error").execute();
				
			}
		});
		
		
	}
	
	
	class Data {
		int value;
		String name;
	}
	
	public class InstantUpload extends android.os.AsyncTask<Void, Data, String>{
		private Context context;
		String errormesg;
		InstantUpload(Context context, String string) {
			this.context = context;
			this.errormesg =  string;
		}
		
		
		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
			
			dialog = new Dialog(context);
			dialog.setContentView(R.layout.custom);
			dialog.setTitle("Fetching Messages...");
			dialog.setCancelable(false);
			dialog.show();
			pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
			percentage = (TextView) dialog.findViewById(R.id.percentage);
			message = (TextView) dialog.findViewById(R.id.message);
			
		}
		
		
		@Override
		protected String doInBackground(Void... params) {
			
			try {
				
				
				data = new Data();
				// JCP

				data.value = 10;
				data.name = "";
				publishProgress(data);

				HttpParams myParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(myParams, 10000);
				HttpConnectionParams.setSoTimeout(myParams, 10000);
				HttpClient httpclient = new DefaultHttpClient();
				InputStream inputStream = null;

				JSONObject jo1 = new JSONObject();
				jo1.put("USER_ID", username);

				String jo = jo1.toString();

				HttpPost httppost = new HttpPost(CommonString.URL
						+ CommonString.METHOD_STORE_VISIT);
				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("Content-type", "application/json");

				StringEntity se = new StringEntity(jo);

				httppost.setEntity(se);

				HttpResponse response = httpclient.execute(httppost);

				inputStream = response.getEntity().getContent();

				if (inputStream != null) {
					result = convertInputStreamToString(inputStream);
				}
				
				//result = removequotes(result);

				if ( result.toString().replace("\"", "")
						.equalsIgnoreCase(CommonString.KEY_NO_DATA)) {
					return CommonString.METHOD_STORE_VISIT+ ","
							+ AlertMessage.MESSAGE_JCP_FALSE;
				}

				// for failure

				if ( result.toString().replace("\"", "")
						.equalsIgnoreCase(CommonString.KEY_FAILURE)) {
					return CommonString.METHOD_STORE_VISIT;
				}

				jcpData = JsonHandler.MessageXMLHandler(result);
				

				return errormesg;
			
			} catch (Exception e) {
				
				
				errormesg = e.toString();
				return errormesg;
	
				
			}
		
		}
		
		
		@Override
		protected void onProgressUpdate(Data... values) {
			
			pb.setProgress(values[0].value);
			percentage.setText(values[0].value + "%");
			message.setText(values[0].name);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if (result.equalsIgnoreCase("")) {
				
//				lv.setBackground(background)
				
			} else {
				
				lv.setAdapter(new MyAdaptor(jcpData));
				

			}

			dialog.dismiss();
			
			

		

		}
		
		

	}
	
	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		
		return result;

	}
	
public  class MyAdaptor extends BaseAdapter{
	
	MessageSetterGetter data;
		
		LayoutInflater mInflater;
		private Context mcontext;

		public MyAdaptor(MessageSetterGetter msgData) {
			data = msgData;
			
		}

		@Override
		public int getCount() {
			
			return data.getMessage().size();
		}

		@Override
		public Object getItem(int position) {
			
			return position;
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			
			if (convertView == null) {
				
				holder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.messageview, null);
				
				holder.msgs = (TextView)convertView.findViewById(R.id.mesgs);
				holder.sent_date = (TextView)convertView.findViewById(R.id.date);
			
				
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.msgs.setText(data.getMessage().get(position));
			holder.sent_date.setText(data.getSentDate().get(position));
			
	/*		holder.sku_name.setText(promotionlist.get(position).getSku_name());
			holder.promo_name.setText(promotionlist.get(position).getPromotion());
			holder.stock.setId(position);
			holder.pop.setId(position);
			holder.running.setId(position);*/
			
			
    			return convertView;
		}
		
	}
	
	 static class ViewHolder{
			
		 ToggleButton stock, pop, running;
		 TextView msgs, sent_date;
		
	}

}
