/*
   * 文件名 Drive_ic_reg.java
   * 包含类名列表com.szaoto.ak10.common
   * 版本信息，版本号
   * 创建日期2014年4月1日下午7:28:37
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.common.CabinetData;

/*
 * 类名Drive_ic_reg
 * 作者 liangdb
 * 主要功能 _TLC5958 FC 寄存器信息
 * 创建日期2014年4月1日
 * 修改者，修改日期，修改内容
 */
public class Drive_ic_reg {

	/**
	 * @return the nBright
	 */
	public Drive_ic_reg()
	{
			nBright = 4;//全局亮度调节
			nLgse_R = 0;//红色低灰增强	
			nLgse_G = 0;//绿色低灰增强
			nLgse_B = 0;//蓝色低灰增强
			nGdly_Enable = 1;//输出通道延迟使能
			nTD_Delay = 1;//输入数据延迟
			nLodvth = 1;//开路检测电压设定
			//reg2
			nGlobal_Lgse = 0;//全局低灰增强
			nPVM_Mode = 0;//打散模式
			nEMI_R = 0;//红色EMI削减
			nEMI_G = 0;//绿色EMI削减
			nEMI_B = 0;//蓝色EMI削减	
			nPre_Charge = 0;//预充电模式
			
			//_MBI5152/_MBI5153
			//reg1
			nPre_Charge1 = 0;
			nPwm_Count_Mode = 0;
			nGray_Mode = 0;
			nEnable_GCLK = 0;

			//reg2
			nDouble_RefreseRate = 0;
			nVoltage = 0;
			nIC_Recognition = 1;
			nAdjust_Red = 0;
			nAdjust_Green = 0;
			nAdjust_Blue = 0;
			nImhl_DoNotStretch = 0;

			sDrive_ic_reg_MBI5153_E = new Drive_ic_reg_MBI5153_E();
			sDrive_ic_reg_MBI5043 = new Drive_ic_reg_MBI5043();
			sDrive_ic_reg_MBI5155 = new Drive_ic_reg_MBI5155();
	}
	public int getnBright() {
		return nBright;
	}
	/**
	 * @param nBright the nBright to set
	 */
	public void setnBright(int nBright) {
		this.nBright = nBright;
	}
	/**
	 * @return the nLgse_R
	 */
	public int getnLgse_R() {
		return nLgse_R;
	}
	/**
	 * @param nLgse_R the nLgse_R to set
	 */
	public void setnLgse_R(int nLgse_R) {
		this.nLgse_R = nLgse_R;
	}
	/**
	 * @return the nLgse_G
	 */
	public int getnLgse_G() {
		return nLgse_G;
	}
	/**
	 * @param nLgse_G the nLgse_G to set
	 */
	public void setnLgse_G(int nLgse_G) {
		this.nLgse_G = nLgse_G;
	}
	/**
	 * @return the nLgse_B
	 */
	public int getnLgse_B() {
		return nLgse_B;
	}
	/**
	 * @param nLgse_B the nLgse_B to set
	 */
	public void setnLgse_B(int nLgse_B) {
		this.nLgse_B = nLgse_B;
	}
	/**
	 * @return the nGdly_Enable
	 */
	public int getnGdly_Enable() {
		return nGdly_Enable;
	}
	/**
	 * @param nGdly_Enable the nGdly_Enable to set
	 */
	public void setnGdly_Enable(int nGdly_Enable) {
		this.nGdly_Enable = nGdly_Enable;
	}
	/**
	 * @return the nTD_Delay
	 */
	public int getnTD_Delay() {
		return nTD_Delay;
	}
	/**
	 * @param nTD_Delay the nTD_Delay to set
	 */
	public void setnTD_Delay(int nTD_Delay) {
		this.nTD_Delay = nTD_Delay;
	}
	/**
	 * @return the nLodvth
	 */
	public int getnLodvth() {
		return nLodvth;
	}
	/**
	 * @param nLodvth the nLodvth to set
	 */
	public void setnLodvth(int nLodvth) {
		this.nLodvth = nLodvth;
	}
	/**
	 * @return the nGlobal_Lgse
	 */
	public int getnGlobal_Lgse() {
		return nGlobal_Lgse;
	}
	/**
	 * @param nGlobal_Lgse the nGlobal_Lgse to set
	 */
	public void setnGlobal_Lgse(int nGlobal_Lgse) {
		this.nGlobal_Lgse = nGlobal_Lgse;
	}
	/**
	 * @return the nPVM_Mode
	 */
	public int getnPVM_Mode() {
		return nPVM_Mode;
	}
	/**
	 * @param nPVM_Mode the nPVM_Mode to set
	 */
	public void setnPVM_Mode(int nPVM_Mode) {
		this.nPVM_Mode = nPVM_Mode;
	}
	/**
	 * @return the nEMI_R
	 */
	public int getnEMI_R() {
		return nEMI_R;
	}
	/**
	 * @param nEMI_R the nEMI_R to set
	 */
	public void setnEMI_R(int nEMI_R) {
		this.nEMI_R = nEMI_R;
	}
	/**
	 * @return the nEMI_G
	 */
	public int getnEMI_G() {
		return nEMI_G;
	}
	/**
	 * @param nEMI_G the nEMI_G to set
	 */
	public void setnEMI_G(int nEMI_G) {
		this.nEMI_G = nEMI_G;
	}
	/**
	 * @return the nEMI_B
	 */
	public int getnEMI_B() {
		return nEMI_B;
	}
	/**
	 * @param nEMI_B the nEMI_B to set
	 */
	public void setnEMI_B(int nEMI_B) {
		this.nEMI_B = nEMI_B;
	}
	/**
	 * @return the nPre_Charge
	 */
	public int getnPre_Charge() {
		return nPre_Charge;
	}
	/**
	 * @param nPre_Charge the nPre_Charge to set
	 */
	public void setnPre_Charge(int nPre_Charge) {
		this.nPre_Charge = nPre_Charge;
	}
	public int getnPre_Charge1() {
		return nPre_Charge1;
	}
	public void setnPre_Charge1(int nPre_Charge1) {
		this.nPre_Charge1 = nPre_Charge1;
	}
	public int getnPwm_Count_Mode() {
		return nPwm_Count_Mode;
	}
	public void setnPwm_Count_Mode(int nPwm_Count_Mode) {
		this.nPwm_Count_Mode = nPwm_Count_Mode;
	}
	public int getnGray_Mode() {
		return nGray_Mode;
	}
	public void setnGray_Mode(int nGray_Mode) {
		this.nGray_Mode = nGray_Mode;
	}
	public int getnEnable_GCLK() {
		return nEnable_GCLK;
	}
	public void setnEnable_GCLK(int nEnable_GCLK) {
		this.nEnable_GCLK = nEnable_GCLK;
	}
	public int getnDouble_RefreseRate() {
		return nDouble_RefreseRate;
	}
	public void setnDouble_RefreseRate(int nDouble_RefreseRate) {
		this.nDouble_RefreseRate = nDouble_RefreseRate;
	}
	public int getnVoltage() {
		return nVoltage;
	}
	public void setnVoltage(int nVoltage) {
		this.nVoltage = nVoltage;
	}
	public int getnIC_Recognition() {
		return nIC_Recognition;
	}
	public void setnIC_Recognition(int nIC_Recognition) {
		this.nIC_Recognition = nIC_Recognition;
	}
	public int getnAdjust_Red() {
		return nAdjust_Red;
	}
	public void setnAdjust_Red(int nAdjust_Red) {
		this.nAdjust_Red = nAdjust_Red;
	}
	public int getnAdjust_Green() {
		return nAdjust_Green;
	}
	public void setnAdjust_Green(int nAdjust_Green) {
		this.nAdjust_Green = nAdjust_Green;
	}
	public int getnAdjust_Blue() {
		return nAdjust_Blue;
	}
	public void setnAdjust_Blue(int nAdjust_Blue) {
		this.nAdjust_Blue = nAdjust_Blue;
	}
	public int getnImhl_DoNotStretch() {
		return nImhl_DoNotStretch;
	}
	public void setnImhl_DoNotStretch(int nImhl_DoNotStretch) {
		this.nImhl_DoNotStretch = nImhl_DoNotStretch;
	}

	public Drive_ic_reg_MBI5153_E getsDrive_ic_reg_MBI5153_E() {
		return sDrive_ic_reg_MBI5153_E;
	}
	public void setsDrive_ic_reg_MBI5153_E(
			Drive_ic_reg_MBI5153_E sDrive_ic_reg_MBI5153_E) {
		this.sDrive_ic_reg_MBI5153_E = sDrive_ic_reg_MBI5153_E;
	}
	public Drive_ic_reg_MBI5043 getsDrive_ic_reg_MBI5043() {
		return sDrive_ic_reg_MBI5043;
	}
	public void setsDrive_ic_reg_MBI5043(Drive_ic_reg_MBI5043 sDrive_ic_reg_MBI5043) {
		this.sDrive_ic_reg_MBI5043 = sDrive_ic_reg_MBI5043;
	}
	public Drive_ic_reg_MBI5155 getsDrive_ic_reg_MBI5155() {
		return sDrive_ic_reg_MBI5155;
	}
	public void setsDrive_ic_reg_MBI5155(Drive_ic_reg_MBI5155 sDrive_ic_reg_MBI5155) {
		this.sDrive_ic_reg_MBI5155 = sDrive_ic_reg_MBI5155;
	}
	
	/**
	 * 
	 */

	
	//reg1
	private int nBright;//全局亮度调节
	private int nLgse_R;//红色低灰增强
	private int nLgse_G;//绿色低灰增强
	private int nLgse_B;//蓝色低灰增强
	private int nGdly_Enable;//输出通道延迟使能
	private int nTD_Delay;//输入数据延迟
	private int nLodvth;//开路检测电压设定

	//reg2
	private int nGlobal_Lgse;//全局低灰增强
	private int nPVM_Mode;//打散模式
	private int nEMI_R;//红色EMI削减
	private int nEMI_G;//绿色EMI削减
	private int nEMI_B;//蓝色EMI削减
	private int nPre_Charge;//预充电模式

	//_MBI5152/_MBI5153
	//reg1
	private int nPre_Charge1;		//预充电模式：0关闭，1开启
	private int nPwm_Count_Mode;	//PWM计数模式：0正数，1倒数
	private int nGray_Mode;		//灰阶模式：MBI5152：0=16bit，1=14bit	MBI5153：0=14bit，1=13bit
	private int nEnable_GCLK;		//GCLK倍频：0禁用，1使能
	
	//reg2
	private int nDouble_RefreseRate;//双倍刷新率：0关闭，1开启
	private int nVoltage;			//开路检测电压：00: 0.3V，01: 0.4V，10: 0.5V，11: 0.6V
	private int nIC_Recognition;	//红绿蓝IC识别：01红色，10绿色，11蓝色
	private int nAdjust_Red;		//首行偏暗调节（红色）：000: 0 ns，100: 18ns，001: 6 ns，101: 21ns，010: 9 ns，110: 27ns，011: 15 ns，111: 33ns
	private int nAdjust_Green;		//首行偏暗调节（绿色）：000: 0 ns，100: 18ns，001: 6 ns，101: 21ns，010: 9 ns，110: 27ns，011: 15 ns，111: 33ns
	private int nAdjust_Blue;		//首行偏暗调节（蓝色）：000: 0 ns，100: 18ns，001: 6 ns，101: 21ns，010: 9 ns，110: 27ns，011: 15 ns，111: 33ns
	private int nImhl_DoNotStretch;//倒数模式高电平不延伸：0关闭，1开启
	
	private Drive_ic_reg_MBI5153_E sDrive_ic_reg_MBI5153_E;//最新_MBI5153，聚积开放了MBI5153驱动芯片的第三组寄存器的设置
	private Drive_ic_reg_MBI5043 sDrive_ic_reg_MBI5043;//MBI5043
	private Drive_ic_reg_MBI5155 sDrive_ic_reg_MBI5155;//最新_MBI5155渐变过渡优化，在寄存器5153保留的基础上增加的字段
	
	
}
