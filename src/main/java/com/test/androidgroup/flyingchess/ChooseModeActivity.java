package com.test.androidgroup.flyingchess;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by kehan on 16-3-21.
 */
public class ChooseModeActivity extends Activity {

    private Button main, one, two, three, four, one_one, one_two, one_three, two_one, two_two, two_three,
            three_one, three_two, three_three, four_one, four_two, four_three;
    private Context context;
    private TextView title;
    private int whichIsShow = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_mode_layout);
        context = this;
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        main = (Button) findViewById(R.id.main);
        one = (Button) findViewById(R.id.one);
        two = (Button) findViewById(R.id.two);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        one_one = (Button) findViewById(R.id.one_one);
        one_two = (Button) findViewById(R.id.one_two);
        one_three = (Button) findViewById(R.id.one_three);
        two_one = (Button) findViewById(R.id.two_one);
        two_two = (Button) findViewById(R.id.two_two);
        two_three = (Button) findViewById(R.id.two_three);
        three_one = (Button) findViewById(R.id.three_one);
        three_two = (Button) findViewById(R.id.three_two);
        three_three = (Button) findViewById(R.id.three_three);
        four_one = (Button) findViewById(R.id.four_one);
        four_two = (Button) findViewById(R.id.four_two);
        four_three = (Button) findViewById(R.id.four_three);

        title.setText("选择模式");

        Handler moveInHandler = new Handler();
        moveInHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animationShow(one, 300, 315);
                animationShow(two, 300, 225);
                animationShow(three, 300, 135);
                animationShow(four, 300, 45);
            }
        }, 300);

        one.setOnClickListener(new BigModeClick());
        two.setOnClickListener(new BigModeClick());
        three.setOnClickListener(new BigModeClick());
        four.setOnClickListener(new BigModeClick());

        main.setOnClickListener(new MainClick());

    }

    class BigModeClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.one:
                    whichIsShow = 0;
                    animationShow(one_one, 300, 0);
                    animationShow(one_two, 300, 120);
                    animationShow(one_three, 300, 240);
                    main.setText(one.getText());
                    break;
                case R.id.two:
                    whichIsShow = 1;
                    animationShow(two_one, 300, 30);
                    animationShow(two_two, 300, 150);
                    animationShow(two_three, 300, 290);
                    main.setText(two.getText());
                    break;
                case R.id.three:
                    whichIsShow = 2;
                    animationShow(three_one, 300, 60);
                    animationShow(three_two, 300, 180);
                    animationShow(three_three, 300, 300);
                    main.setText(three.getText());
                    break;
                case R.id.four:
                    whichIsShow = 3;
                    animationShow(four_one, 300, 90);
                    animationShow(four_two, 300, 210);
                    animationShow(four_three, 300, 330);
                    main.setText(four.getText());
                    break;
                default:
                    break;
            }

            animationHint(one, 300, 315);
            animationHint(two, 300, 225);
            animationHint(three, 300, 135);
            animationHint(four, 300, 45);

            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    main.setAlpha(main.getAlpha() + 0.1f);
                    if (main.getAlpha() >= 1) {
                        handler.removeCallbacks(this);
                        return;
                    }
                    handler.postDelayed(this, 30);
                }
            });

        }
    }

    class MainClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getVisibility() == View.INVISIBLE || view.getAlpha() == 0) {
                return;
            }
            switch(whichIsShow) {
                case 0:
                    animationHint(one_one, 300, 0);
                    animationHint(one_two, 300, 120);
                    animationHint(one_three, 300, 240);
                    break;
                case 1:
                    animationHint(two_one, 300, 30);
                    animationHint(two_two, 300, 150);
                    animationHint(two_three, 300, 270);
                    break;
                case 2:
                    animationHint(three_one, 300, 60);
                    animationHint(three_two, 300, 180);
                    animationHint(three_three, 300, 300);
                    break;
                case 3:
                    animationHint(four_one, 300, 90);
                    animationHint(four_two, 300, 210);
                    animationHint(four_three, 300, 330);
                    break;
                default:
                    break;
            }
            main.setAlpha(0f);
            whichIsShow = -1;
            animationShow(one, 300, 315);
            animationShow(two, 300, 225);
            animationShow(three, 300, 135);
            animationShow(four, 300, 45);
        }
    }

    private void animationShow(View view, int radius, double degree) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        degree = degree * Math.PI / 180;
        // 根据三角函数来获得view平移的x,和y
        int translateX = (int) ((int) radius * Math.sin(degree));
        int translateY = (int) ((int) radius * Math.cos(degree));
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0, translateX),
                ObjectAnimator.ofFloat(view, "translationY", 0, translateY),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f));
        set.start();
    }

    private void animationHint(View view, int radius, double degree) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        degree = degree * Math.PI / 180;
        // 根据三角函数来获得view平移的x,和y
        int translateX = (int) ((int) radius * Math.sin(degree));//横向移动距离
        int translateY = (int) ((int) radius * Math.cos(degree));//纵向移动距离
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", translateX, 0),
                ObjectAnimator.ofFloat(view, "translationY", translateY, 0),
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f),
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0f));
        set.start();
    }

}