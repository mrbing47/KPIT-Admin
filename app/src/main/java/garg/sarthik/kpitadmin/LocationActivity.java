package garg.sarthik.kpitadmin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import garg.sarthik.kpitadmin.Adapters.OrderAdapter;
import garg.sarthik.kpitadmin.Constants.Database;
import garg.sarthik.kpitadmin.Constants.POJODetails;
import garg.sarthik.kpitadmin.POJO.Location;
import garg.sarthik.kpitadmin.POJO.Order;
import garg.sarthik.kpitadmin.Statics.Functions;
import garg.sarthik.kpitadmin.Statics.Variables;

public class LocationActivity extends AppCompatActivity {

    private String TAG = "LocationActivity";

    private LinearLayout llLocation;

    private TextView tvLocationName;
    private TextView tvLocationOwner;
    private TextView tvLocationAvail;
    private TextView tvLocationTotal;
    private TextView tvLocationLatLng;


    private Button btnLocationGoogle;

    private RecyclerView rvUsers;

    private double lat, lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        final Location location = getIntent().getParcelableExtra(POJODetails.PARCEL_LOCATION);

        llLocation = findViewById(R.id.llLocation);

        tvLocationName = findViewById(R.id.tvLocationName);
        tvLocationOwner = findViewById(R.id.tvLocationOwner);
        tvLocationAvail = findViewById(R.id.tvLocationAvail);
        tvLocationTotal = findViewById(R.id.tvLocationTotal);
        tvLocationLatLng = findViewById(R.id.tvLocationLatLng);
        rvUsers = findViewById(R.id.rvUsers);
        btnLocationGoogle = findViewById(R.id.btnLocationGoogle);

        tvLocationLatLng.setText(location.getLatLng());
        tvLocationName.setText(location.getName().toUpperCase());
        tvLocationAvail.setText("" + location.getAvailSlots());
        tvLocationTotal.setText("/ " + location.getTotalSlots());

        String[] args = location.getLatLng().split(" , ");
        lat = Double.parseDouble(args[0]);
        lng = Double.parseDouble(args[1]);

        tvLocationOwner.setText(Functions.toCapitalise(Variables.admin.getName()));

        btnLocationGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (Parking Location)";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            }
        });


        Variables.colLocations.document(Variables.admin.getEmailId() + location.getLocationId()).collection(Database.COL_BOOKINGS).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<Order> orders = queryDocumentSnapshots.toObjects(Order.class);
                callAdapter(orders);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
            }
        });


    }

    private void callAdapter(List<Order> orders) {

        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        OrderAdapter orderAdapter = new OrderAdapter(this, orders);
        rvUsers.setAdapter(orderAdapter);

    }


}

