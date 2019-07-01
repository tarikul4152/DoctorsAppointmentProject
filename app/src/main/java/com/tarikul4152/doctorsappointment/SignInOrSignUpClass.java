package com.tarikul4152.doctorsappointment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Credentials;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;

public class SignInOrSignUpClass
{
    private Activity activity;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider phoneAuthProvider;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks MyCallBack;

    private boolean AccountCreationResult=false,AccountSignInResult=false;

    private String EmailString;
    private String PasswordString;
    private String PhoneString;
    private String ValidationInsertCode;

    private MyCommunicator myCommunicator;

    public SignInOrSignUpClass(Activity activity)
    {
        this.activity=activity;
    }

    public void EmailSignUp(String EmailString,String PasswordString)
    {
        this.EmailString=EmailString;
        this.PasswordString=PasswordString;
        EmailSignUpCompleting();
    }
    public void EmailSignIn(String EmailString,String PasswordString)
    {
        this.EmailString=EmailString;
        this.PasswordString=PasswordString;
        EmailSignInCompleting();
    }
    public boolean IsEmailVerified()
    {
        return mAuth.getCurrentUser().isEmailVerified();
    }
    public void ResendEmailVerification()
    {
        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    myCommunicator.Communicator("ResendEmailVerification",true);
                }
                else
                {
                    myCommunicator.Communicator("ResendEmailVerification",false);
                }
            }
        });
    }
    public void PhoneSignUp(String PhoneString)
    {
        this.PhoneString=PhoneString;
        PhoneSignUpCompleting();
    }
    public void GetPhoneVarificationCode(String ValidationInsertCode)
    {
        this.ValidationInsertCode=ValidationInsertCode;
        CreateCredendial();
    }
    private void PhoneSignUpCompleting()
    {
        phoneAuthProvider=PhoneAuthProvider.getInstance();
        phoneAuthProvider.verifyPhoneNumber(PhoneString,60, TimeUnit.SECONDS,activity,MyCallBack);
        MyCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {
                AccountSignInWithCredential(phoneAuthCredential);
            }

            @Override
            public void onCodeSent(String ValidationIdentity, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                SaveValidationIdentity(ValidationIdentity);
                AccountCreationResult=false;
            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                AccountCreationResult=false;
            }
        };
    }

    private void CreateCredendial()
    {
        SharedPreferences sharedPreferences=(activity).getSharedPreferences("VerificationCode", Context.MODE_PRIVATE);
        String VerificationSavedCode=sharedPreferences.getString(FixedVariable.ValidationIdentity,"");
        if (ValidationInsertCode.length()>5  && VerificationSavedCode.length()>5)
        {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationSavedCode, ValidationInsertCode);
            AccountSignInWithCredential(credential);
        }
    }

    public void AccountSignInWithCredential(AuthCredential credentials)
    {
        mAuth.signInWithCredential(credentials).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    myCommunicator.Communicator("Credential",true);
                }
                else
                {
                    myCommunicator.Communicator("Credential",false);
                }
            }
        });
    }

    private void SaveValidationIdentity(String ValidationIdentity)
    {
        SharedPreferences sharedPreferences=activity.getSharedPreferences("VerificationCode", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(FixedVariable.ValidationIdentity,ValidationIdentity);
        editor.commit();
    }
    private void EmailSignInCompleting()
    {
        mAuth=FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(EmailString,PasswordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    myCommunicator.Communicator("EmailSignIn",true);
                }
                else
                {
                    myCommunicator.Communicator("EmailSignIn",false);
                }
            }
        });
    }
    private void EmailSignUpCompleting()
    {
        myCommunicator=(MyCommunicator)activity;
        mAuth=FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(EmailString,PasswordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    myCommunicator.Communicator("EmailSignUp",true);
                }
                else
                {
                    myCommunicator.Communicator("EmailSignUp",false);
                }
            }
        });
    }
}
