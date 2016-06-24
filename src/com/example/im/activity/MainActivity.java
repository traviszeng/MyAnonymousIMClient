package com.example.im.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.im.R;
import com.example.im.R.layout;
import com.example.im.adapter.ContactInfoAdapter;
import com.example.im.core.QQConnection.OnMessageListener;
import com.example.im.domain.ContactInfo;
import com.example.im.domain.ContactInfoList;
import com.example.im.domain.QQMessage;
import com.example.im.domain.QQMessageType;
import com.example.im.util.ThreadUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends BaseActivity {
	/**
	 * listView��ʹ�÷����������listView��������װ�������ݣ�д��������������listView��Ŀ�Ĳ���
	 */
	@ViewInject(R.id.listview)
	ListView listView;
	// listView�ļ���
	private List<ContactInfo> infos = new ArrayList<ContactInfo>();
	private ImApp app;
	private ContactInfoAdapter adapter;
	/**
	 * ��������ˢ�º����б�,�����Ϣ������������������ߵĺ��ѣ���������ݼ��ϣ�֪ͨ�������������ݷ����˱仯���������º����б�
	 */
	private OnMessageListener listener = new OnMessageListener() {

		public void onReveive(final QQMessage msg) {
			/**
			 * ע�⣺onReceive�������߳��н��У�������listView�б�����Ҫ��Ui�߳���
			 */
			ThreadUtils.runInUiThread(new Runnable() {

				public void run() {

					
					//�ж��Ƿ����뵱ǰ�û���ͻ���˺�
					
					/*if((app.getMyAccount()==msg.newAddAccount)){
						Intent intent=new Intent("com.example.im.FORCE_OFFLINE");
						sendBroadcast(intent);
					}*/
					
					// ���з��������غ����б���˵���к�������\
					if (QQMessageType.MSG_TYPE_BUDDY_LIST.equals(msg.type)) {
						// �к�������
						String newBuddyListJson = msg.content;// �����ߺ��ѵ���Ϣjson��
						// ��jsonת�ɼ��ϣ�����һ��infos����
						Gson gson = new Gson();
						ContactInfoList newList = gson.fromJson(
								newBuddyListJson, ContactInfoList.class);// �����ߺ��ѵļ���
						
						infos.clear();// ���һ��ԭ���ĺ��������
						
						infos.addAll(newList.buddyList);// �µĺ����б�
						if (adapter != null) {
							adapter.notifyDataSetChanged();
						}
					}

				}
			});
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		// /**
		// * ������
		// */
		// for (int i = 0; i < 20; i++) {
		// ContactInfo info = new ContactInfo();
		// info.account = new Long(100 + i);
		// info.avatar = 0;
		// info.nick = "hjahahah" + i;
		// infos.add(info);
		// }
		/**
		 * �Ѽ����ݱ��������
		 */
		// ���ݱ�����application��
		app = (ImApp) getApplication();
		// ��ȡ�����ӣ�������������Ӽ���,ʱ�̼�������������������Ϣ,�������Ϣ�����ִ��onReceive
		app.getMyConn().addOnMessageListener(listener);

		// �����б��json��
		String buddyListJson = app.getBuddyListJson();
		System.out.println(buddyListJson);
		Gson gson = new Gson();
		ContactInfoList list = gson.fromJson(buddyListJson,
				ContactInfoList.class);
		infos.addAll(list.buddyList);// buddyList��һ�����ϣ���buddyList������Ķ���ȫ����ӽ�infos
		adapter = new ContactInfoAdapter(getBaseContext(), infos);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ��õ�ǰ�����Ŀ����Ϣ.�����˺ź��ǳ�
				ContactInfo info = infos.get(position);
				// ���ܸ��Լ�����
				if (info.account != app.getMyAccount()) {
					Intent intent = new Intent(getBaseContext(),
							ChartActivity.class);
					// ���˺ź͸���ǩ��������һ��activity
					intent.putExtra("account", info.account);
					intent.putExtra("nick", info.nick);
					// TODO �������
					startActivity(intent);

				} else {
					Toast.makeText(getBaseContext(), "���ܸ��Լ�����", 0).show();
				}

			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		app.getMyConn().removeOnMessageListener(listener);

	}
}
