/*
   * �ļ��� PlayerListActivity.java
   * ���������б�com.szaoto.ak10.player
   * �汾��Ϣ���汾��
   * ��������2013��11��8������11:54:28
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.player;

import android.app.Activity;
import android.os.Bundle;

import com.szaoto.ak10.R;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;

/*
 * ����PlayerListActivity
 * ���� liangdb
 * ��Ҫ����
 * ��������2013��11��8��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class PlayerListActivity extends Activity {

	//private DragListView mShowAll;
    //private EditListAdapter mSelectAdapter;
    //private ArrayList<String> mCodeData = new ArrayList<String>();
    //private ArrayList<String> mNameData = new ArrayList<String>();

    
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_list);
		SerialPortControlBroadCast.SetCurrentContext(this);
	}
	
}
