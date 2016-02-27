#include "CLScanCardPackData.h"
#include "../../util/util.h"

extern GLOBALVARIABLE g_GlobalVariable;				//工程的全局变量

CCLScanCardPackData::CCLScanCardPackData(void)
{
}

CCLScanCardPackData::~CCLScanCardPackData(void)
{
}

//打扫描卡主配置数据包包返回 int - 包长度
int CCLScanCardPackData::PackScanCardData(int nScanCardAddress,//扫描卡地址
										  short nAtlvcAddressSecond,
										  int nPackID,//顺序号
										  CStructSingleScanCard sScanCard, //扫描卡参数结构
										  unsigned char* ucSendData,//发送数据
										  int nEmptyByte)//校正前默认空字节数
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	unsigned int unDestAddress = (unsigned int)nScanCardAddress;
	unsigned char ucPackType = 0x40;

	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));

	//9 数据域字节   16 从低到高排列，从低到高发送  高位在前，低位在后
	//unsigned char ucTemp = 0;
	//当_a1时，不减1，否则都是减1, 控制类不需减一，数字类需减一
	switch (nPackID)
	{
	case 0://智能设置相关参数
		{
			//使用行信号D作为第二路时钟使带载高度加倍:扩区功能，左右进线：模组高度扩区；上下进线：宽度扩区
			//1  1字节 mod_width	0x1f	5--0	模组的像素宽度,做 减1处理
			if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)
			{
				ucData[0] = sScanCard.nModuleWidth - 1;
			}
			else
			{
				ucData[0] = (unsigned char)(sScanCard.nModuleWidth  * pow(2.0,sScanCard.nDCBlineClkEn)- 1);
			}

			//2 1字节 mod_height	8	0x1f	5--0	模组的像素高度,做 减1处理
			if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)
			{
				ucData[1] = (unsigned char)(sScanCard.nModuleHeight * pow(2.0, sScanCard.nDCBlineClkEn) -1);
			}
			else
			{
				ucData[1] = sScanCard.nModuleHeight - 1;
			}

			//3 1字节 scan_mode	8	0x35	7--4	扫描的模式，默认为0x3。做 减1处理
			// scan_mode			3--0	扫描的模式的高位
			ucData[2] = ((sScanCard.nScanMode - 1)<< 4)|( ((sScanCard.nScanMode - 1)>>4)& 0x0F);


			//4 1字节 card_section_row_num	8	0x0F	5--0	扫描卡每区行数，行数最大为64行。
			ucData[3] = (unsigned char)(sScanCard.nScanCardSectionRowNumber * pow(2.0, sScanCard.nDCBlineClkEn) - 1);

			//5 1字节  data_line_type	8	0x00	7--0	数据类型：默认为0x00，RGBR具体见附录3
			//Bit7-5：定义数据线的大类
			//0x0	红绿蓝分开, 0x1	红绿蓝合一三色1点串行
			//0x2	红绿蓝合一三色8点串行    0x3	红绿蓝合一三色16点串行
			//0x4	红绿蓝合一四色串行	Bit4-0：定义四线的具体情况。见附录3
			ucData[4] = (sScanCard.nDataLineTypeRange << 5) | (sScanCard.nDataLineType & 0x1F);


			//6 1字节 
			//row_decode_mode	8	0x2f	7--4	行译码方式：默认0x2
			//0x0	静态无译码			0x1	无译码芯片，直接驱动行管			0x2	  138译码		
			//0x3	139译码				0x4	145译码或138双0						0x5  154译码	
			//0x6	164译码				0x7	192译码								0x8	193译码
			//0x9	595译码				0xA	4096译码							0xB	
			// data_line_ctrl			3--0	控制4根数据线RB,B,G,RA的亮灭。
			//注：对用bit为0：亮，为1：灭。默认：都亮
			ucData[5] = (sScanCard.nRowDecodeMOde << 4 )|(sScanCard.nDataLineCtrl & 0x0F);


			//7 1字节
			//empty_insert_en	8	0x00	7	空点插入使能，即每多少点插入多少空点.默认0x0，不使能
			//insert_mode			6	插入空点方式，前插入还是后插入。
			//empty_dot_num			5--0	插入的空点数，每次最大只能插入64空点，做减1处理。
			ucData[6] = (sScanCard.bEmptyInsertEnable ? 0x80 : 0x00) | 
				((sScanCard.nInsertMode & 0x01)<< 6) | ((sScanCard.nEmptyDotNum - 1) & 0x1F);

			//8 2字节 
			//real_dot_num	16	0x0000	15--0	每多少点插入空点，做减1处理。
			short nRealDotNum = sScanCard.nRealDotNum - 1;
			CTool::ExchangeInteger(nRealDotNum, ucData + 7, 2);

			//9 1字节
			//oe_polarity	8	0x01	7	OE极性：默认为0x0  0x0-低有效    0x1-高有效
			//data_polarity			6-4	数据极性：默认为0x0    0x0-高电平点亮    0x1-低电平点亮   0x2-0x7	其他6种情况，预留
			//data_input_dir		3--0	数据方向：（从显示屏正面看）默认为从右到左0x1
			//0x0	从左往右     0x1	从右往左     0x2	从上往下     0x3	从下往上
			ucData[9] = ((sScanCard.nOePolarity & 0x01) << 7) | 
				((sScanCard.nDataPolarity & 0x07) << 4) | 
				(sScanCard.nDataInputDir & 0x0F);

			// 			//10 1字节
			//Drive_ic_type	8	0x00	Bit7	0:PWM 1:通用恒流
			//Bit6-0 指示具体的芯片类型		0x00	MBI5042
			//0x01	MBI5030		0x02	TC62D722		0x03	MBI5050   TLC5958
			short nICTpye = 0;
			short nPWMType = 0;
			if (_GENERAL == sScanCard.nChipType)//通用恒流
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
				nPWMType = _MBI5041 - 1;//类型和_MBI5041类型一样
			}
			else
			{
				nICTpye = 0;
				nPWMType = sScanCard.nChipType - 1;
			}
			ucData[10] = (nICTpye << 7) | (nPWMType & 0x7F);
			
			//Gclk_delay_num	8	0x40		芯片预充电时，GCLK的延迟时钟数。（三色分开时为R的GCLK延迟时钟数）
			//寄存器的最高位开辟为R、G、B的使能位，分别控制RGB的预充电功能;“0”为使能；“1”为关闭
			if (!sScanCard.bGClkCtrlByRGBEnable) //不由三色分开控制
			{
				ucData[11] = (0x00 << 7) | (unsigned char)sScanCard.nGClkDelay;
				ucData[12] = (0x00 << 7) | (unsigned char)sScanCard.nGClkDelay;
				ucData[13] = (0x00 << 7) | (unsigned char)sScanCard.nGClkDelay;
			}
			else //由三色分开控制
			{
				if (sScanCard.bGClkCtrlByREnable)
				{
					ucData[11] = (0x00 << 7) | (unsigned char)sScanCard.nGClkDelay;
				} 
				else
				{
					ucData[11] = (0x01 << 7) | (unsigned char)sScanCard.nGClkDelay;
				}

				//G的GCLK延迟时钟数
				if (sScanCard.bGClkCtrlByGEnable)
				{
					ucData[12] = (0x00 << 7) | (unsigned char)sScanCard.nGClkDelay_G;
				} 
				else
				{
					ucData[12] = (0x01 << 7) | (unsigned char)sScanCard.nGClkDelay_G;
				}

				//B的GCLK延迟时钟数
				if (sScanCard.bGClkCtrlByBEnable)
				{
					ucData[13] = (0x00 << 7) | (unsigned char)sScanCard.nGClkDelay_B;
				} 
				else
				{
					ucData[13] = (0x01 << 7) | (unsigned char)sScanCard.nGClkDelay_B;
				}
			}

			//预留2字节，填0x00
			memset( ucData + 14, 0 , 2);
			break;
		}
	case 1://扫描相关参数
		{
			//1 1字节 
			//scan_color_depth	8	0XD7    7--4	扫描卡扫描的颜色深度，取8~16的整数。默认为0xD。即读取SDRAM中的位面数。做减1处理。
			//origin_color_bit			3-0	原始颜色深度，比如8BIT,10bit，12bit，16bit
			ucData[0] = ((sScanCard.nScanColorDepth - 1) & 0x0F) << 4 |((sScanCard.nOrginColorBit - 1) & 0x0F);

			//2  1字节
			//virtual_disp_en	8	0x00	7	虚拟显示使能，默认为0x0，不使能（显示屏类型），芯片类型，虚拟像素排列方式
			//syn_refresh_en			6	同步刷新使能。默认为0x1，使能
			//dcb_line_clk_en			5--4	使能行信号DCB为时钟使带载高度加倍;默认为0
			//0x0	不使能   0x1	2倍     0x3	4倍
			//output_reverse			3--0	输出口逆序：默认为0x0
			//bit0=0x0	关闭数据线逆序		bit0=0x1	使能数据线逆序
			//Bit1=0x0	关闭扫描线逆序		Bit1=0x1	使能扫描线逆序
			//Bit2=0x0	单列输出			Bit2=0x1	双列输出
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

			//3 1字节
			//oe_delay_value	8	0x06	7-4	消隐延迟时钟数，消除暗亮，默认为0x00，做减1处理。
			//half_field_num			3--0	半场数，最大为9场.默认为0x6，减1处理
			//ucData[2] = (((sScanCard.nOeDelayValue - 1) & 0x0F) << 4 ) |
			//	((sScanCard.nHalfFieldNumber - 1) & 0x0F);

			//4 1字节
			//标准版：field_num	8	0x25	7--0	总场数，最大为136场，做减1处理。
			//PWM版：//freq_division_coef	8	0x18	7--0	分频系数，最大为200分频，值为200。默认为10分频，值为0x09。做减1处理。
			if (_GENERAL == sScanCard.nChipType)//标准版
			{
				ucData[3] = sScanCard.nFieldNum - 1;
			}
			else//PWM
			{
				ucData[3] = sScanCard.nPWMFreqDivisionCoeff - 1;
			}

			//5 1字节
			//标准版:full_field_num_a1	8	0x7f	6--0	全场数，最大为128场
			//PWM:duty_cycle_low_value_a1	8	0x05	7--0 占空比可调等级,数据移位时钟时钟占空比设置，设置低电平的计数值
			//默认为0x5。不做减1处理。
			if (_GENERAL == sScanCard.nChipType)
			{
				ucData[4] = (unsigned char)sScanCard.nFullFieldNumber;
			}
			else
			{
				ucData[4] = (unsigned char)sScanCard.nPWMDutyCycle;
			}

			//6 1字节
			//freq_division_coef	8	0x18	7--0	分频系数，最大为200分频，值为200。默认为10分频，值为0x09。做减1处理。
			ucData[5] = sScanCard.nFreqDivisionCoeff - 1;

			//7 1字节
			//duty_cycle_low_value_a1	8	0x05	7--0 占空比可调等级,数据移位时钟时钟占空比设置，设置低电平的计数值
			//默认为0x5。不做减1处理。
			ucData[6] = (unsigned char)sScanCard.nDutyCycle;

			//8 2字节
			//row_oe_clk_num	16	0x00C1  15--6	
			//标准版：行消隐时间数，默认为0x0063，由界面输入
			//PWM版：自动计算,(150Mhz/（Ref_freq*Scan_mode）-afield_clk_num	须大于等于0x0063。
			int nRowOeClkNum = 0x0063;

			//9 2字节
			//zone_clk_num	16	0x1207	15--0	每区移数据（125Mhz）的时钟数。
			int nZoneClkNum = sScanCard.GetZoneClkNum(g_GlobalVariable.nVersionType);
			CTool::ExchangeInteger(nZoneClkNum, ucData + 9, 2);

			//afield_clk_num_a1	20	0x1300	15--0,15-12	
			//标准版：(150Mhz/(Ref_freq*Field_num*Scan_mode))-100
			//PWM版：(颜色深度/ ref_doule_value)*(Pwm_clk_freq_div / freq_division_coef) 
			//须大于：16*Zone_clk_num
			long nAfieldClkNum = sScanCard.GetAfieldClkNum(g_GlobalVariable.nVersionType);


			nRowOeClkNum = sScanCard.GetRowOeClkNum(g_GlobalVariable.nVersionType) - 1;
			CTool::ExchangeInteger(nRowOeClkNum, ucData + 7, 2);

			//10 4字节
			//afield_clk_num_a1	20	0x1300	15--0,15-12	
			//min_oe_clk_num_a1	16	0x0026	11--0	
			//标准版：afield_clk_num_a1/起始场。起始场见附录：灰度等级对用的场信息表。
			//PWM版：配寄存器与逐点检测的时间(配芯片间隔时间)：
			//Config_ic_times*(Afield_clk_num+Row_oe_clk_num)*6.6ns
			//2013-3-13 将一场时钟数扩展到24bit（扫描相关参数（01h）），将OE移位延迟参数去掉。
			int nMinOeClkNum = sScanCard.GetMinOeClkNum(g_GlobalVariable.nVersionType);

			//long nAfieldClkNum1 = (nAfieldClkNum >> 16) | (nAfieldClkNum << 4); 

			long nAfieldClk1 = nAfieldClkNum & 0x0FFFF;
			long nAfieldClk2 = (nAfieldClkNum & 0xF0000);

			//PWM芯片：zone_clk_num[19-16] 3-0  每区移数据（125Mhz）的时钟数高4bit；
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

			//ref_doule_en	8	0x00	7	刷新率倍增使能 0 - 不使能，1 - 使能
			//ref_doule_value			6--0	刷新率倍增的倍数，默认是0，最大128倍，做减1
			/*short nRefDouEn = 0;
			if (sScanCard.nRefreshDoubleValue > 1)
				nRefDouEn = 1;
			nTemp = (nRefDouEn == 0 ? 0x00 : 0x80) | (sScanCard.nRefreshDoubleValue - 1);*/
			nTemp = sScanCard.nRefreshDoubleValue - 1;
			ucData[15] = (unsigned char)nTemp;
			break;
		}
	case 2://连线图计算相关参数
		{
			//1  1字节 
			//zhe_rdwr_mode	2	0x03	7--6	折处理模块读写折DPRAM的方式。默认为0
			//0：按列8读写，1：按箱体行读写
			//scan_mode_a1	6		5--0	扫描的模式，默认为0x3。
			ucData[0] = (sScanCard.nZheRdwrMode << 6) | (sScanCard.nScanMode & 0x3F);

			//使用行信号D作为第二路时钟使带载高度加倍
			//2 1字节 
			//mod_height_a1	8	0x1f	6--0	模组的像素高度
			if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)
			{
				ucData[1] = (unsigned char) (sScanCard.nModuleHeight * pow(2.0, sScanCard.nDCBlineClkEn));
			}
			else
			{
				ucData[1] = (unsigned char)sScanCard.nModuleHeight;
			}

			//3 1字节 
			//mod_width_a1	8	0x1f	6--0	模组的像素宽度
			if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)
			{
				ucData[ 2] = (unsigned char) sScanCard.nModuleWidth;
			}
			else
			{
				ucData[ 2] = (unsigned char)(sScanCard.nModuleWidth * pow(2.0, sScanCard.nDCBlineClkEn));
			}

			//4 1字节 
			//card_sect_row_num_a1	8	0x07	6--0	每区行数	
			ucData[ 3] = (unsigned char) (sScanCard.nScanCardSectionRowNumber  * pow(2.0, sScanCard.nDCBlineClkEn));


			//5 2字节  
			//mod_0mode_pix_num	16	0x003f	9--0	左右：模组宽度*区行数/几扫
			//上下：模组高度*区行数/几扫
			//艺术屏：mod_0mode_pix_num与数据线大类有关
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
				//红绿蓝分开
				break;
			case 1:
				//红绿蓝合一三色1点串行
			case 2:
				//红绿蓝合一三色8点串行
			case 3:
				//红绿蓝合一三色16点串行
				nMod0modePixNum = 3 * nMod0modePixNum;//(三倍)
				break;
			case 4:
				//红绿蓝合一四色串行
				nMod0modePixNum = 4 * nMod0modePixNum ;//(四倍)
				break;
			default:
				break;
			}
			nMod0modePixNum *= (int)pow(2.0, sScanCard.nDCBlineClkEn);

			//与空点插入有关
			if (sScanCard.bEmptyInsertEnable)
			{
				nMod0modePixNum = (int)((nMod0modePixNum / (sScanCard.nRealDotNum * 1.0)) * (sScanCard.nRealDotNum + sScanCard.nEmptyDotNum));
			}
			nMod0modePixNum--;
			//使用行信号D作为第二路时钟使带载高度加倍
			CTool::ExchangeInteger(nMod0modePixNum, ucData + 4, 2);


			//6 1字节 
			//zone_col_div_smode	8	0x01	5--0	上下：区列数/几扫
			//左右：区行数/几扫，需减1
			int nZoneColDivSmode;
			if (_SSeriesVersion == g_GlobalVariable.nVersionType)//S系列
			{
				nZoneColDivSmode = (int)(sScanCard.nScanCardSectionRowNumber_cabinet * pow(2.0, sScanCard.nDCBlineClkEn) / sScanCard.nScanMode - 1);
			} 
			else
			{
				nZoneColDivSmode = (int)(sScanCard.nScanCardSectionRowNumber * pow(2.0, sScanCard.nDCBlineClkEn) / sScanCard.nScanMode - 1);
			}
			ucData[6] = nZoneColDivSmode;


			//7 1字节 
			//zone_col_div_smode_a1	8	0x01	4--0	上下：区列数/几扫
			//左右：区行数/几扫
			ucData[7] = nZoneColDivSmode + 1;

			//8 2字节 
			//real_height_0mode	16	0x00ff	15--0	1区静态下所带的像素数，
			int nPixsPerSection = sScanCard.GetPixPerSetion(g_GlobalVariable.nVersionType) - 1;
			CTool::ExchangeInteger(nPixsPerSection, ucData + 8, 2);

			//9 2字节 
			//real_height_0mode_a1	16	0x00ff	15--0	
			nPixsPerSection++;
			CTool::ExchangeInteger(nPixsPerSection, ucData + 10, 2);

			//10 2字节 
			//real_empty_height_0mode	16	0x0100	15--0	实点空点像素数。空点插入使能才有效, 
			//real_height_0mode_a1*(每多少点插入空点+插入空点数)/每多少点插入空点
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


			//预留8字节 
			memset(ucData + 14, 0, 2);

			//11 2字节  模组内区内静态像素点数，需要加上插入的空点数
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
	case 3://连线图计算相关参数
		{
			//1  2字节 
			//one_row_bytes	16	0x017f	10--0	
			//左右实像素或调亮：卡宽*3。卡宽凑16的倍数
			//上下实像素或调亮：区列数*48.做减1处理;   
			//左右虚拟或调亮：卡宽*16.卡宽凑16的倍数;
			//上下虚拟或调亮：不支持;    
			//左右并调色：卡宽*9。注：卡宽凑16的倍数;
			//上下并调色：区列数*144.做减1处理
			int nOneRowByte = 0x017F;
			int nCardWidth = sScanCard.nScanCardWidth % 16 ? 
				(sScanCard.nScanCardWidth / 16 + 1) * 16 : sScanCard.nScanCardWidth ;
//			int nSectionWidth16 = sScanCard.nSectionWidth % 16 ?
//				( sScanCard.nSectionWidth / 16 + 1) * 16 : sScanCard.nSectionWidth ;
			int nSectionWidth16 = sScanCard.nCard_zone_width % 16 ?
				( sScanCard.nCard_zone_width / 16 + 1) * 16 : sScanCard.nCard_zone_width ;
			if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//调亮
			{
				if (sScanCard.nScreenType == DISPLAY_TYPE_REAL)//实像素
				{
					if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//左右进线
					{
						if (sScanCard.bExtendedEnable || sScanCard.bExtendedEnableEx)
						{
							//扩区：区宽（16的倍数）* 横向区数 * 3
							nOneRowByte =  nSectionWidth16 * sScanCard.nSectionHorNum * sScanCard.nCard_zone_Num * 3 - 1;
						}
						else
						{
							nOneRowByte = nCardWidth * 3 - 1;
						}
					}
					else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//上下进线
					{
						nOneRowByte = (int)(sScanCard.nScanCardSectionRowNumber * pow(2.0, sScanCard.nDCBlineClkEn) * 48 - 1);
						//nOneRowByte = nCardWidth * 3 - 1;
					}

				}
				else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//虚拟屏
				{
					if (sScanCard.bVirtvalDisp)
					{
						if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//左右进线
						{
							if (0 == sScanCard.nVirTualArray)
							{
								//虚拟屏虚拟显示，4灯虚拟
								nOneRowByte = nCardWidth * 16 - 1;
							}
							else if (1 == sScanCard.nVirTualArray
								|| 2 == sScanCard.nVirTualArray
								|| 3 == sScanCard.nVirTualArray )
							{
								//三色灯虚拟一像素点为卡宽*12（2为两行数据）
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
						if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//左右进线
						{
							nOneRowByte = nCardWidth * 3 - 1;
						}
						else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//上下进线
						{
							nOneRowByte = (int)(sScanCard.nScanCardSectionRowNumber * pow(2.0, sScanCard.nDCBlineClkEn) * 48 - 1);
						}
					}
				}
			}
			else if (sScanCard.nDotCorrectTye == 2)//调色
			{
				if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//左右进线
				{
					//nOneRowByte = nCardWidth * 9 - 1;
					nOneRowByte = (nSectionWidth16 * sScanCard.nSectionHorNum * sScanCard.nCard_zone_Num) * 9 - 1;
				}
				else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//上下进线
				{
					nOneRowByte = (int)(sScanCard.nScanCardSectionRowNumber * pow(2.0, sScanCard.nDCBlineClkEn) * 144 - 1);
				}
			}
			CTool::ExchangeInteger(nOneRowByte, ucData , 2);


			//2 2字节 
			//one_row_bytes_a1	16	0x0180	11--0   实际卡宽*3

			nOneRowByte = sScanCard.nScanCardWidthReal * 3;
			CTool::ExchangeInteger(nOneRowByte, ucData + 2, 2);

			//3 2字节 
			//one_row_bytes2_a1	16	0x0180	10--0	左右虚拟:带载宽度*6
			//上下实像素：带载宽度*3
			//其他：暂定为默认。
			int nOneRowByte2 = 0;
			if (sScanCard.nScreenType == DISPLAY_TYPE_REAL)//实像素
			{
				if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//上下进线
				{
					nOneRowByte2 = sScanCard.nScanCardWidthReal * 3;
				}
				else
				{
					nOneRowByte2 = nOneRowByte;
				}
			}
			else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//虚拟屏
			{
				if (sScanCard.bVirtvalDisp)
				{
					if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//左右进线
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
					if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//上下进线
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

			//4 2字节 
			int nTmp = 0;
			//card_width16	16	0x007f	11--0	左右：卡宽(须凑成16倍数),做减1处理
			//nCardWidth --;
			// memcpy(data + nLen + 6, &nCardWidth, 2);
			if ( sScanCard.bExtendedEnable ||sScanCard.bExtendedEnableEx)
			{
				//扩区：区宽（16的倍数）*横向区数
				//nTmp = nSectionWidth16 * sScanCard.nSectionHorNum - 1 ;
				
				//扩区：区宽（16的倍数）*模组内区个数 * 模组外区个数
				nTmp = nSectionWidth16 * sScanCard.nSectionHorNum * sScanCard.nCard_zone_Num - 1 ;

			}
			else
			{
				nTmp = nCardWidth - 1 ;
			}
			CTool::ExchangeInteger(nTmp, ucData + 6, 2);

			//5 2字节  
			//card_width16_a1	16	0x0080	12--0	Card_width16+1
			//nCardWidth ++;
			// memcpy(data + nLen + 8, &nCardWidth, 2);
			nTmp ++ ;
			CTool::ExchangeInteger(nTmp, ucData + 8, 2);



			//6 2字节 
			//使用行信号D作为第二路时钟使带载高度加倍：艺术屏扩区
			//card_height_mode16	16	0x00ff	15--0	1区静态下所带的像素数，
			//左右:卡宽(须凑成16倍数)*区行数/(几扫)，上下:卡高*区行数/(几扫)，
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

			//7 2字节 
			//card_height_mode16_a1	16	0x0100	15--0	Card_height_0mode+1
			nPixsPerSection++;
			CTool::ExchangeInteger(nPixsPerSection, ucData + 12, 2);

			//区宽
			CTool::ExchangeInteger(nSectionWidth16 ,ucData + 14, 2) ; 
			break;
		}
	case 4://连线图计算相关参数
		{
			//1 
			//smart_addr_sel	8	0x00	1--0	Bit7-2	预留
			//Bit1-0	智能设置选择位.1，选采集卡发出的   2，选上位机发出的。
			ucData[0] = 2;

			//2
			//card_width	16	0x007f	15--0	卡所带的像素宽度，虚拟时也指实际的像素数
			int nCardWidth = sScanCard.nScanCardWidth % 16 ? 
				(sScanCard.nScanCardWidth / 16 + 1) * 16 : sScanCard.nScanCardWidth;
			int nScanCardWidth = nCardWidth - 1;
			CTool::ExchangeInteger(nScanCardWidth, ucData + 1, 2);

			//3
			//card_height	16	0x005f	15--0	卡所带的像素高度
			int nScanCardHieght = sScanCard.nScanCardHeight - 1;
			CTool::ExchangeInteger(nScanCardHieght, ucData + 3, 2);

			//4 Drive_ic_reg	16			配驱动芯片的寄存器值,见附件驱动芯片的寄存器值
			if (_MBI5030 == sScanCard.nChipType || _TC62D722 == sScanCard.nChipType 
				||_TLC5948 == sScanCard.nChipType)//MBI5030,TC62D722,TLC5948
			{
				int nDriveIC = (sScanCard.nPWMOutputMode << 10 |0x0800) | ((sScanCard.bDotOpenDetection ? 1 : 0) << 13 |0x4000);
				CTool::ExchangeInteger(nDriveIC, ucData + 5, 2);
			}
			else if (_MBI5040 == sScanCard.nChipType)//MBI5040
			{
				//15bit Thermal_shut:缺省0                0:Disable;1:Enable
				//14bit NA
				//13bit Error_detc                        0:Disable;1:Enable,缺省0 可设置根据开路检测 
				//12bit NA
				//11bit Auto_sync:缺省1                   0: Auto;1: Manual
				//10bit Scramble_PWM:缺省1                0: S-PWM;1: PWM
				//9bit-8bit Thresh_vol_short[1:0]缺省00   00:Disable, 01:0.4V	10:0.5V	11：0.73V
				//7bit Switch_speed 缺省1                 0:Low;1:High,
				//4bit-6bit 默认为0
				//3bit  PWM_gray_scale: 缺省0             0:16bit;1:12bit	可设置根据扫描卡颜色深度来设置
				//2bit 默认为0 
				//1bit 默认为0 
				//0bit Count_mode:缺省00:Continuous;1:One-shot
				//NA-软件下发0；
				//int nDriveIC = (((sScanCard.nScanColorDepth ==16)? 1: 0)<< 15 & 0x8000) | ((sScanCard.nPWMOutputMode<<14) & 0x4000) | 0x2080 | (((sScanCard.bDotOpenDetection? 1:0) << 10)& 0x0400);
				int nDriveIC = ((sScanCard.bDotOpenDetection ? 1 : 0) << 13 )| (sScanCard.nPWMOutputMode << 10| 0x0800 )  |((sScanCard.nScanColorDepth ==16)? 0: 1)<< 3;
				CTool::ExchangeInteger(nDriveIC, ucData + 5, 2);
			}
			else if (_MBI5041 == sScanCard.nChipType || _MBI5045 == sScanCard.nChipType
				|| _MBI5043 == sScanCard.nChipType)
			{
				//15bit NA                
				//14bit NA
				//13bit Error_detc  5041,5043固定为0,5045根据用户选择      0:Disable;1:Enable,缺省0 可设置根据开路检测 
				//12bit NA
				//11bit NA                   
				//10bit NA               
				//9bit-8bit NA   
				//6-7bit NA              
				//4bit NA		1：使能预充电，去除下残影,0：关闭预充电功能
				//3bit  NA		5041,5045固定为0，5043:GCLK双沿采样	0：关闭，	1：开启，缺省0
				//2bit NA
				//1bit NA		5041,5045固定为0，5043:PWM模式选择	0: 16bit，	1: 10bit
				//0bit NA
				//NA-软件下发0；
				int nDriveIC = 0;
				if (_MBI5041 == sScanCard.nChipType)
				{
					nDriveIC = (sScanCard.bChipPrecharge ? 1 : 0) << 4 ;
				}
				else if (_MBI5043 == sScanCard.nChipType)
				{
					nDriveIC = ((sScanCard.bChipPrecharge ? 1 : 0) << 4) 
						| ((sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5043.bGCLKDoublesampling ? 1 : 0) << 3)//MBI5153 GCLK双沿采样	0：关闭， 1：开启
						| (sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5043.nPWMMode << 1);					//MBI5153 PWM Modo选择	0: 16bit，1: 10bit
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
				// Bit11--Bit13	BC(100)：全局亮度调节
				// Bit9--Bit10	LGSE1_R(00)：红色低灰增强
				// Bit7--Bit8	LGSE1_G(00)：绿色低灰增强
				// Bit5--Bit6	LGSE1_B(00)：蓝色低灰增强
				// Bit4			GDLY(1)：输出通道延迟使能
				// Bit2--Bit3	TD0(01)：输入数据延迟设定
				// Bit0--Bit1	LODVTH(01)：开路检测电压设定
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
				//Bit15	预充电模式：0关闭，1开启
				//Bit14	PWM计数模式：0正数，1倒数
				//Bit7	_MBI5152 灰阶模式：0=16bit，1=14bit	Bit7	_MBI5153 灰阶模式：0=14bit，1=13bit
				//Bit6	GCLK倍频：0禁用，1使能
				int nDrive_ic_reg = sScanCard.nDrive_ic_reg.nPre_Charge1 << 15
					| sScanCard.nDrive_ic_reg.nPwm_Count_Mode << 14
					| (sScanCard.bDotOpenDetection ? 1 : 0) << 13
					| sScanCard.nDrive_ic_reg.nGray_Mode << 7
					| sScanCard.nDrive_ic_reg.nEnable_GCLK << 6;
				CTool::ExchangeInteger(nDrive_ic_reg, ucData + 5, 2);
			}
			else if (_MBI5153_E == sScanCard.nChipType || _MBI5155 == sScanCard.nChipType)
			{
				//R_REG1：MBI5153红色驱动芯片第1组寄存器，默认值为0x9F2B
				ucData[5] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RHigh;
				ucData[6] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1RLow;
			}
			else
			{
				CTool::ExchangeInteger(0x0802, ucData + 5, 2);
			}

			//5 Mzone_width  16	模组内区宽
			CTool::ExchangeInteger(sScanCard.nSectionWidth, ucData + 7, 2);

			//6	_TLC5958 Drive_ic_reg2 16 配驱动芯片的寄存器值,见附件TLC5958_FC2  驱动芯片的寄存器值
			// 其他芯片默认
			if (_TLC5958 == sScanCard.nChipType)
			{
				//reg2 见协议
				// Bit15		NA
				// Bit14		NA
				// Bit13		NA
				// Bit12		NA
				// Bit11		NA
				// Bit10		NA
				// Bit9 		NA
				// Bit8 		NA
				// Bit7 		NA
				// Bit5--Bit6	LGSE2(00)：低灰增强
				// Bit4 		PWM_MODE(0)：打散模式
				// Bit3 		EMI_R(0)：红色EMI削减
				// Bit2 		EMI_G(0)：绿色EMI削减
				// Bit1 		EMI_B(0)：蓝色EMI削减
				// Bit0 		PRE_CHARGE(0)：预充电模式

				int nDrive_ic_reg2 = sScanCard.nDrive_ic_reg.nGlobal_Lgse << 5
					| sScanCard.nDrive_ic_reg.nPVM_Mode << 4
					| sScanCard.nDrive_ic_reg.nEMI_R << 3
					| sScanCard.nDrive_ic_reg.nEMI_G << 2
					| sScanCard.nDrive_ic_reg.nEMI_B << 1
					| sScanCard.nDrive_ic_reg.nPre_Charge;
				CTool::ExchangeInteger(nDrive_ic_reg2, ucData + 9, 2);

				//预留5位
				memset(ucData + 11, 0, 5);
			}
			else if (_MBI5152 == sScanCard.nChipType || _MBI5153 == sScanCard.nChipType)
			{
				//Bit15	_MBI5153 双倍刷新率：0关闭，1开启 MBI5152 NA
				//Bit13-Bit12 开路检测电压：00: 0.3V，01: 0.4V，10: 0.5V，11: 0.6V
				//Bit11-Bit10 红绿蓝IC识别：01红色，10绿色，11蓝色
				//Bit9-Bit7 首行偏暗调节（蓝色）：000: 0 ns，100: 18ns，001: 6 ns，101: 21ns，010: 9 ns，110: 27ns，011: 15 ns，111: 33ns
				//Bit6-Bit4 首行偏暗调节（绿色）：000: 0 ns，100: 18ns，001: 6 ns，101: 21ns，010: 9 ns，110: 27ns，011: 15 ns，111: 33ns
				//Bit3-Bit1 首行偏暗调节（红色）：000: 0 ns，100: 18ns，001: 6 ns，101: 21ns，010: 9 ns，110: 27ns，011: 15 ns，111: 33ns
				//Bit0 倒数模式高电平不延伸：0关闭，1开启

				int nDrive_ic_reg2 = sScanCard.nDrive_ic_reg.nDouble_RefreseRate << 15
					| 0x4000
					| sScanCard.nDrive_ic_reg.nVoltage << 12
					/*| sScanCard.nDrive_ic_reg.nIC_Recognition << 10*/
					| sScanCard.nDrive_ic_reg.nAdjust_Blue << 7
					| sScanCard.nDrive_ic_reg.nAdjust_Green << 4
					| sScanCard.nDrive_ic_reg.nAdjust_Red << 1
					| sScanCard.nDrive_ic_reg.nImhl_DoNotStretch;
				CTool::ExchangeInteger(nDrive_ic_reg2, ucData + 9, 2);

				//预留5位
				memset(ucData + 11, 0, 5);
			}
			else if (_MBI5153_E == sScanCard.nChipType || _MBI5155 == sScanCard.nChipType)
			{
				//R_REG2：MBI5153红色驱动芯片第2组寄存器，默认值为0x4600
				ucData[9] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2RHigh;
				ucData[10] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2RLow;
			}
			else
			{
				//预留7位
				memset(ucData + 9, 0, 7);
			}

			//网口优先设置
			//锁定优先网口
			ucData[11]= sScanCard.nNetPortPriority |
						(sScanCard.bLockNetPort ? 1 : 0) << 1;
			
			break;
		}
	case 5://校正用计算相关参数
		{
			//1  2字节 
			//one_row_coef_num	16	0x017f	15--0	1行视频数据对用的有效校正参数。
			//实像素单点调亮：左右进线：卡宽*3；上下进线：区列数*16*3
			//虚拟单点调亮：卡宽*4
			//实像素单点调色：卡宽*9
			//调亮：卡宽是16的整数倍，调色：实际卡宽
			int nCardWidth = 0 ;
			if ( sScanCard.bExtendedEnable ||sScanCard.bExtendedEnableEx)
			{
				//扩区：卡宽 = 区宽凑16的倍数 * 横向区数
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
			if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//调亮
			{
				if (sScanCard.nScreenType == DISPLAY_TYPE_REAL)//实像素
				{
					if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//左右进线
					{
						nOneRowCoefNum = nCardWidth * 3 - 1;
					}
					else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//上下进线
					{
						nOneRowCoefNum = (int)(sScanCard.nScanCardSectionRowNumber * pow(2.0, sScanCard.nDCBlineClkEn) * 16 * 3 - 1);
					}
				}
				else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//虚拟屏
				{
					if (sScanCard.bVirtvalDisp)
					{
						if (0 == sScanCard.nVirTualArray)
						{
							nOneRowCoefNum = nCardWidth * 4 - 1;
						} 
						else if (1 == sScanCard.nVirTualArray || 2 == sScanCard.nVirTualArray || 3 == sScanCard.nVirTualArray)
						{
							//三灯一像素，两行数据
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
			else if (sScanCard.nDotCorrectTye == 2)//调色
			{
				if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//左右进线
				{
					nOneRowCoefNum = nCardWidth * 9 - 1;
				}
				else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//上下进线
				{
					nOneRowCoefNum = (int)(sScanCard.nScanCardSectionRowNumber * pow(2.0, sScanCard.nDCBlineClkEn) * 144 - 1);
				}
			}
			// memcpy(data + nLen, &nOneRowCoefNum, 2);
			CTool::ExchangeInteger(nOneRowCoefNum, ucData, 2);


			//2 2字节 
			//one_row_coef_all_a1	16	0x0200	15--0	1行视频数据对用总的校正参数。
			//实像素虚拟像素单点调亮：左右进线：[(mod_width * 4 + x)/ 32] * 32 * mod_col； 上下进线：mod_width  * 4 *mod_row
			//	实像素单点调色：[(mod_width * 9 + x)/ 32] * 32 * mod_col				
			//	注：x是指相加后可以整除32.如：模宽为15的调亮，则x=2
			//	注：Mod_col指横向模组数

			int nOneRowCoefNumAllA1 = 0x0200;
			short nModuleWidth16 = sScanCard.nModuleWidth % 16 ? 
				(sScanCard.nModuleWidth / 16 + 1) * 16 : sScanCard.nModuleWidth;
			nModuleWidth16 = nModuleWidth16 > 42 ? sScanCard.nModuleWidth : nModuleWidth16;

			int nTemp = 0;
			if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//调亮
			{
				if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//全彩
				{
					if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//左右进线
					{
						//nTemp = (sScanCard.nModuleWidth * 4) % 32 ? 
						//	(sScanCard.nModuleWidth  * 4 / 32 + 1) * 32 :sScanCard.nModuleWidth  * 4;
						nTemp = (nModuleWidth16 * 4) % 32 ? 
							(nModuleWidth16  * 4 / 32 + 1) * 32 : nModuleWidth16 * 4;
						nOneRowCoefNumAllA1 = nTemp * sScanCard.nModuleHorNum;
					}
					else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//上下进线
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
			else if (sScanCard.nDotCorrectTye == 2)//调色
			{
				if (sScanCard.nScreenType == DISPLAY_TYPE_REAL ||
					(sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && !sScanCard.bVirtvalDisp))//实像素或虚拟屏不虚拟显示
				{
					nTemp = (nModuleWidth16 * 9) % 32 ? 
						(nModuleWidth16 * 9 / 32 + 1) * 32 :nModuleWidth16 * 9;
					nOneRowCoefNumAllA1 = nTemp * sScanCard.nModuleHorNum;
				}
			}
			CTool::ExchangeInteger(nOneRowCoefNumAllA1, ucData + 2, 2);

			//3 2字节 
			//row_coef_novalid_nums_a1	16	0x0080	15--0	1行视频数据对应的无效校正参数。
			//实像素单点调亮：one_row_coef_all_a1-卡宽*3
			//	虚拟单点调亮：one_row_coef_all_a1-卡宽*4
			//	实像素单点调色：one_row_coef_all_a1-卡宽*9

			int nRowCoefNovailNumAl = 0x0080;
			nRowCoefNovailNumAl = nOneRowCoefNumAllA1 - (nOneRowCoefNum + 1);
			CTool::ExchangeInteger(nRowCoefNovailNumAl, ucData + 4, 2);

			//4 1字节 
			//mod_row_coef_sdram_col_a1 	8	0x10	7--0	模组1行像素的校正参数在sdram占用的空间：
			//实像素虚拟像素单点调亮：左右进线：[(mod_width * 4 + x)/ 32] * 16 -16；上下进线：[(mod_width * 16 * 4 + x)/ (32 * 16)] * 16 -16；				
			//	实像素单点调色：[(mod_width * 9 + x)/ 32] * 16 -16				
			//	注：x是指相加后可以整除32.如：模宽为15的调亮，则x=2
			int nModeRowCoefSdramCol = 0x10;
			if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//调亮
			{
				if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//全彩
				{
					if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//左右进线
					{
						//nTemp = (sScanCard.nModuleWidth * 4) % 32 ? 
						//	(sScanCard.nModuleWidth * 4 / 32 + 1)  : sScanCard.nModuleWidth * 4 / 32;
 						nTemp = (nModuleWidth16 * 4) % 32 ? 
 							(nModuleWidth16 * 4 / 32 + 1)  : nModuleWidth16 * 4 / 32;
					}
					else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//上下进线
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
			else if (sScanCard.nDotCorrectTye == 2)//调色
			{
				if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || 
					(sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && !sScanCard.bVirtvalDisp))//实像素或虚拟屏不虚拟显示
				{
					nTemp = (nModuleWidth16 * 9) % 32 ? 
						(nModuleWidth16 * 9 / 32 + 1) : nModuleWidth16 * 9 / 32;
					nModeRowCoefSdramCol = nTemp * 16 -16;
				}
			}
			int nnModeRowCoefSdramColLow = nModeRowCoefSdramCol & 0xFF;
			memcpy(ucData + 6, &nnModeRowCoefSdramColLow, 1);

			//5 2字节  
			//mod_coef_bytes_a1	16	0x2000	15--0	1个模组视频数据对用总的校正参数。
			//实像素虚拟像素单点调亮：左右进线：[(mod_width * 4 + x)/ 32] * 64 * mod_heigh； 上下进线：模组宽度*8*模组高度
			//实像素单点调色：[(mod_width * 9 + x)/ 32] * 64 * mod_heigh
			//注：x是指相加后可以整除32.如：模宽为15的调亮，则x=2
			long nModeCoefByteA1 = sScanCard.GetModCoefByte(nEmptyByte);
			int nModeCoefByteA1Low = nModeCoefByteA1 & 0xFFFF;
			CTool::ExchangeInteger(nModeCoefByteA1Low, ucData + 7, 2);

			short nModuleVerNum = 0;
			short nModuleHorNum = 0;

			if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)//左右进线
			{
				nModuleHorNum = sScanCard.nModuleHorNum;
				nModuleVerNum = (int)(sScanCard.nModuleVerNum / pow(2.0, sScanCard.nDCBlineClkEn));
			}
			else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//上下进线
			{
				nModuleHorNum = 16;//sScanCard.nModuleHorNum / pow(2.0, sScanCard.nDCBlineClkEn);
				nModuleVerNum = sScanCard.nModuleVerNum;
			}

			//6 1字节 
			//mod_colrow	8	0x32	7--0	Bit7-4	1卡所带的横向模组数，减1处理
			//Bit3-0	1卡所带的竖向模组数,减1处理
			nTemp =  (nModuleHorNum - 1) << 4 | (nModuleVerNum - 1);
			memcpy(ucData + 9, &nTemp, 1);

			//7 1字节 
			//mod_col_a1	8	0x04	4--0	1卡所带的横向模组数

			memcpy(ucData + 10, &nModuleHorNum, 1);

			//8 1字节 
			//mod_row_a1	8	0x03	4--0	1卡所带的竖向模组数
			memcpy(ucData + 11, &nModuleVerNum, 1);

			//9 1字节
			//mod_row_coef_sdram_col_a1 	8	0x10	高4位	模组1行像素的校正参数在sdram占用的空间：
			//实像素虚拟像素单点调亮：左右进线：[(mod_width * 4 + x)/ 32] * 16 -16；上下进线：[(mod_width * 16 * 4 + x)/ (32 * 16)] * 16 -16；				
			//	实像素单点调色：[(mod_width * 9 + x)/ 32] * 16 -16				
			//	注：x是指相加后可以整除32.如：模宽为15的调亮，则x=2
			int nnModeRowCoefSdramColHight = (nModeRowCoefSdramCol & 0xFF00) >> 8;
			memcpy(ucData+ 12, &nnModeRowCoefSdramColHight, 1);

			//10 1字节
			//mod_coef_bytes_a1	16	0x2000	高4位	1个模组视频数据对用总的校正参数。
			//实像素虚拟像素单点调亮：左右进线：[(mod_width * 4 + x)/ 32] * 64 * mod_heigh； 上下进线：模组宽度*8*模组高度
			//实像素单点调色：[(mod_width * 9 + x)/ 32] * 64 * mod_heigh
			//注：x是指相加后可以整除32.如：模宽为15的调亮，则x=2
			int nModeCoefByteA1Hight = (nModeCoefByteA1 >> 16) & 0xFF;
			CTool::ExchangeInteger(nModeCoefByteA1Hight, ucData + 13, 1);

			//11 1字节
			//排线方式：0x00：红A,绿/蓝,红B；
			//	0x01：蓝,绿/空,红;绿,蓝/空,红
			//	0x02: 蓝,绿/空,红;蓝,绿/红,空
			//	0x03: 红,绿/空,蓝;绿,红/空,蓝
			CTool::ExchangeInteger(sScanCard.nVirTualArray, ucData + 14, 1);
			//预留1字节 
			memset(ucData + 15, 0, 1);
			break;
		}
	case 6: //MBI5153寄存器参数（15h）
		{
			// 1-2	R_REG3	16	0xC003	15-0	MBI5153红色驱动芯片第3组寄存器，默认值0xC003
			ucData[0] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RHigh;
			ucData[1] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3RLow;

			// 3-4	G_REG1	16	0xDX2X	15-0	MBI5153绿色驱动芯片第1组寄存器，默认值0xDF2B
			ucData[2] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1GHigh;
			ucData[3] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1GLow;

			// 5-6	G_REG2	16	0x4500	15-0	MBI5153绿色驱动芯片第2组寄存器，默认值0x4500
			ucData[4] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2GHigh;
			ucData[5] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2GLow;

			// 7-8	G_REG3	16	0x5003	15-0	MBI5153绿色驱动芯片第3组寄存器，默认值0x5003
			ucData[6] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3GHigh;
			ucData[7] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3GLow;

			// 9-10	B_REG1	16	0xDX2X	15-0	MBI5153蓝色驱动芯片第1组寄存器，默认值0xDF2B
			ucData[8] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1BHigh;
			ucData[9] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg1BLow;

			// 11-12	B_REG2	16	0x6500	15-0	MBI5153蓝色驱动芯片第2组寄存器，默认值0x6500
			ucData[10] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2BHigh;
			ucData[11] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg2BLow;

			// 13-14	B_REG3	16	0x4003	15-0	MBI5153蓝色驱动芯片第3组寄存器，默认值0x4003
			ucData[12] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3BHigh;
			ucData[13] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5153_E.nReg3BLow;

			//15-16预留2字节 
			memset(ucData + 14, 0x00, 2);
			break;
		}
	case 7: //5155渐变过度优化（16h）
		{
			// 1	δt	8	0x00	7-0	MBI5155 第513/257个GCLK的低电平宽度
			// 2	DHT	8	0x1E	7-0	MBI5155 第513/257个GCLK的高电平宽度
			// 3	 δf	8	0x00	7-0	MBI5155 第1个GCLK的高电平宽度
			// 4	 DG_H	8	0x26	7-0	MBI5155 第514/258个GCLK的高电平宽度
			// 5	 DG_L	8	0x19	7-0	MBI5155 第514/258个GCLK的低电平宽度
			ucData[0] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaT;
			ucData[1] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDHT;
			ucData[2] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDeltaF;
			ucData[3] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_H;
			ucData[4] = sScanCard.nDrive_ic_reg.sDrive_ic_reg_MBI5155.nDG_L;

			//预留字节				预留11字节
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
	case 7://MBI5155渐变过渡优化（16h）
		nPackID = 0x16;
		break;
	default:
		break;
	}
	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond,unDestAddress,ucPackType,nPackID,false,ucData,ucSendData);
	return (nLen);
}

//打单点校正启动结束设置包(0x91)
int CCLScanCardPackData::PackStartOrEnd(int nScanCardAddress,//扫描卡地址
										short nAtlvcAddressSecond,
										bool bStart, 
										short nType, 
										short nModuleRow,
										short nModulCol,
										unsigned char* ucSendData)//发送数据
{
	memset(ucSendData, 0, CL_MAX_BUF_NUMBER);

	unsigned int unDestAddress = (unsigned int)nScanCardAddress;
	unsigned char ucPackType = 0x91;

	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));

	bool bAnswer = false;

	//单点校正数据包 DOT_CORR_CMD 	1	0x00	0x01	启动单点校正数据包，
	//0x02	单点校正数据包结束，默认值0x00不处理
	if (nType == 0)//启动
	{
		if (bStart)
		{
			ucData[5] = 0x01;
		}
	}
	else if (nType == 1)
	{
		//7 应答标识 0X00-无应答，0x01-有应答
		bAnswer = true;
		//0x11	启动读取扫描卡上SPI_FLASH中的校正数据到SDRAM
		ucData[5] = 0x11;
	}
	if (nType == 2)//结束
	{
		//7 应答标识 0X00-无应答，0x01-有应答
		bAnswer = true;
		//结束
		ucData[5] = 0x02;
	}
	else if (nType == 3)
	{
		//7 应答标识 0X00-无应答，0x01-有应答
		bAnswer = true;
		//0x21	启动读取（从模组上读）单个模组的校正数据写到卡上的SPI_flash（mod_col，mod_row字节有效）
		ucData[5] = 0x21;
	}
	else if (nType == 4)
	{
		//7 应答标识 0X00-无应答，0x01-有应答
		bAnswer = true;
		//0x22	启动读取卡上的SPI_flash的校正数据写到单个模组上（mod_col，mod_row字节有效）
		ucData[5] = 0x22;
	}
	else if (nType == 5)
	{
		//7 应答标识 0X00-无应答，0x01-有应答
		bAnswer = true;
		//0x31	启动读取整个箱体的校正数据写到卡上的SPI_flash（mod_col，mod_row字节无效）
		ucData[5] = 0x31;
	}
	else if (nType == 6)
	{
		//7 应答标识 0X00-无应答，0x01-有应答
		bAnswer = true;
		//0x32	启动读取卡上的SPI_flash的校正数据写到整个箱体的模组上（mod_col，mod_row字节无效）
		ucData[5] = 0x32;
	}

	//MODULE_ROW	1	0x00		单点校正的模组列地址
	CTool::ExchangeInteger(nModulCol, ucData + 6, 1);
	//MODULE_COL	1	0x00		单点校正的模组行地址
	CTool::ExchangeInteger(nModuleRow, ucData + 7, 1);


	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond,unDestAddress,ucPackType,0,bAnswer,ucData,ucSendData);

	return (nLen);
}

//打HUB参数包
int CCLScanCardPackData::PackHUBPara(int nAddress,//扫描卡地址
									 short nAtlvcAddressSecond,
										CStructSingleScanCard sScancard,
										unsigned char* ucSendData)//发送数据
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));

	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后

	//SCAN_CARD_SECTION_NUM	1	9	扫描卡的区数，区数最大为16
	short nScanCardSectionNumber = 1;
	if (sScancard.nDataInputDir == 0 || sScancard.nDataInputDir == 1)//左右
	{
 		nScanCardSectionNumber = sScancard.nScanCardHeightReal / sScancard.nScanCardSectionRowNumber;
		if (0 != sScancard.nScanCardHeightReal % sScancard.nScanCardSectionRowNumber)
		{
			nScanCardSectionNumber += 1;
		}
//  	nScanCardSectionNumber *= sScancard.nSectionHorNum;
		nScanCardSectionNumber *= (sScancard.nSectionHorNum * sScancard.nCard_zone_Num);
	}
	else if (sScancard.nDataInputDir == 2 || sScancard.nDataInputDir == 3)//上下
	{
		nScanCardSectionNumber = sScancard.nScanCardWidthReal / sScancard.nScanCardSectionRowNumber;
		nScanCardSectionNumber *= sScancard.nSectionHorNum;
	}
	
	CTool::ExchangeInteger(nScanCardSectionNumber, ucData, 1);
	//SCAN_CARD_SECTION_ROW_NUM	1	8	扫描卡每区行数，行数最大为16行
	CTool::ExchangeInteger(sScancard.nScanCardSectionRowNumber, ucData + 1, 1);
	//2012-11-23 sunj 逐点检测增加扩区功能：对于扩区，无论是模组间扩区还是模组内扩区，
	//下发HUb配置参数包，需要将横向的扩区拉伸。
	int nModuleHorNumTemp = sScancard.nScanCardWidthReal / sScancard.nModuleWidth;
	int nModuleVerNumTemp = sScancard.nScanCardHeightReal / sScancard.nModuleHeight;

	int nModuleWidthTemp = sScancard.nModuleWidth;
	int nModuleHeightTemp = sScancard.nModuleHeight;

	if (sScancard.bExtendedEnable)
	{
		//模组内扩区:模组的横向个数与纵向个数不变，模组的高度和宽度修改如下：
// 		nModuleWidthTemp = nModuleWidthTemp / sScancard.nSectionHorNum ;
// 		nModuleHeightTemp = nModuleHeightTemp * sScancard.nSectionHorNum ;

// 		nModuleWidthTemp = sScancard.nCard_zone_width;
// 		nModuleHeightTemp = nModuleHeightTemp * sScancard.nSectionHorNum * sScancard.nCard_zone_Num;

		nModuleWidthTemp = nModuleWidthTemp / sScancard.nSectionHorNum ;
		nModuleHeightTemp = nModuleHeightTemp * sScancard.nSectionHorNum * sScancard.nCard_zone_Num;
	}
	if(sScancard.bExtendedEnableEx)
	{
		//模组间扩区:模组的横向个数和纵向个数修改如下：模组的高度和宽度不变
// 		nModuleHorNumTemp = nModuleHorNumTemp / sScancard.nSectionHorNum;
// 		nModuleVerNumTemp = nModuleVerNumTemp * sScancard.nSectionHorNum;		

 		nModuleHorNumTemp = nModuleHorNumTemp / sScancard.nCard_zone_Num;
 		nModuleVerNumTemp = nModuleVerNumTemp * sScancard.nCard_zone_Num;		
	}

	//1    4   模组横向个数  模组横向个数 = 扫描卡宽度 / 模组宽度
	CTool::ExchangeInteger (nModuleHorNumTemp,ucData + 2, 1);
	//1    3   模组纵向个数  模组纵向个数 = 扫描卡高度 / 模组高度
	CTool::ExchangeInteger (nModuleVerNumTemp,ucData + 3, 1);


	//1    24  模组宽度，单个模组的横向点数，取值范围：1-64
	CTool::ExchangeInteger(nModuleWidthTemp, ucData + 4, 1);
	//1    24  模组高度，单个模组的纵向点数，取值范围：1-64
	CTool::ExchangeInteger(nModuleHeightTemp, ucData + 5, 1);//高度低8位	
	CTool::ExchangeInteger(nModuleHeightTemp >> 8, ucData + 15, 1);	//高度高8位

	//SCAN_MODE	1	4	扫描的模式，取 1-2-4-8-16
	CTool::ExchangeInteger(sScancard.nScanMode, ucData + 6, 1);
	//静态行数	1	0x2	每区行数/扫描模式
	int nStaticLine = sScancard.nScanCardSectionRowNumber / sScancard.nScanMode;
	CTool::ExchangeInteger(nStaticLine, ucData + 7, 1);
	//A参数	2	0x06C0	高位在前，低位在后，扫描卡宽度*扫描卡高度/区数
	//int nAPara = sScancard.nScanCardWidthReal * sScancard.nScanCardHeightReal / nScanCardSectionNumber;
	//CTool::ExchangeInteger (nAPara,ucData + 8, 2);

	//Info_empty_zone1、勾选空区插入：每m区有n空区	bit7~bit4表示m	bit3~bit0表示n	2、未勾选空区插入：Info_empty_zone=0x00
	short nInfo_empty_zone = 0x00;
	if (sScancard.bEmptySectionInsertEnable)
	{
		nInfo_empty_zone = (sScancard.nRealSectionNum << 4) + sScancard.nEmptySectionNum;
	}
	CTool::ExchangeInteger (nInfo_empty_zone,ucData + 8, 1);
	CTool::ExchangeInteger (0,ucData + 9, 1);//A参数没有用，作为保留参数

	//Drive_ic_type	1	0x00	7--0	驱动IC类型：
	//0x01：5030     0x02：62d722
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
	//data_input_dir	1	0x06	数据方向：（从显示屏正面看）默认为从右到左0x1	
	CTool::ExchangeInteger (sScancard.nDataInputDir,ucData + 11, 1);

	//Dot_detc_para	2	0x08	15--0	Bit15：抽点使能，‘0’表示没有抽点功能，‘1’表示有抽点功能。
	//Bit14~bit5：隔多少点抽（加1模式）- 实点数。Bit4: 抽点标志，1：前插入空点。0：后插入空点
	//Bit3~bit0：抽掉的点数（加1模式）-空点数。
	int nTmp = (sScancard.bEmptyInsertEnable ? 1 : 0) << 15 | (sScancard.nRealDotNum - 1) << 5| sScancard.nInsertMode << 4 | (sScancard.nEmptyDotNum - 1);
	CTool::ExchangeInteger (nTmp,ucData + 12, 2);

	//14	Mod_zone_num 0x00  7--0 模组区数
	short nModZoneNum = 1;
	if (sScancard.nDataInputDir == 0 || sScancard.nDataInputDir == 1)//左右
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
	else if (sScancard.nDataInputDir == 2 || sScancard.nDataInputDir == 3)//上下
	{
		nModZoneNum = sScancard.nModuleHeight / sScancard.nScanCardSectionRowNumber;
	}
	CTool::ExchangeInteger(nModZoneNum,ucData + 14, 1);

	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond,nAddress,0xE4,0,true,ucData,ucSendData);

	return (nLen);
}



//打调节色温数据包
int CCLScanCardPackData::PackColorTemperatureData(int nAddress,//扫描卡地址
												  short nAtlvcAddressSecond,
												  LPCOLOURRGB sColorRGB,int nCrTempID,short nHighLowGap,
												  short nGrayEnhanceMode,int nDeductbit, unsigned char* ucSendData)//发送数据
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));
	int nMaxColor = 256;
	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后

	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后
	//Red_lumi	8	0xff	7--0	灰度的红色的色温值，做减1处理
	if ( sColorRGB->nRed < nMaxColor )
	{
		memcpy(ucData ,&sColorRGB->nRed, 1);
	}
	else if (sColorRGB->nRed == nMaxColor)
	{
		ucData[0] = 0xFF;
	}
	//Gre_lumi	8	0xff	7--0	灰度的绿色的色温值，做减1处理
	if ( sColorRGB->nGreen < nMaxColor)
	{
		memcpy(ucData + 1, &sColorRGB->nGreen, 1);
	}
	else if( sColorRGB->nGreen == nMaxColor)
	{
		ucData[1] = 0xFF;
	}
	//Blu_lumi	8	0xff	7--0	灰度的蓝色的色温值，做减1处理
	if ( sColorRGB->nBlue < nMaxColor)
	{
		memcpy(ucData + 2, &sColorRGB->nBlue, 1);
	}
	else if( sColorRGB->nBlue == nMaxColor)
	{
		ucData[2] = 0xFF;
	}
	//Red_ic_lumi	8	0xff	7--0	驱动芯片的红色的色温值，
	memcpy(ucData + 3, &sColorRGB->nICRed, 1);
	//Gre_ic_lumi	8	0xff	7--0	驱动芯片的绿色的色温值，
	memcpy(ucData + 4, &sColorRGB->nICGreen, 1);
	//Blu_ic_lumi	8	0xff	7--0	驱动芯片的蓝色的色温值，
	memcpy(ucData + 5, &sColorRGB->nICBlue, 1);


	//低亮场
	//Red_lumi	8	0xff	7--0	灰度的红色的色温值，
	if ( sColorRGB->nRedLow < nMaxColor)
	{
		memcpy(ucData + 6, &sColorRGB->nRedLow, 1);
	}
	else if( sColorRGB->nRedLow == nMaxColor)
	{
		ucData[6] = 0xFF;
	}
	//Gre_lumi	8	0xff	7--0	灰度的绿色的色温值，
	if ( sColorRGB->nGreenLow < nMaxColor)
	{
		memcpy(ucData + 7, &sColorRGB->nGreenLow, 1);
	}
	else if( sColorRGB->nGreenLow == nMaxColor)
	{
		ucData[7] = 0xFF;
	}
	//Blu_lumi	8	0xff	7--0	灰度的蓝色的色温值，
	if ( sColorRGB->nBlueLow < nMaxColor)
	{
		memcpy(ucData + 8, &sColorRGB->nBlueLow, 1);
	}
	else if( sColorRGB->nBlueLow == nMaxColor)
	{
		ucData[8] = 0xFF;
	}
	//Red_ic_lumi	8	0xff	7--0	驱动芯片的红色的色温值，
	memcpy(ucData + 9, &sColorRGB->nICRedLow, 1);
	//Gre_ic_lumi	8	0xff	7--0	驱动芯片的绿色的色温值，
	memcpy(ucData + 10, &sColorRGB->nICGreenLow, 1);
	//Blu_ic_lumi	8	0xff	7--0	驱动芯片的蓝色的色温值，
	memcpy(ucData + 11, &sColorRGB->nICBlueLow, 1);
	//Gain_bypass 6 0x3f
	//bit5	低亮场的蓝色的数值增益旁路
	//bit4	低亮场的绿色的数值增益旁路
	//bit3	低亮场的红色的数值增益旁路
	//bit2	高亮场的蓝色的数值增益旁路
	//bit1	高亮场的绿色的数值增益旁路
	//bit0	高亮场的红色的数值增益旁路
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
	//bit7,6	00-不做高、低亮场处理(使用高亮场参数)
	//01-只扫高亮场   10-只扫低亮场  11-高、低亮场间隔扫描方式
	//bit5--0	高、低亮场位数差
	//liangdb 2012-11-08
	//最大18位
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

	//当前增益及灰度对应的色温值ID 0-7
	CTool::ExchangeInteger(nCrTempID , ucData +  14, 2);

	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond, nAddress,0x40,0x09,false,ucData,ucSendData);

	return (nLen);
}

//打调节色温增益包
int CCLScanCardPackData::PackColorTempCurrentData(int nAddress,//扫描卡地址
												  short nAtlvcAddressSecond,
												  LPCOLOURRGB sColorRGB,unsigned char* ucSendData)//发送数据
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));
	//int nMaxColor = 256;
	//9 数据域字节  

	//Red_ic_lumi	8	0xff	7--0	驱动芯片的红色的色温值，
	memcpy(ucData + 0, &sColorRGB->nICRed, 1);
	//Gre_ic_lumi	8	0xff	7--0	驱动芯片的绿色的色温值，
	memcpy(ucData + 1, &sColorRGB->nICGreen, 1);
	//Blu_ic_lumi	8	0xff	7--0	驱动芯片的蓝色的色温值，
	memcpy(ucData + 2, &sColorRGB->nICBlue, 1);

	//低亮场

	//Red_ic_lumi	8	0xff	7--0	驱动芯片的红色的色温值，
	memcpy(ucData + 3, &sColorRGB->nICRedLow, 1);
	//Gre_ic_lumi	8	0xff	7--0	驱动芯片的绿色的色温值，
	memcpy(ucData + 4, &sColorRGB->nICGreenLow, 1);
	//Blu_ic_lumi	8	0xff	7--0	驱动芯片的蓝色的色温值，
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
	//0x00: ≤16bit(当Ic_gain_en=00时)；当＞16bit (当Ic_gain_en≠00时)：Gray_scale_sel ＝ Ic_gain_en；孙工要求修改
	ucData[12] = nGainEnable;

	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond, nAddress,0x40,0x0C,false,ucData,ucSendData);

	return (nLen);
}

//打亮场阶段增益包
int CCLScanCardPackData::PackBrightCurrentData(int nAddress,//扫描卡地址
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
//打扫描卡在线升级命令包(0x95)
int CCLScanCardPackData::PackScanCardUpdateStart(int nAddress,short nAtlvcAddressSecond,short nUpdateType, bool bStart,bool bUpdateBoot,unsigned char * ucSendData)
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));


	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后

	//升级类型1 1 - 引导程序升级(boot)  0 - 应用程序升级(app)
	unsigned char ucBoot = bUpdateBoot ? 1 : 0;

	//升级类型2；0 - AK308G, 1 - AK308RG, 2 - AK302RG
	unsigned char ucAKCard = 0;	
	switch (nUpdateType)
	{
	case 0://0 - AK308G,
		{
			if ( bStart)
				ucAKCard = 0x01;//启动boot程序在线升级
			else
				ucAKCard = 0x02;//结束
		}
		break;
	case 1://1 - AK308RG,
		{
			if ( bStart)
				ucAKCard = 0x03;//启动boot程序在线升级
			else
				ucAKCard = 0x04;//结束
		}
		break;
	case 2://2 - AK302RG
		{
			if ( bStart)
				ucAKCard = 0x05;//启动boot程序在线升级
			else
				ucAKCard = 0x06;//结束
		}
		break;
	}
	ucData[0] = ucBoot << 4 | ucAKCard;

	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond,nAddress,0x95,0,false,ucData,ucSendData);

	return (nLen);
}
//储存命令与参数初始包(广播有应答)nAddress目的地址，nAtlvcAddressSecond源地址
int CCLScanCardPackData::PackSaveScanCardPara(short nAddress,short nAtlvcAddressSecond, int nTpyeID, bool bDefaul,unsigned char *ucSendData)
{

	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));

	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后

	//0x00 - 不存储不初始化，
	//0x01 - 将扫描卡参数存储到Flash（包括参数、亮度、色温、智能地址）
	//0x02 - 将伽马值存储到Flash
	//0x03 - 将走线查找表存储到Flash
	//0x04	将读取视频地址存到Flash
	//0x05	将运算处理包存到Flash
	//0x06	将读校正地址包存到Flash
	//0x07	将边界点类型查找包存到FLASH
	//0x08	将边界校正系数包存到FLASH
	//0x9	将区查找表存储到Flash


	//0x0	不初始化
	//0x10 - 将扫描卡参数重新初始化（包括参数、亮度、色温、智能地址）
	//0x20 - 伽马参数重新初始化
	//0x30 - 走线参数重新初始化
	//0x40	读取视频地址重新初始化
	//0x50	运算处理包重新初始化
	//0x60	读校正地址重新初始化
	//0x70	边界点类型查找包重新初始化
	//0x80	边界校正系数包重新初始化
	//0x90	区查找表重新初始化

	//…	预留
	//0xf0	将上述所有的参数重新初始化
	ucData[0] =nTpyeID;

	//保存出厂缺省参数标示
	if(bDefaul)
		ucData[1] = 0x01;
	else
		ucData[1] = 0x00;

	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond,nAddress,0x49,0,true,ucData,ucSendData);

	return (nLen);
}

//打扫描卡带载区域设置包
int CCLScanCardPackData::PackScanCardLoadRegionData(short nAddress,
													short nAtlvcAddressSecond,
													RECT rtLoad,
													short nStartX,
													short nStartY,unsigned char * ucSendData)
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));


	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后

	//预留
	CTool::ExchangeInteger(0, ucData, 1);

	//CARD1_WIDTH 1 扫描控制卡所对应的LED显示模块中横向像素点的数量，即宽度。做减1处理
	//高位在前，低位在后。虚拟时，指灯像素的宽度（不是截取宽度）
	CTool::ExchangeInteger(rtLoad.right - rtLoad.left - 1, ucData + 1, 2);

	//CARD1_HEIGHT 1 扫描控制卡所对应的LED显示模块中纵向像素点的数量，即高度。做减1处理
	//高位在前，低位在后。虚拟时，指灯像素的高度（不是截取高度）
	CTool::ExchangeInteger(rtLoad.bottom - rtLoad.top - 1, ucData +  3, 2);

	//CARD1_ORIGIN_X 2 卡所带面积的起始位置在整个视频中的绝对坐标X（列地址），
	CTool::ExchangeInteger(rtLoad.left , ucData +  5, 2);

	//CARD1_ORIGIN_Y 2 卡所带面积的起始位置在整个视频中的绝对坐标Y（行地址）
	CTool::ExchangeInteger(rtLoad.top, ucData +  7, 2);

	//SCREEN_X_VALUE 2 采集卡的U口或D口发送视频数据的起始点坐标X，默认为（0，0），即显示器的左上角坐标X
	CTool::ExchangeInteger(nStartX, ucData +  9, 2);

	//SCREEN_Y_VALUE 2 采集卡的U口或D口发送视频数据的起始点坐标Y，默认为（0，0），即显示器的左上角坐标Y
	CTool::ExchangeInteger(nStartY, ucData +  11, 2);

	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond,nAddress,0x40,0x0D,false,ucData,ucSendData);

	return (nLen);
}

//发送网络卡带载参数包，其带载根据实际扫描卡带载情况计算得到
int CCLScanCardPackData::PackNetCardLoadRegionData(short nAddress,
													short nAtlvcAddressSecond,
													RECT rtLoad,
													short nStartX,
													short nStartY,unsigned char * ucSendData)
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));

	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后
	//预留
	CTool::ExchangeInteger(0, ucData, 1);

	// netcard_width	2	0x007F	网络卡所对应的LED显示模块中横向像素点的数量，即宽度。高位在前，低位在后。
	// 注：虚拟时也是指截取的宽高度，扫描卡实际的宽高度从顺序号04中得到。
	CTool::ExchangeInteger(rtLoad.right - rtLoad.left - 1, ucData + 1, 2);

	// netcard _height	2	0x005F	网络卡所对应的LED显示模块中纵向像素点的数量，即高度。高位在前，低位在后。
	CTool::ExchangeInteger(rtLoad.bottom - rtLoad.top - 1, ucData +  3, 2);

	// netcard _origin_x	2	0x0000	卡所带面积的起始位置在整个视频中的绝对坐标X（列地址）。
	CTool::ExchangeInteger(rtLoad.left , ucData +  5, 2);

	// netcard _origin_y	2	0x0000	卡所带面积的起始位置在整个视频中的绝对坐标Y（行地址）。
	CTool::ExchangeInteger(rtLoad.top, ucData +  7, 2);

	// Screen_x_value	2	0x0000	采集卡的U口或D口发送视频数据的起始点坐标X，默认为（0，0），即显示器的左上角坐标。
	CTool::ExchangeInteger(nStartX, ucData +  9, 2);

	// Screen_y_value	2	0x0000	采集卡的U口或D口发送视频数据的起始点坐标Y，默认为（0，0），即显示器的左上角坐标， 
	CTool::ExchangeInteger(nStartY, ucData +  11, 2);

	int nLen = CCLPackCommunicationData::PackDataBase(nAtlvcAddressSecond,nAddress,0x40,0x1C,false,ucData,ucSendData);

	return (nLen);
}

//发送射灯带载参数包
int CCLScanCardPackData::PackSpotlightLoadRegionData(short nAddress,short nAtlvcAddressSecond,RECT rtLoad,
								short nStartX,short nStartY,unsigned char * ucSendData, short nFlag)
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));

	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后
	//预留
	CTool::ExchangeInteger(0, ucData, 1);

	if ( 0 == rtLoad.right - rtLoad.left || 0 == rtLoad.bottom - rtLoad.top)
	{
		CTool::ExchangeInteger(0, ucData + 1, 2);
		CTool::ExchangeInteger(0, ucData + 3, 2);
	} 
	else
	{
		// netcard_width	2	0x007F	网络卡所对应的LED显示模块中横向像素点的数量，即宽度。高位在前，低位在后。
		// 注：虚拟时也是指截取的宽高度，扫描卡实际的宽高度从顺序号04中得到。
		CTool::ExchangeInteger(rtLoad.right - rtLoad.left - 1, ucData + 1, 2);

		// netcard _height	2	0x005F	网络卡所对应的LED显示模块中纵向像素点的数量，即高度。高位在前，低位在后。
		CTool::ExchangeInteger(rtLoad.bottom - rtLoad.top - 1, ucData +  3, 2);
	}

	// netcard _origin_x	2	0x0000	卡所带面积的起始位置在整个视频中的绝对坐标X（列地址）。
	CTool::ExchangeInteger(rtLoad.left , ucData +  5, 2);

	// netcard _origin_y	2	0x0000	卡所带面积的起始位置在整个视频中的绝对坐标Y（行地址）。
	CTool::ExchangeInteger(rtLoad.top, ucData +  7, 2);

	// Screen_x_value	2	0x0000	采集卡的U口或D口发送视频数据的起始点坐标X，默认为（0，0），即显示器的左上角坐标。
	CTool::ExchangeInteger(nStartX, ucData +  9, 2);

	// Screen_y_value	2	0x0000	采集卡的U口或D口发送视频数据的起始点坐标Y，默认为（0，0），即显示器的左上角坐标， 
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
//打模组亮暗线边界系数包
int CCLScanCardPackData::PackModelLineCoeffData(short nAddress,short nAtlvcAddressSecond,short nRowID,short nColID,
												LineCoeff sLineCoeff,
												unsigned char * ucSendData)
{
	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, sizeof(ucData));

	//模组相对于扫描卡的行，0起
	ucData[0] = (unsigned char)nRowID;
	//模组相对于扫描卡的列，0起
	ucData[1] = (unsigned char)nColID;

	//模组中上、左、右、下4边和左上、右上、左下、右下4点的校正系数
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

//解扫描卡参数包
void CCLScanCardPackData::UnPackScanCardParam(map<int,CCLPackCommunicationData> & mUnPackData,
											  CStructSingleScanCard & sScanCard,RECT & rtLoad)
{
	unsigned int nTmp = 0;
	//data_input_dir		3--0	数据方向：（从显示屏正面看）默认为从右到左0x1
	//0x0	从左往右     0x1	从右往左     0x2	从上往下     0x3	从下往上
	sScanCard.nDataInputDir = mUnPackData[0].m_ucRcvData[9]& 0x0F;

	//oe_polarity	8	0x01	7	OE极性：默认为0x0  0x0-低有效    0x1-高有效
	sScanCard.nOePolarity = mUnPackData[0].m_ucRcvData[9] >> 7;

	//data_polarity			6-4	数据极性：默认为0x0    0x0-高电平点亮    0x1-低电平点亮   0x2-0x7	其他6种情况，预留
	sScanCard.nDataPolarity = ( mUnPackData[0].m_ucRcvData[9] >> 4 ) &  0x07;


	//virtual_disp_en	8	0x00	7	虚拟显示使能，默认为0x0，不使能（显示屏类型），芯片类型，虚拟像素排列方式
	sScanCard.bVirtvalDisp = mUnPackData[1].m_ucRcvData[1] >> 7 ? true : false;

	//syn_refresh_en			6	同步刷新使能。默认为0x1，使能
	sScanCard.bSyncRefresh = mUnPackData[1].m_ucRcvData[1] & 0x40 ? true : false;

	//dcb_line_clk_en			5--4	使能行信号DCB为时钟使带载高度加倍;默认为0
	//0x0	不使能   0x1	2倍     0x3	4倍
	short nDCBLineClkEn = (mUnPackData[1].m_ucRcvData[1] >> 4) &0x03;
	if (nDCBLineClkEn == 0x03)
	{
		sScanCard.nDCBlineClkEn = 2;
	}
	else
	{
		sScanCard.nDCBlineClkEn = nDCBLineClkEn;
	}

	//output_reverse			3--0	输出口逆序：默认为0x0
	//bit0=0x0	关闭数据线逆序		bit0=0x1	使能数据线逆序
	sScanCard.bDualOutput = mUnPackData[1].m_ucRcvData[1] & 0x04 ? true : false;

	//Bit1=0x0	关闭扫描线逆序		Bit1=0x1	使能扫描线逆序
	sScanCard.bScanOutUpReverse = mUnPackData[1].m_ucRcvData[1] & 0x02 ? true : false;

	//Bit2=0x0	单列输出			Bit2=0x1	双列输出
	sScanCard.bDataOutUpReverse = mUnPackData[1].m_ucRcvData[1] & 0x01 ? true : false;

	//使用行信号D作为第二路时钟使带载高度加倍:扩区功能，左右进线：模组高度扩区；上下进线：宽度扩区
	//1  1字节 mod_width	0x1f	5--0	模组的像素宽度,做 减1处理
	if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)
		sScanCard.nModuleWidth = mUnPackData[0].m_ucRcvData[0] + 1;
	else
		sScanCard.nModuleWidth = (short)((mUnPackData[0].m_ucRcvData[0] + 1) / pow(2.0,sScanCard.nDCBlineClkEn));


	//2 1字节 mod_height	8	0x1f	5--0	模组的像素高度,做 减1处理
	if (sScanCard.nDataInputDir == 0 || sScanCard.nDataInputDir == 1)
		sScanCard.nModuleHeight = (short)((mUnPackData[0].m_ucRcvData[1] + 1) / pow(2.0,sScanCard.nDCBlineClkEn));
	else
		sScanCard.nModuleHeight = mUnPackData[0].m_ucRcvData[1] + 1;

	//scan_mode	8	0x35	7--4	扫描的模式，默认为0x3。做 减1处理
	short nTmpScanMode = ((mUnPackData[0].m_ucRcvData[2] >> 4) & 0x0F) | (mUnPackData[0].m_ucRcvData[2] & 0x0F) << 4;
	sScanCard.nScanMode = nTmpScanMode + 1;

	//4 1字节 card_section_row_num	8	0x0F	5--0	扫描卡每区行数，行数最大为64行。
	sScanCard.nScanCardSectionRowNumber =  (short)((mUnPackData[0].m_ucRcvData[3] + 1) / pow(2.0,sScanCard.nDCBlineClkEn));

	//5 1字节  data_line_type	8	0x00	7--0	数据类型：默认为0x00，RGBR具体见附录3
	//Bit7-5：定义数据线的大类
	sScanCard.nDataLineTypeRange = mUnPackData[0].m_ucRcvData[4] >> 5;
	//0x0	红绿蓝分开, 0x1	红绿蓝合一三色1点串行
	//0x2	红绿蓝合一三色8点串行    0x3	红绿蓝合一三色16点串行
	//0x4	红绿蓝合一四色串行	Bit4-0：定义四线的具体情况。见附录3
	sScanCard.nDataLineType = mUnPackData[0].m_ucRcvData[4] & 0x1F;

	//6 1字节 
	//row_decode_mode	8	0x2f	7--4	行译码方式：默认0x2
	//0x0	静态无译码			0x1	无译码芯片，直接驱动行管			0x2	  138译码		
	//0x3	139译码				0x4	145译码或138双0						0x5  154译码	
	//0x6	164译码				0x7	192译码								0x8	193译码
	//0x9	595译码				0xA	4096译码							0xB	
	sScanCard.nRowDecodeMOde = mUnPackData[0].m_ucRcvData[5] >> 4;
	// data_line_ctrl			3--0	控制4根数据线RB,B,G,RA的亮灭。
	//注：对用bit为0：亮，为1：灭。默认：都亮
	sScanCard.nDataLineCtrl = mUnPackData[0].m_ucRcvData[5] & 0x0F;

	//7 1字节
	//empty_insert_en	8	0x00	7	空点插入使能，即每多少点插入多少空点.默认0x0，不使能
	sScanCard.bEmptyInsertEnable = mUnPackData[0].m_ucRcvData[6] & 0x80 ? true : false;
	//insert_mode			6	插入空点方式，前插入还是后插入。
	sScanCard.nInsertMode = (mUnPackData[0].m_ucRcvData[6] & 0x40) >> 6;
	//empty_dot_num			5--0	插入的空点数，每次最大只能插入64空点，做减1处理。
	sScanCard.nEmptyDotNum = (mUnPackData[0].m_ucRcvData[6] & 0x1F) + 1;

	//8 2字节 
	//real_dot_num	16	0x0000	15--0	每多少点插入空点，做减1处理。
	unsigned int nRealDotNum = 0;
	CTool::ExchangeChar(nRealDotNum, mUnPackData[0].m_ucRcvData + 7, 2);
	sScanCard.nRealDotNum = nRealDotNum + 1;

	// 			//10 1字节
	//Drive_ic_type	8	0x00	Bit7	0:PWM 1:通用恒流
	//Bit6-0 指示具体的芯片类型		0x00	MBI5042
	//0x01	MBI5030		0x02	TC62D722		0x03	MBI5050   TLC5958
	if(mUnPackData[0].m_ucRcvData[10] & 0x80)
	{
		sScanCard.nChipType = _GENERAL;//通用恒流
	}
	else
	{
		int nTmp1 = (mUnPackData[0].m_ucRcvData[10] & 0x7F) + 1;
		sScanCard.nChipType = (CHIP_TYPE)(nTmp1);
	}


	//scan_color_depth	8	0XD7    7--4	扫描卡扫描的颜色深度，取8~16的整数。默认为0xD。即读取SDRAM中的位面数。做减1处理。
	sScanCard.nScanColorDepth = ((mUnPackData[1].m_ucRcvData[0] & 0xF0) >> 4) + 1;
	//origin_color_bit			3-0	原始颜色深度，比如8BIT,10bit，12bit，16bit
	sScanCard.nOrginColorBit = (mUnPackData[1].m_ucRcvData[0] & 0x0F) + 1;

	//PWM芯片：zone_clk_num[19-16] 3-0  每区移数据（125Mhz）的时钟数高4bit；
	if(_GENERAL == sScanCard.nChipType)
		sScanCard.nHalfFieldNumber = mUnPackData[1].m_ucRcvData[2] & 0x0F + 1;


	//4 1字节
	//标准版：field_num	8	0x25	7--0	总场数，最大为136场，做减1处理。
	//PWM版：//freq_division_coef	8	0x18	7--0	分频系数，最大为200分频，值为200。默认为10分频，值为0x09。做减1处理。
	if (_GENERAL == sScanCard.nChipType)//标准版
		sScanCard.nFieldNum = mUnPackData[1].m_ucRcvData[3] + 1;
	else//PWM
		sScanCard.nPWMFreqDivisionCoeff = mUnPackData[1].m_ucRcvData[3] + 1;

	double fClkFreq[] = 
	{
		30.0, 25.0, 21.4, 18.8, 16.7, 15.0, 13.6, 12.5, 11.5, 10.7,
		10.0, 9.4, 8.3, 7.5, 6.5, 6.0, 5.6, 5.0, 4.5, 4.0 ,
		3.5, 3.0, 2.5, 2.0, 1.5, 1.0, 0.6
	};

	//灰度时钟
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


	//5 1字节
	//标准版:full_field_num_a1	8	0x7f	6--0	全场数，最大为128场
	//PWM:duty_cycle_low_value_a1	8	0x05	7--0 占空比可调等级,数据移位时钟时钟占空比设置，设置低电平的计数值
	//默认为0x5。不做减1处理。
	if (_GENERAL == sScanCard.nChipType)
		sScanCard.nFullFieldNumber = mUnPackData[1].m_ucRcvData[4] + 1;
	else
		sScanCard.nPWMDutyCycle = mUnPackData[1].m_ucRcvData[4];

	//6 1字节
	//freq_division_coef	8	0x18	7--0	分频系数，最大为200分频，值为200。默认为10分频，值为0x09。做减1处理。
	sScanCard.nFreqDivisionCoeff = mUnPackData[1].m_ucRcvData[5] + 1;

	//移位时钟
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


	//7 1字节
	//duty_cycle_low_value_a1	8	0x05	7--0 占空比可调等级,数据移位时钟时钟占空比设置，设置低电平的计数值
	//默认为0x5。不做减1处理。
	sScanCard.nDutyCycle = mUnPackData[1].m_ucRcvData[6];

	//场信息
	sScanCard.SetFieldNumByGrayLevel();

	//ref_doule_value			6--0	刷新率倍增的倍数，默认是0，最大128倍，做减1
	sScanCard.nRefreshDoubleValue = mUnPackData[1].m_ucRcvData[15] + 1;

	//1  1字节 
	//zhe_rdwr_mode	2	0x03	7--6	折处理模块读写折DPRAM的方式。默认为0
	//0：按列8读写，1：按箱体行读写
	sScanCard.nZheRdwrMode = mUnPackData[2].m_ucRcvData[0] >> 6;
	//scan_mode_a1	6		5--0	扫描的模式，默认为0x3。
	sScanCard.nScanMode = mUnPackData[2].m_ucRcvData[0] & 0x3F;
	
	
	//4 Drive_ic_reg	16			配驱动芯片的寄存器值,见附件驱动芯片的寄存器值
	//PWM输出模式,逐点检测
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

	//静态扫描，刷新倍增
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

	//扫描卡虚拟宽度
	nTmp = 0;
	CTool::ExchangeChar(nTmp, mUnPackData[3].m_ucRcvData + 2, 2);
	sScanCard.nScanCardWidth = nTmp / 3;

	//扫描卡虚拟高度	16	0x005f	15--0	卡所带的像素高度
	CTool::ExchangeChar(nTmp, mUnPackData[4].m_ucRcvData + 3, 2);
	sScanCard.nScanCardHeight = nTmp + 1;


	//确定区宽，03包中判断卡宽和区宽是否一致
	unsigned int nCardW16 = 0;
	CTool::ExchangeChar(nCardW16, mUnPackData[3].m_ucRcvData + 8, 2);
	unsigned int nSectionW16 = 0;
	CTool::ExchangeChar(nSectionW16, mUnPackData[3].m_ucRcvData + 14, 2);
	if((short)nSectionW16 < sScanCard.nModuleWidth)
	{
		//区宽的16倍数小于模组宽度，则确定为模组内扩区
		sScanCard.bExtendedEnable = true;
		sScanCard.bExtendedEnableEx = false;
		sScanCard.nSectionHorNum = nCardW16 / nSectionW16;
		sScanCard.nSectionWidth = sScanCard.nModuleWidth / sScanCard.nSectionHorNum;
	}
	else
	{
		//模组外扩区或不扩区
		sScanCard.bExtendedEnable = false;
		if(nCardW16 == nSectionW16)
		{
			//参数一致，即不扩区
			sScanCard.nSectionHorNum = 1;
			sScanCard.bExtendedEnableEx = false;
			sScanCard.nSectionWidth = sScanCard.nScanCardWidth;
			sScanCard.nCard_zone_Num = 1;
			sScanCard.nCard_zone_width = sScanCard.nScanCardWidth;
		}
		else
		{
			//不一致，则模组外扩区;
			sScanCard.bExtendedEnableEx = true;
			sScanCard.nSectionHorNum = nCardW16 / nSectionW16;
			sScanCard.nSectionWidth = sScanCard.nScanCardWidth / sScanCard.nSectionHorNum;
		}
	}

	//1卡所带的横向和纵向模组数
	//模组横向个数 = 扫描卡宽度/模组宽度
	sScanCard.nModuleHorNum = sScanCard.nScanCardWidth / sScanCard.nModuleWidth;
	//模组纵向个数 = 扫描卡高度 / 模组高度
	sScanCard.nModuleVerNum = sScanCard.nScanCardHeight / sScanCard.nModuleHeight;

	//row_oe_clk_num
	CTool::ExchangeChar(nTmp, mUnPackData[1].m_ucRcvData + 7, 2);
	int nRowOeClkNum = nTmp + 1;
	sScanCard.nOeClkNumber = nRowOeClkNum;

	//min_oe_clk_num
	CTool::ExchangeChar(nTmp, mUnPackData[1].m_ucRcvData + 13, 2);
	int nMinOeClkNum = nTmp & 0x0FFF;

	long nAfieldClkNum = sScanCard.GetAfieldClkNum(g_GlobalVariable.nVersionType);

	//CONFIG_IC_TIME	配寄存器与逐点检测的时间(芯片间隔时间)， 1-2047
	if(sScanCard.nChipType !=_GENERAL)
		sScanCard.nConfigICTime = (int) (nMinOeClkNum * pow(10.0,6)/ ((nAfieldClkNum + nRowOeClkNum) * 6.6));
	else
		sScanCard.nConfigICTime = 1024;

	//计算刷新率
	sScanCard.GetRefreshRateByReadParam(nMinOeClkNum,nAfieldClkNum,nRowOeClkNum, g_GlobalVariable.nVersionType);


	//由扫描卡带载包解包扫描卡实际宽度和高度
	nTmp = 0;
	CTool::ExchangeChar(nTmp, mUnPackData[13].m_ucRcvData + 1, 2);
	sScanCard.nScanCardWidthReal = nTmp + 1;//扫描卡实际宽度

	nTmp = 0;
	CTool::ExchangeChar(nTmp, mUnPackData[13].m_ucRcvData + 3, 2);
	sScanCard.nScanCardHeightReal = nTmp + 1;//扫描卡实际高度

	nTmp = 0;
	CTool::ExchangeChar(nTmp, mUnPackData[13].m_ucRcvData + 5, 2);
	rtLoad.left = nTmp;

	nTmp = 0;
	CTool::ExchangeChar(nTmp, mUnPackData[13].m_ucRcvData + 7, 2);
	rtLoad.top = nTmp;

	rtLoad.right = rtLoad.left + sScanCard.nScanCardWidthReal;
	rtLoad.bottom = rtLoad.top + sScanCard.nScanCardHeightReal;

	//解包，无信号显示：默认为0   0x0：黑屏，0x1：随机画面。0x2：图片
	sScanCard.nNoSingleDisp = mUnPackData[7].m_ucRcvData[0];

	//设置单点校正使能。nDotCorrectTye：0 - 无， 1 - 调亮， 2 - 调色
	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后
	//Corr_en_ctrl	8	0xFF	7--0
	//bit7--4	预留，填充0xff
	//bit3	启动校正功能（不涉及使能，只涉及功能，启动时才上电默认读参数到SDRAM）0：启动，1：关闭
	//	bit2	1	从卡FLASH读到SDRAM		0	从模组读参数FLASH，再到SDRAM
	//	bit1	0	调亮	1	调色
	//	bit0	0	关闭	1	使能
	int nStart = (mUnPackData[10].m_ucRcvData[0] & 0x08) >> 3;
	if(nStart)
	{
		//关闭，无校正
		sScanCard.nDotCorrectTye = 0;
	}
	else
	{
		sScanCard.nDotCorrectTye = (mUnPackData[10].m_ucRcvData[0] & 0x02) >> 1 ? 2 : 1;
	}

	sScanCard.bEmendBrightness = mUnPackData[10].m_ucRcvData[0] & 0x01 ? true : false;

	//打开箱体指示灯
	sScanCard.bOpenCabinetLamp = mUnPackData[6].m_ucRcvData[0] & 0x01 ? false : true;
	
	sScanCard.nCustomGamam = 8;

	//灰度增强方式与位数
	nTmp = mUnPackData[9].m_ucRcvData[13];
	int nHighLowGap = nTmp & 0x3F ;

	//灰度增强方式 0 或 3
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
//解包色温参数
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
	//高亮场的R/G/B的数值增益
	crRGB.nRed = mUnPackData[9].m_ucRcvData[0];
	crRGB.nGreen = mUnPackData[9].m_ucRcvData[1];
	crRGB.nBlue = mUnPackData[9].m_ucRcvData[2];

	//低亮场的R/G/B的数值增益
	crRGB.nRedLow = mUnPackData[9].m_ucRcvData[6];
	crRGB.nGreenLow = mUnPackData[9].m_ucRcvData[7];
	crRGB.nBlueLow = mUnPackData[9].m_ucRcvData[8];
	

	//高亮场的驱动芯片的R/G/B的电流增益，
	crRGB.nICRed = mUnPackData[9].m_ucRcvData[3];
	crRGB.nICGreen = mUnPackData[9].m_ucRcvData[4];
	crRGB.nICBlue = mUnPackData[9].m_ucRcvData[5];

	//低亮场的驱动芯片的R/G/B的电流增益，
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
	//bit5	低亮场的蓝色的数值增益旁路
	//bit4	低亮场的绿色的数值增益旁路
	//bit3	低亮场的红色的数值增益旁路
	//bit2	高亮场的蓝色的数值增益旁路
	//bit1	高亮场的绿色的数值增益旁路
	//bit0	高亮场的红色的数值增益旁路
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

//打扫描卡主配置数据包包返回 int - 包长度（6个包）
int CCLScanCardPackData::Pack6ScanCardData(int nScanCardAddress,//扫描卡地址
										CStructSingleScanCard & sScanCard, //扫描卡参数结构
										unsigned char* ucSendData,//发送数据
										int nEmptyByte)//校正前默认空字节数
{
	memset(ucSendData, 0, CL_MAX_BUF_NUMBER);

	int nLength = 0;
	unsigned char uSendData[CL_SEND_PACK_SIZE];

	int nMax;
	switch (sScanCard.nChipType)
	{
	case _MBI5153_E:
		nMax = 7; //发送包00,01,02,03,04,05,15
		break;
	case _MBI5155:
		nMax = 8; //发送包00,01,02,03,04,05,15,16
		break;
	default:
		nMax = 6; //发送包00,01,02,03,04,05
		break;
	}
	/*
	if (_SP10Version == g_GlobalVariable.nVersionType)
	{
		nMax++;//视频分离占空比设置 发送包0x17
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

//打无信号显示包
int CCLScanCardPackData::PackNoSingleDisp(short nAddress,
						short nNoSingleDisp,
						unsigned char * ucSendData)
{
	unsigned char ucCorrect[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucCorrect, 0, CL_SEND_EFFECT_DATA_SIZE);
	//无信号显示：默认为0   0x0：黑屏，0x1：随机画面。0x2：图片
	ucCorrect[0] = (unsigned char)nNoSingleDisp;

	int nLength = CCLPackCommunicationData::PackDataBase(0,nAddress,0x40,7,false,ucCorrect,ucSendData);

	return nLength;
}

//运算处理包
int CCLScanCardPackData::PackOperationProcessing(short nAddress,
						short nScancardSectionRowNumber,
						unsigned char * ucSendData)
{
	//显示行数/区行数：行数0-255，区行数1-32（区行数确定就不变）。
	//按0-255依次存入。注意：上位机先产生余数，再产生商。扫描卡按顺序写入DPRAM。

	unsigned char ucOperationPro[CL_MAX_BUF_NUMBER];
	memset(ucOperationPro, 0, sizeof(CL_MAX_BUF_NUMBER));

	for (int n = 0; n < 256; n ++)
	{
		ucOperationPro[n * 2] = n % nScancardSectionRowNumber;//余数
		ucOperationPro[n * 2 + 1] = n /nScancardSectionRowNumber;//商
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

//视频处理包
int CCLScanCardPackData::PackVideoProcessing(short nAddress,
						CStructSingleScanCard & sScanCard,
						unsigned char * ucSendData)
{
	//视频处理数据缓存
	unsigned char ucVideoProcessing[CL_MAX_BUF_NUMBER];
	memset(ucVideoProcessing, 0, sizeof(ucVideoProcessing));

	//视频处理数据长度
	int nDataLen = 0;

	//原则1：读取卡一行对应的视频数据地址
	//原则2：顺序读取16个像素的R,G,B,RA，
	if (sScanCard.nDataInputDir == 0)//从左到右
	{
		if (sScanCard.nScreenType == DISPLAY_TYPE_REAL)//实像素
		{
			if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//调亮
			{
				//只读取卡一行对应的视频数据地址，16个像素一组，
				//先读取最后一组16个像素的R，再最后一组16个像素的G，再最后一组16个像素的B，
				//后读取倒数第二组16个像素的R，再倒数第二组16个像素的G，再倒数第二组16个像素的B，
				//依次类推，组数乘16必须大于卡宽
				//视频数据长度：卡宽（凑成16的倍数）* 3 * 2字节
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
			else if (sScanCard.nDotCorrectTye == 2)//调色
			{
				//只读取卡一行对应的视频数据地址，16个像素一组，
				//先读取第一组16个像素的R，再第一组16个像素的G，再第一组16个像素的B，
				//后读取第二组16个像素的R，再第二组16个像素的G，再第二组16个像素的B，
				//依次类推，组数乘16必须大于卡宽
				//视频数据长度：卡宽（凑成16的倍数）* 3 * 3 * 2字节
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
		else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//虚拟
		{
			if (sScanCard.bVirtvalDisp)//虚拟显示时，虚拟参数
			{
				if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//调亮
				{
					//只读取卡一行对应的视频数据地址，16个像素一组，
					//虚拟屏的一个单灯对应左上、右上、左下、右下四个视频地址，卡一行对应三行视频数据地址
					//先读取最后一组16个像素的R，再最后一组16个像素的G，再最后一组16个像素的B，再最后一组16个像素的RA
					//后读取倒数第二组16个像素的R，再倒数第二组16个像素的G，再倒数第二组16个像素的B，再倒数第二组16个像素的RA
					//依次类推，组数乘16必须大于卡宽
					//每个RGB对应左上、右上、左下、右下四个视频地址，每个视频地址都含有RGB
					//视频数据长度：卡宽（凑成16的倍数）* 4 * 4 * 2字节
					//例如：
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
									nTemp = nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 9 -  m);//左上
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp =  nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 6 -  m);//右上
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp =  2 * nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 9 -  m);//左下
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = 2 * nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 6 -  m);//右下
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
									break;
								case 1://G
									nTemp = nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 6 -  m);//左上
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 3 -  m);//右上
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = 2 * nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 6 -  m);//左下
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = 2 * nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 3 -  m);//右下
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
									break;
								case 2://B
								case 3://RA
									nTemp = 2 * nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 9 -  m);//左上
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp =  2 * nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 6 -  m);//右上
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp =  3 * nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 9 -  m);//左下
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = 3 * nScanCardWidth * 3 - (i * 16 * 6 + n * 6 + 6 -  m);//右下
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
				else if (sScanCard.nDotCorrectTye == 2)//调色
				{
				}
			}
			else//不虚拟显示时，实像素参数
			{
				if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//调亮
				{
					//只读取卡一行对应的视频数据地址，16个像素一组，
					//先读取最后一组16个像素的R，再最后一组16个像素的G，再最后一组16个像素的B，
					//后读取倒数第二组16个像素的R，再倒数第二组16个像素的G，再倒数第二组16个像素的B，
					//依次类推，组数乘16必须大于卡宽
					//视频数据长度：卡宽（凑成16的倍数）* 3 * 2字节
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
				else if (sScanCard.nDotCorrectTye == 2)//调色
				{
					//只读取卡一行对应的视频数据地址，16个像素一组，
					//先读取第一组16个像素的R，再第一组16个像素的G，再第一组16个像素的B，
					//后读取第二组16个像素的R，再第二组16个像素的G，再第二组16个像素的B，
					//依次类推，组数乘16必须大于卡宽
					//视频数据长度：卡宽（凑成16的倍数）* 3 * 3 * 2字节
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
	else if (sScanCard.nDataInputDir == 1)//从右到左
	{
		if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && !sScanCard.bVirtvalDisp))//实像素或虚拟屏实像素显示
		{
			if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//调亮
			{
				if(!sScanCard.bExtendedEnable && !sScanCard.bExtendedEnableEx)
				{
					//只读取卡一行对应的视频数据地址，16个像素一组，
					//先读取第一组16个像素的R，再第一组16个像素的G，再第一组16个像素的B，
					//后读取第二组16个像素的R，再第二组16个像素的G，再第二组16个像素的B，
					//依次类推，组数乘16必须大于卡宽
					//视频数据长度：卡宽（凑成16的倍数）* 3 * 2字节
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
					for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //卡区个数循环
					{
						for (int u = 0; u < sScanCard.nSectionHorNum; ++u)//模组内区个数
						{
							for (int i = 0; i < nLop; ++i)
							{
								for (int m = 0; m < 3; ++m)//三种颜色
								{
									for (int n = 0; n < 16; ++n )
									{
										//横向区所在模组的序号
										int index1 = (i * 16 + n) / sScanCard.nSectionWidth;
										//横向模组像素像素点所在模组的位置
										int index2 = (i * 16 + n) % sScanCard.nSectionWidth;
										//区内偏移
										int nZoneOffset = ((index1 * sScanCard.nSectionHorNum) + u) * sScanCard.nSectionWidth * 3;
										//卡区的偏移
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
			else if (sScanCard.nDotCorrectTye == 2)//调色
			{
				//只读取卡一行对应的视频数据地址，16个像素一组，
				//先读取第一组16个像素的R对应的RGB，再第一组16个像素的G对应的RGB，再第一组16个像素的B对应的RGB，
				//后读取第二组16个像素的R对应的RGB，再第二组16个像素的G对应的RGB，再第二组16个像素的B对应的RGB，
				//依次类推，组数乘16必须大于卡宽
				//视频数据长度：卡宽（凑成16的倍数）* 3 * 3 * 2字节
				//相当于每组数据读取三遍
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
					//扩区
					int nLop = sScanCard.nCard_zone_width % 16 ?
						sScanCard.nCard_zone_width / 16 + 1 : sScanCard.nCard_zone_width / 16;
					for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //卡区个数循环
					{
						for(int u = 0; u < sScanCard.nSectionHorNum; ++u)//模组内区个数
						{
							for (int i = 0; i < nLop; i ++)
							{
								for (int m = 0; m < 3; m ++)
								{
									for (int n = 0; n < 16 * 3; n ++ )
									{
										//横向区所在模组的序号
										int index1 = (i * 16 * 3 + n) / (sScanCard.nSectionWidth * 3);
										//横向模组像素像素点所在模组的位置
										int index2 = (i * 16 * 3 + n) % (sScanCard.nSectionWidth * 3);
										//区内偏移
										int nZoneOffset = ((index1 * sScanCard.nSectionHorNum) + u) * sScanCard.nSectionWidth * 3;
										//卡区的偏移
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
		}//实像素或虚拟屏实像素显示
		else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && sScanCard.bVirtvalDisp)//虚拟屏虚拟显示，虚拟参数
		{
			if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//调亮
			{
				//只读取卡一行对应的视频数据地址，16个像素一组，
				//虚拟屏的一个单灯对应左上、右上、左下、右下四个视频地址，卡一行对应三行视频数据地址
				//先读取第一组16个像素的R，再第一组16个像素的G，再第一组16个像素的B，再第一组16个像素的RA
				//后读取第二组16个像素的R，再第二组16个像素的G，再第二组16个像素的B，再第二组16个像素的RA
				//依次类推，组数乘16必须大于卡宽
				//每个RGB对应左上、右上、左下、右下四个视频地址，每个视频地址都含有RGB
				//视频数据长度：卡宽（凑成16的倍数）* 4 * 4 * 2字节
				//例如：
				if (0 == sScanCard.nVirTualArray)//0：红A,绿/蓝,红B
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
									nTemp = i * 16 * 6 + n * 6 + m;//左上
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 + 3 + m;//右上
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 +  nScanCardWidth * 3 + m;//左下
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 + 3 + m;//右下
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
									break;
								case 1://G
									nTemp = i * 16 * 6 + n * 6 + 3 + m;//左上
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 + 3 + 3 + m;//右上
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 + 3 + nScanCardWidth * 3 + m;//左下
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 + 3 + nScanCardWidth * 3 + 3 + m;//右下
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
									break;
								case 2://B
									nTemp = i * 16 * 6 + n * 6 +  nScanCardWidth * 3 + m;//左上
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 + 3 +  nScanCardWidth * 3 + m;//右上
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 +  nScanCardWidth * 3 * 2 + m;//左下
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * 2 + 3 + m;//右下
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
									break;
								case 3://RA
									nTemp = i * 16 * 6 + n * 6 +  nScanCardWidth * 3 + m;//左上
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 +  nScanCardWidth * 3 + 3 + m;//右上
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 +  nScanCardWidth * 3 * 2 + m;//左下
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;

									nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * 2 + 3 + m;//右下
									memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
									nDataLen += 2;
									break;
								default:
									break;
								}
							}
						}
					}
				} //end if (0 == sScanCard.nVirTualArray)//0:红A,绿/蓝,红B
				else if (1 == sScanCard.nVirTualArray	//1:蓝,绿/空,红;绿,蓝/空,红
					|| 2 == sScanCard.nVirTualArray		//2:蓝,绿/空,红;蓝,绿/红,空
					|| 3 == sScanCard.nVirTualArray)	//3:红,绿/空,蓝;绿,红/空,蓝
				{
					int nScanCardWidth = sScanCard.nScanCardWidth * 2 + 1;
					int nLop = sScanCard.nCard_zone_width % 16 ?
						sScanCard.nCard_zone_width / 16 + 1 : sScanCard.nCard_zone_width / 16;

					int nTemp = 0;

					for (int k = 0; k < 2; k++) //行数据, 三灯一像素要发两行数据
					{
						for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //卡区个数循环
						{
							for (int u = 0; u < sScanCard.nSectionHorNum; ++u)//模组内区个数
							{
								for (int i = 0; i < nLop; i ++)
								{
									for (int m = 0; m < 3; m ++)//R G B
									{
										int nIndex = GetIndex(m, sScanCard.nVirTualArray, k);//代表灯（非像素点）对应的相对位置，0左上，1右上, 2左下，3右下
										for (int n = 0; n < 16; n ++ )
										{
											//横向区所在模组的序号
											int index1 = (i * 16 + n) / sScanCard.nSectionWidth;
											//横向模组像素像素点所在模组的位置
											int index2 = (i * 16 + n) % sScanCard.nSectionWidth;
											//区内偏移
											int nZoneOffset = ((index1 * sScanCard.nSectionHorNum) + u) * sScanCard.nSectionWidth * 6;
											//卡区的偏移
											int nZoneDot = v * sScanCard.nCard_zone_width * sScanCard.nSectionHorNum * 6;

											int nOffset = nZoneOffset + index2 * 6 + nZoneDot;

											switch (nIndex)
											{
											case 0:
												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (0 + 2 * k) + m;//左上
												nTemp = nOffset +  nScanCardWidth * 3 * (0 + 2 * k) + m;//左上
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (0 + 2 * k) + 3 + m;//右上
												nTemp = nOffset + nScanCardWidth * 3 * (0 + 2 * k) + m + 3;//右上
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (1 + 2 * k) + m;//左下
												nTemp = nOffset + nScanCardWidth * 3 * (1 + 2 * k) + m;//左下
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (1 + 2 * k) + 3 + m;//右下
												nTemp = nOffset + nScanCardWidth * 3 * (1 + 2 * k) + m + 3;//右下
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;
												break;
											case 1:
												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (0 + 2 * k) + 3 + m;//左上
												nTemp = nOffset + nScanCardWidth * 3 * (0 + 2 * k) + 3 + m;//左上
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (0 + 2 * k) + 3 + 3 + m;//右上
												nTemp = nOffset + nScanCardWidth * 3 * (0 + 2 * k) + 3 + 3 + m;//右上
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (1 + 2 * k) + 3 + m;//左下
												nTemp = nOffset + nScanCardWidth * 3 * (1 + 2 * k) + 3 + m;//左下
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (1 + 2 * k) + 3 + 3 + m;//右下
												nTemp = nOffset + nScanCardWidth * 3 * (1 + 2 * k) + 3 + 3 + m;//右下
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;
												break;
											case 2:
												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (1 + 2 * k) + m;//左上
												nTemp = nOffset + nScanCardWidth * 3 * (1 + 2 * k) + m;//左上
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (1 + 2 * k) + 3 + m;//右上
												nTemp = nOffset + nScanCardWidth * 3 * (1 + 2 * k) + 3 + m;//右上
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (2 + 2 * k) + m;//左下
												nTemp = nOffset + nScanCardWidth * 3 * (2 + 2 * k) + m;//左下
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (2 + 2 * k) + 3 + m;//右下
												nTemp = nOffset + nScanCardWidth * 3 * (2 + 2 * k) + 3 + m;//右下
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;
												break;
											case 3:
												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (1 + 2 * k) + 3 + m;//左上
												nTemp = nOffset + nScanCardWidth * 3 * (1 + 2 * k) + 3 + m;//左上
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (1 + 2 * k) + 3 + 3 + m;//右上
												nTemp = nOffset + nScanCardWidth * 3 * (1 + 2 * k) + 3 + 3 + m;//右上
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (2 + 2 * k) + 3 + m;//左下
												nTemp = nOffset + nScanCardWidth * 3 * (2 + 2 * k) + 3 + m;//左下
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;

												//nTemp = i * 16 * 6 + n * 6 + nScanCardWidth * 3 * (2 + 2 * k) + 3 + 3 + m;//右下
												nTemp = nOffset + nScanCardWidth * 3 * (2 + 2 * k) + 3 + 3 + m;//右下
												memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
												nDataLen += 2;
												break;
											default:
												break;
											}
										}
									}
								}
							}//end for (int u = 0; u < sScanCard.nSectionHorNum; ++u)//模组内区个数
						}//end for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //卡区个数循环
					}//end for (int l = 0; l < 2; l++) //2行数据
				}
			}
			else if (sScanCard.nDotCorrectTye == 2)//调色
			{
			}
		}//虚拟屏虚拟显示
	}
	else if (sScanCard.nDataInputDir == 2 ||sScanCard.nDataInputDir == 3)//上下进线
	{
		//使用行信号D作为第二路时钟使带载高度加倍:艺术屏中使用，扩区
		//每区列数由带载加倍而翻倍，
		//上下进线和左右进线的不同处：左右进线是按块划分，读取16个相邻像素的地址；
		//上下进线是是按区划分，读取16个区对应位的像素地址
		int nDCBLine = (int)pow(2.0, sScanCard.nDCBlineClkEn);
		if (sScanCard.nScreenType == DISPLAY_TYPE_REAL)//实像素
		{
			if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//调亮
			{
				//只读取卡一行对应的视频数据地址，16个像素一组，一组像素包括每个区对应位的像素，
				//区数按16个计算，不足是16的按默认地址取
				//第1组数据：第一区第一列R，第二区第一列R....第十六区第一列R
				//第2组数据：第一区第一列G，第二区第一列G....第十六区第一列G
				//第3组数据：第一区第一列B，第二区第一列B....第十六区第一列B
				//第4组数据：第一区第二列R，第二区第二列R....第十六区第二列R
				//第5组数据：第一区第二列G，第二区第二列G....第十六区第二列G
				//第6组数据：第一区第二列B，第二区第二列B....第十六区第二列B
				//依次类推，组数乘16必须大于卡宽
				//视频数据长度：每区行数 * 带载高度加倍倍数 * 16 * 3 * 2字节

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
			else if (sScanCard.nDotCorrectTye == 2)//调色
			{
				//只读取卡一行对应的视频数据地址，16个像素一组，一组像素包括每个区对应位的像素，
				//区数按16个计算，不足是16的按默认地址取
				//第1组数据：第一区第一列R对应的RGB，第二区第一列R对应的RGB....第十六区第一列R对应的RGB
				//第2组数据：第一区第一列G对应的RGB，第二区第一列G对应的RGB....第十六区第一列G对应的RGB
				//第3组数据：第一区第一列B对应的RGB，第二区第一列B对应的RGB....第十六区第一列B对应的RGB
				//第4组数据：第一区第二列R对应的RGB，第二区第二列R对应的RGB....第十六区第二列R对应的RGB
				//第5组数据：第一区第二列G对应的RGB，第二区第二列G对应的RGB....第十六区第二列G对应的RGB
				//第6组数据：第一区第二列B对应的RGB，第二区第二列B对应的RGB....第十六区第二列B对应的RGB
				//依次类推，组数乘16必须大于卡宽
				//视频数据长度：每区行数 * 带载高度加倍倍数 * 16 * 3 * 3 * 2字节
				//相当于每组数据读取三遍

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
		else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//虚拟屏
		{
			if (sScanCard.bVirtvalDisp)//虚拟显示，虚拟参数
			{
				if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//调亮
				{

					//只读取卡一行对应的视频数据地址，16个像素一组，一组像素包括每个区对应位的像素，
					//虚拟屏的一个单灯对应左上、右上、左下、右下四个视频地址，卡一行对应三行视频数据地址
					//区数按16个计算，不足是16的按默认地址取
					//第1组数据：第一区第一列R对应的左上、右上、左下、右下的R，第二区第一列R对应的左上、右上、左下、右下的R....第十六区第一列R对应的左上、右上、左下、右下的R
					//第2组数据：第一区第一列G对应的左上、右上、左下、右下的G，第二区第一列G对应的左上、右上、左下、右下的G....第十六区第一列G对应的左上、右上、左下、右下的G
					//第3组数据：第一区第一列B对应的左上、右上、左下、右下的B，第二区第一列B对应的左上、右上、左下、右下的B....第十六区第一列B对应的左上、右上、左下、右下的B
					//第4组数据：第一区第一列RA对应的左上、右上、左下、右下的R，第二区第一列R对应的左上、右上、左下、右下的R....第十六区第一列R对应的左上、右上、左下、右下的R
					//第5组数据：第一区第二列R对应的左上、右上、左下、右下的R，第二区第二列R对应的左上、右上、左下、右下的R....第十六区第二列R对应的左上、右上、左下、右下的R
					//第6组数据：第一区第二列G对应的左上、右上、左下、右下的G，第二区第二列G对应的左上、右上、左下、右下的G....第十六区第二列G对应的左上、右上、左下、右下的G
					//第7组数据：第一区第二列B对应的左上、右上、左下、右下的B，第二区第二列B对应的左上、右上、左下、右下的B....第十六区第二列B对应的左上、右上、左下、右下的B
					//第8组数据：第一区第一列RA对应的左上、右上、左下、右下的R，第二区第一列R对应的左上、右上、左下、右下的R....第十六区第一列R对应的左上、右上、左下、右下的R
					//依次类推，组数乘16必须大于卡宽
					//视频数据长度：每区行数 * 带载高度加倍倍数 * 16 * 4 * 4 * 2字节
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
										nTemp = i * 16 * 6 + n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + m;//左上
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + 3 + m;//右上
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 + n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 +  nScanCardWidth * 3 + m;//左下
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + nScanCardWidth * 3 + 3 + m;//右下
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;
										break;
									case 1://G
										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber *nDCBLine * 3 + j * 6 + 3 + m;//左上
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 + n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + 3 + 3 + m;//右上
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + 3 + nScanCardWidth * 3 + m;//左下
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + 3 + nScanCardWidth * 3 + 3 + m;//右下
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;
										break;
									case 2://B
										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 +  nScanCardWidth * 3 + m;//左上
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + 3 +  nScanCardWidth * 3 + m;//右上
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 +  nScanCardWidth * 3 * 2 + m;//左下
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + nScanCardWidth * 3 * 2 + 3 + m;//右下
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;
										break;
									case 3://RA
										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 +  nScanCardWidth * 3 + m;//左上
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 +  nScanCardWidth * 3 + 3 + m;//右上
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 +  nScanCardWidth * 3 * 2 + m;//左下
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;

										nTemp = i * 16 * 6 +  n * sScanCard.nScanCardSectionRowNumber * nDCBLine * 3 + j * 6 + nScanCardWidth * 3 * 2 + 3 + m;//右下
										memcpy(ucVideoProcessing + nDataLen, &nTemp, 2);
										nDataLen += 2;
										break;
									default:
										break;
									}
								}
				}
				else if (sScanCard.nDotCorrectTye == 2)//调色
				{
				}
			}
			else//不虚拟显示，实像素参数
			{
				if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//调亮
				{
					//只读取卡一行对应的视频数据地址，16个像素一组，一组像素包括每个区对应位的像素，
					//区数按16个计算，不足是16的按默认地址取
					//第1组数据：第一区第一列R，第二区第一列R....第十六区第一列R
					//第2组数据：第一区第一列G，第二区第一列G....第十六区第一列G
					//第3组数据：第一区第一列B，第二区第一列B....第十六区第一列B
					//第4组数据：第一区第二列R，第二区第二列R....第十六区第二列R
					//第5组数据：第一区第二列G，第二区第二列G....第十六区第二列G
					//第6组数据：第一区第二列B，第二区第二列B....第十六区第二列B
					//依次类推，组数乘16必须大于卡宽
					//视频数据长度：每区行数 * 带载高度加倍倍数 * 16 * 3 * 2字节

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
				else if (sScanCard.nDotCorrectTye == 2)//调色
				{
					//只读取卡一行对应的视频数据地址，16个像素一组，一组像素包括每个区对应位的像素，
					//区数按16个计算，不足是16的按默认地址取
					//第1组数据：第一区第一列R对应的RGB，第二区第一列R对应的RGB....第十六区第一列R对应的RGB
					//第2组数据：第一区第一列G对应的RGB，第二区第一列G对应的RGB....第十六区第一列G对应的RGB
					//第3组数据：第一区第一列B对应的RGB，第二区第一列B对应的RGB....第十六区第一列B对应的RGB
					//第4组数据：第一区第二列R对应的RGB，第二区第二列R对应的RGB....第十六区第二列R对应的RGB
					//第5组数据：第一区第二列G对应的RGB，第二区第二列G对应的RGB....第十六区第二列G对应的RGB
					//第6组数据：第一区第二列B对应的RGB，第二区第二列B对应的RGB....第十六区第二列B对应的RGB
					//依次类推，组数乘16必须大于卡宽
					//视频数据长度：每区行数 * 带载高度加倍倍数 * 16 * 3 * 3 * 2字节
					//相当于每组数据读取三遍

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

//设置读取校正数据地址包
int CCLScanCardPackData::PackCorrectProcessing(short nAddress,
						CStructSingleScanCard & sScanCard,
						unsigned char * ucSendData)
{
	//无校正时不发送校正数据地址包
	if (sScanCard.nDotCorrectTye == 0)
		return -1;

	//视频处理数据缓存
	unsigned char ucCorrectProcessing[CL_MAX_BUF_NUMBER];
	memset(ucCorrectProcessing, 0, sizeof(ucCorrectProcessing));

	//视频处理数据长度
	int nDataLen = 0;

	//原则1：读取卡一行对应的校正数据地址
	//原则2：顺序读取16个像素的R,G,B,RA，
	//原则3：跳过无效数据


	//有效校正参数。
	//调亮：实际模宽*4
	//实像素单点调色：实际模宽*9

	int nOneRowCoefNum = 0x017F;//有效校正参数
	if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//调亮
	{
		nOneRowCoefNum = sScanCard.nModuleWidth * 4;

		if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && sScanCard.bVirtvalDisp
			&& (1 == sScanCard.nVirTualArray || 2 == sScanCard.nVirTualArray || 3 == sScanCard.nVirTualArray))
		{
			nOneRowCoefNum = sScanCard.nModuleWidth * 3;
		}
	}
	else if (sScanCard.nDotCorrectTye == 2)//调色
	{
		nOneRowCoefNum = sScanCard.nModuleWidth * 9;
	}

	//总的校正参数。
	//实像素虚拟像素单点调亮：[(mod_width * 4 + x)/ 32] * 32
	//	实像素单点调色：[(mod_width * 9 + x)/ 32] * 32
	//	注：x是指相加后可以整除32.如：模宽为15的调亮，则x=2
	int nOneRowCoefNumAllA1 = 0x017F;//总的校正参数
	int nModuleWidth = sScanCard.nModuleWidth % 16 ?
		(sScanCard.nModuleWidth / 16 + 1) * 16 : sScanCard.nModuleWidth ;
	nModuleWidth = nModuleWidth > 42 ? sScanCard.nModuleWidth : nModuleWidth;

	if (sScanCard.nDotCorrectTye == 0 || sScanCard.nDotCorrectTye == 1)//调亮
	{
		if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//全彩
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
	else if (sScanCard.nDotCorrectTye == 2)//调色
	{
		if (sScanCard.nScreenType == DISPLAY_TYPE_REAL ||
			(sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && !sScanCard.bVirtvalDisp))//实像素或虚拟屏实像素显示
		{
			nOneRowCoefNumAllA1 = (nModuleWidth * 9) % 32 ?
				(nModuleWidth * 9 / 32 + 1) * 32 :nModuleWidth * 9;
		}
	}

	//无效校正参数。按照模组个数平分；
	int nRowCoefNovailNumAl = nOneRowCoefNumAllA1 - nOneRowCoefNum;

	int nTemp = 0;
	int nOffset = 0;
	if (sScanCard.nDataInputDir == 0)//从左到右
	{
		if (sScanCard.nScreenType == DISPLAY_TYPE_REAL)//实像素
		{
			if (sScanCard.nDotCorrectTye == 1)//调亮
			{
				if (!sScanCard.bExtendedEnable && !sScanCard.bExtendedEnableEx)
				{
					//只读取卡一行对应的视频数据地址，16个像素一组，
					//先读取最后一组16个像素的R，再最后一组16个像素的G，再最后一组16个像素的B，
					//后读取倒数第二组16个像素的R，再倒数第二组16个像素的G，再倒数第二组16个像素的B，
					//依次类推，组数乘16必须大于卡宽
					//视频数据长度：卡宽（凑成16的倍数）* 3 * 2字节
					//视频数据排列顺序：R、G、B、两个空字节，即一个像素占8字节视频地址
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
					//扩区使能
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
			else if (sScanCard.nDotCorrectTye == 2)//调色
			{
				if (!sScanCard.bExtendedEnable && !sScanCard.bExtendedEnableEx)
				{
					//只读取卡一行对应的视频数据地址，16个像素一组，
					//先读取最后一组16个像素的R，再最后一组16个像素的G，再最后一组16个像素的B，
					//后读取倒数第二组16个像素的R，再倒数第二组16个像素的G，再倒数第二组16个像素的B，
					//依次类推，组数乘16必须大于卡宽
					//视频数据长度：卡宽（凑成16的倍数）* 3 * 3 * 2字节
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
		else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//虚拟
		{
			if (sScanCard.bVirtvalDisp)
			{
				if (sScanCard.nDotCorrectTye == 1)//调亮
				{
					//只读取卡一行对应的视频数据地址，16个像素一组，
					//与灯视频数据一一对应
					//先读取最后一组16个像素的R，再最后一组16个像素的G，再最后一组16个像素的B，再最后一组16个像素的RA
					//后读取倒数第二组16个像素的R，再倒数第二组16个像素的G，再倒数第二组16个像素的B，再倒数第二组16个像素的RA
					//依次类推，组数乘16必须大于卡宽
					//视频数据长度：卡宽（凑成16的倍数）* 4 * 2字节

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
				else if (sScanCard.nDotCorrectTye == 2)//调色
				{
				}
			}
			else
			{
				if (sScanCard.nDotCorrectTye == 1)//调亮
				{
					//只读取卡一行对应的视频数据地址，16个像素一组，
					//先读取最后一组16个像素的R，再最后一组16个像素的G，再最后一组16个像素的B，
					//后读取倒数第二组16个像素的R，再倒数第二组16个像素的G，再倒数第二组16个像素的B，
					//依次类推，组数乘16必须大于卡宽
					//视频数据长度：卡宽（凑成16的倍数）* 3 * 2字节
					//视频数据排列顺序：R、G、B、两个空字节，即一个像素占8字节视频地址
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
				else if (sScanCard.nDotCorrectTye == 2)//调色
				{
					//只读取卡一行对应的视频数据地址，16个像素一组，
					//先读取最后一组16个像素的R，再最后一组16个像素的G，再最后一组16个像素的B，
					//后读取倒数第二组16个像素的R，再倒数第二组16个像素的G，再倒数第二组16个像素的B，
					//依次类推，组数乘16必须大于卡宽
					//视频数据长度：卡宽（凑成16的倍数）* 3 * 3 * 2字节
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
	else if (sScanCard.nDataInputDir == 1)//从右到左
	{
		if (sScanCard.nDotCorrectTye == 1)//调亮
		{
			if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && !sScanCard.bVirtvalDisp))//实像素或虚拟屏实像素显示
			{
				if(!sScanCard.bExtendedEnable && !sScanCard.bExtendedEnableEx )
				{
					//只读取卡一行对应的视频数据地址，16个像素一组，
					//先读取第一组16个像素的R，再第一组16个像素的G，再第一组16个像素的B，
					//后读取第二组16个像素的R，再第二组16个像素的G，再第二组16个像素的B，
					//依次类推，组数乘16必须大于卡宽
					//视频数据长度：卡宽（凑成16的倍数）* 3 * 2字节
					//视频数据排列顺序：R、G、B、两个空字节，即一个像素占8字节视频地址
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;

					for (int i = 0; i < nLop; i ++)//组
						for (int m = 0; m < 3; m ++)//RGB三色
							for (int n = 0; n < 16; n ++ )//16个像素
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
					for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //卡区个数循环，若不外部扩nCard_zone_Num=1
					{
						for(int u = 0; u < sScanCard.nSectionHorNum; ++u)
						{
							for (int i = 0; i < nLop; i ++)//组
							{
								for (int m = 0; m < 3; m ++)//RGB三色
								{
									for (int n = 0; n < 16; n ++ )//16个像素
									{
										//横向区所在模组的序号
										int index1 = (i * 16 + n) / sScanCard.nSectionWidth;
										//横向模组像素像素点所在模组的位置
										int index2 = (i * 16 + n) % sScanCard.nSectionWidth;
										//区内偏移
										int nZoneOffset = ((index1 * sScanCard.nSectionHorNum) + u) * sScanCard.nSectionWidth * 4;
										//卡区的偏移
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
			else if(sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && sScanCard.bVirtvalDisp)//虚拟屏虚拟显示
			{
				//虚拟像素排布方式,//new虚拟像素排布方式，0：红A,绿/蓝,红B，1：蓝,绿/空,红;绿,蓝/空,红，2：蓝,绿/空,红;蓝,绿/红,空，3：红,绿/空,蓝;绿,红/空,蓝
				if (0 == sScanCard.nVirTualArray)//红A,绿/蓝,红
				{
					//只读取卡一行对应的视频数据地址，16个像素一组，
					//与灯视频数据一一对应
					//先读取第一组16个像素的R，再第一组16个像素的G，再第一组16个像素的B，再第一组16个像素的RA
					//后读取第二组16个像素的R，再第二组16个像素的G，再第二组16个像素的B，再第二组16个像素的RA
					//依次类推，组数乘16必须大于卡宽
					//视频数据长度：卡宽（凑成16的倍数）* 4 * 2字节

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
				else if (1 == sScanCard.nVirTualArray	//1：蓝,绿/空,红;绿,蓝/空,红
					|| 2 == sScanCard.nVirTualArray		//2：蓝,绿/空,红;蓝,绿/红,空
					|| 3 == sScanCard.nVirTualArray)	//3：红,绿/空,蓝;绿,红/空,蓝
				{
					int nLop = sScanCard.nCard_zone_width % 16 ?
						sScanCard.nCard_zone_width / 16 + 1 : sScanCard.nCard_zone_width / 16;

					for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //卡区个数循环，若不外部扩nCard_zone_Num=1
					{
						for(int u = 0; u < sScanCard.nSectionHorNum; ++u)
						{
							for (int i = 0; i < nLop; i ++)//组
							{
								for (int m = 0; m < 3; m ++)//RGB三色
								{
									for (int n = 0; n < 16; n ++ )//16个像素
									{
										//横向区所在模组的序号
										int index1 = (i * 16 + n) / sScanCard.nSectionWidth;
										//横向模组像素像素点所在模组的位置
										int index2 = (i * 16 + n) % sScanCard.nSectionWidth;
										//区内偏移
										int nZoneOffset = ((index1 * sScanCard.nSectionHorNum) + u) * sScanCard.nSectionWidth * 3;
										//卡区的偏移
										//int nZoneDot = v * sScanCard.nCard_zone_width * sScanCard.nSectionHorNum * 4;

										//卡区的总点数
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
		else if (sScanCard.nDotCorrectTye == 2)//调色
		{
			if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && !sScanCard.bVirtvalDisp))//实像素或虚拟屏实像素显示
			{
				if(!sScanCard.bExtendedEnable && !sScanCard.bExtendedEnableEx)
				{
					//只读取卡一行对应的视频数据地址，16个像素一组，
					//先读取第一组16个像素的R对应的RGB(036)，再第一组16个像素的G对应的RGB(147)，
					//再第一组16个像素的B对应的RGB(258)，
					//后读取第二组16个像素的R对应的RGB，再第二组16个像素的G对应的RGB，再第二组16个像素的B对应的RGB，
					//依次类推，组数乘16必须大于卡宽
					//视频数据长度：卡宽（凑成16的倍数）* 3 * 3 * 2字节
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
					for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //卡区个数循环，若不外部扩nCard_zone_Num=1
					{
						for(int u = 0; u < sScanCard.nSectionHorNum; ++u)
						{
							for (int i = 0; i < nLop;  ++i)//组
							{
								for (int m = 0; m < 3; ++m)//RGB三色
								{
									for (int n = 0; n < 16;  ++n )//16个像素
									{
										//横向区所在模组的序号
										int index1 = (i * 16 + n) / sScanCard.nSectionWidth;
										//横向模组像素像素点所在模组的位置
										int index2 = (i * 16 + n) % sScanCard.nSectionWidth;
										//区内偏移
										int nZoneOffset = ((index1 * sScanCard.nSectionHorNum) + u) * sScanCard.nSectionWidth * 9;

										//卡区的偏移
										//int nZoneDot = v * sScanCard.nCard_zone_width * sScanCard.nSectionHorNum * 9;

										int nZoneDot = v * sScanCard.nCard_zone_width * sScanCard.nSectionHorNum * 9 +
											nRowCoefNovailNumAl * v * sScanCard.nCard_zone_width / sScanCard.nSectionWidth;

										for (int j = 0; j < 3;  ++j) //三色中三分量
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
	else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//上下进线
	{
		if (sScanCard.nScreenType == DISPLAY_TYPE_REAL)//实像素
		{
			if (sScanCard.nDotCorrectTye == 1)//调亮
			{
				//只读取卡一行对应的视频数据地址，16个像素一组，校正地址与视频地址对应
				//区数固定，按16个计算，不足是16的按默认地址取
				//视频是按扫描卡一行扫描，发送校正数据时按模组发送，模组宽度必须是16的倍数
				//第1组数据：第一区第一列RGB，第二区第一列RGB....第十六区第一列RGB
				//第2组数据：第一区第二列RGB，第二区第二列RGB....第十六区第二列RGB
				//校正数据排列顺序：R、G、B、RB(实像素时为0)，即一个像素占8字节地址
				//校正地址包(0x9C)的扫描行是扫描卡的卡宽，而校正数据(0x94)是一个模组一个模组的发送，所以每模组区数个点后加偏移量(模组宽度*4)

				int nDLine = sScanCard.nScanCardSectionRowNumber * ((int)pow(2.0, sScanCard.nDCBlineClkEn));
				for( int j = 0; j < nDLine; ++j)
					for (int m = 0; m < 3; ++m)//RGB三色
						for (int i = 0; i < 16; ++i)//默认16区
						{
							nTemp = 16 * 4 * i + m + j * 4;
							memcpy(ucCorrectProcessing + nDataLen, &nTemp, 2);
							nDataLen += 2;
						}
			}
			else if (sScanCard.nDotCorrectTye == 2)//调色
			{
				//只读取卡一行对应的视频数据地址，16个像素一组，一组像素包括每个区对应位的像素，
				//区数按16个计算，不足是16的按默认地址取
				//第1组数据：第一区第一列R对应的RGB，第二区第一列R对应的RGB....第十六区第一列R对应的RGB
				//第2组数据：第一区第一列G对应的RGB，第二区第一列G对应的RGB....第十六区第一列G对应的RGB
				//第3组数据：第一区第一列B对应的RGB，第二区第一列B对应的RGB....第十六区第一列B对应的RGB
				//第4组数据：第一区第二列R对应的RGB，第二区第二列R对应的RGB....第十六区第二列R对应的RGB
				//第5组数据：第一区第二列G对应的RGB，第二区第二列G对应的RGB....第十六区第二列G对应的RGB
				//第6组数据：第一区第二列B对应的RGB，第二区第二列B对应的RGB....第十六区第二列B对应的RGB
				//依次类推，组数乘16必须大于卡宽
				//视频数据长度：每区行数 * 16 * 3 * 3 * 2字节

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
		else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//虚拟屏
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
//设置边界系数查找表
int CCLScanCardPackData::PackCorrectProcessingLookup(short nAddress,
						CStructSingleScanCard & sScanCard,
						unsigned char * ucSendData)
{
	//无校正时不发送校正数据地址包
	if (0 == sScanCard.nDotCorrectTye)
	{
		return -1;
	}

	//视频处理数据缓存
	unsigned char ucCorrectProcessing[CL_MAX_BUF_NUMBER];
	memset(ucCorrectProcessing, 0, sizeof(ucCorrectProcessing));

	//视频处理数据长度
	int nDataLen = 0;

	int nTemp = 0;
	if (sScanCard.nDataInputDir == 0)//从左到右
	{
	}
	else if (sScanCard.nDataInputDir == 1)//从右到左
	{
		int nLeftIndex = 0;//模组左边界编号index
#ifdef M3_5
		nLeftIndex = sScanCard.nScanCardWidth - sScanCard.nScanCardWidthReal; //M3.5的虚拟宽度为72，实际宽度为68，左边界需要右移动4个像素点
#else
		nLeftIndex = 0;
#endif

		if (sScanCard.nDotCorrectTye == 1)//调亮
		{
			if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && !sScanCard.bVirtvalDisp))//实像素或虚拟屏实像素显示
			{
				if(!sScanCard.bExtendedEnable && !sScanCard.bExtendedEnableEx )
				{
					//模组边界查找表和校正地址包对应；
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;
					for (int i = 0; i < nLop; i ++)
						for (int m = 0; m < 3; m ++)//RGB三种颜色
						{
							int nModID = (16 * i) / sScanCard.nModuleWidth;
							for (int n = 0; n < 16; n ++ )
							{
								unsigned char cTmp[2];
								cTmp[0] = nModID;
								if ((i * 16 + n) % sScanCard.nModuleWidth == nLeftIndex )
								{
									//模组第一列
									cTmp[1] = 1;//模组边界编号，1-最左1列，2-中间列，3-最右1列
								}
								else if((i * 16 + n) % sScanCard.nModuleWidth == sScanCard.nModuleWidth - 1)
								{
									//模组最后一列
									cTmp[1] = 3;
									nModID ++;
								}
								else
								{
									//模组中间列
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
					for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //卡区个数循环
					{
						for(int u = 0; u < sScanCard.nSectionHorNum; ++u)//模组内区个数
						{
							for (int i = 0; i < nLop; i ++)
							{
								for (int m = 0; m < 3; m ++)//RGB三种颜色
								{
									for (int n = 0; n < 16; n ++ )
									{
										//横向区所在模组的序号
										int index1 = (i * 16 + n) / sScanCard.nSectionWidth;
										//横向模组像素像素点所在模组的位置
										int index2 = (i * 16 + n) % sScanCard.nSectionWidth;
										//区内偏移
										int nZoneOffset = ((index1 * sScanCard.nSectionHorNum) + u) * sScanCard.nSectionWidth;
										//卡区的偏移
										int nZoneDot = v * sScanCard.nCard_zone_width * sScanCard.nSectionHorNum;
										nTemp = nZoneOffset + index2 + nZoneDot;
										int nModID = nTemp/sScanCard.nModuleWidth;

										unsigned char cTmp[2];
										cTmp[0] = nModID;
										if (nTemp % sScanCard.nModuleWidth == nLeftIndex)//M3.5 右移4个点，特殊情况
										{
											//模组第一列
											cTmp[1] = 1;//模组边界编号，1-最左1列，2-中间列，3-最右1列
										}
										else if(nTemp % sScanCard.nModuleWidth == sScanCard.nModuleWidth - 1)
										{
											//模组最后一列
											cTmp[1] = 3;
										}
										else
										{
											//模组中间列
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
			else if(sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && sScanCard.bVirtvalDisp)//虚拟屏虚拟显示
			{
				if (0 == sScanCard.nVirTualArray)
				{
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;
					for (int i = 0; i < nLop; i ++)
					{
						for (int m = 0; m < 4; m ++)//R/G/B/RA四种颜色
						{
							int nModID = (16 * i) / sScanCard.nModuleWidth;
							for (int n = 0; n < 16; n ++ )
							{
								unsigned char cTmp[2];
								cTmp[0] = nModID;
								if ((i * 16 + n) % sScanCard.nModuleWidth == 0 )
								{
									//模组第一列
									cTmp[1] = 1;//模组边界编号，1-最左1列，2-中间列，3-最右1列
								}
								else if((i * 16 + n) % sScanCard.nModuleWidth == sScanCard.nModuleWidth - 1)
								{
									//模组最后一列
									cTmp[1] = 3;
									nModID ++;
								}
								else
								{
									//模组中间列
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
					//1：蓝,绿/空,红;绿,蓝/空,红，2：蓝,绿/空,红;蓝,绿/红,空，3：红,绿/空,蓝;绿,红/空,蓝
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;
					//int abc = 0;
					for (int k = 0; k < 2; k++) //line index
					{
						for (int i = 0; i < nLop; i ++)
						{
							for (int m = 0; m < 3; m ++)//R/G/B/三种颜色
							{
								//代表灯（非像素点）对应的相对位置，0左上，1右上, 2左下，3右下
								int nIndex = GetIndex(m, sScanCard.nVirTualArray, k);
								for (int n = 0; n < 16; n ++ )
								{
									switch (nIndex)
									{
									case 0:
									case 1:
										nTemp = i * 16 + n + sScanCard.nScanCardWidth * (0 + k);//左上，右上
										break;
									case 2:
									case 3:
										nTemp = i * 16 + n + sScanCard.nScanCardWidth * (1 + k);//右下，右下
										break;
									}

									//Module ID
									int nModID = (nTemp % (16 * nLop))/sScanCard.nModuleWidth;

									unsigned char cTmp[2];
									cTmp[0] = nModID;

									if (nTemp % sScanCard.nModuleWidth == 0 && (nIndex == 0 || nIndex == 2))
									{
										//模组第一列
										cTmp[1] = 1;//模组边界编号，1-最左1列，2-中间列，3-最右1列
									}
									else if(nTemp % sScanCard.nModuleWidth == sScanCard.nModuleWidth - 1
										&& (nIndex == 1 || nIndex == 3))
									{
										//模组最后一列
										cTmp[1] = 3;
									}
									else
									{
										//模组中间列
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
		else if (sScanCard.nDotCorrectTye == 2)//调色
		{
			if (sScanCard.nScreenType == DISPLAY_TYPE_REAL || (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL && !sScanCard.bVirtvalDisp))//实像素或虚拟屏实像素显示
			{
				if(!sScanCard.bExtendedEnable && !sScanCard.bExtendedEnableEx)
				{
					//模组边界查找表和校正地址包对应；
					int nLop = sScanCard.nScanCardWidth % 16 ?
						sScanCard.nScanCardWidth / 16 + 1 : sScanCard.nScanCardWidth / 16;
					for (int i = 0; i < nLop; i ++)
						for (int m = 0; m < 3; m ++)//RGB三种颜色
						{
							int nModID = (16 * i) / sScanCard.nModuleWidth;
							for (int n = 0; n < 16; n ++ )
							{
								unsigned char cTmp[2];
								cTmp[0] = nModID;
								if ((i * 16 + n) % sScanCard.nModuleWidth == nLeftIndex )
								{
									//模组第一列
									cTmp[1] = 1;//模组边界编号，1-最左1列，2-中间列，3-最右1列
								}
								else if((i * 16 + n) % sScanCard.nModuleWidth == sScanCard.nModuleWidth - 1)
								{
									//模组最后一列
									cTmp[1] = 3;
									nModID ++;
								}
								else
								{
									//模组中间列
									cTmp[1] = 2;
								}
								for (int j = 0; j < 3; j ++)//一种颜色对应的RGB
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
					for(int v = 0; v < sScanCard.nCard_zone_Num; ++v) //卡区个数循环
					{
						for(int u = 0; u < sScanCard.nSectionHorNum; ++u)//模组内区个数
						{
							for (int i = 0; i < nLop; i ++)
							{
								for (int m = 0; m < 3; m ++)//RGB三种颜色
								{
									//int nModID = (16 * i + u * sScanCard.nSectionWidth + nZoneDot) / sScanCard.nModuleWidth;

									for (int n = 0; n < 16; n ++ )
									{
										//横向区所在模组的序号
										int index1 = (i * 16 + n) / sScanCard.nSectionWidth;
										//横向模组像素像素点所在模组的位置
										int index2 = (i * 16 + n) % sScanCard.nSectionWidth;
										//区内偏移
										int nZoneOffset = ((index1 * sScanCard.nSectionHorNum) + u) * sScanCard.nSectionWidth;
										//卡区的偏移
										int nZoneDot = v * sScanCard.nCard_zone_width * sScanCard.nSectionHorNum;
										nTemp = nZoneOffset + index2 + nZoneDot;
										int nModID = nTemp/sScanCard.nModuleWidth;

										unsigned char cTmp[2];
										cTmp[0] = nModID;
										if (nTemp % sScanCard.nModuleWidth == nLeftIndex )
										{
											//模组第一列
											cTmp[1] = 1;//模组边界编号，1-最左1列，2-中间列，3-最右1列
										}
										else if(nTemp % sScanCard.nModuleWidth == sScanCard.nModuleWidth - 1)
										{
											//模组最后一列
											cTmp[1] = 3;
										}
										else
										{
											//模组中间列
											cTmp[1] = 2;
										}

										//abc++;
										for (int j = 0; j < 3; j ++)//一种颜色对应的RGB
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
	else if (sScanCard.nDataInputDir == 2 || sScanCard.nDataInputDir == 3)//上下进线
	{
		if (sScanCard.nScreenType == DISPLAY_TYPE_REAL)//实像素
		{
			if (sScanCard.nDotCorrectTye == 1)//调亮
			{

				int nDLine = sScanCard.nScanCardSectionRowNumber * ((int)pow(2.0, sScanCard.nDCBlineClkEn));
				for( int i = 0; i < nDLine; ++i)
					for (int m = 0; m < 3; m ++)//RGB三种颜色
					{
						int nModID = (16 * i) / sScanCard.nModuleWidth;
						for (int n = 0; n < 16; n ++ )
						{
							unsigned char cTmp[2];
							cTmp[0] = nModID;
							if ((i * 16 + n) % sScanCard.nModuleWidth == 0 )
							{
								//模组第一列
								cTmp[1] = 1;//模组边界编号，1-最左1列，2-中间列，3-最右1列
							}
							else if((i * 16 + n) % sScanCard.nModuleWidth == sScanCard.nModuleWidth - 1)
							{
								//模组最后一列
								cTmp[1] = 3;
								nModID ++;
							}
							else
							{
								//模组中间列
								cTmp[1] = 2;
							}

							memcpy(ucCorrectProcessing + nDataLen, cTmp, 2);
							nDataLen += 2;
						}
					}
			}
			else if (sScanCard.nDotCorrectTye == 2)//调色
			{
				int nDLine = sScanCard.nScanCardSectionRowNumber * ((int)pow(2.0, sScanCard.nDCBlineClkEn));
				for (int i = 0; i < nDLine; i ++)
					for (int m = 0; m < 3; m ++)//RGB三种颜色
					{
						int nModID = (16 * i) / sScanCard.nModuleWidth;
						for (int n = 0; n < 16; n ++ )
						{
							unsigned char cTmp[2];
							cTmp[0] = nModID;
							if ((i * 16 + n) % sScanCard.nModuleWidth == 0 )
							{
								//模组第一列
								cTmp[1] = 1;//模组边界编号，1-最左1列，2-中间列，3-最右1列
							}
							else if((i * 16 + n) % sScanCard.nModuleWidth == sScanCard.nModuleWidth - 1)
							{
								//模组最后一列
								cTmp[1] = 3;
								nModID ++;
							}
							else
							{
								//模组中间列
								cTmp[1] = 2;
							}
							for (int j = 0; j < 3; j ++)//一种颜色对应的RGB
							{
								memcpy(ucCorrectProcessing + nDataLen, cTmp, 2);
								nDataLen += 2;
							}
						}
					}
			}
		}
		else if (sScanCard.nScreenType == DISPLAY_TYPE_VIRTUAL)//虚拟屏
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

//设置扫描卡走线查找表
int CCLScanCardPackData::PackScanCardLinkTable(short nAddress,
						short nDataLineRange,
						short nDCBlineClkEn,
						LINKTABLE &sLinkTable,
						unsigned char * ucSendData)
{

	//走线查找表和数据线大类有关
	unsigned char dataLinkTemp[CL_MAX_BUF_NUMBER * 2];
	memset(dataLinkTemp, 0, CL_MAX_BUF_NUMBER * 2);
	int nLenTemp = 0;

	switch(nDataLineRange)
	{
	case 0:
		//红绿蓝分开
		memcpy(dataLinkTemp, sLinkTable.ucLinkTable, sLinkTable.nLen);
		nLenTemp = sLinkTable.nLen;
		break;
	case 1:
		//红绿蓝合一三色1点串行
	case 2:
		//红绿蓝合一三色8点串行
	case 3:
		//红绿蓝合一三色16点串行
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
		//红绿蓝合一四色1点串行
	case 5:
		//红绿蓝合一四色8点串行
	case 6:
		//红绿蓝合一四色16点串行
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

//设置扫描卡区走线查找表
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

		//打扫描卡区走线查找包
		int nLen = CCLPackCommunicationData::PackDataBase(0,nAddress,0x56,n,false,
								dataLinkTemp + n * CL_SEND_EFFECT_DATA_SIZE,uSendData);

		memcpy(ucSendData + nLength,uSendData,nLen);
		nLength += nLen;
	}

	return nLength;

}

int CCLScanCardPackData::PackHUBLookup(int nAddress,//扫描卡地址
										LinkTable hublinktable,
										unsigned char* ucSendData
										)//发送数据
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
		//打扫描卡区走线查找包
			int nLen = CCLPackCommunicationData::PackDataBase(nAddress,0,0xE5,n,false,
								dataLinkTemp + n * CL_SEND_EFFECT_DATA_SIZE,uSendData);
		memcpy(ucSendData + nLength,uSendData,nLen);
		nLength += nLen;
	}
	return nLength;
//	return 0;
}

//设置GAMMA表
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
			//高位在后，低位在前
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
			//高位在前，低位在后
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


//设置打开箱体指示灯
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

	//打开箱体指示灯
	int nLength = CCLPackCommunicationData::PackDataBase(0,nAddress,0x40,0x06,false,ucData,ucSendData);

	return nLength;
}

//设置单点校正使能(广播无应答)。nDotType：0 - 无， 1 - 调亮， 2 - 调色
int CCLScanCardPackData::PackCalibrationEnable(short nAddress,
						bool bEnable, short nDotType,
						unsigned char * ucSendData)
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	unsigned char dataCalib[CL_SEND_EFFECT_DATA_SIZE];
	memset(dataCalib, 0, CL_SEND_EFFECT_DATA_SIZE);
	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后
	//Corr_en_ctrl	8	0xFF	7--0
	//bit7--4	预留，填充0xff
	//bit3	启动校正功能（不涉及使能，只涉及功能，启动时才上电默认读参数到SDRAM）0：启动，1：关闭
	//	bit2	1	从卡FLASH读到SDRAM		0	从模组读参数FLASH，再到SDRAM
	//	bit1	1	调亮	0	调色
	//	bit0	0	关闭	1	使能
	int nDotCorrType = 0;
	int nStart = 0;
	if (nDotType == 0)
	{
		nStart = 1;//关闭
	}
	else if (nDotType == 2)
	{
		nDotCorrType = 1;
	}

	dataCalib[0] = (nStart << 3) | (nDotCorrType << 1) | (bEnable ? 1 : 0);

	int nLength = CCLPackCommunicationData::PackDataBase(0,nAddress,0x40,0x0A,false,dataCalib,ucSendData);

	return nLength;
}

//发送亮度参数,nBrightPercent:亮度百分比(0 - 100)
int CCLScanCardPackData::PackBrightness(short nAddress,
					int nBrightPercent,
					unsigned char * ucSendData)
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	//亮度0-1000换算成0-255
	//int nBrightness =(int) (0.256 * nBrightPercent + 0.5);
	int nBrightness = nBrightPercent;

	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, CL_SEND_EFFECT_DATA_SIZE);
	//亮度0-256，为了兼容原版本，当值为256时，赋值0x1FF
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

//发送3D配置信息
//1.	同步延时参数（Synchron Delay）：设置范围 -127 - +127
//2.	关闭扫描周期参数（Disable Scan Cycle）：设置范围 0 - 255
int CCLScanCardPackData::Pack3DPara(short nAddress,
					int nSynchronDelay,
					int DisableScanCycle,
					unsigned char * ucSendData)
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, CL_SEND_EFFECT_DATA_SIZE);

	char cDelay = 0;//同步信号延时相位	0：正	1：负
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


//锁屏或解屏
int CCLScanCardPackData::PackLockUnlock(short nAddress,
					bool bLock,
					unsigned char * ucSendData)
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, CL_SEND_EFFECT_DATA_SIZE);

	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后
	//解锁标识   0x01 - 手动锁屏   0x02 - 手动解屏
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

//智能编址包
int CCLScanCardPackData::PackScanCardIntelligentPara(int nAddressMin,
					unsigned char * ucSendData)
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	unsigned char ucData[CL_SEND_EFFECT_DATA_SIZE];
	memset(ucData, 0, CL_SEND_EFFECT_DATA_SIZE);

	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后
	//Smart_addr_beg	8	0x01	7--0	智能编址起始号.默认0x01
	//预留15字节
	ucData[0] = (unsigned char) nAddressMin;
	int nLength = CCLPackCommunicationData::PackDataBase(0,0xFF,0x40,0X0B,false,ucData,ucSendData);

	return nLength;
}

//继电器输出
int CCLScanCardPackData::PackRelay(short nAddress,short nRelayID, bool bPower,
			unsigned char * ucSendData)
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	//数据域字节
	unsigned char data[CL_SEND_EFFECT_DATA_SIZE];
	memset(data, 0, CL_SEND_EFFECT_DATA_SIZE);

	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后
	//1 继电器标号(RELAY_TAG)	1	0X01~0X04	继电器标号N，对应第一道第四路
	data[0] = nRelayID + 1;
	//2 继电器的输出状态（RELAY_STATUES）	1	0x00	0X00-关   0X01-开
	if (bPower)
		data[1] = 0x01;
	else
		data[1] = 0x00;

	//往串口发送数据并等待获取数据
	int nLength = CCLPackCommunicationData::PackDataBase(0,nAddress,0xD2,0,true,data,ucSendData);

	return nLength;
}

//继电器自动控制
int CCLScanCardPackData::PackRelayAttribute(short nAddress,short nRelayID,
					bool bOverHeatOff,bool bOverHumidityOff,bool bSmogOff,
					unsigned char * ucSendData)
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	//数据域字节
	unsigned char data[CL_SEND_EFFECT_DATA_SIZE];
	memset(data, 0, CL_SEND_EFFECT_DATA_SIZE);

	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后
	//1 继电器标号(RELAY_TAG)	1	0X01~0X04	继电器标号N，对应第一道第四路
	data[0] = nRelayID + 1;
	//一级属性  0x01	大屏电源模式    0x02	风扇模式
	data[1] = 1;
	//二级属性	0x01	手动模式　0x02	定时模式	0x03	温控模式	0x04	湿控模式
	data[2] = 0;
	//0x01	烟雾关屏
	data[3] =  bSmogOff ? 1 : 0;
	//0x01	超温关屏(上限温度不能为负)
	data[4] =  bOverHeatOff ? 1 : 0;
	//0x01　	超湿关屏
	data[5] =  bOverHumidityOff ? 1 : 0;

	//往串口发送数据并等待获取数据
	int nLength = CCLPackCommunicationData::PackDataBase(0,nAddress,0xD1,0,true,data,ucSendData);

	return nLength;
}

//继电器打开、闭合门限
int CCLScanCardPackData::PackRelayThreshold(short nAddress,short nRelayID,
						float fTemperatureMin,
						float fTemperatureMax,
						float fHumidityMin,
						float fHumidityMax,
						unsigned char * ucSendData)
{
	memset(ucSendData, 0, CL_SEND_PACK_SIZE);

	//数据域字节
	unsigned char data[CL_SEND_EFFECT_DATA_SIZE];
	memset(data, 0, CL_SEND_EFFECT_DATA_SIZE);

	//9 数据域字节   16 从低到高排列，从低到高发送  低位在前，高位在后
	//1 继电器标号(RELAY_TAG)	1	0X01~0X04	继电器标号N，对应第一道第四路
	data[0] = nRelayID + 1;
	//温度上限值	2	-40.0℃--123.0 ℃		温度值，最高bit为符号位：0：正数； 1：负数。高位在前，低位在后。精确到0.1度。上位机将要设置的值乘以10发送给监控板，监控板将接收值除以10。
	int nTemp = ((int)(abs(fTemperatureMax) * 10)) | (fTemperatureMax < 0 ? 0x8000 : 0x0000);
	CTool::ExchangeInteger(nTemp, data + 1, 2);
	//温度下限值	2	-40.0℃--123.0 ℃		温度值，最高bit为符号位：0：正数； 1：负数。高位在前，低位在后。精确到0.1度。上位机将要设置的值乘以10发送给监控板，监控板将接收值除以10。
	nTemp = ((int)(abs(fTemperatureMin) * 10)) | (fTemperatureMin < 0 ? 0x8000 : 0x0000);
	CTool::ExchangeInteger(nTemp, data + 3, 2);
	//湿度上限值	2	0.0-%--100.0%		湿度值，精确到0.1度。上位机将要设置的值乘以10发送给监控板，监控板将接收值除以10。
	nTemp = ((int)(abs(fHumidityMax) * 10)) | (fHumidityMax < 0 ? 0x8000 : 0x0000);
	CTool::ExchangeInteger(nTemp, data + 5, 2);
	//湿度下限值	2	0.0-%--100.0%		湿度值，精确到0.1度。上位机将要设置的值乘以10发送给监控板，监控板将接收值除以10。
	nTemp = ((int)(abs(fHumidityMin) * 10)) | (fHumidityMin < 0 ? 0x8000 : 0x0000);
	CTool::ExchangeInteger(nTemp, data + 7, 2);


	//往串口发送数据并等待获取数据
	int nLength = CCLPackCommunicationData::PackDataBase(0,nAddress,0xD3,0,true,data,ucSendData);

	return nLength;
}
//获取虚拟三色灯一像素屏 某灯（非像素点）在灯板中的相对位置索引0左上，1右上, 2左下，3右下
//nRGBIndex: 0:R，1:G, 2:B;
//nVirTualArray:排线 1：蓝,绿/空,红;绿,蓝/空,红；2：蓝,绿/空,红;蓝,绿/红,空；3：红,绿/空,蓝;绿,红/空,蓝
//nLine:数据行序号，三色灯是两行数据，所以nLine<2
int CCLScanCardPackData::GetIndex(int nRGBIndex,int nVirTualArray, int nLine)
{
	if ( (0 != nRGBIndex && 1 != nRGBIndex && 2 != nRGBIndex)
		|| (1 != nVirTualArray && 2 != nVirTualArray && 3 != nVirTualArray)
		|| (nLine != 0 && nLine != 1) )
	{
		return -1;
	}

	int nIndex = 0;//代表灯（非像素点）对应的相对位置，0左上，1右上, 2左下，3右下
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
