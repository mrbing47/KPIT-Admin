package garg.sarthik.kpitadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import garg.sarthik.kpitadmin.Constants.Database;
import garg.sarthik.kpitadmin.POJO.Admin;
import garg.sarthik.kpitadmin.Statics.Variables;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etRegEmail;
    private EditText etRegName;
    private EditText etRegMobile;

    private Button btnRegUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Variables.admin = null;

        etRegMobile = findViewById(R.id.etRegMobile);
        etRegName = findViewById(R.id.etRegName);
        etRegEmail = findViewById(R.id.etRegEmail);
        btnRegUser = findViewById(R.id.btnRegUser);

        etRegEmail.setEnabled(false);
        etRegName.setEnabled(false);

        etRegName.setText(Variables.firebaseUser.getDisplayName());
        etRegEmail.setText(Variables.firebaseUser.getEmail());

        if (Variables.firebaseUser.getPhoneNumber() != null)
            etRegMobile.setText(Variables.firebaseUser.getPhoneNumber());


        btnRegUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = etRegMobile.getText().toString().trim().toLowerCase();

                if (mobile.length() != 10) {
                    Toast.makeText(RegistrationActivity.this, "Invalid Data", Toast.LENGTH_SHORT).show();
                    return;
                }

                final Admin admin = new Admin(Variables.firebaseUser.getDisplayName(), Variables.firebaseUser.getEmail(), mobile);


                Variables.colAdmins.document(Variables.firebaseUser.getUid()).set(admin).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Variables.admin = admin;
                        startActivity(new Intent(RegistrationActivity.this, AdminActivity.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrationActivity.this, "Error occurred, Please try again later", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}

