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
	 * listView的使用分三步，获得listView容器，封装集合数据，写数据适配器加载listView条目的布局
	 */
	@ViewInject(R.id.listview)
	ListView listView;
	// listView的集合
	private List<ContactInfo> infos = new ArrayList<ContactInfo>();
	private ImApp app;
	private ContactInfoAdapter adapter;
	/**
	 * 好友上线刷新好友列表,添加消息监听器，如果有新上线的好友，则更新数据集合，通知数据适配器数据发生了变化，进而更新好友列表
	 */
	private OnMessageListener listener = new OnMessageListener() {

		public void onReveive(final QQMessage msg) {
			/**
			 * 注意：onReceive是在子线程中进行，而更新listView列表数据要在Ui线程中
			 */
			ThreadUtils.runInUiThread(new Runnable() {

				public void run() {

					
					//判断是否有与当前用户冲突的账号
					
					/*if((app.getMyAccount()==msg.newAddAccount)){
						Intent intent=new Intent("com.example.im.FORCE_OFFLINE");
						sendBroadcast(intent);
					}*/
					
					// 如有服务器返回好友列表，则说明有好友上线\
					if (QQMessageType.MSG_TYPE_BUDDY_LIST.equals(msg.type)) {
						// 有好友上线
						String newBuddyListJson = msg.content;// 新上线好友的信息json串
						// 将json转成集合，更新一下infos集合
						Gson gson = new Gson();
						ContactInfoList newList = gson.fromJson(
								newBuddyListJson, ContactInfoList.class);// 新上线好友的集合
						
						infos.clear();// 清空一下原来的好友类表集合
						
						infos.addAll(newList.buddyList);// 新的好友列表
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
		// * 假数据
		// */
		// for (int i = 0; i < 20; i++) {
		// ContactInfo info = new ContactInfo();
		// info.account = new Long(100 + i);
		// info.avatar = 0;
		// info.nick = "hjahahah" + i;
		// infos.add(info);
		// }
		/**
		 * 把假数据变成真数据
		 */
		// 数据保存在application中
		app = (ImApp) getApplication();
		// 获取长连接，往长连接里添加监听,时刻监听服务器返回来的消息,如果有消息到达，就执行onReceive
		app.getMyConn().addOnMessageListener(listener);

		// 好友列表的json串
		String buddyListJson = app.getBuddyListJson();
		System.out.println(buddyListJson);
		Gson gson = new Gson();
		ContactInfoList list = gson.fromJson(buddyListJson,
				ContactInfoList.class);
		infos.addAll(list.buddyList);// buddyList是一个集合，把buddyList集合里的东西全部添加进infos
		adapter = new ContactInfoAdapter(getBaseContext(), infos);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 获得当前点击条目的信息.包含账号和昵称
				ContactInfo info = infos.get(position);
				// 不能跟自己聊天
				if (info.account != app.getMyAccount()) {
					Intent intent = new Intent(getBaseContext(),
							ChartActivity.class);
					// 将账号和个性签名带到下一个activity
					intent.putExtra("account", info.account);
					intent.putExtra("nick", info.nick);
					// TODO 聊天界面
					startActivity(intent);

				} else {
					Toast.makeText(getBaseContext(), "不能跟自己聊天", 0).show();
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
