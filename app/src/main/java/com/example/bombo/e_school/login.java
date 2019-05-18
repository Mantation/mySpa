package com.example.bombo.e_school;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class login extends AppCompatActivity implements View.OnClickListener{
    TextView Forgot;
    TextView SignIn;
    TextView Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SignIn = findViewById(R.id.signin);
        Forgot = findViewById(R.id.forgot);
        Register = findViewById(R.id.register);
        SignIn.setOnClickListener(this);
        Forgot.setOnClickListener(this);
        Register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.signin){
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            this.finish();
        }else if (id==R.id.forgot){
            Intent intent = new Intent(this, teacher.class);
            this.startActivity(intent);
            this.finish();
        }else{
            Intent intent = new Intent(this, admin.class);
            this.startActivity(intent);
            this.finish();
        }

    }
}
