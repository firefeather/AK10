/*
   * 文件名 CustomToast.java
   * 包含类名列表com.szaoto.ak10.custom
   * 版本信息，版本号
   * 创建日期2014年1月22日上午8:37:12
   * 版权声明 liangdb-szaoto
*/
package com.szaoto.ak10.custom;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

/*
 * 类名CustomToast
 * 作者 liangdb
 * 主要功能 每次创建Toast时先做一下判断，如果前面有Toast在显示，只需调用Toast中的setText（）方法将要显示的信息替换即可
 * 创建日期2014年1月22日
 * 修改者，修改日期，修改内容
 */
public class CustomToast {

	 private static Toast mToast;
     private static Handler mHandler = new Handler();
     private static Runnable r = new Runnable() {
         public void run() {
             mToast.cancel();
         }
     };

     public static void showToast(Context mContext, String text, int duration) {
        
         mHandler.removeCallbacks(r);
         if (mToast != null)
         {
             mToast.setText(text);
	         View tView1  = mToast.getView();
	         tView1.setBackgroundColor(0);
	         mToast.setView(tView1);
	         tView1.invalidate();
         }
         else
         {
             mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
             View tView  = mToast.getView();
             tView.setBackgroundColor(0);
             mToast.setView(tView);
             tView.invalidate();
         }
         mHandler.postDelayed(r, duration);
         mToast.show();
     }

     public static void showToast(Context mContext, int resId, int duration) {
         showToast(mContext, mContext.getResources().getString(resId), duration);
     }


}
