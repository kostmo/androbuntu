package com.googlecode.androbuntu;

import com.googlecode.androbuntu.services.ServiceSocketMonitor;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Bundle;
import android.os.IBinder;

public class ScriptListActivity extends ListActivity {

	private ServiceSocketMonitor service_binder;
	
	
    
    @Override
    protected void onCreate(Bundle icicle){
        super.onCreate(icicle);
        
        
        
		Intent i = new Intent();
		i.setClass(ScriptListActivity.this, ServiceSocketMonitor.class);
		boolean connect_successful = bindService(i, my_relay_service, BIND_AUTO_CREATE);
		   
		if (connect_successful) {
			
		}

    }
    
    
    
    
    
    
    
    
	   
	   private ServiceConnection my_relay_service = new ServiceConnection() {
	       public void onServiceConnected(ComponentName className, IBinder service) {


	    	   service_binder = ((ServiceSocketMonitor.LocalBinder) service).getService();
	    	   
	    	   Log.d("forker", "Successfully connected to SocketMonitor service.");
	    	   
	    	   
	    	   
	    	   
	    	   
			   String[] reply = service_binder.send_message( "list_scripts" );
			   
		       ListAdapter adapter = new ArrayAdapter<String>(ScriptListActivity.this, android.R.layout.simple_list_item_1, reply);
		       setListAdapter(adapter);

			   
	       }

	       public void onServiceDisconnected(ComponentName componentName) {

	    	   Log.d("forker", "SocketMonitor service disconnected.");
	       }
	   };

	   
	   

	   @Override
	   protected void onListItemClick(ListView l, View v, int position, long id) {
		   
		   String clickedrow = (String) getListView().getItemAtPosition(position);

		   String[] reply = service_binder.send_message( clickedrow );

		   Toast.makeText(ScriptListActivity.this, reply[0], Toast.LENGTH_SHORT).show();	   
	   }
	   
	   
	   
	   
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();

	    	unbindService(my_relay_service);
	    }
}

