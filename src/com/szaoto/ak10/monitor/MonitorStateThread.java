package com.szaoto.ak10.monitor;

import android.content.Context;
import android.os.Message;
import android.os.SystemClock;

import com.szaoto.ak10.common.MonitorData;
import com.szaoto.ak10.commsdk.FrameDataField;
import com.szaoto.ak10.commsdk.Packager;
import com.szaoto.ak10.datacomm.MonitorComm;

//public class MonitorStateThread
public class MonitorStateThread implements java.lang.Runnable{
	MonitorActivity monitorActivity;
	int mMainChanelPort;
	int mBackupChanelPort;
	Object lockObject;
	Packager packager;
	int prestate;
//	GetMainChanelPort();//p3,p2,p1, p0 ��Ӧ  һ���ֽڵĵ���λ 
//	GetBackupChanelPort();
	MonitorStateThread(Context context ,Object lockoObject)
	{
		monitorActivity = (MonitorActivity)context;
		mMainChanelPort = GetMainChanelPort();
		mBackupChanelPort = GetBackChanelport();
		prestate = -2;
		this.lockObject = lockoObject;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
	/*	while(!monitorActivity.bExit){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	    }
		synchronized (lockObject){
			lockObject.notify();
		}*/
		while(true)
		{
			/*
			synchronized (lockObject){
    		try {
				lockObject.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		if(monitorActivity.lockObjectst == "run")
    		{
    			monitorActivity.lockObjectst = "wait";
    		}
    		else if(monitorActivity.lockObjectst == "stop")
    		{
    			break;
    		}
    		int state = StateJudgment();//����ֵ Ϊ-1 ״̬Ϊ�Ͽ� ������ֵΪ0��ʾ����״̬������ֵΪ1��ʾ����״̬
    	
			Message message = new Message();
			message.what = 3;
			message.arg1 = state;
			if(prestate != state)
    		{ 
				message.arg2 = 1;
			}
			else {
				message.arg2 = 0;
			}
			prestate = state;
			monitorActivity.getHandler().sendMessage(message);
			}
			*/
		}
		
}
	Byte GetMainChanelPort()//p3.p2.p1.p0 �ֱ� ��ʾһ���ֽڵĵ�4λ 
	{
		//int   readUnsignedByte()    
		
		
		return 0;//p0
	}
	Byte GetBackChanelport()
	{
		return 2;//p1
	}
	void SendfromPort(Byte ports)
	{
		
	}
	 int StateJudgment()//
	 {
		 byte[] ucSendData = new byte[FrameDataField.ETH_DATA_MAX_SIZE - 5];
		 SendfromPort(GetMainChanelPort());
		 if (0 < MonitorComm.SendMonitorConnectstateCommond(1))
			{
				SystemClock.sleep(1000);
				ucSendData = MonitorComm.ReadBackData(1);
				if(null != ucSendData)
				{
					return 1;
				}
			}
		 SendfromPort(GetBackChanelport());
		 if (0 < MonitorComm.SendMonitorConnectstateCommond(1))
			{
				SystemClock.sleep(1000);
				ucSendData = MonitorComm.ReadBackData(1);
				if(null != ucSendData)
				{
					return 0;
				}
			}
		 return -1;
	 }
}
