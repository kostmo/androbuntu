package com.android.AndroBuntu;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.Socket;

import org.xmlpull.v1.XmlSerializer;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Xml;

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
		
		return send_message(command, null);
	}
	

	public String send_message(String command, String payload) {
    	
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
	   	
	   	
	   	
	   	String transmission_string = "";
	   	
	   	
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
			serializer.setOutput(writer);
			

			serializer.startDocument(null, null);

			serializer.startTag(null, "root");
			serializer.startTag(null, "command");
			serializer.text( command );
			serializer.endTag(null, "command");
			
			
			if (payload != null) {
				// TODO: Implement the payload as an "attribute" to the tag
				serializer.startTag(null, "payload");
				serializer.text( payload );
				serializer.endTag(null, "payload");	
				
			}
			serializer.endTag(null, "root");
			serializer.endDocument();
			serializer.flush();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	   	
	   	
		transmission_string = writer.toString();

	   	
    	
        try {
        	

            wr.write( transmission_string );
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
