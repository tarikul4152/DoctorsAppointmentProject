package com.tarikul4152.doctorsappointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ActivityVarificationCheck extends AppCompatActivity implements MyCommunicator, View.OnClickListener
{
    private MyToastClass myToast;
    private SignInOrSignUpClass Verification;
    private FirebaseAuth mAuth;
    private String Parent_Activity;

    private Button VerificationRecheckBtn,VerificationReSendBtn,VerificationSubmitBtn;

    private TextView VerificationStatusTv;

    private int counter=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        Parent_Activity=getIntent().getStringExtra("Activity");
        if (Parent_Activity.matches("Email"))
        {
            if (mAuth.getCurrentUser()!=null)
            {
                mAuth.getCurrentUser().sendEmailVerification();
            }
        }
        else if (Parent_Activity.matches("Phone"))
        {

        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.verification_resend_btn:
                VerificationResendMethod();
        }
    }

    private void VerificationResendMethod()
    {
        if (counter<3)
        {
            Verification.ResendEmailVerification();
        }
        else
        {

        }
    }

    private boolean isEmailVerified()
    {

    }

    @Override
    public void Communicator(String type, boolean result)
    {
        switch (type)
        {
            case "ResendEmailVerification":
                if (result)
                {
                    counter++;
                    VerificationStatusTv.setText("Verification resent "+counter+" from limit 3");
                }
                else
                {
                    VerificationStatusTv.setText("Verification couldn't resent ");
                }
        }
    }
}
