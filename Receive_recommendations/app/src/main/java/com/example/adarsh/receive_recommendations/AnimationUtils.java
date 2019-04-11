package com.example.adarsh.receive_recommendations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

/**
 * A collection of util functions for animations
 */
public class  AnimationUtils {

    private static final long SHORT_ANIMATION_DURATION = 400; // milliseconds

    /**
     * Animate a transition between two views
     *
     *  viewToFadeOut
     *  viewToFadeIn
     * viewToFadeOutVisibilityState
     */
    public static void crossFade(
            final View viewToFadeOut,
            final View viewToFadeIn,
            final int viewToFadeOutVisibilityState) {
        viewToFadeIn.setAlpha(0f);
        viewToFadeIn.setVisibility(View.VISIBLE);

        viewToFadeIn.animate()
                .alpha(1f)
                .setDuration(SHORT_ANIMATION_DURATION)
                .setListener(null);

        viewToFadeOut.animate()
                .alpha(0f)
                .setDuration(SHORT_ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewToFadeOut.setVisibility(viewToFadeOutVisibilityState);
                    }
                });
    }

    /**
     * Show a view
     *
     * fadedOutView the view to show
     */
    public static void crossFadeReset(final View fadedOutView) {
        fadedOutView.setAlpha(1f);
        fadedOutView.setVisibility(View.VISIBLE);
    }
}
