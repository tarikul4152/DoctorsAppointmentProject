package com.tarikul4152.doctorsappointment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FragmentVerificationCheck extends Fragment implements View.OnClickListener
{
    private View view;
    private EditText VerificationEt;
    private Button CheckBtn,ResendBtn,GotoNext;
    private TextView VerificationTv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.verification,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        VerificationTv=view.findViewById(R.id.verification_tv);
        VerificationEt=view.findViewById(R.id.verification_et);
        CheckBtn=view.findViewById(R.id.check_btn);
        CheckBtn.setOnClickListener(this);
        ResendBtn=view.findViewById(R.id.resend_btn);
        ResendBtn.setOnClickListener(this);
        GotoNext=view.findViewById(R.id.gotonext_btn);
        GotoNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.check_btn:
                break;
            case R.id.resend_btn:
                break;
            case R.id.gotonext_btn:
                break;
        }
    }

    public void StatusTextViewChange(String message)
    {
        VerificationTv.setText(message);
    }
}
