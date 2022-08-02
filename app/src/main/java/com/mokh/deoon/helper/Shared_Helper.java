package com.mokh.deoon.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Shared_Helper {

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    public static void putKey(Context context,String key,String value){
        sharedPreferences=context.getSharedPreferences("Cache", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();

    }


    public static String getkey(Context contextGetKey,String key){
        sharedPreferences=contextGetKey.getSharedPreferences("Cache",Context.MODE_PRIVATE);
        return  sharedPreferences.getString(key,"");

    }
}
