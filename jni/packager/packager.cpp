/*
 * packager.cpp
 *
 *  Created on: 2013-12-31
 *      Author: liangdb
 */

#include "com_szaoto_ak10_commsdk_Packager.h"
#include "EthernetFrame/PackageBase/PackEthernetFrame.h"
#include "28ByteFrame/PackageBase/CLPackCommunicationData.h"
#include "28ByteFrame/CLScanCardPackData.h"
#include "Datastruct/DataStructDef.h"
//#include "Datastruct/DataStructDef_E.h"
#include "../util/util.h"
#include "28ByteFrame/BLLMonitorProc.h"
#include <stdio.h>
//#include <string.h>
#include <android/log.h>


CPackEthernetFrame packethernet;
CCLPackCommunicationData pack28byte;
CCLScanCardPackData packscandcard;
CBLLMonitorProc packmonitor;
static int g_lasterror = -1;

GLOBALVARIABLE	  g_GlobalVariable;				//工程的全局变量
int globe_CardVesion;

/*
* Set some test stuff up.
*
* Returns the JNI version on success, -1 on failure.
*/
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
{
	globe_CardVesion = 0;
	return g_GlobalVariable.nVersionType = _CustomVersion;
}


int PackEthernetDataWriteOrRead(uint8_t *ucDestAddress,uint8_t * ucAddress,int  nSequenceNumber, int nLength, uint8_t *ucData, uint8_t *ucSendData,bool bRead)
{
		FrameDataField framedata;
		framedata.reset();
		framedata.bMulticast =  false;
		framedata.nMulticastNum = 0;
		framedata.bNoMulticast = false;
		framedata.bAnswer = false;
		framedata.bRead = bRead;
		memcpy(framedata.ucAddress,ucAddress,2);
		//char * message = new char[2] ;
		if(0x10 <= framedata.ucAddress[0])
		{
			framedata.bFIFO = true;
		}
		else
		{
			framedata.bFIFO = false;
		}
		framedata.nSerialNumber = nSequenceNumber;
		framedata.nLength = nLength;
		if(!bRead)
		{
			memcpy(framedata.ucData,ucData,nLength);
		}
		return packethernet.PackDataBase(ucDestAddress,&framedata,ucSendData);
}
jint  Java_com_szaoto_ak10_commsdk_Packager_TestClass1
 (JNIEnv * env, jclass obj, jobject TestClass)
{
	jclass objectClass111 = env->GetObjectClass(TestClass) ;
	jclass objectClass = (env)->FindClass("com/szaoto/ak10/common/TestClass");
	jclass objectClass1 = (env)->FindClass("com/szaoto/ak10/common/RECT");
//	jobject MonitorData = env->AllocObject(objectClass);

	jfieldID jfield1ID = env->GetFieldID(objectClass, "boolean1", "Z" );
	jfieldID jfield11ID = env->GetFieldID(objectClass,"boolean2", "Z" );
	jfieldID jfield2ID = env->GetFieldID(objectClass, "int1", "I" );
	jfieldID jfield3ID = env->GetFieldID(objectClass, "int2", "I" );
	jfieldID jfield4ID = env->GetFieldID(objectClass, "long1", "J");
	jfieldID jfield5ID = env->GetFieldID(objectClass, "long2", "J");
	jfieldID jfield6ID = env->GetFieldID(objectClass, "string1", "Ljava/lang/String;" );
	jfieldID jfield7ID = env->GetFieldID(objectClass, "string2", "Ljava/lang/String;" );
	jfieldID jfield8ID = env->GetFieldID(objectClass, "short1", "S" );
	jfieldID jfield9ID = env->GetFieldID(objectClass, "short2", "S" );
	jfieldID jfield10ID = env->GetFieldID(objectClass,"rect", "Lcom/szaoto/ak10/common/RECT;" );
	jshort st =  env->GetShortField(TestClass,jfield8ID );

	jobject objss = (env)->AllocObject(objectClass1);
	objss = env->GetObjectField(TestClass,jfield10ID);

//	jobject objq = env->GetObjectField(TestClass,jfield10ID);

	__android_log_print(ANDROID_LOG_ERROR,"18ge","5");
	return 1;
}
 /* Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    EthernetPackDataBase
 * Signature: ([BLcom/szaoto/ak10/commsdk/FrameDataField;)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_EthernetPackDataBase
  (JNIEnv * env, jclass obj, jbyteArray ucDestAddress, jobject objs)
{
	jclass objectClass = (env)->FindClass("com/.../.../.../FrameDataField");
	jfieldID jfield1ID = env->GetFieldID(objectClass, "bMulticast", "Z" );
	jfieldID jfield2ID= env->GetFieldID(objectClass, "nMulticastNum", "I" );
	jfieldID jfield3ID= env->GetFieldID(objectClass, "bNoMulticast", "Z" );
	jfieldID jfield4ID= env->GetFieldID(objectClass, "bAnswer", "Z" );
	jfieldID jfield5ID= env->GetFieldID(objectClass, "bRead", "Z" );
	jfieldID jfield6ID= env->GetFieldID(objectClass, "bFIFO", "Z" );
	jfieldID jfield7ID= env->GetFieldID(objectClass, "ucAddress", "[B" );
	jfieldID jfield8ID= env->GetFieldID(objectClass, "nSerialNumber", "I" );
	jfieldID jfield9ID= env->GetFieldID(objectClass, "nLength", "I" );
	jfieldID jfield10ID= env->GetFieldID(objectClass, "ucData", "[B" );

	FrameDataField framedata;
	framedata.reset();
	framedata.bMulticast = env->GetBooleanField(obj, jfield1ID);
	framedata.nMulticastNum = env->GetIntField(obj,jfield2ID);
	framedata.bNoMulticast = env->GetBooleanField(obj,jfield3ID);
	framedata.bAnswer = env->GetBooleanField(obj,jfield4ID);
	framedata.bRead = env->GetBooleanField(obj,jfield5ID);
	//framedata.bFIFO = env->GetBooleanField(obj,jfield6ID);
	if(0x10 <= framedata.ucAddress[0])
	{
		framedata.bFIFO = true;
	}
	else
	{
		framedata.bFIFO = false;
	}
	framedata.nSerialNumber = env->GetIntField(obj,jfield8ID);
	framedata.nLength = env->GetIntField(obj,jfield9ID);
	//
	jbyteArray baucAddress = (jbyteArray) env->GetObjectField(obj, jfield7ID);
	jbyte *uAddress = env->GetByteArrayElements(baucAddress, JNI_FALSE);
	memcpy(framedata.ucAddress,uAddress,2);
	env->ReleaseByteArrayElements(baucAddress, uAddress, JNI_FALSE);
	//
	jbyteArray baucData = (jbyteArray) env->GetObjectField(obj, jfield10ID);
	jbyte *uData = env->GetByteArrayElements(baucData, JNI_FALSE);
	memcpy(framedata.ucData,uData,framedata.nLength);
	env->ReleaseByteArrayElements(baucData, uData, JNI_FALSE);

	//

	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t uSendData[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData,0,ETH_DATA_MAX_SIZE + 18);
	int nLengthResult = packethernet.PackDataBase(uDestAddress,&framedata,uSendData);
	jbyteArray ucSendData = NULL;
	if(0 < nLengthResult)
	{
		ucSendData = CTool::as_byte_array(env,uSendData,nLengthResult);
	}
	else
	{
		g_lasterror = nLengthResult;
		return NULL;
	}
	return ucSendData;
}

jobject Java_com_ldq_list_WifiManager_getScanResults (JNIEnv *env, jclass cls)
{
    jclass m_cls_list    = env->FindClass("java/util/ArrayList");
    jmethodID m_mid_list = env->GetMethodID(m_cls_list,"<init>","()V");
    jobject m_obj_list   = env->NewObject(m_cls_list,m_mid_list);
    jmethodID m_mid_add  = env->GetMethodID(m_cls_list,"add","(Ljava/lang/Object;)Z");
    jclass m_cls_result    = env->FindClass("com/ldq/list/ScanResult");
    jmethodID m_mid_result = env->GetMethodID(m_cls_result,"<init>","()V");
    jobject m_obj_result   = env->NewObject(m_cls_result,m_mid_result);
    jfieldID m_fid_1 = env->GetFieldID(m_cls_result,"ssid","Ljava/lang/String;");
    jfieldID m_fid_2 = env->GetFieldID(m_cls_result,"mac","Ljava/lang/String;");
    jfieldID m_fid_3 = env->GetFieldID(m_cls_result,"level","I");
    env->SetObjectField(m_obj_result,m_fid_1,env->NewStringUTF("AP6"));
    env->SetObjectField(m_obj_result,m_fid_2,env->NewStringUTF("66-66-66-66-66-66"));
    env->SetIntField(m_obj_result,m_fid_3,-66);
    env->CallBooleanMethod(m_obj_list,m_mid_add,m_obj_result);
    return m_obj_list;
}
jint JNICALL Java_com_szaoto_ak10_commsdk_Packager_UnPack28Data(JNIEnv * env , jclass obj, jbyteArray ucRecieve,jshort commond )
{
	uint8_t *uRecieve = CTool::as_unsigned_char_array(env,ucRecieve);

	pack28byte.UnPackData(uRecieve , CL_SEND_PACK_SIZE);
	if (pack28byte.m_ucPackType != commond)
		return -1;

	int nAddress = pack28byte.m_ucSourAddress;

	return nAddress;
}
jobject Java_com_szaoto_ak10_commsdk_Packager_UnPackCustomMonitorData//zhangjjnow
  (JNIEnv *env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucRecieve, jobject sMonitorData)
{//zhangjj1
//	uint8_t *uDestAddSress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uRecieve = CTool::as_unsigned_char_array(env,ucRecieve);
	FrameDataField sFrameDataField;
	Monitordata monitorData;
	packethernet.UnPackData(uRecieve , 64, sFrameDataField);
	pack28byte.UnPackData(sFrameDataField.ucData , CL_SEND_PACK_SIZE);
	pack28byte.GetCustomMonitorData(&monitorData);

	jclass objectClass = (env)->FindClass("com/szaoto/ak10/common/MonitorData");
	jobject MonitorData = env->AllocObject(objectClass);
	jfieldID jfield1ID= env->GetFieldID(objectClass, "nBrightness", "S" );///////////
	env->SetShortField(MonitorData , jfield1ID , monitorData.nBrightness);
	jfieldID jfield2ID= env->GetFieldID(objectClass, "nBrightness2", "S" );////////////
	env->SetShortField(MonitorData , jfield2ID , monitorData.nBrightness2);
	jfieldID jfield3ID= env->GetFieldID(objectClass, "fTemperature", "F" );///////////
	env->SetFloatField(MonitorData ,jfield3ID , monitorData.fTemperature) ;
	jfieldID jfield4ID= env->GetFieldID(objectClass, "fHumidity", "F" );/////////
	env->SetFloatField(MonitorData,jfield4ID , monitorData.fHumidity );
	jfieldID jfield5ID= env->GetFieldID(objectClass, "bSmog", "Z" );/////////
	env->SetBooleanField(MonitorData,jfield5ID , monitorData.bSmog );
	jfieldID jfield11ID= env->GetFieldID(objectClass, "nErrorPointNum", "S" );/////////////
	env->SetShortField(MonitorData,jfield11ID ,monitorData.nErrorPointNum ) ;
	jfieldID jfield18ID= env->GetFieldID(objectClass, "nNetWebErrorPackageFlag", "I" );//////////
	env->SetIntField(MonitorData,jfield18ID ,monitorData.nNetWebErrorPackageFlag) ;
	jfieldID jfield19ID= env->GetFieldID(objectClass, "tttttttt", "J" );
	//env->SetLongField(MonitorData,jfield19ID ,monitorData.tttttttt ) ;

	jfieldID jfieldversion = env->GetFieldID(objectClass , "version" ,"Lcom/szaoto/ak10/common/Version;");
	jclass verClass = (env)->FindClass("com/szaoto/ak10/common/Version");
	jobject verobj = env->AllocObject(verClass);
	jfieldID JV1 = env->GetFieldID(verClass,"nYear","I");
	env->SetIntField(verobj , JV1 , monitorData.version.nYear);
	env->SetObjectField(MonitorData ,jfieldversion ,verobj);
	return MonitorData;
}
///////////////////
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackAcquiAcquisitionCardEdidInfor
 * Signature: ([B[B)[B
 */

jbyteArray Java_com_szaoto_ak10_commsdk_Packager_UnPackPowerMonitorData
  (JNIEnv * env, jclass obj, jbyteArray ucDestAddress , jbyteArray ucRecieve, jobject MonitorData)
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uRecieve = CTool::as_unsigned_char_array(env,ucRecieve);
	FrameDataField sFrameDataField;
	Monitordata monitorData;
	packethernet.UnPackData(uRecieve , 0, sFrameDataField);
	pack28byte.UnPackData(sFrameDataField.ucData , CL_SEND_PACK_SIZE);
	pack28byte.GetPowerMonitorData(&monitorData);

	jclass objectClass = (env)->FindClass("com/szaoto/ak10/common/MonitorData");
	jfieldID jfield10ID= env->GetFieldID(objectClass, "nCapacityFactor", "I" );////////////
	env->SetFloatField(MonitorData, jfield10ID, monitorData.nCapacityFactor );
	return NULL;
	//double dCapacity =(double) CTool::doGetIntegerData(m_ucRcvData + 1, 2);
	//	pMonitorData->nCapacityFactor = (int) ((dCapacity / 10000.0) * 1250);
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    UnPackPointDetectMonitorData
 * Signature: ([B[BLcom/szaoto/ak10/common/MonitorData;Lcom/szaoto/ak10/common/ScanCardAttachment;)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_UnPackPointDetectMonitorData
  (JNIEnv * env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucRecieve, jobject MonitorData, jobject sScanCardAtt)
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uRecieve = CTool::as_unsigned_char_array(env,ucRecieve);
	FrameDataField sFrameDataField;
	Monitordata monitorData;
	packethernet.UnPackData(uRecieve , 0, sFrameDataField);
	pack28byte.UnPackData(sFrameDataField.ucData , CL_SEND_PACK_SIZE);
	pack28byte.GetPowerMonitorData(&monitorData);

	jclass objectClass = (env)->FindClass("com/szaoto/ak10/common/MonitorData");
	jfieldID jfield10ID= env->GetFieldID(objectClass, "nCapacityFactor", "I" );////////////
	env->SetFloatField(MonitorData, jfield10ID, monitorData.nCapacityFactor );

	return NULL;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    UnPackNetWebErrorPackage
 * Signature: ([B[BLcom/szaoto/ak10/common/MonitorData;)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_UnPackNetWebErrorPackage
  (JNIEnv * env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucRecieve, jobject MonitorData)
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uRecieve = CTool::as_unsigned_char_array(env,ucRecieve);
	FrameDataField sFrameDataField;
	Monitordata monitorData;
	packethernet.UnPackData(uRecieve , 0, sFrameDataField);
	pack28byte.UnPackData(sFrameDataField.ucData , CL_SEND_PACK_SIZE);
	pack28byte.GetNetWebErrorPackage(&monitorData);

	jclass objectClass = (env)->FindClass("com/szaoto/ak10/common/MonitorData");
	jfieldID jfield19ID= env->GetFieldID(objectClass, "nNetWebErrorNum", "S" );
	monitorData.nNetWebErrorNum = env->GetFloatField(objectClass,jfield19ID);
	return NULL;
}

/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    EthernetPackDataWrite
 * Signature: ([B[BII[B[B)I*/

jbyteArray Java_com_szaoto_ak10_commsdk_Packager_EthernetPackDataWrite
  (JNIEnv * env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucAddress, jint nSequenceNumber, jint nLength, jbyteArray ucData)
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);
	uint8_t *uData = CTool::as_unsigned_char_array(env,ucData);

	uint8_t uSendData[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData,0,ETH_DATA_MAX_SIZE + 18);

	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,nSequenceNumber,nLength,uData,uSendData,false);

	jbyteArray ucSendData = NULL;
	if(0 < nLengthResult)
	{
		ucSendData = CTool::as_byte_array(env,uSendData,nLengthResult);
	}
	else
	{
		g_lasterror = nLengthResult;
		return NULL;
	}

	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    EthernetPackDataRead
 * Signature: ([B[BI[B)I
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_EthernetPackDataRead
  (JNIEnv *env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucAddress, jint nLength)
{

	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);
	uint8_t uData[1] = { 0 };

	uint8_t uSendData[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData,0,ETH_DATA_MAX_SIZE + 18);

	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,nLength,uData,uSendData,true);

	jbyteArray ucSendData = NULL;
	if(0 < nLengthResult)
	{
		ucSendData = CTool::as_byte_array(env,uSendData,nLengthResult);
	}
	else
	{
		g_lasterror = nLengthResult;
		return NULL;
	}

	return ucSendData;
}

/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    EthernetUnPackDataRead
 * Signature: ([BI)Lcom/szaoto/ak10/commsdk/FrameDataField;
 */
jobject  Java_com_szaoto_ak10_commsdk_Packager_EthernetUnPackDataRead
  (JNIEnv * env, jclass obj, jbyteArray revData, jint length)
{
//	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
//			uint8_t *uRecieve = CTool::as_unsigned_char_array(env,ucRecieve);
			uint8_t *uRecieve = CTool::as_unsigned_char_array(env,revData);
			FrameDataField framedata;
			memset(framedata.ucData , 0,1495);
//			Monitordata monitorData;
			packethernet.UnPackData(uRecieve , length, framedata);
			LPCOLOURRGB pColorRGB = new COLOURRGB;
				jclass objectClass = (env)->FindClass("com/szaoto/ak10/commsdk/FrameDataField");
				jmethodID initfuncion = env->GetMethodID(objectClass,"<init>","()V");
				jobject sFrameDataField   = env->NewObject(objectClass,initfuncion);
					jfieldID jfield1ID = env->GetFieldID(objectClass, "bMulticast", "Z" );
					jfieldID jfield2ID= env->GetFieldID(objectClass, "nMulticastNum", "I" );
					jfieldID jfield3ID= env->GetFieldID(objectClass, "bNoMulticast", "Z" );
					jfieldID jfield4ID= env->GetFieldID(objectClass, "bAnswer", "Z" );
					jfieldID jfield5ID= env->GetFieldID(objectClass, "bRead", "Z" );
					jfieldID jfield6ID= env->GetFieldID(objectClass, "bFIFO", "Z" );
					jfieldID jfield7ID= env->GetFieldID(objectClass, "ucAddress", "[B" );
					jfieldID jfield8ID= env->GetFieldID(objectClass, "nSerialNumber", "I" );
					jfieldID jfield9ID= env->GetFieldID(objectClass, "nLength", "I" );
					jfieldID jfield10ID= env->GetFieldID(objectClass, "ucData", "[B" );
					//	FrameDataField framedata;
					//	framedata.reset();
					env->SetBooleanField(sFrameDataField, jfield1ID,framedata.bMulticast);
					env->SetIntField(sFrameDataField, jfield2ID,framedata.nMulticastNum);
					env->SetBooleanField(sFrameDataField, jfield3ID,framedata.bNoMulticast);
					env->SetBooleanField(sFrameDataField, jfield4ID,framedata.bAnswer);
					env->SetBooleanField(sFrameDataField, jfield5ID,framedata.bRead);
					if(0x10 <= framedata.ucAddress[0])
						{
						env->SetBooleanField(sFrameDataField, jfield6ID,true);
						}
						else
						{
							env->SetBooleanField(sFrameDataField, jfield6ID,false);
						}
						env->SetIntField(sFrameDataField, jfield8ID,framedata.nSerialNumber);
						env->SetIntField(sFrameDataField, jfield9ID,framedata.nLength);

						jbyteArray temp =	CTool::as_byte_array(env,framedata.ucAddress,2);
						jbyte   *   arr   =   env-> GetByteArrayElements(temp,   0);
						jbyteArray baucAddress = (jbyteArray) env->GetObjectField(sFrameDataField, jfield7ID);
						env->SetByteArrayRegion(baucAddress,0,2,arr);//baucAddress
						env->SetObjectField(sFrameDataField, jfield7ID,baucAddress);
						jbyte *uAddress = env->GetByteArrayElements(baucAddress, JNI_FALSE);
						env->ReleaseByteArrayElements(baucAddress, uAddress, JNI_FALSE);

						jbyteArray temp1 =	CTool::as_byte_array(env,framedata.ucData,1495);
						jbyte   *   arr1   =   env-> GetByteArrayElements(temp1,   0);
						jbyteArray baucData = (jbyteArray) env->GetObjectField(sFrameDataField, jfield10ID);
						env->SetByteArrayRegion(baucData,0,1495,arr1);//baucAddress
						env->SetObjectField(sFrameDataField, jfield7ID,baucAddress);
						jbyte *uData = env->GetByteArrayElements(baucData, JNI_FALSE);
						env->ReleaseByteArrayElements(baucData, uData, JNI_FALSE);
						return sFrameDataField;
}


/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    NativePackDataBase
 * Signature: (IBII[B)[B
 */
jbyteArray  Java_com_szaoto_ak10_commsdk_Packager_NativePackDataBase
  (JNIEnv * env, jclass obj, jint ucDestAddress, jbyte ucPackType, jint unSequenceNumber, jint nAnsweredFlag, jbyteArray ucData)
{
	uint8_t *uData = CTool::as_unsigned_char_array(env,ucData);

	uint8_t uSendData[CL_SEND_PACK_SIZE];
	memset(uSendData,0,CL_SEND_PACK_SIZE);
	int length = pack28byte.PackDataBase(0,ucDestAddress,ucPackType,unSequenceNumber,nAnsweredFlag,uData,uSendData);
	jbyteArray ucSendData = CTool::as_byte_array(env,uSendData,length);

	return ucSendData;
}

jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackMonitorData
  (JNIEnv *env, jclass obj, jint monitortype, jint monitoractiontype, jbyteArray ucDestAddress, jbyteArray ucAddress, jint SCAddress)
 {
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);
	uint8_t uSendData[CL_SEND_PACK_SIZE];
	memset(uSendData,0,CL_SEND_PACK_SIZE);
	int length = packmonitor.PackMonitorCommond(monitortype ,monitoractiontype,SCAddress,uSendData);
	uint8_t uSendData1[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData1,0,ETH_DATA_MAX_SIZE + 18);
	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,length,uSendData,uSendData1,false);
	jbyteArray ucSendData = NULL;
	if(0 < nLengthResult)
	{
		ucSendData = CTool::as_byte_array(env,uSendData1,nLengthResult);
	}
	else
	{
		g_lasterror = nLengthResult;
		return NULL;
	}
	return ucSendData;
}

jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackMonitorConnectstate
(JNIEnv *env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucAddress, jint nBrightness)
{
		uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
		uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

		uint8_t uSendData1[CL_SEND_PACK_SIZE];
		memset(uSendData1,0,CL_SEND_PACK_SIZE);
		int length = pack28byte.PackDataBase(0,0xFF,0x42,0 ,true,0,uSendData1);
		uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
		memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);
		int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,length,uSendData1,uSendData2,false);

		jbyteArray ucSendData = NULL;
		if(0 < nLengthResult)
		{
			ucSendData = CTool::as_byte_array(env,uSendData2,nLengthResult);
		}
		else
		{
			g_lasterror = nLengthResult;
			return NULL;
		}

		return ucSendData;

}

jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackAcquiAcquisitionCardEdidInfor
  (JNIEnv * env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucAddress,jbyteArray edidbuf )
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);
	uint8_t *edidbuffer = CTool::as_unsigned_char_array(env,edidbuf);
	uint8_t uSendData[256];
//	uint8_t * kk[10] ;
//	memset(kk,uDestAddress,sizeof(kk));
//	int s = sizeof(edidbuffer);
	memset(uSendData,0,256);
	for(int i = 0;i<256;i++)
	{
		uSendData[i] = edidbuffer[i];
	}

	//uSendData = edidbuffer;
//	int length = pack28byte.PackDataBase(0,0xFF,0x42,0 ,true,0,uSendData);
	uint8_t uSendData1[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData1,0,ETH_DATA_MAX_SIZE + 18);
	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,256,uSendData,uSendData1,false);
		jbyteArray ucSendData = NULL;
			if(0 < nLengthResult)
			{
				ucSendData = CTool::as_byte_array(env,uSendData1,nLengthResult);
			}
			else
			{
				g_lasterror = nLengthResult;
				return NULL;
			}
			return ucSendData;
}
///////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackSetBright
 * Signature: ([B[BI)[B
 */
jbyteArray  Java_com_szaoto_ak10_commsdk_Packager_PackSetBright
  (JNIEnv *env, jclass obj,  jbyteArray ucDestAddress, jbyteArray ucAddress, jint nBrightness)
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	uint8_t uData[CL_SEND_EFFECT_DATA_SIZE];

	memset(uData, 0, CL_SEND_EFFECT_DATA_SIZE);

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);

	int length = packscandcard.PackBrightness(0xFF,nBrightness,uSendData1);

	uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);

	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,length,uSendData1,uSendData2,false);

	jbyteArray ucSendData = NULL;
	if(0 < nLengthResult)
	{
		ucSendData = CTool::as_byte_array(env,uSendData2,nLengthResult);
	}
	else
	{
		g_lasterror = nLengthResult;
		return NULL;
	}

	return ucSendData;

}

/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackSet3DPara
 * Signature: ([B[BI)[B
 */
jbyteArray  Java_com_szaoto_ak10_commsdk_Packager_PackSet3DPara
  (JNIEnv *env, jclass obj,  jbyteArray ucDestAddress, jbyteArray ucAddress, jint nSynchronDelay, jint nDisableScanCycle)
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	uint8_t uData[CL_SEND_EFFECT_DATA_SIZE];

	memset(uData, 0, CL_SEND_EFFECT_DATA_SIZE);

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);

	int length = packscandcard.Pack3DPara(0xFF,nSynchronDelay, nDisableScanCycle,uSendData1);

	uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);

	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,length,uSendData1,uSendData2,false);

	jbyteArray ucSendData = NULL;
	if(0 < nLengthResult)
	{
		ucSendData = CTool::as_byte_array(env,uSendData2,nLengthResult);
	}
	else
	{
		g_lasterror = nLengthResult;
		return NULL;
	}

	return ucSendData;

}


/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackSetColorTemperature
 * Signature: ([B[BLcom/szaoto/ak10/common/ColourRGB;BB)[B
 */
jbyteArray  Java_com_szaoto_ak10_commsdk_Packager_PackSetColorTemperature
  (JNIEnv *env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucAddress, jobject sColorRGB, jshort nHighLowGap, jshort nGrayEnhanceMode)
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	LPCOLOURRGB pColorRGB = new COLOURRGB;

	jclass objectClass = (env)->FindClass("com/szaoto/ak10/common/Display/ColourRGB");

	jfieldID jfield1ID= env->GetFieldID(objectClass, "nRed", "I" );
	jfieldID jfield2ID= env->GetFieldID(objectClass, "nGreen", "I" );
	jfieldID jfield3ID= env->GetFieldID(objectClass, "nBlue", "I" );
	jfieldID jfield4ID= env->GetFieldID(objectClass, "nICRed", "I" );
	jfieldID jfield5ID= env->GetFieldID(objectClass, "nICGreen", "I" );
	jfieldID jfield6ID= env->GetFieldID(objectClass, "nICBlue", "I" );
	jfieldID jfield7ID= env->GetFieldID(objectClass, "nRedLow", "I" );
	jfieldID jfield8ID= env->GetFieldID(objectClass, "nGreenLow", "I" );
	jfieldID jfield9ID= env->GetFieldID(objectClass, "nBlueLow", "I" );
	jfieldID jfield10ID= env->GetFieldID(objectClass, "nICRedLow", "I" );
	jfieldID jfield11ID= env->GetFieldID(objectClass, "nICGreenLow", "I" );
	jfieldID jfield12ID= env->GetFieldID(objectClass, "nICBlueLow", "I" );

	pColorRGB->nRed = env->GetIntField(sColorRGB,jfield1ID);
	pColorRGB->nGreen = env->GetIntField(sColorRGB,jfield2ID);
	pColorRGB->nBlue = env->GetIntField(sColorRGB,jfield3ID);
	pColorRGB->nICRed = env->GetIntField(sColorRGB,jfield4ID);
	pColorRGB->nICGreen = env->GetIntField(sColorRGB,jfield5ID);
	pColorRGB->nICBlue = env->GetIntField(sColorRGB,jfield6ID);
	pColorRGB->nRedLow = env->GetIntField(sColorRGB,jfield7ID);
	pColorRGB->nGreenLow = env->GetIntField(sColorRGB,jfield8ID);
	pColorRGB->nBlueLow = env->GetIntField(sColorRGB,jfield9ID);
	pColorRGB->nICRedLow = env->GetIntField(sColorRGB,jfield10ID);
	pColorRGB->nICGreenLow = env->GetIntField(sColorRGB,jfield11ID);
	pColorRGB->nICBlueLow = env->GetIntField(sColorRGB,jfield12ID);

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);
	int length = packscandcard.PackColorTemperatureData(0xFF,0,pColorRGB,1,nHighLowGap,nGrayEnhanceMode,0,uSendData1);

	delete pColorRGB;
	pColorRGB = NULL;

	uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);

	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,length,uSendData1,uSendData2,false);

	if(0 < nLengthResult)
	{
		ucSendData = CTool::as_byte_array(env,uSendData2,nLengthResult);
	}
	else
	{
		g_lasterror = nLengthResult;
		return NULL;
	}

	return ucSendData;
}

/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackColorTempCurrentData
 * Signature: (ILcom/szaoto/ak10/common/ColourRGB;)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackColorTempCurrentData
  (JNIEnv *env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucAddress, jint nAddress, jobject sColorRGB)
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	LPCOLOURRGB pColorRGB = new COLOURRGB;

	jclass objectClass = (env)->FindClass("com/szaoto/ak10/common/ColourRGB");

	jfieldID jfield1ID= env->GetFieldID(objectClass, "nRed", "I" );
	jfieldID jfield2ID= env->GetFieldID(objectClass, "nGreen", "I" );
	jfieldID jfield3ID= env->GetFieldID(objectClass, "nBlue", "I" );
	jfieldID jfield4ID= env->GetFieldID(objectClass, "nICRed", "I" );
	jfieldID jfield5ID= env->GetFieldID(objectClass, "nICGreen", "I" );
	jfieldID jfield6ID= env->GetFieldID(objectClass, "nICBlue", "I" );
	jfieldID jfield7ID= env->GetFieldID(objectClass, "nRedLow", "I" );
	jfieldID jfield8ID= env->GetFieldID(objectClass, "nGreenLow", "I" );
	jfieldID jfield9ID= env->GetFieldID(objectClass, "nBlueLow", "I" );
	jfieldID jfield10ID= env->GetFieldID(objectClass, "nICRedLow", "I" );
	jfieldID jfield11ID= env->GetFieldID(objectClass, "nICGreenLow", "I" );
	jfieldID jfield12ID= env->GetFieldID(objectClass, "nICBlueLow", "I" );

	pColorRGB->nRed = env->GetIntField(sColorRGB,jfield1ID);
	pColorRGB->nGreen = env->GetIntField(sColorRGB,jfield2ID);
	pColorRGB->nBlue = env->GetIntField(sColorRGB,jfield3ID);
	pColorRGB->nICRed = env->GetIntField(sColorRGB,jfield4ID);
	pColorRGB->nICGreen = env->GetIntField(sColorRGB,jfield5ID);
	pColorRGB->nICBlue = env->GetIntField(sColorRGB,jfield6ID);
	pColorRGB->nRedLow = env->GetIntField(sColorRGB,jfield7ID);
	pColorRGB->nGreenLow = env->GetIntField(sColorRGB,jfield8ID);
	pColorRGB->nBlueLow = env->GetIntField(sColorRGB,jfield9ID);
	pColorRGB->nICRedLow = env->GetIntField(sColorRGB,jfield10ID);
	pColorRGB->nICGreenLow = env->GetIntField(sColorRGB,jfield11ID);
	pColorRGB->nICBlueLow = env->GetIntField(sColorRGB,jfield12ID);

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);
	int length = packscandcard.PackColorTempCurrentData(nAddress,0,pColorRGB,uSendData1);

	delete pColorRGB;
	pColorRGB = NULL;

	uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);

	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,length,uSendData1,uSendData2,false);

	if(0 < nLengthResult)
	{
		ucSendData = CTool::as_byte_array(env,uSendData2,nLengthResult);
	}
	else
	{
		g_lasterror = nLengthResult;
		return NULL;
	}

	return ucSendData;
}


/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackSetGamma
 * Signature: ([B[BLcom/szaoto/ak10/common/GammaData;I)[B
 */
jbyteArray  Java_com_szaoto_ak10_commsdk_Packager_PackSetGamma
  (JNIEnv *env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucAddress, jobject sGammaData, jint nColorIndex)
{
	return Java_com_szaoto_ak10_commsdk_Packager_PackGammaTable(env,obj,
			//ucDestAddress,ucAddress,
			0xFF,sGammaData,nColorIndex);
}

/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackGammaTable
 * Signature: ([B[BSLcom/szaoto/ak10/common/GammaData;I)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackGammaTable
  (JNIEnv *env, jclass obj,  jshort nAddress, jobject sGammaData, jint nColorIndex)
{

	//uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	//uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	GammaData gammadata;

	jclass objectClass = (env)->FindClass("com/szaoto/ak10/common/GammaData");

	jfieldID jfield1ID= env->GetFieldID(objectClass, "nVideowid", "S" );
	jfieldID jfield2ID= env->GetFieldID(objectClass, "bSendThreeColor", "Z" );
	jfieldID jfield3ID= env->GetFieldID(objectClass, "nGrayLevel", "S" );
	jfieldID jfield4ID= env->GetFieldID(objectClass, "fGamma", "[F" );
	jfieldID jfield5ID= env->GetFieldID(objectClass, "sGammaTable", "[[S" );
	jfieldID jfield6ID= env->GetFieldID(objectClass, "fGammaRGB", "F" );
	jfieldID jfield7ID= env->GetFieldID(objectClass, "sGammaTableRGB", "[S" );

	gammadata.nVideowid = env->GetShortField(sGammaData,jfield1ID);
	gammadata.bSendThreeColor = env->GetBooleanField(sGammaData,jfield2ID);
	gammadata.nGrayLevel = env->GetShortField(sGammaData,jfield3ID);

	jfloatArray jarr1 = env->NewFloatArray(3);
	jarr1 = (jfloatArray)env->GetObjectField(sGammaData, jfield4ID);
	env->GetFloatArrayRegion(jarr1, 0, 3, gammadata.fGamma);
	//
	jclass fltarrCls = env->FindClass("[S");
	jobjectArray jarr2 = env->NewObjectArray(3, fltarrCls ,NULL);
	jarr2 = (jobjectArray)env->GetObjectField(sGammaData,jfield5ID);
	for(int i = 0; i < 3; i++)
	{
		jshort tmp[256];
		jshortArray tmparr = env->NewShortArray(256);
		tmparr =(jshortArray)env->GetObjectArrayElement(jarr2, i);
		env->GetShortArrayRegion(tmparr, 0, 256, tmp);
		for(int j = 0; j < 256; j ++)
		{
			gammadata.sGammaTable[i][j] = tmp[j];
		}
	}
	//
	gammadata.fGammaRGB = env->GetFloatField(sGammaData,jfield6ID);

	jshortArray jarr3 = env->NewShortArray(4096);
	jarr3 = (jshortArray)env->GetObjectField(sGammaData, jfield7ID);
	env->GetShortArrayRegion(jarr3, 0, 4096, gammadata.sGammaTableRGB);
	//

	uint8_t uSendData1[CL_MAX_BUF_NUMBER];
	memset(uSendData1,0,CL_MAX_BUF_NUMBER);
	int length = packscandcard.PackGammaTable(nAddress,gammadata,nColorIndex,uSendData1);

	if(0 < length)
	{
		ucSendData = CTool::as_byte_array(env,uSendData1,length);
	}
	else
	{
		g_lasterror = length;
		return NULL;
	}

	return ucSendData;
}

void GetScanCard(JNIEnv *env,jobject obj,CStructSingleScanCard & scancard)
{
	///jclass objectClass = env->FindClass("com/szaoto/ak10/common/CabinetData/CStructSingleScanCard");
	jclass objectClass = env->GetObjectClass(obj);
	//env->FindClass("test/Test$TestInner")InnerClass

	jfieldID jfield1ID= env->GetFieldID(objectClass, "nScanCardWidth", "S");
	__android_log_print(ANDROID_LOG_ERROR, "18",  "00");
	jfieldID jfield2ID= env->GetFieldID(objectClass, "nScanCardHeight", "S");
	jfieldID jfield3ID= env->GetFieldID(objectClass, "nScanCardWidthReal", "S");
	jfieldID jfield4ID= env->GetFieldID(objectClass, "nScanCardHeightReal", "S");
	jfieldID jfield5ID= env->GetFieldID(objectClass, "nModuleWidth", "S");
	jfieldID jfield6ID= env->GetFieldID(objectClass, "nModuleHeight", "S");
	jfieldID jfield7ID= env->GetFieldID(objectClass, "nModuleSectionNumber", "S");
	jfieldID jfield8ID= env->GetFieldID(objectClass, "nModuleHorNum", "S");
	jfieldID jfield9ID= env->GetFieldID(objectClass, "nModuleVerNum", "S");
	jfieldID jfield10ID= env->GetFieldID(objectClass, "nScanCardSectionNumber", "S");
	jfieldID jfield11ID= env->GetFieldID(objectClass, "nScanCardSectionRowNumber", "S");
	jfieldID jfield12ID= env->GetFieldID(objectClass, "nScanColorDepth", "S");
	jfieldID jfield13ID= env->GetFieldID(objectClass, "nGrayLedvel", "S");
	jfieldID jfield14ID= env->GetFieldID(objectClass, "nOrginColorBit", "S");
	jfieldID jfield15ID= env->GetFieldID(objectClass, "nScanMode", "S");
	jfieldID jfield16ID= env->GetFieldID(objectClass, "bEmendBrightness", "Z");
	jfieldID jfield17ID= env->GetFieldID(objectClass, "fScanClkFrequency", "F");
	jfieldID jfield18ID= env->GetFieldID(objectClass, "nZoneDClkNum", "S");
	jfieldID jfield19ID= env->GetFieldID(objectClass, "nDutyCycle", "S");
	jfieldID jfield20ID= env->GetFieldID(objectClass, "fPWMScanClkFrequency", "F");
	jfieldID jfield21ID= env->GetFieldID(objectClass, "nPWMDutyCycle", "S");
	jfieldID jfield22ID= env->GetFieldID(objectClass, "nOeClkNumber", "J");
	__android_log_print(ANDROID_LOG_ERROR, "18",  "1");
	jfieldID jfield23ID= env->GetFieldID(objectClass, "nRefreshRate", "I");
	jfieldID jfield24ID= env->GetFieldID(objectClass, "nRefreshRateMin", "I");
	jfieldID jfield25ID= env->GetFieldID(objectClass, "nRefreshRateMax", "I");
	jfieldID jfield26ID= env->GetFieldID(objectClass, "nConfigICTime", "S");
	jfieldID jfield27ID= env->GetFieldID(objectClass, "nDatFreqNum", "I");
	jfieldID jfield28ID= env->GetFieldID(objectClass, "nOeDelayValue", "S");
	jfieldID jfield29ID= env->GetFieldID(objectClass, "bSyncRefresh", "Z");
	jfieldID jfield30ID= env->GetFieldID(objectClass, "bVirtvalDisp", "Z");
	jfieldID jfield31ID= env->GetFieldID(objectClass, "nFreqDivisionCoeff", "S");
	jfieldID jfield32ID= env->GetFieldID(objectClass, "nPWMFreqDivisionCoeff", "S");
	jfieldID jfield33ID= env->GetFieldID(objectClass, "bDataOutUpReverse", "Z");
	jfieldID jfield34ID= env->GetFieldID(objectClass, "bScanOutUpReverse", "Z");
	jfieldID jfield35ID= env->GetFieldID(objectClass, "nDCBlineClkEn", "S");
	jfieldID jfield36ID= env->GetFieldID(objectClass, "nNoSingleDisp", "S");
	jfieldID jfield37ID= env->GetFieldID(objectClass, "nDataInputDir", "S");
	jfieldID jfield38ID= env->GetFieldID(objectClass, "nRowDecodeMOde", "S");
	jfieldID jfield39ID= env->GetFieldID(objectClass, "nDataLineTypeRange", "S");
	jfieldID jfield40ID= env->GetFieldID(objectClass, "nDataLineType", "S");
	jfieldID jfield41ID= env->GetFieldID(objectClass, "nDataLineCtrl", "S");
	jfieldID jfield42ID= env->GetFieldID(objectClass, "nFieldNum", "S");
	jfieldID jfield43ID= env->GetFieldID(objectClass, "nHalfFieldNumber", "S");
	jfieldID jfield44ID= env->GetFieldID(objectClass, "nFullFieldNumber", "S");
	jfieldID jfield45ID= env->GetFieldID(objectClass, "nStartField", "S");
	jfieldID jfield46ID= env->GetFieldID(objectClass, "nEndField", "S");
	jfieldID jfield47ID= env->GetFieldID(objectClass, "nDataPolarity", "S");
	jfieldID jfield48ID= env->GetFieldID(objectClass, "nOePolarity", "S");
	jfieldID jfield49ID= env->GetFieldID(objectClass, "bEmptyInsertEnable", "Z");
	jfieldID jfield50ID= env->GetFieldID(objectClass, "nInsertMode", "S");
	jfieldID jfield51ID= env->GetFieldID(objectClass, "nEmptyDotNum", "S");
	jfieldID jfield52ID= env->GetFieldID(objectClass, "nRealDotNum", "S");
	jfieldID jfield53ID= env->GetFieldID(objectClass, "bDualOutput", "Z");
	jfieldID jfield54ID= env->GetFieldID(objectClass, "nVirTualArray", "S");
	jfieldID jfield55ID= env->GetFieldID(objectClass, "nChipType", "I");
//	__android_log_print(ANDROID_LOG_ERROR, "18",  "4");
	jfieldID jfield56ID= env->GetFieldID(objectClass, "nRefreshDoubleValue", "S");
	jfieldID jfield57ID= env->GetFieldID(objectClass, "nZheRdwrMode", "S");
	jfieldID jfield58ID= env->GetFieldID(objectClass, "nScreenType", "S");
	jfieldID jfield59ID= env->GetFieldID(objectClass, "nDotCorrectTye", "S");
	jfieldID jfield60ID= env->GetFieldID(objectClass, "bVirtualChangeFlag", "Z");
	jfieldID jfield61ID= env->GetFieldID(objectClass, "bVirtualPrime", "Z");
	jfieldID jfield62ID= env->GetFieldID(objectClass, "bTest", "Z");
	jfieldID jfield63ID= env->GetFieldID(objectClass, "fBrightnessEfficent", "F");
	jfieldID jfield64ID= env->GetFieldID(objectClass, "nMinOEWidth", "S");
	jfieldID jfield65ID= env->GetFieldID(objectClass, "bScanColorDepthChangeFlag", "Z");
	jfieldID jfield66ID= env->GetFieldID(objectClass, "nScanColorDepthPrime", "S");
	jfieldID jfield67ID= env->GetFieldID(objectClass, "bDotOpenDetection", "Z");
	jfieldID jfield68ID= env->GetFieldID(objectClass, "nPWMOutputMode", "S");
	jfieldID jfield69ID= env->GetFieldID(objectClass, "bMultiRefreshUnderStaticScan", "Z");
	jfieldID jfield70ID= env->GetFieldID(objectClass, "nPixsPerSection", "I");
	jfieldID jfield71ID= env->GetFieldID(objectClass, "nZoneClkNum", "I");
	jfieldID jfield72ID= env->GetFieldID(objectClass, "bExtendedEnable", "Z");
	jfieldID jfield73ID= env->GetFieldID(objectClass, "bExtendedEnableEx", "Z");
	jfieldID jfield74ID= env->GetFieldID(objectClass, "nSectionWidth", "S");
	jfieldID jfield75ID= env->GetFieldID(objectClass, "nSectionHorNum", "S");
	jfieldID jfield76ID= env->GetFieldID(objectClass, "nCard_zone_width", "S");
	jfieldID jfield77ID= env->GetFieldID(objectClass, "nCard_zone_Num", "S");
	jfieldID jfield78ID= env->GetFieldID(objectClass, "nGrayEnhance", "S");
	jfieldID jfield79ID= env->GetFieldID(objectClass, "nGrayEnhanceMode", "S");
	jfieldID jfield80ID= env->GetFieldID(objectClass, "bOpenCabinetLamp", "Z");
	jfieldID jfield81ID= env->GetFieldID(objectClass, "nSecondHighLevel", "S");
	jfieldID jfield82ID= env->GetFieldID(objectClass, "fLightRatio", "F");
	jfieldID jfield83ID= env->GetFieldID(objectClass, "nCustomGamam", "I");
	jfieldID jfield84ID= env->GetFieldID(objectClass, "bChipPrecharge", "Z");
	jfieldID jfield85ID= env->GetFieldID(objectClass, "bGClkCtrlByRGBEnable", "Z");
	jfieldID jfield86ID= env->GetFieldID(objectClass, "bGClkCtrlByREnable", "Z");
	jfieldID jfield87ID= env->GetFieldID(objectClass, "bGClkCtrlByGEnable", "Z");
	jfieldID jfield88ID= env->GetFieldID(objectClass, "bGClkCtrlByBEnable", "Z");
	jfieldID jfield89ID= env->GetFieldID(objectClass, "nGClkDelay", "S");
	jfieldID jfield90ID= env->GetFieldID(objectClass, "nGClkDelay_G", "S");
	jfieldID jfield91ID= env->GetFieldID(objectClass, "nGClkDelay_B", "S");
	////////////////////////////////////////////////////////////////////////////////
	jfieldID jfield96ID= env->GetFieldID(objectClass, "nNetPortPriority", "S");
	jfieldID jfield97ID= env->GetFieldID(objectClass, "bLockNetPort", "Z");
	jfieldID jfield98ID= env->GetFieldID(objectClass, "nDeductBit", "I");
	////////////////////////////////////////////////////////////////////////////////
	jfieldID jfield92ID = env->GetFieldID(objectClass , "nDrive_ic_reg" ,"Lcom/szaoto/ak10/common/CabinetData/Drive_ic_reg;");

	scancard.nScanCardWidth = env->GetShortField(obj,jfield1ID);
	__android_log_print(ANDROID_LOG_ERROR, "18",  "111");
	scancard.nScanCardHeight = env->GetShortField(obj,jfield2ID);
	scancard.nScanCardWidthReal = env->GetShortField(obj,jfield3ID);
	scancard.nScanCardHeightReal = env->GetShortField(obj,jfield4ID);
	scancard.nModuleWidth = env->GetShortField(obj,jfield5ID);
	scancard.nModuleHeight = env->GetShortField(obj,jfield6ID);
	scancard.nModuleSectionNumber = env->GetShortField(obj,jfield7ID);

	scancard.nModuleHorNum = env->GetShortField(obj,jfield8ID);
	scancard.nModuleVerNum = env->GetShortField(obj,jfield9ID);
	scancard.nScanCardSectionNumber = env->GetShortField(obj,jfield10ID);
	scancard.nScanCardSectionRowNumber = env->GetShortField(obj,jfield11ID);
	scancard.nScanColorDepth = env->GetShortField(obj,jfield12ID);
	scancard.nGrayLedvel = env->GetShortField(obj,jfield13ID);
	scancard.nOrginColorBit = env->GetShortField(obj,jfield14ID);
	scancard.nScanMode = env->GetShortField(obj,jfield15ID);
	scancard.bEmendBrightness = env->GetBooleanField(obj,jfield16ID);
	scancard.fScanClkFrequency = env->GetFloatField(obj,jfield17ID);
	scancard.nZoneDClkNum = env->GetShortField(obj,jfield18ID);
	scancard.nDutyCycle = env->GetShortField(obj,jfield19ID);
	scancard.fPWMScanClkFrequency = env->GetFloatField(obj,jfield20ID);
	scancard.nPWMDutyCycle = env->GetShortField(obj,jfield21ID);
	scancard.nOeClkNumber = env->GetLongField(obj,jfield22ID);
	scancard.nRefreshRate = env->GetIntField(obj,jfield23ID);
	scancard.nRefreshRateMin = env->GetIntField(obj,jfield24ID);
	scancard.nRefreshRateMax = env->GetIntField(obj,jfield25ID);
	scancard.nConfigICTime = env->GetShortField(obj,jfield26ID);
	scancard.nDatFreqNum = env->GetIntField(obj,jfield27ID);
	scancard.nOeDelayValue = env->GetShortField(obj,jfield28ID);
	scancard.bSyncRefresh = env->GetBooleanField(obj,jfield29ID);
	scancard.bVirtvalDisp = env->GetBooleanField(obj,jfield30ID);
	scancard.nFreqDivisionCoeff = env->GetShortField(obj,jfield31ID);
	scancard.nPWMFreqDivisionCoeff = env->GetShortField(obj,jfield32ID);
	scancard.bDataOutUpReverse = env->GetBooleanField(obj,jfield33ID);
	scancard.bScanOutUpReverse = env->GetBooleanField(obj,jfield34ID);
	scancard.nDCBlineClkEn = env->GetShortField(obj,jfield35ID);
	scancard.nNoSingleDisp = env->GetShortField(obj,jfield36ID);
	scancard.nDataInputDir = env->GetShortField(obj,jfield37ID);
	scancard.nRowDecodeMOde = env->GetShortField(obj,jfield38ID);
	scancard.nDataLineTypeRange = env->GetShortField(obj,jfield39ID);
	scancard.nDataLineType = env->GetShortField(obj,jfield40ID);
	scancard.nDataLineCtrl = env->GetShortField(obj,jfield41ID);
	scancard.nFieldNum = env->GetShortField(obj,jfield42ID);
	scancard.nHalfFieldNumber = env->GetShortField(obj,jfield43ID);
	scancard.nFullFieldNumber = env->GetShortField(obj,jfield44ID);
	scancard.nStartField = env->GetShortField(obj,jfield45ID);
	scancard.nEndField = env->GetShortField(obj,jfield46ID);
	scancard.nDataPolarity = env->GetShortField(obj,jfield47ID);
	scancard.nOePolarity = env->GetShortField(obj,jfield48ID);
	scancard.bEmptyInsertEnable = env->GetBooleanField(obj,jfield49ID);
	scancard.nInsertMode = env->GetShortField(obj,jfield50ID);
	scancard.nEmptyDotNum = env->GetShortField(obj,jfield51ID);
	scancard.nRealDotNum = env->GetShortField(obj,jfield52ID);
	scancard.bDualOutput = env->GetBooleanField(obj,jfield53ID);
	scancard.nVirTualArray = env->GetShortField(obj,jfield54ID);

	scancard.nChipType = env->GetIntField(obj,jfield55ID);

	scancard.nRefreshDoubleValue = env->GetShortField(obj,jfield56ID);
	scancard.nZheRdwrMode = env->GetShortField(obj,jfield57ID);
	scancard.nScreenType = env->GetShortField(obj,jfield58ID);
	scancard.nDotCorrectTye = env->GetShortField(obj,jfield59ID);
	scancard.bVirtualChangeFlag = env->GetBooleanField(obj,jfield60ID);
	scancard.bVirtualPrime = env->GetBooleanField(obj,jfield61ID);
	scancard.bTest = env->GetBooleanField(obj,jfield62ID);
	scancard.fBrightnessEfficent = env->GetFloatField(obj,jfield63ID);
	scancard.nMinOEWidth = env->GetShortField(obj,jfield64ID);
	scancard.bScanColorDepthChangeFlag = env->GetBooleanField(obj,jfield65ID);
	scancard.nScanColorDepthPrime = env->GetShortField(obj,jfield66ID);
	scancard.bDotOpenDetection = env->GetBooleanField(obj,jfield67ID);
	scancard.nPWMOutputMode = env->GetShortField(obj,jfield68ID);
	scancard.bMultiRefreshUnderStaticScan = env->GetBooleanField(obj,jfield69ID);
	scancard.nPixsPerSection = env->GetIntField(obj,jfield70ID);
	scancard.nZoneClkNum = env->GetIntField(obj,jfield71ID);
	scancard.bExtendedEnable = env->GetBooleanField(obj,jfield72ID);
	scancard.bExtendedEnableEx = env->GetBooleanField(obj,jfield73ID);
	scancard.nSectionWidth = env->GetShortField(obj,jfield74ID);
	scancard.nSectionHorNum = env->GetShortField(obj,jfield75ID);
	scancard.nCard_zone_width = env->GetShortField(obj,jfield76ID);
	scancard.nCard_zone_Num = env->GetShortField(obj,jfield77ID);
	scancard.nGrayEnhance = env->GetShortField(obj,jfield78ID);
	scancard.nGrayEnhanceMode = env->GetShortField(obj,jfield79ID);
	scancard.bOpenCabinetLamp = env->GetBooleanField(obj,jfield80ID);
	scancard.nSecondHighLevel = env->GetShortField(obj,jfield81ID);
	scancard.fLightRatio = env->GetFloatField(obj,jfield82ID);
	scancard.nCustomGamam = env->GetIntField(obj,jfield83ID);
	scancard.bChipPrecharge = env->GetBooleanField(obj,jfield84ID);
	scancard.bGClkCtrlByRGBEnable = env->GetBooleanField(obj,jfield85ID);
	scancard.bGClkCtrlByREnable = env->GetBooleanField(obj,jfield86ID);
	scancard.bGClkCtrlByGEnable = env->GetBooleanField(obj,jfield87ID);
	scancard.bGClkCtrlByBEnable = env->GetBooleanField(obj,jfield88ID);
	scancard.nGClkDelay = env->GetShortField(obj,jfield89ID);
	scancard.nGClkDelay_G = env->GetShortField(obj,jfield90ID);
	scancard.nGClkDelay_B = env->GetShortField(obj,jfield91ID);

	///////////////////////////////////////////////////////////////////////
	scancard.nNetPortPriority = env->GetShortField(obj,jfield96ID);
	scancard.bLockNetPort = env->GetBooleanField(obj,jfield97ID);
	scancard.nDeductBit = env->GetIntField(obj,jfield98ID);

	__android_log_print(ANDROID_LOG_ERROR, "18",  "zhangjj");
	//jclass Drive_ic_regClass = (env)->FindClass("com/szaoto/ak10/common/CabinetData/Drive_ic_reg");
//	jclass Drive_ic_regClass = (env)->GetObjectClass(subobj);

	//jobject mDrive_ic_regobj = env->AllocObject(Drive_ic_regClass);
	jobject mDrive_ic_regobj = env->GetObjectField(obj,jfield92ID);
	jclass Drive_ic_regClass = (env)->GetObjectClass(mDrive_ic_regobj);
	jfieldID jfield92ID_1 = env->GetFieldID(Drive_ic_regClass,"nBright", "I");
	jfieldID jfield92ID_2 = env->GetFieldID(Drive_ic_regClass,"nLgse_R", "I");
	jfieldID jfield92ID_3 = env->GetFieldID(Drive_ic_regClass,"nLgse_G", "I");
	jfieldID jfield92ID_4 = env->GetFieldID(Drive_ic_regClass,"nLgse_B", "I");
	jfieldID jfield92ID_5 = env->GetFieldID(Drive_ic_regClass,"nGdly_Enable", "I");
	jfieldID jfield92ID_6 = env->GetFieldID(Drive_ic_regClass,"nTD_Delay", "I");
	jfieldID jfield92ID_7 = env->GetFieldID(Drive_ic_regClass,"nLodvth", "I");

	jfieldID jfield92ID_8 = env->GetFieldID(Drive_ic_regClass,"nGlobal_Lgse", "I");
	jfieldID jfield92ID_9 = env->GetFieldID(Drive_ic_regClass,"nPVM_Mode", "I");
	jfieldID jfield92ID_10 = env->GetFieldID(Drive_ic_regClass,"nEMI_R", "I");
	jfieldID jfield92ID_11 = env->GetFieldID(Drive_ic_regClass,"nEMI_G", "I");
	jfieldID jfield92ID_12 = env->GetFieldID(Drive_ic_regClass,"nEMI_B", "I");
	jfieldID jfield92ID_13 = env->GetFieldID(Drive_ic_regClass,"nPre_Charge", "I");

	///////////////////////////////////////////////////////////////////////////////////
	jfieldID jfield92ID_14 = env->GetFieldID(Drive_ic_regClass,"nPre_Charge1", "I");
	jfieldID jfield92ID_15 = env->GetFieldID(Drive_ic_regClass,"nPwm_Count_Mode", "I");
	jfieldID jfield92ID_16 = env->GetFieldID(Drive_ic_regClass,"nGray_Mode", "I");
	jfieldID jfield92ID_17 = env->GetFieldID(Drive_ic_regClass,"nEnable_GCLK", "I");

	jfieldID jfield92ID_18 = env->GetFieldID(Drive_ic_regClass,"nDouble_RefreseRate", "I");
	jfieldID jfield92ID_19 = env->GetFieldID(Drive_ic_regClass,"nVoltage", "I");
	jfieldID jfield92ID_20 = env->GetFieldID(Drive_ic_regClass,"nIC_Recognition", "I");
	jfieldID jfield92ID_21 = env->GetFieldID(Drive_ic_regClass,"nAdjust_Red", "I");
	jfieldID jfield92ID_22 = env->GetFieldID(Drive_ic_regClass,"nAdjust_Green", "I");
	jfieldID jfield92ID_23 = env->GetFieldID(Drive_ic_regClass,"nAdjust_Blue", "I");
	jfieldID jfield92ID_24 = env->GetFieldID(Drive_ic_regClass,"nImhl_DoNotStretch", "I");


			scancard.nDrive_ic_reg.nBright = env->GetIntField(mDrive_ic_regobj,jfield92ID_1);

			scancard.nDrive_ic_reg.nLgse_R = env->GetIntField(mDrive_ic_regobj,jfield92ID_2);

			scancard.nDrive_ic_reg.nLgse_G = env->GetIntField(mDrive_ic_regobj,jfield92ID_3);
			scancard.nDrive_ic_reg.nLgse_B = env->GetIntField(mDrive_ic_regobj,jfield92ID_4);
			scancard.nDrive_ic_reg.nGdly_Enable = env->GetIntField(mDrive_ic_regobj,jfield92ID_5);
			scancard.nDrive_ic_reg.nTD_Delay = env->GetIntField(mDrive_ic_regobj,jfield92ID_6);
			scancard.nDrive_ic_reg.nLodvth = env->GetIntField(mDrive_ic_regobj,jfield92ID_7);
			scancard.nDrive_ic_reg.nGlobal_Lgse = env->GetIntField(mDrive_ic_regobj,jfield92ID_8);
			scancard.nDrive_ic_reg.nPVM_Mode = env->GetIntField(mDrive_ic_regobj,jfield92ID_9);
			scancard.nDrive_ic_reg.nEMI_R = env->GetIntField(mDrive_ic_regobj,jfield92ID_10);
			scancard.nDrive_ic_reg.nEMI_G = env->GetIntField(mDrive_ic_regobj,jfield92ID_11);
			scancard.nDrive_ic_reg.nEMI_B = env->GetIntField(mDrive_ic_regobj,jfield92ID_12);
			scancard.nDrive_ic_reg.nPre_Charge = env->GetIntField(mDrive_ic_regobj,jfield92ID_13);

			///////////////////////////////////////////////////////////////////////////////////
			scancard.nDrive_ic_reg.nPre_Charge1 = env->GetIntField(mDrive_ic_regobj,jfield92ID_14);
			scancard.nDrive_ic_reg.nPwm_Count_Mode = env->GetIntField(mDrive_ic_regobj,jfield92ID_15);
			scancard.nDrive_ic_reg.nGray_Mode = env->GetIntField(mDrive_ic_regobj,jfield92ID_16);
			scancard.nDrive_ic_reg.nEnable_GCLK = env->GetIntField(mDrive_ic_regobj,jfield92ID_17);

			scancard.nDrive_ic_reg.nDouble_RefreseRate = env->GetIntField(mDrive_ic_regobj,jfield92ID_18);
			scancard.nDrive_ic_reg.nVoltage = env->GetIntField(mDrive_ic_regobj,jfield92ID_19);
			scancard.nDrive_ic_reg.nIC_Recognition = env->GetIntField(mDrive_ic_regobj,jfield92ID_20);
			scancard.nDrive_ic_reg.nAdjust_Red = env->GetIntField(mDrive_ic_regobj,jfield92ID_21);
			scancard.nDrive_ic_reg.nAdjust_Green = env->GetIntField(mDrive_ic_regobj,jfield92ID_22);
			scancard.nDrive_ic_reg.nAdjust_Blue = env->GetIntField(mDrive_ic_regobj,jfield92ID_23);
			scancard.nDrive_ic_reg.nImhl_DoNotStretch = env->GetIntField(mDrive_ic_regobj,jfield92ID_24);

			__android_log_print(ANDROID_LOG_ERROR, "18",  "zhangjjnow");

			jfieldID jfield93ID = env->GetFieldID(Drive_ic_regClass , "sDrive_ic_reg_MBI5153_E" ,"Lcom/szaoto/ak10/common/CabinetData/Drive_ic_reg_MBI5153_E;");
			jfieldID jfield94ID = env->GetFieldID(Drive_ic_regClass , "sDrive_ic_reg_MBI5043" ,"Lcom/szaoto/ak10/common/CabinetData/Drive_ic_reg_MBI5043;");
			jfieldID jfield95ID = env->GetFieldID(Drive_ic_regClass , "sDrive_ic_reg_MBI5155" ,"Lcom/szaoto/ak10/common/CabinetData/Drive_ic_reg_MBI5155;");

			///////////////////////////////////////////////////////////////////////////////////
			jobject Drive_ic_reg_MBI5153_Eobj = env->GetObjectField(mDrive_ic_regobj,jfield93ID);
			jclass Drive_ic_reg_MBI5153_EClass = (env)->GetObjectClass(Drive_ic_reg_MBI5153_Eobj);

			jfieldID jfield93ID_1 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg1RHigh", "I");
			jfieldID jfield93ID_2 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg1RLow", "I");
			jfieldID jfield93ID_3 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg1GHigh", "I");
			jfieldID jfield93ID_4 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg1GLow", "I");
			jfieldID jfield93ID_5 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg1BHigh", "I");
			jfieldID jfield93ID_6 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg1BLow", "I");

			jfieldID jfield93ID_7 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg2RHigh", "I");
			jfieldID jfield93ID_8 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg2RLow", "I");
			jfieldID jfield93ID_9 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg2GHigh", "I");
			jfieldID jfield93ID_10 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg2GLow", "I");
			jfieldID jfield93ID_11 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg2BHigh", "I");
			jfieldID jfield93ID_12 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg2BLow", "I");

			jfieldID jfield93ID_13 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg3RHigh", "I");
			jfieldID jfield93ID_14 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg3RLow", "I");
			jfieldID jfield93ID_15 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg3GHigh", "I");
			jfieldID jfield93ID_16 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg3GLow", "I");
			jfieldID jfield93ID_17 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg3BHigh", "I");
			jfieldID jfield93ID_18 = env->GetFieldID(Drive_ic_reg_MBI5153_EClass,"nReg3BLow", "I");

			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RHigh = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_1);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RLow = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_2);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1GHigh = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_3);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1GLow = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_4);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1BHigh = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_5);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1BLow = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_6);

			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2RHigh = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_7);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2RLow = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_8);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2GHigh = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_9);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2GLow = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_10);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2BHigh = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_11);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2BLow = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_12);

			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RHigh = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_13);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RLow = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_14);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3GHigh = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_15);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3GLow = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_16);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3BHigh = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_17);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3BLow = env->GetIntField(Drive_ic_reg_MBI5153_Eobj,jfield93ID_18);

			///////////////////////////////////////////////////////////////////////////////////
			jobject Drive_ic_reg_MBI5043obj = env->GetObjectField(mDrive_ic_regobj,jfield94ID);
			jclass Drive_ic_reg_MBI5043Class = (env)->GetObjectClass(Drive_ic_reg_MBI5043obj);

			jfieldID jfield94ID_1 = env->GetFieldID(Drive_ic_reg_MBI5043Class,"bGCLKDoublesampling", "Z");
			jfieldID jfield94ID_2 = env->GetFieldID(Drive_ic_reg_MBI5043Class,"nPWMMode", "S");

			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5043.bGCLKDoublesampling = env->GetBooleanField(Drive_ic_reg_MBI5043obj,jfield94ID_1);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5043.nPWMMode = env->GetShortField(Drive_ic_reg_MBI5043obj,jfield94ID_2);

			///////////////////////////////////////////////////////////////////////////////////
			jobject Drive_ic_reg_MBI5155obj = env->GetObjectField(mDrive_ic_regobj,jfield95ID);
			jclass Drive_ic_reg_MBI5155Class = (env)->GetObjectClass(Drive_ic_reg_MBI5155obj);

			jfieldID jfield95ID_1 = env->GetFieldID(Drive_ic_reg_MBI5155Class,"nDeltaT", "I");
			jfieldID jfield95ID_2 = env->GetFieldID(Drive_ic_reg_MBI5155Class,"nDeltaF", "I");
			jfieldID jfield95ID_3 = env->GetFieldID(Drive_ic_reg_MBI5155Class,"nDHT", "I");
			jfieldID jfield95ID_4 = env->GetFieldID(Drive_ic_reg_MBI5155Class,"nDG_H", "I");
			jfieldID jfield95ID_5 = env->GetFieldID(Drive_ic_reg_MBI5155Class,"nDG_L", "I");

			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT = env->GetIntField(Drive_ic_reg_MBI5155obj,jfield95ID_1);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaF = env->GetIntField(Drive_ic_reg_MBI5155obj,jfield95ID_2);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT = env->GetIntField(Drive_ic_reg_MBI5155obj,jfield95ID_3);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_H = env->GetIntField(Drive_ic_reg_MBI5155obj,jfield95ID_4);
			scancard.nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_L = env->GetIntField(Drive_ic_reg_MBI5155obj,jfield95ID_5);


}


/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackScanCardData
 * Signature: ([B[BIILcom/szaoto/ak10/common/CStructSingleScanCard;I)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackScanCardData
  (JNIEnv *env, jclass obj,// jbyteArray ucDestAddress, jbyteArray ucAddress,
		  jint nScanCardAddress, jint nPackID, jobject sScanCard,jint nEmptyByte)
{
	//uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	//uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	CStructSingleScanCard scancard;

	GetScanCard(env,sScanCard,scancard);

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);
	int length = packscandcard.PackScanCardData(nScanCardAddress,0,nPackID,scancard,uSendData1,nEmptyByte);
	if(0 < length)
	{
		ucSendData = CTool::as_byte_array(env,uSendData1,length);
	}
	else
	{
		g_lasterror = length;
		return NULL;
	}

	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackStartOrEnd
 * Signature: ([B[BIZSSS)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackStartOrEnd
  (JNIEnv *env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucAddress, jint nScanCardAddress, jboolean bStart, jshort nType, jshort nModuleRow, jshort nModulCol)
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);
	int length = packscandcard.PackStartOrEnd(nScanCardAddress,0,bStart,nType,nModuleRow,nModulCol,uSendData1);

	uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);

	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,length,uSendData1,uSendData2,false);

	if(0 < nLengthResult)
	{
		ucSendData = CTool::as_byte_array(env,uSendData2,nLengthResult);
	}
	else
	{
		g_lasterror = nLengthResult;
		return NULL;
	}

	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackHUBPara
 * Signature: ([B[BILcom/szaoto/ak10/common/CStructSingleScanCard;)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackHUBPara
  (JNIEnv *env, jclass obj, //jbyteArray ucDestAddress, jbyteArray ucAddress,
		  jint nAddress, jobject sScancard)
{
//	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
//	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	CStructSingleScanCard scancard;

	GetScanCard(env,sScancard,scancard);

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);
	int length = packscandcard.PackHUBPara(nAddress,0,scancard,uSendData1);



	if(0 < length)
	{
		ucSendData = CTool::as_byte_array(env,uSendData1,length);
	}
	else
	{
		g_lasterror = length;
		return NULL;
	}

	return ucSendData;
}

 jbyteArray JNICALL Java_com_szaoto_ak10_commsdk_Packager_PackHUBLookup
 (JNIEnv *env, jclass obj,jint nAddress, jobject hublinktable)
 {
	// uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	// 	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	 	jbyteArray ucSendData = NULL;
	 	LinkTable linkTable;

	 	jclass objectClass;
	 //	jclass objectClass = (env)->FindClass("com/szaoto/ak10/common/CabinetData/LinkTable");
		objectClass = env->GetObjectClass(hublinktable);

	 	jfieldID jfield1ID= env->GetFieldID(objectClass, "nLen", "J" );
	 	jfieldID jfield2ID= env->GetFieldID(objectClass, "ucLinkTable", "[B" );
	 	const char *str;

	 	linkTable.nLen = env->GetLongField(hublinktable,jfield1ID);

	 	jbyteArray jarr1 = env->NewByteArray(linkTable.nLen);
	 	jarr1 = (jbyteArray)env->GetObjectField(hublinktable, jfield2ID);
	 	//str = env->GetStringUTFChars(jarr1,0);
	 	env->GetByteArrayRegion(jarr1, 0, linkTable.nLen, linkTable.ucLinkTable);
	 	//
	 //	__android_log_print(ANDROID_LOG_ERROR,"18ge","99");
	 	uint8_t uSendData1[CL_MAX_BUF_NUMBER];
	 	memset(uSendData1,0,CL_MAX_BUF_NUMBER);
	 	int length = packscandcard.PackHUBLookup(nAddress,linkTable,uSendData1);

	 //	__android_log_print(ANDROID_LOG_ERROR,"18ge","200");
	 	if(0 < length)
	 	{
	 		ucSendData = CTool::as_byte_array(env,uSendData1,length);
	 	}
	 	else
	 	{
	 		g_lasterror = length;
	 		return NULL;
	 	}
	 return ucSendData;
 }
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackScanCardUpdateStart
 * Signature: ([B[BISZZ)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackScanCardUpdateStart
  (JNIEnv *env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucAddress, jint nAddress, jshort nUpdateType, jboolean bStart, jboolean bUpdateBoot)
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);
	int length = packscandcard.PackScanCardUpdateStart(nAddress,0,nUpdateType,bStart,bUpdateBoot,uSendData1);

	uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);

	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,length,uSendData1,uSendData2,false);

	if(0 < nLengthResult)
	{
		ucSendData = CTool::as_byte_array(env,uSendData2,nLengthResult);
	}
	else
	{
		g_lasterror = nLengthResult;
		return NULL;
	}

	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackSaveScanCardPara
 * Signature: ([B[BSIZ)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackSaveScanCardPara
  (JNIEnv *env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucAddress, jshort nAddress, jint nTpyeID, jboolean bDefaul)
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);
	int length = packscandcard.PackSaveScanCardPara(nAddress,0,nTpyeID,bDefaul,uSendData1);

	/*uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);

	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,length,uSendData1,uSendData2,false);*/

	if(0 < length)
	{
		ucSendData = CTool::as_byte_array(env,uSendData1,length);
	}
	else
	{
		g_lasterror = length;
		return NULL;
	}

	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackScanCardLoadRegionData
 * Signature: ([B[BSLcom/szaoto/ak10/common/RECT;SS)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackScanCardLoadRegionData
  (JNIEnv *env, jclass obj,// jbyteArray ucDestAddress, jbyteArray ucAddress,
		  jshort nAddress, jobject rtLoad, jshort nStartX, jshort nStartY)
{
//	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
//	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);
	__android_log_print(ANDROID_LOG_ERROR,"18ge","0");
	jbyteArray ucSendData = NULL;

	RECT rLoad;

	jclass objectClass = (env)->FindClass("com/szaoto/ak10/common/RECT");
	jobject rect = env->AllocObject(objectClass);
	jfieldID jfield1ID= env->GetFieldID(objectClass, "left", "I" );
	jfieldID jfield2ID= env->GetFieldID(objectClass, "top", "I" );
	jfieldID jfield3ID= env->GetFieldID(objectClass, "right", "I" );
	jfieldID jfield4ID= env->GetFieldID(objectClass, "bottom", "I" );


	rLoad.left = env->GetIntField(rtLoad,jfield1ID);
	rLoad.top = env->GetIntField(rtLoad,jfield2ID);
	rLoad.right = env->GetIntField(rtLoad,jfield3ID);
	rLoad.bottom = env->GetIntField(rtLoad,jfield4ID);

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);
	int length = packscandcard.PackScanCardLoadRegionData(nAddress,0,rLoad,nStartX,nStartY,uSendData1);

	if(0 < length)
	{
		ucSendData = CTool::as_byte_array(env,uSendData1,length);
	}
	else
	{
		g_lasterror = length;
		return NULL;
	}
	__android_log_print(ANDROID_LOG_ERROR,"18ge","3");
	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    Pack6ScanCardData
 * Signature: ([B[BILcom/szaoto/ak10/common/CStructSingleScanCard;I)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_Pack6ScanCardData
  (JNIEnv *env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucAddress, jint nScanCardAddress, jobject sScanCard,jint nEmptyByte)
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	CStructSingleScanCard scancard;

	GetScanCard(env,sScanCard,scancard);

	uint8_t uSendData1[CL_MAX_BUF_NUMBER];
	memset(uSendData1,0,CL_MAX_BUF_NUMBER);
	int length = packscandcard.Pack6ScanCardData(nScanCardAddress,scancard,uSendData1,nEmptyByte);

	uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);

	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,length,uSendData1,uSendData2,false);

	if(0 < nLengthResult)
	{
		ucSendData = CTool::as_byte_array(env,uSendData2,nLengthResult);
	}
	else
	{
		g_lasterror = nLengthResult;
		return NULL;
	}

	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackNoSingleDisp
 * Signature: ([B[BSS)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackNoSingleDisp
  (JNIEnv *env, jclass obj,// jbyteArray ucDestAddress, jbyteArray ucAddress,
		  jshort nAddress, jshort nNoSingleDisp)
{
	//uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	//uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);
	int length = packscandcard.PackNoSingleDisp(nAddress,nNoSingleDisp,uSendData1);

	if(0 < length)
	{
		ucSendData = CTool::as_byte_array(env,uSendData1,length);
	}
	else
	{
		g_lasterror = length;
		return NULL;
	}

	return ucSendData;

}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackOperationProcessing
 * Signature: ([B[BSS)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackOperationProcessing
  (JNIEnv *env, jclass obj, //jbyteArray ucDestAddress, jbyteArray ucAddress,
		  jshort nAddress, jshort nScancardSectionRowNumber)
{
//	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
//	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	uint8_t uSendData1[CL_MAX_BUF_NUMBER];
	memset(uSendData1,0,CL_MAX_BUF_NUMBER);
	int length = packscandcard.PackOperationProcessing(nAddress,nScancardSectionRowNumber,uSendData1);
	if(0 < length)
		{
			ucSendData = CTool::as_byte_array(env,uSendData1,length);
		}
		else
		{
			g_lasterror = length;
			return NULL;
		}
	/*
	 * uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);
	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,length,uSendData1,uSendData2,false);

	if(0 < nLengthResult)
	{
		ucSendData = CTool::as_byte_array(env,uSendData2,nLengthResult);
	}
	else
	{
		g_lasterror = nLengthResult;
		return NULL;
	}*/

	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackVideoProcessing
 * Signature: ([B[BSLcom/szaoto/ak10/common/CStructSingleScanCard;)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackVideoProcessing
  (JNIEnv *env, jclass obj,// jbyteArray ucDestAddress, jbyteArray ucAddress,
		  jshort nAddress, jobject sScanCard)
{
//	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
//	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	CStructSingleScanCard scancard;

	GetScanCard(env,sScanCard,scancard);

	uint8_t uSendData1[CL_MAX_BUF_NUMBER];
	memset(uSendData1,0,CL_MAX_BUF_NUMBER);
	int length = packscandcard.PackVideoProcessing(nAddress,scancard,uSendData1);

	if(0 < length)
	{
		ucSendData = CTool::as_byte_array(env,uSendData1,length);
	}
	else
	{
		g_lasterror = length;
		return NULL;
	}

	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackCorrectProcessing
 * Signature: ([B[BSLcom/szaoto/ak10/common/CStructSingleScanCard;)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackCorrectProcessing
  (JNIEnv *env, jclass obj,// jbyteArray ucDestAddress, jbyteArray ucAddress,
		  jshort nAddress, jobject sScanCard)
{
//	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
//	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	CStructSingleScanCard scancard;

	GetScanCard(env,sScanCard,scancard);

	uint8_t uSendData1[CL_MAX_BUF_NUMBER];
	memset(uSendData1,0,CL_MAX_BUF_NUMBER);
	int length = packscandcard.PackCorrectProcessing(nAddress,scancard,uSendData1);

	if(0 < length)
	{
		ucSendData = CTool::as_byte_array(env,uSendData1,length);
	}
	else
	{
		g_lasterror = length;
		return NULL;
	}

	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackCorrectProcessingLookup
 * Signature: ([B[BSLcom/szaoto/ak10/common/CStructSingleScanCard;)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackCorrectProcessingLookup
  (JNIEnv *env, jclass obj,// jbyteArray ucDestAddress, jbyteArray ucAddress,
		  jshort nAddress, jobject sScanCard)
{
//	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
//	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	CStructSingleScanCard scancard;

	GetScanCard(env,sScanCard,scancard);

	uint8_t uSendData1[CL_MAX_BUF_NUMBER];
	memset(uSendData1,0,CL_MAX_BUF_NUMBER);
	int length = packscandcard.PackCorrectProcessingLookup(nAddress,scancard,uSendData1);

//	int length = packscandcard.PackCorrectProcessing(nAddress,scancard,uSendData1);

	if(0 < length)
	{
		ucSendData = CTool::as_byte_array(env,uSendData1,length);
	}
	else
	{
		g_lasterror = length;
		return NULL;
	}

	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackScanCardLinkTable
 * Signature: ([B[BSSSLcom/szaoto/ak10/common/LinkTable;)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackScanCardLinkTable
  (JNIEnv *env, jclass obj, //jbyteArray ucDestAddress, jbyteArray ucAddress,
		  jshort nAddress, jshort nDataLineRange, jshort nDCBlineClkEn, jobject sLinkTable)
{
//	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
//	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	LinkTable linkTable;
	linkTable.nLen = 0;
	memset(linkTable.ucLinkTable, 0 ,65535);


	jclass objectClass = (env)->FindClass("com/szaoto/ak10/common/CabinetData/LinkTable");


	jfieldID jfield1ID= env->GetFieldID(objectClass, "nLen", "J" );
	jfieldID jfield2ID= env->GetFieldID(objectClass, "ucLinkTable", "[B" );


	linkTable.nLen= env->GetLongField(sLinkTable,jfield1ID);

	jbyteArray jarr1 = env->NewByteArray(linkTable.nLen);
	jarr1 = (jbyteArray)env->GetObjectField(sLinkTable, jfield2ID);
	env->GetByteArrayRegion(jarr1, 0, linkTable.nLen, linkTable.ucLinkTable);
	//

	uint8_t uSendData1[CL_MAX_BUF_NUMBER];
	unsigned char lp[CL_MAX_BUF_NUMBER];

	memset(uSendData1,0,CL_MAX_BUF_NUMBER);
	int length = packscandcard.PackScanCardLinkTable(nAddress,nDataLineRange,nDCBlineClkEn,linkTable,uSendData1);
//	memset(lp,0,CL_MAX_BUF_NUMBER);
//	uSendData1

	if(0 < length)
			{
				ucSendData = CTool::as_byte_array(env,uSendData1,length);
			}
			else
			{
				g_lasterror = length;
				return NULL;
			}

	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackScanCardSectionLinkTable
 * Signature: ([B[BSLcom/szaoto/ak10/common/LinkTable;)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackScanCardSectionLinkTable
  (JNIEnv *env, jclass obj,// jbyteArray ucDestAddress, jbyteArray ucAddress,
		  jshort nAddress, jobject sLinkTable)
{
//	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
//	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;
	LinkTable linkTable;
	//	jclass objectClass = (env)->FindClass("com/szaoto/ak10/common/LinkTable");
	jclass objectClass = (env)->GetObjectClass(sLinkTable);

	jfieldID jfield1ID= env->GetFieldID(objectClass, "nLen", "J" );
	jfieldID jfield2ID= env->GetFieldID(objectClass, "ucLinkTable", "[B" );


	linkTable.nLen= env->GetLongField(sLinkTable,jfield1ID);

	jbyteArray jarr1 = env->NewByteArray(linkTable.nLen);
	jarr1 = (jbyteArray)env->GetObjectField(sLinkTable, jfield2ID);
	env->GetByteArrayRegion(jarr1, 0, linkTable.nLen, linkTable.ucLinkTable);
	//

	uint8_t uSendData1[CL_MAX_BUF_NUMBER];
	memset(uSendData1,0,CL_MAX_BUF_NUMBER);
	int length = packscandcard.PackScanCardSectionLinkTable(nAddress,linkTable,uSendData1);


	if(0 < length)
			{
				ucSendData = CTool::as_byte_array(env,uSendData1,length);
			}
			else
			{
				g_lasterror = length;
				return NULL;
			}
	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackCalibrationEnable
 * Signature: ([B[BSZS)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackCalibrationEnable
  (JNIEnv *env, jclass obj,// jbyteArray ucDestAddress, jbyteArray ucAddress,
		  jshort nAddress, jboolean bEnable, jshort nDotType)
{
//	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
//	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);
	int length = packscandcard.PackCalibrationEnable(nAddress,bEnable,nDotType,uSendData1);

	uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);

	if(0 < length)
	{
		ucSendData = CTool::as_byte_array(env,uSendData1,length);
	}
	else
	{
		g_lasterror = length;
		return NULL;
	}

	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackLockUnlock
 * Signature: ([B[BSZ)[B
 */
 jbyteArray  Java_com_szaoto_ak10_commsdk_Packager_PackMutiple28byteData
  (JNIEnv * env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucAddress, jbyteArray ucsendData,jint length)
 {
	 uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	 uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);
	 uint8_t *ucsenddata = CTool::as_unsigned_char_array(env,ucsendData);
	 jbyteArray ucSendData = NULL;

	uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);

	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,length,ucsenddata,uSendData2,false);

	if(0 < nLengthResult)
	{
		ucSendData = CTool::as_byte_array(env,uSendData2,nLengthResult);
	}
	else
	{
		g_lasterror = nLengthResult;
		return NULL;
	}

	 return ucSendData;
 }
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackLockUnlock
  (JNIEnv *env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucAddress, jshort nAddress, jboolean bLock)
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);
	int length = packscandcard.PackLockUnlock(nAddress,bLock,uSendData1);

	uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);

	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,length,uSendData1,uSendData2,false);

	if(0 < nLengthResult)
	{
		ucSendData = CTool::as_byte_array(env,uSendData2,nLengthResult);
	}
	else
	{
		g_lasterror = nLengthResult;
		return NULL;
	}

	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackScanCardIntelligentPara
 * Signature: ([B[BI)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackScanCardIntelligentPara
  (JNIEnv *env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucAddress, jint nAddressMin)
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);
	int length = packscandcard.PackScanCardIntelligentPara(nAddressMin,uSendData1);

	/*uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);

	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,length,uSendData1,uSendData2,false);*/

	if(0 < length)
	{
		ucSendData = CTool::as_byte_array(env,uSendData1,length);
	}
	else
	{
		g_lasterror = length;
		return NULL;
	}

	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackRelay
 * Signature: ([B[BSSZ)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackRelay
  (JNIEnv *env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucAddress, jshort nAddress, jshort nRelayID, jboolean bPower)
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);
	int length = packscandcard.PackRelay(nAddress,nRelayID,bPower,uSendData1);

	uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);

	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,length,uSendData1,uSendData2,false);

	if(0 < nLengthResult)
	{
		ucSendData = CTool::as_byte_array(env,uSendData2,nLengthResult);
	}
	else
	{
		g_lasterror = nLengthResult;
		return NULL;
	}

	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackRelayAttribute
 * Signature: ([B[BSSZZZ)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackRelayAttribute
  (JNIEnv *env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucAddress, jshort nAddress, jshort nRelayID, jboolean bOverHeatOff, jboolean bOverHumidityOff, jboolean bSmogOff)
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);
	int length = packscandcard.PackRelayAttribute(nAddress,nRelayID,bOverHeatOff,bOverHumidityOff,bSmogOff,uSendData1);

	uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);

	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,length,uSendData1,uSendData2,false);

	if(0 < nLengthResult)
	{
		ucSendData = CTool::as_byte_array(env,uSendData2,nLengthResult);
	}
	else
	{
		g_lasterror = nLengthResult;
		return NULL;
	}

	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackRelayThreshold
 * Signature: ([B[BSSFFFF)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackRelayThreshold
  (JNIEnv *env, jclass obj, jbyteArray ucDestAddress, jbyteArray ucAddress, jshort nAddress, jshort nRelayID, jfloat fTemperatureMin, jfloat fTemperatureMax, jfloat fHumidityMin, jfloat fHumidityMax)
{
	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);
	int length = packscandcard.PackRelayThreshold(nAddress,nRelayID,fTemperatureMin,fTemperatureMax,fHumidityMin,fHumidityMax,uSendData1);

	uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);

	int nLengthResult = PackEthernetDataWriteOrRead(uDestAddress,uAddress,0,length,uSendData1,uSendData2,false);

	if(0 < nLengthResult)
	{
		ucSendData = CTool::as_byte_array(env,uSendData2,nLengthResult);
	}
	else
	{
		g_lasterror = nLengthResult;
		return NULL;
	}

	return ucSendData;
}
/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    PackOpenCabinetLamps
 * Signature: ([B[BSZ)[B
 */
jbyteArray Java_com_szaoto_ak10_commsdk_Packager_PackOpenCabinetLamps
  (JNIEnv *env, jclass obj ,jshort nAddress, jboolean bOpen)
{
//	uint8_t *uDestAddress = CTool::as_unsigned_char_array(env,ucDestAddress);
//	uint8_t *uAddress = CTool::as_unsigned_char_array(env,ucAddress);

	jbyteArray ucSendData = NULL;

	uint8_t uSendData1[CL_SEND_PACK_SIZE];
	memset(uSendData1,0,CL_SEND_PACK_SIZE);
	int length = packscandcard.PackOpenCabinetLamps(nAddress,bOpen,uSendData1);

	uint8_t uSendData2[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData2,0,ETH_DATA_MAX_SIZE + 18);

	if(0 < length)
	{
		ucSendData = CTool::as_byte_array(env,uSendData1,length);
	}
	else
	{
		g_lasterror = length;
		return NULL;
	}

	return ucSendData;
}


////////////////////////////////////////////////////////////////////////////////////


/*
 * Class:     com_szaoto_ak10_commsdk_Packager
 * Method:    getlasterror
 * Signature: ()I
 */
jint  Java_com_szaoto_ak10_commsdk_Packager_getlasterror
  (JNIEnv *, jclass)
{
	return g_lasterror;
}

