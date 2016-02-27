#pragma once

enum VersionType
{
	//版本类型 0：通用版本；
	//		   1：M3.5箱体特殊情况； 
	//         2：FS08箱体特殊情况
	//         3：SP10箱体特殊情况
	//         4：S系列箱体特殊情况，S6，S8等
	_CustomVersion = 0,
	_M3_5Version,
	_FS08Version,
	_SP10Version,
	_SSeriesVersion,
	_JSTV
};

enum CHIP_TYPE
{
	_GENERAL,			//通用芯片
	_MBI5042,
	_MBI5030,
	_TC62D722,
	_MBI5050,
	_TLC5948,
	_MBI5040,
	_MBI5041,
	_MBI5045,
	_TLC5958,
	_MBI5152,
	_MBI5153,
	_MBI5153_E,//聚积开放了MBI5153驱动芯片的第三组寄存器的设置
	_MBI5043, //芯片类型和_MBI5041一样，10bitPWM模式时刷新率算法不一样
	_MBI5155 //芯片和5153_E稍微不同
};


//箱体系列

//最新_MBI5153，聚积开放了MBI5153驱动芯片的第三组寄存器的设置
typedef struct Drive_ic_reg_MBI5153_E
{
	unsigned int nReg1RHigh;//寄存器1红高字节
	unsigned int nReg1RLow; //寄存器1红低字节
	unsigned int nReg1GHigh;//寄存器1绿高字节
	unsigned int nReg1GLow; //寄存器1绿低字节
	unsigned int nReg1BHigh;//寄存器1蓝高字节
	unsigned int nReg1BLow; //寄存器1蓝低字节

	unsigned int nReg2RHigh;//寄存器2红高字节
	unsigned int nReg2RLow; //寄存器2红低字节
	unsigned int nReg2GHigh;//寄存器2绿高字节
	unsigned int nReg2GLow; //寄存器2绿低字节
	unsigned int nReg2BHigh;//寄存器2蓝高字节
	unsigned int nReg2BLow; //寄存器2蓝低字节

	unsigned int nReg3RHigh;//寄存器3红高字节
	unsigned int nReg3RLow; //寄存器3红低字节
	unsigned int nReg3GHigh;//寄存器3绿高字节
	unsigned int nReg3GLow; //寄存器3绿低字节
	unsigned int nReg3BHigh;//寄存器3蓝高字节
	unsigned int nReg3BLow; //寄存器3蓝低字节
}Drive_ic_reg_MBI5153_E;

//最新_MBI5155渐变过渡优化，在寄存器5153保留的基础上增加的字段
typedef struct Drive_ic_reg_MBI5155
{
	unsigned int nDeltaT;//MBI5155 第513/257个GCLK的低电平宽度
	unsigned int nDeltaF;//MBI5155 第513/257个GCLK的高电平宽度
	unsigned int nDHT;//MBI5155 第1个GCLK的高电平宽度
	unsigned int nDG_H;//MBI5155 第514/258个GCLK的高电平宽度
	unsigned int nDG_L;//MBI5155 第514/258个GCLK的低电平宽度
}Drive_ic_reg_MBI5155;

typedef struct Drive_ic_reg_MBI5043
{
	bool bGCLKDoublesampling;//MBI5043 GCLK双沿采样	0：关闭， 1：开启
	short nPWMMode;//MBI5043 PWM模式选择	0: 16bit，1: 10bit
}Drive_ic_reg_MBI5043;

//FC 寄存器信息
typedef struct Drive_ic_reg
{
	//_TLC5958
	//reg1
	unsigned int nBright;//全局亮度调节
	unsigned int nLgse_R;//红色低灰增强
	unsigned int nLgse_G;//绿色低灰增强
	unsigned int nLgse_B;//蓝色低灰增强
	unsigned int nGdly_Enable;//输出通道延迟使能
	unsigned int nTD_Delay;//输入数据延迟
	unsigned int nLodvth;//开路检测电压设定

	//reg2
	unsigned int nGlobal_Lgse;//全局低灰增强
	unsigned int nPVM_Mode;//打散模式
	unsigned int nEMI_R;//红色EMI削减
	unsigned int nEMI_G;//绿色EMI削减
	unsigned int nEMI_B;//蓝色EMI削减
	unsigned int nPre_Charge;//预充电模式


	//_MBI5152/_MBI5153
	//reg1
	unsigned int nPre_Charge1;		//预充电模式：0关闭，1开启
	unsigned int nPwm_Count_Mode;	//PWM计数模式：0正数，1倒数
	unsigned int nGray_Mode;		//灰阶模式：MBI5152：0=16bit，1=14bit	MBI5153：0=14bit，1=13bit
	unsigned int nEnable_GCLK;		//GCLK倍频：0禁用，1使能
	
	//reg2
	unsigned int nDouble_RefreseRate;//双倍刷新率：0关闭，1开启
	unsigned int nVoltage;			//开路检测电压：00: 0.3V，01: 0.4V，10: 0.5V，11: 0.6V
	unsigned int nIC_Recognition;	//红绿蓝IC识别：01红色，10绿色，11蓝色
	unsigned int nAdjust_Red;		//首行偏暗调节（红色）：000: 0 ns，100: 18ns，001: 6 ns，101: 21ns，010: 9 ns，110: 27ns，011: 15 ns，111: 33ns
	unsigned int nAdjust_Green;		//首行偏暗调节（绿色）：000: 0 ns，100: 18ns，001: 6 ns，101: 21ns，010: 9 ns，110: 27ns，011: 15 ns，111: 33ns
	unsigned int nAdjust_Blue;		//首行偏暗调节（蓝色）：000: 0 ns，100: 18ns，001: 6 ns，101: 21ns，010: 9 ns，110: 27ns，011: 15 ns，111: 33ns
	unsigned int nImhl_DoNotStretch;//倒数模式高电平不延伸：0关闭，1开启

	Drive_ic_reg_MBI5153_E sDrive_ic_reg_MBI5153_E;//最新_MBI5153，聚积开放了MBI5153驱动芯片的第三组寄存器的设置
	Drive_ic_reg_MBI5043 sDrive_ic_reg_MBI5043;//MBI5043
	Drive_ic_reg_MBI5155 sDrive_ic_reg_MBI5155;//最新_MBI5155渐变过渡优化，在寄存器5153保留的基础上增加的字段
}Drive_ic_reg;


typedef int (*GetRegInfoCallBackFun)(Drive_ic_reg* nDrive_ic_reg);//获取寄存器信息回调函数定义


class CStructSingleScanCard
{
public:
	CStructSingleScanCard(void);
	~CStructSingleScanCard(void);
public:
	//灰度级别转换成颜色深度
	int GrayLevel2ColorDepth(int nGrayLevel);
	//获取1区静态下的像素数
	int GetPixPerSetion(VersionType versionType);
	//获取每区移数据(125MHz)的时钟数
	int GetZoneClkNum(VersionType versionType);
	//获取刷新倍增值范围,刷新倍数为最小值到最大值之间的2的n次方
	void GetRefDoubleVableRange(int & nMultMin, int & nMultMax, VersionType versionType);
	//获取刷新频率范围
	void GetRefreshRateRange(int & nRateMin, int & nRateMax, int nRefreshDouble, VersionType versionType);
	//获取min_oe_clk_num
	long GetMinOeClkNum(VersionType versionType);
	//获取afield_clk_num_a1
	long GetAfieldClkNum(VersionType versionType);
	//获取行消隐时间
	int GetRowOeClkNum(VersionType versionType);
	//获取1个模组视频数据对应总的校正参数；
	long GetModCoefByte(int nEmptyByte);
	//获取亮度有效率，与场信息，分频系数有关
	void GetBrightnessEffective(VersionType versionType);
	//根据灰度等级确定场信息
	bool SetFieldNumByGrayLevel();
	//数据线排列查找表，0--black，1-- redA，2--green，3--blue, 4--redB
	short LookupDataLineType( short nState4, short nState3, short nState2, short nState1);
	//从配置文件中读取
	bool ReadConfig(const char * chOpenFile);
	//写扫描卡信息到文件中
	void WriteConfig(const char * chCfgFile);
	//获取水平模组个数和垂直模组个数
	void GetHorVerModuleNum(short & nModHorNum,short & nModVerNum);
	//获取水平模组个数和垂直模组个数,用于校正和逐点检测
	void GetHorVerModuleNumForCalibrationAndDotDetect(short & nModHorNum,short & nModVerNum);
	//获取模组宽度和高度
	void GetHorModuleWidthHeight(short & nModWidth,short & nModHeight);
	//通过行消隐时间反推刷新率
	long GetRefreshRateByReadParam(long nMinOeClkNum,long nAfieldClkNum,int nRowOeClkNum, VersionType versionType);

public:
	//ONE_SCAN_CARD_WIDTH	2	128	单块扫描控制卡所对应的LED显示模块中横向像素点的数量，
	//即扫描卡宽度。高位在前，低位在后。取值范围1~256，必须是16的整数倍
	short nScanCardWidth;
	//ONE_SCAN_CARD_HEIGHT	2	96	单块扫描控制卡所对应的LED显示模块中纵向像素点的数量，
	//即扫描卡高度。高位在前，低位在后。取值范围1~256，必须是16的整数倍
	short nScanCardHeight;
	//ONE_SCAN_CARD_WIDTH_REAL		
	//即扫描卡实际宽度。主要用于逐点检测和校正中有关模组的水平垂直个数
	short nScanCardWidthReal;
	//ONE_SCAN_CARD_HEIGHT_REAL
	//即扫描卡实际高度。主要用于逐点检测和校正中有关模组的水平垂直个数
	short nScanCardHeightReal;
	//1   32  模组宽度，取值范围1-64
	short nModuleWidth;
	//1   32  模组宽度，取值范围1-64
	short nModuleHeight;
	//1   3   模组区数 
	//左右进线：模组区数 = 模组高度 / 每区行数
	//上下进线：模组区数 = 模组宽度 / 每区行数
	short nModuleSectionNumber;
	//1    4  模组横向个数，模组横向个数 = 扫描卡宽度/模组宽度
	short nModuleHorNum;
	//1    3  模组纵向个数，模组纵向个数 = 扫描卡高度、模组高度
	short nModuleVerNum;
	//SCAN_CARD_SECTION_NUM	1	6	扫描卡的区数，区数最大为16 
	//左右进线：扫描卡区数 = 扫描卡高度 / 每区行数
	//上下进线：扫描卡区数 = 扫描卡宽度 / 每区行数
	short nScanCardSectionNumber;
	//SCAN_CARD_SECTION_ROW_NUM	1	16	扫描卡每区行数，行数最大为16行
	short nScanCardSectionRowNumber;


	// 	S8参数：
	// 		扫描参数 3*54		6扫，每区54行,1区
	// 		箱体参数 54*18		1扫，每区1行，18区	
	// 		S6参数：
	// 		扫描参数 3*70		7扫，每区70行,1区
	// 		箱体参数 69*23		1扫，每区1行，23区
	//S8
	//SCAN_CARD_SECTION_NUM="1" SCAN_CARD_SECTION_ROW_NUM="54"
	//SCAN_CARD_SECTION_NUM_CABINET="18" SCAN_CARD_SECTION_ROW_NUM_CABINET="1"

	//S6
	//SCAN_CARD_SECTION_NUM="1" SCAN_CARD_SECTION_ROW_NUM="70"
	//SCAN_CARD_SECTION_NUM_CABINET="23" SCAN_CARD_SECTION_ROW_NUM_CABINET="1"

	//S系列增加变量	
	short nScanCardSectionNumber_cabinet;
	short nScanCardSectionRowNumber_cabinet;


	//SCAN_COLOR_DEPTH	1	14	扫描卡扫描的颜色深度，取12~16的整数。
	short nScanColorDepth;
	//GRAY_LEVEL 扫描卡灰度级别
	short nGrayLedvel;
	//origin_color_bit		原始颜色深度，比如8BIT,10bit，12bit，16bit
	short nOrginColorBit;

	//SCAN_MODE	1	4	扫描的模式，取 1-2-4-8-16
	short nScanMode;

	//SCAN_MODE_CABINET	1	4	箱体参数：扫描的模式，送数据
	short nScanMode_cabinet;

	//DOT_CORRECTION_EN	1	1	单点校正使能，取0，1
	bool bEmendBrightness;
	//SCAN_GCLK_FREQUENCY	1	12.5	数据移位时钟时钟频率，最大30Mhz
	float fScanClkFrequency;
	//ZONE_DCLK_NUM	2		每区移位时钟数，256*16
	unsigned short nZoneDClkNum;
	//1  0-100  数据移位时钟占空比可调等级 
	short nDutyCycle;
	//PWM_SCAN_GCLK_FREQUENCY	1	12.5	PWM时钟时钟频率，最大30Mhz
	float fPWMScanClkFrequency;
	//1  0-100  PWM时钟占空比可调等级 
	short nPWMDutyCycle;
	//CLR_CLK_NUM	1	4	1~255 消隐时钟数(行消隐时间)
	long nOeClkNumber;
	//REF_FREQ 刷新频率 根据颜色深度和扫描方式不同而不同
	int nRefreshRate;
	//REF_FREQ_MIN 刷新率最小值
	int nRefreshRateMin;
	//REF_FREQ_MAX 刷新率最大值
	int nRefreshRateMax;
	//CONFIG_IC_TIME	配寄存器与逐点检测的时间(芯片间隔时间)， 1-2047
	short nConfigICTime;
	//dat_freq_num   换帧频率计数器
	int nDatFreqNum;
	//OE_DELAY_VALUE	0x01	消隐延迟时钟数，消除暗亮，默认为0x01，做减1处理。
	short nOeDelayValue;
	//SYN_REFRESH_EN		同步刷新使能。默认为0x1，使能
	bool bSyncRefresh;
	//VIRTUAL_DISP_EN			虚拟显示使能，默认为0x0，不使能
	bool bVirtvalDisp;
	//FREQ_DIVISION_COEF	0x7		150Mhz的分频系数，最大为200分频，值为0.625Mhz。默认为10分频，值为0x09。做减1处理。
	short nFreqDivisionCoeff;
	//PWM_FREQ_DIVISION_COEF	0x7		150Mhz的PWM分频系数，最大为200分频，值为0.625Mhz。默认为10分频，值为0x09。做减1处理。
	short nPWMFreqDivisionCoeff;
	//DATA_OUTPUT_REVERSE	输出口逆序包括数据线和扫描线	0x00	数据线逆序：默认为0x0	0x0	不使能	0x1	使能
	bool bDataOutUpReverse;	
	//SCAN_OUTPUT_REVERSE		0x00	扫描线逆序：默认为0x0	0x0	不使能	0x1	使能
	bool bScanOutUpReverse;	
	//	DCB_LINE_CLK_EN			0x00	使能行信号DCB为时钟使带载高度加倍;	0x0	不使能	0x1	2倍	0x2	3倍	0x3	4倍
	short nDCBlineClkEn;
	//NO_SIGNAL_DISP	无信号显示：默认为0		0x0：黑屏，0x1：随机画面。0x2：图片
	short nNoSingleDisp;
	//DATA_INPUT_DIR		0x32	数据方向：（从显示屏正面看）	
	//默认为从右到左0x1	0x0	从左往右	0x1	从右往左	0x2	从上往下	0x3	从下往上
	short nDataInputDir;
	//ROW_DECODE_MODE		行译码方式：默认0x2
	//	0x0	静态无译码					0x6	164译码
	//	0x1	无译码芯片，直接驱动行管	0x7	192译码
	//	0x2	138译码						0x8	193译码
	//	0x3	139译码						0x9	595译码
	//	0x4	145译码或138双O				0xA	4096译码
	//	0x5	154译码						0xB	
	short nRowDecodeMOde;
	//	DATA_LINE_TYPE	8	0x14	7--0	数据类型大类：默认为0x00，
	short nDataLineTypeRange;
	//	DATA_LINE_TYPE	8	0x14	7--0	数据类型：默认为0x00，
	//	0x00-0x1F	红绿蓝分开, 
	//	0x20-0x18	红绿蓝合一三色1点串行
	//	0x30-0x38	红绿蓝合一三色8点串行
	//	0x40-0x48	红绿蓝合一三色16点串行
	//	0x50-0x6F	红绿蓝合一四色串行
	short nDataLineType;
	//DATA_LINE_CTRL	8	0x00	数据线控制,
	//控制4根数据线RB,B,G,RA的亮灭。注：对用bit为0：亮，为1：灭。默认：都亮
	short nDataLineCtrl;
	//FIELD_NUM			总场数，最大为136场，做减1处理。
	short nFieldNum;
	//HALF_FIELD_NUM	半场数，最大为9场.默认为0x6，减1处理
	short nHalfFieldNumber;
	//FULL_FIELD_NUM	全场数，最大为128场，减1处理
	short nFullFieldNumber;
	//起始场
	short nStartField;
	//终止场
    short nEndField;
	//DATA_POLARITY	4	数据极性：默认为0x0	0x0	高电平点亮	0x1	低电平点亮	0x2-0xF	其他14种情况，预留
	short nDataPolarity;
	//OE_POLARITY	0x1F	OE极性：默认为0x0	0x0	低有效	0x1	高有效
	short nOePolarity;
	
	///////////空点插入////////////
	//EMPTY_INSERT_EN			空点插入使能，即每多少点插入多少空点.	默认0x0，不使能
	bool bEmptyInsertEnable;
	//INSERT_MODE				插入空点方式，前插入还是后插入。	1：前插入空点。0：后插入空点
	short nInsertMode;
	//EMPTY_DOT_NUM				插入的空点数，每次最大只能插入64空点，做减1处理。
	short nEmptyDotNum;
	//REAL_DOT_NUM		15--0	每多少点插入空点，做减1处理。
	short nRealDotNum;

	///////////空区插入////////////
	//EMPTYSECTION_INSERT_EN		空区插入使能，即每多少区插入多少空区.	默认0x0，不使能
	bool bEmptySectionInsertEnable;
	//INSERTSECTION_MODE			插入空区方式，前插入还是后插入。	1：前插入空区。0：后插入空区
	short nInsertSectionMode;
	//EMPTY_SECTION_NUM				插入的空区数，每次最大只能插入64空区，做减1处理。
	short nEmptySectionNum;
	//REAL_SECTION_NUM		15--0	每多少区插入空区，做减1处理。
	short nRealSectionNum;

	//双列输出
	bool bDualOutput;
	//虚拟像素排布方式,//new虚拟像素排布方式，0：红A,绿/蓝,红B，1：蓝,绿/空,红;绿,蓝/空,红，2：蓝,绿/空,红;蓝,绿/红,空，3：红,绿/空,蓝;绿,红/空,蓝
	short nVirTualArray;
	//灯板芯片
	//CHIP_TYPE nChipType;
	int nChipType;
	//ref_doule_value	刷新率倍增的倍数，
	short nRefreshDoubleValue;
	//zhe_rdwr_mode		折处理模块读写折DPRAM的方式。默认为0
	//0：按列8读写，1：按箱体行读写
	short nZheRdwrMode;

	//显示屏类型  2-全彩实像素，3-全彩虚拟
	short nScreenType;
	//单点校正类型, 0-无 1-调亮，2-调色
	short nDotCorrectTye;

	//虚拟显示变化
	bool bVirtualChangeFlag;
	//原先虚拟显示
	bool bVirtualPrime;

	//测试效果/测试结束
	bool bTest;

	//亮度有效率
	float fBrightnessEfficent;
	//最小OE宽度
	short nMinOEWidth;

	//颜色深度变化标识
	bool bScanColorDepthChangeFlag;
	//原先颜色深度
	short nScanColorDepthPrime;
	//使能逐点开路检测功能 PWM版有,通用芯片无；1 - 使能， 0-不使能
	bool bDotOpenDetection;
	//PWM输出模式	MBI5030: 0-打撒模式 1-普通模式；TC62D722: 1-打撒模式，0-普通模式
	short nPWMOutputMode;

	//文件名称
	//string sFileName;
	//刷新率倍增，PWM静态扫描下有效
	bool bMultiRefreshUnderStaticScan;

	//1区静态下的像素数
	int nPixsPerSection;
	//每区移数据(125MHz)的时钟数
	int nZoneClkNum;

	//sunj 扩区
	//扩区使能
	bool bExtendedEnable;
	bool bExtendedEnableEx;
	//区宽
	short nSectionWidth;
	//横向区个数
	short nSectionHorNum;

	//卡区宽
	short nCard_zone_width;
	//卡区个数
	short nCard_zone_Num;

	//灰度增强位数,0-16
	short nGrayEnhance;
	//灰度增强方式
	short nGrayEnhanceMode;
	//箱体指示灯
	bool bOpenCabinetLamp;
	//2013-3-5 通用芯片升级
	short nSecondHighLevel;
	float fLightRatio;
	//Gamma位数，8，10,12位，默认8位
	int nCustomGamam;

	//芯片预充电功能, 0 - 打开 1， 关闭,默认打开
	bool bChipPrecharge;

	//RGB三色分别控制GCLK的延迟时间 使能，如果为false，则nGClkDelay有效，如果true，则nGClkDelay，nGClkDelay_G,nGClkDelay_B同时有效
	bool bGClkCtrlByRGBEnable;

	//R色控制GCLK的延迟时间
	bool bGClkCtrlByREnable;

	//G色控制GCLK的延迟时间
	bool bGClkCtrlByGEnable;

	//B色控制GCLK的延迟时间
	bool bGClkCtrlByBEnable;

	//（R的）GCLK的延迟时钟数
	short nGClkDelay;

	//G的GCLK的延迟时钟数
	short nGClkDelay_G;

	//B的GCLK的延迟时钟数
	short nGClkDelay_B;

	//寄存器信息 _TLC5958使用
	Drive_ic_reg nDrive_ic_reg;

	//网口优先设置
	//0：A口优先；1：B口优先
	short nNetPortPriority;

	//锁定优先网口
	bool bLockNetPort;

	//减去的色深位数
	int nDeductBit;
};
