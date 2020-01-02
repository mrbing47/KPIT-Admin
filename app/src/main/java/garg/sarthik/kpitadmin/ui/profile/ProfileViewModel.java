package garg.sarthik.kpitadmin.ui.profile;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import garg.sarthik.kpitadmin.POJO.Admin;
import garg.sarthik.kpitadmin.POJO.User;
import garg.sarthik.kpitadmin.Statics.Variables;

public class ProfileViewModel extends ViewModel {

    private String TAG = "ProfileVM";
    private MutableLiveData<Admin> mAdmin;

    public ProfileViewModel() {
        mAdmin = new MutableLiveData<>();
        mAdmin.setValue(Variables.admin);
    }

    public LiveData<Admin> getAdmin() {
        return mAdmin;
    }

    public void EditAdminData(String mobileNo){

        final Admin admin = new Admin(Variables.firebaseUser.getDisplayName(), Variables.firebaseUser.getEmail(), mobileNo);

        Variables.colAdmins.document(Variables.firebaseUser.getUid()).set(admin).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Variables.admin = admin;
                mAdmin.setValue(admin);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ",e );
            }
        });
    }
}