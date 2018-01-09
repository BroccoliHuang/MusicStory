package org.metol.musicstory.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.metol.musicstory.R;

/**
 * Created by Broccoli.Huang on 2018/1/5.
 */

public class CategoryButtons extends HorizontalScrollView {
    Context cnx;
    LinearLayout ll_category;
    Callback_CategoryButtons callback_CategoryButtons;

    public CategoryButtons(Context context) {
        super(context);
        this.cnx = context;
    }

    public CategoryButtons(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cnx = context;
        setBackgroundColor(getResources().getColor(R.color.categorybuttons_background));
    }

    public void setCategory(String[] sa_category, int index_category, final Callback_CategoryButtons callback_CategoryButtons) {
        if(sa_category==null || sa_category.length==0) return;

        this.callback_CategoryButtons = callback_CategoryButtons;

        LinearLayout.LayoutParams layoutParams_WrapContent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParams_tv_category = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams_tv_category.setMargins((int)getPixelByDP(14), (int)getPixelByDP(14), (int)getPixelByDP(0), (int)getPixelByDP(14));

        ll_category = new LinearLayout(cnx);
        ll_category.setOrientation(LinearLayout.HORIZONTAL);
        ll_category.setLayoutParams(layoutParams_WrapContent);

        for(String category : sa_category) {
            TextView tv_category = new TextView(cnx);
            tv_category.setText(category);
            tv_category.setTextSize(14);
            tv_category.setLayoutParams(layoutParams_tv_category);
            tv_category.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback_CategoryButtons.onButtonClick(ll_category.indexOfChild(v));
                    paintCategory(ll_category.indexOfChild(v));
                }
            });
            ll_category.addView(tv_category);
        }

        this.setHorizontalScrollBarEnabled(false);
        this.setLayoutParams(layoutParams_WrapContent);
        this.addView(ll_category);

        if(ll_category!=null && ll_category.getChildCount()>0){
            paintCategory(index_category);
        }
    }

    private void paintCategory(int category){
        GradientDrawable background_pressed = new GradientDrawable();
        background_pressed.setShape(GradientDrawable.RECTANGLE);
        background_pressed.setColor(getResources().getColor(R.color.categorybuttons_pressed_button_background));
        background_pressed.setStroke((int)getPixelByDP(1), getResources().getColor(R.color.categorybuttons_pressed_button_background));
        background_pressed.setCornerRadius(90f);

        GradientDrawable background_not_pressed = new GradientDrawable();
        background_not_pressed.setShape(GradientDrawable.RECTANGLE);
        background_not_pressed.setStroke((int)getPixelByDP(1), getResources().getColor(R.color.categorybuttons_not_pressed_button_border));
        background_not_pressed.setCornerRadius(90f);

        for(int count=0;count<ll_category.getChildCount();count++){
            TextView tv_category = (TextView)ll_category.getChildAt(count);
            tv_category.setPadding((int)getPixelByDP(16), (int)getPixelByDP(6), (int)getPixelByDP(16), (int)getPixelByDP(6));
            if(count==category){
                tv_category.setBackground(background_pressed);
                tv_category.setTextColor(getResources().getColor(R.color.categorybuttons_pressed_button_font));
            }else{
                tv_category.setBackground(background_not_pressed);
                tv_category.setTextColor(getResources().getColor(R.color.categorybuttons_not_pressed_button_font));
            }
        }
    }

    private float getPixelByDP(float dp){
        return getResources().getDisplayMetrics().density * dp;
    }

    private float getPixelBySP(float sp){
        return getResources().getDisplayMetrics().scaledDensity * sp;
    }

    public interface Callback_CategoryButtons{
        void onButtonClick(int position);
    }
}
