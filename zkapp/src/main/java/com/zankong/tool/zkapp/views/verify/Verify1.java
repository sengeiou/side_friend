package com.zankong.tool.zkapp.views.verify;

import android.view.ViewGroup;

import com.luozm.captcha.Captcha;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Element;

/**
 * Created by YF on 2018/8/14.
 */

public class Verify1 extends ZKViewAgent {

    private Captcha mCaptcha;

    public Verify1(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.view_verify_1);
        mCaptcha = ((Captcha) findViewById(R.id.captcha));
    }

    @Override
    public void fillData(Element selfElement) {
        mCaptcha.setCaptchaListener(new Captcha.CaptchaListener() {
            @Override
            public String onAccess(long time) {
                Util.log("verify","验证成功,耗时"+time/60/1000+"秒");
                return null;
            }

            @Override
            public String onFailed(int failCount) {
                Util.log("verify","验证失败,已失败"+failCount+"次");
                return null;
            }

            @Override
            public String onMaxFailed() {
                return null;
            }
        });
    }

    @Override
    public Object getResult() {
        return null;
    }
}
