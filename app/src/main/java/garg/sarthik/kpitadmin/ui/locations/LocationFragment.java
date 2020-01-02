package garg.sarthik.kpitadmin.ui.locations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import garg.sarthik.kpitadmin.Adapters.LocationAdapter;
import garg.sarthik.kpitadmin.AddActivity;
import garg.sarthik.kpitadmin.Constants.POJODetails;
import garg.sarthik.kpitadmin.POJO.Location;
import garg.sarthik.kpitadmin.R;

public class LocationFragment extends Fragment {

    private LocationViewModel locationViewModel;
    private RecyclerView rvLocations;
    private FloatingActionButton fabAddLocation;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        locationViewModel =
                ViewModelProviders.of(this).get(LocationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_locations, container, false);

        rvLocations = root.findViewById(R.id.rvLocations);
        fabAddLocation = root.findViewById(R.id.fabAddLocation);

        locationViewModel.getLocations().observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(@Nullable List<Location> locations) {
                rvLocations.setLayoutManager(new LinearLayoutManager(getContext()));
                LocationAdapter locationAdapter = new LocationAdapter(locations,getContext());
                rvLocations.setAdapter(locationAdapter);
            }
        });

        fabAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddActivity.class);
                startActivityForResult(intent, POJODetails.REQUEST_CODE);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == POJODetails.REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){

                Location loc = data.getParcelableExtra(POJODetails.PARCEL_LOCATION);
                locationViewModel.addLocation(loc);
            }
        }
    }
}