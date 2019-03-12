package com.example.zkapp_identity;

import android.content.Context;

/**
 * @author Fsnzzz
 * @Created on 2018/10/23 0023 15:03
 */
public class IdentityInit {
    public static void initIdentity(Context context){
        try {
            IdentityInto.class.newInstance().addV8Fn();
            test.set();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

}
