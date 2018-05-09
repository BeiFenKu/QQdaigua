package com.king.util;

import java.io.IOException;

/**
 * Created by KingLee on 2018/4/11.
 */

/***
 * 网络检测工具
 *
 * 连同移动数据下 ret = 0;
 * 在需要网页认证的wifi下 ret = 1;
 * 在wifi打开但没有网络连接，数据也不可用的状态下 ret = 2;
 * 在不可用wifi下 ret = 2;
 * 在可用wifi下 ret = 0;
 *
 */
public class InternetCheck {

    public static boolean Check() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("ping -c 3 www.dkingdg.com");
            int ret = process.waitFor();
//            Log.e("Avalible", "Process:"+ret);
            if (ret == 0) {
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
