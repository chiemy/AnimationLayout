package com.chiemy.demo.animationlinearlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created: chiemy
 * Date: 16/6/29
 * Description:
 */
public class AnimationLinearLayout extends LinearLayout implements AnimationLayout{
    private SlideAlphaLayoutAnimator animation;

    public AnimationLinearLayout(Context context) {
        this(context, null);
    }

    public AnimationLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        animation = new SlideAlphaLayoutAnimator(this);
    }

    public void show() {
        animation.visibleAnimation();
    }

    @Override
    public void gone() {
        animation.invisibleAnimation(View.GONE);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewGroup viewGroup = (ViewGroup) getParent();
        if (viewGroup != null) {
            viewGroup.setClipChildren(false);
        }
        setClipChildren(false);
        if (getVisibility() == View.VISIBLE) {
            show();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animation.cancel();
    }

}
