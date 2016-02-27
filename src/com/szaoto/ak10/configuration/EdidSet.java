package com.szaoto.ak10.configuration;

import java.util.List;
import android.content.Context;
import com.szaoto.ak10.common.EDIDCfg;
import com.szaoto.ak10.dataaccess.DataAccessAcquisitionCardEdidCfg;
import com.szaoto.ak10.datacomm.ChanComm;
public class EdidSet {
	private String  resolution;
	private int frame;
	private List<EDIDCfg> m_ListEdidCfgs;
	static byte[] byteadd  = {   
		0x02,0x03,0x2D,(byte)0xF1,0x52,(byte)0x90,0x1F,0x01,0x02,0x03,0x07,0x16,0x04,0x13,0x14,0x05,
		0x12,0x11,0x06,0x15,0x20,0x21,0x22,0x23,0x09,0x1F,0x07,(byte)0x83,0x01,0x00,0x00,0x6D,
		0x03,0x0C,0x00,0x10,0x00,0x00,0x3C,0x20,0x00,0x60,0x03,0x02,0x01,0x02,0x3A,(byte)0x80,
		0x18,0x71,0x38,0x2D,0x40,0x58,0x2C,0x45,0x00,0x6D,0x55,0x21,0x00,0x00,0x1E,0x01,
		0x1D,(byte)0x80,0x18,0x71,0x1C,0x16,0x20,0x58,0x2C,0x25,0x00,0x6D,0x55,0x21,0x00,0x00,
		(byte)0x9E,0x01,0x1D,0x00,0x72,0x51,(byte)0xD0,0x1E,0x20,0x6E,0x28,0x55,0x00,0x6D,0x55,0x21,
		0x00,0x00,0x1E,(byte)0x8C,0x0A,(byte)0xD0,(byte)0x8A,0x20,(byte)0xE0,0x2D,0x10,0x10,0x3E,(byte)0x96,0x00,0x6D,
		0x55,0x21,0x00,0x00,0x18,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,(byte)0x86
	};
	
	Context context;
	
	public void AnalyseEDIDCfgFile(int Activitytype){
		DataAccessAcquisitionCardEdidCfg cfg =new DataAccessAcquisitionCardEdidCfg(Activitytype);
		m_ListEdidCfgs = cfg.DataAccessAcquisitionCardGetEdidcfgs();
	}
	public List<EDIDCfg> GetListEdidCfgs()
	{
		return m_ListEdidCfgs;
	}
	
	public EdidSet(String resolution,int frame)
	{
		this.resolution = resolution;
		this.frame = frame;	
	}
	public int  SetResolutionAndFrame(int PortNumber, byte[] MacAddress,int Activitytype)
	{
		EDIDCfg sEDIDcfg = new EDIDCfg() ;
		sEDIDcfg.m_iHBlanking = 80;//ˮƽ��û����(Horizontal Blanking)
		sEDIDcfg.m_iHSyncOffset = 30;//ˮƽͬ��ƫ��H.Sync Offset
		sEDIDcfg.m_iHSyncPulseWidth = 30;//ˮƽͬ��������H.Sync Pulse Width
		sEDIDcfg.m_iVBlanking = 10;//��ֱ��û����(Vertical Blanking)
		sEDIDcfg.m_iVSyncOffset = 3;//��ֱͬ��ƫ��V.Sync Offset
		sEDIDcfg.m_iVSyncPulseWidth = 4;//��ֱͬ��������V.Sync Pulse Width
		
		sEDIDcfg.m_sResolution = resolution;
		sEDIDcfg.m_iFrame = frame;
		
		DataAccessAcquisitionCardEdidCfg cfg =new DataAccessAcquisitionCardEdidCfg(Activitytype);
		List<EDIDCfg> edids = cfg.DataAccessAcquisitionCardGetEdidcfgs();

		for (EDIDCfg cf : edids) {
			if(cf.m_iFrame == frame && cf.m_sResolution.equals(resolution))
			{
				sEDIDcfg.m_iFrame = cf.m_iFrame;
				sEDIDcfg.m_sResolution = cf.m_sResolution;
				sEDIDcfg.m_iHBlanking = cf.m_iHBlanking;
				sEDIDcfg.m_iHSyncOffset = cf.m_iHSyncOffset;
				sEDIDcfg.m_iHSyncPulseWidth = cf.m_iHSyncPulseWidth;
				sEDIDcfg.m_iSupportValue = cf.m_iSupportValue;
				sEDIDcfg.m_iVBlanking = cf.m_iVBlanking;
				sEDIDcfg.m_iVSyncOffset = cf.m_iVSyncOffset;
				sEDIDcfg.m_iVSyncPulseWidth = cf.m_iVSyncPulseWidth;
				break;
			}
			//return -1;
		}
		
		byte edidinfo[] = new byte[256];
	
		boolean bIs4K =false;
		
		String strResolution = sEDIDcfg.m_sResolution;
		int nIndex = strResolution.indexOf('X');
		String strHActive = strResolution.substring(0, nIndex);	
		int HValue = Integer.parseInt(strHActive,10); 

		
		if (HValue>=3840) {
			bIs4K = true;
		}
		
		GetEDIDData(sEDIDcfg, edidinfo,bIs4K);
		
	
		long test = ChanComm.SetATLVCAK10EDIDInfo(edidinfo, PortNumber, MacAddress);
		if(test>0)//dvi1
		{
			return 1;
		}
		else 
			return -1;
		
		
	}
	public int GetEDIDData(EDIDCfg sEdidcfg, byte[] ucEdidData,boolean bIs4K)
	{
		int iHActive;//ˮƽ������(Horizontal Active)
		int iVActive;//��ֱ��������(Vertical Active)
		int iFrame = sEdidcfg.m_iFrame;//֡��
		double iPixels;//���ص㣨HA+HB��*��VA+VB��*֡��,��λHz
	
//		int iHBlanking = 560;//ˮƽ��û����(Horizontal Blanking)
//		int iHSyncOffset = 176;//ˮƽͬ��ƫ��H.Sync Offset
//		int iHSyncPulseWith = 88;//ˮƽͬ��������H.Sync Pulse Width
//		int iVBlanking = 90;//��ֱ��û����(Vertical Blanking)
//		int iVSyncOffset = 8;//��ֱͬ��ƫ��V.Sync Offset
//		int iVSyncPulseWith = 10;//��ֱͬ��������V.Sync Pulse Width			
		
		int iHBlanking = sEdidcfg.m_iHBlanking;//ˮƽ��û����(Horizontal Blanking)
		int iHSyncOffset = sEdidcfg.m_iHSyncOffset;//ˮƽͬ��ƫ��H.Sync Offset
		int iHSyncPulseWith = sEdidcfg.m_iHSyncPulseWidth;//ˮƽͬ��������H.Sync Pulse Width
		int iVBlanking = sEdidcfg.m_iVBlanking;//��ֱ��û����(Vertical Blanking)
		int iVSyncOffset = sEdidcfg.m_iVSyncOffset;//��ֱͬ��ƫ��V.Sync Offset
		int iVSyncPulseWith = sEdidcfg.m_iVSyncPulseWidth;//��ֱͬ��������V.Sync Pulse Width
		
		String strResolution = sEdidcfg.m_sResolution;
		int nIndex = strResolution.indexOf('X');
		String strHActive = strResolution.substring(0, nIndex);
		String strVActive = strResolution.substring(nIndex+1);
		iHActive = Integer.parseInt(strHActive,10); 
		iVActive = Integer.parseInt(strVActive,10);
		iPixels = (iHActive + iHBlanking)*(iVActive + iVBlanking)*iFrame;

		//1��	EDID��ʼ�ı�ʾ��8bytes��
		ucEdidData[0x00] = 0x00;
		ucEdidData[0x01] = (byte) 0xFF;
		ucEdidData[0x02] = (byte) 0xFF;
		ucEdidData[0x03] = (byte) 0xFF;
		ucEdidData[0x04] = (byte) 0xFF;
		ucEdidData[0x05] = (byte) 0xFF;
		ucEdidData[0x06] = (byte) 0xFF;
		ucEdidData[0x07] = 0x00;

		//2��	���쳧������(Vendor ID:2bytes) :ATE
		ucEdidData[0x08] = 0x06;
		ucEdidData[0x09] = (byte) 0x85;
	
		//3��	��Ʒ���루Product:2bytes)��0803
		ucEdidData[0x0A] = 0x03;
		ucEdidData[0x0B] = 0x08;
	
		//4��	��Ʒ���кţ�Serial:4bytes�����ݶ�268040������ָ����
		//	int iFrame = sEdidcfg.m_iFrame;//֡��
		//iPixels = (iHActive + iHBlanking)*(iVActive + iVBlanking)*iFrame;
		//ucEdidData[0x0C] = (byte) iFrame;//
		ucEdidData[0x0C] = 0x08;
		ucEdidData[0x0D] = 0x17;
		ucEdidData[0x0E] = 0x04;
		ucEdidData[0x0F] = 0x00;

		//5��	�����ܣ�Mfg week:1byte��
		ucEdidData[0x10] = 0x10;
	
		//6��	������ݣ�Mfg year:1byte��
		ucEdidData[0x11] = 0x0C;
	
		//7��	EDID�汾�ţ�Version.Revision: 2bytes��V 1.3
		ucEdidData[0x12] = 0x01;
		ucEdidData[0x13] = 0x03;
	
		//8��	��Ƶ�ź����������1byte�� �����źŵ�ƽ
		ucEdidData[0x14] = (byte) 0x81;
	
		//9��	ͼ���С������2bytes�� Max.H Image Size:30cm;  Max.V Image Size :23cm
		ucEdidData[0x15] = 0x1E;
		ucEdidData[0x16] = 0x17;

		//10��	��ʾ��������(Gammaֵ) ��1byte�� 2.7
		ucEdidData[0x17] = (byte) 0xAA;
	
		//11��	DPMS����֧�֣�1byte��
		ucEdidData[0x18] = (byte) 0xEA;
	
		//12��	��ʾ����ɫ������color Characteristics�� 10bytes��C1 E5 A3 57 4E   9C 23 1D 50 54
		ucEdidData[0x19] = (byte) 0xC1;
		ucEdidData[0x1A] = (byte) 0xE5;
		ucEdidData[0x1B] = (byte) 0xA3;
		ucEdidData[0x1C] = 0x57;
		ucEdidData[0x1D] = 0x4E;
		ucEdidData[0x1E] = (byte) 0x9C;
		ucEdidData[0x1F] = 0x23;
		ucEdidData[0x20] = 0x1D;
		ucEdidData[0x21] = 0x50;
		ucEdidData[0x22] = 0x54;

		//13��	�ڽ�ʱ��Established Timing:3bytes�� (�ṩĬ�ϵ�ʱ��Ҫ��֧��)
		// 	ucEdidData[0x23] = 0xFF;
		// 	ucEdidData[0x24] = 0xFF;
		// 	ucEdidData[0x25] = 0x80;
		ucEdidData[0x23] = 0x00;
		ucEdidData[0x24] = 0x00;
		ucEdidData[0x25] = 0x00;

		//14��	��׼ʱ���ʶ��16bytes����ʹ�ã���Ϊ��01��0x26--0x35
		for(int i=0 ;i<16;i++)
		{
			ucEdidData[i+0x26] = 0x01;
		}
		//	memset(ucEdidData+0x26, 0x01, 16);

		//15��	��ϸ��ʱ����������72bytes��
		// 	//block1
		// 	double lPixel = 141.570;// MHz  (374D,�ȴ洢��λ�ֽ�)
		byte[] ucPixeldata = new byte[10];
		byte[] ucHActive = new byte[10];
		byte[] ucHBlanking = new byte[10];
		byte[] ucVActive = new byte[10];
		byte[] ucVBlanking = new byte[10];
	
		byte[] ucHSyncOffset = new byte[10];
		byte[] ucHSyncPulseWith = new byte[10];
		byte[] ucVSyncOffset = new byte[10];
		byte[] ucVSyncPulseWith = new byte[10];
	
		ToModeData(ucPixeldata,(int)(iPixels/10000), 16);//D473
		ToModeData(ucHActive,iHActive, 16);
		ToModeData(ucHBlanking,iHBlanking, 16);
		ToModeData(ucVActive,iVActive, 16);
		ToModeData(ucVBlanking,iVBlanking, 16);
	
		ToModeData(ucHSyncOffset,iHSyncOffset, 16);
		ToModeData(ucHSyncPulseWith,iHSyncPulseWith, 16);
		ToModeData(ucVSyncOffset,iVSyncOffset, 16);
		ToModeData(ucVSyncPulseWith,iVSyncPulseWith, 16);

		//�洢��Ƶ,�ȴ洢��λ�ֽ�(374D)
		ucEdidData[0x36] = (byte) (ucPixeldata[0] + ucPixeldata[1]*16); //4D
		ucEdidData[0x37] = (byte) (ucPixeldata[2] + ucPixeldata[3]*16); //37
	
		ucEdidData[0x38] = (byte) (ucHActive[0] + ucHActive[1]*16);//ˮƽ������(Horizontal Active)�����8λ
		ucEdidData[0x39] = (byte) (ucHBlanking[0] + ucHBlanking[1]*16);//ˮƽ��û����(Horizontal Blanking)�����8λ
		ucEdidData[0x3A] = (byte) (ucHBlanking[2] + ucHActive[2]*16);//ˮƽ����ˮƽ��û(H.Active/H.Blanking),����λ:ˮƽ���Ե�ĸ���λ;����λ:ˮƽ��û����λ
	
		ucEdidData[0x3B] = (byte) (ucVActive[0] + ucVActive[1]*16);//��ֱ��������(Vertical Active),���8λ
		ucEdidData[0x3C] = (byte) (ucVBlanking[0] + ucVBlanking[1]*16);//��ֱ��û����(Vertical Blanking),���8λ
		ucEdidData[0x3D] = (byte) (ucVBlanking[2] + ucVActive[2]*16);//��ֱ���Դ�ֱ��û(V.Active/V.Blanking),����λ:��ֱ���Ե�ĸ���λ;����λ:��ֱ��û����λ
	
		ucEdidData[0x3E] = (byte) (ucHSyncOffset[0] + ucHSyncOffset[1]*16);//ˮƽͬ��ƫ��(H.Sync Offset),����û��ʼ���8λ
		ucEdidData[0x3F] = (byte) (ucHSyncPulseWith[0] + ucHSyncPulseWith[1]*16);//ˮƽͬ��������H.Sync Pulse Width�����8λ
		ucEdidData[0x40] = (byte) (ucVSyncPulseWith[0] + ucVSyncOffset[0]*16);//��ֱͬ��ƫ��/��ֱͬ�������ȣ�V.Sync Offset/V.Sync Pulse Width��,����λ������ֱͬ��ƫ�Ƶ���λ,����λ������ֱͬ�������ȵ���λ
	
		ucEdidData[0x41] = 0x00;//δ��
		ucEdidData[0x42] = 0x33;//H.Image Size		Ĭ��307����0001�� 0011 0011
		ucEdidData[0x43] = (byte) 0xE6;//V.Image Size		Ĭ��230����0000�� 1110 0110
		ucEdidData[0x44] = 0x10;//H / V Image Size	0001 0000
		ucEdidData[0x45] = 0x00;//H.Border
		ucEdidData[0x46] = 0x00;//V.Border
		ucEdidData[0x47] = 0x18;//Flags

		//block2 �洢ASCII���ʾ����ţ�����Ϊ�� BZ  268040�� 00 00 00 FF 00 20 42 5A  20 20 32 36 38 30 34 30 0A 20 
		ucEdidData[0x48] = 0x00;//Flag
		ucEdidData[0x49] = 0x00;
		ucEdidData[0x4A] = 0x00;//Flag
		ucEdidData[0x4B] = (byte) 0xFF;//Data Type Flag
		ucEdidData[0x4C] = 0x00;//Flag
		//��������13bytes  0x4D--0x58
		String strSN = " BZ  268040";
		int i;
		int iBeginAddr = 0x4D;
		int iLen = strSN.length();//////size();
		int iDateLen = iLen > 12 ? 12 : iLen;
		for (i=0; i<iDateLen; i++)
		{
			//ucEdidData[iBeginAddr+i] = strSN.operator [](i);
			ucEdidData[iBeginAddr+i] = (byte) strSN.charAt(i);
		}
		ucEdidData[iBeginAddr+iDateLen] = 0x0A;//��0x0A����
		//	memset(ucEdidData+iBeginAddr+iDateLen, 0x0A, 1);
		if (iDateLen < 12)
		{
		//	memset(ucEdidData+iBeginAddr+iDateLen+1, 0x20, 12-iDateLen);//�����λ��0x20(����д12��ASCII��)
			for(int j=0 ; j<12-iDateLen ; j++)//�����λ��0x20(����д12��ASCII��)
			{
				ucEdidData[iBeginAddr+iDateLen+1+j] = 0x20;
			}
		}

		//block3 �洢ASCII���ʾ�Ļ���������Ϣ(Model No.)������ΪLED�����أ����硰LED1922X1200��
		//00 00 00 FC 00 4C  45 44 31 39 32 30 58 31 32 30 30 0A
		ucEdidData[0x5A] = 0x00;//Flag
		ucEdidData[0x5B] = 0x00;
		ucEdidData[0x5C] = 0x00;//Flag
		ucEdidData[0x5D] = (byte) 0xFC;//Data Type Flag
		ucEdidData[0x5E] = 0x00;//Flag
		//��������13bytes  0x5F--0x6B
		
		/*CString cstrLedName;
		cstrLedName.Format("LED%dX%d", iHActive, iVActive);
		string strName(cstrLedName.GetBuffer());*/
		String strName;
		strName = String.format("LED%dX%d", iHActive,iVActive);
		iBeginAddr = 0x5F;
		iLen = strName.length();
		iDateLen = iLen > 12 ? 12 : iLen;
		for (i=0; i<iDateLen; i++)
		{
			ucEdidData[iBeginAddr+i] = (byte) strName.charAt(i);
		}
		//memset(ucEdidData+iBeginAddr+iDateLen, 0x0A, 1);//��0x0A����
		ucEdidData[iBeginAddr+iDateLen] =0x0A;
		if (iDateLen < 12)
		{
		//	memset(ucEdidData+iBeginAddr+iDateLen+1, 0x20, 12-iDateLen);//�����λ��0x20(����д12��ASCII��)
			for(int k=0 ;k<12-iDateLen ; k++)
			{
				ucEdidData[iBeginAddr+iDateLen+1+k] = 0x20;
			}
		}

		//block4 00 00 00 FD 00  00 FF 00 FF FA 00 0A 20 20 20 20 20 20
		ucEdidData[0x6C] = 0x00;//Flag
		ucEdidData[0x6D] = 0x00;
		ucEdidData[0x6E] = 0x00;//Flag
		ucEdidData[0x6F] = (byte) 0xFD;//Data Type Flag
		ucEdidData[0x70] = 0x00;//Flag
		//��������13bytes  0x71--0x7D
		ucEdidData[0x71] = 0x00;//��Ƶ����Min.V Rate  0
		ucEdidData[0x72] = (byte) 0xFF;//��Ƶ����Max.V Rate  255
		ucEdidData[0x73] = 0x00;//��Ƶ����Min.H Rate  0
		ucEdidData[0x74] = (byte) 0xFF;//��Ƶ����Max.H Rate  255
		ucEdidData[0x75] = (byte) 0xFA;//����(��Ƶ) Max. Supported Pixel Clock  2500
		ucEdidData[0x76] = 0x00;
		ucEdidData[0x77] = 0x0A;
		ucEdidData[0x78] = 0x20;
		ucEdidData[0x79] = 0x20;
		ucEdidData[0x7A] = 0x20;
		ucEdidData[0x7B] = 0x20;
		ucEdidData[0x7C] = 0x20;
		ucEdidData[0x7D] = 0x20;
		ucEdidData[0x7E] = 0x01;
		
		//16��	��չ��־��1byte��
//		bIs4K = true;//��֤��׼EDID���˿��ش�
//		if (bIs4K) 
//		{
//		 ucEdidData[0x7E] = 0x01;
//		}
//		else 
//		{
//			ucEdidData[0x7E] = 0x00;
//		}
		
		//17��	У��ͣ�Checksum��1byte��
		int sum = 0;
		for (i = 0x00; i < 0x7F; i++)
		{
			sum += ucEdidData[i];
		}
		ucEdidData[0x7F] = (byte) (256 - (sum % 256));
		
//		if(bIs4K)
//		{
//			for(int s = 0; s<128 ;s++)
//			{
//				ucEdidData[128+s] = byteadd[s];
//			}	
//		}
//		else 
//		{
//			for(int s = 0; s<128 ;s++)
//			{
//				ucEdidData[128+s] = 0x00;
//			}
//		}

		//��չEdid��ʼ
		//OLD
		/*
		//18��	Tag:2
		ucEdidData[0x80] = 0x02;
		//19��  Revision:3
		ucEdidData[0x81] = 0x03;
		//20��  Video data ����/Extension Fields Presence
		ucEdidData[0x82] = 0x0F;
		//21��  Monitor Support/Detailed Timing
		ucEdidData[0x83] = 0x71;
		//22��  Video Data
		ucEdidData[0x84] = 0x43;
		ucEdidData[0x85] = (byte)0x90;
		ucEdidData[0x86] = 0x01;
		ucEdidData[0x87] = 0x04;
		ucEdidData[0x88] = 0x66;
		ucEdidData[0x89] = 0x03;
		ucEdidData[0x8A] = 0x0C;
		ucEdidData[0x8B] = 0x00;
		// �����ַ
		ucEdidData[0x8C] = 0x10;
		ucEdidData[0x8D] = 0x00;
		//Misc
		ucEdidData[0x8E] = 0x00;
		//23��  ��ϸ��ʱ�������� ���ƴ�0x36��0x47��������0x8F��0xA0
		ucEdidData[0x8F] = ucEdidData[0x36]; */

		//NEW 	02 03 14 01 48 90 1F 04 13 03 02 01 5F 66 03 0C
		ucEdidData[0x80] = 0x02;
		//19��  Revision:3
		ucEdidData[0x81] = 0x03;
		//20��  Video data ����/Extension Fields Presence
		ucEdidData[0x82] = 0x14;
		//21��  Monitor Support/Detailed Timing
		ucEdidData[0x83] = 0x01;
		//22��  Video Data
		ucEdidData[0x84] = 0x48;
		ucEdidData[0x85] = (byte)0x90;
		ucEdidData[0x86] = 0x1F;
		ucEdidData[0x87] = 0x04;
		ucEdidData[0x88] = 0x13;
		ucEdidData[0x89] = 0x03;
		ucEdidData[0x8A] = 0x02;
		ucEdidData[0x8B] = 0x01;
		// �����ַ
		ucEdidData[0x8C] = 0x5F;
		ucEdidData[0x8D] = 0x66;
		//Misc
		ucEdidData[0x8E] = 0x03;
		//23��  ��ϸ��ʱ�������� ���ƴ�0x36��0x47��������0x8F��0xA0
		//ucEdidData[0x8F] = ucEdidData[0x36];
		ucEdidData[0x8F] = 0x0C;
		
		/* OLD
		ucEdidData[0x90] = ucEdidData[0x37];
		ucEdidData[0x91] = ucEdidData[0x38];
		ucEdidData[0x92] = ucEdidData[0x39];
		ucEdidData[0x93] = ucEdidData[0x3A];
		ucEdidData[0x94] = ucEdidData[0x3B];
		ucEdidData[0x95] = ucEdidData[0x3C];
		ucEdidData[0x96] = ucEdidData[0x3D];
		ucEdidData[0x97] = ucEdidData[0x3E];
		ucEdidData[0x98] = ucEdidData[0x3F];
		ucEdidData[0x99] = ucEdidData[0x40];
		ucEdidData[0x9A] = ucEdidData[0x41];
		ucEdidData[0x9B] = ucEdidData[0x42];
		ucEdidData[0x9C] = ucEdidData[0x43];
		ucEdidData[0x9D] = ucEdidData[0x44];
		ucEdidData[0x9E] = ucEdidData[0x45];
		ucEdidData[0x9F] = ucEdidData[0x46];
		ucEdidData[0xA0] = ucEdidData[0x47];
		//����Detailed Timing data
		ucEdidData[0xA1] = 0x00;
		ucEdidData[0xA2] = 0x00;
		ucEdidData[0xA3] = 0x00;
		ucEdidData[0xA4] = 0x00;
		ucEdidData[0xA5] = 0x00;
		*/
		//NEW	00 10 00 	00 9A 29 	A0 D0 51 	84 22 30	 50 98 06 00
		ucEdidData[0x90] = 0x00;
		ucEdidData[0x91] = 0x10;
		ucEdidData[0x92] = 0x00;	
		ucEdidData[0x93] = 0x00;
		ucEdidData[0x94] = (byte)0x9A;
		ucEdidData[0x95] = 0x29;	
		ucEdidData[0x96] = (byte)0xA0;
		ucEdidData[0x97] = (byte)0xD0;
		ucEdidData[0x98] = 0x51;
		ucEdidData[0x99] = (byte)0x84;
		ucEdidData[0x9A] = 0x22;
		ucEdidData[0x9B] = 0x30;
		ucEdidData[0x9C] = 0x50;
		ucEdidData[0x9D] = (byte)0x98;
		ucEdidData[0x9E] = 0x06;
		ucEdidData[0x9F] = 0x00;
		ucEdidData[0xA0] = 00;	
		
		//����Detailed Timing data
		ucEdidData[0xA1] = 0x00;
		ucEdidData[0xA2] = 0x00;
		ucEdidData[0xA3] = 0x00;
		ucEdidData[0xA4] = 0x00;
		ucEdidData[0xA5] = 0x1E;
		ucEdidData[0xA6] = 0x00;
		ucEdidData[0xA7] = 0x00;
		ucEdidData[0xA8] = 0x00;
		ucEdidData[0xA9] = 0x00;
		ucEdidData[0xAA] = 0x00;
		ucEdidData[0xAB] = 0x00;
		ucEdidData[0xAC] = 0x00;
		ucEdidData[0xAD] = 0x00;
		ucEdidData[0xAE] = 0x00;
		ucEdidData[0xAF] = 0x00;

		ucEdidData[0xB0] = 0x00;
		ucEdidData[0xB1] = 0x00;
		ucEdidData[0xB2] = 0x00;
		ucEdidData[0xB3] = 0x00;
		ucEdidData[0xB4] = 0x00;
		ucEdidData[0xB5] = 0x00;
		ucEdidData[0xB6] = 0x00;
		ucEdidData[0xB7] = 0x00;
		ucEdidData[0xB8] = 0x00;
		ucEdidData[0xB9] = 0x00;
		ucEdidData[0xBA] = 0x00;
		ucEdidData[0xBB] = 0x00;
		ucEdidData[0xBC] = 0x00;
		ucEdidData[0xBD] = 0x00;
		ucEdidData[0xBE] = 0x00;
		ucEdidData[0xBF] = 0x00;

		ucEdidData[0xC0] = 0x00;
		ucEdidData[0xC1] = 0x00;
		ucEdidData[0xC2] = 0x00;
		ucEdidData[0xC3] = 0x00;
		ucEdidData[0xC4] = 0x00;
		ucEdidData[0xC5] = 0x00;
		ucEdidData[0xC6] = 0x00;
		ucEdidData[0xC7] = 0x00;
		ucEdidData[0xC8] = 0x00;
		ucEdidData[0xC9] = 0x00;
		ucEdidData[0xCA] = 0x00;
		ucEdidData[0xCB] = 0x00;
		ucEdidData[0xCC] = 0x00;
		ucEdidData[0xCD] = 0x00;
		ucEdidData[0xCE] = 0x00;
		ucEdidData[0xCF] = 0x00;

		ucEdidData[0xD0] = 0x00;
		ucEdidData[0xD1] = 0x00;
		ucEdidData[0xD2] = 0x00;
		ucEdidData[0xD3] = 0x00;
		ucEdidData[0xD4] = 0x00;
		ucEdidData[0xD5] = 0x00;
		ucEdidData[0xD6] = 0x00;
		ucEdidData[0xD7] = 0x00;
		ucEdidData[0xD8] = 0x00;
		ucEdidData[0xD9] = 0x00;
		ucEdidData[0xDA] = 0x00;
		ucEdidData[0xDB] = 0x00;
		ucEdidData[0xDC] = 0x00;
		ucEdidData[0xDD] = 0x00;
		ucEdidData[0xDE] = 0x00;
		ucEdidData[0xDF] = 0x00;

		ucEdidData[0xE0] = 0x00;
		ucEdidData[0xE1] = 0x00;
		ucEdidData[0xE2] = 0x00;
		ucEdidData[0xE3] = 0x00;
		ucEdidData[0xE4] = 0x00;
		ucEdidData[0xE5] = 0x00;
		ucEdidData[0xE6] = 0x00;
		ucEdidData[0xE7] = 0x00;
		ucEdidData[0xE8] = 0x00;
		ucEdidData[0xE9] = 0x00;
		ucEdidData[0xEA] = 0x00;
		ucEdidData[0xEB] = 0x00;
		ucEdidData[0xEC] = 0x00;
		ucEdidData[0xED] = 0x00;
		ucEdidData[0xEE] = 0x00;
		ucEdidData[0xEF] = 0x00;

		ucEdidData[0xF0] = 0x00;
		ucEdidData[0xF1] = 0x00;
		ucEdidData[0xF2] = 0x00;
		ucEdidData[0xF3] = 0x00;
		ucEdidData[0xF4] = 0x00;
		ucEdidData[0xF5] = 0x00;
		ucEdidData[0xF6] = 0x00;
		ucEdidData[0xF7] = 0x00;
		ucEdidData[0xF8] = 0x00;
		ucEdidData[0xF9] = 0x00;
		ucEdidData[0xFA] = 0x00;
		ucEdidData[0xFB] = 0x00;
		ucEdidData[0xFC] = 0x00;
		ucEdidData[0xFD] = 0x00;
		ucEdidData[0xFE] = 0x00;		
		
		//24��  Unknown PartУ��ͣ�Checksum��1byte��
		sum = 0;
		for (i = 0x80; i < 0xFF; i++)
		{
			sum += ucEdidData[i];
		}
		ucEdidData[0xFF] = (byte)(256 - (sum % 256));

		return 1;
	}
	
	
	//10����ת����iMode����,output[i]�����洢��λ�õ�iMode���Ƶ�ֵ
	//���磺ToModeData(data,14157, 16);	14157��16����Ϊ374D
	//��data[3]=0x03,data[2]=0x07,data[1]=0x04,data[0]=0x0D,
	void ToModeData(byte[] output, int input, int iMode)
	{
		int i,t;
		for(i=0,t=input;t!=0;i++)
		{
			output[i]=(byte) (t%iMode);
			t=t/iMode;
		}
	}

	void ToModeData1(byte[] output, int input, int iMode)
	{
		for (int i = 0 ; i < iMode; i ++)
		{
			output[i] = (byte) (input >> (8 * i));
		}
	}
}
