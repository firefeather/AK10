/*
   * 文件名 CabinetInformationActivity.java
   * 包含类名列表com.szaoto.ak10.leddisplay
   * 版本信息，版本号
   * 创建日期2013年11月8日上午11:53:35
   * 版权声明 liangdb-szaoto
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
 * 类名CabinetInformationActivity
 * 作者 zhangsj
 * 主要功能 显示箱体模组信息 扫描卡信息 监控卡 信息 
 * 创建日期2014年8月11日
 * 修改者，修改日期，修改内容
 */

public class CabinetInfomationActivity extends Activity {
	// ================变量=================
	private TextView btn_Back;

	private String cabinetName;

	private TextView tvCabinetInfo;

	private TextView tvModelInfo;

	private TextView tvScancardInfo;

	private TextView tvMonitorInfo2;

	private TextView tv_Modle;
	private TextView tv_ScanCard;

	//private TextView tv_Monitor;
	// 当前 要读取的 箱体Xml 元素
	Element currentReadElement;

	// ScanCard List
	List<Node> list = new ArrayList<Node>();

	// MonitorCard List
	List<Node> list1 = new ArrayList<Node>();

//	private Button btn_ModifyName;

	public static String CONFIG_PATH;

	// ================外部类/接口 对象=================

	// ================方法=================

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

		// 1、 模组信息

		StringBuffer sbModelInfo = new StringBuffer();

		// 2、 扫描卡信息
		StringBuffer sbScancardInfo = new StringBuffer();

		// 3、 监控卡信息
		StringBuffer sbMonitorInfo = new StringBuffer();

		// ================

		// 箱体 子节点
		Element childElement;

		// 箱体 子节点下一节点
		Element grandchildElement;

		// 箱体 子节点下一节点再下一级子节点
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
			//添加芯片类型判断，显示芯片类型
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
			  //单点校正
		     String  dottype=e.attribute("dot_correct_tye").getValue();
				int  ndot_type=Integer.parseInt(dottype);
			
			if (ndot_type==0) {
				//无
				dottype=getString(R.string.text_cabinetinfo_dotcorrecttype_null);
			}else if (ndot_type==1) {
				//调亮
				dottype=getString(R.string.text_cabinetinfo_dotcorrecttype_bright);
			}else if (ndot_type==2) {
				//调色
				dottype=getString(R.string.text_cabinetinfo_dotcorrecttype_color);
			}	
			sbScancardInfo.append(getString(R.string.text_dot_correct_tye)
					+ dottype + "\r\n");
			}

		for (Node monitor : list1) {
			Element e1 = (Element) monitor;
			// if (tv_Monitor.getText().equals("监控卡信息")) {

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

		Log.i("信息：", valueOf);

	}

	/**
	 * 解析 XML 文件
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

	// ================事件=================

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
