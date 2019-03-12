package com.example.zkapp_map;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.activity.ZKNaiveActivity;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Fsnzzz
 * @Created on 2018/9/20 0020 14:41
 */
public class MapOne extends ZKViewAgent {
    public static int LOCATION_CODE = 100;
    private MapView mapView;
    private BaiduMap map;
    private BaiduMapLocationUtils instance;
    private Boolean isFirstLoc = true;
    private GeoCoder mSearch;
    private List<Map<String, Object>> datas = new ArrayList<>();
    private V8Array data = new V8Array(ZKToolApi.runtime);
    private ZKDocument mZKDocument;
    private LatLng mLatLng;
    //定位设置
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private Marker marker;
    private V8Function locationwatch, locationdialogclick, locationcurrent;
    private MyLocationData.Builder mBuilder;
    private RelativeLayout layout;
    private ImageView location_icon;

    public MapOne(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        this.mZKDocument = zkDocument;
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.map);
        mapView = (MapView) findViewById(R.id.mapview);
        map = mapView.getMap();
        location_icon = (ImageView) findViewById(R.id.location_icon);
        layout = (RelativeLayout) findViewById(R.id.layout);
        map.setMyLocationEnabled(true);
        //        mBaiduMap.setMyLocationEnabled(true);// 开启定位图层


        onCurrentLocationClick();
        mapView.showScaleControl(false);
        // 设置是否显示缩放控件
        mapView.showZoomControls(true);
        // 删除百度地图LoGo
        mapView.removeViewAt(1);
        //初始化定位
        instance = BaiduMapLocationUtils.getInstance(getContext());
        //开启定位
        instance.onStartLocation();
        //执行定位回调
        getLocation();

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        map.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));


        OnMarkerClickListener();
        getCenterPoint();
        setOnTouchListener();
        ((ZKActivity) getContext()).requestRuntimePermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_NETWORK_STATE}, new ZKNaiveActivity.PermissionListener() {
            @Override
            public void granted() {
            }

            @Override
            public void denied(List<String> deniedList) {

            }
        });
    }

    @Override
    public void fillData(Element selfElement) {
        List<Element> childrenElement = selfElement.elements();
        ViewGroup.LayoutParams layoutParams = mapView.getLayoutParams();
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "height":
                    layoutParams.height = Util.px2px(value);
                    break;
                case "locationwatch":
                    locationwatch = mZKDocument.genContextFn(value);
                    break;
                case "locationdialogclick":
                    locationdialogclick = mZKDocument.genContextFn(value);
                    break;
                case "locationcurrent":
                    locationcurrent = mZKDocument.genContextFn(value);
                    break;


            }
        }
        //   mapView.setLayoutParams(layoutParams);


    }

    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public void onPause() {
        super.onPause();
        instance.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        instance.onStartLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
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
                Log.e("yyyyyyy", latitude + "," + longitude);
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

                setCenterPoint(latitude, longitude, location);
                instance.onStop();
            }
        });
    }


    /**
     * @author Fsnzzz
     * @time 2018/9/25 0025  22:18
     * @describe 设置地图中心点
     */
    //设置中心点
    private void setCenterPoint(double latitude, double longitude, BDLocation locations) {
        if (isFirstLoc) {
            isFirstLoc = false;
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
               map.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null));
        }
    }

    /**
     * @author Fsnzzz
     * @time 2018/9/25 0025  22:18
     * @describe 设置地图中心点
     */
    //设置中心点
    private void setCenterPoint2(double latitude, double longitude, BDLocation locations) {

        MapStatusUpdate mapstatusupdate = MapStatusUpdateFactory.newLatLng(new LatLng(latitude, longitude));
        //对地图的中心点进行更新，
        //mBaiduMap.setMapStatus(mapstatusupdate);
        setMarker(latitude, longitude, R.drawable.icon_marka, null);
        //drawCircle(latitude, longitude);
        MapStatus.Builder builder = new MapStatus.Builder();
        //设置缩放中心点；缩放比例；
        LatLng ll = new LatLng(latitude, longitude);
        builder.target(ll).zoom(19);
        //给地图设置状态
        //配置定位图层显示方式(显示模式（普通，罗盘，跟随）)
        // mode - 定位图层显示方式, 默认为 LocationMode.NORMAL 普通态
        //enableDirection - 是否允许显示方向信息
        //customMarker - 设置用户自定义定位图标，可以为 null
        map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        map.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS, true, null));
    }


    /**
     * @author Fsnzzz
     * @time 2018/9/26 0026  0:23
     * @describe 自定义覆盖物图标
     */
    //显示坐标位置
    private void setMarker(double latitude, double longitude, int drawable, String name) {
        //定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(drawable);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap)
                .title(name);
        Marker marker = (Marker) map.addOverlay(option);
        Bundle bundle = new Bundle();
        bundle.putString("city", name);
        marker.setExtraInfo(bundle);
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

    /**
     * @author Fsnzzz
     * @time 2018/9/26 0026  0:43
     * @describe 地图点击事件
     */
    private void OnMapClickListener() {

        map.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                String name = mapPoi.getName();
                LatLng position = mapPoi.getPosition();
                double latitude = position.latitude;
                double longitude = position.longitude;
                //getOnClickListenerPoi(position);
                //  latlngToAddress(position,"","");
                return false;
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
                    data.release();
                    data = null;
                    data = new V8Array(ZKToolApi.runtime);
                    for (int i = 0; i < poiList.size(); i++) {
                        V8Object object = new V8Object(ZKToolApi.runtime);
                        object.add("address", poiList.get(i).address);
                        object.add("name", poiList.get(i).name);
                        object.add("latitude", poiList.get(i).location.latitude);
                        object.add("longitude", poiList.get(i).location.longitude);
                        data.push(object);
                        // datas.add(map);
                        //                        Log.e("aaaaaaaaaaaa4", poiList.get(i).location.latitude+","+poiList.get(i).location.longitude+","+poiList.get(i).name+"1111"+poiList.get(i).address);
                    }
                    mZKDocument.invokeWithContext(locationwatch, data);

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
                LatLng target = mapStatus.target;
                // map.clear();
                //setCenterPoint2(target.latitude,target.longitude,null);
                latlngToAddress(new LatLng(target.latitude, target.longitude), "上海", "");

                mLatLng = target;

                Log.e("wwwwwww1", target.latitude + "," + target.longitude);
            }
        });
    }


    /**
     * @author Fsnzzz
     * @time 2018/9/26 0026  23:43
     * @describe 任意点击地图获取信息、覆盖物表示（只显示覆盖物）
     */

    private void getOnClickListenerPoi(LatLng latLng) {
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_markj);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .icon(bitmap)
                .title("");
        if (marker == null) {
            marker = (Marker) map.addOverlay(option);
        } else {
            marker.remove();
            marker = (Marker) map.addOverlay(option);

        }
        Bundle bundle = new Bundle();
        bundle.putString("city", "");
        marker.setExtraInfo(bundle);
    }


    /**
     * @author Fsnzzz
     * @time 2018/9/27 0027  10:11
     * @describe 滑动冲突
     */
    private void setOnTouchListener() {
        mapView.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (getZKDocument().getScrollView() != null) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        getZKDocument().getScrollView().requestDisallowInterceptTouchEvent(false);
                    } else {
                        getZKDocument().getScrollView().requestDisallowInterceptTouchEvent(true);
                    }
                } else if (getZKDocument().getRecycleView() != null) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        getZKDocument().getRecycleView().requestDisallowInterceptTouchEvent(false);
                    } else {
                        getZKDocument().getRecycleView().requestDisallowInterceptTouchEvent(true);
                    }
                }
                return false;
            }
        });
    }

    /**
     * @author Fsnzzz
     * @time 2018/9/27 0027  11:00
     * @describe density(屏幕宽 、 高转换)
     */
    //    private int onBecomeDensity(String value){
    //        return  (int) (Integer.parseInt(value) * density);
    //    }
    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                V8Array array = parameters.getArray(0);
                for (int i = 0; i < array.length(); i++) {
                    V8Object object = array.getObject(i);
                    setMarker(object.getDouble("latitude"), object.getDouble("longitude"), R.drawable.icon_markb, "");
                    Util.log("AAAAAAAA", object.getDouble("latitude") + "," + object.getDouble("longitude"));
                }

                return null;
            }
        }, "setList");
    }

    /**
     * @author Fsnzzz
     * @time 2018/10/9 0009  16:41
     * @describe 点击显示dialog
     */
    private void onShowDialogMarker(Marker marker) {
        map.hideInfoWindow();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.show_overlay_info, null);

        double height = onViewHeight(view);

//
//        TextView name = view.findViewById(R.id.name);
//        TextView latidue = view.findViewById(R.id.latidue);
//        ImageView del_icon = (ImageView) view.findViewById(R.id.del_icon);
//        TextView longdue = view.findViewById(R.id.longdue);
//        TextView btn = view.findViewById(R.id.btn);
//        LatLng ll = marker.getPosition();
        final InfoWindow infoWin = new InfoWindow(view, mLatLng, (int)height/2);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                V8Object object = new V8Object(ZKToolApi.runtime);
//                object.add("text", "dddddddd");
//                mZKDocument.invokeWithContext(locationdialogclick, object);
//                map.hideInfoWindow();
//            }
//        });


        map.showInfoWindow(infoWin);

        // getOnClickListenerPoi(latLng);
        // latlngToAddress(latLng,"","");
        //                setMarke(latitude,longitude,R.drawable.icon_markb,"");
    }

    @Override
    public void release() {
        if (locationwatch != null)
            locationwatch.release();
        if (locationdialogclick != null)
            locationdialogclick.release();
        if (location_icon != null)
            locationcurrent.release();
    }


    /**
     * @author Fsnzzz
     * @time 2018/11/8 0008  13:52
     * @describe 回到当前位置
     */

    private void onCurrentLocationClick() {
        location_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirstLoc = true;
                instance.onStartLocation();
                getLocation();

                //    mZKDocument.invokeWithContext(locationcurrent,object);
            }
        });
    }



    private void showDialog(){

    }

    private double onViewWidth(View view){
        view.measure( 0,  0);

        Log.e("wwwwww",view.getMeasuredWidth()+"");
        return view.getMeasuredWidth();
    }
    private double onViewHeight(View view){
        view.measure( 0,  0);
        Log.e("hhhhhhh",view.getMeasuredHeight()+"");
        return view.getMeasuredHeight();
    }
}
