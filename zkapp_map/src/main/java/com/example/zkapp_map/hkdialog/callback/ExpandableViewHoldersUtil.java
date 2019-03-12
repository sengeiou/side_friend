package com.example.zkapp_map.hkdialog.callback;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.zkapp_map.hkdialog.adapter.ItemRecyclerViewAdapter;

public class ExpandableViewHoldersUtil {

    public static void openH(final RecyclerView.ViewHolder holder, final View expandView, final boolean animate) {
        if (animate) {
            expandView.setVisibility(View.VISIBLE);
            final Animator animator = ViewHolderAnimator.ofItemViewHeight(holder);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd(Animator animation) {
                    final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(expandView, View.ALPHA, 1);
                    alphaAnimator.addListener(new ViewHolderAnimator.ViewHolderAnimatorListener(holder));
                    alphaAnimator.start();
                }
            });
            animator.start();
        }
        else {
            expandView.setVisibility(View.VISIBLE);
            expandView.setAlpha(1);
        }
    }

    public static void closeH(final RecyclerView.ViewHolder holder, final View expandView, final boolean animate) {
        if (animate) {
            expandView.setVisibility(View.GONE);
            final Animator animator = ViewHolderAnimator.ofItemViewHeight(holder);
            expandView.setVisibility(View.VISIBLE);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd(Animator animation) {
                    expandView.setVisibility(View.GONE);
                    expandView.setAlpha(0);
                }
                @Override public void onAnimationCancel(Animator animation) {
                    expandView.setVisibility(View.GONE);
                    expandView.setAlpha(0);
                }
            });
            animator.start();
        }
        else {
            expandView.setVisibility(View.GONE);
            expandView.setAlpha(0);
        }
    }

    public static interface Expandable {
        public View getExpandView();
    }

    public static class KeepOneH<ItemVH extends RecyclerView.ViewHolder & Expandable> {
        private int _opened = -1;
        private int _i = -1;
        private int sPosition = -1;
        public void bind(ItemRecyclerViewAdapter.ItemVH holder, int pos) {
            if (pos == _opened) {
                ExpandableViewHoldersUtil.openH(holder, holder.getExpandView(), false);
            }else {
                ExpandableViewHoldersUtil.closeH(holder, holder.getExpandView(), false);
            }
        }


        /**
         * 隐藏除点击之外的所有
         */
        public ItemRecyclerViewAdapter.ItemVH hide(ItemRecyclerViewAdapter.ItemVH oldHolder){
            if (oldHolder != null){
                ExpandableViewHoldersUtil.closeH(oldHolder, oldHolder.getExpandView(), true);
                _opened = -1;
                _i = -1;
                sPosition = -1;
            }
            return null;
        };

        @SuppressWarnings("unchecked")
        public ItemRecyclerViewAdapter.ItemVH toggle(ItemRecyclerViewAdapter.ItemVH holder,int i,int position) {
            if (_opened == holder.getPosition()) {
                if (_i != i){
                    _opened = -1;
                    ExpandableViewHoldersUtil.closeH(holder, holder.getExpandView(), true);
                }else if (sPosition == position){
                    _opened = -1;
                    ExpandableViewHoldersUtil.closeH(holder, holder.getExpandView(), true);
                }else {
                    _opened = -1;
                    return holder;
                }
                sPosition = position;
            }
            else {
                int previous = _opened;
                int s = _i;
                _i = i;
                sPosition = position;
                _opened = holder.getPosition();
                ExpandableViewHoldersUtil.openH(holder, holder.getExpandView(), true);
                final ItemRecyclerViewAdapter.ItemVH oldHolder = (ItemRecyclerViewAdapter.ItemVH) ((RecyclerView) holder.itemView.getParent()).findViewHolderForPosition(previous);
                if (oldHolder != null)
                    ExpandableViewHoldersUtil.closeH(oldHolder, oldHolder.getExpandView(), true);
                return holder;
            }
            return null;
        }
    }

}
