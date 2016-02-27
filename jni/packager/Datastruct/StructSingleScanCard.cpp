#include "StructSingleScanCard.h"
#include "DataStructDef.h"
int max(int x, int y)
{
  return x > y ? x : y;
}

CStructSingleScanCard::CStructSingleScanCard(void)
{
	//ONE_SCAN_CARD_WIDTH	2	128	单块扫描控制卡所对应的LED显示模块中横向像素点的数量，
	//即扫描卡宽度。高位在前，低位在后。取值范围1~256，必须是16的整数倍
	nScanCardWidth = 128;
	//ONE_SCAN_CARD_HEIGHT	2	96	单块扫描控制卡所对应的LED显示模块中纵向像素点的数量，
	//即扫描卡高度。高位在前，低位在后。取值范围1~256，必须是16的整数倍
	nScanCardHeight = 96;
	//ONE_SCAN_CARD_WIDTH_REAL		
	//即扫描卡实际宽度。主要用于逐点检测和校正中有关模组的水平垂直个数
	nScanCardWidthReal = 128;
	//ONE_SCAN_CARD_HEIGHT_REAL
	//即扫描卡实际高度。主要用于逐点检测和校正中有关模组的水平垂直个数
	nScanCardHeightReal = 128;
	//1   32  模组宽度，取值范围1-64
	nModuleWidth = 32;
	//1   32  模组宽度，取值范围1-64
	nModuleHeight = 32;
	//1   3   模组区数  模组区数 = 模组高度/每区行数
	nModuleSectionNumber = 3;
	//1    4  模组横向个数，模组横向个数 = 扫描卡宽度/模组宽度
	nModuleHorNum = 4;
	//1    3  模组纵向个数，模组纵向个数 = 扫描卡高度、模组高度
	nModuleVerNum = 3;
	//SCAN_CARD_SECTION_NUM	1	16	扫描卡的区数，区数最大为16
	nScanCardSectionNumber = 16;
	//SCAN_CARD_SECTION_ROW_NUM	1	16	扫描卡每区行数，行数最大为16行
	nScanCardSectionRowNumber = 32;

	//S系列参数添加：箱体参数
	//SCAN_CARD_SECTION_NUM	1	16	扫描卡的区数，区数最大为16
	nScanCardSectionNumber_cabinet = 16;
	//SCAN_CARD_SECTION_ROW_NUM	1	16	扫描卡每区行数，行数最大为16行
	nScanCardSectionRowNumber_cabinet = 32;


	//SCAN_COLOR_DEPTH	1	14	扫描卡扫描的颜色深度，取12~16的整数。
	nScanColorDepth = 8;
	//GRAY_LEVEL 扫描卡灰度级别
	nGrayLedvel = 18;
	//origin_color_bit		原始颜色深度，比如8BIT,10bit，12bit，16bit
	nOrginColorBit = 8;
	//SCAN_MODE	1	16	扫描的模式，取 1-2-4-8-16
	nScanMode = 32;

	//SCAN_MODE_CABINET	1	4	箱体参数：扫描的模式，送数据
	nScanMode_cabinet = 32;

	//DOT_CORRECTION_EN	1	1	单点校正使能，取0，1
	bEmendBrightness = 0;
	//SCAN_GCLK_FREQUENCY	1	15.0	数据移位时钟时钟频率，最大30Mhz
	fScanClkFrequency = 1.5;
	//ZONE_DCLK_NUM	2		每区移位时钟数，256*16
	nZoneDClkNum = 0x1207;
	//duty_cycle_low_value_a1	8	0x32	7--0	数据移位时钟时钟占空比设置，设置低电平的计数值
	nDutyCycle = 0x32;
	//PWM_SCAN_GCLK_FREQUENCY	1	15.0	PWM时钟时钟频率，最大30Mhz
	fPWMScanClkFrequency = 1.5;
	//1  0-100  PWM时钟占空比可调等级 
	nPWMDutyCycle = 0x32;
	//row_oe_clk_num	16	 行消隐时间数，消除暗亮，默认为0x0003，做减1处理
	nOeClkNumber = 0x63;
	//刷新频率 根据颜色深度和扫描方式不同而不同
	nRefreshRate = 360;
	//REF_FREQ_MIN 刷新率最小值
	nRefreshRateMin = 1;
	//REF_FREQ_MAX 刷新率最大值
	nRefreshRateMax = 10000;
	//CONFIG_IC_TIME	配寄存器与逐点检测的时间(芯片间隔时间)， 1ms--30s
	nConfigICTime = 1024;
	//dat_freq_num   换帧频率计数器
	nDatFreqNum = 0;
	//OE_DELAY_VALUE	0x01	消隐延迟时钟数，消除暗亮，默认为0x01，做减1处理。
	nOeDelayValue = 2;
	//SYN_REFRESH_EN		同步刷新使能。默认为0x1，使能
	bSyncRefresh = false;
	//VIRTUAL_DISP_EN			虚拟显示使能，默认为0x0，不使能
	bVirtvalDisp = false;
	//FREQ_DIVISION_COEF	0x64		125Mhz的分频系数，最大为200分频，值为0.625Mhz。默认为10分频，值为0x09。做减1处理。
	nFreqDivisionCoeff = 0x64;
	//PWM_FREQ_DIVISION_COEF	0x64		125Mhz的PWM分频系数，最大为200分频，值为0.625Mhz。默认为10分频，值为0x09。做减1处理。
	nPWMFreqDivisionCoeff = 0x64;
	//DATA_OUTPUT_REVERSE	输出口逆序包括数据线和扫描线	0x00	数据线逆序：默认为0x0	0x0	不使能	0x1	使能
	bDataOutUpReverse = false;	
	//SCAN_OUTPUT_REVERSE		0x00	扫描线逆序：默认为0x0	0x0	不使能	0x1	使能
	bScanOutUpReverse = false;		
	//	DCB_LINE_CLK_EN			0x00	使能行信号DCB为时钟使带载高度加倍;	0x0	不使能	0x1	2倍	0x2	3倍	0x3	4倍
	nDCBlineClkEn = 0;
	//NO_SIGNAL_DISP	无信号显示：默认为0		0x0：黑屏，0x1：随机画面。0x2：图片
	nNoSingleDisp = 0;
	//DATA_INPUT_DIR		0x01	数据方向：（从显示屏正面看）	
	//默认为从右到左0x1	0x0	从左往右	0x1	从右往左	0x2	从上往下	0x3	从下往上
	nDataInputDir = 1;
	//ROW_DECODE_MODE		行译码方式：默认0x2
	//	0x0	静态无译码					0x6	164译码
	//	0x1	无译码芯片，直接驱动行管	0x7	192译码
	//	0x2	138译码						0x8	193译码
	//	0x3	139译码						0x9	595译码
	//	0x4	145译码或138双O				0xA	4096译码
	//	0x5	154译码						0xB	
	nRowDecodeMOde = 2;
	//	DATA_LINE_TYPE	8	0x14	7--0	数据类型大类：默认为0x00，
	nDataLineTypeRange = 0;
	//	DATA_LINE_TYPE	8	0x14	7--0	数据类型：默认为0x00，
	//	0x00-0x1F	红绿蓝分开, 
	//	0x20-0x18	红绿蓝合一三色1点串行
	//	0x30-0x38	红绿蓝合一三色8点串行
	//	0x40-0x48	红绿蓝合一三色16点串行
	//	0x50-0x6F	红绿蓝合一四色串行
	nDataLineType = 0;
	//DATA_LINE_CTRL	8	0x00	数据线控制,
	//控制4根数据线RB,B,G,RA的亮灭。注：对用bit为0：亮，为1：灭。默认：都亮
	nDataLineCtrl = 0x0F;
	//FIELD_NUM			总场数，最大为136场，做减1处理。
	nFieldNum = 12;
	//HALF_FIELD_NUM	半场数，最大为9场.默认为0x6，减1处理
	nHalfFieldNumber = 5;
	//FULL_FIELD_NUM	全场数，最大为128场
	nFullFieldNumber = 6;
	//起始场
	nStartField = 32;
	//终止场
	nEndField = 8;
	//DATA_POLARITY	4	数据极性：默认为0x0	0x0	高电平点亮	0x1	低电平点亮	0x2-0xF	其他14种情况，预留
	nDataPolarity = 0;
	//OE_POLARITY	0x1F	OE极性：默认为0x0	0x0	低有效	0x1	高有效
	nOePolarity = 0;
	
	///////////空点插入////////////
	//EMPTY_INSERT_EN			空点插入使能，即每多少点插入多少空点.	默认0x0，不使能
	bEmptyInsertEnable = 0;
	//INSERT_MODE				插入空点方式，前插入还是后插入。	1：前插入空点。0：后插入空点
	nInsertMode = 0;
	//EMPTY_DOT_NUM				插入的空点数，每次最大只能插入64空点，做减1处理。
	nEmptyDotNum = 1;
	//REAL_DOT_NUM		15--0	每多少点插入空点，做减1处理。
	nRealDotNum = 1;

	///////////空区插入////////////
	//EMPTY_INSERT_EN			空区插入使能，即每多少区插入多少空区.	默认0x0，不使能
	bEmptySectionInsertEnable = 0;
	//INSERT_MODE				插入空区方式，前插入还是后插入。	1：前插入空区。0：后插入空区
	nInsertSectionMode = 0;
	//EMPTY_DOT_NUM				插入的空区数，每次最大只能插入64空区，做减1处理。
	nEmptySectionNum = 1;
	//REAL_DOT_NUM		15--0	每多少点插入空区，做减1处理。
	nRealSectionNum = 1;


	//双列输出
	bDualOutput = 0;
	//old虚拟像素排布方式，0：红A绿/蓝红B，1：红A蓝/绿红B，2：绿红A/红B蓝，3：蓝红A/红B绿
	//new虚拟像素排布方式，0：红A,绿/蓝,红B，1：蓝,绿/空,红，2：蓝,绿/空,红;蓝,绿/红,空
	nVirTualArray = 0;
	//灯板芯片  0:通用芯片，1：28161/165，2：28162
	nChipType = _GENERAL;
	//ref_doule_value	刷新率倍增的倍数，
	nRefreshDoubleValue = 1;
	//zhe_rdwr_mode		折处理模块读写折DPRAM的方式。默认为0
	//0：按列8读写，1：按箱体行读写
	nZheRdwrMode = 0;
	//显示屏类型  0-全彩实像素，1-全彩虚拟
	nScreenType = DISPLAY_TYPE_REAL;
	//单点校正类型, 0-无 1-调亮，2-调色
	nDotCorrectTye = 0;

	//虚拟显示变化
	bVirtualChangeFlag = false;
	//原先虚拟显示
	bVirtualPrime = bVirtvalDisp;

	//测试效果/测试结束
	bTest = false;

	//亮度有效率
	fBrightnessEfficent = 0.5;
	//最小OE宽度
	nMinOEWidth = 8;

	//颜色深度变化标识
	bScanColorDepthChangeFlag = false;
	//原先颜色深度
	nScanColorDepthPrime = nScanColorDepth;
	//使能逐点开路检测功能 PWM版有,通用芯片无；1 - 使能， 0-不使能
	bDotOpenDetection = false;
	//PWM输出模式	MBI5030: 0-打撒模式 1-普通模式；TC62D722: 1-打撒模式，0-普通模式
	nPWMOutputMode = 0;

	//文件名称
	//sFileName = "";
	//刷新率倍增，PWM静态扫描下有效
	bMultiRefreshUnderStaticScan = false;
	//sunj 扩区
	//扩区使能
	bExtendedEnable = false;
	bExtendedEnableEx = false;
	//区宽
	nSectionWidth = nModuleWidth;
	//横向区个数
	nSectionHorNum = 1;
	//灰度增强位数
	nGrayEnhance = 0;
	//灰度增强方式
	nGrayEnhanceMode = 3;
	//箱体指示灯
	bOpenCabinetLamp = true;
	//2013-3-5通用芯片升级
	nSecondHighLevel = 2;
	fLightRatio = 1;
	nCustomGamam = 8;

	//芯片预充电功能
	bChipPrecharge = false;

	//RGB三色分别控制GCLK的延迟时间 使能
	bGClkCtrlByRGBEnable = false;

	//R色控制GCLK的延迟时间
	bGClkCtrlByREnable = false;
	//G色控制GCLK的延迟时间
	bGClkCtrlByGEnable = false;
	//B色控制GCLK的延迟时间
	bGClkCtrlByBEnable = false;

	//GCLK的延迟时钟数（三色分开时为R的GCLK延迟时钟数）
	nGClkDelay = 0;
	nGClkDelay_G = 0;//G的GCLK延迟时钟数
	nGClkDelay_B = 0;//B的GCLK延迟时钟数

	//if (_TLC5958 == nChipType)
	{
		//reg1
		nDrive_ic_reg.nBright = 4;//全局亮度调节
		nDrive_ic_reg.nLgse_R = 0;//红色低灰增强	
		nDrive_ic_reg.nLgse_G = 0;//绿色低灰增强
		nDrive_ic_reg.nLgse_B = 0;//蓝色低灰增强
		nDrive_ic_reg.nGdly_Enable = 1;//输出通道延迟使能
		nDrive_ic_reg.nTD_Delay = 1;//输入数据延迟
		nDrive_ic_reg.nLodvth = 1;//开路检测电压设定

		//reg2
		nDrive_ic_reg.nGlobal_Lgse = 0;//全局低灰增强
		nDrive_ic_reg.nPVM_Mode = 0;//打散模式
		nDrive_ic_reg.nEMI_R = 0;//红色EMI削减
		nDrive_ic_reg.nEMI_G = 0;//绿色EMI削减
		nDrive_ic_reg.nEMI_B = 0;//蓝色EMI削减	
		nDrive_ic_reg.nPre_Charge = 0;//预充电模式

		//_MBI5152/_MBI5153
		//reg1
		nDrive_ic_reg.nPre_Charge1 = 0;
		nDrive_ic_reg.nPwm_Count_Mode = 0;
		nDrive_ic_reg.nGray_Mode = 0;
		nDrive_ic_reg.nEnable_GCLK = 0;

		//reg2
		nDrive_ic_reg.nDouble_RefreseRate = 0;
		nDrive_ic_reg.nVoltage = 0;
		nDrive_ic_reg.nIC_Recognition = 1;
		nDrive_ic_reg.nAdjust_Red = 0;
		nDrive_ic_reg.nAdjust_Green = 0;
		nDrive_ic_reg.nAdjust_Blue = 0;
		nDrive_ic_reg.nImhl_DoNotStretch = 0;

		//新MBI5153，聚积开放了MBI5153驱动芯片的第三组寄存器的设置.
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RHigh = 0xFF;//寄存器1红高字节,默认值0x9F2B
		if (_MBI5155 == nChipType)
		{
			//灰阶cfg1[7] 16，14，默认14bit
			nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RLow = 0xAB; //寄存器1红低字节
		} 
		else//_MBI5153_E == m_nChipType
		{
			//灰阶cfg1[7] 14，13，默认14bit
			nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RLow = 0x2B; //寄存器1红低字节
		}
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1GHigh = 0xFF;//寄存器1绿高字节,默认值0xDF2B
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1GLow = 0x2B; //寄存器1绿低字节
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1BHigh = 0xFF;//寄存器1蓝高字节,默认值0xDF2B
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1BLow = 0x2B; //寄存器1蓝低字节

		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2RHigh = 0x0F;//寄存器2红高字节,默认值0x4600
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2RLow = 0x00; //寄存器2红低字节
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2GHigh = 0x06;//寄存器2绿高字节,默认值0x4500
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2GLow = 0x00; //寄存器2绿低字节
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2BHigh = 0x26;//寄存器2蓝高字节,默认值0x6500
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2BLow = 0x00; //寄存器2蓝低字节

		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RHigh = 0xC0;//寄存器3红高字节,默认值0xC003
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RLow = 0x03; //寄存器3红低字节
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3GHigh = 0x50;//寄存器3绿高字节,默认值0x5003
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3GLow = 0x03; //寄存器3绿低字节
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3BHigh = 0x40;//寄存器3蓝高字节,默认值0x4003
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3BLow = 0x03; //寄存器3蓝低字节

		nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT = 0;//MBI5155 第513/257个GCLK的低电平宽度 
		nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaF = 0;//MBI5155 第513/257个GCLK的高电平宽度 
		nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT = 30;//MBI5155 第1个GCLK的高电平宽度 
		nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_H = 34;//MBI5155 第514/258个GCLK的高电平宽度
		nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_L = 22;//MBI5155 第514/258个GCLK的低电平宽度 
	}

	//if (MBI5043 == nChipType)
	{
		//MBI5153 GCLK双沿采样	0：关闭， 1：开启
		nDrive_ic_reg.sDrive_ic_reg_MBI5043.bGCLKDoublesampling = false;
		//MBI5153 PWM模式选择	0: 16bit，1: 10bit
		nDrive_ic_reg.sDrive_ic_reg_MBI5043.nPWMMode = 0;
	}

	//网口优先设置
	//0：A口优先；1：B口优先
	nNetPortPriority = 0;

	//锁定优先网口
	bLockNetPort = false;

	//减去的色深位数,默认为4
	nDeductBit = 4;
}

CStructSingleScanCard::~CStructSingleScanCard(void)
{
}

//灰度级别转换成颜色深度
int CStructSingleScanCard::GrayLevel2ColorDepth(int nGrayLevel)
{
	int nColorDepth  = 8;

	if (nGrayLevel <= 5 && nGrayLevel >= 0)
	{
		nColorDepth = nGrayLevel + 1;
	}
	else if (nGrayLevel > 5 && nGrayLevel < 24)
	{
		nColorDepth = (nGrayLevel - 4 + 1) /2 + 1;
	}
	else if (nGrayLevel >= 24 && nGrayLevel < 33)
	{
		nColorDepth = (nGrayLevel - 24 ) / 3 + 11;
	}
	else if (nGrayLevel == 33 || nGrayLevel == 34)
	{
		nColorDepth = 14;
	}
	else if (nGrayLevel == 35)
	{
		nColorDepth = 15;
	}
	else if (nGrayLevel == 36)
	{
		nColorDepth = 16;
	}
	return nColorDepth;
}

//获取1区静态下的像素数
int CStructSingleScanCard::GetPixPerSetion(VersionType versionType)
{
	//1区静态下的像素数与数据线大类有关
	//1区静态下的像素数=(区行数*卡宽)/扫描方式=(区列数*卡高)/扫描方式 = (卡高*卡宽)/(区数*扫描方式)
	int nPixsPerSection = 1;
	if (nDataInputDir == 0 || nDataInputDir == 1)
	{
		if(bExtendedEnable || bExtendedEnableEx)
		{
			//扩区使能：1区静态下的像素数=(区行数*卡区宽)/扫描方式
			//nPixsPerSection =(nSectionWidth * nScanCardSectionRowNumber) / nScanMode;
			//nPixsPerSection =(nCard_zone_width * nScanCardSectionRowNumber) / nScanMode;
			if (0 == nScanCardSectionRowNumber % nScanMode)
			{
				nPixsPerSection = nCard_zone_width * (nScanCardSectionRowNumber / nScanMode);
			}
			else
			{
				nPixsPerSection = nCard_zone_width * (nScanCardSectionRowNumber / nScanMode + 1);
			}

			if (_SSeriesVersion == versionType)
			{
				if (nCard_zone_width % nScanMode)
				{
					nPixsPerSection = (nCard_zone_width / nScanMode + 1) * nScanCardSectionRowNumber;
				}
			}
		}
		else
		{
			//nPixsPerSection = (nScanCardWidth * nScanCardSectionRowNumber) / nScanMode;
			if (0 == nScanCardSectionRowNumber % nScanMode)
			{
				nPixsPerSection = nScanCardWidth * (nScanCardSectionRowNumber / nScanMode);
			}
			else
			{
				nPixsPerSection = nScanCardWidth * (nScanCardSectionRowNumber / nScanMode + 1);
			}

			if (_SSeriesVersion == versionType)
			{
				if (nCard_zone_width % nScanMode)
				{
					nPixsPerSection = (nScanCardWidth / nScanMode + 1) * nScanCardSectionRowNumber;
				}
			}
		}		
	}
	else if (nDataInputDir == 2 || nDataInputDir == 3)
	{
		//nPixsPerSection = (nScanCardHeight * nScanCardSectionRowNumber) / nScanMode;
		if (0 == nScanCardSectionRowNumber % nScanMode)
		{
			nPixsPerSection = nScanCardHeight * (nScanCardSectionRowNumber / nScanMode);
		}
		else
		{
			nPixsPerSection = nScanCardHeight * (nScanCardSectionRowNumber / nScanMode + 1);
		}

		if (_SSeriesVersion == versionType)
		{
			if (nCard_zone_width % nScanMode)
			{
				nPixsPerSection = (nCard_zone_width / nScanMode + 1) * nScanCardSectionRowNumber;
			}
		}
	}

	switch(nDataLineTypeRange)
	{
	case 0:
		//红绿蓝分开
		break;
	case 1:
		//红绿蓝合一三色1点串行
	case 2:
		//红绿蓝合一三色8点串行
	case 3:
		//红绿蓝合一三色16点串行
		nPixsPerSection = 3 * nPixsPerSection;//(三倍)
		break;
	case 4:
		//红绿蓝合一四色串行
		nPixsPerSection = 4 * nPixsPerSection;//(四倍)
		break;
	default:
		break;
	}
	//使用行信号D作为第二路时钟使带载高度加倍
	nPixsPerSection  *= (int)pow(2.0, nDCBlineClkEn);

	return nPixsPerSection;
}

//获取每区移数据(125MHz)的时钟数
int CStructSingleScanCard::GetZoneClkNum(VersionType versionTypev)
{
	//获取1区静态下的像素数
	int nPixsPerSection = GetPixPerSetion(versionTypev);

	//zone_clk_num	16	0x1207	15--0	每区移数据（125Mhz）的时钟数。
	//标准版：（1区静态下所带的像素数*分频系数+7） 
	//PWM版：1区静态下像素数*数据移位时钟分频系数（不减1）
	int nZoneClkNum = 0;
	if (_GENERAL == nChipType)
	{
		nZoneClkNum = nPixsPerSection * nFreqDivisionCoeff + 7;

		if (bEmptyInsertEnable)
		{
			nZoneClkNum = (int)((  (nPixsPerSection * nFreqDivisionCoeff) / (nRealDotNum * 1.0)) * (nRealDotNum + nEmptyDotNum)) + 7;
		}
	}
	else
	{
		if(_TLC5948 == nChipType)//TLC5948
		{
			int nModScanLamp = 0;
			if (nDataInputDir == 0 || nDataInputDir == 1)
			{
				nModScanLamp = nModuleWidth * nScanCardSectionRowNumber / nScanMode;
			}
			else if (nDataInputDir == 2 || nDataInputDir == 3)
			{
				nModScanLamp = nModuleHeight * nScanCardSectionRowNumber / nScanMode;
			}

			int nChipSizeForOneModule = nModScanLamp % LED_NUM_CONTROLED_BY_EACH_CHIP == 0 ?
				nModScanLamp / LED_NUM_CONTROLED_BY_EACH_CHIP 
				: nModScanLamp / LED_NUM_CONTROLED_BY_EACH_CHIP + 1;

			int nChipSize = 0;
			if (nDataInputDir == 0 || nDataInputDir == 1)
			{
				nChipSize = nScanCardWidth * nChipSizeForOneModule / nModuleWidth;
			}
			else if (nDataInputDir == 2 || nDataInputDir == 3)
			{
				nChipSize = nScanCardHeight * nChipSizeForOneModule / nModuleHeight;
			}

			//每区移数据(125MHz)的时钟数 = 257 * 一扫的驱动芯片数 * 移数据时钟分频系数 
			//257表示一个芯片驱动16个灯，每个灯的通道数据为16bit，16X16 + 1 = 257,1表示256数据前一位表示是视频还是灰度，
			nZoneClkNum = 257 * nFreqDivisionCoeff * nChipSize;
		}
		else
		{
			nZoneClkNum = nPixsPerSection * nFreqDivisionCoeff;
			//与空点插入有关
			if (bEmptyInsertEnable)
			{
				nZoneClkNum = (int)((nZoneClkNum / (nRealDotNum * 1.0)) * (nRealDotNum + nEmptyDotNum));
			}

			if (_MBI5043 == nChipType && 1 == nDrive_ic_reg.sDrive_ic_reg_MBI5043.nPWMMode)//0: 16bit，1: 10bit
			{
				nZoneClkNum = nZoneClkNum * 10 / 16;//5043芯片如果PWM模式为10bit，需要进行换算
			}
		}
	}

	return nZoneClkNum;
}

//获取刷新倍增值范围,刷新倍数为最小值到最大值之间的2的n次方
void CStructSingleScanCard::GetRefDoubleVableRange(int & nMultMin, int & nMultMax, VersionType versionType)
{
	nMultMin = 1;
	nMultMax = 1;

	//zone_clk_num	每区移数据（125Mhz）的时钟数
	int nZoneClkNum = GetZoneClkNum(versionType);

	if (_GENERAL == nChipType)
	{
		//通用版
		return;
	}
	else
	{
		//PWM静态扫描，刷新率倍增
		if (nScanMode == 1 && bMultiRefreshUnderStaticScan)
		{
			return;
		}

		//PWM
		if(_TLC5948 == nChipType)//TLC5948
		{
			//TLC5948是16个通道一起送，与通用芯片类似
			//Afield_clk_num_a1=颜色等级*gclk分频系数/刷新率倍增的倍数>Zone_clk_num + 100。

			//ref_double_vable < (gclk分频系数 * 颜色等级) / (Zone_clk_num + 100)
			double dTemp1 = nZoneClkNum + 100;
			double dTemp2 = nPWMFreqDivisionCoeff * pow(2.0, nScanColorDepth);
			nMultMax = (int)(dTemp2 / dTemp1);
			if (nMultMax == 0)
			{
				nMultMax = 1;
			}
			else if(nMultMax > 256)
			{
				nMultMax = 256;
			}
		}
		else if (_TLC5958 == nChipType 
			|| _MBI5152 == nChipType 
			|| _MBI5153 == nChipType)
		{
			nMultMin = 128;
			nMultMax = 256;
		}
		else if (_MBI5153_E == nChipType)//14bit模式，刷新倍数（32/64/128）；13bit模式时候，扫描卡暂时不支持
		{
			nMultMin = 32;
			nMultMax = 128;
		}
		else if (_MBI5155 == nChipType)
		{
			unsigned int nReg1R = (nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RHigh<<8) 
				+ nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RLow;//寄存器1红
			if (0 == ((nReg1R >> 7) & 0x01))//_MBI_5155 cfg1[7]=0时候，为16bit模式，刷新倍数（64/128/256）
			{
				nMultMin = 64;
				nMultMax = 256;
			}
			else //_MBI_5155 cfg1[7]=1时候，为14bit模式，刷新倍数（32/64/128）
			{
				nMultMin = 32;
				nMultMax = 128;
			}
		}
		else
		{
			//Afield_clk_num_a1=（颜色等级/刷新率倍增的倍数）*gclk分频系数>16*Zone_clk_num 100。

			//ref_double_vable < (gclk分频系数平方 * 颜色等级) / (dclk分频系数 * (16 * Zone_clk_num + 100))
// 			double dTemp1 = (16.0 * nZoneClkNum + 100);
// 			double dTemp2 =  nPWMFreqDivisionCoeff * pow(2.0, nScanColorDepth);
// 			nMultMax = (int)(dTemp2 / dTemp1);
// 			if (nMultMax == 0)
// 				nMultMax = 1;

			/*double dTemp1 = (16.0 * nZoneClkNum + 100) * nFreqDivisionCoeff;
			double dTemp2 = nPWMFreqDivisionCoeff * nPWMFreqDivisionCoeff * pow(2.0, nScanColorDepth);*/
			//sunj 扩区
			double dTemp1 = 16.0 * nZoneClkNum + 100;
			double dTemp2 = nPWMFreqDivisionCoeff * pow(2.0, nScanColorDepth);
			nMultMax = (int)(dTemp2 / dTemp1);
			if (nMultMax == 0)
			{
				nMultMax = 1;
			}
			else if(nMultMax > 256)
			{
				nMultMax = 256;
			}
		}
	}
}
//获取刷新频率范围
void CStructSingleScanCard::GetRefreshRateRange(int & nRateMin, int & nRateMax, int nRefreshDouble, VersionType versionType)
{
	nRateMin = SCANCARD_REFRESH_RATE_MIN;
	nRateMax = SCANCARD_REFRESH_RATE_MIN;


	//zone_clk_num	每区移数据（125Mhz）的时钟数
	int nZoneClkNum = GetZoneClkNum(versionType);

	if (_GENERAL == nChipType)
	{
		if (nStartField == 0)
		{
			nRateMax = nRateMin;
			return;
		}
		//通用版
		nZoneClkNum = (int (nZoneClkNum / nStartField + 1)) * nStartField;

		//刷新率最大值 = 150MHz / (Field_num * (zone_clk_num + 100) * scan_mode)
		/*double dTemp1 = nFieldNum * (nZoneClkNum + 120) * nScanMode;
		nRateMax = (int)(SCANCARD_MHZ * pow(10.0, 6) / dTemp1);*/
		nRateMax = (int)( SCANCARD_MHZ * pow(10.0,6) * nSecondHighLevel / (( nZoneClkNum + max(100,nOeClkNumber) )* nScanMode * nFieldNum ));
	}
	else
	{
		//PWM
		if(nScanMode == 1 && bMultiRefreshUnderStaticScan)
		{
			//静态扫描,刷新倍增
			//刷新率固定为最大：ref_freq = PWM时钟频率 / 颜色等级
			//PWM分频系数 = 150MHz / PWM时钟频率，PWM分频系数必须是整数，所以 ：ref_freq = 150MHz / (颜色等级 * PWM分频系数)
			nRateMax = (int) (SCANCARD_MHZ * pow(10.0, 6) / (nPWMFreqDivisionCoeff * pow(2.0, nScanColorDepth)));
			nRateMin = nRateMax;
		}
		else
		{
			double nAfieldClkNum = 0;
			if(_TLC5948 == nChipType)//TLC5948
			{
				nAfieldClkNum =  nPWMFreqDivisionCoeff * pow(2.0,nScanColorDepth) / nRefreshDouble;
				double dTemp2 = nZoneClkNum + 100;
				nAfieldClkNum = nAfieldClkNum > dTemp2 ? nAfieldClkNum : dTemp2;
			}
			else if (_TLC5958 == nChipType 
				|| _MBI5152 == nChipType 
				|| _MBI5153 == nChipType)
			{
				nAfieldClkNum =  nPWMFreqDivisionCoeff * pow(2.0,nScanColorDepth) / nRefreshDouble;
			}
			else if (_MBI5153_E == nChipType)
			{
				nAfieldClkNum =  nPWMFreqDivisionCoeff * pow(2.0,14) / nRefreshDouble;
			}
			else if (_MBI5155 == nChipType)
			{
				unsigned int nReg1R = (nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RHigh<<8) 
					+ nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RLow;//寄存器1红
				if (0 == ((nReg1R >> 7) & 0x01))//_MBI_5155 cfg1[7]=0时候，为16bit模式，刷新倍数（64/128/256）
				{
					nAfieldClkNum =  nPWMFreqDivisionCoeff * pow(2.0,16) / nRefreshDouble;
				}
				else //_MBI_5155 cfg1[7]=1时候，为14bit模式，刷新倍数（32/64/128）
				{
					nAfieldClkNum =  nPWMFreqDivisionCoeff * pow(2.0,14) / nRefreshDouble;
				}
			}
			else
			{
				nAfieldClkNum =  nPWMFreqDivisionCoeff * pow(2.0,nScanColorDepth) / nRefreshDouble;
				double dTemp2 = 16 * nZoneClkNum + 100;
				nAfieldClkNum = nAfieldClkNum > dTemp2 ? nAfieldClkNum : dTemp2;
			}

			if (_MBI5153_E == nChipType || _MBI5155 == nChipType)
			{
				//刷新率最小值:row_oe_num最大为65536，ref_fresh >= (1 - Cfg_time*帧频) * 125MHz / ((row_oe_clk_num + afield_clk_num) * scan_mode) 
				//Cfg_time=≈ 区时钟数时间 = Dclk分频系数 * 1/125M * 一个区静态像素点数
				int nPixsPerSection = GetPixPerSetion(versionType);
				int nRealPixsPerSection = 0;
				if(bEmptyInsertEnable)
				{
					nRealPixsPerSection = (int)((nPixsPerSection / (nRealDotNum * 1.0)) * (nRealDotNum + nEmptyDotNum));
				}
				else
				{
					nRealPixsPerSection = nPixsPerSection;
				}

				double fCfg_time = nRealPixsPerSection * nFreqDivisionCoeff / (SCANCARD_MHZ_MBI5153_E * pow(10.0, 6));

				double fTmp = SCANCARD_MHZ_MBI5153_E * pow(10.0, 6) / nScanMode;
				if (_MBI5155 == nChipType)
				{
					unsigned int nReg1R = (nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RHigh<<8) 
						+ nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RLow;//寄存器1红
					unsigned int nReg3R = (nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RHigh<<8) 
						+ nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RLow;//寄存器3红
					bool bOptimize = ((nReg3R >> 6) & 0x01) == 0 ? false : true;//渐变过度优化功能使能 cfg3 bit[6]
					int nLastGCLKClock = bOptimize 
						? (nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_H + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_L)
						: (nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT);
					if (0 == ((nReg1R >> 7) & 0x01))//_MBI_5155 cfg[7]=0时候，刷新倍数（64/128/256）为16bit模式（1024*64）
					{
						fTmp = fTmp * (1 - fCfg_time*MBI5153_FRAME) /(65536 + nAfieldClkNum + nLastGCLKClock);
					} 
					else //_MBI_5155 cfg[7]=1时候，刷新倍数（32/64/128）为14bit模式（512*32）
					{
						fTmp = fTmp * (1 - fCfg_time*MBI5153_FRAME) /(16384 + nAfieldClkNum + nLastGCLKClock);
					}

					nRateMin = (int) fTmp;

					//刷新率最大值：当oe_clk_num = 100时，ref_freq <= (1 - Cfg_time*帧频) * 125MHz / ((afield_clk_num + 100) * scan_mode)
					fTmp = SCANCARD_MHZ_MBI5153_E * pow(10.0, 6) / nScanMode;
					fTmp = fTmp * (1 - fCfg_time*MBI5153_FRAME) /(100 + nAfieldClkNum + nLastGCLKClock);//35是第256+1或者是128+1个Clock数，卢圣才提供
					nRateMax = (int) fTmp; 
				} 
				else
				{
					fTmp = fTmp * (1 - fCfg_time*MBI5153_FRAME) /(16384 + nAfieldClkNum + 35);
					nRateMin = (int) fTmp;

					//刷新率最大值：当oe_clk_num = 100时，ref_freq <= (1 - Cfg_time*帧频) * 125MHz / ((afield_clk_num + 100) * scan_mode)
					fTmp = SCANCARD_MHZ_MBI5153_E * pow(10.0, 6) / nScanMode;
					fTmp = fTmp * (1 - fCfg_time*MBI5153_FRAME) /(100 + nAfieldClkNum + 35);//35是第256+1或者是128+1个Clock数，卢圣才提供
					nRateMax = (int) fTmp; 
				}
			} 
			else
			{
				//刷新率最小值:row_oe_num最大为65536，ref_fresh >= 150MHz / ((row_oe_clk_num + afield_clk_num) * scan_mode) 
				double fTmp = SCANCARD_MHZ * pow(10.0, 6) / nScanMode;
				fTmp = fTmp /(65536 + nAfieldClkNum);
				nRateMin = (int) fTmp; 

				//刷新率最大值：当oe_clk_num = 100时，ref_freq <= 150MHz / ((afield_clk_num + 100) * scan_mode)
				fTmp = SCANCARD_MHZ * pow(10.0, 6) / nScanMode;
				fTmp = fTmp /(100 + nAfieldClkNum);
				nRateMax = (int) fTmp; 
			}
		}
	}
}

//获取min_oe_clk_num
long CStructSingleScanCard::GetMinOeClkNum(VersionType versionType)
{
	long nMinOeClkNum = 1;
	if (nRefreshRate == 0 || nFieldNum == 0 ||nScanMode == 0
		||nRefreshDoubleValue == 0 || nFreqDivisionCoeff == 0
		||nStartField == 0)
	{
		return nMinOeClkNum;
	}

	//zone_clk_num	16	0x1207	15--0	每区移数据（125Mhz）的时钟数。
	int nZoneClkNum = GetZoneClkNum(versionType);

	if(_GENERAL == nChipType)
	{
		long nAfieldClkNum = GetAfieldClkNum(versionType);
		nMinOeClkNum = nAfieldClkNum / nStartField;

		return nMinOeClkNum;
	}
	else
	{
		//afield_clk_num_a1	20	0x1300	15--0,15-12	
		//PWM版：颜色深度 * Pwm_clk_freq_div / ref_doule_value
		double nAfieldClkNum = GetAfieldClkNum(versionType);

		//min_oe_clk_num_a1	16	0x0026	11--0	
		//PWM版：配寄存器与逐点检测的时间(配芯片间隔时间)：
		//Config_ic_times*(Afield_clk_num+Row_oe_clk_num)*6.6ns
		int nRowOeClkNum = (int) (SCANCARD_MHZ * pow(10.0, 6) / (nRefreshRate * nScanMode) - nAfieldClkNum);
		if (_MBI5153_E == nChipType || _MBI5155 == nChipType)
		{
			//Cfg_time=≈ 区时钟数时间 = Dclk分频系数 * 1/125M * 一个区静态像素点数
			int nPixsPerSection = GetPixPerSetion(versionType);
			int nRealPixsPerSection = 0;
			if(bEmptyInsertEnable)
			{
				nRealPixsPerSection = (int)((nPixsPerSection / (nRealDotNum * 1.0)) * (nRealDotNum + nEmptyDotNum));
			}
			else
			{
				nRealPixsPerSection = nPixsPerSection;
			}

			double fCfg_time = nRealPixsPerSection * nFreqDivisionCoeff / (SCANCARD_MHZ_MBI5153_E * pow(10.0, 6));

			if (_MBI5155 == nChipType)
			{
				unsigned int nReg3R = (nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RHigh<<8) 
					+ nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RLow;//寄存器3红
				bool bOptimize = ((nReg3R >> 6) & 0x01) == 0 ? false : true;//渐变过度优化功能使能 cfg3 bit[6]
				int nLastGCLKClock = bOptimize 
					? (nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_H + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_L)
					: (nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT);

				nRowOeClkNum = (int) (SCANCARD_MHZ_MBI5153_E * pow(10.0, 6) * (1 - fCfg_time * MBI5153_FRAME) / (nRefreshRate * nScanMode) - nAfieldClkNum - nLastGCLKClock);//nLastGCLKClock是第256+1或者是128+1个Clock数，卢圣才提供
			} 
			else//_MBI5153_E == nChipType
			{
				nRowOeClkNum = (int) (SCANCARD_MHZ_MBI5153_E * pow(10.0, 6) * (1 - fCfg_time * MBI5153_FRAME) / (nRefreshRate * nScanMode) - nAfieldClkNum - 35);//35是第256+1或者是128+1个Clock数，卢圣才提供
			}
		}
		nRowOeClkNum = nRowOeClkNum > 0x0063 ? nRowOeClkNum : 0x0063;
		nMinOeClkNum = (int) (nConfigICTime * (nAfieldClkNum + nRowOeClkNum) * 6.6 * pow(10.0,-6) + 0.5);
	}

	return nMinOeClkNum;


}

//通过行消隐时间反推刷新率
long CStructSingleScanCard::GetRefreshRateByReadParam(long nMinOeClkNum,long nAfieldClkNum,int nRowOeClkNum, VersionType versionType)
{

	if ( nFieldNum == 0 ||nScanMode == 0
		||nRefreshDoubleValue == 0 || nFreqDivisionCoeff == 0
		||nMinOeClkNum == 0)
	{
		return 0;
	}

	if(_GENERAL == nChipType)
	{
		nRefreshRate = (int) (SCANCARD_MHZ * pow(10.0,6) * nSecondHighLevel / ((nMinOeClkNum * nStartField + 100) * nFieldNum * nScanMode));
	}
	else
	{
		if(nScanMode == 1 && bMultiRefreshUnderStaticScan)
		{
			nRefreshRate = (int) (SCANCARD_MHZ * 6.6  * nConfigICTime / (nMinOeClkNum * nScanMode));
		}
		else
		{
			nRefreshRate = (int) (SCANCARD_MHZ * pow(10.0, 6) / ((nAfieldClkNum + nRowOeClkNum) * nScanMode));
			if (_MBI5153_E == nChipType || _MBI5155 == nChipType)
			{
				//Cfg_time=≈ 区时钟数时间 = Dclk分频系数 * 1/125M * 一个区静态像素点数
				int nPixsPerSection = GetPixPerSetion(versionType);
				int nRealPixsPerSection = 0;
				if(bEmptyInsertEnable)
				{
					nRealPixsPerSection = (int)((nPixsPerSection / (nRealDotNum * 1.0)) * (nRealDotNum + nEmptyDotNum));
				}
				else
				{
					nRealPixsPerSection = nPixsPerSection;
				}
				double fCfg_time = nRealPixsPerSection * nFreqDivisionCoeff / (SCANCARD_MHZ_MBI5153_E * pow(10.0, 6));

				if (_MBI5155 == nChipType)
				{
					unsigned int nReg3R = (nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RHigh<<8) 
						+ nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RLow;//寄存器3红
					bool bOptimize = ((nReg3R >> 6) & 0x01) == 0 ? false : true;//渐变过度优化功能使能 cfg3 bit[6]
					int nLastGCLKClock = bOptimize 
						? (nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_H + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_L)
						: (nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT);

					nRefreshRate = (int) (SCANCARD_MHZ_MBI5153_E * pow(10.0, 6) * (1 - fCfg_time*MBI5153_FRAME) / ((nAfieldClkNum + nRowOeClkNum + nLastGCLKClock) * nScanMode));//nLastGCLKClock是第256+1或者是128+1个Clock数，卢圣才提供
				} 
				else//_MBI5153_E == nChipType
				{
					nRefreshRate = (int) (SCANCARD_MHZ_MBI5153_E * pow(10.0, 6) * (1 - fCfg_time*MBI5153_FRAME) / ((nAfieldClkNum + nRowOeClkNum + 35) * nScanMode));//35是第256+1或者是128+1个Clock数，卢圣才提供
				}
			}
		}
	}

	return 1;
}

//获取afield_clk_num_a1
long CStructSingleScanCard::GetAfieldClkNum(VersionType versionType)
{

	long nAfieldClkNum = 0;

	//afield_clk_num_a1	20	0x1300	15--0,15-12	
	//标准版：(150Mhz/(Ref_freq*Field_num*Scan_mode))-100
	//必须大于：Zone_clk_num + 100
	//PWM版：(颜色深度 *Pwm_clk_freq_div/ ref_doule_value)
	//须大于：16*Zone_clk_num + 100


	//zone_clk_num	16	0x1207	15--0	每区移数据（125Mhz）的时钟数。
	int nZoneClkNum = GetZoneClkNum(versionType);

	if (_GENERAL == nChipType)
	{
		//2013-3-5 通用芯片升级
		nAfieldClkNum = (long)(SCANCARD_MHZ * pow(10.0,6) * nSecondHighLevel / ( ( nRefreshRate * nFieldNum ) * nScanMode) - nOeClkNumber);

		nAfieldClkNum = nAfieldClkNum > nZoneClkNum + 100 ? nAfieldClkNum : nZoneClkNum + 100;

		//afield_clk_num_a1必须是起始场的倍数
		nAfieldClkNum = nAfieldClkNum % nStartField == 0 ? nAfieldClkNum : (nAfieldClkNum / nStartField + 1) * nStartField;
	}
	else
	{	
		if(nScanMode == 1 && bMultiRefreshUnderStaticScan)
		{
			//PWM静态扫描，刷新倍增
			//afield_clk_num_a1 = PWM时钟频率 / 显示屏屏幕刷新率 - row_oe_clk_num
			/*DEVMODE     lpDevMode;   
			::EnumDisplaySettings(NULL,   ENUM_CURRENT_SETTINGS,   &lpDevMode); 
			int nScreenRefreshFreq = lpDevMode.dmDisplayFrequency;
			nAfieldClkNum = (long) (fPWMScanClkFrequency * pow(10.0, 6) / nScreenRefreshFreq - nOeClkNumber);*/
			nAfieldClkNum = (long) (nPWMFreqDivisionCoeff * pow(2.0,nScanColorDepth) / nRefreshDoubleValue);
			long dTemp2 = nZoneClkNum + 100;
			nAfieldClkNum = nAfieldClkNum > dTemp2 ? nAfieldClkNum : dTemp2;
		}
		else
		{
			if(_TLC5948 == nChipType)//TLC5948
			{
				nAfieldClkNum = (long) (nPWMFreqDivisionCoeff * pow(2.0,nScanColorDepth) / nRefreshDoubleValue);
				long dTemp2 = nZoneClkNum + 100;
				nAfieldClkNum = nAfieldClkNum > dTemp2 ? nAfieldClkNum : dTemp2;
			}
			else if (_TLC5958 == nChipType 
				|| _MBI5152 == nChipType 
				|| _MBI5153 == nChipType)
			{
				nAfieldClkNum = (long) (nPWMFreqDivisionCoeff * pow(2.0,nScanColorDepth) / nRefreshDoubleValue);
			}
			else if (_MBI5153_E == nChipType)
			{
				nAfieldClkNum = (long) (nPWMFreqDivisionCoeff * pow(2.0,14) / nRefreshDoubleValue);
			}
			else if (_MBI5155 == nChipType)
			{
				unsigned int nReg1R = (nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RHigh<<8) 
					+ nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RLow;//寄存器1红
				if (0 == ((nReg1R >> 7) & 0x01))//_MBI_5155 cfg1[7]=0时候，为16bit模式，刷新倍数（64/128/256）
				{
					nAfieldClkNum = (long) (nPWMFreqDivisionCoeff * pow(2.0,16) / nRefreshDoubleValue);//65536;卢圣才说固定
				}
				else //_MBI_5155 cfg1[7]=1时候，为14bit模式，刷新倍数（32/64/128）
				{
					nAfieldClkNum = (long) (nPWMFreqDivisionCoeff * pow(2.0,14) / nRefreshDoubleValue);//16384;卢圣才说固定
				}
			}
			else
			{
				/*double dTemp1 = nPWMFreqDivisionCoeff * nPWMFreqDivisionCoeff * pow(2.0,nScanColorDepth);
				nAfieldClkNum = (long) (dTemp1 / (nRefreshDoubleValue * nFreqDivisionCoeff));*/
				nAfieldClkNum =  (long)(nPWMFreqDivisionCoeff * pow(2.0,nScanColorDepth) / nRefreshDoubleValue);
				long dTemp2 = 16 * nZoneClkNum + 100;
				nAfieldClkNum = nAfieldClkNum > dTemp2 ? nAfieldClkNum : dTemp2;
			}
		}			
	}
	return (long)nAfieldClkNum;
}

//获取行消隐时间
int CStructSingleScanCard::GetRowOeClkNum(VersionType versionType)
{
	if (_GENERAL == nChipType)
	{
		return nOeClkNumber;
	}
	else 
	{
		//PWM	
		if(nScanMode == 1 && bMultiRefreshUnderStaticScan)
		{
			return nOeClkNumber;
		}
		else
		{
			long lAfieldClkNum = GetAfieldClkNum(versionType);
			nOeClkNumber = (int) (SCANCARD_MHZ * pow(10.0, 6) / (nRefreshRate * nScanMode) - lAfieldClkNum);
			if (_MBI5153_E == nChipType || _MBI5155 == nChipType)
			{
				//Cfg_time=≈ 区时钟数时间 = Dclk分频系数 * 1/125M * 一个区静态像素点数
				int nPixsPerSection = GetPixPerSetion(versionType);
				int nRealPixsPerSection = 0;
				if(bEmptyInsertEnable)
				{
					nRealPixsPerSection = (int)((nPixsPerSection / (nRealDotNum * 1.0)) * (nRealDotNum + nEmptyDotNum));
				}
				else
				{
					nRealPixsPerSection = nPixsPerSection;
				}

				double fCfg_time = nRealPixsPerSection * nFreqDivisionCoeff / (SCANCARD_MHZ_MBI5153_E * pow(10.0, 6));

				if (_MBI5155 == nChipType)
				{
					unsigned int nReg3R = (nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RHigh<<8) 
						+ nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RLow;//寄存器3红
					bool bOptimize = ((nReg3R >> 6) & 0x01) == 0 ? false : true;//渐变过度优化功能使能 cfg3 bit[6]
					int nLastGCLKClock = bOptimize 
						? (nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_H + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_L)
						: (nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT);

					nOeClkNumber = (int) (SCANCARD_MHZ_MBI5153_E * pow(10.0, 6) * (1 - fCfg_time*MBI5153_FRAME) / (nRefreshRate * nScanMode) - lAfieldClkNum - nLastGCLKClock);//nLastGCLKClock是第256+1或者是128+1个Clock数，卢圣才提供
				} 
				else//_MBI5153_E == nChipType
				{
					nOeClkNumber = (int) (SCANCARD_MHZ_MBI5153_E * pow(10.0, 6) * (1 - fCfg_time*MBI5153_FRAME) / (nRefreshRate * nScanMode) - lAfieldClkNum - 35);//35是第256+1或者是128+1个Clock数，卢圣才提供
				}
			}

			nOeClkNumber = nOeClkNumber > 0x0063 ? nOeClkNumber : 0x0063;
		}
	}
	return nOeClkNumber;
}

//获取1个模组视频数据对应总的校正参数；
long CStructSingleScanCard::GetModCoefByte(int nEmptyByte)
{
	//mod_coef_bytes_a1	16	0x2000	15--0	1个模组视频数据对应总的校正参数。
	//实像素虚拟像素单点调亮：左右进线：[(mod_width * 4 + x)/ 32] * 64 * mod_heigh； 上下进线：模组宽度*8*模组高度
	//实像素单点调色：[(mod_width * 9 + x)/ 32] * 64 * mod_heigh
	long nCoefByteNum;
	int nTemp;
	int nModWidth16 = nModuleWidth % 16 ? (nModuleWidth / 16 + 1) * 16 : nModuleWidth;
	nModWidth16 = nModWidth16 > 42 ? nModuleWidth : nModWidth16;

	//校正数据首部加空字节
	if (nDotCorrectTye == 0 || nDotCorrectTye == 1)//调亮
	{
		if (nDataInputDir == 0 || nDataInputDir == 1)//左右进线
		{
			nTemp = (nModWidth16 * 4) % 32 ? (nModWidth16 * 4 / 32 + 1) : nModWidth16 * 4 / 32;
			nCoefByteNum = nTemp * 64 * nModuleHeight;
		}
		else if (nDataInputDir == 2 || nDataInputDir == 3)//上下进线
		{
			//nCoefByteNum = nModuleWidth * 8 * nModuleHeight;

			int nModW = (int) (nModuleWidth * pow(2.0, nDCBlineClkEn));

			short nModW16 = nModW % 16 ? (nModW / 16 + 1) * 16 : nModW;

			nTemp = (nModW16 * 4) % 32 ? (nModW16 * 4 / 32 + 1) : nModW16 * 4 / 32;
			nCoefByteNum = nTemp * 64 * nModuleHeight;
		}

	}
	else if (nDotCorrectTye == 2)//调色
	{
		nTemp = (nModWidth16 * 9) % 32 ? (nModWidth16 * 9 / 32 + 1) : nModWidth16 * 9 / 32;
		nCoefByteNum= nTemp * 64 * nModuleHeight;
	}
	nCoefByteNum += nEmptyByte;
	return nCoefByteNum;
}

//获取亮度有效率，与场信息，分频系数有关
void CStructSingleScanCard::GetBrightnessEffective(VersionType versionType)
{
	//亮度有效率 = afield_clk_num_a1 / (afield_clk_num_a1 + row_oe_clk_num)
	int nAfieldClkNum = GetAfieldClkNum(versionType);
	long nRowOeClkNum = GetRowOeClkNum(versionType);
	if(_GENERAL == nChipType)
	{
		fBrightnessEfficent = (float) (nAfieldClkNum / (double (nAfieldClkNum + nRowOeClkNum)))* fLightRatio;
	}
	else
	{
		fBrightnessEfficent = (float) (nAfieldClkNum / (double (nAfieldClkNum + nRowOeClkNum)));
	}
}
//根据灰度等级确定场信息
bool CStructSingleScanCard::SetFieldNumByGrayLevel()
{
	/* B50去掉
	if (_GENERAL == nChipType )//如果是通用芯片
	{
		float min = (float)((( pow(2.0,16) -1) )/pow(2.0,9));
		float max = 136;
		switch (nScanColorDepth)
		{
		case 8:
			nHalfFieldNumber =(short) (1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16);//半场数
			nSecondHighLevel = 4;
			min = (float)(min - 64 - 32 -16 - 1.0 / 32-1.0 /64 - 1.0 / 128 - 1.0 / 256 - 1.0 /512);
			max = max - 64-16 - 32 - 5;
			fLightRatio = min / max;
			nFieldNum = (short) max;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 16;//起始场（场数的最小值的分母）
			nEndField = 8;//终止场（场数的最大值）
			break;
		case 9:
			nHalfFieldNumber =(short) (1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16 + 1.0 /32);//半场数
			nSecondHighLevel= 8;
			min = (float)(min - 64 - 32 -16 - 1.0 /64 - 1.0 / 128 - 1.0 / 256 - 1.0 /512);
			max = max - 64-16 - 32 - 4;
			fLightRatio = min / max;
			nFieldNum =(short) max;
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 32;//起始场（场数的最小值的分母）
			nEndField = 8;//终止场（场数的最大值）
			break;
		case 10:
			nHalfFieldNumber =(short) (1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16 + 1.0 /32);//半场数
			nSecondHighLevel = 8;
			min =(float) (min - 64 - 32 - 1.0 /64 - 1.0 / 128 - 1.0 / 256 - 1.0 /512);
			max = max - 64 - 32 - 4;
			fLightRatio = min / max;
			nFieldNum =(short) max;
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 32;//起始场（场数的最小值的分母）
			nEndField = 16;//终止场（场数的最大值）
			break;
		case 11:
			nHalfFieldNumber =(short) (1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16 + 1.0 /32 + 1.0 / 64);//半场数
			nSecondHighLevel = 16;
			min =(float) (min - 64 - 32 - 1.0 / 128 - 1.0 / 256 - 1.0 /512);
			max = max - 64 - 32 - 3;
			fLightRatio = min / max;
			nFieldNum = (short)max;
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 64;//起始场（场数的最小值的分母）
			nEndField = 16;//终止场（场数的最大值）
			break;
		case 12:
			nHalfFieldNumber =(short) ( 1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16 + 1.0 /32 + 1.0 / 64);//半场数
			nSecondHighLevel = 16;
			min = (float)(min - 64 - 1.0 / 128 - 1.0 / 256 - 1.0 /512);
			max = max - 64 - 3;
			fLightRatio = min / max;
			nFieldNum =(short) max;
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 64;//起始场（场数的最小值的分母）
			nEndField = 32;//终止场（场数的最大值）
			break;
		case 13:
			nHalfFieldNumber = (short) (1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16 + 1.0 /32 + 1.0 / 64 + 1.0 / 128);//半场数
			nSecondHighLevel = 32;
			min = (float)(min - 64 - 1.0 / 256 - 1.0 /512);
			max = max - 2 - 64;
			fLightRatio = min / max;
			nFieldNum =(short) max;
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 128;//起始场（场数的最小值的分母）
			nEndField = 32;//终止场（场数的最大值）
			break;
		case 14:
			nHalfFieldNumber = (short) (1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16 + 1.0 /32 + 1.0 / 64 + 1.0 / 128) ;//半场数
			nSecondHighLevel = 32;
			min =(float)( min - 1.0 / 256 - 1.0 /512);
			max = max -2;
			fLightRatio = min / max;
			nFieldNum =(short) max;
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 128;//起始场（场数的最小值的分母）
			nEndField = 64;//终止场（场数的最大值）
			break;
		case 15:
			nHalfFieldNumber = (short) (1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16 + 1.0 /32 + 1.0 / 64 + 1.0 / 128 + 1.0 / 256);
			nSecondHighLevel= 32;
			min = (float)(min - 1.0/ 512) ;
			max = max - 1;
			fLightRatio = min / max;
			nFieldNum =(short) max;
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 256;//起始场（场数的最小值的分母）
			nEndField = 64;//终止场（场数的最大值）
			break;
		case 16:
			nHalfFieldNumber = (short) (1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16 + 1.0 /32 + 1.0 / 64 + 1.0 / 128 + 1.0 / 256 + 1.0 / 512);
			nSecondHighLevel= 32;
			fLightRatio = min / max;
			nFieldNum = 136;
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 512;//起始场（场数的最小值的分母）
			nEndField = 64;//终止场（场数的最大值）
			break;
		default:
			break;

		}
	}
	else */
	{
		//全场数 = 总场数 - 半场数
		switch (nGrayLedvel)
		{
		case 0:
			nHalfFieldNumber = 0;//半场数
			nFieldNum = 1;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 1;//起始场（场数的最小值的分母）
			nEndField = 1;//终止场（场数的最大值）
			break;
		case 1:
			nHalfFieldNumber = 0;//半场数
			nFieldNum = 3;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 1;
			nEndField = 2;//终止场
			break;
		case 2:
			nHalfFieldNumber = 0;//半场数
			nFieldNum = 7;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 1;
			nEndField = 4;//终止场
			break;
		case 3:
			nHalfFieldNumber = 0;//半场数
			nFieldNum = 15;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 1;
			nEndField = 8;//终止场
			break;
		case 4:
			nHalfFieldNumber = 0;//半场数
			nFieldNum = 31;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 1;
			nEndField = 16;//终止场
			break;
		case 5:
			nHalfFieldNumber = 0;//半场数
			nFieldNum = 63;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 1;
			nEndField = 32;//终止场
			break;
		case 6:
			nHalfFieldNumber = 1;//半场数
			nFieldNum = 2;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 2;
			nEndField = 1;//终止场
			break;
		case 7:
			nHalfFieldNumber = 0;//半场数
			nFieldNum = 3;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 1;
			nEndField = 2;//终止场
			break;
		case 8:
			nHalfFieldNumber = 1;//半场数
			nFieldNum = 4;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 2;
			nEndField = 2;//终止场
			break;
		case 9:
			nHalfFieldNumber = 0;//半场数
			nFieldNum = 7;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 1;
			nEndField = 4;//终止场
			break;
		case 10:
			nHalfFieldNumber = 2;//半场数
			nFieldNum = 5;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 4;
			nEndField = 2;//终止场
			break;
		case 11:
			nHalfFieldNumber = 1;//半场数
			nFieldNum = 8;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 2;
			nEndField = 4;//终止场
			break;
		case 12:
			nHalfFieldNumber = 3;//半场数
			nFieldNum = 6;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 8;
			nEndField = 2;//终止场
			break;
		case 13:
			nHalfFieldNumber = 2;//半场数
			nFieldNum = 9;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 4;
			nEndField = 4;//终止场
			break;
		case 14:
			nHalfFieldNumber = 3;//半场数
			nFieldNum = 10;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 8;
			nEndField = 4;//终止场
			break;
		case 15:
			nHalfFieldNumber = 2;//半场数
			nFieldNum = 17;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 4;
			nEndField = 8;//终止场
			break;
		case 16:
			nHalfFieldNumber = 4;//半场数
			nFieldNum = 11;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 16;
			nEndField = 4;//终止场
			break;
		case 17:
			nHalfFieldNumber = 3;//半场数
			nFieldNum = 18;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 8;
			nEndField = 8;//终止场
			break;
		case 18:
			nHalfFieldNumber = 5;//半场数
			nFieldNum = 12;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 32;
			nEndField = 4;//终止场
			break;
		case 19:
			nHalfFieldNumber = 4;//半场数
			nFieldNum = 19;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 16;
			nEndField = 8;//终止场
			break;
		case 20:
			nHalfFieldNumber = 6;//半场数
			nFieldNum = 13;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 64;
			nEndField = 4;//终止场
			break;
		case 21:
			nHalfFieldNumber = 5;//半场数
			nFieldNum = 20;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 32;
			nEndField = 8;//终止场
			break;
		case 22:
			nHalfFieldNumber = 7;//半场数
			nFieldNum = 14;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 128;
			nEndField = 4;//终止场
			break;
		case 23:
			nHalfFieldNumber = 6;//半场数
			nFieldNum = 21;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 64;
			nEndField = 8;//终止场
			break;
		case 24:
			nHalfFieldNumber = 8;//半场数
			nFieldNum = 15;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 256;
			nEndField = 4;//终止场
			break;
		case 25:
			nHalfFieldNumber = 7;//半场数
			nFieldNum = 22;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 128;
			nEndField = 8;//终止场
			break;
		case 26:
			nHalfFieldNumber = 6;//半场数
			nFieldNum = 37;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 64;
			nEndField = 16;//终止场
			break;
		case 27:
			nHalfFieldNumber = 8;//半场数
			nFieldNum = 23;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 256;
			nEndField = 8;//终止场
			break;
		case 28:
			nHalfFieldNumber = 7;//半场数
			nFieldNum = 38;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 128;
			nEndField = 16;//终止场
			break;
		case 29:
			nHalfFieldNumber = 6;//半场数
			nFieldNum = 69;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 64;
			nEndField = 32;//终止场
			break;
		case 30:
			nHalfFieldNumber = 8;//半场数
			nFieldNum = 39;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 256;
			nEndField = 16;//终止场
			break;
		case 31:
			nHalfFieldNumber = 7;//半场数
			nFieldNum = 70;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 128;
			nEndField = 32;//终止场
			break;
		case 32:
			nHalfFieldNumber = 6;//半场数
			nFieldNum = 133;//总场数
			nFullFieldNumber = nFullFieldNumber - nHalfFieldNumber;//全场数
			nStartField = 64;
			nEndField = 64;//终止场
			break;
		case 33:
			nHalfFieldNumber = 8;//半场数
			nFieldNum = 71;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 256;
			nEndField = 32;//终止场
			break;
		case 34:
			nHalfFieldNumber = 7;//半场数
			nFieldNum = 134;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 128;
			nEndField = 64;//终止场
			break;
		case 35:
			nHalfFieldNumber = 8;//半场数
			nFieldNum = 135;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 256;
			nEndField = 64;//终止场
			break;
		case 36:
			nHalfFieldNumber = 9;//半场数
			nFieldNum = 136;//总场数
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//全场数
			nStartField = 512;
			nEndField = 64;//终止场
			break;
		default:
			break;
		}
	}

	return true;
}
//数据线排列查找表，0--black，1-- redA，2--green，3--blue, 4--redB
short CStructSingleScanCard::LookupDataLineType( short nState4, short nState3, short nState2, short nState1)
{
	short nLineType = 0;
	const short nBlack = 0;
	const short nRedA =  1;
	const short nGreen = 2;
	const short nBlue = 3;
	const short nRedB = 4;

	if (nState1 == nRedA && nState2 == nGreen && nState3 == nBlue && nState4 == nRedB)
	{
		nLineType = 0x00;
	}
	else if (nState1 == nRedA && nState2 == nGreen && nState3 == nRedB && nState4 == nBlue)
	{
		nLineType = 0x01;
	}
	else if (nState1 == nRedA && nState2 == nBlue && nState3 == nGreen && nState4 == nRedB)
	{
		nLineType = 0x02;
	}
	else if (nState1 == nRedA && nState2 == nBlue && nState3 == nRedB && nState4 == nGreen)
	{
		nLineType = 0x03;
	}
	else if (nState1 == nRedA && nState2 == nRedB && nState3 == nBlue && nState4 == nGreen)
	{
		nLineType = 0x04;
	}
	else if (nState1 == nRedA && nState2 == nRedB && nState3 == nGreen && nState4 == nBlue)
	{
		nLineType = 0x05;
	}
	else if (nState1 == nGreen && nState2 == nRedA && nState3 == nBlue && nState4 == nRedB)
	{
		nLineType = 0x06;
	}
	else if (nState1 == nGreen && nState2 == nRedA && nState3 == nRedB && nState4 == nBlue)
	{
		nLineType = 0x07;
	}
	else if (nState1 == nGreen && nState2 == nBlue && nState3 == nRedA && nState4 == nRedB)
	{
		nLineType = 0x08;
	}
	else if (nState1 == nGreen && nState2 == nBlue && nState3 == nRedB && nState4 == nRedA)
	{
		nLineType = 0x09;
	}
	else if (nState1 == nGreen && nState2 == nRedB && nState3 == nBlue && nState4 == nRedA)
	{
		nLineType = 0x0A;
	}
	else if (nState1 == nGreen && nState2 == nRedB && nState3 == nRedA && nState4 == nBlue)
	{
		nLineType = 0x0B;
	}
	else if (nState1 == nBlue && nState2 == nRedA && nState3 == nGreen && nState4 == nRedB)
	{
		nLineType = 0x0C;
	}
	else if (nState1 == nBlue && nState2 == nRedA && nState3 == nRedB && nState4 == nGreen)
	{
		nLineType = 0x0D;
	}
	else if (nState1 == nBlue && nState2 == nGreen && nState3 == nRedA && nState4 == nRedB)
	{
		nLineType = 0x0E;
	}
	else if (nState1 == nBlue && nState2 == nGreen && nState3 == nRedB && nState4 == nRedA)
	{
		nLineType = 0x0F;
	}
	else if (nState1 == nBlue && nState2 == nRedB && nState3 == nRedA && nState4 == nGreen)
	{
		nLineType = 0x10;
	}
	else if (nState1 == nBlue && nState2 == nRedB && nState3 == nGreen && nState4 == nRedA)
	{
		nLineType = 0x11;
	}
	else if (nState1 == nRedB && nState2 == nGreen && nState3 == nBlue && nState4 == nRedA)
	{
		nLineType = 0x12;
	}
	else if (nState1 == nRedB && nState2 == nGreen && nState3 == nRedA && nState4 == nBlue)
	{
		nLineType = 0x13;
	}
	else if (nState1 == nRedB && nState2 == nBlue && nState3 == nGreen && nState4 == nRedA)
	{
		nLineType = 0x14;
	}
	else if (nState1 == nRedB && nState2 == nBlue && nState3 == nRedA && nState4 == nGreen)
	{
		nLineType = 0x15;
	}
	else if (nState1 == nRedB && nState2 == nRedA && nState3 == nGreen && nState4 == nBlue)
	{
		nLineType = 0x16;
	}
	else if (nState1 == nRedB && nState2 == nRedA && nState3 == nBlue && nState4 == nGreen)
	{
		nLineType = 0x17;
	}
	else
	{
		;
	}

	return nLineType;
}

//从配置文件中读取
/*
bool CStructSingleScanCard::ReadConfig(const char * chOpenFile)
{
	CIniFile iniFile;
	iniFile.SetPath(chOpenFile);  //设置ini文件路径

	char chSection[260] = _T("screen");
	char chKey[260];

	bool bExit = iniFile.SectionExist(chSection);
	if(!bExit)
	{
		return false;
	}
	
	//减去的色深位数
	strcpy(chKey,_T("DeductBit"));
	iniFile.GetKeyValue(chSection,chKey,"4",chKey);
	nDeductBit = atoi(chKey);


	//ONE_SCAN_CARD_WIDTH	2	128	单块扫描控制卡所对应的LED显示模块中横向像素点的数量，
	//即扫描卡宽度。高位在前，低位在后。取值范围1~256，必须是16的整数倍
	strcpy(chKey,_T("ONE_SCAN_CARD_WIDTH"));
	iniFile.GetKeyValue(chSection,chKey,"128",chKey);
	nScanCardWidth = atoi(chKey);

	nScanCardWidthReal = nScanCardWidth;

	//ONE_SCAN_CARD_HEIGHT	2	96	单块扫描控制卡所对应的LED显示模块中纵向像素点的数量，
	//即扫描卡高度。高位在前，低位在后。取值范围1~256，必须是16的整数倍
	strcpy(chKey,_T("ONE_SCAN_CARD_HEIGHT"));
	iniFile.GetKeyValue(chSection,chKey,"96",chKey);
	nScanCardHeight = atoi(chKey);

	nScanCardHeightReal = nScanCardHeight;

	//mod_width 1   32  模组宽度，取值范围1-64
	strcpy(chKey,_T("mod_width"));
	iniFile.GetKeyValue(chSection,chKey,"32",chKey);
	nModuleWidth = atoi(chKey);

	//mod_height 1   32  模组高度，取值范围1-64
	strcpy(chKey,_T("mod_height"));
	iniFile.GetKeyValue(chSection,chKey,"32",chKey);
	nModuleHeight = atoi(chKey);

	//1   3   模组区数  模组区数 = 模组高度/每区行数
	strcpy(chKey,_T("mod_section_number"));
	iniFile.GetKeyValue(chSection,chKey,"3",chKey);
	nModuleSectionNumber = atoi(chKey);

	//1    4  模组横向个数，模组横向个数 = 扫描卡宽度/模组宽度
	strcpy(chKey,_T("mod_hor_number"));
	iniFile.GetKeyValue(chSection,chKey,"4",chKey);
	nModuleHorNum = atoi(chKey);

	//1    3  模组纵向个数，模组纵向个数 = 扫描卡高度、模组高度
	strcpy(chKey,_T("mod_ver_number"));
	iniFile.GetKeyValue(chSection,chKey,"3",chKey);
	nModuleVerNum = atoi(chKey);

	//SCAN_CARD_SECTION_NUM	1	6	扫描卡的区数，区数最大为16
	strcpy(chKey,_T("SCAN_CARD_SECTION_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"16",chKey);
	nScanCardSectionNumber = atoi(chKey);

	//SCAN_CARD_SECTION_ROW_NUM	1	16	扫描卡每区行数，行数最大为16行
	strcpy(chKey,_T("SCAN_CARD_SECTION_ROW_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"16",chKey);
	nScanCardSectionRowNumber = atoi(chKey);

	//S系列参数添加：箱体参数
	//SCAN_CARD_SECTION_NUM_CABINET	1	6	扫描卡的区数，区数最大为16
	strcpy(chKey,_T("SCAN_CARD_SECTION_NUM_CABINET"));
	iniFile.GetKeyValue(chSection,chKey,"16",chKey);
	nScanCardSectionNumber_cabinet = atoi(chKey);

	//SCAN_CARD_SECTION_ROW_NUM_CABINET	1	16	扫描卡每区行数，行数最大为16行
	strcpy(chKey,_T("SCAN_CARD_SECTION_ROW_NUM_CABINET"));
	iniFile.GetKeyValue(chSection,chKey,"16",chKey);
	nScanCardSectionRowNumber_cabinet = atoi(chKey);


	//SCAN_COLOR_DEPTH	1	14	扫描卡扫描的颜色深度，取12~16的整数。
	strcpy(chKey,_T("SCAN_COLOR_DEPTH"));
	iniFile.GetKeyValue(chSection,chKey,"8",chKey);
	nScanColorDepth = atoi(chKey);

	//GRAY_LEVEL 扫描卡灰度级别
	strcpy(chKey,_T("GRAY_LEVEL"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nGrayLedvel = atoi(chKey);

	//origin_color_bit		原始颜色深度，比如8BIT,10bit，12bit，16bit
	strcpy(chKey,_T("origin_color_bit"));
	iniFile.GetKeyValue(chSection,chKey,"8",chKey);
	nOrginColorBit = atoi(chKey);

	//SCAN_MODE	1	4	扫描的模式，取 1-2-4-8-16
	strcpy(chKey,_T("SCAN_MODE"));
	iniFile.GetKeyValue(chSection,chKey,"16",chKey);
	nScanMode = atoi(chKey);

	//SCAN_MODE_CABINET	1	4	箱体参数：扫描的模式，送数据，S系列箱体使用
	strcpy(chKey,_T("SCAN_MODE_CABINET"));
	iniFile.GetKeyValue(chSection,chKey,"16",chKey);
	nScanMode_cabinet = atoi(chKey);


	//DOT_CORRECTION_EN	1	1	单点校正使能，取0，1
	strcpy(chKey,_T("DOT_CORRECTION_EN"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bEmendBrightness = atoi(chKey) == 1 ? true : false;

	//SCAN_GCLK_FREQUENCY	1	12.5	扫描时钟频率，最大30Mhz
	strcpy(chKey,_T("SCAN_GCLK_FREQUENCY"));
	iniFile.GetKeyValue(chSection,chKey,"12.5",chKey);
	fScanClkFrequency = (float)atof(chKey);

	//ZONE_DCLK_NUM	2		每区移位时钟数，256*16
	strcpy(chKey,_T("ZONE_DCLK_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"0x1207",chKey);
	nZoneDClkNum = atoi(chKey);

	//1  0-100  占空比  扫描时钟的占空比
	strcpy(chKey,_T("duty_cycle_low_value"));
	iniFile.GetKeyValue(chSection,chKey,"50",chKey);
	nDutyCycle = atoi(chKey);

	//PWM_SCAN_GCLK_FREQUENCY	1	12.5	PWM时钟时钟频率，最大30Mhz
	strcpy(chKey,_T("PWM_SCAN_GCLK_FREQUENCY"));
	iniFile.GetKeyValue(chSection,chKey,"12.5",chKey);
	fPWMScanClkFrequency = (float)atof(chKey);

	//1  0-100  PWM时钟占空比可调等级 
	strcpy(chKey,_T("pwm_duty_cycle_low_value"));
	iniFile.GetKeyValue(chSection,chKey,"50",chKey);
	nPWMDutyCycle = atoi(chKey);

	//CLR_CLK_NUM	1	4	1~255 消隐时钟数(行消隐时间)
	strcpy(chKey,_T("CLR_CLK_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"4",chKey);
	nOeClkNumber = atoi(chKey);

	//刷新频率 根据颜色深度和扫描方式不同而不同
	strcpy(chKey,_T("refresh_rate"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nRefreshRate = atoi(chKey);

	//REF_FREQ_MIN 刷新率最小值
	strcpy(chKey,_T("refresh_rate_min"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nRefreshRateMin = atoi(chKey);

	//REF_FREQ_MAX 刷新率最大值
	strcpy(chKey,_T("refresh_rate_max"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nRefreshRateMax = atoi(chKey);

	//CONFIG_IC_TIME	配寄存器与逐点检测的时间(芯片间隔时间)， 1ms--30s
	strcpy(chKey,_T("config_ic_time"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	nConfigICTime = atoi(chKey);

	//dat_freq_num   换帧频率计数器
	strcpy(chKey,_T("dat_freq_num"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nDatFreqNum = atoi(chKey);

	//OE_DELAY_VALUE	0x01	消隐延迟时钟数，消除暗亮，默认为0x01，做减1处理。
	strcpy(chKey,_T("OE_DELAY_VALUE"));
	iniFile.GetKeyValue(chSection,chKey,"2",chKey);
	nOeDelayValue = atoi(chKey);

	//SYN_REFRESH_EN		同步刷新使能。默认为0x1，使能
	strcpy(chKey,_T("SYN_REFRESH_EN"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	bSyncRefresh = atoi(chKey) == 1 ? true : false;

	//VIRTUAL_DISP_EN			虚拟显示使能，默认为0x0，不使能
	strcpy(chKey,_T("VIRTUAL_DISP_EN"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bVirtvalDisp = atoi(chKey) == 1 ? true : false;

	//FREQ_DIVISION_COEF	0x7		150Mhz的分频系数，最大为200分频，值为0.625Mhz。默认为10分频，值为0x09。做减1处理。
	strcpy(chKey,_T("FREQ_DIVISION_COEF"));
	iniFile.GetKeyValue(chSection,chKey,"100",chKey);
	nFreqDivisionCoeff = atoi(chKey);

	//PWM_FREQ_DIVISION_COEF	0x7		125Mhz的分频系数，最大为200分频，值为0.625Mhz。默认为10分频，值为0x09。做减1处理。
	strcpy(chKey,_T("PWM_FREQ_DIVISION_COEF"));
	iniFile.GetKeyValue(chSection,chKey,"100",chKey);
	nPWMFreqDivisionCoeff = atoi(chKey);

	//DATA_OUTPUT_REVERSE	输出口逆序包括数据线和扫描线	0x00	数据线逆序：默认为0x0	0x0	不使能	0x1	使能
	strcpy(chKey,_T("DATA_OUTPUT_REVERSE"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bDataOutUpReverse = atoi(chKey) == 1 ? true : false;

	//SCAN_OUTPUT_REVERSE		0x00	扫描线逆序：默认为0x0	0x0	不使能	0x1	使能
	strcpy(chKey,_T("SCAN_OUTPUT_REVERSE"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bScanOutUpReverse = atoi(chKey) == 1 ? true : false;

	//	DCB_LINE_CLK_EN			0x00	使能行信号DCB为时钟使带载高度加倍;	0x0	不使能	0x1	2倍	0x2	3倍	0x3	4倍
	strcpy(chKey,_T("DCB_LINE_CLK_EN"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nDCBlineClkEn = atoi(chKey);

	//NO_SIGNAL_DISP	无信号显示：默认为0		0x0：黑屏，0x1：随机画面。0x2：图片
	strcpy(chKey,_T("NO_SIGNAL_DISP"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nNoSingleDisp = atoi(chKey);

	//DATA_INPUT_DIR		0x32	数据方向：（从显示屏正面看）	
	//默认为从右到左0x1	0x0	从左往右	0x1	从右往左	0x2	从上往下	0x3	从下往上
	strcpy(chKey,_T("DATA_INPUT_DIR"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	nDataInputDir = atoi(chKey);

	//ROW_DECODE_MODE		行译码方式：默认0x2
	//	0x0	静态无译码					0x6	164译码
	//	0x1	无译码芯片，直接驱动行管	0x7	192译码
	//	0x2	138译码						0x8	193译码
	//	0x3	139译码						0x9	595译码
	//	0x4	145译码或138双O				0xA	4096译码
	//	0x5	154译码						0xB	
	strcpy(chKey,_T("ROW_DECODE_MODE"));
	iniFile.GetKeyValue(chSection,chKey,"2",chKey);
	nRowDecodeMOde = atoi(chKey);

	//	DATA_LINE_TYPE	8	0x14	7--0	数据类型大类：默认为0x00，
	strcpy(chKey,_T("DATA_LINE_TYPE_RANGE"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nDataLineTypeRange = atoi(chKey);

	//	DATA_LINE_TYPE	8	0x14	7--0	数据类型：默认为0x00，
	//	0x00-0x1F	红绿蓝分开, 
	//	0x20-0x18	红绿蓝合一三色1点串行
	//	0x30-0x38	红绿蓝合一三色8点串行
	//	0x40-0x48	红绿蓝合一三色16点串行
	//	0x50-0x6F	红绿蓝合一四色串行
	strcpy(chKey,_T("DATA_LINE_TYPE"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nDataLineType = atoi(chKey);

	//DATA_LINE_CTRL	8	0x00	数据线控制,
	//控制4根数据线RB,B,G,RA的亮灭。注：对用bit为0：亮，为1：灭。默认：都亮
	strcpy(chKey,_T("DATA_LINE_CTRL"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nDataLineCtrl = atoi(chKey);

	//FIELD_NUM			总场数，最大为136场，做减1处理。
	strcpy(chKey,_T("FIELD_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"12",chKey);
	nFieldNum = atoi(chKey);

	//HALF_FIELD_NUM	半场数，最大为9场.默认为0x6，减1处理
	strcpy(chKey,_T("HALF_FIELD_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"5",chKey);
	nHalfFieldNumber = atoi(chKey);

	//FULL_FIELD_NUM	全场数，最大为128场，减1处理
	strcpy(chKey,_T("FULL_FIELD_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"6",chKey);
	nFullFieldNumber = atoi(chKey);

	//起始场
	strcpy(chKey,_T("start_field"));
	iniFile.GetKeyValue(chSection,chKey,"32",chKey);
	nStartField = atoi(chKey);

	//终止场
	strcpy(chKey,_T("end_field"));
	iniFile.GetKeyValue(chSection,chKey,"64",chKey);
	nEndField = atoi(chKey);

	//次高场 2013-3-5 通用芯片升级
	strcpy(chKey,_T("Second_Filed"));
	iniFile.GetKeyValue(chSection,chKey,"64",chKey);
	nStartField = atoi(chKey);
	
	//亮度有效率 2013-3-5 通用芯片升级
	strcpy(chKey,_T("brightness_efficent"));
	iniFile.GetKeyValue(chSection,chKey,"64",chKey);
	fBrightnessEfficent =(float) atof(chKey);

	//DATA_POLARITY	4	数据极性：默认为0x0	0x0	高电平点亮	0x1	低电平点亮	0x2-0xF	其他14种情况，预留
	strcpy(chKey,_T("DATA_POLARITY"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nDataPolarity = atoi(chKey);

	//OE_POLARITY	0x1F	OE极性：默认为0x0	0x0	低有效	0x1	高有效
	strcpy(chKey,_T("OE_POLARITY"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nOePolarity = atoi(chKey);

	///////////空点插入////////////
	//EMPTY_INSERT_EN			空点插入使能，即每多少点插入多少空点.	默认0x0，不使能
	strcpy(chKey,_T("EMPTY_INSERT_EN"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bEmptyInsertEnable = atoi(chKey) == 1 ? true : false;

	//INSERT_MODE				插入空点方式，前插入还是后插入。	1：前插入空点。0：后插入空点
	strcpy(chKey,_T("INSERT_MODE"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nInsertMode = atoi(chKey);

	//EMPTY_DOT_NUM				插入的空点数，每次最大只能插入64空点，做减1处理。
	strcpy(chKey,_T("EMPTY_DOT_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	nEmptyDotNum = atoi(chKey);

	//REAL_DOT_NUM		15--0	每多少点插入空点，做减1处理。
	strcpy(chKey,_T("REAL_DOT_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	nRealDotNum = atoi(chKey);

	///////////空区插入////////////
	//EMPTYSECTION_INSERT_EN			空区插入使能，即每多少点插入多少空区.	默认0x0，不使能
	strcpy(chKey,_T("EMPTYSECTION_INSERT_EN"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bEmptySectionInsertEnable = atoi(chKey) == 1 ? true : false;

	//INSERTSECTION_MODE				插入空区方式，前插入还是后插入。	1：前插入空点。0：后插入空点
	strcpy(chKey,_T("INSERTSECTION_MODE"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nInsertSectionMode = atoi(chKey);

	//EMPTY_SECTION_NUM				插入的空区数，每次最大只能插入64空点，做减1处理。
	strcpy(chKey,_T("EMPTY_SECTION_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	nEmptySectionNum = atoi(chKey);

	//REAL_SECTION_NUM		15--0	每多少区插入空区，做减1处理。
	strcpy(chKey,_T("REAL_SECTION_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	nRealSectionNum = atoi(chKey);



	//双列输出
	strcpy(chKey,_T("dual_out_put"));
	iniFile.GetKeyValue(chSection,chKey,"171",chKey);
	bDualOutput = atoi(chKey) == 1 ? true : false;

	//虚拟像素排布方式
	strcpy(chKey,_T("virTual_array"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nVirTualArray = atoi(chKey);

	//灯板芯片
	strcpy(chKey,_T("chip_type"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nChipType = (CHIP_TYPE)atoi(chKey);

	//ref_doule_value	刷新率倍增的倍数，
	strcpy(chKey,_T("ref_doule_value"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	nRefreshDoubleValue = atoi(chKey);

	//zhe_rdwr_mode		折处理模块读写折DPRAM的方式。默认为0
	//0：按列8读写，1：按箱体行读写
	strcpy(chKey,_T("zhe_rdwr_mode"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nZheRdwrMode = atoi(chKey);

	//显示屏类型  0-全彩实像素，1-全彩虚拟
	strcpy(chKey,_T("screen_type"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nScreenType = atoi(chKey);

	//单点校正类型, 0-调亮，1-调色
	strcpy(chKey,_T("dot_correct_tye"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nDotCorrectTye = atoi(chKey);

	//虚拟显示变化
	bVirtualChangeFlag = false;
	//原先虚拟显示
	bVirtualPrime = bVirtvalDisp;

	//测试效果/测试结束
	strcpy(chKey,_T("test_start"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bTest = atoi(chKey) ? true : false;

	//亮度有效率 2013-3-5 通用芯片升级
	strcpy(chKey,_T("brightness_efficent"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	fBrightnessEfficent = (float) atof(chKey);

	//最小OE宽度
	strcpy(chKey,_T("min_oe_width"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nMinOEWidth = atoi(chKey);

	//颜色深度变化标识
	bScanColorDepthChangeFlag = false;
	//原先颜色深度
	nScanColorDepthPrime = nScanColorDepth;

	//使能逐点开路检测功能 PWM版有,通用芯片无；1 - 使能， 0-不使能
	strcpy(chKey,_T("dot_open_detection"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bDotOpenDetection = atoi(chKey) ? true : false;

	//PWM输出模式	MBI5030: 0-打撒模式 1-普通模式；TC62D722: 1-打散模式，0-普通模式
	strcpy(chKey,_T("pwm_output_mode"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nPWMOutputMode = atoi(chKey);

	//刷新率倍增，PWM静态扫描下有效
	strcpy(chKey,_T("multi_refresh_under_static_scan"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bMultiRefreshUnderStaticScan = atoi(chKey) ? true : false;

	//扩区使能
	strcpy(chKey,_T("extend_enable"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bExtendedEnable = atoi(chKey) ? true : false;

	strcpy(chKey,_T("extend_enableEx"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bExtendedEnableEx = atoi(chKey) ? true : false;

	//区宽
	strcpy(chKey,_T("section_width"));
	iniFile.GetKeyValue(chSection,chKey,"32",chKey);
	nSectionWidth = atoi(chKey);

	//横向区个数
	strcpy(chKey,_T("section_hor_number"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	nSectionHorNum = atoi(chKey);

	//灰度增强位数
	strcpy(chKey,_T("gray_enhance_bit"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nGrayEnhance = atoi(chKey);

	//灰度增强方式
	strcpy(chKey,_T("gray_enhance_mode"));
	iniFile.GetKeyValue(chSection,chKey,"3",chKey);
	nGrayEnhanceMode = atoi(chKey);

	//箱体指示灯
	strcpy(chKey,_T("open_cabinet_lamp"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	bOpenCabinetLamp = atoi(chKey) ? true : false;
	return true;
}


//写扫描卡信息到文件中
void CStructSingleScanCard::WriteConfig(const char * chCfgFile)
{

	CIniFile IniFlie;		
	IniFlie.SetPath(chCfgFile);
	
	const int MAX_BUF = 260;
	TCHAR chSection[MAX_BUF] = _T("screen");
	TCHAR chKey[MAX_BUF];
	TCHAR chKeyVal[MAX_BUF];

	//减去的色深位数
	strcpy(chKey,_T("DeductBit"));
	sprintf(chKeyVal,"%d",nDeductBit );
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	
	//ONE_SCAN_CARD_WIDTH	2	128	单块扫描控制卡所对应的LED显示模块中横向像素点的数量，
	//即扫描卡宽度。高位在前，低位在后。取值范围1~256，必须是16的整数倍
	strcpy(chKey,_T("ONE_SCAN_CARD_WIDTH"));
	sprintf(chKeyVal,"%d",nScanCardWidth);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//ONE_SCAN_CARD_HEIGHT	2	96	单块扫描控制卡所对应的LED显示模块中纵向像素点的数量，
	//即扫描卡高度。高位在前，低位在后。取值范围1~256，必须是16的整数倍
	strcpy(chKey,_T("ONE_SCAN_CARD_HEIGHT"));
	sprintf(chKeyVal,"%d",nScanCardHeight);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//mod_width 1   32  模组宽度，取值范围1-64
	strcpy(chKey,_T("mod_width"));
	sprintf(chKeyVal,"%d",nModuleWidth);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//mod_height 1   32  模组高度，取值范围1-64
	strcpy(chKey,_T("mod_height"));
	sprintf(chKeyVal,"%d",nModuleHeight);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//1   3   模组区数  模组区数 = 模组高度/每区行数
	strcpy(chKey,_T("mod_section_number"));
	sprintf(chKeyVal,"%d",nModuleSectionNumber);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//1    4  模组横向个数，模组横向个数 = 扫描卡宽度/模组宽度
	strcpy(chKey,_T("mod_hor_number"));
	sprintf(chKeyVal,"%d",nModuleHorNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//1    3  模组纵向个数，模组纵向个数 = 扫描卡高度、模组高度
	strcpy(chKey,_T("mod_ver_number"));
	sprintf(chKeyVal,"%d",nModuleVerNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//SCAN_CARD_SECTION_NUM	1	6	扫描卡的区数，区数最大为16
	strcpy(chKey,_T("SCAN_CARD_SECTION_NUM"));
	sprintf(chKeyVal,"%d",nScanCardSectionNumber);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//SCAN_CARD_SECTION_ROW_NUM	1	16	扫描卡每区行数，行数最大为16行
	strcpy(chKey,_T("SCAN_CARD_SECTION_ROW_NUM	"));
	sprintf(chKeyVal,"%d",nScanCardSectionRowNumber);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);


	//S系列参数添加：箱体参数
	//SCAN_CARD_SECTION_NUM_CABINET	1	6	扫描卡的区数，区数最大为16
	strcpy(chKey,_T("SCAN_CARD_SECTION_NUM_CABINET"));
	sprintf(chKeyVal,"%d",nScanCardSectionNumber_cabinet);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//SCAN_CARD_SECTION_ROW_NUM	1	16	扫描卡每区行数，行数最大为16行
	strcpy(chKey,_T("SCAN_CARD_SECTION_ROW_NUM_CABINET"));
	sprintf(chKeyVal,"%d",nScanCardSectionRowNumber_cabinet);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);


	//SCAN_COLOR_DEPTH	1	14	扫描卡扫描的颜色深度，取12~16的整数。
	strcpy(chKey,_T("SCAN_COLOR_DEPTH"));
	sprintf(chKeyVal,"%d",nScanColorDepth);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//GRAY_LEVEL 扫描卡灰度级别
	strcpy(chKey,_T("GRAY_LEVEL"));
	sprintf(chKeyVal,"%d",nGrayLedvel);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//origin_color_bit		原始颜色深度，比如8BIT,10bit，12bit，16bit
	strcpy(chKey,_T("origin_color_bit"));
	sprintf(chKeyVal,"%d",nOrginColorBit);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//SCAN_MODE	1	4	扫描的模式，取 1-2-4-8-16
	strcpy(chKey,_T("SCAN_MODE"));
	sprintf(chKeyVal,"%d",nScanMode);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//SCAN_MODE_CABINET	1	4	箱体参数：扫描的模式，送数据，S系列箱体使用
	strcpy(chKey,_T("SCAN_MODE_CABINET"));
	sprintf(chKeyVal,"%d",nScanMode_cabinet);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//DOT_CORRECTION_EN	1	1	单点校正使能，取0，1
	strcpy(chKey,_T("DOT_CORRECTION_EN"));
	sprintf(chKeyVal,"%d",bEmendBrightness ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//SCAN_GCLK_FREQUENCY	1	12.5	扫描时钟频率，最大30Mhz
	strcpy(chKey,_T("SCAN_GCLK_FREQUENCY"));
	sprintf(chKeyVal,"%0.1f",fScanClkFrequency);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//ZONE_DCLK_NUM	2		每区移位时钟数，256*16
	strcpy(chKey,_T("ZONE_DCLK_NUM"));
	sprintf(chKeyVal,"%d",nZoneDClkNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//1  0-100  占空比  扫描时钟的占空比
	strcpy(chKey,_T("duty_cycle_low_value"));
	sprintf(chKeyVal,"%d",nDutyCycle);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//PWM_SCAN_GCLK_FREQUENCY	1	12.5	PWM时钟时钟频率，最大30Mhz
	strcpy(chKey,_T("PWM_SCAN_GCLK_FREQUENCY"));
	sprintf(chKeyVal,"%0.1f",fPWMScanClkFrequency);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//1  0-100  PWM时钟占空比可调等级 
	strcpy(chKey,_T("pwm_duty_cycle_low_value"));
	sprintf(chKeyVal,"%d",nPWMDutyCycle);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//CLR_CLK_NUM	1	4	1~255 消隐时钟数(行消隐时间)
	strcpy(chKey,_T("CLR_CLK_NUM"));
	sprintf(chKeyVal,"%d",nOeClkNumber);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//刷新频率 根据颜色深度和扫描方式不同而不同
	strcpy(chKey,_T("refresh_rate"));
	sprintf(chKeyVal,"%d",nRefreshRate);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//REF_FREQ_MIN 刷新率最小值
	strcpy(chKey,_T("refresh_rate_min"));
	sprintf(chKeyVal,"%d",nRefreshRateMin);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//REF_FREQ_MAX 刷新率最大值
	strcpy(chKey,_T("refresh_rate_max"));
	sprintf(chKeyVal,"%d",nRefreshRateMax);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//CONFIG_IC_TIME	配寄存器与逐点检测的时间(芯片间隔时间)， 1ms--30s
	strcpy(chKey,_T("config_ic_time"));
	sprintf(chKeyVal,"%d",nConfigICTime);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//dat_freq_num   换帧频率计数器
	strcpy(chKey,_T("dat_freq_num"));
	sprintf(chKeyVal,"%d",nDatFreqNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//OE_DELAY_VALUE	0x01	消隐延迟时钟数，消除暗亮，默认为0x01，做减1处理。
	strcpy(chKey,_T("OE_DELAY_VALUE"));
	sprintf(chKeyVal,"%d",nOeDelayValue);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//SYN_REFRESH_EN		同步刷新使能。默认为0x1，使能
	strcpy(chKey,_T("SYN_REFRESH_EN"));
	sprintf(chKeyVal,"%d",bSyncRefresh ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//VIRTUAL_DISP_EN			虚拟显示使能，默认为0x0，不使能
	strcpy(chKey,_T("VIRTUAL_DISP_EN"));
	sprintf(chKeyVal,"%d",bVirtvalDisp ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//FREQ_DIVISION_COEF	0x7		150Mhz的分频系数，最大为200分频，值为0.625Mhz。默认为10分频，值为0x09。做减1处理。
	strcpy(chKey,_T("FREQ_DIVISION_COEF"));
	sprintf(chKeyVal,"%d",nFreqDivisionCoeff);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//PWM_FREQ_DIVISION_COEF	0x7		150Mhz的分频系数，最大为200分频，值为0.625Mhz。默认为10分频，值为0x09。做减1处理。
	strcpy(chKey,_T("PWM_FREQ_DIVISION_COEF"));
	sprintf(chKeyVal,"%d",nPWMFreqDivisionCoeff);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//DATA_OUTPUT_REVERSE	输出口逆序包括数据线和扫描线	0x00	数据线逆序：默认为0x0	0x0	不使能	0x1	使能
	strcpy(chKey,_T("DATA_OUTPUT_REVERSE"));
	sprintf(chKeyVal,"%d",bDataOutUpReverse ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//SCAN_OUTPUT_REVERSE		0x00	扫描线逆序：默认为0x0	0x0	不使能	0x1	使能
	strcpy(chKey,_T("SCAN_OUTPUT_REVERSE"));
	sprintf(chKeyVal,"%d",bScanOutUpReverse);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//	DCB_LINE_CLK_EN			0x00	使能行信号DCB为时钟使带载高度加倍;	0x0	不使能	0x1	2倍	0x2	3倍	0x3	4倍
	strcpy(chKey,_T("DCB_LINE_CLK_EN"));
	sprintf(chKeyVal,"%d",nDCBlineClkEn);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//NO_SIGNAL_DISP	无信号显示：默认为0		0x0：黑屏，0x1：随机画面。0x2：图片
	strcpy(chKey,_T("NO_SIGNAL_DISP"));
	sprintf(chKeyVal,"%d",nNoSingleDisp);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//DATA_INPUT_DIR		0x32	数据方向：（从显示屏正面看）	
	//默认为从右到左0x1	0x0	从左往右	0x1	从右往左	0x2	从上往下	0x3	从下往上
	strcpy(chKey,_T("DATA_INPUT_DIR"));
	sprintf(chKeyVal,"%d",nDataInputDir);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//ROW_DECODE_MODE		行译码方式：默认0x2
	//	0x0	静态无译码					0x6	164译码
	//	0x1	无译码芯片，直接驱动行管	0x7	192译码
	//	0x2	138译码						0x8	193译码
	//	0x3	139译码						0x9	595译码
	//	0x4	145译码或138双O				0xA	4096译码
	//	0x5	154译码						0xB	
	strcpy(chKey,_T("ROW_DECODE_MODE"));
	sprintf(chKeyVal,"%d",nRowDecodeMOde);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//	DATA_LINE_TYPE	8	0x14	7--0	数据类型大类：默认为0x00，
	strcpy(chKey,_T("DATA_LINE_TYPE_RANGE"));
	sprintf(chKeyVal,"%d",nDataLineTypeRange);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//	DATA_LINE_TYPE	8	0x14	7--0	数据类型：默认为0x00，
	//	0x00-0x1F	红绿蓝分开, 
	//	0x20-0x18	红绿蓝合一三色1点串行
	//	0x30-0x38	红绿蓝合一三色8点串行
	//	0x40-0x48	红绿蓝合一三色16点串行
	//	0x50-0x6F	红绿蓝合一四色串行
	strcpy(chKey,_T("DATA_LINE_TYPE"));
	sprintf(chKeyVal,"%d",nDataLineType);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//DATA_LINE_CTRL	8	0x00	数据线控制,
	//控制4根数据线RB,B,G,RA的亮灭。注：对用bit为0：亮，为1：灭。默认：都亮
	strcpy(chKey,_T("DATA_LINE_CTRL"));
	sprintf(chKeyVal,"%d",nDataLineCtrl);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//FIELD_NUM			总场数，最大为136场，做减1处理。
	strcpy(chKey,_T("FIELD_NUM"));
	sprintf(chKeyVal,"%d",nFieldNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//HALF_FIELD_NUM	半场数，最大为9场.默认为0x6，减1处理
	strcpy(chKey,_T("HALF_FIELD_NUM"));
	sprintf(chKeyVal,"%d",nHalfFieldNumber);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//FULL_FIELD_NUM	全场数，最大为128场，减1处理
	strcpy(chKey,_T("FULL_FIELD_NUM"));
	sprintf(chKeyVal,"%d",nFullFieldNumber);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//起始场
	strcpy(chKey,_T("start_field"));
	sprintf(chKeyVal,"%d",nStartField);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//终止场
	strcpy(chKey,_T("end_field"));
	sprintf(chKeyVal,"%d",nEndField);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//次高场 2013-3-5 通用芯片升级
	strcpy(chKey,_T("Second_Filed"));
	sprintf(chKeyVal,"%d",nSecondHighLevel);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//亮度有效率 2013-3-5 通用芯片升级
	strcpy(chKey,_T("brightness_efficent"));
	sprintf(chKeyVal,"%0.3f",fBrightnessEfficent);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//DATA_POLARITY	4	数据极性：默认为0x0	0x0	高电平点亮	0x1	低电平点亮	0x2-0xF	其他14种情况，预留
	strcpy(chKey,_T("DATA_POLARITY"));
	sprintf(chKeyVal,"%d",nDataPolarity);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//OE_POLARITY	0x1F	OE极性：默认为0x0	0x0	低有效	0x1	高有效
	strcpy(chKey,_T("OE_POLARITY"));
	sprintf(chKeyVal,"%d",nOePolarity);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	///////////空点插入////////////
	//EMPTY_INSERT_EN			空点插入使能，即每多少点插入多少空点.	默认0x0，不使能
	strcpy(chKey,_T("EMPTY_INSERT_EN"));
	sprintf(chKeyVal,"%d",bEmptyInsertEnable ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//INSERT_MODE				插入空点方式，前插入还是后插入。	1：前插入空点。0：后插入空点
	strcpy(chKey,_T("INSERT_MODE"));
	sprintf(chKeyVal,"%d",nInsertMode);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//EMPTY_DOT_NUM				插入的空点数，每次最大只能插入64空点，做减1处理。
	strcpy(chKey,_T("EMPTY_DOT_NUM"));
	sprintf(chKeyVal,"%d",nEmptyDotNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//REAL_DOT_NUM		15--0	每多少点插入空点，做减1处理。
	strcpy(chKey,_T("REAL_DOT_NUM"));
	sprintf(chKeyVal,"%d",nRealDotNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	///////////空区插入////////////
	//EMPTYSECTION_INSERT_EN			空区插入使能，即每多少区插入多少空区.	默认0x0，不使能
	strcpy(chKey,_T("EMPTYSECTION_INSERT_EN"));
	sprintf(chKeyVal,"%d",bEmptySectionInsertEnable ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//INSERTSECTION_MODE				插入空区方式，前插入还是后插入。	1：前插入空区。0：后插入空区
	strcpy(chKey,_T("INSERTSECTION_MODE"));
	sprintf(chKeyVal,"%d",nInsertSectionMode);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//EMPTY_SECTION_NUM				插入的空区数，每次最大只能插入64空区，做减1处理。
	strcpy(chKey,_T("EMPTY_SECTION_NUM"));
	sprintf(chKeyVal,"%d",nEmptySectionNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//REAL_SECTION_NUM		15--0	每多少点插入空区，做减1处理。
	strcpy(chKey,_T("REAL_SECTION_NUM"));
	sprintf(chKeyVal,"%d",nRealSectionNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);



	//双列输出
	strcpy(chKey,_T("dual_out_put"));
	sprintf(chKeyVal,"%d",bDualOutput ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//虚拟像素排布方式
	strcpy(chKey,_T("virTual_array"));
	sprintf(chKeyVal,"%d",nVirTualArray);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//灯板芯片
	strcpy(chKey,_T("chip_type"));
	sprintf(chKeyVal,"%d",nChipType);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//ref_doule_value	刷新率倍增的倍数，
	strcpy(chKey,_T("ref_doule_value"));
	sprintf(chKeyVal,"%d",nRefreshDoubleValue);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//zhe_rdwr_mode		折处理模块读写折DPRAM的方式。默认为0
	//0：按列8读写，1：按箱体行读写
	strcpy(chKey,_T("zhe_rdwr_mode"));
	sprintf(chKeyVal,"%d",nZheRdwrMode);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//显示屏类型 0-全彩实像素，1-全彩虚拟
	strcpy(chKey,_T("screen_type"));
	sprintf(chKeyVal,"%d",nScreenType);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//单点校正类型, 0-调亮，1-调色
	strcpy(chKey,_T("dot_correct_tye"));
	sprintf(chKeyVal,"%d",nDotCorrectTye);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);	
	
	//测试效果/测试结束
	strcpy(chKey,_T("test_start"));
	sprintf(chKeyVal,"%d",bTest ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//亮度有效率
	strcpy(chKey,_T("brightness_efficent"));
	sprintf(chKeyVal,"%0.3f",fBrightnessEfficent);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//最小OE宽度
	strcpy(chKey,_T("min_oe_width"));
	sprintf(chKeyVal,"%d",nMinOEWidth);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//使能逐点开路检测功能 PWM版有,通用芯片无；1 - 使能， 0-不使能
	strcpy(chKey,_T("dot_open_detection"));
	sprintf(chKeyVal,"%d",bDotOpenDetection ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//PWM输出模式	MBI5030: 0-打撒模式 1-普通模式；TC62D722: 1-打散模式，0-普通模式
	strcpy(chKey,_T("pwm_output_mode"));
	sprintf(chKeyVal,"%d",nPWMOutputMode);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//刷新率倍增，PWM静态扫描下有效
	strcpy(chKey,_T("multi_refresh_under_static_scan"));
	sprintf(chKeyVal,"%d",bMultiRefreshUnderStaticScan ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	
	//扩区使能
	strcpy(chKey,_T("extend_enable"));
	sprintf(chKeyVal,"%d",bExtendedEnable ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	strcpy(chKey,_T("extend_enableEx"));
	sprintf(chKeyVal,"%d",bExtendedEnableEx ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//区宽
	strcpy(chKey,_T("section_width"));
	sprintf(chKeyVal,"%d",nSectionWidth );
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//横向区个数
	strcpy(chKey,_T("section_hor_number"));
	sprintf(chKeyVal,"%d",nSectionHorNum );
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//灰度增强位数
	strcpy(chKey,_T("gray_enhance_bit"));
	sprintf(chKeyVal,"%d",nGrayEnhance );
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//灰度增强方式
	strcpy(chKey,_T("gray_enhance_mode"));
	sprintf(chKeyVal,"%d",nGrayEnhanceMode );
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//箱体指示灯
	strcpy(chKey,_T("open_cabinet_lamp"));
	sprintf(chKeyVal,"%d",bOpenCabinetLamp );
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

}
*/
//获取水平模组个数和垂直模组个数
void CStructSingleScanCard::GetHorVerModuleNum(short & nModHorNum,short & nModVerNum)
{
	nModHorNum = nModuleHorNum;
	nModVerNum = nModuleHorNum;

	if (nDataInputDir == 0 || nDataInputDir == 1)//左右进线
	{
		nModVerNum /= (int)pow(2.0,nDCBlineClkEn);
	}
	else if (nDataInputDir == 2 || nDataInputDir == 3)//上下进线
	{
		nModHorNum /= (int)pow(2.0,nDCBlineClkEn);
	}

}

//获取水平模组个数和垂直模组个数,用于校正和逐点检测
void CStructSingleScanCard::GetHorVerModuleNumForCalibrationAndDotDetect(short & nModHorNum,short & nModVerNum)
{
	nModHorNum = nScanCardWidthReal / nModuleWidth;
	nModVerNum = nScanCardHeightReal / nModuleHeight;

	nModHorNum = max(1,nModHorNum);
	nModVerNum = max(1,nModVerNum);

	if (nDataInputDir == 0 || nDataInputDir == 1)//左右进线
	{
		nModVerNum /= (int)pow(2.0,nDCBlineClkEn);
	}
	else if (nDataInputDir == 2 || nDataInputDir == 3)//上下进线
	{
		nModHorNum /= (int)pow(2.0,nDCBlineClkEn);
	}

}

//获取模组宽度和高度
void CStructSingleScanCard::GetHorModuleWidthHeight(short & nModWidth,short & nModHeight)
{

	nModWidth = nModuleWidth;
	nModHeight = nModuleHeight;
	if (nDataInputDir == 0 || nDataInputDir == 1)//左右进线
	{
		nModHeight *= (short)pow(2.0, nDCBlineClkEn);
	}
	else if (nDataInputDir == 2 || nDataInputDir == 3)//上下进线
	{
		nModWidth *= (short)pow(2.0, nDCBlineClkEn);
	}
}
