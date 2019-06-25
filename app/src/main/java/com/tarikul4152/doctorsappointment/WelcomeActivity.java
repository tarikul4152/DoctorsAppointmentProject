package com.tarikul4152.doctorsappointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity
{
    Thread thread;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        thread=new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent i=new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        thread.start();
    }
}
