package com.example.im.activity;

import com.example.im.core.QQConnection;

import android.app.Application;

/*
 * ************************************************************************
 * 
 * @������ ZHY
 * 
 * @����ʱ�� 2016-2-26 ����10:18:46
 * 
 * @���� ���͵�ַ��http://blog.csdn.net/qq_20889581/article/details/50755449
 * 
 * @��Ȩ���� ������С��å
 * 
 * ************************************************************************
 */
public class ImApp extends Application {
	private QQConnection myConn;// ������
	private long myAccount;// �û��ĵ�¼�˺�
	private String buddyListJson;// �����б��json��

	public QQConnection getMyConn() {
		return myConn;
	}

	public void setMyConn(QQConnection myConn) {
		this.myConn = myConn;
	}

	public long getMyAccount() {
		return myAccount;
	}

	public void setMyAccount(long myAccount) {
		this.myAccount = myAccount;
	}

	public String getBuddyListJson() {
		return buddyListJson;
	}

	public void setBuddyListJson(String buddyListJson) {
		this.buddyListJson = buddyListJson;
	}

}
