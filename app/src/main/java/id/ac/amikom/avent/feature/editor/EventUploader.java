package id.ac.amikom.avent.feature.editor;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import id.ac.amikom.avent.model.Event;

/**
 * Created by dhiyaulhaqza on 12/28/17.
 */

public class EventUploader {
    private EventUploaderListener eventUploaderListener;

    public EventUploader(EventUploaderListener eventUploaderListener) {
        this.eventUploaderListener = eventUploaderListener;
    }

    public void postEvent(Event event) {
        eventUploaderListener.onEventUploadLoadingStart();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = firebaseDatabase.getReference("event_board");
        mDatabase.push()
                .setValue(event)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        eventUploaderListener.onEventUploadLoadingStop();
                        eventUploaderListener.onEventUploadSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        eventUploaderListener.onEventUploadLoadingStop();
                        eventUploaderListener.onEventUploadFailure(e.getMessage());
                    }
                });
    }
}
