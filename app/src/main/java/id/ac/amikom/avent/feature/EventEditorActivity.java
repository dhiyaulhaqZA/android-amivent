package id.ac.amikom.avent.feature;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.util.zip.Inflater;

import id.ac.amikom.avent.BaseActivity;
import id.ac.amikom.avent.R;
import id.ac.amikom.avent.model.Event;
import id.ac.amikom.avent.model.Participant;
import id.ac.amikom.avent.picker.DatePickerFragment;
import id.ac.amikom.avent.picker.DatePickerListener;
import id.ac.amikom.avent.picker.TimePickerFragment;
import id.ac.amikom.avent.picker.TimePickerListener;

public class EventEditorActivity extends BaseActivity implements DatePickerListener, TimePickerListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_PHOTO_PICKER = 2;
    private static final String TAG = EventEditorActivity.class.getSimpleName();

    private ProgressBar mPbPhotoLoading;
    private ProgressBar mPbLoading;
    private ImageView mImgPoster;
    private EditText mEtTitle;
    private EditText mEtOrganizer;
    private EditText mEtDescription;
    private EditText mEtLocation;
    private EditText mEtLocationDescription;
    private EditText mEtContactPerson;
    private EditText mEtDate;
    private EditText mEtTimeStart;
    private EditText mEtTimeEnd;
    private Uri mEventPosterUri;

    private static final int PLACE_PICKER_REQUEST = 1;
    protected GeoDataClient mGeoDataClient;
    private PlaceDetectionClient placeDetectionClient;
    private GoogleApiClient mGoogleApiClient;
    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_editor);
        setTitle("Add Event");
        setupView();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
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
            case R.menu.menu_create:
                postNewEvent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupView() {
        mImgPoster = findViewById(R.id.img_event_poster);
        mEtTitle = findViewById(R.id.et_event_title);
        mEtOrganizer = findViewById(R.id.et_event_organizer);
        mEtDescription = findViewById(R.id.et_event_description);
        mEtLocation = findViewById(R.id.et_event_location);
        mEtLocationDescription = findViewById(R.id.et_event_location_description);
        mEtContactPerson = findViewById(R.id.et_event_cp);
        mEtDate = findViewById(R.id.et_event_date);
        mEtTimeStart = findViewById(R.id.et_event_start_time);
        mEtTimeEnd = findViewById(R.id.et_event_end_time);
        mPbPhotoLoading = findViewById(R.id.pb_event_photo_loading);
        mPbLoading = findViewById(R.id.pb_event_loading);

        mImgPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });
        mEtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "date_picker");
            }
        });

        mEtTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getFragmentManager(), "time_start_picker");
            }
        });

        mEtTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getFragmentManager(), "time_end_picker");
            }
        });

        mEtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(EventEditorActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


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
                uploadPosterEvent(selectedImageUri);
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

    private void uploadPosterEvent(Uri selectedImageUri) {
        mPbPhotoLoading.setVisibility(View.VISIBLE);
        StorageReference mEventPosterStorageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoRef = mEventPosterStorageReference
                .child("event_board")
                .child("poster")
                .child(getMainApp().getUser().getUid() + System.currentTimeMillis() + ".jpg");

        photoRef.putFile(selectedImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mPbPhotoLoading.setVisibility(View.GONE);
                        mEventPosterUri = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mPbPhotoLoading.setVisibility(View.GONE);
                    }
                });
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

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = firebaseDatabase.getReference("event_board");
        mDatabase.push()
                .setValue(event)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mPbLoading.setVisibility(View.GONE);
                        Toast.makeText(EventEditorActivity.this, "Post success", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mPbLoading.setVisibility(View.GONE);
                        Toast.makeText(EventEditorActivity.this, "Post Failed", Toast.LENGTH_SHORT).show();
                    }
                });

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
}
