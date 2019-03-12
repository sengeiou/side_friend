package com.zankong.tool.zkapp.views;

import android.view.View;
import android.view.ViewGroup;

import com.eclipsesource.v8.Releasable;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.banner.Banner;
import com.zankong.tool.zkapp.views.card.Card;
import com.zankong.tool.zkapp.views.face.Face;
import com.zankong.tool.zkapp.views.group.Group;
import com.zankong.tool.zkapp.views.hr.Hr;
import com.zankong.tool.zkapp.views.img.Img;
import com.zankong.tool.zkapp.views.input.Input;
import com.zankong.tool.zkapp.views.layout.Layout;
import com.zankong.tool.zkapp.views.music.Music;
import com.zankong.tool.zkapp.views.p.P;
import com.zankong.tool.zkapp.views.picker.Picker;
import com.zankong.tool.zkapp.views.range.Range;
import com.zankong.tool.zkapp.views.rating.Rating;
import com.zankong.tool.zkapp.views.recycle.recycle;
import com.zankong.tool.zkapp.views.script.script;
import com.zankong.tool.zkapp.views.switcher.Switcher;
import com.zankong.tool.zkapp.views.table.Table;
import com.zankong.tool.zkapp.views.text.Text;
import com.zankong.tool.zkapp.views.time.Time;
import com.zankong.tool.zkapp.views.toolbar.ZKToolbar;
import com.zankong.tool.zkapp.views.verify.Verify;
import com.zankong.tool.zkapp.views.video.Video;
import com.zankong.tool.zkapp.views.viewpage.ZKViewPage;
import com.zankong.tool.zkapp.views.web.Web;
import com.zankong.tool.zkapp.zk_interface.ZKActivityLife;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.HashMap;

/**
 * Created by YF on 2018/6/21.
 */

public abstract class ZKViews implements ZKActivityLife,Releasable{
    protected ZKViewAgent          mZKViewAgent;       //真实布局代理
    private ZKDocument              mZKDocument;        //
    private Element                 mElement;           //
    private String                  style = "1",        //根据style生成布局代理
                                     id;                 //document.$('id')用到的id值
    private V8Object                mThisV8;            //控件对应的js对象;
    public static HashMap<String,Class<?>>        ViewMap = new HashMap<String, Class<?>>(){{
        put("banner", Banner.class);
        put("card", Card.class);
        put("face", Face.class);
        put("group", Group.class);
        put("hr", Hr.class);
        put("img", Img.class);
        put("input", Input.class);
        put("layout", Layout.class);
        put("music", Music.class);
        put("p", P.class);
        put("picker", Picker.class);
        put("range", Range.class);
        put("rating", Rating.class);
        put("recycle", recycle.class);
        put("script", script.class);
        put("switch", Switcher.class);
        put("table", Table.class);
        put("text", Text.class);
        put("time", Time.class);
        put("verify", Verify.class);
        put("video", Video.class);
        put("viewpage", ZKViewPage.class);
        put("web", Web.class);
        put("toolbar", ZKToolbar.class);
    }}; //控件标签集合
    abstract protected HashMap<String,Class<?>>   getStyleMap();             //获得之类的sytle集合

    /**
     * 构造函数,在此解析style和id值,但是并不在此生成布局代理,为了和document.createElement()方法搭配
     */
    public ZKViews(ZKDocument ZKDocument, Element element){
        this.mZKDocument = ZKDocument;
        this.mElement = element;
        for (Attribute attribute : mElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "style":
                    style = value;
                    break;
                case "id":
                    id = value;
                    break;
            }
        }
        if (style.equals("")){
            for (String s : getStyleMap().keySet()) {
                if (getStyleMap().get(s) != null) {
                    style = s;
                }
            }
        }
    }

    /**
     * 代理布局生成布局,解析xml
     */
    public void init(ViewGroup viewGroup)throws Exception{
        mZKViewAgent = (ZKViewAgent) getStyleMap().get(style).getConstructor(ZKDocument.class,Element.class).newInstance(mZKDocument,mElement);
        mThisV8 = new V8Object(ZKToolApi.runtime);
        mZKViewAgent.initThisV8(mThisV8);
        mZKViewAgent.setViewGroup(viewGroup);
        mZKViewAgent.initView(viewGroup);
        mZKViewAgent.setParentAttr(mElement,mZKViewAgent.getView());
    }

    public void fillData(){
        mZKViewAgent.fillData(mElement);
    }

    public View getView(){
        return mZKViewAgent.getView();
    }

    public String getId() {
        return id;
    }

    public V8Object getThisV8() {
        return mThisV8;
    }

    @Override
    public void onCreate() {
        mZKViewAgent.onCreate();
    }

    @Override
    public void onPause() {
        mZKViewAgent.onPause();
    }

    @Override
    public void onStart() {
        mZKViewAgent.onStart();
    }

    @Override
    public void onResume() {
        mZKViewAgent.onResume();
    }

    @Override
    public void onStop() {
        mZKViewAgent.onStop();
    }

    @Override
    public void onDestroy() {
        mZKViewAgent.onDestroy();
    }

    @Override
    public void onRestart() {
        mZKViewAgent.onRestart();
    }

    @Override
    public boolean onBack() {
        return mZKViewAgent.onBack();
    }

    @Override
    public void release() {
        if (mThisV8 != null) {
            mThisV8.release();
        }
        mZKViewAgent.release();
    }
}
