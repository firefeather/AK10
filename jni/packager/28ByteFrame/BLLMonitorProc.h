#pragma once
#include "../Datastruct/datastructdef.h"


//////////////////////////////////////////////////////////////////////////
//	created:		2012/01/05
//	created:		5:1:2012   16:59
//	copyright:		�����а��ص��ӹɷ����޹�˾��Ȩ����
//	file base:		BLLMonitorProc
//	author:			liangdb
//	comment:		LED��ʾ������߳�
//1)		LED��ʾ��������
//2)		LED��ʾ������״̬����ѯATLVC����״̬��
//3)		LED��ʾ������״̬����ѯ����������ATLVC����״̬��
//4)		���壨ɨ�迨���ĳ�����
//5)		���壨ɨ�迨������״̬����ѯ�汾��
//6)		���壨ɨ�迨��5·��ѹֵ
//7)		���壨ɨ�迨������ֵ
//8)		���壨ɨ�迨�������
//�������̴߳�����
//LED��ʾ������̣߳�����1��2��3���ж��Ƿ��б��������ͱ����ʼ���
//�������̣߳�����4��5��6��7��8���ж��Ƿ��б��������ͱ����ʼ���
//////////////////////////////��1��LED����////////////////////////////////
//////////////////////////////////////////////////////////////////////////
//V2.0 ֧�ֶ���̷��ʣ��ݲ�֧�����Ƚ���
//////////////////////////////////////////////////////////////////////////
#include "PackageBase/CLPackCommunicationData.h"
#include "../../util/util.h"
#include <stdio.h>
class CBLLMonitorProc
{


public:
	CBLLMonitorProc(void);
	~CBLLMonitorProc(void);

public:
	//////////////////////////////////////////////////////////////////////////
	//�����������������������ѯ

	long PackMonitorCommond(int ScAddress,int PackCommond,int SCAddress,uint8_t *ucdata);/* MONITORTYPE_COSTOM = 0;
					 MONITORTYPE_POWER = 1;              monitortype��ʾ5�ּ������
					 MONITORTYPE_POINTDETECT = 2;
					 MONITORTYPE_VOTAGE = 3;
					 MONITORTYPE_WEBERROR = 4;

					 MONITORACTIONTYPE_FF = 1;
					 MONITORACTIONTYPE_NORMAL = 0;     monitoractiontype��ʾ�㲥���ǵ㲥*/
	//bool GetCustomMonitorData(LPMONITORDATA pMonitorData ,unsigned char * m_ucRcvData);

public:
	CCLPackCommunicationData pack28byte;
};