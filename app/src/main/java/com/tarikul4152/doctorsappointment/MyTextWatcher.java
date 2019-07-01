package com.tarikul4152.doctorsappointment;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

public class MyTextWatcher implements TextWatcher
{
    private ValidationPatternClass PatternClass;
    private Activity activity;
    private EditText Et;
    private String WhichEditText,CheckString;
    private boolean CheckTextWatcherResult=false;
    public MyTextWatcher()
    {

    }
    public MyTextWatcher(Activity activity,String WhichEditText,int EditTextId)
    {
        this.activity=activity;
        this.WhichEditText=WhichEditText;
        Et=activity.findViewById(EditTextId);
        PatternClass=new ValidationPatternClass();
    }
    @Override
    public void afterTextChanged(Editable s) { }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        CheckString=Et.getText().toString();
        switch (WhichEditText)
        {
            case FixedVariable.Email:
                CheckEmailValidity();
                SetTextWatcherResult();
                break;
            case FixedVariable.Password:
                CheckPasswordValidity();
                SetTextWatcherResult();
                break;
        }
    }
    private void CheckEmailValidity()
    {
        CheckTextWatcherResult = PatternClass.getEmail_Pattern().matcher(CheckString).matches();
    }
    private void CheckPasswordValidity()
    {
        CheckTextWatcherResult = PatternClass.getPassword_Normmal_Pattern().matcher(CheckString).matches();
    }
    private void SetTextWatcherResult()
    {
        if (CheckTextWatcherResult)
        {
            Et.setTextColor((activity).getResources().getColor(R.color.colorPrimary));
        }
        else
        {
            Et.setTextColor((activity).getResources().getColor(R.color.colorAccent));
        }
    }
    private boolean GetTextWatcherResult()
    {
        return CheckTextWatcherResult;
    }
}
