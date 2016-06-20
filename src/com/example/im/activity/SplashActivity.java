package com.example.im.activity;

import com.example.im.R;
import com.example.im.util.ThreadUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

/*
 * ************************************************************************
 * 
 * @创建者 ZHY
 * 
 * @创建时间 2016-2-26 下午10:16:28
 * 
 * @描述 博客地址：http://blog.csdn.net/qq_20889581/article/details/50755449
 * 
 * @版权所有 文明的小流氓
 * 
 * ************************************************************************
 */
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		/**
		 * 在子线程中做一些操作，之后跳转到登录界面
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
