/*
   * 文件名 SpiControl.java
   * 包含类名列表com.szaoto.ak10.commsdk
   * 版本信息，版本号
   * 创建日期2013年12月31日下午6:00:26
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.commsdk;

/*
 * 类名SpiControl
 * 作者 liangdb
 * 主要功能
 * 创建日期2013年12月31日
 * 修改者，修改日期，修改内容
 */
public class SpiControl {
	private static int m_fd = -1;
	
	public static int OpenSpiDevice()
	{
		int fd = SpiControler.open_spi_device("/dev/spidev0.0");
		//m_fd = SpiControler.open_spi_device("/dev/spidev0.0");
		m_fd = fd;
		return SpiControler.spi_cfg(m_fd,SpiControler.SPI_MODE_3,8,SpiControler.MAX_SPEED);
	}
	public static void CloseSpiDevice() {
		SpiControler.close_spi_device(m_fd);
	}
	public static int WriteSpi(byte data[], int length) {
		return SpiControler.spi_write(m_fd,data,length);
	}
	public static byte[] ReadSpi(int length) {
		return SpiControler.spi_read(m_fd,length);
	}
	public static int GetLastError(){
		return SpiControler.spi_getlasterror();
	}
	
	public static void Setfd(int fd){
		m_fd = fd;
	}
	
	public static int Getfd(){
		return m_fd;
	}
}
