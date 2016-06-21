package com.example.im.broadcast;



import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.WindowManager;

public class ForceOfflineBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub

		AlertDialog.Builder dialogBuild= new AlertDialog.Builder(context);
		dialogBuild.setTitle("WARNING");
		dialogBuild.setMessage("You are forced to be offline. Please try to login again!");
		dialogBuild.setCancelable(false);//将对话框设为不可取消，用户按Back也不能关闭对话框继续使用
		dialogBuild.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				ActivityCollector.finishAll();//销毁所有活动
				Intent intent = new Intent(context,com.example.im.activity.LoginActivity.class);
			    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    //在广播接收器里启动活动，一定要在Intent加入FLAG_ACTIVITY_NEW_TASK这个标志
			    context.startActivity(intent);
			    
			}
		});
		AlertDialog alertDialog = dialogBuild.create();
		//需要设置AlertDialog的类型，保证在广播接收器中可以正常弹出
		
		alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		//将对话框类型设置为TYPE_SYSTEM_ALERT，不然它将无法在广播接收器里弹出
		alertDialog.show();
	}

}
