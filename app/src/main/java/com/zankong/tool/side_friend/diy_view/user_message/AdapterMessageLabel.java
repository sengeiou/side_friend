package com.zankong.tool.side_friend.diy_view.user_message;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.zankong.tool.side_friend.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdapterMessageLabel extends RecyclerView.Adapter<AdapterMessageLabel.ViewHodler> {

    private List<String> tvs;
    private Context mContext;
    private List<Map<Object, Object>> list;

    private OnItemClickListener onItemClickListener;

    public AdapterMessageLabel(Context context, List<Map<Object, Object>> list) {
        this.mContext = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.message_label_item, viewGroup, false);
        return new ViewHodler(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler viewHodler, int i) {
        if (viewHodler instanceof ViewHodler) {
            if (tvs == null) {
                tvs = new ArrayList<>();
            }
            Map<Object, Object> objectMap = list.get(i);
            boolean aBoolean = (boolean) objectMap.get("boolean");
            viewHodler.check_box.setText(objectMap.get("text").toString());
            if (aBoolean)
                viewHodler.check_box.setChecked(true);
            else
                viewHodler.check_box.setChecked(false);


            viewHodler.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        tvs.clear();
                        for (int i = 0; i < list.size(); i++) {
                            if ((boolean) list.get(i).get("boolean")) {
                                tvs.add(list.get(i).get("text").toString());
                            }
                        }
                        if (tvs.size() == 5) {
                            viewHodler.check_box.setChecked(false);
                            list.get(i).put("boolean", false);
                            Toast.makeText(mContext, "最多选取5个标签", Toast.LENGTH_SHORT).show();
                        } else {
                            viewHodler.check_box.setChecked(true);
                            list.get(i).put("boolean", true);
                        }

                    } else {
                        viewHodler.check_box.setChecked(false);
                        list.get(i).put("boolean", false);
                    }
                    tvs.clear();
                    for (int j = 0; j < list.size(); j++) {
                        Map<Object, Object> objectMap1 = list.get(j);
                        if ((boolean) objectMap1.get("boolean") == true) {
                            String text = objectMap1.get("text").toString();
                            tvs.add(text);
                        }
                    }
                    onItemClickListener.onClick(tvs);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder {
        private CheckBox check_box;

        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            check_box = itemView.findViewById(R.id.check_box);
        }
    }

    public interface OnItemClickListener {
        void onClick(List<String> str);
    }

    public void onRefresh(List<Map<Object, Object>> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
