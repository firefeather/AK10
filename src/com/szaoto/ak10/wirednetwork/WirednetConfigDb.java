package com.szaoto.ak10.wirednetwork;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.szaoto.ak10.sqlitedata.SqliteDB;
import android.database.Cursor;

/*
 * 类名WirednetConfigDb
 * 作者   gaom
 * 主要功能
 * 创建日期2015年5月27日
 * 修改者，修改日期，修改内容
 */

public class WirednetConfigDb {
	
	private static String m_sIpAddress;

	public WirednetConfigDb() {
		// TODO Auto-generated constructor stub
	}

	public static String GetIpAddress(){
		return m_sIpAddress;
	}
	
	public static WirednetConfig GetWirednetConfig() {
		
		WirednetConfig netConfig = new WirednetConfig();
		String strSQLString = "SELECT * FROM tb_wirednet";

		try {
			Cursor c = SqliteDB.m_DbDatabase.rawQuery(strSQLString, null);
			if (c != null) {
				while (c.moveToNext()) {					
					netConfig.ipaddr = c.getString(c.getColumnIndex("ipaddr"));
					netConfig.mask = c.getString(c.getColumnIndex("mask"));
					netConfig.gateway = c.getString(c.getColumnIndex("gateway"));
					netConfig.dhcp = c.getString(c.getColumnIndex("dhcp"));
					netConfig.dns = c.getString(c.getColumnIndex("dns"));
				}
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return netConfig;
	}
	
	public static void OpenWirednetHotplugDetect() throws IOException{		
		wiredhotplug netDetect = new wiredhotplug();
		netDetect.start();
	}
	
	public static boolean SaveWirednetConfig(WirednetConfig netConfig) throws IOException{
		EnableWiredConfig(netConfig);
		UpdateDb(netConfig);
		m_sIpAddress = netConfig.ipaddr;
		return true;
	}
	
	public static boolean initWirednetConfig() throws IOException{
		
		if(SaveWirednetConfig(GetWirednetConfig()))
			return true;
		
		return false;
	}
	

	private static void UpdateDb(WirednetConfig netConfig) {
	
		String strIpSQL = "UPDATE tb_wirednet SET ipaddr = '"
				+ netConfig.ipaddr + "'";	
		SqliteDB.m_DbDatabase.execSQL(strIpSQL);
		
		String strMaskSQL = "UPDATE tb_wirednet SET mask = '"
				+ netConfig.mask + "'";
		SqliteDB.m_DbDatabase.execSQL(strMaskSQL);
		
		String strGatewaySQL = "UPDATE tb_wirednet SET gateway = '"
				+ netConfig.gateway + "'";
		SqliteDB.m_DbDatabase.execSQL(strGatewaySQL);
		
		String strDnsSQL = "UPDATE tb_wirednet SET dns = '"
				+ netConfig.dns + "'";
		SqliteDB.m_DbDatabase.execSQL(strDnsSQL);
		
		String strDhcpSQL = "UPDATE tb_wirednet SET dhcp = '"
				+ netConfig.dhcp + "'";
		SqliteDB.m_DbDatabase.execSQL(strDhcpSQL);
	}

	private static void EnableWiredConfig(WirednetConfig netConfig) throws IOException {
		try {
			List<String> cmdList = new ArrayList<String>();
			
			if(netConfig.dhcp.equals("yes")){
				String setDhcpComm0 = "setprop dhcp.eth0.reason BOUND";
				String setDhcpComm1 = "setprop dhcp.eth0.result ok";				
				cmdList.add(setDhcpComm0);
				cmdList.add(setDhcpComm1);	
				
			}else if(netConfig.dhcp.equals("no")){
				String setIpAddrComm = "busybox ifconfig eth0 " + netConfig.ipaddr + " netmask "
						+ netConfig.mask;
				String setGatewayComm = "route add default gw " + netConfig.gateway + " dev eth0";
				String setDnsComm = "setprop net.eth0.dns1 " + netConfig.dns;
											
				cmdList.add(setIpAddrComm);			
				cmdList.add(setGatewayComm);
				cmdList.add(setDnsComm);
			}				
			doCmds(cmdList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void doCmds(List<String> cmds) throws Exception {
		Process process = Runtime.getRuntime().exec("su");
		DataOutputStream os = new DataOutputStream(process.getOutputStream());
		for (String tmpCmd : cmds) {
			os.writeBytes(tmpCmd + "\n");
		}
		os.writeBytes("exit\n");
		os.flush();
		os.close();
		process.waitFor();
	}
	
    static boolean pingIpAddr(String ipAddress) {
        try {
            //String ipAddress = "172.16.200.213";
            Process p = Runtime.getRuntime().exec("ping -c 1 -w 100 " + ipAddress);
            int status = p.waitFor();
            if (status == 0) {
                return true;
            } else {
            }
        } catch (IOException e) {
        } catch (InterruptedException e) {
        }
        return false;
    }

		
 }
	
  