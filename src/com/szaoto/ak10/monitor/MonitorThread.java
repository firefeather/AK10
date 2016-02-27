package com.szaoto.ak10.monitor;

import android.content.Context;
import android.os.Message;

public class MonitorThread implements java.lang.Runnable{
	 	private MonitorActivity contextactivity;
	    private Object lockObject;
	    public MonitorThread(Context context, Object obj)
	    {
	    	this.contextactivity = (MonitorActivity)context;
	        this.lockObject = obj;
	    } 
	    @Override
	    public  void run() 
	    {/*
	    	while(true)
	    	{	
	    		synchronized (lockObject){
	    		try {lockObject.wait();
	    		
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		if(contextactivity.lockObjectau == "run")
	    		{
	    			contextactivity.lockObjectau = "wait";
	    		}
	    		else if(contextactivity.lockObjectau == "stop")
	    		{
	    			break;
	    		}
	    		Message message = new Message();
				message.what = 2;
				contextactivity.getHandler().sendMessage(message);
	    	}
	     }
	    	*/
	 }
}
