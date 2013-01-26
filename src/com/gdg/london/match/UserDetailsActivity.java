package com.gdg.london.match;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UserDetailsActivity extends Activity {

	public static String NAME_KEY="com.gdg.london.match.NAME_PREF";
	public static String TYPE_KEY="com.gdg.london.match.TYPE_PREF";
	public static String SPEC_KEY="com.gdg.london.match.SPEC_PREF";
	public static String TECHS_KEY="com.gdg.london.match.TECHS_PREF";
	public static String UPLOAD_KEY="com.gdg.london.match.UPLOAD_PREF";
	public static String UID_KEY="com.gdg.london.match.UID_PREF";
	public static String TEAM_KEY="com.gdg.london.match.TEAM_PREF";
	
	private String name;
	private String type;
	private String speciality;
	private String techs;
	
	private boolean isWaitingUpload;
	private boolean hasTeamInfo;
	private String serverUid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_details);
		
		Intent launchIntent = getIntent();
		if (launchIntent != null && launchIntent.hasExtra(NAME_KEY)) {
			name = launchIntent.getStringExtra(NAME_KEY);
			type = launchIntent.getStringExtra(TYPE_KEY);
			speciality = launchIntent.getStringExtra(SPEC_KEY);
			techs= launchIntent.getStringExtra(TECHS_KEY);
			storePrefs();
		}
		
		unpackPrefs();
		
		if (isWaitingUpload) {
			
			new UploadTask().execute(paramsToJson());
//			serverUid = "TODO";
		} else if (hasTeamInfo) {
			new RetreiveTeamTask().execute(serverUid);
		}
//		findViewById(R.id.user_img).setVisibility(View.GONE);
		
		findViewById(R.id.user_img).setBackgroundColor(Color.BLUE);
		
		((TextView) findViewById(R.id.name)).setText(name);
		((TextView) findViewById(R.id.type)).setText(type);
		((TextView) findViewById(R.id.style)).setText(speciality);
		((TextView) findViewById(R.id.tech)).setText(techs);
	}
	
	private void renderTeam(TeamModel team) {
		findViewById(R.id.pendingTeamText).setVisibility(View.GONE);
		
		ListView teamList = (ListView) findViewById(R.id.teamList);
		teamList.setVisibility(View.VISIBLE);
		
		UserDetailsModel[] teamMembers = team.getMembers().toArray(new UserDetailsModel[team.getMembers().size()]);
		
		teamList.setAdapter(new TeamListItemAdaptor(this, teamMembers));
		
	}
	
	private static class TeamListItemAdaptor extends ArrayAdapter<UserDetailsModel> {

		private static int[] COLOURS = new int[] {Color.BLUE, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.YELLOW};
		public TeamListItemAdaptor(Context context,
				UserDetailsModel[] objects) {
			super(context, 0, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_name_row, parent, false);
			}
			
			ImageView image = (ImageView) convertView.findViewById(R.id.userDetailsRowImage);
			
			image.setBackgroundColor(COLOURS[position % COLOURS.length]);
			TextView nameView = (TextView) convertView.findViewById(R.id.userDetailsRowName);
			nameView.setText(getItem(position).getSummary());
			
			return convertView;
		}
	}
	
	private void unpackPrefs() {
		SharedPreferences prefs = SharedPrefsHelper.getPrefs(this);
		name = prefs.getString(NAME_KEY, "");
		type = prefs.getString(TYPE_KEY, "");
		speciality = prefs.getString(SPEC_KEY, "");
		techs = prefs.getString(TECHS_KEY, "");
		serverUid = prefs.getString(UID_KEY, "");
		isWaitingUpload = prefs.getBoolean(UPLOAD_KEY, true);
		hasTeamInfo = prefs.getBoolean(TEAM_KEY, false);
	}
	
	private void storePrefs() {
		SharedPreferences prefs = SharedPrefsHelper.getPrefs(this);
		Editor editor = prefs.edit();
		editor.putString(NAME_KEY, name );
		editor.putString(TYPE_KEY, type) ;
		editor.putString(SPEC_KEY, speciality);
		editor.putString(TECHS_KEY, techs);
		editor.putBoolean(UPLOAD_KEY, isWaitingUpload);
		editor.apply();
	}
	
	private String paramsToJson() {
		SharedPreferences prefs = SharedPrefsHelper.getPrefs(getApplicationContext());
		String gcmKey = prefs.getString(GCMIntentService.GCM_KEY, "");
		UserDetailsModel userDetails = new UserDetailsModel();
		userDetails.setGcmKey(gcmKey);
		userDetails.setName(name);
		userDetails.setSpeciality(speciality);;
		userDetails.setTechs(techs);
		userDetails.setType(type);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(userDetails);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private  class UploadTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			
			
			String json = params[0];
			String uid = null;
			try{
				URL url = new URL("http://dev.andthats.it/login");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setFixedLengthStreamingMode(json.getBytes().length);
				conn.setRequestProperty("content-type", "application/json");
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(json.getBytes());
				
				outputStream.flush();
				
				BufferedInputStream input = new BufferedInputStream(conn.getInputStream());
				ByteArrayOutputStream dataRead = new ByteArrayOutputStream();
				byte[] buffer = new byte[512];
				int bytesRead=0;
				while ((bytesRead=input.read(buffer)) != -1) {
					dataRead.write(buffer, 0, bytesRead);
				}
				uid = new String(dataRead.toByteArray());
			}catch (IOException e){
				throw new RuntimeException(e);
			}
			
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			return uid;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			serverUid = result;
			isWaitingUpload = false;
			storePrefs();
			
//			new RetreiveTeamTask().execute(serverUid);
		}
		
	}
	
	private  class RetreiveTeamTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String uid = params[0];
			try{
				URL url = new URL("http://dev.andthats.it/get_team?uid="+uid);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				
				BufferedInputStream input = new BufferedInputStream(conn.getInputStream());
				ByteArrayOutputStream dataRead = new ByteArrayOutputStream();
				byte[] buffer = new byte[512];
				int bytesRead=0;
				while ((bytesRead=input.read(buffer)) != -1) {
					dataRead.write(buffer);
				}
				return new String(dataRead.toByteArray());
			}catch (IOException e){
				throw new RuntimeException(e);
			}
//			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			TeamModel team ;
			try {
				 team = mapper.readValue(result, TeamModel.class);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			renderTeam(team);
			
			hasTeamInfo = true;
			storePrefs();
		}
		
	}
}
