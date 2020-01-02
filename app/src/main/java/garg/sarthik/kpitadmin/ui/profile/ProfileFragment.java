package garg.sarthik.kpitadmin.ui.profile;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import garg.sarthik.kpitadmin.POJO.Admin;
import garg.sarthik.kpitadmin.R;

public class ProfileFragment extends Fragment {

    private String TAG = "ProfileFrag";
    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        final TextView tvProfileName = root.findViewById(R.id.tvProfileName);
        final TextView tvProfileEmail = root.findViewById(R.id.tvProfileEmail);
        final EditText etProfileMobile = root.findViewById(R.id.etProfileMobile);

        final FloatingActionButton fabEditProfile = root.findViewById(R.id.fabEditProfile);

        profileViewModel.getAdmin().observe(this, new Observer<Admin>() {
            @Override
            public void onChanged(Admin admin) {
                tvProfileEmail.setText(admin.getEmailId());
                etProfileMobile.setText(admin.getMobileNo());
                tvProfileName.setText(admin.getName().toUpperCase());
            }
        });

        etProfileMobile.setEnabled(false);
        final Drawable originalDrawable = etProfileMobile.getBackground();
        etProfileMobile.setBackgroundColor(Color.TRANSPARENT);

        fabEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etProfileMobile.isEnabled()) {
                    Log.e(TAG, "onClick: " + etProfileMobile.isEnabled());
                    String mobile = etProfileMobile.getText().toString().trim();

                    if (mobile.length() != 10) {
                        Toast.makeText(getContext(), "Invalid Data", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    profileViewModel.EditAdminData(mobile);

                    etProfileMobile.setEnabled(false);
                    etProfileMobile.setBackgroundColor(Color.TRANSPARENT);
                    fabEditProfile.setImageResource(R.drawable.ic_edit_white);
                    Log.e(TAG, "onClick: \nHere I'm");

                } else {

                    Log.e(TAG, "onClick: " + etProfileMobile.isEnabled());
                    etProfileMobile.setEnabled(true);
                    etProfileMobile.setBackground(originalDrawable);
                    fabEditProfile.setImageResource(R.drawable.ic_done_white);
                }
            }
        });

        return root;
    }
}