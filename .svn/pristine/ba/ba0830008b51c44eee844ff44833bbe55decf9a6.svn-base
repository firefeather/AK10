/*
   * 文件名 SpiControler.java
   * 包含类名列表com.szaoto.ak10.commsdk
   * 版本信息，版本号
   * 创建日期2013年12月28日下午9:26:59
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.commsdk;

import android.util.Log;

/*
 * 类名SpiControler
 * 作者 liangdb
 * 主要功能 spi控制接口
 * 创建日期2013年12月28日
 * 修改者，修改日期，修改内容
 */
public class SpiControler {

	/**
	 * 
	 */
	public SpiControler() {
		// TODO Auto-generated constructor stub
	}
	
	/* SPI */
	//mode_set
	public static final int SPI_CPHA = 0x01;
	public static final int SPI_CPOL = 0x02;
	public static final int SPI_MODE_0 = (0|0);
	public static final int SPI_MODE_1 = (0|SPI_CPHA);
	public static final int SPI_MODE_2 = (SPI_CPOL|0);
	public static final int SPI_MODE_3	 = (SPI_CPOL|SPI_CPHA);
	public static final int SPI_CS_HIGH = 0x04;
	public static final int SPI_LSB_FIRST = 0x08;
	public static final int SPI_3WIRE = 0x10;
	public static final int SPI_LOOP = 0x20;
	public static final int SPI_NO_CS = 0x40;
	public static final int SPI_READY = 0x80;

	//bits_set:4~32bit

	//speed_set:
	public static final int MAX_SPEED = (500 * 1000);
	public static final int DEFALUT = (500 * 1000);
	
	static public native int open_spi_device(String devName);
	static public native int spi_cfg(int fd,int mode_set,int bits_set,int speed_set);
	static public native void close_spi_device(int fd);
	static public native int spi_write(int fd, byte data[], int length);
	static public native byte[] spi_read(int fd, int length);
	
	static public native byte[] spi_transfer(int fd, byte data[], int length);
	
	static public native int read(int fd, byte[] buf, int len);
	static public native int select(int fd, int sec, int usec);

	static public native int spi_getlasterror();
	
	static {
        try {
        	System.loadLibrary("spi");
        } catch (UnsatisfiedLinkError e) {
            Log.d("spi", "spi library not found!");
        }
    }

	
}
