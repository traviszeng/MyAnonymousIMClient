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
	private String accountStr;// 账号
	private String passwordStr;// 密码
	QQConnection conn;
	private Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);// 使注解生效
		/*
		 * 登录的逻辑:首先连接服务器，获取用户输入的账号和密码，讲账户和密码发送给服务器;服务器做一些验证操作，将验证结果返回给客户端，
		 * 客户端再进行接收消息的操作
		 */
		// 与网络相关的操作要放在子线程中进行
		ThreadUtils.runInSubThread(new Runnable() {

			public void run() {
				try {
					conn = new QQConnection("192.168.220.1", 8090);// Socket
					conn.connect();// 建立连接
					// 建立连接之后，将监听器添加到连接里面
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
		// activity销毁的时候取消监听
		conn.removeOnMessageListener(listener);
	}

	/**
	 * 按钮的点击事件
	 * 
	 * @param view
	 */
	@OnClick(R.id.login)
	public void login(View view) {
		// Toast.makeText(getBaseContext(), "haha", 0).show();
		MD5 md5 = new MD5();
		accountStr = account.getText().toString().trim();
		passwordStr = password.getText().toString().trim();
		
		//利用MD5加密密码
		
		passwordStr= md5.getMD5(passwordStr);
		// 获得输入的账号和密码，发送
		/*
		 * 登录的协议 <QQMessage> <type>login</type> <sender>0</sender>
		 * <senderNick></senderNick> <senderAvatar>1</senderAvatar>
		 * <receiver>0</receiver> <content>1001#123</content> <sendTime>05-09
		 * 13:01:46</sendTime> </QQMessage>
		 */
		/*
		 * 用户登录的时候获取账号和密码，封装成Java对象，字段包括类型和内容，内容是用户名和密码之间的拼接
		 */

		// 封装好Java对象，将数据写到服务器,在子线程中运行
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
	 * 登录成功之后返回好友列表 <QQMessage> <type>buddylist</type> <from>0</from>
	 * <fromNick></fromNick> <fromAvatar>1</fromAvatar> <to>0</to>
	 * <content>{&quot
	 * ;buddyList&quot;:[{&quot;account&quot;:101,&quot;nick&quot;:&quot;QQ
	 * 1&quot;,&quot;avatar&quot;:0}]}</content> <sendTime>05-21
	 * 21:51:39</sendTime> </QQMessage>
	 */
	// 客户端这里要完成接收的逻辑
	// 使用接收消息的监听器
	private OnMessageListener listener = new OnMessageListener() {

		public void onReveive(final QQMessage msg) {
			System.out.println(msg.toXML());
			// 接收服务器返回的结果.处理数据的显示,运行在主线程中
			ThreadUtils.runInUiThread(new Runnable() {

				public void run() {
					if (QQMessageType.MSG_TYPE_BUDDY_LIST.equals(msg.type)) {
						// 登录成功，返回的数据是好友列表
						/**
						 * <QQMessage>
						 * <content>{&quot;buddyList&quot;:[{&quot;account
						 * &quot;:101,&quot;nick&quot;:&quot;金莉
						 * 1&quot;,&quot;avatar&quot;:0}]}</content>
						 * <type>buddylist</type> <sendTime>02-26
						 * 15:18:19</sendTime> <fromNick></fromNick>
						 * <from>0</from> <to>0</to> <fromAvatar>1</fromAvatar>
						 * <existAnother>false</existAnother>
						 * </QQMessage>
						 */
						// 有用的信息是content的json串
						Toast.makeText(getBaseContext(), "登录成功 ！", 0).show();
						// 将连接conn保存到application中，作为一个长连接存在，这样在其他的activity，server中都能拿到这个连接，保证了项目连接的唯一性
						// 新建一个application类，给出get，set方法，使用application可以在整个项目中共享数据
						ImApp app = (ImApp) getApplication();
						// 保存一个长连接
						app.setMyConn(conn);
						// 保存好友列表的json串
						app.setBuddyListJson(msg.content);
						// 保存当前登录用户的账号
						app.setMyAccount(Long.parseLong(accountStr));
						// 打开主页
						Intent intent = new Intent();
						intent.setClass(getBaseContext(), MainActivity.class);
						intent.putExtra("account", accountStr);
						startActivity(intent);

						finish();

					}
					/*else if(QQMessageType.MSG_TYPE_BUDDY_LIST.equals(msg.type)&&(msg.existAnother==true)){
						//如果账号被别人登陆则强制下线
						//发送强制下线广播
						Intent intent = new Intent("com.example.im.FORCE_OFFLINE");
						sendBroadcast(intent);
						
					} */
					else {
						Toast.makeText(getBaseContext(), "登录失败", 0).show();
					}
				}
			});

		}
	};

}
