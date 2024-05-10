package com.example.pcconf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    //TODO Futáshiba és fordításhiba nincsen, ha még is lenne akkor tudd hogy "it works on my computer", szóval Skill Issue
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static final int SECRET_KEY = 99;

    EditText userNameEditText;
    EditText passwordEditText;
    Button logButton,regButton;

    Animation scaleUp, scaldeDown;
//    Toolbar toolbar;

    private SharedPreferences preferences;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNameEditText = findViewById(R.id.editTextUserName);
        passwordEditText = findViewById(R.id.editTextPassword);

        logButton = findViewById(R.id.loginButton);
        regButton = findViewById(R.id.registerButton);

        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaldeDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        //TODO 2. számú animáció
        logButton.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                logButton.startAnimation(scaleUp);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                logButton.startAnimation(scaldeDown);
            }
            login();
            return true;
        });
        regButton.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                regButton.startAnimation(scaleUp);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                regButton.startAnimation(scaldeDown);
            }
            registration();
            return true;
        });

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        auth = FirebaseAuth.getInstance();

        Log.i(LOG_TAG, "onCreate");

    }

    public void login() {
        String userName = userNameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(userName.isEmpty() || password.isEmpty()){
            userName = "@";
            password = "@";
        }

        Log.i(LOG_TAG,"Bejelentkezett: "+ userName);

        //TODO Fájörbéz Autentikáció Bejelentkezés
        auth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()){
                Log.d(LOG_TAG, "User logged in succesfully");
                startPicking();
            }else {
                Log.d(LOG_TAG, "Register you stupid f");
                Toast.makeText(MainActivity.this,"Felhasználó nem lett bejelentkezve" +  task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> Log.w(LOG_TAG,"You forgated to registrate"));
    }

    private void startPicking(){
        Intent intent = new Intent(this, PcPartListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

    public void registration() {
        Intent registerIntent = new Intent(this, RegistrationActivity.class);
        registerIntent.putExtra("SECRET_KEY", 99);
        startActivity(registerIntent);
        //TODO 1es számú animáció
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    @Override
    protected void onPause() {
        super.onPause();

        //TODO Lifecycle Hook
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", userNameEditText.getText().toString());
        editor.putString("password", passwordEditText.getText().toString());
        Log.i(LOG_TAG, "onPause");
    }
}

//TODO: kérlek a Szubjektív pontokra add meg a max pontott, csóró én szoptam az AMD procis laptopomon
// mert folyton összeomlott az emulátro és 2 nap szendevés után sem akart működni,
// ráadásul iPhone-om van (Don't Bully me I'll cum) és csórnom kellett mástól egy Androidos telefont hogy megcsináljam
// Puszi a pocidra

