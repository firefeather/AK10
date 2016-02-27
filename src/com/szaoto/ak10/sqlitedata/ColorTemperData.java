package com.szaoto.ak10.sqlitedata;

public class ColorTemperData {

	    public int nEnable;

	   //高亮场
	    public int nRed;					//灰度红色亮度值(0-255)
	    public int nGreen;					//灰度绿色亮度值(0-255)
	    public int nBlue;					//灰度蓝色亮度值(0-255)
	    public int nICRed;					//驱动芯片红色亮度值(0-255)		->电流
	    public int nICGreen;				//驱动芯片绿色亮度值(0-255)		->电流
	    public int nICBlue;				//驱动芯片蓝色亮度值(0-255)		->电流
		//低亮场
	    public int nRedLow;				//灰度红色亮度值(0-256)			->电流
	    public int nGreenLow;				//灰度绿色亮度值(0-256)			->电流
	    public int nBlueLow;				//灰度蓝色亮度值(0-256)			->电流
	    public int nICRedLow;				//驱动芯片红色亮度值(0-255)		->电流
	    public int nICGreenLow;			//驱动芯片绿色亮度值(0-255)		->电流
	    public int nICBlueLow;				//驱动芯片蓝色亮度值(0-255)		->电流

		//其它亮场处理（电流增益）
	    public int nICRed1;				//驱动芯片红色亮度值(0-255)		->电流
	    public int nICGreen1;				//驱动芯片绿色亮度值(0-255)		->电流
	    public int nICBlue1;				//驱动芯片蓝色亮度值(0-255)		->电流
		
	    public int nICRed2;				//驱动芯片红色亮度值(0-255)		->电流
	    public int nICGreen2;				//驱动芯片绿色亮度值(0-255)		->电流
	    public int nICBlue2;				//驱动芯片蓝色亮度值(0-255)		->电流


	    public int nICRed6;				//驱动芯片红色亮度值(0-255)		->电流
	    public int nICGreen6;				//驱动芯片绿色亮度值(0-255)		->电流
	    public int nICBlue6;				//驱动芯片蓝色亮度值(0-255)		->电流
		
	    public int nICRed7;				//驱动芯片红色亮度值(0-255)		->电流
	    public int nICGreen7;				//驱动芯片绿色亮度值(0-255)		->电流
	    public int nICBlue7;				//驱动芯片蓝色亮度值(0-255)		->电流
		
	    public int nICRed8;				//驱动芯片红色亮度值(0-255)		->电流
	    public int nICGreen8;				//驱动芯片绿色亮度值(0-255)		->电流
	    public int nICBlue8;				//驱动芯片蓝色亮度值(0-255)		->电流
		
	    public int nICRed9;				//驱动芯片红色亮度值(0-255)		->电流
	    public int nICGreen9;				//驱动芯片绿色亮度值(0-255)		->电流
	    public int nICBlue9;				//驱动芯片蓝色亮度值(0-255)		->电流

	    public int m_bGainEnable_0;		//各阶段电流增益（/2-1/256）使能	1：使能，：禁用
	    public int m_bGainEnable_1;		//各阶段电流增益（/2-1/256）使能	1：使能，：禁用
	    public int m_bGainEnable_2;		//各阶段电流增益（/2-1/256）使能	1：使能，：禁用
	    public int m_bGainEnable_3;		//各阶段电流增益（/2-1/256）使能	1：使能，：禁用
	    public int m_bGainEnable_4;		//各阶段电流增益（/2-1/256）使能	1：使能，：禁用
	    public int m_bGainEnable_5;		//各阶段电流增益（/2-1/256）使能	1：使能，：禁用
	    public int m_bGainEnable_6;		//各阶段电流增益（/2-1/256）使能	1：使能，：禁用
	    public int m_bGainEnable_7;		//各阶段电流增益（/2-1/256）使能	1：使能，：禁用
		
		
	    public int m_bResEnable_0;		//各阶段（/2-1/256）电阻使能	1：使能，：禁用
	    public int m_bResEnable_1;		//各阶段（/2-1/256）电阻使能	1：使能，：禁用
	    public int m_bResEnable_2;		//各阶段（/2-1/256）电阻使能	1：使能，：禁用
	    public int m_bResEnable_3;		//各阶段（/2-1/256）电阻使能	1：使能，：禁用
	    public int m_bResEnable_4;		//各阶段（/2-1/256）电阻使能	1：使能，：禁用
	    public int m_bResEnable_5;		//各阶段（/2-1/256）电阻使能	1：使能，：禁用
	    public int m_bResEnable_6;		//各阶段（/2-1/256）电阻使能	1：使能，：禁用
	    public int m_bResEnable_7;		//各阶段（/2-1/256）电阻使能	1：使能，：禁用

	public ColorTemperData() {
		// TODO Auto-generated constructor stub
	}

}
