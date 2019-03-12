package com.zankong.tool.side_friend.diy_view.home_screen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import com.zankong.tool.side_friend.R;
import java.util.List;
/**
 * @author Fsnzzz
 * @Created on 2018/11/22 0022 11:28
 */
public class HomeScreenAdapter extends RecyclerView.Adapter<HomeScreenAdapter.HomeScreenViewHordle> {
    private Context mContext;
    private ItemClickListener itemClickListener;
    private List<String> lists;
    private List<Integer> positions;
    public HomeScreenAdapter(Context context, List<String> lists,List<Integer> positions) {
        this.mContext = context;
        this.lists = lists;
        this.positions = positions;
    }

    public void setData(List<String> lists,List<Integer> positions){
        this.lists = lists;
        this.positions = positions;

    }

    public void onClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    @NonNull
    @Override
    public HomeScreenViewHordle onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.home_screen_item, viewGroup, false);
        return new HomeScreenViewHordle(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeScreenViewHordle viewHordle, int i) {
        if (viewHordle instanceof HomeScreenViewHordle){
            viewHordle.checkBox.setText(lists.get(i));
                if (positions.contains(i)){
                    viewHordle.checkBox.setChecked(true);
                }else {
                    viewHordle.checkBox.setChecked(false);
            }
            viewHordle.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        positions.clear();
                        positions.add(i);
                        itemClickListener.onData(i,viewHordle.checkBox.getText()+"");
                        viewHordle.checkBox.setChecked(true);
                        notifyDataSetChanged();
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }

    public class HomeScreenViewHordle extends RecyclerView.ViewHolder {

        private final CheckBox checkBox;
        private LinearLayout layout;

        public HomeScreenViewHordle(@NonNull View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.check_box);
            layout = (LinearLayout)itemView.findViewById(R.id.layout);
        }
    }


    public interface ItemClickListener{
        void onData(int postion, String str);
        void onCancel(int postion);
    }
    public void onCancelAll(){
        positions.clear();
    }
}
