package com.szaoto.ak10.util;

import java.util.Stack;

public class StackManager {
	
	Stack<Object> backStack = new Stack<Object>();
	Stack<Object> forwardStack = new Stack<Object>();

	int maxsize = 10;

	public StackManager() {

	}
	// 入栈操作 （撤销）
	public void backStackPush(Object obj) {
		//如果大于10次就把最后面一次剔除
		int nSize = backStack.size();
		if (nSize>maxsize) {
			backStack.remove(backStack.get(0));
			backStack.add(nSize - 1, obj);
		}
		else {
		  backStack.push(obj);
		}

	}

	//弹出一个对象并获取当前对象
	public Object backStackPopAndGetObj(){	
		//后撤对象堆栈，弹出并取最前段的对象，用于位置操作
		Object obj=backStack.pop();
		forwardStack.push(obj);
		int nSize = backStack.size();	
		if (nSize==0) {			
			return null;					
		}
		
        Object object = backStack.get(nSize-1);
        return object;	
	}
	
	//获取向前堆栈的最新对象,并弹出
	public Object forwardStackPopAndGetObj(){	
		//后撤对象堆栈，弹出并取最前段的对象，用于位置操作
		Object obj = backStack.pop();	    
        return obj;	
	}
		
	
	// 恢复操作（恢复）
	public void forwardStackPush(Object obj) {
		int nSize = forwardStack.size();
		if (nSize>maxsize) {
			forwardStack.remove(forwardStack.get(0));
			forwardStack.add(nSize - 1, obj);
		}
		else {
		  forwardStack.push(obj);
		}
	}

	// 撤销操作
	public Object backStackPop() {
		//LeddisplayEdit mLeddisplayEdit = new LeddisplayEdit();
		int nSize = backStack.size();
		if (nSize == 0) {
			return null;
		}
		Object obj = backStack.get(backStack.size() - 1);
		backStack.pop();
		forwardStack.push(obj);
		return obj;
	}

	// 恢复操作
	public Object forwardStackPop() {	
		int nRedoSize = forwardStack.size();
		if (nRedoSize == 0) {
			return null;
		}
		Object obj = forwardStack.get(forwardStack.size() - 1);
		forwardStack.pop();
		backStack.push(obj);
		return obj;
	}

}
