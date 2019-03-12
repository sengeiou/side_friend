package com.zankong.tool.side_friend;

import android.app.Application;

import com.example.zkapp_identity.identity.IdentityInit2;
import com.example.zkapp_map.MapInit;
import com.example.zkapp_map.adapter.AdapterMapTask;
import com.example.zkapp_map.document.MapLayout;
import com.zankong.tool.side_friend.div_item.AllClassfiyItem;
import com.zankong.tool.side_friend.div_item.ApplyFriend;
import com.zankong.tool.side_friend.div_item.BankCard;
import com.zankong.tool.side_friend.div_item.Bills;
import com.zankong.tool.side_friend.div_item.Chat_Task;
import com.zankong.tool.side_friend.div_item.Contacts;
import com.zankong.tool.side_friend.div_item.Credit;
import com.zankong.tool.side_friend.div_item.CreditItemNew;
import com.zankong.tool.side_friend.div_item.EditAddAdapter;
import com.zankong.tool.side_friend.div_item.EnterpriseDescription;
import com.zankong.tool.side_friend.div_item.Evaluate;
import com.zankong.tool.side_friend.div_item.FindFriendItem;
import com.zankong.tool.side_friend.div_item.GroupDetailsItem;
import com.zankong.tool.side_friend.div_item.HomeTask;
import com.zankong.tool.side_friend.div_item.LocitionItem;
import com.zankong.tool.side_friend.div_item.MessageEditExperience;
import com.zankong.tool.side_friend.div_item.NavigationItem;
import com.zankong.tool.side_friend.div_item.OftenAddress;
import com.zankong.tool.side_friend.div_item.OftenAddressModify;
import com.zankong.tool.side_friend.div_item.PhotoWall;
import com.zankong.tool.side_friend.div_item.RecruitApplyItem;
import com.zankong.tool.side_friend.div_item.RecruitItem;
import com.zankong.tool.side_friend.div_item.RecruitReleaseItem;
import com.zankong.tool.side_friend.div_item.SOS_list;
import com.zankong.tool.side_friend.div_item.ShareRecordItem;
import com.zankong.tool.side_friend.div_item.SkillList;
import com.zankong.tool.side_friend.div_item.TaskSenderPlayItem;
import com.zankong.tool.side_friend.div_item.Task_Receiver_List;
import com.zankong.tool.side_friend.div_item.Task_Release;
import com.zankong.tool.side_friend.div_item.Task_receiver;
import com.zankong.tool.side_friend.div_item.test;
import com.zankong.tool.side_friend.diy_document.LinearLayout;
import com.zankong.tool.side_friend.diy_v8fn.Aliput;
import com.zankong.tool.side_friend.diy_view.button_dialog.DialogButton;
import com.zankong.tool.side_friend.diy_view.credit_manage.CreditManage;
import com.zankong.tool.side_friend.diy_view.evaluate.RatingBarView;
import com.zankong.tool.side_friend.diy_view.group_details.GroupDetails;
import com.zankong.tool.side_friend.diy_view.home_screen.HomeScreen;
import com.zankong.tool.side_friend.diy_view.index_user.IndexUser;
import com.zankong.tool.side_friend.diy_view.mail_list.MailList;
import com.zankong.tool.side_friend.diy_view.pangyou.PangYouHeader;
import com.zankong.tool.side_friend.diy_view.release.Release;
import com.zankong.tool.side_friend.diy_view.screen.Screen;
import com.zankong.tool.side_friend.diy_view.sos.SosView;
import com.zankong.tool.side_friend.diy_view.task_detail.TaskDetailall;
import com.zankong.tool.side_friend.diy_view.text.Text_edit;
import com.zankong.tool.side_friend.diy_view.toolbar.ZKToolbar_1;
import com.zankong.tool.side_friend.diy_view.toolbar.ZKToolbar_2;
import com.zankong.tool.side_friend.diy_view.toolbar.ZKToolbar_3;
import com.zankong.tool.side_friend.diy_view.user_message.UserMessage;
import com.zankong.tool.side_friend.diy_view.wallet.Wallet;
import com.zankong.tool.side_friend.test_item.Item1;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.item.ZKAdapter;
import com.zankong.tool.zkapp.views.ZKViews;
import com.zankong.tool.zkapp.views.text.Text;
import com.zankong.tool.zkapp.views.toolbar.ZKToolbar;
import com.zankong.tool.zkapp_alipay.AliPay;
import com.zk.tool.zkapp_call.ZKCallManager;
import com.zk.tool.zkapp_umeng.ZKUmengInit;
import com.zk.tool.zkapp_update.ZKUpdateInit;
import com.zk.tool.zkapp_wechat.WeChatPay;
//import com.zk.tool.zkapp_wechat.WeChatPay;

/**
 * Created by YF on 2018/6/23.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ZKToolApi.init(this);
        MapInit.initMap(this);
        ZKUmengInit.init();
//        IdentityInit.initIdentity(this);
        IdentityInit2.initIdentity2(this);


        ZKAdapter.adapterMap.put("chat_task", Chat_Task.class);
        ZKAdapter.adapterMap.put("contacts", Contacts.class);
        ZKAdapter.adapterMap.put("credit", Credit.class);
        ZKAdapter.adapterMap.put("credititem", CreditItemNew.class);
        ZKAdapter.adapterMap.put("groupDetails".toLowerCase(), GroupDetailsItem.class);
        ZKAdapter.adapterMap.put("homeTask".toLowerCase(), HomeTask.class);
        ZKAdapter.adapterMap.put("locationItem".toLowerCase(), LocitionItem.class);
        ZKAdapter.adapterMap.put("process", Process.class);
        ZKAdapter.adapterMap.put("sos_list", SOS_list.class);
        ZKAdapter.adapterMap.put("edit_experience", MessageEditExperience.class);
        ZKAdapter.adapterMap.put("task_receiver", Task_receiver.class);
        ZKAdapter.adapterMap.put("play_item", TaskSenderPlayItem.class);
        ZKAdapter.adapterMap.put("task_receiver_list", Task_Receiver_List.class);
        ZKAdapter.adapterMap.put("task_release", Task_Release.class);
        ZKAdapter.adapterMap.put("test", test.class);
        ZKAdapter.adapterMap.put("bills", Bills.class);
        ZKAdapter.adapterMap.put("oftenAddress".toLowerCase(), OftenAddress.class);
        ZKAdapter.adapterMap.put("share_record".toLowerCase(), ShareRecordItem.class);
        ZKAdapter.adapterMap.put("oftenAddressModify".toLowerCase(), OftenAddressModify.class);
        ZKAdapter.adapterMap.put("editAdd".toLowerCase(), EditAddAdapter.class);
        ZKAdapter.adapterMap.put("ApplyFriend".toLowerCase(), ApplyFriend.class);
        ZKAdapter.adapterMap.put("bankCard".toLowerCase(), BankCard.class);
        ZKAdapter.adapterMap.put("evaluate".toLowerCase(), Evaluate.class);
        ZKAdapter.adapterMap.put("skillList".toLowerCase(), SkillList.class);
        ZKAdapter.adapterMap.put("AdapterMapTask".toLowerCase(),AdapterMapTask.class);
        ZKAdapter.adapterMap.put("enterpriseDescription".toLowerCase(), EnterpriseDescription.class);
        ZKAdapter.adapterMap.put("photoWall".toLowerCase(), PhotoWall.class);
        ZKAdapter.adapterMap.put("navigation".toLowerCase(), NavigationItem.class);
        ZKAdapter.adapterMap.put("recruit".toLowerCase(), RecruitItem.class);
        ZKAdapter.adapterMap.put("recruit_release".toLowerCase(), RecruitReleaseItem.class);
        ZKAdapter.adapterMap.put("recruitApply".toLowerCase(), RecruitApplyItem.class);
        ZKAdapter.adapterMap.put("findFriend".toLowerCase(), FindFriendItem.class);

        /**
         *  hk
         */
        ZKAdapter.adapterMap.put("list".toLowerCase(),Item1.class);


        ZKViews.ViewMap.put("screen",Screen.class);
        ZKViews.ViewMap.put("user_message", UserMessage.class);
        ZKViews.ViewMap.put("index_user", IndexUser.class);
        ZKViews.ViewMap.put("taskdetail", TaskDetailall.class);
        ZKViews.ViewMap.put("release", Release.class);
        ZKViews.ViewMap.put("sos", SosView.class);
        Text.styleMap.put("edit", Text_edit.class);
        ZKViews.ViewMap.put("wallet", Wallet.class);
        ZKViews.ViewMap.put("pangyouheader", PangYouHeader.class);
        ZKViews.ViewMap.put("maillist", MailList.class);
        ZKViews.ViewMap.put("dialog_button", DialogButton.class);
        ZKViews.ViewMap.put("groupdetails", GroupDetails.class);
        ZKDocument.docMap.put("linearlayout", LinearLayout.class);
        
        ZKDocument.docMap.put("maplayout",MapLayout.class);
        ZKAdapter.adapterMap.put("allclassfiy", AllClassfiyItem.class);
        ZKViews.ViewMap.put("homescreen", HomeScreen.class);
        ZKViews.ViewMap.put("creditmanage", CreditManage.class);
        ZKViews.ViewMap.put("groupdetails", GroupDetails.class);
        ZKViews.ViewMap.put("ratingbar", RatingBarView.class);
        ZKToolbar.styleMap.put("1", ZKToolbar_1.class);
        ZKToolbar.styleMap.put("2", ZKToolbar_2.class);
        ZKToolbar.styleMap.put("3", ZKToolbar_3.class);

        ZKUpdateInit.init(this);


//                CallInit.init();
         new WeChatPay().addV8Fn();
        try {
            AliPay.class.newInstance().addV8Fn();
            Aliput.class.newInstance().addV8Fn();
            ZKCallManager.class.newInstance().addV8Fn();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}