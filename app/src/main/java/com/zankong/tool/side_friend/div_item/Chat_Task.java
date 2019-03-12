package com.zankong.tool.side_friend.div_item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.HolderInit;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.util.ZKAppAdapter;
import com.zankong.tool.zkapp.v8fn.media_pck.MediaManager;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zankong.tool.zkapp.views.ZKPView;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.HashMap;
/**
 * Created by YF on 2018/8/30.
 */
@ZKAppAdapter("chat_task")
public class Chat_Task extends ZKAdapter{
    private final int TYPE_TEXT = 1,
                        TYPE_IMAGE = 2,
                        TYPE_BARGAINING = 3,
                        TYPE_AUDIO = 4,
                        TYPE_VIDEO = 5,
                        TYPE_TASK = 6;

    private HashMap<String,Integer> types = new HashMap<String,Integer>(){{
        put("text", TYPE_TEXT);
        put("image", TYPE_IMAGE);
        put("bargaining",TYPE_BARGAINING);
        put("audio",TYPE_AUDIO);
        put("video", TYPE_VIDEO);
        put("task", TYPE_TASK);
    }};
    private String task_agree = null,task_disagree = null;

    public Chat_Task(ZKDocument zKDocument, Element element) {
        super(zKDocument, element);
        Element task = element.element("chat_task").element("task");
        if (task != null){
            Util.log("chat_task",task.asXML());
            for (Attribute attribute : task.attributes()) {
                String value = attribute.getValue();
                switch (attribute.getName()) {
                    case "agree":
                        task_agree = value;
                        break;
                    case "disagree":
                        task_disagree = value;
                        break;
                }
            }
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreate(@NonNull ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (i){
            case TYPE_TEXT:
                viewHolder = new TextHolder(mLayoutInflater.inflate(R.layout.item_chat_task_text,viewGroup,false));
                break;
            case TYPE_IMAGE:
                viewHolder = new ImageHolder(mLayoutInflater.inflate(R.layout.item_chat_task_image,viewGroup,false));
                break;
            case TYPE_BARGAINING:
                viewHolder = new BargainingHolder(mLayoutInflater.inflate(R.layout.item_chat_task_bargaining,viewGroup,false));
                break;
            case TYPE_AUDIO:
                viewHolder = new AudioHolder(mLayoutInflater.inflate(R.layout.item_chat_task_audio,viewGroup,false));
                break;
            case TYPE_VIDEO:
                viewHolder = new VideoHolder(mLayoutInflater.inflate(R.layout.item_chat_task_video,viewGroup,false));
                break;
            case TYPE_TASK:
                viewHolder = new TaskHolder(mLayoutInflater.inflate(R.layout.item_chat_task_task,viewGroup,false));
                break;
        }
        return viewHolder;
    }

    @Override
    protected int getViewType(int position) {
        V8Object object = mList.get(position);
        return types.get(object.getString("type"));
    }

    @Override
    protected void onBind(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof HolderInit){
            ((HolderInit) viewHolder).init(position);
        }
    }

    private class TextHolder extends RecyclerView.ViewHolder implements HolderInit{
        private ImageView mImg;
        private RelativeLayout mFrame;
        private ZKPView mMessage;
        private int self = -1,sender = 0;

        public TextHolder(@NonNull View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.img);
            mFrame = itemView.findViewById(R.id.frame);
            mMessage = itemView.findViewById(R.id.message);
        }
        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key) {
                    case "self":
                        self = object.getInteger(key);
                        break;
                    case "sender":
                        sender = object.getInteger(key);
                        break;
                    case "content":
                        mMessage.setFaceText(object.getString(key));
                        break;
                    case "senderName":
                        break;
                    case "senderImg":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(key))).into(mImg);
                        break;
                }
            }
            RelativeLayout.LayoutParams imgLayoutParams = (RelativeLayout.LayoutParams) mImg.getLayoutParams();
            RelativeLayout.LayoutParams messageLayoutParams = (RelativeLayout.LayoutParams) mFrame.getLayoutParams();
            if (self == sender){
                imgLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                messageLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                mFrame.setBackgroundResource(R.drawable.mychat_001);
                messageLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                imgLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }else {
                imgLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                messageLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                mFrame.setBackgroundResource(R.drawable.mychat_002);
                messageLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                imgLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }
        }
    }
    private class ImageHolder extends RecyclerView.ViewHolder implements HolderInit {
        private ImageView img,content;
        private RelativeLayout frame;
        private int self = -1,sender = 0;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            content = itemView.findViewById(R.id.content);
            frame = itemView.findViewById(R.id.frame);
        }

        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key){
                    case "self":
                        self = object.getInteger(key);
                        break;
                    case "sender":
                        sender = object.getInteger(key);
                        break;
                    case "content":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(key))).into(content);
                        break;
                    case "senderName":
                        break;
                    case "senderImg":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(key))).into(img);
                        break;
                }
            }
            RelativeLayout.LayoutParams imgLayoutParams = (RelativeLayout.LayoutParams) img.getLayoutParams();
            RelativeLayout.LayoutParams frameLayoutParams = (RelativeLayout.LayoutParams) frame.getLayoutParams();
            RelativeLayout.LayoutParams contentLayoutParams = (RelativeLayout.LayoutParams) content.getLayoutParams();
            if (self == sender){
                imgLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                frameLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                frame.setBackgroundResource(R.drawable.mychat_001);
                frameLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                imgLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                contentLayoutParams.setMargins(0,0,Util.dip2px(5),0);
            }else {
                imgLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                frameLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                frame.setBackgroundResource(R.drawable.mychat_002);
                frameLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                imgLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                contentLayoutParams.setMargins(Util.dip2px(5),0,0,0);
            }
        }
    }

    private class BargainingHolder extends RecyclerView.ViewHolder implements HolderInit {
        private int self = -1,sender = 0,valid = 0;
        private String status;
        private TextView mTaskDetail,mAgree,mDisagree,mStatus;
        private LinearLayout mOperation;
        public BargainingHolder(@NonNull View itemView) {
            super(itemView);
            mTaskDetail = itemView.findViewById(R.id.taskDetail);
            mAgree = itemView.findViewById(R.id.agree);
            mDisagree = itemView.findViewById(R.id.disagree);
            mStatus = itemView.findViewById(R.id.status);
            mOperation = itemView.findViewById(R.id.operation);
        }
        @Override
        public void init(int position) {
            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key) {
                    case "self":
                        self = object.getInteger(key);
                        break;
                    case "sender":
                        sender = object.getInteger(key);
                        break;
                    case "bargainingApply":
                        V8Object bargainingApplyObj = object.getObject(key);
                        for (String s : bargainingApplyObj.getKeys()) {
                            switch (s){
                                case "status":
                                    status = bargainingApplyObj.getString(s);
                                    break;
                                case "valid":
                                    valid = bargainingApplyObj.getInteger(s);
                                    mStatus.setText("申请状态:"+(valid==1?"有效":"无效"));
                                    break;
                                case "bargainingPrice":
                                    mTaskDetail.setText("议价价格:"+bargainingApplyObj.get(s));
                                    break;
                                case "createdAt":

                                    break;
                            }
                        }
                        break;
                }
            }
            mStatus.setVisibility(View.VISIBLE);
            if(valid == 1){
                if (self == sender){
                    mOperation.setVisibility(View.GONE);
                }else {
                    mOperation.setVisibility(View.VISIBLE);
                }
            }else{
                mOperation.setVisibility(View.GONE);
            }
            if("valid".equals(status)){
                mOperation.setVisibility(View.GONE);
                mStatus.setText("申请状态:已接收");
            }
            mAgree.setOnClickListener(view -> {
                invokeWithContext(task_agree,BargainingHolder.this);
            });
            mDisagree.setOnClickListener(view -> {
                invokeWithContext(task_disagree,BargainingHolder.this);
            });
        }
    }
    private class AudioHolder extends RecyclerView.ViewHolder implements HolderInit{
        private ImageView mImg;
        private RelativeLayout mFrame;
        private ZKPView audio;
        private int self = -1,sender = 0;
        int second;
        String url = null;
        public AudioHolder(@NonNull View itemView) {
            super(itemView);
            mImg = itemView.findViewById(R.id.img);
            mFrame = itemView.findViewById(R.id.frame);
            audio = itemView.findViewById(R.id.audio);
        }

        @Override
        public void init(int position) {

            V8Object object = mList.get(position);
            for (String key : object.getKeys()) {
                switch (key) {
                    case "self":
                        self = object.getInteger(key);
                        break;
                    case "sender":
                        sender = object.getInteger(key);
                        break;
                    case "content":
                        try {
                            V8Object parse = V8Utils.parse(object.getString(key));
                            second = parse.getInteger("second");
                            url = parse.getString("url");
                            audio.setFaceText(second+"秒");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case "senderName":
                        break;
                    case "senderImg":
                        Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(key))).into(mImg);
                        break;
                }
            }
            RelativeLayout.LayoutParams imgLayoutParams = (RelativeLayout.LayoutParams) mImg.getLayoutParams();
            RelativeLayout.LayoutParams messageLayoutParams = (RelativeLayout.LayoutParams) mFrame.getLayoutParams();
            if (self == sender){
                imgLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                messageLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
                mFrame.setBackgroundResource(R.drawable.mychat_001);
                messageLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                imgLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }else {
                imgLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                messageLayoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                mFrame.setBackgroundResource(R.drawable.mychat_002);
                messageLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                imgLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            }
            audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(url != null) MediaManager.getInstance().playSound(url);
                }
            });
        }
    }
    private class VideoHolder extends RecyclerView.ViewHolder implements HolderInit {
        public VideoHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void init(int position) {

        }
    }
    private class TaskHolder extends RecyclerView.ViewHolder implements HolderInit{
        public TaskHolder(@NonNull View itemView) {
            super(itemView);

        }
        @Override
        public void init(int position) {
            Util.log(V8Utils.js2string(mList.get(position)));
        }
    }

}
