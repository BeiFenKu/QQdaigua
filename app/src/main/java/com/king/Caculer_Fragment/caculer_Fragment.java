package com.king.Caculer_Fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.king.qqdaigua.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.king.qqdaigua.R.id.web_view;

/**
 * Created by KingLee on 2018/5/8.
 */

public class caculer_Fragment extends Fragment {

    private String level_qq = "576777915";
    private String level_url = "https://vip.qq.com/pk/index?param=";

    private View view;
    private WebView webView;
    private CookieManager cookieManager;
    private String cookie;
    protected boolean useThemestatusBarColor = true;//是否使用特殊的标题栏背景颜色，android5
    // .0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useStatusBarColor = false;//是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6
    private EditText editText;
    private Button button;
    private RelativeLayout relativeLayout;
    // .0以上可以设置


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_caculer, container, false);
        setTitle();
        bindViews();

        return view;
    }

    private void bindViews() {

        webView = (WebView) view.findViewById(web_view);
        CookieSyncManager.createInstance(getContext());
        cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
        relativeLayout = (RelativeLayout) view.findViewById(R.id.input_qq_rel);
        editText = (EditText) view.findViewById(R.id.editText);
        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() < 5) {
                    Toast.makeText(getContext(), "QQ号输入不规范", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    level_qq = editText.getText().toString();
                    relativeLayout.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                }
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 8.0; MI 6 Build/OPR1.170623.027; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/6.2 TBS/044006 Mobile Safari/537.36 V1_AND_SQ_7.5.5_806_YYB_D QQ/7.5.5.3460 NetType/4G WebP/0.3.0 Pixel/1080");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    if (webView.getUrl().equals("https://h5.qzone.qq.com/mqzone/index") ||
                            webView.getUrl().equals("https://h5.qzone.qq.com/mqzone/profile")) {
                        cookieManager = CookieManager.getInstance();
                        cookie = cookieManager.getCookie("https://h5.qzone.qq.com/mqzone/index");
                        Toast.makeText(getContext(),
                                "登陆成功，如未查询成功，请重新登录查询",
                                Toast
                                        .LENGTH_LONG).show();
                        webView.loadUrl(level_url + level_qq);
                    }
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, final String paramAnonymousString) {
//                Log.e("拿到地址::::", "" + paramAnonymousString);
                if (paramAnonymousString.indexOf("vip.qq.com/pk/index?param") != -1) {
                    cookieManager = CookieManager.getInstance();
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                int sign = 0;
                                String text = "";
                                URL url = new URL(paramAnonymousString);
                                HttpURLConnection urlConnection = (HttpURLConnection) url
                                        .openConnection();
                                urlConnection.setConnectTimeout(5000);
                                urlConnection.setReadTimeout(5000);
                                urlConnection.setRequestProperty("cookie", cookie);
                                int responsecode = urlConnection.getResponseCode();
                                if (responsecode == 200) {
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));

                                    String str = "";
                                    while ((str = reader.readLine()) != null) {
                                        str = reader.readLine();
                                        if (str.indexOf("//imgcache.gtimg.cn/club/platform/lib/seajs/sea-with-plugin-p-v2-2.2.1.js?_bid=250&max_age=2592000&v=4") != -1) {
                                            sign = 1;
                                        } else if (str.indexOf("seajs.config({") != -1) {
                                            sign = 0;
                                        }
                                        if (sign == 1) {
                                            text += "\n";
                                            text += str;
                                        }
//                                        Log.e(paramAnonymousString + "拿到" + sign + "源码：", str);
//                                        resource[sign++] = str;
                                    }
                                    System.out.println("输出：" + text);
                                } else {
                                    Log.e("获取不到网页的源码，服务器响应代码为：", "" + responsecode);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                super.onLoadResource(view, paramAnonymousString);
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //此方法为检测是否在加载【弹起本地浏览器JS】的方法，以免QQ空间被本地浏览器打开
                try {
                    boolean bool = url.startsWith("jsbridge://");
                    if (bool) {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }
                webView.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl("https://qzone.qq.com/");
    }

    public void setTitle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getActivity().getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //根据上面设置是否对状态栏单独设置颜色
            if (useThemestatusBarColor) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.blue_title));
            } else {
                getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getActivity().getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !useStatusBarColor) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}