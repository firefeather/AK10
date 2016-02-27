package com.szaoto.ak10.commsdk;

import android.util.Log;

public class Wirednethotplugdetect {

	static public native int Net_detect(int fd, String devName);
	static public native int OpenSocket();
	static public native void CloseSocket(int fd);
	
    static {
        try {
        	System.loadLibrary("netdetect");
        } catch (UnsatisfiedLinkError e) {
            Log.d("netdetect", "netdetect  not found!");
        }
    }
}
