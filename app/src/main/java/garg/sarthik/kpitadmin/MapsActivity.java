package garg.sarthik.kpitadmin;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import garg.sarthik.kpitadmin.Constants.POJODetails;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private Marker currentMarker = null;
    private LatLng currentLatLng = null;
    private boolean isCurrentLocation = false;

    private LocationManager locationManager;

    private Button btnSubmitLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locationManager = (LocationManager) getSystemService(getBaseContext().LOCATION_SERVICE);
        btnSubmitLocation = findViewById(R.id.btnMapSubmitLocation);

        btnSubmitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentLatLng == null) {
                    Toast.makeText(MapsActivity.this, "First select a location", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent returnIntent = new Intent();
                String latlng = "" + currentLatLng.latitude + " , " + currentLatLng.longitude;
                returnIntent.putExtra(POJODetails.INTENT_LATLNG, latlng);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, POJODetails.REQUEST_CODE_PERMISSION);
                return;
            }

            locationSuccess();

        } catch (Exception exception) {
            Log.e("Location", "attachLocation: Request Location Update Fail");
        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                plotMarker(latLng);

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getBaseContext(), "Thank you for the permission", Toast.LENGTH_SHORT).show();
            locationSuccess();

        } else {
            Toast.makeText(getBaseContext(), "App Need Permissions to work!!!", Toast.LENGTH_LONG).show();
            finish();

        }
    }

    @SuppressLint("MissingPermission")
    public void locationSuccess() {

        Log.e("TAG", "locationSuccess: ");
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000, 500, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50000, 500, this);
        Toast.makeText(this, "Requested Location ", Toast.LENGTH_SHORT).show();
    }

    private void plotMarker(LatLng latLng) {

        if (currentMarker != null)
            currentMarker.remove();

        currentLatLng = latLng;
        currentMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Parking Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12));

    }

    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(this, location.getProvider(), Toast.LENGTH_SHORT).show();
        if (!isCurrentLocation) {
            plotMarker(new LatLng(location.getLatitude(), location.getLongitude()));
            isCurrentLocation = true;
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

        /*Toast.makeText(this, "Location Enabled", Toast.LENGTH_SHORT).show();
        locationSuccess();*/
    }

    @Override
    public void onProviderDisabled(String provider) {

        //Toast.makeText(getBaseContext(), "Location is not enabled", Toast.LENGTH_SHORT).show();

    }
}
