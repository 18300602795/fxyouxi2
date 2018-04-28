package com.etsdk.app.huov7.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 带描边的textview
 * Created by Administrator on 2017/4/26 0026.
 */

public class StrokeTextView extends android.support.v7.widget.AppCompatTextView{
    private TextView outlineTextView = null;

    public StrokeTextView(Context context)
    {
        super(context);

        outlineTextView = new TextView(context);
        init();
    }

    public StrokeTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        outlineTextView = new TextView(context, attrs);
        init();
    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        outlineTextView = new TextView(context, attrs, defStyle);
        init();
    }

    public void init()
    {
        TextPaint paint = outlineTextView.getPaint();
        paint.setStrokeWidth(2);// 描边宽度
        paint.setStyle(Paint.Style.STROKE);
        outlineTextView.setTextColor(Color.parseColor("#000000"));// 描边颜色
        outlineTextView.setGravity(getGravity());
    }

    @Override
    public void setLayoutParams (ViewGroup.LayoutParams params)
    {
        super.setLayoutParams(params);
        outlineTextView.setLayoutParams(params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 设置轮廓文字
        CharSequence outlineText = outlineTextView.getText();
        if (outlineText == null || !outlineText.equals(this.getText()))
        {
            outlineTextView.setText(getText());
            postInvalidate();
        }
        outlineTextView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout (boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        outlineTextView.layout(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        outlineTextView.draw(canvas);
        super.onDraw(canvas);
    }
}
