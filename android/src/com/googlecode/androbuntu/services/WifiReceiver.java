package com.googlecode.androbuntu.services;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;

import com.googlecode.androbuntu.PreferencesServer;

public class WifiReceiver extends BroadcastReceiver {

	static final String TAG = "AndroBuntu Wifi";


	@Override
	public void onReceive(Context context, Intent intent) {

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		Calendar c = new GregorianCalendar();

//		Log.e(TAG, "State changed? " + WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction()));
//		Log.e(TAG, "After 5? " + (c.get(Calendar.HOUR_OF_DAY) >= 12 + 5));
//		Log.e(TAG, "Before 9? " + (c.get(Calendar.HOUR_OF_DAY) < 12 + 9));
//		Log.e(TAG, "Not triggered yet today? " + !PreferencesServer.isLightsTriggeredToday(settings, c));
		
		if (	WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())
				&& c.get(Calendar.HOUR_OF_DAY) >= 12 + 5	// After 5pm
				&& c.get(Calendar.HOUR_OF_DAY) < 12 + 9	// Before 9pm
				&& c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY	// Not on Saturday
				&& c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY	// Not on Sunday
				&& !PreferencesServer.isLightsTriggeredToday(settings, c)) {

			NetworkInfo ni = (NetworkInfo) intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			if (ni.isConnected()) {

				WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
				String name = wifiInfo.getSSID();

				String trigger_network_name = settings.getString(PreferencesServer.PREFKEY_TRIGGER_WIFI_NETWORK, PreferencesServer.DEFAULT_TRIGGER_WIFI_NETWORK);

				if ( trigger_network_name.equals(name) ) {

					Intent i = new Intent();
					i.putExtra(ServiceBlankScreen.EXTRA_TURN_ON, true);
					i.setClass(context, ServiceBlankScreen.class);
					context.startService(i);

					settings.edit().putLong(PreferencesServer.PREFKEY_LAST_ON_DATE_EPOCH, c.getTimeInMillis()).commit();
				}
			}
		}
	}
}
