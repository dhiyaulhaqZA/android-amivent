package id.ac.amikom.avent.feature.editor;

import android.net.Uri;

/**
 * Created by dhiyaulhaqza on 12/28/17.
 */

public interface ImageUploaderListener {
    void onImageUploadLoadingStart();
    void onImageUploadLoadingStop();
    void onImageUploadSuccess(Uri eventPosterUri);
    void onImageUploadFailure(String errMsg);
}
