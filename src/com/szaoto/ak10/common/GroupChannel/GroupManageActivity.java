package com.szaoto.ak10.common.GroupChannel;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.szaoto.ak10.Ak10Application;
import com.szaoto.ak10.R;
import com.szaoto.ak10.common.GroupChannel.GroupChannelActivity.InterfaceListAdapter;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.sqlitedata.ChanGroupDb;

/**
 * ����GroupManageActivity 
 * ���� zhangsj 
 * ��Ҫ���� Ⱥ�������� 
 * ��������2015��1��28�� 
 * �޸��ߣ��޸����ڣ��޸�����
 * 
 */
public class GroupManageActivity extends Activity {
	InterfaceListAdapter  mInteradapter;
	GroupChannelActivity mActivity;
	private TextView txtGroupBack;
	private TextView txtGroupAdd;
	private ListView groupListview;
	private int nledId; //LED��ID
	private GroupListAdapter mGroupAdapter=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_manage);
		SerialPortControlBroadCast.SetCurrentContext(this);
		initView();
		LoadDataFromDb();
	}

	public void LoadDataFromDb()
	{
		nledId = Ak10Application.GetLedId();
		ArrayList<String> tArrayList = ChanGroupDb.GetAllGpNameRecords(nledId);
		
		if (tArrayList.size()==0) {
			
			Toast.makeText(this, getString(R.string.notice_nocard), Toast.LENGTH_SHORT).show();
		}
		
		mGroupAdapter = new GroupListAdapter(this, tArrayList);
		groupListview.setAdapter(mGroupAdapter);
		mGroupAdapter.notifyDataSetChanged();
		
	}
	

	private void initView() {
		txtGroupBack = (TextView) findViewById(R.id.text_gpmanageback);
		txtGroupAdd = (TextView) findViewById(R.id.text_groupadd);
		groupListview = (ListView) findViewById(R.id.DragGroupListView);
		txtGroupBack.setOnClickListener(ClickHandler);
		txtGroupAdd.setOnClickListener(ClickHandler);
	}

	View.OnClickListener ClickHandler = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.text_gpmanageback:
				finish();
				break;
			case R.id.text_groupadd:
				// �����Ի���������ӵ�����
				AddGroupDialogShow();
				break;
			default:
				break;
			}
		}
	};

	
   
	//Ⱥ�����adapter
	public class GroupListAdapter extends BaseAdapter {
		private Context context;
		private ArrayList<String> listItems;
		private LayoutInflater listContainer;
		private ListGroupView listItemView;

		public GroupListAdapter(GroupManageActivity groupactivity,ArrayList<String> tGroupsData) {
			super();
			this.context = groupactivity;
			listContainer = LayoutInflater.from(context);
			this.listItems = tGroupsData;
		}

		public GroupListAdapter(Context application,	ArrayList<String> tGroupsData) {
			this.context = application;
			listContainer = LayoutInflater.from(context); // ������ͼ����������������
			this.listItems = tGroupsData;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listItems.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// �Զ�����ͼ
			listItemView = null;
			if (convertView == null) {
				listItemView = new ListGroupView();
				// ��ȡlist_item�����ļ�����ͼ
				convertView = listContainer.inflate(R.layout.group_chlist, null);
				// Ⱥ�����ƿؼ�
				listItemView.txtGroupName = (TextView) convertView.findViewById(R.id.group_list_item_text1);
				listItemView.btnDeleteGp = (Button) convertView.findViewById(R.id.btnDelFile);	
				// ���ÿؼ�����convertView
				convertView.setTag(listItemView);
				
			} else {
				listItemView = (ListGroupView) convertView.getTag();
			}
			// ��������
			listItemView.txtGroupName.setText((String) listItems.get(position));
			
			listItemView.btnDeleteGp.setOnClickListener(new OnClickListener() {
					
						@Override
						public void onClick(View v) {
							
							
							new AlertDialog.Builder(GroupManageActivity.this)
							/* �������ڵ�����ͷ���� */
							.setTitle(R.string.btn_groupdata_del)
							/* ���õ������ڵ�ͼʽ */
							.setIcon(android.R.drawable.ic_dialog_info)
							/* ���õ������ڵ���Ϣ */
							.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialoginterface, int i) {
									
									String strGpNameString = (String) listItemView.txtGroupName.getText();
									// ��xml�ļ�����Ⱥ������ɾ��Ⱥ��		
									ChanGroupDb.DeleteRecordByGpName(strGpNameString, nledId);	
									LoadDataFromDb();
								}
							})
							.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() { /* �����������ڵķ����¼� */
								public void onClick(DialogInterface dialoginterface, int i) {
									
								}
							}).show();
			

							

						}
					});
			
			    convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ModifyDialogShow((String) listItemView.txtGroupName.getText());
				}
			});
			return convertView;
		}

		public class ListGroupView {
			public TextView txtGroupName;
			public Button   btnDeleteGp;
		}
	}
	//���Ⱥ��
	protected void AddGroupDialogShow() {
		
		LayoutInflater inflater = (LayoutInflater) GroupManageActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.input_add, null);
		// �����ĶԻ���
		new AlertDialog.Builder(GroupManageActivity.this)
				/* �������ڵ�����ͷ���� */
				.setTitle(R.string.text_groupdatadd)
				/* ���õ������ڵ�ͼʽ */
				.setIcon(android.R.drawable.ic_dialog_info)
				/* ���õ������ڵ���Ϣ */
				.setMessage(R.string.text_groupname_input)
				.setView(layout)
				.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						
						EditText inputString = (EditText) layout.findViewById(R.id.input_add_string);
						String str = inputString.getText().toString();
						if (str == null || str.equals("")) {
							Toast.makeText(getApplicationContext(),R.string.text_noemputy_input, Toast.LENGTH_SHORT).show();
						} else {						
							//���Ⱥ����Ϊ inputString �����ݿ�
							ChanGroupDb.InitAddGpItem(str, nledId);			
							LoadDataFromDb();
						}
					}
				})
				.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() { /* �����������ڵķ����¼� */
					public void onClick(DialogInterface dialoginterface, int i) {
						
					}
				}).show();

	}
	
	//�޸�Ⱥ������
	protected void ModifyDialogShow(final String strGpName) {

		LayoutInflater inflater = (LayoutInflater) GroupManageActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.input_edit, null);
		final EditText  EditGroupName= (EditText) layout.findViewById(R.id.input_edit_string);
		EditGroupName.setText(strGpName);
	
		// �����ĶԻ���
		new AlertDialog.Builder(GroupManageActivity.this)
				/* �������ڵ�����ͷ���� */
				.setTitle(R.string.text_group_item_modify)
				/* ���õ������ڵ�ͼʽ */
				.setIcon(android.R.drawable.ic_dialog_info)
				/* ���õ������ڵ���Ϣ */
				.setMessage(R.string.text_group_item_modify_content)
				.setView(layout)
				.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
			        @Override
					public void onClick(DialogInterface dialog, int which) {					
						 EditGroupName.setFocusable(true);
						 ChanGroupDb.UpdateGpName(strGpName, EditGroupName.getText().toString(), nledId);	 
						 LoadDataFromDb();		
					}
				})
				.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() { /* �����������ڵķ����¼� */
					public void onClick(DialogInterface dialoginterface, int i) {

					}
				}).show();
		
	}
	@Override
	protected void onResume() {
		LoadDataFromDb();	
		super.onResume();
	}


	

	
}
