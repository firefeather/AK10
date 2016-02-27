/*
   * �ļ��� ControlActivity.java
   * ���������б�com.szaoto.ak10.control
   * �汾��Ϣ���汾��
   * ��������2013��11��8������11:53:51
   * ��Ȩ���� liangdb-szaoto
*/
package com.szaoto.ak10.control;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szaoto.ak10.Ak10Application;
import com.szaoto.ak10.HomePageActivity;
import com.szaoto.ak10.IInfoChangeObserver;
import com.szaoto.ak10.PannelButtonDownService;
import com.szaoto.ak10.R;
import com.szaoto.ak10.adapter.HorizontalListViewAdapter;
import com.szaoto.ak10.common.Display.ColourRGB;
import com.szaoto.ak10.commsdk.PannelLedControlBroadCast;
import com.szaoto.ak10.commsdk.SerialPortControlBroadCast;
import com.szaoto.ak10.custom.CustomToast;
import com.szaoto.ak10.custom.HorizontalListView;
import com.szaoto.ak10.custom.SeekBarDownUp;
import com.szaoto.ak10.datacomm.ConnChartComm;
import com.szaoto.ak10.datacomm.LEDParamComm;
import com.szaoto.ak10.sqlitedata.ChanGroupDb;
import com.szaoto.ak10.sqlitedata.ChannelDB;
import com.szaoto.ak10.sqlitedata.ChnData;
import com.szaoto.ak10.sqlitedata.ColorTemperData;
import com.szaoto.ak10.sqlitedata.ColorTemperDb;
import com.szaoto.ak10.sqlitedata.CtrlData;
import com.szaoto.ak10.sqlitedata.CtrlDb;
import com.szaoto.ak10.sqlitedata.CtrlLastStationData;
import com.szaoto.ak10.sqlitedata.CtrlLastStationDb;
import com.szaoto.ak10.sqlitedata.InterfaceDB;
import com.szaoto.ak10.sqlitedata.IntfData;
import com.szaoto.ak10.test.TestActivity;


/*
 * ����ControlActivity
 * ���� huh
 * ��Ҫ����
 * ��������2015��6��19��
 * �޸��ߣ��޸����ڣ��޸�����
 */
public class ControlActivity extends Activity implements IInfoChangeObserver {
	
	private static ControlActivity mControlActivity = null;
	private Button btn_Group1;
	private Button btn_Group2;
	private Button btn_Group3;
	private Button btn_Group4;
	private Button btn_Group5;
	
	private Button btn_SaveCur;

	public  LinearLayout layout_brightness; // ����
	public  LinearLayout layout_colortemperature; // ɫ��
	
	public  RelativeLayout layout_colortemperatureID; // ��
	public  LinearLayout seekbar_colortemperature_r; // ɫ��
	public  LinearLayout seekbar_colortemperature_g; // ɫ��
	public  LinearLayout seekbar_colortemperature_b; // ɫ��
	
	public  LinearLayout layout_contrast;
	public  LinearLayout layout_saturation;

	private ImageButton imgbtn_Left;
	private ImageButton imgbtn_Right;

	
	private static int nCurrentChannel = -1;

	private TextView btn_ControlHome;
	
	private TextView btn_DisplayID;
	private TextView btn_ControlBack;
	private HorizontalListView hListView;
	private HorizontalListViewAdapter hListViewAdapter; // ˮƽ��̬�Ų�Ⱥ��ͨ��

	int[] nColorTmp = {2800,3200,4500,5000,5600,6500,8000,9300};
	int  ColorTmpIndex = 5;//ɫ�¸���ţ�0~7
	private int CurrentLeddisplayID ;
	private int CurrentProfileID = 1 ;

	//���水ť
	ImageButton btnRPlusButton;
	ImageButton btnGPlusButton;
	ImageButton btnBPlusButton;
	ImageButton btnRMinusButton;
	ImageButton btnGMinusButton;
	ImageButton btnBMinusButton;
	ImageButton btnBrightPlusButton;
	ImageButton btnBrightMinusButton;
	ImageButton btnContrastPlusButton;
	ImageButton btnContrastMinusButton;
	ImageButton btnSaturationPlusButton;
	ImageButton btnSaturationMinusButton;
	
	SeekBarDownUp barBright; // ����������
	SeekBarDownUp barR; // ����������
	SeekBarDownUp barG; // ����������
	SeekBarDownUp barB; // ����������
	SeekBarDownUp barContrast; // ����������
	SeekBarDownUp barSaturation; // ����������

	//��ǩ
	TextView txtBright;
	TextView txtColorTemp;
	TextView txtR;
	TextView txtG;
	TextView txtB;
	TextView txtContrast;
	TextView txtSaturation;
	int nCurrentColorTemp = -1;//0:ɫ�¸�; 1:R; 2:G; 3:B
	
	ArrayList<String> m_ArrGpNameArrayList = new ArrayList<String>();
	
	
	public int setCurrentLeddisplayID(int currentLeddisplayID) {
		CurrentLeddisplayID = currentLeddisplayID;
		return LoadLastSavedStation(CurrentLeddisplayID);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control);
		SerialPortControlBroadCast.SetCurrentContext(this);
		CurrentLeddisplayID = 1;
		mControlActivity = this;
		SerialPortControlBroadCast.SetCurrentContext(this);
		PannelButtonDownService.observers.add(this);	
		InitViewObj();
		LoadGpConfig(CurrentLeddisplayID);			
	}
	
   public void	SetColorTempIncrease(){
	   switch (nCurrentColorTemp) {
		case 1:
			{
				int nRed = barR.getProgress();
				nRed++;
		        if (nRed > 256) {
		        	nRed = 256;
		        }		      				
				SetR(nRed);
				txtR.setText(String.valueOf(nRed));
				barR.setProgress(nRed);
			}
			break;
		case 2:
			{
				int nGreen = barG.getProgress();
				nGreen++;
		        if (nGreen > 256) {
		        	nGreen = 256;
		        }
				SetG(nGreen);
				txtG.setText(String.valueOf(nGreen));
				barG.setProgress(nGreen);
			}				
			break;
		case 3:
			{
				int nBlue = barB.getProgress();
				nBlue++;
		        if (nBlue > 256) {
		        	nBlue = 256;
		        }
				SetB(nBlue);
				txtB.setText(String.valueOf(nBlue));
				barB.setProgress(nBlue);
			}
			break;
		default:
			{
			   if(ColorTmpIndex>=7){
				   ColorTmpIndex=7;
			   }else {
				   ColorTmpIndex++;
			   }
			   int ColorTempPlus = nColorTmp[ColorTmpIndex];
			   txtColorTemp.setText(String.valueOf(ColorTempPlus));
	
			   //���浽���ݿ�
			   CtrlLastStationDb.UpdateLastColorTemper(ColorTempPlus, CurrentLeddisplayID);
			   SetRGBDataByTemper(ColorTempPlus);
			}
			break;
		}
	   
	   	RefreshUI();
	}
	
   public void  SetColorTempReduce(){
	   switch (nCurrentColorTemp) {
		case 1:
			{
				int nRed = barR.getProgress();
				nRed--;
		        if (nRed < 0) {
		        	nRed = 0;
		        }
				SetR(nRed);
				txtR.setText(String.valueOf(nRed));
				barR.setProgress(nRed);
			}
			break;
		case 2:
			{
				int nGreen = barG.getProgress();
				nGreen--;
		        if (nGreen < 0) {
		        	nGreen = 0;
		        }
				SetG(nGreen);
				txtG.setText(String.valueOf(nGreen));
				barG.setProgress(nGreen);
			}				
			break;
		case 3:
			{
				int nBlue = barB.getProgress();
				nBlue--;
		        if (nBlue < 0) {
		        	nBlue = 0;
		        }
				SetB(nBlue);
				txtB.setText(String.valueOf(nBlue));
				barB.setProgress(nBlue);
			}
			break;
		default:
			{
			   if(ColorTmpIndex<=0){
				   ColorTmpIndex=0;
			   }else {
				   ColorTmpIndex--;
			   }
				
			   int ColorTemp = nColorTmp[ColorTmpIndex];
			   txtColorTemp.setText(String.valueOf(ColorTemp));	

			   //���浽���ݿ�
			   CtrlLastStationDb.UpdateLastColorTemper(ColorTemp, CurrentLeddisplayID);
			   SetRGBDataByTemper(ColorTemp);
			}
			break;
		}

	   	RefreshUI();
   }
   
	public void InitViewObj(){
		
		hListView = (HorizontalListView) findViewById(R.id.horizon_listview);
		
		btn_ControlHome = (TextView) findViewById(R.id.text_controlmain);
		btn_DisplayID = (TextView) findViewById(R.id.text_leddisplasetting);
		btn_ControlBack = (TextView) findViewById(R.id.text_controlback);
		btn_ControlHome.setOnClickListener(clickHandler);
		btn_DisplayID.setOnClickListener(clickHandler);
		btn_ControlBack.setOnClickListener(clickHandler);
		
		btn_Group1 = (Button) findViewById(R.id.btn_Group1);
		btn_Group2 = (Button) findViewById(R.id.btn_Group2);
		btn_Group3 = (Button) findViewById(R.id.btn_Group3);
		btn_Group4 = (Button) findViewById(R.id.btn_Group4);
		btn_Group5 = (Button) findViewById(R.id.btn_Group5);

		btn_SaveCur = (Button)findViewById(R.id.btn_Save);
		
		btn_Group1.setOnClickListener(clickHandler);
		btn_Group2.setOnClickListener(clickHandler);
		btn_Group3.setOnClickListener(clickHandler);
		btn_Group4.setOnClickListener(clickHandler);
		btn_Group5.setOnClickListener(clickHandler);


		btn_Group1.setOnLongClickListener(longclickHandler);
		btn_Group2.setOnLongClickListener(longclickHandler);
		btn_Group3.setOnLongClickListener(longclickHandler);
		btn_Group4.setOnLongClickListener(longclickHandler);
		btn_Group5.setOnLongClickListener(longclickHandler);

		layout_brightness = (LinearLayout) findViewById(R.id.layout_brightness);
		layout_colortemperature = (LinearLayout) findViewById(R.id.layout_colortemperature);
		
		layout_colortemperatureID = (RelativeLayout) findViewById(R.id.colortemperatureID);
		seekbar_colortemperature_r = (LinearLayout) findViewById(R.id.seekbar_colortemperature_r);
		seekbar_colortemperature_g = (LinearLayout) findViewById(R.id.seekbar_colortemperature_g);
		seekbar_colortemperature_b = (LinearLayout) findViewById(R.id.seekbar_colortemperature_b);
		
		layout_contrast = (LinearLayout) findViewById(R.id.layout_contrast);
		layout_saturation = (LinearLayout) findViewById(R.id.layout_saturation);

		imgbtn_Left = (ImageButton) findViewById(R.id.imgbtn_Left);
		imgbtn_Right = (ImageButton) findViewById(R.id.imgbtn_Right);
		
		txtColorTemp = (TextView) findViewById(R.id.txt_ColorTempValue);

		imgbtn_Left.setOnClickListener(clickHandler);
		imgbtn_Right.setOnClickListener(clickHandler);

		View subLayout[] = new View[6];
		subLayout[0] = (View) findViewById(R.id.seekbar_brightness);
		subLayout[1] = (View) findViewById(R.id.seekbar_colortemperature_r);
		subLayout[2] = (View) findViewById(R.id.seekbar_colortemperature_g);
		subLayout[3] = (View) findViewById(R.id.seekbar_colortemperature_b);
		subLayout[4] = (View) findViewById(R.id.seekbar_contrast);
		subLayout[5] = (View) findViewById(R.id.seekbar_saturation);

		
		btnBrightPlusButton = (ImageButton) subLayout[0].findViewById(R.id.imgbtn_Increase);
		btnBrightMinusButton = (ImageButton) subLayout[0].findViewById(R.id.imgbtn_Reduce);
		barBright           = (SeekBarDownUp)subLayout[0].findViewById(R.id.seekbar_Adjust);
		barBright.setMax(100);
		txtBright = (TextView)subLayout[0].findViewById(R.id.txt_Value);
	
		btnRPlusButton = (ImageButton) subLayout[1].findViewById(R.id.imgbtn_Increase);
		btnRMinusButton = (ImageButton) subLayout[1].findViewById(R.id.imgbtn_Reduce);
		barR           = (SeekBarDownUp)subLayout[1].findViewById(R.id.seekbar_Adjust);
		txtR= (TextView)subLayout[1].findViewById(R.id.txt_Value);
		barR.setMax(256);
		
		btnGPlusButton = (ImageButton) subLayout[2].findViewById(R.id.imgbtn_Increase);
		btnGMinusButton = (ImageButton) subLayout[2].findViewById(R.id.imgbtn_Reduce);
		barG           = (SeekBarDownUp)subLayout[2].findViewById(R.id.seekbar_Adjust);
		txtG= (TextView)subLayout[2].findViewById(R.id.txt_Value);
		barG.setMax(256);
		
		btnBPlusButton = (ImageButton) subLayout[3].findViewById(R.id.imgbtn_Increase);
		btnBMinusButton = (ImageButton) subLayout[3].findViewById(R.id.imgbtn_Reduce);
		barB          = (SeekBarDownUp)subLayout[3].findViewById(R.id.seekbar_Adjust);
		txtB= (TextView)subLayout[3].findViewById(R.id.txt_Value);
		barB.setMax(256);
		
		btnContrastPlusButton = (ImageButton) subLayout[4].findViewById(R.id.imgbtn_Increase);
		btnContrastMinusButton = (ImageButton) subLayout[4].findViewById(R.id.imgbtn_Reduce);
		barContrast           = (SeekBarDownUp)subLayout[4].findViewById(R.id.seekbar_Adjust);
		txtContrast= (TextView)subLayout[4].findViewById(R.id.txt_Value);
		barContrast.setMax(100);
		
		btnSaturationPlusButton = (ImageButton) subLayout[5].findViewById(R.id.imgbtn_Increase);
		btnSaturationMinusButton = (ImageButton) subLayout[5].findViewById(R.id.imgbtn_Reduce);
		barSaturation           = (SeekBarDownUp)subLayout[5].findViewById(R.id.seekbar_Adjust);
		txtSaturation= (TextView)subLayout[5].findViewById(R.id.txt_Value);
		barSaturation.setMax(100);
 

		// //////////////////////////////////////////////////////////////////////////////
		//����
		btnBrightPlusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetBrightIncrease();
		}
		});
		btnBrightMinusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetBrightReduce();
			}
		});
		
		//ɫ�� R
		btnRPlusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//��ȡ��ǰ��Rֵ��colortemp����					
				SetColorTempRGBIncrease(0);
			}
		});
		btnRMinusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetColorTempRGBReduce(0);
			}
		});
		
		//ɫ�� G
		btnGPlusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetColorTempRGBIncrease(1);
			}
			});
		btnGMinusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			     SetColorTempRGBReduce(1);
			}
		});
		
		//ɫ�� B
		btnBPlusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetColorTempRGBIncrease(2);
			}
			});
		btnBMinusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    SetColorTempRGBReduce(2);
			}
		});
		
		btnContrastPlusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetContrastIncrease();
			}
			});
		btnContrastMinusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetContrastReduce();
			}
		});
		
		btnSaturationPlusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    SetSaturationIncrease();
			}
			});
		
		btnSaturationMinusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			   SetSaturationReduce();
			}
		});
		// //////////////////////////////////////////////////////////////////////////////
		barBright.setOnSeekBarChangeListener(new SeekBarDownUp.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(
							SeekBarDownUp VerticalSeekBar, int progress,
							boolean fromUser) {
						   txtBright.setText(String.valueOf(progress));
						   int nBrightness =barBright.getProgress();
						   SetBrightValue(nBrightness);
					}
					@Override
					public void onStartTrackingTouch(
							SeekBarDownUp VerticalSeekBar) {
					}

					@Override
					public void onStopTrackingTouch(
							SeekBarDownUp VerticalSeekBar) {// �����϶�
						int nBrightness =barBright.getProgress();
			            
						SetBrightValue(nBrightness);
						
					}
				});
		barR.setOnSeekBarChangeListener(new SeekBarDownUp.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(
							SeekBarDownUp VerticalSeekBar, int progress,
							boolean fromUser) {
					
						int nRed = barR.getProgress();
						SetR(nRed);
						txtR.setText(String.valueOf(progress));
					}

					@Override
					public void onStartTrackingTouch(
							SeekBarDownUp VerticalSeekBar) {// ��ʼ�϶�
					}

					@Override
					public void onStopTrackingTouch(
							SeekBarDownUp VerticalSeekBar) {// �����϶� s
						int nRed = barR.getProgress();
						SetR(nRed);

					}
				});
		barG.setOnSeekBarChangeListener(new SeekBarDownUp.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(
							SeekBarDownUp VerticalSeekBar, int progress,
							boolean fromUser) {
						txtG.setText(String.valueOf(progress));
						int nGreen = barG.getProgress();	
						SetG(nGreen);
					}

					@Override
					public void onStartTrackingTouch(
							SeekBarDownUp VerticalSeekBar) {// ��ʼ�϶�	
					}

					@Override
					public void onStopTrackingTouch(
							SeekBarDownUp VerticalSeekBar) {// �����϶� s
						int nGreen = barG.getProgress();	
						SetG(nGreen);

					}
				});
		barB.setOnSeekBarChangeListener(new SeekBarDownUp.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(
							SeekBarDownUp VerticalSeekBar, int progress,
							boolean fromUser) {
						txtB.setText(String.valueOf(progress));
						int nBlue = barB.getProgress();			
						SetB(nBlue);
					}

					@Override
					public void onStartTrackingTouch(
							SeekBarDownUp VerticalSeekBar) {// ��ʼ�϶�
					
					}

					@Override
					public void onStopTrackingTouch(
							SeekBarDownUp VerticalSeekBar) {// �����϶� s
						int nBlue = barB.getProgress();
						
						SetB(nBlue);

					}
				});
		barContrast.setOnSeekBarChangeListener(new SeekBarDownUp.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(
							SeekBarDownUp VerticalSeekBar, int progress,
							boolean fromUser) {	
						txtContrast.setText(String.valueOf(progress));
						int nContrast = barContrast.getProgress();	
						SetContrastValue(nContrast);
					}

					@Override
					public void onStartTrackingTouch(
							SeekBarDownUp VerticalSeekBar) {// ��ʼ�϶�
					
					}

					@Override
					public void onStopTrackingTouch(
							SeekBarDownUp VerticalSeekBar) {// �����϶� s
					
						int nContrast = barContrast.getProgress();	
						SetContrastValue(nContrast);
      
					}
				});
		barSaturation.setOnSeekBarChangeListener(new SeekBarDownUp.OnSeekBarChangeListener() {
					@Override
					public void onProgressChanged(
							SeekBarDownUp VerticalSeekBar, int progress,
							boolean fromUser) {
						txtSaturation.setText(String.valueOf(progress));
						int nSaturation = barSaturation.getProgress();
						SetSaturationValue(nSaturation);
					}

					@Override
					public void onStartTrackingTouch(
							SeekBarDownUp VerticalSeekBar) {// ��ʼ�϶�
					}

					@Override
					public void onStopTrackingTouch(
							SeekBarDownUp VerticalSeekBar) {// �����϶� s
					
						int nSaturation = barSaturation.getProgress();
			
						SetSaturationValue(nSaturation);

					}
				});
		
		
		/**
		 * ���浱ǰ���ݵ���Ļ
		 */
		
		btn_SaveCur.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				/**
				 * �Ƿ񽫵�ǰ�Ĳ������浽��Ļ
				 */
				
				//��ʾ�Ƿ�ɾ��
				new AlertDialog.Builder(v.getContext())
				/* �������ڵ�����ͷ���� */
				.setTitle(getString(R.string.save))
				.setMessage(getString(R.string.control_save))
				/* ���õ������ڵ�ͼʽ */
				.setIcon(android.R.drawable.ic_dialog_info)
				/* ���õ������ڵ���Ϣ */
				.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						
						ArrayList<IntfData> tArrayList = InterfaceDB.GetAllRecord(CurrentLeddisplayID);
						for (int i1 = 0; i1 < tArrayList.size(); i1++) {
							IntfData tInterfData =tArrayList.get(i1);				
							ConnChartComm.SaveCabinetParam(tInterfData.Id, CurrentLeddisplayID);	
						}
						
					}
				})
				.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() { /* �����������ڵķ����¼� */
					public void onClick(DialogInterface dialoginterface, int i) {
						
					}
				}).show();
			}
		});

	}
		
	public void SetSaturationReduce() {
        int progress = barSaturation.getProgress();
		if (progress<=0) {
			progress=0;
		}
		else {
			progress--;
		}
		barSaturation.setProgress(progress);
		txtSaturation.setText(String.valueOf(progress));
		
		//��������
	    SetSaturationValue(progress);	
	      
	    RefreshUI();
	}

	public void SetSaturationIncrease() {
		int progress = barSaturation.getProgress();	  	      
		if (progress>=100) {
			progress=100;
		}
		else {
			progress++;
		}
		barSaturation.setProgress(progress);
		txtSaturation.setText(String.valueOf(progress));
		
		//��������
		SetSaturationValue(progress);		  
		RefreshUI();
	}

	public void SetContrastReduce() {
	
          int progress = barContrast.getProgress();
		  
		  if (progress<=0) {
			progress=0;
		  }
		  else {
			progress--;
		  }
		  
		  //��������
		  barContrast.setProgress(progress);
		  txtContrast.setText(String.valueOf(progress));
		  
		  SetContrastValue(progress);
		  RefreshUI();
	}

	public void SetContrastIncrease() {

	      int progress = barContrast.getProgress();
		  
		  if (progress>=100) {
			progress=100;
		  }
		  else {
			progress++;
		  }
		  
		  barContrast.setProgress(progress);
		  txtContrast.setText(String.valueOf(progress));
		  
		  //��������
		  SetContrastValue(progress);
		  
		  RefreshUI();
	}

	public void SetColorTempRGBReduce(int RGBSEL) {	
		int nValue = 0 ; 
		if (RGBSEL==0) {
			nValue = barR.getProgress();
		}
		if (RGBSEL==1) {
			nValue = barG.getProgress();
		}
		if (RGBSEL==2) {
			nValue = barB.getProgress();
		}
		
		if (nValue<=0) {
			nValue=0;
		}
		else {
			nValue--;
		}
			  
		if (RGBSEL==0) {
			barR.setProgress(nValue);
			SetR(nValue);
		}
		if (RGBSEL==1) {
			barG.setProgress(nValue);
			SetG(nValue);
		}
		if (RGBSEL==2) {
			barB.setProgress(nValue);
			SetB(nValue);
		}
		
		RefreshUI();
	}

	public void SetColorTempRGBIncrease(int RGBSEL) {
	
		int nValue = 0 ; 
		
		if (RGBSEL==0) {
			nValue = barR.getProgress();
		}
		if (RGBSEL==1) {
			nValue = barG.getProgress();
		}
		if (RGBSEL==2) {
			nValue = barB.getProgress();
		}
		
		if (nValue>=256) {
			nValue=256;
		}
		else {
			nValue++;
		}
	
		if (RGBSEL==0) {
			barR.setProgress(nValue);
			SetR(nValue);
		}
		if (RGBSEL==1) {
			barG.setProgress(nValue);
			SetG(nValue);
		}
		if (RGBSEL==2) {
			barB.setProgress(nValue);
			SetB(nValue);
		}
	
		RefreshUI();
		
	}

	public void SetBrightReduce() {

		  int progress = barBright.getProgress();
	  
		  progress--;
		  
		  if (progress<=0) {
			progress=0;
		  }
	  
		  barBright.setProgress(progress);
		  txtBright.setText(String.valueOf(progress));
		  
		  //��������
		  SetBrightValue(progress);
		  
		  RefreshUI();
	}


	public void SetBrightIncrease() {
		
		  int progress = barBright.getProgress();
		  
		   progress++;
		   if (progress>=100) {
				progress=100;
		   }
		   
		  barBright.setProgress(progress);
		  txtBright.setText(String.valueOf(progress));
		  //��������
		  SetBrightValue(progress);
		  
		  RefreshUI();
	}

	public void SetBrightValue(int bright)
	{
		CtrlLastStationDb.UpdateLastBright(bright, CurrentLeddisplayID);
		
	
		ArrayList<IntfData> tArrayList = InterfaceDB.GetAllRecord(CurrentLeddisplayID);
		
		for (int i = 0; i < tArrayList.size(); i++) {
			IntfData tInterfData =tArrayList.get(i);
			LEDParamComm.SetBright(bright,tInterfData.macaddress, tInterfData.Id%1000);
		}
		
		
	}
	
	public void SetContrastValue(int contrast)
	{
		CtrlLastStationDb.UpdateLastContrast(contrast, CurrentLeddisplayID);
		
		 ArrayList<ChnData> tArrayList = ChannelDB.GetAllRecord(CurrentLeddisplayID);
		
		for (int i = 0; i < tArrayList.size(); i++) {
			LEDParamComm.SetContrast(contrast, tArrayList.get(i).Id);
		}
		
	
	}
	
	public void SetSaturationValue(int saturation)
	{
		CtrlLastStationDb.UpdateLastSaturation(saturation, CurrentLeddisplayID);
		
	    ArrayList<ChnData> tArrayList = ChannelDB.GetAllRecord(CurrentLeddisplayID);
		
		for (int i = 0; i < tArrayList.size(); i++) {
			LEDParamComm.SetSaturation(saturation, tArrayList.get(i).Id);
		}
		
	
	}
	
	public void SetRGBDataByTemper(int nTemper)
	{	

		ArrayList<IntfData> tArrayList = InterfaceDB.GetAllRecord(CurrentLeddisplayID);
			
		ColourRGB sColorRGB = new ColourRGB();
		
		sColorRGB.setId(1);
		sColorRGB.setM_bEnable(true);	
		String m_nColorTemperature = txtColorTemp.getText().toString();
		sColorRGB.setM_nColorTemperature(m_nColorTemperature);	
		
	
		ColorTemperData tTemperData = ColorTemperDb.GetTmperRecord(nTemper, CurrentLeddisplayID);
			
		
		barR.setProgress(tTemperData.nRed);
		barG.setProgress(tTemperData.nGreen);
		barB.setProgress(tTemperData.nBlue);
		
		sColorRGB.setnRed(barR.getProgress());
		sColorRGB.setnGreen(barG.getProgress());
		sColorRGB.setnBlue(barB.getProgress());
		
		sColorRGB.setnICRed(tTemperData.nICRed);
		sColorRGB.setnICGreen(tTemperData.nICGreen);
		sColorRGB.setnICBlue(tTemperData.nICBlue);
		
		sColorRGB.setnRedLow(tTemperData.nRedLow);
		sColorRGB.setnGreenLow(tTemperData.nGreenLow);
		sColorRGB.setnBlueLow(tTemperData.nRedLow);
		
		sColorRGB.setnICRedLow(tTemperData.nICRedLow);
		sColorRGB.setnICGreenLow(tTemperData.nICGreenLow);
		sColorRGB.setnICBlueLow(tTemperData.nICBlueLow);
		
		
		for (int i = 0; i < tArrayList.size(); i++) {
			IntfData tInterfData =tArrayList.get(i);
			LEDParamComm.SetColorTemp(tInterfData.macaddress, tInterfData.Id%1000, sColorRGB, (short)0, (short)0);
		}
		
	}
	
	public void SetRGBData()
	{	

		ArrayList<IntfData> tArrayList = InterfaceDB.GetAllRecord(CurrentLeddisplayID);
			
		ColourRGB sColorRGB = new ColourRGB();
		
		sColorRGB.setId(1);
		sColorRGB.setM_bEnable(true);	
		String m_nColorTemperature = txtColorTemp.getText().toString();
		sColorRGB.setM_nColorTemperature(m_nColorTemperature);	
		
		int nTemper = nColorTmp[ColorTmpIndex];
		ColorTemperData tTemperData = ColorTemperDb.GetTmperRecord(nTemper, CurrentLeddisplayID);
				
		sColorRGB.setnRed(barR.getProgress());
		sColorRGB.setnGreen(barG.getProgress());
		sColorRGB.setnBlue(barB.getProgress());
		
		sColorRGB.setnICRed(tTemperData.nICRed);
		sColorRGB.setnICGreen(tTemperData.nICGreen);
		sColorRGB.setnICBlue(tTemperData.nICBlue);
		
		sColorRGB.setnRedLow(tTemperData.nRedLow);
		sColorRGB.setnGreenLow(tTemperData.nGreenLow);
		sColorRGB.setnBlueLow(tTemperData.nRedLow);
		
		sColorRGB.setnICRedLow(tTemperData.nICRedLow);
		sColorRGB.setnICGreenLow(tTemperData.nICGreenLow);
		sColorRGB.setnICBlueLow(tTemperData.nICBlueLow);
		
		////////////////
		sColorRGB.setnICRed1(tTemperData.nICRed1);
		sColorRGB.setnICGreen1(tTemperData.nICGreen1);
		sColorRGB.setnICBlue1(tTemperData.nICBlue1);
		
		sColorRGB.setnICRed2(tTemperData.nICRed2);
		sColorRGB.setnICGreen2(tTemperData.nICGreen2);
		sColorRGB.setnICBlue2(tTemperData.nICBlue2);
		
		sColorRGB.setnICRed6(tTemperData.nICRed6);
		sColorRGB.setnICGreen6(tTemperData.nICGreen6);
		sColorRGB.setnICBlue6(tTemperData.nICBlue6);
		
		sColorRGB.setnICRed7(tTemperData.nICRed7);
		sColorRGB.setnICGreen7(tTemperData.nICGreen7);
		sColorRGB.setnICBlue7(tTemperData.nICBlue7);
		
		sColorRGB.setnICRed8(tTemperData.nICRed8);
		sColorRGB.setnICGreen8(tTemperData.nICGreen8);
		sColorRGB.setnICBlue8(tTemperData.nICBlue8);
		
		sColorRGB.setnICRed9(tTemperData.nICRed9);
		sColorRGB.setnICGreen9(tTemperData.nICGreen9);
		sColorRGB.setnICBlue9(tTemperData.nICBlue9);
		
		///////////
		for (int i = 0; i < tArrayList.size(); i++) {
			IntfData tInterfData =tArrayList.get(i);
			LEDParamComm.SetColorTemp(tInterfData.macaddress, tInterfData.Id%1000, sColorRGB, (short)0, (short)0);
		}
		
	}
	
	public void SetColortempSet(int ColorTemp)
	{   
		CtrlLastStationDb.UpdateLastColorTemper(ColorTemp, CurrentLeddisplayID);
		SetRGBData();	
	}
	
	public void SetR(int RValue)
	{
		CtrlLastStationDb.UpdateLastR(RValue, CurrentLeddisplayID);	
		int nTemper = nColorTmp[ColorTmpIndex];
		ColorTemperDb.UpdateColorTemperRValue(nTemper, RValue, CurrentLeddisplayID);
		SetRGBData();
	}
	
	public void SetG(int GValue)
	{
		CtrlLastStationDb.UpdateLastR(GValue, CurrentLeddisplayID);	
		int nTemper = nColorTmp[ColorTmpIndex];
		ColorTemperDb.UpdateColorTemperGValue(nTemper, GValue, CurrentLeddisplayID);
		SetRGBData();
	}
	
	public void SetB(int BValue)
	{
		CtrlLastStationDb.UpdateLastR(BValue, CurrentLeddisplayID);
		int nTemper = nColorTmp[ColorTmpIndex];
		ColorTemperDb.UpdateColorTemperBValue(nTemper, BValue, CurrentLeddisplayID);
		SetRGBData();
	}
	

	
	public CtrlData LoadDbProfile(int DisplayId,int ProfileId)
	{
		//����	
		CtrlData tCtrlData = CtrlDb.GetRecordByLedidAndProfileId(DisplayId, ProfileId);	
		return tCtrlData;
	
	}
	
	public void LoadGpConfig(int LEDID) {
		
		m_ArrGpNameArrayList.clear();
		m_ArrGpNameArrayList = ChanGroupDb.GetAllGpNameRecords(LEDID);
		hListViewAdapter = new HorizontalListViewAdapter(this,m_ArrGpNameArrayList,LEDID);
		hListView.setAdapter(hListViewAdapter);
		
		
		//ĳ��ѡ���item
		hListView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {                              
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		
		hListViewAdapter.notifyDataSetChanged();

	}



	public static ControlActivity getInstance() {
		if (mControlActivity != null) {
			return mControlActivity;
		}
		return null;
	}

	public void SetFocusForSetBright() {
		layout_brightness.setBackgroundResource(R.drawable.frame_selected);
		layout_colortemperature.setBackgroundResource(R.drawable.frame_normal);
		layout_colortemperatureID.setBackgroundResource(R.drawable.frame_normal);
		seekbar_colortemperature_r.setBackgroundResource(R.drawable.frame_normal);
		seekbar_colortemperature_g.setBackgroundResource(R.drawable.frame_normal);
		seekbar_colortemperature_b.setBackgroundResource(R.drawable.frame_normal);
		layout_contrast.setBackgroundResource(R.drawable.frame_normal);
		layout_saturation.setBackgroundResource(R.drawable.frame_normal);

		txtColorTemp.setBackgroundColor(Color.TRANSPARENT);
		RefreshUI();
//		for (int i = 0; i < 6; i++) {
//			if (0 == i) {
//				txt_Value[i].setBackgroundColor(Color.BLUE);
//			} else {
//				txt_Value[i].setBackgroundColor(Color.TRANSPARENT);
//			}
//		}
//		Toast.makeText(this, getString(R.string.log_control_brightness), 500);
//		CustomToast.showToast(ControlActivity.this,getString(R.string.log_control_brightness), 500);
	}

	public void SetFocusForSetColorTemp() {
		layout_brightness.setBackgroundResource(R.drawable.frame_normal);
		layout_colortemperature.setBackgroundResource(R.drawable.frame_selected);
		layout_contrast.setBackgroundResource(R.drawable.frame_normal);
		layout_saturation.setBackgroundResource(R.drawable.frame_normal);

		nCurrentColorTemp++;
		switch (nCurrentColorTemp) {
		case 1:
			layout_colortemperatureID.setBackgroundResource(R.drawable.frame_normal);
			seekbar_colortemperature_r.setBackgroundResource(R.drawable.frame_selected);
			seekbar_colortemperature_g.setBackgroundResource(R.drawable.frame_normal);
			seekbar_colortemperature_b.setBackgroundResource(R.drawable.frame_normal);
			break;
		case 2:
			layout_colortemperatureID.setBackgroundResource(R.drawable.frame_normal);
			seekbar_colortemperature_r.setBackgroundResource(R.drawable.frame_normal);
			seekbar_colortemperature_g.setBackgroundResource(R.drawable.frame_selected);
			seekbar_colortemperature_b.setBackgroundResource(R.drawable.frame_normal);
			break;		
		case 3:
			layout_colortemperatureID.setBackgroundResource(R.drawable.frame_normal);
			seekbar_colortemperature_r.setBackgroundResource(R.drawable.frame_normal);
			seekbar_colortemperature_g.setBackgroundResource(R.drawable.frame_normal);
			seekbar_colortemperature_b.setBackgroundResource(R.drawable.frame_selected);
			break;
		default:
			layout_colortemperatureID.setBackgroundResource(R.drawable.frame_selected);
			seekbar_colortemperature_r.setBackgroundResource(R.drawable.frame_normal);
			seekbar_colortemperature_g.setBackgroundResource(R.drawable.frame_normal);
			seekbar_colortemperature_b.setBackgroundResource(R.drawable.frame_normal);
			nCurrentColorTemp = 0;
			break;
		}
		RefreshUI();
		/*
		if (3 == nCurrentColorTemp || -1 == nCurrentColorTemp) {
			nCurrentColorTemp = 0;
		} else {
			nCurrentColorTemp++;
		}*/
		/*
		switch (nCurrentColorTemp) {
		case 0:
			txt_ColorTempValue.setBackgroundColor(Color.BLUE);
			for (int i = 0; i < 6; i++) {
				txt_Value[i].setBackgroundColor(Color.TRANSPARENT);
			}
			CustomToast.showToast(ControlActivity.this,
					getString(R.string.log_control_colortemplevel), 500);

			break;
		case 1:
			txt_ColorTempValue.setBackgroundColor(Color.TRANSPARENT);
			for (int i = 0; i < 6; i++) {
				if (1 == i) {
					txt_Value[i].setBackgroundColor(Color.BLUE);
				} else {
					txt_Value[i].setBackgroundColor(Color.TRANSPARENT);
				}
			}
			CustomToast.showToast(ControlActivity.this,
					R.string.log_control_colortempred, 500);
			break;
		case 2:
			txt_ColorTempValue.setBackgroundColor(Color.TRANSPARENT);
			for (int i = 0; i < 6; i++) {
				if (2 == i) {
					txt_Value[i].setBackgroundColor(Color.BLUE);
				} else {
					txt_Value[i].setBackgroundColor(Color.TRANSPARENT);
				}
			}
			CustomToast.showToast(ControlActivity.this,
					R.string.log_control_colortempgreen, 500);
			break;
		case 3:
			txt_ColorTempValue.setBackgroundColor(Color.TRANSPARENT);
			for (int i = 0; i < 6; i++) {
				if (3 == i) {
					txt_Value[i].setBackgroundColor(Color.BLUE);
				} else {
					txt_Value[i].setBackgroundColor(Color.TRANSPARENT);
				}
			}
			CustomToast.showToast(ControlActivity.this,
					R.string.log_control_colortempgreen, 500);
			break;

		default:
			break;
		}
		*/

	}

	public void SetFocusForSetSaturation() {
		layout_brightness.setBackgroundResource(R.drawable.frame_normal);
		layout_colortemperature.setBackgroundResource(R.drawable.frame_normal);
		layout_colortemperatureID.setBackgroundResource(R.drawable.frame_normal);
		seekbar_colortemperature_r.setBackgroundResource(R.drawable.frame_normal);
		seekbar_colortemperature_g.setBackgroundResource(R.drawable.frame_normal);
		seekbar_colortemperature_b.setBackgroundResource(R.drawable.frame_normal);
		layout_contrast.setBackgroundResource(R.drawable.frame_normal);
		layout_saturation.setBackgroundResource(R.drawable.frame_selected);
		RefreshUI();
//
//		txt_ColorTempValue.setBackgroundColor(Color.TRANSPARENT);
//		for (int i = 1; i < 6; i++) {
//			if (5 == i) {
//				txt_Value[i].setBackgroundColor(Color.BLUE);
//			} else {
//				txt_Value[i].setBackgroundColor(Color.TRANSPARENT);
//			}
//		}
//		CustomToast.showToast(ControlActivity.this,
//				getString(R.string.log_control_cont), 500);
//		nCurrentColorTemp = -1;
	}

	public void SetFocusForSetContrast() {
		layout_brightness.setBackgroundResource(R.drawable.frame_normal);
		layout_colortemperature.setBackgroundResource(R.drawable.frame_normal);
		layout_colortemperatureID.setBackgroundResource(R.drawable.frame_normal);
		seekbar_colortemperature_r.setBackgroundResource(R.drawable.frame_normal);
		seekbar_colortemperature_g.setBackgroundResource(R.drawable.frame_normal);
		seekbar_colortemperature_b.setBackgroundResource(R.drawable.frame_normal);
		layout_contrast.setBackgroundResource(R.drawable.frame_selected);
		layout_saturation.setBackgroundResource(R.drawable.frame_normal);
		RefreshUI();
//		txt_ColorTempValue.setBackgroundColor(Color.TRANSPARENT);
//		for (int i = 1; i < 6; i++) {
//			if (4 == i) {
//				txt_Value[i].setBackgroundColor(Color.BLUE);
//			} else {
//				txt_Value[i].setBackgroundColor(Color.TRANSPARENT);
//			}
//		}
//		CustomToast.showToast(ControlActivity.this,
//				getString(R.string.log_control_satn), 500);
//		nCurrentColorTemp = -1;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	private void ClearSetFocus() {
		layout_brightness.setBackgroundResource(R.drawable.frame_normal);
		layout_colortemperature.setBackgroundResource(R.drawable.frame_normal);
		layout_colortemperatureID.setBackgroundResource(R.drawable.frame_normal);
		seekbar_colortemperature_r.setBackgroundResource(R.drawable.frame_normal);
		seekbar_colortemperature_g.setBackgroundResource(R.drawable.frame_normal);
		seekbar_colortemperature_b.setBackgroundResource(R.drawable.frame_normal);
		layout_contrast.setBackgroundResource(R.drawable.frame_normal);
		layout_saturation.setBackgroundResource(R.drawable.frame_normal);
		RefreshUI();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	// ѡ��ģ��
	public int SetTemplate(int index) {

		CurrentProfileID = index;
		
		CtrlData tCtrlData = LoadDbProfile(CurrentLeddisplayID, CurrentProfileID);
		
	    barBright.setProgress(tCtrlData.nBright);
	    txtBright.setText(String.valueOf(tCtrlData.nBright));
		
		//ɫ��
		int nColortemp = tCtrlData.nColorTemp;
		txtColorTemp.setText(String.valueOf(nColortemp));
		
		//ɫ�¶�Ӧ��RGB
		barR.setProgress(tCtrlData.nR);
		barG.setProgress(tCtrlData.nG);
		barB.setProgress(tCtrlData.nB);
		
		barContrast.setProgress(tCtrlData.nContrast);
		barSaturation.setProgress(tCtrlData.nSaturation);
		txtR.setText(String.valueOf(tCtrlData.nR));
		txtG.setText(String.valueOf(tCtrlData.nG));
		txtB.setText(String.valueOf(tCtrlData.nB));

		txtContrast.setText(String.valueOf(tCtrlData.nContrast));
		txtSaturation.setText(String.valueOf(tCtrlData.nSaturation));
		
		CustomToast.showToast(this, "", 5);

//		BtnClearSelStates();
//		switch (index) {
//		case 1:
//			btn_Group1.setBackgroundColor(R.drawable.selector_button);
//		    break;
//		case 2:
//			btn_Group2.setBackgroundColor(R.drawable.selector_button);	
//			break;
//		case 3:
//			btn_Group3.setBackgroundColor(R.drawable.selector_button);	
//			break;
//		case 4:
//			btn_Group4.setBackgroundColor(R.drawable.selector_button);	
//			break;
//		case 5:	
//			btn_Group5.setBackgroundColor(R.drawable.selector_button);	
//			break;
//		} 
//		

		return 0 ;
		
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	
	public void RefreshUI()
	{
		CustomToast.showToast(ControlActivity.this,"", 50);
	}

	// ѡ��ͨ��
	public void SwitchChannel() {
		ClearSetFocus();
		int nGpCnt = m_ArrGpNameArrayList.size();
		if (nGpCnt > 0) {
			if (nCurrentChannel < 0 || nCurrentChannel >= nGpCnt - 1) {
				nCurrentChannel = 0;
			} else {
				nCurrentChannel++;
			}
			
			String strGpNameString = m_ArrGpNameArrayList.get(nCurrentChannel);
			hListViewAdapter.SwitchChannel(strGpNameString);
		}
	}
	
	// ////////////////////////////////////////////////////////////////////////////////////////
	// ����ȷ��
	public int SetOK() {
		ProgressDialog m_pDialog;
		// ����ProgressDialog����
		m_pDialog = new ProgressDialog(ControlActivity.this);
		// ���ý�������񣬷��ΪԲ�Σ���ת��
		m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// ����ProgressDialog ����
		m_pDialog.setTitle(getString(R.string.log_control_message));
		// ����ProgressDialog ��ʾ��Ϣ
		m_pDialog.setMessage(getString(R.string.log_control_confirmingopration));
		// ����ProgressDialog �Ľ������Ƿ���ȷ
		m_pDialog.setIndeterminate(false);
		// ����ProgressDialog �Ƿ���԰��˻ذ���ȡ��
		// ��ProgressDialog��ʾ
		m_pDialog.show();
		return 0;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////
	// ����ȡ��
	public int SetCancel() {
		return 0;

	}

	// ////////////////////////////////////////////////////////////////////////////////////////

	public void BtnClearSelStates()
	{
		btn_Group1.setBackgroundColor(Color.rgb(0x3e, 0x3d, 0x43));
		btn_Group2.setBackgroundColor(Color.rgb(0x3e, 0x3d, 0x43));
		btn_Group3.setBackgroundColor(Color.rgb(0x3e, 0x3d, 0x43));
		btn_Group4.setBackgroundColor(Color.rgb(0x3e, 0x3d, 0x43));
		btn_Group5.setBackgroundColor(Color.rgb(0x3e, 0x3d, 0x43));
	}

	View.OnClickListener clickHandler = new View.OnClickListener() {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.text_leddisplasetting: {
				
				CurrentLeddisplayID++;
				
				if (CurrentLeddisplayID==5) {
					CurrentLeddisplayID=1;
				}
				
				Ak10Application.SetLedId(CurrentLeddisplayID);				
				int nRet = setCurrentLeddisplayID(CurrentLeddisplayID);
			
				btn_DisplayID.setText("LED"+String.valueOf(CurrentLeddisplayID).toString());
				
				if (nRet!=-1) {			
					LoadGpConfig(CurrentLeddisplayID);
				}
				
			}
				break;
			case R.id.text_controlback:
				finish();
				break;
			case R.id.text_controlmain:
				startActivity(new Intent(ControlActivity.this,
						HomePageActivity.class));
				break;

			case R.id.btn_Group1:
		
//				BtnClearSelStates();
//				btn_Group1.setBackgroundColor(R.anim.shape_normal);
				SetTemplate(1);
				
				break;
			case R.id.btn_Group2:

//				BtnClearSelStates();
//				btn_Group2.setBackgroundColor(R.anim.shape_normal);
				SetTemplate(2);
				break;
			case R.id.btn_Group3:
		
//				BtnClearSelStates();
//				btn_Group3.setBackgroundColor(R.anim.shape_normal);
				SetTemplate(3);
				
				break;
			case R.id.btn_Group4:
			
//				BtnClearSelStates();
//				btn_Group4.setBackgroundColor(R.anim.shape_normal);
				SetTemplate(4);
				break;
			case R.id.btn_Group5:
			
//				BtnClearSelStates();
//				btn_Group5.setBackgroundColor(R.anim.shape_normal);
				SetTemplate(5);
				break;

			case R.id.imgbtn_Left:
				// �������״̬
			
				if(ColorTmpIndex<=0){
					ColorTmpIndex=0;
				}else {
					ColorTmpIndex--;
				}
				
				int ColorTemp = nColorTmp[ColorTmpIndex];
				
				txtColorTemp.setText(String.valueOf(ColorTemp));				
				//���浽���ݿ�
				CtrlLastStationDb.UpdateLastColorTemper(ColorTemp, CurrentLeddisplayID);
				
				//
				SetRGBDataByTemper(ColorTemp);
				break;
			case R.id.imgbtn_Right:
			
				if(ColorTmpIndex>=7){
					ColorTmpIndex=7;
				}else {
					ColorTmpIndex++;
				}
				
				int ColorTempPlus = nColorTmp[ColorTmpIndex];
				
				txtColorTemp.setText(String.valueOf(ColorTempPlus));
		
				//���浽���ݿ�
				CtrlLastStationDb.UpdateLastColorTemper(ColorTempPlus, CurrentLeddisplayID);
				
				SetRGBDataByTemper(ColorTempPlus);
				break;

			default:
				break;
			}
			
			RefreshUI();
		}
	};
	View.OnLongClickListener longclickHandler = new View.OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_Group1:
				new AlertDialog.Builder(ControlActivity.this)
				.setTitle(getString(R.string.log_control_message))
				.setMessage(getString(R.string.saveastemplate)+"1?")
				.setPositiveButton(getString(R.string.OK),new DialogInterface.OnClickListener() {
				        @Override
				        public void onClick(DialogInterface dialog, int which) {
				              	SaveTemplate(1);
				        }
				})
				.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() { 
				       public void onClick(DialogInterface dialog, int id) { 
				                dialog.cancel(); 
				       } 
				}) 
				.show();
			
				break;
			case R.id.btn_Group2:
				new AlertDialog.Builder(ControlActivity.this)
				.setTitle(getString(R.string.log_control_message))
				.setMessage(getString(R.string.saveastemplate)+"2?")
				.setPositiveButton(getString(R.string.OK),new DialogInterface.OnClickListener() {
				        @Override
				        public void onClick(DialogInterface dialog, int which) {
				              	SaveTemplate(2);
				        }
				})
				.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() { 
				       public void onClick(DialogInterface dialog, int id) { 
				                dialog.cancel(); 
				       } 
				}) 
				.show();
				break;
			case R.id.btn_Group3:
				new AlertDialog.Builder(ControlActivity.this)
				.setTitle(getString(R.string.log_control_message))
				.setMessage(getString(R.string.saveastemplate)+"3?")
				.setPositiveButton(getString(R.string.OK),new DialogInterface.OnClickListener() {
				        @Override
				        public void onClick(DialogInterface dialog, int which) {
				              	SaveTemplate(3);
				        }
				})
				.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() { 
				       public void onClick(DialogInterface dialog, int id) { 
				                dialog.cancel(); 
				       } 
				}) 
				.show();
				break;
			case R.id.btn_Group4:
				new AlertDialog.Builder(ControlActivity.this)
				.setTitle(getString(R.string.log_control_message))
				.setMessage(getString(R.string.saveastemplate)+"4?")
				.setPositiveButton(getString(R.string.OK),new DialogInterface.OnClickListener() {
				        @Override
				        public void onClick(DialogInterface dialog, int which) {
				              	SaveTemplate(4);
				        }
				})
				.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() { 
				       public void onClick(DialogInterface dialog, int id) { 
				                dialog.cancel(); 
				       } 
				}) 
				.show();
				break;
			case R.id.btn_Group5:
				new AlertDialog.Builder(ControlActivity.this)
				.setTitle(getString(R.string.log_control_message))
				.setMessage(getString(R.string.saveastemplate)+"5?")
				.setPositiveButton(getString(R.string.OK),new DialogInterface.OnClickListener() {
				        @Override
				        public void onClick(DialogInterface dialog, int which) {
				              	SaveTemplate(5);
				        }
				})
				.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() { 
				       public void onClick(DialogInterface dialog, int id) { 
				                dialog.cancel(); 
				       } 
				}) 
				.show();
				break;
			default:
				break;
			}
			return false;
		}
	};

	public void SaveTemplate(int nTmplateIndex)
	{		
		CtrlData tCtrlData  = new CtrlData();		
		tCtrlData.nBright = barBright.getProgress();
		tCtrlData.nR = barR.getProgress();
		tCtrlData.nG = barG.getProgress();
		tCtrlData.nB = barB.getProgress();
		tCtrlData.nColorTemp = nColorTmp[ColorTmpIndex];
		tCtrlData.nContrast = barContrast.getProgress();
		tCtrlData.nSaturation = barSaturation.getProgress();
		
		CtrlDb.UpdateTempateParams(CurrentLeddisplayID, nTmplateIndex, tCtrlData);		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}


	@Override
	protected void onRestart() {
		SerialPortControlBroadCast.SetCurrentContext(this);
		mControlActivity = this;
		// TODO Auto-generated method stub
		super.onRestart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		//�������һ�α��������״̬
		LoadLastSavedStation(1);
		super.onResume();
	}
	
	
	/**�������һ�α䶯������
	 * 
	 */
	private int LoadLastSavedStation(int LEDID)
	{		
		CtrlLastStationData ctrlSavedData = CtrlLastStationDb.GetLEDRecord(LEDID);

		//������ݿ���û�����led����
		if (ctrlSavedData==null) {	
			Toast.makeText(this, "û��LED"+LEDID+"������", Toast.LENGTH_LONG).show();
			return -1;
		}		
		
	    barBright.setProgress(ctrlSavedData.bright);
	    txtBright.setText(String.valueOf(ctrlSavedData.bright));
		
		//ɫ��
		int nColortemp =ctrlSavedData.colortemper;
		txtColorTemp.setText(String.valueOf(nColortemp));
		
		//ɫ�¶�Ӧ��RGB
	
		barR.setProgress(ctrlSavedData.r);
		barG.setProgress(ctrlSavedData.g);
		barB.setProgress(ctrlSavedData.b);
		try {
			barContrast.setProgress(ctrlSavedData.contrast);
			barSaturation.setProgress(ctrlSavedData.saturation);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		txtR.setText(String.valueOf(ctrlSavedData.r));
		txtG.setText(String.valueOf(ctrlSavedData.g));
		txtB.setText(String.valueOf(ctrlSavedData.b));

		txtContrast.setText(String.valueOf(ctrlSavedData.contrast));
		txtSaturation.setText(String.valueOf(ctrlSavedData.saturation));
		
		String strGpNameString = ctrlSavedData.strGpName;
		for (int i = 0; i < m_ArrGpNameArrayList.size(); i++) {			
			if (strGpNameString.equals(m_ArrGpNameArrayList.get(i))) {			
				hListView.setSelection(i);
			}
		}

		//��ʼ��������
		SetBrightValue(ctrlSavedData.bright);
		SetRGBData();
		SetContrastValue(ctrlSavedData.contrast);
		SetSaturationValue(ctrlSavedData.saturation);
		
		return 0;
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mControlActivity = null;
		super.onStop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		PannelButtonDownService.observers.remove(this);
		PannelLedControlBroadCast.MakeLightsAlwaysOFF();
		mControlActivity = null;
		super.onDestroy();
	}

	@Override
	public int onChangedNotify(int xMsg, String xParam1, String xParam2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int onChangedNotifyKey(String xMsg, String xParam1, String xParam2) {
		// TODO Auto-generated method stub
		if (!xParam1.equals(ControlActivity.class.getName().toString())) {
			return 0;
		}
		byte cmd = Byte.parseByte(xParam2);
		switch (cmd) {
		case SerialPortControlBroadCast.CMD_TEMPLATE_NO1:
		
			SetTemplate(1);
			PannelLedControlBroadCast.MakeLightsAlwaysOFF();
			PannelLedControlBroadCast
					.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_TEMPLATE_NO1LIGHT);
			break;
		case SerialPortControlBroadCast.CMD_TEMPLATE_NO2:

			SetTemplate(2);
			PannelLedControlBroadCast.MakeLightsAlwaysOFF();
			PannelLedControlBroadCast
					.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_TEMPLATE_NO2LIGHT);
			break;
		case SerialPortControlBroadCast.CMD_TEMPLATE_NO3:
			SetTemplate(3);
			PannelLedControlBroadCast.MakeLightsAlwaysOFF();
			PannelLedControlBroadCast
					.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_TEMPLATE_NO3LIGHT);
			break;
		case SerialPortControlBroadCast.CMD_TEMPLATE_NO4:
			SetTemplate(4);
			PannelLedControlBroadCast.MakeLightsAlwaysOFF();
			PannelLedControlBroadCast
					.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_TEMPLATE_NO4LIGHT);
			break;
		case SerialPortControlBroadCast.CMD_TEMPLATE_NO5:
			SetTemplate(5);
			PannelLedControlBroadCast.MakeLightsAlwaysOFF();
			PannelLedControlBroadCast
					.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_TEMPLATE_NO5LIGHT);
			break;
		case SerialPortControlBroadCast.CMD_BRIGHTNESS:
			SetFocusForSetBright();
			PannelLedControlBroadCast.MakePannelChoicesOFF();
			PannelLedControlBroadCast
					.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_BRIGHTNESSLIGHT);
			break;
		case SerialPortControlBroadCast.CMD_COLORTEMP:
			SetFocusForSetColorTemp();
			PannelLedControlBroadCast.MakePannelChoicesOFF();
			PannelLedControlBroadCast
					.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_COLORTEMPLIGHT);
			break;
		case SerialPortControlBroadCast.CMD_SATURATION:
			SetFocusForSetSaturation();
			PannelLedControlBroadCast.MakePannelChoicesOFF();
			PannelLedControlBroadCast
					.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_SATURATIONLIGHT);
			break;
		case SerialPortControlBroadCast.CMD_CONTRAST:
			SetFocusForSetContrast();
			PannelLedControlBroadCast.MakePannelChoicesOFF();
			PannelLedControlBroadCast
					.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_CONTRASTLIGHT);
			break;
		case SerialPortControlBroadCast.CMD_SWITCH:
			SwitchChannel();
			PannelLedControlBroadCast.MakePannelChoicesOFF();
			PannelLedControlBroadCast
					.MakeSingleLightsAlwaysON(PannelLedControlBroadCast.CMD_SWITCHLIGHT);
			break;
		case SerialPortControlBroadCast.CMD_OK:
			break;
		case SerialPortControlBroadCast.CMD_CANCEL:
			 System.out.println("cancel..."); 
			PannelLedControlBroadCast.MakeLightsAlwaysOFF();
			finish();
			break;
		case SerialPortControlBroadCast.CMD_CLOCKWISE:
			switch (SerialPortControlBroadCast.GetCMD_CURRENT()) {
			case SerialPortControlBroadCast.CMD_BRIGHTNESS:
				if (null != ControlActivity.getInstance()) {
					try {
						ControlActivity.getInstance().SetBrightIncrease();
					} catch (IllegalArgumentException e) {

						e.printStackTrace();
					} catch (IllegalStateException e) {

						e.printStackTrace();
					}
				}
				break;
			case SerialPortControlBroadCast.CMD_COLORTEMP:
				if (null != ControlActivity.getInstance()) {
					ControlActivity.getInstance().SetColorTempIncrease();
				}
				break;
			case SerialPortControlBroadCast.CMD_SATURATION:
				if (null != ControlActivity.getInstance()) {
					ControlActivity.getInstance().SetSaturationIncrease();

				}
				break;
			case SerialPortControlBroadCast.CMD_CONTRAST:
				if (null != ControlActivity.getInstance()) {
					ControlActivity.getInstance().SetContrastIncrease();
				}
				break;
			case SerialPortControlBroadCast.CMD_TEST:
				 System.out.println("test..."); 
				if (null != TestActivity.getInstance()) {
					TestActivity.getInstance().SetTestMode(1);
				}
			default:
				break;
			}
			break;
		case SerialPortControlBroadCast.CMD_ANTICLOCKWISE:
			switch (SerialPortControlBroadCast.GetCMD_CURRENT()) {
			case SerialPortControlBroadCast.CMD_BRIGHTNESS:
				if (null != ControlActivity.getInstance()) {
					ControlActivity.getInstance().SetBrightReduce();
				}
				break;
			case SerialPortControlBroadCast.CMD_COLORTEMP:
				if (null != ControlActivity.getInstance()) {
					//hh
					ControlActivity.getInstance().SetColorTempReduce();
				}
				break;
			case SerialPortControlBroadCast.CMD_SATURATION:
				if (null != ControlActivity.getInstance()) {
					ControlActivity.getInstance().SetSaturationReduce();
				}
				break;
			case SerialPortControlBroadCast.CMD_CONTRAST:
				if (null != ControlActivity.getInstance()) {
					ControlActivity.getInstance().SetContrastReduce();

				}
				break;

			default:
				break;
			}

			break;
		default:
			if (SerialPortControlBroadCast.CMD_CLOCK_VALUE == (cmd & 0xF0)) {

			}
			break;

		}
		return 1;
	}
}