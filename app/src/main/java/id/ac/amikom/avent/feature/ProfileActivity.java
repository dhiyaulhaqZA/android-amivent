package id.ac.amikom.avent.feature;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import id.ac.amikom.avent.BaseActivity;
import id.ac.amikom.avent.MainActivity;
import id.ac.amikom.avent.R;
import id.ac.amikom.avent.model.User;
import id.ac.amikom.avent.profile.UpdateUserProfile;
import id.ac.amikom.avent.profile.UserPref;
import id.ac.amikom.avent.utility.ImageUtil;

public class ProfileActivity extends BaseActivity implements View.OnClickListener, UpdateUserProfile.OnProfileUpdateListener {

    private static final int RC_PHOTO_PICKER = 2;
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private ImageView mImgPhoto;
    private ProgressBar mPbPhotoLoading;
    private ProgressBar mPbLoading;
    private EditText mEtNoId;
    private EditText mEtName;
    private EditText mEtOrganization;
    private EditText mEtPhoneNumber;

    private UpdateUserProfile mUpdateUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUpdateUserProfile = new UpdateUserProfile(this, this);

        setupView();
        writeDataIfExists();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                submitUserData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void writeDataIfExists() {
        UserPref userPref = new UserPref(this);
        User user = userPref.getUser();
        Uri photoUri = getMainApp().getUser().getPhotoUrl();

        if (photoUri != null) {
            ImageUtil.loadImageFromUrl(mImgPhoto, photoUri.toString());
        }

        mEtNoId.setText(user.getNoId());
        mEtName.setText(user.getName());
        mEtOrganization.setText(user.getOrganizationName());
        mEtPhoneNumber.setText(user.getPhoneNumber());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_profile_photo:
                pickImageFromGallery();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            mPbPhotoLoading.setVisibility(View.VISIBLE);

            Uri selectedImageUri = data.getData();
            if (selectedImageUri == null) return;

            mImgPhoto.setImageURI(selectedImageUri);
            mUpdateUserProfile.updateUserPhoto(selectedImageUri);
        }
    }

    private void setupView() {
        mImgPhoto = findViewById(R.id.img_profile_photo);
        mPbPhotoLoading = findViewById(R.id.pb_profile_photo_loading);
        mPbLoading = findViewById(R.id.pb_profile_loading);
        mEtNoId = findViewById(R.id.et_profile_no_id);
        mEtName = findViewById(R.id.et_profile_name);
        mEtOrganization = findViewById(R.id.et_profile_organization);
        mEtPhoneNumber = findViewById(R.id.et_profile_phone);

        mImgPhoto.setOnClickListener(this);
    }

    private User buildUserData() {
        String noId = mEtNoId.getText().toString().trim();
        String name = mEtName.getText().toString().trim();
        String organization = mEtOrganization.getText().toString().trim();
        String phoneNumber = mEtPhoneNumber.getText().toString().trim();

        return new User(noId, name, organization,
                phoneNumber, getMainApp().getUser().getEmail());
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }

    private void submitUserData() {
        mPbLoading.setVisibility(View.VISIBLE);

        User user = buildUserData();
        mUpdateUserProfile.updateUserData(user);
    }

    @Override
    public void onProfileUpdateSuccess() {
        if (mPbLoading.getVisibility() == View.VISIBLE ||
                mPbPhotoLoading.getVisibility() == View.GONE) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onProfileUpdateFailed(String errMsg, String excLog) {
        Log.e(TAG, "onProfileUpdateFailed: " + excLog);
        Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProfileUpdateLoadingStart() {
        mPbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProfileUpdateLoadingStop() {
        mPbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onPhotoUpdateLoadingStart() {
        mPbPhotoLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPhotoUpdateLoadingStop() {
        mPbPhotoLoading.setVisibility(View.GONE);
    }
}
