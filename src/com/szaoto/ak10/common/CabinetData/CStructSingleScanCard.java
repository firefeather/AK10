/*
   * �ļ��� CStructSingleScanCard.java
   * ���������б�com.szaoto.ak10.common
   * �汾��Ϣ���汾��
   * ��������2014��4��1������7:20:34
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.common.CabinetData;

/*
 * ����CStructSingleScanCard
 * ���� liangdb
 * ��Ҫ���� ����ɨ�迨�ṹ
 * ��������2014��4��1��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class CStructSingleScanCard {
	public CStructSingleScanCard()
	{
		//ONE_SCAN_CARD_WIDTH	2	128	����ɨ����ƿ�����Ӧ��LED��ʾģ���к������ص��������
		//��ɨ�迨��ȡ���λ��ǰ����λ�ں�ȡֵ��Χ1~256��������16��������
		nScanCardWidth = 128;
		//ONE_SCAN_CARD_HEIGHT	2	96	����ɨ����ƿ�����Ӧ��LED��ʾģ�����������ص��������
		//��ɨ�迨�߶ȡ���λ��ǰ����λ�ں�ȡֵ��Χ1~256��������16��������
		nScanCardHeight = 96;
		//ONE_SCAN_CARD_WIDTH_REAL		
		//��ɨ�迨ʵ�ʿ�ȡ���Ҫ����������У�����й�ģ���ˮƽ��ֱ����
		nScanCardWidthReal = 128;
		//ONE_SCAN_CARD_HEIGHT_REAL
		//��ɨ�迨ʵ�ʸ߶ȡ���Ҫ����������У�����й�ģ���ˮƽ��ֱ����
		nScanCardHeightReal = 128;
		//1   32  ģ���ȣ�ȡֵ��Χ1-64
		nModuleWidth = 32;
		//1   32  ģ���ȣ�ȡֵ��Χ1-64
		nModuleHeight = 32;
		//1   3   ģ������  ģ������ = ģ��߶�/ÿ������
		nModuleSectionNumber = 3;
		//1    4  ģ����������ģ�������� = ɨ�迨���/ģ����
		nModuleHorNum = 4;
		//1    3  ģ�����������ģ��������� = ɨ�迨�߶ȡ�ģ��߶�
		nModuleVerNum = 3;
		//SCAN_CARD_SECTION_NUM	1	16	ɨ�迨���������������Ϊ16
		nScanCardSectionNumber = 16;
		//SCAN_CARD_SECTION_ROW_NUM	1	16	ɨ�迨ÿ���������������Ϊ16��
		nScanCardSectionRowNumber = 32;
		//SCAN_COLOR_DEPTH	1	14	ɨ�迨ɨ�����ɫ��ȣ�ȡ12~16��������
		nScanColorDepth = 8;
		//GRAY_LEVEL ɨ�迨�Ҷȼ���
		nGrayLedvel = 18;
		//origin_color_bit		ԭʼ��ɫ��ȣ�����8BIT,10bit��12bit��16bit
		nOrginColorBit = 8;
		//SCAN_MODE	1	16	ɨ���ģʽ��ȡ 1-2-4-8-16
		nScanMode = 32;
		//DOT_CORRECTION_EN	1	1	����У��ʹ�ܣ�ȡ0��1
		bEmendBrightness = false;
		//SCAN_GCLK_FREQUENCY	1	15.0	������λʱ��ʱ��Ƶ�ʣ����30Mhz
		fScanClkFrequency = 1.5f;
		//ZONE_DCLK_NUM	2		ÿ����λʱ������256*16
		nZoneDClkNum = 0x1207;
		//duty_cycle_low_value_a1	8	0x32	7--0	������λʱ��ʱ��ռ�ձ����ã����õ͵�ƽ�ļ���ֵ
		nDutyCycle = 0x32;
		//PWM_SCAN_GCLK_FREQUENCY	1	15.0	PWMʱ��ʱ��Ƶ�ʣ����30Mhz
		fPWMScanClkFrequency = 1.5f;
		//1  0-100  PWMʱ��ռ�ձȿɵ��ȼ� 
		nPWMDutyCycle = 0x32;
		//row_oe_clk_num	16	 ������ʱ����������������Ĭ��Ϊ0x0003������1����
		nOeClkNumber = 0x63;
		//ˢ��Ƶ�� ������ɫ��Ⱥ�ɨ�跽ʽ��ͬ����ͬ
		nRefreshRate = 360;
		//REF_FREQ_MIN ˢ������Сֵ
		nRefreshRateMin = 1;
		//REF_FREQ_MAX ˢ�������ֵ
		nRefreshRateMax = 10000;
		//CONFIG_IC_TIME	��Ĵ�����������ʱ��(оƬ���ʱ��)�� 1ms--30s
		nConfigICTime = 1024;
		//dat_freq_num   ��֡Ƶ�ʼ�����
		nDatFreqNum = 0;
		//OE_DELAY_VALUE	0x01	�����ӳ�ʱ����������������Ĭ��Ϊ0x01������1����
		nOeDelayValue = 2;
		//SYN_REFRESH_EN		ͬ��ˢ��ʹ�ܡ�Ĭ��Ϊ0x1��ʹ��
		bSyncRefresh = false;
		//VIRTUAL_DISP_EN			������ʾʹ�ܣ�Ĭ��Ϊ0x0����ʹ��
		bVirtvalDisp = false;
		//FREQ_DIVISION_COEF	0x64		125Mhz�ķ�Ƶϵ�������Ϊ200��Ƶ��ֵΪ0.625Mhz��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1����
		nFreqDivisionCoeff = 0x64;
		//PWM_FREQ_DIVISION_COEF	0x64		125Mhz��PWM��Ƶϵ�������Ϊ200��Ƶ��ֵΪ0.625Mhz��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1����
		nPWMFreqDivisionCoeff = 0x64;
		//DATA_OUTPUT_REVERSE	�����������������ߺ�ɨ����	0x00	����������Ĭ��Ϊ0x0	0x0	��ʹ��	0x1	ʹ��
		bDataOutUpReverse = false;	
		//SCAN_OUTPUT_REVERSE		0x00	ɨ��������Ĭ��Ϊ0x0	0x0	��ʹ��	0x1	ʹ��
		bScanOutUpReverse = false;		
		//	DCB_LINE_CLK_EN			0x00	ʹ�����ź�DCBΪʱ��ʹ���ظ߶ȼӱ�;	0x0	��ʹ��	0x1	2��	0x2	3��	0x3	4��
		nDCBlineClkEn = 0;
		//NO_SIGNAL_DISP	���ź���ʾ��Ĭ��Ϊ0		0x0��������0x1��������档0x2��ͼƬ
		nNoSingleDisp = 0;
		//DATA_INPUT_DIR		0x01	���ݷ��򣺣�����ʾ�����濴��	
		//Ĭ��Ϊ���ҵ���0x1	0x0	��������	0x1	��������	0x2	��������	0x3	��������
		nDataInputDir = 1;
		//ROW_DECODE_MODE		�����뷽ʽ��Ĭ��0x2
		//	0x0	��̬������					0x6	164����
		//	0x1	������оƬ��ֱ�������й�	0x7	192����
		//	0x2	138����						0x8	193����
		//	0x3	139����						0x9	595����
		//	0x4	145�����138˫O				0xA	4096����
		//	0x5	154����						0xB	
		nRowDecodeMOde = 2;
		//	DATA_LINE_TYPE	8	0x14	7--0	�������ʹ��ࣺĬ��Ϊ0x00��
		nDataLineTypeRange = 0;
		//	DATA_LINE_TYPE	8	0x14	7--0	�������ͣ�Ĭ��Ϊ0x00��
		//	0x00-0x1F	�������ֿ�, 
		//	0x20-0x18	��������һ��ɫ1�㴮��
		//	0x30-0x38	��������һ��ɫ8�㴮��
		//	0x40-0x48	��������һ��ɫ16�㴮��
		//	0x50-0x6F	��������һ��ɫ����
		nDataLineType = 0;
		//DATA_LINE_CTRL	8	0x00	�����߿���,
		//����4��������RB,B,G,RA������ע������bitΪ0������Ϊ1����Ĭ�ϣ�����
		nDataLineCtrl = 0x0F;
		//FIELD_NUM			�ܳ��������Ϊ136��������1����
		nFieldNum = 12;
		//HALF_FIELD_NUM	�볡�������Ϊ9��.Ĭ��Ϊ0x6����1����
		nHalfFieldNumber = 5;
		//FULL_FIELD_NUM	ȫ���������Ϊ128��
		nFullFieldNumber = 6;
		//��ʼ��
		nStartField = 32;
		//��ֹ��
		nEndField = 8;
		//DATA_POLARITY	4	���ݼ��ԣ�Ĭ��Ϊ0x0	0x0	�ߵ�ƽ����	0x1	�͵�ƽ����	0x2-0xF	����14�������Ԥ��
		nDataPolarity = 0;
		//OE_POLARITY	0x1F	OE���ԣ�Ĭ��Ϊ0x0	0x0	����Ч	0x1	����Ч
		nOePolarity = 0;
		//EMPTY_INSERT_EN			�յ����ʹ�ܣ���ÿ���ٵ������ٿյ�.	Ĭ��0x0����ʹ��
		bEmptyInsertEnable = false;
		//INSERT_MODE				����յ㷽ʽ��ǰ���뻹�Ǻ���롣	1��ǰ����յ㡣0�������յ�
		nInsertMode = 0;
		//EMPTY_DOT_NUM				����Ŀյ�����ÿ�����ֻ�ܲ���64�յ㣬����1����
		nEmptyDotNum = 1;
		//REAL_DOT_NUM		15--0	ÿ���ٵ����յ㣬����1����
		nRealDotNum = 1;
		//˫�����
		bDualOutput = false;
		//old���������Ų���ʽ��0����A��/����B��1����A��/�̺�B��2���̺�A/��B����3������A/��B��
		//new���������Ų���ʽ��0����A,��/��,��B��1����,��/��,�죬2����,��/��,��;��,��/��,��
		nVirTualArray = 0;
		//�ư�оƬ  0:ͨ��оƬ��1��28161/165��2��28162
		nChipType = CHIP_TYPE._GENERAL.ordinal();
		//ref_doule_value	ˢ���ʱ����ı�����
		nRefreshDoubleValue = 1;
		//zhe_rdwr_mode		�۴���ģ���д��DPRAM�ķ�ʽ��Ĭ��Ϊ0
		//0������8��д��1���������ж�д
		nZheRdwrMode = 0;
		//��ʾ������  0-ȫ��ʵ���أ�1-ȫ������
		nScreenType = 1;
		//����У������, 0-�� 1-������2-��ɫ
		nDotCorrectTye = 0;

		//������ʾ�仯
		bVirtualChangeFlag = false;
		//ԭ��������ʾ
		bVirtualPrime = bVirtvalDisp;

		//����Ч��/���Խ���
		bTest = false;

		//������Ч��
		fBrightnessEfficent = 0.5f;
		//��СOE���
		nMinOEWidth = 8;

		//��ɫ��ȱ仯��ʶ
		bScanColorDepthChangeFlag = false;
		//ԭ����ɫ���
		nScanColorDepthPrime = nScanColorDepth;
		//ʹ����㿪·��⹦�� PWM����,ͨ��оƬ�ޣ�1 - ʹ�ܣ� 0-��ʹ��
		bDotOpenDetection = false;
		//PWM���ģʽ	MBI5030: 0-����ģʽ 1-��ͨģʽ��TC62D722: 1-����ģʽ��0-��ͨģʽ
		nPWMOutputMode = 0;
		//�ļ�����
		//sFileName = "";
		//ˢ���ʱ�����PWM��̬ɨ������Ч
		bMultiRefreshUnderStaticScan = false;
		//sunj ����
		//����ʹ��
		bExtendedEnable = false;
		bExtendedEnableEx = false;
		//����
		nSectionWidth = nModuleWidth;
		//����������
		nSectionHorNum = 1;
		//�Ҷ���ǿλ��
		nGrayEnhance = 0;
		//�Ҷ���ǿ��ʽ
		nGrayEnhanceMode = 3;
		//����ָʾ��
		bOpenCabinetLamp = true;
		//2013-3-5ͨ��оƬ����
		nSecondHighLevel = 2;
		fLightRatio = 1;
		nCustomGamam = 8;

		//оƬԤ��繦��
		bChipPrecharge = false;

		//RGB��ɫ�ֱ����GCLK���ӳ�ʱ�� ʹ��
		bGClkCtrlByRGBEnable = false;

		//Rɫ����GCLK���ӳ�ʱ��
		bGClkCtrlByREnable = false;
		//Gɫ����GCLK���ӳ�ʱ��
		bGClkCtrlByGEnable = false;
		//Bɫ����GCLK���ӳ�ʱ��
		bGClkCtrlByBEnable = false;

		//GCLK���ӳ�ʱ��������ɫ�ֿ�ʱΪR��GCLK�ӳ�ʱ������
		nGClkDelay = 0;
		nGClkDelay_G = 0;//G��GCLK�ӳ�ʱ����
		nGClkDelay_B = 0;//B��GCLK�ӳ�ʱ����
		
		//��ȥ��ɫ��λ��
		nDrive_ic_reg = new Drive_ic_reg();

		//������������
		//0��A�����ȣ�1��B������
		nNetPortPriority = 0;

		//������������
		bLockNetPort = false;

		//��ȥ��ɫ��λ��,Ĭ��Ϊ4
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
		_GENERAL,			//ͨ��оƬ
		_MBI5042,
		_MBI5030,
		_TC62D722,
		_MBI5050,
		_TLC5948,
		_MBI5040,
		_MBI5041,
		_MBI5045,
		_MBI5152,  //��������оƬ
		_MBI5153,
		_MBI5153_E,//�ۻ�������MBI5153����оƬ�ĵ�����Ĵ���������
		_MBI5043, //оƬ���ͺ�_MBI5041һ����bitPWMģʽʱˢ�����㷨��һ��
		_MBI5155 ,//оƬ��_E��΢��ͬ
		_TLC5958
	};
	
	//ONE_SCAN_CARD_WIDTH	2	128	����ɨ����ƿ�����Ӧ��LED��ʾģ���к������ص��������
	//��ɨ�迨��ȡ���λ��ǰ����λ�ں�ȡֵ��Χ1~256��������16��������
	private short nScanCardWidth;
	//ONE_SCAN_CARD_HEIGHT	2	96	����ɨ����ƿ�����Ӧ��LED��ʾģ�����������ص��������
	//��ɨ�迨�߶ȡ���λ��ǰ����λ�ں�ȡֵ��Χ1~256��������16��������
	private short nScanCardHeight;
	//ONE_SCAN_CARD_WIDTH_REAL		
	//��ɨ�迨ʵ�ʿ�ȡ���Ҫ����������У�����й�ģ���ˮƽ��ֱ����
	private short nScanCardWidthReal;
	//ONE_SCAN_CARD_HEIGHT_REAL
	//��ɨ�迨ʵ�ʸ߶ȡ���Ҫ����������У�����й�ģ���ˮƽ��ֱ����
	private short nScanCardHeightReal;
	//1   32  ģ���ȣ�ȡֵ��Χ1-64
	private short nModuleWidth;
	//1   32  ģ���ȣ�ȡֵ��Χ1-64
	private short nModuleHeight;
	//1   3   ģ������ 
	//���ҽ��ߣ�ģ������ = ģ��߶� / ÿ������
	//���½��ߣ�ģ������ = ģ���� / ÿ������
	private short nModuleSectionNumber;
	//1    4  ģ����������ģ�������� = ɨ�迨���/ģ����
	private short nModuleHorNum;
	//1    3  ģ�����������ģ��������� = ɨ�迨�߶ȡ�ģ��߶�
	private short nModuleVerNum;
	//SCAN_CARD_SECTION_NUM	1	6	ɨ�迨���������������Ϊ16 
	//���ҽ��ߣ�ɨ�迨���� = ɨ�迨�߶� / ÿ������
	//���½��ߣ�ɨ�迨���� = ɨ�迨��� / ÿ������
	private short nScanCardSectionNumber;
	//SCAN_CARD_SECTION_ROW_NUM	1	16	ɨ�迨ÿ���������������Ϊ16��
	private short nScanCardSectionRowNumber;
	//SCAN_COLOR_DEPTH	1	14	ɨ�迨ɨ�����ɫ��ȣ�ȡ12~16��������
	private short nScanColorDepth;
	//GRAY_LEVEL ɨ�迨�Ҷȼ���
	private short nGrayLedvel;
	//origin_color_bit		ԭʼ��ɫ��ȣ�����8BIT,10bit��12bit��16bit
	private short nOrginColorBit;
	//SCAN_MODE	1	4	ɨ���ģʽ��ȡ 1-2-4-8-16
	private short nScanMode;
	//DOT_CORRECTION_EN	1	1	����У��ʹ�ܣ�ȡ0��1
	private boolean bEmendBrightness;
	//SCAN_GCLK_FREQUENCY	1	12.5	������λʱ��ʱ��Ƶ�ʣ����30Mhz
	private float fScanClkFrequency;
	//ZONE_DCLK_NUM	2		ÿ����λʱ������256*16
	private short nZoneDClkNum;
	//1  0-100  ������λʱ��ռ�ձȿɵ��ȼ� 
	private short nDutyCycle;
	//PWM_SCAN_GCLK_FREQUENCY	1	12.5	PWMʱ��ʱ��Ƶ�ʣ����30Mhz
	private float fPWMScanClkFrequency;
	//1  0-100  PWMʱ��ռ�ձȿɵ��ȼ� 
	private short nPWMDutyCycle;
	//CLR_CLK_NUM	1	4	1~255 ����ʱ����(������ʱ��)
	private long nOeClkNumber;
	//REF_FREQ ˢ��Ƶ�� ������ɫ��Ⱥ�ɨ�跽ʽ��ͬ����ͬ
	private int nRefreshRate;
	//REF_FREQ_MIN ˢ������Сֵ
	private int nRefreshRateMin;
	//REF_FREQ_MAX ˢ�������ֵ
	private int nRefreshRateMax;
	//CONFIG_IC_TIME	��Ĵ�����������ʱ��(оƬ���ʱ��)�� 1-2047
	private short nConfigICTime;
	//dat_freq_num   ��֡Ƶ�ʼ�����
	private int nDatFreqNum;
	//OE_DELAY_VALUE	0x01	�����ӳ�ʱ����������������Ĭ��Ϊ0x01������1����
	private short nOeDelayValue;
	//SYN_REFRESH_EN		ͬ��ˢ��ʹ�ܡ�Ĭ��Ϊ0x1��ʹ��
	private boolean bSyncRefresh;
	//VIRTUAL_DISP_EN			������ʾʹ�ܣ�Ĭ��Ϊ0x0����ʹ��
	private boolean bVirtvalDisp;
	//FREQ_DIVISION_COEF	0x7		150Mhz�ķ�Ƶϵ�������Ϊ200��Ƶ��ֵΪ0.625Mhz��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1����
	private short nFreqDivisionCoeff;
	//PWM_FREQ_DIVISION_COEF	0x7		150Mhz��PWM��Ƶϵ�������Ϊ200��Ƶ��ֵΪ0.625Mhz��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1����
	private short nPWMFreqDivisionCoeff;
	//DATA_OUTPUT_REVERSE	�����������������ߺ�ɨ����	0x00	����������Ĭ��Ϊ0x0	0x0	��ʹ��	0x1	ʹ��
	private boolean bDataOutUpReverse;	
	//SCAN_OUTPUT_REVERSE		0x00	ɨ��������Ĭ��Ϊ0x0	0x0	��ʹ��	0x1	ʹ��
	private boolean bScanOutUpReverse;	
	//	DCB_LINE_CLK_EN			0x00	ʹ�����ź�DCBΪʱ��ʹ���ظ߶ȼӱ�;	0x0	��ʹ��	0x1	2��	0x2	3��	0x3	4��
	private short nDCBlineClkEn;
	//NO_SIGNAL_DISP	���ź���ʾ��Ĭ��Ϊ0		0x0��������0x1��������档0x2��ͼƬ
	private short nNoSingleDisp;
	//DATA_INPUT_DIR		0x32	���ݷ��򣺣�����ʾ�����濴��	
	//Ĭ��Ϊ���ҵ���0x1	0x0	��������	0x1	��������	0x2	��������	0x3	��������
	private short nDataInputDir;
	//ROW_DECODE_MODE		�����뷽ʽ��Ĭ��0x2
	//	0x0	��̬������					0x6	164����
	//	0x1	������оƬ��ֱ�������й�	0x7	192����
	//	0x2	138����						0x8	193����
	//	0x3	139����						0x9	595����
	//	0x4	145�����138˫O				0xA	4096����
	//	0x5	154����						0xB	
	private short nRowDecodeMOde;
	//	DATA_LINE_TYPE	8	0x14	7--0	�������ʹ��ࣺĬ��Ϊ0x00��
	private short nDataLineTypeRange;
	//	DATA_LINE_TYPE	8	0x14	7--0	�������ͣ�Ĭ��Ϊ0x00��
	//	0x00-0x1F	�������ֿ�, 
	//	0x20-0x18	��������һ��ɫ1�㴮��
	//	0x30-0x38	��������һ��ɫ8�㴮��
	//	0x40-0x48	��������һ��ɫ16�㴮��
	//	0x50-0x6F	��������һ��ɫ����
	private short nDataLineType;
	//DATA_LINE_CTRL	8	0x00	�����߿���,
	//����4��������RB,B,G,RA������ע������bitΪ0������Ϊ1����Ĭ�ϣ�����
	private short nDataLineCtrl;
	//FIELD_NUM			�ܳ��������Ϊ136��������1����
	private short nFieldNum;
	//HALF_FIELD_NUM	�볡�������Ϊ9��.Ĭ��Ϊ0x6����1����
	private short nHalfFieldNumber;
	//FULL_FIELD_NUM	ȫ���������Ϊ128������1����
	private short nFullFieldNumber;
	//��ʼ��
	private short nStartField;
	//��ֹ��
	private short nEndField;
	//DATA_POLARITY	4	���ݼ��ԣ�Ĭ��Ϊ0x0	0x0	�ߵ�ƽ����	0x1	�͵�ƽ����	0x2-0xF	����14�������Ԥ��
	private short nDataPolarity;
	//OE_POLARITY	0x1F	OE���ԣ�Ĭ��Ϊ0x0	0x0	����Ч	0x1	����Ч
	private short nOePolarity;
	//EMPTY_INSERT_EN			�յ����ʹ�ܣ���ÿ���ٵ������ٿյ�.	Ĭ��0x0����ʹ��
	private boolean bEmptyInsertEnable;
	//INSERT_MODE				����յ㷽ʽ��ǰ���뻹�Ǻ���롣	1��ǰ����յ㡣0�������յ�
	private short nInsertMode;
	//EMPTY_DOT_NUM				����Ŀյ�����ÿ�����ֻ�ܲ���64�յ㣬����1����
	private short nEmptyDotNum;
	//REAL_DOT_NUM		15--0	ÿ���ٵ����յ㣬����1����
	private short nRealDotNum;
	//˫�����
	private boolean bDualOutput;
	//���������Ų���ʽ,//new���������Ų���ʽ��0����A,��/��,��B��1����,��/��,��;��,��/��,�죬2����,��/��,��;��,��/��,�գ�3����,��/��,��;��,��/��,��
	private short nVirTualArray;
	//�ư�оƬ
	//private CHIP_TYPE nChipType;
	private int nChipType;
	//ref_doule_value	ˢ���ʱ����ı�����
	private short nRefreshDoubleValue;
	//zhe_rdwr_mode		�۴���ģ���д��DPRAM�ķ�ʽ��Ĭ��Ϊ0
	//0������8��д��1���������ж�д
	private short nZheRdwrMode;

	//��ʾ������  2-ȫ��ʵ���أ�3-ȫ������
	private short nScreenType;
	//����У������, 0-�� 1-������2-��ɫ
	private short nDotCorrectTye;

	//������ʾ�仯
	private boolean bVirtualChangeFlag;
	//ԭ��������ʾ
	private boolean bVirtualPrime;

	//����Ч��/���Խ���
	private boolean bTest;

	//������Ч��
	private float fBrightnessEfficent;
	//��СOE���
	private short nMinOEWidth;

	//��ɫ��ȱ仯��ʶ
	private boolean bScanColorDepthChangeFlag;
	//ԭ����ɫ���
	private short nScanColorDepthPrime;
	//ʹ����㿪·��⹦�� PWM����,ͨ��оƬ�ޣ�1 - ʹ�ܣ� 0-��ʹ��
	private boolean bDotOpenDetection;
	//PWM���ģʽ	MBI5030: 0-����ģʽ 1-��ͨģʽ��TC62D722: 1-����ģʽ��0-��ͨģʽ
	private short nPWMOutputMode;
	//�ļ�����
	//string sFileName;
	//ˢ���ʱ�����PWM��̬ɨ������Ч
	private boolean bMultiRefreshUnderStaticScan;

	//1����̬�µ�������
	private int nPixsPerSection;
	//ÿ��������(125MHz)��ʱ����
	private int nZoneClkNum;

	//sunj ����
	//����ʹ��
	private boolean bExtendedEnable;
	private boolean bExtendedEnableEx;
	//����
	private short nSectionWidth;
	//����������
	private short nSectionHorNum;

	//������
	private short nCard_zone_width;
	//��������
	private short nCard_zone_Num;

	//�Ҷ���ǿλ��,0-16
	private short nGrayEnhance;
	//�Ҷ���ǿ��ʽ
	private short nGrayEnhanceMode;
	//����ָʾ��
	private boolean bOpenCabinetLamp;
	//2013-3-5 ͨ��оƬ����
	private short nSecondHighLevel;
	private float fLightRatio;
	//Gammaλ����8��10,12λ��Ĭ��8λ
	private int nCustomGamam;

	//оƬԤ��繦��, 0 - �� 1�� �ر�,Ĭ�ϴ�
	private boolean bChipPrecharge;

	//RGB��ɫ�ֱ����GCLK���ӳ�ʱ�� ʹ�ܣ����Ϊfalse����nGClkDelay��Ч�����true����nGClkDelay��nGClkDelay_G,nGClkDelay_Bͬʱ��Ч
	private boolean bGClkCtrlByRGBEnable;

	//Rɫ����GCLK���ӳ�ʱ��
	private boolean bGClkCtrlByREnable;

	//Gɫ����GCLK���ӳ�ʱ��
	private boolean bGClkCtrlByGEnable;

	//Bɫ����GCLK���ӳ�ʱ��
	private boolean bGClkCtrlByBEnable;
	//��R�ģ�GCLK���ӳ�ʱ����
	private short nGClkDelay;

	//G��GCLK���ӳ�ʱ����
	private short nGClkDelay_G;

	//B��GCLK���ӳ�ʱ����
	private short nGClkDelay_B;

	//�Ĵ�����Ϣ _TLC5958ʹ��
	private Drive_ic_reg nDrive_ic_reg;
	
	//������������
	//0��A�����ȣ�1��B������
	private short nNetPortPriority;

	//������������
	private boolean bLockNetPort;

	//��ȥ��ɫ��λ��
	private int nDeductBit;

}
