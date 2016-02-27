#ifndef LEDCTRL_DEF
#define LEDCTRL_DEF

#include <stdio.h>
#include <stdlib.h>
#include "StructSingleScanCard.h"
#include <jni.h>
#include <vector>
#include <string>
#include <math.h>
#include <map>
#include "DataStructDef_E.h"

using namespace std;

typedef unsigned char	Uint8;
typedef unsigned short	Uint16;
typedef unsigned int	Uint32;
typedef unsigned long	Uint64;

typedef int BOOL,HWND,HANDLE,DWORD,TRUE;
typedef struct tagRECT
{
	long    left;
	long    top;
	long    right;
	long    bottom;
}RECT;
typedef struct tagPOINT
{
	long  x;
	long  y;
}POINT;

//#define extern "C" __declspec(dllexport)

#pragma region //宏定义
//////////////////////////////////////////////////////////////////////////
//////////////////////////////////宏定义//////////////////////////////////
//////////////////////////////////////////////////////////////////////////

#define ATLVC_COMM_TYPE_COM                     0  //ATIVC通讯方式：串口
#define ATLVC_COMM_TYPE_USB                     1  //ATIVC通讯方式：USB
#define ATLVC_COMM_TYPE_NET                     2  //ATIVC通讯方式：NET
#define ATLVC_COMM_TYPE_SOCKET                  3  //ATIVC通讯方式：Socket

#define AUTO_CORRECTION_PATTERN_ALL             0  //自动校正显示模式：ALL
#define AUTO_CORRECTION_PATTERN_ODD             1  //自动校正显示模式：Odd
#define AUTO_CORRECTION_PATTERN_EVEN            2  //自动校正显示模式：Even
#define AUTO_CORRECTION_PATTERN_DOTS            3  //自动校正显示模式：Module Dots
#define AUTO_CORRECTION_PATTERN_SPACE           4  //自动校正显示模式：Spaced Pixel

#define RESOLUTION_NUMBER						13//分辨率个数
#define SCAN_CARD_WIDTH_MULTIPLE				16//扫描卡宽度的整数倍
#define SCAN_CARD_WIDTH_HIGH_MIN				1//扫描卡宽度和高度的最小值
#define MODULE_WIDTH_HIGH_MIN					1//模组宽度和高度的最小值
#define MODULE_HOR_VER_NUM_MIN					1//模组水平垂直个数最小值
#define MODULE_HOR_VER_NUM_MAX					16//模组水平垂直个数最大值
#define MODULE_PER_CARD_MIN						1//一张卡所带的模组个数最小值
#define MODULE_PER_CARD_MAX						64//一张卡所带的模组个数最大值
#define SCAN_CARD_SECTION_ROW_NUM_MAX			32//扫描卡每区行数最大值
#define SCAN_CARD_SECTION_ROW_NUM_MIN			1//扫描卡每区行数最小值
#define SCAN_CARD_SECTION_NUM_MAX				16//扫描卡区数最大值
#define SCAN_CARD_SECTION_NUM_MIN				1//扫描卡区数最小值 
#define START_X_Y_MIN							0//起始XY最小值
#define START_X_Y_MAX							32767//起始XY最大值
#define COLOR_TEMP_NUM                          8//色温个数
#define COLOR_TEMP_NUM_EX                       71//色温个数
#define COLOR_TEMP_NUM_TOTAL                    79//色温个数
#define BRIGHTFIELD_STEP_NUM					8//亮场阶段数 
#define COM_DALEY_TIME							100//串口延时时间，USB转串口时广播无应答需延时，否则收不要应答
#define LED_NUM_CONTROLED_BY_EACH_CHIP			16//一个芯片控制的LED灯的个数
#define POWER_VOL_NUM							5//监控电源电压线路个数
#define FAN_NUM									2//风扇个数
#define SCAN_LINE_STATE_NUM							16//扫描线状态监控个数
#define REFRESH_RATE_MIN						60//刷新率最小值
#define RELAY_NUMBER_MAX						4//继电器最大个数
#define COM_ANSWER_DELAY						300//串口应答超时默认值

//#define SCANCARD_MODULE_WIDTH_HIGH_MAX					 128      //模组宽度和高度的最大值
#define SCANCARD_MODULE_WIDTH_HIGH_MAX					 256      //模组宽度和高度的最大值
//hh
#define SCANCARD_ADRESS_MAX								65535		//扫描卡地址最大值
///end

#define SCANCARD_WIDTH_HIGH_MAX							512      //扫描卡宽度和高度的最大值
#define SCANCARD_LEDSIZE_CONTROLED_BY_EACH_CHIP			16//一个芯片控制的LED灯的个数
#define SCANCARD_IN_CABINET_COUNT_MAX					4//箱体中扫描卡的最大个数
#define SCANCARD_CONFIG_IC_TIME_MAX						 2047//芯片间隔时间最大值
#define SCANCARD_REFRESH_RATE_MIN						60//刷新率最小值
#define SCANCARD_MHZ									150//扫描卡150MHz
#define SCANCARD_MHZ_MBI5153_E						125//暂时考虑寄存器芯片5153的情况，其他的仍然为150MHZ
#define MBI5153_FRAME 60 //帧率暂时固定60

#define SCANCARD_MODULE_HOR_VER_NUM_MAX					16 //模组水平垂直个数最大值
#define SCANCARD_MODULE_PER_CARD_MAX					64  //一张卡所带的模组个数最大值
#define GAMMA_CALIBRATION_COLOR_DEPTH					16//校正、伽马颜色灰度级数
#define SCANCARD_SECTION_PER_CARD_MAX					64  //一张卡所带的区个数最大值
#define SCANCARD_SAVE_INIT_SIZE							9  //扫描卡参数保存和初始化命令个数

#define MONITOR_TEMPATURE_MAX							123//温度最大值
#define MONITOR_TEMPATURE_MIN							-40//温度最小值

#define CALIBRATION_MODULE_SERIAL_LEN							 16//模组序列号长度
#define CALIBRATION_ORDER_NUM_LEN							 7//订单号长度
#define CALIBRATION_DISPLAY_NUM_LEN							 2//屏体号长度
#define CALIBRATION_CABINET_NUM_LEN							 4//箱体ID号长度
#define CALIBRATION_MODULE_NUM_LEN							 3//模组号长度
#define CALIBRATION_LINE_COEFF_MAX							 8//模组亮暗线边界系数个数								

#define CALIBRATION_CHROMINANCE_PARA_SIZE					9//色度校正参数个数
#define CALIBRATION_BRIGHTNESS_PARA_SIZE					3//亮度校正参数个数
#define FAN_ROTATION_NUM_MAX								8//风扇转速监测最大个数
#define DEAD_LEAD_NUM_MAX									65535//逐点检测坏灯的最大个数


#define CL_SEND_EFFECT_DATA_SIZE				16//发送包数据域长度
#define CL_SEND_PACK_SIZE						28//发送/接收包包长度
#define CL_SEND_PACK_SIZE_V1                    32



#define CL_MAX_BUF_NUMBER						65535	//串口接收缓冲区最大值
#define	CL_COM_MAX_NUM							32//最大串口号
#define CL_GET_CALIBRATION_DATA_SIZE			256//回读校正数据，一次返回的字节数

#define CHANNEL_PORT_MAX								4	//单通道最大等待队列数


#define ATLVC_1P0	1		//AK6通用ATLVC采集卡1的P0口
#define	ATLVC_1P1	2		//AK6通用ATLVC采集卡1的P1口
#define	ATLVC_2P0	3		//AK6通用ATLVC采集卡2的P0口
#define	ATLVC_2P1	4		//AK6通用ATLVC采集卡2的P1口

#define	ATLVC_EX_1P0	101		//AK6升级ATLVC采集卡1的P0口
#define	ATLVC_EX_1P1	102		//AK6升级ATLVC采集卡1的P1口
#define	ATLVC_EX_1P2	103		//AK6升级ATLVC采集卡1的P2口
#define	ATLVC_EX_1P3	104		//AK6升级ATLVC采集卡1的P3口
#define	ATLVC_EX_2P0	105		//AK6升级ATLVC采集卡2的P0口
#define	ATLVC_EX_2P1	106		//AK6升级ATLVC采集卡2的P1口
#define	ATLVC_EX_2P2	107		//AK6升级ATLVC采集卡2的P2口
#define	ATLVC_EX_2P3	108		//AK6升级ATLVC采集卡2的P3口

#define	ATLVC_EX_P0_P1	109		//AK6升级ATLVC打开P0,P1口
#define	ATLVC_EX_P0_P2	110		//AK6升级ATLVC打开P0,P2口
#define	ATLVC_EX_P0_P3	111		//AK6升级ATLVC打开P0,P3口
#define	ATLVC_EX_P1_P2	112		//AK6升级ATLVC打开P1,P2口
#define	ATLVC_EX_P1_P3	113		//AK6升级ATLVC打开P1,P3口
#define	ATLVC_EX_P2_P3	114		//AK6升级ATLVC打开P2,P3口

#define CORE_DLL					"LedCtrl.dll"				        //核心动态库
//#define CORE_DLL					"AOTOCorrection.dll"				        //核心动态库
#define DATABASE_CFG				"DataBase"							//数据库存放文件夹
#define TEXTBASE_DIR				"TextBase"							//校正数据存放文件夹（txt）
#define	CABINET_SERIES_NAME			"CabinetSeries.cbs"		//箱体系列文件名称
#define	CABINET_NAME				"Cabinet.cbt"			//箱体文件名称
#define	CABINET_FOLDER				"Cabinet\\"								//箱体文件夹
#define CABINET_EXTENSION				".cbt"								//箱体XML文件后缀名
#define SYS_CFG_FILE				"SysConfig.xml"								//屏体参数全局变量文件

#define MOD_LINE_COEFF_EXTENSION				".lc"								//模组亮暗线系数文件后缀名
#define MOD_CUSTOM_EXTENSION				".tcd"								//模组自定义文件后缀名
#define MOD_TUNING_EXTENSION				".mfc"								//模组微调文件后缀名
#define MOD_CHROMINANCE_EXTENSION				".ccd"								//色度校正文件后缀名
#define MOD_CHROMINANCE_EXTENSION_DISPLAY				".mcd"								//全屏校正色度校正文件后缀名
#define MOD_BRIGHTNESS_EXTENSION				".bcd"								//亮度校正文件后缀名
#define MOD_BRIGHTNESS_READ_EXTENSION				".bcr"								//回读亮度校正数据后生成亮度校正文件的后缀名
#define DATABASE_EXTENSION				".mdb"								//数据库后缀名
#define CABINET_ID_EXTENSION				".cid"								//箱体ID文件后缀名

#define MONITOR_DATA_DEFAULT			-10000	//监控数据的默认值
#define MONITOR_COUNT					23		//监控项个数

#define TEMPERATUREFLAG        0x00000001
#define HUMIDITYFLAG           0x00000002
#define SMOGFLAG			   0x00000004
#define FANSTATEFLAG1          0x00000008
#define FANSTATEFLAG2          0x00000010
#define POWERVOLFLAG1          0x00000020
#define POWERVOLFLAG2          0x00000040
#define POWERVOLFLAG3          0x00000080
#define POWERVOLFLAG4          0x00000100
#define POWERVOLFLAG5          0x00000200
#define CAPACITYFACTORFLAG     0x00000400//功率
#define LEDPOINTDETECTFLAG     0x00000800//逐点检测
#define VERSIONFLAG			   0x00004000
#define FANROTATION            0x00001000//风扇转速
#define NETWEBERRO             0x00002000//网络错包数
#define FANROTATION1           0x00008000//第1路风扇转速
#define FANROTATION2           0x00010000//第2路风扇转速
#define FANROTATION3           0x00020000//第3路风扇转速
#define FANROTATION4           0x00040000//第4路风扇转速
#define FANROTATION5           0x00080000//第5路风扇转速
#define FANROTATION6           0x00100000//第6路风扇转速
#define FANROTATION7           0x00200000//第7路风扇转速
#define FANROTATION8           0x00400000//第8路风扇转速

#define POWERSTATE             0x00800000//电源状态


#define ST_OK 0
#define ST_ERROR 1
#define ST_NA 2

#define STRING_NA			"N/A"	//监控无数据显示

#define INDEX_TEMPERATURE 0
#define INDEX_HUMIDITY    1
#define INDEX_SMOKE       2
#define INDEX_LFAN        3
#define INDEX_RFAN        4
#define INDEX_VOL1        5
#define INDEX_VOL2        6
#define INDEX_VOL3        7
#define INDEX_VOL4        8
#define INDEX_VOL5        9
#define INDEX_POWER       10
#define INDEX_BADLEDS     11
#define INDEX_FANAUTO1     12
#define INDEX_FANAUTO2     13
#define INDEX_FANAUTO3     14
#define INDEX_FANAUTO4     15
#define INDEX_FANAUTO5     16
#define INDEX_FANAUTO6     17
#define INDEX_FANAUTO7     18
#define INDEX_FANAUTO8     19
#define INDEX_NETWEBERROR  20
#define INDEX_VERSION     21
//hh
#define INDEX_POWERSTATE     22



#define MONITORSCREENPANEL_COUNT					12		//屏幕监控项列表项
#define SCREENPANEL_INDEX_TEMPERATURE	1 //温度
#define SCREENPANEL_INDEX_HUMIDITY		2 //湿度
#define SCREENPANEL_INDEX_SMOG			9 //烟感


#pragma endregion

#pragma region //注册消息
//////////////////////////////////////////////////////////////////////////
////////////////////////////////注册消息//////////////////////////////////
//////////////////////////////////////////////////////////////////////////
#define WM_MSG_								WM_USER + 100
#define UM_UPDATA							WM_USER + 101
#define UM_AK6ATLVCSET						WM_USER + 102
#define UM_AK6UPDATEATLVCSET				WM_USER + 103
#define UM_TRAYMSG							WM_USER + 104 //自定义托盘消息	
#define UM_CREATECABINETTOTREE              WM_USER + 105 //创建箱体序列表
#define UM_SHOW_CARDGRAYGLEVEL_DLG			WM_USER + 106 //打开灰度级别设置窗体
#define UM_SHOW_SETDEDUCTBIT_DLG			WM_USER + 107 //打开减去色温设置窗体
#define UM_GETCALIBRATIONID					WM_USER + 108 //获取箱体校正ID
#define UM_SHOW_VERSIONSWITCH_DLG			WM_USER + 109 //打开特殊版本切换窗体
#define UM_BOOT_LOGO_SET                    WM_USER + 110

#pragma endregion

#pragma region //错误代码
//////////////////////////////////////////////////////////////////////////
///////////////////////////错误代码（信息）///////////////////////////////
//////////////////////////////////////////////////////////////////////////

////////////////////////////////通用//////////////////////////////////////
#define BLL_SUCCESS							1	//操作成功
#define BLL_INTERRUPT						2	//操作中断
#define BLL_NO_ERROR						0	//没有错误
#define BLL_CONNECT_ERROR					-1	//通讯信道关闭或打开错误
#define BLL_INIT_NO							-2  //没有初始化
#define BLL_SEND_FAIL						-3	//发送失败
#define BLL_RECEIVE_FAIL					-4	//应答失败
#define BLL_QUEUE_FULL						-5	//队列满
#define BLL_SUCTIONPARAMETER_ERROR			-6	//入参错误
#define BLL_COM_NOT_EXIST					-7	//串口不存在
#define BLL_COM_OCCUPY						-8	//串口占用
#define BLL_NO_CHANNEL						-9	//未关联信道

#define BLL_READ_ERROR						-10	//读取文件错误
#define BLL_FILEFORMAT_ERROR				-11	//文件格式错误
#define BLL_CREATEFILE_ERROR				-12	//创建文件错误
#define BLL_MODIFYFILE_ERROR				-13	//更新文件错误
#define BLL_DELETEFILE_ERROR				-14	//删除文件错误
#define BLL_FILE_NOT_EXIT_ERROR				-15 //文件不存在
#define BLL_FILE_OPEN_ERROR					-16 //文件打开失败

////////////////////////////////连线图////////////////////////////////////


///////////////////////////////箱体参数///////////////////////////////////


////////////////////////////////校正//////////////////////////////////////
#define BLL_CALIBRATION_BRIGHTNESS_FAIL			-300	//亮度校正数据发送失败
#define BLL_CALIBRATION_CHROMINANCE_FAIL		-301	//色度校正数据发送失败
#define BLL_CALIBRATION_READ_BDATABASE_FAIL		-302	//亮度校正数据库数据读取失败
#define BLL_CALIBRATION_READ_CDATABASE_FAIL		-303	//色度校正数据库数据读取失败
#define BLL_CALIBRATION_BCD_NOTEXIT				-304	//BCD文件不存在
#define BLL_CALIBRATION_CCD_NOTEXIT				-305	//CCD文件不存在
#define BLL_CALIBRATION_READ_BCD_FAIL			-306	//BCD文件读取失败
#define BLL_CALIBRATION_READ_CCD_FAIL			-307	//CCD文件读取失败
#define BLL_CALIBRATION_WRITE2MODUL_FAIL		-308	//写扫描卡校正数据到模组失败
#define BLL_CALIBRATION_WRITE2SCANDCARD_FAIL	-309	//读模组校正数据到扫描卡失败
#define BLL_CALIBRATION_READMODULNO_FAIL		-310	//读模组序列号失败

#define BLL_PHOTO_LARGE_THAN_CONNECTION			-311	//图片分辨率必须大于连线图的分辨率
#define BLL_CONNECTION_ERROR_LOADED_ROW			-312	//连线图中的带载行大于采集卡最大带载行
#define BLL_CONNECTION_ERROR_LOADED_COL			-313	//连线图中的带载列大于采集卡最大带载列
#define BLL_NO_PHOTO_SEND_FOR_REA_LVIR			-314	//异型屏不支持图片发送功能

#define BLL_CALIBRATION_MCD_NOTEXIT				-315	//MCD文件不存在
#define BLL_CALIBRATION_READ_MCD_FAIL			-316	//MCD文件读取失败

#define BLL_TXT_CHROMINANCE_NANE_ERROR			-317	//色度TXT数据分割文件名称错误，e.g.:C0001_0002_0003_0004.txt
#define BLL_TXT_BRIGHTNESS_NANE_ERROR			-318	//亮度TXT数据分割文件名称错误，e.g.:L0001_0002_0003_0004.txt
#define BLL_CONNECTION_NO_CABINET				-319	//请在连线图中添加此型号箱体
#define BLL_TXT_LENGTH_ERROR					-320	//TXT文件长度和箱体像素点数不一致


#define CL_ANSWER_ERROE_PACK_HEAD_1					-500		//应答包包头1错误
#define CL_ANSWER_ERROE_PACK_HEAD_2					-501		//应答包包头2错误
#define CL_ANSWER_ERROE_DEST_ADDRESS				-502		//应答包目的地址错误
#define CL_ANSWER_ERROE_SOUR_ADDRESS				-503		//应答包源地址错误
#define CL_ANSWER_ERROE_CHECKSUM					-504		//应答包校验码错误
#define CL_ANSWER_ERROE_PACK_TYPE					-505		//应答包包类型错误
#define CL_ANSWER_ERROE_CONNECT						-506		//应答包内容错误
#define CL_ANSWER_ERROR_NO_DATA						-507		//无数据返回，请检查硬件通信

#define APL_DB_OPEN_ERROR							-700		//数据库打开失败
#define APL_SQL_EXEC_ERROR							-701		//SQL语言执行失败
#define APL_CONNECT_DB_ERROR						-702		//连接数据库失败
#define APL_DB_CLOSE_ERROR							-703		//数据库关闭失败
#define APL_SLEC_ALL_TABLE_ERROR					-704		//查询所有表的名称失败
#define APL_DB_TABLE_NO_DATA						-705		//数据库表中无数据
#define API_MDB_NO_TEXIT							-706		//MDB数据库不存在
#define API_DB_NO_TABLE								-707		//数据库中无该模组数据表
#define APL_CREATE_DB_ERROR							-708		//生成数据库失败

#define UI_CABINET_ERROE_NO_ID							-900		//箱体ID不存在
#define UI_CABINET_ERROR_NAME_REPEAT					-901		//箱体库中箱体名称不能重复
#define	UI_CABINET_ERROR_MODIFY_NAME					-902		//箱体文件中无此箱体ID
#define UI_SERIES_ERROE_NO_ID							-903		//系列ID不存在
#define UI_SERIES_ERROE_NAME_REPEAT						-904		//同父系列下的系列名称不能重复
#define	UI_SERIES_ERROR_MODIFY_NAME						-905		//系列文件中无此系列ID
#define UI_CABINET_ERROE_NO_NAME						-906		//箱体库中以该箱体名称命名的箱体不存在
#define UI_CABINET_ERROE_BE_USED_DELETE					-907		//箱体在项目中已使用，不能删除
#define UI_CABINET_ERROE_BE_USED_MODIFY_NAME			-908		//箱体在项目中已使用，不能修改名称
#define UI_SERIES_NO_COPY								-909		//系列不能复制
#define UI_NO_DISPLAY_IN_BUILD							-910		//请在系统搭建中，搭建一个LED屏体

////////////////////////////////控制//////////////////////////////////////
#define BLL_PHOTO_SEND_NOT_24BIT			    -1000	     //图片色深不是24位真彩色
#define BLL_BOOTLOGO_NO_CABIENT			        -1100	     //没有箱体
#define BLL_BOOTLOGO_IRREGULAR_SHAPE		    -1101	     //不规则形状
#define BLL_BOOTLOGO_FAIL_TO_LOAD_LOGO			-1102	     //加载图片失败
#define BLL_BOOTLOGO_TOO_SMALL_IMAGE			-1103	     //图片太小


#define MONITORTYPE_COUSTOM 0
#define MONITORTYPE_POWER 1
#define MONITORTYPE_POINTDETECT 2
#define MONITORTYPE_VOTAGE 3
#define MONITORTYPE_WEBERROR 4

#define MONITORACTIONTYPE_FF 1
#define MONITORACTIONTYPE_NORMAL 0

#define ETH_DATA_MAX_SIZE						1500

//////////////////////////////////////////////////////////////////////////

#define ANSWER_ERROR_DEST_ADDRESS			-600
#define ANSWER_ERROR_SOUR_ADDRESS			-601
#define ANSWER_ERROR_DATA_LENGTH			-602
#define ANSWER_ERROR_CRC					-603


typedef struct FrameDataField
{
	bool bMulticast;
	int nMulticastNum;
	bool bNoMulticast;
	bool bAnswer;
	bool bRead;
	bool bFIFO;
	unsigned char  ucAddress[2];

	int nSerialNumber;
	int nLength;
	unsigned char ucData[ETH_DATA_MAX_SIZE - 5];
	void reset()
	{
		memset(this, 0, sizeof(FrameDataField));
	}
}FRAMEDATAFIELD;

#define CL_WEIGHT_HIGH								2			//通信命令权限-高
#define CL_WEIGHT_LOW								1			//通信命令权限-低


#define UI_HOTKEY_CTRL_ATL_1						1000		//热键：ctrl+atl+1
#define UI_HOTKEY_CTRL_ATL_2						1001		//热键：ctrl+atl+2
#define UI_HOTKEY_CTRL_ATL_3						1002		//热键：ctrl+atl+3
#define UI_HOTKEY_CTRL_C							1003		//热键：ctrl+c
#define UI_HOTKEY_CTRL_V							1004		//热键：ctrl+v

//#define AUTOBRIGHTSEGMENT10
#ifdef AUTOBRIGHTSEGMENT10
	#define AUTOBRIGHTSEGMENT	10 //自动亮度调节段数定义
#else
	#define AUTOBRIGHTSEGMENT	5 //自动亮度调节段数定义
#endif

//箱体特殊情况宏定义
//#define M3_5	//M3.5箱体特殊情况
//#define FS08	//FS08箱体特殊情况
//#define  SP10	//SP10箱体特殊情况

//开关屏类型
#define  POWER_SWITCH_MODE_MANUAL   0    //手动
#define  POWER_SWITCH_MODE_TIMING   1    //定时

//GAMMA调节类型
#define  GAMMA_ADJUST_MODE_SIMPLE    0    //简单
#define  GAMMA_ADJUST_MODE_COMPLEX   1    //高级

#define LOCK_SCREEN_IMAGE_SET        0    //锁屏画面
#define POWER_ON_LOGO_SET            1    //开机LOGO

#define PACKAGE_NUMBER_10            10   //10个包数

//监控记录类型
#define  MONITOR_LOG_AMOUNT         30   //监控记录数量
#define  MONITOR_LOG_TYPE_AMBIENT   0    //环境
#define  MONITOR_LOG_TYPE_CABIENT   1    //箱体

#pragma endregion

#pragma region //结构体定义


//入线方式
enum INLINEMODE
{
	_1CARD,

	_2CARD_RIGHTLEFT,		//从右到左：1,2
	_2CARD_DOWNUP,			//从下到上：1,2
	_2CARD_LEFTRIGHT,       //从左到右：1,2

	_3CARD_RIGHTLEFT,		//从右到左：1,2,3	
	_3CARD_DOWNUP,			//从下到上：1,2,3
	_3CARD_LEFTRIGHT,		//从左到右：1,2,3 （add）

	_4CARD_RIGHTLEFT,		//从右到左：1,2,3,4
	_4CARD_DOWNUP,			//从下到上：1,2,3,4
	_4CARD_LEFTRIGHT,		//从左到右：1,2,3,4 （add）

	_4CARD_TIAN_RIGHTBUTTON_RVS,	//田字，右下进，垂直S走线
	_4CARD_TIAN_RIGHTBUTTON_RVZ,	//田字，右下进，垂直Z走线
	_4CARD_TIAN_LEFTBUTTON_LVS,		//田字，左下进，垂直S走线
	_4CARD_TIAN_LEFTBUTTON_LVZ,		//田字，左下进，垂直Z走线

	_4CARD_TIAN_LEFTTOP_LVZ,	//田字，左上进，垂直Z走线（add）
	_4CARD_TIAN_RIGHTTOP_RVZ,	//田字，右上进，垂直Z走线（add）
};

enum CABINETPOS
{
	_CABI_ALL,		//整箱体
	_CABI_LEFT,		//左部分
	_CABI_RIGHT,		//右部分
	_CABI_UPPER,		//上部分
	_CABI_UNDER,		//下部分
	_CABI_CENTRAL,			//中部的
	_CABI_UPPERLEFT,		//左上
	_CABI_UPPERRIGHT,			//右上
	_CABI_LOWERLEFT,	//左下
	_CABI_LOWERRIGHT,	//右下
	_CABI_UPPER_CENTRAL,			//上中部的
	_CABI_LOWER_CENTRAL,			//下中部的
	_CABI_LEFT_CENTRAL,			//左中部的
	_CABI_RIGHT_CENTRAL			//右中部的
};


enum SCANCARDPROC
{
	SAVE_DEFAUL_PARA,				//保存出厂缺省参数
	SAVE_SCAN_CARD_PARA,			//保存扫描卡参数
	SAVE_CONNECTION_PARA,				//保存连线图参数
	SAVE_BRIGHTNESS_PARA,				//保存亮度参数
	SAVE_COLOR_TEMP_PARA,				//保存色温参数
	SAVE_GAMMA_PARA,					//保存伽马参数
	SAVE_LINE_COEFF,					//保存亮暗线校正参数
	INIT_SCAN_CARD_PARA,				//初始化扫描卡参数
	ININT_GAMMA_PARA,					//初始化伽马参数

	QUERY_SCANCARD_VERSION,				//查询扫描卡的版本
	QUERY_HUB_VERSION,					//查询HUB的版本
	QUERY_MODULE_RUNTIME,				//查询模组运行时间
	RESET_MODULE_RUNTIME,				//重置模组运行时间

	SET_RELAY_AATTRIBUTE,				//设置继电器的自动控制属性
	SET_RELAY_THRESHOLD,				//设置继电器的门限
	SET_CABINET_PARA,					//发送箱体参数
	READ_MODULE_TO_SPI,					//读模组
	SAVE_NOSIGNALPICTURE_PARA			//保存无信号图片参数
};

enum eONEKEYSEND
{
	ONEKEYSEND_FINISH, //完成
	INTELLIGENTADDRESS, //发送箱体智能编址
	LOADEDAREA,	//发送扫描卡带载区域
	SENDSCANCARDPARA,//发送扫描卡参数
	SENDCABINETRELAYPARA, //发送风扇门限与自动控制功能
	SENDCOLOR, //发送控制参数:色温
	SENDGAMMA, //伽马
	SENDBRIGHTNESS, //亮度
	SETSATURATION, //饱和度
	SETCONTRAST, //对比度
	SAVEPARA, //保存扫描卡所有参数
	READCALIBRATIONDATAFROMM2C//启动读取模组的校正数据到扫描卡
};

//升级类型
enum UPDATE_HARDWARE_TYPE
{
	UPDATA_SCAN_CARD,			//升级扫描卡
	UPDATA_SEND_CARD_1,				//升级ATLVC的第一张采集卡
	UPDATA_SEND_CARD_2,				//升级ATLVC的第二张采集卡
	UPDATA_ATIEC_AM100,				//升级ATIEC的监控卡
	UPDATA_CABINET_AM100,				//升级箱体的监控卡
	UPDATA_ATIEC_SCANCARD,			//升级ATIEC的扫描卡
	UPDATA_HUB,					//升级HUB
	UPDATA_SEND_CARD_3          //升级ATLVC(ak6-1000)
};
//逻辑线程提示类型
enum THREAD_TIP_TYPE
{
	THREAD_TIP_INCREASE_PROSS,		//增加进度,提示
	THREAD_TIP_SET_PROSS,			//设置异步进度的总进度
	THREAD_TIP_OVER,			//线程完成
	THREAD_TIP_SHOW,			//线程中显示提示，不加进度
	THREAD_TIP_INCREASE_MAX_PROSS,	//增加进度的最大值
	THREAD_TIP_INCREASE_PROSS_ONLY,		//增加进度，不提示
	THREAD_SHOW_WAITDLG,				//显示等待对话框
};
//监控类型
enum MONIOTR_TYPE
{
	MONITOR_TYPE_SCREEN,	//环境监控
	MONITOR_TYPR_COMMON,	//箱体常规监控，T,H,S,Fan,Version
	MONIOTR_TYPE_POWERSTATE,
	MONITOR_TYPE_POWER_VOL,	//箱体电源电压
	MONITOR_TYPE_CAPACITY_FACTOR,	//箱体功率
	MONITOR_TYPE_DOT_DECTION,		//箱体逐点检测
	MONITOR_TYPE_WEB_ERROR,			//箱体网络坏包数
	MONITOR_TYPR_FF,		//箱体所有监控，广播
};

//选择类型
enum SELECT_TYPE
{
	SELECT_CLEAR,//不选，恢复默认值
	SELECT_FOUR_LINE,//选择四边
	SELECT_LEFT,//选择左边
	SELECT_TOP,//选择上边
	SELECT_RIGHT,//选择右边
	SELECT_BOTTOM,//选择底边
	SELECT_ALL,	//全选
	SELLECT_ADD,//加
	SELECT_SUBTRACTION,//减法
	SELECT_DISPLAY_ALL,//全屏全选
	SELECT_ROW_LINE,//选择箱体间的行边界
	SELECT_COL_LINE,//选择箱体间的列边界
	SELECT_ROW_LINE_ALL,//选择模组所有的行边界
	SELECT_COL_LINE_ALL,//选择模组所有的列边界
};

struct Ratio
{
	float ratioX;
	float ratioY;

	Ratio(float initX, float initY)
	{
		ratioX = initX;
		ratioY = initY;
	}
};
struct LinkRelation 
{
	string sFrom;
	int nFromPort;
	string sTo;
	int nToPort;
	int nAtlvcId;
	int nPortId;
	int nAtlvcBackId;
	int nPortBackId;
	int nFromATLVCID;
	int nToATLVCID;
	int nDirection;
	vector<Ratio> vRatio;
};

//版本结构
typedef struct Version
{
	double dVersion;//版本号
	unsigned char ucTper;//版本类型(0x01:标准APP版 0x11:标准BOOT版 0x02:PWM-APP版 0x12:PWM-BOOT版)
	bool bBackup;//备份状态,视频通道。 1：P1口。0：P0口
	bool bLockScreen;//锁屏
	bool bInit;//初始化
	short nNetPortPriority;	//网口优先设置 0：A口优先；1：B口优先
	bool bLockNetPort;		//锁定优先网口
	short nP0LinkState;//P0连接状态，1，有连接，0，无连接
	short nP1LinkState;//P1连接状态，1，有连接，0，无连接

	int nYear;
	int nMon;
	int nDay;
	int nSection;
	int nCorrectType;
	int nScanOut;
	int nScanOutSection;
	int nModuleEx;
	int nTestVersion;	//测试版本号，在日期的时间中的小时显示出来
	int nTestVersion_Second; //测试版本号，在日期的时间中的秒显示出来
	long nRuntime;		//用于模组运行时间
}VERSION,*LPVERSION;


//GAMMA
typedef struct GammaData
{
	short nVideowid;			//b00-8bit视频 b01-10bit视频 b10-12bit视频
	bool bSendThreeColor;		//三色同时发送
	short nGrayLevel;			//灰度级别
	float fGamma[3];			//GAMMA值
	short sGammaTable[3][256];//GAMMA表
	float fGammaRGB;			//Gamma值，用于10,12位
	short sGammaTableRGB[4096];//GAMMA表，用于10,12位
	GammaData()
	{
		memset(this, 0, sizeof(GammaData));
	}
	short nAdjustMode;             //调节方式 add by wanglq
	vector<POINT> rKnot;          //r曲线节点
	vector<POINT> gKnot;          //g曲线节点
	vector<POINT> bKnot;          //b曲线节点
}GAMMADATA;

//hh 色空间转换
typedef struct ColGmutData
{
	bool bIsEnable;
	int  pIntColorValues[9];//色空间矩阵系数*灰度级数
}COLGMUTDATA;

//红绿蓝三色白平衡值
typedef struct ColourRGB
{
	//高亮场
	int nRed;					//灰度红色亮度值(0-255)
	int nGreen;					//灰度绿色亮度值(0-255)
	int nBlue;					//灰度蓝色亮度值(0-255)
	int nICRed;					//驱动芯片红色亮度值(0-255)		->电流1
	int nICGreen;				//驱动芯片绿色亮度值(0-255)		->电流1
	int nICBlue;				//驱动芯片蓝色亮度值(0-255)		->电流1
	//低亮场
	int nRedLow;				//灰度红色亮度值(0-256)			->电流2
	int nGreenLow;				//灰度绿色亮度值(0-256)			->电流2
	int nBlueLow;				//灰度蓝色亮度值(0-256)			->电流2
	int nICRedLow;				//驱动芯片红色亮度值(0-255)		->电流3
	int nICGreenLow;			//驱动芯片绿色亮度值(0-255)		->电流3
	int nICBlueLow;				//驱动芯片蓝色亮度值(0-255)		->电流3

	//其它亮场处理（电流增益）
	int nICRed2;				//驱动芯片红色亮度值(0-255)		->电流4
	int nICGreen2;				//驱动芯片绿色亮度值(0-255)		->电流4
	int nICBlue2;				//驱动芯片蓝色亮度值(0-255)		->电流4
	int nICRed1;				//驱动芯片红色亮度值(0-255)		->电流5
	int nICGreen1;				//驱动芯片绿色亮度值(0-255)		->电流5
	int nICBlue1;				//驱动芯片蓝色亮度值(0-255)		->电流5

	int nICRed6;				//驱动芯片红色亮度值(0-255)		->电流6
	int nICGreen6;				//驱动芯片绿色亮度值(0-255)		->电流6
	int nICBlue6;				//驱动芯片蓝色亮度值(0-255)		->电流6
	int nICRed7;				//驱动芯片红色亮度值(0-255)		->电流7
	int nICGreen7;				//驱动芯片绿色亮度值(0-255)		->电流7
	int nICBlue7;				//驱动芯片蓝色亮度值(0-255)		->电流7
	int nICRed8;				//驱动芯片红色亮度值(0-255)		->电流8
	int nICGreen8;				//驱动芯片绿色亮度值(0-255)		->电流8
	int nICBlue8;				//驱动芯片蓝色亮度值(0-255)		->电流8
	int nICRed9;				//驱动芯片红色亮度值(0-255)		->电流9
	int nICGreen9;				//驱动芯片绿色亮度值(0-255)		->电流9
	int nICBlue9;				//驱动芯片蓝色亮度值(0-255)		->电流9

	bool m_bGainEnable[BRIGHTFIELD_STEP_NUM];		//各阶段电流增益（1/2-1/256）使能	1：使能，0：禁用
	bool m_bResEnable[BRIGHTFIELD_STEP_NUM];		//各阶段（1/2-1/256）电阻使能	1：使能，0：禁用

}COLOURRGB,*LPCOLOURRGB;

//走线表
typedef struct LinkTable
{
	long nLen;					//长度
	signed char ucLinkTable[65535];	//数据
	void Init()
	{
		memset(this, 0, sizeof(LinkTable));
	}
}LINKTABLE,*LPLINKTABLE;

//继电器
typedef struct RelayPar 
{
	short nID;					//继电器硬件编号
	string sRelayName;			//继电器名称（用于箱体：风扇1～4、用于ATIEC：ATECC1～2,屏体散热设备1～2）
	bool bEnable;				//继电器使能 (  0 - 不使能  1 - 使能)
	bool bWorkState;			//继电器工作状态（0 - 继电器断开 1 - 继电器闭合   ）
	float fTemperatureMin;		//温度下限值,精确到0.1
	float fTemperatureMax;		//温度上限值,精确到0.1
	float fHumidityMin;			//湿度下限值,精确到0.1
	float fHumidityMax;			//湿度上限值,精确到0.1
	bool bOverHeatOff;			//超温断电
	bool bOverHumidityOff;		//潮湿断电
	bool bSmogOff;				//烟雾断电
	RelayPar()
	{
		memset(this, 0, sizeof(RelayPar));
	}
}RELAYPAR;
//监控项使能标识（是否监控）
typedef struct MonitorItem
{
	bool bHumidityFlag;			//湿度
	bool bTemperatureFlag;		//温度
	bool bSmogFlag;				//烟雾
	bool bBrightFlag;			//亮度
	bool bPowerStateFlag;           //主备电源状态 hh
	bool bLEDPointDetect;		//逐点监测
	bool bPowerVolFlag[POWER_VOL_NUM];		//电源电压
	bool bFanStateFlag[FAN_NUM];		//风扇 0-左 1-右
	bool bCapacityFactorFlag;	//功率
	bool bVersionFlag;			//版本

	bool bFanRotationFlag;         //2013-03-25 风扇转速

	bool bNetWebErrorFlag ;        //2013-04-26

	bool bEightFanRotationFlag[FAN_ROTATION_NUM_MAX];
	MonitorItem()
	{
		memset(this, 0, sizeof(MonitorItem));
	}
	void Init()
	{
	  memset(this, 0, sizeof(MonitorItem));
	}
}MONITORITEM;


//色温
typedef struct ColourTemFlag
{
	ColourRGB colourrgb[COLOR_TEMP_NUM_EX];			//色温值(3500,4500,5500...10500)
	string m_nColorTemperature[COLOR_TEMP_NUM_EX];	//色温(3500,4500,5500...10500)
	bool m_bEnable[COLOR_TEMP_NUM_EX];				//色温位使能

	short m_nAnswerID;								//应答方式 0 - 广播无应答，  1 - 点播无应答
	short m_nMode;									//色温调节方式 0 - 灰度调节， 1 - 电流调节
	bool m_nGrayAdjustEnable;						//色温灰度调节使能
	bool m_nCurrentAdjustEnable;					//色温电流调节使能

	string GetColorTemTem(short nTemID)
	{
		char cBuf[10];
		sprintf(cBuf,"%dK", 2300 + nTemID*100);
		return cBuf;

		string sTemTem;		
		switch (nTemID)
		{
		case 0:
			sTemTem = "2800K";
			break;
		case 1:
			sTemTem = "3200K";
			break;
		case 2:
			sTemTem = "4500K";
			break;
		case 3:
			sTemTem = "5000K";
			break;
		case 4:
			sTemTem = "5600K";
			break;
		case 5:
			sTemTem = "6500K";
			break;
		case 6:
			sTemTem = "8000K";
			break;
		case 7:
			sTemTem = "9300K";
			break;
		}			
		return sTemTem;
	}

	void Init()
	{
		this->m_nAnswerID = 0;
		this->m_nMode = 0;
		this->m_nGrayAdjustEnable =false;
		this->m_nCurrentAdjustEnable = false;

		for (int i = 0; i < COLOR_TEMP_NUM_EX; i++)
		{
			this->m_nColorTemperature[i] = GetColorTemTem(i);
			this->m_bEnable[i] = true;

			this->colourrgb[i].nRed = 256;
			this->colourrgb[i].nGreen = 256;
			this->colourrgb[i].nBlue = 256;
			this->colourrgb[i].nICRed = 171;
			this->colourrgb[i].nICGreen = 171;
			this->colourrgb[i].nICBlue = 171;

			this->colourrgb[i].nRedLow = 171;
			this->colourrgb[i].nGreenLow = 171;
			this->colourrgb[i].nBlueLow = 171;
			this->colourrgb[i].nICRedLow = 171;
			this->colourrgb[i].nICGreenLow = 171;
			this->colourrgb[i].nICBlueLow = 171;

			this->colourrgb[i].nICRed2 = 171;
			this->colourrgb[i].nICGreen2 = 171;
			this->colourrgb[i].nICBlue2 = 171;
			this->colourrgb[i].nICRed1 = 171;
			this->colourrgb[i].nICGreen1 = 171;
			this->colourrgb[i].nICBlue1 = 171;

			//
			this->colourrgb[i].nICRed6 = 171;
			this->colourrgb[i].nICGreen6 = 171;
			this->colourrgb[i].nICBlue6 = 171;
			this->colourrgb[i].nICRed7 = 171;
			this->colourrgb[i].nICGreen7 = 171;
			this->colourrgb[i].nICBlue7 = 171;
			this->colourrgb[i].nICRed8 = 171;
			this->colourrgb[i].nICGreen8 = 171;
			this->colourrgb[i].nICBlue8 = 171;
			this->colourrgb[i].nICRed9 = 171;
			this->colourrgb[i].nICGreen9 = 171;
			this->colourrgb[i].nICBlue9 = 171;

			this->colourrgb[i].m_bGainEnable[0] = false;
			this->colourrgb[i].m_bGainEnable[1] = false;
			this->colourrgb[i].m_bGainEnable[2] = false;
			this->colourrgb[i].m_bGainEnable[3] = false;
			this->colourrgb[i].m_bGainEnable[4] = false;
			this->colourrgb[i].m_bGainEnable[5] = false;
			this->colourrgb[i].m_bGainEnable[6] = false;
			this->colourrgb[i].m_bGainEnable[7] = false;

			this->colourrgb[i].m_bResEnable[0] = false;
			this->colourrgb[i].m_bResEnable[1] = false;
			this->colourrgb[i].m_bResEnable[2] = false;
			this->colourrgb[i].m_bResEnable[3] = false;
			this->colourrgb[i].m_bResEnable[4] = false;
			this->colourrgb[i].m_bResEnable[5] = false;
			this->colourrgb[i].m_bResEnable[6] = false;
			this->colourrgb[i].m_bResEnable[7] = false;
		}

	}
}COLOURTEMFLAG;



//扫描卡左上角及大小
typedef struct ScanCardpRect
{
	short nAddress;				//扫描卡地址（编址）
	RECT prect;				//扫描卡左上角及大小
}SCANCARDPRECT;

//坏灯结构
typedef struct ErrorPoint
{
	short nX;					//出错LED灯在箱体内X坐标
	short nY;					//出错LED灯在箱体内Y坐标
	bool bRedFlag;				//红灯标识 0- 正常	1-坏
	bool bGreenFlag;			//绿灯标识
	bool bBlueFlag;				//蓝灯标识
	ErrorPoint()
	{
		nX = 0;
		nY = 0;
		bRedFlag = false;
		bGreenFlag = false;
		bBlueFlag = false;
	}
}ERRORPOINT;

// 监控报警参数
typedef struct MonitorAlarmParam
{
	float fATIECTemperatureMax;   //2012-10-18 by sunj ATIEC温度上限
	float fATIECHumidityMax;     // 2012-10-18 by sunj ATIEC湿度上限

	float fTemperatureMax;  // 温度上限
	float fHumidityMax;     // 湿度上限
	float fPSMin[5];        // 五路电压下限
	float fPSMax[5];        // 五路电压上限
	int nBadLedMax;         // 坏灯数上限(箱体)
	BOOL bSmoke;            // 是否烟雾报警
	BOOL bFan;              // 是否风扇报警
	int nBadLedMaxOfDisplay ;//屏幕坏灯个数

	int nTemperatureUnit ; //2013-02-18 by sunj 温度单位 0-摄氏度 1-华氏度

}MONITORALARMPARAM;

//最后一次监控报警状态（发送邮件判断使用）
struct LastMonitorAlarm
{
	bool bLastTemperatureAlarm;
	bool bLastHumidityAlarm;
	bool bLastSmogAlarm;
	bool bLastFanAlarm[2];
	bool bLastPowerVolAlarm[5];
	bool bLastCapacityFactorAlarm;
	bool bLastPointDetectAlarm;

	bool bConnectStatusAlarm;
	bool bBackUpStatusAlarm;
};
/*
create date :2012-10-20 by sunj
discription:标示监控项有报警时是否需要邮件通知
*/

typedef struct Email_ATIEC_Send_flag
{
	bool bTemperatureFlag;//true-监控项可以发送邮件 false-不能
	bool bHumityFlag;
	bool bFunFlag;
	bool bSmogFlag;
	bool bPowerFlag ;
}EMAILATIECSENDFLAG,*LPEMAILATIECSENDFLAG;
/*
create date :2012-10-20 by sunj
discription:标示监控项有报警时是否需要邮件通知
*/
typedef struct Email_Cabinet_Send_flag
{
	bool bTemperatureFlag;//true-监控项可以发送邮件 false-不能
	bool bHumityFlag;
	bool bFunFlag;
	bool bPowerOverFlag;
	bool bPowerLowFlag;
	bool bSmog;
	bool bDisplayDeadFlag;
	bool bCableFlag;
	bool bCabinetDeadLedFlag;
	bool bDisplayDeadLedFlag;
}EMAILCABINETSENDFLAG, *LPEMAILCABINETSENDFLAG;

typedef struct Email_Send_flag
{
	EMAILATIECSENDFLAG sEmail_ATIEC_Send_flag;
	EMAILCABINETSENDFLAG sEmail_Cabinet_Send_flag;
}EMAILSENDFLAG,*LPEMAILSENDFLAG;

//监控数据 包括
//常规监控：（亮度、温度、湿度、是否烟雾报警、左右风扇状态、4路继电器状态、监控卡版本）
//箱体电压监控：箱体（扫描卡）5路电压值
//箱体功率监控：箱体（扫描卡）功率值
//箱体逐点检测：箱体（扫描卡）出错灯个数及情况
typedef struct Monitordata
{
	//MonitorItem smonitoritem;	//监控项使能标识
	//所有项目值=-1代表应答失败！

	short nCabinetAddress;
	short nBrightness;			//亮度值
	short nBrightness2;			//亮度值
	float fTemperature;			//温度值
	float fHumidity;			//湿度值
	bool bSmog;					//烟雾值

	bool bTemperatureFlag;		//是否温度报警
	bool bHumidityFlag;			//是否湿度报警
	bool bBrightFlag;			//是否亮度报警
	bool bSmogFlag;				//是否烟雾报警

	short nFanState[FAN_NUM];			//风扇FAN_L状态	1	0x00	未检测		0x01	正常开启	0x03	异常关闭	0x04	正常关闭	0x05	异常开启
	short nPowerState[2];       //电源状态
	bool bWorkState[RELAY_NUMBER_MAX];			//4路继电器工作状态（0 - 继电器断开 1 - 继电器闭合）
	Version version;			//监控卡版本

	//////////////////////////////////////////////////////////////////////////
	float fPowerVol[POWER_VOL_NUM];			//箱体（扫描卡）5路电压值

	int nCapacityFactor;		//箱体（扫描卡）功率

	short nErrorPointNum;		//坏灯个数
	ErrorPoint sErrorPoint[DEAD_LEAD_NUM_MAX];//坏灯数组
	short nDClkState[SCAN_LINE_STATE_NUM];		//DCLK出错状态，相应bit位为1标示对应DCLK出错
	short nScanLineState[SCAN_LINE_STATE_NUM];	//扫描线出错状态，相应bit位为1标示对应扫描线出错
	//////////////////////////////////////////////////////////////////////////
	bool bConnectStatus;		//LED显示屏连接状态（查询LED显示屏主ATLVC版本）
								//箱体（扫描卡）连接状态（查询箱体（扫描卡）版本）
	bool bBackUpStatus;			//LED显示屏备份状态（查询LED显示屏备ATLVC版本）

	short bDotDetectErrorFlag;//逐点检测错误标识，-1 --成功 0 -- 初始化  1 -- 失败  2 -- 异常，超出32坏点 2012-10-26 sunj
 
	short nFanRotation[FAN_ROTATION_NUM_MAX];

	int nNetWebErrorPackageFlag ;
	short nNetWebErrorNum;
	LastMonitorAlarm mLastAlarm;

	Monitordata()
	{
		memset(nDClkState,0,sizeof(short) * SCAN_LINE_STATE_NUM);
		memset(nScanLineState,0,sizeof(short) * SCAN_LINE_STATE_NUM);
	}

}MONITORDATA,*LPMONITORDATA;

typedef struct MonitordataQ
{
	short nBrightness;			//亮度值
	short nBrightness2;			//亮度值2
	float fTemperature;			//温度值
	float fHumidity;			//湿度值
	bool bSmog;					//是否烟雾报警
}MONITORDATAQ,*LPMONITORDATAQ;

struct scan_card
{
	short nID;					//扫描卡ID（1～4，在同一箱体内唯一）
	short nAddress;				//扫描卡地址（编址得到，在连接在同一链路中唯一）
	class CStructSingleScanCard scandcard;			//扫描卡参数
};
typedef struct
{
	//模组亮暗线系数，0-4:模组中上、左、右、下4边,5-7：左上、右上、左下、右下4点的校正系数
	float fLineCoeff[CALIBRATION_LINE_COEFF_MAX];
	//模组亮暗线系数选择，0-4:模组中上、左、右、下4边,5-7：左上、右上、左下、右下4点的校正系数
	bool bLineCoeffSel[CALIBRATION_LINE_COEFF_MAX];
}LineCoeff;
//绑定扫描卡的硬件及其属性
typedef struct ScanCardAttachment
{
	int nID;					//扫描卡ID（1～4，在同一箱体内唯一）
	int nAddress;				//扫描卡地址（编址得到，在连接在同一链路中唯一）

	CStructSingleScanCard scancard;			//扫描卡参数

	LINKTABLE slinktable;		//扫描卡走线查找

	LINKTABLE hublinktable;	//HUB走线查找

	LINKTABLE sSectionlinktable;	//扫描卡区走线查找

	//是否存在
	bool bTHSBoard;				//温、湿度传感器板	
	bool bMFCard;				//多功能卡
	bool bPDCard;				//功率检测卡
	bool bPBPDCard;				//逐点检测卡
	//是否监控
	MonitorItem monitoritem;	//监控项使能
	//继电器
	RelayPar relaypar[2];		//继电器
	
	//
	ColourRGB colourrgb;		//扫描卡色温

	RECT rtRect;				//扫描卡左上角及大小
	RECT rtSpotlightRect;		//射灯左上角及大小

	int nAtlvcID;				//连接的ATLVC
	short nPort;				//连接的端口（1～4：AK6：1U、1D、2U、2D；AK10：A、B、C、D）
	bool bBackUp;				//是否备份返回
}SCANCARDATTACHMENT;

//箱体连线图（各箱体左上角及大小、连接端口）
typedef struct ConnectionDiagram 
{
	short nID;					//箱体ID
	int nAddress;				//箱体地址（编址）
	int nNetCardNo;				//网络卡号（S系列使用，S系列中（nNetCardNo， nAddress）坐标位置作为地址）
	int nSegID;                 //箱体所在片段ID
	int cs_id;				    //所属系列ID
	string sName;				//箱体名称(类型)

	short nScanCardCount;		//扫描卡个数（1～4）
	bool bScanCardParaSynchro;		//扫描卡参数是否同步修改
	bool bMonitorParaSynchro;		//监控参数是否同步修改
	INLINEMODE InlineMode;		//扫描卡入线方式
	map<short,ScanCardAttachment> mScancardAttachment;	//扫描卡及相关附件表

	int nCabinetID;				//箱体背后贴的箱体ID号，4位，0001起
	int nCabinetType;			//箱体类型：默认0：一般箱体；1：射灯箱体；2：主网络卡显示箱体；3：备网络卡显示箱体；4：主备网络卡箱体显示
	int nFromCabinetId;			//射灯情况下，对应箱体的id，如果是非射灯箱体，值和nID一样即可

	RECT rtRect;				//箱体左上角及大小
	int nAtlvcID;				//连接的ATLVC
	short nPort;				//连接的端口（1～4：AK6：1U、1D、2U、2D；AK10：A、B、C、D）
	bool bBackUp;				//是否备份返回

	int nBackAtlvcID;
	short nBackPort;

	double fCoefAverage[9];		//箱体校正系数的平均值

}CONNECTIONDIAGRAM;
//箱体
typedef struct Cabinet 
{
	int nID;					//箱体ID
	int nSeriesID;				//箱体所属系列的ID
	string sSeriesName;         //箱体所属系列的名称
	string sName;				//箱体型号
	bool bRead;					//已读取标示

	string sPhoto;				//箱体照片
	
	int nAddress;				//箱体在连线图中的地址（编址得到，在连接在同一链路中唯一）

	short nScanCardCount;				//扫描卡个数（1～4）
	bool bScanCardParaSynchro;		//扫描卡参数是否同步修改
	bool bMonitorParaSynchro;		//监控参数是否同步修改
	INLINEMODE InlineMode;		//入线方式
	map<short,ScanCardAttachment> mScancardAttachment;	//扫描卡及相关附件表

	RECT rtRect;				//箱体左上角及大小
	RECT rtSpotlightRect;		//射灯左上角及大小

	int nAtlvcID;				//连接的ATLVC
}CABINET,*LPCABINET;

//屏幕一部分
typedef struct tagSCREENPART
{
	int  nAtlvcID;				//ID
	int  nBoxNumber;
	RECT rtSize;				//大小
}SCREENPART;

//箱体系列
typedef struct CabinetSeries
{
	int nID;					//系列ID
	int nParentID;				//父系列ID, 0-根节点
	string sName;			//系列名称		
}CABINETSERIES;

//串口通讯参数
typedef struct SerialPortPar
{
	char Serial;				//通讯串口
	Uint64 BaudRate;			//波特率：57600
	char StopBits;				//停止位：1位	/* 0,1,2 = 1, 1.5, 2            */
	char ByteSize;				//数据位：8位；	/* number of bits/byte, 4-8     */
	char Parity;				//校验位：无 /* 0-4 = no,odd,even,mark,space */
}SERIALPORTPAR;
//网络通讯参数
typedef struct SocketPar
{
	unsigned long  ulIP;			//IP地址
	unsigned short usPort;			//端口
}SOCKETPAR;

typedef struct ATLVCAK6Status
{
	int nReadErrorFlag;	//读取有效位, -1 --成功 0 -- 初始化  1 -- 失败
	int nDvi;			//采集卡dvi状态，-1：无效；0：无信号；1：正常
	int nNetPort[CHANNEL_PORT_MAX];		//采集卡P0-P3网络发送状态 -1：无效；0：连接；1：未连接

	int nHdmi;                 //HDMI状态 1- 有效 0 - 无效 
	int nDivPort;              //DVI端口 1- 有效 0 -无效
	int nHdmiport ;            //HDMI端口 1- 有效 0- 无效
	int nResolutionDvi;        //Dvi
	int nResolutionHDMI;       //HDMI

	int nVideowid ;            //b00-8bit视频 b01-10bit视频 b10-12bit视频
	int nResolution[2];			//分辨率 0-X, 1-Y 

	float fTemperature; //ATLVC的温度
	float fHumidity;	//ATLVC的湿度

	/*discription:表示ATLVC的自动亮度调节和定时调节的状态*/
	short nAutoLightAdjustStatus; //0-自动亮度调节不使能 1-表示自动亮度调节使能 -1 未知
	short nTimingLightAdjustStatus;//0-定时亮度调节使能，1-表示定时亮度调节使能 -1 未知

	short nFrame; //AK1000使用帧频
	int iFrame; //AK100使用

	int nVideoRotate;          //0-不旋转 1-顺时针90度 2-顺时针180度 3-顺时针270度
	 
	long nSaturation;          //采集卡饱和度
	long nContrast;            //采集卡对比度

}*LPATLVCAK6_STATUS;
struct ATIECStatus//zhangjj004added
{
	ATIECStatus()
	{
		dVersion=0.0;
		ucTper=0;
		bBackup=false;
		bLockScreen=false;
		bInit=false;
		nP0LinkState=0;
		nP1LinkState=0;
		nP2LinkState=0;
		nP3LinkState=0;
		bBackupP2P3=false;
	}
	double dVersion;//版本号
	unsigned char ucTper;//版本类型(0x01:标准APP版 0x11:标准BOOT版 0x02:PWM-APP版 0x12:PWM-BOOT版)
	bool bBackup;//备份状态,视频通道。 1：P1口。0：P0口
	bool bLockScreen;//锁屏
	bool bInit;//初始化
	short nP0LinkState;//P0连接状态，1，有连接，0，无连接
	short nP1LinkState;//P1连接状态，1，有连接，0，无连接
	short nP2LinkState;
	short nP3LinkState;
	bool bBackupP2P3;
};
typedef struct ScanCard_Cabinet
{
	int nScanCardAdress;		//扫描卡地址，索引
	int nScanCardID;			//扫描卡ID
	int nCabinetID;				//扫描卡所在的箱体ID
	short nCabinetAddress;		//箱体地址
	short nNetCardNo;			//网络卡地址，S系列使用
	bool isAtiec;
	//int nScancard
}SCANCARDCABINET;

//AK6 LED视频控制器 单个采集卡(集系统、采集、发送)
struct ATLVCAK6
{			
	short nCommType;	        //通讯方式 0:串口，1：USB
	short nUsbPort;             //USB接口号，用于识别同驱动下不同设备
	SerialPortPar sppar;		//串口通讯参数;
	SocketPar skTCP;			//网络通讯参数

	short nAddress;				//AK6单个采集卡地址
	short nSecondAddress ;      //级联地址
	
	RECT rtRect;				//总带载的区域
	RECT rtRectU;				//U带载的区域
	RECT rtRectD;				//D带载的区域

    RECT rtRectP0;				//2012-6-21 P0带载的区域
	RECT rtRectP1;				//2012-6-21 P1带载的区域
	RECT rtRectP2;				//2012-6-21 P2带载的区域
	RECT rtRectP3;				//2012-6-21 P3带载的区域
	int nResolution[2];		   //分辨率 0-X, 1-Y
	int nFrame;	//AK1000使用帧频
	int iFrame;	//AK100使用
	int nVideoRotate;          //2012-6-21 0-不旋转 1-顺时针90度 2-顺时针180度 3-顺时针270度
};
//AK10采集卡
typedef struct  CollectCard
{

}COLLECTCARD;
//AK10发送卡
typedef struct SendCard 
{

}SENDCARD;
//AK10系统卡
typedef struct SystemCard
{
	short type;					//通讯类型
	SocketPar spar;				//网络通讯参数
	SerialPortPar sppar;		//串口通讯参数

}SYSTEMCARD;
//AK10 LED视频控制器 
struct ATLVCAK10
{
	SystemCard systemcard;		//AK10系统卡
	CollectCard collectcard;	//AK10采集卡
	SendCard sendcard;			//AK10发送卡
};


//LED视频控制器
struct ATLVC 
{
	short nID;					//LED视频控制器ID
	short nType;				//LED视频控制器类型
	short portType;				//连接的端口：U：1	D：2；P0\P1\P2\P3：1001\1002\1003\1004
	bool bSingleLEDPort;		//是否单口带单屏情况
	union
	{
		ATLVCAK6 atlvcak6;			//AK6 LED视频控制器
		ATLVCAK10 atlvcak10;		//AK10 LED视频控制器 
	};
	bool bMainCard;				//是否是主卡
	int nBackupCardID;			//备卡ID
	/************************************************************************************************
	function :GetPort(short nPort)
	Description:将网口有整数转换为字符串 
	Date:
	input：网口序号（1,2,3,4,5,6,7,8）
	out: 采集卡的网口
	Modify: 2012-06-22 采集卡的网口如2P2表示ATLVC控制盒第二张采集卡的P3口
	*************************************************************************************************/
	string GetPort(short nPort)
	{
		string sPort;
		switch (nType)
		{
		case 1:
		case 2:
			{
				switch (nPort)
				{
				case ATLVC_1P0:
					sPort = "1U";
					break;
				case ATLVC_1P1:
					sPort = "1D";
					break;
				case ATLVC_2P0:
					sPort = "2U";
					break;
				case ATLVC_2P1:
					sPort = "2D";
					break;
				}
			}
			break;
		case 3:
			{
				switch (nPort)
				{
				case 1:
					sPort = "A";
					break;
				case 2:
					sPort = "B";
					break;
				case 3:
					sPort = "C";
					break;
				case 4:
					sPort = "D";
					break;
				}
			}
			break;
		case 4:
		case 5:
			{
				switch (nPort)
				{
				case ATLVC_EX_1P0:
					sPort = "1OUT1";
					break;
				case ATLVC_EX_1P1:
					sPort = "1OUT2";
					break;
				case ATLVC_EX_1P2:
					sPort = "1OUT3";
					break;
				case ATLVC_EX_1P3:
					sPort = "1OUT4";
					break;
				case ATLVC_EX_2P0:
					sPort = "2OUT1";
					break;
				case ATLVC_EX_2P1:
					sPort = "2OUT2";
					break;
				case ATLVC_EX_2P2:
					sPort = "2OUT3";
					break;
				case ATLVC_EX_2P3:
					sPort = "2OUT4";
					break;
				}
			}
			break;
		}
		return sPort;
	}
	// input：
	/************************************************************************************************
	function :GetPort(string sPort)
	Description:将网口有整数转换为字符串 
	Date:
	input：采集卡的网口
	out: 网口序号（1,2,3,4,5,6,7,8）
	Modify: 2012-06-22 网口序号（1,2,3,4,5,6,7,8）out:采集卡的网口如2P2表示ATLVC控制盒第二张采集卡的P3口
	*************************************************************************************************/
	short GetPort(string sPort)
	{
		short nPort;
		switch (nType)
		{
		case 1:
		case 2:
			{
				if (0 == strcmp("1U",sPort.c_str()))
				{
					nPort = ATLVC_1P0;
				}
				else if (0 == strcmp("1D",sPort.c_str()))
				{
					nPort = ATLVC_1P1;
				}
				else if (0 == strcmp("2U",sPort.c_str()))
				{
					nPort = ATLVC_2P0;
				}
				else if (0 == strcmp("2D",sPort.c_str()))
				{
					nPort = ATLVC_2P1;
				}
				break;
			}
		case 3:
			{
				if (0 == strcmp("A",sPort.c_str()))
				{
					nPort = 1;
				}
				else if (0 == strcmp("B",sPort.c_str()))
				{
					nPort = 2;
				}
				else if (0 == strcmp("C",sPort.c_str()))
				{
					nPort = 3;
				}
				else if (0 == strcmp("D",sPort.c_str()))
				{
					nPort = 4;
				}
				break;
			}
		case 4:
		case 5:
			{
				if (0 == strcmp("1OUT1",sPort.c_str()))
				{
					nPort = ATLVC_EX_1P0;
				}
				else if (0 == strcmp("1OUT2",sPort.c_str()))
				{
					nPort = ATLVC_EX_1P1;
				}
				else if (0 == strcmp("1OUT3",sPort.c_str()))
				{
					nPort = ATLVC_EX_1P2;
				}
				else if (0 == strcmp("1OUT4",sPort.c_str()))
				{
					nPort = ATLVC_EX_1P3;
				}
				else if (0 == strcmp("2OUT1",sPort.c_str()))
				{
					nPort = ATLVC_EX_2P0;
				}
				else if (0 == strcmp("2OUT2",sPort.c_str()))
				{
					nPort = ATLVC_EX_2P1;
				}
				else if (0 == strcmp("2OUT3",sPort.c_str()))
				{
					nPort = ATLVC_EX_2P2;
				}
				else if (0 == strcmp("2OUT4",sPort.c_str()))
				{
					nPort = ATLVC_EX_2P3;
				}
				break;
			}
		}
		return nPort;
	}

	string GetType()
	{
		string sType;
		switch (nType)
		{
		case 1:
			{
				sType = "AF";
				break;
			}
			break;
		case 2:
			{
				sType = "AS";
				break;
			}
			break;
		case 3:
			{
				sType = "AK10";
				break;
			}
			break;
		case 4:
			{
				sType = "XF";
			}
			break;
		case 5:
			{
				sType = "XS";
			}
			break;
		default:
			break;
		}
		return sType;
	}
};
//ATIEC
struct ATIEC 
{
	ATIEC()
	{
		LightPort1Enable = 0;
		LightPort2Enable = 0;
		nType=-1;
	}
	short nID;					//ATIEC ID
	short nAddress;	//ATIEC地址
	short nType;    //类型 
	//是否监控
	MonitorItem monitoritem;	//监控项使能
	//继电器
	RelayPar relaypar[4];		//继电器
	bool LightPort1Enable;//zhangjj001
	bool LightPort2Enable;

	int atlvcid;				//连接的通信信道ATLVC ID
	int backupatlvcid;			//备卡ID
	ATIECStatus  ATIEC_EStatus;
	Version VScanCard;          //2012-09-24，记录ATIEC的扫描卡版本
	Version vMonitorCard;       //2012-09-24，记录ATIEC的监控卡版本
};
//////////////////////////////////////////////////////////////////////////
//显示屏控制相关

//定时开关屏，定时时间段，以周为同期
//包括排除日期，也即在这些排除日期内不操作
struct Display_Timing_PowerControl
{
	short id;
	short dayofweek;			//7：每天，否则星期一～日（0：星期天～6：星期六）
	string openTime;			//开屏时间
	string closeTime;			//关屏时间
	bool bOpenSend;				//是否已发送
	bool bCloseSend;			//是否已发送
};
/*
create date:2012-10-24
author:sunj
discription:定时开关屏的指定日期。优先权高于Display_Timing_PowerControl
*/
struct Display_Special_PowerControl
{
	short id;
	string openDate;           //开屏日期
	string closeDate;          //关屏日期
	string openTime;           //开屏时间
	string closeTime;          //关屏日期
	bool bOpenSend;				//是否已发送
	bool bCloseSend;			//是否已发送
};
//定时亮度调节
struct Display_Timing_BrightAdjust 
{
	short id;
	string adjustTime;			//调节时间
	short bright;				//调节值
	bool bSend;					//是否已发送
};


//自动亮度调节
struct Display_Auto_BrightAdjust
{
	//short brightMin;			//亮度最小值
    //short adjustMin;			//调节最小值（间隔）
	short interval;				//调节间隔
	short bright[AUTOBRIGHTSEGMENT];			//100级环境亮度对应的调节亮度值
	short nBrightMax[AUTOBRIGHTSEGMENT];        //亮度最大值
	short nBrightMin[AUTOBRIGHTSEGMENT];        //亮度最小值
	short nAutoEnable ;			//自动使能,1代表使能，0代表不使能
};

//DMS控制台通道配置
struct DMXCtrlChannelInfo
{
	unsigned int nLight_pass;	//亮度调节通道
	unsigned int nRed_pass;		//红色灰度调节通道
	unsigned int nGreen_pass;	//绿色灰度调节通道
	unsigned int nBlue_pass;	//蓝色灰度调节通道
	unsigned int nElectric_gain_pass;//电流增益色温调节通道
};

//一键发送参数配置
struct OneKeySendParaCfg
{
	bool bIntelligentAddress;	//发送箱体智能编址
	bool bLoadArea;				//发送扫描卡带载区域
	bool bSendScanCardPara;		//发送扫描卡参数
	bool bSendCabinetRelayPara;	//发送风扇门限与自动控制功能
	bool bSendColor;			//发送控制参数:色温
	bool bSendGama;				//伽马
	bool bSetBrightness;		//亮度
	bool bSetSaturation;		//饱和度
	bool bSetContrast;			//对比度
	bool bSavePara;				//保存扫描卡所有参数
	bool bReadCalibrationDataFromM2C;//启动读取模组的校正数据到扫描卡
};

struct Display_Custom_Template
{
	string sName;               //模板名字 2012-10-23 by sunj
	short nBright;				//亮度
	short nColourTem;			//色温模板号
	int nRed;					//灰度红色亮度值(0-255)
	int nGreen;					//灰度绿色亮度值(0-255)
	int nBlue;					//灰度蓝色亮度值(0-255)

	GammaData gammadata;		//GAMMA
	long nSaturation;           //2012-06-23饱和度
	long nContrast;             //2012-06-23对比度
};
enum display_type
{
	DISPLAY_TYPE_ONE_COLOR,
	DISPLAY_TYPE_DOUBLE_COLOR,
	DISPLAY_TYPE_REAL,			//0 - 全彩实像素屏
	DISPLAY_TYPE_VIRTUAL		//1 - 全彩虚拟屏
};

//显示屏状态（最新状态）
typedef struct Display_Status
{
	int nShowDisplay;			//是否显示该屏幕，0：不显示，1：显示，-1：无屏
	bool bTimingBrightAdjust;	//是否启动定时调亮
	bool bAutoBrightAdjust;		//是否启动自动调亮
	short nPowerSwitchMode;     //电源开关方式
	bool bPowerOpen;			//电源是否打开
	bool bLock;					//是否锁屏
	bool bHaveSignal;			//是否有信号
	bool bMonitor;				//是否监控
	bool bLamps;				//箱体背面指示灯打开：true 关闭：false

	short nBright;				//亮度
	short nColourTem;			//色温模板号
	short nNoSignalType;		//无信号显示类型

	long nSaturation ;          //2012-06-25饱和度
	long nContrast;             //2012-06-25对比度

}DISPLAYSTATUS;

typedef struct FanAutoAdjust
{
	vector<short> vFunTempMin;
	vector<short> vFunTempMax;
	vector<short> vFunTempTarget;
}FANAUTOADJUST;
//LED显示屏
typedef struct Display
{
	short nID;					//LED显示屏ID
	string sName;				//LED显示屏名称
	display_type nScreenType;		//LED显示屏类型   2 - 全彩实像素屏 ， 3 - 全彩虚拟屏 4 - 异型屏
	bool bActive;				//是否激活

	map<short,ATLVC> mATLVC;	//ATLVC 用于通讯
	map<short,ATIEC> mATIEC;		//ATIEC 控制该显示屏的

	//====校正相关=====
	string sOrderID;			//订单号，7位
	string sDisplayID;			//屏体号，2位
	short nDotDataType;//点校正数据类型 0 - 真实数据 1 - RGB同色渐变 2 - RGB异色渐变 3 - 模组收尾渐变 4-模组颜色自定义
	double fTarget[3];//红绿蓝目标值

	int nDatabaseType;		//数据库类型 0-MDB，1-TXT，默认为MDB
	string sDatabaseDefaultPath;	//数据库默认路径
	int nCabinetInTXTHor;			//TXT分割文件中包括箱体的水平个数
	int nCabinetInTXTVor;			//TXT分割文件中包括箱体的垂直个数
	bool bRepairDotWhileSend;		//发送校正数据时，启动异常点自动修改功能
	bool bRepairDBWhileCheck;		//启动异常点检测时,自动修复异常点,并修改数据库
	double fCalibCoeffThreshold[9];	//校正系数阈值
	int nOperatingType;				//运算类型，0-乘法，1-加法


	map<short,ConnectionDiagram> mConnectionDiagram;	//箱体连线图（各箱体（扫描卡））

	ColourTemFlag colourtemflag;//色温
	GammaData gammadata;		//GAMMA

	int nWeekOrEveryday;
	vector<Display_Timing_PowerControl> mTimingPower;			//定时开关屏，按星期
	vector<string> mExludeday;									//定时开关屏，排除日期
	vector<Display_Timing_PowerControl> mTimingPowerEveryday;			//定时开关屏，按每天
	vector<Display_Special_PowerControl> mSpecialPower;          //定时开关屏 ，指定日期
	vector<Display_Timing_BrightAdjust> mTimingBright;			//定时调节亮度
	Display_Auto_BrightAdjust AutoBright;						//自动调节亮度

	Display_Custom_Template mCustomTemplate[9];						//用户自定义模板

	Display_Status mLastStatus;	
	FANAUTOADJUST mFanAutoAdjust;//
}DISPLAY;

//自动校正
typedef struct tagAUTOCORRECTIONPARAM
{
	short    nProjectID;
	short    nConfigID;
	short    nDisplayID;
	int      nModulePixelW;
	int      nModulePixelH;
	long     nBegModCol;
	long     nBegModRow;
	long     nEndModCol;
	long     nEndModRow;
	string   sAdbPath;
	string   sTxtPath;
	string   sOrderNumber;
	string   sDisplayNumber;
	string   sCabinetID;
}AUTOCORRECTIONPARAM;

//校正状态
typedef struct Calibration_Status
{	
	double fCoeff[3];//色度校正RGB系数，0 -R, 1 - G, 2 - B
	
	double nLightCorrectCoff[3];//亮度校正卡边界变量系数-RGB

}CALIBRATIONSTATUS;

//配置
typedef struct Configure
{
	short nID;					//配置ID
	string sName;				//配置名称
	string sDescription;			//配置描述
	bool bActive;				//是否激活

	map<short,ATLVC> mATLVC;	//ATLVC
	map<short,ATIEC> mATIEC;	//ATIEC

	map<short,Display> mDisplay;		//LED显示屏表

	CALIBRATIONSTATUS CalibrationStatus;		//校正状态

}CONFIGURE;
//项目
typedef struct Project
{
	short nID;					//项目ID
	string sName;				//项目名称
	string sDescription;			//项目描述
	bool bActive;				//是否激活
	map<short,Configure> mConfigure;		//配置表
}PROJECT;

//工程的全局变量
typedef struct global_variable
{
	//升级类型1 1 - 引导程序升级(boot)  0 - 应用程序升级(app)
	bool bScanCardUpdateBoot;
	//升级类型2；0 - AK308G, 1 - AK308RG, 2 - AK302RG
	short nScanCardUpdateType;
	int nComLog;//写串口 1 - 写串口 ,0 - 不写串口
	int nCalibrationEmptyByte;//校正数据首部添加的空字节
	int nSpiAnswerDelay;			//启动结束设置包中，0x21、0x22、0x31、0x32有关SPI应答延时
	int nRefreshRateAdd;		//界面刷新率增加值

	short nSkinTheme;			//界面主题
	short nLanguage;			//界面语言
	bool bOperateLog;			//是否保存操作日志
	short nSaveDays;			//操作日志保存天数
	int nMinimizeType;			//最小化类型 0-最小化到任务栏 1-最小化到托盘
	int nCloseType;				//软件关闭类型 0-直接退出 1-程序不退出，但最小化到托盘
	int nAutoRun;				//软件开机自动启动 0-开机不自动启动 1-开机自动启动
	int nAutoRunToTray;			//软件自动启动到托盘 0-没有自动启动，或者自动启动，但不到托盘 1-自动启动到托盘

	int nCustomerversion;		//用户版本工程版本标识 0-工程 1-用户1 2-客户2
	int nCustomerversionSwitch;	//用户切换选择：0：专家模式（对应工程），1-用户1模式  2-用户2模式
	
	//版本类型 0：通用版本；
	//		   1：M3.5箱体特殊情况； 
	//         2：FS08箱体特殊情况
	//         3：SP10箱体特殊情况
	//         4：S8箱体特殊情况	
	VersionType nVersionType;

	string sExpertsPassword;	//专家密码（工程密码）
	string sSwitchTime;			//切换操作时间

	bool bNotconnectreturn;     // 加密狗

	bool bAotoOrToshiba;        //是否为东芝版 0- aoto 1 -toshiba

	long nPointDotInterval ;     //以毫秒为单位

	int nDeductBit;				//减去的色深位数
	short nDeadPointMax;		//最大坏点个数限制
	// add for USB by wanglq at 2014-07-16
	long nBundleSize;             //捆的大小，单位:byte
	long nBundleInterval;         //捆传输的时间间隔，单位:微秒
	int  nBundlePkNumber;         //捆的大小，单位：包

	//block保护配置参数值
	// 	打开扫描卡引导程序保护	低1位 B: 0：不使能；1：使能	1
	// 	关闭扫描卡引导程序保护	低2位 B: 0：不使能；1：使能	2
	// 	打开扫描卡校正数据保护	低3位 B: 0：不使能；1：使能	4	
	// 	关闭扫描卡校正数据保护	低4位 B: 0：不使能；1：使能	8
	// 	打开模组校正数据保护	低5位 B: 0：不使能；1：使能	16
	// 	关闭模组校正数据保护	低6位 B: 0：不使能；1：使能	32
	int nBlockProtectCfgValue;	//block保护配置参数值
}GLOBALVARIABLE;

//邮件发送端设置
struct EmailSendSet
{
	bool bEmailEnable;			//是否使用邮件发送报警功能	0：不使能，1：使能
	int iEmailSendTerminal;		// 0: 不发，1：服务器发，2：终端发，3：服务器和终端发
	string sDeviceID;			//设备编码

	string sSmtpAddress;		//SMTP邮件服务器地址
	int nPortNumber;			//SMTP邮件服务器端口号
	int nAuthenticationType;	//认证类型
	string sSendEmailAddress;	//发送用户邮箱地址
	string sLoginName;			//发送用户邮箱登录名
	string sLoginPassword;		//发送用户邮箱登录密码
	string sSendUserName;		//发送用户名称

	int nSendInterval;			//邮件重发时间间隔
};
//邮件接收端设置
struct EmailReceiveSet
{
	short nID;
	short nType;					//接收方式： TO，CC, BCC
	string sReceiveEmailAddress;//接收者邮箱地址
	string sReceiveUserName;	//接收者邮箱用户名
};
//vector<EmailReceiveSet> emailreceive;

#pragma endregion

#pragma region //BLL接口

//////////////////////////////////////////////////////////////////////////
///////////////////////////////BLL接口////////////////////////////////////
//////////////////////////////////////////////////////////////////////////

//获取错误代码
/*
功	能：获取错误代码（与SetLastError对应）
入	参:	
返回值：
错误代码
相关说明：
在调用某接口函数返回失败时（通常该函数有更关心的返回参数），
可通过此函数获取造成失败的错误代码
*/
Uint64 /*__stdcall*/ BLL_GetLastError();


typedef struct calib_proc_point
{
	short nID;					//模组在箱体里的编号，从左到右，从上到下;箱体级别时为箱体ID
	short nCanibetID;			//模组所在的箱体ID
	short nScanCardID;			//模组所在的扫描卡ID
	short nAddress;				//模组对应扫描卡的地址
	short nNetCardNo;			//模组对应网络卡的地址，S系列使用
	short nR;					//模组相对扫描卡的水平位置
	short nC;					//模组相对扫描卡的垂直位置

	short nRC;					//模组相对箱体的水平位置
	short nCC;					//模组相对箱体的垂直位置

	int nModPixelX;				//模组起始X坐标（相对于全屏）
	int nModPixelY;				//模组起始Y坐标（相对于全屏）

	string sInfo;				//相关信息，导出校正数据到文件时的文件路径，或模组序列号

	int nAtlvcID;				//连接ATLVC ID
	int nAtlvcPort;				//连接ATLVC端口
} CALIBPOINT;

struct OPERATEID
{
	short nProjectID;		//工程ID
	short nConfigID;		//配置ID
	short nDisplayID;		//显示屏ID
	short nAtlvcID;			//ATLVCID
	short nAtlvcPort;		//ATLVC Port
	short nCabinetID;		//箱体ID
	short nCabinetAddress;	//箱体地址
	CABINETPOS nCabinetPart;	//箱体的哪个部分，根据进线方式不同而不同
	short nScandCardID;		//扫描卡ID
	CALIBPOINT Module;		//模组

	int nOperateType;  //0-发送扫描卡参数 1-箱体风扇查找表,2-获取扫描卡参数,3-显示校正箱体ID
	string sAchieveInfo;		//完成提示说明
	HWND hWnd;	//传给窗体的句柄
};

typedef struct send_receive
{
	unsigned char ucData[CL_MAX_BUF_NUMBER];		//发送接收数据缓存
	short	nSndSize;								//发送字节长度
	long	nRcvSize;								//接收字节长度
	int		nNeedRcvPackSize;						//需要接收的包个数
	long	nErrorCode;								//应答错误码
}SENDRECEIVE;

typedef struct channel_queue
{
	string sMainID;			//主信道ID，项目ID_配置ID_ATLVC的ID,AK6升级时为级联第一张ATLVC的ID
	string sBackID;			//备份信道ID，,AK6升级时为级联第一张ATLVC备卡的ID
	short nMainAtlvcID;		//主卡ID
	short nBackAtlvcID;		//备卡ID -1 无备卡
}CHANNELQUEUE;
/*
返回值：
long：		错误代码	：当!=BLL_NO_ERROR时该回调函数将停止，返回成功或失败消息
int：		进度值		：<65535
OPERATEID	操作返回ID
*/
//设置图片回调函数
typedef void ( *SetPhotoCallback)(OPERATEID ,long ,int ,int );

typedef void ( *SetBootLogoCallback)(OPERATEID ,long ,int ,int );

//升级回调函数
//OPERATEID	操作返回ID; long - 错误码;int - 类型,int - 进度值
typedef void ( *UpdateCallback)(OPERATEID,long,int,UPDATE_HARDWARE_TYPE,int);

//发送连线图回调函数
//OPERATEID	操作返回ID; long - 错误码;int - 类型,HANDLE - 时间句柄
typedef void ( *SendConnectionCallback)(OPERATEID,long,short,int,HANDLE,bool &);

//发送箱体参数回调函数
//OPERATEID	操作返回ID; long - 错误码;int - 类型,int - 进度值
typedef void ( *SendCabinetParaCallback)(OPERATEID,long,int,int);

//保存箱体参数回调函数
typedef void ( *SaveCabinetParaCallback)(OPERATEID,long,int,int,SCANCARDPROC nProcType);

//一键发送参数回调函数
typedef void ( *OneKeySendCallback)(OPERATEID,long,int,int,eONEKEYSEND);

//发送校正数据回调函数
//int nType 发送类型
typedef void ( *SendCalibrationDataCallback)(int,DWORD,long,int,int,vector<short>&);

//监控回调函数
typedef void ( *MonitorCallback)(OPERATEID,long,DWORD lpMonitordata_handle);

//OPERATEID	操作返回ID; long - 错误码;int - 类型,int - 进度值 ,Version版本
typedef void ( *InquireScanCardVersionCallback)(OPERATEID,long,int,Version,SCANCARDPROC);

//OPERATEID	操作返回ID; long - 错误码
typedef void ( *SetCabinetColorTemperatureCallback)(OPERATEID ,long ,int ,int ,SCANCARDPROC )/*(OPERATEID,long)*/;

/////////////////////////////LED显示屏控制///////////////////////////////
//开关LED显示屏
/*
功	能：开关LED显示屏
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	：显示屏ID（-1：所有显示屏）
bPower		：true:开 false:关
返回值:
true	：成功
false	：失败
相关说明：通过nDisplayID查找相应链路
*/
long /*__stdcall*/ BLL_Power(short nProjectID,
							 short nConfigID,
							 short nDisplayID,
							 bool bPower);

//锁定、解除锁定LED显示屏
/*
功	能:	锁定、解除锁定LED显示屏
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	：显示屏ID（-1：所有显示屏）
bLock：true	: 锁定 false:解锁
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_Lock(short nProjectID,
							short nConfigID,
							short nDisplayID,
							bool bLock);

//切断、恢复LED显示屏信号
/*
功	能:	切断、恢复LED显示屏信号
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	：显示屏ID（-1：所有显示屏）
bOff		：true:切断 false:恢复
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_CutOff(short nProjectID,
							  short nConfigID,
							  short nDisplayID,
							  bool bOff);


/*
功	能:	设置无信号显示类型
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	：显示屏ID（-1：所有显示屏）
nType		：默认为0,0x0:黑屏，0x1：随机画面。0x2：图片
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_SetNoSingleDisp(short nProjectID,
										  short nConfigID,
										  short nDisplayID,
										  short nType);


//发送图片数据设置
/*
功	能:	切断、恢复LED显示屏信号
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	：显示屏ID

FileName	: 图片文件名及路径

回调函数：
cbFunc
返回值:
-1			: 失败
其它		：返回发送包数，设置进度
相关说明：
可中断
*/
long /*__stdcall*/ BLL_SetPhoto(short nProjectID,
								short nConfigID,
								short nDisplayID,
								string& sFileName,
								SetPhotoCallback cbFunc);
//设置开机Logo
long /*__stdcall*/ BLL_SetBootLogo(short nProjectID,
									   short nConfigID,
									   short nDisplayID,
									   string& sFileName,
									   SetBootLogoCallback cbFunc);
//中断发送图片数据设置
//中断结果在 SetPhotoCallback 回调中
long /*__stdcall*/ BLL_InterruptSetPhoto(short nProjectID,
										 short nConfigID,
										 short nDisplayID);


//设置LED显示屏亮度
/*
功	能:	设置LED显示屏亮度
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	：显示屏ID（-1：所有显示屏）
nBrightness	：亮度值(0-100)
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_SetBright(short nProjectID,
								 short nConfigID,
								 short nDisplayID, 
								 int nBrightness);

//设置LED显示屏色温（自定义）
/*
功	能:	设置LED显示屏色温
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	：显示屏ID（-1：所有显示屏）
nCrTempID	：色温编号（0-7级）
nHighLowGap	：高、低亮场位数差
nGrayEnhanceMode：灰度增强方式
lpRGB		：红绿蓝三色白平衡值		
返回值:
true：成功
false：失败
*/
 
// long /*__stdcall*/ BLL_SetColorTemperatureRGB(short nProjectID,
// 											  short nConfigID,
// 											  short nDisplayID,
// 											  int nType,
// 											  int nRed, int nGreen, int nBlue);
//对全屏
//由UI传入
//设置其中3个RGB值，另外3个RGB值不变
long /*__stdcall*/ BLL_SetColorTemperatureRGB(short nProjectID,
												  short nConfigID,
												  short nDisplayID,
												  int nCrTempID,
												  short nHighLowGap,
												  short nGrayEnhanceMode,
												  LPCOLOURRGB lpRGB);

long /*__stdcall*/ BLL_SetColorTemperatureRGBEx(short nProjectID,
												  short nConfigID,
												  short nDisplayID,
												  int nCrTempID,
												  short nHighLowGap,
												  short nGrayEnhanceMode,
												  LPCOLOURRGB lpRGB);


//设置LED显示屏色温（系统预设）
/*
功	能:	设置LED显示屏色温
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	：显示屏ID（-1：所有显示屏）
nHighLowGap	：高、低亮场位数差
nGrayEnhanceMode：灰度增强方式
nColorTemperIndex：色温编号（0-7级）
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_SetColorTemperature(short nProjectID,
										   short nConfigID,
										   short nDisplayID,
										   short nHighLowGap,
										   short nGrayEnhanceMode,
										   short nColorTemperIndex);

/*
功	能:	设置箱体扫描卡色温值
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	：显示屏ID（-1：所有显示屏）
nCabibet	:调节箱体的个数 0 - 全屏
bCurrent	：调节方式（false：灰度调节 true：电流调节）
nCrTempID：色温编号（0-7级）
返回值:
true	：成功
false	：失败*/

long /*__stdcall*/ BLL_SetCabinetColorTemperature(short nProjectID,
													  short nConfigID,
													  short nDisplayID,
													  short nCabibet,
													  bool bCurrent,
													  int nCrTempID,
													  map<short,COLOURRGB> & vCabinet,
													  short nHighLowGap,
													  short nGrayEnhanceMode,
													  SendCabinetParaCallback cbFunc);

//设置LED显示屏GAMMA值
/*
功	能:	设置LED显示屏GAMMA值
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	：显示屏ID（-1：所有显示屏）
sGammaData		：Gamma数据
nColorIndex	：颜色编号 Red-0  Green-1 Blue-2 RGB-3
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_SetGammaTable(short nProjectID,
									 short nConfigID,
									 short nDisplayID,
									 GAMMADATA &sGammaData, 
									 short nColorIndex);

//hh
//设置色空间转换命令
/*
功	能:	设置LED显示屏色空间转换参数值
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	：显示屏ID（-1：所有显示屏）
tColGMutStruct		：色空间转换参数值结构体

返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_SetColGamut(short nProjectID,
									 short nConfigID,
									 short nDisplayID,
									 COLGMUTDATA& tColGMutStruct 
									);


//设置色空间转换命令
/*
功	能:	设置LED显示屏色空间转换是否使能
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	：显示屏ID（-1：所有显示屏）
tColGMutStruct		：色空间转换参数值结构体

返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_SetColGamutEnable(short nProjectID,
									 short nConfigID,
									 short nDisplayID,
									 COLGMUTDATA& tColGMutStruct 
									);

////////////////////////////////ATLVC操作/////////////////////////////////

//获取 AK6 LED视频控制器 采集卡的状态数据（包括分辨率）
/*
功	能:	获取 AK6 LED视频控制器 采集卡的状态数据
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nATLVCAK6ID	: ATLVCAK6 ID

sAK6Status : 返回ATLVCAK6Status状态的结构 
返回值:
true	：成功
false	：失败
相关说明：通过nATLVCAK6ID、nCollectCardID查找相应链路
*/
//AK6 LED视频控制器 单个采集卡状态									  
long /*__stdcall*/ BLL_ReadATLVCAK6Status(short nProjectID,
										  short nConfigID,
										  short nATLVCAK6ID,
										  short nAddressSecond,
										  ATLVCAK6Status & sAK6Status);
										  
//获取 AK6 LED视频控制器 采集卡的版本
/*
功	能:	获取 AK6 LED视频控制器 采集卡的版本
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nATLVCAK6ID	: ATLVCAK6 ID

version	    ：返回版本结构体

返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_ReadATLVCAK6Version(short nProjectID,
										   short nConfigID,
										   short nATLVCAK6ID,
										   short nAddressSecond,
										   Version & sVersion);

//zhangjj004 获取ATIEC的版本
long BLL_ReadATIECVerson(short nProjectID,
						 short nConfigID,
						 ATIEC &atiec);
										   
/*
功 能:	获取采集卡的温湿度
入 参: 
nProjectID		：工程ID 
nConfigID		：配置ID 
nATLVCAK6ID		：采集卡ID
nAddressSecond	：级联地址
ATLVCAK6Status	：采集卡状态

ATLVCAK6Status	: 返回采集卡状态
返回值:
true	：成功
false	：失败
*/
long BLL_ReadATLVCTemperatureAndHumity(short nProjectID,
									   short nConfigID,
									   short nATLVCAK6ID,
									   short nAddressSecond,
									   ATLVCAK6Status & sAK6Status);
//复位 AK6 LED视频控制器 采集卡
/*
功	能:	复位 AK6 LED视频控制器 采集卡
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nATLVCAK6ID	: ATLVCAK6 ID

nCollectCardID:采集卡ID（0：采集卡1，1：采集卡2）
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_ResetATLVCAK6(short nProjectID,
									 short nConfigID,
									 short nATLVCAK6ID);


//设置 AK6 LED视频控制器 采集卡 EDID（包括分辨率），针对AK100
/*
功	能:	设置 AK6 LED视频控制器 采集卡 EDID
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nATLVCAK6ID	: ATLVCAK6 ID

sEDIDInfo	: EDID内容
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_SetATLVCAK6EDIDInfo(short nProjectID,
											   short nConfigID,
											   short nATLVCAK6ID,
											   short nATLVCType,
										   unsigned char *sEDIDInfo);


//设置 AK6 LED视频控制器 采集卡 EDID（包括分辨率），针对AK1000
/*
功	能:	设置 AK6 LED视频控制器 采集卡 EDID
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nATLVCAK6ID	: ATLVCAK6 ID

sEDIDInfo	: EDID内容
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_SetATLVCAK6UpdateEDIDInfo(short nProjectID,
											   short nConfigID,
											   short nATLVCAK6ID,
											   short nATLVCType,
											   short nATLVCSecondAdrss,
											   unsigned char *sEDIDInfo);


//升级 AK6 LED视频控制器 采集卡
/*
功	能:	升级 AK6 LED视频控制器 采集卡
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nATLVCAK6ID	: ATLVCAK6 ID

sFileName	: 升级文件名及路径
nCollectCardID:采集卡ID（0：采集卡1，1：采集卡2）

回调函数：
cbFunc
返回值:
-1			: 失败
其它		：返回发送包数，设置进度
相关说明：
无中断
*/
long /*__stdcall*/ BLL_UpdateATLVCAK6(short nProjectID,
									  short nConfigID,
									  short nATLVCAK6ID,
									  char *sFileName,
									  short nCollectCardID,
									  UpdateCallback cbFunc);

//AK6 LED视频控制器 采集卡 带载设置（对输入视频的截取）
/*
功	能:	AK6 LED视频控制器 采集卡 带载设置
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nATLVCAK6ID	: ATLVCAK6 ID
nAddressSecond： 级联地址
rtRegion	: 带载区域
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_SetATLVCAK6Region(short nProjectID,
										 short nConfigID,
										 short nATLVCAK6ID,
										 short nAddressSecond,
										 RECT rtRegion);

/*
功	能:	获取存在的串口号与个数
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nATLVCAK6ID	: ATLVCAK6 ID
nAddressSecond： 级联地址

vCOMPort	：返回获取的串口号向量列表
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_GetCOMPort(vector<short> & vCOMPort);

/*
功	能:	AK6 LED视频控制器 采集卡 匹配USB设备
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nATLVCAK6ID	: ATLVCAK6 ID
nAddress	: 采集卡地址
nAddressSecond： 级联地址
nTotalPortCount		：USB设备总数
nMatchedPort	：匹配成功的USB设备
返回值:
true	：成功
false	：失败
Add by wanglq at 2014-07-04
*/
long /*__stdcall*/ BLL_ATLVCAK6CardMatchUsbPort(short nProjectID,
													short nConfigID,
													short nATLVCAK6ID,
													short nAddress,
													short nAddressSecond,
													short & nTotalPortCount,
													short & nMatchedPort);

/*
功	能:	AK6 LED视频控制器 采集卡 匹配串口
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nATLVCAK6ID	: ATLVCAK6 ID
nAddress	: 采集卡地址
nAddressSecond： 级联地址
nComID		：保存的串口列表
nComCount	：保存的串口个数
nUsedCom	：能够正常使用的串口号
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_ATLVCAK6CardMatchPort(short nProjectID,
												 short nConfigID,
												 short nATLVCAK6ID,
												 short nAddress,
												 short nAddressSecond,
												 short * nComID,
												 short & nComCount,
												 short & nUsedCom);
	

////////////////////////////////继电器操作/////////////////////////////////

//打开、关闭ATIEC继电器（继电器手动模式控制）
/*
功	能:	打开、关闭ATIEC继电器
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nATIVCID	: ATIEC ID
nATIECAdress: ATIEC 地址（0XF9，0xFA）
nRelayDeviceID:继电器ID（1～4）
bOpen		：true:开 false:关
返回值:
true	：成功
false	：失败
相关说明：通过nATLVCAK6ID查找相应链路
*/
long /*__stdcall*/ BLL_OperateATIECRelay(short nProjectID,
											 short nConfigID,
											 short nATIECID,
											 short nATIECAdress,
											 short nRelayDeviceID,
											 bool bOpen);
//设置ATIEC继电器属性（继电器属性）
/*
功	能:	设置ATIEC继电器属性
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nATIVCID	: ATIEC ID
nATIECAdress: ATIEC 地址（0XF9，0xFA）
nRelayDeviceID:继电器ID（1～4）
bOverHeatOff	：是否启动超温断电
bOverHumidityOff：是否启动潮湿断电
bSmogOff		：是否启动烟雾断电
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_SetATIECRelayAttribute(short nProjectID,
												  short nConfigID,
												  short nATIECID,
												  short nATIECAdress,
												  short nRelayDeviceID,
												  bool bOverHeatOff,
												  bool bOverHumidityOff,
												  bool bSmogOff);

//设置ATIEC继电器门限（继电器自动模式门限值）
/*
功	能:	设置ATIEC继电器属性
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nATIVCID	: ATIEC ID
nATIECAdress: ATIEC 地址（0XF9，0xFA）
nRelayDeviceID:继电器ID（1～4）

fTemperatureMin	:温度下限值,精确到0.1
fTemperatureMax	:温度上限值,精确到0.1
fHumidityMin	:湿度下限值,精确到0.1
fHumidityMax	:湿度上限值,精确到0.1
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_SetATIECRelayThreshold(short nProjectID,
												  short nConfigID,
												  short nATIECID,
												  short nATIECAdress,
												  short nRelayDeviceID,
												  float fTemperatureMin,
												  float fTemperatureMax,
												  float fHumidityMin,
												  float fHumidityMax);

/*
功	能:	查询监控卡的版本
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nATIVCID	: ATIEC ID
sAtiec		: ATIEC信息
返回值:
true	：成功
false	：失败
*/
long BLL_GetATIECVersion(short nProjectID,short nConfigID,short nATIECID,ATIEC & sAtiec);

//打开、关闭箱体继电器（继电器手动模式控制）
/*
功	能:	打开、关闭箱体继电器
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nAtlvcID	: ATLVCAK6 ID
nAddress: 箱体地址(1-248)
nRelayDeviceID:继电器ID（1～4）
bOpen		：true:开 false:关
返回值:
true	：成功
false	：失败
相关说明：通过nATLVCAK6ID查找相应链路
*/
long /*__stdcall*/ BLL_OperateCabinetRelay(short nProjectID,
										   short nConfigID,
										   short nAtlvcID,
										   short nAddress,
										   short nRelayDeviceID,
										   bool bOpen);
/*
功	能:	设置箱体继电器属性（继电器属性）
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nAtlvcID	: ATLVCAK6 ID
nAddress: 箱体地址(1-248)
nRelayDeviceID:继电器ID（1～4）
bOverHeatOff	：是否启动超温断电
bOverHumidityOff：是否启动潮湿断电
bSmogOff		：是否启动烟雾断电
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_SetCabinetRelayAttribute(short nProjectID,
												short nConfigID,
												short nAtlvcID,
												short nAddress,
												short nRelayDeviceID,
												bool bOverHeatOff,
												bool bOverHumidityOff,
												bool bSmogOff);

//设置箱体继电器门限（继电器自动模式门限值）
/*
功	能:	设置箱体继电器属性
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nAtlvcID	: ATLVCAK6 ID
nAddress: 箱体地址(1-248)
nRelayDeviceID:继电器ID（1～4）
fTemperatureMin	:温度下限值,精确到0.1
fTemperatureMax	:温度上限值,精确到0.1
fHumidityMin	:湿度下限值,精确到0.1
fHumidityMax	:湿度上限值,精确到0.1
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_SetCabinetRelayThreshold(short nProjectID,
												short nConfigID,
												short nAtlvcID,
												short nAddress,
												short nRelayDeviceID,
												float fTemperatureMin,
												float fTemperatureMax,
												float fHumidityMin,
												float fHumidityMax);



//升级 ATIEC（ATIEC扫描卡、ATIEC监控卡（多功能卡））
/*
功	能:	升级 ATIEC
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nATIVCID	: ATIEC ID
nATIECAdress: ATIEC 地址（0XF9，0xFA）

nType		: 类型（0：ATIEC扫描卡、1：ATIEC监控卡）
sFileName	: 升级文件名及路径

返回值:
-1			: 失败
其它		：返回发送包数
相关说明：
无中断
*/
long /*__stdcall*/ BLL_UpdateATIEC(short nProjectID,
								   short nConfigID,
								   short nATIECID,
								   short nATIECAdress,
								   int nType,
								   char *sFileName,
								   UpdateCallback cbFunc);


//发送DMX控制通道信息
/*
功	能:	发送DMX控制通道信息
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	: LED ID
nDMXCtrlChannelInfo	: DMX控制通道信息
返回值:
-1			: 失败
其它		：返回发送包数
相关说明	：无中断
*/
long /*__stdcall*/ BLL_SendDMXCtrlChannelInfo(short nProjectID, short nConfigID, short nDisplayID, DMXCtrlChannelInfo nDMXCtrlChannelInfo);


/*
功	能:	发送DMX控制通道信息
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	: LED ID
nDMXCtrlChannelInfo	: DMX控制通道信息
返回值:
-1			: 失败
其它		：返回发送包数
相关说明	：无中断
*/
long /*__stdcall*/ BLL_SendDMXCtrlChannelInfoEx(short nProjectID, short nConfigID, short nATLVCID, short nATLVCSecondAdrss,
												  DMXCtrlChannelInfo nDMXCtrlChannelInfo);



//发送DMX控制台色温查找表
/*
功	能:	发送DMX控制台色温查找表
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	: LED ID
nCnt		: 数量
nRed		: 红色色温值列表
nGreen		: 绿色色温值列表
nBlue		: 蓝色色温值列表
nControl	: 电流增益范围值列表
返回值:
-1			: 失败
其它		：返回发送包数
相关说明	：无中断
*/
long /*__stdcall*/ BLL_SendGraytable(short nProjectID, short nConfigID, short nDisplayID,
										 int nCnt, int *nRed, int *nGreen, int *nBlue, int *nControl,short nType);

/*
功	能:	发送DMX控制台色温查找表
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nATLVCID	: 采集卡组件ID
nATLVCSecondAdrss：采集卡级联状态地址
nCnt		: 数量
nRed		: 红色色温值列表
nGreen		: 绿色色温值列表
nBlue		: 蓝色色温值列表
nControl	: 电流增益范围值列表
返回值:
-1			: 失败
其它		：返回发送包数
相关说明	：无中断
*/
long /*__stdcall*/ BLL_SendGraytableEx(short nProjectID, short nConfigID, short nATLVCID, short nSendCardIndex, short naDisplayID[CHANNEL_PORT_MAX], short nType);

//保存DMX参数
/*
功	能:	保存DMX参数
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	: LED ID
返回值:
-1			: 失败
其它		：返回发送包数
相关说明	：无中断
*/
long /*__stdcall*/ BLL_SaveDMXPara(short nProjectID, short nConfigID, short nDisplayID, short nType);

//保存DMX参数
/*
功	能:	保存DMX参数
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nATLVCID	: 采集卡组件ID
nATLVCSecondAdrss：采集卡级联状态地址
nSaveType	：保存类型
返回值:
-1			: 失败
其它		：返回发送包数
相关说明	：无中断
*/
long /*__stdcall*/ BLL_SaveDMXParaEx(short nProjectID, short nConfigID, short nATLVCID, short nATLVCSecondAdrss, short nSaveType);


///////////////////////////////LED显示屏监控//////////////////////////////
//启动、停止监控
//（包括LED显示屏监控、箱体监控、箱体电压监控、箱体功率监控、箱体逐点检测）
/*
功	能:	启动、停止LED显示屏监控
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	: 显示屏ID（-1：所有显示屏）

bStart		: 启动\停止标识 true-启动，false-停止
nIndex		: 监控项
返回值:
true	：成功
false	：失败
*/

//（LED显示屏、箱体）常规监控：（亮度、温度、湿度、是否烟雾报警、左右风扇状态、4路继电器状态、监控卡版本）
//箱体电压监控：箱体（扫描卡）5路电压值
//箱体功率监控：箱体（扫描卡）功率值
//箱体逐点检测：箱体（扫描卡）出错灯个数及情况
#define M_DISPLAY_CUSTOM 0x0001			//LED显示屏监控
#define M_DISPLAY_CONNECT_STATUS 0x0002	//LED显示屏连接状态
#define M_DISPLAY_BACKUP_STATUS 0x0004	//LED显示屏备份状态
#define M_CABINET_CUSTOM 0x0008			//箱体（扫描卡）的常规监控
#define M_CABINET_CONNECT_STATUS 0x0010	//箱体（扫描卡）连接状态（查询版本）
#define M_CABINET_VOLTAGE 0x0020		//箱体（扫描卡）5路电压值
#define M_CABINET_POWER   0x0030		//箱体（扫描卡）功率值
#define M_CABINET_PBP     0x0040		//箱体（扫描卡）逐点检测

/*
1)	线程1：LED显示屏常规监控
2)	线程2：LED显示屏连接状态（查询ATLVC连接状态）
3)	线程3：LED显示屏备份状态（查询备份箱体与ATLVC连接状态）
4)	线程4：箱体（扫描卡）的常规监控
5)	线程5：箱体（扫描卡）连接状态（查询版本）
6)	线程6：箱体（扫描卡）5路电压值
7)	线程7：箱体（扫描卡）功率值
8)	线程8：箱体（扫描卡）逐点检测*/

long /*__stdcall*/ BLL_SetMonitor(short nProjectID,
								  short nConfigID,
								  short nDisplayID,
								  MonitorCallback cbFunc,
								  bool bStart=true,
								  int nIndex=M_DISPLAY_CUSTOM
								  |M_DISPLAY_CONNECT_STATUS
  								  |M_DISPLAY_BACKUP_STATUS|M_CABINET_CUSTOM
  								  |M_CABINET_CONNECT_STATUS|M_CABINET_VOLTAGE|M_CABINET_POWER
  								  |M_CABINET_PBP);

//设置监控间隔时间（全局）
/*
功	能:	设置监控间隔时间
入	参:	
nInterval ：间隔时间（单位：S <3600）
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_SetMonitorInterval(int nInterval);

//获取 LED显示屏 常规监控数据
/*
功	能: 获取 LED显示屏 常规监控数据
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	: 显示屏ID

lpMonitordata：指向MONITORDATA
返回值:
相关说明：
UI使用轮巡方式进行读取（有一个轮巡时间）
*/
void /*__stdcall*/ BLL_GetMonitorData_Display(short nProjectID,
											  short nConfigID,
											  short nDisplayID,
											  map<short,LPMONITORDATA> lpMonitordata);

//获取 LED显示屏 指定箱体 所有监控数据
/*
功	能: 获取 LED显示屏 指定箱体 所有监控数据
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	: 显示屏ID
nCabinetID	: 箱体ID

lpMonitordata：指向MONITORDATA指针容器（1个箱体有多个扫描卡）
返回值:

*/
void /*__stdcall*/ BLL_GetMonitorData_Cabinet(short nProjectID,
											  short nConfigID,
											  short nDisplayID,
											  short nCabinetID,
											  map<short,map<short,LPMONITORDATA> > lpMonitordata);

//启动、停止自动亮度调节
/*
功	能:	设置自动亮度调节
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	: 显示屏ID

bStart		: 启动\停止标识 true-启动，false-停止
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_SetAutoBright(short nProjectID,
									 short nConfigID,
									 short nDisplayID,
									 bool bStart);
//设置自动亮度调节时间间隔
/*
功	能:	设置自动亮度调节时间间隔
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	: 显示屏ID

nInterval	：间隔时间（单位：S <3600）
返回值:
true	：成功
false	：失败
*/
long /*__stdcall*/ BLL_SetAutoBrightInterval(short nProjectID,
											 short nConfigID,
											 short nDisplayID,
											 int nInterval);

long /*__stdcall*/ BLL_SetTimingBright(short nProjectID,
										 short nConfigID,
										 short nDisplayID,
										 bool bStart);
//////////////////////////LED显示屏连线图设计操作/////////////////////////
//预览连线图（发送到LED显示屏）
/*
功	能:	预览连线图
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	: 显示屏ID

回调函数：
cbFunc
返回值:
-1			: 失败
相关说明：
无中断
*/
long /*__stdcall*/ BLL_PreviewConnection(short nProjectID,
										 short nConfigID,
										 short nDisplayID,
										 SendConnectionCallback cbFunc);

//应用连线图（保存到LED显示屏0x49-0x01）
long /*__stdcall*/ BLL_SaveConnection(short nProjectID,
									  short nConfigID,
									  short nDisplayID,
									  short nCabinetID,
									  SendConnectionCallback cbFunc);


//////////////////////////箱体（扫描卡）参数发送与保存操作////////////////
//发送箱体（扫描卡））相关参数（广播、点播）
/*
功	能:	发送箱体（扫描卡））相关参数
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	: 显示屏ID

nCabinetID	: （-1：全屏）
回调函数：
cbFunc
返回值:
-1			: 失败
其它		：返回MAX进度
相关说明：
无中断
*/

//发送箱体参数
long /*__stdcall*/ BLL_SetCabinetParam(short nProjectID,
										   short nConfigID,
										   short nDisplayID,
										   vector<short> & vCabinetID,
										   SendCabinetParaCallback cbFunc,
										   SaveCabinetParaCallback cbSaveFunc,
										   SendConnectionCallback cbFuncConnection);

//一键发送参数
// long /*__stdcall*/ BLL_OneKeySend( short nProjectID,
// 									   short nConfigID,
// 									   short nDisplayID,
// 									   vector<short> & vCabinetID,
// 									   SendCabinetParaCallback cbFunc,
// 									   OneKeySendCallback cbOneKeySendFunc,
// 									   SaveCabinetParaCallback cbSaveFunc);

long /*__stdcall*/ BLL_OneKeySend(short nProjectID,
									  short nConfigID,                        
									  short nDisplayID,                       
									  vector<short> & vCabinetID,             
									  SendCabinetParaCallback cbFunc,         
									  SaveCabinetParaCallback cbSaveFunc,     
									  SendConnectionCallback cbFuncConnection,
									  OneKeySendCallback cbOneKeySendFunc);


//发送扫描卡参数
long /*__stdcall*/ BLL_SetScanCardParam(short nProjectID,
											short nConfigID,
											short nDisplayID,
											vector<short> & vCabinetID,
											SendCabinetParaCallback cbFunc,
											bool bSend);

//预览MBI5153_E,5155寄存器参数
long /*__stdcall*/ BLL_PreviewRegMBI515XParam(short nProjectID,
											short nConfigID,
											short nDisplayID,
											Drive_ic_reg sDrive_ic_Reginfo,
											vector<short> & vCabinetID,
											SendCabinetParaCallback cbFunc,
											bool bSend);

//查询版本号，扫描卡，HUB
long /*__stdcall*/ BLL_StartInquireVersion(short nProjectID,
											   short nConfigID,
											   short nDisplayID,
											   vector<short> & vCabinetID,
											   SCANCARDPROC nType,
											   InquireScanCardVersionCallback cbFunc);
/*发送箱体风扇和自动控制*/
long BLL_SetCabinetRelayParam(short nProjectID,
								  short nConfigID,
								  short nDisplayID,
								  vector<short> & vCabinetID,
								  SaveCabinetParaCallback cbSaveFunc);

enum interrup_thread
{
	INTERRUP_SCAN_CARD,			//扫描卡逻辑类线程停止
	INTERRUP_CONNECTION,		//连线图逻辑类线程停止
	INTERRUP_CALIBRATION,		//校正逻辑类线程停止
	INTERRUP_UPDATA,		//升级线程停止
	INTERRUP_COLORTEMP,     //色温线程停止
	INTERRUP_BOOTLOGO
};

//中断线程
long /*__stdcall*/ BLL_InterruptThread(interrup_thread nType);

long /*__stdcall*/ BLL_UpdataHandware(short nProjectID,
									  short nConfigID,
									  short nDisplayID,
									  short nATLVCID,
									  map<short,bool> & mSelCabinet,
									  vector<short> & vSelectedCardNum,
									  char * sFileName,
									  UPDATE_HARDWARE_TYPE nUpdataType,
									  char * sExtension,
									  string sAtlec,
									  UpdateCallback cbFunc);

//初始化发送（从箱体配置文件中读取初始化）
//发送LED显示屏（箱体（扫描卡））相关参数（包含多种）
//首次发送（对刚搭建起的LED显示屏，箱体参数均未进行设置，可调用此接口）
/*
功	能:	发送LED显示屏（箱体（扫描卡））其它数据包
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	: 显示屏ID

nCabinetID	: （-1：全屏）
nIndex		: 发送项
回调函数：
cbFunc
返回值:
-1			: 失败
其它		：返回MAX进度
相关说明：
无中断
*/

#define PARAM_CORRELATION	0x0001	//（箱体（扫描卡））相关参数
#define PARAM_OPERATION		0x0002	//运算处理
#define PARAM_VIDEO			0x0004	//读取视频地址
#define PARAM_CALIBRATION   0x0008	//读取校正数据地址
#define PARAM_LINKTABLE		0x0010	//走线参数
#define PARAM_HUB			0x0020	//HUB走线查找、HUB参数
#define PARAM_GAMMA			0x0030	//伽马值（设置值，默认RGB）
#define PARAM_ENABLE_CALIBRATION 0x0040//校正使能（设置值，默认不使能）
#define PARAM_LOCK			0x0050	//解屏锁屏（设置值，解屏）
#define PARAM_BRIGHTNESS	0x0060	//亮度（设置值，50%）
// 
long /*__stdcall*/ BLL_SetCabinetParamEx(short nProjectID,
									   short nConfigID,
									   short nDisplayID,
									   short nCabinetID,
									   SendCabinetParaCallback cbFunc,
									   int nIndex=PARAM_CORRELATION
									   |PARAM_OPERATION|
   									   PARAM_VIDEO|PARAM_CALIBRATION|PARAM_LINKTABLE|
   									   PARAM_HUB|PARAM_ENABLE_CALIBRATION);

//LED显示屏（箱体（扫描卡））参数存储
/*
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	: 显示屏ID,-1：所有屏

nType		: save_init_cabinet发送类型
cbFunc		：回调函数
			  
返回值:
-1			: 失败
其它		：返回MAX进度

相关说明：
有中断*/


long /*__stdcall*/ BLL_SaveInitCabinetParam(short nProjectID,
										short nConfigID,
										short nDisplayID,
										vector<short> & vCabinetID,
										SCANCARDPROC nType,
										bool bDefaul,
										SaveCabinetParaCallback cbFunc);



///////////////////////////////自定义箱体操作/////////////////////////////
//在进行自定义箱体向导时，需添加（项目-配置-LED1-ATLVCAK61）设置采集卡1的连接属性
//自定义箱体默认发送	为项目1-配置1-LED1-ATLVCAK61-发送卡1的U口所带的模组

//模组参数设置
/*
功	能:	模组参数设置
入	参:	
sCabinet	: Cabinet结构
返回值:
true	：成功
false	：失败
相关说明：
会设置BLL_GetLastError
实现：
0x40-0Bh
0x40-00h~05h
*/
long /*__stdcall*/ BLL_SetModuleALLPara(CABINET sCabinet);


//判断数据线极性
/*
功	能:	判断数据线极性
入	参:	
sCabinet	: Cabinet结构
返回值:
true	：成功
false	：失败
相关说明：
会设置BLL_GetLastError
实现：
0x40-00h
*/
long /*__stdcall*/ BLL_SetModuleDataPolarity(CABINET sCabinet);

//判断OE极性
//实现：0x40-00h
long /*__stdcall*/ BLL_SetModuleOEPolarity(CABINET sCabinet);
//判断数据线定位
//实现：0x40-00h
long /*__stdcall*/ BLL_SetModuleDataLineCtrl(CABINET sCabinet);

//发送箱体参数
//应用：
//判断扫描方式（PARAM_CORRELATION|PARAM_OPERATION|PARAM_VIDEO|PARAM_CALIBRATION|
//PARAM_GAMMA|PARAM_ENABLE_CALIBRATION|PARAM_LOCK|PARAM_BRIGHTNESS）
//判折方式（PARAM_LINKTABLE）
/*
功	能:	判断扫描方式
入	参:	
sCabinet	: Cabinet结构
nIndex		: 发送项
回调函数：
cbFunc
返回值:
-1			: 失败
其它		：返回MAX进度
相关说明：
无中断
*/

long /*__stdcall*/ BLL_SetCabinetParamEx_(CABINET sCabinet,SendCabinetParaCallback cbFunc,int nIndex =PARAM_CORRELATION);


//////////////////////////////////屏幕校正////////////////////////////////

enum CALIBRATION_LEVEL
{
	CALIBRATION_LEVEL_DISPLAY,							//全屏级别设置
	CALIBRATION_LEVEL_CABINET,							//箱体级别设置
	CALIBRATION_LEVEL_MODULE,							//模组级别设置

};

enum CALIBRATION_TYPE
{
	CALIBRATION_MODULE_2_SPI,						//启动读取模组的校正数据到扫描卡
	CALIBRATION_SPI_2_MODULE,						//启动读取扫描卡的校正数据到模组

	CALIBRATION_CABINET_BRIGHTNESS,					//箱体校正，发送模组亮度校正数据
	CALIBRATION_CABINET_CHROMINANCE,				//箱体校正，发送模组色度校正数据
	CALIBRATION_CABINET_BRIGHTNESS_SELECT_FILE,			//箱体校正，发送亮度校正数据(选择文件目录):读取序列号，查找文件BCD，发送亮度校正数据

	CALIBRATION_CABINET_BRIGHTNESS_READ,					//箱体校正，读取模组亮度校正数据
	CALIBRATION_CABINET_CHROMINANCE_READ,				//箱体校正，读取模组色度校正数据

	CALIBRATION_CABINET_CHROMINANCE_LINE_TUNING,	//箱体校正，色度校正亮暗线微调，从默认数据库读取(paradata.mdb)
	CALIBRATION_CABINET_BRIGHTNESS_LINE_TUNING,		//箱体校正，亮度校正亮暗线微调，从默认数据库读取(paradata.mdb)
	CALIBRATION_CABINET_BRIGHTNESS_MODULE_TUNING,		//箱体校正，亮度校正逐点微调，从BCD文件读取

	CALIBRATION_CHROMINANCE_SELECT_FILE,				//发送色度校正数据(选择文件目录):读取序列号，查找文件CCD，发送色度校正数据
	CALIBRATION_CHROMINANCE_MODULE_TUNING,				//色度校正逐点微调，从CCD文件读取
	CALIBRATION_READ_MODULE_SERIAL,				//读取模组序列号
	CALIBRATION_READ_CALIBRATIONID_TO_DISPLAY,	//读取校正ID显示到箱体			

	CALIBRATION_DISPLAY_CHROMINANCE,				//全屏校正，发送模组色度校正数据
	CALIBRATION_DISPLAY_CHROMINANCE_SELECT_FILE,				//全屏校正，发送模组色度校正数据(选择文件目录):读取序列号，查找文件MCD，发送色度校正数据
	CALIBRATION_DISPALY_CHROMINANCE_LINE_TUNING,	//全屏校正，色度校正亮暗线微调，从默认数据库读取(AOTOCorrectionData.mdb)
	CALIBRATION_DISPALY_CHROMINANCE_MODULE_TUNING,	//全屏校正，色度校正模组微调，从MCD文件读取

	CALIBRATION_FF_MODULE_2_SPI,					//读模组的校正数据到扫描卡,广播
	CALIBRATION_FF_SPI_2_MODULE,					//写扫描卡的校正数据到模组，广播
	CALIBRATION_FF_TEST_CHROMINANCE,				//发送色度校正模拟数据,广播
	CALIBRATION_FF_TEST_BRIGHTNESS,					//发送亮度校正模拟数据,广播

	CALIBRATION_TRANS_DB_TO_FILE_BRIGHTNESS,		//导出亮度校正数据到文件BCD
	CALIBRATION_TRANS_DB_TO_FILE_CHEOMINANCE,			//导出亮度校正数据到文件CCD
	CALIBRATION_TRANS_DB_TO_FILE_CHEOMINANCE_DISPLAY,			//导出亮度校正数据到文件MCD,全屏校正

	CALIBRATION_SPI_2_MOD_CHROM_LINE_TUNING,				//亮暗线微调，保存，色度校正，生成新数据库(paradata.mdb)
	CALIBRATION_SPI_2_MOD_BRIGHT_LINE_TUNING,				//亮暗线微调，保存，亮度校正，生成新数据库(paradata.mdb)
	CALIBRATION_SPI_2_MOD_CHROM_MODULE_TUNING,				//模组微调，保存，色度校正，生成CCD微调文件
	CALIBRATION_SPI_2_MOD_BRIGHT_MODULE_TUNING,				//模组微调，保存，亮度校正，生成BCD微调文件

	CALIBRATION_SPI_2_MOD_CHROM_MODULE_TUNING_DISPLAY,		//模组微调，保存，色度校正，生成MCD微调文件,全屏校正
	CALIBRATION_SPI_2_MOD_CHROM_LINE_TUNING_DISPLAY,		//亮暗线微调，保存，色度校正，生成新数据库(paradata.mdb)，全屏校正

	CALIBRATION_TXT_DIVISE_CHROMINANCE,					//色度TXT数据文本分割
	CALIBRATION_TXT_DIVISE_BRIGHTNESS,					//亮度TXT数据文本分割

	CALIBRATION_SEND_LINE_COEFF,			//发送亮暗线校正系数

	CALIBRATION_ENABLE_CALIB,			//发送发开单点校正功能
	CALIBRATION_DISENABLE_CALIB,			//发送关闭单点校正功能

	CALIBRATION_GET_CHROMINANCE_DATA_DISPLAY,		//获取色度校正数据，数据库,全屏校正
	CALIBRATION_GET_CHROMINANCE_DATA,		//获取色度校正数据，数据库,箱体校正
	CALIBRATION_GET_BRIGHTNSEE_DATA,		//获取亮度校正数据，数据库

	CALIBRATION_GET_CHROMINANCE_DATA_FILE_DISPLAY,		//获取色度校正数据，MCD文件
	CALIBRATION_GET_CHROMINANCE_DATA_FILE,		//获取色度校正数据，CCD文件
	CALIBRATION_GET_BRIGHTNSEE_DATA_FLLE,		//获取亮度校正数据，BCD文件

	CALIBRATION_CHROMINANCE_ANOMALY_DETECTION,			//色度校正,异常点检测
	CALIBRATION_BRIGHTNESS_ANOMALY_DETECTION,			//亮度校正,异常点检测

};

//发送校正命令数据
/*
功	能:	判断扫描方式
入	参:	
nProjectID	：项目ID
nConfigID	：配置ID
nDisplayID	: 显示屏ID

begin		：起始模组编号及在箱体位置（nModuleID=-1：整个箱体）
end			：结束模组编号及在箱体位置

nType		: 发送项
回调函数：
cbFunc
返回值:
-1			: 失败
其它		：返回MAX进度
相关说明：
有中断，中断消息在回调函数中
*/

long /*__stdcall*/ BLL_SendCalibrationData(CALIBRATION_LEVEL nLevel,CALIBRATION_TYPE nType,
									   short nProjectID,
									   short nConfigID,
									   short nDisplayID,
									   vector<CALIBPOINT>::iterator begin,
									   vector<CALIBPOINT>::iterator end, 
									   SendCalibrationDataCallback cbFunc,
									   string & sCalibFilePath,
									   vector<string> * pvSerial,
									   HWND hWnd = NULL,
									   bool bNewFlow = false);

//读取校正数据库数据
long BLL_GetCalibrationData(CALIBRATION_TYPE nType,
										  short nProjectID,
										  short nConfigID,
										  short nDisplayID,
										  short nCabinetID,
										  CALIBPOINT ptCalib,
										  double ** pReadDB);

//BLL接口
//////////////////////////////////////////////////////////////////////////


//自定义向导2发送参数
long /*__stdcall*/ BLL_Custom2ModuleSendData(short nProjectID,
											 short nConfigID,short nAtlvcID,
											 CStructSingleScanCard & sScanCard);

//发送扫描卡第一个参数包
long /*__stdcall*/ BLL_SendScancardFirstPack(short nProjectID,
												 short nConfigID,
												 short nAtlvcID,
												 CStructSingleScanCard & sScanCard);

//自定义箱体向导11发送参数
long /*__stdcall*/ BLL_Custom11ScandMode2SendData(short nProjectID,
													  short nConfigID,
													  short nDisplayID,
													  short nAtlvcID,
													  CStructSingleScanCard & sScanCard,
													  GammaData & gammadata);
//发送扫描卡区走线包
long /*__stdcall*/ BLL_SendScanCardSectionLookupData(short nProjectID,
														 short nConfigID,
														 short nAtlvcID,
														 LINKTABLE &sLinkTable);
//发送扫描卡走线包
long /*__stdcall*/ BLL_SednScanCardLookupData(short nProjectID,
												  short nConfigID,
												  short nAtlvcID,
												  short nDataLineRange, 
												  short nDCBlineClkEn, 
												  LINKTABLE &sLinkTable);

//自定义箱体向导13
long /*__stdcall*/ BLL_SendScanCardPara(short nProjectID,
											short nConfigID,
											short nDisplayID,
											short nAtlvcID,
											short nAddressID,
											SCANCARDATTACHMENT & sScanCardAttachment,
											bool bSendScanCardLoadRegion,GammaData & sGammaData);


//停止所有监控过程
long /*__stdcall*/ BLL_StopALLMonitor();

//发送扫描卡参数
long /*__stdcall*/ BLL_SetOpenCabinetLamps(short nProjectID,
											   short nConfigID,
											   short nDisplayID,
											   short nCardID, 
											   bool bOpen);


//打开关闭箱体背面指示灯
long /*__stdcall*/ BLL_OpenCabinetLamps(short nProjectID,
										   short nConfigID,
										   short nDisplayID,
										   bool bOpen);


//采集卡智能编址
long BLL_SendATLVCIntelligentAddress(short nProjectID,short nConfigID, short nATLVCAK6ID, short nSecondAddressMax, short nSendCardNum);
/******************************************************************
//sunj 2012-06-28
采集卡视频源输入选择设置
nDviOrHdmiCheck： 0-dvi 1-hdmi
nATLVCSecendAddress 0-7：点播， 8-广播
*******************************************************************/
long BLL_SendATLVCVideoInputSet(short nProjectID,
								short nConfigID,
								short nATLVCID,
								short nATLVCSecendAddress,//级联线路上的采集卡序号
								short nDviOrHdmiCheck,
								short nType 
								);
/******************************************************************
//sunj 2012-06-28
采集卡视频源旋转选择设置
nDviOrHdmiCheck： 0-dvi 1-hdmi
nATLVCSecendAddress 0-7：点播， 8-广播
*******************************************************************/
long BLL_SendATLVCVideoRoTateSet(short nProjectID,
								 short nConfigID,
								 short nATLVCID,
								 short nATLVCSecendAddress,//级联线路上的采集卡序号
								 short nVideoRotate,
								 short nType
								 );
/******************************************************************
//sunj 2012-06-28
采集卡保存设置
*******************************************************************/
long BLL_ATLVCUpdateSave(short nProjectID,short nConfigID, short nATLVCAK6ID,short nType,int nSecondAddress,
							 SetPhotoCallback cbFunSendCard);

/******************************************************************
//sunj 2012-07-04
采集卡保存设置
*******************************************************************/
long BLL_ATLVCUpdateSaveByDisplayID(short nProjectID,
									short nConfigID, 
									short nDisplayID,
									short nType,
									short nAddress,
									int nSecondAddress ,
									SetPhotoCallback cbFunSendCard);
/******************************************************************
//sunj 2012-06-29
采集卡饱和度设置
*******************************************************************/
long BLL_SetATLVCUpdateSaturation(short nProjectID,short nConfigID, short nDisplayID ,long nSaturation,  short nAddress,short nSecondAddress ,
									   short nType,SetPhotoCallback cbFunSendCard);

/******************************************************************
//sunj 2012-06-29
采集卡设置对比度设置
*******************************************************************/
long BLL_SetATLVCUpdateContrast(short nProjectID,short nConfigID, short nDisplayID ,long nContrast,short nAddress,short nSecondAddress ,
									short nType,SetPhotoCallback cbFunSendCard);
/******************************************************************
//sunj 2012-06-29
采集卡视频接口芯片参数配置包
*******************************************************************/
long BLL_SetATLVCUpdateChipParam(short nProjectID,short nConfigID, short nATLVCID,short nDviOrHdmi, short nSencondAddress,short nType);

long BLL_SetATLVCUpdateInputVideo(short nProjectID, short nConfigID, bool bDvi);

long BLL_SaveATLVCUpdateInputVideo(short nProjectID, short nConfigID);

long BLL_EnableATLVCAutoLighting(short nProjectID,
								 short nConfigID,
								 short nDisplayID,
								 int nSendCardNable,
								  int nIternal);
								  
long BLL_SendSendCardAutoLightingAdjustIterval(short nProjectID,
											   short nConfigID,
											   short nDisplayID,
											   int nIterval);
long BLL_SendSendCardAutoLightingAdjustTableParam(short nProjectID,
											 short nConfigID,
											 short nDisplayID,
											 Display_Auto_BrightAdjust autoBrightAdust);

//发送测试切换命令（控制ATLVC图像发生器的图像切换）
long BLL_SetATLVCTest(short nProjectID,short nConfigID, short nDisplayID);

long BLL_StarSingleCabinetMonitor(short nProjectID,
									  short nConfigID,
									  short nDisplayID,
									  short nCabinetID,
									  MonitorCallback cbFunc);
long BLL_SetFanAutoLookupTable(short nProjectID,
							   short nConfigID,
							   short nDisplayID,
							   SendCabinetParaCallback cbFanFunc);


//保留接口
//////////////////////////////////////////////////////////////////////////

//同步数据接口
/*
功	能：同步数据接口
入	参:	
	nProjectID	：项目ID
	nConfigID	：配置ID
	nDisplayID	: 显示屏ID

	nType		: 同步位置			DATAOPERATE_ADD				DATAOPERATE_MODIFY			DATAOPERATE_DELETE
				0：导入				停止所有处理线程
									初始化项目链表、通讯队列
									启动所有处理线程
				1：项目				
									停止所有处理线程										停止所有处理线程
									初始化项目链表、通讯队列								初始化项目链表、通讯队列
									启动所有处理线程			无							启动所有处理线程
				2：配置		
									停止所有处理线程										停止所有处理线程
									初始化项目链表、通讯队列								初始化项目链表、通讯队列
									启动所有处理线程			无							启动所有处理线程

				3：LED显示屏		
									停止所有处理线程										停止所有处理线程
									初始化项目链表、通讯队列								初始化项目链表、通讯队列
									启动所有处理线程			无							启动所有处理线程

				4：ATLVC			
									停止所有处理线程			停止所有处理线程			停止所有处理线程
									初始化项目链表、通讯队列	初始化项目链表、通讯队列	初始化项目链表、通讯队列
									启动所有处理线程			启动所有处理线程			启动所有处理线程
				5：ATIEC			
									停止所有处理线程			停止所有处理线程			停止所有处理线程
									初始化项目链表、通讯队列	初始化项目链表、通讯队列	初始化项目链表、通讯队列
									启动所有处理线程			启动所有处理线程			启动所有处理线程
				6：关联（LED显示屏、ATLVC、ATIEC、ATECC）				
									停止所有处理线程			停止所有处理线程			停止所有处理线程
									初始化项目链表、通讯队列	初始化项目链表、通讯队列	初始化项目链表、通讯队列
									启动所有处理线程			启动所有处理线程			启动所有处理线程

				7：箱体				重启指定LED监控				重启指定LED监控(连接端口\编号)	重启指定LED监控

				8：自动亮度调节		无							重启指定配置自动亮度调节	无 
				9：定时亮度调节		无							重启指定配置定时亮度调节	无 
				10：定时开、关屏	无							重启指定配置定时开、关屏	无
				11：监控配置		无							重启指定LED监控				无

	nOperate	: 操作类型
返回值:
相关说明：
*/
enum OPERATETYPE{
	DATAOPERATE_ADD,
	DATAOPERATE_MODIFY,
	DATAOPERATE_DELETE
};
long /*__stdcall*/ BLL_SynchroData(short nType,
									   OPERATETYPE nOperate,
									   short nProjectID,
										short nConfigID,
										short nDisplayID);

#pragma endregion


//////////////////////////////////////////////////////////////////////////
////////////////////////////////保留接口//////////////////////////////////
//////////////////////////////////////////////////////////////////////////
//模组参数
typedef struct ModuleParam
{
	short nModelRows;
	short nModelCols;
	short nPixelRows;
	short nPixelCols;
}MODULEPARAM;
//显示屏参数
typedef struct ScreenParam
{
	short nBoxRows;
	short nBoxCols;
	map<short,ModuleParam> mModuleParam;
}SCREENPARAM;

//监控项使能标识（是否监控）(环境和箱体)
typedef struct MonitorATIECAndCabinetItem
{
	//环境
	bool bATIECHumidityFlag;			//湿度
	bool bATIECTemperatureFlag;		    //温度
	bool bATIECSmogFlag;				//烟雾
	bool bATIECBrightFlag;			    //亮度
	//箱体
	bool bCabinetHumidityFlag;			//湿度
	bool bCabinetTemperatureFlag;		//温度
	bool bCabinetSmogFlag;				//烟雾
	bool bCabinetBrightFlag;			//亮度
	bool bCabinetLEDPointDetect;		//逐点监测
	bool bCabinetPowerVolFlag[5];		//电源电压
	bool bCabinetFanStateFlag[2];		//风扇 0-左 1-右
	bool bCabinetCapacityFactorFlag;	//功率
	bool bCabinetVersionFlag;			//版本

	MonitorATIECAndCabinetItem()
	{
		memset(this, 0, sizeof(MonitorATIECAndCabinetItem));
	}
}MONITORTIECANDCABINETITEM;

// 监控报警使能
typedef struct AlarmParamEnable
{
	//环境监控项
	bool bPowerOffSmog;
	bool bPowerOffTemperature;
	bool bPowerOffHumidity;
	bool bPowerOffBox;

	//箱体监控项
	bool bAlarmSmog;
	bool bAlarmTemperature;
	bool bAlarmHumidity;
	bool bAlarmCapacityFactor;
	bool bAlarmPower1;
	bool bAlarmPower2;
	bool bAlarmPower3;
	bool bAlarmPower4;
	bool bAlarmPower5;
	bool bAlarmFanL;
	bool bAlarmFanR;
	bool bAlarmPointDetect;
	
}ALARMPARAMENABLE;
//报警条件
typedef struct AlarmCondition
{
	//环境报警条件
	float fATIECTemperaturelimit;
	float fATIECHumiditylimit;
	//箱体报警条件
	float fCabinetTemperaturelimit;
	float fCabinetHumiditylimit;
	float fCabinetPs1upper;
	float fCabinetPs1lower;
	float fCabinetPs2upper;
	float fCabinetPs2lower;
	float fCabinetPs3upper;
	float fCabinetPs3lower;
	float fCabinetPs4upper;
	float fCabinetPs4lower;
	float fCabinetPs5upper;
	float fCabinetPs5lower;
	int nCabinetBoxpixelfaultlimit;
	int nCabinetScreenpixelfaultlimit;

}ALARMCONDITION;

//监控数据读取

typedef struct BaseMonitorData
{
	int nlight;
	int ncolortemperature;
	float fgamma;
	bool bdvi;
	bool lock;
	bool bpower;
	bool bautopower;
	int nautolight;
}BASEMONITOR;
typedef struct EnvironmentMonitorData
{
	float ftemperature;
	float fhumidity;
	int nbright;
	bool bsmog;
	short ntemperaturecontrol[2];
	EnvironmentMonitorData()
	{
		ftemperature = MONITOR_DATA_DEFAULT;
		fhumidity= MONITOR_DATA_DEFAULT;
		nbright= MONITOR_DATA_DEFAULT;
	}
}ENVIRONMENTMONITOR;
typedef struct PixelData
{
	int pixelrow;
	int pixelcol;

	bool br;
	bool bg;
	bool bb;
}PIXELDATA;
typedef struct ModelData
{
	int modelrow;
	int modelcol;

	bool bstatus;

	vector<PixelData> vpixeldata;
}MODELDATA;
typedef struct CabinetMonitorData
{
	int boxrow;
	int boxcol;
	bool bstatus;

	float ftemperature;
	float fhumidity;
	bool bsmog;
	int nleftfan;
	int nrightfan;
	float voltage1;
	float voltage2;
	float voltage3;
	float voltage4;
	float voltage5;
	float wattlevel;
	//bool bline;
	vector<int> vLine;

	vector<ModelData> vmodeldata;
	CabinetMonitorData()
	{
		ftemperature = MONITOR_DATA_DEFAULT;
		fhumidity = MONITOR_DATA_DEFAULT;
		voltage1 = MONITOR_DATA_DEFAULT;
		voltage2 = MONITOR_DATA_DEFAULT;
		voltage3 = MONITOR_DATA_DEFAULT;
		voltage4 = MONITOR_DATA_DEFAULT;
		voltage5 = MONITOR_DATA_DEFAULT;
		wattlevel = MONITOR_DATA_DEFAULT;
	}
}CABINETMONITOR;


//开关LED显示屏（默认开关项目1-配置1-所有显示屏）
/*
功	能：开关LED显示屏
入	参:	
bPower：true:开 false:关
返回值:
true：成功
false：失败
相关说明：
调用 BLL_Power
*/
bool /*__stdcall*/	AT_Power(bool bPower);

bool /*__stdcall*/	AT_Lock(bool bLock);

bool /*__stdcall*/	AT_CutOff(bool bOff);

int /*__stdcall*/ AT_GetBrightEx();

bool /*__stdcall*/ AT_SetBrightEx(int nBrightness);

int /*__stdcall*/ AT_GetLedScrNumb();

void /*__stdcall*/ AT_SaveToScanCard(int nScreenID, int nBrightness) ;

bool /*__stdcall*/ AT_SetColorTemperatureRGB(int nLedNo, short nColorTemperIndex,int nRed, int nGreen, int nBlue);

bool /*__stdcall*/ AT_IsReturn();

bool /*__stdcall*/ AT_IsPower();

bool /*__stdcall*/ AT_IsPower();

bool /*__stdcall*/	AT_SetBright(int nBrightness);

bool /*__stdcall*/	AT_AutoBright(bool bStart);

bool /*__stdcall*/	AT_TimingBright(bool bStart);
/*函数功能:	启动/停止监控*/
bool /*__stdcall*/  AT_SetMonitor(bool bStart);
/*监控数据读取*/
bool /*__stdcall*/  AT_GetMonitorData(BaseMonitorData &basemonitordata,EnvironmentMonitorData &environmentmonitordata,vector<CabinetMonitorData> * vcabinetmonitordata);

/*报警条件读取*/
//bool /*__stdcall*/	AT_GetAlarmCondition(AlarmCondition &alarmPara);
/*显示屏参数读取*/
//bool /*__stdcall*/	AT_GetDisplayPara(ScreenParam &screenParam, AlarmParamEnable  &alarmEnable);
/*显示屏参数读取 监控项读取 报警条件读取 自动控制项读取*/
bool /*__stdcall*/	AT_GetDisplayParaAndAlarmCondition(ScreenParam & screenParam,MONITORTIECANDCABINETITEM & monitorItem,
														   AlarmCondition & alarmcondition,vector<RELAYPAR>* vRelaypar);

/*设置定时开关屏配置*/
bool AT_SetTimingPowerControlConfig(vector<Display_Timing_PowerControl> &TimingPower,vector<Display_Special_PowerControl> &TimeSpecialPower,int nWeekOrEveryday);
/*设置定时亮度调节配置*/
bool AT_SetTimingBrightAdjustConfig(vector<Display_Timing_BrightAdjust> &TimingBright,bool &bAuto);
/*设置自动亮度调节配置*/
bool AT_SetAutoBrightAdjustConfig(Display_Auto_BrightAdjust &AutoBrightAdjust,bool &bAuto);
/*获取当前项目路径*/
bool AT_GetCurrentPath(string & sPath);

#endif
