#include "CLScanCardPackData.h"
#include "../../util/util.h"

extern GLOBALVARIABLE g_GlobalVariable;				//���̵�ȫ�ֱ���

CCLScanCardPackData::CCLScanCardPackData(void)
{
}

CCLScanCardPackData::~CCLScanCardPackData(void)
{
}

//��ɨ�迨���������ݰ������� int - ������
int CCLScanCardPackData::PackScanCardData(int nScanCardAddress,//ɨ�迨��ַ
										  short nAtlvcAddressSecond,
										  int nPackID,//˳���
										  CStructSingleScanCard sScanCard, //ɨ�迨�����ṹ
										  unsigned char* ucSendData,//��������
										  int nEmptyByte)//У��ǰĬ�Ͽ��ֽ���
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	unsigned int unDestAddress = (unsigned int)nScanCardAddress;
	unsigned char ucPackType = 0x40;

	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));

	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�
	//unsigned char ucTemp = 0;
	//��_a1ʱ������1�������Ǽ�1, �����಻���һ�����������һ
	switch (nPackID)
	{
	case 0://����������ز���
		{
			//ʹ�����ź�D��Ϊ�ڶ�·ʱ��ʹ���ظ߶ȼӱ�:�������ܣ����ҽ��ߣ�ģ��߶����������½��ߣ��������
			//1  1�ֽ� mod_width	0x1f	5--0	ģ������ؿ��,�� ��1����
			if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)
			{
				ucData[0] = sScanCard.nModuleWidth - 1;
			}
			else
			{
				ucData[0] = (unsigned char)(sScanCard.nModuleWidth  * pow(2.0,sScanCard.nDCBlineClkEn)- 1);
			}

			//2 1�ֽ� mod_height	8	0x1f	5--0	ģ������ظ߶�,�� ��1����
			if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)
			{
				ucData[1] = (unsigned char)(sScanCard.nModuleHeight * pow(2.0, sScanCard.nDCBlineClkEn) -1);
			}
			else
			{
				ucData[1] = sScanCard.nModuleHeight - 1;
			}

			//3 1�ֽ� scan_mode	8	0x35	7--4	ɨ���ģʽ��Ĭ��Ϊ0x3���� ��1����
			// scan_mode			3--0	ɨ���ģʽ�ĸ�λ
			ucData[2] = ((sScanCard.nScanMode - 1)<< 4)|( ((sScanCard.nScanMode - 1)>>4)& 0x0F);


			//4 1�ֽ� card_section_row_num	8	0x0F	5--0	ɨ�迨ÿ���������������Ϊ64�С�
			ucData[3] = (unsigned char)(sScanCard.nScanCardSectionRowNumber * pow(2.0, sScanCard.nDCBlineClkEn) - 1);

			//5 1�ֽ�  data_line_type	8	0x00	7--0	�������ͣ�Ĭ��Ϊ0x00��RGBR�������¼3
			//Bit7-5�����������ߵĴ���
			//0x0	�������ֿ�, 0x1	��������һ��ɫ1�㴮��
			//0x2	��������һ��ɫ8�㴮��    0x3	��������һ��ɫ16�㴮��
			//0x4	��������һ��ɫ����	Bit4-0���������ߵľ������������¼3
			ucData[4] = (sScanCard.nDataLineTypeRange << 5) | (sScanCard.nDataLineType & 0x1F);


			//6 1�ֽ� 
			//row_decode_mode	8	0x2f	7--4	�����뷽ʽ��Ĭ��0x2
			//0x0	��̬������			0x1	������оƬ��ֱ�������й�			0x2	  138����		
			//0x3	139����				0x4	145�����138˫0						0x5  154����	
			//0x6	164����				0x7	192����								0x8	193����
			//0x9	595����				0xA	4096����							0xB	
			// data_line_ctrl			3--0	����4��������RB,B,G,RA������
			//ע������bitΪ0������Ϊ1����Ĭ�ϣ�����
			ucData[5] = (sScanCard.nRowDecodeMOde << 4 )|(sScanCard.nDataLineCtrl & 0x0F);


			//7 1�ֽ�
			//empty_insert_en	8	0x00	7	�յ����ʹ�ܣ���ÿ���ٵ������ٿյ�.Ĭ��0x0����ʹ��
			//insert_mode			6	����յ㷽ʽ��ǰ���뻹�Ǻ���롣
			//empty_dot_num			5--0	����Ŀյ�����ÿ�����ֻ�ܲ���64�յ㣬����1����
			ucData[6] = (sScanCard.bEmptyInsertEnable ? 0x80 : 0x00) | 
				((sScanCard.nInsertMode & 0x01)<< 6) | ((sScanCard.nEmptyDotNum - 1) & 0x1F);

			//8 2�ֽ� 
			//real_dot_num	16	0x0000	15--0	ÿ���ٵ����յ㣬����1����
			short nRealDotNum = sScanCard.nRealDotNum - 1;
			CTool::ExchangeInteger(nRealDotNum, ucData + 7, 2);

			//9 1�ֽ�
			//oe_polarity	8	0x01	7	OE���ԣ�Ĭ��Ϊ0x0  0x0-����Ч    0x1-����Ч
			//data_polarity			6-4	���ݼ��ԣ�Ĭ��Ϊ0x0    0x0-�ߵ�ƽ����    0x1-�͵�ƽ����   0x2-0x7	����6�������Ԥ��
			//data_input_dir		3--0	���ݷ��򣺣�����ʾ�����濴��Ĭ��Ϊ���ҵ���0x1
			//0x0	��������     0x1	��������     0x2	��������     0x3	��������
			ucData[9] = ((sScanCard.nOePolarity & 0x01) << 7) | 
				((sScanCard.nDataPolarity & 0x07) << 4) | 
				(sScanCard.nDataInputDir & 0x0F);

			// 			//10 1�ֽ�
			//Drive_ic_type	8	0x00	Bit7	0:PWM 1:ͨ�ú���
			//Bit6-0 ָʾ�����оƬ����		0x00	MBI5042
			//0x01	MBI5030		0x02	TC62D722		0x03	MBI5050   TLC5958
			short nICTpye = 0;
			short nPWMType = 0;
			if (_GENERAL == sScanCard.nChipType)//ͨ�ú���
			{
				nICTpye = 1;
			}
			else if (_MBI5152 == sScanCard.nChipType 
				|| _MBI5153 == sScanCard.nChipType 
				|| _MBI5153_E == sScanCard.nChipType
				|| _MBI5155 == sScanCard.nChipType)
			{
				nICTpye = 0;
				nPWMType = 3;
			}
			else if (_MBI5043 == sScanCard.nChipType)
			{
				nICTpye = 0;
				nPWMType = _MBI5041 - 1;//���ͺ�_MBI5041����һ��
			}
			else
			{
				nICTpye = 0;
				nPWMType = sScanCard.nChipType - 1;
			}
			ucData[10] = (nICTpye << 7) | (nPWMType & 0x7F);
			
			//Gclk_delay_num	8	0x40		оƬԤ���ʱ��GCLK���ӳ�ʱ����������ɫ�ֿ�ʱΪR��GCLK�ӳ�ʱ������
			//�Ĵ��������λ����ΪR��G��B��ʹ��λ���ֱ����RGB��Ԥ��繦��;��0��Ϊʹ�ܣ���1��Ϊ�ر�
			if (!sScanCard.bGClkCtrlByRGBEnable) //������ɫ�ֿ�����
			{
				ucData[11] = (0x00 << 7) | (unsigned char)sScanCard.nGClkDelay;
				ucData[12] = (0x00 << 7) | (unsigned char)sScanCard.nGClkDelay;
				ucData[13] = (0x00 << 7) | (unsigned char)sScanCard.nGClkDelay;
			}
			else //����ɫ�ֿ�����
			{
				if (sScanCard.bGClkCtrlByREnable)
				{
					ucData[11] = (0x00 << 7) | (unsigned char)sScanCard.nGClkDelay;
				} 
				else
				{
					ucData[11] = (0x01 << 7) | (unsigned char)sScanCard.nGClkDelay;
				}

				//G��GCLK�ӳ�ʱ����
				if (sScanCard.bGClkCtrlByGEnable)
				{
					ucData[12] = (0x00 << 7) | (unsigned char)sScanCard.nGClkDelay_G;
				} 
				else
				{
					ucData[12] = (0x01 << 7) | (unsigned char)sScanCard.nGClkDelay_G;
				}

				//B��GCLK�ӳ�ʱ����
				if (sScanCard.bGClkCtrlByBEnable)
				{
					ucData[13] = (0x00 << 7) | (unsigned char)sScanCard.nGClkDelay_B;
				} 
				else
				{
					ucData[13] = (0x01 << 7) | (unsigned char)sScanCard.nGClkDelay_B;
				}
			}

			//Ԥ��2�ֽڣ���0x00
			memset( ucData + 14, 0 , 2);
			break;
		}
	case 1://ɨ����ز���
		{
			//1 1�ֽ� 
			//scan_color_depth	8	0XD7    7--4	ɨ�迨ɨ�����ɫ��ȣ�ȡ8~16��������Ĭ��Ϊ0xD������ȡSDRAM�е�λ����������1����
			//origin_color_bit			3-0	ԭʼ��ɫ��ȣ�����8BIT,10bit��12bit��16bit
			ucData[0] = ((sScanCard.nScanColorDepth - 1) & 0x0F) << 4 |((sScanCard.nOrginColorBit - 1) & 0x0F);

			//2  1�ֽ�
			//virtual_disp_en	8	0x00	7	������ʾʹ�ܣ�Ĭ��Ϊ0x0����ʹ�ܣ���ʾ�����ͣ���оƬ���ͣ������������з�ʽ
			//syn_refresh_en			6	ͬ��ˢ��ʹ�ܡ�Ĭ��Ϊ0x1��ʹ��
			//dcb_line_clk_en			5--4	ʹ�����ź�DCBΪʱ��ʹ���ظ߶ȼӱ�;Ĭ��Ϊ0
			//0x0	��ʹ��   0x1	2��     0x3	4��
			//output_reverse			3--0	���������Ĭ��Ϊ0x0
			//bit0=0x0	�ر�����������		bit0=0x1	ʹ������������
			//Bit1=0x0	�ر�ɨ��������		Bit1=0x1	ʹ��ɨ��������
			//Bit2=0x0	�������			Bit2=0x1	˫�����
			short nDCBLineClkEn;
			if (sScanCard.nDCBlineClkEn == 2)
			{
				nDCBLineClkEn = 0x03;
			}
			else
			{
				nDCBLineClkEn = sScanCard.nDCBlineClkEn;
			}
			ucData[1] = (sScanCard.bVirtvalDisp ? 0x80 : 0x00) |
				(sScanCard.bSyncRefresh ? 0x40 : 0x00) |
				((nDCBLineClkEn & 0x03) << 4) | 
				((sScanCard.bDualOutput ? 0x01 : 0x00) << 2) |
				((sScanCard.bScanOutUpReverse ? 0x01 : 0x00) << 1) | 
				(sScanCard.bDataOutUpReverse ? 0x01 : 0x00);

			//3 1�ֽ�
			//oe_delay_value	8	0x06	7-4	�����ӳ�ʱ����������������Ĭ��Ϊ0x00������1����
			//half_field_num			3--0	�볡�������Ϊ9��.Ĭ��Ϊ0x6����1����
			//ucData[2] = (((sScanCard.nOeDelayValue - 1) & 0x0F) << 4 ) |
			//	((sScanCard.nHalfFieldNumber - 1) & 0x0F);

			//4 1�ֽ�
			//��׼�棺field_num	8	0x25	7--0	�ܳ��������Ϊ136��������1����
			//PWM�棺//freq_division_coef	8	0x18	7--0	��Ƶϵ�������Ϊ200��Ƶ��ֵΪ200��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1����
			if (_GENERAL == sScanCard.nChipType)//��׼��
			{
				ucData[3] = sScanCard.nFieldNum - 1;
			}
			else//PWM
			{
				ucData[3] = sScanCard.nPWMFreqDivisionCoeff - 1;
			}

			//5 1�ֽ�
			//��׼��:full_field_num_a1	8	0x7f	6--0	ȫ���������Ϊ128��
			//PWM:duty_cycle_low_value_a1	8	0x05	7--0 ռ�ձȿɵ��ȼ�,������λʱ��ʱ��ռ�ձ����ã����õ͵�ƽ�ļ���ֵ
			//Ĭ��Ϊ0x5��������1����
			if (_GENERAL == sScanCard.nChipType)
			{
				ucData[4] = (unsigned char)sScanCard.nFullFieldNumber;
			}
			else
			{
				ucData[4] = (unsigned char)sScanCard.nPWMDutyCycle;
			}

			//6 1�ֽ�
			//freq_division_coef	8	0x18	7--0	��Ƶϵ�������Ϊ200��Ƶ��ֵΪ200��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1����
			ucData[5] = sScanCard.nFreqDivisionCoeff - 1;

			//7 1�ֽ�
			//duty_cycle_low_value_a1	8	0x05	7--0 ռ�ձȿɵ��ȼ�,������λʱ��ʱ��ռ�ձ����ã����õ͵�ƽ�ļ���ֵ
			//Ĭ��Ϊ0x5��������1����
			ucData[6] = (unsigned char)sScanCard.nDutyCycle;

			//8 2�ֽ�
			//row_oe_clk_num	16	0x00C1  15--6	
			//��׼�棺������ʱ������Ĭ��Ϊ0x0063���ɽ�������
			//PWM�棺�Զ�����,(150Mhz/��Ref_freq*Scan_mode��-afield_clk_num	����ڵ���0x0063��
			int nRowOeClkNum = 0x0063;

			//9 2�ֽ�
			//zone_clk_num	16	0x1207	15--0	ÿ�������ݣ�125Mhz����ʱ������
			int nZoneClkNum = sScanCard.GetZoneClkNum(g_GlobalVariable.nVersionType);
			CTool::ExchangeInteger(nZoneClkNum, ucData + 9, 2);

			//afield_clk_num_a1	20	0x1300	15--0,15-12	
			//��׼�棺(150Mhz/(Ref_freq*Field_num*Scan_mode))-100
			//PWM�棺(��ɫ���/ ref_doule_value)*(Pwm_clk_freq_div / freq_division_coef) 
			//����ڣ�16*Zone_clk_num
			long nAfieldClkNum = sScanCard.GetAfieldClkNum(g_GlobalVariable.nVersionType);


			nRowOeClkNum = sScanCard.GetRowOeClkNum(g_GlobalVariable.nVersionType) - 1;
			CTool::ExchangeInteger(nRowOeClkNum, ucData + 7, 2);

			//10 4�ֽ�
			//afield_clk_num_a1	20	0x1300	15--0,15-12	
			//min_oe_clk_num_a1	16	0x0026	11--0	
			//��׼�棺afield_clk_num_a1/��ʼ������ʼ������¼���Ҷȵȼ����õĳ���Ϣ��
			//PWM�棺��Ĵ�����������ʱ��(��оƬ���ʱ��)��
			//Config_ic_times*(Afield_clk_num+Row_oe_clk_num)*6.6ns
			//2013-3-13 ��һ��ʱ������չ��24bit��ɨ����ز�����01h��������OE��λ�ӳٲ���ȥ����
			int nMinOeClkNum = sScanCard.GetMinOeClkNum(g_GlobalVariable.nVersionType);

			//long nAfieldClkNum1 = (nAfieldClkNum >> 16) | (nAfieldClkNum << 4); 

			long nAfieldClk1 = nAfieldClkNum & 0x0FFFF;
			long nAfieldClk2 = (nAfieldClkNum & 0xF0000);

			//PWMоƬ��zone_clk_num[19-16] 3-0  ÿ�������ݣ�125Mhz����ʱ������4bit��
			if(_GENERAL != sScanCard.nChipType)
			{
				ucData[2] = (unsigned char)((((nAfieldClkNum) & 0x00F00000) >> 16 ) 
					| ((nZoneClkNum >> 16) & 0x0F));
			}
			else
			{
				ucData[2] = (unsigned char)((((nAfieldClkNum) & 0x00F00000) >> 16 ) 
					| ((sScanCard.nHalfFieldNumber - 1) & 0x0F));
			}

			long nTemp = ( nAfieldClk1<< 16 | nAfieldClk2 >> 4 | (nMinOeClkNum &0x0FFF));

			//long nTemp = ((nAfieldClkNum1 << 12) & 0xFFFFF000) | (nMinOeClkNum &0x0FFF);
			CTool::ExchangeInteger(nTemp, ucData + 11, 4);

			//ref_doule_en	8	0x00	7	ˢ���ʱ���ʹ�� 0 - ��ʹ�ܣ�1 - ʹ��
			//ref_doule_value			6--0	ˢ���ʱ����ı�����Ĭ����0�����128��������1
			/*short nRefDouEn = 0;
			if (sScanCard.nRefreshDoubleValue > 1)
				nRefDouEn = 1;
			nTemp = (nRefDouEn == 0 ? 0x00 : 0x80) | (sScanCard.nRefreshDoubleValue - 1);*/
			nTemp = sScanCard.nRefreshDoubleValue - 1;
			ucData[15] = (unsigned char)nTemp;
			break;
		}
	case 2://����ͼ������ز���
		{
			//1  1�ֽ� 
			//zhe_rdwr_mode	2	0x03	7--6	�۴���ģ���д��DPRAM�ķ�ʽ��Ĭ��Ϊ0
			//0������8��д��1���������ж�д
			//scan_mode_a1	6		5--0	ɨ���ģʽ��Ĭ��Ϊ0x3��
			ucData[0] = (sScanCard.nZheRdwrMode << 6) | (sScanCard.nScanMode & 0x3F);

			//ʹ�����ź�D��Ϊ�ڶ�·ʱ��ʹ���ظ߶ȼӱ�
			//2 1�ֽ� 
			//mod_height_a1	8	0x1f	6--0	ģ������ظ߶�
			if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)
			{
				ucData[1] = (unsigned char) (sScanCard.nModuleHeight * pow(2.0, sScanCard.nDCBlineClkEn));
			}
			else
			{
				ucData[1] = (unsigned char)sScanCard.nModuleHeight;
			}

			//3 1�ֽ� 
			//mod_width_a1	8	0x1f	6--0	ģ������ؿ��
			if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)
			{
				ucData[ 2] = (unsigned char) sScanCard.nModuleWidth;
			}
			else
			{
				ucData[ 2] = (unsigned char)(sScanCard.nModuleWidth * pow(2.0, sScanCard.nDCBlineClkEn));
			}

			//4 1�ֽ� 
			//card_sect_row_num_a1	8	0x07	6--0	ÿ������	
			ucData[ 3] = (unsigned char) (sScanCard.nScanCardSectionRowNumber  * pow(2.0, sScanCard.nDCBlineClkEn));


			//5 2�ֽ�  
			//mod_0mode_pix_num	16	0x003f	9--0	���ң�ģ����*������/��ɨ
			//���£�ģ��߶�*������/��ɨ
			//��������mod_0mode_pix_num�������ߴ����й�
			int nMod0modePixNum = 1;
			if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)
			{
				nMod0modePixNum = sScanCard.nModuleWidth * sScanCard.nScanCardSectionRowNumber / sScanCard.nScanMode;
				
				if ( _SSeriesVersion == g_GlobalVariable.nVersionType )
				{
					if (sScanCard.nModuleWidth % sScanCard.nScanMode)
					{
						nMod0modePixNum = (sScanCard.nModuleWidth / sScanCard.nScanMode + 1) * sScanCard.nScanCardSectionRowNumber;
					}
				}
				else
				{
					if (0 != sScanCard.nScanCardSectionRowNumber % sScanCard.nScanMode)
					{
						nMod0modePixNum = sScanCard.nModuleWidth * (sScanCard.nScanCardSectionRowNumber / sScanCard.nScanMode + 1);
					} 
				}
			}
			else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)
			{
				nMod0modePixNum = sScanCard.nModuleHeight * sScanCard.nScanCardSectionRowNumber / sScanCard.nScanMode;
				if ( _SSeriesVersion == g_GlobalVariable.nVersionType )
				{
					if (sScanCard.nModuleHeight % sScanCard.nScanMode)
					{
						nMod0modePixNum = (sScanCard.nModuleHeight / sScanCard.nScanMode + 1) * sScanCard.nScanCardSectionRowNumber;
					}
				}
				else
				{
					if (0 != sScanCard.nScanCardSectionRowNumber % sScanCard.nScanMode)
					{
						nMod0modePixNum = sScanCard.nModuleWidth * (sScanCard.nScanCardSectionRowNumber / sScanCard.nScanMode + 1);
					} 
				}
			}

			switch(sScanCard.nDataLineTypeRange)
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
				nMod0modePixNum = 3 * nMod0modePixNum;//(����)
				break;
			case 4:
				//��������һ��ɫ����
				nMod0modePixNum = 4 * nMod0modePixNum ;//(�ı�)
				break;
			default:
				break;
			}
			nMod0modePixNum *= (int)pow(2.0, sScanCard.nDCBlineClkEn);

			//��յ�����й�
			if (sScanCard.bEmptyInsertEnable)
			{
				nMod0modePixNum = (int)((nMod0modePixNum / (sScanCard.nRealDotNum * 1.0)) * (sScanCard.nRealDotNum + sScanCard.nEmptyDotNum));
			}
			nMod0modePixNum--;
			//ʹ�����ź�D��Ϊ�ڶ�·ʱ��ʹ���ظ߶ȼӱ�
			CTool::ExchangeInteger(nMod0modePixNum, ucData + 4, 2);


			//6 1�ֽ� 
			//zone_col_div_smode	8	0x01	5--0	���£�������/��ɨ
			//���ң�������/��ɨ�����1
			int nZoneColDivSmode;
			if (_SSeriesVersion == g_GlobalVariable.nVersionType)//Sϵ��
			{
				nZoneColDivSmode = (int)(sScanCard.nScanCardSectionRowNumber_cabinet * pow(2.0, sScanCard.nDCBlineClkEn) / sScanCard.nScanMode - 1);
			} 
			else
			{
				nZoneColDivSmode = (int)(sScanCard.nScanCardSectionRowNumber * pow(2.0, sScanCard.nDCBlineClkEn) / sScanCard.nScanMode - 1);
			}
			ucData[6] = nZoneColDivSmode;


			//7 1�ֽ� 
			//zone_col_div_smode_a1	8	0x01	4--0	���£�������/��ɨ
			//���ң�������/��ɨ
			ucData[7] = nZoneColDivSmode + 1;

			//8 2�ֽ� 
			//real_height_0mode	16	0x00ff	15--0	1����̬����������������
			int nPixsPerSection = sScanCard.GetPixPerSetion(g_GlobalVariable.nVersionType) - 1;
			CTool::ExchangeInteger(nPixsPerSection, ucData + 8, 2);

			//9 2�ֽ� 
			//real_height_0mode_a1	16	0x00ff	15--0	
			nPixsPerSection++;
			CTool::ExchangeInteger(nPixsPerSection, ucData + 10, 2);

			//10 2�ֽ� 
			//real_empty_height_0mode	16	0x0100	15--0	ʵ��յ����������յ����ʹ�ܲ���Ч, 
			//real_height_0mode_a1*(ÿ���ٵ����յ�+����յ���)/ÿ���ٵ����յ�
			int nRealPixsPerSection = 0;
			if(sScanCard.bEmptyInsertEnable)
			{
				nRealPixsPerSection = (int)((nPixsPerSection / (sScanCard.nRealDotNum * 1.0)) * (sScanCard.nRealDotNum + sScanCard.nEmptyDotNum) - 1);
			}
			else
			{
				nRealPixsPerSection = nPixsPerSection - 1;
			}
			
			CTool::ExchangeInteger(nRealPixsPerSection, ucData + 12, 2);


			//Ԥ��8�ֽ� 
			memset(ucData + 14, 0, 2);

			//11 2�ֽ�  ģ�������ھ�̬���ص�������Ҫ���ϲ���Ŀյ���
// 			nPixsPerSection = (sScanCard.nSectionWidth * sScanCard.nScanCardSectionRowNumber) / sScanCard.nScanMode;
// 			if (sScanCard.nModuleWidth % sScanCard.nScanMode)
// 			{
// 				nPixsPerSection = (sScanCard.nSectionWidth / sScanCard.nScanMode + 1) * sScanCard.nScanCardSectionRowNumber;
// 			}

			if(sScanCard.bEmptyInsertEnable)
			{
				nRealPixsPerSection = (int)((nPixsPerSection / (sScanCard.nRealDotNum * 1.0)) * (sScanCard.nRealDotNum + sScanCard.nEmptyDotNum) - 1);
			}
			else
			{
				nRealPixsPerSection = nPixsPerSection - 1;
			}
			CTool::ExchangeInteger(nRealPixsPerSection, ucData + 14, 2);
			break;
		}
	case 3://����ͼ������ز���
		{
			//1  2�ֽ� 
			//one_row_bytes	16	0x017f	10--0	
			//����ʵ���ػ����������*3�������16�ı���
			//����ʵ���ػ������������*48.����1����;   
			//������������������*16.�����16�ı���;
			//����������������֧��;    
			//���Ҳ���ɫ������*9��ע�������16�ı���;
			//���²���ɫ��������*144.����1����
			int nOneRowByte = 0x017F;
			int nCardWidth = sScanCard.nScanCardWidth % 16 ? 
				(sScanCard.nScanCardWidth / 16 + 1) * 16 : sScanCard.nScanCardWidth ;
//			int nSectionWidth16 = sScanCard.nSectionWidth % 16 ?
//				( sScanCard.nSectionWidth / 16 + 1) * 16 : sScanCard.nSectionWidth ;
			int nSectionWidth16 = sScanCard.nCard_zone_width % 16 ?
				( sScanCard.nCard_zone_width / 16 + 1) * 16 : sScanCard.nCard_zone_width ;
			if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//����
			{
				if (sScanCard.nScreenType == DISPLAY_TYPE_REAL)//ʵ����
				{
					if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//���ҽ���
					{
						if (sScanCard.bExtendedEnable || sScanCard.bExtendedEnableEx)
						{
							//����������16�ı�����* �������� * 3
							nOneRowByte =  nSectionWidth16 * sScanCard.nSectionHorNum * sScanCard.nCard_zone_Num * 3 - 1;
						}
						else
						{
							nOneRowByte = nCardWidth * 3 - 1;
						}
					}
					else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//���½���
					{
						nOneRowByte = (int)(sScanCard.nScanCardSectionRowNumber * pow(2.0, sScanCard.nDCBlineClkEn) * 48 - 1);
						//nOneRowByte = nCardWidth * 3 - 1;
					}

				}
				else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//������
				{
					if (sScanCard.bVirtvalDisp)
					{
						if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//���ҽ���
						{
							if (0 == sScanCard.nVirTualArray)
							{
								//������������ʾ��4������
								nOneRowByte = nCardWidth * 16 - 1;
							}
							else if (1 == sScanCard.nVirTualArray
								|| 2 == sScanCard.nVirTualArray
								|| 3 == sScanCard.nVirTualArray )
							{
								//��ɫ������һ���ص�Ϊ����*12��2Ϊ�������ݣ�
								nOneRowByte = nCardWidth * 12/* * 2*/ - 1;
							}
							else
							{
								nOneRowByte = nCardWidth * 16 - 1;
							}
						}
					}
					else
					{
						if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//���ҽ���
						{
							nOneRowByte = nCardWidth * 3 - 1;
						}
						else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//���½���
						{
							nOneRowByte = (int)(sScanCard.nScanCardSectionRowNumber * pow(2.0, sScanCard.nDCBlineClkEn) * 48 - 1);
						}
					}
				}
			}
			else if (sScanCard.nDotCorrectTye == 2)//��ɫ
			{
				if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//���ҽ���
				{
					//nOneRowByte = nCardWidth * 9 - 1;
					nOneRowByte = (nSectionWidth16 * sScanCard.nSectionHorNum * sScanCard.nCard_zone_Num) * 9 - 1;
				}
				else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//���½���
				{
					nOneRowByte = (int)(sScanCard.nScanCardSectionRowNumber * pow(2.0, sScanCard.nDCBlineClkEn) * 144 - 1);
				}
			}
			CTool::ExchangeInteger(nOneRowByte, ucData , 2);


			//2 2�ֽ� 
			//one_row_bytes_a1	16	0x0180	11--0   ʵ�ʿ���*3

			nOneRowByte = sScanCard.nScanCardWidthReal * 3;
			CTool::ExchangeInteger(nOneRowByte, ucData + 2, 2);

			//3 2�ֽ� 
			//one_row_bytes2_a1	16	0x0180	10--0	��������:���ؿ��*6
			//����ʵ���أ����ؿ��*3
			//�������ݶ�ΪĬ�ϡ�
			int nOneRowByte2 = 0;
			if (sScanCard.nScreenType == DISPLAY_TYPE_REAL)//ʵ����
			{
				if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//���½���
				{
					nOneRowByte2 = sScanCard.nScanCardWidthReal * 3;
				}
				else
				{
					nOneRowByte2 = nOneRowByte;
				}
			}
			else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//������
			{
				if (sScanCard.bVirtvalDisp)
				{
					if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//���ҽ���
					{
						nOneRowByte2 = (sScanCard.nScanCardWidthReal * 2 + 1) * 6;
					}
					else
					{
						nOneRowByte2 = nOneRowByte;
					}
				}
				else
				{
					if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//���½���
					{
						nOneRowByte2 = sScanCard.nScanCardWidthReal * 3;
					}
					else
					{
						nOneRowByte2 = nOneRowByte;
					}
				}

			}

			CTool::ExchangeInteger(nOneRowByte2, ucData + 4, 2);

			//4 2�ֽ� 
			int nTmp = 0;
			//card_width16	16	0x007f	11--0	���ң�����(��ճ�16����),����1����
			//nCardWidth --;
			// memcpy(data + nLen + 6, &nCardWidth, 2);
			if ( sScanCard.bExtendedEnable ||sScanCard.bExtendedEnableEx)
			{
				//����������16�ı�����*��������
				//nTmp = nSectionWidth16 * sScanCard.nSectionHorNum - 1 ;
				
				//����������16�ı�����*ģ���������� * ģ����������
				nTmp = nSectionWidth16 * sScanCard.nSectionHorNum * sScanCard.nCard_zone_Num - 1 ;

			}
			else
			{
				nTmp = nCardWidth - 1 ;
			}
			CTool::ExchangeInteger(nTmp, ucData + 6, 2);

			//5 2�ֽ�  
			//card_width16_a1	16	0x0080	12--0	Card_width16+1
			//nCardWidth ++;
			// memcpy(data + nLen + 8, &nCardWidth, 2);
			nTmp ++ ;
			CTool::ExchangeInteger(nTmp, ucData + 8, 2);



			//6 2�ֽ� 
			//ʹ�����ź�D��Ϊ�ڶ�·ʱ��ʹ���ظ߶ȼӱ�������������
			//card_height_mode16	16	0x00ff	15--0	1����̬����������������
			//����:����(��ճ�16����)*������/(��ɨ)������:����*������/(��ɨ)��
			int nPixsPerSection = 0;
			if (_SSeriesVersion == g_GlobalVariable.nVersionType)
			{
				if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)
				{
					nPixsPerSection = (int)((nCardWidth * sScanCard.nScanCardSectionRowNumber
						* pow(2.0, sScanCard.nDCBlineClkEn)) / sScanCard.nScanMode_cabinet - 1);
				}
				else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)
				{
					nPixsPerSection = (int)((sScanCard.nScanCardHeight * sScanCard.nScanCardSectionRowNumber 
						* pow(2.0, sScanCard.nDCBlineClkEn)) / sScanCard.nScanMode_cabinet - 1);
				}
			} 
			else
			{
				if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)
				{
					nPixsPerSection = (int)((nCardWidth * sScanCard.nScanCardSectionRowNumber
						* pow(2.0, sScanCard.nDCBlineClkEn)) / sScanCard.nScanMode - 1);
				}
				else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)
				{
					nPixsPerSection = (int)((sScanCard.nScanCardHeight * sScanCard.nScanCardSectionRowNumber 
						* pow(2.0, sScanCard.nDCBlineClkEn)) / sScanCard.nScanMode - 1);
				}
			}

			// 	memcpy(data + nLen + 10, &nPixsPerSection, 2);
			CTool::ExchangeInteger(nPixsPerSection, ucData + 10, 2);

			//7 2�ֽ� 
			//card_height_mode16_a1	16	0x0100	15--0	Card_height_0mode+1
			nPixsPerSection++;
			CTool::ExchangeInteger(nPixsPerSection, ucData + 12, 2);

			//����
			CTool::ExchangeInteger(nSectionWidth16 ,ucData + 14, 2) ; 
			break;
		}
	case 4://����ͼ������ز���
		{
			//1 
			//smart_addr_sel	8	0x00	1--0	Bit7-2	Ԥ��
			//Bit1-0	��������ѡ��λ.1��ѡ�ɼ���������   2��ѡ��λ�������ġ�
			ucData[0] = 2;

			//2
			//card_width	16	0x007f	15--0	�����������ؿ�ȣ�����ʱҲָʵ�ʵ�������
			int nCardWidth = sScanCard.nScanCardWidth % 16 ? 
				(sScanCard.nScanCardWidth / 16 + 1) * 16 : sScanCard.nScanCardWidth;
			int nScanCardWidth = nCardWidth - 1;
			CTool::ExchangeInteger(nScanCardWidth, ucData + 1, 2);

			//3
			//card_height	16	0x005f	15--0	�����������ظ߶�
			int nScanCardHieght = sScanCard.nScanCardHeight - 1;
			CTool::ExchangeInteger(nScanCardHieght, ucData + 3, 2);

			//4 Drive_ic_reg	16			������оƬ�ļĴ���ֵ,����������оƬ�ļĴ���ֵ
			if (_MBI5030 == sScanCard.nChipType || _TC62D722 == sScanCard.nChipType 
				||_TLC5948 == sScanCard.nChipType)//MBI5030,TC62D722,TLC5948
			{
				int nDriveIC = (sScanCard.nPWMOutputMode << 10 |0x0800) | ((sScanCard.bDotOpenDetection ? 1 : 0) << 13 |0x4000);
				CTool::ExchangeInteger(nDriveIC, ucData + 5, 2);
			}
			else if (_MBI5040 == sScanCard.nChipType)//MBI5040
			{
				//15bit Thermal_shut:ȱʡ0                0:Disable;1:Enable
				//14bit NA
				//13bit Error_detc                        0:Disable;1:Enable,ȱʡ0 �����ø��ݿ�·��� 
				//12bit NA
				//11bit Auto_sync:ȱʡ1                   0: Auto;1: Manual
				//10bit Scramble_PWM:ȱʡ1                0: S-PWM;1: PWM
				//9bit-8bit Thresh_vol_short[1:0]ȱʡ00   00:Disable, 01:0.4V	10:0.5V	11��0.73V
				//7bit Switch_speed ȱʡ1                 0:Low;1:High,
				//4bit-6bit Ĭ��Ϊ0
				//3bit  PWM_gray_scale: ȱʡ0             0:16bit;1:12bit	�����ø���ɨ�迨��ɫ���������
				//2bit Ĭ��Ϊ0 
				//1bit Ĭ��Ϊ0 
				//0bit Count_mode:ȱʡ00:Continuous;1:One-shot
				//NA-����·�0��
				//int nDriveIC = (((sScanCard.nScanColorDepth ==16)? 1: 0)<< 15 & 0x8000) | ((sScanCard.nPWMOutputMode<<14) & 0x4000) | 0x2080 | (((sScanCard.bDotOpenDetection? 1:0) << 10)& 0x0400);
				int nDriveIC = ((sScanCard.bDotOpenDetection ? 1 : 0) << 13 )| (sScanCard.nPWMOutputMode << 10| 0x0800 )  |((sScanCard.nScanColorDepth ==16)? 0: 1)<< 3;
				CTool::ExchangeInteger(nDriveIC, ucData + 5, 2);
			}
			else if (_MBI5041 == sScanCard.nChipType || _MBI5045 == sScanCard.nChipType
				|| _MBI5043 == sScanCard.nChipType)
			{
				//15bit NA                
				//14bit NA
				//13bit Error_detc  5041,5043�̶�Ϊ0,5045�����û�ѡ��      0:Disable;1:Enable,ȱʡ0 �����ø��ݿ�·��� 
				//12bit NA
				//11bit NA                   
				//10bit NA               
				//9bit-8bit NA   
				//6-7bit NA              
				//4bit NA		1��ʹ��Ԥ��磬ȥ���²�Ӱ,0���ر�Ԥ��繦��
				//3bit  NA		5041,5045�̶�Ϊ0��5043:GCLK˫�ز���	0���رգ�	1��������ȱʡ0
				//2bit NA
				//1bit NA		5041,5045�̶�Ϊ0��5043:PWMģʽѡ��	0: 16bit��	1: 10bit
				//0bit NA
				//NA-����·�0��
				int nDriveIC = 0;
				if (_MBI5041 == sScanCard.nChipType)
				{
					nDriveIC = (sScanCard.bChipPrecharge ? 1 : 0) << 4 ;
				}
				else if (_MBI5043 == sScanCard.nChipType)
				{
					nDriveIC = ((sScanCard.bChipPrecharge ? 1 : 0) << 4) 
						| ((sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5043.bGCLKDoublesampling ? 1 : 0) << 3)//MBI5153 GCLK˫�ز���	0���رգ� 1������
						| (sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5043.nPWMMode << 1);					//MBI5153 PWM Modoѡ��	0: 16bit��1: 10bit
				}
				else
				{
					nDriveIC = (((sScanCard.bDotOpenDetection ? 1 : 0) << 13 )|((sScanCard.bChipPrecharge ? 1 : 0) << 4 ));	
				}
				CTool::ExchangeInteger(nDriveIC, ucData + 5, 2);

			}
			else if (_TLC5958 == sScanCard.nChipType)
			{
				//reg1
				// Bit15	NA
				// Bit14	NA
				// Bit11--Bit13	BC(100)��ȫ�����ȵ���
				// Bit9--Bit10	LGSE1_R(00)����ɫ�ͻ���ǿ
				// Bit7--Bit8	LGSE1_G(00)����ɫ�ͻ���ǿ
				// Bit5--Bit6	LGSE1_B(00)����ɫ�ͻ���ǿ
				// Bit4			GDLY(1)�����ͨ���ӳ�ʹ��
				// Bit2--Bit3	TD0(01)�����������ӳ��趨
				// Bit0--Bit1	LODVTH(01)����·����ѹ�趨
				int nDrive_ic_reg = sScanCard.nDrive_ic_reg.nBright << 11
					| sScanCard.nDrive_ic_reg.nLgse_R << 9
					| sScanCard.nDrive_ic_reg.nLgse_G << 7
					| sScanCard.nDrive_ic_reg.nLgse_B << 5
					| sScanCard.nDrive_ic_reg.nGdly_Enable << 4
					| sScanCard.nDrive_ic_reg.nTD_Delay << 2
					| sScanCard.nDrive_ic_reg.nLodvth;
				CTool::ExchangeInteger(nDrive_ic_reg, ucData + 5, 2);
			}
			else if (_MBI5152 == sScanCard.nChipType || _MBI5153 == sScanCard.nChipType)
			{
				//reg1
				//Bit15	Ԥ���ģʽ��0�رգ�1����
				//Bit14	PWM����ģʽ��0������1����
				//Bit7	_MBI5152 �ҽ�ģʽ��0=16bit��1=14bit	Bit7	_MBI5153 �ҽ�ģʽ��0=14bit��1=13bit
				//Bit6	GCLK��Ƶ��0���ã�1ʹ��
				int nDrive_ic_reg = sScanCard.nDrive_ic_reg.nPre_Charge1 << 15
					| sScanCard.nDrive_ic_reg.nPwm_Count_Mode << 14
					| (sScanCard.bDotOpenDetection ? 1 : 0) << 13
					| sScanCard.nDrive_ic_reg.nGray_Mode << 7
					| sScanCard.nDrive_ic_reg.nEnable_GCLK << 6;
				CTool::ExchangeInteger(nDrive_ic_reg, ucData + 5, 2);
			}
			else if (_MBI5153_E == sScanCard.nChipType || _MBI5155 == sScanCard.nChipType)
			{
				//R_REG1��MBI5153��ɫ����оƬ��1��Ĵ�����Ĭ��ֵΪ0x9F2B
				ucData[5] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RHigh;
				ucData[6] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RLow;
			}
			else
			{
				CTool::ExchangeInteger(0x0802, ucData + 5, 2);
			}

			//5 Mzone_width  16	ģ��������
			CTool::ExchangeInteger(sScanCard.nSectionWidth, ucData + 7, 2);

			//6	_TLC5958 Drive_ic_reg2 16 ������оƬ�ļĴ���ֵ,������TLC5958_FC2  ����оƬ�ļĴ���ֵ
			// ����оƬĬ��
			if (_TLC5958 == sScanCard.nChipType)
			{
				//reg2 ��Э��
				// Bit15		NA
				// Bit14		NA
				// Bit13		NA
				// Bit12		NA
				// Bit11		NA
				// Bit10		NA
				// Bit9 		NA
				// Bit8 		NA
				// Bit7 		NA
				// Bit5--Bit6	LGSE2(00)���ͻ���ǿ
				// Bit4 		PWM_MODE(0)����ɢģʽ
				// Bit3 		EMI_R(0)����ɫEMI����
				// Bit2 		EMI_G(0)����ɫEMI����
				// Bit1 		EMI_B(0)����ɫEMI����
				// Bit0 		PRE_CHARGE(0)��Ԥ���ģʽ

				int nDrive_ic_reg2 = sScanCard.nDrive_ic_reg.nGlobal_Lgse << 5
					| sScanCard.nDrive_ic_reg.nPVM_Mode << 4
					| sScanCard.nDrive_ic_reg.nEMI_R << 3
					| sScanCard.nDrive_ic_reg.nEMI_G << 2
					| sScanCard.nDrive_ic_reg.nEMI_B << 1
					| sScanCard.nDrive_ic_reg.nPre_Charge;
				CTool::ExchangeInteger(nDrive_ic_reg2, ucData + 9, 2);

				//Ԥ��5λ
				memset(ucData + 11, 0, 5);
			}
			else if (_MBI5152 == sScanCard.nChipType || _MBI5153 == sScanCard.nChipType)
			{
				//Bit15	_MBI5153 ˫��ˢ���ʣ�0�رգ�1���� MBI5152 NA
				//Bit13-Bit12 ��·����ѹ��00: 0.3V��01: 0.4V��10: 0.5V��11: 0.6V
				//Bit11-Bit10 ������ICʶ��01��ɫ��10��ɫ��11��ɫ
				//Bit9-Bit7 ����ƫ�����ڣ���ɫ����000: 0 ns��100: 18ns��001: 6 ns��101: 21ns��010: 9 ns��110: 27ns��011: 15 ns��111: 33ns
				//Bit6-Bit4 ����ƫ�����ڣ���ɫ����000: 0 ns��100: 18ns��001: 6 ns��101: 21ns��010: 9 ns��110: 27ns��011: 15 ns��111: 33ns
				//Bit3-Bit1 ����ƫ�����ڣ���ɫ����000: 0 ns��100: 18ns��001: 6 ns��101: 21ns��010: 9 ns��110: 27ns��011: 15 ns��111: 33ns
				//Bit0 ����ģʽ�ߵ�ƽ�����죺0�رգ�1����

				int nDrive_ic_reg2 = sScanCard.nDrive_ic_reg.nDouble_RefreseRate << 15
					| 0x4000
					| sScanCard.nDrive_ic_reg.nVoltage << 12
					/*| sScanCard.nDrive_ic_reg.nIC_Recognition << 10*/
					| sScanCard.nDrive_ic_reg.nAdjust_Blue << 7
					| sScanCard.nDrive_ic_reg.nAdjust_Green << 4
					| sScanCard.nDrive_ic_reg.nAdjust_Red << 1
					| sScanCard.nDrive_ic_reg.nImhl_DoNotStretch;
				CTool::ExchangeInteger(nDrive_ic_reg2, ucData + 9, 2);

				//Ԥ��5λ
				memset(ucData + 11, 0, 5);
			}
			else if (_MBI5153_E == sScanCard.nChipType || _MBI5155 == sScanCard.nChipType)
			{
				//R_REG2��MBI5153��ɫ����оƬ��2��Ĵ�����Ĭ��ֵΪ0x4600
				ucData[9] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2RHigh;
				ucData[10] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2RLow;
			}
			else
			{
				//Ԥ��7λ
				memset(ucData + 9, 0, 7);
			}

			//������������
			//������������
			ucData[11]= sScanCard.nNetPortPriority |
						(sScanCard.bLockNetPort ? 1 : 0) << 1;
			
			break;
		}
	case 5://У���ü�����ز���
		{
			//1  2�ֽ� 
			//one_row_coef_num	16	0x017f	15--0	1����Ƶ���ݶ��õ���ЧУ��������
			//ʵ���ص�����������ҽ��ߣ�����*3�����½��ߣ�������*16*3
			//���ⵥ�����������*4
			//ʵ���ص����ɫ������*9
			//������������16������������ɫ��ʵ�ʿ���
			int nCardWidth = 0 ;
			if ( sScanCard.bExtendedEnable ||sScanCard.bExtendedEnableEx)
			{
				//���������� = �����16�ı��� * ��������
//				int nSectionWidth16 = sScanCard.nSectionWidth % 16 ?
//					(sScanCard.nSectionWidth / 16 + 1) * 16 : sScanCard.nSectionWidth ;
//				nCardWidth = nSectionWidth16 * sScanCard.nSectionHorNum ;
				int nSectionWidth16 = sScanCard.nCard_zone_width % 16 ?
					(sScanCard.nCard_zone_width / 16 + 1) * 16 : sScanCard.nCard_zone_width ;
				nCardWidth = nSectionWidth16 * sScanCard.nSectionHorNum  * sScanCard.nCard_zone_Num;

			}
			else
			{
				nCardWidth = sScanCard.nScanCardWidth % 16 ?
					(sScanCard.nScanCardWidth / 16 + 1) * 16 : sScanCard.nScanCardWidth ;
			}

			int nOneRowCoefNum = 0x017F;
			if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//����
			{
				if (sScanCard.nScreenType == DISPLAY_TYPE_REAL)//ʵ����
				{
					if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//���ҽ���
					{
						nOneRowCoefNum = nCardWidth * 3 - 1;
					}
					else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//���½���
					{
						nOneRowCoefNum = (int)(sScanCard.nScanCardSectionRowNumber * pow(2.0, sScanCard.nDCBlineClkEn) * 16 * 3 - 1);
					}
				}
				else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//������
				{
					if (sScanCard.bVirtvalDisp)
					{
						if (0 == sScanCard.nVirTualArray)
						{
							nOneRowCoefNum = nCardWidth * 4 - 1;
						} 
						else if (1 == sScanCard.nVirTualArray || 2 == sScanCard.nVirTualArray || 3 == sScanCard.nVirTualArray)
						{
							//����һ���أ���������
							nOneRowCoefNum = nCardWidth * 3/* * 2*/ - 1;
						}
						else
						{
							nOneRowCoefNum = nCardWidth * 4 - 1;
						}
					}
					else
					{
						nOneRowCoefNum = nCardWidth * 3 - 1;
					}

				}
			}
			else if (sScanCard.nDotCorrectTye == 2)//��ɫ
			{
				if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//���ҽ���
				{
					nOneRowCoefNum = nCardWidth * 9 - 1;
				}
				else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//���½���
				{
					nOneRowCoefNum = (int)(sScanCard.nScanCardSectionRowNumber * pow(2.0, sScanCard.nDCBlineClkEn) * 144 - 1);
				}
			}
			// memcpy(data + nLen, &nOneRowCoefNum, 2);
			CTool::ExchangeInteger(nOneRowCoefNum, ucData, 2);


			//2 2�ֽ� 
			//one_row_coef_all_a1	16	0x0200	15--0	1����Ƶ���ݶ����ܵ�У��������
			//ʵ�����������ص�����������ҽ��ߣ�[(mod_width * 4 + x)/ 32] * 32 * mod_col�� ���½��ߣ�mod_width  * 4 *mod_row
			//	ʵ���ص����ɫ��[(mod_width * 9 + x)/ 32] * 32 * mod_col				
			//	ע��x��ָ��Ӻ��������32.�磺ģ��Ϊ15�ĵ�������x=2
			//	ע��Mod_colָ����ģ����

			int nOneRowCoefNumAllA1 = 0x0200;
			short nModuleWidth16 = sScanCard.nModuleWidth % 16 ? 
				(sScanCard.nModuleWidth / 16 + 1) * 16 : sScanCard.nModuleWidth;
			nModuleWidth16 = nModuleWidth16 > 42 ? sScanCard.nModuleWidth : nModuleWidth16;

			int nTemp = 0;
			if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//����
			{
				if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//ȫ��
				{
					if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//���ҽ���
					{
						//nTemp = (sScanCard.nModuleWidth * 4) % 32 ? 
						//	(sScanCard.nModuleWidth  * 4 / 32 + 1) * 32 :sScanCard.nModuleWidth  * 4;
						nTemp = (nModuleWidth16 * 4) % 32 ? 
							(nModuleWidth16  * 4 / 32 + 1) * 32 : nModuleWidth16 * 4;
						nOneRowCoefNumAllA1 = nTemp * sScanCard.nModuleHorNum;
					}
					else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//���½���
					{
						//nOneRowCoefNumAllA1 = sScanCard.nModuleWidth  *  4 * sScanCard.nModuleHorNum;
						int nModW = (int)(sScanCard.nModuleWidth * pow(2.0, sScanCard.nDCBlineClkEn));

						short nModW16 = nModW % 16 ? (nModW / 16 + 1) * 16 : nModW;
						nTemp = (nModW16 * 4) % 32 ? (nModW16 * 4 / 32 + 1) * 32 :nModW16 * 4;
						//nOneRowCoefNumAllA1 = nTemp * (sScanCard.nModuleHorNum / pow(2.0, sScanCard.nDCBlineClkEn));
						nOneRowCoefNumAllA1 = nTemp * 16;
					}

				}

			}
			else if (sScanCard.nDotCorrectTye == 2)//��ɫ
			{
				if (sScanCard.nScreenType == DISPLAY_TYPE_REAL ||
					(sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && !sScanCard.bVirtvalDisp))//ʵ���ػ���������������ʾ
				{
					nTemp = (nModuleWidth16 * 9) % 32 ? 
						(nModuleWidth16 * 9 / 32 + 1) * 32 :nModuleWidth16 * 9;
					nOneRowCoefNumAllA1 = nTemp * sScanCard.nModuleHorNum;
				}
			}
			CTool::ExchangeInteger(nOneRowCoefNumAllA1, ucData + 2, 2);

			//3 2�ֽ� 
			//row_coef_novalid_nums_a1	16	0x0080	15--0	1����Ƶ���ݶ�Ӧ����ЧУ��������
			//ʵ���ص��������one_row_coef_all_a1-����*3
			//	���ⵥ�������one_row_coef_all_a1-����*4
			//	ʵ���ص����ɫ��one_row_coef_all_a1-����*9

			int nRowCoefNovailNumAl = 0x0080;
			nRowCoefNovailNumAl = nOneRowCoefNumAllA1 - (nOneRowCoefNum + 1);
			CTool::ExchangeInteger(nRowCoefNovailNumAl, ucData + 4, 2);

			//4 1�ֽ� 
			//mod_row_coef_sdram_col_a1 	8	0x10	7--0	ģ��1�����ص�У��������sdramռ�õĿռ䣺
			//ʵ�����������ص�����������ҽ��ߣ�[(mod_width * 4 + x)/ 32] * 16 -16�����½��ߣ�[(mod_width * 16 * 4 + x)/ (32 * 16)] * 16 -16��				
			//	ʵ���ص����ɫ��[(mod_width * 9 + x)/ 32] * 16 -16				
			//	ע��x��ָ��Ӻ��������32.�磺ģ��Ϊ15�ĵ�������x=2
			int nModeRowCoefSdramCol = 0x10;
			if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//����
			{
				if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//ȫ��
				{
					if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//���ҽ���
					{
						//nTemp = (sScanCard.nModuleWidth * 4) % 32 ? 
						//	(sScanCard.nModuleWidth * 4 / 32 + 1)  : sScanCard.nModuleWidth * 4 / 32;
 						nTemp = (nModuleWidth16 * 4) % 32 ? 
 							(nModuleWidth16 * 4 / 32 + 1)  : nModuleWidth16 * 4 / 32;
					}
					else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//���½���
					{
						//nTemp = (sScanCard.nModuleWidth * 16 * 4) % (32 * 16) ? 
						//	(sScanCard.nModuleWidth * 16 * 4 / (32 * 16) + 1)  : sScanCard.nModuleWidth * 16 * 4 / (32 * 16);
						int nModW = (int)(sScanCard.nModuleWidth * pow(2.0, sScanCard.nDCBlineClkEn));

						short nModW16 = nModW % 16 ? (nModW / 16 + 1) * 16 : nModW;

						nTemp = (nModW16 * 4) % 32 ? (nModW16* 4 / 32 + 1) : nModW16 * 4 / 32;
					}
					nModeRowCoefSdramCol = nTemp * 16 -16;
				}

			}
			else if (sScanCard.nDotCorrectTye == 2)//��ɫ
			{
				if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || 
					(sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && !sScanCard.bVirtvalDisp))//ʵ���ػ���������������ʾ
				{
					nTemp = (nModuleWidth16 * 9) % 32 ? 
						(nModuleWidth16 * 9 / 32 + 1) : nModuleWidth16 * 9 / 32;
					nModeRowCoefSdramCol = nTemp * 16 -16;
				}
			}
			int nnModeRowCoefSdramColLow = nModeRowCoefSdramCol & 0xFF;
			memcpy(ucData + 6, &nnModeRowCoefSdramColLow, 1);

			//5 2�ֽ�  
			//mod_coef_bytes_a1	16	0x2000	15--0	1��ģ����Ƶ���ݶ����ܵ�У��������
			//ʵ�����������ص�����������ҽ��ߣ�[(mod_width * 4 + x)/ 32] * 64 * mod_heigh�� ���½��ߣ�ģ����*8*ģ��߶�
			//ʵ���ص����ɫ��[(mod_width * 9 + x)/ 32] * 64 * mod_heigh
			//ע��x��ָ��Ӻ��������32.�磺ģ��Ϊ15�ĵ�������x=2
			long nModeCoefByteA1 = sScanCard.GetModCoefByte(nEmptyByte);
			int nModeCoefByteA1Low = nModeCoefByteA1 & 0xFFFF;
			CTool::ExchangeInteger(nModeCoefByteA1Low, ucData + 7, 2);

			short nModuleVerNum = 0;
			short nModuleHorNum = 0;

			if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//���ҽ���
			{
				nModuleHorNum = sScanCard.nModuleHorNum;
				nModuleVerNum = (int)(sScanCard.nModuleVerNum / pow(2.0, sScanCard.nDCBlineClkEn));
			}
			else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//���½���
			{
				nModuleHorNum = 16;//sScanCard.nModuleHorNum / pow(2.0, sScanCard.nDCBlineClkEn);
				nModuleVerNum = sScanCard.nModuleVerNum;
			}

			//6 1�ֽ� 
			//mod_colrow	8	0x32	7--0	Bit7-4	1�������ĺ���ģ��������1����
			//Bit3-0	1������������ģ����,��1����
			nTemp =  (nModuleHorNum - 1) << 4 | (nModuleVerNum - 1);
			memcpy(ucData + 9, &nTemp, 1);

			//7 1�ֽ� 
			//mod_col_a1	8	0x04	4--0	1�������ĺ���ģ����

			memcpy(ucData + 10, &nModuleHorNum, 1);

			//8 1�ֽ� 
			//mod_row_a1	8	0x03	4--0	1������������ģ����
			memcpy(ucData + 11, &nModuleVerNum, 1);

			//9 1�ֽ�
			//mod_row_coef_sdram_col_a1 	8	0x10	��4λ	ģ��1�����ص�У��������sdramռ�õĿռ䣺
			//ʵ�����������ص�����������ҽ��ߣ�[(mod_width * 4 + x)/ 32] * 16 -16�����½��ߣ�[(mod_width * 16 * 4 + x)/ (32 * 16)] * 16 -16��				
			//	ʵ���ص����ɫ��[(mod_width * 9 + x)/ 32] * 16 -16				
			//	ע��x��ָ��Ӻ��������32.�磺ģ��Ϊ15�ĵ�������x=2
			int nnModeRowCoefSdramColHight = (nModeRowCoefSdramCol & 0xFF00) >> 8;
			memcpy(ucData+ 12, &nnModeRowCoefSdramColHight, 1);

			//10 1�ֽ�
			//mod_coef_bytes_a1	16	0x2000	��4λ	1��ģ����Ƶ���ݶ����ܵ�У��������
			//ʵ�����������ص�����������ҽ��ߣ�[(mod_width * 4 + x)/ 32] * 64 * mod_heigh�� ���½��ߣ�ģ����*8*ģ��߶�
			//ʵ���ص����ɫ��[(mod_width * 9 + x)/ 32] * 64 * mod_heigh
			//ע��x��ָ��Ӻ��������32.�磺ģ��Ϊ15�ĵ�������x=2
			int nModeCoefByteA1Hight = (nModeCoefByteA1 >> 16) & 0xFF;
			CTool::ExchangeInteger(nModeCoefByteA1Hight, ucData + 13, 1);

			//11 1�ֽ�
			//���߷�ʽ��0x00����A,��/��,��B��
			//	0x01����,��/��,��;��,��/��,��
			//	0x02: ��,��/��,��;��,��/��,��
			//	0x03: ��,��/��,��;��,��/��,��
			CTool::ExchangeInteger(sScanCard.nVirTualArray, ucData + 14, 1);
			//Ԥ��1�ֽ� 
			memset(ucData + 15, 0, 1);
			break;
		}
	case 6: //MBI5153�Ĵ���������15h��
		{
			// 1-2	R_REG3	16	0xC003	15-0	MBI5153��ɫ����оƬ��3��Ĵ�����Ĭ��ֵ0xC003
			ucData[0] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RHigh;
			ucData[1] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RLow;

			// 3-4	G_REG1	16	0xDX2X	15-0	MBI5153��ɫ����оƬ��1��Ĵ�����Ĭ��ֵ0xDF2B
			ucData[2] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1GHigh;
			ucData[3] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1GLow;

			// 5-6	G_REG2	16	0x4500	15-0	MBI5153��ɫ����оƬ��2��Ĵ�����Ĭ��ֵ0x4500
			ucData[4] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2GHigh;
			ucData[5] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2GLow;

			// 7-8	G_REG3	16	0x5003	15-0	MBI5153��ɫ����оƬ��3��Ĵ�����Ĭ��ֵ0x5003
			ucData[6] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3GHigh;
			ucData[7] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3GLow;

			// 9-10	B_REG1	16	0xDX2X	15-0	MBI5153��ɫ����оƬ��1��Ĵ�����Ĭ��ֵ0xDF2B
			ucData[8] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1BHigh;
			ucData[9] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1BLow;

			// 11-12	B_REG2	16	0x6500	15-0	MBI5153��ɫ����оƬ��2��Ĵ�����Ĭ��ֵ0x6500
			ucData[10] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2BHigh;
			ucData[11] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2BLow;

			// 13-14	B_REG3	16	0x4003	15-0	MBI5153��ɫ����оƬ��3��Ĵ�����Ĭ��ֵ0x4003
			ucData[12] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3BHigh;
			ucData[13] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3BLow;

			//15-16Ԥ��2�ֽ� 
			memset(ucData + 14, 0x00, 2);
			break;
		}
	case 7: //5155��������Ż���16h��
		{
			// 1	��t	8	0x00	7-0	MBI5155 ��513/257��GCLK�ĵ͵�ƽ���
			// 2	DHT	8	0x1E	7-0	MBI5155 ��513/257��GCLK�ĸߵ�ƽ���
			// 3	 ��f	8	0x00	7-0	MBI5155 ��1��GCLK�ĸߵ�ƽ���
			// 4	 DG_H	8	0x26	7-0	MBI5155 ��514/258��GCLK�ĸߵ�ƽ���
			// 5	 DG_L	8	0x19	7-0	MBI5155 ��514/258��GCLK�ĵ͵�ƽ���
			ucData[0] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT;
			ucData[1] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT;
			ucData[2] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaF;
			ucData[3] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_H;
			ucData[4] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_L;

			//Ԥ���ֽ�				Ԥ��11�ֽ�
			memset(ucData + 5, 0x00, 11);
			break;
		}
	default:
		break;
	}

	switch (nPackID)
	{
	case 6:
		nPackID = 0x15;
		break;
	case 7://MBI5155��������Ż���16h��
		nPackID = 0x16;
		break;
	default:
		break;
	}
	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond,unDestAddress,ucPackType,nPackID,false,ucData,ucSendData);
	return (nLen);
}

//�򵥵�У�������������ð�(0x91)
int CCLScanCardPackData::PackStartOrEnd(int nScanCardAddress,//ɨ�迨��ַ
										short nAtlvcAddressSecond,
										bool bStart, 
										short nType, 
										short nModuleRow,
										short nModulCol,
										unsigned char* ucSendData)//��������
{
	memset(ucSendData, 0, CL_MAX_BUF_NUMBER);

	unsigned int unDestAddress = (unsigned int)nScanCardAddress;
	unsigned char ucPackType = 0x91;

	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));

	bool bAnswer = false;

	//����У�����ݰ� DOT_CORR_CMD 	1	0x00	0x01	��������У�����ݰ���
	//0x02	����У�����ݰ�������Ĭ��ֵ0x00������
	if (nType == 0)//����
	{
		if (bStart)
		{
			ucData[5] = 0x01;
		}
	}
	else if (nType == 1)
	{
		//7 Ӧ���ʶ 0X00-��Ӧ��0x01-��Ӧ��
		bAnswer = true;
		//0x11	������ȡɨ�迨��SPI_FLASH�е�У�����ݵ�SDRAM
		ucData[5] = 0x11;
	}
	if (nType == 2)//����
	{
		//7 Ӧ���ʶ 0X00-��Ӧ��0x01-��Ӧ��
		bAnswer = true;
		//����
		ucData[5] = 0x02;
	}
	else if (nType == 3)
	{
		//7 Ӧ���ʶ 0X00-��Ӧ��0x01-��Ӧ��
		bAnswer = true;
		//0x21	������ȡ����ģ���϶�������ģ���У������д�����ϵ�SPI_flash��mod_col��mod_row�ֽ���Ч��
		ucData[5] = 0x21;
	}
	else if (nType == 4)
	{
		//7 Ӧ���ʶ 0X00-��Ӧ��0x01-��Ӧ��
		bAnswer = true;
		//0x22	������ȡ���ϵ�SPI_flash��У������д������ģ���ϣ�mod_col��mod_row�ֽ���Ч��
		ucData[5] = 0x22;
	}
	else if (nType == 5)
	{
		//7 Ӧ���ʶ 0X00-��Ӧ��0x01-��Ӧ��
		bAnswer = true;
		//0x31	������ȡ���������У������д�����ϵ�SPI_flash��mod_col��mod_row�ֽ���Ч��
		ucData[5] = 0x31;
	}
	else if (nType == 6)
	{
		//7 Ӧ���ʶ 0X00-��Ӧ��0x01-��Ӧ��
		bAnswer = true;
		//0x32	������ȡ���ϵ�SPI_flash��У������д�����������ģ���ϣ�mod_col��mod_row�ֽ���Ч��
		ucData[5] = 0x32;
	}

	//MODULE_ROW	1	0x00		����У����ģ���е�ַ
	CTool::ExchangeInteger(nModulCol, ucData + 6, 1);
	//MODULE_COL	1	0x00		����У����ģ���е�ַ
	CTool::ExchangeInteger(nModuleRow, ucData + 7, 1);


	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond,unDestAddress,ucPackType,0,bAnswer,ucData,ucSendData);

	return (nLen);
}

//��HUB������
int CCLScanCardPackData::PackHUBPara(int nAddress,//ɨ�迨��ַ
									 short nAtlvcAddressSecond,
										CStructSingleScanCard sScancard,
										unsigned char* ucSendData)//��������
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));

	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�

	//SCAN_CARD_SECTION_NUM	1	9	ɨ�迨���������������Ϊ16
	short nScanCardSectionNumber = 1;
	if (sScancard.nDataInputDir == 0 || sScancard.nDataInputDir == 1)//����
	{
 		nScanCardSectionNumber = sScancard.nScanCardHeightReal / sScancard.nScanCardSectionRowNumber;
		if (0 != sScancard.nScanCardHeightReal % sScancard.nScanCardSectionRowNumber)
		{
			nScanCardSectionNumber += 1;
		}
//  	nScanCardSectionNumber *= sScancard.nSectionHorNum;
		nScanCardSectionNumber *= (sScancard.nSectionHorNum * sScancard.nCard_zone_Num);
	}
	else if (sScancard.nDataInputDir == 2 || sScancard.nDataInputDir == 3)//����
	{
		nScanCardSectionNumber = sScancard.nScanCardWidthReal / sScancard.nScanCardSectionRowNumber;
		nScanCardSectionNumber *= sScancard.nSectionHorNum;
	}
	
	CTool::ExchangeInteger(nScanCardSectionNumber, ucData, 1);
	//SCAN_CARD_SECTION_ROW_NUM	1	8	ɨ�迨ÿ���������������Ϊ16��
	CTool::ExchangeInteger(sScancard.nScanCardSectionRowNumber, ucData + 1, 1);
	//2012-11-23 sunj ����������������ܣ�����������������ģ�����������ģ����������
	//�·�HUb���ò���������Ҫ��������������졣
	int nModuleHorNumTemp = sScancard.nScanCardWidthReal / sScancard.nModuleWidth;
	int nModuleVerNumTemp = sScancard.nScanCardHeightReal / sScancard.nModuleHeight;

	int nModuleWidthTemp = sScancard.nModuleWidth;
	int nModuleHeightTemp = sScancard.nModuleHeight;

	if (sScancard.bExtendedEnable)
	{
		//ģ��������:ģ��ĺ������������������䣬ģ��ĸ߶ȺͿ���޸����£�
// 		nModuleWidthTemp = nModuleWidthTemp / sScancard.nSectionHorNum ;
// 		nModuleHeightTemp = nModuleHeightTemp * sScancard.nSectionHorNum ;

// 		nModuleWidthTemp = sScancard.nCard_zone_width;
// 		nModuleHeightTemp = nModuleHeightTemp * sScancard.nSectionHorNum * sScancard.nCard_zone_Num;

		nModuleWidthTemp = nModuleWidthTemp / sScancard.nSectionHorNum ;
		nModuleHeightTemp = nModuleHeightTemp * sScancard.nSectionHorNum * sScancard.nCard_zone_Num;
	}
	if(sScancard.bExtendedEnableEx)
	{
		//ģ�������:ģ��ĺ����������������޸����£�ģ��ĸ߶ȺͿ�Ȳ���
// 		nModuleHorNumTemp = nModuleHorNumTemp / sScancard.nSectionHorNum;
// 		nModuleVerNumTemp = nModuleVerNumTemp * sScancard.nSectionHorNum;		

 		nModuleHorNumTemp = nModuleHorNumTemp / sScancard.nCard_zone_Num;
 		nModuleVerNumTemp = nModuleVerNumTemp * sScancard.nCard_zone_Num;		
	}

	//1    4   ģ��������  ģ�������� = ɨ�迨��� / ģ����
	CTool::ExchangeInteger (nModuleHorNumTemp,ucData + 2, 1);
	//1    3   ģ���������  ģ��������� = ɨ�迨�߶� / ģ��߶�
	CTool::ExchangeInteger (nModuleVerNumTemp,ucData + 3, 1);


	//1    24  ģ���ȣ�����ģ��ĺ��������ȡֵ��Χ��1-64
	CTool::ExchangeInteger(nModuleWidthTemp, ucData + 4, 1);
	//1    24  ģ��߶ȣ�����ģ������������ȡֵ��Χ��1-64
	CTool::ExchangeInteger(nModuleHeightTemp, ucData + 5, 1);//�߶ȵ�8λ	
	CTool::ExchangeInteger(nModuleHeightTemp >> 8, ucData + 15, 1);	//�߶ȸ�8λ

	//SCAN_MODE	1	4	ɨ���ģʽ��ȡ 1-2-4-8-16
	CTool::ExchangeInteger(sScancard.nScanMode, ucData + 6, 1);
	//��̬����	1	0x2	ÿ������/ɨ��ģʽ
	int nStaticLine = sScancard.nScanCardSectionRowNumber / sScancard.nScanMode;
	CTool::ExchangeInteger(nStaticLine, ucData + 7, 1);
	//A����	2	0x06C0	��λ��ǰ����λ�ں�ɨ�迨���*ɨ�迨�߶�/����
	//int nAPara = sScancard.nScanCardWidthReal * sScancard.nScanCardHeightReal / nScanCardSectionNumber;
	//CTool::ExchangeInteger (nAPara,ucData + 8, 2);

	//Info_empty_zone1����ѡ�������룺ÿm����n����	bit7~bit4��ʾm	bit3~bit0��ʾn	2��δ��ѡ�������룺Info_empty_zone=0x00
	short nInfo_empty_zone = 0x00;
	if (sScancard.bEmptySectionInsertEnable)
	{
		nInfo_empty_zone = (sScancard.nRealSectionNum << 4) + sScancard.nEmptySectionNum;
	}
	CTool::ExchangeInteger (nInfo_empty_zone,ucData + 8, 1);
	CTool::ExchangeInteger (0,ucData + 9, 1);//A����û���ã���Ϊ��������

	//Drive_ic_type	1	0x00	7--0	����IC���ͣ�
	//0x01��5030     0x02��62d722
	short nDriveIC = 0;
	if (_MBI5030 == sScancard.nChipType)
		nDriveIC = 1;//5030
	else if (_TC62D722 == sScancard.nChipType)
		nDriveIC = 2;//62d722
	else if(_TLC5948 == sScancard.nChipType)
		nDriveIC = 3;//5948
	else if ( _MBI5040 == sScancard.nChipType)
		nDriveIC = 4;//MBI5040
	else if(_MBI5045 == sScancard.nChipType)
		nDriveIC = 5;
	else if(_TLC5958 == sScancard.nChipType)
		nDriveIC = 6;
	else if(_MBI5152 == sScancard.nChipType || _MBI5153 == sScancard.nChipType || _MBI5153_E == sScancard.nChipType || _MBI5155 == sScancard.nChipType)
		nDriveIC = 7;
	CTool::ExchangeInteger (nDriveIC,ucData + 10, 1);
	//data_input_dir	1	0x06	���ݷ��򣺣�����ʾ�����濴��Ĭ��Ϊ���ҵ���0x1	
	CTool::ExchangeInteger (sScancard.nDataInputDir,ucData + 11, 1);

	//Dot_detc_para	2	0x08	15--0	Bit15�����ʹ�ܣ���0����ʾû�г�㹦�ܣ���1����ʾ�г�㹦�ܡ�
	//Bit14~bit5�������ٵ�飨��1ģʽ��- ʵ������Bit4: ����־��1��ǰ����յ㡣0�������յ�
	//Bit3~bit0������ĵ�������1ģʽ��-�յ�����
	int nTmp = (sScancard.bEmptyInsertEnable ? 1 : 0) << 15 | (sScancard.nRealDotNum - 1) << 5| sScancard.nInsertMode << 4 | (sScancard.nEmptyDotNum - 1);
	CTool::ExchangeInteger (nTmp,ucData + 12, 2);

	//14	Mod_zone_num 0x00  7--0 ģ������
	short nModZoneNum = 1;
	if (sScancard.nDataInputDir == 0 || sScancard.nDataInputDir == 1)//����
	{
		if (sScancard.bExtendedEnable)
		{
			//nModZoneNum = (sScancard.nModuleHeight / sScancard.nScanCardSectionRowNumber) * sScancard.nSectionHorNum;
			
			if (0 == sScancard.nModuleHeight % sScancard.nScanCardSectionRowNumber)
			{
				nModZoneNum = (sScancard.nModuleHeight / sScancard.nScanCardSectionRowNumber) * sScancard.nSectionHorNum;
			}
			else
			{
				nModZoneNum = (sScancard.nModuleHeight / sScancard.nScanCardSectionRowNumber + 1) * sScancard.nSectionHorNum;
			}
		}
		else
		{
			nModZoneNum = sScancard.nModuleHeight / sScancard.nScanCardSectionRowNumber;
			if (0 != sScancard.nModuleHeight % sScancard.nScanCardSectionRowNumber)
			{
				nModZoneNum += 1;
			}
		}
	}
	else if (sScancard.nDataInputDir == 2 || sScancard.nDataInputDir == 3)//����
	{
		nModZoneNum = sScancard.nModuleHeight / sScancard.nScanCardSectionRowNumber;
	}
	CTool::ExchangeInteger(nModZoneNum,ucData + 14, 1);

	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond,nAddress,0xE4,0,true,ucData,ucSendData);

	return (nLen);
}



//�����ɫ�����ݰ�
int CCLScanCardPackData::PackColorTemperatureData(int nAddress,//ɨ�迨��ַ
												  short nAtlvcAddressSecond,
												  LPCOLOURRGB sColorRGB,int nCrTempID,short nHighLowGap,
												  short nGrayEnhanceMode,int nDeductbit, unsigned char* ucSendData)//��������
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));
	int nMaxColor = 256;
	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�

	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�
	//Red_lumi	8	0xff	7--0	�Ҷȵĺ�ɫ��ɫ��ֵ������1����
	if ( sColorRGB->nRed < nMaxColor )
	{
		memcpy(ucData ,&sColorRGB->nRed, 1);
	}
	else if (sColorRGB->nRed == nMaxColor)
	{
		ucData[0] = 0xFF;
	}
	//Gre_lumi	8	0xff	7--0	�Ҷȵ���ɫ��ɫ��ֵ������1����
	if ( sColorRGB->nGreen < nMaxColor)
	{
		memcpy(ucData + 1, &sColorRGB->nGreen, 1);
	}
	else if( sColorRGB->nGreen == nMaxColor)
	{
		ucData[1] = 0xFF;
	}
	//Blu_lumi	8	0xff	7--0	�Ҷȵ���ɫ��ɫ��ֵ������1����
	if ( sColorRGB->nBlue < nMaxColor)
	{
		memcpy(ucData + 2, &sColorRGB->nBlue, 1);
	}
	else if( sColorRGB->nBlue == nMaxColor)
	{
		ucData[2] = 0xFF;
	}
	//Red_ic_lumi	8	0xff	7--0	����оƬ�ĺ�ɫ��ɫ��ֵ��
	memcpy(ucData + 3, &sColorRGB->nICRed, 1);
	//Gre_ic_lumi	8	0xff	7--0	����оƬ����ɫ��ɫ��ֵ��
	memcpy(ucData + 4, &sColorRGB->nICGreen, 1);
	//Blu_ic_lumi	8	0xff	7--0	����оƬ����ɫ��ɫ��ֵ��
	memcpy(ucData + 5, &sColorRGB->nICBlue, 1);


	//������
	//Red_lumi	8	0xff	7--0	�Ҷȵĺ�ɫ��ɫ��ֵ��
	if ( sColorRGB->nRedLow < nMaxColor)
	{
		memcpy(ucData + 6, &sColorRGB->nRedLow, 1);
	}
	else if( sColorRGB->nRedLow == nMaxColor)
	{
		ucData[6] = 0xFF;
	}
	//Gre_lumi	8	0xff	7--0	�Ҷȵ���ɫ��ɫ��ֵ��
	if ( sColorRGB->nGreenLow < nMaxColor)
	{
		memcpy(ucData + 7, &sColorRGB->nGreenLow, 1);
	}
	else if( sColorRGB->nGreenLow == nMaxColor)
	{
		ucData[7] = 0xFF;
	}
	//Blu_lumi	8	0xff	7--0	�Ҷȵ���ɫ��ɫ��ֵ��
	if ( sColorRGB->nBlueLow < nMaxColor)
	{
		memcpy(ucData + 8, &sColorRGB->nBlueLow, 1);
	}
	else if( sColorRGB->nBlueLow == nMaxColor)
	{
		ucData[8] = 0xFF;
	}
	//Red_ic_lumi	8	0xff	7--0	����оƬ�ĺ�ɫ��ɫ��ֵ��
	memcpy(ucData + 9, &sColorRGB->nICRedLow, 1);
	//Gre_ic_lumi	8	0xff	7--0	����оƬ����ɫ��ɫ��ֵ��
	memcpy(ucData + 10, &sColorRGB->nICGreenLow, 1);
	//Blu_ic_lumi	8	0xff	7--0	����оƬ����ɫ��ɫ��ֵ��
	memcpy(ucData + 11, &sColorRGB->nICBlueLow, 1);
	//Gain_bypass 6 0x3f
	//bit5	����������ɫ����ֵ������·
	//bit4	����������ɫ����ֵ������·
	//bit3	�������ĺ�ɫ����ֵ������·
	//bit2	����������ɫ����ֵ������·
	//bit1	����������ɫ����ֵ������·
	//bit0	�������ĺ�ɫ����ֵ������·
	int nBit[6];
	if (sColorRGB->nRed == nMaxColor)
		nBit[0] = 1 ;
	else 
		nBit[0] = 0 ;
	if (sColorRGB->nGreen == nMaxColor)
		nBit[1] = 1 ;
	else 
		nBit[1] = 0 ;
	if (sColorRGB->nBlue == nMaxColor)
		nBit[2] = 1 ;
	else 
		nBit[2] = 0 ;
	if (sColorRGB->nRedLow == nMaxColor)
		nBit[3] = 1 ;
	else 
		nBit[3] = 0 ;
	if (sColorRGB->nGreenLow == nMaxColor)
		nBit[4] = 1 ;
	else 
		nBit[4] = 0 ;
	if (sColorRGB->nBlueLow == nMaxColor)
		nBit[5] = 1 ;
	else 
		nBit[5] = 0 ;

	int nByPass = 0;
	for(int i = 0; i < 6; ++i)
	{
		nByPass |= (nBit[i] << i);
	}
	memcpy(ucData + 12, &nByPass, 1);

	int nEnhance = 0;
	if(nGrayEnhanceMode > 0)
	{
		nEnhance = nGrayEnhanceMode;
	}
	//Gain_control 8 
	//bit7,6	00-�����ߡ�����������(ʹ�ø���������)
	//01-ֻɨ������   10-ֻɨ������  11-�ߡ����������ɨ�跽ʽ
	//bit5--0	�ߡ�������λ����
	//liangdb 2012-11-08
	//���18λ
	// 	if (2 <= nHighLowGap)
	// 	{
	// 		nHighLowGap = 2;
	// 
	// 	}
	// 	if (0 == nHighLowGap)
	// 	{
	// 		nEnhance = 0;
	// 	}

	//liangdb 2013-06-04
// 	if (g_GlobalVariable.nDeductBit > nHighLowGap)
// 	{
// 		nHighLowGap = 0;
// 	}
// 	else
// 	{
// 		nHighLowGap -= g_GlobalVariable.nDeductBit;
// 	}
	if (nDeductbit > nHighLowGap)
	{
		nHighLowGap = 0;
	}
	else
	{
		nHighLowGap -= nDeductbit;
	}

	if (0 == nHighLowGap)
	{
		nEnhance = 0;
	}

	int nTmp = (nEnhance << 6) | nHighLowGap;
	memcpy(ucData + 13, &nTmp, 1);

	//��ǰ���漰�Ҷȶ�Ӧ��ɫ��ֵID 0-7
	CTool::ExchangeInteger(nCrTempID , ucData +  14, 2);

	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond, nAddress,0x40,0x09,false,ucData,ucSendData);

	return (nLen);
}

//�����ɫ�������
int CCLScanCardPackData::PackColorTempCurrentData(int nAddress,//ɨ�迨��ַ
												  short nAtlvcAddressSecond,
												  LPCOLOURRGB sColorRGB,unsigned char* ucSendData)//��������
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));
	//int nMaxColor = 256;
	//9 �������ֽ�  

	//Red_ic_lumi	8	0xff	7--0	����оƬ�ĺ�ɫ��ɫ��ֵ��
	memcpy(ucData + 0, &sColorRGB->nICRed, 1);
	//Gre_ic_lumi	8	0xff	7--0	����оƬ����ɫ��ɫ��ֵ��
	memcpy(ucData + 1, &sColorRGB->nICGreen, 1);
	//Blu_ic_lumi	8	0xff	7--0	����оƬ����ɫ��ɫ��ֵ��
	memcpy(ucData + 2, &sColorRGB->nICBlue, 1);

	//������

	//Red_ic_lumi	8	0xff	7--0	����оƬ�ĺ�ɫ��ɫ��ֵ��
	memcpy(ucData + 3, &sColorRGB->nICRedLow, 1);
	//Gre_ic_lumi	8	0xff	7--0	����оƬ����ɫ��ɫ��ֵ��
	memcpy(ucData + 4, &sColorRGB->nICGreenLow, 1);
	//Blu_ic_lumi	8	0xff	7--0	����оƬ����ɫ��ɫ��ֵ��
	memcpy(ucData + 5, &sColorRGB->nICBlueLow, 1);

	//
	memcpy(ucData + 6, &sColorRGB->nICRed2, 1);
	memcpy(ucData + 7, &sColorRGB->nICGreen2, 1);
	memcpy(ucData + 8, &sColorRGB->nICBlue2, 1);
	memcpy(ucData + 9, &sColorRGB->nICRed1, 1);
	memcpy(ucData + 10, &sColorRGB->nICGreen1, 1);
	memcpy(ucData + 11, &sColorRGB->nICBlue1, 1);
	
	unsigned char Gray_scale_sel = 0x00;

	int nGainEnable = 0;
	for (int i = 0 ; i < BRIGHTFIELD_STEP_NUM ; i ++)
	{
		nGainEnable |= (int)sColorRGB->m_bGainEnable[i] << (BRIGHTFIELD_STEP_NUM - i - 1);
	}

	/*
	int nGainEnable = 0;
	for (int i = 0 ; i < BRIGHTFIELD_STEP_NUM ; i ++)
	{
		//nGainEnable |= (int)sColorRGB->m_bGainEnable[i] << (BRIGHTFIELD_STEP_NUM - i - 1);
		int nValue = (int)sColorRGB->m_bGainEnable[i] << (BRIGHTFIELD_STEP_NUM - i - 1);
		if (nValue > 0)
		{
			if (nGainEnable > 0)
			{
				nGainEnable = min(nGainEnable, nValue);
			} 
			else
			{
				nGainEnable = nValue;
			}
		}
	}
	
	switch (nGainEnable)
	{
	case 0x00:
		Gray_scale_sel = 0;
		break;
	case 0x80://1000 0000 
		Gray_scale_sel = 10;
		break;
	case 0x40://0100 0000
		Gray_scale_sel = 11;
		break;
	case 0x20:
		Gray_scale_sel = 12;
		break;
	case 0x10:
		Gray_scale_sel = 13;
		break;
	case 0x08:
		Gray_scale_sel = 14;
		break;
	case 0x04:
		Gray_scale_sel = 15;
		break;
	case 0x02:
		Gray_scale_sel = 16;
		break;
	case 0x01:
		Gray_scale_sel = 17;
		break;
	default:
		Gray_scale_sel = 0;
		break;
	}
	ucData[12] = Gray_scale_sel;
	*/
	//0x00: ��16bit(��Ic_gain_en=00ʱ)������16bit (��Ic_gain_en��00ʱ)��Gray_scale_sel �� Ic_gain_en���﹤Ҫ���޸�
	ucData[12] = nGainEnable;

	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond, nAddress,0x40,0x0C,false,ucData,ucSendData);

	return (nLen);
}

//�������׶������
int CCLScanCardPackData::PackBrightCurrentData(int nAddress,//ɨ�迨��ַ
											  short nAtlvcAddressSecond,
											  LPCOLOURRGB sColorRGB,int nCrTempID,unsigned char* ucSendData,int nIndex)
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));

	unsigned int ucOrder;

	switch (nIndex)
	{
	case 0:
		{
			ucOrder = 0x10;

			memcpy(ucData + 0, &sColorRGB->nICRed, 1);
			memcpy(ucData + 1, &sColorRGB->nICGreen, 1);
			memcpy(ucData + 2, &sColorRGB->nICBlue, 1);

			memcpy(ucData + 3, &sColorRGB->nRedLow, 1);
			memcpy(ucData + 4, &sColorRGB->nGreenLow, 1);
			memcpy(ucData + 5, &sColorRGB->nBlueLow, 1);

			memcpy(ucData + 6, &sColorRGB->nICRedLow, 1);
			memcpy(ucData + 7, &sColorRGB->nICGreenLow, 1);
			memcpy(ucData + 8, &sColorRGB->nICBlueLow, 1);

			memcpy(ucData + 9, &sColorRGB->nICRed2, 1);
			memcpy(ucData + 10, &sColorRGB->nICGreen2, 1);
			memcpy(ucData + 11, &sColorRGB->nICBlue2, 1);

			memcpy(ucData + 12, &sColorRGB->nICRed1, 1);
			memcpy(ucData + 13, &sColorRGB->nICGreen1, 1);
			memcpy(ucData + 14, &sColorRGB->nICBlue1, 1);

			break;
		}
	case 1:

		{
			ucOrder = 0x11;

			memcpy(ucData + 0, &sColorRGB->nICRed6, 1);
			memcpy(ucData + 1, &sColorRGB->nICGreen6, 1);
			memcpy(ucData + 2, &sColorRGB->nICBlue6, 1);

			memcpy(ucData + 3, &sColorRGB->nICRed7, 1);
			memcpy(ucData + 4, &sColorRGB->nICGreen7, 1);
			memcpy(ucData + 5, &sColorRGB->nICBlue7, 1);

			memcpy(ucData + 6, &sColorRGB->nICRed8, 1);
			memcpy(ucData + 7, &sColorRGB->nICGreen8, 1);
			memcpy(ucData + 8, &sColorRGB->nICBlue8, 1);

			memcpy(ucData + 9, &sColorRGB->nICRed9, 1);
			memcpy(ucData + 10, &sColorRGB->nICGreen9, 1);
			memcpy(ucData + 11, &sColorRGB->nICBlue9, 1);

			int nGainEnable = 0;
			for (int i = 0 ; i < BRIGHTFIELD_STEP_NUM ; i ++)
			{
				nGainEnable |= (int)sColorRGB->m_bGainEnable[i] << (BRIGHTFIELD_STEP_NUM - i - 1);
			}
			int nResEnable = 0;

			for (int i = 0 ; i < BRIGHTFIELD_STEP_NUM ; i ++)
			{
				nResEnable |= (int)sColorRGB->m_bResEnable[i] << (BRIGHTFIELD_STEP_NUM - i - 1);
			}

			memcpy(ucData + 12, &nGainEnable, 1);
			memcpy(ucData + 13, &nResEnable, 1);

			break;
		}

	default:

		break;
	}

	
	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond, nAddress,0x40,ucOrder,false,ucData,ucSendData);

	return (nLen);

}
//��ɨ�迨�������������(0x95)
int CCLScanCardPackData::PackScanCardUpdateStart(int nAddress,short nAtlvcAddressSecond,short nUpdateType, bool bStart,bool bUpdateBoot,unsigned char * ucSendData)
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));


	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�

	//��������1 1 - ������������(boot)  0 - Ӧ�ó�������(app)
	unsigned char ucBoot = bUpdateBoot ? 1 : 0;

	//��������2��0 - AK308G, 1 - AK308RG, 2 - AK302RG
	unsigned char ucAKCard = 0;	
	switch (nUpdateType)
	{
	case 0://0 - AK308G,
		{
			if ( bStart)
				ucAKCard = 0x01;//����boot������������
			else
				ucAKCard = 0x02;//����
		}
		break;
	case 1://1 - AK308RG,
		{
			if ( bStart)
				ucAKCard = 0x03;//����boot������������
			else
				ucAKCard = 0x04;//����
		}
		break;
	case 2://2 - AK302RG
		{
			if ( bStart)
				ucAKCard = 0x05;//����boot������������
			else
				ucAKCard = 0x06;//����
		}
		break;
	}
	ucData[0] = ucBoot << 4 | ucAKCard;

	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond,nAddress,0x95,0,false,ucData,ucSendData);

	return (nLen);
}
//���������������ʼ��(�㲥��Ӧ��)nAddressĿ�ĵ�ַ��nAtlvcAddressSecondԴ��ַ
int CCLScanCardPackData::PackSaveScanCardPara(short nAddress,short nAtlvcAddressSecond, int nTpyeID, bool bDefaul,unsigned char *ucSendData)
{

	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));

	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�

	//0x00 - ���洢����ʼ����
	//0x01 - ��ɨ�迨�����洢��Flash���������������ȡ�ɫ�¡����ܵ�ַ��
	//0x02 - ��٤��ֵ�洢��Flash
	//0x03 - �����߲��ұ�洢��Flash
	//0x04	����ȡ��Ƶ��ַ�浽Flash
	//0x05	�����㴦����浽Flash
	//0x06	����У����ַ���浽Flash
	//0x07	���߽�����Ͳ��Ұ��浽FLASH
	//0x08	���߽�У��ϵ�����浽FLASH
	//0x9	�������ұ�洢��Flash


	//0x0	����ʼ��
	//0x10 - ��ɨ�迨�������³�ʼ�����������������ȡ�ɫ�¡����ܵ�ַ��
	//0x20 - ٤��������³�ʼ��
	//0x30 - ���߲������³�ʼ��
	//0x40	��ȡ��Ƶ��ַ���³�ʼ��
	//0x50	���㴦������³�ʼ��
	//0x60	��У����ַ���³�ʼ��
	//0x70	�߽�����Ͳ��Ұ����³�ʼ��
	//0x80	�߽�У��ϵ�������³�ʼ��
	//0x90	�����ұ����³�ʼ��

	//��	Ԥ��
	//0xf0	���������еĲ������³�ʼ��
	ucData[0] =nTpyeID;

	//�������ȱʡ������ʾ
	if(bDefaul)
		ucData[1] = 0x01;
	else
		ucData[1] = 0x00;

	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond,nAddress,0x49,0,true,ucData,ucSendData);

	return (nLen);
}

//��ɨ�迨�����������ð�
int CCLScanCardPackData::PackScanCardLoadRegionData(short nAddress,
													short nAtlvcAddressSecond,
													RECT rtLoad,
													short nStartX,
													short nStartY,unsigned char * ucSendData)
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));


	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�

	//Ԥ��
	CTool::ExchangeInteger(0, ucData, 1);

	//CARD1_WIDTH 1 ɨ����ƿ�����Ӧ��LED��ʾģ���к������ص������������ȡ�����1����
	//��λ��ǰ����λ�ں�����ʱ��ָ�����صĿ�ȣ����ǽ�ȡ��ȣ�
	CTool::ExchangeInteger(rtLoad.right - rtLoad.left - 1, ucData + 1, 2);

	//CARD1_HEIGHT 1 ɨ����ƿ�����Ӧ��LED��ʾģ�����������ص�����������߶ȡ�����1����
	//��λ��ǰ����λ�ں�����ʱ��ָ�����صĸ߶ȣ����ǽ�ȡ�߶ȣ�
	CTool::ExchangeInteger(rtLoad.bottom - rtLoad.top - 1, ucData +  3, 2);

	//CARD1_ORIGIN_X 2 �������������ʼλ����������Ƶ�еľ�������X���е�ַ����
	CTool::ExchangeInteger(rtLoad.left , ucData +  5, 2);

	//CARD1_ORIGIN_Y 2 �������������ʼλ����������Ƶ�еľ�������Y���е�ַ��
	CTool::ExchangeInteger(rtLoad.top, ucData +  7, 2);

	//SCREEN_X_VALUE 2 �ɼ�����U�ڻ�D�ڷ�����Ƶ���ݵ���ʼ������X��Ĭ��Ϊ��0��0��������ʾ�������Ͻ�����X
	CTool::ExchangeInteger(nStartX, ucData +  9, 2);

	//SCREEN_Y_VALUE 2 �ɼ�����U�ڻ�D�ڷ�����Ƶ���ݵ���ʼ������Y��Ĭ��Ϊ��0��0��������ʾ�������Ͻ�����Y
	CTool::ExchangeInteger(nStartY, ucData +  11, 2);

	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond,nAddress,0x40,0x0D,false,ucData,ucSendData);

	return (nLen);
}

//�������翨���ز�����������ظ���ʵ��ɨ�迨�����������õ�
int CCLScanCardPackData::PackNetCardLoadRegionData(short nAddress,
													short nAtlvcAddressSecond,
													RECT rtLoad,
													short nStartX,
													short nStartY,unsigned char * ucSendData)
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));

	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�
	//Ԥ��
	CTool::ExchangeInteger(0, ucData, 1);

	// netcard_width	2	0x007F	���翨����Ӧ��LED��ʾģ���к������ص������������ȡ���λ��ǰ����λ�ں�
	// ע������ʱҲ��ָ��ȡ�Ŀ�߶ȣ�ɨ�迨ʵ�ʵĿ�߶ȴ�˳���04�еõ���
	CTool::ExchangeInteger(rtLoad.right - rtLoad.left - 1, ucData + 1, 2);

	// netcard _height	2	0x005F	���翨����Ӧ��LED��ʾģ�����������ص�����������߶ȡ���λ��ǰ����λ�ں�
	CTool::ExchangeInteger(rtLoad.bottom - rtLoad.top - 1, ucData +  3, 2);

	// netcard _origin_x	2	0x0000	�������������ʼλ����������Ƶ�еľ�������X���е�ַ����
	CTool::ExchangeInteger(rtLoad.left , ucData +  5, 2);

	// netcard _origin_y	2	0x0000	�������������ʼλ����������Ƶ�еľ�������Y���е�ַ����
	CTool::ExchangeInteger(rtLoad.top, ucData +  7, 2);

	// Screen_x_value	2	0x0000	�ɼ�����U�ڻ�D�ڷ�����Ƶ���ݵ���ʼ������X��Ĭ��Ϊ��0��0��������ʾ�������Ͻ����ꡣ
	CTool::ExchangeInteger(nStartX, ucData +  9, 2);

	// Screen_y_value	2	0x0000	�ɼ�����U�ڻ�D�ڷ�����Ƶ���ݵ���ʼ������Y��Ĭ��Ϊ��0��0��������ʾ�������Ͻ����꣬ 
	CTool::ExchangeInteger(nStartY, ucData +  11, 2);

	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond,nAddress,0x40,0x1C,false,ucData,ucSendData);

	return (nLen);
}

//������ƴ��ز�����
int CCLScanCardPackData::PackSpotlightLoadRegionData(short nAddress,short nAtlvcAddressSecond,RECT rtLoad,
								short nStartX,short nStartY,unsigned char * ucSendData, short nFlag)
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));

	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�
	//Ԥ��
	CTool::ExchangeInteger(0, ucData, 1);

	if ( 0 == rtLoad.right - rtLoad.left || 0 == rtLoad.bottom - rtLoad.top)
	{
		CTool::ExchangeInteger(0, ucData + 1, 2);
		CTool::ExchangeInteger(0, ucData + 3, 2);
	} 
	else
	{
		// netcard_width	2	0x007F	���翨����Ӧ��LED��ʾģ���к������ص������������ȡ���λ��ǰ����λ�ں�
		// ע������ʱҲ��ָ��ȡ�Ŀ�߶ȣ�ɨ�迨ʵ�ʵĿ�߶ȴ�˳���04�еõ���
		CTool::ExchangeInteger(rtLoad.right - rtLoad.left - 1, ucData + 1, 2);

		// netcard _height	2	0x005F	���翨����Ӧ��LED��ʾģ�����������ص�����������߶ȡ���λ��ǰ����λ�ں�
		CTool::ExchangeInteger(rtLoad.bottom - rtLoad.top - 1, ucData +  3, 2);
	}

	// netcard _origin_x	2	0x0000	�������������ʼλ����������Ƶ�еľ�������X���е�ַ����
	CTool::ExchangeInteger(rtLoad.left , ucData +  5, 2);

	// netcard _origin_y	2	0x0000	�������������ʼλ����������Ƶ�еľ�������Y���е�ַ����
	CTool::ExchangeInteger(rtLoad.top, ucData +  7, 2);

	// Screen_x_value	2	0x0000	�ɼ�����U�ڻ�D�ڷ�����Ƶ���ݵ���ʼ������X��Ĭ��Ϊ��0��0��������ʾ�������Ͻ����ꡣ
	CTool::ExchangeInteger(nStartX, ucData +  9, 2);

	// Screen_y_value	2	0x0000	�ɼ�����U�ڻ�D�ڷ�����Ƶ���ݵ���ʼ������Y��Ĭ��Ϊ��0��0��������ʾ�������Ͻ����꣬ 
	CTool::ExchangeInteger(nStartY, ucData +  11, 2);

	int nLen;
	if (1 == nFlag)
	{
		nLen = CCLPackCommunicationData::PackDataBaseV2(nAtlvcAddressSecond,nAddress,0x40,0x1D,false,ucData,ucSendData, nFlag);
	}
	else
	{
		nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond,nAddress,0x40,0x1D,false,ucData,ucSendData);
	}

	return (nLen);
}
//��ģ�������߽߱�ϵ����
int CCLScanCardPackData::PackModelLineCoeffData(short nAddress,short nAtlvcAddressSecond,short nRowID,short nColID,
												LineCoeff sLineCoeff,
												unsigned char * ucSendData)
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));

	//ģ�������ɨ�迨���У�0��
	ucData[0] = (unsigned char)nRowID;
	//ģ�������ɨ�迨���У�0��
	ucData[1] = (unsigned char)nColID;

	//ģ�����ϡ����ҡ���4�ߺ����ϡ����ϡ����¡�����4���У��ϵ��
	for ( int i = 0; i < CALIBRATION_LINE_COEFF_MAX; ++i)
	{
		int nTmp = 0;
		double fCoeff = sLineCoeff.fLineCoeff[i];
		int nFlg = 0;
		do 
		{
			int nInter =(int) fCoeff;
			nTmp |= nInter << (7 - nFlg);
			fCoeff = (fCoeff - nInter) * 2;
			if(fCoeff <= 0)
			{
				break;
			}
			++nFlg;
		} while (nFlg < 8);
		CTool::ExchangeInteger(nTmp, ucData + 2 + i, 1);
	}

	

	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond,nAddress,0x55,0x00,false,ucData,ucSendData);

	return (nLen);
}

//��ɨ�迨������
void CCLScanCardPackData::UnPackScanCardParam(map<int,CCLPackCommunicationData> & mUnPackData,
											  CStructSingleScanCard & sScanCard,RECT & rtLoad)
{
	unsigned int nTmp = 0;
	//data_input_dir		3--0	���ݷ��򣺣�����ʾ�����濴��Ĭ��Ϊ���ҵ���0x1
	//0x0	��������     0x1	��������     0x2	��������     0x3	��������
	sScanCard.nDataInputDir = mUnPackData[0].m_ucRcvData[9]& 0x0F;

	//oe_polarity	8	0x01	7	OE���ԣ�Ĭ��Ϊ0x0  0x0-����Ч    0x1-����Ч
	sScanCard.nOePolarity = mUnPackData[0].m_ucRcvData[9] >> 7;

	//data_polarity			6-4	���ݼ��ԣ�Ĭ��Ϊ0x0    0x0-�ߵ�ƽ����    0x1-�͵�ƽ����   0x2-0x7	����6�������Ԥ��
	sScanCard.nDataPolarity = ( mUnPackData[0].m_ucRcvData[9] >> 4 ) &  0x07;


	//virtual_disp_en	8	0x00	7	������ʾʹ�ܣ�Ĭ��Ϊ0x0����ʹ�ܣ���ʾ�����ͣ���оƬ���ͣ������������з�ʽ
	sScanCard.bVirtvalDisp = mUnPackData[1].m_ucRcvData[1] >> 7 ? true : false;

	//syn_refresh_en			6	ͬ��ˢ��ʹ�ܡ�Ĭ��Ϊ0x1��ʹ��
	sScanCard.bSyncRefresh = mUnPackData[1].m_ucRcvData[1] & 0x40 ? true : false;

	//dcb_line_clk_en			5--4	ʹ�����ź�DCBΪʱ��ʹ���ظ߶ȼӱ�;Ĭ��Ϊ0
	//0x0	��ʹ��   0x1	2��     0x3	4��
	short nDCBLineClkEn = (mUnPackData[1].m_ucRcvData[1] >> 4) &0x03;
	if (nDCBLineClkEn == 0x03)
	{
		sScanCard.nDCBlineClkEn = 2;
	}
	else
	{
		sScanCard.nDCBlineClkEn = nDCBLineClkEn;
	}

	//output_reverse			3--0	���������Ĭ��Ϊ0x0
	//bit0=0x0	�ر�����������		bit0=0x1	ʹ������������
	sScanCard.bDualOutput = mUnPackData[1].m_ucRcvData[1] & 0x04 ? true : false;

	//Bit1=0x0	�ر�ɨ��������		Bit1=0x1	ʹ��ɨ��������
	sScanCard.bScanOutUpReverse = mUnPackData[1].m_ucRcvData[1] & 0x02 ? true : false;

	//Bit2=0x0	�������			Bit2=0x1	˫�����
	sScanCard.bDataOutUpReverse = mUnPackData[1].m_ucRcvData[1] & 0x01 ? true : false;

	//ʹ�����ź�D��Ϊ�ڶ�·ʱ��ʹ���ظ߶ȼӱ�:�������ܣ����ҽ��ߣ�ģ��߶����������½��ߣ��������
	//1  1�ֽ� mod_width	0x1f	5--0	ģ������ؿ��,�� ��1����
	if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)
		sScanCard.nModuleWidth = mUnPackData[0].m_ucRcvData[0] + 1;
	else
		sScanCard.nModuleWidth = (short)((mUnPackData[0].m_ucRcvData[0] + 1) / pow(2.0,sScanCard.nDCBlineClkEn));


	//2 1�ֽ� mod_height	8	0x1f	5--0	ģ������ظ߶�,�� ��1����
	if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)
		sScanCard.nModuleHeight = (short)((mUnPackData[0].m_ucRcvData[1] + 1) / pow(2.0,sScanCard.nDCBlineClkEn));
	else
		sScanCard.nModuleHeight = mUnPackData[0].m_ucRcvData[1] + 1;

	//scan_mode	8	0x35	7--4	ɨ���ģʽ��Ĭ��Ϊ0x3���� ��1����
	short nTmpScanMode = ((mUnPackData[0].m_ucRcvData[2] >> 4) & 0x0F) | (mUnPackData[0].m_ucRcvData[2] & 0x0F) << 4;
	sScanCard.nScanMode = nTmpScanMode + 1;

	//4 1�ֽ� card_section_row_num	8	0x0F	5--0	ɨ�迨ÿ���������������Ϊ64�С�
	sScanCard.nScanCardSectionRowNumber =  (short)((mUnPackData[0].m_ucRcvData[3] + 1) / pow(2.0,sScanCard.nDCBlineClkEn));

	//5 1�ֽ�  data_line_type	8	0x00	7--0	�������ͣ�Ĭ��Ϊ0x00��RGBR�������¼3
	//Bit7-5�����������ߵĴ���
	sScanCard.nDataLineTypeRange = mUnPackData[0].m_ucRcvData[4] >> 5;
	//0x0	�������ֿ�, 0x1	��������һ��ɫ1�㴮��
	//0x2	��������һ��ɫ8�㴮��    0x3	��������һ��ɫ16�㴮��
	//0x4	��������һ��ɫ����	Bit4-0���������ߵľ������������¼3
	sScanCard.nDataLineType = mUnPackData[0].m_ucRcvData[4] & 0x1F;

	//6 1�ֽ� 
	//row_decode_mode	8	0x2f	7--4	�����뷽ʽ��Ĭ��0x2
	//0x0	��̬������			0x1	������оƬ��ֱ�������й�			0x2	  138����		
	//0x3	139����				0x4	145�����138˫0						0x5  154����	
	//0x6	164����				0x7	192����								0x8	193����
	//0x9	595����				0xA	4096����							0xB	
	sScanCard.nRowDecodeMOde = mUnPackData[0].m_ucRcvData[5] >> 4;
	// data_line_ctrl			3--0	����4��������RB,B,G,RA������
	//ע������bitΪ0������Ϊ1����Ĭ�ϣ�����
	sScanCard.nDataLineCtrl = mUnPackData[0].m_ucRcvData[5] & 0x0F;

	//7 1�ֽ�
	//empty_insert_en	8	0x00	7	�յ����ʹ�ܣ���ÿ���ٵ������ٿյ�.Ĭ��0x0����ʹ��
	sScanCard.bEmptyInsertEnable = mUnPackData[0].m_ucRcvData[6] & 0x80 ? true : false;
	//insert_mode			6	����յ㷽ʽ��ǰ���뻹�Ǻ���롣
	sScanCard.nInsertMode = (mUnPackData[0].m_ucRcvData[6] & 0x40) >> 6;
	//empty_dot_num			5--0	����Ŀյ�����ÿ�����ֻ�ܲ���64�յ㣬����1����
	sScanCard.nEmptyDotNum = (mUnPackData[0].m_ucRcvData[6] & 0x1F) + 1;

	//8 2�ֽ� 
	//real_dot_num	16	0x0000	15--0	ÿ���ٵ����յ㣬����1����
	unsigned int nRealDotNum = 0;
	CTool::ExchangeChar(nRealDotNum, mUnPackData[0].m_ucRcvData + 7, 2);
	sScanCard.nRealDotNum = nRealDotNum + 1;

	// 			//10 1�ֽ�
	//Drive_ic_type	8	0x00	Bit7	0:PWM 1:ͨ�ú���
	//Bit6-0 ָʾ�����оƬ����		0x00	MBI5042
	//0x01	MBI5030		0x02	TC62D722		0x03	MBI5050   TLC5958
	if(mUnPackData[0].m_ucRcvData[10] & 0x80)
	{
		sScanCard.nChipType = _GENERAL;//ͨ�ú���
	}
	else
	{
		int nTmp1 = (mUnPackData[0].m_ucRcvData[10] & 0x7F) + 1;
		sScanCard.nChipType = (CHIP_TYPE)(nTmp1);
	}


	//scan_color_depth	8	0XD7    7--4	ɨ�迨ɨ�����ɫ��ȣ�ȡ8~16��������Ĭ��Ϊ0xD������ȡSDRAM�е�λ����������1����
	sScanCard.nScanColorDepth = ((mUnPackData[1].m_ucRcvData[0] & 0xF0) >> 4) + 1;
	//origin_color_bit			3-0	ԭʼ��ɫ��ȣ�����8BIT,10bit��12bit��16bit
	sScanCard.nOrginColorBit = (mUnPackData[1].m_ucRcvData[0] & 0x0F) + 1;

	//PWMоƬ��zone_clk_num[19-16] 3-0  ÿ�������ݣ�125Mhz����ʱ������4bit��
	if(_GENERAL == sScanCard.nChipType)
		sScanCard.nHalfFieldNumber = mUnPackData[1].m_ucRcvData[2] & 0x0F + 1;


	//4 1�ֽ�
	//��׼�棺field_num	8	0x25	7--0	�ܳ��������Ϊ136��������1����
	//PWM�棺//freq_division_coef	8	0x18	7--0	��Ƶϵ�������Ϊ200��Ƶ��ֵΪ200��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1����
	if (_GENERAL == sScanCard.nChipType)//��׼��
		sScanCard.nFieldNum = mUnPackData[1].m_ucRcvData[3] + 1;
	else//PWM
		sScanCard.nPWMFreqDivisionCoeff = mUnPackData[1].m_ucRcvData[3] + 1;

	double fClkFreq[] = 
	{
		30.0, 25.0, 21.4, 18.8, 16.7, 15.0, 13.6, 12.5, 11.5, 10.7,
		10.0, 9.4, 8.3, 7.5, 6.5, 6.0, 5.6, 5.0, 4.5, 4.0 ,
		3.5, 3.0, 2.5, 2.0, 1.5, 1.0, 0.6
	};

	//�Ҷ�ʱ��
	double fPWMFreqDivision = SCANCARD_MHZ * 1.0 / sScanCard.nPWMFreqDivisionCoeff;
	if (_MBI5153_E == sScanCard.nChipType || _MBI5155 == sScanCard.nChipType)
	{
		fPWMFreqDivision = SCANCARD_MHZ_MBI5153_E * 1.0/ sScanCard.nPWMFreqDivisionCoeff;
	}

	for(int n = 0; n < 27; ++n)
	{
		if(fPWMFreqDivision < (fClkFreq[n] + 0.4) && fPWMFreqDivision > (fClkFreq[n] - 0.4))
		{
			sScanCard.fPWMScanClkFrequency = (float) fClkFreq[n];
			break;
		}
	}


	//5 1�ֽ�
	//��׼��:full_field_num_a1	8	0x7f	6--0	ȫ���������Ϊ128��
	//PWM:duty_cycle_low_value_a1	8	0x05	7--0 ռ�ձȿɵ��ȼ�,������λʱ��ʱ��ռ�ձ����ã����õ͵�ƽ�ļ���ֵ
	//Ĭ��Ϊ0x5��������1����
	if (_GENERAL == sScanCard.nChipType)
		sScanCard.nFullFieldNumber = mUnPackData[1].m_ucRcvData[4] + 1;
	else
		sScanCard.nPWMDutyCycle = mUnPackData[1].m_ucRcvData[4];

	//6 1�ֽ�
	//freq_division_coef	8	0x18	7--0	��Ƶϵ�������Ϊ200��Ƶ��ֵΪ200��Ĭ��Ϊ10��Ƶ��ֵΪ0x09������1����
	sScanCard.nFreqDivisionCoeff = mUnPackData[1].m_ucRcvData[5] + 1;

	//��λʱ��
	double fFreqDivision = SCANCARD_MHZ * 1.0/ sScanCard.nFreqDivisionCoeff;
	if (_MBI5153_E == sScanCard.nChipType || _MBI5155 == sScanCard.nChipType)
	{
		fFreqDivision = SCANCARD_MHZ_MBI5153_E * 1.0/ sScanCard.nFreqDivisionCoeff;
	}
	for(int n = 0; n < 27; ++n)
	{
		if(fFreqDivision < fClkFreq[n] + 0.4 && fFreqDivision > fClkFreq[n] - 0.4)
		{
			sScanCard.fScanClkFrequency = (float) fClkFreq[n];
			break;
		}
	}


	//7 1�ֽ�
	//duty_cycle_low_value_a1	8	0x05	7--0 ռ�ձȿɵ��ȼ�,������λʱ��ʱ��ռ�ձ����ã����õ͵�ƽ�ļ���ֵ
	//Ĭ��Ϊ0x5��������1����
	sScanCard.nDutyCycle = mUnPackData[1].m_ucRcvData[6];

	//����Ϣ
	sScanCard.SetFieldNumByGrayLevel();

	//ref_doule_value			6--0	ˢ���ʱ����ı�����Ĭ����0�����128��������1
	sScanCard.nRefreshDoubleValue = mUnPackData[1].m_ucRcvData[15] + 1;

	//1  1�ֽ� 
	//zhe_rdwr_mode	2	0x03	7--6	�۴���ģ���д��DPRAM�ķ�ʽ��Ĭ��Ϊ0
	//0������8��д��1���������ж�д
	sScanCard.nZheRdwrMode = mUnPackData[2].m_ucRcvData[0] >> 6;
	//scan_mode_a1	6		5--0	ɨ���ģʽ��Ĭ��Ϊ0x3��
	sScanCard.nScanMode = mUnPackData[2].m_ucRcvData[0] & 0x3F;
	
	
	//4 Drive_ic_reg	16			������оƬ�ļĴ���ֵ,����������оƬ�ļĴ���ֵ
	//PWM���ģʽ,�����
	unsigned int nDriveIC = 0;
	CTool::ExchangeChar(nDriveIC,mUnPackData[4].m_ucRcvData + 5,2);
	if (_MBI5030 == sScanCard.nChipType || _TC62D722 == sScanCard.nChipType 
		||_TLC5948 == sScanCard.nChipType || _MBI5045 == sScanCard.nChipType
		|| _MBI5040 ==sScanCard.nChipType)
	{
		sScanCard.nPWMOutputMode = (nDriveIC & 0x0400) >> 10;
		sScanCard.bDotOpenDetection = (nDriveIC & 0x2000) >> 13 ? true : false;
	}
	else
	{
		sScanCard.nPWMOutputMode = 0;
		sScanCard.bDotOpenDetection = false;
	}

	//��̬ɨ�裬ˢ�±���
	if(sScanCard.nScanMode == 1 && _GENERAL != sScanCard.nChipType)
	{
		if(sScanCard.nChipType == _TC62D722)
		{
			if(sScanCard.nPWMOutputMode == 1)
				sScanCard.bMultiRefreshUnderStaticScan = true;
			else
				sScanCard.bMultiRefreshUnderStaticScan = false;
		}
		else
		{
			if(sScanCard.nPWMOutputMode == 0)
				sScanCard.bMultiRefreshUnderStaticScan = true;
			else
				sScanCard.bMultiRefreshUnderStaticScan = false;
		}
	}

	//ɨ�迨������
	nTmp = 0;
	CTool::ExchangeChar(nTmp, mUnPackData[3].m_ucRcvData + 2, 2);
	sScanCard.nScanCardWidth = nTmp / 3;

	//ɨ�迨����߶�	16	0x005f	15--0	�����������ظ߶�
	CTool::ExchangeChar(nTmp, mUnPackData[4].m_ucRcvData + 3, 2);
	sScanCard.nScanCardHeight = nTmp + 1;


	//ȷ������03�����жϿ���������Ƿ�һ��
	unsigned int nCardW16 = 0;
	CTool::ExchangeChar(nCardW16, mUnPackData[3].m_ucRcvData + 8, 2);
	unsigned int nSectionW16 = 0;
	CTool::ExchangeChar(nSectionW16, mUnPackData[3].m_ucRcvData + 14, 2);
	if((short)nSectionW16 < sScanCard.nModuleWidth)
	{
		//�����16����С��ģ���ȣ���ȷ��Ϊģ��������
		sScanCard.bExtendedEnable = true;
		sScanCard.bExtendedEnableEx = false;
		sScanCard.nSectionHorNum = nCardW16 / nSectionW16;
		sScanCard.nSectionWidth = sScanCard.nModuleWidth / sScanCard.nSectionHorNum;
	}
	else
	{
		//ģ��������������
		sScanCard.bExtendedEnable = false;
		if(nCardW16 == nSectionW16)
		{
			//����һ�£���������
			sScanCard.nSectionHorNum = 1;
			sScanCard.bExtendedEnableEx = false;
			sScanCard.nSectionWidth = sScanCard.nScanCardWidth;
			sScanCard.nCard_zone_Num = 1;
			sScanCard.nCard_zone_width = sScanCard.nScanCardWidth;
		}
		else
		{
			//��һ�£���ģ��������;
			sScanCard.bExtendedEnableEx = true;
			sScanCard.nSectionHorNum = nCardW16 / nSectionW16;
			sScanCard.nSectionWidth = sScanCard.nScanCardWidth / sScanCard.nSectionHorNum;
		}
	}

	//1�������ĺ��������ģ����
	//ģ�������� = ɨ�迨���/ģ����
	sScanCard.nModuleHorNum = sScanCard.nScanCardWidth / sScanCard.nModuleWidth;
	//ģ��������� = ɨ�迨�߶� / ģ��߶�
	sScanCard.nModuleVerNum = sScanCard.nScanCardHeight / sScanCard.nModuleHeight;

	//row_oe_clk_num
	CTool::ExchangeChar(nTmp, mUnPackData[1].m_ucRcvData + 7, 2);
	int nRowOeClkNum = nTmp + 1;
	sScanCard.nOeClkNumber = nRowOeClkNum;

	//min_oe_clk_num
	CTool::ExchangeChar(nTmp, mUnPackData[1].m_ucRcvData + 13, 2);
	int nMinOeClkNum = nTmp & 0x0FFF;

	long nAfieldClkNum = sScanCard.GetAfieldClkNum(g_GlobalVariable.nVersionType);

	//CONFIG_IC_TIME	��Ĵ�����������ʱ��(оƬ���ʱ��)�� 1-2047
	if(sScanCard.nChipType !=_GENERAL)
		sScanCard.nConfigICTime = (int) (nMinOeClkNum * pow(10.0,6)/ ((nAfieldClkNum + nRowOeClkNum) * 6.6));
	else
		sScanCard.nConfigICTime = 1024;

	//����ˢ����
	sScanCard.GetRefreshRateByReadParam(nMinOeClkNum,nAfieldClkNum,nRowOeClkNum, g_GlobalVariable.nVersionType);


	//��ɨ�迨���ذ����ɨ�迨ʵ�ʿ�Ⱥ͸߶�
	nTmp = 0;
	CTool::ExchangeChar(nTmp, mUnPackData[13].m_ucRcvData + 1, 2);
	sScanCard.nScanCardWidthReal = nTmp + 1;//ɨ�迨ʵ�ʿ��

	nTmp = 0;
	CTool::ExchangeChar(nTmp, mUnPackData[13].m_ucRcvData + 3, 2);
	sScanCard.nScanCardHeightReal = nTmp + 1;//ɨ�迨ʵ�ʸ߶�

	nTmp = 0;
	CTool::ExchangeChar(nTmp, mUnPackData[13].m_ucRcvData + 5, 2);
	rtLoad.left = nTmp;

	nTmp = 0;
	CTool::ExchangeChar(nTmp, mUnPackData[13].m_ucRcvData + 7, 2);
	rtLoad.top = nTmp;

	rtLoad.right = rtLoad.left + sScanCard.nScanCardWidthReal;
	rtLoad.bottom = rtLoad.top + sScanCard.nScanCardHeightReal;

	//��������ź���ʾ��Ĭ��Ϊ0   0x0��������0x1��������档0x2��ͼƬ
	sScanCard.nNoSingleDisp = mUnPackData[7].m_ucRcvData[0];

	//���õ���У��ʹ�ܡ�nDotCorrectTye��0 - �ޣ� 1 - ������ 2 - ��ɫ
	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�
	//Corr_en_ctrl	8	0xFF	7--0
	//bit7--4	Ԥ�������0xff
	//bit3	����У�����ܣ����漰ʹ�ܣ�ֻ�漰���ܣ�����ʱ���ϵ�Ĭ�϶�������SDRAM��0��������1���ر�
	//	bit2	1	�ӿ�FLASH����SDRAM		0	��ģ�������FLASH���ٵ�SDRAM
	//	bit1	0	����	1	��ɫ
	//	bit0	0	�ر�	1	ʹ��
	int nStart = (mUnPackData[10].m_ucRcvData[0] & 0x08) >> 3;
	if(nStart)
	{
		//�رգ���У��
		sScanCard.nDotCorrectTye = 0;
	}
	else
	{
		sScanCard.nDotCorrectTye = (mUnPackData[10].m_ucRcvData[0] & 0x02) >> 1 ? 2 : 1;
	}

	sScanCard.bEmendBrightness = mUnPackData[10].m_ucRcvData[0] & 0x01 ? true : false;

	//������ָʾ��
	sScanCard.bOpenCabinetLamp = mUnPackData[6].m_ucRcvData[0] & 0x01 ? false : true;
	
	sScanCard.nCustomGamam = 8;

	//�Ҷ���ǿ��ʽ��λ��
	nTmp = mUnPackData[9].m_ucRcvData[13];
	int nHighLowGap = nTmp & 0x3F ;

	//�Ҷ���ǿ��ʽ 0 �� 3
	sScanCard.nGrayEnhanceMode = (nTmp & 0xC0) >> 6;
	if(sScanCard.nGrayEnhanceMode)
	{
		//sScanCard.nGrayEnhance = nHighLowGap + g_GlobalVariable.nDeductBit;
		sScanCard.nGrayEnhance = nHighLowGap + sScanCard.nDeductBit;
	}
	else
	{
		sScanCard.nGrayEnhance = 0;
	}
	sScanCard.nGrayEnhanceMode = 3;
}
//���ɫ�²���
void CCLScanCardPackData::UnPackColorTempParam(map<int,CCLPackCommunicationData> & mUnPackData,
											  short & nColorTempIndex, COLOURTEMFLAG & ColorTempFlag)
{
	unsigned int nTmp = 0;
	CTool::ExchangeChar(nTmp, mUnPackData[9].m_ucRcvData + 14, 2);
	if(nTmp > 7)
	{
		nColorTempIndex = 5;
	}
	else
	{
		nColorTempIndex = nTmp;
	}

	COLOURRGB & crRGB = ColorTempFlag.colourrgb[nColorTempIndex];
	//��������R/G/B����ֵ����
	crRGB.nRed = mUnPackData[9].m_ucRcvData[0];
	crRGB.nGreen = mUnPackData[9].m_ucRcvData[1];
	crRGB.nBlue = mUnPackData[9].m_ucRcvData[2];

	//��������R/G/B����ֵ����
	crRGB.nRedLow = mUnPackData[9].m_ucRcvData[6];
	crRGB.nGreenLow = mUnPackData[9].m_ucRcvData[7];
	crRGB.nBlueLow = mUnPackData[9].m_ucRcvData[8];
	

	//������������оƬ��R/G/B�ĵ������棬
	crRGB.nICRed = mUnPackData[9].m_ucRcvData[3];
	crRGB.nICGreen = mUnPackData[9].m_ucRcvData[4];
	crRGB.nICBlue = mUnPackData[9].m_ucRcvData[5];

	//������������оƬ��R/G/B�ĵ������棬
	crRGB.nICRedLow = mUnPackData[9].m_ucRcvData[9];
	crRGB.nICGreenLow = mUnPackData[9].m_ucRcvData[10];
	crRGB.nICBlueLow = mUnPackData[9].m_ucRcvData[11];

	if(crRGB.nICBlue == 0 && crRGB.nICGreen == 0  && crRGB.nICRed == 0 )
	{
		crRGB.nICRed = mUnPackData[12].m_ucRcvData[0];
		crRGB.nICGreen = mUnPackData[12].m_ucRcvData[1];
		crRGB.nICBlue = mUnPackData[12].m_ucRcvData[2];
	
		crRGB.nICRedLow = mUnPackData[12].m_ucRcvData[3];
		crRGB.nICGreenLow = mUnPackData[12].m_ucRcvData[4];
		crRGB.nICBlueLow = mUnPackData[12].m_ucRcvData[5];

	}

	//Gain_bypass 6 0x3f
	//bit5	����������ɫ����ֵ������·
	//bit4	����������ɫ����ֵ������·
	//bit3	�������ĺ�ɫ����ֵ������·
	//bit2	����������ɫ����ֵ������·
	//bit1	����������ɫ����ֵ������·
	//bit0	�������ĺ�ɫ����ֵ������·
	if (mUnPackData[9].m_ucRcvData[12] & 0x01)
		++crRGB.nRed;
	if (mUnPackData[9].m_ucRcvData[12] & 0x02)
		++crRGB.nGreen;
	if (mUnPackData[9].m_ucRcvData[12] & 0x04)
		++crRGB.nBlue;

	if (mUnPackData[9].m_ucRcvData[12] & 0x08)
		++crRGB.nRedLow;
	if (mUnPackData[9].m_ucRcvData[12] & 0x10)
		++crRGB.nGreenLow;
	if (mUnPackData[9].m_ucRcvData[12] & 0x20)
		++crRGB.nBlueLow;
}

///////////////////////////////////////////////////////////////////////////////////////////////

//��ɨ�迨���������ݰ������� int - �����ȣ�6������
int CCLScanCardPackData::Pack6ScanCardData(int nScanCardAddress,//ɨ�迨��ַ
										CStructSingleScanCard & sScanCard, //ɨ�迨�����ṹ
										unsigned char* ucSendData,//��������
										int nEmptyByte)//У��ǰĬ�Ͽ��ֽ���
{
	memset(ucSendData, 0, CL_MAX_BUF_NUMBER);

	int nLength = 0;
	unsigned char uSendData[CL_SEND_PACK_SIZE];

	int nMax;
	switch (sScanCard.nChipType)
	{
	case _MBI5153_E:
		nMax = 7; //���Ͱ�00,01,02,03,04,05,15
		break;
	case _MBI5155:
		nMax = 8; //���Ͱ�00,01,02,03,04,05,15,16
		break;
	default:
		nMax = 6; //���Ͱ�00,01,02,03,04,05
		break;
	}
	/*
	if (_SP10Version == g_GlobalVariable.nVersionType)
	{
		nMax++;//��Ƶ����ռ�ձ����� ���Ͱ�0x17
	}*/

	for (int n = 0; n < nMax; n ++)
	{

		memset(uSendData,0,sizeof(uSendData));

		//int nPackID = ((n == nMax - 1) && (_SP10Version == g_GlobalVariable.nVersionType)) ? 0x17 : n;
		int nLen = PackScanCardData(0,nScanCardAddress, n ,sScanCard,uSendData,nEmptyByte);

		memcpy(ucSendData + nLength,uSendData,nLen);

		nLength += nLen;
	}

	return nLength;
}

//�����ź���ʾ��
int CCLScanCardPackData::PackNoSingleDisp(short nAddress,
						short nNoSingleDisp,
						unsigned char * ucSendData)
{
	unsigned char ucCorrect[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucCorrect, 0, CL_SEND_EFFECT_DATA_SIZE);
	//���ź���ʾ��Ĭ��Ϊ0   0x0��������0x1��������档0x2��ͼƬ
	ucCorrect[0] = (unsigned char)nNoSingleDisp;

	int nLength = CCLPackCommunicationData::PackDataBase(0,nAddress,0x40,7,false,ucCorrect,ucSendData);

	return nLength;
}

//���㴦���
int CCLScanCardPackData::PackOperationProcessing(short nAddress,
						short nScancardSectionRowNumber,
						unsigned char * ucSendData)
{
	//��ʾ����/������������0-255��������1-32��������ȷ���Ͳ��䣩��
	//��0-255���δ��롣ע�⣺��λ���Ȳ����������ٲ����̡�ɨ�迨��˳��д��DPRAM��

	unsigned char ucOperationPro[CL_MAX_BUF_NUMBER];
	memset(ucOperationPro, 0, sizeof(CL_MAX_BUF_NUMBER));

	for (int n = 0; n < 256; n ++)
	{
		ucOperationPro[n * 2] = n % nScancardSectionRowNumber;//����
		ucOperationPro[n * 2 + 1] = n /nScancardSectionRowNumber;//��
	}

	memset(ucSendData, 0, CL_MAX_BUF_NUMBER);
	int nLength = 0;
	unsigned char uSendData[CL_SEND_PACK_SIZE];

	int nLop = 256 * 2 / CL_SEND_EFFECT_DATA_SIZE;

	for (int n = 0; n < nLop; ++n)
	{
		memset(uSendData,0,sizeof(uSendData));

		int nLen = CCLPackCommunicationData::PackDataBase(0,nAddress,0x9B,n,false,
									ucOperationPro + n * CL_SEND_EFFECT_DATA_SIZE,uSendData);

		memcpy(ucSendData + nLength,uSendData,nLen);

		nLength += nLen;
	}

	return nLength;
}

//��Ƶ�����
int CCLScanCardPackData::PackVideoProcessing(short nAddress,
						CStructSingleScanCard & sScanCard,
						unsigned char * ucSendData)
{
	//��Ƶ�������ݻ���
	unsigned char ucVideoProcessing[CL_MAX_BUF_NUMBER];
	memset(ucVideoProcessing, 0, sizeof(ucVideoProcessing));

	//��Ƶ�������ݳ���
	int nDataLen = 0;

	//ԭ��1����ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ
	//ԭ��2��˳���ȡ16�����ص�R,G,B,RA��
	if (sScanCard.nDataInputDir == 0)//������
	{
		if (sScanCard.nScreenType == DISPLAY_TYPE_REAL)//ʵ����
		{
			if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//����
			{
				//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬
				//�ȶ�ȡ���һ��16�����ص�R�������һ��16�����ص�G�������һ��16�����ص�B��
				//���ȡ�����ڶ���16�����ص�R���ٵ����ڶ���16�����ص�G���ٵ����ڶ���16�����ص�B��
				//�������ƣ�������16������ڿ���
				//��Ƶ���ݳ��ȣ������ճ�16�ı�����* 3 * 2�ֽ�
				int nLop = sScanCard.nScanCardWidth % 16 ?
					sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;
				int nTemp = 0;

				for (int i = 0 ; i < nLop; i ++)
				{
					for (int m = 0; m < 3; m ++)
					{
						for (int n = 0; n < 16; n ++ )
						{
							nTemp = nLop * 16 * 3 - ( i * 16 * 3 + n * 3 + 3 - m);
							memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
							nDataLen += 2;
						}
					}
				}

			}
			else if (sScanCard.nDotCorrectTye == 2)//��ɫ
			{
				//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬
				//�ȶ�ȡ��һ��16�����ص�R���ٵ�һ��16�����ص�G���ٵ�һ��16�����ص�B��
				//���ȡ�ڶ���16�����ص�R���ٵڶ���16�����ص�G���ٵڶ���16�����ص�B��
				//�������ƣ�������16������ڿ���
				//��Ƶ���ݳ��ȣ������ճ�16�ı�����* 3 * 3 * 2�ֽ�
				int nLop = sScanCard.nScanCardWidth % 16 ?
					sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;
				int nTemp = 0;

				for (int i = 0; i < nLop; i ++)
				{
					for (int j = 0; j < 3; j ++)
					{
						for (int n = 0; n < 16; n ++ )
						{
							for (int m = 0; m < 3; m ++)
							{
								nTemp = nLop * 16 * 3 - ( i * 16 * 3 + n * 3 + 3 - m);
								memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
								nDataLen += 2;
							}
						}
					}
				}
			}
		}
		else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//����
		{
			if (sScanCard.bVirtvalDisp)//������ʾʱ���������
			{
				if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//����
				{
					//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬
					//��������һ�����ƶ�Ӧ���ϡ����ϡ����¡������ĸ���Ƶ��ַ����һ�ж�Ӧ������Ƶ���ݵ�ַ
					//�ȶ�ȡ���һ��16�����ص�R�������һ��16�����ص�G�������һ��16�����ص�B�������һ��16�����ص�RA
					//���ȡ�����ڶ���16�����ص�R���ٵ����ڶ���16�����ص�G���ٵ����ڶ���16�����ص�B���ٵ����ڶ���16�����ص�RA
					//�������ƣ�������16������ڿ���
					//ÿ��RGB��Ӧ���ϡ����ϡ����¡������ĸ���Ƶ��ַ��ÿ����Ƶ��ַ������RGB
					//��Ƶ���ݳ��ȣ������ճ�16�ı�����* 4 * 4 * 2�ֽ�
					//���磺
					int nScanCardWidth = sScanCard.nScanCardWidth * 2 + 1;


					int nLop = ( sScanCard.nScanCardWidth  % 16 ?
						(sScanCard.nScanCardWidth / 16 + 1 ) * 16 : sScanCard.nScanCardWidth ) / 16;

					int nTemp = 0;


					for (int i = 0; i < nLop; i ++)
					{
						for (int m = 0; m < 4; m ++)
						{
							for (int n = 0; n < 16; n ++ )
							{
								switch (m)
								{
								case 0://R
									nTemp = nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 9 -  m);//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp =  nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 6 -  m);//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp =  2 * nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 9 -  m);//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = 2 * nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 6 -  m);//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
									break;
								case 1://G
									nTemp = nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 6 -  m);//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 3 -  m);//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = 2 * nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 6 -  m);//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = 2 * nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 3 -  m);//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
									break;
								case 2://B
								case 3://RA
									nTemp = 2 * nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 9 -  m);//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp =  2 * nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 6 -  m);//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp =  3 * nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 9 -  m);//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = 3 * nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 6 -  m);//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
									break;
								default:
									break;
								}
							}
						}
					}
				}
				else if (sScanCard.nDotCorrectTye == 2)//��ɫ
				{
				}
			}
			else//��������ʾʱ��ʵ���ز���
			{
				if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//����
				{
					//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬
					//�ȶ�ȡ���һ��16�����ص�R�������һ��16�����ص�G�������һ��16�����ص�B��
					//���ȡ�����ڶ���16�����ص�R���ٵ����ڶ���16�����ص�G���ٵ����ڶ���16�����ص�B��
					//�������ƣ�������16������ڿ���
					//��Ƶ���ݳ��ȣ������ճ�16�ı�����* 3 * 2�ֽ�
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;
					int nTemp = 0;

					for (int i = 0 ; i < nLop; i ++)
					{
						for (int m = 0; m < 3; m ++)
						{
							for (int n = 0; n < 16; n ++ )
							{
								nTemp = nLop * 16 * 3 - ( i * 16 * 3 + n * 3 + 3 - m);
								memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
								nDataLen += 2;
							}
						}
					}

				}
				else if (sScanCard.nDotCorrectTye == 2)//��ɫ
				{
					//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬
					//�ȶ�ȡ��һ��16�����ص�R���ٵ�һ��16�����ص�G���ٵ�һ��16�����ص�B��
					//���ȡ�ڶ���16�����ص�R���ٵڶ���16�����ص�G���ٵڶ���16�����ص�B��
					//�������ƣ�������16������ڿ���
					//��Ƶ���ݳ��ȣ������ճ�16�ı�����* 3 * 3 * 2�ֽ�
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;
					int nTemp = 0;

					for (int i = 0; i < nLop; i ++)
					{
						for (int j = 0; j < 3; j ++)
						{
							for (int n = 0; n < 16; n ++ )
							{
								for (int m = 0; m < 3; m ++)
								{
									nTemp = nLop * 16 * 3 - ( i * 16 * 3 + n * 3 + 3 - m);
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
								}
							}
						}
					}
				}
			}
		}
	}
	else if (sScanCard.nDataInputDir == 1)//���ҵ���
	{
		if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && !sScanCard.bVirtvalDisp))//ʵ���ػ�������ʵ������ʾ
		{
			if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//����
			{
				if(!sScanCard.bExtendedEnable && !sScanCard.bExtendedEnableEx)
				{
					//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬
					//�ȶ�ȡ��һ��16�����ص�R���ٵ�һ��16�����ص�G���ٵ�һ��16�����ص�B��
					//���ȡ�ڶ���16�����ص�R���ٵڶ���16�����ص�G���ٵڶ���16�����ص�B��
					//�������ƣ�������16������ڿ���
					//��Ƶ���ݳ��ȣ������ճ�16�ı�����* 3 * 2�ֽ�
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;
					int nTemp = 0;

					for (int i = 0; i < nLop; i ++)
					{
						for (int m = 0; m < 3; m ++)
						{
							for (int n = 0; n < 16; n ++ )
							{
								nTemp = i * 16 * 3 + n * 3 + m;
								memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
								nDataLen += 2;
							}
						}
					}
				}
				else
				{
					int nLop = sScanCard.nCard_zone_width % 16 ?
						sScanCard.nCard_zone_width / 16 + 1 : sScanCard.nCard_zone_width / 16;
					for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //��������ѭ��
					{
						for (int u = 0; u < sScanCard.nSectionHorNum; ++u)//ģ����������
						{
							for (int i = 0; i < nLop; ++i)
							{
								for (int m = 0; m < 3; ++m)//������ɫ
								{
									for (int n = 0; n < 16; ++n )
									{
										//����������ģ������
										int index1 = (i * 16 + n) / sScanCard.nSectionWidth;
										//����ģ���������ص�����ģ���λ��
										int index2 = (i * 16 + n) % sScanCard.nSectionWidth;
										//����ƫ��
										int nZoneOffset = ((index1 * sScanCard.nSectionHorNum) + u) * sScanCard.nSectionWidth * 3;
										//������ƫ��
										int nZoneDot = v * sScanCard.nCard_zone_width * sScanCard.nSectionHorNum * 3;
										int nTemp = nZoneOffset + index2 * 3 + m + nZoneDot;

#ifdef M3_5
										int nEmptyDot = sScanCard.nScanCardWidth - sScanCard.nScanCardWidthReal;
										nTemp = (nTemp - nEmptyDot * 3) > 0 ? (nTemp - nEmptyDot * 3) : 0;
#endif

										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;
									}
								}
							}
						}
					}
				}
			}
			else if (sScanCard.nDotCorrectTye == 2)//��ɫ
			{
				//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬
				//�ȶ�ȡ��һ��16�����ص�R��Ӧ��RGB���ٵ�һ��16�����ص�G��Ӧ��RGB���ٵ�һ��16�����ص�B��Ӧ��RGB��
				//���ȡ�ڶ���16�����ص�R��Ӧ��RGB���ٵڶ���16�����ص�G��Ӧ��RGB���ٵڶ���16�����ص�B��Ӧ��RGB��
				//�������ƣ�������16������ڿ���
				//��Ƶ���ݳ��ȣ������ճ�16�ı�����* 3 * 3 * 2�ֽ�
				//�൱��ÿ�����ݶ�ȡ����
				if (!sScanCard.bExtendedEnable && !sScanCard.bExtendedEnableEx)
				{

					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;
					int nTemp = 0;

					for (int i = 0; i < nLop; i ++)
					{
						for (int m = 0; m < 3; m ++)
						{
							for (int n = 0; n < 16 * 3; n ++ )
							{
								nTemp = n + i * 16 * 3;
								memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
								nDataLen += 2;
							}
						}
					}
				}
				else
				{
					//����
					int nLop = sScanCard.nCard_zone_width % 16 ?
						sScanCard.nCard_zone_width / 16 + 1 : sScanCard.nCard_zone_width / 16;
					for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //��������ѭ��
					{
						for(int u = 0; u < sScanCard.nSectionHorNum; ++u)//ģ����������
						{
							for (int i = 0; i < nLop; i ++)
							{
								for (int m = 0; m < 3; m ++)
								{
									for (int n = 0; n < 16 * 3; n ++ )
									{
										//����������ģ������
										int index1 = (i * 16 * 3 + n) / (sScanCard.nSectionWidth * 3);
										//����ģ���������ص�����ģ���λ��
										int index2 = (i * 16 * 3 + n) % (sScanCard.nSectionWidth * 3);
										//����ƫ��
										int nZoneOffset = ((index1 * sScanCard.nSectionHorNum) + u) * sScanCard.nSectionWidth * 3;
										//������ƫ��
										int nZoneDot = v * sScanCard.nCard_zone_width * sScanCard.nSectionHorNum * 3;

										int nTemp = nZoneDot + nZoneOffset + index2;

#ifdef M3_5
										int nEmptyDot = sScanCard.nScanCardWidth - sScanCard.nScanCardWidthReal;
										nTemp = (nTemp - nEmptyDot * 3) > 0 ? (nTemp - nEmptyDot * 3) : 0;
#endif

										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;
									}
								}
							}
						}
					}

				}
			}
		}//ʵ���ػ�������ʵ������ʾ
		else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && sScanCard.bVirtvalDisp)//������������ʾ���������
		{
			if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//����
			{
				//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬
				//��������һ�����ƶ�Ӧ���ϡ����ϡ����¡������ĸ���Ƶ��ַ����һ�ж�Ӧ������Ƶ���ݵ�ַ
				//�ȶ�ȡ��һ��16�����ص�R���ٵ�һ��16�����ص�G���ٵ�һ��16�����ص�B���ٵ�һ��16�����ص�RA
				//���ȡ�ڶ���16�����ص�R���ٵڶ���16�����ص�G���ٵڶ���16�����ص�B���ٵڶ���16�����ص�RA
				//�������ƣ�������16������ڿ���
				//ÿ��RGB��Ӧ���ϡ����ϡ����¡������ĸ���Ƶ��ַ��ÿ����Ƶ��ַ������RGB
				//��Ƶ���ݳ��ȣ������ճ�16�ı�����* 4 * 4 * 2�ֽ�
				//���磺
				if (0 == sScanCard.nVirTualArray)//0����A,��/��,��B
				{
					int nScanCardWidth = sScanCard.nScanCardWidth * 2 + 1;
					int nLop = ( sScanCard.nScanCardWidth  % 16 ?
						(sScanCard.nScanCardWidth / 16 + 1 ) * 16 : sScanCard.nScanCardWidth ) / 16;

					int nTemp = 0;

					for (int i = 0; i < nLop; i ++)
					{
						for (int m = 0; m < 4; m ++)//R G B RA
						{
							for (int n = 0; n < 16; n ++ )
							{
								switch (m)
								{
								case 0://R
									nTemp = i * 16 * 6 + n * 6 + m;//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 + 3 + m;//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 +  nScanCardWidth * 3 + m;//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 + 3 + m;//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
									break;
								case 1://G
									nTemp = i * 16 * 6 + n * 6 + 3 + m;//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 + 3 + 3 + m;//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 + 3 + nScanCardWidth * 3 + m;//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 + 3 + nScanCardWidth * 3 + 3 + m;//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
									break;
								case 2://B
									nTemp = i * 16 * 6 + n * 6 +  nScanCardWidth * 3 + m;//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 + 3 +  nScanCardWidth * 3 + m;//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 +  nScanCardWidth * 3 * 2 + m;//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * 2 + 3 + m;//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
									break;
								case 3://RA
									nTemp = i * 16 * 6 + n * 6 +  nScanCardWidth * 3 + m;//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 +  nScanCardWidth * 3 + 3 + m;//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 +  nScanCardWidth * 3 * 2 + m;//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * 2 + 3 + m;//����
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
									break;
								default:
									break;
								}
							}
						}
					}
				} //end if (0 == sScanCard.nVirTualArray)//0:��A,��/��,��B
				else if (1 == sScanCard.nVirTualArray	//1:��,��/��,��;��,��/��,��
					|| 2 == sScanCard.nVirTualArray		//2:��,��/��,��;��,��/��,��
					|| 3 == sScanCard.nVirTualArray)	//3:��,��/��,��;��,��/��,��
				{
					int nScanCardWidth = sScanCard.nScanCardWidth * 2 + 1;
					int nLop = sScanCard.nCard_zone_width % 16 ?
						sScanCard.nCard_zone_width / 16 + 1 : sScanCard.nCard_zone_width / 16;

					int nTemp = 0;

					for (int k = 0; k < 2; k++) //������, ����һ����Ҫ����������
					{
						for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //��������ѭ��
						{
							for (int u = 0; u < sScanCard.nSectionHorNum; ++u)//ģ����������
							{
								for (int i = 0; i < nLop; i ++)
								{
									for (int m = 0; m < 3; m ++)//R G B
									{
										int nIndex = GetIndex(m, sScanCard.nVirTualArray, k);//����ƣ������ص㣩��Ӧ�����λ�ã�0���ϣ�1����, 2���£�3����
										for (int n = 0; n < 16; n ++ )
										{
											//����������ģ������
											int index1 = (i * 16 + n) / sScanCard.nSectionWidth;
											//����ģ���������ص�����ģ���λ��
											int index2 = (i * 16 + n) % sScanCard.nSectionWidth;
											//����ƫ��
											int nZoneOffset = ((index1 * sScanCard.nSectionHorNum) + u) * sScanCard.nSectionWidth * 6;
											//������ƫ��
											int nZoneDot = v * sScanCard.nCard_zone_width * sScanCard.nSectionHorNum * 6;

											int nOffset = nZoneOffset + index2 * 6 + nZoneDot;

											switch (nIndex)
											{
											case 0:
												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (0 + 2 * k) + m;//����
												nTemp = nOffset +  nScanCardWidth * 3 * (0 + 2 * k) + m;//����
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (0 + 2 * k) + 3 + m;//����
												nTemp = nOffset + nScanCardWidth * 3 * (0 + 2 * k) + m + 3;//����
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (1 + 2 * k) + m;//����
												nTemp = nOffset + nScanCardWidth * 3 * (1 + 2 * k) + m;//����
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (1 + 2 * k) + 3 + m;//����
												nTemp = nOffset + nScanCardWidth * 3 * (1 + 2 * k) + m + 3;//����
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;
												break;
											case 1:
												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (0 + 2 * k) + 3 + m;//����
												nTemp = nOffset + nScanCardWidth * 3 * (0 + 2 * k) + 3 + m;//����
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (0 + 2 * k) + 3 + 3 + m;//����
												nTemp = nOffset + nScanCardWidth * 3 * (0 + 2 * k) + 3 + 3 + m;//����
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (1 + 2 * k) + 3 + m;//����
												nTemp = nOffset + nScanCardWidth * 3 * (1 + 2 * k) + 3 + m;//����
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (1 + 2 * k) + 3 + 3 + m;//����
												nTemp = nOffset + nScanCardWidth * 3 * (1 + 2 * k) + 3 + 3 + m;//����
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;
												break;
											case 2:
												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (1 + 2 * k) + m;//����
												nTemp = nOffset + nScanCardWidth * 3 * (1 + 2 * k) + m;//����
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (1 + 2 * k) + 3 + m;//����
												nTemp = nOffset + nScanCardWidth * 3 * (1 + 2 * k) + 3 + m;//����
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (2 + 2 * k) + m;//����
												nTemp = nOffset + nScanCardWidth * 3 * (2 + 2 * k) + m;//����
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (2 + 2 * k) + 3 + m;//����
												nTemp = nOffset + nScanCardWidth * 3 * (2 + 2 * k) + 3 + m;//����
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;
												break;
											case 3:
												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (1 + 2 * k) + 3 + m;//����
												nTemp = nOffset + nScanCardWidth * 3 * (1 + 2 * k) + 3 + m;//����
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (1 + 2 * k) + 3 + 3 + m;//����
												nTemp = nOffset + nScanCardWidth * 3 * (1 + 2 * k) + 3 + 3 + m;//����
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (2 + 2 * k) + 3 + m;//����
												nTemp = nOffset + nScanCardWidth * 3 * (2 + 2 * k) + 3 + m;//����
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (2 + 2 * k) + 3 + 3 + m;//����
												nTemp = nOffset + nScanCardWidth * 3 * (2 + 2 * k) + 3 + 3 + m;//����
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;
												break;
											default:
												break;
											}
										}
									}
								}
							}//end for (int u = 0; u < sScanCard.nSectionHorNum; ++u)//ģ����������
						}//end for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //��������ѭ��
					}//end for (int l = 0; l < 2; l++) //2������
				}
			}
			else if (sScanCard.nDotCorrectTye == 2)//��ɫ
			{
			}
		}//������������ʾ
	}
	else if (sScanCard.nDataInputDir == 2 ||sScanCard.nDataInputDir == 3)//���½���
	{
		//ʹ�����ź�D��Ϊ�ڶ�·ʱ��ʹ���ظ߶ȼӱ�:��������ʹ�ã�����
		//ÿ�������ɴ��ؼӱ���������
		//���½��ߺ����ҽ��ߵĲ�ͬ�������ҽ����ǰ��黮�֣���ȡ16���������صĵ�ַ��
		//���½������ǰ������֣���ȡ16������Ӧλ�����ص�ַ
		int nDCBLine = (int)pow(2.0, sScanCard.nDCBlineClkEn);
		if (sScanCard.nScreenType == DISPLAY_TYPE_REAL)//ʵ����
		{
			if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//����
			{
				//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬һ�����ذ���ÿ������Ӧλ�����أ�
				//������16�����㣬������16�İ�Ĭ�ϵ�ַȡ
				//��1�����ݣ���һ����һ��R���ڶ�����һ��R....��ʮ������һ��R
				//��2�����ݣ���һ����һ��G���ڶ�����һ��G....��ʮ������һ��G
				//��3�����ݣ���һ����һ��B���ڶ�����һ��B....��ʮ������һ��B
				//��4�����ݣ���һ���ڶ���R���ڶ����ڶ���R....��ʮ�����ڶ���R
				//��5�����ݣ���һ���ڶ���G���ڶ����ڶ���G....��ʮ�����ڶ���G
				//��6�����ݣ���һ���ڶ���B���ڶ����ڶ���B....��ʮ�����ڶ���B
				//�������ƣ�������16������ڿ���
				//��Ƶ���ݳ��ȣ�ÿ������ * ���ظ߶ȼӱ����� * 16 * 3 * 2�ֽ�

				int nTemp = 0;

				for (int i = 0; i <sScanCard.nScanCardSectionRowNumber * nDCBLine; i ++)
				{
					for (int m = 0; m < 3; m ++)
					{
						for (int n = 0; n < 16; n ++ )
						{
							nTemp = sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 * n + m + i * 3;
							memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
							nDataLen += 2;
						}
					}
				}

			}
			else if (sScanCard.nDotCorrectTye == 2)//��ɫ
			{
				//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬һ�����ذ���ÿ������Ӧλ�����أ�
				//������16�����㣬������16�İ�Ĭ�ϵ�ַȡ
				//��1�����ݣ���һ����һ��R��Ӧ��RGB���ڶ�����һ��R��Ӧ��RGB....��ʮ������һ��R��Ӧ��RGB
				//��2�����ݣ���һ����һ��G��Ӧ��RGB���ڶ�����һ��G��Ӧ��RGB....��ʮ������һ��G��Ӧ��RGB
				//��3�����ݣ���һ����һ��B��Ӧ��RGB���ڶ�����һ��B��Ӧ��RGB....��ʮ������һ��B��Ӧ��RGB
				//��4�����ݣ���һ���ڶ���R��Ӧ��RGB���ڶ����ڶ���R��Ӧ��RGB....��ʮ�����ڶ���R��Ӧ��RGB
				//��5�����ݣ���һ���ڶ���G��Ӧ��RGB���ڶ����ڶ���G��Ӧ��RGB....��ʮ�����ڶ���G��Ӧ��RGB
				//��6�����ݣ���һ���ڶ���B��Ӧ��RGB���ڶ����ڶ���B��Ӧ��RGB....��ʮ�����ڶ���B��Ӧ��RGB
				//�������ƣ�������16������ڿ���
				//��Ƶ���ݳ��ȣ�ÿ������ * ���ظ߶ȼӱ����� * 16 * 3 * 3 * 2�ֽ�
				//�൱��ÿ�����ݶ�ȡ����

				int nTemp = 0;

				for (int i = 0; i <sScanCard.nScanCardSectionRowNumber * nDCBLine; i ++)
				{
					for (int j = 0; j < 3; j ++)
					{
						for (int n = 0; n < 16; n ++ )
						{
							for (int m = 0; m < 3; m ++)
							{
								nTemp = sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 * n + m + i * 3;
								memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
								nDataLen += 2;
							}
						}
					}
				}
			}
		}
		else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//������
		{
			if (sScanCard.bVirtvalDisp)//������ʾ���������
			{
				if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//����
				{

					//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬һ�����ذ���ÿ������Ӧλ�����أ�
					//��������һ�����ƶ�Ӧ���ϡ����ϡ����¡������ĸ���Ƶ��ַ����һ�ж�Ӧ������Ƶ���ݵ�ַ
					//������16�����㣬������16�İ�Ĭ�ϵ�ַȡ
					//��1�����ݣ���һ����һ��R��Ӧ�����ϡ����ϡ����¡����µ�R���ڶ�����һ��R��Ӧ�����ϡ����ϡ����¡����µ�R....��ʮ������һ��R��Ӧ�����ϡ����ϡ����¡����µ�R
					//��2�����ݣ���һ����һ��G��Ӧ�����ϡ����ϡ����¡����µ�G���ڶ�����һ��G��Ӧ�����ϡ����ϡ����¡����µ�G....��ʮ������һ��G��Ӧ�����ϡ����ϡ����¡����µ�G
					//��3�����ݣ���һ����һ��B��Ӧ�����ϡ����ϡ����¡����µ�B���ڶ�����һ��B��Ӧ�����ϡ����ϡ����¡����µ�B....��ʮ������һ��B��Ӧ�����ϡ����ϡ����¡����µ�B
					//��4�����ݣ���һ����һ��RA��Ӧ�����ϡ����ϡ����¡����µ�R���ڶ�����һ��R��Ӧ�����ϡ����ϡ����¡����µ�R....��ʮ������һ��R��Ӧ�����ϡ����ϡ����¡����µ�R
					//��5�����ݣ���һ���ڶ���R��Ӧ�����ϡ����ϡ����¡����µ�R���ڶ����ڶ���R��Ӧ�����ϡ����ϡ����¡����µ�R....��ʮ�����ڶ���R��Ӧ�����ϡ����ϡ����¡����µ�R
					//��6�����ݣ���һ���ڶ���G��Ӧ�����ϡ����ϡ����¡����µ�G���ڶ����ڶ���G��Ӧ�����ϡ����ϡ����¡����µ�G....��ʮ�����ڶ���G��Ӧ�����ϡ����ϡ����¡����µ�G
					//��7�����ݣ���һ���ڶ���B��Ӧ�����ϡ����ϡ����¡����µ�B���ڶ����ڶ���B��Ӧ�����ϡ����ϡ����¡����µ�B....��ʮ�����ڶ���B��Ӧ�����ϡ����ϡ����¡����µ�B
					//��8�����ݣ���һ����һ��RA��Ӧ�����ϡ����ϡ����¡����µ�R���ڶ�����һ��R��Ӧ�����ϡ����ϡ����¡����µ�R....��ʮ������һ��R��Ӧ�����ϡ����ϡ����¡����µ�R
					//�������ƣ�������16������ڿ���
					//��Ƶ���ݳ��ȣ�ÿ������ * ���ظ߶ȼӱ����� * 16 * 4 * 4 * 2�ֽ�
					int nScanCardWidth = sScanCard.nScanCardWidth * 2 + 1;


					int nLop = ( sScanCard.nScanCardWidth  % 16 ?
						(sScanCard.nScanCardWidth / 16 + 1 ) * 16 : sScanCard.nScanCardWidth ) / 16;

					int nTemp = 0;

					for (int i = 0; i < nLop; i ++)
						for (int j = 0; j < sScanCard.nScanCardSectionRowNumber * nDCBLine; j ++)
							for (int m = 0; m < 4; m ++)
								for (int n = 0; n < 16; n ++ )
								{
									switch (m)
									{
									case 0://R
										nTemp = i * 16 * 6 + n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + m;//����
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + 3 + m;//����
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 + n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 +  nScanCardWidth * 3 + m;//����
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + nScanCardWidth * 3 + 3 + m;//����
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;
										break;
									case 1://G
										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber *nDCBLine * 3 + j * 6 + 3 + m;//����
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 + n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + 3 + 3 + m;//����
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + 3 + nScanCardWidth * 3 + m;//����
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + 3 + nScanCardWidth * 3 + 3 + m;//����
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;
										break;
									case 2://B
										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 +  nScanCardWidth * 3 + m;//����
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + 3 +  nScanCardWidth * 3 + m;//����
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 +  nScanCardWidth * 3 * 2 + m;//����
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + nScanCardWidth * 3 * 2 + 3 + m;//����
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;
										break;
									case 3://RA
										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 +  nScanCardWidth * 3 + m;//����
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 +  nScanCardWidth * 3 + 3 + m;//����
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 +  nScanCardWidth * 3 * 2 + m;//����
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + nScanCardWidth * 3 * 2 + 3 + m;//����
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;
										break;
									default:
										break;
									}
								}
				}
				else if (sScanCard.nDotCorrectTye == 2)//��ɫ
				{
				}
			}
			else//��������ʾ��ʵ���ز���
			{
				if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//����
				{
					//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬һ�����ذ���ÿ������Ӧλ�����أ�
					//������16�����㣬������16�İ�Ĭ�ϵ�ַȡ
					//��1�����ݣ���һ����һ��R���ڶ�����һ��R....��ʮ������һ��R
					//��2�����ݣ���һ����һ��G���ڶ�����һ��G....��ʮ������һ��G
					//��3�����ݣ���һ����һ��B���ڶ�����һ��B....��ʮ������һ��B
					//��4�����ݣ���һ���ڶ���R���ڶ����ڶ���R....��ʮ�����ڶ���R
					//��5�����ݣ���һ���ڶ���G���ڶ����ڶ���G....��ʮ�����ڶ���G
					//��6�����ݣ���һ���ڶ���B���ڶ����ڶ���B....��ʮ�����ڶ���B
					//�������ƣ�������16������ڿ���
					//��Ƶ���ݳ��ȣ�ÿ������ * ���ظ߶ȼӱ����� * 16 * 3 * 2�ֽ�

					int nTemp = 0;

					for (int i = 0; i <sScanCard.nScanCardSectionRowNumber * nDCBLine; i ++)
						for (int m = 0; m < 3; m ++)
							for (int n = 0; n < 16; n ++ )
							{
								nTemp = sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 * n + m + i * 3;
								memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
								nDataLen += 2;
							}

				}
				else if (sScanCard.nDotCorrectTye == 2)//��ɫ
				{
					//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬һ�����ذ���ÿ������Ӧλ�����أ�
					//������16�����㣬������16�İ�Ĭ�ϵ�ַȡ
					//��1�����ݣ���һ����һ��R��Ӧ��RGB���ڶ�����һ��R��Ӧ��RGB....��ʮ������һ��R��Ӧ��RGB
					//��2�����ݣ���һ����һ��G��Ӧ��RGB���ڶ�����һ��G��Ӧ��RGB....��ʮ������һ��G��Ӧ��RGB
					//��3�����ݣ���һ����һ��B��Ӧ��RGB���ڶ�����һ��B��Ӧ��RGB....��ʮ������һ��B��Ӧ��RGB
					//��4�����ݣ���һ���ڶ���R��Ӧ��RGB���ڶ����ڶ���R��Ӧ��RGB....��ʮ�����ڶ���R��Ӧ��RGB
					//��5�����ݣ���һ���ڶ���G��Ӧ��RGB���ڶ����ڶ���G��Ӧ��RGB....��ʮ�����ڶ���G��Ӧ��RGB
					//��6�����ݣ���һ���ڶ���B��Ӧ��RGB���ڶ����ڶ���B��Ӧ��RGB....��ʮ�����ڶ���B��Ӧ��RGB
					//�������ƣ�������16������ڿ���
					//��Ƶ���ݳ��ȣ�ÿ������ * ���ظ߶ȼӱ����� * 16 * 3 * 3 * 2�ֽ�
					//�൱��ÿ�����ݶ�ȡ����

					int nTemp = 0;

					for (int i = 0; i <sScanCard.nScanCardSectionRowNumber * nDCBLine; i ++)
						for (int j = 0; j < 3; j ++)
							for (int n = 0; n < 16; n ++ )
								for (int m = 0; m < 3; m ++)
								{
									nTemp = sScanCard.nScanCardSectionRowNumber * nDCBLine* 3 * n + m + i * 3;
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
								}
				}
			}
		}
	}
	int nLop = nDataLen / CL_SEND_EFFECT_DATA_SIZE;

	memset(ucSendData, 0, CL_MAX_BUF_NUMBER);
	int nLength = 0;
	unsigned char uSendData[CL_SEND_PACK_SIZE];

	for (int n = 0; n < nLop; ++n)
	{

		memset(uSendData,0,sizeof(uSendData));

		int nLen = CCLPackCommunicationData::PackDataBase(0,nAddress,0x9A,n,false,
								ucVideoProcessing + n * CL_SEND_EFFECT_DATA_SIZE,uSendData);

		memcpy(ucSendData + nLength,uSendData,nLen);

		nLength += nLen;
	}

	return nLength;
}

//���ö�ȡУ�����ݵ�ַ��
int CCLScanCardPackData::PackCorrectProcessing(short nAddress,
						CStructSingleScanCard & sScanCard,
						unsigned char * ucSendData)
{
	//��У��ʱ������У�����ݵ�ַ��
	if (sScanCard.nDotCorrectTye == 0)
		return -1;

	//��Ƶ�������ݻ���
	unsigned char ucCorrectProcessing[CL_MAX_BUF_NUMBER];
	memset(ucCorrectProcessing, 0, sizeof(ucCorrectProcessing));

	//��Ƶ�������ݳ���
	int nDataLen = 0;

	//ԭ��1����ȡ��һ�ж�Ӧ��У�����ݵ�ַ
	//ԭ��2��˳���ȡ16�����ص�R,G,B,RA��
	//ԭ��3��������Ч����


	//��ЧУ��������
	//������ʵ��ģ��*4
	//ʵ���ص����ɫ��ʵ��ģ��*9

	int nOneRowCoefNum = 0x017F;//��ЧУ������
	if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//����
	{
		nOneRowCoefNum = sScanCard.nModuleWidth * 4;

		if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && sScanCard.bVirtvalDisp
			&& (1 == sScanCard.nVirTualArray || 2 == sScanCard.nVirTualArray || 3 == sScanCard.nVirTualArray))
		{
			nOneRowCoefNum = sScanCard.nModuleWidth * 3;
		}
	}
	else if (sScanCard.nDotCorrectTye == 2)//��ɫ
	{
		nOneRowCoefNum = sScanCard.nModuleWidth * 9;
	}

	//�ܵ�У��������
	//ʵ�����������ص��������[(mod_width * 4 + x)/ 32] * 32
	//	ʵ���ص����ɫ��[(mod_width * 9 + x)/ 32] * 32
	//	ע��x��ָ��Ӻ��������32.�磺ģ��Ϊ15�ĵ�������x=2
	int nOneRowCoefNumAllA1 = 0x017F;//�ܵ�У������
	int nModuleWidth = sScanCard.nModuleWidth % 16 ?
		(sScanCard.nModuleWidth / 16 + 1) * 16 : sScanCard.nModuleWidth ;
	nModuleWidth = nModuleWidth > 42 ? sScanCard.nModuleWidth : nModuleWidth;

	if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//����
	{
		if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//ȫ��
		{
			nOneRowCoefNumAllA1 = (nModuleWidth * 4) % 32 ?
				(nModuleWidth * 4 / 32 + 1) * 32 : nModuleWidth * 4;

			if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && sScanCard.bVirtvalDisp
				&& (1 == sScanCard.nVirTualArray || 2 == sScanCard.nVirTualArray || 3 == sScanCard.nVirTualArray))
			{
				nOneRowCoefNumAllA1 = (nModuleWidth * 3) % 32 ?
					(nModuleWidth * 3 / 32 + 1) * 32 : nModuleWidth * 3;
			}
		}

	}
	else if (sScanCard.nDotCorrectTye == 2)//��ɫ
	{
		if (sScanCard.nScreenType == DISPLAY_TYPE_REAL ||
			(sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && !sScanCard.bVirtvalDisp))//ʵ���ػ�������ʵ������ʾ
		{
			nOneRowCoefNumAllA1 = (nModuleWidth * 9) % 32 ?
				(nModuleWidth * 9 / 32 + 1) * 32 :nModuleWidth * 9;
		}
	}

	//��ЧУ������������ģ�����ƽ�֣�
	int nRowCoefNovailNumAl = nOneRowCoefNumAllA1 - nOneRowCoefNum;

	int nTemp = 0;
	int nOffset = 0;
	if (sScanCard.nDataInputDir == 0)//������
	{
		if (sScanCard.nScreenType == DISPLAY_TYPE_REAL)//ʵ����
		{
			if (sScanCard.nDotCorrectTye == 1)//����
			{
				if (!sScanCard.bExtendedEnable && !sScanCard.bExtendedEnableEx)
				{
					//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬
					//�ȶ�ȡ���һ��16�����ص�R�������һ��16�����ص�G�������һ��16�����ص�B��
					//���ȡ�����ڶ���16�����ص�R���ٵ����ڶ���16�����ص�G���ٵ����ڶ���16�����ص�B��
					//�������ƣ�������16������ڿ���
					//��Ƶ���ݳ��ȣ������ճ�16�ı�����* 3 * 2�ֽ�
					//��Ƶ��������˳��R��G��B���������ֽڣ���һ������ռ8�ֽ���Ƶ��ַ
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;

					for (int i = 0 ; i < nLop; i ++)
						for (int m = 0; m < 3; m ++)
							for (int n = 0; n < 16; n ++ )
							{
								if (sScanCard.nModuleWidth % 16 > 0 && ((n == 0) || (i * 16 + n) % sScanCard.nModuleWidth == 0 )/*&& (i * 16 + n) >= sScanCard.nModuleWidth*/)
								{
									nOffset = nRowCoefNovailNumAl * ((i * 16 + n) / sScanCard.nModuleWidth);
								}
								nTemp = nLop * 16 * 4 - ( i * 16 * 4 + n * 4 + 4 - m) + nOffset;
								memcpy(ucCorrectProcessing + nDataLen, &nTemp, 2);
								nDataLen += 2;
							}
				}
				else
				{
					//����ʹ��
					int nLop = sScanCard.nSectionWidth % 16 ?
						sScanCard.nSectionWidth / 16 + 1 : sScanCard.nSectionWidth / 16;

					for(int v = 0; v < sScanCard.nSectionHorNum; ++v)
					{
						int nZoneOffset = v * sScanCard.nSectionWidth * 4;
						for (int i = 0 ; i < nLop; ++i)
							for (int m = 0; m < 3;  ++m)
								for (int n = 0; n < 16;  ++n )
								{
									if (sScanCard.nModuleWidth % 16 > 0 && ((n == 0) || (i * 16 + n) % sScanCard.nModuleWidth == 0 ))
									{
										nOffset = nRowCoefNovailNumAl * ((i * 16 + n) / sScanCard.nModuleWidth);
									}
									nTemp = nZoneOffset - ( i * 16 * 4 + n * 4 + 4 - m) + nOffset;
									memcpy(ucCorrectProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
								}
					}
				}
			}
			else if (sScanCard.nDotCorrectTye == 2)//��ɫ
			{
				if (!sScanCard.bExtendedEnable && !sScanCard.bExtendedEnableEx)
				{
					//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬
					//�ȶ�ȡ���һ��16�����ص�R�������һ��16�����ص�G�������һ��16�����ص�B��
					//���ȡ�����ڶ���16�����ص�R���ٵ����ڶ���16�����ص�G���ٵ����ڶ���16�����ص�B��
					//�������ƣ�������16������ڿ���
					//��Ƶ���ݳ��ȣ������ճ�16�ı�����* 3 * 3 * 2�ֽ�
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;
					for (int i = 0; i < nLop; i ++)
						for (int j = 0; j < 3; j ++)
							for (int n = 0; n < 16; n ++ )
								for (int m = 0; m < 3; m ++)
								{
									if ((n == 0) || (i * 16 + n) % sScanCard.nModuleWidth == 0)
									{
										nOffset = nRowCoefNovailNumAl * ((i * 16 + n) / sScanCard.nModuleWidth);
									}
									nTemp = nLop * 16 * 9 - ( i * 16 * 9 + n * 9 + 9 - m * 3 - j) + nOffset;
									memcpy(ucCorrectProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
								}
				}
				else
				{
					int nLop = sScanCard.nSectionWidth % 16 ?
						sScanCard.nSectionWidth / 16 + 1 : sScanCard.nSectionWidth / 16;
					for(int v = 0; v < sScanCard.nSectionHorNum; ++v)
					{
						int nZoneOffset = v * sScanCard.nSectionWidth * 9;
						for (int i = 0; i < nLop; ++i)
							for (int j = 0; j < 3; ++j)
								for (int n = 0; n < 16; ++n )
									for (int m = 0; m < 3; ++m)
									{
										if ((n == 0) || (i * 16 + n) % sScanCard.nModuleWidth == 0)
										{
											nOffset = nRowCoefNovailNumAl * ((i * 16 + n) / sScanCard.nModuleWidth);
										}
										nTemp = nZoneOffset - ( i * 16 * 9 + n * 9 + 9 - m * 3 - j) + nOffset;
										memcpy(ucCorrectProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;
									}
					}
				}
			}
		}
		else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//����
		{
			if (sScanCard.bVirtvalDisp)
			{
				if (sScanCard.nDotCorrectTye == 1)//����
				{
					//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬
					//�����Ƶ����һһ��Ӧ
					//�ȶ�ȡ���һ��16�����ص�R�������һ��16�����ص�G�������һ��16�����ص�B�������һ��16�����ص�RA
					//���ȡ�����ڶ���16�����ص�R���ٵ����ڶ���16�����ص�G���ٵ����ڶ���16�����ص�B���ٵ����ڶ���16�����ص�RA
					//�������ƣ�������16������ڿ���
					//��Ƶ���ݳ��ȣ������ճ�16�ı�����* 4 * 2�ֽ�

					int nLop = ( sScanCard.nScanCardWidth  % 16 ?
						(sScanCard.nScanCardWidth / 16 + 1 ) * 16 : sScanCard.nScanCardWidth ) / 16;

					for (int i = 0; i < nLop; i ++)
						for (int m = 0; m < 4; m ++)
							for (int n = 0; n < 16; n ++ )
							{
								if (sScanCard.nModuleWidth % 16 > 0 && ((n == 0) || (i * 16 + n) % sScanCard.nModuleWidth == 0 )/*&& (i * 16 + n) >= sScanCard.nModuleWidth*/)
								{
									nOffset = nRowCoefNovailNumAl * ((i * 16 + n) / sScanCard.nModuleWidth);
								}
								nTemp = nLop * 16 * 4 - (i * 16 * 4 + n * 4 + 4 - m) + nOffset;
								memcpy(ucCorrectProcessing + nDataLen, &nTemp, 2);
								nDataLen += 2;

							}
				}
				else if (sScanCard.nDotCorrectTye == 2)//��ɫ
				{
				}
			}
			else
			{
				if (sScanCard.nDotCorrectTye == 1)//����
				{
					//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬
					//�ȶ�ȡ���һ��16�����ص�R�������һ��16�����ص�G�������һ��16�����ص�B��
					//���ȡ�����ڶ���16�����ص�R���ٵ����ڶ���16�����ص�G���ٵ����ڶ���16�����ص�B��
					//�������ƣ�������16������ڿ���
					//��Ƶ���ݳ��ȣ������ճ�16�ı�����* 3 * 2�ֽ�
					//��Ƶ��������˳��R��G��B���������ֽڣ���һ������ռ8�ֽ���Ƶ��ַ
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;

					for (int i = 0 ; i < nLop; i ++)
						for (int m = 0; m < 3; m ++)
							for (int n = 0; n < 16; n ++ )
							{
								if (sScanCard.nModuleWidth % 16 > 0 && ((n == 0) || (i * 16 + n) % sScanCard.nModuleWidth == 0 )/*&& (i * 16 + n) >= sScanCard.nModuleWidth*/)
								{
									nOffset = nRowCoefNovailNumAl * ((i * 16 + n) / sScanCard.nModuleWidth);
								}
								nTemp = nLop * 16 * 4 - ( i * 16 * 4 + n * 4 + 4 - m) + nOffset;
								memcpy(ucCorrectProcessing + nDataLen, &nTemp, 2);
								nDataLen += 2;
							}

				}
				else if (sScanCard.nDotCorrectTye == 2)//��ɫ
				{
					//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬
					//�ȶ�ȡ���һ��16�����ص�R�������һ��16�����ص�G�������һ��16�����ص�B��
					//���ȡ�����ڶ���16�����ص�R���ٵ����ڶ���16�����ص�G���ٵ����ڶ���16�����ص�B��
					//�������ƣ�������16������ڿ���
					//��Ƶ���ݳ��ȣ������ճ�16�ı�����* 3 * 3 * 2�ֽ�
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;

					for (int i = 0; i < nLop; i ++)
						for (int j = 0; j < 3; j ++)
							for (int n = 0; n < 16; n ++ )
								for (int m = 0; m < 3; m ++)
								{
									if ((n == 0) || (i * 16 + n) % sScanCard.nModuleWidth == 0)
									{
										nOffset = nRowCoefNovailNumAl * ((i * 16 + n) / sScanCard.nModuleWidth);
									}
									nTemp = nLop * 16 * 9 - ( i * 16 * 9 + n * 9 + 9 - m * 3 - j) + nOffset;
									memcpy(ucCorrectProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
								}
				}
			}
		}
	}
	else if (sScanCard.nDataInputDir == 1)//���ҵ���
	{
		if (sScanCard.nDotCorrectTye == 1)//����
		{
			if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && !sScanCard.bVirtvalDisp))//ʵ���ػ�������ʵ������ʾ
			{
				if(!sScanCard.bExtendedEnable && !sScanCard.bExtendedEnableEx )
				{
					//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬
					//�ȶ�ȡ��һ��16�����ص�R���ٵ�һ��16�����ص�G���ٵ�һ��16�����ص�B��
					//���ȡ�ڶ���16�����ص�R���ٵڶ���16�����ص�G���ٵڶ���16�����ص�B��
					//�������ƣ�������16������ڿ���
					//��Ƶ���ݳ��ȣ������ճ�16�ı�����* 3 * 2�ֽ�
					//��Ƶ��������˳��R��G��B���������ֽڣ���һ������ռ8�ֽ���Ƶ��ַ
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;

					for (int i = 0; i < nLop; i ++)//��
						for (int m = 0; m < 3; m ++)//RGB��ɫ
							for (int n = 0; n < 16; n ++ )//16������
							{
								if (sScanCard.nModuleWidth % 16 > 0 && ((n == 0) || (i * 16 + n) % sScanCard.nModuleWidth == 0 )/*&& (i * 16 + n) >= sScanCard.nModuleWidth*/)
								{
									nOffset = nRowCoefNovailNumAl * ((i * 16 + n) / sScanCard.nModuleWidth);
								}
								nTemp = i * 16 * 4 + n * 4 + m + nOffset;
								memcpy(ucCorrectProcessing + nDataLen, &nTemp, 2);
								nDataLen += 2;
							}
				}
				else
				{
					int nLop = sScanCard.nCard_zone_width % 16 ?
						sScanCard.nCard_zone_width / 16 + 1 : sScanCard.nCard_zone_width / 16;
					for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //��������ѭ���������ⲿ��nCard_zone_Num=1
					{
						for(int u = 0; u < sScanCard.nSectionHorNum; ++u)
						{
							for (int i = 0; i < nLop; i ++)//��
							{
								for (int m = 0; m < 3; m ++)//RGB��ɫ
								{
									for (int n = 0; n < 16; n ++ )//16������
									{
										//����������ģ������
										int index1 = (i * 16 + n) / sScanCard.nSectionWidth;
										//����ģ���������ص�����ģ���λ��
										int index2 = (i * 16 + n) % sScanCard.nSectionWidth;
										//����ƫ��
										int nZoneOffset = ((index1 * sScanCard.nSectionHorNum) + u) * sScanCard.nSectionWidth * 4;
										//������ƫ��
										int nZoneDot = v * sScanCard.nCard_zone_width * sScanCard.nSectionHorNum * 4 +
											nRowCoefNovailNumAl * v * sScanCard.nCard_zone_width / sScanCard.nSectionWidth;
										//if (sScanCard.nModuleWidth % 16 > 0 && ((n == 0) || (i * 16 + n) % sScanCard.nModuleWidth == 0 )/*&& (i * 16 + n) >= sScanCard.nModuleWidth*/)
										//{
										//	nOffset = nRowCoefNovailNumAl * ((int)((i * 16 + n) / sScanCard.nSectionWidth));
										//}

										nOffset = nRowCoefNovailNumAl * ((int)((i * 16 + n) / sScanCard.nSectionWidth));

										nTemp = nZoneDot + nZoneOffset + index2 * 4 + m + nOffset;
										memcpy(ucCorrectProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;
									}
								}
							}
						}
					}
				}
			}
			else if(sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && sScanCard.bVirtvalDisp)//������������ʾ
			{
				//���������Ų���ʽ,//new���������Ų���ʽ��0����A,��/��,��B��1����,��/��,��;��,��/��,�죬2����,��/��,��;��,��/��,�գ�3����,��/��,��;��,��/��,��
				if (0 == sScanCard.nVirTualArray)//��A,��/��,��
				{
					//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬
					//�����Ƶ����һһ��Ӧ
					//�ȶ�ȡ��һ��16�����ص�R���ٵ�һ��16�����ص�G���ٵ�һ��16�����ص�B���ٵ�һ��16�����ص�RA
					//���ȡ�ڶ���16�����ص�R���ٵڶ���16�����ص�G���ٵڶ���16�����ص�B���ٵڶ���16�����ص�RA
					//�������ƣ�������16������ڿ���
					//��Ƶ���ݳ��ȣ������ճ�16�ı�����* 4 * 2�ֽ�

					int nLop = ( sScanCard.nScanCardWidth  % 16 ?
						(sScanCard.nScanCardWidth / 16 + 1 ) * 16 : sScanCard.nScanCardWidth ) / 16;

					for (int i = 0; i < nLop; i ++)
						for (int m = 0; m < 4; m ++)
							for (int n = 0; n < 16; n ++ )
							{
								if (sScanCard.nModuleWidth % 16 > 0 && ((n == 0) || (i * 16 + n) % sScanCard.nModuleWidth == 0 )/*&& (i * 16 + n) >= sScanCard.nModuleWidth*/)
								{
									nOffset = nRowCoefNovailNumAl * ((i * 16 + n) / sScanCard.nModuleWidth);
								}
								nTemp = i * 16 * 4 + n * 4 + m + nOffset;
								memcpy(ucCorrectProcessing + nDataLen, &nTemp, 2);
								nDataLen += 2;
							}
				}
				else if (1 == sScanCard.nVirTualArray	//1����,��/��,��;��,��/��,��
					|| 2 == sScanCard.nVirTualArray		//2����,��/��,��;��,��/��,��
					|| 3 == sScanCard.nVirTualArray)	//3����,��/��,��;��,��/��,��
				{
					int nLop = sScanCard.nCard_zone_width % 16 ?
						sScanCard.nCard_zone_width / 16 + 1 : sScanCard.nCard_zone_width / 16;

					for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //��������ѭ���������ⲿ��nCard_zone_Num=1
					{
						for(int u = 0; u < sScanCard.nSectionHorNum; ++u)
						{
							for (int i = 0; i < nLop; i ++)//��
							{
								for (int m = 0; m < 3; m ++)//RGB��ɫ
								{
									for (int n = 0; n < 16; n ++ )//16������
									{
										//����������ģ������
										int index1 = (i * 16 + n) / sScanCard.nSectionWidth;
										//����ģ���������ص�����ģ���λ��
										int index2 = (i * 16 + n) % sScanCard.nSectionWidth;
										//����ƫ��
										int nZoneOffset = ((index1 * sScanCard.nSectionHorNum) + u) * sScanCard.nSectionWidth * 3;
										//������ƫ��
										//int nZoneDot = v * sScanCard.nCard_zone_width * sScanCard.nSectionHorNum * 4;

										//�������ܵ���
										int nZoneDot = v * sScanCard.nCard_zone_width * sScanCard.nSectionHorNum * 3 +
											nRowCoefNovailNumAl * v * sScanCard.nCard_zone_width / sScanCard.nSectionWidth;

										nOffset = nRowCoefNovailNumAl * ((int)((i * 16 + n) / sScanCard.nSectionWidth));

										nTemp = nZoneDot + nZoneOffset + index2 * 3 + m + nOffset;

										memcpy(ucCorrectProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;
									}
								}
							}
						}
					}
				}
			}
		}
		else if (sScanCard.nDotCorrectTye == 2)//��ɫ
		{
			if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && !sScanCard.bVirtvalDisp))//ʵ���ػ�������ʵ������ʾ
			{
				if(!sScanCard.bExtendedEnable && !sScanCard.bExtendedEnableEx)
				{
					//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬
					//�ȶ�ȡ��һ��16�����ص�R��Ӧ��RGB(036)���ٵ�һ��16�����ص�G��Ӧ��RGB(147)��
					//�ٵ�һ��16�����ص�B��Ӧ��RGB(258)��
					//���ȡ�ڶ���16�����ص�R��Ӧ��RGB���ٵڶ���16�����ص�G��Ӧ��RGB���ٵڶ���16�����ص�B��Ӧ��RGB��
					//�������ƣ�������16������ڿ���
					//��Ƶ���ݳ��ȣ������ճ�16�ı�����* 3 * 3 * 2�ֽ�
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;

					for (int i = 0; i < nLop; i ++)
						for (int m = 0; m < 3; m ++)
							for (int n = 0; n < 16; n ++ )
								for (int j = 0; j < 3; j ++)
								{
									if ((n == 0) || (i * 16 + n) % sScanCard.nModuleWidth == 0 )
									{
										nOffset = nRowCoefNovailNumAl * ((i * 16 + n) / sScanCard.nModuleWidth);
									}
									nTemp = i * 16 * 9 + m + n * 9 + j * 3 + nOffset;
									memcpy(ucCorrectProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
								}
				}
				else
				{
					int nLop = sScanCard.nCard_zone_width % 16 ?
						sScanCard.nCard_zone_width / 16 + 1 : sScanCard.nCard_zone_width / 16;
					for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //��������ѭ���������ⲿ��nCard_zone_Num=1
					{
						for(int u = 0; u < sScanCard.nSectionHorNum; ++u)
						{
							for (int i = 0; i < nLop;  ++i)//��
							{
								for (int m = 0; m < 3; ++m)//RGB��ɫ
								{
									for (int n = 0; n < 16;  ++n )//16������
									{
										//����������ģ������
										int index1 = (i * 16 + n) / sScanCard.nSectionWidth;
										//����ģ���������ص�����ģ���λ��
										int index2 = (i * 16 + n) % sScanCard.nSectionWidth;
										//����ƫ��
										int nZoneOffset = ((index1 * sScanCard.nSectionHorNum) + u) * sScanCard.nSectionWidth * 9;

										//������ƫ��
										//int nZoneDot = v * sScanCard.nCard_zone_width * sScanCard.nSectionHorNum * 9;

										int nZoneDot = v * sScanCard.nCard_zone_width * sScanCard.nSectionHorNum * 9 +
											nRowCoefNovailNumAl * v * sScanCard.nCard_zone_width / sScanCard.nSectionWidth;

										for (int j = 0; j < 3;  ++j) //��ɫ��������
										{
											nOffset = nRowCoefNovailNumAl * ((int)((i * 16 + n) / sScanCard.nSectionWidth));

											//nTemp = nZoneDot + u * sScanCard.nSectionWidth * 9 +
											//	((int)((i * 16 * 9 + n) / sScanCard.nSectionWidth)) * sScanCard.nModuleWidth * 9 +
											//	((i * 16 * 9 + n) % sScanCard.nSectionWidth) * 9 + j * 3 + nOffset + m;

											nTemp = nZoneDot + nZoneOffset + index2 * 9 + j * 3 + m + nOffset;
											memcpy(ucCorrectProcessing + nDataLen, &nTemp, 2);
											nDataLen += 2;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//���½���
	{
		if (sScanCard.nScreenType == DISPLAY_TYPE_REAL)//ʵ����
		{
			if (sScanCard.nDotCorrectTye == 1)//����
			{
				//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬У����ַ����Ƶ��ַ��Ӧ
				//�����̶�����16�����㣬������16�İ�Ĭ�ϵ�ַȡ
				//��Ƶ�ǰ�ɨ�迨һ��ɨ�裬����У������ʱ��ģ�鷢�ͣ�ģ���ȱ�����16�ı���
				//��1�����ݣ���һ����һ��RGB���ڶ�����һ��RGB....��ʮ������һ��RGB
				//��2�����ݣ���һ���ڶ���RGB���ڶ����ڶ���RGB....��ʮ�����ڶ���RGB
				//У����������˳��R��G��B��RB(ʵ����ʱΪ0)����һ������ռ8�ֽڵ�ַ
				//У����ַ��(0x9C)��ɨ������ɨ�迨�Ŀ�����У������(0x94)��һ��ģ��һ��ģ��ķ��ͣ�����ÿģ������������ƫ����(ģ����*4)

				int nDLine = sScanCard.nScanCardSectionRowNumber * ((int)pow(2.0, sScanCard.nDCBlineClkEn));
				for( int j = 0; j < nDLine; ++j)
					for (int m = 0; m < 3; ++m)//RGB��ɫ
						for (int i = 0; i < 16; ++i)//Ĭ��16��
						{
							nTemp = 16 * 4 * i + m + j * 4;
							memcpy(ucCorrectProcessing + nDataLen, &nTemp, 2);
							nDataLen += 2;
						}
			}
			else if (sScanCard.nDotCorrectTye == 2)//��ɫ
			{
				//ֻ��ȡ��һ�ж�Ӧ����Ƶ���ݵ�ַ��16������һ�飬һ�����ذ���ÿ������Ӧλ�����أ�
				//������16�����㣬������16�İ�Ĭ�ϵ�ַȡ
				//��1�����ݣ���һ����һ��R��Ӧ��RGB���ڶ�����һ��R��Ӧ��RGB....��ʮ������һ��R��Ӧ��RGB
				//��2�����ݣ���һ����һ��G��Ӧ��RGB���ڶ�����һ��G��Ӧ��RGB....��ʮ������һ��G��Ӧ��RGB
				//��3�����ݣ���һ����һ��B��Ӧ��RGB���ڶ�����һ��B��Ӧ��RGB....��ʮ������һ��B��Ӧ��RGB
				//��4�����ݣ���һ���ڶ���R��Ӧ��RGB���ڶ����ڶ���R��Ӧ��RGB....��ʮ�����ڶ���R��Ӧ��RGB
				//��5�����ݣ���һ���ڶ���G��Ӧ��RGB���ڶ����ڶ���G��Ӧ��RGB....��ʮ�����ڶ���G��Ӧ��RGB
				//��6�����ݣ���һ���ڶ���B��Ӧ��RGB���ڶ����ڶ���B��Ӧ��RGB....��ʮ�����ڶ���B��Ӧ��RGB
				//�������ƣ�������16������ڿ���
				//��Ƶ���ݳ��ȣ�ÿ������ * 16 * 3 * 3 * 2�ֽ�

				for (int i = 0; i <sScanCard.nScanCardSectionRowNumber; i ++)
					for (int j = 0; j < 3; j ++)
						for (int n = 0; n < 16; n ++ )
							for (int m = 0; m < 3; m ++)
							{
								nTemp = i * 9 + sScanCard.nScanCardSectionRowNumber * 9 * n + j + m * 3;
								memcpy(ucCorrectProcessing + nDataLen, &nTemp, 2);
								nDataLen += 2;
							}
			}
		}
		else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//������
		{
		}
	}

	int nLop = nDataLen / CL_SEND_EFFECT_DATA_SIZE;

	memset(ucSendData, 0, CL_MAX_BUF_NUMBER);
	int nLength = 0;
	unsigned char uSendData[CL_SEND_PACK_SIZE];

	for (int n = 0; n < nLop; ++n)
	{

		memset(uSendData,0,sizeof(uSendData));

		int nLen = CCLPackCommunicationData::PackDataBase(0,nAddress,0x9C,n,false,
									ucCorrectProcessing + n * CL_SEND_EFFECT_DATA_SIZE,uSendData);

		memcpy(ucSendData + nLength,uSendData,nLen);

		nLength += nLen;
	}

	return nLength;
}
//���ñ߽�ϵ�����ұ�
int CCLScanCardPackData::PackCorrectProcessingLookup(short nAddress,
						CStructSingleScanCard & sScanCard,
						unsigned char * ucSendData)
{
	//��У��ʱ������У�����ݵ�ַ��
	if (0 == sScanCard.nDotCorrectTye)
	{
		return -1;
	}

	//��Ƶ�������ݻ���
	unsigned char ucCorrectProcessing[CL_MAX_BUF_NUMBER];
	memset(ucCorrectProcessing, 0, sizeof(ucCorrectProcessing));

	//��Ƶ�������ݳ���
	int nDataLen = 0;

	int nTemp = 0;
	if (sScanCard.nDataInputDir == 0)//������
	{
	}
	else if (sScanCard.nDataInputDir == 1)//���ҵ���
	{
		int nLeftIndex = 0;//ģ����߽���index
#ifdef M3_5
		nLeftIndex = sScanCard.nScanCardWidth - sScanCard.nScanCardWidthReal; //M3.5��������Ϊ72��ʵ�ʿ��Ϊ68����߽���Ҫ���ƶ�4�����ص�
#else
		nLeftIndex = 0;
#endif

		if (sScanCard.nDotCorrectTye == 1)//����
		{
			if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && !sScanCard.bVirtvalDisp))//ʵ���ػ�������ʵ������ʾ
			{
				if(!sScanCard.bExtendedEnable && !sScanCard.bExtendedEnableEx )
				{
					//ģ��߽���ұ��У����ַ����Ӧ��
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;
					for (int i = 0; i < nLop; i ++)
						for (int m = 0; m < 3; m ++)//RGB������ɫ
						{
							int nModID = (16 * i) / sScanCard.nModuleWidth;
							for (int n = 0; n < 16; n ++ )
							{
								unsigned char cTmp[2];
								cTmp[0] = nModID;
								if ((i * 16 + n) % sScanCard.nModuleWidth == nLeftIndex )
								{
									//ģ���һ��
									cTmp[1] = 1;//ģ��߽��ţ�1-����1�У�2-�м��У�3-����1��
								}
								else if((i * 16 + n) % sScanCard.nModuleWidth == sScanCard.nModuleWidth - 1)
								{
									//ģ�����һ��
									cTmp[1] = 3;
									nModID ++;
								}
								else
								{
									//ģ���м���
									cTmp[1] = 2;
								}

								memcpy(ucCorrectProcessing + nDataLen, cTmp, 2);
								nDataLen += 2;
							}
						}

				}
				else
				{
					//int abc = 0;
					int nLop = sScanCard.nCard_zone_width % 16 ?
						sScanCard.nCard_zone_width / 16 + 1 : sScanCard.nCard_zone_width / 16;
					for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //��������ѭ��
					{
						for(int u = 0; u < sScanCard.nSectionHorNum; ++u)//ģ����������
						{
							for (int i = 0; i < nLop; i ++)
							{
								for (int m = 0; m < 3; m ++)//RGB������ɫ
								{
									for (int n = 0; n < 16; n ++ )
									{
										//����������ģ������
										int index1 = (i * 16 + n) / sScanCard.nSectionWidth;
										//����ģ���������ص�����ģ���λ��
										int index2 = (i * 16 + n) % sScanCard.nSectionWidth;
										//����ƫ��
										int nZoneOffset = ((index1 * sScanCard.nSectionHorNum) + u) * sScanCard.nSectionWidth;
										//������ƫ��
										int nZoneDot = v * sScanCard.nCard_zone_width * sScanCard.nSectionHorNum;
										nTemp = nZoneOffset + index2 + nZoneDot;
										int nModID = nTemp/sScanCard.nModuleWidth;

										unsigned char cTmp[2];
										cTmp[0] = nModID;
										if (nTemp % sScanCard.nModuleWidth == nLeftIndex)//M3.5 ����4���㣬�������
										{
											//ģ���һ��
											cTmp[1] = 1;//ģ��߽��ţ�1-����1�У�2-�м��У�3-����1��
										}
										else if(nTemp % sScanCard.nModuleWidth == sScanCard.nModuleWidth - 1)
										{
											//ģ�����һ��
											cTmp[1] = 3;
										}
										else
										{
											//ģ���м���
											cTmp[1] = 2;
										}

										//abc++;

										memcpy(ucCorrectProcessing + nDataLen, cTmp, 2);
										nDataLen += 2;
									}
								}
							}
						}
					}

				}

			}
			else if(sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && sScanCard.bVirtvalDisp)//������������ʾ
			{
				if (0 == sScanCard.nVirTualArray)
				{
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;
					for (int i = 0; i < nLop; i ++)
					{
						for (int m = 0; m < 4; m ++)//R/G/B/RA������ɫ
						{
							int nModID = (16 * i) / sScanCard.nModuleWidth;
							for (int n = 0; n < 16; n ++ )
							{
								unsigned char cTmp[2];
								cTmp[0] = nModID;
								if ((i * 16 + n) % sScanCard.nModuleWidth == 0 )
								{
									//ģ���һ��
									cTmp[1] = 1;//ģ��߽��ţ�1-����1�У�2-�м��У�3-����1��
								}
								else if((i * 16 + n) % sScanCard.nModuleWidth == sScanCard.nModuleWidth - 1)
								{
									//ģ�����һ��
									cTmp[1] = 3;
									nModID ++;
								}
								else
								{
									//ģ���м���
									cTmp[1] = 2;
								}
								memcpy(ucCorrectProcessing + nDataLen, cTmp, 2);
								nDataLen += 2;
							}
						}
					}
				}
				else if (1 == sScanCard.nVirTualArray || 2 == sScanCard.nVirTualArray || 3 == sScanCard.nVirTualArray)
				{
					//1����,��/��,��;��,��/��,�죬2����,��/��,��;��,��/��,�գ�3����,��/��,��;��,��/��,��
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;
					//int abc = 0;
					for (int k = 0; k < 2; k++) //line index
					{
						for (int i = 0; i < nLop; i ++)
						{
							for (int m = 0; m < 3; m ++)//R/G/B/������ɫ
							{
								//����ƣ������ص㣩��Ӧ�����λ�ã�0���ϣ�1����, 2���£�3����
								int nIndex = GetIndex(m, sScanCard.nVirTualArray, k);
								for (int n = 0; n < 16; n ++ )
								{
									switch (nIndex)
									{
									case 0:
									case 1:
										nTemp = i * 16 + n + sScanCard.nScanCardWidth * (0 + k);//���ϣ�����
										break;
									case 2:
									case 3:
										nTemp = i * 16 + n + sScanCard.nScanCardWidth * (1 + k);//���£�����
										break;
									}

									//Module ID
									int nModID = (nTemp % (16 * nLop))/sScanCard.nModuleWidth;

									unsigned char cTmp[2];
									cTmp[0] = nModID;

									if (nTemp % sScanCard.nModuleWidth == 0 && (nIndex == 0 || nIndex == 2))
									{
										//ģ���һ��
										cTmp[1] = 1;//ģ��߽��ţ�1-����1�У�2-�м��У�3-����1��
									}
									else if(nTemp % sScanCard.nModuleWidth == sScanCard.nModuleWidth - 1
										&& (nIndex == 1 || nIndex == 3))
									{
										//ģ�����һ��
										cTmp[1] = 3;
									}
									else
									{
										//ģ���м���
										cTmp[1] = 2;
									}
									//abc++;
									memcpy(ucCorrectProcessing + nDataLen, cTmp, 2);
									nDataLen += 2;
								}
							}
						}
					}

				}
				else
				{
					;
				}

			}
		}
		else if (sScanCard.nDotCorrectTye == 2)//��ɫ
		{
			if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && !sScanCard.bVirtvalDisp))//ʵ���ػ�������ʵ������ʾ
			{
				if(!sScanCard.bExtendedEnable && !sScanCard.bExtendedEnableEx)
				{
					//ģ��߽���ұ��У����ַ����Ӧ��
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;
					for (int i = 0; i < nLop; i ++)
						for (int m = 0; m < 3; m ++)//RGB������ɫ
						{
							int nModID = (16 * i) / sScanCard.nModuleWidth;
							for (int n = 0; n < 16; n ++ )
							{
								unsigned char cTmp[2];
								cTmp[0] = nModID;
								if ((i * 16 + n) % sScanCard.nModuleWidth == nLeftIndex )
								{
									//ģ���һ��
									cTmp[1] = 1;//ģ��߽��ţ�1-����1�У�2-�м��У�3-����1��
								}
								else if((i * 16 + n) % sScanCard.nModuleWidth == sScanCard.nModuleWidth - 1)
								{
									//ģ�����һ��
									cTmp[1] = 3;
									nModID ++;
								}
								else
								{
									//ģ���м���
									cTmp[1] = 2;
								}
								for (int j = 0; j < 3; j ++)//һ����ɫ��Ӧ��RGB
								{
									memcpy(ucCorrectProcessing + nDataLen, cTmp, 2);
									nDataLen += 2;

								}
							}
						}
				}
				else
				{
					//int abc = 0;
					int nLop = sScanCard.nCard_zone_width % 16 ?
						sScanCard.nCard_zone_width / 16 + 1 : sScanCard.nCard_zone_width / 16;
					for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //��������ѭ��
					{
						for(int u = 0; u < sScanCard.nSectionHorNum; ++u)//ģ����������
						{
							for (int i = 0; i < nLop; i ++)
							{
								for (int m = 0; m < 3; m ++)//RGB������ɫ
								{
									//int nModID = (16 * i + u * sScanCard.nSectionWidth + nZoneDot) / sScanCard.nModuleWidth;

									for (int n = 0; n < 16; n ++ )
									{
										//����������ģ������
										int index1 = (i * 16 + n) / sScanCard.nSectionWidth;
										//����ģ���������ص�����ģ���λ��
										int index2 = (i * 16 + n) % sScanCard.nSectionWidth;
										//����ƫ��
										int nZoneOffset = ((index1 * sScanCard.nSectionHorNum) + u) * sScanCard.nSectionWidth;
										//������ƫ��
										int nZoneDot = v * sScanCard.nCard_zone_width * sScanCard.nSectionHorNum;
										nTemp = nZoneOffset + index2 + nZoneDot;
										int nModID = nTemp/sScanCard.nModuleWidth;

										unsigned char cTmp[2];
										cTmp[0] = nModID;
										if (nTemp % sScanCard.nModuleWidth == nLeftIndex )
										{
											//ģ���һ��
											cTmp[1] = 1;//ģ��߽��ţ�1-����1�У�2-�м��У�3-����1��
										}
										else if(nTemp % sScanCard.nModuleWidth == sScanCard.nModuleWidth - 1)
										{
											//ģ�����һ��
											cTmp[1] = 3;
										}
										else
										{
											//ģ���м���
											cTmp[1] = 2;
										}

										//abc++;
										for (int j = 0; j < 3; j ++)//һ����ɫ��Ӧ��RGB
										{
											memcpy(ucCorrectProcessing + nDataLen, cTmp, 2);
											nDataLen += 2;
										}

									}
								}
							}
						}
					}
				}
			}
		}
	}
	else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//���½���
	{
		if (sScanCard.nScreenType == DISPLAY_TYPE_REAL)//ʵ����
		{
			if (sScanCard.nDotCorrectTye == 1)//����
			{

				int nDLine = sScanCard.nScanCardSectionRowNumber * ((int)pow(2.0, sScanCard.nDCBlineClkEn));
				for( int i = 0; i < nDLine; ++i)
					for (int m = 0; m < 3; m ++)//RGB������ɫ
					{
						int nModID = (16 * i) / sScanCard.nModuleWidth;
						for (int n = 0; n < 16; n ++ )
						{
							unsigned char cTmp[2];
							cTmp[0] = nModID;
							if ((i * 16 + n) % sScanCard.nModuleWidth == 0 )
							{
								//ģ���һ��
								cTmp[1] = 1;//ģ��߽��ţ�1-����1�У�2-�м��У�3-����1��
							}
							else if((i * 16 + n) % sScanCard.nModuleWidth == sScanCard.nModuleWidth - 1)
							{
								//ģ�����һ��
								cTmp[1] = 3;
								nModID ++;
							}
							else
							{
								//ģ���м���
								cTmp[1] = 2;
							}

							memcpy(ucCorrectProcessing + nDataLen, cTmp, 2);
							nDataLen += 2;
						}
					}
			}
			else if (sScanCard.nDotCorrectTye == 2)//��ɫ
			{
				int nDLine = sScanCard.nScanCardSectionRowNumber * ((int)pow(2.0, sScanCard.nDCBlineClkEn));
				for (int i = 0; i < nDLine; i ++)
					for (int m = 0; m < 3; m ++)//RGB������ɫ
					{
						int nModID = (16 * i) / sScanCard.nModuleWidth;
						for (int n = 0; n < 16; n ++ )
						{
							unsigned char cTmp[2];
							cTmp[0] = nModID;
							if ((i * 16 + n) % sScanCard.nModuleWidth == 0 )
							{
								//ģ���һ��
								cTmp[1] = 1;//ģ��߽��ţ�1-����1�У�2-�м��У�3-����1��
							}
							else if((i * 16 + n) % sScanCard.nModuleWidth == sScanCard.nModuleWidth - 1)
							{
								//ģ�����һ��
								cTmp[1] = 3;
								nModID ++;
							}
							else
							{
								//ģ���м���
								cTmp[1] = 2;
							}
							for (int j = 0; j < 3; j ++)//һ����ɫ��Ӧ��RGB
							{
								memcpy(ucCorrectProcessing + nDataLen, cTmp, 2);
								nDataLen += 2;
							}
						}
					}
			}
		}
		else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//������
		{
		}
	}

	int nLop = nDataLen / CL_SEND_EFFECT_DATA_SIZE;

	memset(ucSendData, 0, CL_MAX_BUF_NUMBER);
	int nLength = 0;
	unsigned char uSendData[CL_SEND_PACK_SIZE];

	for (int n = 0; n < nLop; ++n)
	{

		memset(uSendData,0,sizeof(uSendData));

		int nLen = CCLPackCommunicationData::PackDataBase(0,nAddress,0xA1,n,false,
						ucCorrectProcessing + n * CL_SEND_EFFECT_DATA_SIZE,uSendData);

		memcpy(ucSendData + nLength,uSendData,nLen);

		nLength += nLen;
	}

	return nLength;
}

//����ɨ�迨���߲��ұ�
int CCLScanCardPackData::PackScanCardLinkTable(short nAddress,
						short nDataLineRange,
						short nDCBlineClkEn,
						LINKTABLE &sLinkTable,
						unsigned char * ucSendData)
{

	//���߲��ұ�������ߴ����й�
	unsigned char dataLinkTemp[CL_MAX_BUF_NUMBER * 2];
	memset(dataLinkTemp, 0, CL_MAX_BUF_NUMBER * 2);
	int nLenTemp = 0;

	switch(nDataLineRange)
	{
	case 0:
		//�������ֿ�
		memcpy(dataLinkTemp, sLinkTable.ucLinkTable, sLinkTable.nLen);
		nLenTemp = sLinkTable.nLen;
		break;
	case 1:
		//��������һ��ɫ1�㴮��
	case 2:
		//��������һ��ɫ8�㴮��
	case 3:
		//��������һ��ɫ16�㴮��
		{
			int nDotNum = 0;
			if (nDataLineRange == 1)
			{
				nDotNum = 1;
			}
			else if (nDataLineRange == 2)
			{
				nDotNum = 8;
			}
			else if (nDataLineRange == 3)
			{
				nDotNum = 16;
			}
			int nFlag = 0;

			do
			{
				for (int n = 0; n < 3; ++ n)
				{
					for (int m = 0; m < nDotNum; ++m)
					{
						for (int i = 0; i < pow(2.0, nDCBlineClkEn); ++i)
						{
							dataLinkTemp[nLenTemp++] = sLinkTable.ucLinkTable[nFlag + m * 2] + i;

							dataLinkTemp[nLenTemp++] = sLinkTable.ucLinkTable[nFlag + m * 2 + 1];
						}
					}
				}
				nFlag += nDotNum * 2;
			}while(nFlag < sLinkTable.nLen);

		}
		break;
	case 4:
		//��������һ��ɫ1�㴮��
	case 5:
		//��������һ��ɫ8�㴮��
	case 6:
		//��������һ��ɫ16�㴮��
		{
			int nDotNum = 0;
			if (nDataLineRange == 4)
			{
				nDotNum = 1;
			}
			else if (nDataLineRange == 5)
			{
				nDotNum = 8;
			}
			else if (nDataLineRange == 6)
			{
				nDotNum = 16;
			}
			int nFlag = 0;

			do
			{
				for (int n = 0; n < 4; ++ n)
				{
					for (int m = 0; m < nDotNum; ++m)
					{
						for (int i = 0; i < pow(2.0, nDCBlineClkEn); ++i)
						{
							dataLinkTemp[nLenTemp++] = sLinkTable.ucLinkTable[nFlag + m * 2] + i;

							dataLinkTemp[nLenTemp++] = sLinkTable.ucLinkTable[nFlag + m * 2 + 1];
						}
					}
				}
				nFlag += nDotNum * 2;
			}while(nFlag < sLinkTable.nLen);

		}
		break;
	default:
		return -1;
		//break;
	}

	memset(ucSendData, 0, CL_MAX_BUF_NUMBER);
	int nLength = 0;
	unsigned char uSendData[CL_SEND_PACK_SIZE];

	for (int n = 0; n < nLenTemp / CL_SEND_EFFECT_DATA_SIZE; n ++)
	{

		memset(uSendData,0,sizeof(uSendData));

		int nLen = CCLPackCommunicationData::PackDataBase(0,nAddress,0x99,n,false,
					dataLinkTemp + n * CL_SEND_EFFECT_DATA_SIZE,uSendData);

		memcpy(ucSendData + nLength,uSendData,nLen);

		nLength += nLen;
	}

	return nLength;
}

//����ɨ�迨�����߲��ұ�
int CCLScanCardPackData::PackScanCardSectionLinkTable(short nAddress,
						LINKTABLE &sLinkTable,
						unsigned char * ucSendData)
{
	unsigned char dataLinkTemp[CL_MAX_BUF_NUMBER];
	memset(dataLinkTemp, 0, CL_MAX_BUF_NUMBER);
	int nLenTemp = SCANCARD_SECTION_PER_CARD_MAX;
	memcpy(dataLinkTemp, sLinkTable.ucLinkTable, sLinkTable.nLen);

	memset(ucSendData, 0, CL_MAX_BUF_NUMBER);
	int nLength = 0;
	unsigned char uSendData[CL_SEND_PACK_SIZE];

	for (int n = 0; n < nLenTemp / CL_SEND_EFFECT_DATA_SIZE; n++)
	{

		memset(uSendData,0,sizeof(uSendData));

		//��ɨ�迨�����߲��Ұ�
		int nLen = CCLPackCommunicationData::PackDataBase(0,nAddress,0x56,n,false,
								dataLinkTemp + n * CL_SEND_EFFECT_DATA_SIZE,uSendData);

		memcpy(ucSendData + nLength,uSendData,nLen);
		nLength += nLen;
	}

	return nLength;

}

int CCLScanCardPackData::PackHUBLookup(int nAddress,//ɨ�迨��ַ
										LinkTable hublinktable,
										unsigned char* ucSendData
										)//��������
{
	const int nLookupLen = 1024;
	unsigned char dataLinkTemp[CL_MAX_BUF_NUMBER];
		memset(dataLinkTemp, 0, CL_MAX_BUF_NUMBER);
	int nLenTemp = SCANCARD_SECTION_PER_CARD_MAX;
	memcpy(dataLinkTemp, hublinktable.ucLinkTable, hublinktable.nLen);

	memset(ucSendData, 0, CL_MAX_BUF_NUMBER);
	int nLength = 0;
	unsigned char uSendData[CL_SEND_PACK_SIZE];

	for (int n = 0; n < nLenTemp / CL_SEND_EFFECT_DATA_SIZE; n++)
	{
		memset(uSendData,0,sizeof(uSendData));
		//��ɨ�迨�����߲��Ұ�
			int nLen = CCLPackCommunicationData::PackDataBase(nAddress,0,0xE5,n,false,
								dataLinkTemp + n * CL_SEND_EFFECT_DATA_SIZE,uSendData);
		memcpy(ucSendData + nLength,uSendData,nLen);
		nLength += nLen;
	}
	return nLength;
//	return 0;
}

//����GAMMA��
int CCLScanCardPackData::PackGammaTable(short nAddress,
					GAMMADATA & sGammaData,
					int nColorIndex,
					unsigned char * ucSendData)
{
	memset(ucSendData, 0, CL_MAX_BUF_NUMBER);
	int nLength = 0;

	unsigned char dataGamma[CL_SEND_EFFECT_DATA_SIZE];
	unsigned char uSendData[CL_SEND_PACK_SIZE];

	switch (sGammaData.nVideowid)
	{
	case 10:
	case 12:

		for (int n = 0; n < 512; ++n)
		{
			memset(uSendData,0,sizeof(uSendData));

			int nLen1 = 0;
			memset(dataGamma, 0, CL_SEND_EFFECT_DATA_SIZE);
			//��λ�ں󣬵�λ��ǰ
			for(int i = 0; i < 8; ++i)
			{
				memcpy(dataGamma + nLen1 + i * 2, &sGammaData.sGammaTableRGB[n * 8 + i], 2);
			}

			int nLen = CCLPackCommunicationData::PackDataBase(0,nAddress,0x93,32 * 0 + n,false,
									dataGamma,uSendData);

			memcpy(ucSendData + nLength,uSendData,nLen);
			nLength += nLen;
		}
		break;
	case 8:
	default:
		for (int n = 0; n < 32; ++n)
		{
			memset(uSendData,0,sizeof(uSendData));

			int nLen1 = 0;
			memset(dataGamma, 0, CL_SEND_EFFECT_DATA_SIZE);
			//��λ��ǰ����λ�ں�
			for(int i = 0; i < 8; ++i)
			{
				memcpy(dataGamma + nLen1 + i * 2, &sGammaData.sGammaTable[nColorIndex][n * 8 + i], 2);
			}

			int nLen = CCLPackCommunicationData::PackDataBase(0,nAddress,0x93,32 * nColorIndex + n,false,
									dataGamma,uSendData);

			memcpy(ucSendData + nLength,uSendData,nLen);
			nLength += nLen;
		}
		break;
	}

	return nLength;
}


//���ô�����ָʾ��
int CCLScanCardPackData::PackOpenCabinetLamps(short nAddress,
					bool bOpen,
					unsigned char * ucSendData)
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));

	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	if (bOpen)
		ucData[0] = 0x00;
	else
		ucData[0] = 0x01;

	//������ָʾ��
	int nLength = CCLPackCommunicationData::PackDataBase(0,nAddress,0x40,0x06,false,ucData,ucSendData);

	return nLength;
}

//���õ���У��ʹ��(�㲥��Ӧ��)��nDotType��0 - �ޣ� 1 - ������ 2 - ��ɫ
int CCLScanCardPackData::PackCalibrationEnable(short nAddress,
						bool bEnable, short nDotType,
						unsigned char * ucSendData)
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	unsigned char dataCalib[CL_SEND_EFFECT_DATA_SIZE];
	memset(dataCalib, 0, CL_SEND_EFFECT_DATA_SIZE);
	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�
	//Corr_en_ctrl	8	0xFF	7--0
	//bit7--4	Ԥ�������0xff
	//bit3	����У�����ܣ����漰ʹ�ܣ�ֻ�漰���ܣ�����ʱ���ϵ�Ĭ�϶�������SDRAM��0��������1���ر�
	//	bit2	1	�ӿ�FLASH����SDRAM		0	��ģ�������FLASH���ٵ�SDRAM
	//	bit1	1	����	0	��ɫ
	//	bit0	0	�ر�	1	ʹ��
	int nDotCorrType = 0;
	int nStart = 0;
	if (nDotType == 0)
	{
		nStart = 1;//�ر�
	}
	else if (nDotType == 2)
	{
		nDotCorrType = 1;
	}

	dataCalib[0] = (nStart << 3) | (nDotCorrType << 1) | (bEnable ? 1 : 0);

	int nLength = CCLPackCommunicationData::PackDataBase(0,nAddress,0x40,0x0A,false,dataCalib,ucSendData);

	return nLength;
}

//�������Ȳ���,nBrightPercent:���Ȱٷֱ�(0 - 100)
int CCLScanCardPackData::PackBrightness(short nAddress,
					int nBrightPercent,
					unsigned char * ucSendData)
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	//����0-1000�����0-255
	//int nBrightness =(int) (0.256 * nBrightPercent + 0.5);
	int nBrightness = nBrightPercent;

	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, CL_SEND_EFFECT_DATA_SIZE);
	//����0-256��Ϊ�˼���ԭ�汾����ֵΪ256ʱ����ֵ0x1FF
	if(nBrightness >= 0 && nBrightness < 256)
	{
		ucData[0] = nBrightness;
	}
	else if(nBrightness == 256)
	{
		ucData[0] = 0xFF;
		ucData[1] = 1;
	}

	int nLength = CCLPackCommunicationData::PackDataBase(0,nAddress,0x40,0x08,false,ucData,ucSendData);

	return nLength;
}

//����3D������Ϣ
//1.	ͬ����ʱ������Synchron Delay�������÷�Χ -127 - +127
//2.	�ر�ɨ�����ڲ�����Disable Scan Cycle�������÷�Χ 0 - 255
int CCLScanCardPackData::Pack3DPara(short nAddress,
					int nSynchronDelay,
					int DisableScanCycle,
					unsigned char * ucSendData)
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, CL_SEND_EFFECT_DATA_SIZE);

	char cDelay = 0;//ͬ���ź���ʱ��λ	0����	1����
	if (nSynchronDelay < 0)
	{
		cDelay = 1 << 7;
		nSynchronDelay = nSynchronDelay < -127 ? -127 : nSynchronDelay;
		cDelay += (-nSynchronDelay);
	}
	else
	{
		cDelay = 0 << 7;
		nSynchronDelay = nSynchronDelay > 127 ? 127 : nSynchronDelay;
		cDelay += nSynchronDelay;
	} 
	ucData[0] = cDelay;
	ucData[1] = DisableScanCycle;

	int nLength = CCLPackCommunicationData::PackDataBase(0,nAddress,0x40,0x16,false,ucData,ucSendData);

	return nLength;
}


//���������
int CCLScanCardPackData::PackLockUnlock(short nAddress,
					bool bLock,
					unsigned char * ucSendData)
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, CL_SEND_EFFECT_DATA_SIZE);

	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�
	//������ʶ   0x01 - �ֶ�����   0x02 - �ֶ�����
	if (bLock)
	{
		ucData[0] = 0x01;
	}
	else
	{
		ucData[0] = 0x02;
	}
	int nLength = CCLPackCommunicationData::PackDataBase(0,0xFF,0x44,0,false,ucData,ucSendData);

	return nLength;
}

//���ܱ�ַ��
int CCLScanCardPackData::PackScanCardIntelligentPara(int nAddressMin,
					unsigned char * ucSendData)
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, CL_SEND_EFFECT_DATA_SIZE);

	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�
	//Smart_addr_beg	8	0x01	7--0	���ܱ�ַ��ʼ��.Ĭ��0x01
	//Ԥ��15�ֽ�
	ucData[0] = (unsigned char) nAddressMin;
	int nLength = CCLPackCommunicationData::PackDataBase(0,0xFF,0x40,0X0B,false,ucData,ucSendData);

	return nLength;
}

//�̵������
int CCLScanCardPackData::PackRelay(short nAddress,short nRelayID, bool bPower,
			unsigned char * ucSendData)
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	//�������ֽ�
	unsigned char data[CL_SEND_EFFECT_DATA_SIZE];
	memset(data, 0, CL_SEND_EFFECT_DATA_SIZE);

	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�
	//1 �̵������(RELAY_TAG)	1	0X01~0X04	�̵������N����Ӧ��һ������·
	data[0] = nRelayID + 1;
	//2 �̵��������״̬��RELAY_STATUES��	1	0x00	0X00-��   0X01-��
	if (bPower)
		data[1] = 0x01;
	else
		data[1] = 0x00;

	//�����ڷ������ݲ��ȴ���ȡ����
	int nLength = CCLPackCommunicationData::PackDataBase(0,nAddress,0xD2,0,true,data,ucSendData);

	return nLength;
}

//�̵����Զ�����
int CCLScanCardPackData::PackRelayAttribute(short nAddress,short nRelayID,
					bool bOverHeatOff,bool bOverHumidityOff,bool bSmogOff,
					unsigned char * ucSendData)
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	//�������ֽ�
	unsigned char data[CL_SEND_EFFECT_DATA_SIZE];
	memset(data, 0, CL_SEND_EFFECT_DATA_SIZE);

	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�
	//1 �̵������(RELAY_TAG)	1	0X01~0X04	�̵������N����Ӧ��һ������·
	data[0] = nRelayID + 1;
	//һ������  0x01	������Դģʽ    0x02	����ģʽ
	data[1] = 1;
	//��������	0x01	�ֶ�ģʽ��0x02	��ʱģʽ	0x03	�¿�ģʽ	0x04	ʪ��ģʽ
	data[2] = 0;
	//0x01	�������
	data[3] =  bSmogOff ? 1 : 0;
	//0x01	���¹���(�����¶Ȳ���Ϊ��)
	data[4] =  bOverHeatOff ? 1 : 0;
	//0x01��	��ʪ����
	data[5] =  bOverHumidityOff ? 1 : 0;

	//�����ڷ������ݲ��ȴ���ȡ����
	int nLength = CCLPackCommunicationData::PackDataBase(0,nAddress,0xD1,0,true,data,ucSendData);

	return nLength;
}

//�̵����򿪡��պ�����
int CCLScanCardPackData::PackRelayThreshold(short nAddress,short nRelayID,
						float fTemperatureMin,
						float fTemperatureMax,
						float fHumidityMin,
						float fHumidityMax,
						unsigned char * ucSendData)
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	//�������ֽ�
	unsigned char data[CL_SEND_EFFECT_DATA_SIZE];
	memset(data, 0, CL_SEND_EFFECT_DATA_SIZE);

	//9 �������ֽ�   16 �ӵ͵������У��ӵ͵��߷���  ��λ��ǰ����λ�ں�
	//1 �̵������(RELAY_TAG)	1	0X01~0X04	�̵������N����Ӧ��һ������·
	data[0] = nRelayID + 1;
	//�¶�����ֵ	2	-40.0��--123.0 ��		�¶�ֵ�����bitΪ����λ��0�������� 1����������λ��ǰ����λ�ں󡣾�ȷ��0.1�ȡ���λ����Ҫ���õ�ֵ����10���͸���ذ壬��ذ彫����ֵ����10��
	int nTemp = ((int)(abs(fTemperatureMax) * 10)) | (fTemperatureMax < 0 ? 0x8000 : 0x0000);
	CTool::ExchangeInteger(nTemp, data + 1, 2);
	//�¶�����ֵ	2	-40.0��--123.0 ��		�¶�ֵ�����bitΪ����λ��0�������� 1����������λ��ǰ����λ�ں󡣾�ȷ��0.1�ȡ���λ����Ҫ���õ�ֵ����10���͸���ذ壬��ذ彫����ֵ����10��
	nTemp = ((int)(abs(fTemperatureMin) * 10)) | (fTemperatureMin < 0 ? 0x8000 : 0x0000);
	CTool::ExchangeInteger(nTemp, data + 3, 2);
	//ʪ������ֵ	2	0.0-%--100.0%		ʪ��ֵ����ȷ��0.1�ȡ���λ����Ҫ���õ�ֵ����10���͸���ذ壬��ذ彫����ֵ����10��
	nTemp = ((int)(abs(fHumidityMax) * 10)) | (fHumidityMax < 0 ? 0x8000 : 0x0000);
	CTool::ExchangeInteger(nTemp, data + 5, 2);
	//ʪ������ֵ	2	0.0-%--100.0%		ʪ��ֵ����ȷ��0.1�ȡ���λ����Ҫ���õ�ֵ����10���͸���ذ壬��ذ彫����ֵ����10��
	nTemp = ((int)(abs(fHumidityMin) * 10)) | (fHumidityMin < 0 ? 0x8000 : 0x0000);
	CTool::ExchangeInteger(nTemp, data + 7, 2);


	//�����ڷ������ݲ��ȴ���ȡ����
	int nLength = CCLPackCommunicationData::PackDataBase(0,nAddress,0xD3,0,true,data,ucSendData);

	return nLength;
}
//��ȡ������ɫ��һ������ ĳ�ƣ������ص㣩�ڵư��е����λ������0���ϣ�1����, 2���£�3����
//nRGBIndex: 0:R��1:G, 2:B;
//nVirTualArray:���� 1����,��/��,��;��,��/��,�죻2����,��/��,��;��,��/��,�գ�3����,��/��,��;��,��/��,��
//nLine:��������ţ���ɫ�����������ݣ�����nLine<2
int CCLScanCardPackData::GetIndex(int nRGBIndex,int nVirTualArray, int nLine)
{
	if ( (0 != nRGBIndex && 1 != nRGBIndex && 2 != nRGBIndex)
		|| (1 != nVirTualArray && 2 != nVirTualArray && 3 != nVirTualArray)
		|| (nLine != 0 && nLine != 1) )
	{
		return -1;
	}

	int nIndex = 0;//����ƣ������ص㣩��Ӧ�����λ�ã�0���ϣ�1����, 2���£�3����
	if (1 == nVirTualArray && 0 == nLine)
	{
		switch (nRGBIndex)
		{
		case 0://R
			nIndex = 3;
			break;
		case 1://G
			nIndex = 1;
			break;
		case 2://B
			nIndex = 0;
			break;
		default:
			break;
		}
	}
	else if (1 == nVirTualArray && 1 == nLine)
	{
		switch (nRGBIndex)
		{
		case 0://R
			nIndex = 3;
			break;
		case 1://G
			nIndex = 0;
			break;
		case 2://B
			nIndex = 1;
			break;
		default:
			break;
		}
	}
	else if (2 == nVirTualArray && 0 == nLine)
	{
		switch (nRGBIndex)
		{
		case 0://R
			nIndex = 3;
			break;
		case 1://G
			nIndex = 1;
			break;
		case 2://B
			nIndex = 0;
			break;
		default:
			break;
		}
	}
	else if (2 == nVirTualArray && 1 == nLine)
	{
		switch (nRGBIndex)
		{
		case 0://R
			nIndex = 2;
			break;
		case 1://G
			nIndex = 1;
			break;
		case 2://B
			nIndex = 0;
			break;
		default:
			break;
		}
	}
	else if (3 == nVirTualArray && 0 == nLine)
	{
		switch (nRGBIndex)
		{
		case 0://R
			nIndex = 0;
			break;
		case 1://G
			nIndex = 1;
			break;
		case 2://B
			nIndex = 3;
			break;
		default:
			break;
		}
	}
	else if (3 == nVirTualArray && 1 == nLine)
	{
		switch (nRGBIndex)
		{
		case 0://R
			nIndex = 1;
			break;
		case 1://G
			nIndex = 0;
			break;
		case 2://B
			nIndex = 3;
			break;
		default:
			break;
		}
	}

	return nIndex;
}
