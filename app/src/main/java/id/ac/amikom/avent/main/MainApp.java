package id.ac.amikom.avent.main;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by dhiyaulhaqza on 11/21/17.
 */

public class MainApp extends Application {
    private FirebaseUser mUser;

    public FirebaseUser getUser() {
        return mUser;
    }

    public void setUser(FirebaseUser user) {
        mUser = user;
    }
}
