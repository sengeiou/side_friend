package com.zankong.tool.side_friend.diy_view.button_dialog;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Element;

public class PayDialogButton extends ZKViewAgent {
    private final int  ARROW_LAYOUT = 0;
    private final int  PAY_WAY_LAYOUT = 1;
    private final int  CONFIRM_LAYOUT = 2;
    private RelativeLayout layout;
    private PayDialog dialog;
    public PayDialogButton(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.pay_dialog_button_layout);
        layout = (RelativeLayout) findViewById(R.id.layout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null) {
                    dialog = new PayDialog(getContext(), R.style.BottomDialog);
                    dialog.onItemClick(new PayDialog.DialogItemClick() {
                        @Override
                        public void onClick(int position) {
                            switch (position) {
                                case ARROW_LAYOUT:
                                    if (dialog.isShowing()){
                                        dialog.dismiss();
                                    }
                                    break;
                                case PAY_WAY_LAYOUT:
                                    break;
                                case CONFIRM_LAYOUT:
                                    break;
                            }

                        }
                    });
                    dialog.show();
                }
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        });


 
    }
    @Override
    public void fillData(Element selfElement) {

    }

    @Override
    public Object getResult() {
        return null;
    }
}
