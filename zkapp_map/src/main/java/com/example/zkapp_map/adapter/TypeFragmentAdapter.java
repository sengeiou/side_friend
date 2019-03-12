package com.example.zkapp_map.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zkapp_map.R;
import com.example.zkapp_map.bean.TestBean;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Fsnzzz
 * @date 2019/1/17
 * @month 01
 * @package com.fsnzzz.adapter
 */
public abstract class TypeFragmentAdapter extends RecyclerView.Adapter {

    private static final int TYPE_GROUP = 1;
    private static final int TYPE_CHILD = 2;

    private Context mContext;
    private ArrayList<TestBean> list;

    private int mItemCount;

    public abstract View setTitleView(ViewGroup parent);

    public abstract View setChildView(ViewGroup parent);

    public TypeFragmentAdapter(Context context, ArrayList<TestBean> list) {
        this.mContext = context;
        this.list = (ArrayList<TestBean>) list.clone();
        onDataItemCount();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_GROUP) {
            return new TestTitleViewHolder(setTitleView(parent));
        } else if (viewType == TYPE_CHILD) {
            return new TestChildViewHolder(setChildView(parent));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Position itemPosition = getGroupChildPosition(position);
        if (holder instanceof TestTitleViewHolder) {
            int group = itemPosition.group;
            TestBean group1 = getGroup(group);
            ((TestTitleViewHolder) holder).tv_title.setText(group1.getTitle() + "");
        } else if (holder instanceof TestChildViewHolder) {
            int group = itemPosition.group;
            String s = getGroup(group).getChildKind().get(itemPosition.child);
            ((TestChildViewHolder) holder).tv_group.setText(s);


            boolean a = false;
            ((TestChildViewHolder) holder).tv_group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((TestChildViewHolder) holder).rv_child.setVisibility(View.VISIBLE);
                }
            });
            ArrayList<String> test = new ArrayList<>();
            for (int i = 0 ; i < 8;i++){
                test.add(i+"");
            }
            TestAdapter adapter = new TestAdapter(mContext,test);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,4,LinearLayoutManager.VERTICAL,false);
            ((TestChildViewHolder) holder).rv_child.setLayoutManager(gridLayoutManager);
            ((TestChildViewHolder) holder).rv_child.setAdapter(adapter);
        }
    }

    public TestBean getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public class TestTitleViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_title;
        public TestTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }

    public class TestChildViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_group;
        private final RecyclerView rv_child;

        public TestChildViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_group = itemView.findViewById(R.id.tv_group);
            rv_child = itemView.findViewById(R.id.rv_child);
        }
    }

    private void onDataItemCount() {
        int count = 0;
        for (TestBean testBean : list) {
            count += testBean.getChildKind().size() + 1;
        }
        mItemCount = count;
    }


    @Override
    public int getItemViewType(int position) {
        return getItemType(position) == ItemType.GROUP_TITLE ? TYPE_GROUP : TYPE_CHILD;
    }


    
    public ItemType getItemType(final int itemPosition) {
        int count = 0;
        int childCount;
        for (TestBean g : list) {
            if (itemPosition == count) {
                return ItemType.GROUP_TITLE;
            }
            childCount = g.getChildKind().size();
            count += 1;
            if (itemPosition == count && childCount != 0) {
                return ItemType.FIRST_CHILD;
            }
            count += childCount;
            if (itemPosition < count) {
                return ItemType.NOT_FIRST_CHILD;
            }
        }
        throw new IllegalStateException("Could not find item type for item position " + itemPosition);
    }

    public enum ItemType {
        GROUP_TITLE,
        FIRST_CHILD,
        NOT_FIRST_CHILD
    }

    public static class Position {
        public int group;
        public int child = -1;
    }


    public Position getGroupChildPosition(int itemPosition) {
        int itemCount = 0;
        int childCount;
        final Position position = new Position();
        for (TestBean g : list) {
            if (itemPosition == itemCount) {
                position.child = -1;
                return position;
            }
            itemCount++;
            childCount = g.getChildKind().size();
            if (childCount > 0) {
                position.child = itemPosition - itemCount;
                if (position.child < childCount) {
                    return position;
                }
                itemCount += childCount;
            }
            position.group++;
        }
        return position;
    }
}
