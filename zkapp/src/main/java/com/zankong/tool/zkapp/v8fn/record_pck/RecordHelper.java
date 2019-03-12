package com.zankong.tool.zkapp.v8fn.record_pck;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;

import com.zankong.tool.zkapp.ZKToolApi;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by YF on 2018/9/12.
 */

public class RecordHelper {
    MediaRecorder mRecorder;
    private static RecordHelper mInstance;
    //文件夹位置
    private String mDirString;
    //录音文件保存路径
    private String mCurrentFilePathString;
    //是否准备好开始录音
    private boolean isPrepared;

    public RecordHelper(String dirString) {
        mDirString = dirString;
        mDirString = getAppRecordDir().toString();
    }

    public static RecordHelper getInstance(String dirString) {
        if (mInstance == null) {
            synchronized (RecordHelper.class) {
                if (mInstance == null) {
                    mInstance = new RecordHelper(dirString);
                }
            }
        }
        return mInstance;
    }

    public void prepare() {
        try {
            isPrepared = false;
            File dir = new File(mDirString);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = UUID.randomUUID().toString() + ".amr";
            File file = new File(dir, fileName);
            mCurrentFilePathString = file.getAbsolutePath();
            mRecorder = new MediaRecorder();
            mRecorder.setOutputFile(file.getAbsolutePath());
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mRecorder.prepare();
                    mRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ZKToolApi.getInstance().getJsHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null) {
                            mListener.prepared();
                        }
                    }
                });
            }
        }).start();



    }

    public int getVoiceLevel(int maxLevel) {
        if (isPrepared) {
            return maxLevel * mRecorder.getMaxAmplitude() / 32768 + 1;
        }
        return 1;
    }

    public void release() {
        if (mRecorder != null) {
            try {
                mRecorder.setOnInfoListener(null);
                mRecorder.setOnErrorListener(null);
                mRecorder.setPreviewDisplay(null);
                mRecorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mRecorder.release();
            mRecorder = null;
            isPrepared = false;
        }
    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
        }
    }

    public void cancel() {
        release();
        if (mCurrentFilePathString != null) {
            File file = new File(mCurrentFilePathString);
            file.delete();
            mCurrentFilePathString = null;
        }
    }

    public String getCurrentFilePathString() {
        return mCurrentFilePathString;
    }


    private OnRecordStateListener mListener;


    public interface OnRecordStateListener {
        void prepared();
    }

    ;

    public void setOnRecordStateListener(OnRecordStateListener listener) {
        mListener = listener;
    }

    //获取录音存放路径
    public static File getAppRecordDir() {
        File appDir = getAppDir(ZKToolApi.getInstance().getContext());
        File recordDir = new File(appDir.getAbsolutePath(), FilePath.RECORD_DIR);
        if (!recordDir.exists()) {
            recordDir.mkdir();
        }
        return recordDir;
    }

    //获取文件存放根路径
    public static File getAppDir(Context context) {
        String dirPath = "";
        //SD卡是否存在
        boolean isSdCardExists = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        boolean isRootDirExists = Environment.getExternalStorageDirectory().exists();
        if (isSdCardExists && isRootDirExists) {
            dirPath = String.format("%s/%s/", Environment.getExternalStorageDirectory().getAbsolutePath(), FilePath.ROOT_PATH);
        } else {
            dirPath = String.format("%s/%s/", context.getApplicationContext().getFilesDir().getAbsolutePath(), FilePath.ROOT_PATH);
        }

        File appDir = new File(dirPath);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        return appDir;
    }

    //文件路径管理类
    public static class FilePath {
        public static final String ROOT_PATH = "wxr/";
        public static final String RECORD_DIR = "record/";
        public static final String RECORD_PATH = ROOT_PATH + RECORD_DIR;
    }

}
