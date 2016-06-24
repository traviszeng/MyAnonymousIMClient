package com.example.im.activity;

import java.io.IOException;

import com.example.im.R;
import com.example.im.core.QQConnection;
import com.example.im.core.QQConnection.OnMessageListener;
import com.example.im.domain.QQMessage;
import com.example.im.domain.QQMessageType;
import com.example.im.util.MD5;
import com.example.im.util.ThreadUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class LoginActivity extends BaseActivity {

	@ViewInject(R.id.account)
	private EditText account;
	@ViewInject(R.id.password)
	private EditText password;
	private String accountStr;// �˺�
	private String passwordStr;// ����
	QQConnection conn;
	private Spinner spinner;

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
		/**spinner= (Spinner) findViewById(R.id.theme_choose);
		String[] mItems = getResources().getStringArray(R.array.themechoice);
		ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,mItems);
		spinner.setAdapter(_Adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		
		});*/
		
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
		MD5 md5 = new MD5();
		accountStr = account.getText().toString().trim();
		passwordStr = password.getText().toString().trim();
		
		//����MD5��������
		
		passwordStr= md5.getMD5(passwordStr);
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
					msg.newAddAccount=Long.parseLong(accountStr);
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
						 * <existAnother>false</existAnother>
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

					}
					/*else if(QQMessageType.MSG_TYPE_BUDDY_LIST.equals(msg.type)&&(msg.existAnother==true)){
						//����˺ű����˵�½��ǿ������
						//����ǿ�����߹㲥
						Intent intent = new Intent("com.example.im.FORCE_OFFLINE");
						sendBroadcast(intent);
						
					} */
					else {
						Toast.makeText(getBaseContext(), "��¼ʧ��", 0).show();
					}
				}
			});

		}
	};

}
