package com.example.dotua.goldendictbridge;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dotua on 30-Jun-16.
 */
public class DirectTranslate_ScrollingCardViewBehavior extends CoordinatorLayout.Behavior<CardView> {
    private int toolbarHeight;

    public DirectTranslate_ScrollingCardViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.toolbarHeight = getToolbarHeight(context);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CardView cardView, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CardView cardView, View dependency) {
        if (dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) cardView.getLayoutParams();
            int cardViewBottomMargin = lp.bottomMargin;
            int distanceToScroll = cardView.getHeight() + cardViewBottomMargin;
            float ratio = (float)dependency.getY()/(float)toolbarHeight;
            cardView.setTranslationY(-distanceToScroll * ratio);
        }
        return true;
    }

    public int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

//    public int getTabsHeight(Context context) {
//        return (int) context.getResources().getDimension(R.dimen.tabsHeight);
//    }
}
