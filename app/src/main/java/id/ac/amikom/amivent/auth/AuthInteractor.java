package id.ac.amikom.amivent.auth;

import android.support.annotation.NonNull;
import android.util.Patterns;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    public void registerAuthentication(String email, String password) {

        if (isEmailPasswordValid(email, password)) return;

        mAuthListener.onLoadingStart();
        mAuth.createUserWithEmailAndPassword(email, password)
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
        boolean isNotEmailMatches = !isEmpty && !Patterns.EMAIL_ADDRESS.matcher(email).matches();
        boolean isNotPasswordSizeGreaterThanFive = !isEmpty && !(password.length() > 5);

        if (isEmpty) mAuthListener.onFailed("Email or password can not be empty", "");
        if (isNotEmailMatches) mAuthListener.onFailed("Invalid email", "");
        if (isNotPasswordSizeGreaterThanFive) mAuthListener.onFailed("Password must be greater than 5", "");

        return isEmpty || isNotEmailMatches || isNotPasswordSizeGreaterThanFive;
    }

    public interface OnAuthListener {
        void onSuccess(FirebaseUser user);

        void onFailed(String errMsg, String errLog);

        void onLoadingStart();

        void onLoadingStop();
    }
}

