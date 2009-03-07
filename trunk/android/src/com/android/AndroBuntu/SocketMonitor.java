package com.android.AndroBuntu;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class SocketMonitor extends Service {
	
	
	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String DEFAULT_HOSTNAME = "192.168.0.9";
	
	

	private Socket MyClient;
	
	private BufferedWriter wr;
	private BufferedReader rd;
	

	private InetAddress addr;
	private int port;
	
    public class LocalBinder extends Binder {
    	SocketMonitor getService() {
            return SocketMonitor.this;
        }
    }
    
	
	@Override
	public void onCreate() {
		
		super.onCreate();
	    

	}
	
	
	
	@Override
	public void onDestroy() {
		
		super.onDestroy();
	
		Log.d("fork", "server onDestroy called.");
	}
	
	
	
    @Override
    public IBinder onBind(Intent intent) {
    	
		Log.d("fork", "About to retrieve preferences.");
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
    	
		Log.d("fork", "Retrieved preferences.");
    	String host = settings.getString("hostname_preference", "");
		Log.d("fork", "Retrieved hostname: " + host);

		String portstring = settings.getString("port_preference", "");
		
        port = Integer.parseInt(portstring);
		Log.d("fork", "Retrieved port: " + Integer.toString(port));
        
    	
	    try {

	    	addr = InetAddress.getByName( host );
	    		
	    } catch (IOException e) {
	        System.out.println(e);
	        
//         message = "Host lookup failed.";
	    }
    	
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();
	
	

	public String send_message(String command) {
    	
	    try {

	    	MyClient = new Socket(addr, port);
	    	
			Log.d("fork", "Created socket.");

        	wr = new BufferedWriter(new OutputStreamWriter(MyClient.getOutputStream()), 256);   
	        rd = new BufferedReader(new InputStreamReader(MyClient.getInputStream()), 256);
	    	
	    }
	    catch (IOException e) {
	        System.out.println(e);
	        
	        return "Socket instantiation failed.";
	    }
	    
		
		
		Log.d("fork", "about to send a message");
	   	String message = "";
    	
        try {
        	

			Log.d("fork", "About to write.");

            wr.write( command );
            

			Log.d("fork", "Succeeded in write.");
			
            wr.flush();
    	
	    	Log.d("fark", "Test1");
	    	

	        while (true) {
	        	
		    	Log.d("fark", "Top of while loop");
	        	
		        String str = null;
	        
	        	str = rd.readLine();

	        	
		    	Log.d("fark", "Read the string.");

	        	if (str == null) {
			    	Log.d("fark", "String is null.  Breaking.");
	        		break;
	        	}
		    	Log.d("fark", "Read the string; it is: "+str);
	        	
	        	
	        	message += str;
	        	
		    	Log.d("fark", "Aggregate message is: " + str);
	        }
	        
	        
	    	Log.d("fark", "Test2");

			
        } catch (IOException e) {
	    	Log.d("fark", "The buffered reader/writer failed somehow...");
        }


		Log.d("fork", "The message reply is: "+message);
		
		
		
		
		
	    try {
	    	

	        rd.close();	
			wr.close();
			
			
			MyClient.close();

	    }
	    catch (IOException e) {
	        System.out.println(e);
	        
//       	message = "Closing connection failed.";
	    }
		
		
		
	    return message;
	}
}
