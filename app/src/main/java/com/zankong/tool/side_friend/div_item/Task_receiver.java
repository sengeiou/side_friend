package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.views.ZKImgView;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.HashMap;

/**
 * Created by YF on 2018/8/31.
 */

@ZKAppAdapter("task_receiver")
public class Task_receiver extends ZKAdapter {

    private final int STATUS_RELEASED = 0;
    private final int STATUS_RUNNING = 1;
    private final int STATUS_SERVICED = 2;
    private final int STATUS_FINISH = 3;
    private HashMap<String,Integer> mStatus = new HashMap<String,Integer>(){{
        put("released", STATUS_RELEASED);
        put("running",STATUS_RUNNING);
        put("serviced", STATUS_SERVICED);
        put("finish",STATUS_FINISH);
    }};
    private HashMap<String,String> mClickMap = new HashMap<>();

    public Task_receiver(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
        for (Attribute attribute : element.elements().get(0).attributes()) {
            mClickMap.put(attribute.getName(),attribute.getValue());
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (i){
            case STATUS_RELEASED:
                viewHolder = new ReleasedHolder(mLayoutInflater.inflate(R.layout.item_task_receiver_released,viewGroup,false));
                break;
            case STATUS_RUNNING:
                viewHolder = new RunningHolder(mLayoutInflater.inflate(R.layout.item_task_receiver_running,viewGroup,false));
                break;
            case STATUS_SERVICED:
                viewHolder = new ServicedHolder(mLayoutInflater.inflate(R.layout.item_task_receiver_serviced,viewGroup,false));
                break;
            case STATUS_FINISH:
                viewHolder = new FinishHolder(mLayoutInflater.inflate(R.layout.item_task_receiver_finish,viewGroup,false));
                break;
        }
        return viewHolder;
    }

    @Override
    protected int getViewType(int position) {
        return mStatus.get(mList.get(position).getString("status"));
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof HolderInit){
            ((HolderInit) viewHolder).init(position);
        }
    }

    private class ReleasedHolder extends RecyclerView.ViewHolder implements HolderInit{
        Button chat,disApply;
        ImageView img;
        TextView nickname,type,receiverTime,receiverNum,outTime,reward,waitPay,timeout;
        private final RelativeLayout disApplyLayout;
        private final RelativeLayout chatLayout;

        public ReleasedHolder(@NonNull View itemView) {
            super(itemView);
            chat = itemView.findViewById(R.id.chat);
            disApply = itemView.findViewById(R.id.disApply);
            img = itemView.findViewById(R.id.img);
            nickname = itemView.findViewById(R.id.nickname);
            type = itemView.findViewById(R.id.type);
            receiverTime = itemView.findViewById(R.id.receiverTime);
            receiverNum = itemView.findViewById(R.id.receiverNum);
            outTime = itemView.findViewById(R.id.outTime);
            reward = itemView.findViewById(R.id.reward);
            waitPay = itemView.findViewById(R.id.waitPay);
            timeout = itemView.findViewById(R.id.timeout);
            chatLayout = itemView.findViewById(R.id.chatLayout);
            disApplyLayout = itemView.findViewById(R.id.disApplyLayout);
        }

        @Override
        public void init(int position) {
            V8Object v8Object = mList.get(position);
            for (String key : v8Object.getKeys()) {
                switch (key) {
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(v8Object.getString(key))).into(img);
                        break;
                    case "nickname":
                        nickname.setText(v8Object.getString(key));
                        break;
                    case "taskStyle":
                    case "taskType":
                        type.setText("任务类型："+v8Object.get("taskStyle")+"("+v8Object.get("taskType")+")");
                        break;
                    case "receiverTime":
                        receiverTime.setText(v8Object.getString(key));
                        break;
                    case "receiverNum":
                        receiverNum.setText("有"+v8Object.getInteger(key)+"人抢单");
                        break;
                    case "outTime":
                        outTime.setText("任务超时时间："+v8Object.getString(key));
                        break;
                    case "reward":
                        reward.setText("任务报酬："+v8Object.get(key)+"");
                        break;
                    case "isEstablish":
                        if (v8Object.getInteger(key) == 1){
                            disApplyLayout.setVisibility(View.GONE);
                            waitPay.setVisibility(View.VISIBLE);
                        }else {
                            disApplyLayout.setVisibility(View.VISIBLE);
                            waitPay.setVisibility(View.GONE);
                        }
                        break;
                    case "timeout":
                        timeout.setText("申请倒计时"+v8Object.getString(key));
                        break;
                }
            }
            chatLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mClickMap.containsKey("chat")){
                        invokeWithContext(mClickMap.get("chat"),ReleasedHolder.this);
                    }
                }
            });
            disApplyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mClickMap.containsKey("disApply")){
                        invokeWithContext(mClickMap.get("disApply"),ReleasedHolder.this);
                    }
                }
            });
        }
    }
    private class RunningHolder extends RecyclerView.ViewHolder implements HolderInit{
        Button chat,back,serviced,handle;
        private ImageView img;
        private TextView name,type,createTime,outTime,reward;
        private boolean isBackApplyUser,isBackApply;
        private final RelativeLayout chatLayout;
        private final RelativeLayout handleLayout;
        private final RelativeLayout backLayout;
        private final RelativeLayout servicedLayout;

        public RunningHolder(@NonNull View itemView) {
            super(itemView);
            chat = itemView.findViewById(R.id.chat);
            handle = itemView.findViewById(R.id.handle);
            back = itemView.findViewById(R.id.back);
            serviced = itemView.findViewById(R.id.serviced);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            createTime = itemView.findViewById(R.id.createTime);
            outTime = itemView.findViewById(R.id.outTime);
            reward = itemView.findViewById(R.id.reward);

            chatLayout = itemView.findViewById(R.id.chatLayout);
            handleLayout = itemView.findViewById(R.id.handleLayout);
            backLayout = itemView.findViewById(R.id.backLayout);
            servicedLayout = itemView.findViewById(R.id.servicedLayout);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key){
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString("img"))).into(img);
                        break;
                    case "nickname":
                        name.setText(object.getString(key));
                        break;
                    case "taskStyle":
                    case "taskType":
                        type.setText("任务类型："+object.get("taskStyle")+"("+object.get("taskType")+")");
                        break;
                    case "reward":
                        reward.setText("任务报酬："+object.get(key));
                        break;
                    case "createTime":
                        createTime.setText("接单时间："+object.getString(key));
                        break;
                    case "outTime":
                        outTime.setText("任务时限："+object.get(key)+"");
                        break;
                    case "isBackApply":
                        isBackApply = object.getBoolean(key);
                        if(object.getBoolean(key)){
                            handleLayout.setVisibility(View.VISIBLE);
                            backLayout.setVisibility(View.GONE);
                        }else {
                            handleLayout.setVisibility(View.GONE);
                            backLayout.setVisibility(View.VISIBLE);
                        }
                        break;
                    case "isBackApplyUser":
                        isBackApplyUser = object.getBoolean(key);
                        break;
                }
            }
            chatLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mClickMap.containsKey("chat")){
                        invokeWithContext(mClickMap.get("chat"),RunningHolder.this);
                    }
                }
            });

            backLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mClickMap.containsKey("back")){
                        invokeWithContext(mClickMap.get("back"),RunningHolder.this);
                    }
                }
            });
            handleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isBackApply && !isBackApplyUser){
                        if (mClickMap.containsKey("handle")){
                            invokeWithContext(mClickMap.get("handle"),RunningHolder.this);
                        }
                    }
                }
            });
            servicedLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mClickMap.containsKey("serviced")){
                        invokeWithContext(mClickMap.get("serviced"),RunningHolder.this);
                    }
                }
            });
        }
    }
    private class ServicedHolder extends RecyclerView.ViewHolder implements HolderInit{
        private ImageView img;
        private TextView name,type,createTime,endTime,reward;
        private RelativeLayout chatLayout,changeAdvancePay;
        public ServicedHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            type = itemView.findViewById(R.id.type);
            createTime = itemView.findViewById(R.id.createTime);
            endTime = itemView.findViewById(R.id.endTime);
            reward = itemView.findViewById(R.id.reward);
            chatLayout = itemView.findViewById(R.id.chatLayout);
            changeAdvancePay = itemView.findViewById(R.id.changeAdvancePay);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key){
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString("img"))).into(img);
                        break;
                    case "nickname":
                        name.setText(object.getString(key));
                        break;
                    case "taskStyle":
                    case "taskType":
                        type.setText("任务类型："+object.get("taskStyle")+"("+object.get("taskType")+")");
                        break;
                    case "reward":
                        reward.setText("任务报酬："+object.get(key));
                        break;
                    case "createTime":
                        createTime.setText("接单时间："+object.getString(key));
                        break;
                    case "outTime":
                        endTime.setText("任务时限："+object.get(key));
                        break;
                }
            }
            chatLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mClickMap.containsKey("chat")){
                        invokeWithContext(mClickMap.get("chat"),ServicedHolder.this);
                    }
                }
            });
            changeAdvancePay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mClickMap.containsKey("changeAdvancePay")){
                        invokeWithContext(mClickMap.get("changeAdvancePay"),ServicedHolder.this);
                    }
                }
            });
        }
    }
    private class FinishHolder extends RecyclerView.ViewHolder implements HolderInit{
        private RelativeLayout chatLayout;
        private ImageView img;
        private TextView name,type,createTime,endTime,reward;
        public FinishHolder(@NonNull View itemView) {
            super(itemView);
            chatLayout = itemView.findViewById(R.id.chatLayout);
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
                switch (key){
                    case "img":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString("img"))).into(img);
                        break;
                    case "nickname":
                        name.setText(object.getString(key));
                        break;
                    case "taskStyle":
                    case "taskType":
                        type.setText("任务类型："+object.get("taskStyle")+"("+object.get("taskType")+")");
                        break;
                    case "reward":
                        reward.setText("任务报酬："+object.get(key));
                        break;
                    case "endTime":
                        endTime.setText("结束时间："+object.getString(key));
                        break;
                }
            }
            chatLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mClickMap.containsKey("chat")){
                        invokeWithContext(mClickMap.get("chat"),FinishHolder.this);
                    }
                }
            });
        }
    }



}
