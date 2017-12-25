package id.ac.amikom.avent.feature;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import id.ac.amikom.avent.R;
import id.ac.amikom.avent.model.Event;
import id.ac.amikom.avent.utility.ImageUtil;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageView imgPoster;
    private TextView tvTitle;
    private TextView tvOrganizer;
    private TextView tvDescription;
    private TextView tvLocation;
    private TextView tvLocationDescription;
    private TextView tvContactPerson;
    private TextView tvDate;
    private TextView tvTime;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Detail Event");
        setupView();

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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void setupView() {
        imgPoster = findViewById(R.id.img_detail_poster);
        tvTitle = findViewById(R.id.tv_detail_event_title);
        tvOrganizer = findViewById(R.id.tv_detail_event_organizer);
        tvDescription = findViewById(R.id.tv_detail_event_description);
        tvLocation = findViewById(R.id.tv_detail_event_location);
        tvLocationDescription = findViewById(R.id.tv_detail_event_location_description);
        tvContactPerson = findViewById(R.id.tv_detail_event_contact);
        tvDate = findViewById(R.id.tv_detail_event_date);
        tvTime = findViewById(R.id.tv_detail_event_time);
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
