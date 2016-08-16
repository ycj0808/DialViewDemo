package com.android.icefire.dialview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by yangchj on 2016/8/16 0016.
 * email:ycj0808@126.com
 */
public class DialView extends View {
    private static final String TAG="DialView";

    private float mBorderWidth;//边框粗细
    private int mBorderColor;//边框颜色
    private int mBackgroundColor;//背景颜色

    private Paint mPaint;//画笔

    private RectF mBounds=new RectF();
    private float width;
    private float height;
    float radius;
    float smallLength;
    float largeLength;


    public DialView(Context context) {
        this(context, null);
    }

    public DialView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DialView, 0, 0);
        try {
            mBorderColor = typedArray.getColor(R.styleable.DialView_border_color, 0xff000000);
            mBackgroundColor=typedArray.getColor(R.styleable.DialView_background_color, 0xff000000);
            mBorderWidth = typedArray.getDimension(R.styleable.DialView_border_width, 2);
        }finally {
            typedArray.recycle();
        }
        init();
    }

    /**
     * 初始化画笔
     */
    private void init(){
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mBorderColor);
    }

    /**
     * 初始化全局参数，自适应界面，每次页面变化的时候都会被调用来重新计算
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBounds=new RectF(getLeft(),getTop(),getRight(),getBottom());
        width=mBounds.right-mBounds.left;
        height=mBounds.bottom-mBounds.top;

        if(width<height){
            radius=width/4;
        }else{
            radius=height/4;
        }
        smallLength=10;
        largeLength=20;
    }

    /**
     * 绘制
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(mBackgroundColor);
        mPaint.setColor(0x66555555);
        canvas.drawRoundRect(new RectF(mBounds.centerX()-(float)0.9*width/2, mBounds.centerY() - (float)0.9*height/2, mBounds.centerX() + (float)0.9*width/2, mBounds.centerY() + (float)0.9*height/2), 30, 30, mPaint);
        mPaint.setColor(mBorderColor);
        canvas.drawCircle(mBounds.centerX(),mBounds.centerY(),radius,mPaint);
        float start_x,start_y;
        float end_x,end_y;
        for(int i=0;i<60;++i){
            start_x= radius *(float)Math.cos(Math.PI/180 * i * 6);
            start_y= radius *(float)Math.sin(Math.PI/180 * i * 6);
            if(i%5==0){
                end_x = start_x+largeLength*(float)Math.cos(Math.PI / 180 * i * 6);
                end_y = start_y+largeLength*(float)Math.sin(Math.PI/180 * i * 6);
            }else{
                end_x = start_x+smallLength*(float)Math.cos(Math.PI/180 * i * 6);
                end_y = start_y+smallLength*(float)Math.sin(Math.PI/180 * i * 6);
            }
            start_x+=mBounds.centerX();
            end_x+=mBounds.centerX();
            start_y+=mBounds.centerY();
            end_y+=mBounds.centerY();
            canvas.drawLine(start_x, start_y, end_x, end_y, mPaint);
        }
        canvas.drawCircle(mBounds.centerX(),mBounds.centerY(),20,mPaint);
        canvas.rotate(60,mBounds.centerX(),mBounds.centerY());
        canvas.drawLine(mBounds.centerX(),mBounds.centerY(),mBounds.centerX(),mBounds.centerY()-radius,mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        float top = 0;
        float left = 0;
        mBounds.set(left,top,left+width,top+height);
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }
}
