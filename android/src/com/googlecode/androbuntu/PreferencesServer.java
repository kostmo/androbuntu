package com.googlecode.androbuntu;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;

public class PreferencesServer extends PreferenceActivity {

	static final String TAG = "AndroBuntu PreferencesServer";
	
	public static int DEFAULT_HOST_PORT = 46645;
	public static String DEFAULT_HOST_PORT_STRING = Integer.toString(DEFAULT_HOST_PORT);
	public static String DEFAULT_HOST_IP_ADDRESS = "192.168.0.80";

	public static String DEFAULT_TRIGGER_WIFI_NETWORK = "our_house";
	public static String PREFKEY_TRIGGER_WIFI_NETWORK = "trigger_wifi_network";
	public static String PREFKEY_LAST_ON_DATE_EPOCH = "last_on_date_epoch";
	public static String PREFKEY_RESET_ARRIVAL_LIGHTS = "reset_arrival_lights";
	

	static final long MILLISECONDS_PER_DAY = 1000L*60*60*24;
	public static boolean isLightsTriggeredToday(SharedPreferences settings, Calendar calendar) {
		
		boolean contains_pref = settings.contains(PreferencesServer.PREFKEY_LAST_ON_DATE_EPOCH);
//		Log.d(TAG, "Contains pref? " + contains_pref);
		
		long day_index_today = calendar.getTimeInMillis()/MILLISECONDS_PER_DAY;
		long day_index_light = settings.getLong(PreferencesServer.PREFKEY_LAST_ON_DATE_EPOCH, 0)/MILLISECONDS_PER_DAY;
		
//		Log.d(TAG, "Day index today: " + day_index_today);
//		Log.d(TAG, "Day index light: " + day_index_light);
		
		boolean are_equal = day_index_today == day_index_light;
//		Log.d(TAG, "Are equal? " + are_equal);
		return contains_pref && are_equal;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource( R.xml.preferences );
		
		final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		if (isLightsTriggeredToday(settings, new GregorianCalendar())) {
			final Preference trigger_reset = findPreference(PREFKEY_RESET_ARRIVAL_LIGHTS);
			trigger_reset.setEnabled(true);
			trigger_reset.setOnPreferenceClickListener(new OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					settings.edit().remove(PreferencesServer.PREFKEY_LAST_ON_DATE_EPOCH).commit();
					trigger_reset.setEnabled(false);
					return true;
				}
			});
		}
	}
}
