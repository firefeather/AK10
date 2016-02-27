package com.szaoto.ak10.entity;
/**
 * 箱体系列实体文件
 * 需要解析成树状结构
 * @author zhangsj
 *
 */
public class CabinetSeries {
	/*
	 * 箱体系列的属性：ID
	 * 箱体系列名称： series
	 * 父节点ID:ParentID
	 */
	
		 private int  ID;                //系列ID    
		 private int  ParentID;    //0-根节点          
		 private String  name;    //系列名称	
		 
		 
			//添加构造方法
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
