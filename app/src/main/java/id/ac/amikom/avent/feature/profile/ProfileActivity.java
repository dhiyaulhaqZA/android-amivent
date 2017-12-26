package id.ac.amikom.avent.feature.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.ac.amikom.avent.main.BaseActivity;
import id.ac.amikom.avent.main.MainActivity;
import id.ac.amikom.avent.R;
import id.ac.amikom.avent.model.User;
import id.ac.amikom.avent.user.UpdateUserProfile;
import id.ac.amikom.avent.user.UserPref;
import id.ac.amikom.avent.utility.ImageUtil;

public class ProfileActivity extends BaseActivity implements View.OnClickListener, UpdateUserProfile.OnProfileUpdateListener {

    private static final int RC_PHOTO_PICKER = 2;
    private static final String TAG = ProfileActivity.class.getSimpleName();

    @BindView(R.id.img_profile_photo) ImageView mImgPhoto;
    @BindView(R.id.pb_profile_photo_loading) ProgressBar mPbPhotoLoading;
    @BindView(R.id.pb_profile_loading) ProgressBar mPbLoading;
    @BindView(R.id.et_profile_no_id) EditText mEtNoId;
    @BindView(R.id.et_profile_name) EditText mEtName;
    @BindView(R.id.et_profile_organization) EditText mEtOrganization;
    @BindView(R.id.et_profile_phone) EditText mEtPhoneNumber;

    private UpdateUserProfile mUpdateUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUpdateUserProfile = new UpdateUserProfile(this, this);

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

    @OnClick(R.id.img_profile_photo)
    public void onImagePhotoClick() {
        pickImageFromGallery();
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
