package garg.sarthik.kpitadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import garg.sarthik.kpitadmin.Constants.Database;
import garg.sarthik.kpitadmin.Constants.POJODetails;
import garg.sarthik.kpitadmin.POJO.Location;
import garg.sarthik.kpitadmin.Statics.Functions;
import garg.sarthik.kpitadmin.Statics.Variables;

public class AddActivity extends AppCompatActivity {

    public String TAG = "testing Add";
    EditText etLocationName;
    EditText etLocationSlots;

    TextView tvLocationLatLng;

    Button btnSubmitLocation;
    ImageButton btnLocationLatLng;

    String locLatLng = "";
    String locName = "";
    int locSlots = 0;

    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final String adminId = Variables.firebaseUser.getUid();

        tvLocationLatLng = findViewById(R.id.tvAddLocationLatLng);

        etLocationName = findViewById(R.id.etAddLocationName);
        etLocationSlots = findViewById(R.id.etAddLocationSlots);

        btnLocationLatLng = findViewById(R.id.btnAddLocationLatLng);
        btnSubmitLocation = findViewById(R.id.btnAddSubmitLocation);

        btnLocationLatLng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, MapsActivity.class);
                startActivityForResult(intent, POJODetails.REQUEST_CODE);
            }
        });


        btnSubmitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locName = etLocationName.getText().toString().trim().toLowerCase();
                String temp = etLocationSlots.getText().toString().trim();

                if (locName.isEmpty() || temp.isEmpty() || locLatLng.isEmpty()) {
                    Toast.makeText(AddActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                    return;
                }

                locSlots = Integer.parseInt(temp);

                Log.e(TAG, "Setting up the connection");


                Variables.colAdmins.document(adminId).update(POJODetails.AdminLocations, FieldValue.increment(1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Variables.colAdmins.document(adminId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String locationId = Functions.getId((long) documentSnapshot.get(POJODetails.AdminLocations));
                                    final Location location = new Location(locationId,locName, locLatLng, locSlots, Variables.admin.getEmailId(), locSlots);
                                    Variables.colAdmins.document(adminId).collection(Database.COL_LOCATION).document(locationId).set(location).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("addSET-admin", "onFailure: ", e);
                                            Toast.makeText(AddActivity.this, "Error in setting the location", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            cnt++;
                                            if(cnt == 2){
                                                returnResult(location);
                                            }
                                            Toast.makeText(AddActivity.this, "Uploaded in Admin", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    Variables.colLocations.document(Variables.admin.getEmailId() + locationId).set(location).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("addSET-location", "onFailure: ", e);
                                            Toast.makeText(AddActivity.this, "Error in setting the location", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            cnt++;
                                            if(cnt == 2){
                                                returnResult(location);
                                            }
                                            Toast.makeText(AddActivity.this, "Uploaded in Location", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    ;

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("addGET", "onFailure: ", e);
                                Toast.makeText(AddActivity.this, "Error while fetching the data", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("addUPDATE", "onFailure: ", e);
                        Toast.makeText(AddActivity.this, "Error while updating the data", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    private void returnResult(Location location){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(POJODetails.PARCEL_LOCATION,location);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == POJODetails.REQUEST_CODE){
            locLatLng = data.getStringExtra(POJODetails.INTENT_LATLNG);
            tvLocationLatLng.setText(locLatLng);
        }
    }
}
