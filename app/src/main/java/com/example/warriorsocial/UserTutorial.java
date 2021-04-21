package com.example.warriorsocial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserTutorial extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tutorial);

        button = (Button) findViewById(R.id.btn_usertut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity_user_tutorial();
            }
    });
}

public void openActivity_user_tutorial()
{
    Intent intent = new Intent (this, openActivity_user_tutorial);
    startActivity(intent);
}
}