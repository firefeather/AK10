/*
   * �ļ��� ColourRGB.java
   * ���������б�com.szaoto.ak10.common
   * �汾��Ϣ���汾��
   * ��������2013��12��28������9:45:13
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.common.Display;

public class ModuleData {
	public ModuleData()
	{								
		id = -1;
		nBright = -1;
		nColourTem = -1;
		nSaturation = -1;
		nContrast = -1;
		nChannel = -1;
	}
	int id; 
	short nBright;
	short nColourTem;
	short nSaturation;
	short nContrast;
	short nChannel;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public short getnBright() {
		return nBright;
	}
	public void setnBright(short nBright) {
		this.nBright = nBright;
	}
	public short getnColourTem() {
		return nColourTem;
	}
	public void setnColourTem(short nColourTem) {
		this.nColourTem = nColourTem;
	}
	public short getnSaturation() {
		return nSaturation;
	}
	public void setnSaturation(short nSaturation) {
		this.nSaturation = nSaturation;
	}
	public short getnContrast() {
		return nContrast;
	}
	public void setnContrast(short nContrast) {
		this.nContrast = nContrast;
	}
	public short getnChannel() {
		return nChannel;
	}
	public void setnChannel(short nChannel) {
		this.nChannel = nChannel;
	}
	
}

