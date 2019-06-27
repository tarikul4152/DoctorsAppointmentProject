package com.tarikul4152.doctorsappointment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyDialog
{
    AlertDialog dialog;
    View view;
    Button[] btn;
    EditText[] et;
    public int communicatorCount=0;
    MyAlertDialogCommunicator myAlertDialogCommunicator;
    private Activity activity;
    public MyDialog(Activity activity)
    {
        this.activity=activity;
    }


    public Dialog MyAlertDialog(String title,String message,String pos,String neg)
    {
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

    public AlertDialog MyCustomDialog(int layout,int[] ButtonId,int[] EditTextId,String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        view=(activity).getLayoutInflater().inflate(layout,null,false);

        btn=new Button[ButtonId.length];
        et=new EditText[EditTextId.length];
        for(int i=0; i<ButtonId.length; i++)
        {
            btn[i]=(view).findViewById(ButtonId[i]);
        }
        for(int i=0; i<EditTextId.length; i++)
        {
            et[i]=(view).findViewById(EditTextId[i]);
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
                myAlertDialogCommunicator=(MyAlertDialogCommunicator)activity;
                Map<Integer,String> map=GetAllData();
                myAlertDialogCommunicator.MyCustomDialogGetData(map);
                dialog.cancel();
            }
        });
    }
    private void SecondButton()
    {
        btn[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAlertDialogCommunicator=(MyAlertDialogCommunicator)activity;
                myAlertDialogCommunicator.DialogResultSuccess("Resend");
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
            map.put(i,et[i].getText().toString());
        }
        return map;
    }

    interface MyAlertDialogCommunicator
    {
        public void DialogResultSuccess(String result);
        public void MyCustomDialogGetData(Map<Integer, String> integerStringMap);
    }
}
