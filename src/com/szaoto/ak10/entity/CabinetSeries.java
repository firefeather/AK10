package com.szaoto.ak10.entity;
/**
 * ����ϵ��ʵ���ļ�
 * ��Ҫ��������״�ṹ
 * @author zhangsj
 *
 */
public class CabinetSeries {
	/*
	 * ����ϵ�е����ԣ�ID
	 * ����ϵ�����ƣ� series
	 * ���ڵ�ID:ParentID
	 */
	
		 private int  ID;                //ϵ��ID    
		 private int  ParentID;    //0-���ڵ�          
		 private String  name;    //ϵ������	
		 
		 
			//��ӹ��췽��
		public CabinetSeries() {
			super();
			// TODO Auto-generated constructor stub
		}

		public CabinetSeries(int iD, int parentID, String name) {
			super();
			ID = iD;
			ParentID = parentID;
			this.name = name;
		}



		public CabinetSeries(int cbtSeriesID) {
			super();
			this.ID = cbtSeriesID;
		}

		public int getID() {
			return ID;
		}



		public  void setID(int iD) {
			this.ID = iD;
		}



		public int getParentID() {
			return ParentID;
		}



		public void setParentID(int parentID) {
			this.ParentID = parentID;
		}



		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		 
		 
	
}
