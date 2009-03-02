package com.android.AndroBuntu;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.android.AndroBuntu.R;
import com.android.Turntable3D.TurntableWidget;



public class AndroBuntu extends Activity implements View.OnClickListener {

	private EditText et;
	private SocketMonitor service_binder;
	
	private int androbuntu_port = 46645;

    private String[] mStrings = {
            "Abondance", "Ackawi", "Acorn"
    };
    
    private int rotowidget_request_code = -1;
    
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       
       
       
       TextView tv = new TextView(this);
       et = new EditText(this);
       tv.setText( R.string.goodbye );
       et.setText( "192.168.0.9" );
       
       
       
       
       // FIXME
       Intent i = new Intent();
       i.setClass(AndroBuntu.this, SocketMonitor.class);
       
       
       String host = et.getText().toString();
       i.putExtra("hostname", host);
       i.putExtra("port", androbuntu_port);
		
		
       boolean connect_successful = bindService(i, my_relay_service, BIND_AUTO_CREATE);
       
       
       
       
       

       LinearLayout myfoo = new LinearLayout(this);
       myfoo.setOrientation(LinearLayout.VERTICAL);
       
       
       

       Button media_button = new Button(this);
       media_button.setOnClickListener(media_button_listener);
       media_button.setText("Media Controls");

       Button x10_button = new Button(this);
       x10_button.setOnClickListener(x10_button_listener);
       x10_button.setText("x10 Controls");
       
       
       Button logo_button = new Button(this);
       logo_button.setOnClickListener(logo_button_listener);
       logo_button.setText("Logo");
       
      
       Button scripts_button = new Button(this);
       scripts_button.setOnClickListener(scripts_button_listener);
       scripts_button.setText("Invoke Script");
        
       
       
       LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(-1, -1);
       l.weight = 1f;
       

        
        Button screen_blank_button = new Button(this);
        screen_blank_button.setOnClickListener(screen_blank_listener);
        screen_blank_button.setText("Blank Screen");
        myfoo.addView(screen_blank_button);
        
        
        
        
        myfoo.addView(media_button);
        myfoo.addView(x10_button);
        myfoo.addView(scripts_button);
        myfoo.addView(logo_button);
        
        myfoo.addView(tv);
        myfoo.addView(et);

       
        
        ListView lv = new ListView(this);
        lv.setAdapter( new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrings) );
       
        myfoo.addView(lv);
        
       setContentView(myfoo);
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
	    	
	    	String reply = service_binder.send_message("screen_blank");    	
		   Toast.makeText(AndroBuntu.this, reply, Toast.LENGTH_SHORT).show();
	    }
	};
	
	
	
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
    	String reply;
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
    	
    	Toast.makeText(AndroBuntu.this, reply, Toast.LENGTH_SHORT).show();
    	
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
	    	
	    	rotowidget_request_code = 42;
	    	AndroBuntu.this.startActivityForResult(i, rotowidget_request_code);
	    }
	};
	
	
   private View.OnClickListener scripts_button_listener = new View.OnClickListener() {
	    public void onClick(View v) {
	    	
	    	Intent i = new Intent();
	    	i.setClass(AndroBuntu.this, ScriptListActivity.class);
	    }
	};
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	
		menu.add("Server Options");
		menu.add("Greet!");

		return true;

    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	int itemid = item.getItemId();
    	if (itemid == 0) {	// FIXME
    		
	    	Intent i = new Intent();
	    	i.setClass(this, ServerPanel.class);
	    	startActivity(i);

    	} else {
    		
        	String reply = service_binder.send_message("greet");    	
        	Toast.makeText(AndroBuntu.this, Integer.toString(itemid) + ": "+reply, Toast.LENGTH_SHORT).show();
    	}

		return true;
    }
    
    
    
   public void onClick(View v) {
	
	   // FIXME
	   
	   String reply = service_binder.send_message("XF86AudioMute");
	   Toast.makeText(this, reply, Toast.LENGTH_SHORT).show();
   }

   

   
   
   // TODO
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	   
	   if (requestCode == rotowidget_request_code) {
		   

           if (resultCode == RESULT_OK) {
               // A contact was picked.  Here we will just display it
               // to the user.
        	   Toast.makeText(AndroBuntu.this, "Nice weather, today.", Toast.LENGTH_SHORT).show();
           } else {

        	   Toast.makeText(AndroBuntu.this, "Roto-cancel.", Toast.LENGTH_SHORT).show();
           }
	   }
   }
}


