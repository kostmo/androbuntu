package com.googlecode.androbuntu;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PreferencesServer extends PreferenceActivity {

	public static String DEFAULT_HOST_IP_ADDRESS = "192.168.0.80";
	public static int DEFAULT_HOST_PORT = 46645;
	public static String DEFAULT_HOST_PORT_STRING = Integer.toString(DEFAULT_HOST_PORT);

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource( R.xml.preferences );
	}
}
