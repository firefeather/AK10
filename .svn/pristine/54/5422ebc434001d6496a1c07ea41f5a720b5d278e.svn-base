/*
   * 文件名 ControlActivity.java
   * 包含类名列表com.szaoto.ak10.control
   * 版本信息，版本号
   * 创建日期2013年11月8日上午11:53:51
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.systemconfig;

import com.szaoto.ak10.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



/*
 * 类名ControlActivity
 * 作者 liangdb
 * 主要功能
 * 创建日期2013年11月8日
 * 修改者，修改日期，修改内容
 */
public class SystemResetFragment extends Fragment {

	String[] flLists;
	Button btnButton;
	View viewthis = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	//	setContentView(R.layout.systemreset);
		
	}
	//View view =inflater.inflate(R.layout.ethernet, null);
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view =inflater.inflate(R.layout.systemreset, null);
		viewthis = view;
		btnButton = (Button)view.findViewById(R.id.buttonreset);
		btnButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*try {
					 flLists =	getActivity().getResources().getAssets().list("");	 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int s = flLists.length;
		//		DataAccessAcquisitionCard
				DataAccessAcquisitionCard dataAccessSystemCard = new DataAccessAcquisitionCard(getActivity());
				List<AcquisitionCard> acquisitionCards = null ;
				try {
					dataAccessSystemCard.SaveAcquisitionCard(acquisitionCards);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DataAccessSystemCard dataSystemCard = new DataAccessSystemCard(getActivity());
				List<SystemCard> SystemCards = null ;
				try {
					dataSystemCard.SaveSystemCard(SystemCards);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				new AlertDialog.Builder(getActivity())  
                .setTitle(R.string.text_tip)  
                .setMessage(R.string.text_reset_tip)  
                .setPositiveButton(R.string.text_reset, new DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialog, int which) { 
                        PowerManager pManager=(PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);  
                    //    pManager.reboot("重启");  
                        System.out.println("execute cmd--> reboot\n" +R.string.text_reset);  
                    }  
                })  
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {  
                    @Override  
                    public void onClick(DialogInterface dialog, int which) {  
                        // 取消当前对话框   
                        dialog.cancel();  
                    }  
                }).show();  
            }  
             
       });  
		//});}
	//	return super.onCreateView(inflater, container, savedInstanceState);
		return view;
	}
}

