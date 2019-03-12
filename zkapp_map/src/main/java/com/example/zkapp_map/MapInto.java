package com.example.zkapp_map;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppV8;
import com.zankong.tool.zkapp.zk_interface.ZKV8Fn;

import java.util.List;

/**
 * @author Fsnzzz
 * @Created on 2018/10/11 0011 17:56
 */
@ZKAppV8
public class MapInto implements ZKV8Fn {
    private V8Object baiDuMap;
    private Context mContext;
    private BaiduMapLocationUtils instance;
    private GeoCoder mSearch;
    private String a;
    private V8Array data = new V8Array(ZKToolApi.runtime);
    private SuggestionSearch mSuggestionSearch;

    private V8Array datas = new V8Array(ZKToolApi.runtime);
    private PoiSearch mPoiSearch;

    public MapInto() {
        mContext = ZKToolApi.getInstance().getContext();
        baiDuMap = new V8Object(ZKToolApi.runtime);
    }
    @Override
    public void addV8Fn() {
        baiDuMap.registerJavaMethod((receiver, parameters) -> {
            return V8Utils.Promise(new V8Utils.promiseHandler() {
                @Override
                public void procedure(V8Function resolve, V8Function reject) {
                    instance = BaiduMapLocationUtils.getInstance(mContext);
                    if (instance != null){
                        instance.onStartLocation();
                        try{
                            instance.setOnJSLocation(new BaiduMapLocationUtils.OnJsLocation() {
                                @Override
                                public void location(double latitude, double longitude, BDLocation location) {
                                    V8Object object = new V8Object(ZKToolApi.runtime);
                                    object.add("address",location.getAddrStr());
                                    object.add("province",location.getProvince());
                                    object.add("name",location.getPoiList().get(0).getName());
                                    object.add("city",location.getCity());
                                    object.add("latitude",latitude);
                                    object.add("longitude",longitude);
                                    resolve.call(null,object);
                                    object.release();
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });
        },"startLocation");
        baiDuMap.registerJavaMethod((receiver, parameters) -> {
            return V8Utils.Promise(new V8Utils.promiseHandler() {
                @Override
                public void procedure(V8Function resolve, V8Function reject) {
                    instance = BaiduMapLocationUtils.getInstance(mContext);
                    String address = parameters.getString(1);
                    String city = parameters.getString(0);
                    if (address == null){
                        address = "人才大厦";
                    }
                    if (city == null){
                        city = "上海市";
                    }
                    if (address != null){
                        onGetSuggestionResult(city,address, new AddressListener() {
                            @Override
                            public void success(Object ...o) {
                                resolve.call(null,o);
                            }

                            @Override
                            public void fause(Object o) {

                            }
                        });
                    }

                }
            });
        },"getData");
        baiDuMap.registerJavaMethod((receiver, parameters) -> {
            instance = BaiduMapLocationUtils.getInstance(mContext);
            if (instance != null){
                instance.onStop();
            }
            return true;
        },"stopLocation");
        baiDuMap.registerJavaMethod((receiver, parameters) -> {
            //初始化定位
            instance = BaiduMapLocationUtils.getInstance(mContext);
            return true;
        },"initLocation");

        ZKToolApi.runtime.add("BaiDuMap",baiDuMap);
    }

    private interface AddressListener{
        void success(Object ...o);
        void fause(Object o);
    }





    private void latlngToAddress(LatLng latlng, String city, String address) {
        mSearch = GeoCoder.newInstance();

        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                //把地址转换成经纬度
                if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                    Log.e("aaaaaaaaaa1", "没有检索到结果");
                } else {
                    double longitude = geoCodeResult.getLocation().longitude;
                    double latitude = geoCodeResult.getLocation().latitude;
                    Log.e("aaaaaaaaaa2", latitude + "," + longitude);
                    a = latitude+"";


//
//
//                    data.release();
//                    data = null;
//                    data = new V8Array(ZKToolApi.runtime);
//                    for (int i = 0; i < poiList.size(); i++) {
//                        V8Object object = new V8Object(ZKToolApi.runtime);
//                        object.add("address",poiList.get(i).address);
//                        object.add("name",poiList.get(i).name);
//                        object.add("latitude",poiList.get(i).location.latitude);
//                        object.add("longitude",poiList.get(i).location.longitude);
//                        data.push(object);
//                        // datas.add(map);
//                        //                        Log.e("aaaaaaaaaaaa4", poiList.get(i).location.latitude+","+poiList.get(i).location.longitude+","+poiList.get(i).name+"1111"+poiList.get(i).address);
//                    }
                }
                //获取地理编码结果
            }
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                //经纬度转换成地址
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                    Log.e("aaaaaaaaaa3", "没有检索到结果");
                } else {

                }

            }
        };
        mSearch.setOnGetGeoCodeResultListener(listener);
                mSearch.geocode(new GeoCodeOption()
                        .city(city)
                        .address(address)
                );
       // mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));

        mSearch.destroy();
    }

    /**
     *  @author Fsnzzz
     *  @time 2018/10/12 0012  11:26
     *  @describe 检索提示
     */
    private void onGetSuggestionResult(String city,String address,AddressListener listeners){
        if (city == null){
            city = "上海市";
        }
        mSuggestionSearch = SuggestionSearch.newInstance();
        OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
            public void onGetSuggestionResult(SuggestionResult res) {
                if (res == null || res.getAllSuggestions() == null) {

                    return;
                    //未找到相关结果
                }
                //获取在线建议检索结果


                List<SuggestionResult.SuggestionInfo> allSuggestions = res.getAllSuggestions();
                data.release();
                data = null;
                data = new V8Array(ZKToolApi.runtime);
                for (int i = 1;i <allSuggestions.size();i++){
                    V8Object object = new V8Object(ZKToolApi.runtime);
                    object.add("address",allSuggestions.get(i).city+allSuggestions.get(i).district+allSuggestions.get(i).address+allSuggestions.get(i).key);
                    object.add("name",allSuggestions.get(i).key);
                    object.add("city",allSuggestions.get(i).city);
                    object.add("latitude",allSuggestions.get(i).pt.latitude);
                    object.add("longitude",allSuggestions.get(i).pt.longitude);
                    data.push(object);
                }


                listeners.success(data);



            }
        };
        mSuggestionSearch.setOnGetSuggestionResultListener(listener);
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                .citylimit(true)
                .keyword(address)
                .city(city));
        mSuggestionSearch.destroy();
    }


    /**
     *  @author Fsnzzz
     *  @time 2018/10/12 0012  11:26
     *  @describe 城市内检索
     */


    private void onGetPoiSearchResult(){

        mPoiSearch = PoiSearch.newInstance();
        OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){
            public void onGetPoiResult(PoiResult result){
                //获取POI检索结果
                List<PoiInfo> allPoi = result.getAllPoi();
                for (PoiInfo p: allPoi) {
                    Log.d("tttttttt", "p.name--->" + p.name +"p.phoneNum"+" -->p.address:" + p.address);
                }


            }

            public void onGetPoiDetailResult(PoiDetailResult result){
                //获取Place详情页检索结果




            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {


            }
        };


        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city("上海市")
                .keyword("斜土路人才大厦")
                .pageNum(10));
        mPoiSearch.destroy();
    }
}
