package com.example.pcconf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.*;

public class PcPartListActivity extends AppCompatActivity {
    private static final String LOG_TAG = PcPartListActivity.class.getName();


    private FirebaseUser user;
    private RecyclerView recyclerView;
    private ArrayList<Part> partList;
    private PartItemAdapter partItemAdapter;

    private int gridNumber = 1;
    private boolean viewRow = true;

    private FirebaseFirestore firestore;

    private CollectionReference reference;

    private NotificationHandler handler;

    private int cartItems = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pc_part_list);


        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            Log.d(LOG_TAG, "Létező felhasználó");
        }else {
            Log.d(LOG_TAG, "Nem létező felhasználó");
            finish();
        }

        firestore = FirebaseFirestore.getInstance();
        reference = firestore.collection("parts");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));

        this.partList = new ArrayList<>();
        initializeData();

        handler = new NotificationHandler(this);
    }

    private void initializeData() {
        partList.clear();
        //TODO CRUD Read
        //TODO Első számú Komplex lekérdezés, nagyon komplex mert beleraktam az orderBy-t
        reference.orderBy("type").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot d: queryDocumentSnapshots) {
                Part p = d.toObject(Part.class);
                p.setId(d.getId());
                Log.d(LOG_TAG, "Get data for database: " + p.partToString());
                this.partList.add(p);
            }
            partItemAdapter = new PartItemAdapter(this, this.partList);
            recyclerView.setAdapter(partItemAdapter);

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.part_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.log_out){
            FirebaseAuth.getInstance().signOut();
            Log.d(LOG_TAG,"log_out");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else if (item.getItemId() == R.id.pickPart){
            Intent intent = new Intent(this, PcPartPickerActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        else if (item.getItemId() == R.id.addPart) {
            Intent intent = new Intent(this, UploadPcPartActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    public void deletePart(Part p){
        Log.d(LOG_TAG, "Reference id : "+ p._getId());
        //TODO CRUD Delete
        firestore.collection("parts").document(p._getId()).delete().addOnSuccessListener(success ->{
            Log.d(LOG_TAG, "Document successfully deleted!");
        }).addOnFailureListener(fail ->{
            Log.d(LOG_TAG, "Document faild");
        });

        handler.send("Part is deleted");

        initializeData();
    }

    public void updatePart(Part p){
        //TODO CRUD Updatet ne keresd lusta voltam folytatni a megvalósítását így csak el van kezdve
        Intent intent = new Intent(this, UploadPcPartActivity.class);
        intent.putExtra("PartUpdate", p._getId());
        intent.putExtra("IsUpdateing",true);
        startActivity(intent);
    }


}