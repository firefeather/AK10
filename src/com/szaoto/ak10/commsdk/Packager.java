/*
   * �ļ��� Packager.java
   * ���������б�com.szaoto.ak10.commsdk
   * �汾��Ϣ���汾��
   * ��������2013��12��28������8:22:33
   * ��Ȩ���� liangdb-szaoto
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
 * ����Packager
 * ���� liangdb
 * ��Ҫ���� ���������ӿ�
 * ��������2013��12��28��
 * �޸��ߣ��޸����ڣ��޸�����
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
	//��̫��Э��֡
	////////////////////////////////////////////////////////////////////////////////////
	//���ش����������
	static public native byte[] EthernetPackDataBase(byte[] ucDestAddress,				//Ŀ�ĵ�ַ
													FrameDataField sFrameDataField	//����(����������Ϣ)
													);
	
	//����
	//���ͣ�EthernetPackDataWrite�󣬵��� spi_write д�뼴��
	//��ȡ��EthernetPackDataRead�󣬵��� spi_write���ٵ��� spi_read �������ݣ�Ȼ�� EthernetUnPackDataRead ���
	
	//���� д���� ���(ֱ�ӵ�д��������)
	//���ش����������
	static public native byte[] EthernetPackDataWrite(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
													byte[] ucAddress,				//FIFO/REG��ַ
													int nSequenceNumber,			//���к�
													int nLength,					//���ȣ���ʵ���ݳ��ȣ�
													byte[] ucData		
													//�������ֽ�
										
			);				
	
	//���� ������ �������ȡ����������
	//���ش����������
	static public native byte[] EthernetPackDataRead(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
													byte[] ucAddress,				//FIFO/REG��ַ
													int nLength						//���ȣ���ʵ���ݳ��ȣ�	//��������
													);
	
	//�����Ӧ��
	static public native FrameDataField EthernetUnPackDataRead(byte[] ucReceiveData,//��������
																int nLength);		//����
	
	////////////////////////////////////////////////////////////////////////////////////
	//28ByteЭ��֡
	////////////////////////////////////////////////////////////////////////////////////
	static public native byte[] NativePackDataBase(int ucDestAddress,	//Ŀ���ַ
												byte ucPackType,	//������
												int unSequenceNumber,//˳���
												int nAnsweredFlag,	//Ӧ���ʾ
												byte[] ucData		//�������ֽ�
												);	//�����������
	
	
	
	//////////////////////////////////////////////////////////////////////////////////
	//���ٴ�����(����)
	//////////////////////////////////////////////////////////////////////////////////
	//######################################
	//ɨ�迨
	//######################################
	//���ȴ��
	static public native byte[] PackSetBright(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
												byte[] ucAddress,			//FIFO/REG��ַ
												int nBrightness);
	//1.	ͬ����ʱ������Synchron Delay����2.	�ر�ɨ�����ڲ�����Disable Scan Cycle�������
	static public native byte[] PackSet3DPara(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
												byte[] ucAddress,			//FIFO/REG��ַ
												int nSynchron_Delay,
												int nDisableScanCycle);
	//ɫ�´��
	static public native byte[] PackSetColorTemperature(byte[] ucDestAddress,		//Ŀ�ĵ�ַ
														byte[] ucAddress,			//FIFO/REG��ַ
														ColourRGB sColorRGB,short nHighLowGap,
														short nGrayEnhanceMode);
	//�����ɫ�������
	static public native byte[] PackColorTempCurrentData(byte[] ucDestAddress,		//Ŀ�ĵ�ַ
													byte[] ucAddress,			//FIFO/REG��ַ
													int nAddress,//ɨ�迨��ַ
													ColourRGB sColorRGB);
	
	//GAMMA���
	
	static public native byte[] PackSetGamma(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
												byte[] ucAddress,			//FIFO/REG��ַ
												GammaData sGammaData,
												int nColorIndex);
	
	//����GAMMA��
	static public native byte[] PackGammaTable(//byte[] ucDestAddress,			//Ŀ�ĵ�ַ
											//byte[] ucAddress,			//FIFO/REG��ַ
											short nAddress,
											GammaData sGammaData,
											int nColorIndex);
	
	//��ɨ�迨���������ݰ������� int - ������
	static public native byte[] PackScanCardData(//byte[] ucDestAddress,			//Ŀ�ĵ�ַ
												//byte[] ucAddress,			//FIFO/REG��ַ
												int nScanCardAddress,//ɨ�迨��ַ
												int nPackID,//˳���
												CStructSingleScanCard sScanCard, //ɨ�迨�����ṹ
												int nEmptyByte);//У��ǰĬ�Ͽ��ֽ���
	
	
	//�������������ð�(0x91)
	static public native byte[] PackStartOrEnd(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
												byte[] ucAddress,			//FIFO/REG��ַ
												int nScanCardAddress,//ɨ�迨��ַ
												boolean bStart,
												short nType,
												short nModuleRow,
												short nModulCol);


	//��HUB������
	static public native byte[] PackHUBPara(//byte[] ucDestAddress,			//Ŀ�ĵ�ַ
										//byte[] ucAddress,			//FIFO/REG��ַ
										int nAddress,//ɨ�迨��ַ
										CStructSingleScanCard sScancard
										);
	
	//��HUB������
	static public native byte[] PackHUBLookup(//byte[] ucDestAddress,			//Ŀ�ĵ�ַ
											//byte[] ucAddress,			//FIFO/REG��ַ
											int nAddress,//ɨ�迨��ַ
											LinkTable hublinktable);


	//��ɨ�迨�������������(0x95)
	static public native byte[] PackScanCardUpdateStart(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
								byte[] ucAddress,			//FIFO/REG��ַ
								int nAddress,
								short nUpdateType,
								boolean bStart,
								boolean bUpdateBoot);

	//���������������ʼ��
	static public native byte[] PackSaveScanCardPara(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
													byte[] ucAddress,			//FIFO/REG��ַ
													short nAddress,
													int nTpyeID,
													boolean bDefaul);

	//��ɨ�迨�����������ð�
	static public native byte[] PackScanCardLoadRegionData(//byte[] ucDestAddress,			//Ŀ�ĵ�ַ
								//	byte[] ucAddress,			//FIFO/REG��ַ
									short nAddress,
									RECT rtLoad,
									short nStartX,
									short nStartY);

	//////////////////////////////////////////////////////////////////////

	//��ɨ�迨���������ݰ������� int - �����ȣ�6������
	/*1������������ز���
	2��ɨ����ز���
	3������ͼ������ز���1
	4������ͼ������ز���2
	5������ͼ������ز���3
	6��У���ü�����ز���
	*/
	static public native byte[] Pack6ScanCardData(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
						byte[] ucAddress,			//FIFO/REG��ַ
						int nScanCardAddress,//ɨ�迨��ַ
						CStructSingleScanCard sScanCard, //ɨ�迨�����ṹ
						Drive_ic_reg ndrive_ic_reg,
						int nEmptyByte);//У��ǰĬ�Ͽ��ֽ���

	//�����ź���ʾ��
	static public native byte[] PackNoSingleDisp(//byte[] ucDestAddress,			//Ŀ�ĵ�ַ
							//byte[] ucAddress,			//FIFO/REG��ַ
							short nAddress,
							short nNoSingleDisp);

	//���㴦���
	static public native byte[] PackOperationProcessing(//byte[] ucDestAddress,			//Ŀ�ĵ�ַ
							//byte[] ucAddress,			//FIFO/REG��ַ
							short nAddress,
							short nScancardSectionRowNumber);

	//��Ƶ�����
	static public native byte[] PackVideoProcessing(//byte[] ucDestAddress,			//Ŀ�ĵ�ַ
							//byte[] ucAddress,			//FIFO/REG��ַ
							short nAddress,
							CStructSingleScanCard sScanCard
							);

	//���ö�ȡУ�����ݵ�ַ��
	static public native byte[] PackCorrectProcessing(//byte[] ucDestAddress,			//Ŀ�ĵ�ַ
							//byte[] ucAddress,			//FIFO/REG��ַ
							short nAddress,
							CStructSingleScanCard sScanCard);

	//���ñ߽�ϵ�����ұ�
	static public native byte[] PackCorrectProcessingLookup(//byte[] ucDestAddress,			//Ŀ�ĵ�ַ
							//byte[] ucAddress,			//FIFO/REG��ַ
							short nAddress,
							CStructSingleScanCard sScanCard);

	//����ɨ�迨���߲��ұ�
	static public native byte[] PackScanCardLinkTable(//byte[] ucDestAddress,			//Ŀ�ĵ�ַ
							//byte[] ucAddress,			//FIFO/REG��ַ
							short nAddress,
							short nDataLineRange,
							short nDCBlineClkEn,
							LinkTable sLinkTable);

	//����ɨ�迨�����߲��ұ�
	static public native byte[] PackScanCardSectionLinkTable(//byte[] ucDestAddress,			//Ŀ�ĵ�ַ
									//byte[] ucAddress,			//FIFO/REG��ַ
									short nAddress,
									LinkTable sLinkTable);
	
	

	//���õ���У��ʹ��(�㲥��Ӧ��)��nDotType��0 - �ޣ� 1 - ������ 2 - ��ɫ
	static public native byte[] PackCalibrationEnable(//byte[] ucDestAddress,			//Ŀ�ĵ�ַ
								//byte[] ucAddress,			//FIFO/REG��ַ
								short nAddress,
								boolean bEnable, short nDotType);
	
	//���������
	static public native byte[] PackLockUnlock(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
						byte[] ucAddress,			//FIFO/REG��ַ
						short nAddress,
						boolean bLock);

	//���ܱ�ַ��
	static public native byte[] PackScanCardIntelligentPara(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
									byte[] ucAddress,			//FIFO/REG��ַ
									int nAddressMin);

	//�̵������
	static public native byte[] PackRelay(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
										byte[] ucAddress,			//FIFO/REG��ַ
										short nAddress,short nRelayID, boolean bPower);

	//�̵����Զ�����
	static public native byte[] PackRelayAttribute(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
						byte[] ucAddress,			//FIFO/REG��ַ
						short nAddress,short nRelayID,
						boolean bOverHeatOff,boolean bOverHumidityOff,boolean bSmogOff);

	//�̵����򿪡��պ�����
	static public native byte[] PackRelayThreshold(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
							byte[] ucAddress,			//FIFO/REG��ַ
							short nAddress,short nRelayID,
							float fTemperatureMin,
							float fTemperatureMax,
							float fHumidityMin,
							float fHumidityMax);

	//���ô�����ָʾ��
	static public native byte[] PackOpenCabinetLamps(//byte[] ucDestAddress,			//Ŀ�ĵ�ַ
						//	byte[] ucAddress,			//FIFO/REG��ַ
							short nAddress,
							boolean bOpen);
	
	
	
	//######################################
	static public native MonitorData test();
	//������ؿ����ض������,��������ض����
	static public native byte[] PackMonitorData(int monitortype,
													int monitoractiontype,
													byte[] ucDestAddress,			//Ŀ�ĵ�ַ
													byte[] ucAddress,			//FIFO/REG��ַ
													int address                  //������ؿ���ַ������㲥�ض���ַ
													
			);
	// 55 55 12 34 FF 42 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 BC
	static public native byte[] PackMonitorConnectstate(//����ɨ�迨��ѯ�����ж�����״̬
															byte[] ucDestAddress,			//Ŀ�ĵ�ַ
															byte[] ucAddress,		//FIFO/REG��ַ
															int address             //�㲥��ַ
															);

	static public native byte[] PackAcquiAcquisitionCardEdidInfor(
															byte[] ucDestAddress,			//Ŀ�ĵ�ַ
															byte[] ucAddress,		//FIFO/REG��ַ
															byte[] edidinform
															);
	/*//������ݹ㲥���
	static public native byte[]PackCustomMonitorDataFF(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
														byte[] ucAddress			//FIFO/REG��ַ
			);
	//�����ѹ������ݴ��
	static public native byte[]PackVoltageMonitorData(int address,					//	����㲥�ض���ַ
														byte[] ucDestAddress,			//Ŀ�ĵ�ַ
														byte[] ucAddress			//FIFO/REG��ַ
			);
	//�����ѹ������ݹ㲥���
	static public native byte[]PackVoltageMonitorDataFF(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
														byte[] ucAddress			//FIFO/REG��ַ
			);
	//��ȡ���壨ɨ�迨�����ʼ�����ݣ��㲥��Ӧ��
	static public native byte[]PackPowerMonitorData(int address,					//���ʼ�����ݣ��㲥)
														byte[] ucDestAddress,			//Ŀ�ĵ�ַ
														byte[] ucAddress			//FIFO/REG��ַ
			);
	//��ȡ���壨ɨ�迨�����ʼ�����ݣ�ȫ���㲥��Ӧ��
	static public native byte[]PackPowerMonitorDataFF(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
													byte[] ucAddress			//FIFO/REG��ַ
			);
	//��ȡ���壨ɨ�迨����������ݣ��㲥��Ӧ��
	static public native byte[]PackPointDetectMonitorData(int address,			//	��������ݣ��㲥)
													byte[] ucDestAddress,			//Ŀ�ĵ�ַ
													byte[] ucAddress			//FIFO/REG��ַ
			);
	//��ȡ���壨ɨ�迨����������ݣ�ȫ���㲥��Ӧ��
	static public native byte[]PackPointDetectMonitorDataFF(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
														byte[] ucAddress			//FIFO/REG��ַ
			);
	//��ȡ���壨ɨ�迨��������������㲥��Ӧ��
	static public native byte[]PackNetWebErrorPakage(int address  ,                  //������������㲥
														byte[] ucDestAddress,			//Ŀ�ĵ�ַ
														byte[] ucAddress			//FIFO/REG��ַ
			);
	//��ȡ���壨ɨ�迨����������ȫ���㲥��Ӧ��
	static public native byte[]PackNetWebErrorPakageFF(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
														byte[] ucAddress			//FIFO/REG��ַ
			);*/
	
	static public native int UnPack28Data(byte[] Data28Commond, short commond );
	static public native MonitorData UnPackCustomMonitorData(byte[] ucDestAddress, byte[] recieve,MonitorData pMonitorData);
	static public native byte[] UnPackPowerMonitorData(byte[] ucDestAddress , byte[] recieve ,MonitorData pMonitorData);
	static public native byte[] UnPackPointDetectMonitorData(byte[] ucDestAddress ,byte[] recieve ,MonitorData pMonitorData , ScanCardAttachment  sScanCardAtt);
	static public native byte[] UnPackNetWebErrorPackage(byte[] ucDestAddress,byte[] recieve , MonitorData pMonitorData);
	static public native byte[] UnPackMonitorDataFunRate(byte[] ucDestAddress , byte[] recieve , MonitorData pMonitorData);
	
		
	
	//######################################
	//######################################
	//ϵͳ��
	//######################################
	
	//######################################
	//�ɼ���
	//######################################
	//�Աȶȴ��
	
	//���Ͷȴ��
	
	//######################################
	//���Ϳ�
	//######################################
	static public native byte[] PackMutiple28byteData(byte[] ucDestAddress,			//Ŀ�ĵ�ַ
													byte[] ucAddress,			//FIFO/REG��ַ
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
