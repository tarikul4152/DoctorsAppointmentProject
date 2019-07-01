package com.tarikul4152.doctorsappointment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Handler;

public class MyDialog
{
    int CountResend=0;
    AlertDialog dialog;
    View view;
    Button[] btn;
    EditText[] et;
    String[] informationString;
    public int communicatorCount=0;
    MyAlertDialogCommunicator myAlertDialogCommunicator;
    private Activity activity;
    public MyDialog(Activity activity)
    {
        this.activity=activity;
    }


    ///This is my simple alert dialog which works only for take decesion wheather it is right or wrong

    public AlertDialog MyAlertDialog(String title,String message,String pos,String neg)
    {
        myAlertDialogCommunicator=(MyAlertDialogCommunicator)activity;
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(pos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                myAlertDialogCommunicator.DialogResultSuccess("Yes");
            }
        });
        builder.setNegativeButton(neg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                myAlertDialogCommunicator.DialogResultSuccess("No");
            }
        });

        AlertDialog dialog=builder.create();
        this.dialog=dialog;
       return dialog;
    }

    ///This is my custom dialog.It take layout and also it can perform for maximum 5 Button onClick and EditText not limited
    ///And Sending also a information string that can identify the operation for sending and receiving data

    public AlertDialog MyCustomDialog(int layout,int[] ButtonId,int[] EditTextId,String[] information,String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        view=(activity).getLayoutInflater().inflate(layout,null,false);

        btn=new Button[ButtonId.length];
        et=new EditText[EditTextId.length];
        informationString=new String[information.length];
        for(int i=0; i<ButtonId.length; i++)
        {
            btn[i]=(view).findViewById(ButtonId[i]);
        }
        for(int i=0; i<EditTextId.length; i++)
        {
            et[i]=(view).findViewById(EditTextId[i]);
        }
        for(int i=0; i<information.length; i++)
        {
            informationString[i]=information[i];
        }
        builder.setView(view);
        if (ButtonId.length==1)
        {
            FirstButton();
        }
        else if (ButtonId.length==2)
        {
            FirstButton();
            SecondButton();
        }
        else if (ButtonId.length==3)
        {
            FirstButton();
            SecondButton();
            ThirdButton();
        }
        else if (ButtonId.length==4)
        {
            FirstButton();
            SecondButton();
            ThirdButton();
            FourthButton();
        }
        else if (ButtonId.length==5)
        {
            FirstButton();
            SecondButton();
            ThirdButton();
            FourthButton();
            FifthButton();
        }
        AlertDialog dialog=builder.create();
        this.dialog=dialog;
        return dialog;
    }

    private void FirstButton()
    {
        btn[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///Sending all the data of the edit text
                if (informationString[0]=="ConfirmBtn")
                {
                    myAlertDialogCommunicator = (MyAlertDialogCommunicator) activity;
                    Map<Integer, String> map = GetAllData();
                    myAlertDialogCommunicator.MyCustomDialogGetData(map);
                }
            }
        });
    }
    private void SecondButton()
    {
        btn[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///This button work only for take decession
                if (informationString[1]=="ResendBtn")
                {
                    CountResend++;
                    myAlertDialogCommunicator=(MyAlertDialogCommunicator)activity;
                    myAlertDialogCommunicator.DialogResultSuccess("Resend");
                    if (CountResend>3)
                    {
                        btn[1].setEnabled(false);
                    }
                    else {
                        btn[1].setEnabled(false);
                        btn[1].postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btn[1].setEnabled(true);
                            }
                        }, 60000);
                    }
                }
            }
        });
    }
    private void ThirdButton()
    {
        btn[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"ButtonClicked"+String.valueOf(3),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void FourthButton()
    {
        btn[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"ButtonClicked"+String.valueOf(4),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void FifthButton()
    {
        btn[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"ButtonClicked"+String.valueOf(5),Toast.LENGTH_LONG).show();
            }
        });
    }

    private Map<Integer,String> GetAllData()
    {
        Map<Integer,String> map=new HashMap<>();
        for(int i=0; i<et.length; i++)
        {
            ///Only takes the first parameter
            if (informationString[2]=="VerificationEt" && i==0)
            {
                map.put(i,et[i].getText().toString());
            }
        }
        return map;
    }

    interface MyAlertDialogCommunicator
    {
        public void DialogResultSuccess(String result);
        public void MyCustomDialogGetData(Map<Integer, String> integerStringMap);
    }
}
