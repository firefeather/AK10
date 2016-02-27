package com.szaoto.ak10.common;


//最后一次监控报警状态（发送邮件判断使用）
public class LastMonitorAlarm {
	boolean bLastTemperatureAlarm;
	boolean bLastHumidityAlarm;
	boolean bLastSmogAlarm;
	boolean bLastFanAlarm[];
	boolean bLastPowerVolAlarm[];
	boolean bLastCapacityFactorAlarm;
	boolean bLastPointDetectAlarm;
	boolean bConnectStatusAlarm;
	boolean bBackUpStatusAlarm;
	LastMonitorAlarm()
	{
		bLastFanAlarm = new boolean[2];
		bLastPowerVolAlarm = new boolean[5];
	}
}
