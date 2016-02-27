#pragma once

#include "../../Datastruct/DataStructDef.h"

class CCLPackCommunicationData
{
public:
	CCLPackCommunicationData(void);
	~CCLPackCommunicationData(void);

	//通信数据打包的基本格式。ucData == null时数据域为0，返回 int - 包长度
	static int PackDataBase(unsigned int ucDestSendCardAddress, //级联采集卡地址 （000~111：对应1号~8号采集卡地址）
							int ucDestAddress,//目标地址
							unsigned char ucPackType,//包类型
							unsigned int unSequenceNumber,//顺序号
							int nAnsweredFlag,//应答标示
							unsigned char *ucData,//数据域字节
							unsigned char * ucSendData//打包发送数据
							);//是否为FF广播包
	//V1.0
	//通信数据打包的基本格式。ucData == null时数据域为0，返回 int - 包长度
	static int PackDataBaseV1(unsigned int ucDestSendCardAddress, //级联采集卡地址 （000~111：对应1号~8号采集卡地址）
		int ucDestAddress,//目标地址
		unsigned char ucPackType,//包类型
		unsigned int unSequenceNumber,//顺序号
		int nAnsweredFlag,//应答标示
		unsigned char *ucData,//数据域字节
		unsigned char * ucSendData
	);//打包发送数据

	//V1.0
	//通信数据打包的基本格式。ucData == null时数据域为0，返回 int - 包长度
	static int PackDataBaseV2(unsigned int ucDestSendCardAddress, //级联采集卡地址 （000~111：对应1号~8号采集卡地址）
		int ucDestAddress,//目标地址
		unsigned char ucPackType,//包类型
		unsigned int unSequenceNumber,//顺序号
		int nAnsweredFlag,//应答标示
		unsigned char *ucData,//数据域字节
		unsigned char * ucSendData,
		short nFlag //标示，1为网络卡命令，0为扫描卡或者其他命令
		);//打包发送数据

	//生成数据包校验码，高位在前，低位在后。
	static void CreateChecksum(unsigned char* ucSenddata,//发送数据
									int nLen, //有效数据长度
									unsigned char ucChecksum[1]);//校验码

	bool GetAtiecVerson(ATIEC& atiec);
	//解应答包
	BOOL UnPackData(unsigned char *pRcvData, int nLen);

	BOOL UnPackDataV1(unsigned char *pRcvData, int nLen);
	//解应答包错误类型
	bool UnPackErrorType();

	//解包模组序列号
	void GetModSeriNum(string & sModSerial, bool bAnswFlag = true);

	//得到采集卡状态
	void GetSendCardStatus(ATLVCAK6Status & sAtlvcStatus);

	//获取发送卡版本
	void GetSendCardVersion(Version & sVersion);

	//获取模组的运行时间
	void GetModuleRuntime(string & sModuleRuntime);
	void GetModuleRuntime(long & sModuleRuntime);

	//获取常规(ATIEC或箱体单个扫描卡)监测数据
	void GetCustomMonitorData(LPMONITORDATA pMonitorData);
	//获取箱体（扫描卡）电源电压监测数据
	void GetVoltageMonitorData(LPMONITORDATA pMonitorData);
	//获取箱体（扫描卡）功率监测数据
	void GetPowerMonitorData(LPMONITORDATA pMonitorData);
	//获取箱体（扫描卡）逐点检测数据
	void GetPointDetectMonitorData(LPMONITORDATA pMonitorData,ScanCardAttachment & sScanCardAtt);
	void GetMonitorDataFunRate(LPMONITORDATA pMonitorData);
	void GetNetWebErrorPackage(LPMONITORDATA pMonitorData);

	void GetSendCardTemperaturAndHumityStatus(ATLVCAK6Status &sAtlvcStatus);
	void GetSendCardLightAdjustStatus(ATLVCAK6Status & sAtlvcStatus);
	void GetMonitorDataPowerState(LPMONITORDATA pMonitorData);
public:
	//源地址
	unsigned short m_ucSourAddress;
	unsigned short m_ucDestSendCardAddress;
	//目的地址
	unsigned short m_unDestAddress;
	//包类型	1字节	具体见"包类型结构定义"
	unsigned char m_ucPackType;
	//数据包长度	1字节/0字节	应答返回值的长度（0字节表示无应答返回值，1字节表示监控指令等有应答返回值）
	short	m_nLen;
	//数据域内容
	unsigned char m_ucRcvData[CL_SEND_EFFECT_DATA_SIZE];
	//是否正确应答
	bool m_bACKOK;
	//应答错误描述
	unsigned char m_ucErrCode;
	//顺序号
	int m_nSequenceNumber;
	//应答标示
	int m_nAnswered;

};

