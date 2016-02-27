/*
 * util.h
 *
 *  Created on: 2013-12-31
 *      Author: liangdb
 */

#include <jni.h>
#include <stdio.h>
#include <stdlib.h>

#ifndef UTIL_H_
#define UTIL_H_

class CTool
{
public:
	CTool(void);
	~CTool(void);

	static void ExchangeInteger(unsigned int nData, unsigned char *cBuf, unsigned int nLen);
	static void ExchangeChar(unsigned int & nData, unsigned char *ucBuf, unsigned int nLen);
	//閿熸枻鎷蜂綅閿熸枻鎷峰墠閿熸枻鎷烽敓鏂ゆ嫹浣嶉敓鑺傜尨鎷烽敓鏂ゆ嫹鍧�敓闃额亷鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿燂拷
	//static void ExchangeChar(unsigned int & nData, unsigned char *ucBuf, unsigned int nLen);

	static int doGetIntegerData(unsigned char *cBuf, unsigned int nLen);

	//char杞敓鏂ゆ嫹涓篵ool閿熼叺锝忔嫹1涓簍rue閿熸枻鎷�涓篺alse閿熸枻鎷種ULL涓篺alse

	static bool Char2Bool(const char* chIn);
	static char* jstringTostring(JNIEnv* env, jstring jstr);
	static jstring stoJstring(JNIEnv* env, const char* pat);
	static jbyteArray as_byte_array(JNIEnv* env, unsigned char* buf, int len) ;
	static unsigned char* as_unsigned_char_array(JNIEnv* env, jbyteArray array) ;
	static void sleep( long wait );
};

/*
//jstring to char*
char* jstringTostring(JNIEnv* env, jstring jstr)
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
jstring stoJstring(JNIEnv* env, const char* pat)
{
       jclass strClass = env->FindClass("Ljava/lang/String;");
       jmethodID ctorID = env->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
       jbyteArray bytes = env->NewByteArray(strlen(pat));
       env->SetByteArrayRegion(bytes, 0, strlen(pat), (jbyte*)pat);
       jstring encoding = env->NewStringUTF("utf-8");
       return (jstring)env->NewObject(strClass, ctorID, bytes, encoding);
}

jbyteArray as_byte_array(JNIEnv* env, unsigned char* buf, int len) {
    jbyteArray array = env->NewByteArray (len);
    env->SetByteArrayRegion (array, 0, len, reinterpret_cast<jbyte*>(buf));
    return array;
}

unsigned char* as_unsigned_char_array(JNIEnv* env, jbyteArray array) {
    int len = env->GetArrayLength (array);
    unsigned char* buf = new unsigned char[len];
    env->GetByteArrayRegion (array, 0, len, reinterpret_cast<jbyte*>(buf));
    return buf;
}
*/
#endif /* UTIL_H_ */
