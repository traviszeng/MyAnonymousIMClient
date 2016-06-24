package com.example.im.security;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;

public class LogWrap {
	
	//���Լ���д��LogWrap���������׼��android.util.Log
	//���Բ���android��־

	public static final String TAG="MyAppTag";
	public static void e(final Object obj,final Throwable cause){
		Log.e(TAG, String.valueOf(obj));
		Log.e(TAG, convertThrowableStackToString(cause));
	}
	
	public static void e(final Object obj){
		Log.e(TAG,String.valueOf(obj));
	}
	
	public static void w(final Object obj,final Throwable cause){
		Log.w(TAG, String.valueOf(obj));
		Log.w(TAG, convertThrowableStackToString(cause));
	}
	
	public static void w(final Object obj){
		Log.w(TAG,String.valueOf(obj));
	}
	
	public static void i(final Object obj){
		Log.i(TAG,String.valueOf(obj));
	}
	
	public static void d(final Object obj){
		Log.d(TAG,String.valueOf(obj));
	}
	
	public static void v(final Object obj){
		Log.v(TAG,String.valueOf(obj));
	}
	
	private static String convertThrowableStackToString(final Throwable cause) {
		// TODO Auto-generated method stub
		StringWriter b = new StringWriter();
		cause.printStackTrace(new PrintWriter(b));
		return b.toString();
	}
}
