package com.sahilda.animationdemo;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btnAnimate = (Button) findViewById(R.id.btnAnimate);


        btnAnimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ObjectAnimator moveToTop = ObjectAnimator.ofFloat(v, View.Y, 0);
                moveToTop.setDuration(500);
                moveToTop.setInterpolator(new DecelerateInterpolator());
                moveToTop.setRepeatCount(3);
                moveToTop.setRepeatMode(ValueAnimator.REVERSE);

                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(v, "alpha", 0);
                fadeOut.setInterpolator(new BounceInterpolator());

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(moveToTop, fadeOut);
                animatorSet.start();
            }
        });

    }

}
