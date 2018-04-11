package com.king.ewrite;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by KingLee on 2018/4/11.
 */

/***
 *
 * 圆形框 Textview
 * 用于启动图右上角提示
 *
 */
public class CustomTextView extends TextView {
    private Paint mPaint;
    public CustomTextView(Context context) {
        super(context);
    }
    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }
    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        mPaint = new Paint();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF();
        //设置线条宽度
        mPaint.setStrokeWidth(2);
        //根据文字的颜色来绘画框
        mPaint.setColor(getPaint().getColor());
        //设置画笔的画出是空心
        mPaint.setStyle(Paint.Style.STROKE);
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //设置画得一个半径，然后比较长和宽，确定半径
        int banjin = getMeasuredWidth()>getMeasuredHeight()?getMeasuredWidth():getMeasuredHeight();
        rectF.set(getPaddingLeft(),getPaddingTop(),banjin-getPaddingRight(),banjin-getPaddingBottom());
        canvas.drawArc(rectF,0,360,false,mPaint);
    }
}
