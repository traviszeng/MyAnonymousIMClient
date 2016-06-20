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
 * @������ ZHY
 * 
 * @����ʱ�� 2016-2-26 ����10:18:37
 * 
 * @���� ���͵�ַ��http://blog.csdn.net/qq_20889581?viewmode=contents
 * 
 * @��Ȩ���� ������С��å
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
	// ���ǵ�����û���Ҳ������Ҫ����Ϣ��˭
	private String toNick;// Ҫ���͸�˭
	private long toAccount;// yҪ���͵��˺�
	private long fromAccount;// �ҵ��˺ţ���Ҫ��˭˯����
	private String inputStr;// ��������
	// ������Ϣ�ļ���
	private List<QQMessage> messages = new ArrayList<QQMessage>();

	// ������Ͱ�ť��ʱ�򣬻��������е����ݣ������ݷ�װ��messages�����У��ĵ���Ϣ��һ��QQMessageJava����,һ�������ĸ��ֶ�
	@OnClick(R.id.send)
	public void send(View view) {
		// Toast.makeText(getBaseContext(), "ddadasf", 0).show();
		inputStr = input.getText().toString().trim();
		// ��������
		input.setText("");
		final QQMessage msg = new QQMessage();
		if ("".equals(inputStr)) {
			Toast.makeText(getApplicationContext(), "����Ϊ��", 0).show();
			return;
		}

		msg.type = QQMessageType.MSG_TYPE_CHAT_P2P;
		msg.from = fromAccount;
		msg.to = toAccount;
		msg.content = inputStr;
		msg.fromAvatar = R.drawable.ic_launcher;

		messages.add(msg);
		// ���ݼ������ˣ�����������
		// ˢ����Ϣ
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

		// չʾ�����·��͵���Ϣ��
		if (messages.size() > 0) {
			listView.setSelection(messages.size() - 1);
		}

		// ������Ϣ��������--���߳�
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
	 * ������Ϣ��ʹ�ü�����
	 */
	private OnMessageListener listener = new OnMessageListener() {

		public void onReveive(final QQMessage msg) {
			// ע��onReveive�����̣߳����½���һ��Ҫ�����߳���
			ThreadUtils.runInUiThread(new Runnable() {

				public void run() {

					// ���������ػ�������Ϣ
					System.out.println(msg.content);
					if (QQMessageType.MSG_TYPE_CHAT_P2P.equals(msg.type)) {
						messages.add(msg);// ����Ϣ�ӵ���Ϣ�����У��������µ���Ϣ
						// ˢ����Ϣ
						if (adapter != null) {
							adapter.notifyDataSetChanged();
						}
						// չʾ�����·��͵���Ϣ��
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
		ViewUtils.inject(this);// �൱��findViewByID
		app = (ImApp) getApplication();
		// ��������
		app.getMyConn().addOnMessageListener(listener);
		// ����Ľ����Ǹ��ӵ�listView��������Ϣ����Ŀ��item_chat_send.xml���֣����յ�����Ϣ��ʵ����Ŀ��item_chat_receive.xml����
		/**
		 * �������Ϣ content "Լ����" type chatp2p from ���� to ��ͷ
		 */
		// ��ô���һ�������ȡ���˺����ǳ�
		Intent intent = getIntent();
		toNick = intent.getStringExtra("nick");
		toAccount = intent.getLongExtra("account", 0);

		title.setText("��" + toNick + "������");
		fromAccount = app.getMyAccount();// �ҵ��˻�

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
