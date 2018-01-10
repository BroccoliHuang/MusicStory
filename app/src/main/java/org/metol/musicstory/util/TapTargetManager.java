package org.metol.musicstory.util;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import org.metol.musicstory.R;

/**
 * Created by Broccoli.Huang on 2018/1/4.
 */

public class TapTargetManager {
    private TapTargetSequence tapTargetSequence;

    public TapTarget getTapTargetForView(View view, String title, String description){
        return TapTarget.forView(view, title, description)
                // All options below are optional
                .outerCircleColor(R.color.taptarget_outer_circle)      // Specify a color for the outer circle
                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                .targetCircleColor(R.color.taptarget_target_circle)   // Specify a color for the target circle
                .titleTextSize(24)                  // Specify the size (in sp) of the title text
                .titleTextColor(R.color.taptarget_title)      // Specify the color of the title text
                .descriptionTextSize(14)            // Specify the size (in sp) of the description text
                .descriptionTextColor(R.color.taptarget_description)  // Specify the color of the description text
//                        .textColor(android.R.color.white)            // Specify a color for both the title and description text
//                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                .dimColor(android.R.color.transparent)            // If set, will dim behind the view with 30% opacity of the given color
                .drawShadow(false)                   // Whether to draw a drop shadow or not
                .cancelable(true)                  // Whether tapping outside the outer circle dismisses the view
                .tintTarget(false)                   // Whether to tint the target view's color
                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
//                        .icon(Drawable)                     // Specify a custom drawable to draw as the target
                .targetRadius(60);                  // Specify the target radius (in dp)
    }

    public TapTarget getTapTargetForMenuItem(Toolbar toolbar, @IdRes int menuItemId, String title, String description){
        //Menu Item
        return TapTarget.forToolbarMenuItem(toolbar, menuItemId, title, description)
                // All options below are optional
                .outerCircleColor(R.color.taptarget_outer_circle)      // Specify a color for the outer circle
                .outerCircleAlpha(0.96f)            // Specify the alpha amount for the outer circle
                .targetCircleColor(R.color.taptarget_target_circle)   // Specify a color for the target circle
                .titleTextSize(24)                  // Specify the size (in sp) of the title text
                .titleTextColor(R.color.taptarget_title)      // Specify the color of the title text
                .descriptionTextSize(16)            // Specify the size (in sp) of the description text
                .descriptionTextColor(R.color.taptarget_description)  // Specify the color of the description text
//                        .textColor(android.R.color.white)            // Specify a color for both the title and description text
//                        .textTypeface(Typeface.SANS_SERIF)  // Specify a typeface for the text
                .dimColor(android.R.color.transparent)            // If set, will dim behind the view with 30% opacity of the given color
                .drawShadow(false)                   // Whether to draw a drop shadow or not
                .cancelable(true)                  // Whether tapping outside the outer circle dismisses the view
                .tintTarget(false)                   // Whether to tint the target view's color
                .transparentTarget(true)           // Specify whether the target is transparent (displays the content underneath)
//                        .icon(Drawable)                     // Specify a custom drawable to draw as the target
                .targetRadius(40);                  // Specify the target radius (in dp)
    }

    public void showTutorialForView(final Activity activity, final TapTargetSequence.Listener tapTargetSequenceListener, final TapTarget... tapTargets) {
        if(activity == null || tapTargets == null) return;

        if(tapTargetSequence==null) tapTargetSequence = new TapTargetSequence(activity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tapTargetSequence
                        .targets(tapTargets)
                        .listener(new TapTargetSequence.Listener() {
                            @Override
                            public void onSequenceFinish() {
                                tapTargetSequenceListener.onSequenceFinish();
                                tapTargetSequence = null;
                            }

                            @Override
                            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                                tapTargetSequenceListener.onSequenceStep(lastTarget, targetClicked);
                            }

                            @Override
                            public void onSequenceCanceled(TapTarget lastTarget) {
                                tapTargetSequenceListener.onSequenceCanceled(lastTarget);
                            }
                        })
                        .continueOnCancel(true)
                        .considerOuterCircleCanceled(true)
                        .start();
            }
        }, 500);
    }
}
