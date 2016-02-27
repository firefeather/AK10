package com.szaoto.ak10.ownerdraw;

import java.util.ArrayList;

public class BackForwardStack {

   public 	ArrayList<ObjLog> m_arrBackAndForwardStack = null;
	
   public	int nCurOpId=-1;
   public	int nLatestOpId=-1;
	
	public BackForwardStack()
	{	
		super();
		m_arrBackAndForwardStack = new ArrayList<ObjLog>();	
	}
	
	//撤销
	public ObjLog getUndoOpStation()
	{		
		if (nCurOpId>=m_arrBackAndForwardStack.size()) {
			return null;
		}
		ObjLog tObj = new ObjLog();
		if (nCurOpId>=0) {
			try
			{
				   tObj = m_arrBackAndForwardStack.get(nCurOpId);
				   nCurOpId--;
				   return tObj;
			} 
			catch (Exception e) 
			{
				    e.printStackTrace();
			}
			
		}	
		//nCurOpId--;
	    return null;		
	}
	//恢复
	public ObjLog getRedoOpStation()
	{		
		if (nCurOpId<m_arrBackAndForwardStack.size()) {
			nCurOpId++;
		}
    	if (nCurOpId==m_arrBackAndForwardStack.size()) {
			return null;
		}
		if (nCurOpId<=10) {
			return m_arrBackAndForwardStack.get(nCurOpId);
		}else
		{
			return null;
		}
	}
	
	//更新操作
	public void UpdateCurOpStation(ObjLog tObjLog)
	{
		//在之前的某一步已经重做了
		if (nCurOpId< nLatestOpId) {	
			int tSize =  m_arrBackAndForwardStack.size();
			for (int i = nCurOpId+1; i < tSize; i++) {
				m_arrBackAndForwardStack.remove(nCurOpId+1);
			}				
			nLatestOpId=nCurOpId;
		}

		//在第11部操作的时候，清除掉第1部操作的数据
		if (nCurOpId>10) {
			m_arrBackAndForwardStack.remove(0);
			nCurOpId=10;
			nLatestOpId=10;
		}
		
		m_arrBackAndForwardStack.add(tObjLog);		
		
		if (nCurOpId==nLatestOpId) {
				nCurOpId++;
		}
	
		nLatestOpId++;
	
	}


}
