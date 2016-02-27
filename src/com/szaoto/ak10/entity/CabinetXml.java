package com.szaoto.ak10.entity;

import com.szaoto.ak10.common.RECT;

/**
 * ����  Cabinet 
 * ����  zhangsj
 * ��Ҫ���� 	   ����ṹ ����"Cabinet.cbt"�ļ�����
 *  	����			����			�ͺ�			��ַ	����
 *
 */
public class CabinetXml {
/*
 * <?xml version="1.0" encoding="gb2312" standalone="yes" ?>
<Cabinets>                          //���弯��
    <Cabinet>                       //����
        <ID>85</ID>                 //����ID
        <SeriesID>80</SeriesID>     //����ϵ��ID
        <Name>FS10J</Name>          //������������
        <Photo />                   //����ͼƬ
        <SCACount>2</SCACount>      //��ʾ�����庬�м�ؿ�����2 
        <inlinemode>1</inlinemode>  //���߷�ʽ
        <ScanCardParaSynchro>1</ScanCardParaSynchro>  //ɨ�迨����ͬ��
        <MonitorParaSynchro>1</MonitorParaSynchro>    //��ز���ͬ��
        <ScanCardAttachments>                         //ɨ�迨����
            <ScanCardAttachment id="1" address="1">  
                <ScanCard ONE_SCAN_CARD_WIDTH="80" ONE_SCAN_CARD_HEIGHT="80" mod_width="16" mod_height="16" mod_section_number="3" mod_hor_number="5" mod_ver_number="5" SCAN_CARD_SECTION_NUM="10" SCAN_CARD_SECTION_ROW_NUM="8" SCAN_COLOR_DEPTH="16" GRAY_LEVEL="36" origin_color_bit="8" SCAN_MODE="2" DOT_CORRECTION_EN="0" SCAN_GCLK_FREQUENCY="16.7" ZONE_DCLK_NUM="4615" duty_cycle_low_value="4" PWM_SCAN_GCLK_FREQUENCY="18.8" pwm_duty_cycle_low_value="4" CLR_CLK_NUM="4246" refresh_rate="1200" refresh_rate_min="141" refresh_rate_max="1285" config_ic_time="1024" dat_freq_num="0" OE_DELAY_VALUE="2" SYN_REFRESH_EN="1" VIRTUAL_DISP_EN="0" FREQ_DIVISION_COEF="9" PWM_FREQ_DIVISION_COEF="8" DATA_OUTPUT_REVERSE="1" SCAN_OUTPUT_REVERSE="0" DCB_LINE_CLK_EN="0" NO_SIGNAL_DISP="0" DATA_INPUT_DIR="1" ROW_DECODE_MODE="3" DATA_LINE_TYPE_RANGE="0" DATA_LINE_TYPE="20" DATA_LINE_CTRL="0" FIELD_NUM="136" HALF_FIELD_NUM="9" FULL_FIELD_NUM="127" start_field="512" end_field="64" DATA_POLARITY="0" OE_POLARITY="0" EMPTY_INSERT_EN="0" INSERT_MODE="0" EMPTY_DOT_NUM="1" REAL_DOT_NUM="1" dual_out_put="0" virTual_array="0" chip_type="2" ref_doule_value="8" zhe_rdwr_mode="0" screen_type="2" dot_correct_tye="2" test_start="0" brightness_efficent="0.932" min_oe_width="8" dot_open_detection="0" pwm_output_mode="1" multi_refresh_under_static_scan="0" ONE_SCAN_CARD_WIDTH_REAL="80" ONE_SCAN_CARD_HEIGHT_REAL="80" />
                <ScanCardLinkup  //ɨ�迨����
                <HUBLinkup       //hub����
                	Len="128" 
                <MonitorCard     //��ؿ�
                	THBoard="0" MultiFuncBoard="0" PowerBoard="0" DotDectorBoard="0" />
                <MonitorItem     //�����
                	Temperature="0" Humidity="0" Smoke="0" LeftFan="0" RightFan="0" DotDetect="0" Capacity="0" PowerVol1="0" PowerVol2="0" PowerVol3="0" PowerVol4="0" PowerVol5="0" />
                <LoadedRegion    //ɨ�迨����
                	top="0" left="80" right="160" bottom="80" />
            </ScanCardAttachment>
        </ScanCardAttachments>
        <LoadedRegion left="0" top="0" right="160" bottom="80" /> //����Ĵ���������ɨ�迨1��ɨ�迨2�Ĳ��� 
    </Cabinet>
 * 
 */
	    int nID;				         	//����ID
		int nSeriesID;				//��������ϵ�е�ID
		String sSeriesName;    //��������ϵ�е�����
		String sName;				//�����ͺ�
		Boolean bRead;			//�Ѷ�ȡ��ʾ
		String sPhoto;				//������Ƭ
		int nAddress;				//����������ͼ�еĵ�ַ����ַ�õ�����������ͬһ��·��Ψһ��
		Boolean bMonitorParaSynchro;		//��ز����Ƿ�ͬ���޸�
		RECT rtRect;				//�������ϽǼ���С
		
		public int getnID() {
			return nID;
		}
		public void setnID(int nID) {
			this.nID = nID;
		}
		public int getnSeriesID() {
			return nSeriesID;
		}
		public void setnSeriesID(int nSeriesID) {
			this.nSeriesID = nSeriesID;
		}
		public String getsSeriesName() {
			return sSeriesName;
		}
		public void setsSeriesName(String sSeriesName) {
			this.sSeriesName = sSeriesName;
		}
		public String getsName() {
			return sName;
		}
		public void setsName(String sName) {
			this.sName = sName;
		}
		public Boolean getbRead() {
			return bRead;
		}
		public void setbRead(Boolean bRead) {
			this.bRead = bRead;
		}
		public String getsPhoto() {
			return sPhoto;
		}
		public void setsPhoto(String sPhoto) {
			this.sPhoto = sPhoto;
		}
		public int getnAddress() {
			return nAddress;
		}
		public void setnAddress(int nAddress) {
			this.nAddress = nAddress;
		}
		
		public Boolean getbMonitorParaSynchro() {
			return bMonitorParaSynchro;
		}
		public void setbMonitorParaSynchro(Boolean bMonitorParaSynchro) {
			this.bMonitorParaSynchro = bMonitorParaSynchro;
		}
		
		public RECT getRtRect() {
			return rtRect;
		}
		public void setRtRect(RECT rtRect) {
			this.rtRect = rtRect;
		}
		//ɨ�迨��Ϣ
		class ScanCard{
			private String Screen_type;  //��ʾ������
			private String Chip_type;  //�ư�оƬ
			private int EMPTY_DOT_NUM ;//�յ���
			private int REAL_DOT_NUM ;//ʵ�ʵ���
			private int SCAN_CARD_WIDTH; //ɨ�迨������
			private int SCAN_CARD_HEIGHT; //ɨ�迨����߶�
			private int SCAN_CARD_WIDTH_REAL; //ɨ�迨ʵ�ʿ��
			private int SCAN_CARD_HEIGHT_REAL; //ɨ�迨ʵ�ʸ߶�
			private String  refresh_rate; //ˢ��Ƶ��
			private int  dot_correct_type; //У������
			int nScanCardCount;				//ɨ�迨������1��4��
			Boolean bScanCardParaSynchro;		//ɨ�迨�����Ƿ�ͬ���޸�
			String InlineMode;		//���߷�ʽ
			public ScanCard() {
				super();
				// TODO Auto-generated constructor stub
			}
			public ScanCard(String screen_type, String chip_type,
					int eMPTY_DOT_NUM, int rEAL_DOT_NUM, int sCAN_CARD_WIDTH,
					int sCAN_CARD_HEIGHT, int sCAN_CARD_WIDTH_REAL,
					int sCAN_CARD_HEIGHT_REAL, String refresh_rate,
					int dot_correct_type, int nScanCardCount,
					Boolean bScanCardParaSynchro, String inlineMode) {
				super();
				Screen_type = screen_type;
				Chip_type = chip_type;
				EMPTY_DOT_NUM = eMPTY_DOT_NUM;
				REAL_DOT_NUM = rEAL_DOT_NUM;
				SCAN_CARD_WIDTH = sCAN_CARD_WIDTH;
				SCAN_CARD_HEIGHT = sCAN_CARD_HEIGHT;
				SCAN_CARD_WIDTH_REAL = sCAN_CARD_WIDTH_REAL;
				SCAN_CARD_HEIGHT_REAL = sCAN_CARD_HEIGHT_REAL;
				this.refresh_rate = refresh_rate;
				this.dot_correct_type = dot_correct_type;
				this.nScanCardCount = nScanCardCount;
				this.bScanCardParaSynchro = bScanCardParaSynchro;
				InlineMode = inlineMode;
			}
			public String getScreen_type() {
				return Screen_type;
			}
			public void setScreen_type(String screen_type) {
				Screen_type = screen_type;
			}
			public String getChip_type() {
				return Chip_type;
			}
			public void setChip_type(String chip_type) {
				Chip_type = chip_type;
			}
			public int getEMPTY_DOT_NUM() {
				return EMPTY_DOT_NUM;
			}
			public void setEMPTY_DOT_NUM(int eMPTY_DOT_NUM) {
				EMPTY_DOT_NUM = eMPTY_DOT_NUM;
			}
			public int getREAL_DOT_NUM() {
				return REAL_DOT_NUM;
			}
			public void setREAL_DOT_NUM(int rEAL_DOT_NUM) {
				REAL_DOT_NUM = rEAL_DOT_NUM;
			}
			public int getSCAN_CARD_WIDTH() {
				return SCAN_CARD_WIDTH;
			}
			public void setSCAN_CARD_WIDTH(int sCAN_CARD_WIDTH) {
				SCAN_CARD_WIDTH = sCAN_CARD_WIDTH;
			}
			public int getSCAN_CARD_HEIGHT() {
				return SCAN_CARD_HEIGHT;
			}
			public void setSCAN_CARD_HEIGHT(int sCAN_CARD_HEIGHT) {
				SCAN_CARD_HEIGHT = sCAN_CARD_HEIGHT;
			}
			public int getSCAN_CARD_WIDTH_REAL() {
				return SCAN_CARD_WIDTH_REAL;
			}
			public void setSCAN_CARD_WIDTH_REAL(int sCAN_CARD_WIDTH_REAL) {
				SCAN_CARD_WIDTH_REAL = sCAN_CARD_WIDTH_REAL;
			}
			public int getSCAN_CARD_HEIGHT_REAL() {
				return SCAN_CARD_HEIGHT_REAL;
			}
			public void setSCAN_CARD_HEIGHT_REAL(int sCAN_CARD_HEIGHT_REAL) {
				SCAN_CARD_HEIGHT_REAL = sCAN_CARD_HEIGHT_REAL;
			}
			public String getRefresh_rate() {
				return refresh_rate;
			}
			public void setRefresh_rate(String refresh_rate) {
				this.refresh_rate = refresh_rate;
			}
			public int getDot_correct_type() {
				return dot_correct_type;
			}
			public void setDot_correct_type(int dot_correct_type) {
				this.dot_correct_type = dot_correct_type;
			}
			public int getnScanCardCount() {
				return nScanCardCount;
			}
			public void setnScanCardCount(int nScanCardCount) {
				this.nScanCardCount = nScanCardCount;
			}
			public Boolean getbScanCardParaSynchro() {
				return bScanCardParaSynchro;
			}
			public void setbScanCardParaSynchro(Boolean bScanCardParaSynchro) {
				this.bScanCardParaSynchro = bScanCardParaSynchro;
			}
			public String getInlineMode() {
				return InlineMode;
			}
			public void setInlineMode(String inlineMode) {
				InlineMode = inlineMode;
			}
			//����ɨ�迨toString()����
			@Override
			public String toString() {
				return "ScanCard [Screen_type=" + Screen_type + ", Chip_type="
						+ Chip_type + ", EMPTY_DOT_NUM=" + EMPTY_DOT_NUM
						+ ", REAL_DOT_NUM=" + REAL_DOT_NUM
						+ ", SCAN_CARD_WIDTH=" + SCAN_CARD_WIDTH
						+ ", SCAN_CARD_HEIGHT=" + SCAN_CARD_HEIGHT
						+ ", SCAN_CARD_WIDTH_REAL=" + SCAN_CARD_WIDTH_REAL
						+ ", SCAN_CARD_HEIGHT_REAL=" + SCAN_CARD_HEIGHT_REAL
						+ ", refresh_rate=" + refresh_rate
						+ ", dot_correct_type=" + dot_correct_type
						+ ", nScanCardCount=" + nScanCardCount
						+ ", bScanCardParaSynchro=" + bScanCardParaSynchro
						+ ", InlineMode=" + InlineMode + "]";
			}
		}
		  //��ؿ���Ϣ
		 class MonitorInfo{
			 private  int THBoard;             //��ʪ�ȴ�����
			 private  int MultiFuncBoard;  //�๦�ܼ�⿨
			 private  int PowerBoard;       //���ʼ�⿨
			 private  int DotDectorBoard;  //����⿨
			public MonitorInfo() {
				super();
				// TODO Auto-generated constructor stub
			}
			public MonitorInfo(int tHBoard, int multiFuncBoard, int powerBoard,
					int dotDectorBoard) {
				super();
				THBoard = tHBoard;
				MultiFuncBoard = multiFuncBoard;
				PowerBoard = powerBoard;
				DotDectorBoard = dotDectorBoard;
			}
			public int getTHBoard() {
				return THBoard;
			}
			public void setTHBoard(int tHBoard) {
				THBoard = tHBoard;
			}
			public int getMultiFuncBoard() {
				return MultiFuncBoard;
			}
			public void setMultiFuncBoard(int multiFuncBoard) {
				MultiFuncBoard = multiFuncBoard;
			}
			public int getPowerBoard() {
				return PowerBoard;
			}
			public void setPowerBoard(int powerBoard) {
				PowerBoard = powerBoard;
			}
			public int getDotDectorBoard() {
				return DotDectorBoard;
			}
			public void setDotDectorBoard(int dotDectorBoard) {
				DotDectorBoard = dotDectorBoard;
			}
			//���¼�ؿ�toString()����
			@Override
			public String toString() {
				return "MonitorInfo [THBoard=" + THBoard + ", MultiFuncBoard="
						+ MultiFuncBoard + ", PowerBoard=" + PowerBoard
						+ ", DotDectorBoard=" + DotDectorBoard + "]";
			}
				 
		 }
		 
		 //��д����toString()����
		@Override
		public String toString() {
			return "Cabinet [nID=" + nID + ", nSeriesID=" + nSeriesID
					+ ", sSeriesName=" + sSeriesName + ", sName=" + sName
					+ ", bRead=" + bRead + ", sPhoto=" + sPhoto + ", nAddress="
					+ nAddress + ", bMonitorParaSynchro=" + bMonitorParaSynchro
					+ ", rtRect=" + rtRect + "]";
		}
		
		
}
