package id.ac.amikom.amivent.auth;

import android.support.annotation.NonNull;
import android.util.Patterns;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * Created by dhiyaulhaqza on 11/22/17.
 */

public class AuthInteractor {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private OnAuthListener mAuthListener;

    public AuthInteractor(OnAuthListener authListener) {
        mAuthListener = authListener;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    public void loginAuthentication(String email, String password) {

        if (isEmailPasswordValid(email, password)) return;

        mAuthListener.onLoadingStart();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mAuthListener.onLoadingStop();
                        if (task.isSuccessful()) {
                            mUser = getCurrentUser();
                            mAuthListener.onSuccess(mUser);
                        } else {
                            notifyTaskFailure(task);
                        }
                    }
                });
    }

    public void registerAuthentication(final String name, String email, String password) {

        if (isEmailPasswordValid(email, password)) return;
        if (name.isEmpty()) {
            mAuthListener.onFailed("Please input your name", "");
        }

        mAuthListener.onLoadingStart();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mUser = getCurrentUser();
                            mAuthListener.onSuccess(mUser);
                        } else {
                            notifyTaskFailure(task);
                        }
                    }
                });
    }

    public FirebaseUser getCurrentUser()    {
        return mAuth.getCurrentUser();
    }

    private void notifyTaskFailure(Task<AuthResult> task) {
        String errLog = "";
        if (task.getException() != null) {
            errLog = task.getException().getMessage();
        }
        mAuthListener.onFailed("Authentication failed.", errLog);
    }

    private boolean isEmailPasswordValid(String email, String password) {
        boolean isEmpty = email.equals("") || password.equals("");
        boolean isEmailMatches = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        boolean isAmikomEmail = (email.endsWith("@students.amikom.ac.id") || email.endsWith("@amikom.ac.id"));
        boolean isEmailInvalid = !isEmpty && !(isEmailMatches && isAmikomEmail);
        boolean isNotPasswordSizeGreaterThanFive = !isEmpty && !(password.length() > 5);

        if (isEmpty) mAuthListener.onFailed("Email or password can not be empty", "");
        if (isEmailInvalid) mAuthListener.onFailed("Must use AMIKOM email", "");
        if (isNotPasswordSizeGreaterThanFive) mAuthListener.onFailed("Password must be greater than 5", "");

        return isEmpty || isEmailInvalid || isNotPasswordSizeGreaterThanFive;
    }

    public interface OnAuthListener {
        void onSuccess(FirebaseUser user);

        void onFailed(String errMsg, String errLog);

        void onLoadingStart();

        void onLoadingStop();
    }
}

