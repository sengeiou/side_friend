package com.zankong.tool.zkapp.views;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.activity.ZKNaiveActivity;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.ViewUtil;
import com.zankong.tool.zkapp.util.ZKRipple;
import com.zankong.tool.zkapp.zk_interface.ZKBaseView;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YF on 2018/7/5.
 */

public class ZKInputView extends AppCompatEditText implements ZKBaseView {
    private Context mContext;
    public InputMethodManager imm;
    private ZKDocument mZKDocument;
    private Element mElement;
    private V8Function watch;
    private ZKParentAttr mZKParentAttribute;
    private V8Object mThisV8;


    public ZKInputView(Context context) {
        super(context);
    }

    public ZKInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZKInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void init(ZKDocument zkDocument,Element element){
        mThisV8 = new V8Object(ZKToolApi.runtime);
        init(zkDocument,element,mThisV8);

    }

    @Override
    public void init(ZKDocument zkDocument,Element element,V8Object v8Object){
        mZKDocument = zkDocument;
        mElement = element;
        mThisV8 = v8Object;
        mZKParentAttribute = new ZKParentAttr(zkDocument,element);
        mZKParentAttribute.initThisV8(v8Object);
        initThisV8(mZKParentAttribute.getThisV8());
        fillData();
    }

    @Override
    public V8Object getThisV8() {
        return mThisV8;
    }

    @Override
    public View getBaseView() {
        return this;
    }

    public void fillData(){
        Boolean isBackNull = false;
        String val = mZKParentAttribute.setView(this);
        for (Attribute attribute : mElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName().toLowerCase()) {
                case "hint"://提示文字
                    setHint(value);
                    break;
                case "hintfont"://提示文字的大小,颜色
                    for (String s : value.split(",")) {
                        if (s.contains("#")){
                            setHintTextColor(Color.parseColor(s));
                        }
                    }
                    break;
                case "maxline":
                    setMaxLines(Integer.parseInt(value));
                    break;
                case "font": //文字大小,颜色
                    ViewUtil.setFont(value,this);
                    break;
                case "type"://文字,数字,密码形式
                    int inputType = ViewUtil.getInputTypeInt.get("null");
                    for (String s : value.split("\\|")) {
                        inputType = inputType | ViewUtil.getInputTypeInt.get(s.trim());
                    }
                    setInputType(ViewUtil.getInputType.get(inputType));
                    break;
                case "gravity":
                    int gravity = 0x0;
                    for (String s : value.split("\\|")) {
                        gravity = gravity|ViewUtil.getGravityMap.get(s);
                    }
                    setGravity(gravity);
                    break;
                case "watch":
                    watch = mZKDocument.genContextFn(value);
                    break;
                case "nobackground":
                    if ("true".equals(value)){
                        isBackNull = true;
                    };
                    break;
            }
        }
        if (val == null){
            val = mElement.getText();
        }
        setText(val);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Util.log("beforeTextChanged");

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Util.log("beforeTextChanged");

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mZKDocument.invokeWithContext(watch,editable.toString());
            }
        });
        mZKDocument.getActivity().setOnReleaseListeners(new ZKNaiveActivity.OnReleaseListener() {
            @Override
            public void release() {
                ZKInputView.this.release();
            }
        });
        if (isBackNull) {
            setBackground(null);
            Util.log("fffffffffffffffffffffffffff");
        }else {
            setBackground(ZKRipple.getRipple(getContext(),mElement).getDrawable());
        }
    }

    @Override
    public void release() {
        if (watch != null)watch.release();
        if (mThisV8 != null)mThisV8.release();
    }

    private void initThisV8(V8Object v8Object){
        v8Object.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return getText().toString();
            }
        },"val");
        v8Object.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                watch = (V8Function) parameters.getObject(0);
                return null;
            }
        },"watch");
        v8Object.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                if (parameters.length() == 0){
                    return ZKInputView.this.getText().toString();
                }else{
                    if (parameters.get(0) instanceof String){
                        ZKInputView.this.setText(parameters.getString(0));
                    }else if (parameters.get(0) instanceof V8Object){
                        V8Object font = parameters.getObject(0);
                        for (String s : font.getKeys()) {
                            switch (s){
                                case "p":
                                    ZKInputView.this.setText(font.getString("p"));
                                    break;
                                case "color":
                                    ZKInputView.this.setTextColor(Color.parseColor(font.getString("color")));
                                    break;
                                case "size":
                                    ZKInputView.this.setTextSize(Util.px2px(font.getString("size")));
                                    break;
                            }
                        }
                    }
                }
                return null;
            }
        },"text");
        v8Object.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                if (parameters.length() == 0){
                    return ZKInputView.this.getText().toString();
                }else{
                    if (parameters.get(0) instanceof String){
                        ZKInputView.this.setText(parameters.getString(0));
                    }else if (parameters.get(0) instanceof V8Object){
                        V8Object font = parameters.getObject(0);
                        for (String s : font.getKeys()) {
                            switch (s){
                                case "p":
                                    ZKInputView.this.setText(font.getString("p"));
                                    break;
                                case "color":
                                    ZKInputView.this.setTextColor(Color.parseColor(font.getString("color")));
                                    break;
                                case "size":
                                    ZKInputView.this.setTextSize(Util.px2px(font.getString("size")));
                                    break;
                            }
                        }
                    }
                }
                return null;
            }
        },"set");
        v8Object.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                V8Object object = parameters.getObject(0);
                if (!object.getString("png").contains("emotion_del_normal")) {// 如果不是删除图标
                    insert(getFace(object.getString("png")));
                } else {
                    delete();
                }
                return null;
            }
        }, "insert");
        v8Object.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                if (parameters.length() == 0){
                    return ZKInputView.this.getHint();
                }else{
                    if (parameters.get(0) instanceof String){
                        ZKInputView.this.setHint(parameters.getString(0));
                    }else if (parameters.get(0) instanceof V8Object){
                        V8Object font = new V8Object(ZKToolApi.runtime);
                        for (String s : font.getKeys()) {
                            switch (s){
                                case "p":
                                    ZKInputView.this.setHint(font.getString("p"));
                                    break;
                                case "color":
                                    ZKInputView.this.setHintTextColor(Color.parseColor(font.getString("color")));
                                    break;
                                case "size":
                                    break;
                            }
                        }
                    }
                }
                return null;
            }
        },"hint");
        v8Object.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return null;
            }
        },"attr");
    }
    private SpannableStringBuilder getFace(String png) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        try {
            /**
             * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
             * 所以这里对这个tempText值做特殊处理
             * 格式：#[face/png/f_static_000.png]#，以方便判斷當前圖片是哪一個
             * */
            String tempText = "#[" + png + "]#";
            sb.append(tempText);
            sb.setSpan(
                    new ImageSpan(
                            getContext(),
                            BitmapFactory.decodeStream(getContext().getAssets().open(png))),
                    sb.length() - tempText.length(),
                    sb.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb;
    }
    private float y;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                y = event.getY();
            case MotionEvent.ACTION_MOVE:
                if(y < event.getY()){
                    getParent().requestDisallowInterceptTouchEvent(canScrollVertically(-1));
                }else {
                    getParent().requestDisallowInterceptTouchEvent(canScrollVertically(1));
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 向输入框里添加表情
     */
    private void insert(CharSequence text) {
        int iCursorStart = Selection.getSelectionStart((getText()));
        int iCursorEnd = Selection.getSelectionEnd((getText()));
        if (iCursorStart != iCursorEnd) {
            getText().replace(iCursorStart, iCursorEnd, "");
        }
        int iCursor = Selection.getSelectionEnd((getText()));
        getText().insert(iCursor, text);
    }



    /**
     * 删除图标执行事件
     * 注：如果删除的是表情，在删除时实际删除的是tempText即图片占位的字符串，所以必需一次性删除掉tempText，才能将图片删除
     */
    private void delete() {
        if (getText().length() != 0) {
            int iCursorEnd = Selection.getSelectionEnd(getText());
            int iCursorStart = Selection.getSelectionStart(getText());
            if (iCursorEnd > 0) {
                if (iCursorEnd == iCursorStart) {
                    if (isDeletePng(iCursorEnd)) {
                        String st = "#[face/png/f_static_000.png]#";
                        getText().delete(
                                iCursorEnd - st.length(), iCursorEnd);
                    } else {
                        getText().delete(iCursorEnd - 1,
                                iCursorEnd);
                    }
                } else {
                    getText().delete(iCursorStart,
                            iCursorEnd);
                }
            }
        }
    }



    /**
     * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：则讲删除整个tempText
     **/
    private boolean isDeletePng(int cursor) {
        String st = "#[face/png/f_static_000.png]#";
        String content = getText().toString().substring(0, cursor);
        if (content.length() >= st.length()) {
            String checkStr = content.substring(content.length() - st.length(),
                    content.length());
            String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(checkStr);
            return m.matches();
        }
        return false;
    }


}
