package com.szaoto.ak10.sqlitedata;

public class CbtData {

    public int Id;     //箱体ID
    public int offsetX;//左上角坐标X
    public int offsetY;//左上角坐标Y
    public int width;
    public int height;
    public String strModelType;//型号
    public int interfaceID; //箱体所在的interface的ID
    public int address; //连线图中的地址
	public int LEDid;
	public int addrshowid;//显示的地址，与建造师同步
    
	public CbtData() {
		super();
	}

}
