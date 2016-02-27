/*
   * 文件名 ControlActivity.java
   * 包含类名列表com.szaoto.ak10.control
   * 版本信息，版本号
   * 创建日期2013年11月8日上午11:53:51
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.systemconfig;


import com.friendlyarm.AndroidSDK.HardwareControler;
import com.szaoto.ak10.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;



/*
 * 类名ControlActivity
 * 作者 liangdb
 * 主要功能
 * 创建日期2013年11月8日
 * 修改者，修改日期，修改内容
 */
public class EthrenetSetFragment extends Fragment {
	SystemConfigActivity mApp;
	 private RadioButton mConTypeDhcp;
	 private RadioButton mConTypeManual;
	 private EditText mIpaddr;
	 private EditText mDns;
	 private EditText mGw;
	 private EditText mMask;
	 private TextView mIPaddressDhcpTextView;
	 private TextView mPorTextView;
	 private TextView mConnectionStateTextView;
	 private TextView mDnsaddressTextView;
	 private Button btn_backButton;
	 View mainview ;
//	 SystemConfig sytconfig = new SystemConfig();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	//	setContentView(R.layout.systemconfig_layout);
		
	      
	
	}
	@SuppressWarnings("unused")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =inflater.inflate(R.layout.ethernet, null);
	//	sytconfig = DataAccessSystemConfig.LoadSystemConfig();
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		mainview = view;
	    mIPaddressDhcpTextView = (TextView)view.findViewById(R.id.ipaddress1);
		mPorTextView = (TextView)view.findViewById(R.id.portnum);
		mConnectionStateTextView = (TextView)view.findViewById(R.id.connectstate);
		mDnsaddressTextView = (TextView)view.findViewById(R.id.connectnum);
		mConTypeDhcp = (RadioButton) view.findViewById(R.id.dhcp_radio);
        mConTypeManual = (RadioButton) view.findViewById(R.id.manual_radio);
        mIpaddr = (EditText)view.findViewById(R.id.ipaddressmannul);
        mMask = (EditText)view.findViewById(R.id.netmaskmannul);
        mDns = (EditText)view.findViewById(R.id.dnsaddressmannul);
        mGw = (EditText)view.findViewById(R.id.gatewaymannul);
        btn_backButton = (Button)view.findViewById(R.id.back);
        
        mConTypeDhcp.setChecked(true);
        mConTypeManual.setChecked(false);
        mIpaddr.setEnabled(false);
        mMask.setEnabled(false);
        mDns.setEnabled(false);
        mGw.setEnabled(false);
        mConTypeManual.setOnClickListener(new RadioButton.OnClickListener() {
            public void onClick(View v) {
                mIpaddr.setEnabled(true);
                mDns.setEnabled(true);
                mGw.setEnabled(true);
                mMask.setEnabled(true);
            }
        });

        mConTypeDhcp.setOnClickListener(new RadioButton.OnClickListener() {
           public void onClick(View v) {
                mIpaddr.setEnabled(false);
                mDns.setEnabled(false);
                mGw.setEnabled(false);
                mMask.setEnabled(false);
            }
        });
        btn_backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				//屏蔽掉
				//handle_saveconf();
			}
		});
      //  if (HardwareControler.loadEthConfig() != 0) {
        if (true) {
//        	 Toast.makeText(view.getContext(),R.string.text_loadeth_fail,Toast.LENGTH_SHORT).show();
        } else {
        	//String tttString = HardwareControler.getEthIP();
            mIpaddr.setText(HardwareControler.getEthIP());
            mGw.setText(HardwareControler.getEthGateway());
            mDns.setText(HardwareControler.getEthDns1());
            mMask.setText(HardwareControler.getEthNetmask());
            if (HardwareControler.isEthUsingDHCP() == 1) {
                mIpaddr.setEnabled(false);
                mDns.setEnabled(false);
                mGw.setEnabled(false);
                mMask.setEnabled(false);
            } else {
                mConTypeDhcp.setChecked(false);
                mConTypeManual.setChecked(true);
                mIpaddr.setEnabled(true);
                mDns.setEnabled(true);
                mGw.setEnabled(true);
                mMask.setEnabled(true);
            }
        }
		return view;
	}
	private boolean isIpAddress(String value) {
        int start = 0; 
        int end = value.indexOf('.'); 
        int numBlocks = 0; 
        
        while (start < value.length()) { 
            if (end == -1) { 
                end = value.length(); 
            } 
            try { 
                int block = Integer.parseInt(value.substring(start, end)); 
                if ((block > 255) || (block < 0)) { 
                    return false; 
                } 
            } catch (NumberFormatException e) { 
                return false; 
            }             
            numBlocks++; 
            start = end + 1; 
            end = value.indexOf('.', start); 
        } 
        return numBlocks == 4;
    }
	private void handle_saveconf() {

        boolean ok = false;
        if (mConTypeDhcp.isChecked()) {
           // Slog.v(TAG, "Config device for DHCP ");

            HardwareControler.setEthUsingDHCP(1);
            if (HardwareControler.saveEthConfig() == 0) {
                ok = true;
            }

            if (ok) {
                MessageBox(getString(R.string.text_dhcp_set));
            } else {
                MessageBox(getString(R.string.text_save_config));
            }

        } else {

            final String ip = mIpaddr.getText().toString();
            final String gateway = mGw.getText().toString();
            final String dns = mDns.getText().toString();
            final String netmask = mMask.getText().toString();

        //    Slog.v(TAG, "Config device for static " + ip + gateway + dns + netmask);

            if (!isIpAddress(ip)) {
                MessageBox(getString(R.string.text_setip_notcorrect));
                return ;
            }

            if (!isIpAddress(gateway)) {
                MessageBox(getString(R.string.text_setgw_notcorrect));
                return ;
            }

            if (!isIpAddress(dns)) {
                MessageBox(getString(R.string.text_setdns_notcorrect));
                return ;
            }

            if (!isIpAddress(netmask)) {
                MessageBox(getString(R.string.text_setnetmask_notcorrect));
                return ;
            }

            HardwareControler.setEthUsingDHCP(0);
            HardwareControler.setEthIP(mIpaddr.getText().toString());
            HardwareControler.setEthGateway(mGw.getText().toString());
            HardwareControler.setEthDns1(mDns.getText().toString());
            HardwareControler.setEthNetmask(mMask.getText().toString());

            if (HardwareControler.saveEthConfig() == 0) {
                ok = true;
            }

            if (ok) {
                MessageBox(getString(R.string.text_staticip_set));
            } else {
                MessageBox(getString(R.string.text_save_config));
            }
        }
    }    
    public void MessageBox(String message){
        Toast.makeText(mainview.getContext(),message,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    //	handle_saveconf();
    	super.onDestroy();
    }
	public void setActivity(SystemConfigActivity mainActivity) {
		// TODO Auto-generated method stub
		mApp = mainActivity;
	}
	
}
