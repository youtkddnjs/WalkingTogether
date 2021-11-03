package com.swsoft.walkingtogether;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class createroom extends AppCompatActivity {

    Button create;
    Spinner spinner_personnel;
    Button time;
    int ahour=0;
    int aminute=0;

    TextView tv_hour;
    TextView tv_minute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createroom);

        Toolbar createRoomToolBar = findViewById(R.id.createRoomToolbar);
        setSupportActionBar(createRoomToolBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv_hour = findViewById(R.id.tv_hour);
        tv_minute = findViewById(R.id.tv_minute);
        time = findViewById(R.id.time);
        spinner_personnel = findViewById(R.id.spinner_personnel);
        create = findViewById(R.id.create);


        //인원 선택 spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(createroom.this, R.array.spinner_personner_array,R.layout.spinner_selected);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_personnel.setAdapter(adapter);

        spinner_personnel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); //인원 선택 spinner

        //시간 선택
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(createroom.this,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay == 0 ){
                            tv_hour.setText(hourOfDay+"0");
                        }else{tv_hour.setText(hourOfDay+"");}
                        if(minute == 0){
                            tv_minute.setText(minute+"0");
                        }else{tv_minute.setText(minute+"");}
                    }
                },ahour,aminute,false);
                tpd.show();
            }
        }); //시간 선택






        //방생성버튼
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),waiting.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(),roomlist.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(),roomlist.class);
        startActivity(intent);
        super.onBackPressed();
    }
}



