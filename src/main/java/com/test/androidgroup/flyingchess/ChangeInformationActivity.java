package com.test.androidgroup.flyingchess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by kehan on 16-3-26.
 */
public class ChangeInformationActivity extends Activity {

    private Button postButton;//提交按钮
    private EditText newUsername;//新昵称EditText
    private EditText oldPassword;//旧密码
    private EditText newPassword;//新密码
    private EditText getNewPasswordAgain;//再次输入的新密码

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_information_layout);

        postButton = (Button) findViewById(R.id.change_info);
        oldPassword = (EditText) findViewById(R.id.old_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        getNewPasswordAgain = (EditText) findViewById(R.id.new_password_again);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断旧密码是否正确

                //判断新密码是否只包含数字和字母

                //判断新密码和二次输入的密码是否相同


                Intent newIntent = new Intent();
                newIntent.putExtra("newName", "new name");
                setResult(RESULT_OK, newIntent);
                finish();
            }
        });
    }
}
