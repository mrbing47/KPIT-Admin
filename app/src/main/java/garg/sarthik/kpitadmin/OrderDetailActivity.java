package garg.sarthik.kpitadmin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import garg.sarthik.kpitadmin.Constants.POJODetails;
import garg.sarthik.kpitadmin.Constants.Status;
import garg.sarthik.kpitadmin.POJO.Order;
import garg.sarthik.kpitadmin.Statics.Functions;

public class OrderDetailActivity extends AppCompatActivity {

    private String TAG = "OrderDetails";

    private TextView tvOrderName;
    private TextView tvOrderStatus;
    private TextView tvOrderCustomer;
    private TextView tvOrderSlots;
    private TextView tvOrderTime;
    private TextView tvOrderPenalty;
    private TextView tvOrderLatLng;

    private Button btnOrderGoogle;

    private double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        tvOrderName = findViewById(R.id.tvOrderName);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvOrderCustomer = findViewById(R.id.tvOrderCustomer);
        tvOrderSlots = findViewById(R.id.tvOrderSlots);
        tvOrderTime = findViewById(R.id.tvOrderTime);
        tvOrderPenalty = findViewById(R.id.tvOrderPenalty);
        tvOrderLatLng = findViewById(R.id.tvOrderLatLng);

        btnOrderGoogle = findViewById(R.id.btnOrderGoogle);

        final Order order = getIntent().getParcelableExtra(POJODetails.PARCEL_ORDER);


        tvOrderName.setText(order.getLocation().getName().toUpperCase());
        tvOrderStatus.setText(order.getBookingStatus());
        tvOrderSlots.setText("" + order.getSlots());
        tvOrderTime.setText(Functions.convertToTime(order.getArrivalTime()));
        tvOrderPenalty.setText("₹" + order.getPenalty());
        tvOrderLatLng.setText(order.getLocation().getLatLng());


        tvOrderCustomer.setText(order.getUserId());

        String[] args = order.getLocation().getLatLng().split(" , ");
        lat = Double.parseDouble(args[0]);
        lng = Double.parseDouble(args[1]);

        btnOrderGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (Parking Location)";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(intent);
            }
        });

        switch (order.getBookingStatus()) {
            case Status.BOOKING_CONFIRM:
                tvOrderStatus.setTextColor(getResources().getColor(R.color.colorActive));
                break;
            case Status.BOOKING_CANCEL:
                tvOrderStatus.setTextColor(getResources().getColor(R.color.colorClosed));
                break;
            case Status.BOOKING_OVER:
                tvOrderStatus.setTextColor(getResources().getColor(R.color.colorAccent));
        }


    }
}

