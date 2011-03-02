package com.googlecode.androbuntu.services;


import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.googlecode.androbuntu.AndroBuntu;


public class ServiceBlankScreen extends Service {

	static final String TAG = "ServiceBlankScreen";


	public static final String EXTRA_TURN_ON = "EXTRA_TURN_ON";
	
	private ServiceSocketMonitor service_binder;
	boolean turn_on = false;
	// ========================================================================
	@Override
	public void onStart(Intent intent, int startId) {
		
		Log.d(TAG, "Service just started.");
		
		this.turn_on = intent.getBooleanExtra(EXTRA_TURN_ON, false);
		do_binding();
	}

	
	void sendLightsOnAndQuit() {

		List<String> messages = new ArrayList<String>();
		messages.add("lights_on");
		
		AndroBuntu.send_lights_out( this, messages, service_binder );
		stopSelf();
	}
	
	void sendScreenBlankAndQuit() {

		List<String> messages = new ArrayList<String>();
		messages.add("screen_blank");
		messages.add("lights_off");
		
		AndroBuntu.send_lights_out( this, messages, service_binder );
		stopSelf();
	}

	private boolean do_binding() {
		Intent i = new Intent();
		i.setClass(ServiceBlankScreen.this, ServiceSocketMonitor.class);
		return bindService(i, my_relay_service, BIND_AUTO_CREATE);
	}

	private ServiceConnection my_relay_service = new ServiceConnection() {
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