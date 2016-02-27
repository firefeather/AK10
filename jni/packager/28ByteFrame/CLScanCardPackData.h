#pragma once
#include "../Datastruct/StructSingleScanCard.h"
#include "PackageBase/CLPackCommunicationData.h"

class CCLScanCardPackData
{
public:
	CCLScanCardPackData(void);
	~CCLScanCardPackData(void);

	//打扫描卡主配置数据包包返回 int - 包长度
	int PackScanCardData(int nScanCardAddress,//扫描卡地址
							short nAtlvcAddressSecond,
							int nPackID,//顺序号
							CStructSingleScanCard sScanCard, //扫描卡参数结构
							unsigned char* ucSendData,//发送数据
							int nEmptyByte = 64);//校正前默认空字节数
	

	//打启动结束设置包(0x91)
	int PackStartOrEnd(int nScanCardAddress,//扫描卡地址
						short nAtlvcAddressSecond,
						bool bStart, 
						short nType, 
						short nModuleRow,
						short nModulCol,
						unsigned char* ucSendData);//发送数据


	//打HUB参数包
	int PackHUBPara(int nAddress,//扫描卡地址
					short nAtlvcAddressSecond,
					CStructSingleScanCard sScancard,
					unsigned char* ucSendData);//发送数据


	//打调节色温增益包
	int PackColorTempCurrentData(int nAddress,//扫描卡地址
						short nAtlvcAddressSecond,
						LPCOLOURRGB sColorRGB,unsigned char* ucSendData);

	//打调节色温数据包
	int PackColorTemperatureData(int nAddress,//扫描卡地址
									short nAtlvcAddressSecond,
									LPCOLOURRGB sColorRGB,int nCrTempID,short nHighLowGap,
									short nGrayEnhanceMode, int nDeductbit, unsigned char* ucSendData);//发送数据

	//打亮场阶段增益包
	int PackBrightCurrentData(int nAddress,//扫描卡地址
								short nAtlvcAddressSecond,
								LPCOLOURRGB sColorRGB,int nCrTempID,unsigned char* ucSendData,int nIndex);

	//打扫描卡在线升级命令包(0x95)
	int PackScanCardUpdateStart(int nAddress,short nAtlvcAddressSecond,short nUpdateType, bool bStart,bool bUpdateBoot,
								unsigned char * ucSendData);

	//储存命令与参数初始包(广播有应答)
	int PackSaveScanCardPara(short nAddress,short nAtlvcAddressSecond,int nTpyeID, bool bDefaul,unsigned char *ucSendData);

	//打扫描卡带载区域设置包
	int PackScanCardLoadRegionData(short nAddress,short nAtlvcAddressSecond,RECT rtLoad,
							short nStartX,short nStartY,unsigned char * ucSendData);

	//发送网络卡带载参数包，其带载根据实际扫描卡带载情况计算得到
	int PackNetCardLoadRegionData(short nAddress,short nAtlvcAddressSecond,RECT rtLoad,
		short nStartX,short nStartY,unsigned char * ucSendData);

	//发送射灯带载参数包，参数//nFlag = 0时候表示射灯区域，//nFlag = 1时候表示网络卡带载射灯区域
	int PackSpotlightLoadRegionData(short nAddress,short nAtlvcAddressSecond,RECT rtLoad,
		short nStartX,short nStartY,unsigned char * ucSendData, short nFlag = 0);


	//打模组亮暗线边界系数包
	int PackModelLineCoeffData(short nAddress,short nAtlvcAddressSecond,short nRowID,short nColID,
							LineCoeff sLineCoeff,unsigned char * ucSendData);

	//解扫描卡参数包
	void UnPackScanCardParam(map<int,CCLPackCommunicationData> & mUnPackData,
								CStructSingleScanCard & sScanCard,RECT & rtLoad);

	//解包色温参数
	void UnPackColorTempParam(map<int,CCLPackCommunicationData> & mUnPackData,
		short & nColorTempIndex, COLOURTEMFLAG & ColorTempFlag);


	//////////////////////////////////////////////////////////////////////

	//打扫描卡主配置数据包包返回 int - 包长度（6个包）
	/*1、智能设置相关参数
	2、扫描相关参数
	3、连线图计算相关参数1
	4、连线图计算相关参数2
	5、连线图计算相关参数3
	6、校正用计算相关参数
	*/
	int Pack6ScanCardData(int nScanCardAddress,//扫描卡地址
						CStructSingleScanCard & sScanCard, //扫描卡参数结构
						unsigned char* ucSendData,//发送数据
						int nEmptyByte = 64);//校正前默认空字节数

	//打无信号显示包
	int PackNoSingleDisp(short nAddress,
							short nNoSingleDisp,
							unsigned char * ucSendData);//发送数据

	//运算处理包
	int PackOperationProcessing(short nAddress,
							short nScancardSectionRowNumber,
							unsigned char * ucSendData);//发送数据

	//视频处理包
	int PackVideoProcessing(short nAddress,
							CStructSingleScanCard & sScanCard,
							unsigned char * ucSendData);//发送数据

	//设置读取校正数据地址包
	int PackCorrectProcessing(short nAddress,
							CStructSingleScanCard & sScanCard,
							unsigned char * ucSendData);//发送数据

	//设置边界系数查找表
	int PackCorrectProcessingLookup(short nAddress,
							CStructSingleScanCard & sScanCard,
							unsigned char * ucSendData);//发送数据

	//设置扫描卡走线查找表
	int PackScanCardLinkTable(short nAddress,
							short nDataLineRange,
							short nDCBlineClkEn,
							LINKTABLE &sLinkTable,
							unsigned char * ucSendData);//发送数据

	//设置扫描卡区走线查找表
	int PackScanCardSectionLinkTable(short nAddress,
							LINKTABLE &sLinkTable,
							unsigned char * ucSendData);//发送数据

	int PackHUBLookup(int nAddress,//扫描卡地址
						LinkTable hublinktable,
						unsigned char* ucSendData);

	//设置GAMMA表
	int PackGammaTable(short nAddress,
						GAMMADATA & sGammaData,
						int nColorIndex,
						unsigned char * ucSendData);//发送数据

	//设置单点校正使能(广播无应答)。nDotType：0 - 无， 1 - 调亮， 2 - 调色
	int PackCalibrationEnable(short nAddress,
							bool bEnable, short nDotType,
							unsigned char * ucSendData);//发送数据

	//发送亮度参数,nBrightPercent:亮度百分比(0 - 100)
	int PackBrightness(short nAddress,
						int nBrightPercent,
						unsigned char * ucSendData);//发送数据


	//发送3D配置信息
	//1.	同步延时参数（Synchron Delay）：设置范围 -127 - +127
	//2.	关闭扫描周期参数（Disable Scan Cycle）：设置范围 0 - 255
	int Pack3DPara(short nAddress,
						int nSynchronDelay,
						int DisableScanCycle,
						unsigned char * ucSendData);//发送数据
						

	//锁屏或解屏
	int PackLockUnlock(short nAddress,
						bool bLock,
						unsigned char * ucSendData);//发送数据

	//智能编址包
	int PackScanCardIntelligentPara(int nAddressMin,
						unsigned char * ucSendData);//发送数据

	//继电器输出
	int PackRelay(short nAddress,short nRelayID, bool bPower,
				unsigned char * ucSendData);//发送数据

	//继电器自动控制
	int PackRelayAttribute(short nAddress,short nRelayID,
						bool bOverHeatOff,bool bOverHumidityOff,bool bSmogOff,
						unsigned char * ucSendData);//发送数据

	//继电器打开、闭合门限
	int PackRelayThreshold(short nAddress,short nRelayID,
							float fTemperatureMin,
							float fTemperatureMax,
							float fHumidityMin,
							float fHumidityMax,
							unsigned char * ucSendData);//发送数据

	//设置打开箱体指示灯
	int PackOpenCabinetLamps(short nAddress,
							bool bOpen,
							unsigned char * ucSendData);//发送数据

private:
	//获取虚拟三色灯一像素屏 某灯（非像素点）在灯板中的相对位置索引0左上，1右上, 2左下，3右下
	//nRGBIndex: 0:R，1:G, 2:B;
	//nVirTualArray:排线 1：蓝,绿/空,红;绿,蓝/空,红；2：蓝,绿/空,红;蓝,绿/红,空；3：红,绿/空,蓝;绿,红/空,蓝
	//nLine:数据行序号，三色灯是两行数据，所以nLine<2
	int GetIndex(int nRGBIndex,int nVirTualArray, int nLine);
};
