package com.zankong.tool.zkapp.views.picker;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.document.ZKDocument;
import com.zankong.tool.zkapp.views.InnerListview;
import com.zankong.tool.zkapp.views.MyBaseAdapter;
import com.zankong.tool.zkapp.views.ZKViewAgent;

import java.util.ArrayList;
import java.util.List;



public class PickerView2_i {
    private LayoutInflater inflater;
    private int count;//默认显示的条目（奇数）
    private int height;//每个item的高度
    private int width;//控件的宽度
    private boolean isScroll = true;//是否需要滑动到中间位置
    private boolean isDown = false;//是否向下滑动
    private boolean isUp = false;//是否向上滑动
    private float downY;//当前按下的坐标Y
    private int selectedCount;//当前选中的item；
    public List<String> dataS;//数据集合
    private PickerView2Adapter pickerView2Adapter;//适配器
    private int nowCount = 0;//当前居顶的第？item
    private int nowCount2 = -2;//当前居顶的第？item
    private float nowY;//当前的坐标Y
    private boolean isFirstUp = false;//下滑的时候手指未离开屏幕情况下上滑
    private boolean isFirstDown = false;//上滑的时候手指未离开屏幕情况下下滑
    public PickerViewBean data;
    private LinearLayout layouy_background;
    private float Y;
    private ZKActivity mActivity;
    private InnerListview listView;
    private V8Object document;
    private V8 mV8;
    public V8Function onClickListenerJS = null;
    public String val;


    public PickerView2Adapter getPickerView2Adapter() {
        return pickerView2Adapter;
    }

    public PickerView2_i(ZKActivity mActivity, LayoutInflater inflater, PickerViewBean data, LinearLayout layouy_background, int count, V8Object document, ZKDocument zkDocument, V8Object thisV8) {
        this.mActivity = mActivity;
        this.inflater = inflater;
        this.data = data;
        this.layouy_background = layouy_background;
        height = (int) (40 * mActivity.getResources().getDisplayMetrics().density);
        selectedCount = count / 2;//当前选中的条目
        this.count = count;
        initView();
    }

    public PickerView2_i(ZKActivity mActivity, LayoutInflater inflater, PickerViewBean data, LinearLayout layouy_background, int count, V8Object document, V8 mV8) {
        this.mActivity = mActivity;
        this.inflater = inflater;
        this.document = document;
        this.mV8 = mV8;
        this.data = data;
        this.layouy_background = layouy_background;
        height = (int) (40 *  mActivity.getResources().getDisplayMetrics().density);
        selectedCount = count / 2;//当前选中的条目
        this.count = count;
        initView();
    }

    public void initView() {
        View v = inflater.inflate(R.layout.item_picker, null, false);
        listView = ((InnerListview) v.findViewById(R.id.pick_listView));
        TextView topLine = ((TextView) v.findViewById(R.id.pick_topLine));
        TextView bottomLine = ((TextView) v.findViewById(R.id.pick_bottomLine));//上下横线
        RelativeLayout allLinearLayout = ((RelativeLayout) v.findViewById(R.id.pickView_allLinearLayout));//listview的父布局
        RelativeLayout textRelativeLayout = (RelativeLayout) v.findViewById(R.id.picker_text_relativeLayout);//textview的父布局
        //设置listview父布局的高，宽,根据item高跟显示个数来调整控件高,
        ViewGroup.LayoutParams layoutParams3 = allLinearLayout.getLayoutParams();
        if (data.getWidth() != 0)//宽
            layoutParams3.width = data.getWidth();
        else
            layoutParams3.width = (int) (120 * mActivity.getResources().getDisplayMetrics().density);
        if (data.getHeight() != 0)//高
            layoutParams3.height = data.getHeight() * count;
        else
            layoutParams3.height = height * count;
        allLinearLayout.setLayoutParams(layoutParams3);
        //调整上下线的属性
        if (data.isLine()) {
            String lineColor = data.getLineColor();
            //设置颜色。默认为红色
            if (lineColor != null) {
                topLine.setBackgroundColor(Color.parseColor(lineColor));
                bottomLine.setBackgroundColor(Color.parseColor(lineColor));
            }
            //设置高度
            ViewGroup.LayoutParams layoutParams1 = topLine.getLayoutParams();
            ViewGroup.LayoutParams layoutParams2 = bottomLine.getLayoutParams();
            int lineHeight = data.getLineHeight();
            if (lineHeight != 0) {
                layoutParams1.height = lineHeight;
                layoutParams2.height = lineHeight;
            }
            topLine.setLayoutParams(layoutParams1);
            bottomLine.setLayoutParams(layoutParams2);
            //设置线的宽度跟距离高度
            ViewGroup.LayoutParams layoutParams = textRelativeLayout.getLayoutParams();
            int lineWidth = data.getLineWidth();
            if (lineWidth != 0) {
                layoutParams.width = lineWidth;
            }
            if (data.getHeight() != 0) {
                layoutParams.height = data.getHeight();
            }
            textRelativeLayout.setLayoutParams(layoutParams);
        } else {
            topLine.setVisibility(View.GONE);
            bottomLine.setVisibility(View.GONE);
        }

        pickerView2Adapter = new PickerView2Adapter(mActivity);//适配器初始化
        listView.setAdapter(pickerView2Adapter);//绑定适配器
        if (data.getText()!=null) {
            dataS = new ArrayList<>();//数据集合初始化
            for (int i1 = 0; i1 < count / 2; i1++) {
                dataS.add("");
            }
            String[] textS = data.getText();
            for (String s : textS) {
                dataS.add(s);
            }
            for (int i1 = 0; i1 < count / 2; i1++) {
                dataS.add("");
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setSelection(dataS.size() / 2 - count / 2);
                            selectedCount = dataS.size() / 2;
                            pickerView2Adapter.addAll(dataS);//绑定数据
                            pickerView2Adapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
        }
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        //当滑动停止时候，做出判断，选择占比例大的item条目居中
                        if ((-view.getChildAt(0).getTop()) <= view.getChildAt(0).getHeight() / 2) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            view.smoothScrollBy(view.getChildAt(0).getTop(), 300);//需要滑动的距离
                                            selectedCount = nowCount + count / 2;//居中的第？条目
                                            pickerView2Adapter.notifyDataSetChanged();//适配器刷新
                                            isScroll = false;//是否需要滑动居中为false；
                                            nowCount2 = nowCount;
                                        }
                                    });
                                }
                            }).start();
                        }
                        if ((-view.getChildAt(0).getTop()) > (view.getChildAt(0).getHeight() / 2)) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    mActivity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            view.smoothScrollBy((view.getChildAt(0).getHeight() + view.getChildAt(0).getTop()), 300);//需要滑动的距离
                                            selectedCount = nowCount + count / 2 + 1;//居中的第？条目
                                            pickerView2Adapter.notifyDataSetChanged();//适配器刷新
                                            isScroll = false;//是否需要滑动居中为false；
                                            nowCount2 = nowCount;
                                        }
                                    });
                                }
                            }).start();
                        }
                        break;
                    case SCROLL_STATE_FLING:
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                nowCount = firstVisibleItem;
                if (nowCount != nowCount2) {
                    isScroll = true;
                }
                if (view.getChildAt(0) != null) {
                    if ((firstVisibleItem == 0 && Y >= 0) || (visibleItemCount == totalItemCount && Y <= 0)) {
                        //需要滑动居中为true
                    } else if (isScroll) {
                        //手指向上滑动，listview的当前item的大小增加，当item距离滑动2/3，上面的item的字体变大
                        if (isUp) {
                            if ((-view.getChildAt(0).getTop()) >= (view.getChildAt(0).getHeight() / 2)) {
                                selectedCount = firstVisibleItem + count / 2 + 1;
                                isScroll = false;
                                nowCount2 = firstVisibleItem;
                                pickerView2Adapter.notifyDataSetChanged();
                                downY = nowY;
                                isFirstDown = true;
                            } else if (isFirstUp) {
                                selectedCount = firstVisibleItem + count / 2;
                                nowCount2 = firstVisibleItem;
                                pickerView2Adapter.notifyDataSetChanged();
                                isFirstUp = false;
                            }
                        }
                        //手指向下滑动，listview的当前item的大小减小，当item距离滑动2/3，下面的item的字体变大
                        if (isDown) {
                            if ((-view.getChildAt(0).getTop()) <= (view.getChildAt(0).getHeight() / 2)) {
                                selectedCount = firstVisibleItem + count / 2;
                                isScroll = false;
                                nowCount2 = firstVisibleItem;
                                pickerView2Adapter.notifyDataSetChanged();
                                downY = nowY;
                                isFirstUp = true;
                            } else if (isFirstDown) {
                                selectedCount = firstVisibleItem + count / 2 + 1;
                                nowCount2 = firstVisibleItem;
                                pickerView2Adapter.notifyDataSetChanged();
                                isFirstDown = false;
                            }
                        }
                    }
                }
            }
        });//滑动监听
        onTouchEvent(listView);//触屏幕触摸事件
        layouy_background.addView(v);
    }

    //触屏幕触摸事件
    private void onTouchEvent(InnerListview listView) {
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //各属性初始化
                        isDown = false;
                        isUp = false;
                        isFirstUp = false;
                        isFirstDown = false;
                        isScroll = true;//需要滑动居中为true
                        downY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        nowY = event.getY();
                        Y = event.getY() - downY;
                        if (Y > 0) {//手指向下滑动，listview的当前item的大小减小
                            isDown = true;
                            isUp = false;
                        }
                        if (Y < 0) {//手指向上滑动，listview的当前item的大小增大
                            isDown = false;
                            isUp = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
    }

    //适配器
    public class PickerView2Adapter extends MyBaseAdapter<String> {
        public PickerView2Adapter(Context context) {
            super(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getInflater().inflate(R.layout.item_pickviewitem, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            convertView.setEnabled(false);
            viewHolder.textView.setText(getItem(position));
            int height = data.getHeight();
            if (height != 0) {
                ViewGroup.LayoutParams layoutParams = viewHolder.textView.getLayoutParams();
                layoutParams.height = data.getHeight();
                viewHolder.textView.setLayoutParams(layoutParams);
            }
            if (position == selectedCount) {
                if (data.getTextSize() != 0)
                    viewHolder.textView.setTextSize(data.getTextSize());
                else
                    viewHolder.textView.setTextSize(18);
                if (data.getTextColor() != null)
                    viewHolder.textView.setTextColor(Color.parseColor(data.getTextColor()));
                else
                    viewHolder.textView.setTextColor(Color.parseColor("#464646"));
            } else if (position == selectedCount - 1 || position == selectedCount + 1) {
                if (data.getTextSize() != 0) {
                    if (data.getTextSize() - 4 >= 10) {
                        viewHolder.textView.setTextSize(data.getTextSize() - 4);
                    } else {
                        viewHolder.textView.setTextSize(10);
                    }
                } else {
                    viewHolder.textView.setTextSize(14);
                }
                if (data.getTextColor() != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(data.getTextColor());
                    StringBuilder aa = stringBuilder.insert(1, "bb");
                    viewHolder.textView.setTextColor(Color.parseColor(aa.toString()));
                } else
                    viewHolder.textView.setTextColor(Color.parseColor("#bb464646"));
            } else if (position == selectedCount - 2 || position == selectedCount + 2) {
                if (data.getTextSize() != 0) {
                    if (data.getTextSize() - 6 >= 8) {
                        viewHolder.textView.setTextSize(data.getTextSize() - 6);
                    } else {
                        viewHolder.textView.setTextSize(8);
                    }
                } else {
                    viewHolder.textView.setTextSize(10);
                }
                if (data.getTextColor() != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(data.getTextColor());
                    StringBuilder aa = stringBuilder.insert(1, "88");
                    viewHolder.textView.setTextColor(Color.parseColor(aa.toString()));
                } else
                    viewHolder.textView.setTextColor(Color.parseColor("#88464646"));
            } else {
                if (data.getTextSize() != 0) {
                    if (data.getTextSize() - 6 >= 6) {
                        viewHolder.textView.setTextSize(data.getTextSize() - 6);
                    } else {
                        viewHolder.textView.setTextSize(6);
                    }
                } else {
                    viewHolder.textView.setTextSize(6);
                }
                if (data.getTextColor() != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(data.getTextColor());
                    StringBuilder aa = stringBuilder.insert(1, "44");
                    viewHolder.textView.setTextColor(Color.parseColor(aa.toString()));
                } else
                    viewHolder.textView.setTextColor(Color.parseColor("#88464646"));
            }
            if (position == selectedCount) {
                val = viewHolder.textView.getText().toString();
                setVal(val);
            }
            return convertView;
        }

        private class ViewHolder {
            private TextView textView;

            public ViewHolder(View convertView) {
                textView = (TextView) convertView.findViewById(R.id.pickerView_item_tv);
            }
        }

        public void refresh(int selectedCount) {
            notifyDataSetChanged();
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }

    public String getVal() {
        return val;
    }


    public void setVal(String val) {
        this.val = val;
    }
}
