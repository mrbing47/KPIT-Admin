package garg.sarthik.kpitadmin.ui.locations;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import garg.sarthik.kpitadmin.Constants.Database;
import garg.sarthik.kpitadmin.POJO.Location;
import garg.sarthik.kpitadmin.Statics.Variables;

public class LocationViewModel extends ViewModel {

    private String TAG = "LocationVM";
    private List<Location> locations;
    private MutableLiveData<List<Location>> mLocations;

    public LocationViewModel() {
        mLocations = new MutableLiveData<>();

        Log.e(TAG, "LocationViewModel: " + Variables.firebaseUser.getUid());

        Variables.colAdmins.document(Variables.firebaseUser.getUid()).collection(Database.COL_LOCATION).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                locations = queryDocumentSnapshots.toObjects(Location.class);
                mLocations.setValue(locations);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e);
            }
        });
    }

    public LiveData<List<Location>> getLocations() {
        return mLocations;
    }

    public void addLocation(Location location){
        locations.add(location);
        mLocations.setValue(locations);
    }
}