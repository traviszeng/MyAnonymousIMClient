package com.example.im.domain;

import com.example.im.util.MyTime;

/**
 * ����Э�鶨�弴ʱͨѶ��ʵ���࣬�����������ݽ���
 * 
 * @author ZHY
 * 
 */
public class QQMessage extends ProtocalObj {
	public String type = QQMessageType.MSG_TYPE_CHAT_P2P;// ���͵����� chat login
	public long from = 0;// ������ account
	public String fromNick = "";// �ǳ�
	public int fromAvatar = 1;// ͷ��
	public long to = 0; // ������ account
	public String content = ""; // ��Ϣ������ Լ��?
	public String sendTime = MyTime.geTime(); // ����ʱ��
	public boolean existAnother = false; //�Ƿ������һ��ʹ��ͬһ�˺ŵ�½����
	public long newAddAccount=0L;   //�����ߵ��û����˺�
}
