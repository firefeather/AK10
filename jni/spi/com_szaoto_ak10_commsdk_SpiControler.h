/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_szaoto_ak10_commsdk_SpiControler */

#ifndef _Included_com_szaoto_ak10_commsdk_SpiControler
#define _Included_com_szaoto_ak10_commsdk_SpiControler
#ifdef __cplusplus
extern "C" {
#endif
#undef com_szaoto_ak10_commsdk_SpiControler_SPI_CPHA
#define com_szaoto_ak10_commsdk_SpiControler_SPI_CPHA 1L
#undef com_szaoto_ak10_commsdk_SpiControler_SPI_CPOL
#define com_szaoto_ak10_commsdk_SpiControler_SPI_CPOL 2L
#undef com_szaoto_ak10_commsdk_SpiControler_SPI_MODE_0
#define com_szaoto_ak10_commsdk_SpiControler_SPI_MODE_0 0L
#undef com_szaoto_ak10_commsdk_SpiControler_SPI_MODE_1
#define com_szaoto_ak10_commsdk_SpiControler_SPI_MODE_1 1L
#undef com_szaoto_ak10_commsdk_SpiControler_SPI_MODE_2
#define com_szaoto_ak10_commsdk_SpiControler_SPI_MODE_2 2L
#undef com_szaoto_ak10_commsdk_SpiControler_SPI_MODE_3
#define com_szaoto_ak10_commsdk_SpiControler_SPI_MODE_3 3L
#undef com_szaoto_ak10_commsdk_SpiControler_SPI_CS_HIGH
#define com_szaoto_ak10_commsdk_SpiControler_SPI_CS_HIGH 4L
#undef com_szaoto_ak10_commsdk_SpiControler_SPI_LSB_FIRST
#define com_szaoto_ak10_commsdk_SpiControler_SPI_LSB_FIRST 8L
#undef com_szaoto_ak10_commsdk_SpiControler_SPI_3WIRE
#define com_szaoto_ak10_commsdk_SpiControler_SPI_3WIRE 16L
#undef com_szaoto_ak10_commsdk_SpiControler_SPI_LOOP
#define com_szaoto_ak10_commsdk_SpiControler_SPI_LOOP 32L
#undef com_szaoto_ak10_commsdk_SpiControler_SPI_NO_CS
#define com_szaoto_ak10_commsdk_SpiControler_SPI_NO_CS 64L
#undef com_szaoto_ak10_commsdk_SpiControler_SPI_READY
#define com_szaoto_ak10_commsdk_SpiControler_SPI_READY 128L
#undef com_szaoto_ak10_commsdk_SpiControler_MAX_SPEED
#define com_szaoto_ak10_commsdk_SpiControler_MAX_SPEED 500000L
#undef com_szaoto_ak10_commsdk_SpiControler_DEFALUT
#define com_szaoto_ak10_commsdk_SpiControler_DEFALUT 500000L
/*
 * Class:     com_szaoto_ak10_commsdk_SpiControler
 * Method:    open_spi_device
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_szaoto_ak10_commsdk_SpiControler_open_1spi_1device
  (JNIEnv *, jclass, jstring);

/*
 * Class:     com_szaoto_ak10_commsdk_SpiControler
 * Method:    spi_cfg
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_com_szaoto_ak10_commsdk_SpiControler_spi_1cfg
  (JNIEnv *, jclass, jint, jint, jint, jint);

/*
 * Class:     com_szaoto_ak10_commsdk_SpiControler
 * Method:    close_spi_device
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_szaoto_ak10_commsdk_SpiControler_close_1spi_1device
  (JNIEnv *, jclass, jint);

/*
 * Class:     com_szaoto_ak10_commsdk_SpiControler
 * Method:    spi_write
 * Signature: (I[BI)I
 */
JNIEXPORT jint JNICALL Java_com_szaoto_ak10_commsdk_SpiControler_spi_1write
  (JNIEnv *, jclass, jint, jbyteArray, jint);

/*
 * Class:     com_szaoto_ak10_commsdk_SpiControler
 * Method:    spi_read
 * Signature: (II)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_szaoto_ak10_commsdk_SpiControler_spi_1read
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     com_szaoto_ak10_commsdk_SpiControler
 * Method:    spi_transfer
 * Signature: (I[BI)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_szaoto_ak10_commsdk_SpiControler_spi_1transfer
  (JNIEnv *, jclass, jint, jbyteArray, jint);

/*
 * Class:     com_szaoto_ak10_commsdk_SpiControler
 * Method:    spi_getlasterror
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_com_szaoto_ak10_commsdk_SpiControler_spi_1getlasterror
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif