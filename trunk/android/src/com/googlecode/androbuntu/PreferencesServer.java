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
	

    public static final int WOL_PORT = 9;
	
	
	public static String PREFKEY_HOST_PORT = "port_preference";
	public static int DEFAULT_HOST_PORT = 46645;
	public static String DEFAULT_HOST_PORT_STRING = Integer.toString(DEFAULT_HOST_PORT);

	public static String PREFKEY_HOSTNAME_OR_IP = "hostname_preference";
	public static String DEFAULT_HOST_IP_ADDRESS = "192.168.0.80";
	
	public static String PREFKEY_HOUSE_CODE_INDEX = "house_code";
	public static int DEFAULT_HOUSE_CODE_INDEX = 2;
	
	
	public static String PREFKEY_WOL_MAC_ADDRESS = "wol_mac_address_preference";
	public static String DEFAULT_WOL_MAC_ADDRESS = "90:e6:ba:5d:16:4e";

	public static String PREFKEY_WOL_DELAY_SECONDS = "wol_delay_seconds";
	public static float DEFAULT_WOL_DELAY_SECONDS = 8;
	
	public static String PREFKEY_HOME_ARRIVAL_ENABLE = "home_arrival_enable";
	public static boolean DEFAULT_HOME_ARRIVAL_ENABLE = true;

	public static String PREFKEY_HOME_ARRIVAL_TURN_ON_LIGHTS_ENABLE = "home_arrival_turn_on_lights";
	public static boolean DEFAULT_HOME_ARRIVAL_TURN_ON_LIGHTS_ENABLE = true;
	
	
	public static String PREFKEY_WOL_ENABLE = "wol_enable";
	public static boolean DEFAULT_WOL_ENABLE = true;
	
	
	public static String PREFKEY_BEDTIME_TURN_OFF_LIGHTS_ENABLE = "bedtime_turn_off_lights";
	public static boolean DEFAULT_BEDTIME_TURN_OFF_LIGHTS_ENABLE = true;
	
	
	public static String PREFKEY_BEDTIME_START_CLOCK_APP_ENABLE = "bedtime_start_clock_app";
	public static boolean DEFAULT_BEDTIME_START_CLOCK_APP_ENABLE = true;
	
	public static String PREFKEY_BEDTIME_BLANK_SCREEN_ENABLE = "bedtime_blank_screen";
	public static boolean DEFAULT_BEDTIME_BLANK_SCREEN_ENABLE = true;
	

	public static String PREFKEY_BEDTIME_SUSPEND_COMPUTER_ENABLE = "bedtime_suspend_computer";
	public static boolean DEFAULT_BEDTIME_SUSPEND_COMPUTER_ENABLE = true;
	
	public static String PREFKEY_TRIGGER_WIFI_NETWORK = "trigger_wifi_network";
	public static String DEFAULT_TRIGGER_WIFI_NETWORK = "our_house";
	
	
	public static String PREFKEY_LAST_ON_DATE_EPOCH = "last_on_date_epoch";
	public static String PREFKEY_RESET_ARRIVAL_LIGHTS = "reset_arrival_lights";
	

	static final long MILLISECONDS_PER_DAY = 1000L*60*60*24;
	public static boolean isLightsTriggeredToday(SharedPreferences settings, Calendar calendar) {
		
		boolean contains_pref = settings.contains(PreferencesServer.PREFKEY_LAST_ON_DATE_EPOCH);

		long day_index_today = calendar.getTimeInMillis()/MILLISECONDS_PER_DAY;
		long day_index_light = settings.getLong(PreferencesServer.PREFKEY_LAST_ON_DATE_EPOCH, 0)/MILLISECONDS_PER_DAY;

		boolean are_equal = day_index_today == day_index_light;
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
