package com.zankong.tool.side_friend.diy_view.evaluate;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zankong.tool.side_friend.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class AdapterLabel extends RecyclerView.Adapter<AdapterLabel.ViewHordle> {
    private Context mContext;
    private List<String> list;
    private Map<Object, Object> map;
    private Map<Integer, String> maps;
    private List<String> data;
    private ItemListener mItemListener;
    private List<Map<Object, Object>> mapList = new ArrayList<>();
    public AdapterLabel(Context mContext) {
        this.mContext = mContext;
    }
    
    public void onClickListener(ItemListener mItemListener) {
        this.mItemListener = mItemListener;
    }
    
    @NonNull
    @Override
    public ViewHordle onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.lable_tiem, viewGroup, false);
        return new ViewHordle(inflate);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHordle viewHordle, int i) {
        if (viewHordle instanceof ViewHordle) {
            viewHordle.tv.setText(list.get(i));
            boolean aBoolean = (boolean) mapList.get(i).get("boolean");
            if (aBoolean) {
                viewHordle.layout.setBackgroundResource(R.drawable.lable_item_btn2);
                viewHordle.tv.setTextColor(Color.parseColor("#ff0000"));
            } else {
                viewHordle.layout.setBackgroundResource(R.drawable.lable_item_btn);
                viewHordle.tv.setTextColor(Color.parseColor("#171718"));
            }
            if (maps == null) {
                maps = new HashMap<>();
            }
            viewHordle.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(boolean) mapList.get(i).get("boolean")) {
                        viewHordle.layout.setBackgroundResource(R.drawable.lable_item_btn2);
                        viewHordle.tv.setTextColor(Color.parseColor("#ff0000"));
                        mapList.get(i).put("boolean", true);
                        maps.put(i, mapList.get(i).get("content").toString());
                        mItemListener.data(maps);
                    } else {
                        viewHordle.layout.setBackgroundResource(R.drawable.lable_item_btn);
                        viewHordle.tv.setTextColor(Color.parseColor("#171718"));
                        mapList.get(i).put("boolean", false);
                        maps.remove(i);
                        mItemListener.data(maps);
                    }
                }
            });
        }
    }
    
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
    public class ViewHordle extends RecyclerView.ViewHolder {
        private final TextView tv;
        private final RelativeLayout layout;
        public ViewHordle(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
            layout = itemView.findViewById(R.id.layout);
        }
    }
    public void onRefresh(List<String> list) {
        this.list = list;
        mapList.clear();
        for (int i = 0; i < list.size(); i++) {
            map = new HashMap<>();
            map.put("boolean", false);
            map.put("content", list.get(i));
            mapList.add(map);
        }
        notifyDataSetChanged();
    }
    public interface ItemListener {
        void data(Map<Integer, String> map);
    }
    public void initMap(){
        if (mapList != null){
            for (int i = 0;i < mapList.size();i++){
                mapList.get(i).put("boolean",false);
            }
            if (maps != null){
                maps.clear();
            }
            notifyDataSetChanged();
        }
    }
}
