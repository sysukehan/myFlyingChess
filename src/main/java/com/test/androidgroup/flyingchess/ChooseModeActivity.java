package com.test.androidgroup.flyingchess;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by kehan on 16-3-21.
 */
public class ChooseModeActivity extends FlyingChessActivity {

    private Button main, one, two, three, four, one_one, one_two, one_three, two_one, two_two, two_three,
            three_one, three_two, three_three, four_one, four_two, four_three;
    private Context context;
    private ImageView back;//返回键，
    private TextView title;//标题
    private ImageView userImage;//用户头像
    private int whichIsShow = -1;//模式判断
    private int leave = 0;//离开判断

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_mode_layout);
        context = this;

        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        userImage = (ImageView) findViewById(R.id.user_image);
        initView();
        //更改Handler
        RunningInformation.mp.sendHandler = new EmptyHandler();

        //设置标题文字
        title.setText("选择模式");
        //如果匿名登录，头像不可见；非匿名登录，绑定点击进入个人资料界面的监听器
        if (RunningInformation.isAnonymous) {
            userImage.setVisibility(View.INVISIBLE);
            userImage.setOnClickListener(null);
        } else {
            userImage.setVisibility(View.VISIBLE);
            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newIntent = new Intent(context, PersonalInformationActivity.class);
                    startActivity(newIntent);
                }
            });
        }
    }

    private void initView() {

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


        if (RunningInformation.isAnonymous) {
            two.setBackgroundDrawable(getResources().getDrawable(R.drawable.disabled_mode_button));
            two.setTextColor(getResources().getColor(R.color.disabledWhite));
            //如果为匿名登录，返回到登录注册界面
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

        } else {
            //如果不为匿名登录，则退出游戏
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    leave++;
                    if (leave >= 2) {
                        ActivityCollector.finishAll();
                    } else {
                        Handler handler = new Handler();
                        Toast.makeText(context, "再按一次退出游戏", Toast.LENGTH_SHORT).show();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                leave--;
                            }
                        }, 2000);
                    }
                }
            });
        }

        //为4个主模式的按钮绑定监听器
        one.setOnClickListener(new BigModeClick());
        two.setOnClickListener(new BigModeClick());
        three.setOnClickListener(new BigModeClick());
        four.setOnClickListener(new BigModeClick());

        //为12个副模式的按钮绑定监听器
        one_one.setOnClickListener(new SmallModeClick());
        one_two.setOnClickListener(new SmallModeClick());
        one_three.setOnClickListener(new SmallModeClick());
        two_one.setOnClickListener(new SmallModeClick());
        two_two.setOnClickListener(new SmallModeClick());
        two_three.setOnClickListener(new SmallModeClick());
        three_one.setOnClickListener(new SmallModeClick());
        three_two.setOnClickListener(new SmallModeClick());
        three_three.setOnClickListener(new SmallModeClick());
        four_one.setOnClickListener(new SmallModeClick());
        four_two.setOnClickListener(new SmallModeClick());
        four_three.setOnClickListener(new SmallModeClick());

        //为中间的大按钮绑定监听器
        main.setOnClickListener(new MainClick());

    }

    //重写返回键
    @Override
    public void onBackPressed() {
        if (RunningInformation.isAnonymous) {
            super.onBackPressed();
        } else {
            leave++;
            if (leave >= 2) {
                ActivityCollector.finishAll();
            } else {
                Handler handler = new Handler();
                Toast.makeText(context, "再按一次退出游戏", Toast.LENGTH_SHORT).show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        leave--;
                    }
                }, 2000);
            }
        }
    }

    private class BigModeClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            if (view.getVisibility() == View.INVISIBLE || view.getAlpha() < 1) {
                return;
            }

            switch(view.getId()) {
                case R.id.one:
                    whichIsShow = 0;
                    animationShow(one_one, 300, 0);
                    animationShow(one_two, 300, 120);
                    animationShow(one_three, 300, 240);
                    main.setText(one.getText());
                    break;
                case R.id.two:
                    if (RunningInformation.isAnonymous) {
                        Toast.makeText(context, "需要先登录才能进入房间", Toast.LENGTH_LONG).show();
                        return;
                    }
                    whichIsShow = 1;
                    animationShow(two_one, 300, 30);
                    animationShow(two_two, 300, 150);
                    animationShow(two_three, 300, 270);
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

    //选完小模式之后要传的东西在这里改
    private class SmallModeClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            if (view.getVisibility() == View.INVISIBLE || view.getAlpha() < 1) {
                return;
            }

            Intent intent;
            Bundle data = new Bundle();

            switch(view.getId()) {

                //1个人VS1个电脑的模式被选中
                case R.id.one_one:
                    intent = new Intent(context, GameActivity.class);
                    data.putInt("playerNumber", 2);
                    data.putInt("mode", 2);
                    intent.putExtras(data);
                    Log.d("FlyingChess", "1v1被点击了");
                    startActivity(intent);
                    break;

                //1个人VS2个电脑的模式被选中
                case R.id.one_two:
                    intent = new Intent(context, GameActivity.class);
                    data.putInt("playerNumber", 3);
                    data.putInt("mode", 2);
                    intent.putExtras(data);
                    Log.d("FlyingChess", "1v2被点击了");
                    startActivity(intent);
                    break;

                //1个人VS3个电脑的模式被选中
                case R.id.one_three:
                    intent = new Intent(context, GameActivity.class);
                    data.putInt("playerNumber", 4);
                    data.putInt("mode", 2);
                    intent.putExtras(data);
                    Log.d("FlyingChess", "1v3被点击了");
                    startActivity(intent);
                    break;

                //2人房间对战的模式被选中
                case R.id.two_one:
                    intent = new Intent(context, ChooseRoomActivity.class);
                    data.putInt("playerNumber", 2);
                    intent.putExtras(data);
                    startActivity(intent);
                    break;

                //3人房间对战的模式被选中
                case R.id.two_two:
                    intent = new Intent(context, ChooseRoomActivity.class);
                    data.putInt("playerNumber", 3);
                    intent.putExtras(data);
                    startActivity(intent);
                    break;

                //4人房间对战的模式被选中
                case R.id.two_three:
                    intent = new Intent(context, ChooseRoomActivity.class);
                    data.putInt("playerNumber", 4);
                    intent.putExtras(data);
                    startActivity(intent);
                    break;

                //wifi下2人对战的模式被选中
                case R.id.three_one:
                    intent = new Intent(context, SearchingActivity.class);
                    data.putInt("playerNumber", 2);
                    intent.putExtras(data);
                    startActivity(intent);
                    break;

                //wifi下3人对战的模式被选中
                case R.id.three_two:
                    intent = new Intent(context, SearchingActivity.class);
                    data.putInt("playerNumber", 3);
                    intent.putExtras(data);
                    startActivity(intent);
                    break;

                //wifi下4人对战的模式被选中
                case R.id.three_three:
                    intent = new Intent(context, SearchingActivity.class);
                    data.putInt("playerNumber", 4);
                    intent.putExtras(data);
                    startActivity(intent);
                    break;

                //蓝牙下2人对战的模式被选中
                case R.id.four_one:
                    intent = new Intent(context, SearchingActivity.class);
                    data.putInt("playerNumber", 2);
                    intent.putExtras(data);
                    startActivity(intent);
                    break;

                //蓝牙下3人对战的模式被选中
                case R.id.four_two:
                    intent = new Intent(context, SearchingActivity.class);
                    data.putInt("playerNumber", 3);
                    intent.putExtras(data);
                    startActivity(intent);
                    break;

                //蓝牙下4人对战的模式被选中
                case R.id.four_three:
                    intent = new Intent(context, SearchingActivity.class);
                    data.putInt("playerNumber", 4);
                    intent.putExtras(data);
                    startActivity(intent);
                    break;

                default:
                    break;
            }
        }
    }

    private class MainClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getVisibility() == View.INVISIBLE || view.getAlpha() < 1) {
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


    //一个空的Handler
    private class EmptyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    //散开的动画效果，传入参数为动画的view，散开的半径，角度
    private void animationShow(View view, int radius, double degree) {
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        degree = degree * Math.PI / 180;
        // 根据三角函数来获得view平移的x和y
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

    //收缩的动画效果，传入参数为动画的view，散开的半径，角度
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

//    @Override
//    public void onStart() {
//        super.onStart();
//        Log.d("FlyingChess", "ChooseModeActivity onStart()");
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d("FlyingChess", "ChooseModeActivity onResume()");
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.d("FlyingChess", "ChooseModeActivity onPause()");
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        Log.d("FlyingChess", "ChooseModeActivity onStop()");
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.d("FlyingChess", "ChooseModeActivity onDestroy()");
//    }

}
