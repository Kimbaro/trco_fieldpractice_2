package com.kimbaro.unitymodulecreate;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kimbaro.plugin.IdCheckModule;

public class IdCheckActivity extends AppCompatActivity {
    private EditText editText1;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_check);
        final Activity activity = this;
        editText1 = findViewById(R.id.username);
        button = findViewById(R.id.login);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IdCheckModule idCheckModule = new IdCheckModule();
                idCheckModule.requestIdCheck(editText1.getText().toString(), activity);
            }
        });
    }
}
