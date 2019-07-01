package com.tarikul4152.doctorsappointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityWelcome extends AppCompatActivity
{
    private Intent i=null;
    private FirebaseAuth mAuth=null;
    private FirebaseUser mUser=null;
    private Thread thread;
    private MyToastClass myToast;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        myToast=new MyToastClass(ActivityWelcome.this);

        try {
            mAuth=FirebaseAuth.getInstance();
            mUser=mAuth.getCurrentUser();
        } catch (Exception e)
        {
            myToast.SToast("Error occurred "+e.toString());
        }

        thread=new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        String email=null;
        String phone=null;
        if (mUser==null)
        {
            i = new Intent(ActivityWelcome.this, ActivitySignInOrSignUp.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        else
        {
            email=mUser.getEmail();
            phone=mUser.getPhoneNumber();
            if (email.isEmpty())
            {
                myToast.SToast("email null");
            }
            else
            {
                myToast.SToast("email "+email.toString());
            }
            /*if (phone==null)
            {
                myToast.SToast("phone null");
            }
            else
            {
                myToast.SToast("Phone "+phone.toString());
            }*/
            i = new Intent(ActivityWelcome.this, ActivityMain.class);
            i.putExtra("UID",mUser.getUid().toString());
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }
}
