package com.test.androidgroup.flyingchess;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kehan on 16-3-26.
 */
public class ChangeInformationActivity extends FlyingChessActivity {

    private Context context;
    private ImageView back;
    private TextView title;
    private ImageView userImage;
    private Button postButton;//提交按钮
    private EditText newUsername;//新昵称EditText
    private EditText oldPassword;//旧密码
    private EditText newPassword;//新密码
    private EditText getNewPasswordAgain;//再次输入的新密码

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_information_layout);
        context = this;

        newUsername = (EditText) findViewById(R.id.change_username);

        newUsername.setText(RunningInformation.playerName);

        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        userImage = (ImageView) findViewById(R.id.user_image);
        userImage.setVisibility(View.INVISIBLE);
        title.setText("修改个人资料");
        postButton = (Button) findViewById(R.id.change_info);
        oldPassword = (EditText) findViewById(R.id.old_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        getNewPasswordAgain = (EditText) findViewById(R.id.new_password_again);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean succeed = false;
                String oldPasswordString = oldPassword.getText().toString();
                //判断旧密码是否正确
                if (!MD5.getInstance().getMD5(oldPasswordString).equals(RunningInformation.md5Password)) {
                    Toast.makeText(context, "原密码不正确", Toast.LENGTH_LONG).show();
                    return;
                }
                String newPasswordString = newPassword.getText().toString();
                //判断新密码是否为空
                if (newPasswordString.equals("")) {
                    Toast.makeText(context, "新密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }

                //判断新密码是否只包含数字和字母
                if (!newPasswordString.matches("^[A-Za-z0-9]+$")) {
                    Toast.makeText(context, "密码只能包含数字和字母", Toast.LENGTH_LONG).show();
                    return;
                }
                //判断新密码和二次输入的密码是否相同
                String newPasswordStringAgain = getNewPasswordAgain.getText().toString();
                if (!newPasswordStringAgain.equals(newPasswordString)) {
                    Toast.makeText(context, "两次密码输入不相同", Toast.LENGTH_LONG).show();
                    return;
                }

                /*
                    与服务器交互更新信息
                    等服务器确认更新成功再更换RunningInformation中的信息
                 */

                Intent newIntent = new Intent();
                newIntent.putExtra("newName", newUsername.getText().toString());
                setResult(RESULT_OK, newIntent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}
