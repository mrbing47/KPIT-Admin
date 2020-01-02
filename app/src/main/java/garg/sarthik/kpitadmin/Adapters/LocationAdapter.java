package garg.sarthik.kpitadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import garg.sarthik.kpitadmin.Constants.POJODetails;
import garg.sarthik.kpitadmin.OrderDetailActivity;
import garg.sarthik.kpitadmin.POJO.Location;
import garg.sarthik.kpitadmin.R;
import garg.sarthik.kpitadmin.Statics.Functions;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private String TAG = "LocationAdapter";
    private List<Location> locations;
    private Context ctx;

    public LocationAdapter(List<Location> locations, Context ctx) {
        this.locations = locations;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.location_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Location location = locations.get(position);

        Log.e(TAG, "onBindViewHolder: " + location.getName());

        holder.tvLocationName.setText(Functions.toCapitalise(location.getName()));
        holder.tvLocationId.setText(location.getLocationId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, OrderDetailActivity.class);
                intent.putExtra(POJODetails.PARCEL_LOCATION, location);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvLocationName;
        public TextView tvLocationId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLocationId = itemView.findViewById(R.id.tvItemLocationId);
            tvLocationName = itemView.findViewById(R.id.tvItemLocationName);
        }
    }
}
