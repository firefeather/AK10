package com.szaoto.ak10.datacomm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.szaoto.ak10.common.GammaData;
import com.szaoto.ak10.common.RECT;
import com.szaoto.ak10.common.CabinetData.CHIP_TYPE;
import com.szaoto.ak10.common.CabinetData.CabinetInformation;
import com.szaoto.ak10.common.CabinetData.ScanCardAttachment;
import com.szaoto.ak10.commsdk.FrameDataField;
import com.szaoto.ak10.commsdk.Packager;
import com.szaoto.ak10.commsdk.SpiControl;
import com.szaoto.ak10.dataaccess.DataAccessCabinetLibrary;
import com.szaoto.ak10.dataaccess.DataAccessGammaTable;
import com.szaoto.ak10.ownerdraw.CabinetViewObj;
import com.szaoto.ak10.sqlitedata.CabinetDB;
import com.szaoto.ak10.sqlitedata.CbtData;
import com.szaoto.ak10.sqlitedata.InterfaceDB;
import com.szaoto.ak10.sqlitedata.IntfData;
import com.szaoto.ak10.util.LogUtil;
import com.szaoto.ak10.util.UtilFun;

public class ConnChartComm {

	static final String TAG = "ConnChartComm";

	public ConnChartComm() {

	}

	// ��ȡ���ļ���Ϣ
	static public CabinetInformation GetCbtInfoFromCbtLib(int CbtId, int LEDID) {
		CabinetInformation retCbtInformation = new CabinetInformation();
		String strTypeName = CabinetDB.GetTypeStringById(CbtId, LEDID);
		DataAccessCabinetLibrary.getCabinetByname(strTypeName,
				retCbtInformation, 0);
		return retCbtInformation;

	}

	static public HashMap<String, CabinetInformation> GetCbtInfoMap(int LEDID) {
		// ��������CabinetInformation�ģ�ap
		// ��ȡ�������͵�TypeName
		ArrayList<String> ArrCabinetNameList = CabinetDB
				.GetTypeStringArray(LEDID);

		HashMap<String, CabinetInformation> CabinetHashMap = new HashMap<String, CabinetInformation>();
		CabinetInformation cbtinfo = new CabinetInformation();
		for (int i = 0; i < ArrCabinetNameList.size(); i++) {
			String nameString = ArrCabinetNameList.get(i);
			DataAccessCabinetLibrary.getCabinetByname(nameString, cbtinfo, 0);
			if (!nameString.equals("default")) {
				CabinetHashMap.put(ArrCabinetNameList.get(i), cbtinfo);
			}
		}

		return CabinetHashMap;
	}

	static public int SendtoScanCard(byte[] ucDestAddress, byte[] ucAddress,
			byte[] ucSendData) {
		byte[] EthPackData;
		byte[] subdata;
		int nResult = -1;
		int length = ucSendData.length;
		if (length % 28 != 0) {
			return 0;
		}
		int Max28Bytelength = 53 * 28;// 1500���ֽ�������ͬʱ����53(1484�ֽ�)��ɨ�迨28�ֽ����1484/28
										// = 53
		if (length / 28 <= 53) {
			EthPackData = Packager.PackMutiple28byteData(ucDestAddress,
					ucAddress, ucSendData, length);
			try {
				LogUtil.WriteLog(EthPackData, false);
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				nResult = SpiControl.WriteSpi(EthPackData,
						(length + 18) < 64 ? 64 : length + 18);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (0 > nResult) {
				return 1;
			}

		} else {
			int mlength = length % Max28Bytelength == 0 ? length
					/ Max28Bytelength : length / Max28Bytelength + 1;
			for (int i = 0; i < mlength; i++) {
				if (i == mlength - 1) {
					subdata = UtilFun.subBytes(ucSendData, i * Max28Bytelength,
							length - i * Max28Bytelength);
					int sublength = 0;
					sublength = subdata.length <= 46 ? 64 : subdata.length;
					EthPackData = Packager.PackMutiple28byteData(ucDestAddress,
							ucAddress, subdata, sublength);
					for (int j = 0; j < subdata.length / 28; j++) {
						nResult = SpiControl.WriteSpi(subdata,
								EthPackData.length);

						try {
							LogUtil.WriteLog(subdata, false);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				} else {
					subdata = UtilFun.subBytes(ucSendData, i * Max28Bytelength,
							Max28Bytelength);
					EthPackData = Packager.PackMutiple28byteData(ucDestAddress,
							ucAddress, subdata, subdata.length);

					nResult = SpiControl.WriteSpi(subdata, EthPackData.length);

					try {
						LogUtil.WriteLog(subdata, false);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}
		return 1;
	}

	// ��������ͼ
	static public void SendConnChart(int tInterfaceId, int LEDID,
			ArrayList<CabinetViewObj> ArrCabinets) {
		byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE + 18];
		byte[] ucAddress = new byte[2];

		// �������ܱ�ַ
		IntfData tInterfData = InterfaceDB.GetRecordById(tInterfaceId, LEDID);

		// ��ַ
		ucAddress[0] = 0x11;
		ucAddress[1] = (byte) (0x00 + (tInterfaceId % 1000 - 1) * 0x10);
		// ��ʼ��ַ
		short sInitAddress = (short) ArrCabinets.get(0).getM_AddressId();
		ucSendData = Packager.PackScanCardIntelligentPara(
				tInterfData.macaddress, ucAddress, sInitAddress);
		// �������ܱ�֯��
		SendtoScanCard(tInterfData.macaddress, ucAddress, ucSendData);

		HashMap<String, CabinetInformation> CbtInfoHashMap = GetCbtInfoMap(LEDID);

		// ���ø����������ɨ�迨�Ď�������
		for (int i = 0; i < ArrCabinets.size(); i++) {

			CabinetViewObj tCabinetViewObj = ArrCabinets.get(i);

			CabinetInformation cbtInformation = CbtInfoHashMap
					.get(tCabinetViewObj.getStrTypeString());

			List<ScanCardAttachment> tListScanCards = cbtInformation.getListScancardAttachment();
			if (tListScanCards == null) {
				return;
			}

			short sCbtInitAddress = (short) tCabinetViewObj.getM_AddressId();
			for (int j = 0; j < tListScanCards.size(); j++) {
				// ����ÿ��ɨ�迨�Ď�������
				short tCbtInitAddress = (short) (sCbtInitAddress + j);
				ScanCardAttachment tScanCardAttachment = tListScanCards.get(j);
				// ÿ��ɨ�迨�Ď�������
				RECT RtLoadRegion = tScanCardAttachment.getRtRect();

				// ��������Interface��Rect
				RECT rectToSend = new RECT();
				rectToSend.left = (int) (tCabinetViewObj.m_leftOrg + RtLoadRegion.left);
				rectToSend.top = (int) (tCabinetViewObj.m_topOrg + RtLoadRegion.top);
				rectToSend.right = (int) (tCabinetViewObj.m_leftOrg + RtLoadRegion.right);
				rectToSend.bottom = (int) (tCabinetViewObj.m_topOrg + RtLoadRegion.bottom);

				ucSendData = Packager.PackScanCardLoadRegionData(
						tCbtInitAddress, rectToSend, (short) 0, (short) 0);
				SendtoScanCard(tInterfData.macaddress, ucAddress, ucSendData);
			}

		}

	}

	// ��������ͼ
	static public void SendConnChartFromDB(int tInterfaceId, int LEDID,
			ArrayList<CbtData> ArrCabinetsData) {
		byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE + 18];
		byte[] ucAddress = new byte[2];

		// �������ܱ�ַ
		IntfData tInterfData = InterfaceDB.GetRecordById(tInterfaceId, LEDID);

		// ��ַ
		ucAddress[0] = 0x11;
		ucAddress[1] = (byte) (0x00 + (tInterfaceId % 1000 - 1) * 0x10);
		// ��ʼ��ַ
		short sInitAddress = (short) ArrCabinetsData.get(0).address;
		ucSendData = Packager.PackScanCardIntelligentPara(
				tInterfData.macaddress, ucAddress, sInitAddress);
		// �������ܱ�֯��
		SendtoScanCard(tInterfData.macaddress, ucAddress, ucSendData);

		HashMap<String, CabinetInformation> CbtInfoHashMap = GetCbtInfoMap(LEDID);

		// ���ø����������ɨ�迨�Ď�������
		for (int i = 0; i < ArrCabinetsData.size(); i++) {

			CbtData tCabinetData = ArrCabinetsData.get(i);

			CabinetInformation cbtInformation = CbtInfoHashMap
					.get(tCabinetData.strModelType);

			List<ScanCardAttachment> tListScanCards = cbtInformation
					.getListScancardAttachment();

			short sCbtInitAddress = (short) tCabinetData.address;
			for (int j = 0; j < tListScanCards.size(); j++) {
				// ����ÿ��ɨ�迨�Ď�������
				short tCbtInitAddress = (short) (sCbtInitAddress + j);
				ScanCardAttachment tScanCardAttachment = tListScanCards.get(j);
				// ÿ��ɨ�迨�Ď�������
				RECT RtLoadRegion = tScanCardAttachment.getRtRect();

				// ��������Interface��Rect
				RECT rectToSend = new RECT();
				rectToSend.left = (int) (tCabinetData.offsetX
						+ RtLoadRegion.left - tInterfData.offsetX);
				rectToSend.top = (int) (tCabinetData.offsetY + RtLoadRegion.top - tInterfData.offsetY);
				rectToSend.right = (int) (tCabinetData.offsetX
						+ RtLoadRegion.right - tInterfData.offsetX);
				rectToSend.bottom = (int) (tCabinetData.offsetY
						+ RtLoadRegion.bottom - tInterfData.offsetY);

				ucSendData = Packager.PackScanCardLoadRegionData(
						tCbtInitAddress, rectToSend, (short) 0, (short) 0);
				SendtoScanCard(tInterfData.macaddress, ucAddress, ucSendData);
			}

		}

	}

	// ��������ͼ
	static public void SaveConnChart(int tInterfaceId, int LEDID) {

		byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE + 18];
		byte[] ucAddress = new byte[2];

		// �������ܱ�ַ
		IntfData tInterfData = InterfaceDB.GetRecordById(tInterfaceId, LEDID);

		// ��ַ
		ucAddress[0] = 0x11;
		ucAddress[1] = (byte) (0x00 + (tInterfaceId % 1000 - 1) * 0x10);
		// ��ʼ��ַ

		ucSendData = Packager.PackSaveScanCardPara(tInterfData.macaddress,
				ucAddress, (short) 0xff, 1, false);

		// ���ͱ�������ͼ
		SendtoScanCard(tInterfData.macaddress, ucAddress, ucSendData);

		try {

			Thread.sleep(100);

			byte[] bytesRcv = InterfaceComm.ReadBackFiFoRcvData(
					tInterfData.macaddress, tInterfaceId % 1000, false);

			LogUtil.WriteLog(bytesRcv, false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// �����������
	static public int SendCabinetParamDromDB(int tInterfaceId, int LEDID,
			ArrayList<CbtData> ArrCabinets) {

		// �������ܱ�ַ
		IntfData tInterfData = InterfaceDB.GetRecordById(tInterfaceId, LEDID);
		// ��ȡgamma��
		GammaData sGammaData = DataAccessGammaTable
				.LoadDisplayGammaTable(LEDID);

		// ��͵�ַ��ɨ�迨�ͺ�

		CbtData tCbt = ArrCabinets.get(0);
		CabinetInformation cbtInformation = GetCbtInfoFromCbtLib(
				tCbt.Id, LEDID);
		ScanCardAttachment sScanCardAttachment = cbtInformation
				.getListScancardAttachment().get(0);

		short nEmptyByte = (short) 64;
		short address = 0xff;

		byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE + 18];
		byte[] ucAddress = new byte[2];

		// ��ַ
		ucAddress[0] = 0x11;
		ucAddress[1] = (byte) (0x00 + (tInterfaceId % 1000 - 1) * 0x10);

		int nMax;
		
		if(CHIP_TYPE._MBI5153_E.ordinal() == sScanCardAttachment.getScancard().getnChipType())
		{
			nMax = 7; //���Ͱ�00,01,02,03,04,05,15
		}
		else if(CHIP_TYPE._MBI5155.ordinal() == sScanCardAttachment.getScancard().getnChipType())
		{
			nMax = 8; //���Ͱ�00,01,02,03,04,05,15,16
		}
		else
		{
			nMax = 6; //���Ͱ�00,01,02,03,04,05
		}

		/*if (_SP10Version == g_GlobalVariable.nVersionType)
		{
			nMax++;//��Ƶ����ռ�ձ����� ���Ͱ�0x17
		}*/
		
		int nResult;
		for (int i = 0; i < nMax; i++) {
			
			//int nPackID = ((n == nMax - 1) && (_SP10Version == g_GlobalVariable.nVersionType)) ? 0x17 : n;		
			ucSendData = Packager.PackScanCardData(address, i,
					sScanCardAttachment.getScancard(), nEmptyByte);

			if (ucSendData == null) {
				return -1;
			}
			for (int j = 0; j < ucSendData.length / 28; j++) {
				byte[] temp = new byte[28];
				System.arraycopy(ucSendData, j * 28, temp, 0, 28);
				nResult = SendtoScanCard(tInterfData.macaddress, ucAddress,
						temp);
				if (0 > nResult) {
					return nResult;
				}
			}
		}

		// SetNoSingleDisp
		ucSendData = Packager.PackNoSingleDisp(address, sScanCardAttachment
				.getScancard().getnNoSingleDisp());
		if (ucSendData == null) {
			return -1;
		}
		for (int i = 0; i < ucSendData.length / 28; i++) {
			byte[] temp = new byte[28];
			System.arraycopy(ucSendData, i * 28, temp, 0, 28);
			nResult = SendtoScanCard(tInterfData.macaddress, ucAddress, temp);
			if (0 > nResult) {
				return nResult;
			}
		}

		// SetOperationProcessing
		ucSendData = Packager.PackOperationProcessing(address,
				sScanCardAttachment.getScancard()
						.getnScanCardSectionRowNumber());
		if (ucSendData == null) {
			return -1;
		}
		// nResult = SendtoScanCard(tInterfData.macaddress,
		// ucAddress,ucSendData);
		for (int i = 0; i < ucSendData.length / 28; i++) {
			byte[] temp = new byte[28];
			System.arraycopy(ucSendData, i * 28, temp, 0, 28);
			nResult = SendtoScanCard(tInterfData.macaddress, ucAddress, temp);
			if (0 > nResult) {
				return nResult;
			}
		}

		// SetVideoProcessing
		ucSendData = Packager.PackVideoProcessing(address,
				sScanCardAttachment.getScancard());
		if (ucSendData == null) {
			return -1;
		}
		for (int i = 0; i < ucSendData.length / 28; i++) {
			byte[] temp = new byte[28];
			System.arraycopy(ucSendData, i * 28, temp, 0, 28);
			nResult = SendtoScanCard(tInterfData.macaddress, ucAddress, temp);
			if (0 > nResult) {
				return nResult;
			}
		}

		// SetCorrectProcessing
		ucSendData = Packager.PackCorrectProcessing(address,
				sScanCardAttachment.getScancard());
		if (ucSendData == null) {
			return -1;
		}

		for (int i = 0; i < ucSendData.length / 28; i++) {
			byte[] temp = new byte[28];
			System.arraycopy(ucSendData, i * 28, temp, 0, 28);
			nResult = SendtoScanCard(tInterfData.macaddress, ucAddress, temp);
			if (0 > nResult) {
				return nResult;
			}
		}

		// CorrectProcessingLookup
		ucSendData = Packager.PackCorrectProcessingLookup(address,
				sScanCardAttachment.getScancard());

		if (ucSendData == null) {
			return -1;
		}
		if (null != ucSendData) {
			for (int i = 0; i < ucSendData.length / 28; i++) {
				byte[] temp = new byte[28];
				System.arraycopy(ucSendData, i * 28, temp, 0, 28);
				nResult = SendtoScanCard(tInterfData.macaddress, ucAddress,
						temp);
				if (0 > nResult) {
					return nResult;
				}
			}
		}

		// SetScanCardLinkTable
		ucSendData = Packager.PackScanCardLinkTable(address,
				sScanCardAttachment.getScancard().getnDataLineTypeRange(),
				sScanCardAttachment.getScancard().getnDCBlineClkEn(),
				sScanCardAttachment.getSlinktable());
		if (ucSendData == null) {
			return -1;
		}

		for (int i = 0; i < ucSendData.length / 28; i++) {
			byte[] temp = new byte[28];
			System.arraycopy(ucSendData, i * 28, temp, 0, 28);
			nResult = SendtoScanCard(tInterfData.macaddress, ucAddress, temp);
			if (0 > nResult) {
				return nResult;
			}
		}

		// SetScanCardSectionLinkTable
		ucSendData = Packager.PackScanCardSectionLinkTable(address,
				sScanCardAttachment.getsSectionlinktable());
		if (ucSendData == null) {
			return -1;
		}

		for (int i = 0; i < ucSendData.length / 28; i++) {
			byte[] temp = new byte[28];
			System.arraycopy(ucSendData, i * 28, temp, 0, 28);
			nResult = SendtoScanCard(tInterfData.macaddress, ucAddress, temp);
			if (0 > nResult) {
				return nResult;
			}
		}

		sGammaData.setnVideowid((short) sScanCardAttachment.getScancard()
				.getnCustomGamam());

		switch (sGammaData.getnVideowid()) {
		case 10:
		case 12:
			// ����GAMMA��(�㲥��Ӧ��)
			ucSendData = Packager.PackGammaTable(address, sGammaData, 0);
			if (ucSendData == null) {
				return -1;
			}
			for (int i = 0; i < ucSendData.length / 28; i++) {
				byte[] temp = new byte[28];
				System.arraycopy(ucSendData, i * 28, temp, 0, 28);
				nResult = SendtoScanCard(tInterfData.macaddress, ucAddress,
						temp);
				if (0 > nResult) {
					return nResult;
				}
			}

			break;
		case 8:
		default:
			for (int m = 0; m < 3; m++) {
				// ����GAMMA��(�㲥��Ӧ��)
				ucSendData = Packager.PackGammaTable(address, sGammaData, m);
				if (ucSendData == null) {
					return -1;
				}
				for (int i = 0; i < ucSendData.length / 28; i++) {
					byte[] temp = new byte[28];
					System.arraycopy(ucSendData, i * 28, temp, 0, 28);
					nResult = SendtoScanCard(tInterfData.macaddress, ucAddress,
							temp);
					if (0 > nResult) {
						return nResult;
					}
				}
			}
			break;
		}

		// OpenCabinetLamps
		ucSendData = Packager.PackOpenCabinetLamps(address, sScanCardAttachment
				.getScancard().isbOpenCabinetLamp());
		if (ucSendData == null) {
			return -1;
		}
		nResult = SendtoScanCard(tInterfData.macaddress, ucAddress, ucSendData);
		if (0 > nResult) {
			return nResult;
		}

		// SetScanCardParam((short)0xFF);
		ucSendData = Packager.PackCalibrationEnable(address,
				sScanCardAttachment.getScancard().isbEmendBrightness(),
				sScanCardAttachment.getScancard().getnDotCorrectTye());
		// nResult = SpiControl.WriteSpi(ucSendData, nSendLength);
		if (ucSendData == null) {
			return -1;
		}
		nResult = SendtoScanCard(tInterfData.macaddress, ucAddress, ucSendData);
		if (0 > nResult) {
			return nResult;
		}

		if (!sScanCardAttachment.getScancard().isbDotOpenDetection()) {
			return 1;
		} else {
			// ����HUB����
			ucSendData = Packager.PackHUBPara(address,
					sScanCardAttachment.getScancard());
			if (ucSendData == null) {
				return -1;
			}
			for (int i = 0; i < ucSendData.length / 28; i++) {
				byte[] temp = new byte[28];
				System.arraycopy(ucSendData, i * 28, temp, 0, 28);
				nResult = SendtoScanCard(tInterfData.macaddress, ucAddress,
						temp);
				if (0 > nResult) {
					return nResult;
				}
			}

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			// ����HUB���߲��ұ�
			ucSendData = Packager.PackHUBLookup(address,
					sScanCardAttachment.getHublinktable());
			if (ucSendData == null) {
				return -1;
			}
			for (int i = 0; i < ucSendData.length / 28; i++) {
				byte[] temp = new byte[28];
				System.arraycopy(ucSendData, i * 28, temp, 0, 28);
				nResult = SendtoScanCard(tInterfData.macaddress, ucAddress,
						temp);
				if (0 > nResult) {
					return nResult;
				}
			}

		}

		return 1;

	}

	// �����������
	static public int SendCabinetParam(int tInterfaceId, int LEDID,
			ArrayList<CabinetViewObj> ArrCabinets) {

		// �������ܱ�ַ
		IntfData tInterfData = InterfaceDB.GetRecordById(tInterfaceId, LEDID);
		// ��ȡgamma��
		GammaData sGammaData = DataAccessGammaTable
				.LoadDisplayGammaTable(LEDID);

		// ��͵�ַ��ɨ�迨�ͺ�

		CabinetViewObj tCbtObj = ArrCabinets.get(0);
		CabinetInformation cbtInformation = GetCbtInfoFromCbtLib(
				tCbtObj.getmBasicViewID(), LEDID);
		ScanCardAttachment sScanCardAttachment = cbtInformation
				.getListScancardAttachment().get(0);

		short nEmptyByte = (short) 64;
		short address = 0xff;

		byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE + 18];
		byte[] ucAddress = new byte[2];

		// ��ַ
		ucAddress[0] = 0x11;
		ucAddress[1] = (byte) (0x00 + (tInterfaceId % 1000 - 1) * 0x10);


		int nMax;
		if(CHIP_TYPE._MBI5153_E.ordinal() == sScanCardAttachment.getScancard().getnChipType())
		{
			nMax = 7; //���Ͱ�00,01,02,03,04,05,15
		}
		else if(CHIP_TYPE._MBI5155.ordinal() == sScanCardAttachment.getScancard().getnChipType())
		{
			nMax = 8; //���Ͱ�00,01,02,03,04,05,15,16
		}
		else
		{
			nMax = 6; //���Ͱ�00,01,02,03,04,05
		}

		/*if (_SP10Version == g_GlobalVariable.nVersionType)
		{
			nMax++;//��Ƶ����ռ�ձ����� ���Ͱ�0x17
		}*/
		
		int nResult;
		for (int i = 0; i < nMax; i++) {
			
			//int nPackID = ((n == nMax - 1) && (_SP10Version == g_GlobalVariable.nVersionType)) ? 0x17 : n;				
			ucSendData = Packager.PackScanCardData(address, i,
					sScanCardAttachment.getScancard(), nEmptyByte);

			if (ucSendData == null) {
				return -1;
			}
			for (int j = 0; j < ucSendData.length / 28; j++) {
				byte[] temp = new byte[28];
				System.arraycopy(ucSendData, j * 28, temp, 0, 28);
				nResult = SendtoScanCard(tInterfData.macaddress, ucAddress,
						temp);
				if (0 > nResult) {
					return nResult;
				}
			}
		}

		// SetNoSingleDisp
		ucSendData = Packager.PackNoSingleDisp(address, sScanCardAttachment
				.getScancard().getnNoSingleDisp());
		if (ucSendData == null) {
			return -1;
		}
		for (int i = 0; i < ucSendData.length / 28; i++) {
			byte[] temp = new byte[28];
			System.arraycopy(ucSendData, i * 28, temp, 0, 28);
			nResult = SendtoScanCard(tInterfData.macaddress, ucAddress, temp);
			if (0 > nResult) {
				return nResult;
			}
		}

		// SetOperationProcessing
		ucSendData = Packager.PackOperationProcessing(address,
				sScanCardAttachment.getScancard()
						.getnScanCardSectionRowNumber());
		if (ucSendData == null) {
			return -1;
		}
		// nResult = SendtoScanCard(tInterfData.macaddress,
		// ucAddress,ucSendData);
		for (int i = 0; i < ucSendData.length / 28; i++) {
			byte[] temp = new byte[28];
			System.arraycopy(ucSendData, i * 28, temp, 0, 28);
			nResult = SendtoScanCard(tInterfData.macaddress, ucAddress, temp);
			if (0 > nResult) {
				return nResult;
			}
		}

		// SetVideoProcessing
		ucSendData = Packager.PackVideoProcessing(address,
				sScanCardAttachment.getScancard());
		if (ucSendData == null) {
			return -1;
		}
		for (int i = 0; i < ucSendData.length / 28; i++) {
			byte[] temp = new byte[28];
			System.arraycopy(ucSendData, i * 28, temp, 0, 28);
			nResult = SendtoScanCard(tInterfData.macaddress, ucAddress, temp);
			if (0 > nResult) {
				return nResult;
			}
		}

		// SetCorrectProcessing
		ucSendData = Packager.PackCorrectProcessing(address,
				sScanCardAttachment.getScancard());
		if (ucSendData == null) {
			return -1;
		}

		for (int i = 0; i < ucSendData.length / 28; i++) {
			byte[] temp = new byte[28];
			System.arraycopy(ucSendData, i * 28, temp, 0, 28);
			nResult = SendtoScanCard(tInterfData.macaddress, ucAddress, temp);
			if (0 > nResult) {
				return nResult;
			}
		}

		// CorrectProcessingLookup
		ucSendData = Packager.PackCorrectProcessingLookup(address,
				sScanCardAttachment.getScancard());

		if (ucSendData == null) {
			return -1;
		}
		if (null != ucSendData) {
			for (int i = 0; i < ucSendData.length / 28; i++) {
				byte[] temp = new byte[28];
				System.arraycopy(ucSendData, i * 28, temp, 0, 28);
				nResult = SendtoScanCard(tInterfData.macaddress, ucAddress,
						temp);
				if (0 > nResult) {
					return nResult;
				}
			}
		}

		// SetScanCardLinkTable
		ucSendData = Packager.PackScanCardLinkTable(address,
				sScanCardAttachment.getScancard().getnDataLineTypeRange(),
				sScanCardAttachment.getScancard().getnDCBlineClkEn(),
				sScanCardAttachment.getSlinktable());
		if (ucSendData == null) {
			return -1;
		}

		for (int i = 0; i < ucSendData.length / 28; i++) {
			byte[] temp = new byte[28];
			System.arraycopy(ucSendData, i * 28, temp, 0, 28);
			nResult = SendtoScanCard(tInterfData.macaddress, ucAddress, temp);
			if (0 > nResult) {
				return nResult;
			}
		}

		// SetScanCardSectionLinkTable
		ucSendData = Packager.PackScanCardSectionLinkTable(address,
				sScanCardAttachment.getsSectionlinktable());
		if (ucSendData == null) {
			return -1;
		}

		for (int i = 0; i < ucSendData.length / 28; i++) {
			byte[] temp = new byte[28];
			System.arraycopy(ucSendData, i * 28, temp, 0, 28);
			nResult = SendtoScanCard(tInterfData.macaddress, ucAddress, temp);
			if (0 > nResult) {
				return nResult;
			}
		}

		sGammaData.setnVideowid((short) sScanCardAttachment.getScancard()
				.getnCustomGamam());

		switch (sGammaData.getnVideowid()) {
		case 10:
		case 12:
			// ����GAMMA��(�㲥��Ӧ��)
			ucSendData = Packager.PackGammaTable(address, sGammaData, 0);
			if (ucSendData == null) {
				return -1;
			}
			for (int i = 0; i < ucSendData.length / 28; i++) {
				byte[] temp = new byte[28];
				System.arraycopy(ucSendData, i * 28, temp, 0, 28);
				nResult = SendtoScanCard(tInterfData.macaddress, ucAddress,
						temp);
				if (0 > nResult) {
					return nResult;
				}
			}

			break;
		case 8:
		default:
			for (int m = 0; m < 3; m++) {
				// ����GAMMA��(�㲥��Ӧ��)
				ucSendData = Packager.PackGammaTable(address, sGammaData, m);
				if (ucSendData == null) {
					return -1;
				}
				for (int i = 0; i < ucSendData.length / 28; i++) {
					byte[] temp = new byte[28];
					System.arraycopy(ucSendData, i * 28, temp, 0, 28);
					nResult = SendtoScanCard(tInterfData.macaddress, ucAddress,
							temp);
					if (0 > nResult) {
						return nResult;
					}
				}
			}
			break;
		}

		// OpenCabinetLamps
		ucSendData = Packager.PackOpenCabinetLamps(address, sScanCardAttachment
				.getScancard().isbOpenCabinetLamp());
		if (ucSendData == null) {
			return -1;
		}
		nResult = SendtoScanCard(tInterfData.macaddress, ucAddress, ucSendData);
		if (0 > nResult) {
			return nResult;
		}

		// SetScanCardParam((short)0xFF);
		ucSendData = Packager.PackCalibrationEnable(address,
				sScanCardAttachment.getScancard().isbEmendBrightness(),
				sScanCardAttachment.getScancard().getnDotCorrectTye());
		// nResult = SpiControl.WriteSpi(ucSendData, nSendLength);
		if (ucSendData == null) {
			return -1;
		}
		nResult = SendtoScanCard(tInterfData.macaddress, ucAddress, ucSendData);
		if (0 > nResult) {
			return nResult;
		}

		if (!sScanCardAttachment.getScancard().isbDotOpenDetection()) {
			return 1;
		} else {
			// ����HUB����
			ucSendData = Packager.PackHUBPara(address,
					sScanCardAttachment.getScancard());
			if (ucSendData == null) {
				return -1;
			}
			for (int i = 0; i < ucSendData.length / 28; i++) {
				byte[] temp = new byte[28];
				System.arraycopy(ucSendData, i * 28, temp, 0, 28);
				nResult = SendtoScanCard(tInterfData.macaddress, ucAddress,
						temp);
				if (0 > nResult) {
					return nResult;
				}
			}

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			// ����HUB���߲��ұ�
			ucSendData = Packager.PackHUBLookup(address,
					sScanCardAttachment.getHublinktable());
			if (ucSendData == null) {
				return -1;
			}
			for (int i = 0; i < ucSendData.length / 28; i++) {
				byte[] temp = new byte[28];
				System.arraycopy(ucSendData, i * 28, temp, 0, 28);
				nResult = SendtoScanCard(tInterfData.macaddress, ucAddress,
						temp);
				if (0 > nResult) {
					return nResult;
				}
			}

		}

		return 1;

	}

	// �����������
	static public int SaveCabinetParam(int tInterfaceId, int LEDID) {
		byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE + 18];
		byte[] ucAddress = new byte[2];

		// ��ַ
		ucAddress[0] = 0x11;
		ucAddress[1] = (byte) (0x00 + (tInterfaceId % 1000 - 1) * 0x10);

		IntfData tInterfData = InterfaceDB.GetRecordById(tInterfaceId, LEDID);

		ucSendData = Packager.PackSaveScanCardPara(tInterfData.macaddress,
				ucAddress, (short) 0xff, 1, false);

		int nResult = SendtoScanCard(tInterfData.macaddress, ucAddress,
				ucSendData);

		try {
			LogUtil.WriteLog(ucSendData, false);
		} catch (IOException e) {

			e.printStackTrace();
		}

		return nResult;

	}

}