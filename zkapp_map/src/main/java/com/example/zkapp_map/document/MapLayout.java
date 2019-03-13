package com.example.zkapp_map.document;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.example.zkapp_map.BaiduMapLocationUtils;
import com.example.zkapp_map.R;
import com.example.zkapp_map.adapter.AdapterMapTask2;
import com.example.zkapp_map.bean.TaskBaseBean;
import com.example.zkapp_map.dialog.SortDialog;
import com.example.zkapp_map.hkdialog.dialog.MyDialog;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.GsonUtils;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppDocument;
import com.zankong.tool.zkapp.v8fn.fetch_pck.ServerAgent;

import org.dom4j.Element;
import org.json.JSONArray;
import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
@ZKAppDocument("maplayout")
public class MapLayout extends ZKDocument {
    private GestureDetector mGestureDetector;
    private RecyclerView rv;
    private RelativeLayout design_bottom_sheet;
    private static RelativeLayout tv1;
    private CoordinatorLayout coordinatorLayout;
    public boolean hasRequest;
    private MyHandler mHandler;
    private BottomSheetBehavior<RelativeLayout> behavior;
    private AppBarLayout appBar;
    private static ArrayList<V8Object> list;
    private TaskBaseBean taskBaseBean;
    private TaskBaseBean.ScreenBean screenBean;
    private TaskBaseBean.ScreenBean.RangeBean rangeBean;
    private int page = 0;
    private MyLocationData.Builder mBuilder;
    private static AdapterMapTask2 adapterMapTask;
    private MapView mBaiDuMap;
    private static BaiduMap map;
    private MyDialog myDialog;
    private BaiduMapLocationUtils instance;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BaiduMap.OnMapTouchListener mMapTouchListener;
    private BaiduMap.OnMapClickListener mMapClickListener;
    private TextView tv_location;
    private GeoCoder mSearch;
    private static BitmapDescriptor bitmap;
    private LinearLayout top_right_layout;
    private JSONArray jsonArray;

    public MapLayout(Context context, Element root, Window window) {
        super(context, root, window);
    }
    public MapLayout(Context context, Element root, ViewGroup viewGroup) {
        super(context, root, viewGroup);
    }
    @Override
    protected void initView() {
        setContextView(R.layout.new_maplayout_layout);
        mBaiDuMap = (MapView) findViewById(R.id.map_view);
        mHandler = new MyHandler(((ZKActivity) getContext()));
        rv = (RecyclerView) findViewById(R.id.rv);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coord_layout);
        tv1 = (RelativeLayout) findViewById(R.id.top_layout);
        appBar = (AppBarLayout) findViewById(R.id.design_bottom_sheet_bar);
        tv_location = (TextView) findViewById(R.id.tv_location);
        design_bottom_sheet = (RelativeLayout) findViewById(R.id.design_bottom_sheet);
        behavior = BottomSheetBehavior.from(design_bottom_sheet);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        top_right_layout = (LinearLayout) findViewById(R.id.top_right_layout);
        
        top_right_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myDialog == null){
                    myDialog = new MyDialog(jsonArray);
                }
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                myDialog.show(getFragmentManager(),"tag");
                myDialog.onClickTileLayout(new MyDialog.OnConfirmAndErr() {
                    @Override
                    public void onPosition(int position) {
                        if (position == 0){
                            myDialog.dismiss();
                        }else if (position == 1){
                            myDialog.dismiss();
                        }
                    }
                    @Override
                    public void getData(TaskBaseBean bean) {
                        int order = bean.getOrder();
                        int reward = bean.getScreen().getReward();
                        int credit = bean.getScreen().getCredit();
                        Log.e("data","order"+order+"--reward"+reward+"--credit"+credit);
                        TaskBaseBean taskBaseBean = onNewBean(0, order, 0, 0, reward, credit, 3000, 0, 0);
                        onDataNet(taskBaseBean,0);
                    }
                });
            }
        });
    }

    @Override
    protected void fillData(Element docElement) {
        initRecyclerView();
        initMap();

        setXml(docElement,coordinatorLayout);
    }
    private static class MyHandler extends Handler {
        WeakReference<ZKActivity> activityWeakReference;
        private MyHandler(ZKActivity activity) {
            activityWeakReference = new WeakReference<ZKActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ZKActivity zkActivity = activityWeakReference.get();
            if (zkActivity != null) {
                switch (msg.what) {
                    case 0:
                        Object obj = msg.obj;
                        String s = obj.toString();
                        list.clear();
                        V8Array array = V8Utils.parseArray(s);
                        for (int i = 0; i < array.length(); i++) {
                            list.add(array.getObject(i));
                        }
                        onMarkerTask(list);
                        adapterMapTask.notifyDataSetChanged();
                        break;
                    case 1:
                        Object obj2 = msg.obj;
                        String s2 = obj2.toString();
                        V8Array array2 = V8Utils.parseArray(s2);
                        for (int i = 0; i < array2.length(); i++) {
                            list.add(array2.getObject(i));
                        }
                        onMarkerTask(list);
                        if (array2.length() == 0) {
                            adapterMapTask.setLoadState(adapterMapTask.LOADING_END);
                            //  adapterMapTask.addFooter(tv1, "没有更多了");
                        } else {
                            adapterMapTask.setLoadState(adapterMapTask.LOADING_COMPLETE);
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) mHandler.removeCallbacksAndMessages(null);
    }

    private void onDataNet(TaskBaseBean baseBean, int what) {
        ServerAgent.invoke("Task-search", GsonUtils.toJson(baseBean)).then(res -> {
            Message message = new Message();
            switch (what) {
                case 0:
                    message.what = 0;
                    break;
                case 1:
                    message.what = 1;
                    break;
            }
            message.obj = res;
            mHandler.sendMessage(message);
            return null;
        });
    }

    private TaskBaseBean onNewBean(int page, int order, int type, int style, int reward, int credit, int r, double lat, double lng) {
        if (taskBaseBean == null)
            taskBaseBean = new TaskBaseBean();
        taskBaseBean.setPage(page);
        taskBaseBean.setLimit(40);
        taskBaseBean.setOrder(order);
        if (screenBean == null)
            screenBean = new TaskBaseBean.ScreenBean();
        screenBean.setReward(reward);
        screenBean.setCredit(credit);
        screenBean.setStyle(style);
        screenBean.setType(type);
        if (rangeBean == null)
            rangeBean = new TaskBaseBean.ScreenBean.RangeBean();
        rangeBean.setLat(lat);
        rangeBean.setLng(lat);
        rangeBean.setR(r);
        screenBean.setRange(rangeBean);
        taskBaseBean.setScreen(screenBean);
        return taskBaseBean;
    }

    private void onScrollListener() {
        rv.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                adapterMapTask.setLoadState(adapterMapTask.LOADING);
                page++;
                TaskBaseBean taskBaseBean = onNewBean(page, 0, 0, 0, 0, 0, 3000, 0, 0);
                onDataNet(taskBaseBean, 1);
            }
        });
    }


    private abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
        // 用来标记是否正在向上滑动
        private boolean isSlidingUpward = false;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            // 当不滑动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                // 获取最后一个完全显示的itemPosition
                int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
                int itemCount = manager.getItemCount();

                // 判断是否滑动到了最后一个item，并且是向上滑动
                if (lastItemPosition == (itemCount - 1) && isSlidingUpward) {
                    // 加载更多
                    onLoadMore();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
            isSlidingUpward = dy > 0;
        }

        /**
         * 加载更多回调
         */
        public abstract void onLoadMore();
    }


    private void initMap() {
        map = mBaiDuMap.getMap();
        map.setMyLocationEnabled(true);
        mBaiDuMap.showScaleControl(false);
        mBaiDuMap.removeViewAt(1);
        instance = BaiduMapLocationUtils.getInstance(getContext());
        instance.onStartLocation();
        getLocation();
        onMapTouchListener();
        map.setOnMapTouchListener(mMapTouchListener);
        map.setOnMapClickListener(mMapClickListener);
        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
        map.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
        getCenterPoint();
        OnMarkerClickListener();
    }

    /**
     * @author Fsnzzz
     * @time 2018/9/25 0025  22:14
     * @describe 地图定位监听回调
     */
    public void getLocation() {
        instance.setOnLocation(new BaiduMapLocationUtils.OnLocation() {
            @Override
            public void location(double latitude, double longitude, BDLocation location) {
                if (mBuilder == null) {
                    mBuilder = new MyLocationData.Builder();
                    MyLocationData locData = mBuilder
                            .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                            .direction(location.getDirection())
                            .latitude(location.getLatitude())
                            .longitude(location.getLongitude()).build();
                    map.setMyLocationData(locData);
                }
                String address = location.getAddrStr();
                tv_location.setText(address);
                setCenterPoint(latitude, longitude, location);
                instance.onStop();
            }
        });
    }

    private void setCenterPoint(double latitude, double longitude, BDLocation location) {
        MapStatusUpdate mapstatusupdate = MapStatusUpdateFactory.newLatLng(new LatLng(latitude, longitude));
        //配置定位图层显示方式(显示模式（普通，罗盘，跟随）)
        // mode - 定位图层显示方式, 默认为 LocationMode.NORMAL 普通态
        //enableDirection - 是否允许显示方向信息
        //customMarker - 设置用户自定义定位图标，可以为 null
        // 显示个人位置图标
        MapStatus.Builder mapStatusBuilder = new MapStatus.Builder();
        LatLng ll = new LatLng(latitude, longitude);
        mapStatusBuilder.target(ll);
        mapStatusBuilder.zoom(18.0f);
        map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatusBuilder.build()));

        map.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null));
        TaskBaseBean taskBaseBean = onNewBean(0, 0, 0, 0, 0, 0, 3000, latitude, longitude);
        onDataNet(taskBaseBean, 0);
    }

    private void initRecyclerView() {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.clear();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        //   rv.setPullRefreshEnabled(false);
        //   rv.setNoMore(true);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                String state = "null";
                switch (newState) {
                    case 1:
                        state = "STATE_DRAGGING";//过渡状态此时用户正在向上或者向下拖动bottom sheet
                    case 2:
                        state = "STATE_SETTLING"; // 视图从脱离手指自由滑动到最终停下的这一小段时间
                        if (bottomSheet.getTop() > coordinatorLayout.getHeight() - (coordinatorLayout.getHeight() / 4) && behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        } else if (bottomSheet.getTop() < coordinatorLayout.getHeight() - (coordinatorLayout.getHeight() / 4) && behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        } else if (bottomSheet.getTop() > coordinatorLayout.getHeight() / 6 && behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        } else if (bottomSheet.getTop() < coordinatorLayout.getHeight() / 6 && behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                        break;
                    case 3:
                        state = "STATE_EXPANDED"; //处于完全展开的状态
                        break;
                    case 4:
                        state = "STATE_COLLAPSED"; //默认的折叠状态
                        break;
                    case 5:
                        state = "STATE_HIDDEN"; //下滑动完全隐藏 bottom sheet
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.d("BottomSheetDemo", "slideOffset:" + slideOffset);
//                   appBar.setAlpha(1-slideOffset);
            }
        });
        list.clear();
        adapterMapTask = new AdapterMapTask2(getContext(), list);
        rv.setAdapter(adapterMapTask);
        onScrollListener();
    }

    private void onMapTouchListener() {
        mMapTouchListener = new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        };
        mMapClickListener = new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                map.hideInfoWindow();
            }
            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        };

    }
    /**
     * @author Fsnzzz
     * @time 2018/9/26 0026  17:37
     * @describe 滑动地图的过程，开始、滑动中、滑动完成时的地图中心点位置
     */
    private void getCenterPoint() {
        map.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
//                LatLng target = mapStatus.target;
//                 map.clear();
////                setCenterPoint2(target.latitude,target.longitude,null);
//                latlngToAddress(new LatLng(target.latitude, target.longitude), "上海", "");
//                mLatLng = target;
//                TaskBaseBean taskBaseBean = onNewBean(0, 0, 0, 0, 0, 0, 3000, target.latitude, target.longitude);
//                onDataNet(taskBaseBean, 0);
            }
        });
    }


    /**
     * @author Fsnzzz
     * @time 2018/9/26 0026  1:01
     * @describe 百度地图通过坐标获取地址，（ 要签名打包才能得到地址）
     */
    private void latlngToAddress(LatLng latlng, String city, String address) {
        mSearch = GeoCoder.newInstance();

        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                //把地址转换成经纬度
                if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                } else {
                    double longitude = geoCodeResult.getLocation().longitude;
                    double latitude = geoCodeResult.getLocation().latitude;
                }
                //获取地理编码结果
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                //经纬度转换成地址
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                } else {
                    //获取反向地理编码结果
                    List<PoiInfo> poiList = reverseGeoCodeResult.getPoiList();
                    for (int i = 0; i < poiList.size(); i++) {
                        V8Object object = new V8Object(ZKToolApi.runtime);
                        object.add("address", poiList.get(i).address);
                        object.add("name", poiList.get(i).name);
                        object.add("latitude", poiList.get(i).location.latitude);
                        object.add("longitude", poiList.get(i).location.longitude);
                        // Log.e("aaaaaaaaaaaa4", poiList.get(i).location.latitude+","+poiList.get(i).location.longitude+","+poiList.get(i).name+"1111"+poiList.get(i).address);
                    }
                    if (poiList.size() != 0) {
                        PoiInfo poiInfo = poiList.get(0);
                        String name = poiInfo.name;
                        String address1 = poiInfo.address;
                        tv_location.setText(address1 + name);
                    }
                }
            }
        };
        mSearch.setOnGetGeoCodeResultListener(listener);
        //        mSearch.geocode(new GeoCodeOption()
        //                .city(city)
        //                .address(address)
        //        );
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
        mSearch.destroy();
    }
    private static void onMarkerTask(ArrayList<V8Object> list) {
        for (int i = 0; i < list.size(); i++) {
            V8Object v8Object = list.get(i);
            if (v8Object.contains("addressA")) {
                V8Object coordinate = v8Object.getObject("addressA").getObject("coordinate");
                V8Array coordinates = coordinate.getArray("coordinates");
                double o = (double) coordinates.get(0);
                double o1 = (double) coordinates.get(1);
                LatLng point = new LatLng(o1,o);
                if (v8Object.getString("styleName").equals("帮买")) {
                    //构建Marker图标
                    bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_jineng);
                    //构建MarkerOption，用于在地图上添加Marker
                }else if (v8Object.getString("styleName").equals("帮取帮送")){
                    bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_paotui);
                }else{
                    bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_jineng);
                }
                
                Bundle bundle = new Bundle();
                bundle.putInt("taskId",v8Object.getInteger("taskId"));
                
                OverlayOptions option = new MarkerOptions()
                        .position(point) //必传参数
                        .icon(bitmap) //必传参数
                        .draggable(true)
                        //设置平贴地图，在地图中双指下拉查看效果
                        .flat(true)
                        .extraInfo(bundle)
                        .alpha(0.5f);
                //在地图上添加Marker，并显示
                map.addOverlay(option);
            } else if (v8Object.contains("addressB")) {
                V8Object coordinate = v8Object.getObject("addressB").getObject("coordinate");
                V8Array coordinates = coordinate.getArray("coordinates");
                double o = (double) coordinates.get(0);
                double o1 = (double) coordinates.get(1);
                LatLng point = new LatLng(o1,o);
                if (v8Object.getString("styleName").equals("帮买")) {
                    //构建Marker图标
                    bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_jineng);
                    //构建MarkerOption，用于在地图上添加Marker
                }else if (v8Object.getString("styleName").equals("帮取帮送")){
                    bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_paotui);
                }else{
                    bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_jineng);
                }
                Bundle bundle = new Bundle();
                bundle.putInt("taskId",v8Object.getInteger("taskId"));
                OverlayOptions option = new MarkerOptions()
                        .position(point) //必传参数
                        .icon(bitmap) //必传参数
                        .draggable(true)
                        //设置平贴地图，在地图中双指下拉查看效果
                        .flat(true)
                        .extraInfo(bundle)
                        .alpha(0.5f);
                //在地图上添加Marker，并显示
                map.addOverlay(option);
            }
        }
    }



    /**
     * @author Fsnzzz
     * @time 2018/9/26 0026  0:43
     * @describe 覆盖物的点击事件
     */
    private void OnMarkerClickListener() {

        map.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                onShowDialogMarker(marker);
                return true;
            }
        });

    }


    @Override
    protected void initV8This() {
        super.initV8This();
        document.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                try {
                    jsonArray = new JSONArray(V8Utils.js2string(parameters.getArray(0)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        },"setSearchList");
    }

    /**
     * @author Fsnzzz
     * @time 2018/10/9 0009  16:41
     * @describe 点击显示dialog
     */
    private void onShowDialogMarker(Marker marker) {
        map.hideInfoWindow();
        Bundle extraInfo = marker.getExtraInfo();
        int taskId = extraInfo.getInt("taskId");
        for (int i = 0; i < list.size();i++){
            V8Object v8Object = list.get(i);
            if (v8Object.getInteger("taskId") == taskId){
                Log.e("hhhhhhhhhhh",v8Object.getInteger("taskId")+"==="+taskId);
            }
        }
        View view = LayoutInflater.from(getContext()).inflate(R.layout.show_overlay_info, null);
        TextView tv = view.findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.hideInfoWindow();
            }
        });
        double height = onViewHeight(view);
//
//        TextView name = view.findViewById(R.id.name);
//        TextView latidue = view.findViewById(R.id.latidue);
//        ImageView del_icon = (ImageView) view.findViewById(R.id.del_icon);
//        TextView longdue = view.findViewById(R.id.longdue);
//        TextView btn = view.findViewById(R.id.btn);
//        LatLng ll = marker.getPosition();

        InfoWindow infoWin = new InfoWindow(view, marker.getPosition(), (int)height/2);

        map.showInfoWindow(infoWin);
      
    }
    private double onViewHeight(View view){
        view.measure( 0,  0);
        return view.getMeasuredHeight();
    }
    
}
