package com.example.pcconf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class UploadPcPartActivity extends AppCompatActivity {

    private static final String LOG_TAG = UploadPcPartActivity.class.getName();

    Spinner brandSpinner;
    Spinner socketSpinner;
    EditText modelEditText;
    EditText infoEditText;
    Spinner typeSpinner;
    EditText priceEditText;
    private NotificationHandler handler;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new NotificationHandler(this);

        setContentView(R.layout.activity_upload_pc_part);

        brandSpinner = findViewById(R.id.brandSpinner);
        socketSpinner = findViewById(R.id.socketSpinner);
        modelEditText = findViewById(R.id.modelEditText);
        infoEditText = findViewById(R.id.infoEditText);
        typeSpinner = findViewById(R.id.typeSpinner);
        priceEditText = findViewById(R.id.priceEditText);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.brand, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.socket, android.R.layout.simple_spinner_item);
        socketSpinner.setAdapter(adapter);

        adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        typeSpinner.setAdapter(adapter);

        String pcId = getIntent().getStringExtra("PartUpdate");

        if (getIntent().getBooleanExtra("IsUpdateing", false)) {
            firestore.collection("parts")
                    .whereEqualTo(pcId, true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Log.d(LOG_TAG, "F@szé nem olvasod az adatot");
//                            if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Part part = document.toObject(Part.class);

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(UploadPcPartActivity.this, android.R.layout.simple_spinner_item);
                                adapter.add(part.getBrand());
                                brandSpinner.setAdapter(adapter);

                                modelEditText.setText(part.getModel());

                                adapter = new ArrayAdapter<>(UploadPcPartActivity.this, android.R.layout.simple_spinner_item);
                                adapter.add(part.getSocket());
                                socketSpinner.setAdapter(adapter);

                                infoEditText.setText(part.getInfo());

                                adapter = new ArrayAdapter<>(UploadPcPartActivity.this, android.R.layout.simple_spinner_item);
                                adapter.add(part.getType());
                                typeSpinner.setAdapter(adapter);

                                priceEditText.setText(part.getPrice());
                            }
                        }
                    });
        }


    }

    public void savePart(View view) {
        Intent intent = new Intent(this, PcPartListActivity.class);

        //TODO CRUD Create
        firestore.collection("parts").add(
                        new Part(
                                brandSpinner.getSelectedItem().toString(),
                                modelEditText.getText().toString(),
                                socketSpinner.getSelectedItem().toString(),
                                infoEditText.getText().toString(),
                                typeSpinner.getSelectedItem().toString(),
                                Integer.parseInt(priceEditText.getText().toString())
                        )
                ).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(LOG_TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        //TODO Notification
                        handler.send("You have created an imaginary PC part just like you created your imaginary girlfriend");
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(LOG_TAG, "Error adding document", e);
                        handler.send("U fucked up");
                    }
                });
    }


    public void cancel(View view) {
        Intent intent = new Intent(this, PcPartListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}

