package com.szaoto.ak10.sqlitedata;

public class IntfData {

    public int Id;     //ChID
    public int offsetX;//左上角坐标X
    public int offsetY;//左上角坐标Y
    public int width;//型号
    public int height; //箱体所在的interface的ID
    public int channelid;//所在视频源id
    public byte[] macaddress = new byte[6];
    public String name;
    
    public int ledid;
    
	public IntfData() {
		// TODO Auto-generated constructor stub
		super();
	}
}
