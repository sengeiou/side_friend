package com.zankong.tool.side_friend.div_item;

import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Object;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.views.ZKImgView;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by YF on 2018/9/17.
 */
@ZKAppAdapter("task_release")
public class Task_Release extends ZKAdapter {
    private final int STATUS_RELEASED = 0;
    private final int STATUS_RUNNING = 1;
    private final int STATUS_SERVICED = 2;
    private final int STATUS_FINISH = 3;

    private int reclen = 90;
    private HashMap<String, Integer> mStatus = new HashMap<String, Integer>() {{
        put("released", STATUS_RELEASED);
        put("running", STATUS_RUNNING);
        put("serviced", STATUS_SERVICED);
        put("finish", STATUS_FINISH);
    }};
    private HashMap<String, String> mClickMap = new HashMap<>();

    public Task_Release(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
        for (Attribute attribute : element.element("task_release").attributes()) {
            mClickMap.put(attribute.getName(), attribute.getValue());
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder holder = null;
        switch (i) {
            case STATUS_RELEASED:
                holder = new ReleasedHolder(mLayoutInflater.inflate(R.layout.item_task_release_released, viewGroup, false));
                break;
            case STATUS_RUNNING:
                holder = new RunningHolder(mLayoutInflater.inflate(R.layout.item_task_release_running, viewGroup, false));
                break;
            case STATUS_SERVICED:
                holder = new ServicedHolder(mLayoutInflater.inflate(R.layout.item_task_release_serviced, viewGroup, false));
                break;
            case STATUS_FINISH:
                holder = new FinishHolder(mLayoutInflater.inflate(R.layout.item_task_release_finish, viewGroup, false));
                break;
        }
        return holder;
    }

    @Override
    protected int getViewType(int position) {
        return mStatus.get(mList.get(position).getString("status"));
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof HolderInit) {
            ((HolderInit) viewHolder).init(position);
        }
    }

    private class ReleasedHolder extends RecyclerView.ViewHolder implements HolderInit {
        private Button receiver, pay;
        private ImageView img;
        private RelativeLayout cancelLayout;
        private TextView name, type, createTime, outTime, reward, change, cancel;
        private final RelativeLayout changeLayout;
        private Handler handler;

        public ReleasedHolder(@NonNull View itemView) {
            super(itemView);
            change = itemView.findViewById(R.id.change);
            cancel = itemView.findViewById(R.id.cancel);
            receiver = itemView.findViewById(R.id.receiver);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            changeLayout = itemView.findViewById(R.id.changeLayout);
            cancelLayout = itemView.findViewById(R.id.cancelLayout);
            type = itemView.findViewById(R.id.type);
            createTime = itemView.findViewById(R.id.createTime);
            outTime = itemView.findViewById(R.id.outTime);
            reward = itemView.findViewById(R.id.reward);
            pay = itemView.findViewById(R.id.pay);
        }

        @Override
        public void init(int position) {
            V8Object v8Object = mList.get(position);
            for (String key : v8Object.getKeys()) {
                switch (key) {
                    case "receiverNum":
                        receiver.setText(v8Object.getInteger(key) + "人接单中");
                        break;
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(v8Object.getString("img"))).into(img);
                        break;
                    case "nickname":
                        name.setText(v8Object.getString(key));
                        break;
                    case "taskType":
                    case "taskStyle":
                        type.setText("任务类型：" + v8Object.get("taskStyle") + "(" + v8Object.get("taskType") + ")");
                        break;
                    case "reward":
                        reward.setText("服务费：" + v8Object.get(key));
                        break;
                    case "outTime":
                        outTime.setText("任务时限：" + v8Object.getString(key) + "小时");
                        break;
                    case "createTime":
                        createTime.setText("下单时间：" + v8Object.get(key));
                        break;
                    case "receiverTime":
                        long time = onOutTime(v8Object.getString(key));
                        break;
                    case "isAdvancePay":
                        if (v8Object.getInteger(key) == 1) {
                            if (v8Object.getInteger("isPay") == 1) {
                                pay.setVisibility(View.GONE);
                                receiver.setVisibility(View.GONE);
                            } else {
                                receiver.setVisibility(View.GONE);
                                pay.setVisibility(View.VISIBLE);
                                pay.setText("去付款:" + "90s");
                            }
                        }
                        break;
                    case "isEstablish":
                        if (v8Object.getInteger(key) == 1) {
                            pay.setVisibility(View.VISIBLE);
                            pay.setText("去付款:" + "90s");
                            receiver.setVisibility(View.GONE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                changeLayout.setBackground(getContext().getDrawable(R.drawable.task_release_noclick));
                                cancelLayout.setBackground(getContext().getDrawable(R.drawable.task_release_noclick));
                            }
                            change.setTextColor(Color.parseColor("#8c8c8e"));
                            cancel.setTextColor(Color.parseColor("#8c8c8e"));
                            change.setText("修改订单");
                            cancel.setText("取消订单");
                            changeLayout.setEnabled(false);
                            cancel.setEnabled(false);
                            cancelLayout.setEnabled(false);
                        } else {
                            pay.setVisibility(View.GONE);
                            receiver.setVisibility(View.VISIBLE);
                            change.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }
            changeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickMap.containsKey("change")) {
                        invokeWithContext(mClickMap.get("change"), ReleasedHolder.this);
                    }
                }
            });


            cancelLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickMap.containsKey("cancel")) {
                        invokeWithContext(mClickMap.get("cancel"), ReleasedHolder.this);
                    }
                }
            });
            receiver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickMap.containsKey("receiver")) {
                        invokeWithContext(mClickMap.get("receiver"), ReleasedHolder.this);
                    }
                }
            });
            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickMap.containsKey("pay")) {
                        invokeWithContext(mClickMap.get("pay"), ReleasedHolder.this);
                    }
                }
            });
        }
    }
    private class RunningHolder extends RecyclerView.ViewHolder implements HolderInit {
        private boolean isBackApplyUser, isBackApply;
        private RoundedImageView img;
        private TextView name, type, createTime, outTime, reward;
        private final RelativeLayout backLayout;
        private final RelativeLayout chatLayout;
        private final RelativeLayout handleLayout;

        public RunningHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            createTime = itemView.findViewById(R.id.createTime);
            outTime = itemView.findViewById(R.id.outTime);
            reward = itemView.findViewById(R.id.reward);
            chatLayout = itemView.findViewById(R.id.chatLayout);
            backLayout = itemView.findViewById(R.id.backLayout);
            handleLayout = itemView.findViewById(R.id.handleLayout);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key) {
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString("img"))).into(img);
                        break;
                    case "nickname":
                        name.setText(object.getString(key));
                        break;
                    case "taskType":
                    case "taskStyle":
                        type.setText("任务类型：" + object.get("taskStyle") + "(" + object.get("taskType") + ")");
                        break;
                    case "reward":
                        reward.setText("服务费：" + "￥" + object.get(key));
                        break;
                    case "outTime":
                        outTime.setText("任务时限：" + object.getString(key) + "小时");
                        break;
                    case "isBackApply":
                        isBackApply = object.getBoolean(key);
                        if (object.getBoolean(key)) {
                            handleLayout.setVisibility(View.VISIBLE);
                            backLayout.setVisibility(View.GONE);
                        } else {
                            handleLayout.setVisibility(View.GONE);
                            backLayout.setVisibility(View.VISIBLE);
                        }
                        break;
                    case "isBackApplyUser":
                        isBackApplyUser = object.getBoolean(key);
                        break;
                    case "receiverTime":
                        createTime.setText("接单时间：" + object.get(key) + "");
                        break;
                }
            }


            handleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isBackApply && !isBackApplyUser) {
                        if (mClickMap.containsKey("handle")) {
                            invokeWithContext(mClickMap.get("handle"), RunningHolder.this);
                        }
                    }
                }
            });
            
            backLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickMap.containsKey("back")) {
                        invokeWithContext(mClickMap.get("back"), RunningHolder.this);
                    }
                }
            });

            chatLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickMap.containsKey("chat")) {
                        invokeWithContext(mClickMap.get("chat"), RunningHolder.this);
                    }
                }
            });
        }
    }

    private class ServicedHolder extends RecyclerView.ViewHolder implements HolderInit {
        private TextView finish, chat;
        private ImageView img;
        private TextView name, type, createTime, endTime, reward;
        private final RelativeLayout chatLayout;
        private final RelativeLayout finishLayout;

        public ServicedHolder(@NonNull View itemView) {
            super(itemView);
            finish = itemView.findViewById(R.id.finish);
            chat = itemView.findViewById(R.id.chat);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            createTime = itemView.findViewById(R.id.createTime);
            endTime = itemView.findViewById(R.id.endTime);
            reward = itemView.findViewById(R.id.reward);
            chatLayout = itemView.findViewById(R.id.chatLayout);
            finishLayout = itemView.findViewById(R.id.finishLayout);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key) {
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString("img"))).into(img);
                        break;
                    case "nickname":
                        name.setText(object.getString(key));
                        break;
                    case "taskType":
                    case "taskStyle":
                        type.setText("任务类型：" + object.get("taskStyle") + "(" + object.get("taskType") + ")");
                        break;
                    case "reward":
                        reward.setText("任务报酬:" + object.get(key));
                        break;
                    case "endTime":
                        endTime.setText("任务结束时间" + object.getString(key));
                        break;
                }
            }
            chatLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickMap.containsKey("chat")) {
                        invokeWithContext(mClickMap.get("chat"), ServicedHolder.this);
                    }
                }
            });


            finishLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickMap.containsKey("finish")) {
                        invokeWithContext(mClickMap.get("finish"), ServicedHolder.this);
                    }
                }
            });
        }
    }


    private class FinishHolder extends RecyclerView.ViewHolder implements HolderInit {
        Button drawbill;
        private ImageView img;
        private TextView name, type, createTime, endTime, reward;

        public FinishHolder(@NonNull View itemView) {
            super(itemView);
            drawbill = itemView.findViewById(R.id.drawbill);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            createTime = itemView.findViewById(R.id.createTime);
            endTime = itemView.findViewById(R.id.endTime);
            reward = itemView.findViewById(R.id.reward);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key) {
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString("img"))).into(img);
                        break;
                    case "nickname":
                        name.setText(object.getString(key));
                        break;
                    case "taskType":
                    case "taskStyle":
                        type.setText("任务类型：" + object.get("taskStyle") + "(" + object.get("taskType") + ")");
                        break;
                    case "reward":
                        reward.setText("服务费：" + object.get(key));
                        break;
                    case "endTime":
                        endTime.setText("任务结束时间：" + object.getString(key));
                        break;
                    case "outTime":
                        createTime.setText("任务时限：" + object.get(key) + "小时");
                        break;
                }
            }
            drawbill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickMap.containsKey("drawbill")) {
                        invokeWithContext(mClickMap.get("drawbill"), FinishHolder.this);
                    }
                }
            });
        }
    }


    private long onOutTime(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = new Date(System.currentTimeMillis());
        try {
            String format = df.format(date1);
            
            Date parse = df.parse(date);
            Date parse1 = df.parse(format);

            long diff = parse1.getTime() - parse.getTime();
            
            return (diff/1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
