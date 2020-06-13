package com.example.kotlinpasswordmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class password extends Activity {

    EditText passw;
    Button passButton;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);

        passw = (EditText) findViewById(R.id.passwordText);
        passButton=(Button)findViewById(R.id.pass_bt);

        // Create Share Preference for Login detials
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed = sp.edit();

        // create shared preference to check if the app is opened for the first time
        SharedPreferences fir = getSharedPreferences("Fir", MODE_PRIVATE);
        SharedPreferences.Editor firEd = fir.edit();

        //checks if app is opened for the first time.
        if (fir.getBoolean("my_first_time", true)) {
            Ed.putString("Psw", "8989");  //if yes.Then password is Login password is set to 8989
            Ed.commit();   // write the value..

            Toast.makeText(getBaseContext(), "Initial Password is 8989", Toast.LENGTH_SHORT).show();

            // record the fact that the app has been started at least once
            fir.edit().putBoolean("my_first_time", false).commit();

        }

        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passw.getText().toString().trim()
                        .equals(getSharedPreferences("Login", MODE_PRIVATE).getString("Psw", null).trim() )) {

                    // if password is correct, we login to the main screen.
                    Intent intent = new Intent(password.this, PasswordManagerActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Wrong Password", Toast.LENGTH_LONG).show();

                }

            }
        });

    }

}