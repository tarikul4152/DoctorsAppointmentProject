package com.tarikul4152.doctorsappointment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivitySignUp extends AppCompatActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener, MyDialog.MyAlertDialogCommunicator
{
    StringBuilder message;

    //Field Validation Variable
    boolean CheckUploadImageValidationResult,
            CheckNameValidationResult=false,
            CheckFatherNameValidationResult=false,
            CheckMotherNameValidationResult=false,
            CheckCredentialValidationResult=false,
            CheckPasswordValidationResult=false,
            CheckRetypePasswordValidationResult=false,
            CheckGenderValidationResult=false,
            CheckBirthDateDateValidationResult=false,
            CheckBirthDateMonthValidationResult=false,
            CheckBirthDateYearValidationResult=false;
    ///Getting Field data String Variable
    String NameString,FatherNameString,MotherNameString,CredentialString, PasswordString,
            RetypePasswordString,GenderString,BirthDateDateString,BirthDateMonthString,
            BirthDateYearString,BirthDateString,AccountTypeString="Patient";

    ///Credential Type Variable
    boolean CheckPhoneSignUp=false,CheckEmailSignUp=false,CheckPasswordMatched=false;

    ///static varibale
    private static final int CAMERA_REQUEST_CODE=100;
    private static final int GALLERY_REQUEST_CODE=101;

    ///Class Instance Type Varibale
    private PermissionGroup permissionGroup;
    private PermissionHelperClass permissionHelperClass;
    private ValidationPatternClass validationPatternClass;
    private MyToastClass myToastClass;
    MyDialog myDialog;

    ///Instance type varibale
    private Intent intent;
    private Activity activity;

    ///Xml Field Reference Varibale
    CircleImageView SignUpImageUploadCiv;
    Button SignUpImageUploadBtn,SignUpBtn,SignUpChoosePatientBtn,SignUpChooseDoctorBtn;
    EditText SignUpNameEt,SignUpFatherNameEt,SignUpMotherNameEt,SignUpCredentialEt,SignUpPasswordEt,SignUpReTypePasswordEt;
    RadioGroup SignUpGenderRadioGroup;
    TextInputEditText SignUpBirthdateDateTiet,SignUpBirthdateMonthTiet,SignUpBirthdateYearTiet;
    TextView GotoSigninActivityTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_form);

        this.activity=ActivitySignUp.this;
        ViewInit();
        ClassInit();
        String name;
    }

    ///View Initialization
    private void ViewInit()
    {
        SignUpImageUploadCiv=findViewById(R.id.signup_image_upload_civ);

        SignUpImageUploadBtn=findViewById(R.id.signup_image_upload_btn);
        SignUpImageUploadBtn.setOnClickListener(this);

        SignUpNameEt=findViewById(R.id.signup_name_et);
        SignUpNameEt.addTextChangedListener(new MyTextWatcher("NameTextWatcher"));
        SignUpFatherNameEt=findViewById(R.id.signup_father_name_et);
        SignUpFatherNameEt.addTextChangedListener(new MyTextWatcher("FatherNameTextWatcher"));
        SignUpMotherNameEt=findViewById(R.id.signup_mother_name_et);
        SignUpMotherNameEt.addTextChangedListener(new MyTextWatcher("MotherNameTextWatcher"));
        SignUpCredentialEt=findViewById(R.id.signup_credential_et);
        SignUpCredentialEt.addTextChangedListener(new MyTextWatcher("CredentialTextWatcher"));
        SignUpPasswordEt=findViewById(R.id.signup_password_et);
        SignUpPasswordEt.addTextChangedListener(new MyTextWatcher("PasswordTextWatcher"));
        SignUpReTypePasswordEt=findViewById(R.id.signup_retype_password_et);
        SignUpReTypePasswordEt.addTextChangedListener(new MyTextWatcher("RetypePasswordTextWatcher"));

        SignUpGenderRadioGroup=findViewById(R.id.signup_gender_radio_group);
        SignUpGenderRadioGroup.setOnCheckedChangeListener(this);

        SignUpBirthdateDateTiet=findViewById(R.id.signup_birthdate_date_tiet);
        SignUpBirthdateDateTiet.addTextChangedListener(new MyTextWatcher("BirthDateDateTextWatcher"));
        SignUpBirthdateMonthTiet=findViewById(R.id.signup_birthdate_month_tiet);
        SignUpBirthdateMonthTiet.addTextChangedListener(new MyTextWatcher("BirthDateMonthTextWatcher"));
        SignUpBirthdateYearTiet=findViewById(R.id.signup_birthdate_year_tiet);
        SignUpBirthdateYearTiet.addTextChangedListener(new MyTextWatcher("BirthDateYearTextWatcher"));

        SignUpChoosePatientBtn=findViewById(R.id.signup_choose_patient_btn);
        SignUpChoosePatientBtn.setOnClickListener(this);
        SignUpChooseDoctorBtn=findViewById(R.id.signup_choose_doctor_btn);
        SignUpChooseDoctorBtn.setOnClickListener(this);

        SignUpBtn=findViewById(R.id.signup_btn);
        SignUpBtn.setOnClickListener(this);

        GotoSigninActivityTv=(TextView) findViewById(R.id.signup_goto_signin_activity_tv);
        GotoSigninActivityTv.setOnClickListener(this);

    }

    private void ClassInit()
    {
        permissionHelperClass=new PermissionHelperClass(activity);
        permissionGroup=new PermissionGroup();
        validationPatternClass=new ValidationPatternClass();
        myToastClass=new MyToastClass(activity);
        myDialog=new MyDialog(ActivitySignUp.this);
    }


    //Event Listener
    @Override
    public void onClick(View v)
    {
        int id=v.getId();
        switch (id)
        {
            case R.id.signup_image_upload_btn:
                SignUpUploadImageFromCameraOrGallery();
                break;
            case R.id.signup_choose_patient_btn:
                SignUpChoosePatientBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                SignUpChooseDoctorBtn.setBackgroundColor(getResources().getColor(R.color.Button_Default));
                AccountTypeString="Patient";
                break;
            case R.id.signup_choose_doctor_btn:
                SignUpChoosePatientBtn.setBackgroundColor(getResources().getColor(R.color.Button_Default));
                SignUpChooseDoctorBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                AccountTypeString="Doctor";
                break;
            case R.id.signup_btn:
                SignUpMethod();
                break;
            case R.id.signup_goto_signin_activity_tv:
                intent=new Intent(ActivitySignUp.this, ActivitySignInOrSignUp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (checkedId==R.id.signup_male_rbtn)
        {
            GenderString="Male";
        }
        else if (checkedId==R.id.signup_female_rbtn)
        {
            GenderString="Female";
        }
    }

    ///SignUp Method Starting
    private void SignUpMethod()
    {
        CheckGenderValidationMethod();
        if (SignUpPasswordEt.getText().toString().matches(SignUpReTypePasswordEt.getText().toString()))
        {
            CheckPasswordMatched=true;
        }
        else
        {
            CheckPasswordMatched=false;
        }
        if (CheckNameValidationResult&&CheckFatherNameValidationResult&&CheckMotherNameValidationResult
                &&CheckCredentialValidationResult&&CheckPasswordValidationResult&&CheckRetypePasswordValidationResult
                &&CheckPasswordMatched&&CheckBirthDateDateValidationResult&&CheckBirthDateMonthValidationResult&&CheckBirthDateYearValidationResult)
        {
            if (CheckPhoneSignUp)
            {
                PhoneSignUpMethod();
                int ButtonId[]={R.id.confrim_btn,R.id.resend_btn};
                int EdittextId[]={R.id.verification_et};
                android.app.AlertDialog dialog= myDialog.MyCustomDialog(R.layout.activity_signin_or_signup,ButtonId,EdittextId,"Title","Message");
                dialog.show();
            }

            if (CheckEmailSignUp)
            {
                EmailSignUpMethod();
                int ButtonId[]={R.id.confrim_btn,R.id.resend_btn};
                int EdittextId[]={R.id.verification_et};
                android.app.AlertDialog dialog= myDialog.MyCustomDialog(R.layout.verification,ButtonId,EdittextId,"Title","Message");
                dialog.show();
            }
        }
        else
        {
            myToastClass.LToast("Something wrong\nCheck every field with green sign");
        }
    }

    private void EmailSignUpMethod()
    {
        Map<String,String> map=new HashMap<>();
        map=GetAllData();
        myToastClass.LToast("EmailSignUpMethod Called");
    }

    private void PhoneSignUpMethod()
    {
        Map<String,String> map=new HashMap<>();
        map=GetAllData();
        myToastClass.LToast("PhoneSignUpMethod Called");
    }

    private Map GetAllData()
    {
        Map<String,String> map=new HashMap<>();
        NameString=SignUpNameEt.getText().toString();
        map.put("NameString",NameString);
        FatherNameString=SignUpFatherNameEt.getText().toString();
        map.put("FatherNameString",FatherNameString);
        MotherNameString=SignUpMotherNameEt.getText().toString();
        map.put("MotherNameString",MotherNameString);
        CredentialString=SignUpCredentialEt.getText().toString();
        map.put("CredentialString",CredentialString);
        PasswordString=PasswordEncryptMethod(SignUpPasswordEt.getText().toString());
        map.put("PasswordString",CredentialString);
        map.put("GenderString",GenderString);
        BirthDateDateString=SignUpBirthdateDateTiet.getText().toString();
        BirthDateMonthString=SignUpBirthdateMonthTiet.getText().toString();
        BirthDateYearString=SignUpBirthdateYearTiet.getText().toString();
        BirthDateString=BirthDateDateString+"-"+BirthDateMonthString+"-"+BirthDateYearString;
        map.put("BirthDateString",BirthDateString);
        map.put("AccountTypeString",AccountTypeString);
        return map;
    }

    ///SignUpUploadImageFromCameraOrGallery Method Starting here
    private void SignUpUploadImageFromCameraOrGallery()
    {
        final CharSequence[] options={"Capture Photo","Select from gallery","Cancel"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Upload Profile Picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                if (options[item]=="Capture Photo")
                {
                    if(permissionHelperClass.CheckAndRequestPermission(permissionGroup.getCameraGroup()))
                    {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                        }
                    }
                }
                else if (options[item]=="Select from gallery")
                {
                    if(permissionHelperClass.CheckAndRequestPermission(permissionGroup.getStorageGroup()))
                    {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, GALLERY_REQUEST_CODE);
                    }
                }
                else if (options[item]=="Cancel")
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    ///Permission Request Callback result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        permissionHelperClass.onRequestPermissionResult(ActivitySignUp.this,requestCode,permissions,grantResults);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CAMERA_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            SignUpImageUploadCiv.setImageBitmap(imageBitmap);
        }
        else if (requestCode==GALLERY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            Uri uri=data.getData();
            Bitmap bitmap= null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            SignUpImageUploadCiv.setImageBitmap(bitmap);
        }
    }

    @Override
    public void DialogResultSuccess(String result)
    {
        if (result=="Resend")
        {
            if (CheckPhoneSignUp)
            {
                PhoneSignUpMethod();
            }
            if (CheckEmailSignUp)
            {
                EmailSignUpMethod();
            }
        }
    }

    @Override
    public void MyCustomDialogGetData(Map<Integer, String> integerStringMap)
    {
        String code=integerStringMap.get(0);
        myToastClass.LToast(code);
    }


    class MyTextWatcher implements TextWatcher
    {
        String whichEditText;
        public MyTextWatcher(String whichEditText)
        {
            this.whichEditText=whichEditText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            switch (whichEditText)
            {
                case "NameTextWatcher":
                    CheckNameValidationMethod();
                    FieldHint("NameTextWatcher");
                    break;
                case "FatherNameTextWatcher":
                    CheckFatherNameValidationMethod();
                    FieldHint("FatherNameTextWatcher");
                    break;
                case "MotherNameTextWatcher":
                    CheckMotherValidationMethod();
                    FieldHint("MotherNameTextWatcher");
                    break;
                case "CredentialTextWatcher":
                    CheckCredentialValidationMethod();
                    FieldHint("CredentialTextWatcher");
                    break;
                case "PasswordTextWatcher":
                    CheckPasswordValidationMethod();
                    FieldHint("PasswordTextWatcher");
                    break;
                case "RetypePasswordTextWatcher":
                    CheckRetypePasswordValidationMethod();
                    FieldHint("RetypePasswordTextWatcher");
                    break;
                case "BirthDateDateTextWatcher":
                    CheckBirthDateDateValidationMethod();
                    FieldHint("BirthDateDateTextWatcher");
                    break;
                case "BirthDateMonthTextWatcher":
                    CheckBirthDateMonthValidationMethod();
                    FieldHint("BirthDateMonthTextWatcher");
                    break;
                case "BirthDateYearTextWatcher":
                    CheckBirthDateYearValidationMethod();
                    FieldHint("BirthDateYearTextWatcher");
                    break;
                    default:
                        myToastClass.LToast("Nothing matched");
                        break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    }
    private void FieldHint(String WhichField)
    {
        switch (WhichField)
        {
            case "NameTextWatcher":
                if (CheckNameValidationResult)
                {
                    SignUpNameEt.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                else
                {
                    SignUpNameEt.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                break;
            case "FatherNameTextWatcher":
                if (CheckFatherNameValidationResult)
                {
                    SignUpFatherNameEt.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                else
                {
                    SignUpFatherNameEt.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                break;
            case "MotherNameTextWatcher":
                if (CheckMotherNameValidationResult)
                {
                    SignUpMotherNameEt.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                else
                {
                    SignUpMotherNameEt.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                break;
            case "CredentialTextWatcher":
                if (CheckCredentialValidationResult)
                {
                    SignUpCredentialEt.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                else
                {
                    SignUpCredentialEt.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                break;
            case "PasswordTextWatcher":
                if (CheckPasswordValidationResult)
                {
                    SignUpPasswordEt.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                else
                {
                    SignUpPasswordEt.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                break;
            case "RetypePasswordTextWatcher":
                if (CheckRetypePasswordValidationResult)
                {
                    SignUpReTypePasswordEt.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                else
                {
                    SignUpReTypePasswordEt.setTextColor(getResources().getColor(R.color.colorAccent));
                }
                break;
            case "BirthDateDateTextWatcher":
                if (!CheckBirthDateDateValidationResult)
                {
                    SignUpBirthdateDateTiet.setError("Date must be 01 to 31");
                }
                break;
            case "BirthDateMonthTextWatcher":
                if (!CheckBirthDateMonthValidationResult)
                {
                    SignUpBirthdateMonthTiet.setError("Month must be 01 to 12");
                }
                break;
            case "BirthDateYearTextWatcher":
                if (!CheckBirthDateYearValidationResult)
                {
                    SignUpBirthdateYearTiet.setError("Year must be 1950 to current");
                }
                break;
            default:
                myToastClass.LToast("Nothing matched");
                break;
        }


        if (CheckCredentialValidationResult)
        {
            SignUpCredentialEt.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        else
        {
            SignUpCredentialEt.setTextColor(getResources().getColor(R.color.colorAccent));
        }
        if (CheckPasswordValidationResult)
        {
            SignUpPasswordEt.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        else
        {
            SignUpPasswordEt.setTextColor(getResources().getColor(R.color.colorAccent));
        }

    }
    private void CheckNameValidationMethod()
    {
        NameString=SignUpNameEt.getText().toString();
        if (validationPatternClass.getName_Pattern().matcher(NameString).matches())
        {
            CheckNameValidationResult=true;
        }
        else
        {
            CheckNameValidationResult=false;
        }
    }
    private void CheckFatherNameValidationMethod()
    {
        FatherNameString=SignUpFatherNameEt.getText().toString();
        if (validationPatternClass.getName_Pattern().matcher(FatherNameString).matches())
        {
            CheckFatherNameValidationResult=true;
        }
        else
        {
            CheckFatherNameValidationResult=false;
        }
    }
    private void CheckMotherValidationMethod()
    {
        MotherNameString=SignUpMotherNameEt.getText().toString();
        if (validationPatternClass.getName_Pattern().matcher(MotherNameString).matches())
        {
            CheckMotherNameValidationResult=true;
        }
        else
        {
            CheckMotherNameValidationResult=false;
        }
    }
    private void CheckCredentialValidationMethod()
    {
        CredentialString=SignUpCredentialEt.getText().toString();
        if (validationPatternClass.getEmail_Pattern().matcher(CredentialString).matches())
        {
            CheckEmailSignUp=true;
            CheckPhoneSignUp=false;
            CheckCredentialValidationResult=true;
        }
        else if (Patterns.PHONE.matcher(CredentialString).matches())
        {
            CheckEmailSignUp=false;
            CheckPhoneSignUp=true;
            CheckCredentialValidationResult=true;
        }
        else
        {
            CheckCredentialValidationResult=false;
        }
    }
    private void CheckPasswordValidationMethod()
    {
        PasswordString=SignUpPasswordEt.getText().toString();
        if (validationPatternClass.getPassword_Normmal_Pattern().matcher(PasswordString).matches())
        {
            CheckPasswordValidationResult=true;
        }
        else
        {
            CheckPasswordValidationResult=false;
        }
    }
    private void CheckRetypePasswordValidationMethod()
    {
        RetypePasswordString=SignUpReTypePasswordEt.getText().toString();
        if ((validationPatternClass.getPassword_Normmal_Pattern().matcher(RetypePasswordString).matches())
                && (RetypePasswordString.matches(SignUpPasswordEt.getText().toString())))
        {
            CheckRetypePasswordValidationResult=true;
        }
        else
        {
            CheckRetypePasswordValidationResult=false;
        }
    }
    private void CheckBirthDateDateValidationMethod()
    {
        BirthDateDateString=SignUpBirthdateDateTiet.getText().toString();
        if (StringToInt(BirthDateDateString)<0 || StringToInt(BirthDateDateString)>31)
        {
            CheckBirthDateDateValidationResult=false;
        }
        else
        {
            CheckBirthDateDateValidationResult=true;
        }
    }
    private void CheckBirthDateMonthValidationMethod()
    {
        BirthDateMonthString=SignUpBirthdateMonthTiet.getText().toString();
        if (StringToInt(BirthDateMonthString)<0 || StringToInt(BirthDateMonthString)>12)
        {
            CheckBirthDateMonthValidationResult=false;
        }
        else
        {
            CheckBirthDateMonthValidationResult=true;
        }
    }
    private void CheckBirthDateYearValidationMethod()
    {
        BirthDateYearString= SignUpBirthdateYearTiet.getText().toString();
        if (StringToInt(BirthDateYearString)<(Calendar.getInstance().get(Calendar.YEAR)-120) || StringToInt(BirthDateYearString)> Calendar.getInstance().get(Calendar.YEAR))
        {
            CheckBirthDateYearValidationResult=false;
        }
        else
        {
            CheckBirthDateYearValidationResult=true;
        }
    }

    private void CheckGenderValidationMethod()
    {
        if (GenderString!=null)
        {
            CheckGenderValidationResult=true;
        }
        else
        {
            CheckGenderValidationResult=false;
        }
    }

    private String PasswordEncryptMethod(String passwordString)
    {
        return "Unknown";
    }

    private boolean CheckFieldEmpty(String checkString)
    {
        if (checkString.matches("")|| checkString.length()<4)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private int StringToInt(String number)
    {
        try {
            return Integer.parseInt(number);
        } catch (Exception e)
        {
            Toast.makeText(ActivitySignUp.this,"Number format incorrect",Toast.LENGTH_SHORT).show();
        }

        return -1;
    }

}


///NameTextWatcher,FatherNameTextWatcher,MotherNameTextWatcher,CredentialTextWatcher,PasswordTextWatcher,RetypePasswordTextWatcher,BirthDateDateTextWatcher,BirthDateMonthTextWatcher,BirthDateYearTextWatcher