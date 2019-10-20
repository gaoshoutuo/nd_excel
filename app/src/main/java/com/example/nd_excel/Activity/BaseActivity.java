package com.example.nd_excel.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.nd_excel.R;

import org.json.JSONObject;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);
        getSupportActionBar().hide();
    }

    protected <T extends View>T f(int rid){
        return (T)this.findViewById(rid);
    }

    protected void aTo(Class activity2, Bundle bundle){
        Intent intent=new Intent(this,activity2);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
