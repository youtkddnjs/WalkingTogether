package com.swsoft.walkingtogether;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.regex.Pattern;

import retrofit2.http.POST;

public class signuppage extends AppCompatActivity {

    TextInputLayout accountInputLayout;
    TextInputEditText account;
    TextInputLayout passwordInputLayout;
    EditText password;
    TextInputLayout passwordConfirmInputLayout;
    EditText passwordConfirm;
    TextInputLayout nicnameInputlayout;
    EditText nickname;
    Button signupBtn;
    Button doublecheck;
    boolean idCehck=false;

    boolean accountOK=false;
    boolean passwordOK=false;
    boolean nicknameOK=false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signuppage);

        //툴바 생성
        Toolbar signupToolbar = findViewById(R.id.signupToolbar);
        setSupportActionBar(signupToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        accountInputLayout = findViewById(R.id.accountInputLayout);
        account = findViewById(R.id.account);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        password = findViewById(R.id.password);
        passwordConfirmInputLayout = findViewById(R.id.passwordConfirmInputLayout);
        passwordConfirm = findViewById(R.id.passwordConfirm);
        nicnameInputlayout = findViewById(R.id.nicknameInputLayout);
        nickname = findViewById(R.id.createNickname);
        signupBtn = findViewById(R.id.signupBtn);
        doublecheck = findViewById(R.id.doublecheck);


        account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if(Pattern.matches("^[a-zA-Z0-9_[-]]+$",s))
                {
                    if(s.length()<11){
                        accountInputLayout.setError(null);
                        accountOK = true;
                    }else {
                        accountInputLayout.setError("10글자를 초과하였습니다.");
                        accountOK = false;
                    }
                }else{
                    if(s.length()==0){
                        accountInputLayout.setError("공백은 사용할 수 없습니다.");
                        accountOK = false;
                    }else {
                        accountInputLayout.setError("영어/숫자만 사용가능 합니다.");
                        accountOK = false;
                    }
                }

            }
        });//계정 EditText listener

        doublecheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Database 객체 생성
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                //"member" 참조 노드 객체
                DatabaseReference signupRef = firebaseDatabase.getReference("member");
                signupRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> keys = snapshot.getChildren();
                        System.out.println("입력된 ID정보 :" + account.getText().toString());
                        System.out.println("children(계정) 갯수 : "+snapshot.getChildrenCount());
                        for(DataSnapshot t : keys){
                            signupItem item = t.getValue(signupItem.class);
                            System.out.println("보유하고있는 계정 :" + item.ID);
                            if(account.getText().toString().equals(item.ID)){
                                System.out.println("중복되었습니다.");
                                accountOK = false;
                                idCehck = false;
                                break;
                            }else{
                                System.out.println("사용가능");
                                idCehck = true;
                            }
                        }//for문
                        
                        //다이얼로그
                        AlertDialog.Builder builder=new AlertDialog.Builder(signuppage.this);
                        builder.setTitle("ID 중복 체크");
                        if( idCehck){
                            builder.setMessage("사용가능 합니다.");
                        }else{
                            builder.setMessage("중복됩니다.");
                        }
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }//데이터 체인지시

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }); //signupRef.addValueEventListener

            }//onClick
        });//중복체크


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if( 7<s.length() && s.length()<16){
                    passwordInputLayout.setError("안전합니다.");
                    passwordInputLayout.setErrorTextColor(ColorStateList.valueOf(0xFF000000));
                    passwordInputLayout.setHintTextColor(ColorStateList.valueOf(0xFF000000));
                    passwordInputLayout.setErrorIconTintList(ColorStateList.valueOf(0xFF000000));
                    passwordInputLayout.setBoxStrokeErrorColor(ColorStateList.valueOf(0xFF000000));
                }else{
                    passwordInputLayout.setError("8글자 이상 16글자 미만 입력해주세요.");
                    passwordInputLayout.setErrorTextColor(ColorStateList.valueOf(0xFFFF0000));
                    passwordInputLayout.setHintTextColor(ColorStateList.valueOf(0xFFFF0000));
                    passwordInputLayout.setErrorIconTintList(ColorStateList.valueOf(0xFFFF0000));
                    passwordInputLayout.setBoxStrokeErrorColor(ColorStateList.valueOf(0xFFFF0000));
                    passwordOK = false;
                }

            }
        });//패스워드

        passwordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals(password.getText().toString())){
                    passwordConfirmInputLayout.setHint("일치합니다.");
                    passwordConfirmInputLayout.setError(null);
                    passwordOK = true;
                }else{
                    passwordConfirmInputLayout.setHint("비밀번호를 한번 더 입력하세요.");
                    passwordConfirmInputLayout.setError("일치하지 않습니다.");
                    passwordOK = false;
                }
            }
        });//패스워드확인

        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(Pattern.matches("^[a-zA-Z0-9_[가-힣][-]]+$",s))
                {
                    if(s.length()<11){
                        nicnameInputlayout.setError(null);
                        nicknameOK = true;
                    }else {
                        nicnameInputlayout.setError("10글자를 초과하였습니다.");
                        nicknameOK = false;
                    }
                }else{
                    if(s.length()==0){
                        nicnameInputlayout.setError("공백은 사용할 수 없습니다.");
                        nicknameOK = false;
                    }else {
                        if(Pattern.matches("^[ㄱ-ㅎㅏ-ㅣ]+$",s)) {
                            nicnameInputlayout.setError("자음/모음은 사용할 수 없습니다.");
                            nicknameOK = false;
                        }else{
                            nicnameInputlayout.setError("특수 문자/공백 문자는 사용할 수 없습니다.");
                            nicknameOK = false;
                        }
                    }
                }
            }
        }); //닉네임 EditText Listener

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(signuppage.this);
                builder.setTitle("회원가입");

                if(accountOK && passwordOK && nicknameOK) {

                    //Database 객체 생성
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    //"member" 참조 노드 객체
                    DatabaseReference signupRef = firebaseDatabase.getReference("member");
                    String NewID = account.getText().toString();
                    String NewPW = password.getText().toString();
                    String NewNickName = nickname.getText().toString();
                    String NewProFile = "https://firebasestorage.googleapis.com/v0/b/walkingtogether-31a69.appspot.com/o/profileImage%2Fdefault%2Fdefault.png?alt=media&token=d2951f38-63c6-46f3-8c43-529c88e3454b";

                    //정보를 Item으로 만들어서 보낼것
                    signupItem item = new signupItem(NewID,NewPW,NewNickName,NewProFile);

                    //ID를 Key으로한 노드에 정보 입력됨.
                    signupRef.child(NewID).setValue(item);

                    builder.setMessage("축하드립니다.");
                    Intent intent = new Intent(signuppage.this, login.class);
                    startActivity(intent);
                    finish();


                }else if(!accountOK){
                    builder.setMessage("ID를 확인 해주세요.");
                }else if(!passwordOK){
                    builder.setMessage("패스워드를 확인 해주세요.");
                }else if(!nicknameOK){
                    builder.setMessage("닉네임을 확인 해주세요.");
                }else{
                    builder.setMessage("정보를 입력 해주세요.");
                }
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }//onCreate

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(),login.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    } //onPotionsItemSelected


    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),login.class);
        startActivity(intent);
        super.onBackPressed();
    } //onBackPressed

}//class
