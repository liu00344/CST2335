package com.example.di.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "LoginActivity";
    private EditText txtUserName;
    private Button loginButton;
    private SharedPreferences sharedPref ;


    static   SharedPreferences.Editor editor ;
    protected void onCreate(Bundle savedInstanceState) {
        //try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);


       // loginNameET= (EditText)findViewById(R.id.editText);
       // sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        //String defaultValue = getResources().getInteger(R.string.saved_high_score_default);
       // String loginEmail = sharedPref.getString(getString(R.string.Login_email), "email@domain.com");

        //loginNameET.setText(loginEmail);

        Log.i(ACTIVITY_NAME,"In onCreate()");
       // }catch(Exception e)
        //{
          //Log.i("Crash:",   e.getMessage());
        //}

    }



    @Override
    protected void onStart() {
        super.onStart();

        Button loginButton = (Button) findViewById(R.id.button2);
        txtUserName = (EditText)findViewById(R.id.editText);

        sharedPref = getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        //sharedPref = getApplicationContext().getSharedPreferences("DefaultEmail", Context.MODE_PRIVATE);
        //final SharedPreferences.Editor editor = sharedPref.edit();
        String restoredEmail = sharedPref.getString("DefaultEmail", "email@domain.com");
        txtUserName.setText(restoredEmail);

        assert loginButton != null;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPref.edit();
                //editor.putString("DefaultEmail" , "email@domain.com");
                String temp = txtUserName.getText().toString();
                editor.putString("DefaultEmail",temp);

                editor.commit();
                Log.i(ACTIVITY_NAME, "In onStart()");

                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);//go to startActivity
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

}
