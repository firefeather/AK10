
#include "CLPackCommunicationData.h"
#include "../../../util/util.h"
#include "math.h"
#include <android/log.h>

extern int globe_CardVesion;
extern GLOBALVARIABLE	  g_GlobalVariable;				//工程的全局变量引用


CCLPackCommunicationData::CCLPackCommunicationData(void)
{
	//memset(this, 0, sizeof(CCLPackCommunicationData));
}

CCLPackCommunicationData::~CCLPackCommunicationData(void)
{
}
//通信数据打包的基本格式。ucData == null时数据域为0，返回 int - 包长度
int CCLPackCommunicationData::PackDataBase(unsigned int ucDestSendCardAddress, //级联采集卡地址 （000~111：对应1号~8号采集卡地址）
										   int ucDestAddress,//目标地址
										   unsigned char ucPackType,//包类型
										   unsigned int unSequenceNumber,//顺序号
										   int nAnsweredFlag,//应答标示
										   unsigned char *ucData,//数据域字节
										   unsigned char * ucSendData//打包发送数据
										   )
{
	//如果不是采集卡的地址
	if (ucPackType>=0x40)//扫描卡命令包
	{
        if (ucDestAddress>248&&ucDestAddress>0)
        {
			globe_CardVesion=1;
        }

		if (ucDestAddress<0)//智能编址包，广播超过248地址的时候，用新协议，其他广播包用旧协议
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
		if (-2 == ucDestAddress)//扩协议广播
		{
			globe_CardVesion=1;
		}
	}
	else
	{
         globe_CardVesion=0;
	}

	/*
    if(globe_CardVesion==1)//版本号为1的
    {
        //如果是针对采集卡操作的，不用新协议
		return PackDataBaseV1(ucDestSendCardAddress,ucDestAddress,ucPackType,unSequenceNumber,nAnsweredFlag,ucData,ucSendData);
    }
    */
	const unsigned char sPackagehead[2] = {0X55,0x55};//数据帧起始标志
	const unsigned char sPackageHeadFree[2] = {0X12,0x34};//数据帧保留字节

	int nLen = 0;

	//1 包头1  2  固定为：0x55 0x55
	memcpy(ucSendData, sPackagehead, 2);
	//2 包头2 2 暂定为 0x12 0x34
	memcpy(ucSendData + 2,sPackageHeadFree, 2);
	//3 目的地址   1
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

	//4 包类型  1
	ucSendData[5] = ucPackType;

	//5 源地址   1
	ucSendData[6] = 0;
	//6 顺序号    2
	CTool::ExchangeInteger(unSequenceNumber, ucSendData + 7, 2);
	//7 应答标识 0X00-无应答，0x01-有应答，0x02-表示有数据无77的应答；若出错，则应答标志位为1，数据为AA。
	ucSendData[9] = nAnsweredFlag;
	//8 保留字节  1   填充0x00，无意义
	ucSendData[10] = ucDestSendCardAddress ;
	nLen = 11;
	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后
	if(ucData)
		memcpy(ucSendData + nLen, ucData, CL_SEND_EFFECT_DATA_SIZE);
	else
		memset(ucSendData + nLen, 0, CL_SEND_EFFECT_DATA_SIZE);
	nLen += 16;
	//10	校验码（CHECKSUM）	2	Checksum = 除包头和校验码外所有字节(23)异或。
	//高位在前，低位在后。
	CreateChecksum(ucSendData + 4, nLen - 4, ucSendData + nLen);
	nLen++;
	return (nLen);
}
///hh modify///////////////////////////////////////////////////////////////////////
int CCLPackCommunicationData::PackDataBaseV1(unsigned int ucDestSendCardAddress, //级联采集卡地址 （000~111：对应1号~8号采集卡地址）
										   int ucDestAddress,//目标地址
										   unsigned char ucPackType,//包类型
										   unsigned int unSequenceNumber,//顺序号
										   int nAnsweredFlag,//应答标示
										   unsigned char *ucData,//数据域字节
										   unsigned char * ucSendData
										  )//打包发送数据
{

	const unsigned char sPackagehead[2] = {0X55,0x55};//数据帧起始标志
	const unsigned char sPackageHeadFree[2] = {0X12,0x56};//数据帧保留字节


	int nLen = 0;

	//1 包头1  2  固定为：0x55 0x55
	memcpy(ucSendData, sPackagehead, 2);
	//2 包头2 2 暂定为 0x12 0x56
	memcpy(ucSendData + 2,sPackageHeadFree, 2);

	//保留字节
    ucSendData[4] = 0x00;
	ucSendData[5] = 0x00;
	if ( 0x1C == unSequenceNumber )//1C代表网络卡命令
	{
		ucSendData[5] = 0x01;
	}

	//3 目的地址   1
	//如果是采集卡或者hub板的地址0x01~0xf8 加256

	if (ucDestAddress<0)//发送广播包
	{
		  ucSendData[6]=0xff;
		  ucSendData[7]=0xff;
	}
	else
	{
		 if ( _SSeriesVersion == g_GlobalVariable.nVersionType )
		 {
			 //S系列发送箱体坐标二维地址
			 char nNetcardNo = (ucDestAddress & 0xFF00) >> 8;
			 char nScanCardId = ucDestAddress & 0x00FF;
			 ucSendData[6] = nNetcardNo;//高8位
			 ucSendData[7] = nScanCardId;//低8位
		 }
		 else
		 {
			 ucDestAddress += 0x08;
			 ucSendData[6] = (ucDestAddress&0xff00)>>8;//高8位
			 ucSendData[7] = (ucDestAddress&0x00ff);//低8位
		 }
	}

	//4 包类型  1
	ucSendData[8] = ucPackType;
	//5 源地址   2
	ucSendData[9] = 0x00;
	ucSendData[10] = 0x00;
	//6 顺序号    2
	CTool::ExchangeInteger(unSequenceNumber, ucSendData + 11, 2);
	//7 应答标识 0X00-无应答，0x01-有应答，0x02-表示有数据无77的应答；若出错，则应答标志位为1，数据为AA。
	ucSendData[13] = nAnsweredFlag;
	//8 保留字节  1   填充0x00，无意义
	ucSendData[14] = ucDestSendCardAddress ;
	nLen = 15;
	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后

	if ( _SSeriesVersion != g_GlobalVariable.nVersionType )
	{
		if (ucPackType==0x40&&unSequenceNumber==0x0b)
		{
			unsigned int nStart=*ucData+*(ucData+1)*0x100;
			if (nStart>248)
			{
				//只能编址+8
				nStart+=0x08;
				*ucData = (nStart&0x00ff);//低8位
				*(ucData+1) = (nStart&0xff00)>>8;
			}
		}
	}

	if(ucData)
		memcpy(ucSendData + nLen, ucData, CL_SEND_EFFECT_DATA_SIZE);
	else
		memset(ucSendData + nLen, 0, CL_SEND_EFFECT_DATA_SIZE);


	nLen += 16;
	//10	校验码（CHECKSUM）	2	Checksum = 除包头和校验码外所有字节(23)异或。
	//高位在前，低位在后。
	CreateChecksum(ucSendData + 4, nLen - 4, ucSendData + nLen);
	nLen++;
	return (nLen);
}

int CCLPackCommunicationData::PackDataBaseV2(unsigned int ucDestSendCardAddress, //级联采集卡地址 （000~111：对应1号~8号采集卡地址）
											 int ucDestAddress,//目标地址
											 unsigned char ucPackType,//包类型
											 unsigned int unSequenceNumber,//顺序号
											 int nAnsweredFlag,//应答标示
											 unsigned char *ucData,//数据域字节
											 unsigned char * ucSendData,
											 short nFlag)//打包发送数据
{
	const unsigned char sPackagehead[2] = {0X55,0x55};//数据帧起始标志
	const unsigned char sPackageHeadFree[2] = {0X12,0x56};//数据帧保留字节

	int nLen = 0;

	//1 包头1  2  固定为：0x55 0x55
	memcpy(ucSendData, sPackagehead, 2);
	//2 包头2 2 暂定为 0x12 0x56
	memcpy(ucSendData + 2,sPackageHeadFree, 2);

	//保留字节
	//1C代表网络卡命令，1D时候，为了区分网络卡命令，用ucDestSendCardAddress=200传作为标志，为其他时表示射灯
	ucSendData[4] = 0x00;
	ucSendData[5] = 0x00;
	if ( 0x1C == unSequenceNumber || (0x1D == unSequenceNumber && 1 == nFlag) )
	{
		ucSendData[5] = 0x01;
	}

	//3 目的地址   1
	//如果是采集卡或者hub板的地址0x01~0xf8 加256

	if (ucDestAddress<0)//发送广播包
	{
		ucSendData[6]=0xff;
		ucSendData[7]=0xff;
	}
	else
	{
		if ( _SSeriesVersion == g_GlobalVariable.nVersionType )
		{
			//S系列发送箱体坐标二维地址
			char nNetcardNo = (ucDestAddress & 0xFF00) >> 8;
			char nScanCardId = ucDestAddress & 0x00FF;
			ucSendData[6] = nNetcardNo;//高8位
			ucSendData[7] = nScanCardId;//低8位
		}
		else
		{
			ucDestAddress += 0x08;
			ucSendData[6] = (ucDestAddress&0xff00)>>8;//高8位
			ucSendData[7] = (ucDestAddress&0x00ff);//低8位
		}
	}

	//4 包类型  1
	ucSendData[8] = ucPackType;
	//5 源地址   2
	ucSendData[9] = 0x00;
	ucSendData[10] = 0x00;
	//6 顺序号    2
	CTool::ExchangeInteger(unSequenceNumber, ucSendData + 11, 2);
	//7 应答标识 0X00-无应答，0x01-有应答，0x02-表示有数据无77的应答；若出错，则应答标志位为1，数据为AA。
	ucSendData[13] = nAnsweredFlag;
	//8 保留字节  1   填充0x00，无意义
	ucSendData[14] = ucDestSendCardAddress ;
	nLen = 15;
	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后

	if ( _SSeriesVersion != g_GlobalVariable.nVersionType )
	{
		if (ucPackType==0x40&&unSequenceNumber==0x0b)
		{
			unsigned int nStart=*ucData+*(ucData+1)*0x100;
			if (nStart>248)
			{
				//只能编址+8
				nStart+=0x08;
				*ucData = (nStart&0x00ff);//低8位
				*(ucData+1) = (nStart&0xff00)>>8;
			}
		}
	}

	if(ucData)
		memcpy(ucSendData + nLen, ucData, CL_SEND_EFFECT_DATA_SIZE);
	else
		memset(ucSendData + nLen, 0, CL_SEND_EFFECT_DATA_SIZE);


	nLen += 16;
	//10	校验码（CHECKSUM）	2	Checksum = 除包头和校验码外所有字节(23)异或。
	//高位在前，低位在后。
	CreateChecksum(ucSendData + 4, nLen - 4, ucSendData + nLen);
	nLen++;
	return (nLen);
}
//////////////////////////////////////////////////////////////////////////

//生成数据包校验码，高位在前，低位在后。
void CCLPackCommunicationData::CreateChecksum(unsigned char* ucSendData,//发送数据
										 int nLen, //有效数据长度
										 unsigned char ucChecksum[1])//校验码
{
	unsigned short nData = ucSendData[0];
	for(int n = 1; n < nLen; n++)
		nData ^= ucSendData[n];
	CTool::ExchangeInteger((unsigned char) nData, ucChecksum, 1);
}
bool CCLPackCommunicationData::GetAtiecVerson(ATIEC& atiec)
{//zhangjj004
	atiec.ATIEC_EStatus.dVersion = (double)(m_ucRcvData[2] + (double) m_ucRcvData[3] / 100);
	//备份状态
	if (m_ucRcvData[4] & 0x01)
		atiec.ATIEC_EStatus.bBackup = true;
	else
		atiec.ATIEC_EStatus.bBackup = false;
	if (m_ucRcvData[4] & 0x02)
		atiec.ATIEC_EStatus.bBackupP2P3 = true;
	else
		atiec.ATIEC_EStatus.bBackupP2P3 = false;
	//Bit1	锁屏状态。 1：锁屏。0：不锁屏
	if (m_ucRcvData[4] & 0x03)
		atiec.ATIEC_EStatus.bLockScreen = true;
	else
		atiec.ATIEC_EStatus.bLockScreen = false;
	//Bit2	上电参数初始化状态。1：成功。0：失败

	if (m_ucRcvData[4] & 0x04)
		atiec.ATIEC_EStatus.bInit = true;
	else
		atiec.ATIEC_EStatus.bInit = false;
	//Bit3	P0连接状态，1，有连接，0，无连接
	//Bit4	P1连接状态，1，有连接，0，无连接
	atiec.ATIEC_EStatus.nP0LinkState = (m_ucRcvData[4] >> 4) & 0x01 ;
	atiec.ATIEC_EStatus.nP1LinkState = (m_ucRcvData[4] >> 5) & 0x01 ;
	atiec.ATIEC_EStatus.nP2LinkState = (m_ucRcvData[4] >> 6) & 0x01 ;
	atiec.ATIEC_EStatus.nP3LinkState = (m_ucRcvData[4] >> 7) & 0x01 ;
	//pMonitorData->bReadMonitorSendCardFlag = -1;
	return true;
}
//解应答包
BOOL CCLPackCommunicationData::UnPackData(unsigned char *pRcvData, int nLen)
{
	const unsigned char sPackagehead[2] = {0X55,0x55};//数据帧起始标志
	const unsigned char sPackageHeadFree[2] = {0X12,0x34};//数据帧保留字节
	const unsigned char sPackageHeadFreeV1[2] = {0X12,0x56};//数据帧保留字节

	//包头1
	if (memcmp(pRcvData,sPackagehead, 2) != 0)
		return CL_ANSWER_ERROE_PACK_HEAD_1;
	//包头2
    if (memcmp(pRcvData + 2, sPackageHeadFreeV1, 2) != 0&&memcmp(pRcvData + 2, sPackageHeadFree, 2) != 0)
	{
        return CL_ANSWER_ERROE_PACK_HEAD_2;
	}
	else if (memcmp(pRcvData + 2, sPackageHeadFreeV1, 2) == 0)
	{
		nLen=CL_SEND_PACK_SIZE_V1;
		return UnPackDataV1(pRcvData,nLen);
	}

	//目的地址(PC)
	if (pRcvData[4] != 0x00)
		return CL_ANSWER_ERROE_DEST_ADDRESS;
	//源地址
	if(pRcvData[6] == 0x00)
		return CL_ANSWER_ERROE_SOUR_ADDRESS;

	//校验数据包是否合法
	unsigned char ucChecksum[1];
	memset(ucChecksum, 0, sizeof(ucChecksum));
	CreateChecksum(pRcvData + 4, nLen - 5, ucChecksum);

	if (pRcvData[nLen-1] != ucChecksum[0])
		return CL_ANSWER_ERROE_CHECKSUM;

	m_ucDestSendCardAddress = pRcvData[10];//级联采集卡地址
	m_ucSourAddress = pRcvData[6];//源地址
	m_unDestAddress = pRcvData[4];//目的地址(PC)
	m_ucPackType = pRcvData[5];//包类型
	m_nSequenceNumber = CTool::doGetIntegerData(pRcvData+ 7 , 2);//序列号
	memset(m_ucRcvData, 0, sizeof(m_ucRcvData));

	m_nLen = nLen - 11;
	memcpy(m_ucRcvData, pRcvData + 11, nLen - 12);//数据域内容
	bool bAnsOK = UnPackErrorType();
	if(bAnsOK)
		return 1;
	else
		return CL_ANSWER_ERROE_CONNECT;
}
//新协议解包
BOOL CCLPackCommunicationData::UnPackDataV1(unsigned char *pRcvData, int nLen)
{
	const unsigned char sPackagehead[2] = {0X55,0x55};//数据帧起始标志
	const unsigned char sPackageHeadFree[2] = {0X12,0x56};//数据帧保留字节

	//包头1
	if (memcmp(pRcvData,sPackagehead, 2) != 0)
		return CL_ANSWER_ERROE_PACK_HEAD_1;
	//包头2
	if (memcmp(pRcvData + 2, sPackageHeadFree, 2) != 0)
		return CL_ANSWER_ERROE_PACK_HEAD_2;

	//目的地址(PC)
	unsigned short nDest= pRcvData[6]*0x100+pRcvData[7];
	if(nDest!=0)
		return CL_ANSWER_ERROE_DEST_ADDRESS;

	//源地址
	unsigned short nSource= pRcvData[9]*0x100+pRcvData[10];
	if ( _SSeriesVersion != g_GlobalVariable.nVersionType )
	{
		nSource-=8;
	}
	if(nSource==0)
		return CL_ANSWER_ERROE_SOUR_ADDRESS;

	//校验数据包是否合法
	unsigned char ucChecksum[1];
	memset(ucChecksum, 0, sizeof(ucChecksum));
	CreateChecksum(pRcvData + 4, nLen - 5, ucChecksum);

	if (pRcvData[nLen-1] != ucChecksum[0])
		return CL_ANSWER_ERROE_CHECKSUM;

	m_ucDestSendCardAddress = pRcvData[12];//级联采集卡地址
	m_ucSourAddress = pRcvData[9]*0x100+pRcvData[10];//源地址
	if ( _SSeriesVersion != g_GlobalVariable.nVersionType )
	{
		m_ucSourAddress -= 8;
	}

	m_unDestAddress = pRcvData[6]*0x100+pRcvData[7];//目的地址(PC)
	m_ucPackType = pRcvData[8];//包类型
	m_nSequenceNumber = CTool::doGetIntegerData(pRcvData+ 9 , 2);//序列号
	memset(m_ucRcvData, 0, sizeof(m_ucRcvData));

	m_nLen = nLen - 15;
	memcpy(m_ucRcvData, pRcvData + 15, nLen - 16);//数据域内容

	bool bAnsOK = UnPackErrorType();
	if(bAnsOK)
		return 1;
	else
		return CL_ANSWER_ERROE_CONNECT;
}

//解应答包错误类型
bool CCLPackCommunicationData::UnPackErrorType()
{
	//应答信息位	1字节	0X77代表成功，0XAA代表失败)
	switch(m_ucPackType)
	{
		//采集卡EDID设置包	0x01
	case 0x01:
		//采集卡状态提取包	0x03
	case 0x03:
		//采集卡版本号查询包 	0x04
	case 0x04:
		//采集卡复位命令包 0x05
	case 0x05:
		//采集卡备份使能包 	0x06
	case 0x06:		
		//采集卡起点设置包	0x07
	case 0x07:
		//采集卡起始编址号设置包	0x08
	case 0x08:	
		//采集卡命令转发设置包	0x0B
	case 0x0B:
		//采集卡截取设置包	0x0D
	case 0x0E://智能编址包
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
		//与扫描卡相关的所有包类型：
		//扫描卡存储命令与参数 初始包	0x49
	case 0x49:
		//扫描卡参数包	0x60
	case 0x50:
	case 0x51:
	case 0x54:
	case 0x60:
		//单点校正数据包	0x61
	case 0x61:
		//伽马校正Red数据包	0x62
	case 0x62:
		//伽马校正Green数据包	0x63
	case 0x63:
		//伽马校正Blue数据包	0x64
	case 0x64:
		//扫描卡走线查找表包	0x65
	case 0x65:
		//锁屏解屏命令包	0x66
	case 0x66:
		//亮度调节命令包	0x45
	case 0x45:
		//智能编址点名包	0x6A
	case 0x6a:
		//报警门限值设置包	0xd2
	case 0x73:
		//扫描卡版本查询包	0x42
	case 0x42:
		//单点校正使能包  0x47
	case 0x47:
		//色温调节命令包       0x40
	case 0x40:
		//显示屏自测控制命令包	0x6B
	case 0x6b:
		//驱动芯片开路检测包	0x6C
	case 0x6c:
		//驱动芯片过热检测包	0x6D
	case 0x6d:
		//扫描卡在线升级命令包	0x6E
	case 0x6e:
		//扫描卡在线升级数据包	0x6F
	case 0x6f:
		//启动结束设置包 启动读取扫描卡上SPI_FLASH中的校正数据到SDRAM	0x91
	case 0x91:
		//读取模组ID号
	case 0xA0:
		m_bACKOK = m_ucRcvData[0] == 0x77 ? true : false;
		break;
		//监测命令包	0xd0
	case 0xd0:
		{
			if (m_ucRcvData[0] == 0x77)
				m_bACKOK = true;
			else if (m_ucRcvData[0] == 0xA1)
			{
				//0xA1	返回校验错误；数据长度正常，校验和错误	应答失败，后续字节填充0x00，值无意义；
				m_bACKOK = true;
				m_ucErrCode = 1;
			}
			else if (m_ucRcvData[0] == 0xA2)
			{
				//0xA2	返回温度超出范围,数据范围：-40.0--123度

				m_bACKOK = true;
				m_ucErrCode = 2;
			}
			else if (m_ucRcvData[0] == 0xA3)
			{
				//0xA3	返回湿度超出范围,数据范围：0.0%---100.0%

				m_bACKOK = true;
				m_ucErrCode = 3;
			}
			else if (m_ucRcvData[0] == 0xA4)
			{
				//返回温湿度传感器没有接,当温湿度标志位=1，采集的温湿度都为0或者返回无数据可判断

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
		//继电器属性设置包（0xD1）
	case 0xd1:
		//继电器手动模式输出控制包（0xD2）
	case 0xd2:
		//继电器自动模式门限值设置包（0xD3）
	case 0xD3:
		//电源电压、声音A/D采样命令(0xDB)
	case 0xDB:
		//功率采样命令(0xDC)
	case 0xDC:
		{
			if (m_ucRcvData[0] == 0x77)
				m_bACKOK = true;
			else if (m_ucRcvData[0] == 0xA0)
			{
				//0xA0	返回监控卡校验和失败
				m_bACKOK = true;
				m_ucErrCode = 1;
			}
			else if (m_ucRcvData[0] == 0xA1)
			{
				//0xA1	当前监控板硬件不支持该协议；例如没有安装继电器模块却收到该协议
				m_bACKOK = true;
				m_ucErrCode = 2;
			}
			else if (m_ucRcvData[0] == 0xA2)
			{
				//0xA2	当前继电器的配置模式不支持该协议；例如继电器D1命令配置为温控模式却收到了该指令


				m_bACKOK = true;
				m_ucErrCode = 3;
			}
			else if (m_ucRcvData[0] == 0xA3)
			{
				//0xA3	返回湿度超出范围,数据范围：0.0%---100.0%

				m_bACKOK = true;
				m_ucErrCode = 4;
			}
			else if (m_ucRcvData[0] == 0xA4)
			{
				//0xA4	继电器编号超出范围

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
		//LED灯开路检测包	0xE0
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
		//HUB在线升级数据包（0XE2）
	case 0xE2:
		//HUB版本查询包（0XE3）
	case 0xE3:
		//打HUB主配置数据包（0XE4）
	case 0xE4:
		m_bACKOK = m_ucRcvData[0] == 0x77 ? true : false;
		break;
	case 0x9D://回读扫描卡参数包
	case 0x9E:
		//回读校正数据包
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

//解包模组序列号
void CCLPackCommunicationData::GetModSeriNum(string & sModSerial, bool bAnswFlag)
{
	sModSerial = "";

	int nOffset = 0;
	if(bAnswFlag)
		nOffset = 1;

	char ucOeder[CALIBRATION_ORDER_NUM_LEN + CALIBRATION_DISPLAY_NUM_LEN + 1];	//订单号+屏体号
	memset(ucOeder,0,CALIBRATION_ORDER_NUM_LEN + CALIBRATION_DISPLAY_NUM_LEN + 1);
	memcpy(ucOeder,m_ucRcvData + nOffset,CALIBRATION_ORDER_NUM_LEN + CALIBRATION_DISPLAY_NUM_LEN);


	char ucModuleID[CALIBRATION_CABINET_NUM_LEN +CALIBRATION_MODULE_NUM_LEN + 1];	//箱体号模组号
	memset(ucModuleID,0,CALIBRATION_CABINET_NUM_LEN +CALIBRATION_MODULE_NUM_LEN + 1);

	//箱体号(2byte)
	int nCardID = CTool::doGetIntegerData(m_ucRcvData + CALIBRATION_ORDER_NUM_LEN + CALIBRATION_DISPLAY_NUM_LEN + nOffset, 2);
	if(nCardID > 9999 || nCardID < 0)
		nCardID = 0;

	//模组号(2byte)
	int nModID = CTool::doGetIntegerData(m_ucRcvData + CALIBRATION_ORDER_NUM_LEN + CALIBRATION_DISPLAY_NUM_LEN + nOffset + 2, 2);
	if(nModID > 999 || nModID < 0)
		nModID = 0;

	sprintf(ucModuleID, "%.4d%.3d",nCardID,nModID);

	sModSerial = ucOeder;
	sModSerial += ucModuleID;

}

//获取发送卡状态
void CCLPackCommunicationData::GetSendCardStatus(ATLVCAK6Status & sAtlvcStatus)
{
	/*
	bit7-bit2	预留扩展,填0，值无意义
	Bit1	1	指示DVI状态值有效
		0	指示DVI状态值无效
	Bit0   1  指示网络发送状态值有效
			0  指示网络发生状态值无效
	*/

	unsigned char c = m_ucRcvData[1]>>1;
	c = c & 0x01;
	//DVI状态值
	if (c)
	{
		unsigned char ucFlag = m_ucRcvData[2];
		sAtlvcStatus.nDvi = ucFlag & 0x01;
	}
	//HDMI状态值
	c = m_ucRcvData[1] >> 2;
	sAtlvcStatus.nHdmi = c & 0x01;
	/*if (c)
	{
		unsigned char ucFlag = m_ucRcvData[2] ;
		sAtlvcStatus.nHdmi = ucFlag & 0x01;
	}*/
	//网络发送状态
// 	c = m_ucRcvData[1];
// 	c = c & 0x01;
// 	if (c)
	{
		/*Bit1	1-P1无效	0-P1有效
		Bit3	1-P3无效    0-P3有效
		Bit5	1-P0无效    0-P0有效
		Bit7	1-P2无效    0-P2有效*/
		//P1口  D  下口  1-无效，0-有效
		unsigned char ucFlag;
		ucFlag = m_ucRcvData[3] >> 1;
		sAtlvcStatus.nNetPort[1] = ucFlag & 0x01;

		//P0口  U  上口  1-无效，0-有效
		ucFlag = m_ucRcvData[3] >> 5;
		sAtlvcStatus.nNetPort[0] = ucFlag & 0x01;

		//P2口
		ucFlag = m_ucRcvData[3] >> 7;

		sAtlvcStatus.nNetPort[2] = ucFlag & 0x01;

		//P3口
		ucFlag = m_ucRcvData[3] >> 3;
		sAtlvcStatus.nNetPort[3] = ucFlag & 0x01;
	}
	//分辨率  1024*768 减一处理
	//分辨率 宽 0x03FF
	sAtlvcStatus.nResolution[0] = CTool::doGetIntegerData(m_ucRcvData + 4, 2) + 1;
	//分辨率 高 0x02FF
	sAtlvcStatus.nResolution[1] = CTool::doGetIntegerData(m_ucRcvData + 6, 2) + 1;

	//帧率获取
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

	//读取有效位, -1 --成功 0 -- 初始化  1 -- 失败
	sAtlvcStatus.nReadErrorFlag = -1;
}
/*
2012-11-12 获取采集卡自动亮度调节和定时亮度调节状态
*/
void CCLPackCommunicationData::GetSendCardLightAdjustStatus(ATLVCAK6Status & sAtlvcStatus)
{
	//0-自动亮度调节不使能 1-表示自动亮度调节使能
	//0-定时亮度调节使能，1-表示定时亮度调节使能
   sAtlvcStatus.nAutoLightAdjustStatus = m_ucRcvData[1];
   sAtlvcStatus.nTimingLightAdjustStatus = m_ucRcvData[2];
   //读取有效位, -1 --成功 0 -- 初始化  1 -- 失败
   sAtlvcStatus.nReadErrorFlag = -1;
}
//获取扫描卡版本
void CCLPackCommunicationData::GetSendCardVersion(Version & sVersion)
{
	//ACK_DATA1 	Bit7-5	0h：扫描卡 ，1h：环境监控卡1,  2h：环境监控卡2，3h：环境监控卡3, 4h：环境监控卡4
	// Bit4	0：boot程序，1：应用程序 ,         Bit3-0	0h：标准版，1h：PWM版 ;2h：艺术屏; 3h：模组式PWM版;4h：PWM艺术屏
	sVersion.ucTper = m_ucRcvData[1];//软件版本类型
	//版本号
	sVersion.dVersion = (double)(m_ucRcvData[2] + (double) m_ucRcvData[3] / 100);
	//备份状态
	if (m_ucRcvData[4] & 0x01)
		sVersion.bBackup = true;
	else
		sVersion.bBackup = false;
	//Bit1	锁屏状态。 1：锁屏。0：不锁屏
	unsigned char c = m_ucRcvData[4]>>1;
	if (c  & 0x01)
		sVersion.bLockScreen = true;
	else
		sVersion.bLockScreen = false;
	//Bit2	上电参数初始化状态。1：成功。0：失败
	c = m_ucRcvData[4] >> 2;
	if (c  & 0x01)
		sVersion.bInit = true;
	else
		sVersion.bInit = false;

	//Bit3	P0连接状态，1，有连接，0，无连接
	sVersion.nP0LinkState = (m_ucRcvData[4] >> 3) & 0x01;
	//Bit4	P1连接状态，1，有连接，0，无连接
	sVersion.nP1LinkState = (m_ucRcvData[4] >> 4) & 0x01;

	//网络端口优先级标志
	sVersion.nNetPortPriority = (m_ucRcvData[4] >> 5) & 0x01;
	//锁定优先网口
	sVersion.bLockNetPort = ((m_ucRcvData[4] >> 6) & 0x01) ? true : false;

	sVersion.nYear = 2000 + (m_ucRcvData[6]>>4&0x0F)*10 +(m_ucRcvData[6]&0x0F) ;
	sVersion.nMon = (m_ucRcvData[7]>>4&0x0F)*10 +(m_ucRcvData[7]&0x0F);
	sVersion.nDay = (m_ucRcvData[8]>>4&0x0F)*10 +(m_ucRcvData[8]&0x0F);
	sVersion.nSection = m_ucRcvData[9];
	sVersion.nCorrectType = m_ucRcvData[10]& 0x03;

	sVersion.nScanOut = m_ucRcvData[11];

	sVersion.nScanOutSection = m_ucRcvData[12];

	sVersion.nModuleEx = m_ucRcvData[13];

	sVersion.nTestVersion = m_ucRcvData[15];//测试版本，小时中显示出来
	sVersion.nTestVersion_Second = m_ucRcvData[5];//测试版本，秒中显示出来
}

//获取模组的运行时间
void CCLPackCommunicationData::GetModuleRuntime(string & sModuleRuntime)
{
	//unsigned char c5 = m_ucRcvData[0];//0x77	成功	0xAA 	失败，后续字节无意义
	unsigned char c1 = m_ucRcvData[1];//运行时间分钟数[31:24]
	unsigned char c2 = m_ucRcvData[2];//运行时间分钟数[23:16]
	unsigned char c3 = m_ucRcvData[3];//运行时间分钟数[15:8]
	unsigned char c4 = m_ucRcvData[4];//运行时间分钟数[7:0]
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

//获取模组的运行时间
void CCLPackCommunicationData::GetModuleRuntime(long & nModuleRuntime)
{
	//unsigned char c5 = m_ucRcvData[0];//0x77	成功	0xAA 	失败，后续字节无意义
	unsigned char c1 = m_ucRcvData[1];//运行时间分钟数[31:24]
	unsigned char c2 = m_ucRcvData[2];//运行时间分钟数[23:16]
	unsigned char c3 = m_ucRcvData[3];//运行时间分钟数[15:8]
	unsigned char c4 = m_ucRcvData[4];//运行时间分钟数[7:0]
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


//获取常规(ATIEC或箱体单个扫描卡)监测数据
void CCLPackCommunicationData::GetCustomMonitorData(LPMONITORDATA pMonitorData)
{
	MonitorItem sMonitorFlag;
	//MONITOR_FLAG	1	bit7-bit4	预留扩展

	//Bit7 风扇检测使能
	unsigned char c = m_ucRcvData[1] >> 7;
	sMonitorFlag.bFanStateFlag[0] = c & 0x01;
	sMonitorFlag.bFanStateFlag[1] = c & 0x01;

	//Bit3	1	指示烟雾监测值有效
	//	0	指示烟雾监测值无效
	c = m_ucRcvData[1] >> 3;
	sMonitorFlag.bSmogFlag = c & 0x01;

	//Bit2	1	指示亮度监测值有效
	//	0	指示亮度监测值无效
	c = m_ucRcvData[1]>> 2;
	sMonitorFlag.bBrightFlag = c & 0x01;

	//Bit1	1	指示湿度监测值有效
	//	0	指示湿度监测值无效
	c = m_ucRcvData[1]>>1;
	sMonitorFlag.bHumidityFlag = c & 0x01;

	//Bit0	1	指示温度监测值有效
	//	0	指示温度监测值无效
	c = m_ucRcvData[1];
	sMonitorFlag.bTemperatureFlag = c & 0x01;


	//TEMPERATURE_VALUE	2	-40.0--123.0度
	//温度值，高位在前，低位在后。精确到0.1度。HUB板应答时，直接将温湿度传感器读上来的无修正的数据上传。
	//上位机收应答后通过修正公式计算温度值并显示。
	//T=0.01*D-40

	/*	2013-03-12
	将SHT11温湿度传感器计算公式修改为SHT21的转换公式：
	温度  T = -46.85 + 175.72*S/2^(16)    14bit
	湿度 RH= -6 + 125*Q/2^(16)        12bit
	注意：
	S要在收到的数据（二进制）最低两位补00。
	Q要在收到的数据（二进制）最低四位补0010*/

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
	//湿度值，精确到0.1度。HUB板应答时，直接将温湿度传感器读上来的无修正的数据上传。
	//上位机收应答后通过修正公式计算湿度值并显示。
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


	//FOG_VALUE	1	0x00	无烟雾告警 0x01	有烟雾告警
	if (sMonitorFlag.bSmogFlag)
		pMonitorData->bSmog = m_ucRcvData[6] == 0x00 ? false : true;

	//LUMINANCE_VALUE	1	0-255	亮度传感器采集的亮度值
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
	//继电器状态	1	二进制位1表示继电器吸合，0表示断开。
	//如00001001：表示第一路和第四路继电器吸合，其他断开

	//2012-10-22 电源状态 modify by sunj 获取电源状态
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


	//风扇FAN_L状态	1	0x00	未检测  0x01、0x02	正常开启  0x03	异常关闭
	//0x04	正常关闭	0x05	异常开启
	//风扇FAN_R状态	1	0x00	未检测  0x01、0x02	正常开启  0x03	异常关闭
	//0x04	正常关闭	0x05	异常开启
	if (sMonitorFlag.bFanStateFlag[0])
		pMonitorData->nFanState[0] = m_ucRcvData[9];

	if(sMonitorFlag.bFanStateFlag[1])
		pMonitorData->nFanState[1] = m_ucRcvData[10];

	//VER_DATA	2	0x00	第一字节低七位表示版本号高位，最高位‘1’表示boot，为‘0’表示app程序,第二字节表示版本号低八位
	//版本定义：例如版本号为8107表示V1.07boot版本；020F表示V2.15app版本
	pMonitorData->version.ucTper = m_ucRcvData[13] >> 7;//1-boot，0-app
	pMonitorData->version.dVersion =(double) ((m_ucRcvData[13] & 0x7F) + (double) m_ucRcvData[14] / 100.0);

}
//获取箱体（扫描卡）电源电压监测数据
void CCLPackCommunicationData::GetVoltageMonitorData(LPMONITORDATA pMonitorData)
{
	//0~255　	第1-5路电源电压
	//要显示的电压值 =  (红灯电压*3.295/256)*2，显示精确到0.01；单位：V
	for (int i = 0; i < POWER_VOL_NUM; ++i)
	{
		pMonitorData->fPowerVol[i] = (float)(2 * m_ucRcvData[1 + i] * 3.295 / 256.0);
	}
}
//获取箱体（扫描卡）功率监测数据
void CCLPackCommunicationData::GetPowerMonitorData(LPMONITORDATA pMonitorData)
{
	//功率
	//将此数据除以10000，得到一个百分比数，然后再乘以1250，即为当前单个箱体功率。
	double dCapacity =(double) CTool::doGetIntegerData(m_ucRcvData + 1, 2);
	pMonitorData->nCapacityFactor = (int) ((dCapacity / 10000.0) * 1250);
}
//获取箱体（扫描卡）逐点检测数据
void CCLPackCommunicationData::GetPointDetectMonitorData(LPMONITORDATA pMonitorData,ScanCardAttachment & sScanCardAtt)
{
	CStructSingleScanCard  & scancard = sScanCardAtt.scancard;

	RECT rt = sScanCardAtt.rtRect;

	LinkTable & ZoneLink = sScanCardAtt.sSectionlinktable;

	//ACK_DATA1	1	0x00-0x54	出错灯个数，每个返回数据包最多上传0x04个灯的出错状态,
	pMonitorData->nErrorPointNum = (int) m_ucRcvData[1];

	//ACK_DATA2	1	0x00-0xFF	DCLK出错状态，相应bit位为1标示对应DCLK出错
	//ACK_DATA3	1	0x00	保留
	for (int m = 0; m < SCAN_LINE_STATE_NUM; ++ m)
	{
		if (m < 8)
		{
			pMonitorData->nDClkState[m] = (m_ucRcvData[2] >> m) & 0x01;			//DCLK状态
		}
		else
		{
			pMonitorData->nDClkState[m] = (m_ucRcvData[3] >> (m - 8)) & 0x01;			//DCLK状态
		}
		pMonitorData->nScanLineState[m] = pMonitorData->nDClkState[m];		//扫描线状态
	}

	// 注释部分用于打桩测试
	for (int n = 0; n < pMonitorData->nErrorPointNum; n ++)
	//for (int n = 0; n < 5; n ++)
	{
		//出错LED灯在箱体内X坐标
		pMonitorData->sErrorPoint[n].nX = m_ucRcvData[4 + n * 3];
		//出错LED灯在箱体内Y坐标
		pMonitorData->sErrorPoint[n].nY = m_ucRcvData[5 + n * 3] + ((m_ucRcvData[6 + n * 3] >> 4) << 8);


// 		//出错LED灯在箱体内X坐标
// 		pMonitorData->sErrorPoint[n].nX = 0x05;
// 		//出错LED灯在箱体内Y坐标
// 		pMonitorData->sErrorPoint[n].nY = 0x50 + n;


		//2013-10-18 caixl 逐点检测 根据区查找表转换返回点的位置；
		//old:HUB所带的抽象灯板的宽度为区宽，高度为扫描卡所带的区个数X每区行数，区纵向排列，编址为0,1,2,3,...,n，
		//new:HUB所带的抽象灯板的宽度为卡区宽，高度为扫描卡所带的区个数X每区行数，区纵向排列，编址为,1,2,3,...,n，
		//区地址即为区查找表的物理地址。区大小为：区宽X每区行数。
		//将返回像素点转化为区的相对位置，再根据区的相对卡的位置转化为卡的相对位置


		//默认为从右到左0x1	0x0	从左往右	0x1	从右往左	0x2	从上往下	0x3	从下往上
		if ( 0 == scancard.nDataInputDir || 1 == scancard.nDataInputDir )//	0x0	从左往右	0x1	从右往左
		{
			//区物理地址，即查找表的内容
			int nZoneAddr = pMonitorData->sErrorPoint[n].nY / scancard.nScanCardSectionRowNumber;

			//通过物理地址转换为sdram地址，找查找表的ID
			int nZoneID = 0;
			for(int i = 0; i < ZoneLink.nLen; ++i)
			{
				if(ZoneLink.ucLinkTable[i] == nZoneAddr)
				{
					nZoneID = i;
					break;
				}
			}

			//点相对于区的位置，X不变化，Y对每区行数求余
			pMonitorData->sErrorPoint[n].nY = pMonitorData->sErrorPoint[n].nY % scancard.nScanCardSectionRowNumber;

			/* old:
			//区相对于卡的列位置
			int nColID = nZoneID % scancard.nSectionHorNum;

			//区相对于卡的行位置
			int nRowID = nZoneID / scancard.nSectionHorNum;

			//像素相对于卡的位置
			pMonitorData->sErrorPoint[n].nX += nColID * scancard.nSectionWidth;
			pMonitorData->sErrorPoint[n].nY += nRowID * scancard.nScanCardSectionRowNumber;
			*/

			// new:---------------begin----------------------
			//点所在卡区相对于卡的列位置（从0开始）
			int nCardColID = nZoneID % (scancard.nSectionHorNum * scancard.nCard_zone_Num); //3

			//点所在卡区相对于卡的行位置（从0开始）
			int nCardRowID = nZoneID / (scancard.nSectionHorNum * scancard.nCard_zone_Num); //1

			//点所在卡区中第几个横向区宽
			int nModSectionID = pMonitorData->sErrorPoint[n].nX / scancard.nSectionWidth;//0

			//点在卡区，所在横向区的X偏移位置
			int nModeSectionOffset = pMonitorData->sErrorPoint[n].nX % scancard.nSectionWidth;

			//点在横向第几个卡区宽上
			//int nHorCardSectId = nCardColID / scancard.nSectionHorNum;

			//点在模组上的第一个模区宽上
			//int nModSectID = nCardColID % scancard.nSectionHorNum;

			//像素相对于卡的位置
			//当前模区之前的卡区带的点数 +  当前模区之前的模区点数 + 点在第几个模组区宽的X偏移位置
			// 		pMonitorData->sErrorPoint[n].nX = nHorCardSectId * nHorCardSectId * scancard.nCard_zone_width * scancard.nSectionHorNum
			// 			+ nModSectionID * scancard.nModuleWidth
			// 			+ nModSectID * scancard.nSectionWidth + nModeSectionOffset;

			pMonitorData->sErrorPoint[n].nX = nCardColID * scancard.nSectionWidth //卡
				+ nModSectionID * scancard.nModuleWidth //横向
				+ nModeSectionOffset; //像素点在模组index
			pMonitorData->sErrorPoint[n].nY += nCardRowID * scancard.nScanCardSectionRowNumber;
			//---------------end----------------------


			//2013.7.4 caixl 将像素相对于扫描卡的位置转换成箱体问题
			pMonitorData->sErrorPoint[n].nX += (short)rt.left;
			pMonitorData->sErrorPoint[n].nY += (short)rt.top;
		}
		else if (2 == scancard.nDataInputDir || 3 == scancard.nDataInputDir)//0x2	从上往下	0x3	从下往上
		{
			;//B50不需要作转换处理
		}


		//出错LED灯颜色指示
		//低三位bit2、bit1、bit0分别用于指示红、绿、蓝三种颜色灯的状态，
		//相应位为'0'表示相应LED灯开路，'1'表示工作正常。
		//红灯标识
		pMonitorData->sErrorPoint[n].bRedFlag = (m_ucRcvData[6 + n * 3] >> 2) & 0x01 ? false : true;
		//绿灯标识
		pMonitorData->sErrorPoint[n].bGreenFlag = (m_ucRcvData[6 + n * 3] >> 1) & 0x01 ? false : true;
		//蓝灯标识
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
	//ACK_DATA10-ACK_DATA11网络监控故障计算器
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
discription: 解0x1A包 获取温湿度
create : 2012-10-19
input:
output:sAtlvcStatus -采集卡状态
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
