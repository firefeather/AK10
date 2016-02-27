/*
   * �ļ��� CabinetInformationActivity.java
   * ���������б�com.szaoto.ak10.leddisplay
   * �汾��Ϣ���汾��
   * ��������2013��11��8������11:53:35
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.leddisplay;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.szaoto.ak10.R;
import com.szaoto.ak10.R.id;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;


/*
 * ����CabinetInformationActivity
 * ���� zhangsj
 * ��Ҫ���� ��ʾ����ģ����Ϣ ɨ�迨��Ϣ ��ؿ� ��Ϣ 
 * ��������2014��8��11��
 * �޸��ߣ��޸����ڣ��޸�����
 */

public class CabinetInfomationActivity extends Activity {
	// ================����=================
	private TextView btn_Back;

	private String cabinetName;

	private TextView tvCabinetInfo;

	private TextView tvModelInfo;

	private TextView tvScancardInfo;

	private TextView tvMonitorInfo2;

	private TextView tv_Modle;
	private TextView tv_ScanCard;

	//private TextView tv_Monitor;
	// ��ǰ Ҫ��ȡ�� ����Xml Ԫ��
	Element currentReadElement;

	// ScanCard List
	List<Node> list = new ArrayList<Node>();

	// MonitorCard List
	List<Node> list1 = new ArrayList<Node>();

//	private Button btn_ModifyName;

	public static String CONFIG_PATH;

	// ================�ⲿ��/�ӿ� ����=================

	// ================����=================

	public void MsgShow(String msg) {

		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

	}

	private void setView() {

		tvCabinetInfo = (TextView) findViewById(R.id.tv_cabinetInfo);

		tvModelInfo = (TextView) findViewById(id.tvModelInfo);
		tvScancardInfo = (TextView) findViewById(id.tvScancardInfo);
		tvMonitorInfo2 = (TextView) findViewById(id.tvMonitorInfo2);

		tv_Modle = (TextView) findViewById(R.id.tv_modle);
		tv_ScanCard = (TextView) findViewById(R.id.tv_modle);
		//tv_Monitor = (TextView) findViewById(R.id.tv_monitor);

	}


	private void setData() {

		cabinetName = getIntent().getStringExtra("CabinetName");

		tvCabinetInfo.setText(cabinetName);

		// showInfo();

	}

	private void showInfo() {

		// 1�� ģ����Ϣ

		StringBuffer sbModelInfo = new StringBuffer();

		// 2�� ɨ�迨��Ϣ
		StringBuffer sbScancardInfo = new StringBuffer();

		// 3�� ��ؿ���Ϣ
		StringBuffer sbMonitorInfo = new StringBuffer();

		// ================

		// ���� �ӽڵ�
		Element childElement;

		// ���� �ӽڵ���һ�ڵ�
		Element grandchildElement;

		// ���� �ӽڵ���һ�ڵ�����һ���ӽڵ�
		Element grandchildElement2;

		for (Iterator cabinetInfoElement = currentReadElement.elementIterator(); cabinetInfoElement
				.hasNext();) {

			childElement = (Element) cabinetInfoElement.next();

			if (childElement.getName().equals("SCACount")) {

				sbScancardInfo.append(getString(R.string.text_scancardnum) + childElement.getText()
						+ "\r\n");
			}
			if (childElement.getName().equals("inlinemode")) {

				sbScancardInfo
						.append(getString(R.string.text_inlinemode )+ childElement.getText() + "\r\n");
			}
			if (childElement.getName().equals("ScanCardAttachments")) {

				for (Iterator scanCardAttachmentInfoElement = childElement
						.elementIterator(); scanCardAttachmentInfoElement
						.hasNext();) {
					grandchildElement = (Element) scanCardAttachmentInfoElement
							.next();

					for (Iterator grandchildElement2Iterator = grandchildElement
							.elementIterator(); grandchildElement2Iterator
							.hasNext();) {

						grandchildElement2 = (Element) grandchildElement2Iterator
								.next();

						if (grandchildElement2.getName().equals("ScanCard")) {
							list.add(grandchildElement2);
						}

						if (grandchildElement2.getName().equals("MonitorCard")) {
							list1.add(grandchildElement2);
						}
					}
				}
			}
		}

		// ================

		for (Node n : list) {

			n.getText();

			Element e = (Element) n;
	        String display_type=e.attribute("screen_type").getValue();
	    	int  ndisplay_type=Integer.parseInt(display_type);
	        if (ndisplay_type==2) {
	        	display_type=getString(R.string.text_cabinetinfo_displaytype_f);
			}else if (ndisplay_type==3) {
				display_type=getString(R.string.text_cabinetinfo_displaytype_v);
			}
			sbModelInfo.append(getString(R.string.text_displaytype )+ display_type
					+ "\r\n");
			//���оƬ�����жϣ���ʾоƬ����
			String  stringtype=e.attribute("chip_type").getValue();
			int  nchip_type=Integer.parseInt(stringtype);
		     if (nchip_type==0){
				
		    	 sbModelInfo.append(getString(R.string.text_chip_type ) + "_GENERAL"
		    			 + "\r\n");
			}else if (nchip_type==1) {
				sbModelInfo.append(getString(R.string.text_chip_type )  + "_MBI5042"
						+ "\r\n");
				
			}else if (nchip_type==2) {
				sbModelInfo.append(getString(R.string.text_chip_type )  + "_MBI5030"
						+ "\r\n");
				
			}else if (nchip_type==3) {
				sbModelInfo.append(getString(R.string.text_chip_type )  + "_TC62D722"
						+ "\r\n");
				
			}else if (nchip_type==4) {
				sbModelInfo.append(getString(R.string.text_chip_type )  + "_MBI5050"
						+ "\r\n");
				
			}else if (nchip_type==5) {
				sbModelInfo.append(getString(R.string.text_chip_type )  + "_TLC5948"
						+ "\r\n");
				
			}else if (nchip_type==6) {
				sbModelInfo.append(getString(R.string.text_chip_type )  + "_MBI5040"
						+ "\r\n");
				
			}else if (nchip_type==7) {
				sbModelInfo.append(getString(R.string.text_chip_type )  + "_MBI5041"
						+ "\r\n");
				
			}else if (nchip_type==8) {
				sbModelInfo.append(getString(R.string.text_chip_type )  + "_MBI5045"
						+ "\r\n");
				
			}else if (nchip_type==9) {
				sbModelInfo.append(getString(R.string.text_chip_type )  + "	_TLC5958"
						+ "\r\n");
				
			}else if (nchip_type==10) {
				sbModelInfo.append(getString(R.string.text_chip_type )  + "_MBI5152"
						+ "\r\n");
				
			}else if (nchip_type==11) {
				sbModelInfo.append(getString(R.string.text_chip_type )  + "_MBI5153"
						+ "\r\n");
				
			}else if (nchip_type==12) {
				sbModelInfo.append(getString(R.string.text_chip_type )  + "_MBI5153_E"
						+ "\r\n");
				
			}else if (nchip_type==13) {
				sbModelInfo.append(getString(R.string.text_chip_type )  + "_MBI5043"
						+ "\r\n");
				
			}else if (nchip_type==14) {
				sbModelInfo.append(getString(R.string.text_chip_type )  + "_MBI5155"
						+ "\r\n");
				
			}else if (nchip_type==15) {
				sbModelInfo.append(getString(R.string.text_chip_type )  + "_TDefalut1"
						+ "\r\n");
				
			}
		   
			
		 	sbModelInfo.append(getString(R.string.text_realdot_num )+ e.attribute("REAL_DOT_NUM").getValue()
					+ "\r\n");
			sbModelInfo.append(getString(R.string.text_emptydot_num)+ e.attribute("EMPTY_DOT_NUM").getValue()
					+ "\r\n");
			sbModelInfo.append(getString(R.string.text_scan_mode) + e.attribute("SCAN_MODE").getValue()
					+ "\r\n");

			sbScancardInfo.append(getString(R.string.text_ONE_SCAN_CARD_WIDTH)
					+ e.attribute("ONE_SCAN_CARD_WIDTH").getValue() + "\r\n");
			sbScancardInfo.append(getString(R.string.text_ONE_SCAN_CARD_HEIGHT)
					+ e.attribute("ONE_SCAN_CARD_HEIGHT").getValue() + "\r\n");
			sbScancardInfo.append(getString(R.string.text_ONE_SCAN_CARD_WIDTH_REAL)
					+ e.attribute("ONE_SCAN_CARD_WIDTH_REAL").getValue()
					+ "\r\n");
			sbScancardInfo.append(getString(R.string.text_ONE_SCAN_CARD_HEIGHT_REAL)
					+ e.attribute("ONE_SCAN_CARD_HEIGHT_REAL").getValue()
					+ "\r\n");
			sbScancardInfo.append(getString(R.string.text_GRAY_LEVEL)
					+ e.attribute("GRAY_LEVEL").getValue() + "\r\n");
			sbScancardInfo.append(getString(R.string.text_refresh_rate)
					+ e.attribute("refresh_rate").getValue() + "\r\n");
			  //����У��
		     String  dottype=e.attribute("dot_correct_tye").getValue();
				int  ndot_type=Integer.parseInt(dottype);
			
			if (ndot_type==0) {
				//��
				dottype=getString(R.string.text_cabinetinfo_dotcorrecttype_null);
			}else if (ndot_type==1) {
				//����
				dottype=getString(R.string.text_cabinetinfo_dotcorrecttype_bright);
			}else if (ndot_type==2) {
				//��ɫ
				dottype=getString(R.string.text_cabinetinfo_dotcorrecttype_color);
			}	
			sbScancardInfo.append(getString(R.string.text_dot_correct_tye)
					+ dottype + "\r\n");
			}

		for (Node monitor : list1) {
			Element e1 = (Element) monitor;
			// if (tv_Monitor.getText().equals("��ؿ���Ϣ")) {

			// ===================
			sbMonitorInfo.append(getString(R.string.text_THBoard)
					+ e1.attribute("THBoard").getValue() + "\r\n");
			sbMonitorInfo.append(getString(R.string.text_MultiFuncBoard)
					+ e1.attribute("MultiFuncBoard").getValue() + "\r\n");
			sbMonitorInfo.append(getString(R.string.text_PowerBoard)
					+ e1.attribute("PowerBoard").getValue() + "\r\n");
			sbMonitorInfo.append(getString(R.string.text_DotDectorBoard)
					+ e1.attribute("DotDectorBoard").getValue() + "\r\n");
		

		}

		tvModelInfo.setText(sbModelInfo.toString());

		tvScancardInfo.setText(sbScancardInfo.toString());

		tvMonitorInfo2.setText(sbMonitorInfo.toString());

	}

	private void p(String valueOf) {

		Log.i("��Ϣ��", valueOf);

	}

	/**
	 * ���� XML �ļ�
	 * 
	 * @throws Exception
	 */
	private void parseXml2() throws Exception {
		CONFIG_PATH = this.getFilesDir() + "//config//";
		SAXReader reader = new SAXReader();
		InputStream inCabinet;

		File xmlCabinet = new File(CONFIG_PATH, "Cabinet.cbt");
		inCabinet = new FileInputStream(xmlCabinet);

		InputStreamReader inputStreamReader = new InputStreamReader(inCabinet,
				"gb2312");

		Document document = reader.read(inputStreamReader);

		Element rootElement = document.getRootElement();
		p(rootElement.getName());

		for (Iterator i = rootElement.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			p(element.getName());// <Cabinet

			for (Iterator i2 = element.elementIterator(); i2.hasNext();) {

				Element element2 = (Element) i2.next();

				if (element2.getName().equals("Name")) {

					if (element2.getText().trim().equals(cabinetName.trim())) {
						currentReadElement = element;
						return;
					}
				}
			}
		}

	}

	// ================�¼�=================

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leddisplay_cabinetinformation);
		SerialPortControlBroadCast.SetCurrentContext(this);

		btn_Back = (TextView) findViewById(R.id.text_cabinettback);
		btn_Back.setOnClickListener(click);

		setView();

		setData();

		try {

			parseXml2();

			showInfo();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {

		super.onSaveInstanceState(savedInstanceState);
	}

	View.OnClickListener click = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};
}
