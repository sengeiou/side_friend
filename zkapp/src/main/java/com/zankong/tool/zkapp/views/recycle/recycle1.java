package com.zankong.tool.zkapp.views.recycle;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.document.listview.ZKRecycleView;
import com.zankong.tool.zkapp.item.NullAdapter;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by YF on 2018/7/6.
 */

public class recycle1 extends ZKViewAgent {

    private ZKRecycleView mZKRecycleView;
    private ZKAdapter mZKAdapter;

    /**
     * 构造函数,完成全局属性的解析
     *
     * @param zkDocument
     * @param element
     */
    public recycle1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_recycle_1);
        mZKRecycleView = ((ZKRecycleView) findViewById(R.id.recycle));
    }

    @Override
    protected void setParentAttr(Element selfElement, View view) {
        super.setParentAttr(selfElement,view);
    }

    @Override
    public void fillData(Element viewElement) {
        RecycleBean.Builder builder = new RecycleBean.Builder(getContext());
        for (Attribute attribute : viewElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "num":
                    builder.setNum(Util.getInt(value));
                    break;
                case "orientation":
                    builder.setOrientation(value);
                    break;
                case "type":
                    builder.setType(value);
                    break;
                case "reverse":
                    builder.setReverse(Boolean.parseBoolean(value));
                    break;
                case "full":
                    if (Boolean.parseBoolean(value)){
                        mZKRecycleView.setHasFixedSize(true);
                        mZKRecycleView.setNestedScrollingEnabled(false);
                    }
                    break;
                case "background":
                    mZKRecycleView.setBackgroundColor(Color.parseColor(value));
                    break;
            }
        }
        mZKRecycleView.setLayoutManager(builder.builder().getLayoutManager());
        try {
            if (viewElement.elements().size() == 0){
                mZKAdapter = new NullAdapter(getZKDocument(),viewElement);
            }else {
                mZKAdapter = (ZKAdapter) ZKAdapter.adapterMap.get(viewElement.elements().get(0).getName().toLowerCase()).getConstructor(ZKDocument.class, Element.class).newInstance(getZKDocument(), viewElement);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        mZKRecycleView.setAdapter(mZKAdapter);
        mZKAdapter.notifyDataSetChanged();
    }

    @Override
    public Object getResult() {
        return null;
    }


    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return V8Utils.createFrom(mZKAdapter.getThisV8());
            }
        },"adapter");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mZKAdapter.onCreate();
    }

    @Override
    public void onPause() {
        super.onPause();
        mZKAdapter.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mZKAdapter.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mZKAdapter.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mZKAdapter.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mZKAdapter.onDestroy();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        mZKAdapter.onRestart();
    }

    @Override
    public boolean onBack() {
        return mZKAdapter.onBack();
    }

    @Override
    public void release() {
        super.release();
        mZKAdapter.release();
    }
}
