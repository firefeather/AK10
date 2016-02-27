
#include "CLPackCommunicationData.h"
#include "../../../util/util.h"
#include "math.h"
#include <android/log.h>

extern int globe_CardVesion;
extern GLOBALVARIABLE	  g_GlobalVariable;				//���̵�ȫ�ֱ�������


CCLPackCommunicationData::CCLPackCommunicationData(void)
{
	//memset(this, 0, sizeof(CCLPackCommunicationData));
}

CCLPackCommunicationData::~CCLPackCommunicationData(void)
{
}
//ͨ�����ݴ���Ļ�����ʽ��ucData == nullʱ������Ϊ0������ int - ������
int CCLPackCommunicationData::PackDataBase(unsigned int ucDestSendCardAddress, //�����ɼ�����ַ ��000~111����Ӧ1��~8�Ųɼ�����ַ��
										   int ucDestAddress,//Ŀ���ַ
										   unsigned char ucPackType,//������
										   unsigned int unSequenceNumber,//˳���
										   int nAnsweredFlag,//Ӧ���ʾ
										   unsigned char *ucData,//�������ֽ�
										   unsigned char * ucSendData//�����������
										   )
{
	//������ǲɼ����ĵ�ַ
	if (ucPackType>=0x40)//ɨ�迨�����
	{
        if (ucDestAddress>248&&ucDestAddress>0)
        {
			globe_CardVesion=1;
        }

		if (ucDestAddress<0)//���ܱ�ַ�����㲥����248��ַ��ʱ������Э�飬�����㲥���þ�Э��
		{
			if (ucPackType==0x40&&unSequenceNumber==0x0b)
			{
				unsigned int nStart=*ucData+*(ucData+1)*0x100;
				if (nStart>248)
				{
					globe_CardVesion=1;
				}

				else
				{
					globe_CardVesion=0;
				}
			}
			else
			{
				globe_CardVesion=0;
			}
		}
		if (-2 == ucDestAddress)//��Э��㲥
		{
			globe_CardVesion=1;
		}
	}
	else
	{
         globe_CardVesion=0;
	}

	/*
    if(globe_CardVesion==1)//�汾��Ϊ1��
    {
        //�������Բɼ��������ģ�������Э��
		return PackDataBaseV1(ucDestSendCardAddress,ucDestAddress,ucPackType,unSequenceNumber,nAnsweredFlag,ucData,ucSendData);
    }
    */
	const unsigned char sPackagehead[2] = {0X55,0x55};//����֡��ʼ��־
	const unsigned char sPackageHeadFree[2] = {0X12,0x34};//����֡�����ֽ�

	int nLen = 0;

	//1 ��ͷ1  2  �̶�Ϊ��0x55 0x55
	memcpy(ucSendData, sPackagehead, 2);
	//2 ��ͷ2 2 �ݶ�Ϊ 0x12 0x34
	memcpy(ucSendData + 2,sPackageHeadFree, 2);
	//3 Ŀ�ĵ�ַ   1
	if(ucDestAddress==-1)
	{
		ucSendData[4]=0xff;
	}
	else if (ucDestAddress==-2)
	{
		ucSendData[4]=0xf9;
	}
	else if (ucDestAddress==-3)
	{
		ucSendData[4]=0xfa;
	}
	else
	{
        ucSendData[4] = ucDestAddress;
	}

	//4 ������  1
	ucSendData[5] = ucPackType;

	//5 Դ��ַ   1
	ucSendData[6] = 0;
	//6 ˳���    2
	CTool::ExchangeInteger(unSequenceNumber, ucSendData + 7, 2);
	//7 Ӧ���ʶ 0X00-��Ӧ��0x01-��Ӧ��0x02-��ʾ��������77��Ӧ����������Ӧ���־λΪ1������ΪAA��
	ucSendData[9] = nAnsweredFlag;
	//8 �����ֽ�  1   ���0x00��������
	ucSendData[10] = ucDestSendCardAddress ;
	nLen = 11;
	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�
	if(ucData)
		memcpy(ucSendData + nLen, ucData, CL_SEND_EFFECT_DATA_SIZE);
	else
		memset(ucSendData + nLen, 0, CL_SEND_EFFECT_DATA_SIZE);
	nLen += 16;
	//10	У���루CHECKSUM��	2	Checksum = ����ͷ��У�����������ֽ�(23)���
	//��λ��ǰ����λ�ں�
	CreateChecksum(ucSendData + 4, nLen - 4, ucSendData + nLen);
	nLen++;
	return (nLen);
}
///hh modify///////////////////////////////////////////////////////////////////////
int CCLPackCommunicationData::PackDataBaseV1(unsigned int ucDestSendCardAddress, //�����ɼ�����ַ ��000~111����Ӧ1��~8�Ųɼ�����ַ��
										   int ucDestAddress,//Ŀ���ַ
										   unsigned char ucPackType,//������
										   unsigned int unSequenceNumber,//˳���
										   int nAnsweredFlag,//Ӧ���ʾ
										   unsigned char *ucData,//�������ֽ�
										   unsigned char * ucSendData
										  )//�����������
{

	const unsigned char sPackagehead[2] = {0X55,0x55};//����֡��ʼ��־
	const unsigned char sPackageHeadFree[2] = {0X12,0x56};//����֡�����ֽ�


	int nLen = 0;

	//1 ��ͷ1  2  �̶�Ϊ��0x55 0x55
	memcpy(ucSendData, sPackagehead, 2);
	//2 ��ͷ2 2 �ݶ�Ϊ 0x12 0x56
	memcpy(ucSendData + 2,sPackageHeadFree, 2);

	//�����ֽ�
    ucSendData[4] = 0x00;
	ucSendData[5] = 0x00;
	if ( 0x1C == unSequenceNumber )//1C�������翨����
	{
		ucSendData[5] = 0x01;
	}

	//3 Ŀ�ĵ�ַ   1
	//����ǲɼ�������hub��ĵ�ַ0x01~0xf8 ��256

	if (ucDestAddress<0)//���͹㲥��
	{
		  ucSendData[6]=0xff;
		  ucSendData[7]=0xff;
	}
	else
	{
		 if ( _SSeriesVersion == g_GlobalVariable.nVersionType )
		 {
			 //Sϵ�з������������ά��ַ
			 char nNetcardNo = (ucDestAddress & 0xFF00) >> 8;
			 char nScanCardId = ucDestAddress & 0x00FF;
			 ucSendData[6] = nNetcardNo;//��8λ
			 ucSendData[7] = nScanCardId;//��8λ
		 }
		 else
		 {
			 ucDestAddress += 0x08;
			 ucSendData[6] = (ucDestAddress&0xff00)>>8;//��8λ
			 ucSendData[7] = (ucDestAddress&0x00ff);//��8λ
		 }
	}

	//4 ������  1
	ucSendData[8] = ucPackType;
	//5 Դ��ַ   2
	ucSendData[9] = 0x00;
	ucSendData[10] = 0x00;
	//6 ˳���    2
	CTool::ExchangeInteger(unSequenceNumber, ucSendData + 11, 2);
	//7 Ӧ���ʶ 0X00-��Ӧ��0x01-��Ӧ��0x02-��ʾ��������77��Ӧ����������Ӧ���־λΪ1������ΪAA��
	ucSendData[13] = nAnsweredFlag;
	//8 �����ֽ�  1   ���0x00��������
	ucSendData[14] = ucDestSendCardAddress ;
	nLen = 15;
	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�

	if ( _SSeriesVersion != g_GlobalVariable.nVersionType )
	{
		if (ucPackType==0x40&&unSequenceNumber==0x0b)
		{
			unsigned int nStart=*ucData+*(ucData+1)*0x100;
			if (nStart>248)
			{
				//ֻ�ܱ�ַ+8
				nStart+=0x08;
				*ucData = (nStart&0x00ff);//��8λ
				*(ucData+1) = (nStart&0xff00)>>8;
			}
		}
	}

	if(ucData)
		memcpy(ucSendData + nLen, ucData, CL_SEND_EFFECT_DATA_SIZE);
	else
		memset(ucSendData + nLen, 0, CL_SEND_EFFECT_DATA_SIZE);


	nLen += 16;
	//10	У���루CHECKSUM��	2	Checksum = ����ͷ��У�����������ֽ�(23)���
	//��λ��ǰ����λ�ں�
	CreateChecksum(ucSendData + 4, nLen - 4, ucSendData + nLen);
	nLen++;
	return (nLen);
}

int CCLPackCommunicationData::PackDataBaseV2(unsigned int ucDestSendCardAddress, //�����ɼ�����ַ ��000~111����Ӧ1��~8�Ųɼ�����ַ��
											 int ucDestAddress,//Ŀ���ַ
											 unsigned char ucPackType,//������
											 unsigned int unSequenceNumber,//˳���
											 int nAnsweredFlag,//Ӧ���ʾ
											 unsigned char *ucData,//�������ֽ�
											 unsigned char * ucSendData,
											 short nFlag)//�����������
{
	const unsigned char sPackagehead[2] = {0X55,0x55};//����֡��ʼ��־
	const unsigned char sPackageHeadFree[2] = {0X12,0x56};//����֡�����ֽ�

	int nLen = 0;

	//1 ��ͷ1  2  �̶�Ϊ��0x55 0x55
	memcpy(ucSendData, sPackagehead, 2);
	//2 ��ͷ2 2 �ݶ�Ϊ 0x12 0x56
	memcpy(ucSendData + 2,sPackageHeadFree, 2);

	//�����ֽ�
	//1C�������翨���1Dʱ��Ϊ���������翨�����ucDestSendCardAddress=200����Ϊ��־��Ϊ����ʱ��ʾ���
	ucSendData[4] = 0x00;
	ucSendData[5] = 0x00;
	if ( 0x1C == unSequenceNumber || (0x1D == unSequenceNumber && 1 == nFlag) )
	{
		ucSendData[5] = 0x01;
	}

	//3 Ŀ�ĵ�ַ   1
	//����ǲɼ�������hub��ĵ�ַ0x01~0xf8 ��256

	if (ucDestAddress<0)//���͹㲥��
	{
		ucSendData[6]=0xff;
		ucSendData[7]=0xff;
	}
	else
	{
		if ( _SSeriesVersion == g_GlobalVariable.nVersionType )
		{
			//Sϵ�з������������ά��ַ
			char nNetcardNo = (ucDestAddress & 0xFF00) >> 8;
			char nScanCardId = ucDestAddress & 0x00FF;
			ucSendData[6] = nNetcardNo;//��8λ
			ucSendData[7] = nScanCardId;//��8λ
		}
		else
		{
			ucDestAddress += 0x08;
			ucSendData[6] = (ucDestAddress&0xff00)>>8;//��8λ
			ucSendData[7] = (ucDestAddress&0x00ff);//��8λ
		}
	}

	//4 ������  1
	ucSendData[8] = ucPackType;
	//5 Դ��ַ   2
	ucSendData[9] = 0x00;
	ucSendData[10] = 0x00;
	//6 ˳���    2
	CTool::ExchangeInteger(unSequenceNumber, ucSendData + 11, 2);
	//7 Ӧ���ʶ 0X00-��Ӧ��0x01-��Ӧ��0x02-��ʾ��������77��Ӧ����������Ӧ���־λΪ1������ΪAA��
	ucSendData[13] = nAnsweredFlag;
	//8 �����ֽ�  1   ���0x00��������
	ucSendData[14] = ucDestSendCardAddress ;
	nLen = 15;
	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�

	if ( _SSeriesVersion != g_GlobalVariable.nVersionType )
	{
		if (ucPackType==0x40&&unSequenceNumber==0x0b)
		{
			unsigned int nStart=*ucData+*(ucData+1)*0x100;
			if (nStart>248)
			{
				//ֻ�ܱ�ַ+8
				nStart+=0x08;
				*ucData = (nStart&0x00ff);//��8λ
				*(ucData+1) = (nStart&0xff00)>>8;
			}
		}
	}

	if(ucData)
		memcpy(ucSendData + nLen, ucData, CL_SEND_EFFECT_DATA_SIZE);
	else
		memset(ucSendData + nLen, 0, CL_SEND_EFFECT_DATA_SIZE);


	nLen += 16;
	//10	У���루CHECKSUM��	2	Checksum = ����ͷ��У�����������ֽ�(23)���
	//��λ��ǰ����λ�ں�
	CreateChecksum(ucSendData + 4, nLen - 4, ucSendData + nLen);
	nLen++;
	return (nLen);
}
//////////////////////////////////////////////////////////////////////////

//�������ݰ�У���룬��λ��ǰ����λ�ں�
void CCLPackCommunicationData::CreateChecksum(unsigned char* ucSendData,//��������
										 int nLen, //��Ч���ݳ���
										 unsigned char ucChecksum[1])//У����
{
	unsigned short nData = ucSendData[0];
	for(int n = 1; n < nLen; n++)
		nData ^= ucSendData[n];
	CTool::ExchangeInteger((unsigned char) nData, ucChecksum, 1);
}
bool CCLPackCommunicationData::GetAtiecVerson(ATIEC& atiec)
{//zhangjj004
	atiec.ATIEC_EStatus.dVersion = (double)(m_ucRcvData[2] + (double) m_ucRcvData[3] / 100);
	//����״̬
	if (m_ucRcvData[4] & 0x01)
		atiec.ATIEC_EStatus.bBackup = true;
	else
		atiec.ATIEC_EStatus.bBackup = false;
	if (m_ucRcvData[4] & 0x02)
		atiec.ATIEC_EStatus.bBackupP2P3 = true;
	else
		atiec.ATIEC_EStatus.bBackupP2P3 = false;
	//Bit1	����״̬�� 1��������0��������
	if (m_ucRcvData[4] & 0x03)
		atiec.ATIEC_EStatus.bLockScreen = true;
	else
		atiec.ATIEC_EStatus.bLockScreen = false;
	//Bit2	�ϵ������ʼ��״̬��1���ɹ���0��ʧ��

	if (m_ucRcvData[4] & 0x04)
		atiec.ATIEC_EStatus.bInit = true;
	else
		atiec.ATIEC_EStatus.bInit = false;
	//Bit3	P0����״̬��1�������ӣ�0��������
	//Bit4	P1����״̬��1�������ӣ�0��������
	atiec.ATIEC_EStatus.nP0LinkState = (m_ucRcvData[4] >> 4) & 0x01 ;
	atiec.ATIEC_EStatus.nP1LinkState = (m_ucRcvData[4] >> 5) & 0x01 ;
	atiec.ATIEC_EStatus.nP2LinkState = (m_ucRcvData[4] >> 6) & 0x01 ;
	atiec.ATIEC_EStatus.nP3LinkState = (m_ucRcvData[4] >> 7) & 0x01 ;
	//pMonitorData->bReadMonitorSendCardFlag = -1;
	return true;
}
//��Ӧ���
BOOL CCLPackCommunicationData::UnPackData(unsigned char *pRcvData, int nLen)
{
	const unsigned char sPackagehead[2] = {0X55,0x55};//����֡��ʼ��־
	const unsigned char sPackageHeadFree[2] = {0X12,0x34};//����֡�����ֽ�
	const unsigned char sPackageHeadFreeV1[2] = {0X12,0x56};//����֡�����ֽ�

	//��ͷ1
	if (memcmp(pRcvData,sPackagehead, 2) != 0)
		return CL_ANSWER_ERROE_PACK_HEAD_1;
	//��ͷ2
    if (memcmp(pRcvData + 2, sPackageHeadFreeV1, 2) != 0&&memcmp(pRcvData + 2, sPackageHeadFree, 2) != 0)
	{
        return CL_ANSWER_ERROE_PACK_HEAD_2;
	}
	else if (memcmp(pRcvData + 2, sPackageHeadFreeV1, 2) == 0)
	{
		nLen=CL_SEND_PACK_SIZE_V1;
		return UnPackDataV1(pRcvData,nLen);
	}

	//Ŀ�ĵ�ַ(PC)
	if (pRcvData[4] != 0x00)
		return CL_ANSWER_ERROE_DEST_ADDRESS;
	//Դ��ַ
	if(pRcvData[6] == 0x00)
		return CL_ANSWER_ERROE_SOUR_ADDRESS;

	//У�����ݰ��Ƿ�Ϸ�
	unsigned char ucChecksum[1];
	memset(ucChecksum, 0, sizeof(ucChecksum));
	CreateChecksum(pRcvData + 4, nLen - 5, ucChecksum);

	if (pRcvData[nLen-1] != ucChecksum[0])
		return CL_ANSWER_ERROE_CHECKSUM;

	m_ucDestSendCardAddress = pRcvData[10];//�����ɼ�����ַ
	m_ucSourAddress = pRcvData[6];//Դ��ַ
	m_unDestAddress = pRcvData[4];//Ŀ�ĵ�ַ(PC)
	m_ucPackType = pRcvData[5];//������
	m_nSequenceNumber = CTool::doGetIntegerData(pRcvData+ 7 , 2);//���к�
	memset(m_ucRcvData, 0, sizeof(m_ucRcvData));

	m_nLen = nLen - 11;
	memcpy(m_ucRcvData, pRcvData + 11, nLen - 12);//����������
	bool bAnsOK = UnPackErrorType();
	if(bAnsOK)
		return 1;
	else
		return CL_ANSWER_ERROE_CONNECT;
}
//��Э����
BOOL CCLPackCommunicationData::UnPackDataV1(unsigned char *pRcvData, int nLen)
{
	const unsigned char sPackagehead[2] = {0X55,0x55};//����֡��ʼ��־
	const unsigned char sPackageHeadFree[2] = {0X12,0x56};//����֡�����ֽ�

	//��ͷ1
	if (memcmp(pRcvData,sPackagehead, 2) != 0)
		return CL_ANSWER_ERROE_PACK_HEAD_1;
	//��ͷ2
	if (memcmp(pRcvData + 2, sPackageHeadFree, 2) != 0)
		return CL_ANSWER_ERROE_PACK_HEAD_2;

	//Ŀ�ĵ�ַ(PC)
	unsigned short nDest= pRcvData[6]*0x100+pRcvData[7];
	if(nDest!=0)
		return CL_ANSWER_ERROE_DEST_ADDRESS;

	//Դ��ַ
	unsigned short nSource= pRcvData[9]*0x100+pRcvData[10];
	if ( _SSeriesVersion != g_GlobalVariable.nVersionType )
	{
		nSource-=8;
	}
	if(nSource==0)
		return CL_ANSWER_ERROE_SOUR_ADDRESS;

	//У�����ݰ��Ƿ�Ϸ�
	unsigned char ucChecksum[1];
	memset(ucChecksum, 0, sizeof(ucChecksum));
	CreateChecksum(pRcvData + 4, nLen - 5, ucChecksum);

	if (pRcvData[nLen-1] != ucChecksum[0])
		return CL_ANSWER_ERROE_CHECKSUM;

	m_ucDestSendCardAddress = pRcvData[12];//�����ɼ�����ַ
	m_ucSourAddress = pRcvData[9]*0x100+pRcvData[10];//Դ��ַ
	if ( _SSeriesVersion != g_GlobalVariable.nVersionType )
	{
		m_ucSourAddress -= 8;
	}

	m_unDestAddress = pRcvData[6]*0x100+pRcvData[7];//Ŀ�ĵ�ַ(PC)
	m_ucPackType = pRcvData[8];//������
	m_nSequenceNumber = CTool::doGetIntegerData(pRcvData+ 9 , 2);//���к�
	memset(m_ucRcvData, 0, sizeof(m_ucRcvData));

	m_nLen = nLen - 15;
	memcpy(m_ucRcvData, pRcvData + 15, nLen - 16);//����������

	bool bAnsOK = UnPackErrorType();
	if(bAnsOK)
		return 1;
	else
		return CL_ANSWER_ERROE_CONNECT;
}

//��Ӧ�����������
bool CCLPackCommunicationData::UnPackErrorType()
{
	//Ӧ����Ϣλ	1�ֽ�	0X77����ɹ���0XAA����ʧ��)
	switch(m_ucPackType)
	{
		//�ɼ���EDID���ð�	0x01
	case 0x01:
		//�ɼ���״̬��ȡ��	0x03
	case 0x03:
		//�ɼ����汾�Ų�ѯ�� 	0x04
	case 0x04:
		//�ɼ�����λ����� 0x05
	case 0x05:
		//�ɼ�������ʹ�ܰ� 	0x06
	case 0x06:		
		//�ɼ���������ð�	0x07
	case 0x07:
		//�ɼ�����ʼ��ַ�����ð�	0x08
	case 0x08:	
		//�ɼ�������ת�����ð�	0x0B
	case 0x0B:
		//�ɼ�����ȡ���ð�	0x0D
	case 0x0E://���ܱ�ַ��
	case 0x0F:
	case 0x10:
	case 0x11:
	case 0x12:
	case 0x13:
	case 0x14:
	case 0x15:
	case 0x16:
	case 0x17:
	case 0x18:
	case 0x19:
	case 0x1A:
	case 0x0D:
		//��ɨ�迨��ص����а����ͣ�
		//ɨ�迨�洢��������� ��ʼ��	0x49
	case 0x49:
		//ɨ�迨������	0x60
	case 0x50:
	case 0x51:
	case 0x54:
	case 0x60:
		//����У�����ݰ�	0x61
	case 0x61:
		//٤��У��Red���ݰ�	0x62
	case 0x62:
		//٤��У��Green���ݰ�	0x63
	case 0x63:
		//٤��У��Blue���ݰ�	0x64
	case 0x64:
		//ɨ�迨���߲��ұ��	0x65
	case 0x65:
		//�������������	0x66
	case 0x66:
		//���ȵ��������	0x45
	case 0x45:
		//���ܱ�ַ������	0x6A
	case 0x6a:
		//��������ֵ���ð�	0xd2
	case 0x73:
		//ɨ�迨�汾��ѯ��	0x42
	case 0x42:
		//����У��ʹ�ܰ�  0x47
	case 0x47:
		//ɫ�µ��������       0x40
	case 0x40:
		//��ʾ���Բ���������	0x6B
	case 0x6b:
		//����оƬ��·����	0x6C
	case 0x6c:
		//����оƬ���ȼ���	0x6D
	case 0x6d:
		//ɨ�迨�������������	0x6E
	case 0x6e:
		//ɨ�迨�����������ݰ�	0x6F
	case 0x6f:
		//�����������ð� ������ȡɨ�迨��SPI_FLASH�е�У�����ݵ�SDRAM	0x91
	case 0x91:
		//��ȡģ��ID��
	case 0xA0:
		m_bACKOK = m_ucRcvData[0] == 0x77 ? true : false;
		break;
		//��������	0xd0
	case 0xd0:
		{
			if (m_ucRcvData[0] == 0x77)
				m_bACKOK = true;
			else if (m_ucRcvData[0] == 0xA1)
			{
				//0xA1	����У��������ݳ���������У��ʹ���	Ӧ��ʧ�ܣ������ֽ����0x00��ֵ�����壻
				m_bACKOK = true;
				m_ucErrCode = 1;
			}
			else if (m_ucRcvData[0] == 0xA2)
			{
				//0xA2	�����¶ȳ�����Χ,���ݷ�Χ��-40.0--123��

				m_bACKOK = true;
				m_ucErrCode = 2;
			}
			else if (m_ucRcvData[0] == 0xA3)
			{
				//0xA3	����ʪ�ȳ�����Χ,���ݷ�Χ��0.0%---100.0%

				m_bACKOK = true;
				m_ucErrCode = 3;
			}
			else if (m_ucRcvData[0] == 0xA4)
			{
				//������ʪ�ȴ�����û�н�,����ʪ�ȱ�־λ=1���ɼ�����ʪ�ȶ�Ϊ0���߷��������ݿ��ж�

				m_bACKOK = true;
				m_ucErrCode = 4;
			}
			else
			{
				if ( 1 == m_nSequenceNumber)
				{
					m_bACKOK = true;
				}
				else if(2 == m_nSequenceNumber)//hh
				{
					m_bACKOK = true;
				}
				else
				{
					m_bACKOK = false;
				}
			}
			break;
		}
		//�̵����������ð���0xD1��
	case 0xd1:
		//�̵����ֶ�ģʽ������ư���0xD2��
	case 0xd2:
		//�̵����Զ�ģʽ����ֵ���ð���0xD3��
	case 0xD3:
		//��Դ��ѹ������A/D��������(0xDB)
	case 0xDB:
		//���ʲ�������(0xDC)
	case 0xDC:
		{
			if (m_ucRcvData[0] == 0x77)
				m_bACKOK = true;
			else if (m_ucRcvData[0] == 0xA0)
			{
				//0xA0	���ؼ�ؿ�У���ʧ��
				m_bACKOK = true;
				m_ucErrCode = 1;
			}
			else if (m_ucRcvData[0] == 0xA1)
			{
				//0xA1	��ǰ��ذ�Ӳ����֧�ָ�Э�飻����û�а�װ�̵���ģ��ȴ�յ���Э��
				m_bACKOK = true;
				m_ucErrCode = 2;
			}
			else if (m_ucRcvData[0] == 0xA2)
			{
				//0xA2	��ǰ�̵���������ģʽ��֧�ָ�Э�飻����̵���D1��������Ϊ�¿�ģʽȴ�յ��˸�ָ��


				m_bACKOK = true;
				m_ucErrCode = 3;
			}
			else if (m_ucRcvData[0] == 0xA3)
			{
				//0xA3	����ʪ�ȳ�����Χ,���ݷ�Χ��0.0%---100.0%

				m_bACKOK = true;
				m_ucErrCode = 4;
			}
			else if (m_ucRcvData[0] == 0xA4)
			{
				//0xA4	�̵�����ų�����Χ

				m_bACKOK = true;
				m_ucErrCode = 5;
			}
			else
				m_bACKOK = false;
			break;
		}
	case 0xDD:
		m_bACKOK = m_ucRcvData[0] == 0x77 ? true : false;
		break;
		//LED�ƿ�·����	0xE0
	case 0XDF:
		m_bACKOK = m_ucRcvData[0] == 0x77 ? true : false;
		break;
	case 0xE0:
		{
			if (m_ucRcvData[0] == 0x77)
				m_bACKOK = true;
			else if (m_ucRcvData[0] == 0xAA)
			{
				m_bACKOK = true;
				m_ucErrCode = m_ucRcvData[0];
			}
			else
				m_bACKOK = false;
		}
		break;
		//HUB�����������ݰ���0XE2��
	case 0xE2:
		//HUB�汾��ѯ����0XE3��
	case 0xE3:
		//��HUB���������ݰ���0XE4��
	case 0xE4:
		m_bACKOK = m_ucRcvData[0] == 0x77 ? true : false;
		break;
	case 0x9D://�ض�ɨ�迨������
	case 0x9E:
		//�ض�У�����ݰ�
		{
			if(m_ucRcvData[0] == 0xAA && m_nAnswered == 1)
			{
				m_bACKOK = false;
			}
			else if(m_nAnswered == 0)
			{
				m_bACKOK = true;
			}
		}
		break;
	}
	return m_bACKOK;
}

//���ģ�����к�
void CCLPackCommunicationData::GetModSeriNum(string & sModSerial, bool bAnswFlag)
{
	sModSerial = "";

	int nOffset = 0;
	if(bAnswFlag)
		nOffset = 1;

	char ucOeder[CALIBRATION_ORDER_NUM_LEN + CALIBRATION_DISPLAY_NUM_LEN + 1];	//������+�����
	memset(ucOeder,0,CALIBRATION_ORDER_NUM_LEN + CALIBRATION_DISPLAY_NUM_LEN + 1);
	memcpy(ucOeder,m_ucRcvData + nOffset,CALIBRATION_ORDER_NUM_LEN + CALIBRATION_DISPLAY_NUM_LEN);


	char ucModuleID[CALIBRATION_CABINET_NUM_LEN +CALIBRATION_MODULE_NUM_LEN + 1];	//�����ģ���
	memset(ucModuleID,0,CALIBRATION_CABINET_NUM_LEN +CALIBRATION_MODULE_NUM_LEN + 1);

	//�����(2byte)
	int nCardID = CTool::doGetIntegerData(m_ucRcvData + CALIBRATION_ORDER_NUM_LEN + CALIBRATION_DISPLAY_NUM_LEN + nOffset, 2);
	if(nCardID > 9999 || nCardID < 0)
		nCardID = 0;

	//ģ���(2byte)
	int nModID = CTool::doGetIntegerData(m_ucRcvData + CALIBRATION_ORDER_NUM_LEN + CALIBRATION_DISPLAY_NUM_LEN + nOffset + 2, 2);
	if(nModID > 999 || nModID < 0)
		nModID = 0;

	sprintf(ucModuleID, "%.4d%.3d",nCardID,nModID);

	sModSerial = ucOeder;
	sModSerial += ucModuleID;

}

//��ȡ���Ϳ�״̬
void CCLPackCommunicationData::GetSendCardStatus(ATLVCAK6Status & sAtlvcStatus)
{
	/*
	bit7-bit2	Ԥ����չ,��0��ֵ������
	Bit1	1	ָʾDVI״ֵ̬��Ч
		0	ָʾDVI״ֵ̬��Ч
	Bit0   1  ָʾ���緢��״ֵ̬��Ч
			0  ָʾ���緢��״ֵ̬��Ч
	*/

	unsigned char c = m_ucRcvData[1]>>1;
	c = c & 0x01;
	//DVI״ֵ̬
	if (c)
	{
		unsigned char ucFlag = m_ucRcvData[2];
		sAtlvcStatus.nDvi = ucFlag & 0x01;
	}
	//HDMI״ֵ̬
	c = m_ucRcvData[1] >> 2;
	sAtlvcStatus.nHdmi = c & 0x01;
	/*if (c)
	{
		unsigned char ucFlag = m_ucRcvData[2] ;
		sAtlvcStatus.nHdmi = ucFlag & 0x01;
	}*/
	//���緢��״̬
// 	c = m_ucRcvData[1];
// 	c = c & 0x01;
// 	if (c)
	{
		/*Bit1	1-P1��Ч	0-P1��Ч
		Bit3	1-P3��Ч    0-P3��Ч
		Bit5	1-P0��Ч    0-P0��Ч
		Bit7	1-P2��Ч    0-P2��Ч*/
		//P1��  D  �¿�  1-��Ч��0-��Ч
		unsigned char ucFlag;
		ucFlag = m_ucRcvData[3] >> 1;
		sAtlvcStatus.nNetPort[1] = ucFlag & 0x01;

		//P0��  U  �Ͽ�  1-��Ч��0-��Ч
		ucFlag = m_ucRcvData[3] >> 5;
		sAtlvcStatus.nNetPort[0] = ucFlag & 0x01;

		//P2��
		ucFlag = m_ucRcvData[3] >> 7;

		sAtlvcStatus.nNetPort[2] = ucFlag & 0x01;

		//P3��
		ucFlag = m_ucRcvData[3] >> 3;
		sAtlvcStatus.nNetPort[3] = ucFlag & 0x01;
	}
	//�ֱ���  1024*768 ��һ����
	//�ֱ��� �� 0x03FF
	sAtlvcStatus.nResolution[0] = CTool::doGetIntegerData(m_ucRcvData + 4, 2) + 1;
	//�ֱ��� �� 0x02FF
	sAtlvcStatus.nResolution[1] = CTool::doGetIntegerData(m_ucRcvData + 6, 2) + 1;

	//֡�ʻ�ȡ
	sAtlvcStatus.iFrame = CTool::doGetIntegerData(m_ucRcvData + 8, 1);

	c = m_ucRcvData[9] & 0x03;
	switch (c)
	{
	case 0:
		sAtlvcStatus.nVideowid =8;
		break;
	case 1:
		sAtlvcStatus.nVideowid =10;
		break;
	case 2:
		sAtlvcStatus.nVideowid =12;
		break;
	default:
		sAtlvcStatus.nVideowid =8;
		break;
	}

	//2012-11-20
	int nTempTime = CTool::doGetIntegerData(m_ucRcvData + 10 , 2);

	float fFrame = ((float)1000000/(nTempTime * 10 ));
	 sAtlvcStatus.nFrame = (int )(ceil(fFrame)) ;
	 if (sAtlvcStatus.nFrame < 20  )
	 {
		 sAtlvcStatus.nFrame = 20;
	 }
	 else if (sAtlvcStatus.nFrame > 300)
	 {
		 sAtlvcStatus.nFrame = 300;
	 }

	//sAtlvcStatus.nHres = CTool::doGetIntegerData(m_ucRcvData + 10 , 2);

	//sAtlvcStatus.nVres = CTool::doGetIntegerData(m_ucRcvData + 12 , 2);

	//sAtlvcStatus.nXclkInPclk = CTool::doGetIntegerData(m_ucRcvData + 14 , 2);

	//��ȡ��Чλ, -1 --�ɹ� 0 -- ��ʼ��  1 -- ʧ��
	sAtlvcStatus.nReadErrorFlag = -1;
}
/*
2012-11-12 ��ȡ�ɼ����Զ����ȵ��ںͶ�ʱ���ȵ���״̬
*/
void CCLPackCommunicationData::GetSendCardLightAdjustStatus(ATLVCAK6Status & sAtlvcStatus)
{
	//0-�Զ����ȵ��ڲ�ʹ�� 1-��ʾ�Զ����ȵ���ʹ��
	//0-��ʱ���ȵ���ʹ�ܣ�1-��ʾ��ʱ���ȵ���ʹ��
   sAtlvcStatus.nAutoLightAdjustStatus = m_ucRcvData[1];
   sAtlvcStatus.nTimingLightAdjustStatus = m_ucRcvData[2];
   //��ȡ��Чλ, -1 --�ɹ� 0 -- ��ʼ��  1 -- ʧ��
   sAtlvcStatus.nReadErrorFlag = -1;
}
//��ȡɨ�迨�汾
void CCLPackCommunicationData::GetSendCardVersion(Version & sVersion)
{
	//ACK_DATA1 	Bit7-5	0h��ɨ�迨 ��1h��������ؿ�1,  2h��������ؿ�2��3h��������ؿ�3, 4h��������ؿ�4
	// Bit4	0��boot����1��Ӧ�ó��� ,         Bit3-0	0h����׼�棬1h��PWM�� ;2h��������; 3h��ģ��ʽPWM��;4h��PWM������
	sVersion.ucTper = m_ucRcvData[1];//����汾����
	//�汾��
	sVersion.dVersion = (double)(m_ucRcvData[2] + (double) m_ucRcvData[3] / 100);
	//����״̬
	if (m_ucRcvData[4] & 0x01)
		sVersion.bBackup = true;
	else
		sVersion.bBackup = false;
	//Bit1	����״̬�� 1��������0��������
	unsigned char c = m_ucRcvData[4]>>1;
	if (c  & 0x01)
		sVersion.bLockScreen = true;
	else
		sVersion.bLockScreen = false;
	//Bit2	�ϵ������ʼ��״̬��1���ɹ���0��ʧ��
	c = m_ucRcvData[4] >> 2;
	if (c  & 0x01)
		sVersion.bInit = true;
	else
		sVersion.bInit = false;

	//Bit3	P0����״̬��1�������ӣ�0��������
	sVersion.nP0LinkState = (m_ucRcvData[4] >> 3) & 0x01;
	//Bit4	P1����״̬��1�������ӣ�0��������
	sVersion.nP1LinkState = (m_ucRcvData[4] >> 4) & 0x01;

	//����˿����ȼ���־
	sVersion.nNetPortPriority = (m_ucRcvData[4] >> 5) & 0x01;
	//������������
	sVersion.bLockNetPort = ((m_ucRcvData[4] >> 6) & 0x01) ? true : false;

	sVersion.nYear = 2000 + (m_ucRcvData[6]>>4&0x0F)*10 +(m_ucRcvData[6]&0x0F) ;
	sVersion.nMon = (m_ucRcvData[7]>>4&0x0F)*10 +(m_ucRcvData[7]&0x0F);
	sVersion.nDay = (m_ucRcvData[8]>>4&0x0F)*10 +(m_ucRcvData[8]&0x0F);
	sVersion.nSection = m_ucRcvData[9];
	sVersion.nCorrectType = m_ucRcvData[10]& 0x03;

	sVersion.nScanOut = m_ucRcvData[11];

	sVersion.nScanOutSection = m_ucRcvData[12];

	sVersion.nModuleEx = m_ucRcvData[13];

	sVersion.nTestVersion = m_ucRcvData[15];//���԰汾��Сʱ����ʾ����
	sVersion.nTestVersion_Second = m_ucRcvData[5];//���԰汾��������ʾ����
}

//��ȡģ�������ʱ��
void CCLPackCommunicationData::GetModuleRuntime(string & sModuleRuntime)
{
	//unsigned char c5 = m_ucRcvData[0];//0x77	�ɹ�	0xAA 	ʧ�ܣ������ֽ�������
	unsigned char c1 = m_ucRcvData[1];//����ʱ�������[31:24]
	unsigned char c2 = m_ucRcvData[2];//����ʱ�������[23:16]
	unsigned char c3 = m_ucRcvData[3];//����ʱ�������[15:8]
	unsigned char c4 = m_ucRcvData[4];//����ʱ�������[7:0]
//test
// 	c1 = 0x05;
// 	c2 = 0x39;
// 	c3 = 0x7F;
// 	c4 = 0xB1;
	long nRuntime = 0;
	nRuntime = c1 * 0x1000000 + c2 * 0x10000 + c3 * 0x100 + c4;

	int nMin;
	long nHour;
	nMin = nRuntime % 60;
	nHour = nRuntime / 60;

	char cRunTime[32];
	sprintf(cRunTime, "%ld:%2d", (nHour % 1000), nMin);
	sModuleRuntime = cRunTime;

	nHour = nHour / 1000;
	while (nHour > 0)
	{
		sprintf(cRunTime, "%ld,", nHour % 1000);
		sModuleRuntime = cRunTime + sModuleRuntime;
		nHour = nHour / 1000;
	}
}

//��ȡģ�������ʱ��
void CCLPackCommunicationData::GetModuleRuntime(long & nModuleRuntime)
{
	//unsigned char c5 = m_ucRcvData[0];//0x77	�ɹ�	0xAA 	ʧ�ܣ������ֽ�������
	unsigned char c1 = m_ucRcvData[1];//����ʱ�������[31:24]
	unsigned char c2 = m_ucRcvData[2];//����ʱ�������[23:16]
	unsigned char c3 = m_ucRcvData[3];//����ʱ�������[15:8]
	unsigned char c4 = m_ucRcvData[4];//����ʱ�������[7:0]
//test
// 	c1 = 0x05;
// 	c2 = 0x39;
// 	c3 = 0x7F;
// 	c4 = 0xB1;
	long nRuntime = 0;
	nRuntime = c1 * 0x1000000 + c2 * 0x10000 + c3 * 0x100 + c4;
	nModuleRuntime = nRuntime;
}


void CCLPackCommunicationData::GetMonitorDataFunRate(LPMONITORDATA pMonitorData)
{

	for (int i = 0 ; i < FAN_ROTATION_NUM_MAX ; i++)
	{
		pMonitorData->nFanRotation[i] = CTool::doGetIntegerData(m_ucRcvData+i *2 , 2);
	}
}
void CCLPackCommunicationData::GetMonitorDataPowerState(LPMONITORDATA pMonitorData)
{
    pMonitorData->nPowerState[0]=m_ucRcvData[0];
    pMonitorData->nPowerState[1]=m_ucRcvData[1];
}


//��ȡ����(ATIEC�����嵥��ɨ�迨)�������
void CCLPackCommunicationData::GetCustomMonitorData(LPMONITORDATA pMonitorData)
{
	MonitorItem sMonitorFlag;
	//MONITOR_FLAG	1	bit7-bit4	Ԥ����չ

	//Bit7 ���ȼ��ʹ��
	unsigned char c = m_ucRcvData[1] >> 7;
	sMonitorFlag.bFanStateFlag[0] = c & 0x01;
	sMonitorFlag.bFanStateFlag[1] = c & 0x01;

	//Bit3	1	ָʾ������ֵ��Ч
	//	0	ָʾ������ֵ��Ч
	c = m_ucRcvData[1] >> 3;
	sMonitorFlag.bSmogFlag = c & 0x01;

	//Bit2	1	ָʾ���ȼ��ֵ��Ч
	//	0	ָʾ���ȼ��ֵ��Ч
	c = m_ucRcvData[1]>> 2;
	sMonitorFlag.bBrightFlag = c & 0x01;

	//Bit1	1	ָʾʪ�ȼ��ֵ��Ч
	//	0	ָʾʪ�ȼ��ֵ��Ч
	c = m_ucRcvData[1]>>1;
	sMonitorFlag.bHumidityFlag = c & 0x01;

	//Bit0	1	ָʾ�¶ȼ��ֵ��Ч
	//	0	ָʾ�¶ȼ��ֵ��Ч
	c = m_ucRcvData[1];
	sMonitorFlag.bTemperatureFlag = c & 0x01;


	//TEMPERATURE_VALUE	2	-40.0--123.0��
	//�¶�ֵ����λ��ǰ����λ�ں󡣾�ȷ��0.1�ȡ�HUB��Ӧ��ʱ��ֱ�ӽ���ʪ�ȴ��������������������������ϴ���
	//��λ����Ӧ���ͨ��������ʽ�����¶�ֵ����ʾ��
	//T=0.01*D-40

	/*	2013-03-12
	��SHT11��ʪ�ȴ��������㹫ʽ�޸�ΪSHT21��ת����ʽ��
	�¶�  T = -46.85 + 175.72*S/2^(16)    14bit
	ʪ�� RH= -6 + 125*Q/2^(16)        12bit
	ע�⣺
	SҪ���յ������ݣ������ƣ������λ��00��
	QҪ���յ������ݣ������ƣ������λ��0010*/

	if (sMonitorFlag.bTemperatureFlag)
	{
		unsigned short l = CTool::doGetIntegerData(m_ucRcvData + 2, 2);
		//pMonitorData->fTemperature = (float)(0.01 * l - 40);
		l = l * 4;
		pMonitorData->fTemperature = (float) (-46.85 + 175.72 * l /pow((float)2,16));

		if (pMonitorData->fTemperature > 123)
			pMonitorData->fTemperature = 123.0;
		else if ( pMonitorData->fTemperature < -40)
			pMonitorData->fTemperature = -40.0;
	}

	//HUMILITY_VALUE	2	0.0---100.0%
	//ʪ��ֵ����ȷ��0.1�ȡ�HUB��Ӧ��ʱ��ֱ�ӽ���ʪ�ȴ��������������������������ϴ���
	//��λ����Ӧ���ͨ��������ʽ����ʪ��ֵ����ʾ��
	//H=-4+0.0405*D-(2.8*10^-6) *D*D

	if (sMonitorFlag.bHumidityFlag)
	{
		unsigned short l = CTool::doGetIntegerData(m_ucRcvData + 4, 2);
		//pMonitorData->fHumidity = (float) (-4 + 0.0405 * l - 2.8 * pow(10.0,-6) * l * l);
		l = l * 16 + 2;
		pMonitorData->fHumidity = (float)(-6 + 125 * l / (pow((float)2,16)));

		if (pMonitorData->fHumidity >100 )
			pMonitorData->fHumidity = 100.0;
		else if (pMonitorData->fHumidity < 0)
			pMonitorData->fHumidity = 0.0;
	}


	//FOG_VALUE	1	0x00	������澯 0x01	������澯
	if (sMonitorFlag.bSmogFlag)
		pMonitorData->bSmog = m_ucRcvData[6] == 0x00 ? false : true;

	//LUMINANCE_VALUE	1	0-255	���ȴ������ɼ�������ֵ
	if (sMonitorFlag.bBrightFlag)
	{
		pMonitorData->nBrightness = m_ucRcvData[7];
		pMonitorData->nBrightness = m_ucRcvData[7];
		pMonitorData->nBrightness2=m_ucRcvData[11];
		pMonitorData->nBrightness = (int)(pMonitorData->nBrightness / 2.55);
		pMonitorData->nBrightness2=(int)(pMonitorData->nBrightness2 / 2.55);;
		if (pMonitorData->nBrightness > 100 )
		{pMonitorData->nBrightness = 100;

		}
		if (pMonitorData->nBrightness2 > 100 )
		{
			pMonitorData->nBrightness2 = 100;
		}
		if (pMonitorData->nBrightness < 0)
		{
			pMonitorData->nBrightness = 0;
		}
		if (pMonitorData->nBrightness2 <0  )
		{
			pMonitorData->nBrightness2 = 0;
		}
	}
	//�̵���״̬	1	������λ1��ʾ�̵������ϣ�0��ʾ�Ͽ���
	//��00001001����ʾ��һ·�͵���·�̵������ϣ������Ͽ�

	//2012-10-22 ��Դ״̬ modify by sunj ��ȡ��Դ״̬
	c = m_ucRcvData[8];

	pMonitorData->nPowerState[0]= c & 0x01;
	pMonitorData->bWorkState[0] = c & 0x01;

	c = c >> 1;

	pMonitorData->nPowerState[1]  = c & 0x01;
	pMonitorData->bWorkState[1] = c & 0x01;

	c = c >> 1;
	pMonitorData->bWorkState[2] = c & 0x01;

	c = c >> 1;
	pMonitorData->bWorkState[3] = c & 0x01;


	//����FAN_L״̬	1	0x00	δ���  0x01��0x02	��������  0x03	�쳣�ر�
	//0x04	�����ر�	0x05	�쳣����
	//����FAN_R״̬	1	0x00	δ���  0x01��0x02	��������  0x03	�쳣�ر�
	//0x04	�����ر�	0x05	�쳣����
	if (sMonitorFlag.bFanStateFlag[0])
		pMonitorData->nFanState[0] = m_ucRcvData[9];

	if(sMonitorFlag.bFanStateFlag[1])
		pMonitorData->nFanState[1] = m_ucRcvData[10];

	//VER_DATA	2	0x00	��һ�ֽڵ���λ��ʾ�汾�Ÿ�λ�����λ��1����ʾboot��Ϊ��0����ʾapp����,�ڶ��ֽڱ�ʾ�汾�ŵͰ�λ
	//�汾���壺����汾��Ϊ8107��ʾV1.07boot�汾��020F��ʾV2.15app�汾
	pMonitorData->version.ucTper = m_ucRcvData[13] >> 7;//1-boot��0-app
	pMonitorData->version.dVersion =(double) ((m_ucRcvData[13] & 0x7F) + (double) m_ucRcvData[14] / 100.0);

}
//��ȡ���壨ɨ�迨����Դ��ѹ�������
void CCLPackCommunicationData::GetVoltageMonitorData(LPMONITORDATA pMonitorData)
{
	//0~255��	��1-5·��Դ��ѹ
	//Ҫ��ʾ�ĵ�ѹֵ =  (��Ƶ�ѹ*3.295/256)*2����ʾ��ȷ��0.01����λ��V
	for (int i = 0; i < POWER_VOL_NUM; ++i)
	{
		pMonitorData->fPowerVol[i] = (float)(2 * m_ucRcvData[1 + i] * 3.295 / 256.0);
	}
}
//��ȡ���壨ɨ�迨�����ʼ������
void CCLPackCommunicationData::GetPowerMonitorData(LPMONITORDATA pMonitorData)
{
	//����
	//�������ݳ���10000���õ�һ���ٷֱ�����Ȼ���ٳ���1250����Ϊ��ǰ�������幦�ʡ�
	double dCapacity =(double) CTool::doGetIntegerData(m_ucRcvData + 1, 2);
	pMonitorData->nCapacityFactor = (int) ((dCapacity / 10000.0) * 1250);
}
//��ȡ���壨ɨ�迨�����������
void CCLPackCommunicationData::GetPointDetectMonitorData(LPMONITORDATA pMonitorData,ScanCardAttachment & sScanCardAtt)
{
	CStructSingleScanCard  & scancard = sScanCardAtt.scancard;

	RECT rt = sScanCardAtt.rtRect;

	LinkTable & ZoneLink = sScanCardAtt.sSectionlinktable;

	//ACK_DATA1	1	0x00-0x54	����Ƹ�����ÿ���������ݰ�����ϴ�0x04���Ƶĳ���״̬,
	pMonitorData->nErrorPointNum = (int) m_ucRcvData[1];

	//ACK_DATA2	1	0x00-0xFF	DCLK����״̬����ӦbitλΪ1��ʾ��ӦDCLK����
	//ACK_DATA3	1	0x00	����
	for (int m = 0; m < SCAN_LINE_STATE_NUM; ++ m)
	{
		if (m < 8)
		{
			pMonitorData->nDClkState[m] = (m_ucRcvData[2] >> m) & 0x01;			//DCLK״̬
		}
		else
		{
			pMonitorData->nDClkState[m] = (m_ucRcvData[3] >> (m - 8)) & 0x01;			//DCLK״̬
		}
		pMonitorData->nScanLineState[m] = pMonitorData->nDClkState[m];		//ɨ����״̬
	}

	// ע�Ͳ������ڴ�׮����
	for (int n = 0; n < pMonitorData->nErrorPointNum; n ++)
	//for (int n = 0; n < 5; n ++)
	{
		//����LED����������X����
		pMonitorData->sErrorPoint[n].nX = m_ucRcvData[4 + n * 3];
		//����LED����������Y����
		pMonitorData->sErrorPoint[n].nY = m_ucRcvData[5 + n * 3] + ((m_ucRcvData[6 + n * 3] >> 4) << 8);


// 		//����LED����������X����
// 		pMonitorData->sErrorPoint[n].nX = 0x05;
// 		//����LED����������Y����
// 		pMonitorData->sErrorPoint[n].nY = 0x50 + n;


		//2013-10-18 caixl ����� ���������ұ�ת�����ص��λ�ã�
		//old:HUB�����ĳ���ư�Ŀ��Ϊ�����߶�Ϊɨ�迨������������Xÿ�����������������У���ַΪ0,1,2,3,...,n��
		//new:HUB�����ĳ���ư�Ŀ��Ϊ�������߶�Ϊɨ�迨������������Xÿ�����������������У���ַΪ,1,2,3,...,n��
		//����ַ��Ϊ�����ұ�������ַ������СΪ������Xÿ��������
		//���������ص�ת��Ϊ�������λ�ã��ٸ���������Կ���λ��ת��Ϊ�������λ��


		//Ĭ��Ϊ���ҵ���0x1	0x0	��������	0x1	��������	0x2	��������	0x3	��������
		if ( 0 == scancard.nDataInputDir || 1 == scancard.nDataInputDir )//	0x0	��������	0x1	��������
		{
			//�������ַ�������ұ������
			int nZoneAddr = pMonitorData->sErrorPoint[n].nY / scancard.nScanCardSectionRowNumber;

			//ͨ�������ַת��Ϊsdram��ַ���Ҳ��ұ��ID
			int nZoneID = 0;
			for(int i = 0; i < ZoneLink.nLen; ++i)
			{
				if(ZoneLink.ucLinkTable[i] == nZoneAddr)
				{
					nZoneID = i;
					break;
				}
			}

			//�����������λ�ã�X���仯��Y��ÿ����������
			pMonitorData->sErrorPoint[n].nY = pMonitorData->sErrorPoint[n].nY % scancard.nScanCardSectionRowNumber;

			/* old:
			//������ڿ�����λ��
			int nColID = nZoneID % scancard.nSectionHorNum;

			//������ڿ�����λ��
			int nRowID = nZoneID / scancard.nSectionHorNum;

			//��������ڿ���λ��
			pMonitorData->sErrorPoint[n].nX += nColID * scancard.nSectionWidth;
			pMonitorData->sErrorPoint[n].nY += nRowID * scancard.nScanCardSectionRowNumber;
			*/

			// new:---------------begin----------------------
			//�����ڿ�������ڿ�����λ�ã���0��ʼ��
			int nCardColID = nZoneID % (scancard.nSectionHorNum * scancard.nCard_zone_Num); //3

			//�����ڿ�������ڿ�����λ�ã���0��ʼ��
			int nCardRowID = nZoneID / (scancard.nSectionHorNum * scancard.nCard_zone_Num); //1

			//�����ڿ����еڼ�����������
			int nModSectionID = pMonitorData->sErrorPoint[n].nX / scancard.nSectionWidth;//0

			//���ڿ��������ں�������Xƫ��λ��
			int nModeSectionOffset = pMonitorData->sErrorPoint[n].nX % scancard.nSectionWidth;

			//���ں���ڼ�����������
			//int nHorCardSectId = nCardColID / scancard.nSectionHorNum;

			//����ģ���ϵĵ�һ��ģ������
			//int nModSectID = nCardColID % scancard.nSectionHorNum;

			//��������ڿ���λ��
			//��ǰģ��֮ǰ�Ŀ������ĵ��� +  ��ǰģ��֮ǰ��ģ������ + ���ڵڼ���ģ�������Xƫ��λ��
			// 		pMonitorData->sErrorPoint[n].nX = nHorCardSectId * nHorCardSectId * scancard.nCard_zone_width * scancard.nSectionHorNum
			// 			+ nModSectionID * scancard.nModuleWidth
			// 			+ nModSectID * scancard.nSectionWidth + nModeSectionOffset;

			pMonitorData->sErrorPoint[n].nX = nCardColID * scancard.nSectionWidth //��
				+ nModSectionID * scancard.nModuleWidth //����
				+ nModeSectionOffset; //���ص���ģ��index
			pMonitorData->sErrorPoint[n].nY += nCardRowID * scancard.nScanCardSectionRowNumber;
			//---------------end----------------------


			//2013.7.4 caixl �����������ɨ�迨��λ��ת������������
			pMonitorData->sErrorPoint[n].nX += (short)rt.left;
			pMonitorData->sErrorPoint[n].nY += (short)rt.top;
		}
		else if (2 == scancard.nDataInputDir || 3 == scancard.nDataInputDir)//0x2	��������	0x3	��������
		{
			;//B50����Ҫ��ת������
		}


		//����LED����ɫָʾ
		//����λbit2��bit1��bit0�ֱ�����ָʾ�졢�̡���������ɫ�Ƶ�״̬��
		//��ӦλΪ'0'��ʾ��ӦLED�ƿ�·��'1'��ʾ����������
		//��Ʊ�ʶ
		pMonitorData->sErrorPoint[n].bRedFlag = (m_ucRcvData[6 + n * 3] >> 2) & 0x01 ? false : true;
		//�̵Ʊ�ʶ
		pMonitorData->sErrorPoint[n].bGreenFlag = (m_ucRcvData[6 + n * 3] >> 1) & 0x01 ? false : true;
		//���Ʊ�ʶ
		pMonitorData->sErrorPoint[n].bBlueFlag = (m_ucRcvData[6 + n * 3]) & 0x01 ? false : true;
	}
	if (m_ucErrCode == 0xAA)
	{
		pMonitorData->bDotDetectErrorFlag = 2;
	}
	else
	{
		pMonitorData->bDotDetectErrorFlag = -1;
	}
}
void CCLPackCommunicationData::GetNetWebErrorPackage(LPMONITORDATA pMonitorData)
{
	//ACK_DATA10-ACK_DATA11�����ع��ϼ�����
	unsigned short nError = CTool::doGetIntegerData(m_ucRcvData + 10, 2);

	if (nError < 0 || nError > 65535)
	{
		nError = 0;
	}
	pMonitorData->nNetWebErrorNum = nError;
	if (m_ucErrCode == 0xAA)
	{
		pMonitorData->nNetWebErrorPackageFlag = 2;
	}
	else
	{
		pMonitorData->nNetWebErrorPackageFlag = -1;
	}
}


/*
function:GetSendCardTemperaturAndHumityStatus(ATLVCAK6Status &sAtlvcStatus)
discription: ��0x1A�� ��ȡ��ʪ��
create : 2012-10-19
input:
output:sAtlvcStatus -�ɼ���״̬
author:sunj
history:
return:
other:
*/
void CCLPackCommunicationData::GetSendCardTemperaturAndHumityStatus(ATLVCAK6Status &sAtlvcStatus)
{
	unsigned short l = CTool::doGetIntegerData(m_ucRcvData + 1, 2);
	sAtlvcStatus.fTemperature = (float)(0.01 * l - 40);

	if (sAtlvcStatus.fTemperature  > 123)
	{
		sAtlvcStatus.fTemperature  = 123.0;
	}
	else if ( sAtlvcStatus.fTemperature  < -40)
	{
		sAtlvcStatus.fTemperature  = -40.0;
	}

	l = CTool::doGetIntegerData(m_ucRcvData + 3, 2);
	sAtlvcStatus.fHumidity = (float) (-4 + 0.0405 * l - 2.8 * pow(10.0,-6) * l * l);

	if (sAtlvcStatus.fHumidity >100 )
	{
		sAtlvcStatus.fHumidity = 100.0;
	}
	else if (sAtlvcStatus.fHumidity < 0)
	{
		sAtlvcStatus.fHumidity = 0.0;
	}
}
