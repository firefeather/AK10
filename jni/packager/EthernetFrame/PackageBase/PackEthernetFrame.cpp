#include "PackEthernetFrame.h"
#include "../../../util/util.h"
#include <android/log.h>
const unsigned char ucSourceAddress[6] = {0x00,0x02,0x04,0x06,0x08,0x0a};
const unsigned char ucFrameInterval[12] = { 0 };

static unsigned int crc_table[256];

void init_crc_table(void)
{
	uint32_t crc_data;
	uint32_t crc_crc;
	int    i, j;

	for( i = 0; i < 256; i++) {
		crc_data = i;
		crc_crc = 0;
		for( j = 0; j < 8; j++) {
			if( (crc_data ^ (crc_crc>>24)) & 0x80)
				crc_crc = (crc_crc << 1) ^ 0x04c11db7U;
			else
				crc_crc <<= 1;
		crc_data <<= 1;
		}
		crc_table[i] = crc_crc;
	}
}

static unsigned int crc32(unsigned char * data, unsigned int length)
{
	uint32_t crc_residue = 0xffffffff;
	int i;
	uint32_t crc_std_residue = 0xc704dd7b;
	for(i=0; i<length; i++)
		crc_residue = (crc_residue << 8) ^ crc_table[data[i] ^ crc_residue>>24];

	return crc_residue ^ 0xffffffff;
}

CPackEthernetFrame::CPackEthernetFrame(void)
{
	init_crc_table();
}

CPackEthernetFrame::~CPackEthernetFrame(void)
{
}
int CPackEthernetFrame::PackDataBase(unsigned char * ucDestAddress,
						FrameDataField * sFrameDataField,
						unsigned char * ucSendData)
{

		memcpy(ucSendData,ucDestAddress,6);
		memcpy(ucSendData + 6,ucSourceAddress,6);
		int nFrameLength = 0;
		if(sFrameDataField->bMulticast)
		{
			if(sFrameDataField->bRead == false)
			{
				nFrameLength = ((46 - 5) < sFrameDataField->nLength) ? (6 + 6 + 2 + (1 + 2 + 2) + sFrameDataField->nLength + 4) : 64;
			}else{
				nFrameLength = 65;
			}

		}
		else
		{
			if(sFrameDataField->bRead == false)
			{
				nFrameLength = ((46 - 4) < sFrameDataField->nLength) ? (6 + 6 + 2 + (2 + 2) + sFrameDataField->nLength + 4) : 64;
			}else{
				nFrameLength = 64;
			}
		}

		CTool::ExchangeInteger(nFrameLength, ucSendData + 12, 2);

		int nlen = 14;
		if(sFrameDataField->bMulticast)
		{
			ucSendData[nlen] = (sFrameDataField->bMulticast ? 0x80:0x00) | sFrameDataField->nMulticastNum;
			nlen ++;
		}

		int nTemp = (sFrameDataField->bNoMulticast ? 0x80 : 0x00) << 8 |
								(sFrameDataField->bAnswer ? 0x40 : 0x00) << 8 |
								(sFrameDataField->bRead ? 0x20 : 0x00) << 8 |
								(sFrameDataField->bFIFO ? 0x10 : 0x00) << 8 |
								(sFrameDataField->ucAddress[0] & 0x0F) << 8 | sFrameDataField->ucAddress[1];

		CTool::ExchangeInteger(nTemp, ucSendData + nlen, 2);

		nlen += 2;
		nTemp = (sFrameDataField->nSerialNumber << 12) |
				(sFrameDataField->nLength);
		CTool::ExchangeInteger(nTemp, ucSendData + nlen, 2);

		if(sFrameDataField->bRead == false)
		{
			nlen += 2;
			memcpy(ucSendData + nlen,sFrameDataField->ucData,sFrameDataField->nLength);
		}else{
			nlen += 2;
			memcpy(ucSendData + nlen,sFrameDataField->ucData,42);
		}

		//CRC
		nTemp = CreateChecksum(ucSendData,nFrameLength - 4);
		CTool::ExchangeInteger(nTemp, ucSendData + (nFrameLength - 4), 4);

		return nFrameLength;
}

int CPackEthernetFrame::CreateChecksum(unsigned char* ucSenddata,
						   int nLen)
{
	//return crc32(0,ucSenddata,nLen);
	return crc32(ucSenddata,nLen);
}

int CPackEthernetFrame::UnPackData(unsigned char *pRcvData, int nLen, FrameDataField &m_sFrameDataField)
{
	if (memcmp(pRcvData,ucSourceAddress,6) != 0)
	{
		return ANSWER_ERROR_DEST_ADDRESS;
	}
	if (0 == memcmp(pRcvData + 6,ucSourceAddress,6))
	{
		return ANSWER_ERROR_SOUR_ADDRESS;
	}
	int nDataLengthResult = pRcvData[12] << 8 |
						pRcvData[13];
	if (nDataLengthResult != nLen)
	{
		return ANSWER_ERROR_DATA_LENGTH;
	}
	int nCheckSumResult = pRcvData[nDataLengthResult - 4] << 24 |
							pRcvData[nDataLengthResult - 3] << 16 |
							pRcvData[nDataLengthResult - 2] << 8 |
							pRcvData[nDataLengthResult - 1];
	int nCheckSum = CreateChecksum(pRcvData,nDataLengthResult - 4);
	if (nCheckSumResult != nCheckSum)
	{
		return ANSWER_ERROR_CRC;
	}
	memset(m_ucDestAddress, 0, 6);
	memcpy(m_ucDestAddress,pRcvData,6);
	memset(m_ucSourceAddress, 0, 6);
	memcpy(m_ucSourceAddress,pRcvData + 6,6);
	m_sFrameDataField.reset();
	
	m_sFrameDataField.bMulticast = (1 == (pRcvData[14] >> 7)) ? true : false;

	int nlen = 14;
	if(m_sFrameDataField.bMulticast)
	{
		m_sFrameDataField.nMulticastNum = pRcvData[nlen] & 0x7F;
		nlen ++;
	}

	m_sFrameDataField.bNoMulticast = (1 == (pRcvData[nlen] >> 7)) ? true : false;
	m_sFrameDataField.bAnswer = (1 == ((pRcvData[nlen] >> 6) & 0x01)) ? true : false;
	m_sFrameDataField.bRead = (1 == ((pRcvData[nlen] >> 5) & 0x01)) ? true : false;
	//FIFO/REG
	m_sFrameDataField.bFIFO = (1 == ((pRcvData[nlen] >> 4) & 0x01)) ? true : false;
	m_sFrameDataField.ucAddress[0] = pRcvData[nlen] & 0x0F;
	m_sFrameDataField.ucAddress[1] = pRcvData[nlen + 1];

	nlen += 2;
	m_sFrameDataField.nSerialNumber = pRcvData[nlen] >> 4;
	m_sFrameDataField.nLength = pRcvData[nlen] << 4 |
								pRcvData[nlen + 1];

	nlen += 2;
	memcpy(m_sFrameDataField.ucData,pRcvData + nlen,m_sFrameDataField.nLength);
	pRcvData = pRcvData + nlen;

	return 1;
}
bool CPackEthernetFrame::UnPackErrorType()
{
	return false;
}
