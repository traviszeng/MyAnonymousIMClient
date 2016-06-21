package com.example.im.activity;

import java.io.IOException;

import com.example.im.R;
import com.example.im.core.QQConnection;
import com.example.im.core.QQConnection.OnMessageListener;
import com.example.im.domain.QQMessage;
import com.example.im.domain.QQMessageType;
import com.example.im.util.ThreadUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/*
 * ************************************************************************
 * 
 * @������ ZHY
 * 
 * @����ʱ�� 2016-2-26 ����10:18:05
 * 
 * @���� ���͵�ַ��http://blog.csdn.net/qq_20889581/article/details/50755449
 * 
 * @��Ȩ���� ������С��å
 * 
 * ************************************************************************
 */
public class LoginActivity extends Activity {

	@ViewInject(R.id.account)
	private EditText account;
	@ViewInject(R.id.password)
	private EditText password;
	private String accountStr;// �˺�
	private String passwordStr;// ����
	QQConnection conn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);// ʹע����Ч
		/*
		 * ��¼���߼�:�������ӷ���������ȡ�û�������˺ź����룬���˻������뷢�͸�������;��������һЩ��֤����������֤������ظ��ͻ��ˣ�
		 * �ͻ����ٽ��н�����Ϣ�Ĳ���
		 */
		// ��������صĲ���Ҫ�������߳��н���
		ThreadUtils.runInSubThread(new Runnable() {

			public void run() {
				try {
					conn = new QQConnection("192.168.220.1", 8090);// Socket
					conn.connect();// ��������
					// ��������֮�󣬽���������ӵ���������
					conn.addOnMessageListener(listener);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// activity���ٵ�ʱ��ȡ������
		conn.removeOnMessageListener(listener);
	}

	/**
	 * ��ť�ĵ���¼�
	 * 
	 * @param view
	 */
	@OnClick(R.id.login)
	public void login(View view) {
		// Toast.makeText(getBaseContext(), "haha", 0).show();
		accountStr = account.getText().toString().trim();
		passwordStr = password.getText().toString().trim();
		// ���������˺ź����룬����
		/*
		 * ��¼��Э�� <QQMessage> <type>login</type> <sender>0</sender>
		 * <senderNick></senderNick> <senderAvatar>1</senderAvatar>
		 * <receiver>0</receiver> <content>1001#123</content> <sendTime>05-09
		 * 13:01:46</sendTime> </QQMessage>
		 */
		/*
		 * �û���¼��ʱ���ȡ�˺ź����룬��װ��Java�����ֶΰ������ͺ����ݣ��������û���������֮���ƴ��
		 */

		// ��װ��Java���󣬽�����д��������,�����߳�������
		ThreadUtils.runInSubThread(new Runnable() {

			public void run() {
				try {
					QQMessage msg = new QQMessage();
					msg.type = QQMessageType.MSG_TYPE_LOGIN;
					msg.content = accountStr + "#" + passwordStr;
					conn.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * ��¼�ɹ�֮�󷵻غ����б� <QQMessage> <type>buddylist</type> <from>0</from>
	 * <fromNick></fromNick> <fromAvatar>1</fromAvatar> <to>0</to>
	 * <content>{&quot
	 * ;buddyList&quot;:[{&quot;account&quot;:101,&quot;nick&quot;:&quot;QQ
	 * 1&quot;,&quot;avatar&quot;:0}]}</content> <sendTime>05-21
	 * 21:51:39</sendTime> </QQMessage>
	 */
	// �ͻ�������Ҫ��ɽ��յ��߼�
	// ʹ�ý�����Ϣ�ļ�����
	private OnMessageListener listener = new OnMessageListener() {

		public void onReveive(final QQMessage msg) {
			System.out.println(msg.toXML());
			// ���շ��������صĽ��.�������ݵ���ʾ,���������߳���
			ThreadUtils.runInUiThread(new Runnable() {

				public void run() {
					if (QQMessageType.MSG_TYPE_BUDDY_LIST.equals(msg.type)) {
						// ��¼�ɹ������ص������Ǻ����б�
						/**
						 * <QQMessage>
						 * <content>{&quot;buddyList&quot;:[{&quot;account
						 * &quot;:101,&quot;nick&quot;:&quot;����
						 * 1&quot;,&quot;avatar&quot;:0}]}</content>
						 * <type>buddylist</type> <sendTime>02-26
						 * 15:18:19</sendTime> <fromNick></fromNick>
						 * <from>0</from> <to>0</to> <fromAvatar>1</fromAvatar>
						 * </QQMessage>
						 */
						// ���õ���Ϣ��content��json��
						Toast.makeText(getBaseContext(), "��¼�ɹ� ��", 0).show();
						// ������conn���浽application�У���Ϊһ�������Ӵ��ڣ�������������activity��server�ж����õ�������ӣ���֤����Ŀ���ӵ�Ψһ��
						// �½�һ��application�࣬����get��set������ʹ��application������������Ŀ�й�������
						ImApp app = (ImApp) getApplication();
						// ����һ��������
						app.setMyConn(conn);
						// ��������б��json��
						app.setBuddyListJson(msg.content);
						// ���浱ǰ��¼�û����˺�
						app.setMyAccount(Long.parseLong(accountStr));
						// ����ҳ
						Intent intent = new Intent();
						intent.setClass(getBaseContext(), MainActivity.class);
						intent.putExtra("account", accountStr);
						startActivity(intent);

						finish();

					} else {
						Toast.makeText(getBaseContext(), "��¼ʧ��", 0).show();
					}
				}
			});

		}
	};

}
