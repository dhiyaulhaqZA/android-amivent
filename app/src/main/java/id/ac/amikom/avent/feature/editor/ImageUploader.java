package id.ac.amikom.avent.feature.editor;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by dhiyaulhaqza on 12/28/17.
 */

public class ImageUploader {
    public ImageUploaderListener imageUploaderListener;

    public ImageUploader(ImageUploaderListener imageUploaderListener) {
        this.imageUploaderListener = imageUploaderListener;
    }

    public void uploadImage(Uri imgUri, String imgName) {
        imageUploaderListener.onImageUploadLoadingStart();
        StorageReference mEventPosterStorageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoRef = mEventPosterStorageReference
                .child("event_board")
                .child("poster")
                .child(imgName);

        photoRef.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageUploaderListener.onImageUploadLoadingStop();
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        imageUploaderListener.onImageUploadSuccess(downloadUrl);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        imageUploaderListener.onImageUploadLoadingStop();
                        imageUploaderListener.onImageUploadFailure(e.getMessage());
                    }
                });
    }
}
