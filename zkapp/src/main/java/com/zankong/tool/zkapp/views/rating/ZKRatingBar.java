package com.zankong.tool.zkapp.views.rating;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zankong.tool.zkapp.R;

import java.math.BigDecimal;

/**
 * Created by YF on 2018/7/17.
 */

public class ZKRatingBar extends LinearLayout {
    private boolean mClickable;
    private boolean halfstart;
    private int starCount;
    private int starNum;
    private OnRatingChangeListener onRatingChangeListener;
    private float starImageSize;
    private float starImageWidth;
    private float starImageHeight;
    private float starImagePadding;
    private Drawable starEmptyDrawable;
    private Drawable starFillDrawable;
    private Drawable starHalfDrawable;
    private int y = 1;
    private boolean isEmpty=true;
    private float ZKStar;

    public float getZKStar() {
        return ZKStar;
    }

    public void setStarHalfDrawable(Drawable starHalfDrawable) {
        this.starHalfDrawable = starHalfDrawable;
    }


    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
        this.onRatingChangeListener = onRatingChangeListener;
    }

    public void setmClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    public void halfStar(boolean halfstart) {
        this.halfstart = halfstart;
    }

    public void setStarFillDrawable(Drawable starFillDrawable) {
        this.starFillDrawable = starFillDrawable;
    }

    public void setStarEmptyDrawable(Drawable starEmptyDrawable) {
        this.starEmptyDrawable = starEmptyDrawable;
    }

    public void setStarImageSize(float starImageSize) {
        this.starImageSize = starImageSize;
    }

    public void setStarImageWidth(float starImageWidth) {
        this.starImageWidth = starImageWidth;
    }

    public void setStarImageHeight(float starImageHeight) {
        this.starImageHeight = starImageHeight;
    }


    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public void setImagePadding(float starImagePadding) {
        this.starImagePadding = starImagePadding;
    }


    public ZKRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        starImageSize = 120;
        starImageWidth = 20;
        starImageHeight = 20;
        starImagePadding = 15;
        starCount = 5;
        starNum = 0;
        mClickable = true;
        halfstart = false;
        starEmptyDrawable = context.getResources().getDrawable(R.drawable.empty_star);
        starFillDrawable = context.getResources().getDrawable(R.drawable.full_star);
        starHalfDrawable = context.getResources().getDrawable(R.drawable.half_star);
        init(context);
    }

    public void init(Context context){
        removeAllViews();
        for (int i = 0; i < starNum; ++i) {
            ImageView imageView = getStarImageView(context,false);
            addView(imageView);
        }

        for (int i = 0; i < starCount; ++i) {
            ImageView imageView = getStarImageView(context,isEmpty);
            imageView.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mClickable) {
                                if (halfstart) {
                                    //TODO:This is not the best way to solve half a star,
                                    //TODO:but That's what I can do,Please let me know if you have a better solution
                                    if (y % 2 == 0) {
                                        ZKStar = indexOfChild(v) + 1f;
                                    } else {
                                        ZKStar = indexOfChild(v) + 0.5f;
                                    }
                                    setStar(ZKStar);
                                    if (onRatingChangeListener != null) {
                                        onRatingChangeListener.onRatingChange(ZKStar);
                                        y++;
                                    }
                                } else {
                                    ZKStar = indexOfChild(v) + 1f;
                                    setStar(ZKStar);
                                    if (onRatingChangeListener != null) {
                                        onRatingChangeListener.onRatingChange(ZKStar);
                                    }
                                }
                            }
                        }
                    }
            );
            addView(imageView);
        }
    }


    private ImageView getStarImageView(Context context,boolean isEmpty) {
        ImageView imageView = new ImageView(context);
        ViewGroup.LayoutParams para = new ViewGroup.LayoutParams(
                Math.round(starImageWidth),
                Math.round(starImageHeight)
        );
        imageView.setLayoutParams(para);
        imageView.setPadding(0, 0, Math.round(starImagePadding), 0);
        if(isEmpty){
            imageView.setImageDrawable(starEmptyDrawable);
        }else{
            imageView.setImageDrawable(starFillDrawable);
        }
        return imageView;
    }

    public void setStar(float starCount) {
        int fint = (int) starCount;
        BigDecimal b1 = new BigDecimal(Float.toString(starCount));
        BigDecimal b2 = new BigDecimal(Integer.toString(fint));
        float fPoint = b1.subtract(b2).floatValue();


        starCount = fint > this.starCount ? this.starCount : fint;
        starCount = starCount < 0 ? 0 : starCount;
        ZKStar = starCount;

        //drawfullstar
        for (int i = 0; i < starCount; ++i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
        }

        //drawhalfstar
        if (fPoint > 0) {
            ((ImageView) getChildAt(fint)).setImageDrawable(starHalfDrawable);

            //drawemptystar
            for (int i = this.starCount - 1; i >= starCount + 1; --i) {
                ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
            }

        } else {
            //drawemptystar
            for (int i = this.starCount - 1; i >= starCount; --i) {
                ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
            }

        }
    }

    /**
     * change start listener
     */
    public interface OnRatingChangeListener {

        void onRatingChange(float RatingCount);

    }

}
