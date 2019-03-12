package com.zankong.tool.zkapp.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.util.keyword.ZKKeywordHelper;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YF on 2018/6/21.
 */

public class Util {
    /**
     * 打印log日志
     */
    public static void log(String text){
        log("zkTool",text);
    }
    public static void log(String tag,String text){
        Log.d(tag,text);
    };
//    public static void log(String tag,Object text){
//        String value = "";
//        if (text instanceof String){
//            value = (String) text;
//        }else if (text instanceof Integer){
//            value = text + "";
//        }else if (text instanceof Element){
//            value = ((Element) text).asXML();
//        }else if (text instanceof V8Object){
//            value = V8Utils.js2string(text);
//        }else if (text instanceof Double){
//            value = text + "";
//        }else if (text instanceof Boolean){
//            value = text +"";
//        }else if (text instanceof Float){
//            value = text + "";
//        }else {
//            value = text +"";
//        }
//        Log.d(tag, value);
//    }


    /**
     * 得到assets目录下的文件
     */
    public static String getFileFromAssets(String path) throws IOException {
        path = getPath(path);
        InputStream in = ZKToolApi.getInstance().getContext().getAssets().open(path);
        int size = in.available();
        byte[] buffer = new byte[size];
        in.read(buffer);
        in.close();
        return new String(buffer, "utf8");
    }

    /**
     * 把带'.'的路径转换成'/'
     */
    public static String getPath(String fileName) {
        if (fileName.contains("http://") || fileName.contains("https://")){
            return fileName;
        }
        StringBuilder sb = new StringBuilder();
        if (fileName.contains(".")) {
            String[] catalog = fileName.split("\\.");
            for (int i = 0; i < catalog.length - 2; i++) {
                sb.append(catalog[i]);
                sb.append("/");
            }
            sb.append(catalog[catalog.length - 2]);
            sb.append(".");
            sb.append(catalog[catalog.length - 1]);
        }
        return sb.toString();
    }

    /**
     * android的吐司
     */
    private static Toast mToast;
    public static void showT(String s) {
        if (mToast == null) {
            mToast = Toast.makeText(ZKToolApi.getInstance().getContext(), s, Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setText(s);
            mToast.show();
        }
    }

    /**
     * 获得文件夹的大小
     */
    public static long getFolderSize(File file) throws Exception{
        long size = 0;
        try{
            File[] files = file.listFiles();
            if (files != null){
                for (File childFile : files) {
                    if (childFile.isDirectory()) {
                        size += getFolderSize(childFile);
                    } else {
                        size += childFile.length();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除文件
     */
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            for (String s : dir.list()) {
                if (deleteDir(new File(dir,s))) {
                    return false;
                }
            }
        }
        return dir == null || dir.delete();
    }

    /**
     * 根据手机的分辨率从 dp(像素) 的单位 转成为 px
     */
    public static int dip2px(float dpValue) {
        float scale = ZKToolApi.getInstance().getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 根据手机的屏幕的宽度
     */

    public static int with2px(){
        Resources resources =  ZKToolApi.getInstance().getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;
        return dm.widthPixels;

    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        float scale = ZKToolApi.getInstance().getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2px(String px){
        if (px.contains("px")){
            return Integer.parseInt(px.split("px")[0]);
        }else if (px.contains("dp")){
            float scale = ZKToolApi.getInstance().getContext().getResources().getDisplayMetrics().density;
            return (int) (Float.parseFloat(px.split("dp")[0]) * scale + 0.5f);
        }else if(px.contains("keyword")){
            int keyword = 0;
            if (ZKKeywordHelper.keywordHeight == 0) {
                keyword = ZKToolApi.runtime.executeIntegerScript("localStorage.get('keywordHeight',0)");
            }
            if(keyword == 0){
                keyword = (int) (ZKToolApi.getInstance().getContext().getResources().getDisplayMetrics().density*333+0.5f);
            }
            return keyword;
        }else if (px.contains("bar")){
            return getStatusBarHeight(ZKToolApi.getInstance().getTopActivity());
        }
        float dpValue = Float.parseFloat(px) / 3;
        if (dpValue < 0){
            return Integer.parseInt(px);
        }
        float scale = ZKToolApi.getInstance().getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    };
    public static int px2px(String px,int max){
        if (px.contains("px")){
            return Integer.parseInt(px.split("px")[0]);
        }else if (px.contains("dp")){
            float scale = ZKToolApi.getInstance().getContext().getResources().getDisplayMetrics().density;
            return (int) (Float.parseFloat(px.split("dp")[0]) * scale + 0.5f);
        }else if(px.contains("keyword")){
            int keyword = 0;
            if (ZKKeywordHelper.keywordHeight == 0) {
                keyword = ZKToolApi.runtime.executeIntegerScript("localStorage.get('keywordHeight',0)");
            }
            if(keyword == 0){
                keyword = (int) (ZKToolApi.getInstance().getContext().getResources().getDisplayMetrics().density*333+0.5f);
            }
            return keyword;
        }else if (px.contains("bar")){
            return getStatusBarHeight(ZKToolApi.getInstance().getTopActivity());
        }
        float dpValue = Float.parseFloat(px);
        if (dpValue == -1){
            return max;
        }else if (dpValue == -2){
            return Integer.parseInt(px);
        }else if (dpValue > 0 && dpValue < 1){
            return (int) (dpValue * max);
        }
        float scale = ZKToolApi.getInstance().getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue / 3 * scale + 0.5f);
    };


    public static int getInt(String s){
        return (int)Float.parseFloat(s.trim());
    }

    /**
     * 判断是否移动到布局外面了
     */
    public static boolean isInside(View view, float x, float y) {
        if (view == null) {
            return false;
        }
        //获取控件在屏幕的位置
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        //控件相对于屏幕的x与y坐标
        int xScreen = location[0];
        int yScreen = location[1];
        Rect rect = new Rect(xScreen,
                yScreen,
                xScreen + view.getWidth(),
                yScreen + view.getHeight());
        return rect.contains((int) x, (int) y);
    }

    /**
     * 保存bitmap
     */
    public static String saveBitmap(Bitmap bm, String picName) {
        File appDir = new File(Environment.getExternalStorageDirectory(), picName);
        if (!appDir.exists())
            appDir.mkdir();
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    /**
     * 根据路径得到element
     */
    public static Element getElementByPath(String fileName) {
        if (fileName == null) return null;
        Element element = null;
        fileName = Util.getPath(fileName);
        InputStream inputStream = null;
        SAXReader reader = new SAXReader();
        try {
            inputStream = ZKToolApi.getInstance().getContext().getAssets().open(fileName);
            Document document = reader.read(inputStream);
            element = document.getRootElement();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return element;
    }

    public interface OnGetHttpElement{
        void get(@Nullable Element element);
    }

    //根据网络路径得到element
    public static void getElementByHttp(String fileName,OnGetHttpElement onGetHttpElement){
        if (fileName.contains("http://") || fileName.contains("https://")) {
            Request request = new Request.Builder().url(fileName).get().build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Util.log("http:element","fail");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String s = response.body().string();
                                ZKToolApi.getInstance().getJsHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            onGetHttpElement.get(DocumentHelper.parseText(s).getRootElement());
                                        } catch (DocumentException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });

        }
    }

    //根据名字获得assert下的图片，有后缀
    public static Bitmap getImageFromAssetsFile(String fileName) {
        fileName = getPath(fileName);
        Bitmap bitmap = null;
        AssetManager assetManager = ZKToolApi.getInstance().getContext().getResources().getAssets();
        try {
            InputStream inputStream = assetManager.open(fileName);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;//图片宽高都为原来的二分之一，即图片为原来的四分之一
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inPurgeable = true;
            options.inInputShareable = true;
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }



    public static Bitmap  drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }


    
    //获得标签下所有的指定标签
    public static ArrayList<Element> getChildElement(Element element, String name){
        ArrayList<Element> list = new ArrayList<>();
        for (Element child : element.elements()) {
            if(child.getName().equals(name)){
                list.add(child);
            }else {
                list.addAll(getChildElement(child,name));
            }
        }
        return list;
    }

    //获得状态栏高度
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Class<?> c;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     *
     * @param context
     * @param spannableString
     * @param patten
     * @param start
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws NumberFormatException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static void dealExpression(Context context, SpannableString spannableString, Pattern patten, int start) throws SecurityException, NoSuchFieldException, NumberFormatException, IllegalArgumentException, IllegalAccessException, IOException {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            if (matcher.start() < start) {
                continue;
            }
            //            Field field = R.drawable.class.getDeclaredField(key);
            //            int resId = Integer.parseInt(field.get(null).toString());       //通过上面匹配得到的字符串来生成图片资源id
            if (key != null) {
                key = key.replace("#[", "");
                key = key.replace("]#", "");
                //                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
                Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open(key));
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                // 设置想要的大小
                int newWidth = 60;
                int newHeight = 60;
                // 计算缩放比例
                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;
                // 取得想要缩放的matrix参数
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                // 得到新的图片
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                ImageSpan imageSpan = new ImageSpan(context, bitmap);                //通过图片资源id来得到bitmap，用一个ImageSpan来包装
                int end = matcher.start() + key.length() + 4;                   //计算该图片名字的长度，也就是要替换的字符串的长度
                spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);   //将该图片替换字符串中规定的位置中
                if (end < spannableString.length()) {                        //如果整个字符串还未验证完，则继续。。
                    dealExpression(context, spannableString, patten, end);
                }
                break;
            }
        }
    }

    /**
     * 得到一个SpanableString对象，通过传入的字符串,并进行正则判断
     *
     * @param context
     * @param str
     * @return
     */
    public static SpannableString getExpressionString(Context context, String str, String zhengze) {

        SpannableString spannableString = new SpannableString(str);
        @SuppressLint("WrongConstant") Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);        //通过传入的正则表达式来生成一个pattern
        try {
            dealExpression(context, spannableString, sinaPatten, 0);
        } catch (Exception e) {
            Log.e("Dom.getExpressionString", e.getMessage());
        }
        return spannableString;
    }

}
