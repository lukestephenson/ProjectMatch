package com.gdg.london.match;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {

	private static String NAME= "Prefs";
	
	private static SharedPreferences prefs;
	public static SharedPreferences getPrefs(Context ctx) {
		if (prefs == null) {
			prefs = ctx.getApplicationContext().getSharedPreferences(NAME, 0);
		}
			
		return prefs;
	}
}
