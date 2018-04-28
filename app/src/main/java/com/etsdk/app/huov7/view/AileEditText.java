package com.etsdk.app.huov7.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by liu hong liang on 2017/2/25.
 *
 * 1.防止setText和addTextChangedListener造成死循环
 * 2.设置完文字，光标放到末尾
 *
 */

public class AileEditText extends EditText {
    private boolean isSetting;//是否设置文字中
    public AileEditText(Context context) {
        super(context);
    }

    public AileEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AileEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        isSetting=true;
        super.setText(text, type);
        setSelection(text.length());//将光标移动至末尾
        isSetting=false;
    }

    @Override
    public void addTextChangedListener(final TextWatcher watcher) {


        super.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(isSetting){
                    return;
                }
                watcher.beforeTextChanged(s,start,count,after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isSetting){
                    return;
                }
                watcher.onTextChanged(s,start,before,count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isSetting){
                    return;
                }
                watcher.afterTextChanged(s);
            }
        });
    }
}
