package id.ac.amikom.avent.feature.editor;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.ac.amikom.avent.main.BaseActivity;
import id.ac.amikom.avent.R;
import id.ac.amikom.avent.model.Event;
import id.ac.amikom.avent.model.Participant;
import id.ac.amikom.avent.picker.DatePickerFragment;
import id.ac.amikom.avent.picker.DatePickerListener;
import id.ac.amikom.avent.picker.TimePickerFragment;
import id.ac.amikom.avent.picker.TimePickerListener;

public class EventEditorActivity extends BaseActivity implements DatePickerListener,
        ImageUploaderListener, TimePickerListener, GoogleApiClient.OnConnectionFailedListener,
        EventUploaderListener   {

    private static final int RC_PHOTO_PICKER = 2;
    private static final String TAG = EventEditorActivity.class.getSimpleName();

    @BindView(R.id.pb_event_photo_loading) ProgressBar mPbPhotoLoading;
    @BindView(R.id.pb_event_loading) ProgressBar mPbLoading;
    @BindView(R.id.img_event_poster) ImageView mImgPoster;
    @BindView(R.id.et_event_title) EditText mEtTitle;
    @BindView(R.id.et_event_organizer) EditText mEtOrganizer;
    @BindView(R.id.et_event_description) EditText mEtDescription;
    @BindView(R.id.et_event_location) EditText mEtLocation;
    @BindView(R.id.et_event_location_description) EditText mEtLocationDescription;
    @BindView(R.id.et_event_cp) EditText mEtContactPerson;
    @BindView(R.id.et_event_date) EditText mEtDate;
    @BindView(R.id.et_event_start_time) EditText mEtTimeStart;
    @BindView(R.id.et_event_end_time) EditText mEtTimeEnd;
    private Uri mEventPosterUri;

    private static final int PLACE_PICKER_REQUEST = 1;
    protected GeoDataClient mGeoDataClient;
    private PlaceDetectionClient placeDetectionClient;
    private GoogleApiClient mGoogleApiClient;
    private Place place;
    private ImageUploader imageUploader;
    private EventUploader eventUploader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_editor);
        ButterKnife.bind(this);
        setTitle("Add Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeComponent();
    }

    private void initializeComponent() {
        imageUploader = new ImageUploader(this);
        eventUploader = new EventUploader(this);
        mGeoDataClient = Places.getGeoDataClient(this, null);
        placeDetectionClient = Places.getPlaceDetectionClient(this, null);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_upload:
                postNewEvent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.img_event_poster)
    public void onPosterClick() {
        pickImageFromGallery();
    }

    @OnClick(R.id.et_event_date)
    public void onDateClick() {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getFragmentManager(), "date_picker");
    }

    @OnClick(R.id.et_event_start_time)
    public void onStartTimeClick() {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getFragmentManager(), "time_start_picker");
    }

    @OnClick(R.id.et_event_end_time)
    public void onEndTimeClick() {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getFragmentManager(), "time_end_picker");
    }

    @OnClick(R.id.et_event_location)
    public void onLocationClick() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(EventEditorActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RC_PHOTO_PICKER) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri == null) return;

                mImgPoster.setImageURI(selectedImageUri);
                imageUploader.uploadImage(selectedImageUri,
                        getMainApp().getUser().getUid()
                                + System.currentTimeMillis() + ".jpg");

            } else if (requestCode == PLACE_PICKER_REQUEST) {
                place = PlacePicker.getPlace(this, data);
                mEtLocation.setText(place.getAddress());
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }

    private void postNewEvent() {
        mPbLoading.setVisibility(View.VISIBLE);
        Event event = buildEvent();
        if (event.getTitle().equals("") && event.getDate().equals("")) return;
        eventUploader.postEvent(event);
    }

    private Event buildEvent() {
        String title = mEtTitle.getText().toString().trim();
        String organization = mEtOrganizer.getText().toString().trim();
        String description = mEtDescription.getText().toString().trim();
        String location = mEtLocation.getText().toString().trim();
        String locationDescription = mEtLocationDescription.getText().toString().trim();
        String contactPerson = mEtContactPerson.getText().toString().trim();
        String date = mEtDate.getText().toString().trim();
        String timeStart = mEtTimeStart.getText().toString().trim();
        String timeEnd = mEtTimeEnd.getText().toString().trim();

        Event event = new Event();
        event.setTitle(title);
        event.setOrganizer(organization);
        event.setDescription(description);
        event.setLocation(location);
        event.setLocationDescription(locationDescription);
        event.setLatitude(String.valueOf(place.getLatLng().latitude));
        event.setLongitude(String.valueOf(place.getLatLng().longitude));
        event.setContactPerson(contactPerson);
        event.setDate(date);
        event.setDate(date);
        event.setStartTime(timeStart);
        event.setEndTime(timeEnd);


        if (mEventPosterUri != null) event.setPosterUrl(mEventPosterUri.toString());
        event.setParticipants(new ArrayList<Participant>());

        return event;
    }

    @Override
    public void onDatePicked(String date) {
        mEtDate.setText(date);
    }

    @Override
    public void onTimeSetListener(String tag, int hour, int minute) {
        if (tag.equals("time_start_picker")) {
            mEtTimeStart.setText(hour + ":" + minute);
        } else {
            mEtTimeEnd.setText(hour + ":" + minute);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed: ");
    }

    @Override
    public void onImageUploadLoadingStart() {
        mPbPhotoLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onImageUploadLoadingStop() {
        mPbPhotoLoading.setVisibility(View.GONE);
    }

    @Override
    public void onImageUploadSuccess(Uri eventPosterUri) {
        mEventPosterUri = eventPosterUri;
        Log.d(TAG, "onImageUploadSuccess ");
    }

    @Override
    public void onImageUploadFailure(String errMsg) {
        mPbPhotoLoading.setVisibility(View.GONE);
        Log.w(TAG, "onImageUploadFailure: " + errMsg);
    }

    @Override
    public void onEventUploadLoadingStart() {
        mPbLoading.setVisibility(View.VISIBLE);

    }

    @Override
    public void onEventUploadLoadingStop() {
        mPbLoading.setVisibility(View.GONE);

    }

    @Override
    public void onEventUploadSuccess() {
        Toast.makeText(EventEditorActivity.this, "Post success", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onEventUploadFailure(String errMsg) {
        Toast.makeText(EventEditorActivity.this, "Post Failed", Toast.LENGTH_SHORT).show();
        Log.w(TAG, "onEventUploadFailure: " + errMsg);
    }
}
