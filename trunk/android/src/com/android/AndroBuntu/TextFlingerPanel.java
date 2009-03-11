package com.android.AndroBuntu;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class TextFlingerPanel extends Activity implements View.OnClickListener {

	private EditText et;
		private SocketMonitor service_binder;
	
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
		   super.onCreate(savedInstanceState);


		   
	       Intent i = new Intent();
	       i.setClass(TextFlingerPanel.this, SocketMonitor.class);
	       boolean connect_successful = bindService(i, my_relay_service, BIND_AUTO_CREATE);
	       
		   

	       
	       Button button = new Button(this);
	       button.setOnClickListener(this);
	       button.setText("Deposit text");
		   
	
		   
	       LinearLayout myfoo = new LinearLayout(this);
	       myfoo.setOrientation(LinearLayout.VERTICAL);
   
	       
	       
	       et = new EditText(this);

	        myfoo.addView(et);

	        myfoo.addView(button);
			   
	        
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

	   
	   
	   
	
	   public void onClick(View v) {
		   String flingtext = et.getText().toString();
		   String reply = service_binder.send_message("fling_text:"+flingtext);
		   Toast.makeText(TextFlingerPanel.this, reply, Toast.LENGTH_SHORT).show();
	   }
	   
	   
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();

	    	unbindService(my_relay_service);
	    }
}
