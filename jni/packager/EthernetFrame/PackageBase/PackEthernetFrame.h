#pragma once

#include "../../Datastruct/DataStructDef.h"
#include "../../Datastruct/DataStructDef_E.h"

class CPackEthernetFrame
{
public:
	CPackEthernetFrame(void);
	~CPackEthernetFrame(void);

public:

	static int PackDataBase(unsigned char * ucDestAddress,
							FrameDataField * sFrameDataField,
							unsigned char * ucSendData);

	static int CreateChecksum(unsigned char* ucSenddata,int nLen);
	int UnPackData(unsigned char *pRcvData, int nLen, FrameDataField &m_sFrameDataField);
	bool UnPackErrorType();

public:
	unsigned char m_ucDestAddress[6] ;
	unsigned char m_ucSourceAddress[6] ;
	FrameDataField m_sFrameDataField;
	unsigned char m_ucErrCode;
};
