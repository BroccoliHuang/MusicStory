package org.metol.musicstory.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

import com.melnykov.fab.FloatingActionButton;

/**
 * Created by Broccoli.Huang on 2018/1/3.
 */

public class FloatingActionButtonBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {
    public FloatingActionButtonBehavior(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        if(!child.isVisible()) return false;
        float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
        child.setTranslationY(translationY);
        return true;
    }
}
