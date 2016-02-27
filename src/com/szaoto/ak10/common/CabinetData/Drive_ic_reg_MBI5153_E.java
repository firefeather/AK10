package com.szaoto.ak10.common.CabinetData;

public class Drive_ic_reg_MBI5153_E {

	public Drive_ic_reg_MBI5153_E() {
		super();
		// TODO Auto-generated constructor stub
		
		//新MBI5153，聚积开放了MBI5153驱动芯片的第三组寄存器的设置.
		nReg1RHigh = 0xFF;//寄存器1红高字节,默认值0x9F2B
		
		//以下在初始化时需要判断赋值
//		if (_MBI5155 == nChipType)
//		{
//			//灰阶cfg1[7] 16，14，默认14bit
//			nReg1RLow = 0xAB; //寄存器1红低字节
//		} 
//		else//_MBI5153_E == m_nChipType
//		{
			//灰阶cfg1[7] 14，13，默认14bit
			nReg1RLow = 0x2B; //寄存器1红低字节
//		}
		
		nReg1GHigh = 0xFF;//寄存器1绿高字节,默认值0xDF2B
		nReg1GLow = 0x2B; //寄存器1绿低字节
		nReg1BHigh = 0xFF;//寄存器1蓝高字节,默认值0xDF2B
		nReg1BLow = 0x2B; //寄存器1蓝低字节

		nReg2RHigh = 0x0F;//寄存器2红高字节,默认值0x4600
		nReg2RLow = 0x00; //寄存器2红低字节
		nReg2GHigh = 0x06;//寄存器2绿高字节,默认值0x4500
		nReg2GLow = 0x00; //寄存器2绿低字节
		nReg2BHigh = 0x26;//寄存器2蓝高字节,默认值0x6500
		nReg2BLow = 0x00; //寄存器2蓝低字节

		nReg3RHigh = 0xC0;//寄存器3红高字节,默认值0xC003
		nReg3RLow = 0x03; //寄存器3红低字节
		nReg3GHigh = 0x50;//寄存器3绿高字节,默认值0x5003
		nReg3GLow = 0x03; //寄存器3绿低字节
		nReg3BHigh = 0x40;//寄存器3蓝高字节,默认值0x4003
		nReg3BLow = 0x03; //寄存器3蓝低字节
 
	}
	private int nReg1RHigh;//寄存器1红高字节
	private int nReg1RLow; //寄存器1红低字节
	private int nReg1GHigh;//寄存器1绿高字节
	private int nReg1GLow; //寄存器1绿低字节
	private int nReg1BHigh;//寄存器1蓝高字节
	private int nReg1BLow; //寄存器1蓝低字节

	private int nReg2RHigh;//寄存器2红高字节
	private int nReg2RLow; //寄存器2红低字节
	private int nReg2GHigh;//寄存器2绿高字节
	private int nReg2GLow; //寄存器2绿低字节
	private int nReg2BHigh;//寄存器2蓝高字节
	private int nReg2BLow; //寄存器2蓝低字节

	private int nReg3RHigh;//寄存器3红高字节
	private int nReg3RLow; //寄存器3红低字节
	private int nReg3GHigh;//寄存器3绿高字节
	private int nReg3GLow; //寄存器3绿低字节
	private int nReg3BHigh;//寄存器3蓝高字节
	private int nReg3BLow; //寄存器3蓝低字节
	
	
	public int getnReg1RHigh() {
		return nReg1RHigh;
	}
	public void setnReg1RHigh(int nReg1RHigh) {
		this.nReg1RHigh = nReg1RHigh;
	}
	public int getnReg1RLow() {
		return nReg1RLow;
	}
	public void setnReg1RLow(int nReg1RLow) {
		this.nReg1RLow = nReg1RLow;
	}
	public int getnReg1GHigh() {
		return nReg1GHigh;
	}
	public void setnReg1GHigh(int nReg1GHigh) {
		this.nReg1GHigh = nReg1GHigh;
	}
	public int getnReg1GLow() {
		return nReg1GLow;
	}
	public void setnReg1GLow(int nReg1GLow) {
		this.nReg1GLow = nReg1GLow;
	}
	public int getnReg1BHigh() {
		return nReg1BHigh;
	}
	public void setnReg1BHigh(int nReg1BHigh) {
		this.nReg1BHigh = nReg1BHigh;
	}
	public int getnReg1BLow() {
		return nReg1BLow;
	}
	public void setnReg1BLow(int nReg1BLow) {
		this.nReg1BLow = nReg1BLow;
	}
	public int getnReg2RHigh() {
		return nReg2RHigh;
	}
	public void setnReg2RHigh(int nReg2RHigh) {
		this.nReg2RHigh = nReg2RHigh;
	}
	public int getnReg2RLow() {
		return nReg2RLow;
	}
	public void setnReg2RLow(int nReg2RLow) {
		this.nReg2RLow = nReg2RLow;
	}
	public int getnReg2GHigh() {
		return nReg2GHigh;
	}
	public void setnReg2GHigh(int nReg2GHigh) {
		this.nReg2GHigh = nReg2GHigh;
	}
	public int getnReg2GLow() {
		return nReg2GLow;
	}
	public void setnReg2GLow(int nReg2GLow) {
		this.nReg2GLow = nReg2GLow;
	}
	public int getnReg2BHigh() {
		return nReg2BHigh;
	}
	public void setnReg2BHigh(int nReg2BHigh) {
		this.nReg2BHigh = nReg2BHigh;
	}
	public int getnReg2BLow() {
		return nReg2BLow;
	}
	public void setnReg2BLow(int nReg2BLow) {
		this.nReg2BLow = nReg2BLow;
	}
	public int getnReg3RHigh() {
		return nReg3RHigh;
	}
	public void setnReg3RHigh(int nReg3RHigh) {
		this.nReg3RHigh = nReg3RHigh;
	}
	public int getnReg3RLow() {
		return nReg3RLow;
	}
	public void setnReg3RLow(int nReg3RLow) {
		this.nReg3RLow = nReg3RLow;
	}
	public int getnReg3GHigh() {
		return nReg3GHigh;
	}
	public void setnReg3GHigh(int nReg3GHigh) {
		this.nReg3GHigh = nReg3GHigh;
	}
	public int getnReg3GLow() {
		return nReg3GLow;
	}
	public void setnReg3GLow(int nReg3GLow) {
		this.nReg3GLow = nReg3GLow;
	}
	public int getnReg3BHigh() {
		return nReg3BHigh;
	}
	public void setnReg3BHigh(int nReg3BHigh) {
		this.nReg3BHigh = nReg3BHigh;
	}
	public int getnReg3BLow() {
		return nReg3BLow;
	}
	public void setnReg3BLow(int nReg3BLow) {
		this.nReg3BLow = nReg3BLow;
	}

}
