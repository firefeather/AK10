#include "BLLMonitorProc.h"
#include <stdio.h>
#include "PackageBase/CLPackCommunicationData.h"
#include <string.h>

CBLLMonitorProc::CBLLMonitorProc(void)
{

}
CBLLMonitorProc::~CBLLMonitorProc(void)
{

}
long CBLLMonitorProc::PackMonitorCommond(int monitortype,int monitoractiontype,int SCAddress,uint8_t * ucData)//打包监控数据命令
/* MONITORTYPE_COSTOM = 0;
 MONITORTYPE_POWER = 1;              monitortype表示5种监控类型
 MONITORTYPE_POINTDETECT = 2;
 MONITORTYPE_VOTAGE = 3;
 MONITORTYPE_WEBERROR = 4;

 MONITORACTIONTYPE_FF = 1;
 MONITORACTIONTYPE_NORMAL = 0;     monitoractiontype表示广播还是点播*/
{
	unsigned int ScAddress;
	unsigned int PackCommond;
	switch(monitortype)
		{
		case MONITORTYPE_COUSTOM:
			PackCommond = 0xD0;
			break;
		case MONITORTYPE_POWER:
			PackCommond = 0xDC;
			break;
		case MONITORTYPE_POINTDETECT:
			PackCommond = 0xE0;
			break;
		case MONITORTYPE_VOTAGE:
			PackCommond = 0xDB;
			break;
		case MONITORTYPE_WEBERROR:
			PackCommond = 0x50;
			break;
		default:
			break;
		}
		if(monitoractiontype == MONITORACTIONTYPE_NORMAL)
		{
			ScAddress = SCAddress;
		}
		else if(monitoractiontype == MONITORACTIONTYPE_FF)
		{
			ScAddress = 0xFF;
		}
	uint8_t uSendData[ETH_DATA_MAX_SIZE + 18];
	memset(uSendData,0,CL_SEND_PACK_SIZE);
	int length = CCLPackCommunicationData::PackDataBase(0,ScAddress,PackCommond,0,true,NULL,uSendData);
		if(0 < length)
		{
			 return length;
		}
	return 0;
}

