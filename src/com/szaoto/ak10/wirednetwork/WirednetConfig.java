package com.szaoto.ak10.wirednetwork;

public class WirednetConfig {
	public String ipaddr;
	public String mask;
	public String gateway;
	public String dns;
	//dhcp = "yes"表明打开dhcp；dhcp = "no"表明关闭dhcp 
	public String dhcp;                  
	public WirednetConfig() {	
		
	}
}
