package com.szaoto.ak10.sqlitedata;

public class IntfData {

    public int Id;     //ChID
    public int offsetX;//���Ͻ�����X
    public int offsetY;//���Ͻ�����Y
    public int width;//�ͺ�
    public int height; //�������ڵ�interface��ID
    public int channelid;//������ƵԴid
    public byte[] macaddress = new byte[6];
    public String name;
    
    public int ledid;
    
	public IntfData() {
		// TODO Auto-generated constructor stub
		super();
	}
}