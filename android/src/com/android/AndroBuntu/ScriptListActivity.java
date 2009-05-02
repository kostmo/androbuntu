package com.android.AndroBuntu;

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


    private String[] mStrings = {
            "Abondance", "Ackawi", "Acorn"
    };
    

	private SocketMonitor service_binder;
	
	
    
    @Override
    protected void onCreate(Bundle icicle){
        super.onCreate(icicle);
        
        
        
		Intent i = new Intent();
		i.setClass(ScriptListActivity.this, SocketMonitor.class);
		boolean connect_successful = bindService(i, my_relay_service, BIND_AUTO_CREATE);
		   
		if (connect_successful) {
			
		}
        
        

        // We'll define a custom screen layout here (the one shown above), but
        // typically, you could just use the standard ListActivity layout.
 //       setContentView(android.R.layout.simple_list_item_1);

        
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrings);
       
        
        setListAdapter(adapter);
    }
    
    
    
    
    
    
    
    
	   
	   private ServiceConnection my_relay_service = new ServiceConnection() {
	       public void onServiceConnected(ComponentName className, IBinder service) {


	    	   service_binder = ((SocketMonitor.LocalBinder) service).getService();
	    	   
	    	   Log.d("forker", "Successfully connected to SocketMonitor service.");
	    	   
	    	   
	    	   
	    	   
	    	   
	    	   // TODO
			   String reply = service_binder.send_message( "list_scripts" );
			   Toast.makeText(ScriptListActivity.this, reply, Toast.LENGTH_SHORT).show();
			   
			   
			   
			   // TODO:
			   /*
			   import javax.xml.parsers.DocumentBuilder;
			   parse(InputStream stream);
			   */
	       }

	       public void onServiceDisconnected(ComponentName componentName) {


	    	   Log.d("forker", "SocketMonitor service disconnected.");
	       }
	   };

	   
	   

	   @Override
	   protected void onListItemClick(ListView l, View v, int position, long id) {
		   
		   String clickedrow = (String) getListView().getItemAtPosition(position);
		   Toast.makeText(ScriptListActivity.this, clickedrow, Toast.LENGTH_SHORT).show();
		   
	   }
	   
	   
	   
	   
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();

	    	unbindService(my_relay_service);
	    }
}

