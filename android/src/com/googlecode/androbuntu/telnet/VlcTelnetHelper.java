package com.googlecode.androbuntu.telnet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;

import org.apache.commons.net.telnet.TelnetClient;

public class VlcTelnetHelper {
	 
	 static final String REMOTE_HOST = "192.168.1.17";
	 static final int REMOTE_PORT = 4212;
	 static final String TELNET_PASSWORD = "admin";
	 
	public static void sendCommand(String command) throws SocketException, IOException {
		TelnetClient tc = new TelnetClient();
//        tc.registerSpyStream(System.out);
        tc.connect(REMOTE_HOST, REMOTE_PORT);

        PrintWriter pw = new PrintWriter(tc.getOutputStream());
        pw.println(TELNET_PASSWORD);
        pw.flush();

        pw.println(command);
        pw.flush();
	}
}
