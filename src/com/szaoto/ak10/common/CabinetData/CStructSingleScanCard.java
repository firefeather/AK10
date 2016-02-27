/*
   * 文件名 CStructSingleScanCard.java
   * 包含类名列表com.szaoto.ak10.common
   * 版本信息，版本号
   * 创建日期2014年4月1日下午7:20:34
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.common.CabinetData;

/*
 * 类名CStructSingleScanCard
 * 作者 liangdb
 * 主要功能 单张扫描卡结构
 * 创建日期2014年4月1日
 * 修改者，修改日期，修改内容
 */
public class CStructSingleScanCard {
	public CStructSingleScanCard()
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
		//SCAN_COLOR_DEPTH	1	14	扫描卡扫描的颜色深度，取12~16的整数。
		nScanColorDepth = 8;
		//GRAY_LEVEL 扫描卡灰度级别
		nGrayLedvel = 18;
		//origin_color_bit		原始颜色深度，比如8BIT,10bit，12bit，16bit
		nOrginColorBit = 8;
		//SCAN_MODE	1	16	扫描的模式，取 1-2-4-8-16
		nScanMode = 32;
		//DOT_CORRECTION_EN	1	1	单点校正使能，取0，1
		bEmendBrightness = false;
		//SCAN_GCLK_FREQUENCY	1	15.0	数据移位时钟时钟频率，最大30Mhz
		fScanClkFrequency = 1.5f;
		//ZONE_DCLK_NUM	2		每区移位时钟数，256*16
		nZoneDClkNum = 0x1207;
		//duty_cycle_low_value_a1	8	0x32	7--0	数据移位时钟时钟占空比设置，设置低电平的计数值
		nDutyCycle = 0x32;
		//PWM_SCAN_GCLK_FREQUENCY	1	15.0	PWM时钟时钟频率，最大30Mhz
		fPWMScanClkFrequency = 1.5f;
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
		//EMPTY_INSERT_EN			空点插入使能，即每多少点插入多少空点.	默认0x0，不使能
		bEmptyInsertEnable = false;
		//INSERT_MODE				插入空点方式，前插入还是后插入。	1：前插入空点。0：后插入空点
		nInsertMode = 0;
		//EMPTY_DOT_NUM				插入的空点数，每次最大只能插入64空点，做减1处理。
		nEmptyDotNum = 1;
		//REAL_DOT_NUM		15--0	每多少点插入空点，做减1处理。
		nRealDotNum = 1;
		//双列输出
		bDualOutput = false;
		//old虚拟像素排布方式，0：红A绿/蓝红B，1：红A蓝/绿红B，2：绿红A/红B蓝，3：蓝红A/红B绿
		//new虚拟像素排布方式，0：红A,绿/蓝,红B，1：蓝,绿/空,红，2：蓝,绿/空,红;蓝,绿/红,空
		nVirTualArray = 0;
		//灯板芯片  0:通用芯片，1：28161/165，2：28162
		nChipType = CHIP_TYPE._GENERAL.ordinal();
		//ref_doule_value	刷新率倍增的倍数，
		nRefreshDoubleValue = 1;
		//zhe_rdwr_mode		折处理模块读写折DPRAM的方式。默认为0
		//0：按列8读写，1：按箱体行读写
		nZheRdwrMode = 0;
		//显示屏类型  0-全彩实像素，1-全彩虚拟
		nScreenType = 1;
		//单点校正类型, 0-无 1-调亮，2-调色
		nDotCorrectTye = 0;

		//虚拟显示变化
		bVirtualChangeFlag = false;
		//原先虚拟显示
		bVirtualPrime = bVirtvalDisp;

		//测试效果/测试结束
		bTest = false;

		//亮度有效率
		fBrightnessEfficent = 0.5f;
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
		
		//减去的色深位数
		nDrive_ic_reg = new Drive_ic_reg();

		//网口优先设置
		//0：A口优先；1：B口优先
		nNetPortPriority = 0;

		//锁定优先网口
		bLockNetPort = false;

		//减去的色深位数,默认为4
		nDeductBit = 4;
	}
	
	

	/**
	 * @return the nChipType
	 */
	public int getnChipType() {
		return nChipType;
	}

	/**
	 * @param nChipType the nChipType to set
	 */
	public void setnChipType(int nChipType) {
		this.nChipType = nChipType;
	}

	/**
	 * @return the nScanCardWidth
	 */
	public short getnScanCardWidth() {
		return nScanCardWidth;
	}

	/**
	 * @param nScanCardWidth the nScanCardWidth to set
	 */
	public void setnScanCardWidth(short nScanCardWidth) {
		this.nScanCardWidth = nScanCardWidth;
	}

	/**
	 * @return the nScanCardHeight
	 */
	public short getnScanCardHeight() {
		return nScanCardHeight;
	}

	/**
	 * @param nScanCardHeight the nScanCardHeight to set
	 */
	public void setnScanCardHeight(short nScanCardHeight) {
		this.nScanCardHeight = nScanCardHeight;
	}

	/**
	 * @return the nScanCardWidthReal
	 */
	public short getnScanCardWidthReal() {
		return nScanCardWidthReal;
	}

	/**
	 * @param nScanCardWidthReal the nScanCardWidthReal to set
	 */
	public void setnScanCardWidthReal(short nScanCardWidthReal) {
		this.nScanCardWidthReal = nScanCardWidthReal;
	}

	/**
	 * @return the nScanCardHeightReal
	 */
	public short getnScanCardHeightReal() {
		return nScanCardHeightReal;
	}

	/**
	 * @param nScanCardHeightReal the nScanCardHeightReal to set
	 */
	public void setnScanCardHeightReal(short nScanCardHeightReal) {
		this.nScanCardHeightReal = nScanCardHeightReal;
	}

	/**
	 * @return the nModuleWidth
	 */
	public short getnModuleWidth() {
		return nModuleWidth;
	}

	/**
	 * @param nModuleWidth the nModuleWidth to set
	 */
	public void setnModuleWidth(short nModuleWidth) {
		this.nModuleWidth = nModuleWidth;
	}

	/**
	 * @return the nModuleHeight
	 */
	public short getnModuleHeight() {
		return nModuleHeight;
	}

	/**
	 * @param nModuleHeight the nModuleHeight to set
	 */
	public void setnModuleHeight(short nModuleHeight) {
		this.nModuleHeight = nModuleHeight;
	}

	/**
	 * @return the nModuleSectionNumber
	 */
	public short getnModuleSectionNumber() {
		return nModuleSectionNumber;
	}

	/**
	 * @param nModuleSectionNumber the nModuleSectionNumber to set
	 */
	public void nModuleHorNum(short nModuleSectionNumber) {
		this.nModuleSectionNumber = nModuleSectionNumber;
	}

	/**
	 * @return the nModuleHorNum
	 */
	public short getnModuleHorNum() {
		return nModuleHorNum;
	}

	/**
	 * @param nModuleHorNum the nModuleHorNum to set
	 */
	public void setnModuleHorNum(short nModuleHorNum) {
		this.nModuleHorNum = nModuleHorNum;
	}

	/**
	 * @return the nModuleVerNum
	 */
	public short getnModuleVerNum() {
		return nModuleVerNum;
	}

	/**
	 * @param nModuleVerNum the nModuleVerNum to set
	 */
	public void setnModuleVerNum(short nModuleVerNum) {
		this.nModuleVerNum = nModuleVerNum;
	}

	/**
	 * @return the nScanCardSectionNumber
	 */
	public short getnScanCardSectionNumber() {
		return nScanCardSectionNumber;
	}

	/**
	 * @param nScanCardSectionNumber the nScanCardSectionNumber to set
	 */
	public void setnScanCardSectionNumber(short nScanCardSectionNumber) {
		this.nScanCardSectionNumber = nScanCardSectionNumber;
	}

	/**
	 * @return the nScanCardSectionRowNumber
	 */
	public short getnScanCardSectionRowNumber() {
		return nScanCardSectionRowNumber;
	}

	/**
	 * @param nScanCardSectionRowNumber the nScanCardSectionRowNumber to set
	 */
	public void setnScanCardSectionRowNumber(short nScanCardSectionRowNumber) {
		this.nScanCardSectionRowNumber = nScanCardSectionRowNumber;
	}

	/**
	 * @return the nScanColorDepth
	 */
	public short getnScanColorDepth() {
		return nScanColorDepth;
	}

	/**
	 * @param nScanColorDepth the nScanColorDepth to set
	 */
	public void setnScanColorDepth(short nScanColorDepth) {
		this.nScanColorDepth = nScanColorDepth;
	}

	/**
	 * @return the nGrayLedvel
	 */
	public short getnGrayLedvel() {
		return nGrayLedvel;
	}

	/**
	 * @param nGrayLedvel the nGrayLedvel to set
	 */
	public void setnGrayLedvel(short nGrayLedvel) {
		this.nGrayLedvel = nGrayLedvel;
	}

	/**
	 * @return the nOrginColorBit
	 */
	public short getnOrginColorBit() {
		return nOrginColorBit;
	}

	/**
	 * @param nOrginColorBit the nOrginColorBit to set
	 */
	public void setnOrginColorBit(short nOrginColorBit) {
		this.nOrginColorBit = nOrginColorBit;
	}

	/**
	 * @return the nScanMode
	 */
	public short getnScanMode() {
		return nScanMode;
	}

	/**
	 * @param nScanMode the nScanMode to set
	 */
	public void setnScanMode(short nScanMode) {
		this.nScanMode = nScanMode;
	}

	/**
	 * @return the bEmendBrightness
	 */
	public boolean isbEmendBrightness() {
		return bEmendBrightness;
	}

	/**
	 * @param bEmendBrightness the bEmendBrightness to set
	 */
	public void setbEmendBrightness(boolean bEmendBrightness) {
		this.bEmendBrightness = bEmendBrightness;
	}

	/**
	 * @return the fScanClkFrequency
	 */
	public float getfScanClkFrequency() {
		return fScanClkFrequency;
	}

	/**
	 * @param fScanClkFrequency the fScanClkFrequency to set
	 */
	public void setfScanClkFrequency(float fScanClkFrequency) {
		this.fScanClkFrequency = fScanClkFrequency;
	}

	/**
	 * @return the nZoneDClkNum
	 */
	public short getnZoneDClkNum() {
		return nZoneDClkNum;
	}

	/**
	 * @param nZoneDClkNum the nZoneDClkNum to set
	 */
	public void setnZoneDClkNum(short nZoneDClkNum) {
		this.nZoneDClkNum = nZoneDClkNum;
	}

	/**
	 * @return the nDutyCycle
	 */
	public short getnDutyCycle() {
		return nDutyCycle;
	}

	/**
	 * @param nDutyCycle the nDutyCycle to set
	 */
	public void setnDutyCycle(short nDutyCycle) {
		this.nDutyCycle = nDutyCycle;
	}

	/**
	 * @return the fPWMScanClkFrequency
	 */
	public float getfPWMScanClkFrequency() {
		return fPWMScanClkFrequency;
	}

	/**
	 * @param fPWMScanClkFrequency the fPWMScanClkFrequency to set
	 */
	public void setfPWMScanClkFrequency(float fPWMScanClkFrequency) {
		this.fPWMScanClkFrequency = fPWMScanClkFrequency;
	}

	/**
	 * @return the nPWMDutyCycle
	 */
	public short getnPWMDutyCycle() {
		return nPWMDutyCycle;
	}

	/**
	 * @param nPWMDutyCycle the nPWMDutyCycle to set
	 */
	public void setnPWMDutyCycle(short nPWMDutyCycle) {
		this.nPWMDutyCycle = nPWMDutyCycle;
	}

	/**
	 * @return the nOeClkNumber
	 */
	public long getnOeClkNumber() {
		return nOeClkNumber;
	}

	/**
	 * @param nOeClkNumber the nOeClkNumber to set
	 */
	public void setnOeClkNumber(long nOeClkNumber) {
		this.nOeClkNumber = nOeClkNumber;
	}

	/**
	 * @return the nRefreshRate
	 */
	public int getnRefreshRate() {
		return nRefreshRate;
	}

	/**
	 * @param nRefreshRate the nRefreshRate to set
	 */
	public void setnRefreshRate(int nRefreshRate) {
		this.nRefreshRate = nRefreshRate;
	}

	/**
	 * @return the nRefreshRateMin
	 */
	public int getnRefreshRateMin() {
		return nRefreshRateMin;
	}

	/**
	 * @param nRefreshRateMin the nRefreshRateMin to set
	 */
	public void setnRefreshRateMin(int nRefreshRateMin) {
		this.nRefreshRateMin = nRefreshRateMin;
	}

	/**
	 * @return the nRefreshRateMax
	 */
	public int getnRefreshRateMax() {
		return nRefreshRateMax;
	}

	/**
	 * @param nRefreshRateMax the nRefreshRateMax to set
	 */
	public void setnRefreshRateMax(int nRefreshRateMax) {
		this.nRefreshRateMax = nRefreshRateMax;
	}

	/**
	 * @return the nConfigICTime
	 */
	public short getnConfigICTime() {
		return nConfigICTime;
	}

	/**
	 * @param nConfigICTime the nConfigICTime to set
	 */
	public void setnConfigICTime(short nConfigICTime) {
		this.nConfigICTime = nConfigICTime;
	}

	/**
	 * @return the nDatFreqNum
	 */
	public int getnDatFreqNum() {
		return nDatFreqNum;
	}

	/**
	 * @param nDatFreqNum the nDatFreqNum to set
	 */
	public void setnDatFreqNum(int nDatFreqNum) {
		this.nDatFreqNum = nDatFreqNum;
	}

	/**
	 * @return the nOeDelayValue
	 */
	public short getnOeDelayValue() {
		return nOeDelayValue;
	}

	/**
	 * @param nOeDelayValue the nOeDelayValue to set
	 */
	public void setnOeDelayValue(short nOeDelayValue) {
		this.nOeDelayValue = nOeDelayValue;
	}

	/**
	 * @return the bSyncRefresh
	 */
	public boolean isbSyncRefresh() {
		return bSyncRefresh;
	}

	/**
	 * @param bSyncRefresh the bSyncRefresh to set
	 */
	public void setbSyncRefresh(boolean bSyncRefresh) {
		this.bSyncRefresh = bSyncRefresh;
	}

	/**
	 * @return the bVirtvalDisp
	 */
	public boolean isbVirtvalDisp() {
		return bVirtvalDisp;
	}

	/**
	 * @param bVirtvalDisp the bVirtvalDisp to set
	 */
	public void setbVirtvalDisp(boolean bVirtvalDisp) {
		this.bVirtvalDisp = bVirtvalDisp;
	}

	/**
	 * @return the nFreqDivisionCoeff
	 */
	public short getnFreqDivisionCoeff() {
		return nFreqDivisionCoeff;
	}

	/**
	 * @param nFreqDivisionCoeff the nFreqDivisionCoeff to set
	 */
	public void setnFreqDivisionCoeff(short nFreqDivisionCoeff) {
		this.nFreqDivisionCoeff = nFreqDivisionCoeff;
	}

	/**
	 * @return the nPWMFreqDivisionCoeff
	 */
	public short getnPWMFreqDivisionCoeff() {
		return nPWMFreqDivisionCoeff;
	}

	/**
	 * @param nPWMFreqDivisionCoeff the nPWMFreqDivisionCoeff to set
	 */
	public void setnPWMFreqDivisionCoeff(short nPWMFreqDivisionCoeff) {
		this.nPWMFreqDivisionCoeff = nPWMFreqDivisionCoeff;
	}

	/**
	 * @return the bDataOutUpReverse
	 */
	public boolean isbDataOutUpReverse() {
		return bDataOutUpReverse;
	}

	/**
	 * @param bDataOutUpReverse the bDataOutUpReverse to set
	 */
	public void setbDataOutUpReverse(boolean bDataOutUpReverse) {
		this.bDataOutUpReverse = bDataOutUpReverse;
	}

	/**
	 * @return the bScanOutUpReverse
	 */
	public boolean isbScanOutUpReverse() {
		return bScanOutUpReverse;
	}

	/**
	 * @param bScanOutUpReverse the bScanOutUpReverse to set
	 */
	public void setbScanOutUpReverse(boolean bScanOutUpReverse) {
		this.bScanOutUpReverse = bScanOutUpReverse;
	}

	/**
	 * @return the nDCBlineClkEn
	 */
	public short getnDCBlineClkEn() {
		return nDCBlineClkEn;
	}

	/**
	 * @param nDCBlineClkEn the nDCBlineClkEn to set
	 */
	public void setnDCBlineClkEn(short nDCBlineClkEn) {
		this.nDCBlineClkEn = nDCBlineClkEn;
	}

	/**
	 * @return the nNoSingleDisp
	 */
	public short getnNoSingleDisp() {
		return nNoSingleDisp;
	}

	/**
	 * @param nNoSingleDisp the nNoSingleDisp to set
	 */
	public void setnNoSingleDisp(short nNoSingleDisp) {
		this.nNoSingleDisp = nNoSingleDisp;
	}

	/**
	 * @return the nDataInputDir
	 */
	public short getnDataInputDir() {
		return nDataInputDir;
	}

	/**
	 * @param nDataInputDir the nDataInputDir to set
	 */
	public void setnDataInputDir(short nDataInputDir) {
		this.nDataInputDir = nDataInputDir;
	}

	/**
	 * @return the nRowDecodeMOde
	 */
	public short getnRowDecodeMOde() {
		return nRowDecodeMOde;
	}

	/**
	 * @param nRowDecodeMOde the nRowDecodeMOde to set
	 */
	public void setnRowDecodeMOde(short nRowDecodeMOde) {
		this.nRowDecodeMOde = nRowDecodeMOde;
	}

	/**
	 * @return the nDataLineTypeRange
	 */
	public short getnDataLineTypeRange() {
		return nDataLineTypeRange;
	}

	/**
	 * @param nDataLineTypeRange the nDataLineTypeRange to set
	 */
	public void setnDataLineTypeRange(short nDataLineTypeRange) {
		this.nDataLineTypeRange = nDataLineTypeRange;
	}

	/**
	 * @return the nDataLineType
	 */
	public short getnDataLineType() {
		return nDataLineType;
	}

	/**
	 * @param nDataLineType the nDataLineType to set
	 */
	public void setnDataLineType(short nDataLineType) {
		this.nDataLineType = nDataLineType;
	}

	/**
	 * @return the nDataLineCtrl
	 */
	public short getnDataLineCtrl() {
		return nDataLineCtrl;
	}

	/**
	 * @param nDataLineCtrl the nDataLineCtrl to set
	 */
	public void setnDataLineCtrl(short nDataLineCtrl) {
		this.nDataLineCtrl = nDataLineCtrl;
	}

	/**
	 * @return the nFieldNum
	 */
	public short getnFieldNum() {
		return nFieldNum;
	}

	/**
	 * @param nFieldNum the nFieldNum to set
	 */
	public void setnFieldNum(short nFieldNum) {
		this.nFieldNum = nFieldNum;
	}

	/**
	 * @return the nHalfFieldNumber
	 */
	public short getnHalfFieldNumber() {
		return nHalfFieldNumber;
	}

	/**
	 * @param nHalfFieldNumber the nHalfFieldNumber to set
	 */
	public void setnHalfFieldNumber(short nHalfFieldNumber) {
		this.nHalfFieldNumber = nHalfFieldNumber;
	}

	/**
	 * @return the nFullFieldNumber
	 */
	public short getnFullFieldNumber() {
		return nFullFieldNumber;
	}

	/**
	 * @param nFullFieldNumber the nFullFieldNumber to set
	 */
	public void setnFullFieldNumber(short nFullFieldNumber) {
		this.nFullFieldNumber = nFullFieldNumber;
	}

	/**
	 * @return the nStartField
	 */
	public short getnStartField() {
		return nStartField;
	}

	/**
	 * @param nStartField the nStartField to set
	 */
	public void setnStartField(short nStartField) {
		this.nStartField = nStartField;
	}

	/**
	 * @return the nEndField
	 */
	public short getnEndField() {
		return nEndField;
	}

	/**
	 * @param nEndField the nEndField to set
	 */
	public void setnEndField(short nEndField) {
		this.nEndField = nEndField;
	}

	/**
	 * @return the nDataPolarity
	 */
	public short getnDataPolarity() {
		return nDataPolarity;
	}

	/**
	 * @param nDataPolarity the nDataPolarity to set
	 */
	public void setnDataPolarity(short nDataPolarity) {
		this.nDataPolarity = nDataPolarity;
	}

	/**
	 * @return the nOePolarity
	 */
	public short getnOePolarity() {
		return nOePolarity;
	}

	/**
	 * @param nOePolarity the nOePolarity to set
	 */
	public void setnOePolarity(short nOePolarity) {
		this.nOePolarity = nOePolarity;
	}

	/**
	 * @return the bEmptyInsertEnable
	 */
	public boolean isbEmptyInsertEnable() {
		return bEmptyInsertEnable;
	}

	/**
	 * @param bEmptyInsertEnable the bEmptyInsertEnable to set
	 */
	public void setbEmptyInsertEnable(boolean bEmptyInsertEnable) {
		this.bEmptyInsertEnable = bEmptyInsertEnable;
	}

	/**
	 * @return the nInsertMode
	 */
	public short getnInsertMode() {
		return nInsertMode;
	}

	/**
	 * @param nInsertMode the nInsertMode to set
	 */
	public void setnInsertMode(short nInsertMode) {
		this.nInsertMode = nInsertMode;
	}

	/**
	 * @return the nEmptyDotNum
	 */
	public short getnEmptyDotNum() {
		return nEmptyDotNum;
	}

	/**
	 * @param nEmptyDotNum the nEmptyDotNum to set
	 */
	public void setnEmptyDotNum(short nEmptyDotNum) {
		this.nEmptyDotNum = nEmptyDotNum;
	}

	/**
	 * @return the nRealDotNum
	 */
	public short getnRealDotNum() {
		return nRealDotNum;
	}

	/**
	 * @param nRealDotNum the nRealDotNum to set
	 */
	public void setnRealDotNum(short nRealDotNum) {
		this.nRealDotNum = nRealDotNum;
	}

	/**
	 * @return the bDualOutput
	 */
	public boolean isbDualOutput() {
		return bDualOutput;
	}

	/**
	 * @param bDualOutput the bDualOutput to set
	 */
	public void setbDualOutput(boolean bDualOutput) {
		this.bDualOutput = bDualOutput;
	}

	/**
	 * @return the nVirTualArray
	 */
	public short getnVirTualArray() {
		return nVirTualArray;
	}

	/**
	 * @param nVirTualArray the nVirTualArray to set
	 */
	public void setnVirTualArray(short nVirTualArray) {
		this.nVirTualArray = nVirTualArray;
	}

	/**
	 * @return the nRefreshDoubleValue
	 */
	public short getnRefreshDoubleValue() {
		return nRefreshDoubleValue;
	}

	/**
	 * @param nRefreshDoubleValue the nRefreshDoubleValue to set
	 */
	public void setnRefreshDoubleValue(short nRefreshDoubleValue) {
		this.nRefreshDoubleValue = nRefreshDoubleValue;
	}

	/**
	 * @return the nZheRdwrMode
	 */
	public short getnZheRdwrMode() {
		return nZheRdwrMode;
	}

	/**
	 * @param nZheRdwrMode the nZheRdwrMode to set
	 */
	public void setnZheRdwrMode(short nZheRdwrMode) {
		this.nZheRdwrMode = nZheRdwrMode;
	}

	/**
	 * @return the nScreenType
	 */
	public short getnScreenType() {
		return nScreenType;
	}

	/**
	 * @param nScreenType the nScreenType to set
	 */
	public void setnScreenType(short nScreenType) {
		this.nScreenType = nScreenType;
	}

	/**
	 * @return the nDotCorrectTye
	 */
	public short getnDotCorrectTye() {
		return nDotCorrectTye;
	}

	/**
	 * @param nDotCorrectTye the nDotCorrectTye to set
	 */
	public void setnDotCorrectTye(short nDotCorrectTye) {
		this.nDotCorrectTye = nDotCorrectTye;
	}

	/**
	 * @return the bVirtualChangeFlag
	 */
	public boolean isbVirtualChangeFlag() {
		return bVirtualChangeFlag;
	}

	/**
	 * @param bVirtualChangeFlag the bVirtualChangeFlag to set
	 */
	public void setbVirtualChangeFlag(boolean bVirtualChangeFlag) {
		this.bVirtualChangeFlag = bVirtualChangeFlag;
	}

	/**
	 * @return the bVirtualPrime
	 */
	public boolean isbVirtualPrime() {
		return bVirtualPrime;
	}

	/**
	 * @param bVirtualPrime the bVirtualPrime to set
	 */
	public void setbVirtualPrime(boolean bVirtualPrime) {
		this.bVirtualPrime = bVirtualPrime;
	}

	/**
	 * @return the bTest
	 */
	public boolean isbTest() {
		return bTest;
	}

	/**
	 * @param bTest the bTest to set
	 */
	public void setbTest(boolean bTest) {
		this.bTest = bTest;
	}

	/**
	 * @return the fBrightnessEfficent
	 */
	public float getfBrightnessEfficent() {
		return fBrightnessEfficent;
	}

	/**
	 * @param fBrightnessEfficent the fBrightnessEfficent to set
	 */
	public void setfBrightnessEfficent(float fBrightnessEfficent) {
		this.fBrightnessEfficent = fBrightnessEfficent;
	}

	/**
	 * @return the nMinOEWidth
	 */
	public short getnMinOEWidth() {
		return nMinOEWidth;
	}

	/**
	 * @param nMinOEWidth the nMinOEWidth to set
	 */
	public void setnMinOEWidth(short nMinOEWidth) {
		this.nMinOEWidth = nMinOEWidth;
	}

	/**
	 * @return the bScanColorDepthChangeFlag
	 */
	public boolean isbScanColorDepthChangeFlag() {
		return bScanColorDepthChangeFlag;
	}

	/**
	 * @param bScanColorDepthChangeFlag the bScanColorDepthChangeFlag to set
	 */
	public void setbScanColorDepthChangeFlag(boolean bScanColorDepthChangeFlag) {
		this.bScanColorDepthChangeFlag = bScanColorDepthChangeFlag;
	}

	/**
	 * @return the nScanColorDepthPrime
	 */
	public short getnScanColorDepthPrime() {
		return nScanColorDepthPrime;
	}

	/**
	 * @param nScanColorDepthPrime the nScanColorDepthPrime to set
	 */
	public void setnScanColorDepthPrime(short nScanColorDepthPrime) {
		this.nScanColorDepthPrime = nScanColorDepthPrime;
	}

	/**
	 * @return the bDotOpenDetection
	 */
	public boolean isbDotOpenDetection() {
		return bDotOpenDetection;
	}

	/**
	 * @param bDotOpenDetection the bDotOpenDetection to set
	 */
	public void setbDotOpenDetection(boolean bDotOpenDetection) {
		this.bDotOpenDetection = bDotOpenDetection;
	}

	/**
	 * @return the nPWMOutputMode
	 */
	public short getnPWMOutputMode() {
		return nPWMOutputMode;
	}

	/**
	 * @param nPWMOutputMode the nPWMOutputMode to set
	 */
	public void setnPWMOutputMode(short nPWMOutputMode) {
		this.nPWMOutputMode = nPWMOutputMode;
	}

	/**
	 * @return the bMultiRefreshUnderStaticScan
	 */
	public boolean isbMultiRefreshUnderStaticScan() {
		return bMultiRefreshUnderStaticScan;
	}

	/**
	 * @param bMultiRefreshUnderStaticScan the bMultiRefreshUnderStaticScan to set
	 */
	public void setbMultiRefreshUnderStaticScan(boolean bMultiRefreshUnderStaticScan) {
		this.bMultiRefreshUnderStaticScan = bMultiRefreshUnderStaticScan;
	}

	/**
	 * @return the nPixsPerSection
	 */
	public int getnPixsPerSection() {
		return nPixsPerSection;
	}

	/**
	 * @param nPixsPerSection the nPixsPerSection to set
	 */
	public void setnPixsPerSection(int nPixsPerSection) {
		this.nPixsPerSection = nPixsPerSection;
	}

	/**
	 * @return the nZoneClkNum
	 */
	public int getnZoneClkNum() {
		return nZoneClkNum;
	}

	/**
	 * @param nZoneClkNum the nZoneClkNum to set
	 */
	public void setnZoneClkNum(int nZoneClkNum) {
		this.nZoneClkNum = nZoneClkNum;
	}

	/**
	 * @return the bExtendedEnable
	 */
	public boolean isbExtendedEnable() {
		return bExtendedEnable;
	}

	/**
	 * @param bExtendedEnable the bExtendedEnable to set
	 */
	public void setbExtendedEnable(boolean bExtendedEnable) {
		this.bExtendedEnable = bExtendedEnable;
	}

	/**
	 * @return the bExtendedEnableEx
	 */
	public boolean isbExtendedEnableEx() {
		return bExtendedEnableEx;
	}

	/**
	 * @param bExtendedEnableEx the bExtendedEnableEx to set
	 */
	public void setbExtendedEnableEx(boolean bExtendedEnableEx) {
		this.bExtendedEnableEx = bExtendedEnableEx;
	}

	/**
	 * @return the nSectionWidth
	 */
	public short getnSectionWidth() {
		return nSectionWidth;
	}

	/**
	 * @param nSectionWidth the nSectionWidth to set
	 */
	public void setnSectionWidth(short nSectionWidth) {
		this.nSectionWidth = nSectionWidth;
	}

	/**
	 * @return the nSectionHorNum
	 */
	public short getnSectionHorNum() {
		return nSectionHorNum;
	}

	/**
	 * @param nSectionHorNum the nSectionHorNum to set
	 */
	public void setnSectionHorNum(short nSectionHorNum) {
		this.nSectionHorNum = nSectionHorNum;
	}

	/**
	 * @return the nCard_zone_width
	 */
	public short getnCard_zone_width() {
		return nCard_zone_width;
	}

	/**
	 * @param nCard_zone_width the nCard_zone_width to set
	 */
	public void setnCard_zone_width(short nCard_zone_width) {
		this.nCard_zone_width = nCard_zone_width;
	}

	/**
	 * @return the nCard_zone_Num
	 */
	public short getnCard_zone_Num() {
		return nCard_zone_Num;
	}

	/**
	 * @param nCard_zone_Num the nCard_zone_Num to set
	 */
	public void setnCard_zone_Num(short nCard_zone_Num) {
		this.nCard_zone_Num = nCard_zone_Num;
	}

	/**
	 * @return the nGrayEnhance
	 */
	public short nGrayEnhanceMode() {
		return nGrayEnhance;
	}

	/**
	 * @param nGrayEnhance the nGrayEnhance to set
	 */
	public void setnGrayEnhance(short nGrayEnhance) {
		this.nGrayEnhance = nGrayEnhance;
	}

	/**
	 * @return the nGrayEnhanceMode
	 */
	public short getnGrayEnhanceMode() {
		return nGrayEnhanceMode;
	}

	/**
	 * @param nGrayEnhanceMode the nGrayEnhanceMode to set
	 */
	public void setnGrayEnhanceMode(short nGrayEnhanceMode) {
		this.nGrayEnhanceMode = nGrayEnhanceMode;
	}

	/**
	 * @return the bOpenCabinetLamp
	 */
	public boolean isbOpenCabinetLamp() {
		return bOpenCabinetLamp;
	}

	/**
	 * @param bOpenCabinetLamp the bOpenCabinetLamp to set
	 */
	public void setbOpenCabinetLamp(boolean bOpenCabinetLamp) {
		this.bOpenCabinetLamp = bOpenCabinetLamp;
	}

	/**
	 * @return the nSecondHighLevel
	 */
	public short getnSecondHighLevel() {
		return nSecondHighLevel;
	}

	/**
	 * @param nSecondHighLevel the nSecondHighLevel to set
	 */
	public void setnSecondHighLevel(short nSecondHighLevel) {
		this.nSecondHighLevel = nSecondHighLevel;
	}

	/**
	 * @return the fLightRatio
	 */
	public float getfLightRatio() {
		return fLightRatio;
	}

	/**
	 * @param fLightRatio the fLightRatio to set
	 */
	public void setfLightRatio(float fLightRatio) {
		this.fLightRatio = fLightRatio;
	}

	/**
	 * @return the nCustomGamam
	 */
	public int getnCustomGamam() {
		return nCustomGamam;
	}

	/**
	 * @param nCustomGamam the nCustomGamam to set
	 */
	public void setnCustomGamam(int nCustomGamam) {
		this.nCustomGamam = nCustomGamam;
	}

	/**
	 * @return the bChipPrecharge
	 */
	public boolean isbChipPrecharge() {
		return bChipPrecharge;
	}

	/**
	 * @param bChipPrecharge the bChipPrecharge to set
	 */
	public void setbChipPrecharge(boolean bChipPrecharge) {
		this.bChipPrecharge = bChipPrecharge;
	}

	/**
	 * @return the bGClkCtrlByRGBEnable
	 */
	public boolean isbGClkCtrlByRGBEnable() {
		return bGClkCtrlByRGBEnable;
	}

	/**
	 * @param bGClkCtrlByRGBEnable the bGClkCtrlByRGBEnable to set
	 */
	public void setbGClkCtrlByRGBEnable(boolean bGClkCtrlByRGBEnable) {
		this.bGClkCtrlByRGBEnable = bGClkCtrlByRGBEnable;
	}

	/**
	 * @return the bGClkCtrlByREnable
	 */
	public boolean isbGClkCtrlByREnable() {
		return bGClkCtrlByREnable;
	}

	/**
	 * @param bGClkCtrlByREnable the bGClkCtrlByREnable to set
	 */
	public void setbGClkCtrlByREnable(boolean bGClkCtrlByREnable) {
		this.bGClkCtrlByREnable = bGClkCtrlByREnable;
	}

	/**
	 * @return the bGClkCtrlByGEnable
	 */
	public boolean isbGClkCtrlByGEnable() {
		return bGClkCtrlByGEnable;
	}

	/**
	 * @param bGClkCtrlByGEnable the bGClkCtrlByGEnable to set
	 */
	public void setbGClkCtrlByGEnable(boolean bGClkCtrlByGEnable) {
		this.bGClkCtrlByGEnable = bGClkCtrlByGEnable;
	}

	/**
	 * @return the bGClkCtrlByBEnable
	 */
	public boolean isbGClkCtrlByBEnable() {
		return bGClkCtrlByBEnable;
	}

	/**
	 * @param bGClkCtrlByBEnable the bGClkCtrlByBEnable to set
	 */
	public void setbGClkCtrlByBEnable(boolean bGClkCtrlByBEnable) {
		this.bGClkCtrlByBEnable = bGClkCtrlByBEnable;
	}

	/**
	 * @return the nGClkDelay
	 */
	public short getnGClkDelay() {
		return nGClkDelay;
	}

	/**
	 * @param nGClkDelay the nGClkDelay to set
	 */
	public void setnGClkDelay(short nGClkDelay) {
		this.nGClkDelay = nGClkDelay;
	}

	/**
	 * @return the nGClkDelay_G
	 */
	public short getnGClkDelay_G() {
		return nGClkDelay_G;
	}

	/**
	 * @param nGClkDelay_G the nGClkDelay_G to set
	 */
	public void setnGClkDelay_G(short nGClkDelay_G) {
		this.nGClkDelay_G = nGClkDelay_G;
	}

	/**
	 * @return the nGClkDelay_B
	 */
	public short getnGClkDelay_B() {
		return nGClkDelay_B;
	}

	/**
	 * @param nGClkDelay_B the nGClkDelay_B to set
	 */
	public void setnGClkDelay_B(short nGClkDelay_B) {
		this.nGClkDelay_B = nGClkDelay_B;
	}

	/**
	 * @return the nDrive_ic_reg
	 */
	public Drive_ic_reg getnDrive_ic_reg() {
		return nDrive_ic_reg;
	}

	/**
	 * @param nDrive_ic_reg the nDrive_ic_reg to set
	 */
	public void setnDrive_ic_reg(Drive_ic_reg nDrive_ic_reg) {
		this.nDrive_ic_reg = nDrive_ic_reg;
	}
	

	public short getnNetPortPriority() {
		return nNetPortPriority;
	}



	public void setnNetPortPriority(short nNetPortPriority) {
		this.nNetPortPriority = nNetPortPriority;
	}



	public boolean isbLockNetPort() {
		return bLockNetPort;
	}



	public void setbLockNetPort(boolean bLockNetPort) {
		this.bLockNetPort = bLockNetPort;
	}



	public int getnDeductBit() {
		return nDeductBit;
	}



	public void setnDeductBit(int nDeductBit) {
		this.nDeductBit = nDeductBit;
	}



	public short getnGrayEnhance() {
		return nGrayEnhance;
	}



	public void setnModuleSectionNumber(short nModuleSectionNumber) {
		this.nModuleSectionNumber = nModuleSectionNumber;
	}
	
	/**
	 * 
	 */

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
		_MBI5152,  //新增以下芯片
		_MBI5153,
		_MBI5153_E,//聚积开放了MBI5153驱动芯片的第三组寄存器的设置
		_MBI5043, //芯片类型和_MBI5041一样，bitPWM模式时刷新率算法不一样
		_MBI5155 ,//芯片和_E稍微不同
		_TLC5958
	};
	
	//ONE_SCAN_CARD_WIDTH	2	128	单块扫描控制卡所对应的LED显示模块中横向像素点的数量，
	//即扫描卡宽度。高位在前，低位在后。取值范围1~256，必须是16的整数倍
	private short nScanCardWidth;
	//ONE_SCAN_CARD_HEIGHT	2	96	单块扫描控制卡所对应的LED显示模块中纵向像素点的数量，
	//即扫描卡高度。高位在前，低位在后。取值范围1~256，必须是16的整数倍
	private short nScanCardHeight;
	//ONE_SCAN_CARD_WIDTH_REAL		
	//即扫描卡实际宽度。主要用于逐点检测和校正中有关模组的水平垂直个数
	private short nScanCardWidthReal;
	//ONE_SCAN_CARD_HEIGHT_REAL
	//即扫描卡实际高度。主要用于逐点检测和校正中有关模组的水平垂直个数
	private short nScanCardHeightReal;
	//1   32  模组宽度，取值范围1-64
	private short nModuleWidth;
	//1   32  模组宽度，取值范围1-64
	private short nModuleHeight;
	//1   3   模组区数 
	//左右进线：模组区数 = 模组高度 / 每区行数
	//上下进线：模组区数 = 模组宽度 / 每区行数
	private short nModuleSectionNumber;
	//1    4  模组横向个数，模组横向个数 = 扫描卡宽度/模组宽度
	private short nModuleHorNum;
	//1    3  模组纵向个数，模组纵向个数 = 扫描卡高度、模组高度
	private short nModuleVerNum;
	//SCAN_CARD_SECTION_NUM	1	6	扫描卡的区数，区数最大为16 
	//左右进线：扫描卡区数 = 扫描卡高度 / 每区行数
	//上下进线：扫描卡区数 = 扫描卡宽度 / 每区行数
	private short nScanCardSectionNumber;
	//SCAN_CARD_SECTION_ROW_NUM	1	16	扫描卡每区行数，行数最大为16行
	private short nScanCardSectionRowNumber;
	//SCAN_COLOR_DEPTH	1	14	扫描卡扫描的颜色深度，取12~16的整数。
	private short nScanColorDepth;
	//GRAY_LEVEL 扫描卡灰度级别
	private short nGrayLedvel;
	//origin_color_bit		原始颜色深度，比如8BIT,10bit，12bit，16bit
	private short nOrginColorBit;
	//SCAN_MODE	1	4	扫描的模式，取 1-2-4-8-16
	private short nScanMode;
	//DOT_CORRECTION_EN	1	1	单点校正使能，取0，1
	private boolean bEmendBrightness;
	//SCAN_GCLK_FREQUENCY	1	12.5	数据移位时钟时钟频率，最大30Mhz
	private float fScanClkFrequency;
	//ZONE_DCLK_NUM	2		每区移位时钟数，256*16
	private short nZoneDClkNum;
	//1  0-100  数据移位时钟占空比可调等级 
	private short nDutyCycle;
	//PWM_SCAN_GCLK_FREQUENCY	1	12.5	PWM时钟时钟频率，最大30Mhz
	private float fPWMScanClkFrequency;
	//1  0-100  PWM时钟占空比可调等级 
	private short nPWMDutyCycle;
	//CLR_CLK_NUM	1	4	1~255 消隐时钟数(行消隐时间)
	private long nOeClkNumber;
	//REF_FREQ 刷新频率 根据颜色深度和扫描方式不同而不同
	private int nRefreshRate;
	//REF_FREQ_MIN 刷新率最小值
	private int nRefreshRateMin;
	//REF_FREQ_MAX 刷新率最大值
	private int nRefreshRateMax;
	//CONFIG_IC_TIME	配寄存器与逐点检测的时间(芯片间隔时间)， 1-2047
	private short nConfigICTime;
	//dat_freq_num   换帧频率计数器
	private int nDatFreqNum;
	//OE_DELAY_VALUE	0x01	消隐延迟时钟数，消除暗亮，默认为0x01，做减1处理。
	private short nOeDelayValue;
	//SYN_REFRESH_EN		同步刷新使能。默认为0x1，使能
	private boolean bSyncRefresh;
	//VIRTUAL_DISP_EN			虚拟显示使能，默认为0x0，不使能
	private boolean bVirtvalDisp;
	//FREQ_DIVISION_COEF	0x7		150Mhz的分频系数，最大为200分频，值为0.625Mhz。默认为10分频，值为0x09。做减1处理。
	private short nFreqDivisionCoeff;
	//PWM_FREQ_DIVISION_COEF	0x7		150Mhz的PWM分频系数，最大为200分频，值为0.625Mhz。默认为10分频，值为0x09。做减1处理。
	private short nPWMFreqDivisionCoeff;
	//DATA_OUTPUT_REVERSE	输出口逆序包括数据线和扫描线	0x00	数据线逆序：默认为0x0	0x0	不使能	0x1	使能
	private boolean bDataOutUpReverse;	
	//SCAN_OUTPUT_REVERSE		0x00	扫描线逆序：默认为0x0	0x0	不使能	0x1	使能
	private boolean bScanOutUpReverse;	
	//	DCB_LINE_CLK_EN			0x00	使能行信号DCB为时钟使带载高度加倍;	0x0	不使能	0x1	2倍	0x2	3倍	0x3	4倍
	private short nDCBlineClkEn;
	//NO_SIGNAL_DISP	无信号显示：默认为0		0x0：黑屏，0x1：随机画面。0x2：图片
	private short nNoSingleDisp;
	//DATA_INPUT_DIR		0x32	数据方向：（从显示屏正面看）	
	//默认为从右到左0x1	0x0	从左往右	0x1	从右往左	0x2	从上往下	0x3	从下往上
	private short nDataInputDir;
	//ROW_DECODE_MODE		行译码方式：默认0x2
	//	0x0	静态无译码					0x6	164译码
	//	0x1	无译码芯片，直接驱动行管	0x7	192译码
	//	0x2	138译码						0x8	193译码
	//	0x3	139译码						0x9	595译码
	//	0x4	145译码或138双O				0xA	4096译码
	//	0x5	154译码						0xB	
	private short nRowDecodeMOde;
	//	DATA_LINE_TYPE	8	0x14	7--0	数据类型大类：默认为0x00，
	private short nDataLineTypeRange;
	//	DATA_LINE_TYPE	8	0x14	7--0	数据类型：默认为0x00，
	//	0x00-0x1F	红绿蓝分开, 
	//	0x20-0x18	红绿蓝合一三色1点串行
	//	0x30-0x38	红绿蓝合一三色8点串行
	//	0x40-0x48	红绿蓝合一三色16点串行
	//	0x50-0x6F	红绿蓝合一四色串行
	private short nDataLineType;
	//DATA_LINE_CTRL	8	0x00	数据线控制,
	//控制4根数据线RB,B,G,RA的亮灭。注：对用bit为0：亮，为1：灭。默认：都亮
	private short nDataLineCtrl;
	//FIELD_NUM			总场数，最大为136场，做减1处理。
	private short nFieldNum;
	//HALF_FIELD_NUM	半场数，最大为9场.默认为0x6，减1处理
	private short nHalfFieldNumber;
	//FULL_FIELD_NUM	全场数，最大为128场，减1处理
	private short nFullFieldNumber;
	//起始场
	private short nStartField;
	//终止场
	private short nEndField;
	//DATA_POLARITY	4	数据极性：默认为0x0	0x0	高电平点亮	0x1	低电平点亮	0x2-0xF	其他14种情况，预留
	private short nDataPolarity;
	//OE_POLARITY	0x1F	OE极性：默认为0x0	0x0	低有效	0x1	高有效
	private short nOePolarity;
	//EMPTY_INSERT_EN			空点插入使能，即每多少点插入多少空点.	默认0x0，不使能
	private boolean bEmptyInsertEnable;
	//INSERT_MODE				插入空点方式，前插入还是后插入。	1：前插入空点。0：后插入空点
	private short nInsertMode;
	//EMPTY_DOT_NUM				插入的空点数，每次最大只能插入64空点，做减1处理。
	private short nEmptyDotNum;
	//REAL_DOT_NUM		15--0	每多少点插入空点，做减1处理。
	private short nRealDotNum;
	//双列输出
	private boolean bDualOutput;
	//虚拟像素排布方式,//new虚拟像素排布方式，0：红A,绿/蓝,红B，1：蓝,绿/空,红;绿,蓝/空,红，2：蓝,绿/空,红;蓝,绿/红,空，3：红,绿/空,蓝;绿,红/空,蓝
	private short nVirTualArray;
	//灯板芯片
	//private CHIP_TYPE nChipType;
	private int nChipType;
	//ref_doule_value	刷新率倍增的倍数，
	private short nRefreshDoubleValue;
	//zhe_rdwr_mode		折处理模块读写折DPRAM的方式。默认为0
	//0：按列8读写，1：按箱体行读写
	private short nZheRdwrMode;

	//显示屏类型  2-全彩实像素，3-全彩虚拟
	private short nScreenType;
	//单点校正类型, 0-无 1-调亮，2-调色
	private short nDotCorrectTye;

	//虚拟显示变化
	private boolean bVirtualChangeFlag;
	//原先虚拟显示
	private boolean bVirtualPrime;

	//测试效果/测试结束
	private boolean bTest;

	//亮度有效率
	private float fBrightnessEfficent;
	//最小OE宽度
	private short nMinOEWidth;

	//颜色深度变化标识
	private boolean bScanColorDepthChangeFlag;
	//原先颜色深度
	private short nScanColorDepthPrime;
	//使能逐点开路检测功能 PWM版有,通用芯片无；1 - 使能， 0-不使能
	private boolean bDotOpenDetection;
	//PWM输出模式	MBI5030: 0-打撒模式 1-普通模式；TC62D722: 1-打撒模式，0-普通模式
	private short nPWMOutputMode;
	//文件名称
	//string sFileName;
	//刷新率倍增，PWM静态扫描下有效
	private boolean bMultiRefreshUnderStaticScan;

	//1区静态下的像素数
	private int nPixsPerSection;
	//每区移数据(125MHz)的时钟数
	private int nZoneClkNum;

	//sunj 扩区
	//扩区使能
	private boolean bExtendedEnable;
	private boolean bExtendedEnableEx;
	//区宽
	private short nSectionWidth;
	//横向区个数
	private short nSectionHorNum;

	//卡区宽
	private short nCard_zone_width;
	//卡区个数
	private short nCard_zone_Num;

	//灰度增强位数,0-16
	private short nGrayEnhance;
	//灰度增强方式
	private short nGrayEnhanceMode;
	//箱体指示灯
	private boolean bOpenCabinetLamp;
	//2013-3-5 通用芯片升级
	private short nSecondHighLevel;
	private float fLightRatio;
	//Gamma位数，8，10,12位，默认8位
	private int nCustomGamam;

	//芯片预充电功能, 0 - 打开 1， 关闭,默认打开
	private boolean bChipPrecharge;

	//RGB三色分别控制GCLK的延迟时间 使能，如果为false，则nGClkDelay有效，如果true，则nGClkDelay，nGClkDelay_G,nGClkDelay_B同时有效
	private boolean bGClkCtrlByRGBEnable;

	//R色控制GCLK的延迟时间
	private boolean bGClkCtrlByREnable;

	//G色控制GCLK的延迟时间
	private boolean bGClkCtrlByGEnable;

	//B色控制GCLK的延迟时间
	private boolean bGClkCtrlByBEnable;
	//（R的）GCLK的延迟时钟数
	private short nGClkDelay;

	//G的GCLK的延迟时钟数
	private short nGClkDelay_G;

	//B的GCLK的延迟时钟数
	private short nGClkDelay_B;

	//寄存器信息 _TLC5958使用
	private Drive_ic_reg nDrive_ic_reg;
	
	//网口优先设置
	//0：A口优先；1：B口优先
	private short nNetPortPriority;

	//锁定优先网口
	private boolean bLockNetPort;

	//减去的色深位数
	private int nDeductBit;

}
