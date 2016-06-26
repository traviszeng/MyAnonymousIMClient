package com.example.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.im.R;
import com.example.im.security.RandomCode;

public class RandomCodeTEST extends BaseActivity {

	private EditText randomcode_input;
	private RandomCode randomcode;
	private ImageView randomcode_pic;
	private Button button;
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_randomtest);
	button = (Button) findViewById(R.id.renzheng);
	randomcode_input = (EditText) findViewById(R.id.RandomCode_input);
	randomcode_pic = (ImageView) findViewById(R.id.RandomCode_pic);
	//������֤��
	randomcode_pic.setImageBitmap(randomcode.getInstance().createBitmap());
	//���ͼƬ�������µ���֤��
	randomcode_pic.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			randomcode_pic.setImageBitmap(randomcode.getInstance().createBitmap());
		}
	});
	
	Toast.makeText(getBaseContext(), randomcode.getInstance().getCode(), Toast.LENGTH_SHORT).show();
	button.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//��֤����֤����
			
			if(!randomcode.getInstance().getCode().equals(randomcode_input.getText().toString())){
				Toast.makeText(getBaseContext(), "Oops����֤���������������", Toast.LENGTH_SHORT).show();
				randomcode_pic.setImageBitmap(randomcode.getInstance().createBitmap());
			}
			else{
				Intent intent = new Intent(getBaseContext(),MainActivity.class);
				startActivity(intent);
			}
		}
	});
	}
}
