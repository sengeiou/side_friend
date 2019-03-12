package com.zankong.tool.side_friend.diy_view.screen;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.example.zkapp_map.bean.JSONBean;
import com.example.zkapp_map.bean.ServiceBean;
import com.google.gson.Gson;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.side_friend.div_item.CityListViewAdapter;
import com.zankong.tool.side_friend.div_item.OrderListViewAdapter;
import com.zankong.tool.side_friend.div_item.ProvinceListViewAdapter;
import com.zankong.tool.side_friend.div_item.SieveItemAdapter;
import com.zankong.tool.side_friend.div_item.TypeRecyclerAdapter;
import com.zankong.tool.side_friend.domain.CityBean;
import com.zankong.tool.side_friend.domain.ProvinceBean;
import com.zankong.tool.side_friend.domain.SieveListBean;
import com.zankong.tool.side_friend.domain.TypeJSONBean;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.v8fn.ZKLocalStorage;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Screen_recruit extends ZKViewAgent implements View.OnClickListener{
    private LinearLayout llBody;
    private RelativeLayout type,city,order,screen;
    private TextView typeName,cityName,orderName,screenName;
    private ImageView typeIv,cityIv,orderIv,screenIv;
    private PopupWindow popMenu;
    private int menuIndex = 0;
    private View buttomView;
    private List<JSONBean> typeList;
    private List<CityBean> cityBeanList;
    private List<ProvinceBean> provinceBeanList;

    public Screen_recruit(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }
    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_screen_recruit);
        llBody = (LinearLayout) findViewById(R.id.ll_body);
        type = (RelativeLayout) findViewById(R.id.type);
        typeName = (TextView) findViewById(R.id.type_name);
        city = (RelativeLayout) findViewById(R.id.city);
        cityName = (TextView) findViewById(R.id.city_name);
        order = (RelativeLayout) findViewById(R.id.order);
        orderName = (TextView) findViewById(R.id.order_name);
        screen = (RelativeLayout) findViewById(R.id.screen);
        screenName = (TextView) findViewById(R.id.screen_name);
        typeIv = (ImageView) findViewById(R.id.type_iv);
        cityIv = (ImageView) findViewById(R.id.city_iv);
        orderIv = (ImageView) findViewById(R.id.order_iv);
        screenIv = (ImageView) findViewById(R.id.screen_iv);
        type.setOnClickListener(this);
        city.setOnClickListener(this);
        order.setOnClickListener(this);
        screen.setOnClickListener(this);
        typeIv.setBackgroundResource(R.drawable.down_arrow);
        cityIv.setBackgroundResource(R.drawable.down_arrow);
        orderIv.setBackgroundResource(R.drawable.down_arrow);
        screenIv.setBackgroundResource(R.drawable.down_arrow);

        /**
         * 初始化城市数据
         */
        initCityData();
        /**
         * 初始化定位城市到城市列表
         */
        String city = ZKLocalStorage.mSharedPreferences.getString("city", "全部");
        String province = ZKLocalStorage.mSharedPreferences.getString("province", "全部");
        for (int i = 0; i < provinceBeanList.size(); i++) {
            if (provinceBeanList.get(i).getProvinceName().equals(province)){
                provincePosition = i;
                for (int i1 = 0; i1 < provinceBeanList.get(i).getCityBeans().size(); i1++) {
                    if(provinceBeanList.get(i).getCityBeans().get(i1).getCityName().equals(city)){
                        cityPosition = i1;
                    }
                }
            }
        }
    }

    /**
     * 解析城市xml
     */
    private void initCityData(){
        Element elementByPath = Util.getElementByPath("address.xml");
        //省份
        provinceBeanList = new ArrayList<>();
        ProvinceBean pb = new ProvinceBean();
        pb.setProvinceName("全国");
        List<CityBean> ls = new ArrayList<>();
        CityBean cb = new CityBean();
        cb.setCityName("全部");
        ls.add(cb);
        pb.setCityBeans(ls);
        provinceBeanList.add(pb);

        ProvinceBean provinceBean = new ProvinceBean();
        for (Element element : elementByPath.elements()) {
            String name = element.getName();
            if (name.equals("key")){
                /**
                 * 解析省份名
                 */
                String province = element.getStringValue();
                provinceBean.setProvinceName(province);
                //Log.e("省",stringValue);
            }
            if (name.equals("array")){
                CityBean cityBean = new CityBean();
                cityBeanList = new ArrayList<>();
                for (Element element1 : element.elements()) {
                for (Element element2 : element1.elements()) {
                    String name1 = element2.getName();
                    if (name1.equals("key")){
                        String city = element2.getStringValue();
                        cityBean.setCityName(city);
                        //Log.e("市",city);
                    }
                    if (name1.equals("array")){
                        List<String> countyList = new ArrayList<>();
                        for (Element element3 : element2.elements()) {
                            String county = element3.getStringValue();
                            countyList.add(county);
                        }
                        cityBean.setCountyList(countyList);
                        cityBeanList.add(cityBean);
                        cityBean = new CityBean();
                    }
                }
                }
                provinceBean.setCityBeans(cityBeanList);
                provinceBeanList.add(provinceBean);
                provinceBean = new ProvinceBean();
            }
        }
    }

    /**
     * 初始化popMenu
     * @param contentView
     */
    private void initPopMenu(View contentView) {
        popMenu = new PopupWindow(contentView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        popMenu.setOutsideTouchable(true);
        popMenu.setBackgroundDrawable(new BitmapDrawable());
        popMenu.setFocusable(true);
        popMenu.setAnimationStyle(R.style.PopuWindowAnim);
        popMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                typeName.setTextColor(Color.parseColor("#5a5959"));
                cityName.setTextColor(Color.parseColor("#5a5959"));
                orderName.setTextColor(Color.parseColor("#5a5959"));
                screenName.setTextColor(Color.parseColor("#5a5959"));
                typeIv.setBackgroundResource(R.drawable.down_arrow);
                cityIv.setBackgroundResource(R.drawable.down_arrow);
                orderIv.setBackgroundResource(R.drawable.down_arrow);
                screenIv.setBackgroundResource(R.drawable.down_arrow);
            }
        });
    }

    /**
     * 类型
     * @param contentView
     */
    private void typePopMenu(View contentView){
        RecyclerView rvType = contentView.findViewById(R.id.rv_type);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(OrientationHelper. VERTICAL);
        rvType.setLayoutManager(linearLayoutManager);
        rvType.setAdapter(new TypeRecyclerAdapter(listList,this));
    }
    public void getTypeOnClickIndex(int position){
        getZKDocument().invokeWithContext(v8Function,"type",position);
    }
    private List<List<TypeJSONBean>> listList;
    public void getTypeData(JSONArray jsonArray) throws JSONException {
        int j = 0;
        Gson gson = new Gson();
        List<TypeJSONBean> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = new JSONArray(jsonArray.toString()).getJSONObject(i);
            TypeJSONBean typeJSONBean = gson.fromJson(jsonObject.toString(), TypeJSONBean.class);
            list.add(typeJSONBean);
        }

        List<TypeJSONBean> typeJSONBeans = new ArrayList<>();
        /**
         * list集合4个一组
         */
        listList = new ArrayList<>();
        for (int k=0;k<list.size();k++){
            typeJSONBeans.add(list.get(k));
            if (j==3){
                listList.add(typeJSONBeans);
                typeJSONBeans = new ArrayList<>();
                //styleNameBean.add(nameBean);
                j=-1;
            }
            j++;
        }
        if (list.size()%4!=0) {
            listList.add(typeJSONBeans);
        }
    }

    /**
     * 城市
     * @param contentView
     */
    private int provincePosition = 0;
    private int cityPosition = -1;
    private void cityPopMenu(View contentView){
        ListView lvProvince = contentView.findViewById(R.id.lv_province);
        GridView gvCity = contentView.findViewById(R.id.gv_city);
        buttomView = contentView.findViewById(R.id.view);
        ProvinceListViewAdapter provinceListViewAdapter = new ProvinceListViewAdapter(provinceBeanList, getContext());
        provinceListViewAdapter.setSeclection(provincePosition);
        provinceListViewAdapter.notifyDataSetChanged();
        /**
         * listView滚动到指定位置
         */
        lvProvince.post(new Runnable() {
            @Override
            public void run() {
                lvProvince.smoothScrollToPosition(provincePosition);
            }
        });
        lvProvince.setAdapter(provinceListViewAdapter);
        List<CityBean> cityBeans = provinceBeanList.get(provincePosition).getCityBeans();
        CityListViewAdapter cityListViewAdapter = new CityListViewAdapter(cityBeans, getContext());
        cityListViewAdapter.setSeclection(cityPosition);
        cityListViewAdapter.notifyDataSetChanged();
        gvCity.setAdapter(cityListViewAdapter);
        /**
         * 省份点击事件，展示右边城市
         */
        lvProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                provincePosition = position;
                cityPosition = -1;
                provinceListViewAdapter.setSeclection(provincePosition);
                provinceListViewAdapter.notifyDataSetChanged();
                List<CityBean> cityBeans = provinceBeanList.get(position).getCityBeans();
                CityListViewAdapter cityListViewAdapter = new CityListViewAdapter(cityBeans, getContext());
                cityListViewAdapter.setSeclection(cityPosition);
                cityListViewAdapter.notifyDataSetChanged();
                gvCity.setAdapter(cityListViewAdapter);
            }
        });
        /**
         * 城市点击事件
         */
        gvCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityPosition = position;
                TextView tvCity = view.findViewById(R.id.tv_city);
                String cityName = tvCity.getText().toString();
                getZKDocument().invokeWithContext(v8Function,"city",cityName);
                popMenu.dismiss();
            }
        });

        buttomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popMenu.dismiss();
            }
        });
    }


    /**
     * 排序
     * @param contentView
     */
    private int orderPosition = 1;
    private void orderPopMenu(View contentView){
        ListView lvOrder = contentView.findViewById(R.id.lv_order);
        buttomView = contentView.findViewById(R.id.view);
        List<String> list = new ArrayList<>();
        list.add("信用度高优先");
        list.add("价格从高到低");
        list.add("最新发布");
        list.add("距离最近");
        OrderListViewAdapter orderListViewAdapter = new OrderListViewAdapter(list, getContext());
        orderListViewAdapter.setSeclection(orderPosition);
        orderListViewAdapter.notifyDataSetChanged();
        lvOrder.setAdapter(orderListViewAdapter);
        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                orderPosition = position;
                orderListViewAdapter.setSeclection(orderPosition);
                orderListViewAdapter.notifyDataSetChanged();
                getZKDocument().invokeWithContext(v8Function,"order",position);
                popMenu.dismiss();
            }
        });

        buttomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popMenu.dismiss();
            }
        });
    }

    /**
     * 筛选
     */
    private int pricePosition = 0;
    private int creditPosition = 0;
    private void sievePopMenu(View contentView){
        GridView gvPrice = contentView.findViewById(R.id.gv_sieve_price);
        GridView gvCredit = contentView.findViewById(R.id.gv_sieve_credit);
        Button btnConfirm = contentView.findViewById(R.id.btn_sieve_confirm);
        Button btnReset = contentView.findViewById(R.id.btn_sieve_reset);

        List<SieveListBean> l1 = new ArrayList<>();
        l1.add(new SieveListBean("全部"));
        l1.add(new SieveListBean("5-20"));
        l1.add(new SieveListBean("20-50"));
        l1.add(new SieveListBean("50-100"));
        l1.add(new SieveListBean("100元以上"));
        List<SieveListBean> l2 = new ArrayList<>();
        l2.add(new SieveListBean("全部"));
        l2.add(new SieveListBean("3-4"));
        l2.add(new SieveListBean("4-5"));
        l2.add(new SieveListBean("3分以下"));
        l2.add(new SieveListBean("3分以上"));
        SieveItemAdapter gvAdapter1 = new SieveItemAdapter(l1, getContext());
        SieveItemAdapter gvAdapter2 = new SieveItemAdapter(l2, getContext());
        gvAdapter1.setSeclection(pricePosition);
        gvAdapter2.setSeclection(creditPosition);
        gvAdapter1.notifyDataSetChanged();
        gvAdapter2.notifyDataSetChanged();
        gvPrice.setAdapter(gvAdapter1);
        gvCredit.setAdapter(gvAdapter2);

        gvPrice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gvAdapter1.setSeclection(position);
                gvAdapter1.notifyDataSetChanged();
                pricePosition = position;
            }
        });
        gvCredit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gvAdapter2.setSeclection(position);
                gvAdapter2.notifyDataSetChanged();
                creditPosition = position;
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gvAdapter1.setSeclection(pricePosition);
                gvAdapter1.notifyDataSetChanged();
                gvAdapter2.setSeclection(creditPosition);
                gvAdapter2.notifyDataSetChanged();
                getZKDocument().invokeWithContext(v8Function,"reward",pricePosition);
                getZKDocument().invokeWithContext(v8Function,"credit",creditPosition);
                popMenu.dismiss();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pricePosition = 0;
                creditPosition = 0;
                gvAdapter1.setSeclection(pricePosition);
                gvAdapter1.notifyDataSetChanged();
                gvAdapter2.setSeclection(creditPosition);
                gvAdapter2.notifyDataSetChanged();
                /*getZKDocument().invokeWithContext(v8Function,"reward",0);
                getZKDocument().invokeWithContext(v8Function,"credit",0);
                popMenu.dismiss();*/
            }
        });

        buttomView = contentView.findViewById(R.id.view);
        buttomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popMenu.dismiss();
            }
        });
    }

    private V8Function v8Function;
    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            switch (attribute.getName().toLowerCase()) {
                case "dialogclick":
                    v8Function = getZKDocument().genContextFn(attribute.getValue());
                    break;
            }
        }

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
                V8Array array = parameters.getArray(0);
                String s = V8Utils.js2string(array);
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    getTypeData(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        },"setScreen");

    }

    @Override
    public void onClick(View v) {
        View contentView;
        switch (v.getId()) {
            case R.id.type:
                typeName.setTextColor(Color.parseColor("#39ac69"));
                typeIv.setBackgroundResource(R.drawable.up_arrow);
                contentView = View.inflate(getContext(),R.layout.popwin_type_list,null);
                initPopMenu(contentView);
                typePopMenu(contentView);
                menuIndex = 0;
                break;
            case R.id.city:
                cityName.setTextColor(Color.parseColor("#39ac69"));
                cityIv.setBackgroundResource(R.drawable.up_arrow);
                contentView = View.inflate(getContext(), R.layout.popwin_city_list,null);
                initPopMenu(contentView);
                cityPopMenu(contentView);
                menuIndex = 1;
                break;
            case R.id.order:
                orderName.setTextColor(Color.parseColor("#39ac69"));
                orderIv.setBackgroundResource(R.drawable.up_arrow);
                contentView = View.inflate(getContext(), R.layout.popwin_order_list,null);
                initPopMenu(contentView);
                orderPopMenu(contentView);
                menuIndex = 2;
                break;
            case R.id.screen:
                screenName.setTextColor(Color.parseColor("#39ac69"));
                screenIv.setBackgroundResource(R.drawable.up_arrow);
                contentView = View.inflate(getContext(),R.layout.popwin_sieve_list,null);
                initPopMenu(contentView);
                sievePopMenu(contentView);
                menuIndex = 3;
                break;
        }
        popMenu.showAsDropDown(llBody, 0, 2);
    }
}
