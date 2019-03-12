package com.zankong.tool.zkapp.views.switcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zankong.tool.zkapp.views.ZKPView;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2018/8/10 0010.
 */

public class Switcher2 extends ZKViewAgent {

    private List<Element> listElement;
    private String img = "", imgb = "",size="null";//当前状态
    private int num = -1;
    private String itemClick;
    //fillData
    private int callBackPosition = 0;
    private ListView listview;
    private ImageView[] btnArr;
    public Switcher2(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }
    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.item_switch3);
        listview = (ListView) findViewById(R.id.list_switch);
    }

    @Override
    public void fillData(Element selfElement) {
        listElement = new ArrayList<Element>();
        List<Attribute> attributes = selfElement.attributes();
        for (Attribute attribute : attributes) {
            String s = attribute.getValue();
            switch (attribute.getName().toLowerCase()) {
                case "font":
                    break;
                case "off":
                    img = s;
                    break;
                case "on":
                    imgb = s;
                    break;
                case "imgsize":
                    size = s;
                    break;
                case "num":
                    num = Integer.parseInt(s)-1;
                    break;
                case "itemclick":
                    itemClick = s;
                    break;
            }
        }
        for (Element element : selfElement.elements()) {
            listElement.add(element);
        }
        MyAdapter adapter = new MyAdapter(getContext());
        listview.setAdapter(adapter);

    }

    @Override
    public Object getResult() {

        Util.log("switch 2",callBackPosition+"");
        return callBackPosition;
    }



    private class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private Context myContent = null;

        public MyAdapter(Context context) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.myContent = context;
            setData();
        }

        private void setData() {
            btnArr = new ImageView[listElement.size()];
            for (int i = 0; i < listElement.size(); i++) {
                btnArr[i] = new ImageView(myContent);
                btnArr[i].setTag(i);
            }
        }

        @Override
        public int getCount() {
            return listElement.size();
        }

        @Override
        public Object getItem(int position) {
            return listElement.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.adapter_switcher2, null, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.init(position,parent);
            return convertView;
        }

        private class ViewHolder {
            private RelativeLayout rl_list_child;
            private ZKImgView img1;
            private ImageView img2;
            private ZKPView tv1;
            private ZKPView tv2;
            public ViewHolder(View convertView){
                rl_list_child = (RelativeLayout) convertView.findViewById(R.id.rl_list_child);
                img1 = (ZKImgView) convertView.findViewById(R.id.list_child_im1);
                img2 = (ImageView) convertView.findViewById(R.id.list_child_im2);
                tv1 = (ZKPView) convertView.findViewById(R.id.list_child_tv1);
                tv2 = (ZKPView) convertView.findViewById(R.id.list_child_tv2);
            }


            public void init(int position,ViewGroup parent){
                if (!size.equals("null")) {
                    ViewGroup.LayoutParams layoutParams = img2.getLayoutParams();
                    String[] mAttr = size.split(",");
                    if (mAttr.length == 1) {
                        layoutParams.width = Util.dip2px(Float.parseFloat(mAttr[0]));
                        layoutParams.height = Util.dip2px(Float.parseFloat(mAttr[0]));
                        layoutParams.width = (int) (Integer.parseInt(mAttr[0]) * (getContext().getResources().getDisplayMetrics().density));
                        layoutParams.height = (int) (Integer.parseInt(mAttr[0]) * (getContext().getResources().getDisplayMetrics().density));
                    } else {
                        layoutParams.width = Util.dip2px(Float.parseFloat(mAttr[0]));
                        layoutParams.height = Util.dip2px(Float.parseFloat(mAttr[1]));
                    }
                }
                img2.setImageBitmap(Util.getImageFromAssetsFile(img));

                for (Element element : listElement.get(position).elements()) {
                    switch (element.getName()) {
                        case "p":
                            tv2.init(getZKDocument(),element,getThisV8());
                            break;
                        case "lp":
                            tv1.init(getZKDocument(),element,getThisV8());
                            break;
                        case "limg":
                            img1.init(getZKDocument(),element,getThisV8());

                            break;
                        case "rimg":

                            break;
                    }
                }
                if (parent.getChildCount() == position) {
                    btnArr[position] = img2;
                    btnArr[position].setTag(position);
//                    rl_list_child.setTag(position);
                    //                    正常情况下应该执行的代码
                } else {
                    //   这里就是多次加载的问题，可以不用理这里面的 代码，
                }
                if (btnArr[0].getTag().equals(num)) {
//                    Dom.getImageFromAssetsFile(mContext, imgb, btnArr[0]);
//                    Glide.with(myContent)
//                            .asBitmap()
//                            .load(Util.getImageFromAssetsFile(imgb))
//                            .into(btnArr[0]);
                    btnArr[0].setImageBitmap(Util.getImageFromAssetsFile(imgb));
                }else {
//                    Dom.getImageFromAssetsFile(mContext, img, btnArr[0]);
//                    Glide.with(myContent)
//                            .asBitmap()
//                            .load(Util.getImageFromAssetsFile(img))
//                            .into(btnArr[0]);
                    btnArr[0].setImageBitmap(Util.getImageFromAssetsFile(img));
                }
//                img2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        for (int i = 0; i < listElement.size(); i++) {
//                            if (v.getTag().equals(i)) {
//                                Dom.getImageFromAssetsFile(mContext, imgb, btnArr[i]);
//                            } else {
//                                Dom.getImageFromAssetsFile(mContext, img, btnArr[i]);
//                            }
//                        }
//                    }
//                });
                rl_list_child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < listElement.size(); i++) {
                            if (img2.getTag().equals(i)) {
                                num = i;
//                                Dom.getImageFromAssetsFile(mContext, imgb, btnArr[i]);
//                                Glide.with(myContent)
//                                        .asBitmap()
//                                        .load(Util.getImageFromAssetsFile(imgb))
//                                        .into(btnArr[i]);
                                btnArr[i].setImageBitmap(Util.getImageFromAssetsFile(imgb));
                            } else {
//                                Dom.getImageFromAssetsFile(mContext, img, btnArr[i]);
//                                Glide.with(myContent)
//                                        .asBitmap()
//                                        .load(Util.getImageFromAssetsFile(img))
//                                        .into(btnArr[i]);
                                btnArr[i].setImageBitmap(Util.getImageFromAssetsFile(img));
                            }
                        }
                        try {
//                            String name = ((TextView) ((LinearLayout) view).getChildAt(1)).getText().toString();
//                            String png = ((TextView) ((LinearLayout) view).getChildAt(2)).getText().toString();
                            callBackPosition = position;
                            V8Object position1 = new V8Object(ZKToolApi.runtime);
                            position1.add("position", position+"");
                            V8Function v8Function = getZKDocument().genContextFn(itemClick);
                            getZKDocument().invokeWithContext(v8Function,position1);
                            v8Function.release();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                return callBackPosition;
            }
        },"ff");
    }
}
