package com.tarikul4152.doctorsappointment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.internal.GoogleSignInOptionsExtensionParcelable;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ActivitySignUp extends AppCompatActivity implements View.OnClickListener,MyCommunicator {
    //Firebase Variable
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;

    //Resource variable
    private Intent intent;
    private FragmentManager fm;
    private FragmentTransaction transaction;

    //Class Variable
    private SignInOrSignUpClass SignUpClass;
    private MyTextWatcher myTextWatcher;
    private MyToastClass myToast;

    //String variable
    private String EmailString, PasswordString;
    private int WRONG_COLOR;
    private boolean AccountCreationResult;

    //Ui Variable
    private EditText SignUpEmailEt, SignUpPasswordEt, SignUpRetypePasswordEt;
    private Button SignUpBtn, SignUpPhoneBtn, SignUpGoogleBtn;
    private ProgressBar SignUpProgressBar;
    private Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.activity = ActivitySignUp.this;
        InitUi();
        InitClass();
    }
    /// iLw+LI72dE1p/N96SbOS5V/vnME=
    private void InitUi() {
        //******************************TextWatcher Class Hints**********************************//
        //*******MyTextWatcher(Activity activity,String WhichEditText,int EditTextId)*********/////
        SignUpEmailEt = findViewById(R.id.signup_email_et);
        SignUpEmailEt.addTextChangedListener(new MyTextWatcher(activity, FixedVariable.Email, R.id.signup_email_et));
        SignUpPasswordEt = findViewById(R.id.signup_password_et);
        SignUpPasswordEt.addTextChangedListener(new MyTextWatcher(activity, FixedVariable.Password, R.id.signup_password_et));
        SignUpRetypePasswordEt = findViewById(R.id.signup_retype_password_et);
        SignUpRetypePasswordEt.addTextChangedListener(new MyTextWatcher(activity, FixedVariable.Password, R.id.signup_retype_password_et));

        SignUpBtn = findViewById(R.id.signup_btn);
        SignUpBtn.setOnClickListener(this);
        SignUpPhoneBtn = findViewById(R.id.signup_phone_btn);
        SignUpPhoneBtn.setOnClickListener(this);
        SignUpGoogleBtn = findViewById(R.id.signup_google_btn);
        SignUpGoogleBtn.setOnClickListener(this);

        SignUpProgressBar = findViewById(R.id.signup_progress_bar);

        WRONG_COLOR = getResources().getColor(R.color.colorAccent);
    }

    private void InitClass() {
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        myTextWatcher = new MyTextWatcher();
        myToast = new MyToastClass(activity);
        SignUpClass = new SignInOrSignUpClass(ActivitySignUp.this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_btn:
                EmailSignUpMethod();
                break;
            case R.id.signup_phone_btn:
                PhoneSignUpMethod();
                break;
            case R.id.signup_google_btn:
                GoogleSignUpMethod();
                break;
        }
    }

    private void EmailSignUpMethod() {
        if (CheckWhichFieldIsWrong())
        {
            SignUpProgressBar.setVisibility(View.VISIBLE);
            SignUpClass.EmailSignUp(EmailString,PasswordString);
        }
    }

    private void PhoneSignUpMethod() {
        intent = new Intent(activity, ActivitySendingPhoneVerification.class);
        startActivity(intent);
    }

    private void GoogleSignUpMethod()
    {
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        GoogleSignInClient signInClient= GoogleSignIn.getClient(activity,gso);
        Intent intent=signInClient.getSignInIntent();
        startActivityForResult(intent,101);
    }


    private boolean CheckWhichFieldIsWrong() {
        if (SignUpEmailEt.getCurrentTextColor() == WRONG_COLOR) {
            SignUpEmailEt.setError("Email is invalid");
            return false;
        } else if (SignUpPasswordEt.getCurrentTextColor() == WRONG_COLOR) {
            SignUpPasswordEt.setError("Password must be atleast 6 digit");
            return false;
        } else if (!SignUpPasswordEt.getText().toString().matches(SignUpRetypePasswordEt.getText().toString())) {
            SignUpPasswordEt.setError("Password hasn't match");
            return false;
        } else {
            EmailString = SignUpEmailEt.getText().toString();
            PasswordString = SignUpPasswordEt.getText().toString();
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==101 && resultCode==RESULT_OK && data!=null)
        {
            try
            {
                Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account=task.getResult(ApiException.class);
                GetDataFromSignedIn(account);
            } catch (ApiException e)
            {

            }
        }
    }
    private void GetDataFromSignedIn(GoogleSignInAccount account)
    {
        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        SignUpClass.AccountSignInWithCredential(credential);
    }
    @Override
    public void Communicator(String type,boolean result) {
        SignUpProgressBar.setVisibility(View.GONE);
        switch (type)
        {
            case "Credential":
                if (result)
                {
                    intent = new Intent(ActivitySignUp.this,ActivityProfileCompleteOne.class);
                    intent.putExtra("Credential","Credential");
                    startActivity(intent);
                }
                else
                {
                    myToast.LToast("Account already created\nOr something error occurred\nTry again later.");
                }
            case "Email":
                if (result)
                {
                    intent = new Intent(ActivitySignUp.this,ActivityProfileCompleteOne.class);
                    intent.putExtra("Email","Email");
                    startActivity(intent);
                }
                else
                {
                    myToast.LToast("Something error occurred\nTry again later.");
                }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mAuth.getCurrentUser()!=null)
        {
            mAuth.signOut();
        }
    }
}
