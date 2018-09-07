/**
 * @Project Name:WeChat
 * @File Name:SharedPreferencesUtil.java
 * @Package Name:com.wuxainedu.util
 * @Date:2016年7月6日下午2:38:41
 * @Copyright(c)2016 www.wuxianedu.com Inc. All rights reserved.
*/

package com.dfwr.zhuanke.zhuanke.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import com.dfwr.zhuanke.zhuanke.application.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;


public class SharedPreferencesUtil {
	
	//文件存储的位置
	private static final String FILE_NAME="GdAPP";

	public static final String student_link="student_link";

	public static final String MESSAGE_ALREADY_LOOKED="MESSAGE_ALREADY_LOOKED";




	/**
	 * 写入String型的数据到。data/data/包名/share_prefs下。map类型存储
	 * @param context 
	 * @param key   String 
	 * @param value  String 
	 */
	public static void putStringData(Context context, String key,String value){
		SharedPreferences pre=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		Editor editor=pre.edit();
		editor.putString(key,value);
		editor.commit();
	}
	
	/**
	 * 写入boolean型的数据到。data/data/包名/share_prefs下。map类型存储
	 * @param context 
	 * @param key   String 
	 */
	public static void putBooleanData(Context context, String key,boolean value){
		SharedPreferences pre=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		Editor editor=pre.edit();
		editor.putBoolean(key,value);
		editor.commit();
	}

	/**
	 * 读取String型数据。
	 * @param context
	 * @param key  String
	 */
	public static String getStringData(Context context, String key){

		SharedPreferences pre=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return pre.getString(key, "");
	}
	
	/**
	 * 读取boolean型数据。
	 * @param context 
	 * @param key  String
	 */
	public static boolean getBooleanData(Context context,String key){
		SharedPreferences pre=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return pre.getBoolean(key,true);
	}
	
	/**
	 * 得到全部数据。return Map<String,?>
	 * Map<String,?> map=new HashMap();
	 */
	public static Map getAllData(Context context){
		SharedPreferences pre=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return pre.getAll();
	}
	
	/**
	 * 判断是否存在Key
	 */
	public static boolean isExist(Context context,String key){
		SharedPreferences pre=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		return  pre.contains(key);
	}
	
	/**
	 * 移除对应key值得数据
	 * @param context
	 * @param key  String
	 */
	public static void removeData(Context context,String key){
		SharedPreferences pre=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		Editor editor=pre.edit();
		editor.remove(key);
		editor.commit();
	}
	
	/**
	 * 移除全部数据。
	 */
	public static void removeAllData(Context context){
		SharedPreferences pre=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
		Editor editor=pre.edit();
		editor.clear();
		editor.commit();
	}


	/**
	 * writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
	 * 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
	 *
	 * @param object 待加密的转换为String的对象
	 * @return String   加密后的String
	 */
	private static String Object2String(Object object) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(object);
			String string = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
			objectOutputStream.close();
			return string;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 使用Base64解密String，返回Object对象
	 *
	 * @param objectString 待解密的String
	 * @return object      解密后的object
	 */
	private static Object String2Object(String objectString) {
		byte[] mobileBytes = Base64.decode(objectString.getBytes(), Base64.DEFAULT);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
		ObjectInputStream objectInputStream = null;
		try {
			objectInputStream = new ObjectInputStream(byteArrayInputStream);
			Object object = objectInputStream.readObject();
			objectInputStream.close();
			return object;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 使用SharedPreference保存对象
	 * @param key        储存对象的key
	 * @param saveObject 储存的对象
	 */
	public static void save(String key, Object saveObject) {
		SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		String string = Object2String(saveObject);
		editor.putString(key, string);
		editor.commit();
	}

	/**
	 * 获取SharedPreference保存的对象

	 * @param key     储存对象的key
	 * @return object 返回根据key得到的对象
	 */
	public static Object get(String key) {
		SharedPreferences sharedPreferences =  MyApplication.getContext().getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
		String string = sharedPreferences.getString(key, null);
		if (string != null) {
			Object object = String2Object(string);
			return object;
		} else {
			return null;
		}
	}



}

