package com.nosmurf.shk.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by Sergio on 12/05/2016.
 */
public class AnimationUtils {

    public AnimationUtils() {
        super();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void enterReveal(View view) {

        int cx = view.getMeasuredWidth() / 2;
        int cy = view.getMeasuredHeight() / 2;

        int finalRadius = Math.max(view.getWidth(), view.getHeight()) / 2;

        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void enterReveal(View view, RevealCallback revealCallback, int divider) {

        int cx = view.getMeasuredWidth() / divider;
        int cy = view.getMeasuredHeight() / divider;

        int finalRadius = Math.max(view.getWidth(), view.getHeight()) / divider;

        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (revealCallback != null) {
                    revealCallback.onRevealFinished();
                }
            }
        });

        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void enterReveal(View view, RevealCallback revealCallback, View revealView, int divider) {

        int location[] = new int[2];
        revealView.getLocationInWindow(location);
        int cx = location[0] + revealView.getMeasuredWidth() / 2;
        int cy = location[1] + revealView.getMeasuredHeight() / 2;

        int finalRadius = Math.max(view.getWidth(), view.getHeight()) / divider;

        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (revealCallback != null) {
                    revealCallback.onRevealFinished();
                }
            }
        });

        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void exitReveal(View view, View revealView, int divider) {

        int location[] = new int[2];
        revealView.getLocationInWindow(location);
        int cx = location[0] + revealView.getMeasuredWidth() / 2;
        int cy = location[1] + revealView.getMeasuredHeight() / 2;

        int initialRadius = Math.max(view.getWidth(), view.getHeight()) / divider;

        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });

        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void exitReveal(View view) {
        // previously visible view

        // get the center for the clipping circle
        int cx = view.getMeasuredWidth() / 2;
        int cy = view.getMeasuredHeight() / 2;

        // get the initial radius for the clipping circle
        int initialRadius = view.getWidth() / 2;

        // create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });

        // start the animation
        anim.start();
    }

    public interface RevealCallback {
        void onRevealFinished();
    }

}
