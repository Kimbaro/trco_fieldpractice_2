package com.kimbaro.unitymodulecreate;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kimbaro.plugin.LoginModule;

public class LoginActivity extends AppCompatActivity {
    private EditText editText1, editText2;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Activity activity = this;
        editText1 = findViewById(R.id.username);
        editText2 = findViewById(R.id.password);
        button = findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginModule loginModule = new LoginModule();
                loginModule.requestLogin(editText1.getText().toString(), editText2.getText().toString(), activity);
            }
        });
    }
}
