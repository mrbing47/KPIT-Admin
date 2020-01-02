package garg.sarthik.kpitadmin.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import garg.sarthik.kpitadmin.Constants.POJODetails;
import garg.sarthik.kpitadmin.LocationActivity;
import garg.sarthik.kpitadmin.OrderDetailActivity;
import garg.sarthik.kpitadmin.POJO.Order;
import garg.sarthik.kpitadmin.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    List<Order> orders;
    Context ctx;

    private String TAG = "ADAPTER";

    public OrderAdapter(Context ctx, List<Order> orders) {

        this.ctx = ctx;
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.location_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Order order = orders.get(position);


        holder.tvLocation.setText(order.getUserId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, OrderDetailActivity.class);
                intent.putExtra(POJODetails.PARCEL_ORDER, order);
                ctx.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLocation = itemView.findViewById(R.id.tvLocation);
        }
    }

}
