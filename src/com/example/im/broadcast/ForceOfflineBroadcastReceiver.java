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
		dialogBuild.setCancelable(false);//���Ի�����Ϊ����ȡ�����û���BackҲ���ܹرնԻ������ʹ��
		dialogBuild.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				ActivityCollector.finishAll();//�������л
				Intent intent = new Intent(context,com.example.im.activity.LoginActivity.class);
			    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    //�ڹ㲥���������������һ��Ҫ��Intent����FLAG_ACTIVITY_NEW_TASK�����־
			    context.startActivity(intent);
			    
			}
		});
		AlertDialog alertDialog = dialogBuild.create();
		//��Ҫ����AlertDialog�����ͣ���֤�ڹ㲥�������п�����������
		
		alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		//���Ի�����������ΪTYPE_SYSTEM_ALERT����Ȼ�����޷��ڹ㲥�������ﵯ��
		alertDialog.show();
	}

}
