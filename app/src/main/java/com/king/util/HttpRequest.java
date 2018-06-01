package com.king.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.king.qqdaigua.MainActivity;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 刘样大帅B on 2018/3/28.
 */


/***
 * 网络请求类
 * 需添加okhttp 3.0
 */
public class HttpRequest extends Thread {
    String url;
    String json;
    OkHttpClient client = new OkHttpClient();
    Handler handler;
    int handlerId = 1;
    MediaType mediaType = MediaType.parse("application/json,charset=utf-8");
    private Message message;
    RequestBody body, body1;

    public HttpRequest() {
    }

    public HttpRequest(String url, String json, Handler handler) {
        this.url = url;
        this.json = json;
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public int getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(int handlerId) {
        this.handlerId = handlerId;
    }

    @Override
    public void run() {
        try {
            JSONObject jsonObject1 = new JSONObject(json);
            String type = jsonObject1.getString("type");
            if (type.equals("login")) {
                //登录判断
                body = new FormBody.Builder().build();
            } else if (type.equals("pay")) {
                //开通判断
                String qq = jsonObject1.getString("qq");
                String kami = jsonObject1.getString("kami");
                String pwd = jsonObject1.getString("pwd");
                body = new FormBody.Builder().add("qq", qq).add("cami", kami).add("cpwd", pwd).build();
            } else if (type.equals("board")) {
                //公告获取判断
                body = new FormBody.Builder().build();
            } else if (type.equals("black")) {
                //拉黑状态显示
                body = new FormBody.Builder().build();
            } else if (type.equals("switch")) {
                //代挂开关获取
                body = new FormBody.Builder().build();
            } else if (type.equals("xufei")) {
                //代挂续费
                body = new FormBody.Builder().build();
            } else if (type.equals("rblack")) {
                //代挂解除拉黑
                body = new FormBody.Builder().build();
            } else if (type.equals("gengx")) {
                //代挂更新密码
                String qq = jsonObject1.getString("qq");
                String pwd = jsonObject1.getString("pwd");
                String npwd = jsonObject1.getString("npwd");
                body = new FormBody.Builder().add("qq", qq).add("pwd", pwd).build();
                body1 = new FormBody.Builder().add("qqpwd", npwd).build();
            } else if (type.equals("bg")) {
                //代挂补挂
                body = new FormBody.Builder().build();
            } else if (type.equals("update")) {
                //检测更新
                body = new FormBody.Builder().build();
            } else if (type.equals("gengx_phonetype")){
                //切换安卓/苹果在线状态
                String qq = jsonObject1.getString("qq");
                String pwd = jsonObject1.getString("pwd");
                String status = jsonObject1.getString("status");
                if (status.equals("1")){
                    status = "2";
                } else {
                    status = "1";
                }
                body = new FormBody.Builder().add("qq", qq).add("pwd", pwd).build();
                body1 = new FormBody.Builder().add("type", status).build();
            }
            Log.e("请求URL：",url);
            Request request = new Request.Builder().url(url).post(body).build();
            Response response = client.newCall(request).execute();
            message = new Message();
            if (true) {
                String str = response.body().string().toString();
                if (type.equals("login")) {
                    // 登录判断
                    message.what = 1;
                } else if (type.equals("pay")) {
                    //购买代挂判断
                    message.what = 2;
                    String error_decode = new JSONObject(str).getString("error");
                    JSONObject jsonObject = new JSONObject(str);
                    jsonObject.put("error", error_decode);
                    str = jsonObject.toString();
                } else if (type.equals("board")) {
                    //公告获取判断
                    message.what = 3;
                    String error_decode = new JSONObject(str).getString("error");
                    JSONObject jsonObject = new JSONObject(str);
                    jsonObject.put("error", error_decode);
                    str = jsonObject.toString();
                } else if (type.equals("black")) {
                    //拉黑状态显示
                    message.what = 4;
                } else if (type.equals("switch")) {
                    //代挂开关获取
                    message.what = 5;
                } else if (type.equals("xufei")) {
                    //代挂续费
                    message.what = 6;
                } else if (type.equals("rblack")) {
                    //代挂解除拉黑
                    message.what = 7;
                } else if (type.equals("gengx")) {
                    //代挂更新密码
                    String cookie = response.headers().get("Set-cookie").toString();
                    request = request.newBuilder().url
                            (MainActivity
                                    .web_jiekou1 + "ajax/dg?ajax=true&star=post&do=yewu&info=upqqpwd")
                            .header
                                    ("Cookie", cookie).post(body1)
                            .build();
                    response = client.newCall(request).execute();
                    str = response.body().string().toString();
                    message.what = 8;
                } else if (type.equals("bg")) {
                    //代挂解除拉黑
                    message.what = 9;
                } else if (type.equals("update")) {
                    //检测更新
                    message.what = 10;
                } else if (type.equals("gengx_phonetype")){
                    //切换安卓/苹果在线状态
                    //代挂更新密码
                    String cookie = response.headers().get("Set-cookie").toString();
                    request = request.newBuilder().url
                            (MainActivity
                                    .web_jiekou1 + "ajax/dg?ajax=true&star=post&do=yewu&info=phonetype")
                            .header
                                    ("Cookie", cookie).post(body1)
                            .build();
                    response = client.newCall(request).execute();
                    str = response.body().string().toString();
                    message.what = 11;
                }
                Log.e("服务器相应内容：", str);
                message.obj = str;
                handler.sendMessage(message);
            } else {
                handler.sendEmptyMessage(0);
            }
        } catch (Exception e) {
            handler.sendEmptyMessage(0);
            e.printStackTrace();
        }
    }


    private static String unicodeToCn(String unicode) {
        try {

            /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格*/
            String[] strs = unicode.split("\\\\u");
            String returnStr = "";
            // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
            for (int i = 1; i < strs.length; i++) {
                returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
                return returnStr;
            }
        } catch (Exception e) {

        }
        return "";
    }


}
