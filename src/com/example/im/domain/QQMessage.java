package com.example.im.domain;

import com.example.im.util.MyTime;

/**
 * 根据协议定义即时通讯的实体类，用来进行数据交互
 * 
 * @author ZHY
 * 
 */
public class QQMessage extends ProtocalObj {
	public String type = QQMessageType.MSG_TYPE_CHAT_P2P;// 类型的数据 chat login
	public long from = 0;// 发送者 account
	public String fromNick = "";// 昵称
	public int fromAvatar = 1;// 头像
	public long to = 0; // 接收者 account
	public String content = ""; // 消息的内容 约不?
	public String sendTime = MyTime.geTime(); // 发送时间
	public boolean existAnother = false; //是否存在另一个使用同一账号登陆的人
	public long newAddAccount=0L;   //新上线的用户的账号
}
