package com.king.util;

/**
 * Created by KingLee on 2018/4/11.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/*
 * 基本功能：存储和访问SharedPreferences，判断是否第一次安装打开。
 * 创建：Jason
 */
public class OperatingSharedPreferences {
    /**
     * <pre>
     * 基本功能：保存启动标识到SharedPreferences
     * 编写：Jason
     * @param context
     * </pre>
     */
    public static void setSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "whtopentimes", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();// 获取编辑器
        editor.putBoolean("firstopen", false);
        editor.commit();// 提交修改
    }

    /**
     * <pre>
     * 基本功能：取得SharedPreferences中存储的启动标识
     * 编写：Jason
     * @param context
     * @return
     * </pre>
     */
    public static boolean getSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "whtopentimes", Context.MODE_PRIVATE);
        // getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值true
        boolean firstopen = sharedPreferences.getBoolean("firstopen", true);
        return firstopen;
    }
}