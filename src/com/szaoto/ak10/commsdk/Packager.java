/*
   * 文件名 Packager.java
   * 包含类名列表com.szaoto.ak10.commsdk
   * 版本信息，版本号
   * 创建日期2013年12月28日下午8:22:33
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.commsdk;

import com.szaoto.ak10.common.CabinetData.CStructSingleScanCard;
import com.szaoto.ak10.common.CabinetData.Drive_ic_reg;
import com.szaoto.ak10.common.Display.ColourRGB;
import com.szaoto.ak10.common.GammaData;
import com.szaoto.ak10.common.CabinetData.LinkTable;
import com.szaoto.ak10.common.MonitorData;
import com.szaoto.ak10.common.RECT;
//import com.szaoto.ak10.common.TestClass;
import com.szaoto.ak10.common.CabinetData.ScanCardAttachment;

import android.util.Log;

/*
 * 类名Packager
 * 作者 liangdb
 * 主要功能 打包解包器接口
 * 创建日期2013年12月28日
 * 修改者，修改日期，修改内容
 */
public class Packager {
	public static final int MONITORTYPE_COSTOM = 0;
	public static final int MONITORTYPE_POWER = 1;
	public static final int MONITORTYPE_POINTDETECT = 2;
	public static final int MONITORTYPE_VOTAGE = 3;
	public static final int MONITORTYPE_WEBERROR = 4;
	
	public static final int MONITORACTIONTYPE_FF = 1;
	public static final int MONITORACTIONTYPE_NORMAL = 0;
	////////////////////////////////////////////////////////////////////////////////////
	//以太网协议帧
	////////////////////////////////////////////////////////////////////////////////////
	//返回打包发送数据
	static public native byte[] EthernetPackDataBase(byte[] ucDestAddress,				//目的地址
													FrameDataField sFrameDataField	//数据(包含长度信息)
													);
	
	//流程
	//发送：EthernetPackDataWrite后，调用 spi_write 写入即可
	//读取：EthernetPackDataRead后，调用 spi_write，再调用 spi_read 读出数据，然后 EthernetUnPackDataRead 解包
	
	//发送 写操作 打包(直接的写至。。。)
	//返回打包发送数据
	static public native byte[] EthernetPackDataWrite(byte[] ucDestAddress,			//目的地址
													byte[] ucAddress,				//FIFO/REG地址
													int nSequenceNumber,			//序列号
													int nLength,					//长度（真实数据长度）
													byte[] ucData		
													//数据域字节
										
			);				
	
	//发送 读操作 打包（读取到。。。）
	//返回打包发送数据
	static public native byte[] EthernetPackDataRead(byte[] ucDestAddress,			//目的地址
													byte[] ucAddress,				//FIFO/REG地址
													int nLength						//长度（真实数据长度）	//不带数据
													);
	
	//解包读应答
	static public native FrameDataField EthernetUnPackDataRead(byte[] ucReceiveData,//接收数据
																int nLength);		//长度
	
	////////////////////////////////////////////////////////////////////////////////////
	//28Byte协议帧
	////////////////////////////////////////////////////////////////////////////////////
	static public native byte[] NativePackDataBase(int ucDestAddress,	//目标地址
												byte ucPackType,	//包类型
												int unSequenceNumber,//顺序号
												int nAnsweredFlag,	//应答标示
												byte[] ucData		//数据域字节
												);	//打包发送数据
	
	
	
	//////////////////////////////////////////////////////////////////////////////////
	//快速打包入口(整包)
	//////////////////////////////////////////////////////////////////////////////////
	//######################################
	//扫描卡
	//######################################
	//亮度打包
	static public native byte[] PackSetBright(byte[] ucDestAddress,			//目的地址
												byte[] ucAddress,			//FIFO/REG地址
												int nBrightness);
	//1.	同步延时参数（Synchron Delay）：2.	关闭扫描周期参数（Disable Scan Cycle）：打包
	static public native byte[] PackSet3DPara(byte[] ucDestAddress,			//目的地址
												byte[] ucAddress,			//FIFO/REG地址
												int nSynchron_Delay,
												int nDisableScanCycle);
	//色温打包
	static public native byte[] PackSetColorTemperature(byte[] ucDestAddress,		//目的地址
														byte[] ucAddress,			//FIFO/REG地址
														ColourRGB sColorRGB,short nHighLowGap,
														short nGrayEnhanceMode);
	//打调节色温增益包
	static public native byte[] PackColorTempCurrentData(byte[] ucDestAddress,		//目的地址
													byte[] ucAddress,			//FIFO/REG地址
													int nAddress,//扫描卡地址
													ColourRGB sColorRGB);
	
	//GAMMA打包
	
	static public native byte[] PackSetGamma(byte[] ucDestAddress,			//目的地址
												byte[] ucAddress,			//FIFO/REG地址
												GammaData sGammaData,
												int nColorIndex);
	
	//设置GAMMA表
	static public native byte[] PackGammaTable(//byte[] ucDestAddress,			//目的地址
											//byte[] ucAddress,			//FIFO/REG地址
											short nAddress,
											GammaData sGammaData,
											int nColorIndex);
	
	//打扫描卡主配置数据包包返回 int - 包长度
	static public native byte[] PackScanCardData(//byte[] ucDestAddress,			//目的地址
												//byte[] ucAddress,			//FIFO/REG地址
												int nScanCardAddress,//扫描卡地址
												int nPackID,//顺序号
												CStructSingleScanCard sScanCard, //扫描卡参数结构
												int nEmptyByte);//校正前默认空字节数
	
	
	//打启动结束设置包(0x91)
	static public native byte[] PackStartOrEnd(byte[] ucDestAddress,			//目的地址
												byte[] ucAddress,			//FIFO/REG地址
												int nScanCardAddress,//扫描卡地址
												boolean bStart,
												short nType,
												short nModuleRow,
												short nModulCol);


	//打HUB参数包
	static public native byte[] PackHUBPara(//byte[] ucDestAddress,			//目的地址
										//byte[] ucAddress,			//FIFO/REG地址
										int nAddress,//扫描卡地址
										CStructSingleScanCard sScancard
										);
	
	//打HUB参数包
	static public native byte[] PackHUBLookup(//byte[] ucDestAddress,			//目的地址
											//byte[] ucAddress,			//FIFO/REG地址
											int nAddress,//扫描卡地址
											LinkTable hublinktable);


	//打扫描卡在线升级命令包(0x95)
	static public native byte[] PackScanCardUpdateStart(byte[] ucDestAddress,			//目的地址
								byte[] ucAddress,			//FIFO/REG地址
								int nAddress,
								short nUpdateType,
								boolean bStart,
								boolean bUpdateBoot);

	//储存命令与参数初始包
	static public native byte[] PackSaveScanCardPara(byte[] ucDestAddress,			//目的地址
													byte[] ucAddress,			//FIFO/REG地址
													short nAddress,
													int nTpyeID,
													boolean bDefaul);

	//打扫描卡带载区域设置包
	static public native byte[] PackScanCardLoadRegionData(//byte[] ucDestAddress,			//目的地址
								//	byte[] ucAddress,			//FIFO/REG地址
									short nAddress,
									RECT rtLoad,
									short nStartX,
									short nStartY);

	//////////////////////////////////////////////////////////////////////

	//打扫描卡主配置数据包包返回 int - 包长度（6个包）
	/*1、智能设置相关参数
	2、扫描相关参数
	3、连线图计算相关参数1
	4、连线图计算相关参数2
	5、连线图计算相关参数3
	6、校正用计算相关参数
	*/
	static public native byte[] Pack6ScanCardData(byte[] ucDestAddress,			//目的地址
						byte[] ucAddress,			//FIFO/REG地址
						int nScanCardAddress,//扫描卡地址
						CStructSingleScanCard sScanCard, //扫描卡参数结构
						Drive_ic_reg ndrive_ic_reg,
						int nEmptyByte);//校正前默认空字节数

	//打无信号显示包
	static public native byte[] PackNoSingleDisp(//byte[] ucDestAddress,			//目的地址
							//byte[] ucAddress,			//FIFO/REG地址
							short nAddress,
							short nNoSingleDisp);

	//运算处理包
	static public native byte[] PackOperationProcessing(//byte[] ucDestAddress,			//目的地址
							//byte[] ucAddress,			//FIFO/REG地址
							short nAddress,
							short nScancardSectionRowNumber);

	//视频处理包
	static public native byte[] PackVideoProcessing(//byte[] ucDestAddress,			//目的地址
							//byte[] ucAddress,			//FIFO/REG地址
							short nAddress,
							CStructSingleScanCard sScanCard
							);

	//设置读取校正数据地址包
	static public native byte[] PackCorrectProcessing(//byte[] ucDestAddress,			//目的地址
							//byte[] ucAddress,			//FIFO/REG地址
							short nAddress,
							CStructSingleScanCard sScanCard);

	//设置边界系数查找表
	static public native byte[] PackCorrectProcessingLookup(//byte[] ucDestAddress,			//目的地址
							//byte[] ucAddress,			//FIFO/REG地址
							short nAddress,
							CStructSingleScanCard sScanCard);

	//设置扫描卡走线查找表
	static public native byte[] PackScanCardLinkTable(//byte[] ucDestAddress,			//目的地址
							//byte[] ucAddress,			//FIFO/REG地址
							short nAddress,
							short nDataLineRange,
							short nDCBlineClkEn,
							LinkTable sLinkTable);

	//设置扫描卡区走线查找表
	static public native byte[] PackScanCardSectionLinkTable(//byte[] ucDestAddress,			//目的地址
									//byte[] ucAddress,			//FIFO/REG地址
									short nAddress,
									LinkTable sLinkTable);
	
	

	//设置单点校正使能(广播无应答)。nDotType：0 - 无， 1 - 调亮， 2 - 调色
	static public native byte[] PackCalibrationEnable(//byte[] ucDestAddress,			//目的地址
								//byte[] ucAddress,			//FIFO/REG地址
								short nAddress,
								boolean bEnable, short nDotType);
	
	//锁屏或解屏
	static public native byte[] PackLockUnlock(byte[] ucDestAddress,			//目的地址
						byte[] ucAddress,			//FIFO/REG地址
						short nAddress,
						boolean bLock);

	//智能编址包
	static public native byte[] PackScanCardIntelligentPara(byte[] ucDestAddress,			//目的地址
									byte[] ucAddress,			//FIFO/REG地址
									int nAddressMin);

	//继电器输出
	static public native byte[] PackRelay(byte[] ucDestAddress,			//目的地址
										byte[] ucAddress,			//FIFO/REG地址
										short nAddress,short nRelayID, boolean bPower);

	//继电器自动控制
	static public native byte[] PackRelayAttribute(byte[] ucDestAddress,			//目的地址
						byte[] ucAddress,			//FIFO/REG地址
						short nAddress,short nRelayID,
						boolean bOverHeatOff,boolean bOverHumidityOff,boolean bSmogOff);

	//继电器打开、闭合门限
	static public native byte[] PackRelayThreshold(byte[] ucDestAddress,			//目的地址
							byte[] ucAddress,			//FIFO/REG地址
							short nAddress,short nRelayID,
							float fTemperatureMin,
							float fTemperatureMax,
							float fHumidityMin,
							float fHumidityMax);

	//设置打开箱体指示灯
	static public native byte[] PackOpenCabinetLamps(//byte[] ucDestAddress,			//目的地址
						//	byte[] ucAddress,			//FIFO/REG地址
							short nAddress,
							boolean bOpen);
	
	
	
	//######################################
	static public native MonitorData test();
	//环境监控卡（回读打包）,单个箱体回读打包
	static public native byte[] PackMonitorData(int monitortype,
													int monitoractiontype,
													byte[] ucDestAddress,			//目的地址
													byte[] ucAddress,			//FIFO/REG地址
													int address                  //环境监控卡地址，箱体点播回读地址
													
			);
	// 55 55 12 34 FF 42 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 BC
	static public native byte[] PackMonitorConnectstate(//采用扫描卡查询命令判断连接状态
															byte[] ucDestAddress,			//目的地址
															byte[] ucAddress,		//FIFO/REG地址
															int address             //广播地址
															);

	static public native byte[] PackAcquiAcquisitionCardEdidInfor(
															byte[] ucDestAddress,			//目的地址
															byte[] ucAddress,		//FIFO/REG地址
															byte[] edidinform
															);
	/*//检测数据广播打包
	static public native byte[]PackCustomMonitorDataFF(byte[] ucDestAddress,			//目的地址
														byte[] ucAddress			//FIFO/REG地址
			);
	//箱体电压监测数据打包
	static public native byte[]PackVoltageMonitorData(int address,					//	箱体点播回读地址
														byte[] ucDestAddress,			//目的地址
														byte[] ucAddress			//FIFO/REG地址
			);
	//箱体电压监测数据广播打包
	static public native byte[]PackVoltageMonitorDataFF(byte[] ucDestAddress,			//目的地址
														byte[] ucAddress			//FIFO/REG地址
			);
	//获取箱体（扫描卡）功率监测数据（点播、应答）
	static public native byte[]PackPowerMonitorData(int address,					//功率监测数据（点播)
														byte[] ucDestAddress,			//目的地址
														byte[] ucAddress			//FIFO/REG地址
			);
	//获取箱体（扫描卡）功率监测数据（全屏广播、应答）
	static public native byte[]PackPowerMonitorDataFF(byte[] ucDestAddress,			//目的地址
													byte[] ucAddress			//FIFO/REG地址
			);
	//获取箱体（扫描卡）逐点检测数据（点播、应答）
	static public native byte[]PackPointDetectMonitorData(int address,			//	逐点检测数据（点播)
													byte[] ucDestAddress,			//目的地址
													byte[] ucAddress			//FIFO/REG地址
			);
	//获取箱体（扫描卡）逐点检测数据（全屏广播、应答）
	static public native byte[]PackPointDetectMonitorDataFF(byte[] ucDestAddress,			//目的地址
														byte[] ucAddress			//FIFO/REG地址
			);
	//获取箱体（扫描卡）逐点网络错包（点播、应答）
	static public native byte[]PackNetWebErrorPakage(int address  ,                  //逐点网络错包（点播
														byte[] ucDestAddress,			//目的地址
														byte[] ucAddress			//FIFO/REG地址
			);
	//获取箱体（扫描卡）网络错包（全屏广播、应答）
	static public native byte[]PackNetWebErrorPakageFF(byte[] ucDestAddress,			//目的地址
														byte[] ucAddress			//FIFO/REG地址
			);*/
	
	static public native int UnPack28Data(byte[] Data28Commond, short commond );
	static public native MonitorData UnPackCustomMonitorData(byte[] ucDestAddress, byte[] recieve,MonitorData pMonitorData);
	static public native byte[] UnPackPowerMonitorData(byte[] ucDestAddress , byte[] recieve ,MonitorData pMonitorData);
	static public native byte[] UnPackPointDetectMonitorData(byte[] ucDestAddress ,byte[] recieve ,MonitorData pMonitorData , ScanCardAttachment  sScanCardAtt);
	static public native byte[] UnPackNetWebErrorPackage(byte[] ucDestAddress,byte[] recieve , MonitorData pMonitorData);
	static public native byte[] UnPackMonitorDataFunRate(byte[] ucDestAddress , byte[] recieve , MonitorData pMonitorData);
	
		
	
	//######################################
	//######################################
	//系统卡
	//######################################
	
	//######################################
	//采集卡
	//######################################
	//对比度打包
	
	//饱和度打包
	
	//######################################
	//发送卡
	//######################################
	static public native byte[] PackMutiple28byteData(byte[] ucDestAddress,			//目的地址
													byte[] ucAddress,			//FIFO/REG地址
													byte[] Muti28bypedata,
													int length);
	
	static public native int getlasterror();
	static {
        try {
        	System.loadLibrary("packager");
        } catch (UnsatisfiedLinkError e) {
            Log.d("packager", "packager library not found!");
        }
    }

}
