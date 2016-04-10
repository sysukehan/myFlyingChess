package com.test.androidgroup.flyingchess;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.test.androidgroup.flyingchess.Server.MSGS;
import com.test.androidgroup.flyingchess.Server.MessageProcess;
import com.test.androidgroup.flyingchess.Server.MessageProcessForUI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kehan on 16-3-19.
 */
public class LoginAndRegisterActivity extends FlyingChessActivity {

    private Context context;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private View view1, view2;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合

    private Button loginButton;//登录按钮
    private Button registerButton;//注册按钮
    private TextView loginAnonymous;//匿名登录按钮
    private TextInputLayout loginUserId;//登录用户名
    private TextInputLayout loginPassword;//登录密码
    private TextInputLayout registerUserId;//注册id
    private TextInputLayout registerUsername;//注册用户名
    private TextInputLayout registerPassword;//注册密码
    private TextInputLayout registerPasswordAgain;//注册密码确认

    private CheckBox autoLogin;
    SharedPreferences userInformationShpf;
    SharedPreferences.Editor editor;

    MessageProcess mp;//处理与服务器的连接，这个句柄要贯穿整个app，因此写在RunningInformation中作为一个静态变量
    Handler loginHandler;
    Handler registerHandler;
    String userID;
    String password;
    String username;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_and_register_layout);
        context = this;

        mp = new MessageProcess();
        mp.flag = true;
        RunningInformation.mp = mp;//给整个app的句柄赋值


        //实例化处理Login和处理Register的Handler
        loginHandler = new LoginHandler();
        registerHandler = new RegisterHandler();

        //获得记录用户信息的SharedPreferences
        userInformationShpf = context.getSharedPreferences("user_login_information", 0);

        //获取布局界面中的ViewPager组件和TabLayout组件
        mViewPager = (ViewPager) findViewById(R.id.vp_FindFragment_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_FindFragment_title);

        //导入登录和注册两页的布局
        mInflater = LayoutInflater.from(this);
        view1 = mInflater.inflate(R.layout.login_layout, null);
        view2 = mInflater.inflate(R.layout.register_layout, null);

        //添加页卡视图
        mViewList.add(view1);
        mViewList.add(view2);

        //添加页卡标题
        mTitleList.add("登录");
        mTitleList.add("注册");

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));

        MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

        //对SharedPreferences的编辑器
        editor = userInformationShpf.edit();

        //获得匿名登录按钮
        loginAnonymous = (TextView) findViewById(R.id.anonymous_login);
        //匿名登录
        loginAnonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RunningInformation.isAnonymous = true;
                Intent intent = new Intent(context, ChooseModeActivity.class);
                startActivity(intent);
            }
        });

        //自动登录复选框
        autoLogin = (CheckBox) view1.findViewById(R.id.auto_login);

        //设置验证进度条
        pd = new ProgressDialog(context);
        pd.setMessage("验证中...");
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setIndeterminate(false);

        //获得登录按钮，用户名输入框和密码输入框
        loginButton = (Button) view1.findViewById(R.id.login);
        loginUserId = (TextInputLayout) view1.findViewById(R.id.login_userid);
        loginPassword = (TextInputLayout) view1.findViewById(R.id.login_password);

        //登录逻辑实现
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //获取输入框字符串
                userID = loginUserId.getEditText().getText().toString();
                password = loginPassword.getEditText().getText().toString();
                boolean succeed = false;
                //对用户名进行判断
                if (userID.equals("")) {
                    loginUserId.setErrorEnabled(true);
                    loginUserId.setError("id不能为空");//错误信息
                    succeed = false;
                    return;
                } else {
                    loginUserId.setErrorEnabled(false);
                    succeed = true;
                }
                if (userID.matches("^[A-Za-z0-9]+$")) {
                    loginUserId.setErrorEnabled(false);
                    succeed = true;
                } else {
                    loginUserId.setErrorEnabled(true);
                    loginUserId.setError("id只能包含字母和数字");//错误信息
                    succeed = false;
                    return;
                }
                //对密码进行判断
                if (password.equals("")) {
                    loginPassword.setErrorEnabled(true);
                    loginPassword.setError("密码不能为空");//错误信息
                    succeed = false;
                    return;
                } else {
                    loginPassword.setErrorEnabled(false);
                    succeed = true;
                }

                //密码不能有中文
                if (!password.matches("^[A-Za-z0-9]+$")) {
                    loginPassword.setErrorEnabled(true);
                    loginPassword.setError("密码只能包含英文或数字");//错误信息
                    succeed = false;
                    return;
                } else {
                    loginPassword.setErrorEnabled(false);
                    succeed = true;
                }

                //验证用户名密码的逻辑实现
                //调用验证函数，返回值有三种，用户名不存在，密码错误，验证成功
                if (succeed) {

                    //将Handler设置为处理Login的Handler
                    mp.sendHandler = loginHandler;
                    mp.ms.mainHandler = loginHandler;

                    try {
                        //将登录消息发送至服务器，等待服务器回应
                        MessageProcessForUI.sendLoginReq(userID, MD5.getInstance().getMD5(password), mp.ms.sendHandler);

                        //显示验证中进度条
                        pd.setMessage("登录中...");
                        pd.show();
                        //等待回应
                    } catch (NullPointerException e) {
                        Toast.makeText(context, "无法发送消息，请稍后再试", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });


        //获得注册按钮，用户名输入框，密码输入框和密码确认输入框
        registerButton = (Button) view2.findViewById(R.id.register);
        registerUserId = (TextInputLayout) view2.findViewById(R.id.register_userid);
        registerUsername = (TextInputLayout) view2.findViewById(R.id.register_username);
        registerPassword = (TextInputLayout) view2.findViewById(R.id.register_password);
        registerPasswordAgain = (TextInputLayout) view2.findViewById(R.id.register_password_again);
        //注册逻辑实现
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //获取输入框字符串
                userID = registerUserId.getEditText().getText().toString();
                username = registerUsername.getEditText().getText().toString();
                password = registerPassword.getEditText().getText().toString();
                String passwordagain = registerPasswordAgain.getEditText().getText().toString();
                boolean succeed = true;

                //对id进行判断
                if (userID.matches("^[A-Za-z0-9]+$")) {
                    registerUserId.setErrorEnabled(false);
                    succeed = true;
                } else {
                    registerUserId.setErrorEnabled(true);
                    registerUserId.setError("id只能包含数字和字母");//错误信息
                    succeed = false;
                    return;
                }

                //对昵称是否为空进行判断
                if (username.equals("")) {
                    registerUsername.setErrorEnabled(true);
                    registerUsername.setError("昵称不能为空");//错误信息
                    succeed = false;
                    return;
                } else {
                    registerUsername.setErrorEnabled(false);
                    succeed = true;
                }

                //对密码进行判断
                if (!password.matches("^[A-Za-z0-9]+$")) {
                    registerPassword.setErrorEnabled(true);
                    registerPassword.setError("密码不能为空");//错误信息
                    succeed = false;
                    return;
                } else {
                    registerPassword.setErrorEnabled(false);
                    succeed = true;
                }

                //密码不能有中文
                if (password.equals("")) {
                    registerPassword.setErrorEnabled(true);
                    registerPassword.setError("密码只能包含英文或数字");//错误信息
                    succeed = false;
                    return;
                } else {
                    registerPassword.setErrorEnabled(false);
                    succeed = true;
                }

                //对二次输入密码进行判断
                if (!password.equals(passwordagain)) {
                    registerPasswordAgain.setErrorEnabled(true);
                    registerPasswordAgain.setError("两次密码输入不相同");//错误信息
                    succeed = false;
                    return;
                } else {
                    registerPasswordAgain.setErrorEnabled(false);
                    succeed = true;
                }

                //与服务器交互的逻辑实现
                if (succeed) {

                    //将Handler设置为处理Login的Handler
                    mp.sendHandler = registerHandler;
                    mp.ms.mainHandler = registerHandler;

                    try {

                        //将注册消息发送至服务器，等待服务器回应
                        MessageProcessForUI.sendRegisterReq(userID, MD5.getInstance().getMD5(password), username, mp.ms.sendHandler);

                        //显示验证中进度条
                        pd.setMessage("注册中...");
                        pd.show();

                        //等待回应

                    } catch (NullPointerException e) {
                        Toast.makeText(context, "无法发送消息，请稍后再试", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });

        mp.start();
        mp.sendHandler = loginHandler;

        //如果上次有选自动登录，则直接将信息与服务器交互
        if (userInformationShpf.getBoolean("isAutoLogin", false)) {
            String autoID = userInformationShpf.getString("userID", "");
            String autoPassword = userInformationShpf.getString("password", "");

            //将Handler设置为处理Login的Handler
            //mp.sendHandler = loginHandler;

            Log.d("FlyingChess", autoID + " " + autoPassword);
            try {
                Log.d("FlyingChess", autoID + " " + autoPassword);
                Log.d("FlyingChess", "到这里了");
                //将登录消息发送至服务器，等待服务器回应
                MessageProcessForUI.sendLoginReq(autoID, autoPassword, mp.ms.sendHandler);
                //Log.d("FlyingChess", "到这里了");
                //显示验证中进度条
                pd.setMessage("自动登录中...");
                pd.show();
                //等待回应

            } catch (NullPointerException e) {
                Log.d("FlyingChess", e.toString());
                Toast.makeText(context, "我去，自动登录又失败了", Toast.LENGTH_LONG).show();
                return;
            }
        } else {
            mp.sendHandler = new EmptyHandler();
        }

    }

    //处理登录的Handler
    private class LoginHandler extends Handler {
        public void handleMessage(Message msg) {

            //收到服务器返回的消息，进度条消失
            pd.dismiss();

            switch (msg.what) {

                //建立连接成功
                case 0x123:
                    try {
                        MSGS.MSG m = MSGS.MSG.parseFrom((byte[]) msg.obj);   //获取消息类

                        if (m.getResponse().getResult()) {

                            //登录成功
                            Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();

                            //将信息录入RunningInformation
                            RunningInformation.isAnonymous = false;
                            RunningInformation.playerId = userID;//服务器返回的登录信息中不包含ID，ID只能从输入框中获取
                            RunningInformation.playerName = m.getResponse().getLoginRes().getNickname();//获得昵称
                            RunningInformation.md5Password = MD5.getInstance().getMD5(password);//从输入框中获取密码并进行MD5加密
                            RunningInformation.sumMatches = m.getResponse().getLoginRes().getTotalGames();//获取游戏总局数
                            RunningInformation.exceedSumMatches = m.getResponse().getLoginRes().getTotalGamesRank();//获取游戏总局数超过人数百分比
                            RunningInformation.winMatches = m.getResponse().getLoginRes().getWinGames();//获取胜利游戏局数
                            RunningInformation.exceedWinMatches = m.getResponse().getLoginRes().getWinGamesRank();//获取胜利游戏局数超过人数百分比
                            if (RunningInformation.sumMatches == 0) {
                                RunningInformation.percent = 0.0;//排除除数为0的情况
                            } else {
                                RunningInformation.percent = (RunningInformation.winMatches + 0.0) / (RunningInformation.sumMatches + 0.0);//胜率本地自己算
                            }

                            RunningInformation.exceedPercent = m.getResponse().getLoginRes().getWinRateRank();//获取胜率超过人数百分比

                            //记录用户名和MD5加密后的密码
                            editor.putString("userID", RunningInformation.playerId);
                            editor.putString("password", RunningInformation.md5Password);

                            //如果下次自动登录被选中
                            if (autoLogin.isChecked()) {
                                editor.putBoolean("isAutoLogin", true);
                            } else {
                                editor.putBoolean("isAutoLogin", false);
                            }

                            //提交修改
                            editor.commit();

                            //进入选择模式Activity
                            Intent intent = new Intent(context, ChooseModeActivity.class);
                            startActivity(intent);

                        } else {
                            //登录失败
                            if (m.getResponse().getError() == 3) {
                                Toast.makeText(context, "id不存在", Toast.LENGTH_LONG).show();
                            } else if (m.getResponse().getError() == 4) {
                                Toast.makeText(context, "密码错误", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "失败但不知道什么原因", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Log.d("FlyingChess", e.toString());
                    }
                    break;

                //无法建立连接
                case 0x130:
                    Toast.makeText(context, "无法建立连接", Toast.LENGTH_LONG).show();
                    break;

                //失去连接
                case 0x131:
                    Toast.makeText(context, "失去连接", Toast.LENGTH_LONG).show();
                    break;

                default:
                    Toast.makeText(context, "不知道是什么鬼" + msg.what, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    //处理注册的Handler
    private class RegisterHandler extends Handler {
        public void handleMessage(Message msg) {

            //收到服务器返回的消息，进度条消失
            pd.dismiss();

            switch (msg.what) {

                //建立连接成功
                case 0x123:
                    try {
                        MSGS.MSG m = MSGS.MSG.parseFrom((byte[]) msg.obj);   //获取消息类

                        if (m.getResponse().getResult()) {

                            //注册成功
                            Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();

                            //初始化信息
                            RunningInformation.initial();//初始化信息
                            RunningInformation.isAnonymous = false;
                            RunningInformation.playerId = userID;//ID从输入框中获取
                            RunningInformation.playerName = username;//昵称
                            RunningInformation.md5Password = MD5.getInstance().getMD5(password);//从输入框中获取密码并进行MD5加密

                            //记录用户名和MD5加密后的密码
                            editor.putString("userID", RunningInformation.playerId);
                            editor.putString("password", RunningInformation.md5Password);

                            //如果下次自动登录被选中
                            if (autoLogin.isChecked()) {
                                editor.putBoolean("isAutoLogin", true);
                            } else {
                                editor.putBoolean("isAutoLogin", false);
                            }

                            //提交修改
                            editor.commit();

                            //进入选择模式Activity
                            Intent intent = new Intent(context, ChooseModeActivity.class);
                            startActivity(intent);

                        } else {
                            //注册失败
                            if (m.getResponse().getError() == 1) {
                                Toast.makeText(context, "id已被使用，换一个吧", Toast.LENGTH_LONG).show();
                            } else if (m.getResponse().getError() == 2) {
                                Toast.makeText(context, "服务器出了点问题，请稍后再试", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "注册失败但不知道什么原因", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                //无法建立连接
                case 0x130:
                    Toast.makeText(context, "无法建立连接", Toast.LENGTH_LONG).show();
                    break;

                //失去连接
                case 0x131:
                    Toast.makeText(context, "失去连接", Toast.LENGTH_LONG).show();
                    break;

                default:
                    Toast.makeText(context, "不知道是什么鬼" + msg.what, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    //一个空的Handler
    private class EmptyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    @Override
    public void onBackPressed() {
        ActivityCollector.finishAll();
    }

    //ViewPager适配器
    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);//页卡标题
        }

    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        Log.d("FlyingChess", "LoginAndRegisterActivity onNewIntent()");
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        Log.d("FlyingChess", "LoginAndRegisterActivity onStart()");
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d("FlyingChess", "LoginAndRegisterActivity onResume()");
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.d("FlyingChess", "LoginAndRegisterActivity onPause()");
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        Log.d("FlyingChess", "LoginAndRegisterActivity onStop()");
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.d("FlyingChess", "LoginAndRegisterActivity onDestroy()");
//    }

}