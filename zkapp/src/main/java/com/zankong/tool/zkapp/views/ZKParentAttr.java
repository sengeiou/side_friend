package com.zankong.tool.zkapp.views;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.HashMap;

import static android.view.View.VISIBLE;

/**
 * Created by YF on 2018/7/12.
 */

public class ZKParentAttr {
    private V8Object mThisV8;
    private ZKDocument mZKDocument;
    private Element mElement;
    private View mView;
    private HashMap<String, String> mV8FnJs;//事件集合
    private OnClickJavaListener mOnClickJavaListener;//java点击事件监听
    private OnLongClickJavaListener mOnLongClickJavaListener;//java长按事件监听
    private int viewId;
    private V8Function mTouchFn;
    V8Object event = new V8Object(ZKToolApi.runtime);

    /**
     * java里的点击回调监听
     */
    public interface OnClickJavaListener {
        void onClickJava();
    }

    /**
     * java里的长按点击回调监听
     */
    public interface OnLongClickJavaListener {
        void onLongClickJava();
    }

    /**
     * 添加java点击监听
     */
    public void setOnClickJavaListener(OnClickJavaListener onClickJavaListener) {
        mOnClickJavaListener = onClickJavaListener;
    }

    /**
     * 设置java长按监听
     */
    public void setOnLongClickJavaListener(OnLongClickJavaListener onLongClickJavaListener) {
        mOnLongClickJavaListener = onLongClickJavaListener;
    }

    public ZKParentAttr(ZKDocument ZKDocument, Element element) {
        mZKDocument = ZKDocument;
        mElement = element;
        mV8FnJs = new HashMap<>();
    }

    //给传过来的view设置父属性
    public String setView(View view) {
        mView = view;
        viewId = mView.getId();
        if (viewId == -1) {
            viewId = View.generateViewId();
            mView.setId(viewId);
        }
        String val = null;
        ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();

        for (Attribute attribute : mElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName().toLowerCase()) {
                case "click"://点击事件
                    mV8FnJs.put("click", value);
                    mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mOnClickJavaListener != null) mOnClickJavaListener.onClickJava();
                            invokeJs("click");
                        }
                    });
                    break;
                case "longclick"://长按事件
                    mV8FnJs.put("longClick", value);
                    mView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            if (mOnLongClickJavaListener != null)
                                mOnLongClickJavaListener.onLongClickJava();
                            invokeJs("longClick");
                            return true;
                        }
                    });
                    break;
                case "height"://高度
                    layoutParams.height = Util.px2px(value, mZKDocument.getDisplayMetrics().heightPixels);
                    break;
                case "width"://宽度
                    layoutParams.width = Util.px2px(value, mZKDocument.getDisplayMetrics().widthPixels);
                    break;
                case "val"://变量
                    try {
                        V8Function v8Function = mZKDocument.genContextVal(value);
                        val = (String) mZKDocument.invokeWithContext(v8Function);
                        v8Function.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "background"://背景色
                    mView.setBackgroundColor(Color.parseColor(value));
                    break;
                case "show"://是否展示
                    show(value);
                    break;
                case "padding"://内边距
                    String[] padding = value.split(",");
                    if (padding.length == 1) {
                        mView.setPadding(
                                Util.px2px(padding[0]),
                                Util.px2px(padding[0]),
                                Util.px2px(padding[0]),
                                Util.px2px(padding[0])
                        );
                    } else {
                        mView.setPadding(
                                Util.px2px(padding[0]),
                                Util.px2px(padding[1]),
                                Util.px2px(padding[2]),
                                Util.px2px(padding[3])
                        );
                    }
                    break;
                case "margin"://外边距
                    String[] margin = value.split(",");
                    if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                        if (margin.length == 1) {
                            ((ViewGroup.MarginLayoutParams) layoutParams).setMargins(
                                    Util.px2px(margin[0]),
                                    Util.px2px(margin[0]),
                                    Util.px2px(margin[0]),
                                    Util.px2px(margin[0])
                            );
                        } else {
                            ((ViewGroup.MarginLayoutParams) layoutParams).setMargins(
                                    Util.px2px(margin[0]),
                                    Util.px2px(margin[1]),
                                    Util.px2px(margin[2]),
                                    Util.px2px(margin[3])
                            );
                        }

                    }
                    break;
                case "touch":
                    mTouchFn = mZKDocument.genContextFn(value);
                    mView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            event.add("action", motionEvent.getAction());
                            event.add("x", motionEvent.getX());
                            event.add("y", motionEvent.getY());
                            event.add("ACTION_DOWN", MotionEvent.ACTION_DOWN);
                            event.add("ACTION_MOVE", MotionEvent.ACTION_MOVE);
                            event.add("ACTION_UP", MotionEvent.ACTION_UP);
                            mZKDocument.invokeWithContext(mTouchFn, event);
                            return true;
                        }
                    });
                    break;
            }
        }
        mView.setLayoutParams(layoutParams);
        showParentView(mView);
        return val;
    }

    //显示所有的父类
    private void showParentView(View view) {
        view.setVisibility(VISIBLE);
        if (view.getParent() instanceof ViewGroup) {
            ViewGroup parentView = (ViewGroup) view.getParent();
            if (parentView != null) {
                showParentView(parentView);
            }
        }
    }

    //设置V8Object的js方法
    public void initThisV8(V8Object object) {
        mThisV8 = object;
        mThisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                String status = "";
                if (parameters.length() > 0) {
                    if (parameters.get(0) instanceof Boolean) {
                        status = parameters.getBoolean(0) + "";
                    } else {
                        status = parameters.getString(0);
                    }
                }
                show(status);
                return null;
            }
        }, "show");
        mThisV8.add("view", viewId);
        mThisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return viewId;
            }
        }, "getView");
        mThisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                mTouchFn = (V8Function) parameters.getObject(0);
                mView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        event.add("action", motionEvent.getAction());
                        event.add("x", motionEvent.getX());
                        event.add("y", motionEvent.getY());
                        event.add("ACTION_DOWN", MotionEvent.ACTION_DOWN);
                        event.add("ACTION_MOVE", MotionEvent.ACTION_MOVE);
                        event.add("ACTION_UP", MotionEvent.ACTION_UP);
                        mZKDocument.invokeWithContext(mTouchFn, event);
                        return true;
                    }
                });
                return null;
            }
        }, "touch");
        mThisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                mView.setBackgroundColor(Color.parseColor(parameters.getString(0)));
                return null;
            }
        }, "background");
    }

    public V8Object getThisV8() {
        return mThisV8;
    }


    /**
     * 执行js代码
     */
    public boolean invokeJs(String key) {
        boolean contains = false;
        if (mV8FnJs.containsKey(key)) {
            contains = true;
            V8Function jsFn = mZKDocument.genContext(mV8FnJs.get(key));
            try {
                mZKDocument.invokeWithContext(jsFn);
            } catch (Exception e) {
                e.printStackTrace();
            }
            jsFn.release();
        }
        return contains;
    }

    /**
     * 显示/隐藏布局
     */
    public void show(String status) {
        switch (status) {
            case "invisible":
                mView.setVisibility(View.INVISIBLE);
                break;
            case "false":
            case "gone":
                mView.setVisibility(View.GONE);
                break;
            case "":
            case "visible":
            case "show":
            case "true":
            default:
                mView.setVisibility(View.VISIBLE);
        }
    }

}
