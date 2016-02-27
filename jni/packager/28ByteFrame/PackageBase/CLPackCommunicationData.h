#pragma once

#include "../../Datastruct/DataStructDef.h"

class CCLPackCommunicationData
{
public:
	CCLPackCommunicationData(void);
	~CCLPackCommunicationData(void);

	//ͨ�����ݴ���Ļ�����ʽ��ucData == nullʱ������Ϊ0������ int - ������
	static int PackDataBase(unsigned int ucDestSendCardAddress, //�����ɼ�����ַ ��000~111����Ӧ1��~8�Ųɼ�����ַ��
							int ucDestAddress,//Ŀ���ַ
							unsigned char ucPackType,//������
							unsigned int unSequenceNumber,//˳���
							int nAnsweredFlag,//Ӧ���ʾ
							unsigned char *ucData,//�������ֽ�
							unsigned char * ucSendData//�����������
							);//�Ƿ�ΪFF�㲥��
	//V1.0
	//ͨ�����ݴ���Ļ�����ʽ��ucData == nullʱ������Ϊ0������ int - ������
	static int PackDataBaseV1(unsigned int ucDestSendCardAddress, //�����ɼ�����ַ ��000~111����Ӧ1��~8�Ųɼ�����ַ��
		int ucDestAddress,//Ŀ���ַ
		unsigned char ucPackType,//������
		unsigned int unSequenceNumber,//˳���
		int nAnsweredFlag,//Ӧ���ʾ
		unsigned char *ucData,//�������ֽ�
		unsigned char * ucSendData
	);//�����������

	//V1.0
	//ͨ�����ݴ���Ļ�����ʽ��ucData == nullʱ������Ϊ0������ int - ������
	static int PackDataBaseV2(unsigned int ucDestSendCardAddress, //�����ɼ�����ַ ��000~111����Ӧ1��~8�Ųɼ�����ַ��
		int ucDestAddress,//Ŀ���ַ
		unsigned char ucPackType,//������
		unsigned int unSequenceNumber,//˳���
		int nAnsweredFlag,//Ӧ���ʾ
		unsigned char *ucData,//�������ֽ�
		unsigned char * ucSendData,
		short nFlag //��ʾ��1Ϊ���翨���0Ϊɨ�迨������������
		);//�����������

	//�������ݰ�У���룬��λ��ǰ����λ�ں�
	static void CreateChecksum(unsigned char* ucSenddata,//��������
									int nLen, //��Ч���ݳ���
									unsigned char ucChecksum[1]);//У����

	bool GetAtiecVerson(ATIEC& atiec);
	//��Ӧ���
	BOOL UnPackData(unsigned char *pRcvData, int nLen);

	BOOL UnPackDataV1(unsigned char *pRcvData, int nLen);
	//��Ӧ�����������
	bool UnPackErrorType();

	//���ģ�����к�
	void GetModSeriNum(string & sModSerial, bool bAnswFlag = true);

	//�õ��ɼ���״̬
	void GetSendCardStatus(ATLVCAK6Status & sAtlvcStatus);

	//��ȡ���Ϳ��汾
	void GetSendCardVersion(Version & sVersion);

	//��ȡģ�������ʱ��
	void GetModuleRuntime(string & sModuleRuntime);
	void GetModuleRuntime(long & sModuleRuntime);

	//��ȡ����(ATIEC�����嵥��ɨ�迨)�������
	void GetCustomMonitorData(LPMONITORDATA pMonitorData);
	//��ȡ���壨ɨ�迨����Դ��ѹ�������
	void GetVoltageMonitorData(LPMONITORDATA pMonitorData);
	//��ȡ���壨ɨ�迨�����ʼ������
	void GetPowerMonitorData(LPMONITORDATA pMonitorData);
	//��ȡ���壨ɨ�迨�����������
	void GetPointDetectMonitorData(LPMONITORDATA pMonitorData,ScanCardAttachment & sScanCardAtt);
	void GetMonitorDataFunRate(LPMONITORDATA pMonitorData);
	void GetNetWebErrorPackage(LPMONITORDATA pMonitorData);

	void GetSendCardTemperaturAndHumityStatus(ATLVCAK6Status &sAtlvcStatus);
	void GetSendCardLightAdjustStatus(ATLVCAK6Status & sAtlvcStatus);
	void GetMonitorDataPowerState(LPMONITORDATA pMonitorData);
public:
	//Դ��ַ
	unsigned short m_ucSourAddress;
	unsigned short m_ucDestSendCardAddress;
	//Ŀ�ĵ�ַ
	unsigned short m_unDestAddress;
	//������	1�ֽ�	�����"�����ͽṹ����"
	unsigned char m_ucPackType;
	//���ݰ�����	1�ֽ�/0�ֽ�	Ӧ�𷵻�ֵ�ĳ��ȣ�0�ֽڱ�ʾ��Ӧ�𷵻�ֵ��1�ֽڱ�ʾ���ָ�����Ӧ�𷵻�ֵ��
	short	m_nLen;
	//����������
	unsigned char m_ucRcvData[CL_SEND_EFFECT_DATA_SIZE];
	//�Ƿ���ȷӦ��
	bool m_bACKOK;
	//Ӧ���������
	unsigned char m_ucErrCode;
	//˳���
	int m_nSequenceNumber;
	//Ӧ���ʾ
	int m_nAnswered;

};

