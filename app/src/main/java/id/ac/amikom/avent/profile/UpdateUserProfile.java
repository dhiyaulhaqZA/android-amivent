package id.ac.amikom.avent.profile;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import id.ac.amikom.avent.MainApp;
import id.ac.amikom.avent.model.User;

/**
 * Created by dhiyaulhaqza on 12/3/17.
 */

public class UpdateUserProfile {

    private OnProfileUpdateListener mUpdateListener;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;
    private StorageReference mPhotoProfileStorageReference;
    private UserProfileChangeRequest.Builder mUserProfileBuilder;

    private Context mContext;
    private boolean mIsDbUpdateComplete;
    private boolean mIsProfileChangeComplete;


    public UpdateUserProfile(Context context, OnProfileUpdateListener updateListener) {
        mContext = context;
        mUpdateListener = updateListener;
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mPhotoProfileStorageReference = FirebaseStorage.getInstance().getReference();
        mUserProfileBuilder = new UserProfileChangeRequest.Builder();
    }

    public void updateUserData(User user) {
        mUpdateListener.onProfileUpdateLoadingStart();
        UserPref userPref = new UserPref(mContext);
        userPref.setUser(user);
        writeUserData(user);
        updateUserProfile(user.getName());
    }

    private void writeUserData(User user) {
        mIsDbUpdateComplete = false;
        mDatabaseReference.child("users").child(mFirebaseUser.getUid())
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mIsDbUpdateComplete = true;
                        completeListener();
                        if (!task.isSuccessful()) {
                            triggerWhenError(
                                    "Update failed, try again", task.getException());
                        }
                    }
                });
    }

    private void triggerWhenError(String errMsg, Exception exception) {
        String excLog = exception == null? "" : exception.getMessage();
        mUpdateListener.onProfileUpdateFailed(errMsg, excLog);
    }

    private void updateUserProfile(String displayName) {
        mIsProfileChangeComplete = false;
        mUserProfileBuilder.setDisplayName(displayName);
        UserProfileChangeRequest userProfile = mUserProfileBuilder.build();
        mFirebaseUser.updateProfile(userProfile)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mIsProfileChangeComplete = true;
                        completeListener();
                        ((MainApp) mContext.getApplicationContext()).setUser(mFirebaseUser);
                        if (!task.isSuccessful()) {
                            triggerWhenError(
                                    "Update failed, try again", task.getException());
                        }
                    }
                });
    }

    public void updateUserPhoto(Uri selectedImageUri) {
        mUpdateListener.onPhotoUpdateLoadingStart();
        StorageReference photoRef = mPhotoProfileStorageReference
                .child("users")
                .child("photo_profile")
                .child(mFirebaseUser.getUid() + "_profile.jpg");

        photoRef.putFile(selectedImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mUpdateListener.onPhotoUpdateLoadingStop();
                        Uri photoProfileUri = taskSnapshot.getDownloadUrl();
                        mUserProfileBuilder.setPhotoUri(photoProfileUri);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mUpdateListener.onPhotoUpdateLoadingStop();
                        triggerWhenError("Upload photo failed, try again", e);
                    }
                });
    }

    private void completeListener() {
        if (mIsDbUpdateComplete && mIsProfileChangeComplete) {
            mUpdateListener.onProfileUpdateLoadingStop();
            mUpdateListener.onProfileUpdateSuccess();
        }
    }

    public interface OnProfileUpdateListener {
        void onProfileUpdateSuccess();

        void onProfileUpdateFailed(String errMsg, String excLog);

        void onProfileUpdateLoadingStart();

        void onProfileUpdateLoadingStop();

        void onPhotoUpdateLoadingStart();

        void onPhotoUpdateLoadingStop();
    }
}
