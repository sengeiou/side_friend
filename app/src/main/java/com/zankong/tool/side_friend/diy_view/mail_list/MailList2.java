package com.zankong.tool.side_friend.diy_view.mail_list;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.side_friend.diy_view.mail_list.adapter.ContactAdapter;
import com.zankong.tool.side_friend.diy_view.mail_list.adapter.ContactAdapter2;
import com.zankong.tool.side_friend.diy_view.mail_list.cn.CNPinyin;
import com.zankong.tool.side_friend.diy_view.mail_list.cn.CNPinyinFactory;
import com.zankong.tool.side_friend.diy_view.mail_list.search.CharIndexView;
import com.zankong.tool.side_friend.diy_view.mail_list.search.Contact;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Fsnzzz
 * @Created on 2018/11/19 0019 19:33
 */
public class MailList2 extends ZKViewAgent {
    private RecyclerView rv_main;
    private CharIndexView iv_main;
    private TextView tv_index;
    private ArrayList<CNPinyin<Contact>> contactList = new ArrayList<>();
    private Subscription subscription;
    private HashMap<String, V8Object> mChildList;
    private ContactAdapter2 adapter;
    private List<Contact> contacts = new ArrayList<>();
    private RelativeLayout bar_layout;
    private LinearLayout content_layout;
    private TextView pangyou_number;
    private ImageView img_finish;
    private V8Function onClickListener, onFinish;
    private ZKDocument mZKDocument;
    private V8Array v8Array = new V8Array(ZKToolApi.runtime);

    public MailList2(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        mChildList = new HashMap<>();
        this.mZKDocument = zkDocument;
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.mail_list_activity);
        rv_main = (RecyclerView) findViewById(R.id.rv_main);
        iv_main = (CharIndexView) findViewById(R.id.iv_main);
        tv_index = (TextView) findViewById(R.id.tv_index);
        bar_layout = (RelativeLayout) findViewById(R.id.bar_layout);
        bar_layout.setVisibility(View.VISIBLE);
        content_layout = (LinearLayout) findViewById(R.id.content_layout);
        pangyou_number = (TextView) findViewById(R.id.pangyou_number);
        img_finish = (ImageView) findViewById(R.id.img_finish);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rv_main.setLayoutManager(manager);
        iv_main.setOnCharIndexChangedListener(new CharIndexView.OnCharIndexChangedListener() {
            @Override
            public void onCharIndexChanged(char currentIndex) {
                for (int i = 0; i < contactList.size(); i++) {
                    if (contactList.get(i).getFirstChar() == currentIndex) {
                        manager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }

            @Override
            public void onCharIndexSelected(String currentIndex) {
                if (currentIndex == null) {
                    tv_index.setVisibility(View.INVISIBLE);
                } else {
                    tv_index.setVisibility(View.VISIBLE);
                    tv_index.setText(currentIndex);
                }
            }
        });

        adapter = new ContactAdapter2(contactList, getContext());
        rv_main.setAdapter(adapter);
        onClickItem();


        img_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                V8Object v8Object = new V8Object(ZKToolApi.runtime);
                v8Object.add("finish", true);

                mZKDocument.invokeWithContext(onFinish, v8Object);
            }
        });

        content_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v8Array != null) {
                    mZKDocument.invokeWithContext(onClickListener, v8Array);
                } else {
                    Toast.makeText(getContext(), "您还未添加好友", Toast.LENGTH_SHORT);
                }

            }
        });


    }

    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName().toLowerCase()) {
                case "click":
                    onClickListener = mZKDocument.genContextFn(value);
                    break;
                case "finish":
                    onFinish = mZKDocument.genContextFn(value);
                    break;
            }
        }
    }


    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod((receiver, parameters) -> {
            int index = 1;
            if (parameters.length() >= 2) {
                index = parameters.getInteger(1);
            }
            String name = parameters.getString(0) + index;
            if (mChildList.containsKey(name)) {
                return V8Utils.createFrom(mChildList.get(name));
            }
            return null;
        }, "get");
        thisV8.registerJavaMethod((receiver, parameters) -> {
            V8Array array = parameters.getArray(0);
            for (int i = 0; i < array.length(); i++) {
                V8Object object = array.getObject(i);
                String name = object.getString("nickname");
                String imgUrl = object.getString("img");
                int id = object.getInteger("uid");
                Contact contact = new Contact(name, imgUrl, id);
                contacts.add(contact);
            }

            getPinyinList();

            return null;
        }, "set");

    }

    @Override
    public Object getResult() {
        return v8Array;
    }


    private void getPinyinList() {
        subscription = Observable.create(new Observable.OnSubscribe<List<CNPinyin<Contact>>>() {
            @Override
            public void call(Subscriber<? super List<CNPinyin<Contact>>> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    List<CNPinyin<Contact>> contactList = CNPinyinFactory.createCNPinyinList(contacts);
                    Collections.sort(contactList);
                    subscriber.onNext(contactList);
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CNPinyin<Contact>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<CNPinyin<Contact>> cnPinyins) {
                        contactList.addAll(cnPinyins);
                        adapter.addData(contactList);
                    }
                });
    }


    private void onClickItem() {
        adapter.onItemClickListener(new ContactAdapter2.MailListGroup() {
            @Override
            public void onData(Map<Integer, Object> data) {
                if (data.size() != 0) {
                    v8Array = new V8Array(ZKToolApi.runtime);
                    pangyou_number.setText("(" + data.size() + ")");
                    for (Map.Entry<Integer, Object> entry : data.entrySet()) {
                        V8Object v8Object = new V8Object(ZKToolApi.runtime);
                        Contact contact = (Contact) entry.getValue();
                        v8Object.add("nickname", contact.name);
                        v8Object.add("uid", contact.iD);
                        v8Object.add("img", contact.imgUrl);
                        v8Array.push(v8Object);
                    }
                } else {
                    pangyou_number.setText("(0)");
                    v8Array = new V8Array(ZKToolApi.runtime);
                }
            }
        });
    }
}