package com.example.pcconf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    EditText userNameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText repasswordEditText;

    private SharedPreferences preferences;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        int secretKey = getIntent().getIntExtra("SECRET_KEY", 0);

        if(secretKey != 99){
            finish();
        }

        userNameEditText = findViewById(R.id.userNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        repasswordEditText = findViewById(R.id.repasswordEditText);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String userName = preferences.getString("userName","");
        String password = preferences.getString("password","");

        userNameEditText.setText(userName);
        passwordEditText.setText(password);

        auth = FirebaseAuth.getInstance();

        Log.i(LOG_TAG, "onCreate");

    }

    public void register(View view) {
        String userName = userNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String repassword = repasswordEditText.getText().toString();

        if(userName.isEmpty() || password.isEmpty() || email.isEmpty() || repassword.isEmpty()){
            userName = "@";
            password = "@";
            email = "@";
            repassword = "@";
        }

        if (!password.equals(repassword)){
            Log.e(LOG_TAG,"Nem egyenlő a jelszó és a megerősítése");
            return;
        }

        //TODO Fájörbéz Regisztráció
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(LOG_TAG, "Felhasználó sikeresen létrehozva");
                    startPicking();
                }else {
                    Log.d(LOG_TAG, "Felhasználó nem lett létrehozva");
                    Toast.makeText(RegistrationActivity.this,"Felhasználó nem lett létrehozva" +  task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        Log.i(LOG_TAG,"Regisztrált: "+ userName+" , Email: " +email  );
    }

    private void startPicking(){
        Intent intent = new Intent(this, PcPartListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
    public void cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


}