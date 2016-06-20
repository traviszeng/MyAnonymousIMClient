package com.example.im.activity;

import com.example.im.R;
import com.example.im.util.ThreadUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

/*
 * ************************************************************************
 * 
 * @������ ZHY
 * 
 * @����ʱ�� 2016-2-26 ����10:16:28
 * 
 * @���� ���͵�ַ��http://blog.csdn.net/qq_20889581/article/details/50755449
 * 
 * @��Ȩ���� ������С��å
 * 
 * ************************************************************************
 */
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		/**
		 * �����߳�����һЩ������֮����ת����¼����
		 */
		ThreadUtils.runInSubThread(new Runnable() {

			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivity(new Intent(getApplicationContext(),
						LoginActivity.class));

			}
		});

	}

}
