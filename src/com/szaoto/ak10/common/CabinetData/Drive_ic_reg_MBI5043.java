package com.szaoto.ak10.common.CabinetData;

public class Drive_ic_reg_MBI5043 {

	
	public Drive_ic_reg_MBI5043() {
		super();
		// TODO Auto-generated constructor stub
		//if (MBI5043 == nChipType)
		{
			//MBI5153 GCLK双沿采样	0：关闭， 1：开启
			bGCLKDoublesampling = false;
			//MBI5153 PWM模式选择	0: 16bit，1: 10bit
			nPWMMode = 0;
		}
	}
	private boolean bGCLKDoublesampling;//MBI5043 GCLK双沿采样	0：关闭， 1：开启
	private short nPWMMode;//MBI5043 PWM模式选择	0: 16bit，1: 10bit	
	public boolean isbGCLKDoublesampling() {
		return bGCLKDoublesampling;
	}
	public void setbGCLKDoublesampling(boolean bGCLKDoublesampling) {
		this.bGCLKDoublesampling = bGCLKDoublesampling;
	}
	public short getnPWMMode() {
		return nPWMMode;
	}
	public void setnPWMMode(short nPWMMode) {
		this.nPWMMode = nPWMMode;
	}
	
}
