package com.googlecode.androbuntu;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
			public void onClick(View v) {

				
				Log.d(TAG, "Sending wake packet...");
				String[] args = new String[] {"192.168.0.80", "90:e6:ba:5d:16:4e"};
				WakeUpPC(args);
				
				Log.d(TAG, "Wake packet sent!");
				
				Handler handler = new Handler(); 
			    handler.postDelayed(new Runnable() { 
			         public void run() { 
			              new WakeAndLightsOnTask().execute(); 
			         } 
			    }, 8000); 
			}
		});
	}

	
	
	public class WakeAndLightsOnTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			
			// FIXME
			// The socket command should go in here!
			
			return null;
		}

		@Override
		protected void onPostExecute (Void voided) {
			

			Log.d(TAG, "Running lights on task");
			List<String> messages = new ArrayList<String>();			
			messages.add("lights_on");
			send_lights_command( AndroBuntu.this, messages, service_binder );
		}
	}
	
	
	 
    public static final int PORT = 9;    
    
    public static void WakeUpPC(String[] args) {
        
              
        String ipStr = args[0];
        String macStr = args[1];
        
        try {
            byte[] macBytes = getMacBytes(macStr);
            byte[] bytes = new byte[6 + 16 * macBytes.length];
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) 0xff;
            }
            for (int i = 6; i < bytes.length; i += macBytes.length) {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }
            
            InetAddress address = InetAddress.getByName(ipStr);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, PORT);
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();
            
                    }
        catch (Exception e) {
            //System.out.println("Failed to send Wake-on-LAN packet: + e");
            System.exit(1); 
           //NOTE exception or error here will close Android-application 
        }
        
    }
    
    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        if (hex.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address."); 
      //NOTE also this error will close Android-application
        }
        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex digit in MAC address."); 
       //NOTE also this error will close Android-application
        }
        return bytes;
    }
    
   
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private ServiceConnection my_relay_service = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			
			Log.d(TAG, "Successfully bound to socket service...");
			
			service_binder = ((ServiceSocketMonitor.LocalBinder) service).getService();
		}

		public void onServiceDisconnected(ComponentName componentName) {
		}
	};

	
	public static void send_lights_command(Context context, List<String> messages, ServiceSocketMonitor service_binder) {
		if (service_binder == null) {
			Log.e(TAG, "I can't do this, because the service isn't bound.");
			return;
		}
		
		
		Log.d(TAG, "Sending lights command...");
		
		String[] reply;

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		int saved_housecode_index = settings.getInt(PreferencesServer.PREFKEY_HOUSE_CODE_INDEX, PreferencesServer.DEFAULT_HOUSE_CODE_INDEX);
		
		for (String message : messages) {
			
			reply = service_binder.send_message(message, Character.toString( (char) (saved_housecode_index + 'A') ) );
			if (reply.length > 0)
				Toast.makeText(context, reply[0], Toast.LENGTH_SHORT).show();
		}


		boolean start_night_clock = true;
		
		if (start_night_clock) {
			PackageManager pm = context.getPackageManager();
			List<ResolveInfo> info = pm.queryIntentActivities(new Intent(Intent.ACTION_MAIN), 0);
	
	
			Intent alarmclock_intent = new Intent();
			alarmclock_intent.setClassName("com.android.alarmclock", "com.android.deskclock.DeskClock");
			// Intent.FLAG_ACTIVITY_NEW_TASK
			try {
				context.startActivity(alarmclock_intent);
			}
			catch  (Exception e) {
				Toast.makeText(context, "Could not find Alarm Clock app.", Toast.LENGTH_SHORT).show();    
			}
		}
	}

	
	
	
	private View.OnClickListener screen_blank_listener = new View.OnClickListener() {
		public void onClick(View v) {

			Log.d(TAG, "Will try to turn the lights off now.");
			
			List<String> messages = new ArrayList<String>();
			messages.add("screen_blank");
			messages.add("lights_off");
			
			send_lights_command( AndroBuntu.this, messages, service_binder );
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
		public void onClick(View v) {

			Intent i = new Intent();
			i.setClass(AndroBuntu.this, MediaPanel.class);

			startActivity(i);
		}
	};

	private View.OnClickListener x10_button_listener = new View.OnClickListener() {
		public void onClick(View v) {

			Intent i = new Intent();
			i.setClass(AndroBuntu.this, X10.class);
			startActivity(i);
		}
	};

	private View.OnClickListener logo_button_listener = new View.OnClickListener() {
		public void onClick(View v) {

			Intent i = new Intent();
			i.setClass(AndroBuntu.this, TurntableWidget.class);

			AndroBuntu.this.startActivityForResult(i, rotowidget_request_code);
		}
	};


	private View.OnClickListener scripts_button_listener = new View.OnClickListener() {
		public void onClick(View v) {

			Intent i = new Intent();
			i.setClass(AndroBuntu.this, ScriptListActivity.class);
			startActivity(i);
		}
	};

	private View.OnClickListener jotter_button_listener = new View.OnClickListener() {
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


	public void onClick(View v) {

		// FIXME

		String[] reply = service_binder.send_message("XF86AudioMute");
		Toast.makeText(this, reply[0], Toast.LENGTH_SHORT).show();
	}





	// TODO
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



