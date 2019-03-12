package com.zankong.tool.side_friend.div_item;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.views.ZKImgView;

import org.dom4j.Element;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ZKAppAdapter("bills")
public class Bills extends ZKAdapter {
    private HashMap<String,String> list;
    private List<Map<String,Object>> mapList;
    private SetMoney setMoney;
    private List<String> dates;
    public Bills(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
    }
    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        return new BillHolder(mLayoutInflater.inflate(R.layout.item_bills,viewGroup,false));
    }
    @Override
    protected int getViewType(int position) {
        return 0;
    }
    
    
    private void setSetMoney(SetMoney setMoney){
        this.setMoney = setMoney;
    }
    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof HolderInit) {
            ((HolderInit) viewHolder).init(position);
        }
    }
    private class BillHolder extends RecyclerView.ViewHolder implements HolderInit {
        private RoundedImageView img;
        private TextView title,tag,time,money,status,top_tv_time;
        private final LinearLayout top_layout;
        private final TextView tv_income;
        private final TextView tv_disburse;
        public BillHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.title);
            tag = itemView.findViewById(R.id.tag);
            money = itemView.findViewById(R.id.money);
            time = itemView.findViewById(R.id.time);
            top_tv_time = itemView.findViewById(R.id.top_tv_time);
            status = itemView.findViewById(R.id.status);
            top_layout = itemView.findViewById(R.id.top_layout);
            tv_income = itemView.findViewById(R.id.tv_income);
            tv_disburse = itemView.findViewById(R.id.tv_disburse);
        }
        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key){
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(key))).into(img);
                        break;
                    case "title":
                        title.setText(object.getString(key));
                        break;
                    case "tag":
                        tag.setText(object.getString(key));
                        break;
                    case "money":
                        money.setText(object.getString(key));
                        break;
                    case "time":
                        String s2 = addStringTime(object.getString(key));
                        Log.e("fffffffffffff",s2+"ppp");
                        if ("".equals(s2)){
                            top_layout.setVisibility(View.GONE);
                        }else {
                            String s = s2.split(",")[0];
                            time.setText(object.getString(key));
                            top_layout.setVisibility(View.VISIBLE);
                            top_tv_time.setText(s);
                            String s1 = s2.split(",")[1];
                            if (mapList != null){
                                for (int i = 0; i < mapList.size();i++){
                                    Map<String, Object> map = mapList.get(i);
                                    if ((map.get("year").toString()+map.get("month").toString()).equals(s1)){
                                        Log.e("tttttttttt","dddddddddddd");
                                        tv_income.setText(map.get("income").toString());
                                        tv_disburse.setText(map.get("pay").toString());
                                    }
                                }
                            }
                          
                        }
                        
                      
                      
                       
                        break;
                    case "symbol":
                        if("+".equals(object.getString(key))){
                            money.setTextColor(Color.parseColor("#ff0000"));
                        }else {
                            money.setTextColor(Color.parseColor("#000000"));
                        }
                        break;
                    case "status":
                        status.setText(object.getString(key));
                        break;
                }
            }
        }
        private String addStringTime(String time) {
            if (list == null) {
                list = new HashMap<>();
            }
            if (dates == null){
                dates = new ArrayList<>();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
            //获取当前时间
            Date date = new Date(System.currentTimeMillis());
            String currentFormat = simpleDateFormat.format(date);
            String createdAt = time.split(" ")[0];
            String[] time1s = createdAt.split("-");
            String time1sOne = time1s[0] + time1s[1];
            String[] time2s = currentFormat.split("-");
            String time1sTwo = time2s[0] + time2s[1];
            if (list.containsKey(time1sOne)){
                if (!time.equals(list.get(time1sOne)))return "";
            }else {
                list.put(time1sOne,time);
            }
            return (time1sTwo.equals(time1sOne)?"本月":(time2s[0].equals(time1s[0]) ?time1s[1]+"月" : time1s[0]+"-"+time1s[1]))+","+time1sOne;
        }
    }

    @Override
    protected void initV8this() {
        super.initV8this();
        getThisV8().registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                V8Array array = parameters.getArray(0);
                Log.e("ggggggggggggg1","t");
                if (array.length() > 0){
                    mapList = new ArrayList<>();
                    for (int i = 0; i < array.length();i++){
                        Map<String,Object> map = new HashMap<>();
                        V8Object object = array.getObject(i);
                        String income = object.get("income")+"";
                        String pay = object.get("pay")+"";
                        String month = object.get("month")+"";
                        String year = object.get("year")+"";
                        map.put("income",income);
                        map.put("pay",pay);
                        map.put("month",month);
                        map.put("year",year);
                        mapList.add(map);
                    }
                }

                notifyDataSetChanged();

                return null;
            }
        },"setMouthList");
    }
    
    
    private interface SetMoney{
        void onData();
    }
}
