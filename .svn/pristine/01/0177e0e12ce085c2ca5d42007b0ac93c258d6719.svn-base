package com.friendlyarm.AndroidSDK;
import android.util.Log;

public class HardwareControler
{
	/* Serial Port */
	static public native int openSerialPort( String devName, long baud, int dataBits, int stopBits );
	
	/* LED */
	static public native int setLedState( int ledID, int ledState );
    
    /* PWM */
	static public native int PWMPlay(int frequency);
	static public native int PWMStop();
    
    /* ADC */
	static public native int readADC();
	static public native int readADCWithChannel(int channel);
    static public native int[] readADCWithChannels(int[] channels);
	
	/* I2C */
	static public native int openI2CDevice();
	static public native int writeByteDataToI2C(int fd, int pos, byte byteData);
	static public native int readByteDataFromI2C(int fd, int pos);
	
	/* I/O */
	static public native int write(int fd, byte[] data);
	static public native int read(int fd, byte[] buf, int len);
	static public native int select(int fd, int sec, int usec);
	static public native void close(int fd);

	/* Ethernet Setting (only for 4412) */
	static public native int loadEthConfig();
	static public native int saveEthConfig();

	static public native int isEthUsingDHCP();
    static public native String getEthIP ();
    static public native String getEthNetmask ();
    static public native String getEthGateway ();
    static public native String getEthDns1 ();
    static public native String getEthDns2 ();

	static public native void setEthUsingDHCP(int dhcp);
    static public native void setEthIP (String ip);
    static public native void setEthNetmask (String netmask);
    static public native void setEthGateway (String gateway);
    static public native void setEthDns1 (String dns1);
    static public native void setEthDns2 (String dns2);

    /* return 6410 or 210 */
    static public native int getBoardType();

    static {
        try {
        	System.loadLibrary("friendlyarm-hardware");
        } catch (UnsatisfiedLinkError e) {
            Log.d("HardwareControler", "libfriendlyarm-hardware library not found!");
        }
    }
}