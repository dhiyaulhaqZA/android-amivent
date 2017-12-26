package id.ac.amikom.avent.feature.detail;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.ac.amikom.avent.R;
import id.ac.amikom.avent.feature.registevent.EventRegisterDialog;
import id.ac.amikom.avent.model.Event;
import id.ac.amikom.avent.utility.ImageUtil;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    @BindView(R.id.img_detail_poster) ImageView imgPoster;
    @BindView(R.id.tv_detail_event_title) TextView tvTitle;
    @BindView(R.id.tv_detail_event_organizer) TextView tvOrganizer;
    @BindView(R.id.tv_detail_event_description) TextView tvDescription;
    @BindView(R.id.tv_detail_event_location) TextView tvLocation;
    @BindView(R.id.tv_detail_event_location_description) TextView tvLocationDescription;
    @BindView(R.id.tv_detail_event_contact) TextView tvContactPerson;
    @BindView(R.id.tv_detail_event_date) TextView tvDate;
    @BindView(R.id.tv_detail_event_time) TextView tvTime;
    @BindView(R.id.btn_detail_register) Button btnRegister;

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setTitle("Detail Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().hasExtra("event")) {
            event = getIntent().getParcelableExtra("event");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupDetail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_bookmark:
                Toast.makeText(this, "not implemented yet", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupDetail() {
        if (event == null) return;
        ImageUtil.loadImageFromUrl(imgPoster, event.getPosterUrl());
        tvTitle.setText(event.getTitle());
        tvOrganizer.setText("by " + event.getOrganizer());
        tvDescription.setText(event.getDescription());
        tvLocation.setText(event.getLocation());
        tvLocationDescription.setText(event.getLocationDescription());
        tvContactPerson.setText(event.getContactPerson());
        tvDate.setText(event.getDate());
        tvTime.setText(event.getStartTime() + " - " + event.getEndTime());
    }

    @OnClick(R.id.btn_detail_register)
    public void btnRegisterClick() {
        DialogFragment dialogFragment = new EventRegisterDialog();
        dialogFragment.show(getFragmentManager(), "register_event");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        if (event == null) return;
        Log.d("Test", "onMapReady: " + event.getDate());
        LatLng sydney =
                new LatLng(Double.valueOf(event.getLatitude()), Double.valueOf(event.getLongitude()));
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Lokasi"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
    }
}
