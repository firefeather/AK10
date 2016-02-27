/*
 * �ļ��� ControlActivity.java
 * ���������б�com.szaoto.ak10.control
 * �汾��Ϣ���汾��
 * ��������2013��11��8������11:53:51
 * ��Ȩ���� liangdb-szaoto
 */
package com.szaoto.ak10.systemconfig;

import java.util.Locale;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.R;
import com.szaoto.ak10.adapter.SpinnerAdapter;
import com.szaoto.ak10.sqlitedata.CardInfoDB;
import com.szaoto.ak10.sqlitedata.ChanGroupDb;
import com.szaoto.ak10.sqlitedata.ChannelDB;
import com.szaoto.ak10.sqlitedata.InterfaceDB;
import com.szaoto.ak10.util.CommunicateDebugActivity;

//import com.szaoto.ak10.systemconfig.SystemFragmentActivity.SpinnerXMLSelectedListener;

/*
 * ����ControlActivity
 * ���� liangdb
 * ��Ҫ����
 * ��������2013��11��8��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class LanguageSetFragment extends Fragment {
	// private NavigationBar navigationBar;
	private Button btn_Debug;
	private Button btn_Reset;
	private SpinnerAdapter adapter;
	private Spinner spinner;

	boolean bFirstIn = true;
	private static final String[] languagetype = { "English", "��������" };
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.systemconfig_layout);
	}
	
	private boolean isZh() {
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.config_system, null);
		getActivity().getApplicationContext();
		preferences = getActivity().getSharedPreferences("11",
				Context.MODE_PRIVATE);
		// ����޸���
		editor = preferences.edit();
		editor.commit();
		spinner = (Spinner) view.findViewById(R.id.spinnerlanguage);
		adapter = new SpinnerAdapter(getActivity(),
				android.R.layout.simple_spinner_item, languagetype);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

//		String sLan = preferences.getString("LANGUAGETYPE", "Default");
//	
//		if (sLan.equals("Default")) {
//			spinner.setSelection(1, true);
//		} else {
//			int type = Integer.valueOf(preferences.getString("LANGUAGETYPE",
//					"Default"));
//			spinner.setSelection(type, true);
//		}
		
		if (isZh()) {
			spinner.setSelection(1, true);
		}else {
			spinner.setSelection(0, true);
		}
		

		spinner.setVisibility(View.VISIBLE);
		btn_Debug = (Button) view.findViewById(R.id.btn_Debug);
		btn_Reset = (Button) view.findViewById(R.id.btn_Reset);
		btn_Debug.setOnClickListener(clickHandler);
		btn_Reset.setOnClickListener(clickHandler);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {

//				 if (bFirstIn) {
//				 bFirstIn =false;
//				 return;
//				 }
				if (pos==0&&isZh()||pos==1&&!isZh()) {
					setlanguage(pos);
				}
				

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		return view;
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void SetcurrentActivitylanguage() {
		btn_Debug.setText(getString(R.string.Debug_Entrance));
	}

	public void setlanguage(int langtype) {// 0,����Ӣ�1������������2��������������
		// �л���ʾ
		dialog(langtype);
	}

	public void dialog(final int langtype) {

		AlertDialog.Builder builder = new Builder(getActivity());
		builder.setMessage(getString(R.string.ensureswitchlang));
		builder.setPositiveButton(R.string.OK, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				
				editor.putString("LANGUAGETYPE", Integer.toString(langtype));
				// �ύ���д��������
				editor.commit();
				
				//���浽���ݿ���
				//PreferencesDb.UpdatePreferencesValue("LANGUAGETYPE", langtype);								
				//Ak10Application mApp = (Ak10Application) getActivity().getApplication();
				//mApp.SetLanguage();
				
				HomePageActivity.getInstance().SetLanguage();

				dialog.dismiss();
				Intent tIntent = new Intent(getActivity(),HomePageActivity.class);
				startActivity(tIntent);
				getActivity().finish();

			}
		});
		builder.setNegativeButton(getString(R.string.Cancel),
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						dialog.dismiss();
					}
				});

		builder.create().show();
	}

	View.OnClickListener clickHandler = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_Debug: {
				Intent intentCommunicateDebugActivity = new Intent(
						getActivity().getApplicationContext(),
						CommunicateDebugActivity.class);
				startActivity(intentCommunicateDebugActivity);
			}
				break;

			case R.id.btn_Reset: {

				// ɾ������������õı��е�����

				for (int i = 1; i <= 4; i++) {
					ChannelDB.DeleteAllChanData(i);
					InterfaceDB.DeleteAllData(i);
					ChanGroupDb.DeleteAllRecords(i);
				}

				CardInfoDB.DeleteAllData();

				/*
				 * SqliteDB.CloseDB();
				 * 
				 * //ɾ��ԭ��������
				 * 
				 * InputStream is = null; try { File inFile = new
				 * File(HomePageActivity.CONFIG_PATH + "akbak.db"); is = new
				 * FileInputStream(inFile); } catch (IOException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 * 
				 * File fileAk10 = new File(HomePageActivity.CONFIG_PATH +
				 * "ak10.db"); fileAk10.delete(); FileOutputStream
				 * fileOutputStream; try { fileOutputStream = new
				 * FileOutputStream(fileAk10); byte[] inOutb = new
				 * byte[is.available()]; is.read(inOutb);
				 * fileOutputStream.write(inOutb);
				 * 
				 * is.close(); fileOutputStream.close();
				 * 
				 * } catch (IOException e) { e.printStackTrace(); }
				 * 
				 * 
				 * SqliteDB.OpenDB();
				 */

			}
				break;

			default:
				break;
			}
		}
	};
}