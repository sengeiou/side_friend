package com.zankong.tool.side_friend.diy_view.user_message;

import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eclipsesource.v8.JavaCallback;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKImgView;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Element;

public class UserMessageRecruit extends ZKViewAgent{
    private RoundedImageView img;
    private TextView nickname,userCharmGrade,reward;
    public UserMessageRecruit(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_user_message_recruit);
        img = (RoundedImageView) findViewById(R.id.img);
        nickname = (TextView) findViewById(R.id.nickname);
        userCharmGrade = (TextView) findViewById(R.id.userCharmGrade);
        reward = (TextView) findViewById(R.id.reward);
    }

    @Override
    public void fillData(Element selfElement) {

    }

    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod(new JavaCallback() {
            @Override
            public Object invoke(V8Object receiver, V8Array parameters) {
                V8Object object = parameters.getObject(0);
                for (String s : object.getKeys()) {
                    switch (s) {
                        case "img":
                            Glide.with(getContext()).load(ZKImgView.getUrl(object.getString(s))).into(img);
                            break;
                        case "nickname":
                            nickname.setText(object.getString(s));
                            break;
                        case "userCharmGrade":
                            userCharmGrade.setText("下单评分"+object.get(s)+"分");
                            break;
                        case "reward":
                            reward.setText(object.get(s)+"");
                            break;
                    }
                }
                return null;
            }
        },"setData");
    }
}
