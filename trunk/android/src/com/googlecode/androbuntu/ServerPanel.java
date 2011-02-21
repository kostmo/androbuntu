package com.googlecode.androbuntu;

import android.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class ServerPanel extends PreferenceActivity {

	   @Override
	   public void onCreate(Bundle savedInstanceState) {
		   super.onCreate(savedInstanceState);

	       addPreferencesFromResource( R.xml.preferences );

	   }
}
