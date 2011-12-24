package com.googlecode.androbuntu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.googlecode.androbuntu.Turntable3D.TurntableWidget;
import com.googlecode.androbuntu.services.ServiceSocketMonitor;
import com.googlecode.androbuntu.task.SendServerCommandTask;
import com.googlecode.androbuntu.task.WakeUpComputerTask;

public class AndroBuntu extends Activity implements View.OnClickListener {

	private static String TAG = "AndroBuntu";
	

	private ServiceSocketMonitor service_binder = null;

	private final int rotowidget_request_code = 42;
	private final int initialize_prefs_code = 43;

	private boolean do_binding() {
		Intent i = new Intent();
		i.setClass(AndroBuntu.this, ServiceSocketMonitor.class);
		return bindService(i, my_relay_service, BIND_AUTO_CREATE);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String host = settings.getString("hostname_preference", null);


		if (host == null) {

			Toast.makeText(AndroBuntu.this, "Please specify server.", Toast.LENGTH_SHORT).show();

			Intent i2 = new Intent();
			i2.setClass(this, PreferencesServer.class);

			AndroBuntu.this.startActivityForResult(i2, initialize_prefs_code);
		}
		else do_binding();





		setContentView(R.layout.main);

		ImageButton screen_blank_button = (ImageButton) findViewById(R.id.screen_blank_button);
		screen_blank_button.setOnClickListener(screen_blank_listener);


		Button jotter_button = (Button) findViewById(R.id.jotter_button);
		jotter_button.setOnClickListener(jotter_button_listener);

		ImageButton x10_button = (ImageButton) findViewById(R.id.x10_button);
		x10_button.setOnClickListener(x10_button_listener);


		ImageButton logo_button = (ImageButton) findViewById(R.id.logo_button);
		logo_button.setOnClickListener(logo_button_listener);


		Button scripts_button = (Button) findViewById(R.id.scripts_button);
		scripts_button.setOnClickListener(scripts_button_listener);


		Button media_button = (Button) findViewById(R.id.media_button);
		media_button.setOnClickListener(media_button_listener);
		

		Button wol_button = (Button) findViewById(R.id.wol_button);
		wol_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				wake_and_turn_on_lights(AndroBuntu.this, service_binder);

			}
		});
	}

	
	public static void wake_and_turn_on_lights(final Context context, final ServiceSocketMonitor service_binder) {
		
		Log.d(TAG, "wake_and_turn_on_lights()");
		
		new WakeUpComputerTask(context, new Runnable() { 
	         @Override
			public void run() {
	    		List<String> messages = new ArrayList<String>();
				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
	    		if (settings.getBoolean(PreferencesServer.PREFKEY_HOME_ARRIVAL_TURN_ON_LIGHTS_ENABLE, PreferencesServer.DEFAULT_HOME_ARRIVAL_TURN_ON_LIGHTS_ENABLE))
	    			messages.add("lights_on");
	            new SendServerCommandTask(context, service_binder, messages).execute(); 
	         } 
	    }).execute();
	}

	
	private ServiceConnection my_relay_service = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			
			Log.d(TAG, "Successfully bound to socket service...");
			service_binder = ((ServiceSocketMonitor.LocalBinder) service).getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			Log.d(TAG, "Socket service disconnected.");
		}
	};

	
	private View.OnClickListener screen_blank_listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			Log.d(TAG, "Will try to turn the lights off now.");
			
			List<String> messages = new ArrayList<String>();
//			messages.add("screen_blank");
//			messages.add("lights_off");
			messages.add("lights_off_with_suspend");
            new SendServerCommandTask(AndroBuntu.this, service_binder, messages).execute(); 
			
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(AndroBuntu.this);
	        boolean start_night_clock = settings.getBoolean(PreferencesServer.PREFKEY_BEDTIME_START_CLOCK_APP_ENABLE, PreferencesServer.DEFAULT_BEDTIME_START_CLOCK_APP_ENABLE);
			if (start_night_clock) {
				Intent alarmclock_intent = new Intent();
				alarmclock_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				alarmclock_intent.setClassName("com.android.alarmclock", "com.android.deskclock.DeskClock");
				try {
					startActivity(alarmclock_intent);
				} catch  (Exception e) {
					Toast.makeText(AndroBuntu.this, "Could not find Alarm Clock app.", Toast.LENGTH_SHORT).show();    
				}
			}
		}
	};


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		String[] reply;
		switch (keyCode) {

		case KeyEvent.KEYCODE_VOLUME_DOWN:
			reply = service_binder.send_message("XF86AudioLowerVolume");    	
			break;

		case KeyEvent.KEYCODE_VOLUME_UP:
			reply = service_binder.send_message("XF86AudioRaiseVolume");    	
			break;

		case KeyEvent.KEYCODE_CAMERA:
			reply = service_binder.send_message("XF86AudioMute");
			break;

		default:
			return super.onKeyDown(keyCode, event);
		}

		Toast.makeText(AndroBuntu.this, reply[0], Toast.LENGTH_SHORT).show();

		return true;
	}

	private View.OnClickListener media_button_listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent i = new Intent();
			i.setClass(AndroBuntu.this, MediaPanel.class);

			startActivity(i);
		}
	};

	private View.OnClickListener x10_button_listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent i = new Intent();
			i.setClass(AndroBuntu.this, X10.class);
			startActivity(i);
		}
	};

	private View.OnClickListener logo_button_listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent i = new Intent();
			i.setClass(AndroBuntu.this, TurntableWidget.class);

			AndroBuntu.this.startActivityForResult(i, rotowidget_request_code);
		}
	};


	private View.OnClickListener scripts_button_listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent i = new Intent();
			i.setClass(AndroBuntu.this, ScriptListActivity.class);
			startActivity(i);
		}
	};

	private View.OnClickListener jotter_button_listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			Intent i = new Intent();
			i.setClass(AndroBuntu.this, TextFlingerPanel.class);
			startActivity(i);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.NONE, 0, Menu.NONE, "Server Options");
		menu.add(Menu.NONE, 1, Menu.NONE, "Greet!");

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int itemid = item.getItemId();
		switch (itemid) {
		case 0:	// FIXME

			Intent i = new Intent();
			i.setClass(this, PreferencesServer.class);
			startActivity(i);
			break;

		default:

			String[] reply = service_binder.send_message("greet");    	
			Toast.makeText(AndroBuntu.this, Integer.toString(itemid) + ": "+reply[0], Toast.LENGTH_SHORT).show();
		}

		return true;
	}


	@Override
	public void onClick(View v) {

		// FIXME

		String[] reply = service_binder.send_message("XF86AudioMute");
		Toast.makeText(this, reply[0], Toast.LENGTH_SHORT).show();
	}





	// TODO
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {


		switch (requestCode) {
		case rotowidget_request_code:

			if (resultCode == RESULT_OK) {
				// A contact was picked.  Here we will just display it
				// to the user.
				Toast.makeText(AndroBuntu.this, "Nice weather, today.", Toast.LENGTH_SHORT).show();
			} else {

				Toast.makeText(AndroBuntu.this, "Roto-cancel.", Toast.LENGTH_SHORT).show();
			}

			break;
		case initialize_prefs_code:

			do_binding();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		unbindService(my_relay_service);
		super.onDestroy();
	}
}



