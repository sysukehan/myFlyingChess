package com.test.androidgroup.flyingchess;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kehan on 16-3-19.
 */
public class LoginAndRegisterActivity extends Activity {

    private Context context;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//页卡标题集合
    private View view1, view2;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合

    private Button loginButton;//登录按钮
    private Button registerButton;//注册按钮
    private TextInputLayout loginUsername;//登录用户名
    private TextInputLayout loginPassword;//登录密码
    private TextInputLayout registerUsername;//注册用户名
    private TextInputLayout registerPassword;//注册密码
    private TextInputLayout registerPasswordAgain;//注册密码确认

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_and_register_layout);
        context = this;

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

        //获得登录按钮，用户名输入框和密码输入框
        loginButton = (Button) view1.findViewById(R.id.login);
        loginUsername = (TextInputLayout) view1.findViewById(R.id.login_username);
        loginPassword = (TextInputLayout) view1.findViewById(R.id.login_password);

        //登录逻辑实现
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入框字符串
                String username = loginUsername.getEditText().getText().toString();
                String password = loginPassword.getEditText().getText().toString();
                boolean succeed = true;
                //对用户名进行判断
                if (username.equals("")) {
                    loginUsername.setErrorEnabled(true);
                    loginUsername.setError("username is empty");//错误信息
                    succeed = false;
                } else {
                    loginUsername.setErrorEnabled(false);
                    succeed = true;
                }
                //对密码进行判断
                if (password.equals("")) {
                    loginPassword.setErrorEnabled(true);
                    loginPassword.setError("password is empty");//错误信息
                    succeed = false;
                } else {
                    loginPassword.setErrorEnabled(false);
                    succeed = true;
                }

                //登录成功逻辑实现
                if (succeed) {
                    Toast.makeText(context, "login succeed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, ChooseModeActivity.class);
                    startActivity(intent);
                }
            }
        });


        //获得注册按钮，用户名输入框，密码输入框和密码确认输入框
        registerButton = (Button) view2.findViewById(R.id.register);
        registerUsername = (TextInputLayout) view2.findViewById(R.id.register_username);
        registerPassword = (TextInputLayout) view2.findViewById(R.id.register_password);
        registerPasswordAgain = (TextInputLayout) view2.findViewById(R.id.register_password_again);
        //注册逻辑实现
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取输入框字符串
                String username = registerUsername.getEditText().getText().toString();
                String password = registerPassword.getEditText().getText().toString();
                String passwordagain = registerPasswordAgain.getEditText().getText().toString();
                boolean succeed = true;

                //对用户名进行判断
                if (username.equals("")) {
                    registerUsername.setErrorEnabled(true);
                    registerUsername.setError("手机号不能为空");//错误信息
                } else {
                    registerUsername.setErrorEnabled(false);
                    succeed = true;
                }

                //对密码进行判断
                if (password.equals("")) {
                    registerPassword.setErrorEnabled(true);
                    registerPassword.setError("密码不能为空");//错误信息
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
                } else {
                    registerPasswordAgain.setErrorEnabled(false);
                    succeed = true;
                }

                //注册成功逻辑实现
                if (succeed) {
                    Toast.makeText(context, "register succeed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, ChooseModeActivity.class);
                    startActivity(intent);
                }

            }
        });
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

}
