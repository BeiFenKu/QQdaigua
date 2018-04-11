package com.king.util;

/**
 * Created by KingLee on 2018/4/11.
 */
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ImageView;

/**
 * Created by KingLee on 2017/12/7.
 */



/**
 * ImageView设置url图片方法
 imageView (要修改的图片)
 imgURL (url直链)
 */
public class SetImageViewUtil {
    public static void setImageToImageView(final ImageView imageView,final String imgURL){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                Log.e("类SetImageViewUtil下","设置图片成功");
                super.handleMessage(msg);
                Bitmap bitmap = (Bitmap)msg.obj;
                imageView.setImageBitmap(bitmap);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitMapUtil.getBitmao(imgURL);
                Message msg = new Message();
                msg.obj=bitmap;
                handler.sendMessage(msg);
            }
        }).start();
    }
}
