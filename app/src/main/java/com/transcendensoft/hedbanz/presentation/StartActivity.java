package com.transcendensoft.hedbanz.presentation;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.transcendensoft.hedbanz.R;
import com.transcendensoft.hedbanz.data.prefs.PreferenceManager;
import com.transcendensoft.hedbanz.presentation.mainscreen.MainActivity;
import com.transcendensoft.hedbanz.utils.ViewUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class StartActivity extends DaggerAppCompatActivity {
    @BindView(R.id.ivHatImage) ImageView mIvHat;
    @BindView(R.id.ivSmileImage) ImageView mIvSmile;
    @BindView(R.id.flLoginFragmentContainer) FrameLayout mFlLoginContainer;

    @Inject PreferenceManager mPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mPreferenceManager.isAuthorised()) {
            startMainActivity();
        } else {
            showLoginWithAnimation();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void showLoginWithAnimation() {
        setContentView(R.layout.activity_start);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        ButterKnife.bind(this, this);

        initHatAnimation();
        initSmileAnimation();
    }

    private void initHatAnimation() {
        Drawable drawable = VectorDrawableCompat.create(getResources(), R.drawable.hat, null);
        mIvHat.setImageDrawable(drawable);

        ViewTreeObserver vto = mIvHat.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mIvHat.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mIvHat.setTranslationY(-mIvHat.getHeight());
            }
        });
    }

    private void initSmileAnimation() {
        ViewTreeObserver vto = mIvSmile.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mIvSmile.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        mIvSmile.getWidth(), mIvSmile.getWidth());
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                mIvSmile.setLayoutParams(params);

                Drawable drawable = AnimatedVectorDrawableCompat.create(
                        StartActivity.this, R.drawable.anim_smile);
                mIvSmile.setImageDrawable(drawable);
                ((AnimatedVectorDrawableCompat) drawable).start();

                animateHat(mIvSmile.getWidth());
            }
        });
    }

    private void animateHat(int smileHeight) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int screenHeight = metrics.heightPixels;
        Log.d("TAG", "Height : " + smileHeight);
        int yToGo = screenHeight / 2 - smileHeight / 2 - mIvHat.getHeight() / 2 +
                ViewUtils.dpToPx(this, 16);

        mIvHat.animate()
                .setDuration(700)
                .setStartDelay(2300)
                .translationY(yToGo)
                .setInterpolator(new BounceInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        // Do not need to implement
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        showLoginFragment();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        // Do not need to implement
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                        // Do not need to implement
                    }
                }).start();
    }

    private void showLoginFragment() {
        int hatSize = mIvHat.getWidth();
        int smileSize = mIvSmile.getWidth();
        int topCoordsHatX = ViewUtils.dpToPx(this, 44);
        int topCoordsHatY = ViewUtils.dpToPx(this, 40);
        int topCoordSmileX = ViewUtils.dpToPx(this, 44);
        int topCoordSmileY = (int) (hatSize * 0.17 + topCoordsHatY);

        mIvHat.animate()
                .setDuration(1000)
                .setStartDelay(0)
                .scaleX(0.15f)
                .scaleY(0.15f)
                .setInterpolator(new DecelerateInterpolator())
                .x(topCoordsHatX - hatSize / 2)
                .y(topCoordsHatY - hatSize / 2)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();
        mIvSmile.animate()
                .setDuration(1000)
                .setStartDelay(0)
                .scaleX(0.2f)
                .scaleY(0.2f)
                .setInterpolator(new DecelerateInterpolator())
                .x(topCoordSmileX - smileSize / 2)
                .y(topCoordSmileY - smileSize / 2)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .start();

        mFlLoginContainer.setTranslationY(mFlLoginContainer.getHeight() / 4);
        mFlLoginContainer.
                animate()
                .setStartDelay(0)
                .setInterpolator(new DecelerateInterpolator())
                .alpha(1.f)
                .setDuration(1500)
                .translationY(0.f)
                .start();
    }
}