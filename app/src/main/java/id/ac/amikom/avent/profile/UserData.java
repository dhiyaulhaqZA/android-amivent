package id.ac.amikom.avent.profile;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import id.ac.amikom.avent.model.User;

/**
 * Created by dhiyaulhaqza on 12/3/17.
 */

public class UserData {

    private DatabaseReference mDatabaseReference;
    private FirebaseUser mFirebaseUser;
    private OnUserDataFetch mOnUserDataFetch;
    private User user;

    public UserData(OnUserDataFetch onUserDataFetch) {
        mOnUserDataFetch = onUserDataFetch;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void fetchUserData() {
        Query query = mDatabaseReference.child("users").child(mFirebaseUser.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user =  dataSnapshot.getValue(User.class);
                mOnUserDataFetch.onUserFetchSuccess(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                user = null;
                mOnUserDataFetch.onUserFetchFailed();
            }
        });
    }

    public interface OnUserDataFetch {
        void onUserFetchSuccess(User user);
        void onUserFetchFailed();
    }
}
