package com.chiemy.demo.animationlinearlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: chiemy
 * Date: 16/6/29
 * Description:
 */
public class SlideAlphaLayoutAnimator extends Animator{

    private static final long DEFUALT_DURATION = 300;
    private static final float MAX_ALPHA = 1.f;
    private static final float MIN_ALPHA = 0.f;
    private static final long DEFAULT_INTERVAL = 100;
    private static final long DEFAULT_OFFSET = 500;

    private long interval = DEFAULT_INTERVAL;
    private long firstItemAnimOffset = DEFAULT_OFFSET;

    private ViewGroup viewGroup;
    private Animator.AnimatorListener animatorListener;
    private boolean isRunning;

    private List<AnimatorSet> visibleAnimatorSetList;
    private List<AnimatorSet> invisibleAnimatorSetList;

    private int count;

    public SlideAlphaLayoutAnimator(ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
    }

    public void setAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

    public void visibleAnimation() {
        if (visibleAnimatorSetList == null) {
            count = this.viewGroup.getChildCount();
            visibleAnimatorSetList = new ArrayList<>(count);
        }
        visibleAnimatorSetList.clear();
        for (int i = 0 ; i < count ; i++) {
            final int index = i;
            final View view = viewGroup.getChildAt(i);
            view.setVisibility(View.INVISIBLE);
            view.setTranslationY(0);
            if (animatorListener != null) {
                animatorListener.onAnimationStart(this);
            }
            isRunning = true;
            viewGroup.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AnimatorSet animatorSet = playAnimatorSet(view, DEFUALT_DURATION, MIN_ALPHA, MAX_ALPHA, view.getHeight(), view.getTranslationY(),
                            new DecelerateInterpolator(), new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    viewGroup.setVisibility(View.VISIBLE);
                                    view.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    visibleAnimatorSetList.remove(animation);
                                    view.setAlpha(MAX_ALPHA);
                                    if (index == count - 1) {
                                        isRunning = false;
                                        if (animatorListener != null) {
                                            animatorListener.onAnimationEnd(SlideAlphaLayoutAnimator.this);
                                        }
                                    }
                                }
                            });
                    visibleAnimatorSetList.add(animatorSet);
                }
            }, i * interval + firstItemAnimOffset);
        }
    }

    public void invisibleAnimation(final int visibility) {
        if (invisibleAnimatorSetList == null) {
            count = this.viewGroup.getChildCount();
            invisibleAnimatorSetList = new ArrayList<>(count);
        }
        invisibleAnimatorSetList.clear();
        for (int i = 0 ; i < count ; i++) {
            final int index = count - i - 1;
            final View view = viewGroup.getChildAt(index);
            view.setTranslationY(0);
            if (animatorListener != null) {
                animatorListener.onAnimationStart(this);
            }
            isRunning = true;
            viewGroup.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AnimatorSet animatorSet = playAnimatorSet(view, DEFUALT_DURATION, MAX_ALPHA, MIN_ALPHA, view.getTranslationY(), view.getHeight(),
                            new AccelerateInterpolator(), new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    invisibleAnimatorSetList.remove(animation);
                                    view.setAlpha(MIN_ALPHA);
                                    if (index == 0) {
                                        isRunning = false;
                                        viewGroup.setVisibility(visibility);
                                        if (animatorListener != null) {
                                            animatorListener.onAnimationEnd(SlideAlphaLayoutAnimator.this);
                                        }
                                    }
                                }
                            });
                    invisibleAnimatorSetList.add(animatorSet);
                }
            }, i * interval + firstItemAnimOffset);
        }
    }

    private AnimatorSet playAnimatorSet(View view, long duration, float fromAlpha, float toAlpha, float fromY, float toY,
                                        TimeInterpolator interpolator, Animator.AnimatorListener listener) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(duration);
        Animator alphaAnimator = getAlphaAnimator(view, duration, fromAlpha, toAlpha);
        Animator translateAnimator = getTranslateYAnimator(view, duration, fromY, toY);
        set.addListener(listener);
        set.setInterpolator(interpolator);
        set.playTogether(alphaAnimator, translateAnimator);
        set.start();
        return set;
    }

    private Animator getAlphaAnimator(final View item, long duration, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(item, "alpha", from, to);
        animator.setDuration(duration);
        return animator;
    }

    private Animator getTranslateYAnimator(View item, long duration, float fromY, float toY) {
        ObjectAnimator translateAnim = ObjectAnimator.ofFloat(item,
                "translationY", fromY, toY);
        translateAnim.setDuration(duration);
        return translateAnim;
    }

    @Override
    public long getStartDelay() {
        return firstItemAnimOffset;
    }

    @Override
    public void setStartDelay(long startDelay) {
        firstItemAnimOffset = startDelay;
    }

    @Override
    public Animator setDuration(long duration) {
        interval = duration;
        return this;
    }

    @Override
    public long getDuration() {
        return interval;
    }

    @Override
    @Deprecated
    public void setInterpolator(TimeInterpolator value) {
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void cancel() {
        super.cancel();
        cancelAnimatorSets(visibleAnimatorSetList);
        cancelAnimatorSets(invisibleAnimatorSetList);
    }

    private void cancelAnimatorSets(List<AnimatorSet> animatorSets) {
        if (animatorSets != null) {
            int size = animatorSets.size();
            for (int i = 0 ; i < size ; i++) {
                animatorSets.get(i).cancel();
            }
        }
    }
}

