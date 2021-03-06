package com.szaoto.ak10.colortemp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.R;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.sqlitedata.ColorTemperData;
import com.szaoto.ak10.sqlitedata.ColorTemperDb;
import com.szaoto.ak10.util.TraverseDictionary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ColorTempSetActivity extends Activity
{
	private String Usb_PATH;
	private String sBinfile = ".xml";
	ArrayList<String> ArrPathInfos = new ArrayList<String>();
	ListView  listview_File;
	
	Button btnSubmit;
	Button btnCancel;
	TextView txtTitle;
	
	String m_strFilePath;
	String strLoadType;
	
	public void InitPathList()
	{
		TraverseDictionary TD = new TraverseDictionary();
		Usb_PATH=TraverseDictionary.GetUDiskDir();
		TD.GetFilePaths(Usb_PATH, sBinfile , true);
		ArrPathInfos = (ArrayList<String>) TD.getLstFilePath();
		listview_File.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice,ArrPathInfos)); 
		listview_File.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.colortemper_layout);
		SerialPortControlBroadCast.SetCurrentContext(this);
	
		
		listview_File = (ListView) findViewById(R.id.lv_add_list);
		btnSubmit     =  (Button) findViewById(R.id.btnSubmit);
		btnCancel     =  (Button) findViewById(R.id.btnCancel);
		txtTitle      =   (TextView) findViewById(R.id.txtTilteColorTemper);
		
		 Intent tIntent = getIntent();
		 strLoadType =  (String) tIntent.getExtras().get("LoadType");
		 
		InitPathList();
		
		
		if (strLoadType.equals("ColorTemper")) {
			//loadcolortemper
			txtTitle.setText(getString(R.string.loadcolortemper));
		}
		else if (strLoadType.equals("Gamma")) {
			//loadgammatable
			txtTitle.setText(getString(R.string.loadgammatable));
		}
		
		listview_File.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				m_strFilePath = ArrPathInfos.get(position);
			}		
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {		
			   finish();	
			}
		});
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
			
                if (m_strFilePath==null) {
					
					Toast.makeText(btnSubmit.getContext(),"No File Select", Toast.LENGTH_LONG).show();
					return;
				}
			
                InputStream is = null;
				try {
					File inFile = new File(m_strFilePath);
					is = new FileInputStream(inFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
               
				if (strLoadType.equals("ColorTemper")) {
					LoadColorTemperFile(is);
				}
				else if (strLoadType.equals("Gamma")) {
					LoadGammaTableFile(is);
				}
				
              
				//导入成功提示
				Toast.makeText(btnSubmit.getContext(), txtTitle.getText()+" Success!", Toast.LENGTH_LONG).show();
                
				
				
			}
		});
	}

	public void LoadGammaTableFile( InputStream inStream){
		
		//将原来的文件删除然后把这个文件复制进去
		File fileAk10 = new File(HomePageActivity.CONFIG_PATH + "GammaTable.xml"); 	
		fileAk10.delete();
		FileOutputStream fileOutputStream;
		try 
		{
			 fileOutputStream  = new FileOutputStream(fileAk10);
			 byte[] inOutb = new byte[inStream.available()];
			 inStream.read(inOutb);
			 fileOutputStream.write(inOutb);
			 
			 inStream.close();
			 fileOutputStream.close();
	
		}
		catch (IOException e) {
				e.printStackTrace();
		} 
		
	}
	
	public void LoadColorTemperFile( InputStream is){
		
		  DomColorTemperParser  parser = new DomColorTemperParser();  
          try {
				HashMap<Integer, ColorTemperData> tHashMap = parser.parse(is);
				
				int ColorTemp = 2800;
				ColorTemperData tColorTemperData = tHashMap.get(ColorTemp);	
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 1);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 2);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 3);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 4);
				
				ColorTemp = 3200;
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 1);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 2);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 3);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 4);
				
				ColorTemp = 4500;
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 1);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 2);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 3);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 4);
				
				ColorTemp = 5000;
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 1);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 2);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 3);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 4);
				
				ColorTemp = 5600;
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 1);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 2);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 3);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 4);
				
				ColorTemp = 6500;
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 1);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 2);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 3);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 4);
				
				ColorTemp = 8000;
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 1);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 2);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 3);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 4);
				
				ColorTemp = 9300;
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 1);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 2);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 3);
				ColorTemperDb.UpdateColorTemperValue(tColorTemperData, ColorTemp, 4);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	}
	
}
