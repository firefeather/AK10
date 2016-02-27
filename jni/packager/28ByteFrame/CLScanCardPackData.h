#pragma once
#include "../Datastruct/StructSingleScanCard.h"
#include "PackageBase/CLPackCommunicationData.h"

class CCLScanCardPackData
{
public:
	CCLScanCardPackData(void);
	~CCLScanCardPackData(void);

	//��ɨ�迨���������ݰ������� int - ������
	int PackScanCardData(int nScanCardAddress,//ɨ�迨��ַ
							short nAtlvcAddressSecond,
							int nPackID,//˳���
							CStructSingleScanCard sScanCard, //ɨ�迨�����ṹ
							unsigned char* ucSendData,//��������
							int nEmptyByte = 64);//У��ǰĬ�Ͽ��ֽ���
	

	//�������������ð�(0x91)
	int PackStartOrEnd(int nScanCardAddress,//ɨ�迨��ַ
						short nAtlvcAddressSecond,
						bool bStart, 
						short nType, 
						short nModuleRow,
						short nModulCol,
						unsigned char* ucSendData);//��������


	//��HUB������
	int PackHUBPara(int nAddress,//ɨ�迨��ַ
					short nAtlvcAddressSecond,
					CStructSingleScanCard sScancard,
					unsigned char* ucSendData);//��������


	//�����ɫ�������
	int PackColorTempCurrentData(int nAddress,//ɨ�迨��ַ
						short nAtlvcAddressSecond,
						LPCOLOURRGB sColorRGB,unsigned char* ucSendData);

	//�����ɫ�����ݰ�
	int PackColorTemperatureData(int nAddress,//ɨ�迨��ַ
									short nAtlvcAddressSecond,
									LPCOLOURRGB sColorRGB,int nCrTempID,short nHighLowGap,
									short nGrayEnhanceMode, int nDeductbit, unsigned char* ucSendData);//��������

	//�������׶������
	int PackBrightCurrentData(int nAddress,//ɨ�迨��ַ
								short nAtlvcAddressSecond,
								LPCOLOURRGB sColorRGB,int nCrTempID,unsigned char* ucSendData,int nIndex);

	//��ɨ�迨�������������(0x95)
	int PackScanCardUpdateStart(int nAddress,short nAtlvcAddressSecond,short nUpdateType, bool bStart,bool bUpdateBoot,
								unsigned char * ucSendData);

	//���������������ʼ��(�㲥��Ӧ��)
	int PackSaveScanCardPara(short nAddress,short nAtlvcAddressSecond,int nTpyeID, bool bDefaul,unsigned char *ucSendData);

	//��ɨ�迨�����������ð�
	int PackScanCardLoadRegionData(short nAddress,short nAtlvcAddressSecond,RECT rtLoad,
							short nStartX,short nStartY,unsigned char * ucSendData);

	//�������翨���ز�����������ظ���ʵ��ɨ�迨�����������õ�
	int PackNetCardLoadRegionData(short nAddress,short nAtlvcAddressSecond,RECT rtLoad,
		short nStartX,short nStartY,unsigned char * ucSendData);

	//������ƴ��ز�����������//nFlag = 0ʱ���ʾ�������//nFlag = 1ʱ���ʾ���翨�����������
	int PackSpotlightLoadRegionData(short nAddress,short nAtlvcAddressSecond,RECT rtLoad,
		short nStartX,short nStartY,unsigned char * ucSendData, short nFlag = 0);


	//��ģ�������߽߱�ϵ����
	int PackModelLineCoeffData(short nAddress,short nAtlvcAddressSecond,short nRowID,short nColID,
							LineCoeff sLineCoeff,unsigned char * ucSendData);

	//��ɨ�迨������
	void UnPackScanCardParam(map<int,CCLPackCommunicationData> & mUnPackData,
								CStructSingleScanCard & sScanCard,RECT & rtLoad);

	//���ɫ�²���
	void UnPackColorTempParam(map<int,CCLPackCommunicationData> & mUnPackData,
		short & nColorTempIndex, COLOURTEMFLAG & ColorTempFlag);


	//////////////////////////////////////////////////////////////////////

	//��ɨ�迨���������ݰ������� int - �����ȣ�6������
	/*1������������ز���
	2��ɨ����ز���
	3������ͼ������ز���1
	4������ͼ������ز���2
	5������ͼ������ز���3
	6��У���ü�����ز���
	*/
	int Pack6ScanCardData(int nScanCardAddress,//ɨ�迨��ַ
						CStructSingleScanCard & sScanCard, //ɨ�迨�����ṹ
						unsigned char* ucSendData,//��������
						int nEmptyByte = 64);//У��ǰĬ�Ͽ��ֽ���

	//�����ź���ʾ��
	int PackNoSingleDisp(short nAddress,
							short nNoSingleDisp,
							unsigned char * ucSendData);//��������

	//���㴦���
	int PackOperationProcessing(short nAddress,
							short nScancardSectionRowNumber,
							unsigned char * ucSendData);//��������

	//��Ƶ�����
	int PackVideoProcessing(short nAddress,
							CStructSingleScanCard & sScanCard,
							unsigned char * ucSendData);//��������

	//���ö�ȡУ�����ݵ�ַ��
	int PackCorrectProcessing(short nAddress,
							CStructSingleScanCard & sScanCard,
							unsigned char * ucSendData);//��������

	//���ñ߽�ϵ�����ұ�
	int PackCorrectProcessingLookup(short nAddress,
							CStructSingleScanCard & sScanCard,
							unsigned char * ucSendData);//��������

	//����ɨ�迨���߲��ұ�
	int PackScanCardLinkTable(short nAddress,
							short nDataLineRange,
							short nDCBlineClkEn,
							LINKTABLE &sLinkTable,
							unsigned char * ucSendData);//��������

	//����ɨ�迨�����߲��ұ�
	int PackScanCardSectionLinkTable(short nAddress,
							LINKTABLE &sLinkTable,
							unsigned char * ucSendData);//��������

	int PackHUBLookup(int nAddress,//ɨ�迨��ַ
						LinkTable hublinktable,
						unsigned char* ucSendData);

	//����GAMMA��
	int PackGammaTable(short nAddress,
						GAMMADATA & sGammaData,
						int nColorIndex,
						unsigned char * ucSendData);//��������

	//���õ���У��ʹ��(�㲥��Ӧ��)��nDotType��0 - �ޣ� 1 - ������ 2 - ��ɫ
	int PackCalibrationEnable(short nAddress,
							bool bEnable, short nDotType,
							unsigned char * ucSendData);//��������

	//�������Ȳ���,nBrightPercent:���Ȱٷֱ�(0 - 100)
	int PackBrightness(short nAddress,
						int nBrightPercent,
						unsigned char * ucSendData);//��������


	//����3D������Ϣ
	//1.	ͬ����ʱ������Synchron Delay�������÷�Χ -127 - +127
	//2.	�ر�ɨ�����ڲ�����Disable Scan Cycle�������÷�Χ 0 - 255
	int Pack3DPara(short nAddress,
						int nSynchronDelay,
						int DisableScanCycle,
						unsigned char * ucSendData);//��������
						

	//���������
	int PackLockUnlock(short nAddress,
						bool bLock,
						unsigned char * ucSendData);//��������

	//���ܱ�ַ��
	int PackScanCardIntelligentPara(int nAddressMin,
						unsigned char * ucSendData);//��������

	//�̵������
	int PackRelay(short nAddress,short nRelayID, bool bPower,
				unsigned char * ucSendData);//��������

	//�̵����Զ�����
	int PackRelayAttribute(short nAddress,short nRelayID,
						bool bOverHeatOff,bool bOverHumidityOff,bool bSmogOff,
						unsigned char * ucSendData);//��������

	//�̵����򿪡��պ�����
	int PackRelayThreshold(short nAddress,short nRelayID,
							float fTemperatureMin,
							float fTemperatureMax,
							float fHumidityMin,
							float fHumidityMax,
							unsigned char * ucSendData);//��������

	//���ô�����ָʾ��
	int PackOpenCabinetLamps(short nAddress,
							bool bOpen,
							unsigned char * ucSendData);//��������

private:
	//��ȡ������ɫ��һ������ ĳ�ƣ������ص㣩�ڵư��е����λ������0���ϣ�1����, 2���£�3����
	//nRGBIndex: 0:R��1:G, 2:B;
	//nVirTualArray:���� 1����,��/��,��;��,��/��,�죻2����,��/��,��;��,��/��,�գ�3����,��/��,��;��,��/��,��
	//nLine:��������ţ���ɫ�����������ݣ�����nLine<2
	int GetIndex(int nRGBIndex,int nVirTualArray, int nLine);
};
