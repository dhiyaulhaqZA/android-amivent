package id.ac.amikom.avent.feature.editor;

/**
 * Created by dhiyaulhaqza on 12/28/17.
 */

public interface EventUploaderListener {
    void onEventUploadLoadingStart();
    void onEventUploadLoadingStop();
    void onEventUploadSuccess();
    void onEventUploadFailure(String errMsg);
}
