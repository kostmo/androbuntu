package com.googlecode.androbuntu.services;


import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.androbuntu.AndroBuntu;
import com.googlecode.androbuntu.PreferencesServer;
import com.googlecode.androbuntu.task.SendServerCommandTask;


public class ServiceBlankScreen extends Service {

	static final String TAG = "ServiceBlankScreen";


	public static final String EXTRA_TURN_ON = "EXTRA_TURN_ON";
	
	private ServiceSocketMonitor service_binder;
	boolean turn_on = false;
	// ========================================================================
	@Override
	public int onStartCommand(Intent intent, int startId, int foo) {
		
		Log.d(TAG, "Service just started.");
		
		this.turn_on = intent.getBooleanExtra(EXTRA_TURN_ON, false);
		
		if (hasWifiConnection()) {
			do_binding();
		} else {
			Toast.makeText(this, "Not connected to WiFi!", Toast.LENGTH_SHORT).show();
//			stopSelf();	// XXX We mustn't attempt to stop the service since we haven't started yet!
		}

		return START_NOT_STICKY;
	}

	void sendLightsOnAndQuit() {
		AndroBuntu.wake_and_turn_on_lights(ServiceBlankScreen.this, service_binder);
		stopSelf();
	}
	
	
	boolean hasWifiConnection() {
    	WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    	
    	String connected_ssid = wm.getConnectionInfo().getSSID();
    	
    	Log.d(TAG, "Is WiFi enabled? " + wm.isWifiEnabled());
    	Log.d(TAG, "Currently connected SSID: " + connected_ssid);
    	return (wm.isWifiEnabled() && connected_ssid != null);
	}

	
	
	void sendScreenBlankAndQuit() {

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

		List<String> messages = new ArrayList<String>();
		if (settings.getBoolean(PreferencesServer.PREFKEY_BEDTIME_BLANK_SCREEN_ENABLE, PreferencesServer.DEFAULT_BEDTIME_BLANK_SCREEN_ENABLE))
			messages.add("screen_blank");

		boolean lights_off = settings.getBoolean(PreferencesServer.PREFKEY_BEDTIME_TURN_OFF_LIGHTS_ENABLE, PreferencesServer.DEFAULT_BEDTIME_TURN_OFF_LIGHTS_ENABLE);
		boolean suspend = settings.getBoolean(PreferencesServer.PREFKEY_BEDTIME_SUSPEND_COMPUTER_ENABLE, PreferencesServer.DEFAULT_BEDTIME_SUSPEND_COMPUTER_ENABLE);

		if (suspend && lights_off)
			messages.add("lights_off_with_suspend");
		else if (lights_off)
			messages.add("lights_off");
		else if (suspend)
			messages.add("suspend");

        new SendServerCommandTask(this, service_binder, messages).execute();

        boolean start_night_clock = settings.getBoolean(PreferencesServer.PREFKEY_BEDTIME_START_CLOCK_APP_ENABLE, PreferencesServer.DEFAULT_BEDTIME_START_CLOCK_APP_ENABLE);
		if (start_night_clock) {
			Intent alarmclock_intent = new Intent();
			alarmclock_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			alarmclock_intent.setClassName("com.android.alarmclock", "com.android.deskclock.DeskClock");
			try {
				startActivity(alarmclock_intent);
			} catch  (Exception e) {
				Toast.makeText(this, "Could not find Alarm Clock app.", Toast.LENGTH_SHORT).show();    
			}
		}

		stopSelf();
	}

	private boolean do_binding() {
		Intent i = new Intent();
		i.setClass(ServiceBlankScreen.this, ServiceSocketMonitor.class);
		return bindService(i, my_relay_service, BIND_AUTO_CREATE);
	}

	private ServiceConnection my_relay_service = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			service_binder = ((ServiceSocketMonitor.LocalBinder) service).getService();
			Log.d(TAG, "Successfully connected to SocketMonitor service.");

			if (turn_on) {
				Log.d(TAG, "I will turn on the lights...");
				sendLightsOnAndQuit();
			} else {
				sendScreenBlankAndQuit();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			Log.d(TAG, "SocketMonitor service disconnected.");
		}
	};


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		unbindService(my_relay_service);
		super.onDestroy();
	}
}