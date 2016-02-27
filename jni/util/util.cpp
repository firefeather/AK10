/*
 * util.cpp
 *
 *  Created on: 2013-12-31
 *      Author: liangdb
 */

#include "util.h"


CTool::CTool(void)
{
}

CTool::~CTool(void)
{
}

//閿熸枻鎷蜂綅閿熸枻鎷峰墠閿熸枻鎷烽敓鏂ゆ嫹浣嶉敓鑺傜尨鎷�
void CTool::ExchangeInteger(unsigned int nData, unsigned char *ucBuf, unsigned int nLen)
{
	unsigned char *ucTmp = new unsigned char[nLen];
	memset(ucTmp, 0, nLen);
	memcpy(ucTmp, &nData, nLen);
	int ii = 0;
	for(int n = nLen - 1; n >= 0; n--)
		ucBuf[n] = ucTmp[ii++];
	delete ucTmp;
	ucTmp = NULL;
}

//閿熸枻鎷蜂綅閿熸枻鎷峰墠閿熸枻鎷烽敓鏂ゆ嫹浣嶉敓鑺傜尨鎷烽敓鏂ゆ嫹鍧�敓闃额亷鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿燂�
void CTool::ExchangeChar(unsigned int & nData, unsigned char *ucBuf, unsigned int nLen)
{
	char *ucTmp = new char[nLen];
	memset(ucTmp, 0, nLen);

	int ii = 0;
	for(int n = nLen - 1; n >= 0; n--)
		ucTmp[ii++] = ucBuf[n];

	memcpy(&nData,ucTmp,nLen);

	delete ucTmp;
	ucTmp = NULL;
}

int CTool::doGetIntegerData(unsigned char *cBuf, unsigned int nLen)
{
	//TODO: Add your source code here
	int iDataValue = 0;
	int iTimes = 1;
	for (int i = nLen - 1; i >= 0; i--) {
		iDataValue += (unsigned char)cBuf[i] * iTimes;
		iTimes *= 256;
	}
	return iDataValue;
}
//char杞敓鏂ゆ嫹涓篵ool閿熼叺锝忔嫹1涓簍rue閿熸枻鎷�涓篺alse閿熸枻鎷種ULL涓篺alse
bool CTool::Char2Bool(const char* chIn)
{
	if(chIn != NULL)
	{
		return (strcmp(chIn, "1") == 0);
	}
	return false;
}

//jstring to char*
char* CTool::jstringTostring(JNIEnv* env, jstring jstr)
{
       char* rtn = NULL;
       jclass clsstring = env->FindClass("java/lang/String");
       jstring strencode = env->NewStringUTF("utf-8");
       jmethodID mid = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
       jbyteArray barr= (jbyteArray)env->CallObjectMethod(jstr, mid, strencode);
       jsize alen = env->GetArrayLength(barr);
       jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);
       if (alen > 0)
       {
                 rtn = (char*)malloc(alen + 1);
                 memcpy(rtn, ba, alen);
                 rtn[alen] = 0;
       }
       env->ReleaseByteArrayElements(barr, ba, 0);
       return rtn;
}

//char* to jstring
jstring CTool::stoJstring(JNIEnv* env, const char* pat)
{
       jclass strClass = env->FindClass("Ljava/lang/String;");
       jmethodID ctorID = env->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
       jbyteArray bytes = env->NewByteArray(strlen(pat));
       env->SetByteArrayRegion(bytes, 0, strlen(pat), (jbyte*)pat);
       jstring encoding = env->NewStringUTF("utf-8");
       return (jstring)env->NewObject(strClass, ctorID, bytes, encoding);
}

jbyteArray CTool::as_byte_array(JNIEnv* env, unsigned char* buf, int len) {
    jbyteArray array = env->NewByteArray (len);
    env->SetByteArrayRegion (array, 0, len, reinterpret_cast<jbyte*>(buf));
    return array;
}

unsigned char* CTool::as_unsigned_char_array(JNIEnv* env, jbyteArray array) {
    int len = env->GetArrayLength (array);
    unsigned char* buf = new unsigned char[len];
    env->GetByteArrayRegion (array, 0, len, reinterpret_cast<jbyte*>(buf));
    return buf;
}

