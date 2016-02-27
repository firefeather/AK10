package com.szaoto.ak10.wirednetwork;


import java.io.IOException;

import com.szaoto.ak10.commsdk.Wirednethotplugdetect;

public class wiredhotplug extends Thread {

	private boolean lastState = false;
	private int skfd;
	private String m_IpAddress;
	wiredhotplug(){
		skfd = Wirednethotplugdetect.OpenSocket();
	}

	public void run() {
		if(skfd < 0)
			return;
		
		while(true){
			//isruning = 1:网线被拔掉  isruning = 2:网线正常工作
			int isruning = Wirednethotplugdetect.Net_detect(skfd,"eth0");
			if(isruning == 1){//网线拔掉
				lastState  = false;
			}else if(isruning == 2){//网线正常工作
				//判断网络是否正常,，通过ping方法
				//boolean bState = WirednetConfigDb.isConnect();
				m_IpAddress = WirednetConfigDb.GetIpAddress();
				boolean bPingIp = WirednetConfigDb.pingIpAddr(m_IpAddress);
				if (!bPingIp) {
					lastState = false;
				}
				
				if(lastState == false)
					try {
						WirednetConfigDb.initWirednetConfig();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				lastState  = true;
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		Wirednethotplugdetect.CloseSocket(skfd);		
	}
}