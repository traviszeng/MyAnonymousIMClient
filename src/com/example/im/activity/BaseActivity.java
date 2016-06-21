package com.example.im.activity;

import com.example.im.broadcast.ActivityCollector;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {
	
	/*
	 * 作为所有活动的父类
	 * 创建活动的时候向活动管理器加入活动，
	 * 销毁的时候将活动移除
	 * */

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
	}
	
	protected void onDestroy(){
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
	
}
