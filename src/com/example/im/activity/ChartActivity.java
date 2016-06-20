package com.example.im.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.im.R;
import com.example.im.adapter.ChartMessageAdapter;
import com.example.im.core.QQConnection.OnMessageListener;
import com.example.im.domain.QQMessage;
import com.example.im.domain.QQMessageType;
import com.example.im.util.ThreadUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/*
 * ************************************************************************
 * 
 * @创建者 ZHY
 * 
 * @创建时间 2016-2-26 下午10:18:37
 * 
 * @描述 博客地址：http://blog.csdn.net/qq_20889581?viewmode=contents
 * 
 * @版权所有 文明的小流氓
 * 
 * ************************************************************************
 */
public class ChartActivity extends Activity {

	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.listview)
	private ListView listView;
	@ViewInject(R.id.input)
	private EditText input;
	private ImApp app;
	private ChartMessageAdapter adapter;
	// 这是点击的用户，也就是你要发消息给谁
	private String toNick;// 要发送给谁
	private long toAccount;// y要发送的账号
	private long fromAccount;// 我的账号，我要跟谁睡聊天
	private String inputStr;// 聊天内容
	// 聊天消息的集合
	private List<QQMessage> messages = new ArrayList<QQMessage>();

	// 点击发送按钮的时候，获得输入框中的内容，将内容封装到messages集合中，聊调消息是一个QQMessageJava对象,一定包含四个字段
	@OnClick(R.id.send)
	public void send(View view) {
		// Toast.makeText(getBaseContext(), "ddadasf", 0).show();
		inputStr = input.getText().toString().trim();
		// 清空输入框
		input.setText("");
		final QQMessage msg = new QQMessage();
		if ("".equals(inputStr)) {
			Toast.makeText(getApplicationContext(), "不能为空", 0).show();
			return;
		}

		msg.type = QQMessageType.MSG_TYPE_CHAT_P2P;
		msg.from = fromAccount;
		msg.to = toAccount;
		msg.content = inputStr;
		msg.fromAvatar = R.drawable.ic_launcher;

		messages.add(msg);
		// 数据集合有了，创建适配器
		// 刷新消息
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

		// 展示到最新发送的消息处
		if (messages.size() > 0) {
			listView.setSelection(messages.size() - 1);
		}

		// 发送消息到服务器--子线程
		ThreadUtils.runInSubThread(new Runnable() {

			public void run() {
				try {
					app.getMyConn().sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 接收消息，使用监听器
	 */
	private OnMessageListener listener = new OnMessageListener() {

		public void onReveive(final QQMessage msg) {
			// 注意onReveive是子线程，更新界面一定要在主线程中
			ThreadUtils.runInUiThread(new Runnable() {

				public void run() {

					// 服务器返回回来的消息
					System.out.println(msg.content);
					if (QQMessageType.MSG_TYPE_CHAT_P2P.equals(msg.type)) {
						messages.add(msg);// 把消息加到消息集合中，这是最新的消息
						// 刷新消息
						if (adapter != null) {
							adapter.notifyDataSetChanged();
						}
						// 展示到最新发送的消息出
						if (messages.size() > 0) {
							listView.setSelection(messages.size() - 1);
						}

					}

				}
			});

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);
		ViewUtils.inject(this);// 相当于findViewByID
		app = (ImApp) getApplication();
		// 开启监听
		app.getMyConn().addOnMessageListener(listener);
		// 聊天的界面是复杂的listView。发送消息的条目是item_chat_send.xml布局，接收到的消息现实的条目是item_chat_receive.xml布局
		/**
		 * 聊天的消息 content "约不？" type chatp2p from 老王 to 大头
		 */
		// 获得从上一个界面获取的账号与昵称
		Intent intent = getIntent();
		toNick = intent.getStringExtra("nick");
		toAccount = intent.getLongExtra("account", 0);

		title.setText("与" + toNick + "聊天中");
		fromAccount = app.getMyAccount();// 我的账户

		adapter = new ChartMessageAdapter(this, messages);
		listView.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		app.getMyConn().removeOnMessageListener(listener);
	}
}
