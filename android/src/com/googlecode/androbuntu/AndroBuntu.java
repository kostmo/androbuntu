package com.googlecode.androbuntu;

import java.util.List;

import android.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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

import com.googlecode.Turntable3D.TurntableWidget;

public class AndroBuntu extends Activity implements View.OnClickListener {


	private SocketMonitor service_binder;
	
    private final int rotowidget_request_code = 42;
    private final int initialize_prefs_code = 43;

    private boolean do_binding() {
        Intent i = new Intent();
        i.setClass(AndroBuntu.this, SocketMonitor.class);
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
	    	i2.setClass(this, ServerPanel.class);

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
	}
   
   
   

   private ServiceConnection my_relay_service = new ServiceConnection() {
       public void onServiceConnected(ComponentName className, IBinder service) {


    	   service_binder = ((SocketMonitor.LocalBinder) service).getService();
    	   
    	   Log.d("forker", "Successfully connected to SocketMonitor service.");
       }

       public void onServiceDisconnected(ComponentName componentName) {


    	   Log.d("forker", "SocketMonitor service disconnected.");
       }
   };

   
   

   
   private View.OnClickListener screen_blank_listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	
	    	String[] reply = service_binder.send_message("screen_blank");
	    	
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(AndroBuntu.this);
			int saved_housecode_index = settings.getInt("house_code", 0);
		 	reply = service_binder.send_message("lights_off", Character.toString( (char) (saved_housecode_index + 'A') ) );
	    	Toast.makeText(AndroBuntu.this, reply[0], Toast.LENGTH_SHORT).show();
		 	
		 	
	    	PackageManager pm = getPackageManager();
	    	List<ResolveInfo> info = pm.queryIntentActivities(new Intent(Intent.ACTION_MAIN), 0);


	    	Intent alarmclock_intent = new Intent();
	    	/*
	    	for (ResolveInfo foo : info) {
	    		if (foo.activityInfo.packageName != null )
	    			Log.d("activ", "Activity name: " + foo.activityInfo.name);

	    		if (foo.activityInfo.applicationInfo.className != null )
	    			Log.d("activ", "Class name: " + foo.activityInfo.applicationInfo.className);
//	        	foo.activityInfo.getClass();
	        }
	    	*/

	    	alarmclock_intent.setClassName("com.ricket.doug.nightclock", "com.ricket.doug.nightclock.NightClockActivity");
	    	// Intent.FLAG_ACTIVITY_NEW_TASK
	    	try {
		    	startActivity(alarmclock_intent);
	    		
	    	}
	    	catch  (Exception e) {

		    	Toast.makeText(AndroBuntu.this, "Could not find Alarm Clock app.", Toast.LENGTH_SHORT).show();    
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
	    	i.setClass(this, ServerPanel.class);
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
       super.onDestroy();
   	unbindService(my_relay_service);
   }
}



