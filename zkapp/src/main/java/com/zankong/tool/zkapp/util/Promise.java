package com.zankong.tool.zkapp.util;

import com.zankong.tool.zkapp.ZKToolApi;

import java.util.ArrayList;

public class Promise {
    public interface Pro{
        void invoke(Resolve resolve,Reject reject);
    }
    public interface Resolve{
        void success(Object res);
    }
    public interface Reject{
        void fault(Object rej);
    }
    public interface Then{
        Object then(Object res);
    }

    private ArrayList<Then> mThens;

    private Resolve mResolve;
    private Reject mReject;
    private Object data;

    public Promise(Pro p){
        mThens = new ArrayList<>();
        mResolve = new Resolve() {
            @Override
            public void success(Object res) {
                ZKToolApi.getInstance().getJsHandler().post(() -> {
                    data = res;
                    for (Then then : mThens) {
                        data = then.then(data);
                    }
                });
            }
        };
        mReject = new Reject() {
            @Override
            public void fault(Object rej) {

            }
        };
        p.invoke(mResolve,mReject);
    }

    public Promise then(Then then){
        try {
            mThens.add(then);
        }catch (Exception e){
            mReject.fault(e);
        }
        return this;
    }


//
//    public static void main(String[] args){
//        new Promise(new Pro() {
//            @Override
//            public void invoke(Resolve resolve, Reject reject) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        System.out.println("aaaaaaaa1");
//                        resolve.success("网路回调结果");
//                        System.out.println("aaaaaaaa2");
//                    }
//                }).start();
//            }
//        }).then(new Then() {
//            @Override
//            public Object then(Object res) {
//
//                System.out.println("aaaaaaaa3");
//                return res;
//            }
//        }).then(new Then() {
//            @Override
//            public Object then(Object res) {
//                System.out.println("aaaaaaaa4");
//                return null;
//            }
//        });
//    }


}
