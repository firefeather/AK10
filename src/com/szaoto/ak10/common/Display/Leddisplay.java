/*
   * �ļ��� ColourRGB.java
   * ���������б�com.szaoto.ak10.common
   * �汾��Ϣ���汾��
   * ��������2013��12��28������9:45:13
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.common.Display;

public class Leddisplay {
	public Leddisplay()
	{								
		ID = 0;						
		Name = "LED_0";				
		resolution  = "0 x 0";		    
		lastState = new LastState();
	}
	int ID;
	String Name;
	String resolution;
	LastState lastState;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public LastState getLastState() {
		return lastState;
	}
	public void setLastState(LastState laststate) {
		lastState = laststate;
	}
}
