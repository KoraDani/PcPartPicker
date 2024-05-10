package com.example.pcconf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class PcPartPickerActivity extends AppCompatActivity {

    private static final String LOG_TAG = PcPartPickerActivity.class.getName();

    private ArrayList<String> cpuList = new ArrayList<>();
    private ArrayList<String> motherBoardList= new ArrayList<>();
    private ArrayList<String> ramList= new ArrayList<>();
    private ArrayList<String> gpuList= new ArrayList<>();
    private ArrayList<String> storageList= new ArrayList<>();

    private ArrayList<Part> partList= new ArrayList<>();

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    Spinner cpuSpinner;
    Spinner motherBoardSpinner;
    Spinner ramSpinner;
    Spinner gpuSpinner;
    Spinner storageSpinner;
    private NotificationHandler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pc_part_picker);

        handler = new NotificationHandler(this);


        cpuSpinner = findViewById(R.id.cpuSpinner);
        motherBoardSpinner = findViewById(R.id.motherBoardSpinner);
        ramSpinner = findViewById(R.id.ramSpinner);
        gpuSpinner = findViewById(R.id.gpuSpinner);
        storageSpinner = findViewById(R.id.storageSpinner);

        //TODO Második számú Komplex lekérdezés, nagyon komplex mert beleraktam az orderBy-t
        firestore.collection("parts").orderBy("type").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot d: queryDocumentSnapshots) {
                Part p = d.toObject(Part.class);
                Log.d(LOG_TAG, p.toString());
                partList.add(p);
            }
            for (Part p :partList) {
                switch (p.getType()) {
                    case "CPU":
                        cpuList.add(p.partToString());
                        break;
                    case "MOTHERBOARD":
                        motherBoardList.add(p.partToString());
                        break;
                    case "GPU":
                        gpuList.add(p.partToString());
                        break;
                    case "RAM":
                        ramList.add(p.partToString());
                        break;
                    case "STORAGE":
                        storageList.add(p.partToString());
                        break;
                    default:
                        break;
                }
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cpuList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cpuSpinner.setAdapter(adapter);

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, motherBoardList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            motherBoardSpinner.setAdapter(adapter);

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ramList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ramSpinner.setAdapter(adapter);

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gpuList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            gpuSpinner.setAdapter(adapter);

            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, storageList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            storageSpinner.setAdapter(adapter);

        });

    }

    //TODO save-et
    public void savePick(View view){
        Intent intent = new Intent(this, PcPartListActivity.class);

        firestore.collection("pickedPart").add(new PickedPart(
                cpuSpinner.getSelectedItem().toString(),
                motherBoardSpinner.getSelectedItem().toString(),
                ramSpinner.getSelectedItem().toString(),
                gpuSpinner.getSelectedItem().toString(),
                storageSpinner.getSelectedItem().toString()
        )).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                handler.send("You have to sell your kidney to buy it");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.send("You don't have to sell your kidney, for now");
            }
        });
    }
    public void cancel(View view) {
        Intent intent = new Intent(this, PcPartListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);
        finish();
    }


}
