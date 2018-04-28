package com.etsdk.app.huov7.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.game.sdk.log.L;

public class DrawCanvas extends View {

    private int alpha=150;
    public DrawCanvas(Context context) {
        super(context);
    }

    /**
     * 设置图片透明度
     * @param alpha
     */
    public void setAlpha(int alpha) {
        L.e("hongliang","最后设置的alpha="+alpha);
        setBackgroundColor(Color.argb(alpha,00,00,00));
//        invalidate();
    }

    public DrawCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawCanvas(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        // 取得Resource 图片的Bitmap
//        Bitmap vBitmap = BitmapFactory.decodeResource( this.getResources(), R.mipmap.gg);
//        // 建立Paint 物件
//        Paint vPaint = new Paint();
//        vPaint .setStyle(Paint.Style.STROKE);   //空心
//        vPaint .setAlpha(alpha);   //
//        drawImage(canvas,vBitmap,vPaint,getLeft(),getTop(),getMeasuredWidth(),getMeasuredHeight(),0,0);
    }

    public static void drawImage(Canvas canvas, Bitmap blt, Paint vPaint, int x, int y, int w, int h, int bx, int by) {                                                       //x,y表示绘画的起点，
        Rect src = new Rect();// 图片
        Rect dst = new Rect();// 屏幕
        //src 这个是表示绘画图片的大小
        src.left = bx;   //0,0
        src.top = by;
        src.right = bx + w;// mBitDestTop.getWidth();,这个是桌面图的宽度，
        src.bottom = by + h;//mBitDestTop.getHeight()/2;// 这个是桌面图的高度的一半
        // 下面的 dst 是表示 绘画这个图片的位置
        dst.left = x;   //miDTX,//这个是可以改变的，也就是绘图的起点X位置
        dst.top = y;    //mBitQQ.getHeight();//这个是QQ图片的高度。 也就相当于 桌面图片绘画起点的Y坐标
        dst.right = x + w;  //miDTX + mBitDestTop.getWidth();// 表示需绘画的图片的右上角
        dst.bottom = y + h; // mBitQQ.getHeight() + mBitDestTop.getHeight();//表示需绘画的图片的右下角
        canvas.drawBitmap(blt, src, dst, vPaint);//这个方法  第一个参数是图片，第二个参数是 绘画该图片需显示多少。也就是说你想绘画该图片的某一些地方，而不是全部图片，第三个参数表示该图片绘画的位置
        src = null;
        dst = null;
    }

}