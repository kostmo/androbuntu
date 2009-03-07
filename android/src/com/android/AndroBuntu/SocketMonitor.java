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
    	
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
    	String host = settings.getString("hostname_preference", "");
		String portstring = settings.getString("port_preference", "0");
        port = Integer.parseInt(portstring);
    
    	
	    try {

	    	addr = InetAddress.getByName( host );
	    		
	    } catch (IOException e) {
	        System.out.println(e);
	        
	        return null;
	    }
    	
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();
	
	

	public String send_message(String command) {
    	
	    try {

	    	MyClient = new Socket(addr, port);
	    	

        	wr = new BufferedWriter(new OutputStreamWriter(MyClient.getOutputStream()), 256);   
	        rd = new BufferedReader(new InputStreamReader(MyClient.getInputStream()), 256);
	    	
	    }
	    catch (IOException e) {
	        System.out.println(e);
	        
	        return "Socket instantiation failed.";
	    }
	    
		
	   	String message = "";
    	
        try {
        	

            wr.write( command );
            wr.flush();
    	

	        while (true) {
	        	
	        	
		        String str = null;
	        
	        	str = rd.readLine();

	        	

	        	if (str == null)
	        		break;

	        	
	        	message += str;
	        	
	        }
	        
	        

			
        } catch (IOException e) {
	    	Log.d("fark", "The buffered reader/writer failed somehow...");
        }
		
		
		
	    try {

	        rd.close();	
			wr.close();
			
			
			MyClient.close();

	    }
	    catch (IOException e) {
	        System.out.println(e);
	        
	        message = "Closing connection failed.";
	    }
		
		
		
	    return message;
	}
}
