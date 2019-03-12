//package com.zk.tool.zkapp_call.old;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//
///**
// * Created by YF on 2018/2/28.
// */
//
//public class NetworkConnectChangedReceiver extends BroadcastReceiver {
//
//    private OnNetworkChangeListener mNetworkChangeListener;
//    public interface OnNetworkChangeListener{
//        void wifi();
//        void mobile();
//        void err();
//    }
//    public void setOnNetworkChangeListener(OnNetworkChangeListener networkChangeListener){
//        this.mNetworkChangeListener = networkChangeListener;
//    }
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
//            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
//            if (activeNetwork != null) { // connected to the internet
//                if (activeNetwork.isConnected()) {
//                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
//                        if (mNetworkChangeListener != null){
//                            mNetworkChangeListener.wifi();
//                        }
//                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
//                        if (mNetworkChangeListener != null){
//                            mNetworkChangeListener.mobile();
//                        }
//                    }
//                }
//            } else {
//                if (mNetworkChangeListener != null){
//                    mNetworkChangeListener.err();
//                }
//            }
//        }
//    }
//}