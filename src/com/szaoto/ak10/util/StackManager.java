package com.szaoto.ak10.util;

import java.util.Stack;

public class StackManager {
	
	Stack<Object> backStack = new Stack<Object>();
	Stack<Object> forwardStack = new Stack<Object>();

	int maxsize = 10;

	public StackManager() {

	}
	// ��ջ���� ��������
	public void backStackPush(Object obj) {
		//�������10�ξͰ������һ���޳�
		int nSize = backStack.size();
		if (nSize>maxsize) {
			backStack.remove(backStack.get(0));
			backStack.add(nSize - 1, obj);
		}
		else {
		  backStack.push(obj);
		}

	}

	//����һ�����󲢻�ȡ��ǰ����
	public Object backStackPopAndGetObj(){	
		//�󳷶����ջ��������ȡ��ǰ�εĶ�������λ�ò���
		Object obj=backStack.pop();
		forwardStack.push(obj);
		int nSize = backStack.size();	
		if (nSize==0) {			
			return null;					
		}
		
        Object object = backStack.get(nSize-1);
        return object;	
	}
	
	//��ȡ��ǰ��ջ�����¶���,������
	public Object forwardStackPopAndGetObj(){	
		//�󳷶����ջ��������ȡ��ǰ�εĶ�������λ�ò���
		Object obj = backStack.pop();	    
        return obj;	
	}
		
	
	// �ָ��������ָ���
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

	// ��������
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

	// �ָ�����
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