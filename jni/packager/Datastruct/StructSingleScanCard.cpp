#include "StructSingleScanCard.h"
#include "DataStructDef.h"
int max(int x, int y)
{
  return x > y ? x : y;
}

CStructSingleScanCard::CStructSingleScanCard(void)
{
	//ONE_SCAN_CARD_WIDTH	2	128	����ɨ����ƿ�����Ӧ��LED��ʾģ���к������ص��������
	//��ɨ�迨���ȡ���λ��ǰ����λ�ں�ȡֵ��Χ1~256��������16��������
	nScanCardWidth = 128;
	//ONE_SCAN_CARD_HEIGHT	2	96	����ɨ����ƿ�����Ӧ��LED��ʾģ�����������ص��������
	//��ɨ�迨�߶ȡ���λ��ǰ����λ�ں�ȡֵ��Χ1~256��������16��������
	nScanCardHeight = 96;
	//ONE_SCAN_CARD_WIDTH_REAL		
	//��ɨ�迨ʵ�ʿ��ȡ���Ҫ����������У�����й�ģ���ˮƽ��ֱ����
	nScanCardWidthReal = 128;
	//ONE_SCAN_CARD_HEIGHT_REAL
	//��ɨ�迨ʵ�ʸ߶ȡ���Ҫ����������У�����й�ģ���ˮƽ��ֱ����
	nScanCardHeightReal = 128;
	//1   32  ģ����ȣ�ȡֵ��Χ1-64
	nModuleWidth = 32;
	//1   32  ģ����ȣ�ȡֵ��Χ1-64
	nModuleHeight = 32;
	//1   3   ģ������  ģ������ = ģ��߶�/ÿ������
	nModuleSectionNumber = 3;
	//1    4  ģ����������ģ�������� = ɨ�迨����/ģ�����
	nModuleHorNum = 4;
	//1    3  ģ�����������ģ��������� = ɨ�迨�߶ȡ�ģ��߶�
	nModuleVerNum = 3;
	//SCAN_CARD_SECTION_NUM	1	16	ɨ�迨���������������Ϊ16
	nScanCardSectionNumber = 16;
	//SCAN_CARD_SECTION_ROW_NUM	1	16	ɨ�迨ÿ���������������Ϊ16��
	nScanCardSectionRowNumber = 32;

	//Sϵ�в������ӣ��������
	//SCAN_CARD_SECTION_NUM	1	16	ɨ�迨���������������Ϊ16
	nScanCardSectionNumber_cabinet = 16;
	//SCAN_CARD_SECTION_ROW_NUM	1	16	ɨ�迨ÿ���������������Ϊ16��
	nScanCardSectionRowNumber_cabinet = 32;


	//SCAN_COLOR_DEPTH	1	14	ɨ�迨ɨ�����ɫ��ȣ�ȡ12~16��������
	nScanColorDepth = 8;
	//GRAY_LEVEL ɨ�迨�Ҷȼ���
	nGrayLedvel = 18;
	//origin_color_bit		ԭʼ��ɫ��ȣ�����8BIT,10bit��12bit��16bit
	nOrginColorBit = 8;
	//SCAN_MODE	1	16	ɨ���ģʽ��ȡ 1-2-4-8-16
	nScanMode = 32;

	//SCAN_MODE_CABINET	1	4	���������ɨ���ģʽ��������
	nScanMode_cabinet = 32;

	//DOT_CORRECTION_EN	1	1	����У��ʹ�ܣ�ȡ0��1
	bEmendBrightness = 0;
	//SCAN_GCLK_FREQUENCY	1	15.0	������λʱ��ʱ��Ƶ�ʣ����30Mhz
	fScanClkFrequency = 1.5;
	//ZONE_DCLK_NUM	2		ÿ����λʱ������256*16
	nZoneDClkNum = 0x1207;
	//duty_cycle_low_value_a1	8	0x32	7--0	������λʱ��ʱ��ռ�ձ����ã����õ͵�ƽ�ļ���ֵ
	nDutyCycle = 0x32;
	//PWM_SCAN_GCLK_FREQUENCY	1	15.0	PWMʱ��ʱ��Ƶ�ʣ����30Mhz
	fPWMScanClkFrequency = 1.5;
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
	//OE_DELAY_VALUE	0x01	�����ӳ�ʱ����������������Ĭ��Ϊ0x01������1������
	nOeDelayValue = 2;
	//SYN_REFRESH_EN		ͬ��ˢ��ʹ�ܡ�Ĭ��Ϊ0x1��ʹ��
	bSyncRefresh = false;
	//VIRTUAL_DISP_EN			������ʾʹ�ܣ�Ĭ��Ϊ0x0����ʹ��
	bVirtvalDisp = false;
	//FREQ_DIVISION_COEF	0x64		125Mhz�ķ�Ƶϵ�������Ϊ200��Ƶ��ֵΪ0.625Mhz��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1������
	nFreqDivisionCoeff = 0x64;
	//PWM_FREQ_DIVISION_COEF	0x64		125Mhz��PWM��Ƶϵ�������Ϊ200��Ƶ��ֵΪ0.625Mhz��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1������
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
	//FIELD_NUM			�ܳ��������Ϊ136��������1������
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
	
	///////////�յ����////////////
	//EMPTY_INSERT_EN			�յ����ʹ�ܣ���ÿ���ٵ������ٿյ�.	Ĭ��0x0����ʹ��
	bEmptyInsertEnable = 0;
	//INSERT_MODE				����յ㷽ʽ��ǰ���뻹�Ǻ���롣	1��ǰ����յ㡣0�������յ�
	nInsertMode = 0;
	//EMPTY_DOT_NUM				����Ŀյ�����ÿ�����ֻ�ܲ���64�յ㣬����1������
	nEmptyDotNum = 1;
	//REAL_DOT_NUM		15--0	ÿ���ٵ����յ㣬����1������
	nRealDotNum = 1;

	///////////��������////////////
	//EMPTY_INSERT_EN			��������ʹ�ܣ���ÿ������������ٿ���.	Ĭ��0x0����ʹ��
	bEmptySectionInsertEnable = 0;
	//INSERT_MODE				���������ʽ��ǰ���뻹�Ǻ���롣	1��ǰ���������0����������
	nInsertSectionMode = 0;
	//EMPTY_DOT_NUM				����Ŀ�������ÿ�����ֻ�ܲ���64����������1������
	nEmptySectionNum = 1;
	//REAL_DOT_NUM		15--0	ÿ���ٵ�������������1������
	nRealSectionNum = 1;


	//˫�����
	bDualOutput = 0;
	//old���������Ų���ʽ��0����A��/����B��1����A��/�̺�B��2���̺�A/��B����3������A/��B��
	//new���������Ų���ʽ��0����A,��/��,��B��1����,��/��,�죬2����,��/��,��;��,��/��,��
	nVirTualArray = 0;
	//�ư�оƬ  0:ͨ��оƬ��1��28161/165��2��28162
	nChipType = _GENERAL;
	//ref_doule_value	ˢ���ʱ����ı�����
	nRefreshDoubleValue = 1;
	//zhe_rdwr_mode		�۴���ģ���д��DPRAM�ķ�ʽ��Ĭ��Ϊ0
	//0������8��д��1���������ж�д
	nZheRdwrMode = 0;
	//��ʾ������  0-ȫ��ʵ���أ�1-ȫ������
	nScreenType = DISPLAY_TYPE_REAL;
	//����У������, 0-�� 1-������2-��ɫ
	nDotCorrectTye = 0;

	//������ʾ�仯
	bVirtualChangeFlag = false;
	//ԭ��������ʾ
	bVirtualPrime = bVirtvalDisp;

	//����Ч��/���Խ���
	bTest = false;

	//������Ч��
	fBrightnessEfficent = 0.5;
	//��СOE����
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

	//if (_TLC5958 == nChipType)
	{
		//reg1
		nDrive_ic_reg.nBright = 4;//ȫ�����ȵ���
		nDrive_ic_reg.nLgse_R = 0;//��ɫ�ͻ���ǿ	
		nDrive_ic_reg.nLgse_G = 0;//��ɫ�ͻ���ǿ
		nDrive_ic_reg.nLgse_B = 0;//��ɫ�ͻ���ǿ
		nDrive_ic_reg.nGdly_Enable = 1;//���ͨ���ӳ�ʹ��
		nDrive_ic_reg.nTD_Delay = 1;//���������ӳ�
		nDrive_ic_reg.nLodvth = 1;//��·����ѹ�趨

		//reg2
		nDrive_ic_reg.nGlobal_Lgse = 0;//ȫ�ֵͻ���ǿ
		nDrive_ic_reg.nPVM_Mode = 0;//��ɢģʽ
		nDrive_ic_reg.nEMI_R = 0;//��ɫEMI����
		nDrive_ic_reg.nEMI_G = 0;//��ɫEMI����
		nDrive_ic_reg.nEMI_B = 0;//��ɫEMI����	
		nDrive_ic_reg.nPre_Charge = 0;//Ԥ���ģʽ

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

		//��MBI5153���ۻ�������MBI5153����оƬ�ĵ�����Ĵ���������.
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RHigh = 0xFF;//�Ĵ���1����ֽ�,Ĭ��ֵ0x9F2B
		if (_MBI5155 == nChipType)
		{
			//�ҽ�cfg1[7] 16��14��Ĭ��14bit
			nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RLow = 0xAB; //�Ĵ���1����ֽ�
		} 
		else//_MBI5153_E == m_nChipType
		{
			//�ҽ�cfg1[7] 14��13��Ĭ��14bit
			nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RLow = 0x2B; //�Ĵ���1����ֽ�
		}
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1GHigh = 0xFF;//�Ĵ���1�̸��ֽ�,Ĭ��ֵ0xDF2B
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1GLow = 0x2B; //�Ĵ���1�̵��ֽ�
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1BHigh = 0xFF;//�Ĵ���1�����ֽ�,Ĭ��ֵ0xDF2B
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1BLow = 0x2B; //�Ĵ���1�����ֽ�

		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2RHigh = 0x0F;//�Ĵ���2����ֽ�,Ĭ��ֵ0x4600
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2RLow = 0x00; //�Ĵ���2����ֽ�
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2GHigh = 0x06;//�Ĵ���2�̸��ֽ�,Ĭ��ֵ0x4500
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2GLow = 0x00; //�Ĵ���2�̵��ֽ�
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2BHigh = 0x26;//�Ĵ���2�����ֽ�,Ĭ��ֵ0x6500
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2BLow = 0x00; //�Ĵ���2�����ֽ�

		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RHigh = 0xC0;//�Ĵ���3����ֽ�,Ĭ��ֵ0xC003
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RLow = 0x03; //�Ĵ���3����ֽ�
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3GHigh = 0x50;//�Ĵ���3�̸��ֽ�,Ĭ��ֵ0x5003
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3GLow = 0x03; //�Ĵ���3�̵��ֽ�
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3BHigh = 0x40;//�Ĵ���3�����ֽ�,Ĭ��ֵ0x4003
		nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3BLow = 0x03; //�Ĵ���3�����ֽ�

		nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT = 0;//MBI5155 ��513/257��GCLK�ĵ͵�ƽ���� 
		nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaF = 0;//MBI5155 ��513/257��GCLK�ĸߵ�ƽ���� 
		nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT = 30;//MBI5155 ��1��GCLK�ĸߵ�ƽ���� 
		nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_H = 34;//MBI5155 ��514/258��GCLK�ĸߵ�ƽ����
		nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_L = 22;//MBI5155 ��514/258��GCLK�ĵ͵�ƽ���� 
	}

	//if (MBI5043 == nChipType)
	{
		//MBI5153 GCLK˫�ز���	0���رգ� 1������
		nDrive_ic_reg.sDrive_ic_reg_MBI5043.bGCLKDoublesampling = false;
		//MBI5153 PWMģʽѡ��	0: 16bit��1: 10bit
		nDrive_ic_reg.sDrive_ic_reg_MBI5043.nPWMMode = 0;
	}

	//������������
	//0��A�����ȣ�1��B������
	nNetPortPriority = 0;

	//������������
	bLockNetPort = false;

	//��ȥ��ɫ��λ��,Ĭ��Ϊ4
	nDeductBit = 4;
}

CStructSingleScanCard::~CStructSingleScanCard(void)
{
}

//�Ҷȼ���ת������ɫ���
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

//��ȡ1����̬�µ�������
int CStructSingleScanCard::GetPixPerSetion(VersionType versionType)
{
	//1����̬�µ��������������ߴ����й�
	//1����̬�µ�������=(������*����)/ɨ�跽ʽ=(������*����)/ɨ�跽ʽ = (����*����)/(����*ɨ�跽ʽ)
	int nPixsPerSection = 1;
	if (nDataInputDir == 0 || nDataInputDir == 1)
	{
		if(bExtendedEnable || bExtendedEnableEx)
		{
			//����ʹ�ܣ�1����̬�µ�������=(������*������)/ɨ�跽ʽ
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
		//�������ֿ�
		break;
	case 1:
		//��������һ��ɫ1�㴮��
	case 2:
		//��������һ��ɫ8�㴮��
	case 3:
		//��������һ��ɫ16�㴮��
		nPixsPerSection = 3 * nPixsPerSection;//(����)
		break;
	case 4:
		//��������һ��ɫ����
		nPixsPerSection = 4 * nPixsPerSection;//(�ı�)
		break;
	default:
		break;
	}
	//ʹ�����ź�D��Ϊ�ڶ�·ʱ��ʹ���ظ߶ȼӱ�
	nPixsPerSection  *= (int)pow(2.0, nDCBlineClkEn);

	return nPixsPerSection;
}

//��ȡÿ��������(125MHz)��ʱ����
int CStructSingleScanCard::GetZoneClkNum(VersionType versionTypev)
{
	//��ȡ1����̬�µ�������
	int nPixsPerSection = GetPixPerSetion(versionTypev);

	//zone_clk_num	16	0x1207	15--0	ÿ�������ݣ�125Mhz����ʱ������
	//��׼�棺��1����̬��������������*��Ƶϵ��+7�� 
	//PWM�棺1����̬��������*������λʱ�ӷ�Ƶϵ��������1��
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

			//ÿ��������(125MHz)��ʱ���� = 257 * һɨ������оƬ�� * ������ʱ�ӷ�Ƶϵ�� 
			//257��ʾһ��оƬ����16���ƣ�ÿ���Ƶ�ͨ������Ϊ16bit��16X16 + 1 = 257,1��ʾ256����ǰһλ��ʾ����Ƶ���ǻҶȣ�
			nZoneClkNum = 257 * nFreqDivisionCoeff * nChipSize;
		}
		else
		{
			nZoneClkNum = nPixsPerSection * nFreqDivisionCoeff;
			//��յ�����й�
			if (bEmptyInsertEnable)
			{
				nZoneClkNum = (int)((nZoneClkNum / (nRealDotNum * 1.0)) * (nRealDotNum + nEmptyDotNum));
			}

			if (_MBI5043 == nChipType && 1 == nDrive_ic_reg.sDrive_ic_reg_MBI5043.nPWMMode)//0: 16bit��1: 10bit
			{
				nZoneClkNum = nZoneClkNum * 10 / 16;//5043оƬ���PWMģʽΪ10bit����Ҫ���л���
			}
		}
	}

	return nZoneClkNum;
}

//��ȡˢ�±���ֵ��Χ,ˢ�±���Ϊ��Сֵ�����ֵ֮���2��n�η�
void CStructSingleScanCard::GetRefDoubleVableRange(int & nMultMin, int & nMultMax, VersionType versionType)
{
	nMultMin = 1;
	nMultMax = 1;

	//zone_clk_num	ÿ�������ݣ�125Mhz����ʱ����
	int nZoneClkNum = GetZoneClkNum(versionType);

	if (_GENERAL == nChipType)
	{
		//ͨ�ð�
		return;
	}
	else
	{
		//PWM��̬ɨ�裬ˢ���ʱ���
		if (nScanMode == 1 && bMultiRefreshUnderStaticScan)
		{
			return;
		}

		//PWM
		if(_TLC5948 == nChipType)//TLC5948
		{
			//TLC5948��16��ͨ��һ���ͣ���ͨ��оƬ����
			//Afield_clk_num_a1=��ɫ�ȼ�*gclk��Ƶϵ��/ˢ���ʱ����ı���>Zone_clk_num + 100��

			//ref_double_vable < (gclk��Ƶϵ�� * ��ɫ�ȼ�) / (Zone_clk_num + 100)
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
		else if (_MBI5153_E == nChipType)//14bitģʽ��ˢ�±�����32/64/128����13bitģʽʱ��ɨ�迨��ʱ��֧��
		{
			nMultMin = 32;
			nMultMax = 128;
		}
		else if (_MBI5155 == nChipType)
		{
			unsigned int nReg1R = (nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RHigh<<8) 
				+ nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RLow;//�Ĵ���1��
			if (0 == ((nReg1R >> 7) & 0x01))//_MBI_5155 cfg1[7]=0ʱ��Ϊ16bitģʽ��ˢ�±�����64/128/256��
			{
				nMultMin = 64;
				nMultMax = 256;
			}
			else //_MBI_5155 cfg1[7]=1ʱ��Ϊ14bitģʽ��ˢ�±�����32/64/128��
			{
				nMultMin = 32;
				nMultMax = 128;
			}
		}
		else
		{
			//Afield_clk_num_a1=����ɫ�ȼ�/ˢ���ʱ����ı�����*gclk��Ƶϵ��>16*Zone_clk_num 100��

			//ref_double_vable < (gclk��Ƶϵ��ƽ�� * ��ɫ�ȼ�) / (dclk��Ƶϵ�� * (16 * Zone_clk_num + 100))
// 			double dTemp1 = (16.0 * nZoneClkNum + 100);
// 			double dTemp2 =  nPWMFreqDivisionCoeff * pow(2.0, nScanColorDepth);
// 			nMultMax = (int)(dTemp2 / dTemp1);
// 			if (nMultMax == 0)
// 				nMultMax = 1;

			/*double dTemp1 = (16.0 * nZoneClkNum + 100) * nFreqDivisionCoeff;
			double dTemp2 = nPWMFreqDivisionCoeff * nPWMFreqDivisionCoeff * pow(2.0, nScanColorDepth);*/
			//sunj ����
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
//��ȡˢ��Ƶ�ʷ�Χ
void CStructSingleScanCard::GetRefreshRateRange(int & nRateMin, int & nRateMax, int nRefreshDouble, VersionType versionType)
{
	nRateMin = SCANCARD_REFRESH_RATE_MIN;
	nRateMax = SCANCARD_REFRESH_RATE_MIN;


	//zone_clk_num	ÿ�������ݣ�125Mhz����ʱ����
	int nZoneClkNum = GetZoneClkNum(versionType);

	if (_GENERAL == nChipType)
	{
		if (nStartField == 0)
		{
			nRateMax = nRateMin;
			return;
		}
		//ͨ�ð�
		nZoneClkNum = (int (nZoneClkNum / nStartField + 1)) * nStartField;

		//ˢ�������ֵ = 150MHz / (Field_num * (zone_clk_num + 100) * scan_mode)
		/*double dTemp1 = nFieldNum * (nZoneClkNum + 120) * nScanMode;
		nRateMax = (int)(SCANCARD_MHZ * pow(10.0, 6) / dTemp1);*/
		nRateMax = (int)( SCANCARD_MHZ * pow(10.0,6) * nSecondHighLevel / (( nZoneClkNum + max(100,nOeClkNumber) )* nScanMode * nFieldNum ));
	}
	else
	{
		//PWM
		if(nScanMode == 1 && bMultiRefreshUnderStaticScan)
		{
			//��̬ɨ��,ˢ�±���
			//ˢ���ʹ̶�Ϊ���ref_freq = PWMʱ��Ƶ�� / ��ɫ�ȼ�
			//PWM��Ƶϵ�� = 150MHz / PWMʱ��Ƶ�ʣ�PWM��Ƶϵ������������������ ��ref_freq = 150MHz / (��ɫ�ȼ� * PWM��Ƶϵ��)
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
					+ nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RLow;//�Ĵ���1��
				if (0 == ((nReg1R >> 7) & 0x01))//_MBI_5155 cfg1[7]=0ʱ��Ϊ16bitģʽ��ˢ�±�����64/128/256��
				{
					nAfieldClkNum =  nPWMFreqDivisionCoeff * pow(2.0,16) / nRefreshDouble;
				}
				else //_MBI_5155 cfg1[7]=1ʱ��Ϊ14bitģʽ��ˢ�±�����32/64/128��
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
				//ˢ������Сֵ:row_oe_num���Ϊ65536��ref_fresh >= (1 - Cfg_time*֡Ƶ) * 125MHz / ((row_oe_clk_num + afield_clk_num) * scan_mode) 
				//Cfg_time=�� ��ʱ����ʱ�� = Dclk��Ƶϵ�� * 1/125M * һ������̬���ص���
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
						+ nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RLow;//�Ĵ���1��
					unsigned int nReg3R = (nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RHigh<<8) 
						+ nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RLow;//�Ĵ���3��
					bool bOptimize = ((nReg3R >> 6) & 0x01) == 0 ? false : true;//��������Ż�����ʹ�� cfg3 bit[6]
					int nLastGCLKClock = bOptimize 
						? (nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_H + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_L)
						: (nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT);
					if (0 == ((nReg1R >> 7) & 0x01))//_MBI_5155 cfg[7]=0ʱ��ˢ�±�����64/128/256��Ϊ16bitģʽ��1024*64��
					{
						fTmp = fTmp * (1 - fCfg_time*MBI5153_FRAME) /(65536 + nAfieldClkNum + nLastGCLKClock);
					} 
					else //_MBI_5155 cfg[7]=1ʱ��ˢ�±�����32/64/128��Ϊ14bitģʽ��512*32��
					{
						fTmp = fTmp * (1 - fCfg_time*MBI5153_FRAME) /(16384 + nAfieldClkNum + nLastGCLKClock);
					}

					nRateMin = (int) fTmp;

					//ˢ�������ֵ����oe_clk_num = 100ʱ��ref_freq <= (1 - Cfg_time*֡Ƶ) * 125MHz / ((afield_clk_num + 100) * scan_mode)
					fTmp = SCANCARD_MHZ_MBI5153_E * pow(10.0, 6) / nScanMode;
					fTmp = fTmp * (1 - fCfg_time*MBI5153_FRAME) /(100 + nAfieldClkNum + nLastGCLKClock);//35�ǵ�256+1������128+1��Clock����¬ʥ���ṩ
					nRateMax = (int) fTmp; 
				} 
				else
				{
					fTmp = fTmp * (1 - fCfg_time*MBI5153_FRAME) /(16384 + nAfieldClkNum + 35);
					nRateMin = (int) fTmp;

					//ˢ�������ֵ����oe_clk_num = 100ʱ��ref_freq <= (1 - Cfg_time*֡Ƶ) * 125MHz / ((afield_clk_num + 100) * scan_mode)
					fTmp = SCANCARD_MHZ_MBI5153_E * pow(10.0, 6) / nScanMode;
					fTmp = fTmp * (1 - fCfg_time*MBI5153_FRAME) /(100 + nAfieldClkNum + 35);//35�ǵ�256+1������128+1��Clock����¬ʥ���ṩ
					nRateMax = (int) fTmp; 
				}
			} 
			else
			{
				//ˢ������Сֵ:row_oe_num���Ϊ65536��ref_fresh >= 150MHz / ((row_oe_clk_num + afield_clk_num) * scan_mode) 
				double fTmp = SCANCARD_MHZ * pow(10.0, 6) / nScanMode;
				fTmp = fTmp /(65536 + nAfieldClkNum);
				nRateMin = (int) fTmp; 

				//ˢ�������ֵ����oe_clk_num = 100ʱ��ref_freq <= 150MHz / ((afield_clk_num + 100) * scan_mode)
				fTmp = SCANCARD_MHZ * pow(10.0, 6) / nScanMode;
				fTmp = fTmp /(100 + nAfieldClkNum);
				nRateMax = (int) fTmp; 
			}
		}
	}
}

//��ȡmin_oe_clk_num
long CStructSingleScanCard::GetMinOeClkNum(VersionType versionType)
{
	long nMinOeClkNum = 1;
	if (nRefreshRate == 0 || nFieldNum == 0 ||nScanMode == 0
		||nRefreshDoubleValue == 0 || nFreqDivisionCoeff == 0
		||nStartField == 0)
	{
		return nMinOeClkNum;
	}

	//zone_clk_num	16	0x1207	15--0	ÿ�������ݣ�125Mhz����ʱ������
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
		//PWM�棺��ɫ��� * Pwm_clk_freq_div / ref_doule_value
		double nAfieldClkNum = GetAfieldClkNum(versionType);

		//min_oe_clk_num_a1	16	0x0026	11--0	
		//PWM�棺��Ĵ�����������ʱ��(��оƬ���ʱ��)��
		//Config_ic_times*(Afield_clk_num+Row_oe_clk_num)*6.6ns
		int nRowOeClkNum = (int) (SCANCARD_MHZ * pow(10.0, 6) / (nRefreshRate * nScanMode) - nAfieldClkNum);
		if (_MBI5153_E == nChipType || _MBI5155 == nChipType)
		{
			//Cfg_time=�� ��ʱ����ʱ�� = Dclk��Ƶϵ�� * 1/125M * һ������̬���ص���
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
					+ nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RLow;//�Ĵ���3��
				bool bOptimize = ((nReg3R >> 6) & 0x01) == 0 ? false : true;//��������Ż�����ʹ�� cfg3 bit[6]
				int nLastGCLKClock = bOptimize 
					? (nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_H + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_L)
					: (nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT);

				nRowOeClkNum = (int) (SCANCARD_MHZ_MBI5153_E * pow(10.0, 6) * (1 - fCfg_time * MBI5153_FRAME) / (nRefreshRate * nScanMode) - nAfieldClkNum - nLastGCLKClock);//nLastGCLKClock�ǵ�256+1������128+1��Clock����¬ʥ���ṩ
			} 
			else//_MBI5153_E == nChipType
			{
				nRowOeClkNum = (int) (SCANCARD_MHZ_MBI5153_E * pow(10.0, 6) * (1 - fCfg_time * MBI5153_FRAME) / (nRefreshRate * nScanMode) - nAfieldClkNum - 35);//35�ǵ�256+1������128+1��Clock����¬ʥ���ṩ
			}
		}
		nRowOeClkNum = nRowOeClkNum > 0x0063 ? nRowOeClkNum : 0x0063;
		nMinOeClkNum = (int) (nConfigICTime * (nAfieldClkNum + nRowOeClkNum) * 6.6 * pow(10.0,-6) + 0.5);
	}

	return nMinOeClkNum;


}

//ͨ��������ʱ�䷴��ˢ����
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
				//Cfg_time=�� ��ʱ����ʱ�� = Dclk��Ƶϵ�� * 1/125M * һ������̬���ص���
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
						+ nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RLow;//�Ĵ���3��
					bool bOptimize = ((nReg3R >> 6) & 0x01) == 0 ? false : true;//��������Ż�����ʹ�� cfg3 bit[6]
					int nLastGCLKClock = bOptimize 
						? (nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_H + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_L)
						: (nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT);

					nRefreshRate = (int) (SCANCARD_MHZ_MBI5153_E * pow(10.0, 6) * (1 - fCfg_time*MBI5153_FRAME) / ((nAfieldClkNum + nRowOeClkNum + nLastGCLKClock) * nScanMode));//nLastGCLKClock�ǵ�256+1������128+1��Clock����¬ʥ���ṩ
				} 
				else//_MBI5153_E == nChipType
				{
					nRefreshRate = (int) (SCANCARD_MHZ_MBI5153_E * pow(10.0, 6) * (1 - fCfg_time*MBI5153_FRAME) / ((nAfieldClkNum + nRowOeClkNum + 35) * nScanMode));//35�ǵ�256+1������128+1��Clock����¬ʥ���ṩ
				}
			}
		}
	}

	return 1;
}

//��ȡafield_clk_num_a1
long CStructSingleScanCard::GetAfieldClkNum(VersionType versionType)
{

	long nAfieldClkNum = 0;

	//afield_clk_num_a1	20	0x1300	15--0,15-12	
	//��׼�棺(150Mhz/(Ref_freq*Field_num*Scan_mode))-100
	//������ڣ�Zone_clk_num + 100
	//PWM�棺(��ɫ��� *Pwm_clk_freq_div/ ref_doule_value)
	//����ڣ�16*Zone_clk_num + 100


	//zone_clk_num	16	0x1207	15--0	ÿ�������ݣ�125Mhz����ʱ������
	int nZoneClkNum = GetZoneClkNum(versionType);

	if (_GENERAL == nChipType)
	{
		//2013-3-5 ͨ��оƬ����
		nAfieldClkNum = (long)(SCANCARD_MHZ * pow(10.0,6) * nSecondHighLevel / ( ( nRefreshRate * nFieldNum ) * nScanMode) - nOeClkNumber);

		nAfieldClkNum = nAfieldClkNum > nZoneClkNum + 100 ? nAfieldClkNum : nZoneClkNum + 100;

		//afield_clk_num_a1��������ʼ���ı���
		nAfieldClkNum = nAfieldClkNum % nStartField == 0 ? nAfieldClkNum : (nAfieldClkNum / nStartField + 1) * nStartField;
	}
	else
	{	
		if(nScanMode == 1 && bMultiRefreshUnderStaticScan)
		{
			//PWM��̬ɨ�裬ˢ�±���
			//afield_clk_num_a1 = PWMʱ��Ƶ�� / ��ʾ����Ļˢ���� - row_oe_clk_num
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
					+ nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RLow;//�Ĵ���1��
				if (0 == ((nReg1R >> 7) & 0x01))//_MBI_5155 cfg1[7]=0ʱ��Ϊ16bitģʽ��ˢ�±�����64/128/256��
				{
					nAfieldClkNum = (long) (nPWMFreqDivisionCoeff * pow(2.0,16) / nRefreshDoubleValue);//65536;¬ʥ��˵�̶�
				}
				else //_MBI_5155 cfg1[7]=1ʱ��Ϊ14bitģʽ��ˢ�±�����32/64/128��
				{
					nAfieldClkNum = (long) (nPWMFreqDivisionCoeff * pow(2.0,14) / nRefreshDoubleValue);//16384;¬ʥ��˵�̶�
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

//��ȡ������ʱ��
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
				//Cfg_time=�� ��ʱ����ʱ�� = Dclk��Ƶϵ�� * 1/125M * һ������̬���ص���
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
						+ nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RLow;//�Ĵ���3��
					bool bOptimize = ((nReg3R >> 6) & 0x01) == 0 ? false : true;//��������Ż�����ʹ�� cfg3 bit[6]
					int nLastGCLKClock = bOptimize 
						? (nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_H + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_L)
						: (nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT + nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT);

					nOeClkNumber = (int) (SCANCARD_MHZ_MBI5153_E * pow(10.0, 6) * (1 - fCfg_time*MBI5153_FRAME) / (nRefreshRate * nScanMode) - lAfieldClkNum - nLastGCLKClock);//nLastGCLKClock�ǵ�256+1������128+1��Clock����¬ʥ���ṩ
				} 
				else//_MBI5153_E == nChipType
				{
					nOeClkNumber = (int) (SCANCARD_MHZ_MBI5153_E * pow(10.0, 6) * (1 - fCfg_time*MBI5153_FRAME) / (nRefreshRate * nScanMode) - lAfieldClkNum - 35);//35�ǵ�256+1������128+1��Clock����¬ʥ���ṩ
				}
			}

			nOeClkNumber = nOeClkNumber > 0x0063 ? nOeClkNumber : 0x0063;
		}
	}
	return nOeClkNumber;
}

//��ȡ1��ģ����Ƶ���ݶ�Ӧ�ܵ�У��������
long CStructSingleScanCard::GetModCoefByte(int nEmptyByte)
{
	//mod_coef_bytes_a1	16	0x2000	15--0	1��ģ����Ƶ���ݶ�Ӧ�ܵ�У��������
	//ʵ�����������ص�����������ҽ��ߣ�[(mod_width * 4 + x)/ 32] * 64 * mod_heigh�� ���½��ߣ�ģ�����*8*ģ��߶�
	//ʵ���ص����ɫ��[(mod_width * 9 + x)/ 32] * 64 * mod_heigh
	long nCoefByteNum;
	int nTemp;
	int nModWidth16 = nModuleWidth % 16 ? (nModuleWidth / 16 + 1) * 16 : nModuleWidth;
	nModWidth16 = nModWidth16 > 42 ? nModuleWidth : nModWidth16;

	//У�������ײ��ӿ��ֽ�
	if (nDotCorrectTye == 0 || nDotCorrectTye == 1)//����
	{
		if (nDataInputDir == 0 || nDataInputDir == 1)//���ҽ���
		{
			nTemp = (nModWidth16 * 4) % 32 ? (nModWidth16 * 4 / 32 + 1) : nModWidth16 * 4 / 32;
			nCoefByteNum = nTemp * 64 * nModuleHeight;
		}
		else if (nDataInputDir == 2 || nDataInputDir == 3)//���½���
		{
			//nCoefByteNum = nModuleWidth * 8 * nModuleHeight;

			int nModW = (int) (nModuleWidth * pow(2.0, nDCBlineClkEn));

			short nModW16 = nModW % 16 ? (nModW / 16 + 1) * 16 : nModW;

			nTemp = (nModW16 * 4) % 32 ? (nModW16 * 4 / 32 + 1) : nModW16 * 4 / 32;
			nCoefByteNum = nTemp * 64 * nModuleHeight;
		}

	}
	else if (nDotCorrectTye == 2)//��ɫ
	{
		nTemp = (nModWidth16 * 9) % 32 ? (nModWidth16 * 9 / 32 + 1) : nModWidth16 * 9 / 32;
		nCoefByteNum= nTemp * 64 * nModuleHeight;
	}
	nCoefByteNum += nEmptyByte;
	return nCoefByteNum;
}

//��ȡ������Ч�ʣ��볡��Ϣ����Ƶϵ���й�
void CStructSingleScanCard::GetBrightnessEffective(VersionType versionType)
{
	//������Ч�� = afield_clk_num_a1 / (afield_clk_num_a1 + row_oe_clk_num)
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
//���ݻҶȵȼ�ȷ������Ϣ
bool CStructSingleScanCard::SetFieldNumByGrayLevel()
{
	/* B50ȥ��
	if (_GENERAL == nChipType )//�����ͨ��оƬ
	{
		float min = (float)((( pow(2.0,16) -1) )/pow(2.0,9));
		float max = 136;
		switch (nScanColorDepth)
		{
		case 8:
			nHalfFieldNumber =(short) (1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16);//�볡��
			nSecondHighLevel = 4;
			min = (float)(min - 64 - 32 -16 - 1.0 / 32-1.0 /64 - 1.0 / 128 - 1.0 / 256 - 1.0 /512);
			max = max - 64-16 - 32 - 5;
			fLightRatio = min / max;
			nFieldNum = (short) max;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 16;//��ʼ������������Сֵ�ķ�ĸ��
			nEndField = 8;//��ֹ�������������ֵ��
			break;
		case 9:
			nHalfFieldNumber =(short) (1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16 + 1.0 /32);//�볡��
			nSecondHighLevel= 8;
			min = (float)(min - 64 - 32 -16 - 1.0 /64 - 1.0 / 128 - 1.0 / 256 - 1.0 /512);
			max = max - 64-16 - 32 - 4;
			fLightRatio = min / max;
			nFieldNum =(short) max;
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 32;//��ʼ������������Сֵ�ķ�ĸ��
			nEndField = 8;//��ֹ�������������ֵ��
			break;
		case 10:
			nHalfFieldNumber =(short) (1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16 + 1.0 /32);//�볡��
			nSecondHighLevel = 8;
			min =(float) (min - 64 - 32 - 1.0 /64 - 1.0 / 128 - 1.0 / 256 - 1.0 /512);
			max = max - 64 - 32 - 4;
			fLightRatio = min / max;
			nFieldNum =(short) max;
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 32;//��ʼ������������Сֵ�ķ�ĸ��
			nEndField = 16;//��ֹ�������������ֵ��
			break;
		case 11:
			nHalfFieldNumber =(short) (1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16 + 1.0 /32 + 1.0 / 64);//�볡��
			nSecondHighLevel = 16;
			min =(float) (min - 64 - 32 - 1.0 / 128 - 1.0 / 256 - 1.0 /512);
			max = max - 64 - 32 - 3;
			fLightRatio = min / max;
			nFieldNum = (short)max;
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 64;//��ʼ������������Сֵ�ķ�ĸ��
			nEndField = 16;//��ֹ�������������ֵ��
			break;
		case 12:
			nHalfFieldNumber =(short) ( 1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16 + 1.0 /32 + 1.0 / 64);//�볡��
			nSecondHighLevel = 16;
			min = (float)(min - 64 - 1.0 / 128 - 1.0 / 256 - 1.0 /512);
			max = max - 64 - 3;
			fLightRatio = min / max;
			nFieldNum =(short) max;
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 64;//��ʼ������������Сֵ�ķ�ĸ��
			nEndField = 32;//��ֹ�������������ֵ��
			break;
		case 13:
			nHalfFieldNumber = (short) (1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16 + 1.0 /32 + 1.0 / 64 + 1.0 / 128);//�볡��
			nSecondHighLevel = 32;
			min = (float)(min - 64 - 1.0 / 256 - 1.0 /512);
			max = max - 2 - 64;
			fLightRatio = min / max;
			nFieldNum =(short) max;
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 128;//��ʼ������������Сֵ�ķ�ĸ��
			nEndField = 32;//��ֹ�������������ֵ��
			break;
		case 14:
			nHalfFieldNumber = (short) (1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16 + 1.0 /32 + 1.0 / 64 + 1.0 / 128) ;//�볡��
			nSecondHighLevel = 32;
			min =(float)( min - 1.0 / 256 - 1.0 /512);
			max = max -2;
			fLightRatio = min / max;
			nFieldNum =(short) max;
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 128;//��ʼ������������Сֵ�ķ�ĸ��
			nEndField = 64;//��ֹ�������������ֵ��
			break;
		case 15:
			nHalfFieldNumber = (short) (1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16 + 1.0 /32 + 1.0 / 64 + 1.0 / 128 + 1.0 / 256);
			nSecondHighLevel= 32;
			min = (float)(min - 1.0/ 512) ;
			max = max - 1;
			fLightRatio = min / max;
			nFieldNum =(short) max;
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 256;//��ʼ������������Сֵ�ķ�ĸ��
			nEndField = 64;//��ֹ�������������ֵ��
			break;
		case 16:
			nHalfFieldNumber = (short) (1.0 / 2 + 1.0 / 4 + 1.0 / 8 + 1.0 / 16 + 1.0 /32 + 1.0 / 64 + 1.0 / 128 + 1.0 / 256 + 1.0 / 512);
			nSecondHighLevel= 32;
			fLightRatio = min / max;
			nFieldNum = 136;
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 512;//��ʼ������������Сֵ�ķ�ĸ��
			nEndField = 64;//��ֹ�������������ֵ��
			break;
		default:
			break;

		}
	}
	else */
	{
		//ȫ���� = �ܳ��� - �볡��
		switch (nGrayLedvel)
		{
		case 0:
			nHalfFieldNumber = 0;//�볡��
			nFieldNum = 1;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 1;//��ʼ������������Сֵ�ķ�ĸ��
			nEndField = 1;//��ֹ�������������ֵ��
			break;
		case 1:
			nHalfFieldNumber = 0;//�볡��
			nFieldNum = 3;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 1;
			nEndField = 2;//��ֹ��
			break;
		case 2:
			nHalfFieldNumber = 0;//�볡��
			nFieldNum = 7;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 1;
			nEndField = 4;//��ֹ��
			break;
		case 3:
			nHalfFieldNumber = 0;//�볡��
			nFieldNum = 15;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 1;
			nEndField = 8;//��ֹ��
			break;
		case 4:
			nHalfFieldNumber = 0;//�볡��
			nFieldNum = 31;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 1;
			nEndField = 16;//��ֹ��
			break;
		case 5:
			nHalfFieldNumber = 0;//�볡��
			nFieldNum = 63;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 1;
			nEndField = 32;//��ֹ��
			break;
		case 6:
			nHalfFieldNumber = 1;//�볡��
			nFieldNum = 2;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 2;
			nEndField = 1;//��ֹ��
			break;
		case 7:
			nHalfFieldNumber = 0;//�볡��
			nFieldNum = 3;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 1;
			nEndField = 2;//��ֹ��
			break;
		case 8:
			nHalfFieldNumber = 1;//�볡��
			nFieldNum = 4;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 2;
			nEndField = 2;//��ֹ��
			break;
		case 9:
			nHalfFieldNumber = 0;//�볡��
			nFieldNum = 7;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 1;
			nEndField = 4;//��ֹ��
			break;
		case 10:
			nHalfFieldNumber = 2;//�볡��
			nFieldNum = 5;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 4;
			nEndField = 2;//��ֹ��
			break;
		case 11:
			nHalfFieldNumber = 1;//�볡��
			nFieldNum = 8;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 2;
			nEndField = 4;//��ֹ��
			break;
		case 12:
			nHalfFieldNumber = 3;//�볡��
			nFieldNum = 6;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 8;
			nEndField = 2;//��ֹ��
			break;
		case 13:
			nHalfFieldNumber = 2;//�볡��
			nFieldNum = 9;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 4;
			nEndField = 4;//��ֹ��
			break;
		case 14:
			nHalfFieldNumber = 3;//�볡��
			nFieldNum = 10;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 8;
			nEndField = 4;//��ֹ��
			break;
		case 15:
			nHalfFieldNumber = 2;//�볡��
			nFieldNum = 17;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 4;
			nEndField = 8;//��ֹ��
			break;
		case 16:
			nHalfFieldNumber = 4;//�볡��
			nFieldNum = 11;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 16;
			nEndField = 4;//��ֹ��
			break;
		case 17:
			nHalfFieldNumber = 3;//�볡��
			nFieldNum = 18;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 8;
			nEndField = 8;//��ֹ��
			break;
		case 18:
			nHalfFieldNumber = 5;//�볡��
			nFieldNum = 12;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 32;
			nEndField = 4;//��ֹ��
			break;
		case 19:
			nHalfFieldNumber = 4;//�볡��
			nFieldNum = 19;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 16;
			nEndField = 8;//��ֹ��
			break;
		case 20:
			nHalfFieldNumber = 6;//�볡��
			nFieldNum = 13;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 64;
			nEndField = 4;//��ֹ��
			break;
		case 21:
			nHalfFieldNumber = 5;//�볡��
			nFieldNum = 20;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 32;
			nEndField = 8;//��ֹ��
			break;
		case 22:
			nHalfFieldNumber = 7;//�볡��
			nFieldNum = 14;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 128;
			nEndField = 4;//��ֹ��
			break;
		case 23:
			nHalfFieldNumber = 6;//�볡��
			nFieldNum = 21;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 64;
			nEndField = 8;//��ֹ��
			break;
		case 24:
			nHalfFieldNumber = 8;//�볡��
			nFieldNum = 15;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 256;
			nEndField = 4;//��ֹ��
			break;
		case 25:
			nHalfFieldNumber = 7;//�볡��
			nFieldNum = 22;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 128;
			nEndField = 8;//��ֹ��
			break;
		case 26:
			nHalfFieldNumber = 6;//�볡��
			nFieldNum = 37;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 64;
			nEndField = 16;//��ֹ��
			break;
		case 27:
			nHalfFieldNumber = 8;//�볡��
			nFieldNum = 23;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 256;
			nEndField = 8;//��ֹ��
			break;
		case 28:
			nHalfFieldNumber = 7;//�볡��
			nFieldNum = 38;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 128;
			nEndField = 16;//��ֹ��
			break;
		case 29:
			nHalfFieldNumber = 6;//�볡��
			nFieldNum = 69;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 64;
			nEndField = 32;//��ֹ��
			break;
		case 30:
			nHalfFieldNumber = 8;//�볡��
			nFieldNum = 39;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 256;
			nEndField = 16;//��ֹ��
			break;
		case 31:
			nHalfFieldNumber = 7;//�볡��
			nFieldNum = 70;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 128;
			nEndField = 32;//��ֹ��
			break;
		case 32:
			nHalfFieldNumber = 6;//�볡��
			nFieldNum = 133;//�ܳ���
			nFullFieldNumber = nFullFieldNumber - nHalfFieldNumber;//ȫ����
			nStartField = 64;
			nEndField = 64;//��ֹ��
			break;
		case 33:
			nHalfFieldNumber = 8;//�볡��
			nFieldNum = 71;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 256;
			nEndField = 32;//��ֹ��
			break;
		case 34:
			nHalfFieldNumber = 7;//�볡��
			nFieldNum = 134;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 128;
			nEndField = 64;//��ֹ��
			break;
		case 35:
			nHalfFieldNumber = 8;//�볡��
			nFieldNum = 135;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 256;
			nEndField = 64;//��ֹ��
			break;
		case 36:
			nHalfFieldNumber = 9;//�볡��
			nFieldNum = 136;//�ܳ���
			nFullFieldNumber = nFieldNum - nHalfFieldNumber;//ȫ����
			nStartField = 512;
			nEndField = 64;//��ֹ��
			break;
		default:
			break;
		}
	}

	return true;
}
//���������в��ұ���0--black��1-- redA��2--green��3--blue, 4--redB
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

//�������ļ��ж�ȡ
/*
bool CStructSingleScanCard::ReadConfig(const char * chOpenFile)
{
	CIniFile iniFile;
	iniFile.SetPath(chOpenFile);  //����ini�ļ�·��

	char chSection[260] = _T("screen");
	char chKey[260];

	bool bExit = iniFile.SectionExist(chSection);
	if(!bExit)
	{
		return false;
	}
	
	//��ȥ��ɫ��λ��
	strcpy(chKey,_T("DeductBit"));
	iniFile.GetKeyValue(chSection,chKey,"4",chKey);
	nDeductBit = atoi(chKey);


	//ONE_SCAN_CARD_WIDTH	2	128	����ɨ����ƿ�����Ӧ��LED��ʾģ���к������ص��������
	//��ɨ�迨���ȡ���λ��ǰ����λ�ں�ȡֵ��Χ1~256��������16��������
	strcpy(chKey,_T("ONE_SCAN_CARD_WIDTH"));
	iniFile.GetKeyValue(chSection,chKey,"128",chKey);
	nScanCardWidth = atoi(chKey);

	nScanCardWidthReal = nScanCardWidth;

	//ONE_SCAN_CARD_HEIGHT	2	96	����ɨ����ƿ�����Ӧ��LED��ʾģ�����������ص��������
	//��ɨ�迨�߶ȡ���λ��ǰ����λ�ں�ȡֵ��Χ1~256��������16��������
	strcpy(chKey,_T("ONE_SCAN_CARD_HEIGHT"));
	iniFile.GetKeyValue(chSection,chKey,"96",chKey);
	nScanCardHeight = atoi(chKey);

	nScanCardHeightReal = nScanCardHeight;

	//mod_width 1   32  ģ����ȣ�ȡֵ��Χ1-64
	strcpy(chKey,_T("mod_width"));
	iniFile.GetKeyValue(chSection,chKey,"32",chKey);
	nModuleWidth = atoi(chKey);

	//mod_height 1   32  ģ��߶ȣ�ȡֵ��Χ1-64
	strcpy(chKey,_T("mod_height"));
	iniFile.GetKeyValue(chSection,chKey,"32",chKey);
	nModuleHeight = atoi(chKey);

	//1   3   ģ������  ģ������ = ģ��߶�/ÿ������
	strcpy(chKey,_T("mod_section_number"));
	iniFile.GetKeyValue(chSection,chKey,"3",chKey);
	nModuleSectionNumber = atoi(chKey);

	//1    4  ģ����������ģ�������� = ɨ�迨����/ģ�����
	strcpy(chKey,_T("mod_hor_number"));
	iniFile.GetKeyValue(chSection,chKey,"4",chKey);
	nModuleHorNum = atoi(chKey);

	//1    3  ģ�����������ģ��������� = ɨ�迨�߶ȡ�ģ��߶�
	strcpy(chKey,_T("mod_ver_number"));
	iniFile.GetKeyValue(chSection,chKey,"3",chKey);
	nModuleVerNum = atoi(chKey);

	//SCAN_CARD_SECTION_NUM	1	6	ɨ�迨���������������Ϊ16
	strcpy(chKey,_T("SCAN_CARD_SECTION_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"16",chKey);
	nScanCardSectionNumber = atoi(chKey);

	//SCAN_CARD_SECTION_ROW_NUM	1	16	ɨ�迨ÿ���������������Ϊ16��
	strcpy(chKey,_T("SCAN_CARD_SECTION_ROW_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"16",chKey);
	nScanCardSectionRowNumber = atoi(chKey);

	//Sϵ�в������ӣ��������
	//SCAN_CARD_SECTION_NUM_CABINET	1	6	ɨ�迨���������������Ϊ16
	strcpy(chKey,_T("SCAN_CARD_SECTION_NUM_CABINET"));
	iniFile.GetKeyValue(chSection,chKey,"16",chKey);
	nScanCardSectionNumber_cabinet = atoi(chKey);

	//SCAN_CARD_SECTION_ROW_NUM_CABINET	1	16	ɨ�迨ÿ���������������Ϊ16��
	strcpy(chKey,_T("SCAN_CARD_SECTION_ROW_NUM_CABINET"));
	iniFile.GetKeyValue(chSection,chKey,"16",chKey);
	nScanCardSectionRowNumber_cabinet = atoi(chKey);


	//SCAN_COLOR_DEPTH	1	14	ɨ�迨ɨ�����ɫ��ȣ�ȡ12~16��������
	strcpy(chKey,_T("SCAN_COLOR_DEPTH"));
	iniFile.GetKeyValue(chSection,chKey,"8",chKey);
	nScanColorDepth = atoi(chKey);

	//GRAY_LEVEL ɨ�迨�Ҷȼ���
	strcpy(chKey,_T("GRAY_LEVEL"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nGrayLedvel = atoi(chKey);

	//origin_color_bit		ԭʼ��ɫ��ȣ�����8BIT,10bit��12bit��16bit
	strcpy(chKey,_T("origin_color_bit"));
	iniFile.GetKeyValue(chSection,chKey,"8",chKey);
	nOrginColorBit = atoi(chKey);

	//SCAN_MODE	1	4	ɨ���ģʽ��ȡ 1-2-4-8-16
	strcpy(chKey,_T("SCAN_MODE"));
	iniFile.GetKeyValue(chSection,chKey,"16",chKey);
	nScanMode = atoi(chKey);

	//SCAN_MODE_CABINET	1	4	���������ɨ���ģʽ�������ݣ�Sϵ������ʹ��
	strcpy(chKey,_T("SCAN_MODE_CABINET"));
	iniFile.GetKeyValue(chSection,chKey,"16",chKey);
	nScanMode_cabinet = atoi(chKey);


	//DOT_CORRECTION_EN	1	1	����У��ʹ�ܣ�ȡ0��1
	strcpy(chKey,_T("DOT_CORRECTION_EN"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bEmendBrightness = atoi(chKey) == 1 ? true : false;

	//SCAN_GCLK_FREQUENCY	1	12.5	ɨ��ʱ��Ƶ�ʣ����30Mhz
	strcpy(chKey,_T("SCAN_GCLK_FREQUENCY"));
	iniFile.GetKeyValue(chSection,chKey,"12.5",chKey);
	fScanClkFrequency = (float)atof(chKey);

	//ZONE_DCLK_NUM	2		ÿ����λʱ������256*16
	strcpy(chKey,_T("ZONE_DCLK_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"0x1207",chKey);
	nZoneDClkNum = atoi(chKey);

	//1  0-100  ռ�ձ�  ɨ��ʱ�ӵ�ռ�ձ�
	strcpy(chKey,_T("duty_cycle_low_value"));
	iniFile.GetKeyValue(chSection,chKey,"50",chKey);
	nDutyCycle = atoi(chKey);

	//PWM_SCAN_GCLK_FREQUENCY	1	12.5	PWMʱ��ʱ��Ƶ�ʣ����30Mhz
	strcpy(chKey,_T("PWM_SCAN_GCLK_FREQUENCY"));
	iniFile.GetKeyValue(chSection,chKey,"12.5",chKey);
	fPWMScanClkFrequency = (float)atof(chKey);

	//1  0-100  PWMʱ��ռ�ձȿɵ��ȼ� 
	strcpy(chKey,_T("pwm_duty_cycle_low_value"));
	iniFile.GetKeyValue(chSection,chKey,"50",chKey);
	nPWMDutyCycle = atoi(chKey);

	//CLR_CLK_NUM	1	4	1~255 ����ʱ����(������ʱ��)
	strcpy(chKey,_T("CLR_CLK_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"4",chKey);
	nOeClkNumber = atoi(chKey);

	//ˢ��Ƶ�� ������ɫ��Ⱥ�ɨ�跽ʽ��ͬ����ͬ
	strcpy(chKey,_T("refresh_rate"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nRefreshRate = atoi(chKey);

	//REF_FREQ_MIN ˢ������Сֵ
	strcpy(chKey,_T("refresh_rate_min"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nRefreshRateMin = atoi(chKey);

	//REF_FREQ_MAX ˢ�������ֵ
	strcpy(chKey,_T("refresh_rate_max"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nRefreshRateMax = atoi(chKey);

	//CONFIG_IC_TIME	��Ĵ�����������ʱ��(оƬ���ʱ��)�� 1ms--30s
	strcpy(chKey,_T("config_ic_time"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	nConfigICTime = atoi(chKey);

	//dat_freq_num   ��֡Ƶ�ʼ�����
	strcpy(chKey,_T("dat_freq_num"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nDatFreqNum = atoi(chKey);

	//OE_DELAY_VALUE	0x01	�����ӳ�ʱ����������������Ĭ��Ϊ0x01������1������
	strcpy(chKey,_T("OE_DELAY_VALUE"));
	iniFile.GetKeyValue(chSection,chKey,"2",chKey);
	nOeDelayValue = atoi(chKey);

	//SYN_REFRESH_EN		ͬ��ˢ��ʹ�ܡ�Ĭ��Ϊ0x1��ʹ��
	strcpy(chKey,_T("SYN_REFRESH_EN"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	bSyncRefresh = atoi(chKey) == 1 ? true : false;

	//VIRTUAL_DISP_EN			������ʾʹ�ܣ�Ĭ��Ϊ0x0����ʹ��
	strcpy(chKey,_T("VIRTUAL_DISP_EN"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bVirtvalDisp = atoi(chKey) == 1 ? true : false;

	//FREQ_DIVISION_COEF	0x7		150Mhz�ķ�Ƶϵ�������Ϊ200��Ƶ��ֵΪ0.625Mhz��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1������
	strcpy(chKey,_T("FREQ_DIVISION_COEF"));
	iniFile.GetKeyValue(chSection,chKey,"100",chKey);
	nFreqDivisionCoeff = atoi(chKey);

	//PWM_FREQ_DIVISION_COEF	0x7		125Mhz�ķ�Ƶϵ�������Ϊ200��Ƶ��ֵΪ0.625Mhz��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1������
	strcpy(chKey,_T("PWM_FREQ_DIVISION_COEF"));
	iniFile.GetKeyValue(chSection,chKey,"100",chKey);
	nPWMFreqDivisionCoeff = atoi(chKey);

	//DATA_OUTPUT_REVERSE	�����������������ߺ�ɨ����	0x00	����������Ĭ��Ϊ0x0	0x0	��ʹ��	0x1	ʹ��
	strcpy(chKey,_T("DATA_OUTPUT_REVERSE"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bDataOutUpReverse = atoi(chKey) == 1 ? true : false;

	//SCAN_OUTPUT_REVERSE		0x00	ɨ��������Ĭ��Ϊ0x0	0x0	��ʹ��	0x1	ʹ��
	strcpy(chKey,_T("SCAN_OUTPUT_REVERSE"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bScanOutUpReverse = atoi(chKey) == 1 ? true : false;

	//	DCB_LINE_CLK_EN			0x00	ʹ�����ź�DCBΪʱ��ʹ���ظ߶ȼӱ�;	0x0	��ʹ��	0x1	2��	0x2	3��	0x3	4��
	strcpy(chKey,_T("DCB_LINE_CLK_EN"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nDCBlineClkEn = atoi(chKey);

	//NO_SIGNAL_DISP	���ź���ʾ��Ĭ��Ϊ0		0x0��������0x1��������档0x2��ͼƬ
	strcpy(chKey,_T("NO_SIGNAL_DISP"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nNoSingleDisp = atoi(chKey);

	//DATA_INPUT_DIR		0x32	���ݷ��򣺣�����ʾ�����濴��	
	//Ĭ��Ϊ���ҵ���0x1	0x0	��������	0x1	��������	0x2	��������	0x3	��������
	strcpy(chKey,_T("DATA_INPUT_DIR"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	nDataInputDir = atoi(chKey);

	//ROW_DECODE_MODE		�����뷽ʽ��Ĭ��0x2
	//	0x0	��̬������					0x6	164����
	//	0x1	������оƬ��ֱ�������й�	0x7	192����
	//	0x2	138����						0x8	193����
	//	0x3	139����						0x9	595����
	//	0x4	145�����138˫O				0xA	4096����
	//	0x5	154����						0xB	
	strcpy(chKey,_T("ROW_DECODE_MODE"));
	iniFile.GetKeyValue(chSection,chKey,"2",chKey);
	nRowDecodeMOde = atoi(chKey);

	//	DATA_LINE_TYPE	8	0x14	7--0	�������ʹ��ࣺĬ��Ϊ0x00��
	strcpy(chKey,_T("DATA_LINE_TYPE_RANGE"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nDataLineTypeRange = atoi(chKey);

	//	DATA_LINE_TYPE	8	0x14	7--0	�������ͣ�Ĭ��Ϊ0x00��
	//	0x00-0x1F	�������ֿ�, 
	//	0x20-0x18	��������һ��ɫ1�㴮��
	//	0x30-0x38	��������һ��ɫ8�㴮��
	//	0x40-0x48	��������һ��ɫ16�㴮��
	//	0x50-0x6F	��������һ��ɫ����
	strcpy(chKey,_T("DATA_LINE_TYPE"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nDataLineType = atoi(chKey);

	//DATA_LINE_CTRL	8	0x00	�����߿���,
	//����4��������RB,B,G,RA������ע������bitΪ0������Ϊ1����Ĭ�ϣ�����
	strcpy(chKey,_T("DATA_LINE_CTRL"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nDataLineCtrl = atoi(chKey);

	//FIELD_NUM			�ܳ��������Ϊ136��������1������
	strcpy(chKey,_T("FIELD_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"12",chKey);
	nFieldNum = atoi(chKey);

	//HALF_FIELD_NUM	�볡�������Ϊ9��.Ĭ��Ϊ0x6����1����
	strcpy(chKey,_T("HALF_FIELD_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"5",chKey);
	nHalfFieldNumber = atoi(chKey);

	//FULL_FIELD_NUM	ȫ���������Ϊ128������1����
	strcpy(chKey,_T("FULL_FIELD_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"6",chKey);
	nFullFieldNumber = atoi(chKey);

	//��ʼ��
	strcpy(chKey,_T("start_field"));
	iniFile.GetKeyValue(chSection,chKey,"32",chKey);
	nStartField = atoi(chKey);

	//��ֹ��
	strcpy(chKey,_T("end_field"));
	iniFile.GetKeyValue(chSection,chKey,"64",chKey);
	nEndField = atoi(chKey);

	//�θ߳� 2013-3-5 ͨ��оƬ����
	strcpy(chKey,_T("Second_Filed"));
	iniFile.GetKeyValue(chSection,chKey,"64",chKey);
	nStartField = atoi(chKey);
	
	//������Ч�� 2013-3-5 ͨ��оƬ����
	strcpy(chKey,_T("brightness_efficent"));
	iniFile.GetKeyValue(chSection,chKey,"64",chKey);
	fBrightnessEfficent =(float) atof(chKey);

	//DATA_POLARITY	4	���ݼ��ԣ�Ĭ��Ϊ0x0	0x0	�ߵ�ƽ����	0x1	�͵�ƽ����	0x2-0xF	����14�������Ԥ��
	strcpy(chKey,_T("DATA_POLARITY"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nDataPolarity = atoi(chKey);

	//OE_POLARITY	0x1F	OE���ԣ�Ĭ��Ϊ0x0	0x0	����Ч	0x1	����Ч
	strcpy(chKey,_T("OE_POLARITY"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nOePolarity = atoi(chKey);

	///////////�յ����////////////
	//EMPTY_INSERT_EN			�յ����ʹ�ܣ���ÿ���ٵ������ٿյ�.	Ĭ��0x0����ʹ��
	strcpy(chKey,_T("EMPTY_INSERT_EN"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bEmptyInsertEnable = atoi(chKey) == 1 ? true : false;

	//INSERT_MODE				����յ㷽ʽ��ǰ���뻹�Ǻ���롣	1��ǰ����յ㡣0�������յ�
	strcpy(chKey,_T("INSERT_MODE"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nInsertMode = atoi(chKey);

	//EMPTY_DOT_NUM				����Ŀյ�����ÿ�����ֻ�ܲ���64�յ㣬����1������
	strcpy(chKey,_T("EMPTY_DOT_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	nEmptyDotNum = atoi(chKey);

	//REAL_DOT_NUM		15--0	ÿ���ٵ����յ㣬����1������
	strcpy(chKey,_T("REAL_DOT_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	nRealDotNum = atoi(chKey);

	///////////��������////////////
	//EMPTYSECTION_INSERT_EN			��������ʹ�ܣ���ÿ���ٵ������ٿ���.	Ĭ��0x0����ʹ��
	strcpy(chKey,_T("EMPTYSECTION_INSERT_EN"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bEmptySectionInsertEnable = atoi(chKey) == 1 ? true : false;

	//INSERTSECTION_MODE				���������ʽ��ǰ���뻹�Ǻ���롣	1��ǰ����յ㡣0�������յ�
	strcpy(chKey,_T("INSERTSECTION_MODE"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nInsertSectionMode = atoi(chKey);

	//EMPTY_SECTION_NUM				����Ŀ�������ÿ�����ֻ�ܲ���64�յ㣬����1������
	strcpy(chKey,_T("EMPTY_SECTION_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	nEmptySectionNum = atoi(chKey);

	//REAL_SECTION_NUM		15--0	ÿ�������������������1������
	strcpy(chKey,_T("REAL_SECTION_NUM"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	nRealSectionNum = atoi(chKey);



	//˫�����
	strcpy(chKey,_T("dual_out_put"));
	iniFile.GetKeyValue(chSection,chKey,"171",chKey);
	bDualOutput = atoi(chKey) == 1 ? true : false;

	//���������Ų���ʽ
	strcpy(chKey,_T("virTual_array"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nVirTualArray = atoi(chKey);

	//�ư�оƬ
	strcpy(chKey,_T("chip_type"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nChipType = (CHIP_TYPE)atoi(chKey);

	//ref_doule_value	ˢ���ʱ����ı�����
	strcpy(chKey,_T("ref_doule_value"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	nRefreshDoubleValue = atoi(chKey);

	//zhe_rdwr_mode		�۴���ģ���д��DPRAM�ķ�ʽ��Ĭ��Ϊ0
	//0������8��д��1���������ж�д
	strcpy(chKey,_T("zhe_rdwr_mode"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nZheRdwrMode = atoi(chKey);

	//��ʾ������  0-ȫ��ʵ���أ�1-ȫ������
	strcpy(chKey,_T("screen_type"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nScreenType = atoi(chKey);

	//����У������, 0-������1-��ɫ
	strcpy(chKey,_T("dot_correct_tye"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nDotCorrectTye = atoi(chKey);

	//������ʾ�仯
	bVirtualChangeFlag = false;
	//ԭ��������ʾ
	bVirtualPrime = bVirtvalDisp;

	//����Ч��/���Խ���
	strcpy(chKey,_T("test_start"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bTest = atoi(chKey) ? true : false;

	//������Ч�� 2013-3-5 ͨ��оƬ����
	strcpy(chKey,_T("brightness_efficent"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	fBrightnessEfficent = (float) atof(chKey);

	//��СOE����
	strcpy(chKey,_T("min_oe_width"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nMinOEWidth = atoi(chKey);

	//��ɫ��ȱ仯��ʶ
	bScanColorDepthChangeFlag = false;
	//ԭ����ɫ���
	nScanColorDepthPrime = nScanColorDepth;

	//ʹ����㿪·��⹦�� PWM����,ͨ��оƬ�ޣ�1 - ʹ�ܣ� 0-��ʹ��
	strcpy(chKey,_T("dot_open_detection"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bDotOpenDetection = atoi(chKey) ? true : false;

	//PWM���ģʽ	MBI5030: 0-����ģʽ 1-��ͨģʽ��TC62D722: 1-��ɢģʽ��0-��ͨģʽ
	strcpy(chKey,_T("pwm_output_mode"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nPWMOutputMode = atoi(chKey);

	//ˢ���ʱ�����PWM��̬ɨ������Ч
	strcpy(chKey,_T("multi_refresh_under_static_scan"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bMultiRefreshUnderStaticScan = atoi(chKey) ? true : false;

	//����ʹ��
	strcpy(chKey,_T("extend_enable"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bExtendedEnable = atoi(chKey) ? true : false;

	strcpy(chKey,_T("extend_enableEx"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	bExtendedEnableEx = atoi(chKey) ? true : false;

	//����
	strcpy(chKey,_T("section_width"));
	iniFile.GetKeyValue(chSection,chKey,"32",chKey);
	nSectionWidth = atoi(chKey);

	//����������
	strcpy(chKey,_T("section_hor_number"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	nSectionHorNum = atoi(chKey);

	//�Ҷ���ǿλ��
	strcpy(chKey,_T("gray_enhance_bit"));
	iniFile.GetKeyValue(chSection,chKey,"0",chKey);
	nGrayEnhance = atoi(chKey);

	//�Ҷ���ǿ��ʽ
	strcpy(chKey,_T("gray_enhance_mode"));
	iniFile.GetKeyValue(chSection,chKey,"3",chKey);
	nGrayEnhanceMode = atoi(chKey);

	//����ָʾ��
	strcpy(chKey,_T("open_cabinet_lamp"));
	iniFile.GetKeyValue(chSection,chKey,"1",chKey);
	bOpenCabinetLamp = atoi(chKey) ? true : false;
	return true;
}


//дɨ�迨��Ϣ���ļ���
void CStructSingleScanCard::WriteConfig(const char * chCfgFile)
{

	CIniFile IniFlie;		
	IniFlie.SetPath(chCfgFile);
	
	const int MAX_BUF = 260;
	TCHAR chSection[MAX_BUF] = _T("screen");
	TCHAR chKey[MAX_BUF];
	TCHAR chKeyVal[MAX_BUF];

	//��ȥ��ɫ��λ��
	strcpy(chKey,_T("DeductBit"));
	sprintf(chKeyVal,"%d",nDeductBit );
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	
	//ONE_SCAN_CARD_WIDTH	2	128	����ɨ����ƿ�����Ӧ��LED��ʾģ���к������ص��������
	//��ɨ�迨���ȡ���λ��ǰ����λ�ں�ȡֵ��Χ1~256��������16��������
	strcpy(chKey,_T("ONE_SCAN_CARD_WIDTH"));
	sprintf(chKeyVal,"%d",nScanCardWidth);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//ONE_SCAN_CARD_HEIGHT	2	96	����ɨ����ƿ�����Ӧ��LED��ʾģ�����������ص��������
	//��ɨ�迨�߶ȡ���λ��ǰ����λ�ں�ȡֵ��Χ1~256��������16��������
	strcpy(chKey,_T("ONE_SCAN_CARD_HEIGHT"));
	sprintf(chKeyVal,"%d",nScanCardHeight);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//mod_width 1   32  ģ����ȣ�ȡֵ��Χ1-64
	strcpy(chKey,_T("mod_width"));
	sprintf(chKeyVal,"%d",nModuleWidth);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//mod_height 1   32  ģ��߶ȣ�ȡֵ��Χ1-64
	strcpy(chKey,_T("mod_height"));
	sprintf(chKeyVal,"%d",nModuleHeight);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//1   3   ģ������  ģ������ = ģ��߶�/ÿ������
	strcpy(chKey,_T("mod_section_number"));
	sprintf(chKeyVal,"%d",nModuleSectionNumber);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//1    4  ģ����������ģ�������� = ɨ�迨����/ģ�����
	strcpy(chKey,_T("mod_hor_number"));
	sprintf(chKeyVal,"%d",nModuleHorNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//1    3  ģ�����������ģ��������� = ɨ�迨�߶ȡ�ģ��߶�
	strcpy(chKey,_T("mod_ver_number"));
	sprintf(chKeyVal,"%d",nModuleVerNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//SCAN_CARD_SECTION_NUM	1	6	ɨ�迨���������������Ϊ16
	strcpy(chKey,_T("SCAN_CARD_SECTION_NUM"));
	sprintf(chKeyVal,"%d",nScanCardSectionNumber);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//SCAN_CARD_SECTION_ROW_NUM	1	16	ɨ�迨ÿ���������������Ϊ16��
	strcpy(chKey,_T("SCAN_CARD_SECTION_ROW_NUM	"));
	sprintf(chKeyVal,"%d",nScanCardSectionRowNumber);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);


	//Sϵ�в������ӣ��������
	//SCAN_CARD_SECTION_NUM_CABINET	1	6	ɨ�迨���������������Ϊ16
	strcpy(chKey,_T("SCAN_CARD_SECTION_NUM_CABINET"));
	sprintf(chKeyVal,"%d",nScanCardSectionNumber_cabinet);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//SCAN_CARD_SECTION_ROW_NUM	1	16	ɨ�迨ÿ���������������Ϊ16��
	strcpy(chKey,_T("SCAN_CARD_SECTION_ROW_NUM_CABINET"));
	sprintf(chKeyVal,"%d",nScanCardSectionRowNumber_cabinet);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);


	//SCAN_COLOR_DEPTH	1	14	ɨ�迨ɨ�����ɫ��ȣ�ȡ12~16��������
	strcpy(chKey,_T("SCAN_COLOR_DEPTH"));
	sprintf(chKeyVal,"%d",nScanColorDepth);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//GRAY_LEVEL ɨ�迨�Ҷȼ���
	strcpy(chKey,_T("GRAY_LEVEL"));
	sprintf(chKeyVal,"%d",nGrayLedvel);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//origin_color_bit		ԭʼ��ɫ��ȣ�����8BIT,10bit��12bit��16bit
	strcpy(chKey,_T("origin_color_bit"));
	sprintf(chKeyVal,"%d",nOrginColorBit);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//SCAN_MODE	1	4	ɨ���ģʽ��ȡ 1-2-4-8-16
	strcpy(chKey,_T("SCAN_MODE"));
	sprintf(chKeyVal,"%d",nScanMode);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//SCAN_MODE_CABINET	1	4	���������ɨ���ģʽ�������ݣ�Sϵ������ʹ��
	strcpy(chKey,_T("SCAN_MODE_CABINET"));
	sprintf(chKeyVal,"%d",nScanMode_cabinet);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//DOT_CORRECTION_EN	1	1	����У��ʹ�ܣ�ȡ0��1
	strcpy(chKey,_T("DOT_CORRECTION_EN"));
	sprintf(chKeyVal,"%d",bEmendBrightness ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//SCAN_GCLK_FREQUENCY	1	12.5	ɨ��ʱ��Ƶ�ʣ����30Mhz
	strcpy(chKey,_T("SCAN_GCLK_FREQUENCY"));
	sprintf(chKeyVal,"%0.1f",fScanClkFrequency);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//ZONE_DCLK_NUM	2		ÿ����λʱ������256*16
	strcpy(chKey,_T("ZONE_DCLK_NUM"));
	sprintf(chKeyVal,"%d",nZoneDClkNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//1  0-100  ռ�ձ�  ɨ��ʱ�ӵ�ռ�ձ�
	strcpy(chKey,_T("duty_cycle_low_value"));
	sprintf(chKeyVal,"%d",nDutyCycle);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//PWM_SCAN_GCLK_FREQUENCY	1	12.5	PWMʱ��ʱ��Ƶ�ʣ����30Mhz
	strcpy(chKey,_T("PWM_SCAN_GCLK_FREQUENCY"));
	sprintf(chKeyVal,"%0.1f",fPWMScanClkFrequency);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//1  0-100  PWMʱ��ռ�ձȿɵ��ȼ� 
	strcpy(chKey,_T("pwm_duty_cycle_low_value"));
	sprintf(chKeyVal,"%d",nPWMDutyCycle);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//CLR_CLK_NUM	1	4	1~255 ����ʱ����(������ʱ��)
	strcpy(chKey,_T("CLR_CLK_NUM"));
	sprintf(chKeyVal,"%d",nOeClkNumber);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//ˢ��Ƶ�� ������ɫ��Ⱥ�ɨ�跽ʽ��ͬ����ͬ
	strcpy(chKey,_T("refresh_rate"));
	sprintf(chKeyVal,"%d",nRefreshRate);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//REF_FREQ_MIN ˢ������Сֵ
	strcpy(chKey,_T("refresh_rate_min"));
	sprintf(chKeyVal,"%d",nRefreshRateMin);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//REF_FREQ_MAX ˢ�������ֵ
	strcpy(chKey,_T("refresh_rate_max"));
	sprintf(chKeyVal,"%d",nRefreshRateMax);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//CONFIG_IC_TIME	��Ĵ�����������ʱ��(оƬ���ʱ��)�� 1ms--30s
	strcpy(chKey,_T("config_ic_time"));
	sprintf(chKeyVal,"%d",nConfigICTime);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//dat_freq_num   ��֡Ƶ�ʼ�����
	strcpy(chKey,_T("dat_freq_num"));
	sprintf(chKeyVal,"%d",nDatFreqNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//OE_DELAY_VALUE	0x01	�����ӳ�ʱ����������������Ĭ��Ϊ0x01������1������
	strcpy(chKey,_T("OE_DELAY_VALUE"));
	sprintf(chKeyVal,"%d",nOeDelayValue);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//SYN_REFRESH_EN		ͬ��ˢ��ʹ�ܡ�Ĭ��Ϊ0x1��ʹ��
	strcpy(chKey,_T("SYN_REFRESH_EN"));
	sprintf(chKeyVal,"%d",bSyncRefresh ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//VIRTUAL_DISP_EN			������ʾʹ�ܣ�Ĭ��Ϊ0x0����ʹ��
	strcpy(chKey,_T("VIRTUAL_DISP_EN"));
	sprintf(chKeyVal,"%d",bVirtvalDisp ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//FREQ_DIVISION_COEF	0x7		150Mhz�ķ�Ƶϵ�������Ϊ200��Ƶ��ֵΪ0.625Mhz��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1������
	strcpy(chKey,_T("FREQ_DIVISION_COEF"));
	sprintf(chKeyVal,"%d",nFreqDivisionCoeff);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//PWM_FREQ_DIVISION_COEF	0x7		150Mhz�ķ�Ƶϵ�������Ϊ200��Ƶ��ֵΪ0.625Mhz��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1������
	strcpy(chKey,_T("PWM_FREQ_DIVISION_COEF"));
	sprintf(chKeyVal,"%d",nPWMFreqDivisionCoeff);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//DATA_OUTPUT_REVERSE	�����������������ߺ�ɨ����	0x00	����������Ĭ��Ϊ0x0	0x0	��ʹ��	0x1	ʹ��
	strcpy(chKey,_T("DATA_OUTPUT_REVERSE"));
	sprintf(chKeyVal,"%d",bDataOutUpReverse ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//SCAN_OUTPUT_REVERSE		0x00	ɨ��������Ĭ��Ϊ0x0	0x0	��ʹ��	0x1	ʹ��
	strcpy(chKey,_T("SCAN_OUTPUT_REVERSE"));
	sprintf(chKeyVal,"%d",bScanOutUpReverse);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//	DCB_LINE_CLK_EN			0x00	ʹ�����ź�DCBΪʱ��ʹ���ظ߶ȼӱ�;	0x0	��ʹ��	0x1	2��	0x2	3��	0x3	4��
	strcpy(chKey,_T("DCB_LINE_CLK_EN"));
	sprintf(chKeyVal,"%d",nDCBlineClkEn);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//NO_SIGNAL_DISP	���ź���ʾ��Ĭ��Ϊ0		0x0��������0x1��������档0x2��ͼƬ
	strcpy(chKey,_T("NO_SIGNAL_DISP"));
	sprintf(chKeyVal,"%d",nNoSingleDisp);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//DATA_INPUT_DIR		0x32	���ݷ��򣺣�����ʾ�����濴��	
	//Ĭ��Ϊ���ҵ���0x1	0x0	��������	0x1	��������	0x2	��������	0x3	��������
	strcpy(chKey,_T("DATA_INPUT_DIR"));
	sprintf(chKeyVal,"%d",nDataInputDir);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//ROW_DECODE_MODE		�����뷽ʽ��Ĭ��0x2
	//	0x0	��̬������					0x6	164����
	//	0x1	������оƬ��ֱ�������й�	0x7	192����
	//	0x2	138����						0x8	193����
	//	0x3	139����						0x9	595����
	//	0x4	145�����138˫O				0xA	4096����
	//	0x5	154����						0xB	
	strcpy(chKey,_T("ROW_DECODE_MODE"));
	sprintf(chKeyVal,"%d",nRowDecodeMOde);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//	DATA_LINE_TYPE	8	0x14	7--0	�������ʹ��ࣺĬ��Ϊ0x00��
	strcpy(chKey,_T("DATA_LINE_TYPE_RANGE"));
	sprintf(chKeyVal,"%d",nDataLineTypeRange);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//	DATA_LINE_TYPE	8	0x14	7--0	�������ͣ�Ĭ��Ϊ0x00��
	//	0x00-0x1F	�������ֿ�, 
	//	0x20-0x18	��������һ��ɫ1�㴮��
	//	0x30-0x38	��������һ��ɫ8�㴮��
	//	0x40-0x48	��������һ��ɫ16�㴮��
	//	0x50-0x6F	��������һ��ɫ����
	strcpy(chKey,_T("DATA_LINE_TYPE"));
	sprintf(chKeyVal,"%d",nDataLineType);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//DATA_LINE_CTRL	8	0x00	�����߿���,
	//����4��������RB,B,G,RA������ע������bitΪ0������Ϊ1����Ĭ�ϣ�����
	strcpy(chKey,_T("DATA_LINE_CTRL"));
	sprintf(chKeyVal,"%d",nDataLineCtrl);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//FIELD_NUM			�ܳ��������Ϊ136��������1������
	strcpy(chKey,_T("FIELD_NUM"));
	sprintf(chKeyVal,"%d",nFieldNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//HALF_FIELD_NUM	�볡�������Ϊ9��.Ĭ��Ϊ0x6����1����
	strcpy(chKey,_T("HALF_FIELD_NUM"));
	sprintf(chKeyVal,"%d",nHalfFieldNumber);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//FULL_FIELD_NUM	ȫ���������Ϊ128������1����
	strcpy(chKey,_T("FULL_FIELD_NUM"));
	sprintf(chKeyVal,"%d",nFullFieldNumber);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//��ʼ��
	strcpy(chKey,_T("start_field"));
	sprintf(chKeyVal,"%d",nStartField);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//��ֹ��
	strcpy(chKey,_T("end_field"));
	sprintf(chKeyVal,"%d",nEndField);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//�θ߳� 2013-3-5 ͨ��оƬ����
	strcpy(chKey,_T("Second_Filed"));
	sprintf(chKeyVal,"%d",nSecondHighLevel);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//������Ч�� 2013-3-5 ͨ��оƬ����
	strcpy(chKey,_T("brightness_efficent"));
	sprintf(chKeyVal,"%0.3f",fBrightnessEfficent);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//DATA_POLARITY	4	���ݼ��ԣ�Ĭ��Ϊ0x0	0x0	�ߵ�ƽ����	0x1	�͵�ƽ����	0x2-0xF	����14�������Ԥ��
	strcpy(chKey,_T("DATA_POLARITY"));
	sprintf(chKeyVal,"%d",nDataPolarity);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//OE_POLARITY	0x1F	OE���ԣ�Ĭ��Ϊ0x0	0x0	����Ч	0x1	����Ч
	strcpy(chKey,_T("OE_POLARITY"));
	sprintf(chKeyVal,"%d",nOePolarity);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	///////////�յ����////////////
	//EMPTY_INSERT_EN			�յ����ʹ�ܣ���ÿ���ٵ������ٿյ�.	Ĭ��0x0����ʹ��
	strcpy(chKey,_T("EMPTY_INSERT_EN"));
	sprintf(chKeyVal,"%d",bEmptyInsertEnable ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//INSERT_MODE				����յ㷽ʽ��ǰ���뻹�Ǻ���롣	1��ǰ����յ㡣0�������յ�
	strcpy(chKey,_T("INSERT_MODE"));
	sprintf(chKeyVal,"%d",nInsertMode);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//EMPTY_DOT_NUM				����Ŀյ�����ÿ�����ֻ�ܲ���64�յ㣬����1������
	strcpy(chKey,_T("EMPTY_DOT_NUM"));
	sprintf(chKeyVal,"%d",nEmptyDotNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//REAL_DOT_NUM		15--0	ÿ���ٵ����յ㣬����1������
	strcpy(chKey,_T("REAL_DOT_NUM"));
	sprintf(chKeyVal,"%d",nRealDotNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	///////////��������////////////
	//EMPTYSECTION_INSERT_EN			��������ʹ�ܣ���ÿ������������ٿ���.	Ĭ��0x0����ʹ��
	strcpy(chKey,_T("EMPTYSECTION_INSERT_EN"));
	sprintf(chKeyVal,"%d",bEmptySectionInsertEnable ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//INSERTSECTION_MODE				���������ʽ��ǰ���뻹�Ǻ���롣	1��ǰ���������0����������
	strcpy(chKey,_T("INSERTSECTION_MODE"));
	sprintf(chKeyVal,"%d",nInsertSectionMode);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//EMPTY_SECTION_NUM				����Ŀ�������ÿ�����ֻ�ܲ���64����������1������
	strcpy(chKey,_T("EMPTY_SECTION_NUM"));
	sprintf(chKeyVal,"%d",nEmptySectionNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//REAL_SECTION_NUM		15--0	ÿ���ٵ�������������1������
	strcpy(chKey,_T("REAL_SECTION_NUM"));
	sprintf(chKeyVal,"%d",nRealSectionNum);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);



	//˫�����
	strcpy(chKey,_T("dual_out_put"));
	sprintf(chKeyVal,"%d",bDualOutput ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//���������Ų���ʽ
	strcpy(chKey,_T("virTual_array"));
	sprintf(chKeyVal,"%d",nVirTualArray);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//�ư�оƬ
	strcpy(chKey,_T("chip_type"));
	sprintf(chKeyVal,"%d",nChipType);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//ref_doule_value	ˢ���ʱ����ı�����
	strcpy(chKey,_T("ref_doule_value"));
	sprintf(chKeyVal,"%d",nRefreshDoubleValue);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//zhe_rdwr_mode		�۴���ģ���д��DPRAM�ķ�ʽ��Ĭ��Ϊ0
	//0������8��д��1���������ж�д
	strcpy(chKey,_T("zhe_rdwr_mode"));
	sprintf(chKeyVal,"%d",nZheRdwrMode);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//��ʾ������ 0-ȫ��ʵ���أ�1-ȫ������
	strcpy(chKey,_T("screen_type"));
	sprintf(chKeyVal,"%d",nScreenType);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	//����У������, 0-������1-��ɫ
	strcpy(chKey,_T("dot_correct_tye"));
	sprintf(chKeyVal,"%d",nDotCorrectTye);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);	
	
	//����Ч��/���Խ���
	strcpy(chKey,_T("test_start"));
	sprintf(chKeyVal,"%d",bTest ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//������Ч��
	strcpy(chKey,_T("brightness_efficent"));
	sprintf(chKeyVal,"%0.3f",fBrightnessEfficent);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//��СOE����
	strcpy(chKey,_T("min_oe_width"));
	sprintf(chKeyVal,"%d",nMinOEWidth);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//ʹ����㿪·��⹦�� PWM����,ͨ��оƬ�ޣ�1 - ʹ�ܣ� 0-��ʹ��
	strcpy(chKey,_T("dot_open_detection"));
	sprintf(chKeyVal,"%d",bDotOpenDetection ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//PWM���ģʽ	MBI5030: 0-����ģʽ 1-��ͨģʽ��TC62D722: 1-��ɢģʽ��0-��ͨģʽ
	strcpy(chKey,_T("pwm_output_mode"));
	sprintf(chKeyVal,"%d",nPWMOutputMode);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//ˢ���ʱ�����PWM��̬ɨ������Ч
	strcpy(chKey,_T("multi_refresh_under_static_scan"));
	sprintf(chKeyVal,"%d",bMultiRefreshUnderStaticScan ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);
	
	//����ʹ��
	strcpy(chKey,_T("extend_enable"));
	sprintf(chKeyVal,"%d",bExtendedEnable ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	strcpy(chKey,_T("extend_enableEx"));
	sprintf(chKeyVal,"%d",bExtendedEnableEx ? 1 : 0);
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//����
	strcpy(chKey,_T("section_width"));
	sprintf(chKeyVal,"%d",nSectionWidth );
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//����������
	strcpy(chKey,_T("section_hor_number"));
	sprintf(chKeyVal,"%d",nSectionHorNum );
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//�Ҷ���ǿλ��
	strcpy(chKey,_T("gray_enhance_bit"));
	sprintf(chKeyVal,"%d",nGrayEnhance );
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//�Ҷ���ǿ��ʽ
	strcpy(chKey,_T("gray_enhance_mode"));
	sprintf(chKeyVal,"%d",nGrayEnhanceMode );
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

	//����ָʾ��
	strcpy(chKey,_T("open_cabinet_lamp"));
	sprintf(chKeyVal,"%d",bOpenCabinetLamp );
	IniFlie.SetKeyValue(chSection,chKey,chKeyVal);

}
*/
//��ȡˮƽģ������ʹ�ֱģ�����
void CStructSingleScanCard::GetHorVerModuleNum(short & nModHorNum,short & nModVerNum)
{
	nModHorNum = nModuleHorNum;
	nModVerNum = nModuleHorNum;

	if (nDataInputDir == 0 || nDataInputDir == 1)//���ҽ���
	{
		nModVerNum /= (int)pow(2.0,nDCBlineClkEn);
	}
	else if (nDataInputDir == 2 || nDataInputDir == 3)//���½���
	{
		nModHorNum /= (int)pow(2.0,nDCBlineClkEn);
	}

}

//��ȡˮƽģ������ʹ�ֱģ�����,����У���������
void CStructSingleScanCard::GetHorVerModuleNumForCalibrationAndDotDetect(short & nModHorNum,short & nModVerNum)
{
	nModHorNum = nScanCardWidthReal / nModuleWidth;
	nModVerNum = nScanCardHeightReal / nModuleHeight;

	nModHorNum = max(1,nModHorNum);
	nModVerNum = max(1,nModVerNum);

	if (nDataInputDir == 0 || nDataInputDir == 1)//���ҽ���
	{
		nModVerNum /= (int)pow(2.0,nDCBlineClkEn);
	}
	else if (nDataInputDir == 2 || nDataInputDir == 3)//���½���
	{
		nModHorNum /= (int)pow(2.0,nDCBlineClkEn);
	}

}

//��ȡģ����Ⱥ͸߶�
void CStructSingleScanCard::GetHorModuleWidthHeight(short & nModWidth,short & nModHeight)
{

	nModWidth = nModuleWidth;
	nModHeight = nModuleHeight;
	if (nDataInputDir == 0 || nDataInputDir == 1)//���ҽ���
	{
		nModHeight *= (short)pow(2.0, nDCBlineClkEn);
	}
	else if (nDataInputDir == 2 || nDataInputDir == 3)//���½���
	{
		nModWidth *= (short)pow(2.0, nDCBlineClkEn);
	}
}