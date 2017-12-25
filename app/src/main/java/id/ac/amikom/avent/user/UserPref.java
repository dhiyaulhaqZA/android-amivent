package id.ac.amikom.avent.user;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import id.ac.amikom.avent.model.User;
import id.ac.amikom.avent.utility.UserPreferenceUtil;

/**
 * Created by dhiyaulhaqza on 12/3/17.
 */

public class UserPref {

    private UserPreferenceUtil mUserPreference;
    private FirebaseUser mFirebaseUser;

    public UserPref(Context context) {
        mUserPreference = UserPreferenceUtil.newInstance(context);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public User getUser() {
        return new User(mUserPreference.readNoId(), mFirebaseUser.getDisplayName(),
                mUserPreference.readOrganization(), mUserPreference.readPhoneNumber(),
                mFirebaseUser.getEmail());
    }

    public void setUser(User user) {
        mUserPreference.writeNoId(user.getNoId());
        mUserPreference.writeOrganization(user.getOrganizationName());
        mUserPreference.writePhoneNumber(user.getPhoneNumber());
    }

    public void cleanUpUserPref() {
        mUserPreference.writeNoId("");
        mUserPreference.writeOrganization("");
        mUserPreference.writePhoneNumber("");
    }
}
