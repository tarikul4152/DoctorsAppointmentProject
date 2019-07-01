package com.tarikul4152.doctorsappointment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class ActivitySignInOrSignUp extends AppCompatActivity implements View.OnClickListener {

    Boolean CredentialValidityResult=false,PasswordValidityResult=false,PhoneSignIn=false,EmailSignIn=false;
    String PasswordString,CredentialString;

    private static final Pattern Password_PATTERN=
            Pattern.compile("^"+
                    //"(?=.*[0-9])"+
                    //"(?=.*[a-z])"+
                    //"(?=.*[A-Z])"+
                    //"(?=.*[!@#$%^&-_=+])"+
                    "(?=\\S+$)"+
                    ".{6,}"+
                    "$");
    Intent intent;

    Button GotoSignUpActivityBtn,SignInBtn;
    EditText SignInCredentialEt,SignInPasswordEt;
    TextView ForgottenPasswordTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_or_signup);
        ViewInit();
    }

    ///View Initialization
    private void ViewInit()
    {
        GotoSignUpActivityBtn= findViewById(R.id.signin_goto_sign_up_activity_btn);
        GotoSignUpActivityBtn.setOnClickListener(this);
        SignInBtn= findViewById(R.id.signin_btn);
        SignInBtn.setOnClickListener(this);
        SignInCredentialEt= findViewById(R.id.signin_credential_et);
        SignInCredentialEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                CredentialString=SignInCredentialEt.getText().toString();
                CredentialValidityResult=CheckCredentialValidity(CredentialString);
                if (!CredentialValidityResult)
                {
                    SignInCredentialEt.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                else
                {
                    SignInCredentialEt.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SignInPasswordEt=findViewById(R.id.signin_password_et);
        SignInPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                PasswordString=SignInPasswordEt.getText().toString();
                PasswordValidityResult=CheckPasswordValidity(PasswordString);
                if (!PasswordValidityResult)
                {
                    SignInPasswordEt.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                else
                {
                    SignInPasswordEt.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ForgottenPasswordTv= findViewById(R.id.signin_forgotten_password_tv);
        ForgottenPasswordTv.setOnClickListener(this);
    }

    //OnClick Method
    @Override
    public void onClick(View v)
    {
        int id=v.getId();
        switch (id)
        {
            case R.id.signin_goto_sign_up_activity_btn:
                intent=new Intent(ActivitySignInOrSignUp.this, mActivitySignUp.class);
                startActivity(intent);
                break;
            case R.id.signin_btn:
                SignInMethod();
                break;
            case R.id.signin_forgotten_password_tv:
                //ForgottenPasswordMethod();
                break;
        }
    }

    ///Sign In Method
    private void SignInMethod()
    {
        if (CredentialValidityResult && PasswordValidityResult)
        {
            CredentialString=SignInCredentialEt.getText().toString();
            PasswordString=GetEncryptPassword(SignInPasswordEt.getText().toString());

            if (PhoneSignIn)
            {
                PhoneSignInMethod(CredentialString,PasswordString);
            }
            else if (EmailSignIn)
            {
                EmailSignInMethod(CredentialString,PasswordString);
            }
        }
        else
        {
            SignInCredentialEt.setError("Please type properly.");
            SignInPasswordEt.setError("Password must be atleast 6 digit");
        }
    }

    private void EmailSignInMethod(String credentialString, String passwordString)
    {
        Toast.makeText(ActivitySignInOrSignUp.this,"Email Sign In Method Called",Toast.LENGTH_SHORT).show();
    }

    private void PhoneSignInMethod(String credentialString, String passwordString)
    {
        Toast.makeText(ActivitySignInOrSignUp.this,"Phone Sign In Method Called",Toast.LENGTH_SHORT).show();
    }

    private String GetEncryptPassword(String toString)
    {
        return toString;
    }


    //CheckCredentialValidityMethod()
    private boolean CheckCredentialValidity(String CredentialString)
    {
        if (Patterns.EMAIL_ADDRESS.matcher(CredentialString).matches())
        {
            EmailSignIn=true;
            PhoneSignIn=false;
            return true;
        }
        else if (CredentialString.length()==11 && Patterns.PHONE.matcher(CredentialString).matches())
        {
            PhoneSignIn=true;
            EmailSignIn=false;
            return true;
        }
        else
        {
            return false;
        }
    }
    //CheckPasswordValidityMethod()
    private boolean CheckPasswordValidity(String PasswordString)
    {
        if (Password_PATTERN.matcher(PasswordString).matches())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
