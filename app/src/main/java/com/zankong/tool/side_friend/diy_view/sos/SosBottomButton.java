package com.zankong.tool.side_friend.diy_view.sos;
import android.view.ViewGroup;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;
import org.dom4j.Element;
public class SosBottomButton extends ZKViewAgent {
    private DiffuseView mDiffuseView;
    public SosBottomButton(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }
    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.sos_bottom_button);
        mDiffuseView = (DiffuseView) findViewById(R.id.diffuseView);
    }
    @Override
    public void fillData(Element selfElement) {
    }
    @Override
    public Object getResult() {
        return null;
    }
}
