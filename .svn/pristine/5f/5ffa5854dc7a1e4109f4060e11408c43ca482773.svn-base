#include "com_szaoto_ak10_commsdk_SpiControler.h"

/*
 *Author:m.g
 *Date:2013.12.17
 *Description:spidev_test.c
 *
 */

#include <stdint.h>
//#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <linux/types.h>
//#include <linux/spi/spidev.h>
//#include "/opt/FriendlyARM/tiny4412/android/linux-3.5/include/linux/spi/spidev.h"

#include "../util/util.h"

#define ARRAY_SIZE(a) (sizeof(a) / sizeof((a)[0]))
#define MAX_RX_BUFF 				65536
#define MAX_DEVICE_BUFF 	4096
//
#define CMD_WR_DAT				0x50
#define CMD_RD_DAT				0x51
#define CMD_WR_CFG				0x52
#define CMD_RD_CFG				0x53

static char *device = "/dev/spidev0.0";
static uint8_t mode = 0;
static uint8_t bits = 8;
static uint32_t speed = 500000;
static uint16_t delay;

//static uint8_t tx[MAX_DEVICE_BUFF] = {0, };
static uint8_t rx_buf[MAX_RX_BUFF] = {0, };
static int rx_buf_length = 0;

static int g_lasterror = -1;

//CTool tool;

/*
 * Class:     com_szaoto_ak10_commsdk_HardwareControler
 * Method:    open_spi_device
 * Signature: (Ljava/lang/String;)I
 */
 jint  Java_com_szaoto_ak10_commsdk_SpiControler_open_1spi_1device
  (JNIEnv *env, jclass obj, jstring device_name)
{
	int ret = 0;
	int fd;

	if(device_name != NULL)
//		device = CTool::jstringTostring(env,device_name);

	fd = open(device, O_RDWR);

	if (fd < 0){
		printf("can't open device %s\n",&device);
       return -1;
	}

	return fd;
}

 /*
    * Class:     com_szaoto_ak10_commsdk_HardwareControler
    * Method:    spi_cfg
    * Signature: (IIII)I
    */
  jint  Java_com_szaoto_ak10_commsdk_SpiControler_spi_1cfg
   (JNIEnv *env, jclass obj, jint fd, jint mode_set, jint bits_set, jint speed_set)
 {
 	int ret;

 	if(mode != 0){
 		mode = mode_set;
 	}
 	//ret = ioctl(fd, SPI_IOC_WR_MODE, &mode);
 	if (ret == -1){
 		perror("can't set spi mode\n");
 		return -1;
 	}

 	if(bits_set != 0){
 		bits = bits_set;
 	}
 	//ret = ioctl(fd, SPI_IOC_WR_BITS_PER_WORD, &bits);
 	if (ret == -1){
 		perror("can't set bits per word");
 		return -2;
 	}

 	if(speed_set != 0){
 		speed = speed_set;
 	}
 //	ret = ioctl(fd, SPI_IOC_WR_MAX_SPEED_HZ, &speed);
 	if (ret == -1){
 		perror("can't set max speed hz");
 		return -3;
 	}

 	printf("spi mode: %d\n", mode);
 	printf("bits per word: %d\n", bits);
 	printf("max speed: %d Hz (%d KHz)\n", speed, speed/1000);

 	return 0;
 }
 /*
  * Class:     com_szaoto_ak10_commsdk_HardwareControler
  * Method:    close_spi_device
  * Signature: (I)V
  */
  void  Java_com_szaoto_ak10_commsdk_SpiControler_close_1spi_1device
   (JNIEnv *env, jclass obj, jint fd)
{
	int ret;

	ret = close(fd);
	if(ret == -1){
		perror("close spi device failed");
	}
}

 int spi_transfer(int fd,uint8_t * tx,uint8_t * rx ,int length)
  {
 	 //	struct spi_ioc_transfer tr;
 	 //	tr.tx_buf = (unsigned long)tx;
 	 //	tr.rx_buf = (unsigned long)rx;
 	 //	tr.len = length;
 	 //	tr.delay_usecs = delay;
 	 //	tr.speed_hz = speed;
 	 //	tr.bits_per_word = bits;

 	// 	int ret = ioctl(fd, SPI_IOC_MESSAGE(1), &tr);
 	 //	if (ret < 1){
 	 //			perror("can't send spi message");
 	 //		     return -1;
 	// 	}
//
 	// 	return ret;
 	 return 0;
 }
 /*
  * Class:     com_szaoto_ak10_commsdk_HardwareControler
  * Method:    spi_transfer
  * Signature: (I[BI)[B
  */
 jbyteArray  Java_com_szaoto_ak10_commsdk_SpiControler_spi_1transfer
   (JNIEnv *env, jclass obj, jint fd, jbyteArray data, jint length)
{
//	uint8_t *tx = CTool::as_unsigned_char_array(env,data);

	uint8_t rx[length];
	memset(rx,0,length);

//	 int ret = spi_transfer(fd,tx,rx,length);

//	 if(0 > ret)
	 {
		 g_lasterror =  -10;
		 return NULL;
	 }
//	jbyteArray rxdata = CTool::as_byte_array(env,rx,length);

//	return rxdata;
}

 /*
  * Class:     com_szaoto_ak10_commsdk_HardwareControler
  * Method:    spi_write
  * Signature: (I[BI)I
  */
  jint  Java_com_szaoto_ak10_commsdk_SpiControler_spi_1write
   (JNIEnv *env, jclass obj, jint fd, jbyteArray data, jint length)
 {
//		uint8_t *tx = CTool::as_unsigned_char_array(env,data);
		uint8_t txw[length + 1];
//		memcpy(txw + 1,tx,length);
		txw[0] = CMD_WR_DAT;

		uint8_t rx[length + 1];
		memset(rx,0,length + 1);

		int ret = spi_transfer(fd,txw,rx,length + 1);

		return ret;
 }

 /*
  * Class:     com_szaoto_ak10_commsdk_HardwareControler
  * Method:    spi_read
  * Signature: (I[BI)I
  */
  jbyteArray  Java_com_szaoto_ak10_commsdk_SpiControler_spi_1read
   (JNIEnv *env, jclass obj, jint fd, jint length)
  {
	  	int ret = -1;
		////////////////////////////////////////////////////////////////
		uint8_t tx_state[4] = {CMD_RD_CFG,0x00,0x00,0x00};
		uint8_t rx_state[4];
		memset(rx_state,0,4);

		ret = spi_transfer(fd,tx_state,rx_state,4);
		if(0 > ret)
		{
			g_lasterror =  -10;
			return NULL;
		}
		// FIFO閻樿埖锟介敍鍫ユ毐鎼达讣绱�
		int getlength = (rx_state[2] << 8) + rx_state[3];
		////////////////////////////////////////////////////////////////
		if(0 >= getlength)
		{
			g_lasterror =  -12;
			return NULL;
		}
		/*
		if(length < getlength)
		{
			g_lasterror =  -13;
			return NULL;
		}
		*/
		///////////////////////////////////////////////////////////////
		rx_buf_length = 0;
		memset(rx_buf,0,MAX_RX_BUFF);

		int loop = getlength / ( MAX_DEVICE_BUFF - 1);
		int nlen = 0;
		if(0 == loop)
		{
			//婢х偛濮�娑擃亜鐡ч懞鍌滄畱COMMAND
			nlen = getlength + 1;

			uint8_t txw[nlen];
			memset(txw,0,nlen);
			txw[0] = CMD_RD_DAT;
			uint8_t rx[nlen];
			memset(rx,0,nlen);
			ret = spi_transfer(fd,txw,rx,nlen);
			if(0 > ret)
			{
				g_lasterror =  -11;
				return NULL;
			}
			//if(CMD_RD_DAT != rx[0])
			//{
			//	g_lasterror =  -12;
			//	return NULL;
			//}

			rx_buf_length = nlen - 1;
			memcpy(rx_buf,rx + 1,rx_buf_length);

		}
		else
		{
			for(int i = 0; i < loop ; i ++)
			{
				nlen = 0;
				if( ( MAX_DEVICE_BUFF - 1) <= (getlength - rx_buf_length) )
				{
						nlen = MAX_DEVICE_BUFF - 1;
				}
				else
				{
					if(0 != (getlength - rx_buf_length) % ( MAX_DEVICE_BUFF - 1))
					{
						nlen = getlength - rx_buf_length;
					}
				}
				nlen ++;

				uint8_t txw[nlen];
				memset(txw,0,nlen);
				txw[0] = CMD_RD_DAT;
				uint8_t rx[nlen];
				memset(rx,0,nlen);
				ret = spi_transfer(fd,txw,rx,nlen);
				if(0 > ret)
				{
					g_lasterror =  -11;
					return NULL;
				}
				//if(CMD_RD_DAT != rx[0])
				//{
				//	g_lasterror =  -12;
				//	return NULL;
				//}

				memcpy(rx_buf + rx_buf_length,rx + 1,nlen - 1);

				rx_buf_length += nlen - 1;
			}
		}

		////////////////////////////////////////////////////////////////
		jbyteArray data = NULL;
		if(0 < rx_buf_length)
		{
//			data = CTool::as_byte_array(env,rx_buf,rx_buf_length);
		}

		return data;
  }
  /*
   * Class:     com_szaoto_ak10_commsdk_SpiControler
   * Method:    spi_getlasterror
   * Signature: ()I
   */
  jint  Java_com_szaoto_ak10_commsdk_SpiControler_spi_1getlasterror
    (JNIEnv *, jclass)
  {
	  return g_lasterror;
  }
