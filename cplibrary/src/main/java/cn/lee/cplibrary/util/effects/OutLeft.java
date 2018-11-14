package cn.lee.cplibrary.util.effects;

import android.animation.ObjectAnimator;
import android.view.View;


public class OutLeft extends BaseEffects {

    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0, -300).setDuration(mDuration),
                ObjectAnimator.ofFloat(view, "alpha", 1, 0).setDuration(mDuration * 3 / 2)
        );
    }
}
