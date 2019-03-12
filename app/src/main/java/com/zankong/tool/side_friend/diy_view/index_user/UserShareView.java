package com.zankong.tool.side_friend.diy_view.index_user;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zankong.tool.side_friend.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.util.V8Utils;
import com.zankong.tool.zkapp.v8fn.ZKLocalStorage;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UserShareView extends ZKViewAgent implements View.OnClickListener {
    private TextView tv_code;
    private TextView tv_save;
    private TextView tv_copy;
    private ImageView img_code;
    private LinearLayout layout_wechat;
    private RelativeLayout layout_img;
    private V8Object v8Object;
    private V8Function onClickListener;
    private ZKDocument zkDocument;
    private String shareUrl;
    private Bitmap qrCode;
    private Bitmap bitmap2;
    private LookBigImgDialog lookBigImgDialog;
    public UserShareView(ZKDocument zkDocument, Element element) {
        super(zkDocument, element);
        this.zkDocument = zkDocument;
    }

    @Override
    public void initView(ViewGroup viewGroup) {
        setContentView(R.layout.user_share_layout);
        tv_code = (TextView) findViewById(R.id.tv_code);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_copy = (TextView) findViewById(R.id.tv_copy);
        img_code = (ImageView) findViewById(R.id.img_code);
        layout_wechat = (LinearLayout) findViewById(R.id.layout_wechat);
        layout_img = (RelativeLayout) findViewById(R.id.layout_img);
        layout_wechat.setOnClickListener(this);
        tv_copy.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        layout_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lookBigImgDialog == null){
                    lookBigImgDialog = new LookBigImgDialog(getContext(),R.style.dialog,bitmap2);
                }
                lookBigImgDialog.show();
            }
        });
        
        
    }
    
    
    @Override
    public void fillData(Element selfElement) {
        for (Attribute attribute : selfElement.attributes()) {
            String value = attribute.getValue();
            switch (attribute.getName()) {
                case "click":
                    onClickListener = zkDocument.genContextFn(value);
                    break;
            }
        }
    }

    @Override
    public Object getResult() {
        if (!"".equals(shareUrl)) {
            return shareUrl + "";
        } else {
        }
        return "0";
    }

    @Override
    public void initThisV8(V8Object thisV8) {
        super.initThisV8(thisV8);
        thisV8.registerJavaMethod((receiver, parameters) -> {
            if (parameters.length() > 0) {
                V8Object object = parameters.getObject(0);
                String code = object.get("code") + "";
                shareUrl = object.get("shareUrl") + "";
                if (!"".equals(code))
                    tv_code.setText(code + "");
                if (!"".equals(shareUrl)) {
                    String img = ZKLocalStorage.mSharedPreferences.getString("img", "");
                    try {
                        returnBitMap(img);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            return null;
        }, "set");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_wechat:
                if (v8Object == null) {
                    v8Object = new V8Object(ZKToolApi.runtime);
                }
                v8Object.add("shareUrl", shareUrl + "");
                zkDocument.invokeWithContext(onClickListener, v8Object);
                break;
            case R.id.tv_copy:
                onCopyToClipboard(getContext(), tv_code.getText().toString());
                Toast.makeText(getContext(), "复制成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_save:
                if (qrCode != null)
                    saveBitmap(qrCode);
                Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void onCopyToClipboard(Context context, String text) {
        ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //clip.getText(); // 粘贴
        clip.setText(text); // 复制    
    }

    public void saveBitmap(Bitmap bitmap) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "zxing_image");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "zxing_img" + ".png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContext().getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Bitmap bitmap;

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param url
     */
    public Bitmap returnBitMap(final String url) {

        new Thread(new Runnable() {


            @Override
            public void run() {
                URL imageurl = null;

                try {
                    imageurl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    Bitmap bitmap1 = compressImage(bitmap);
                    is.close();
                    qrCode = CodeUtils.createQRCodeWithLogo(shareUrl, bitmap1);
                    bitmap2 = compressImage(qrCode);
                    getZKDocument().getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(getContext())
                                    .asBitmap()
                                    .load(bitmap2)
                                    .into(img_code);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return bitmap;
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    
}